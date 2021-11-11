package com.app.services;

import com.app.model.Pemegang;
import com.app.model.Topup;
import com.app.model.request.RequestTopup;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@Service
public class TransactionSubscriptionSvcImpl implements TransactionSubscriptionSvc {

    private static final Logger logger = LogManager.getLogger(TransactionSubscriptionSvcImpl.class);

    private NumberFormat nfZeroTwo = new DecimalFormat("#,##0.00;(#,##0.00)");
    private NumberFormat nfZeroFour = new DecimalFormat("#,##0.0000;(#,##0.0000)");

    @Value("${path.storage.mpolicy}")
    private String storageMpolicy;

    @Autowired
    private VegaCustomResourceLoader customResourceLoader;
    @Autowired
    private VegaServices services;

    @Override
    public String submitDataTransaction(RequestTopup requestTopup, HttpServletRequest request) {
        Date start = new Date();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = new Gson();
        gson = builder.create();
        String req = gson.toJson(requestTopup);
        String res = null;
        String message = null;
        String resultErr = null;
        Boolean error = false;
        HashMap<String, Object> map = new HashMap<>();

        String username = requestTopup.getUsername();
        String key = requestTopup.getKey();
        String no_polis = requestTopup.getNo_polis();
        Integer language_id = requestTopup.getLanguage_id();
        try {
            if (customResourceLoader.validateCredential(username, key)) {
                // Get MPT_ID
                BigInteger mptId = services.selectGetMptId();

                // Get SPAJ
                Pemegang paramSelectSPAJ = new Pemegang();
                paramSelectSPAJ.setMspo_policy_no(no_polis);
                Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);

                String kodeCabang = services.getKodeCabang(no_polis);

                if (requestTopup.getLt_id() != null && requestTopup.getLt_id().equals(2)) {
                    // this is request top up tunggal base on lt id
                    JSONArray fundsCheck = new JSONArray(requestTopup.getFunds());
                    if (fundsCheck.length() > 0) {
                        // Check jumlah fund yang dimasukkan (jumlah fund harus 100)
                        List<Float> sumPercentageFund = new ArrayList<>();
                        float sum = 0;
                        for (int i = 0; i < fundsCheck.length(); i++) {
                            try {
                                Float percentage = fundsCheck.getJSONObject(i).getFloat("percentage");
                                sumPercentageFund.add(percentage);
                                sum += sumPercentageFund.get(i);
                            } catch (Exception e) {
                                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
                            }
                        }
                        Integer sumInt = (int) sum;
                        if (!sumInt.equals(100)) {
                            // Handle total fund tidak 100%
                            error = true;
                            message = "Top up submitted failed";
                            resultErr = "Total fund tidak 100%, tetapi yang diinput user adalah: " + sumInt;
                            logger.error(
                                    "Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
                        } else {
                            String mataUang = null;
                            if (requestTopup.getLku_id().equals("01")) {
                                mataUang = "IDR";
                            } else if (requestTopup.getLku_id().equals("02")) {
                                mataUang = "USD";
                            }

                            String payMethod = null;
                            if (requestTopup.getTransfer_type().equals(0)) {
                                payMethod = "VA";
                            } else if (requestTopup.getTransfer_type().equals(1)) {
                                payMethod = "Transfer";
                            }
                            String nameFile = String.format("%s_%s_%s_%s_%s_%s", storageMpolicy, kodeCabang, dataSPAJ.getReg_spaj(), "Bukti_Transaksi - Top Up Tunggal", mptId, ".pdf");
                            String path = String.format("%s_%s_%s_%s", storageMpolicy, kodeCabang, dataSPAJ.getReg_spaj(), "Bukti_Transaksi - Top Up Tunggal");

                            createNewFile(path, requestTopup, mptId, request, username);

                            // Insert MPOL_TRANS
                            Topup topup = getData(mptId, dataSPAJ, requestTopup, nameFile);
                            services.insertMstMpolTrans(topup);

                            // Insert MPOL_TRANS_DET
                            JSONArray funds = new JSONArray(requestTopup.getFunds());
                            for (int i = 0; i < funds.length(); i++) {
                                try {
                                    String lji_id = funds.getJSONObject(i).getString("lji_id");
                                    Float percentage = funds.getJSONObject(i).getFloat("percentage");

                                    Float percenVal = percentage / 100;
                                    BigDecimal newPercelVal = new BigDecimal(percenVal).add(BigDecimal.ZERO);
                                    BigDecimal mpt_jumlah = requestTopup.getMpt_jumlah().multiply(newPercelVal);

                                    Topup topUpDetails = new Topup();
                                    topUpDetails.setMpt_id(mptId.toString());
                                    topUpDetails.setLji_id(lji_id);
                                    topUpDetails.setMpt_persen(percentage);
                                    topUpDetails.setMpt_jumlah_det(mpt_jumlah);
                                    topUpDetails.setMpt_unit(null);
                                    services.insertMstMpolTransDet(topUpDetails);
                                } catch (Exception e) {
                                    logger.error(
                                            "Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
                                }
                            }

                            // Push Notification
                            String messagePushNotif;

                            if (language_id.equals(1)) {
                                messagePushNotif = "Nasabah Yth, Bukti Pembayaran Premi Top Up Single sebesar " + mataUang + " "
                                        + nfZeroTwo.format(requestTopup.getMpt_jumlah()) + " melalui " + payMethod
                                        + " telah diterima";
                            } else {
                                messagePushNotif = "Dear Customer, Single Top Up Premium Payment Slip of " + mataUang + " "
                                        + nfZeroTwo.format(requestTopup.getMpt_jumlah()) + " via " + payMethod
                                        + " has been received";
                            }

                            customResourceLoader.pushNotif(username, messagePushNotif, no_polis, dataSPAJ.getReg_spaj(), 5, 0);

                            error = false;
                            message = "Top up submitted successfully";
                        }

                    } else {
                        // Handle total fund tidak 100%
                        error = true;
                        message = "Top up submitted failed";
                        resultErr = "List Funds not found";
                        logger.error(
                                "Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
                    }
                } else if (requestTopup.getLt_id() != null && requestTopup.getLt_id().equals(22)) {
                    // this is request premium billing base on lt id
                    String mataUang = null;
                    if (requestTopup.getLku_id().equals("01")) {
                        mataUang = "IDR";
                    } else if (requestTopup.getLku_id().equals("02")) {
                        mataUang = "USD";
                    }

                    String payMethod = null;
                    if (requestTopup.getTransfer_type().equals(0)) {
                        payMethod = "VA";
                    } else if (requestTopup.getTransfer_type().equals(1)) {
                        payMethod = "Transfer";
                    }
                    String nameFile = String.format("%s_%s_%s_%s_%s_%s", storageMpolicy, kodeCabang, dataSPAJ.getReg_spaj(), "Bukti_Transaksi - Premium Billing", mptId, ".pdf");
                    String path = String.format("%s_%s_%s_%s", storageMpolicy, kodeCabang, dataSPAJ.getReg_spaj(), "Bukti_Transaksi - Premium Billing");

                    createNewFile(path, requestTopup, mptId, request, username);

                    // Insert MPOL_TRANS
                    Topup topup = getData(mptId, dataSPAJ, requestTopup, nameFile);
                    services.insertMstMpolTrans(topup);

                    // Push Notification
                    String messagePushNotif;

                    if (language_id.equals(1)) {
                        messagePushNotif = "Nasabah Yth, Bukti Pembayaran Premi Premium Billing sebesar " + mataUang + " "
                                + nfZeroTwo.format(requestTopup.getMpt_jumlah()) + " melalui " + payMethod
                                + " telah diterima";
                    } else {
                        messagePushNotif = "Dear Customer, Premium Billing Payment Slip of " + mataUang + " "
                                + nfZeroTwo.format(requestTopup.getMpt_jumlah()) + " via " + payMethod
                                + " has been received";
                    }

                    customResourceLoader.pushNotif(username, messagePushNotif, no_polis, dataSPAJ.getReg_spaj(), 5, 0);

                    error = false;
                    message = "Premium billing submitted successfully";
                } else {
                    error = true;
                    message = "Error not available choice transaction";
                    resultErr = message + "(Username: " + username + " & Key: " + key + ")";
                    logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
                }
            } else {
                // Handle username & key tidak cocok
                error = true;
                message = "";
                resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
            }
        } catch (Exception e) {
            // Push Notification Telegram
            customResourceLoader.pushTelegram("@mfajarsep_bot",
                    "Path: " + request.getServletPath() + " username: " + username + "," + " Error: " + e);

            error = true;
            message = ResponseMessage.ERROR_SYSTEM;
            resultErr = "bad exception " + e;
            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
        }
        map.put("error", error);
        map.put("message", message);
        res = gson.toJson(map);
        // Update activity user table LST_USER_SIMULTANEOUS
        customResourceLoader.updateActivity(username);
        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 37, new Date(), req, res, 1, resultErr, start, username);

        return res;
    }

    private Topup getData(BigInteger mptId, Pemegang dataSPAJ, RequestTopup requestTopup, String nameFile) {
        Topup topup = new Topup();
        topup.setMpt_id(mptId.toString());
        topup.setDate_created_java1(customResourceLoader.getDatetimeJava1());
        topup.setReg_spaj(dataSPAJ.getReg_spaj());
        topup.setLt_id(requestTopup.getLt_id());
        topup.setLku_id(requestTopup.getLku_id());
        topup.setMpt_jumlah(requestTopup.getMpt_jumlah());
        topup.setDate_created_java2(customResourceLoader.getDatetimeJava());
        topup.setMpt_unit(null);
        topup.setLus_id(null);
        topup.setPayor_name(requestTopup.getPayor_name());
        topup.setPayor_occupation(requestTopup.getPayor_occupation());
        topup.setPayor_income(requestTopup.getPayor_income());
        topup.setPayor_source_income(requestTopup.getPayor_source_income());
        topup.setPath_bsb(nameFile);
        topup.setUnique_code(requestTopup.getUnique_code());
        return topup;
    }

    private void createNewFile(String path, RequestTopup requestTopup, BigInteger mptId, HttpServletRequest request,
                               String username) throws IOException {
        // Upload Proof Transaction
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        byte[] imageByte = Base64.getDecoder().decode(requestTopup.getBsb());
        String directory = path + File.separator + mptId + ".pdf";
        new FileOutputStream(directory).write(imageByte);

        try {
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(directory));
            document.open();
            byte[] decoded = Base64.getDecoder().decode(requestTopup.getBsb().getBytes());
            Image image1 = Image.getInstance(decoded);

            int indentation = 0;
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - indentation) / image1.getWidth()) * 100;

            image1.scalePercent(scaler);
            document.add(image1);
            document.close();
        } catch (Exception e) {
            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
        }
    }
}
