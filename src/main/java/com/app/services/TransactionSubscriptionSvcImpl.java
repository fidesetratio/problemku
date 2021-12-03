package com.app.services;

import com.app.exception.HandleSuccessOrNot;
import com.app.model.*;
import com.app.model.request.RequestTopup;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import io.swagger.models.auth.In;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
        String res;
        String message = null;
        HashMap<String, Object> data = new HashMap<>();
        String resultErr = null;
        Boolean error;
        HashMap<String, Object> map = new HashMap<>();

        String username = requestTopup.getUsername();
        String key = requestTopup.getKey();
        String no_polis = requestTopup.getNo_polis();
        Integer language_id = requestTopup.getLanguage_id();
        try {
            if (customResourceLoader.validateCredential(username, key)) {
                // Get SPAJ
                Pemegang paramSelectSPAJ = new Pemegang();
                paramSelectSPAJ.setMspo_policy_no(no_polis);
                Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);

                String kodeCabang = services.getKodeCabang(no_polis);

                if (requestTopup.getLt_id() != null && requestTopup.getLt_id().equals(2)) {
                    BigInteger mptId = requestTopup.getMpt_id();
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

                            String payMethod = (String) getMapPaymentType(requestTopup).get("payment_type");
                            Integer lsjb_id = (Integer) getMapPaymentType(requestTopup).get("lsjb_id");

                            String nameFile = String.format("%s/%s/%s/%s/%s/%s%s", storageMpolicy, kodeCabang, dataSPAJ.getReg_spaj(), "Bukti_Transaksi", "Top_Up_Tunggal", mptId, ".pdf");
                            String path = String.format("%s/%s/%s/%s/%s", storageMpolicy, kodeCabang, dataSPAJ.getReg_spaj(), "Bukti_Transaksi", "Top_Up_Tunggal");

                            Boolean uploadFile = customResourceLoader.uploadFileToStorage(path, requestTopup.getBsb(),
                                    String.format("%s.pdf", mptId), username, request.getServletPath(), null);
                            if (uploadFile.equals(true)) {
                                // Insert MPOL_TRANS
                                Topup topup = getData(mptId, dataSPAJ, requestTopup, nameFile, lsjb_id);
                                services.insertMstMpolTrans(topup);

                                // Insert MPOL_TRANS_DET
                                JSONArray funds = new JSONArray(requestTopup.getFunds());
                                for (int i = 0; i < funds.length(); i++) {
                                    try {
                                        String lji_id = funds.getJSONObject(i).getString("lji_id");
                                        float percentage = funds.getJSONObject(i).getFloat("percentage");

                                        BigDecimal newPercentVal = new BigDecimal(percentage).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
                                        BigDecimal mpt_jumlah = requestTopup.getMpt_jumlah().multiply(newPercentVal);

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

                                DetailBillingRequest billingRequest = new DetailBillingRequest();
                                billingRequest.setMpt_id(mptId.toString());
                                billingRequest.setReg_spaj(dataSPAJ.getReg_spaj());
                                billingRequest.setPremi_ke(0);
                                billingRequest.setTahun_ke(0);
                                billingRequest.setAmount(requestTopup.getMpt_jumlah());
                                billingRequest.setFlag_bill(1);
                                services.insertMstMpolTransBill(billingRequest);

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
                                data.put("mpt_id", mptId.toString());
                            } else {
                                data = null;
                                error = true;
                                message = "Failed upload file";
                                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                                        + resultErr);
                            }
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
                    BigInteger mptId = services.selectGetMptId();

                    // this is request premium billing base on lt id
                    if (requestTopup.getBillings() != null && requestTopup.getBillings().size() > 0){
                        String mataUang = null;
                        if (requestTopup.getLku_id().equals("01")) {
                            mataUang = "IDR";
                        } else if (requestTopup.getLku_id().equals("02")) {
                            mataUang = "USD";
                        }

                        String payMethod = (String) getMapPaymentType(requestTopup).get("payment_type");
                        Integer lsjb_id = (Integer) getMapPaymentType(requestTopup).get("lsjb_id");

                        String nameFile = String.format("%s/%s/%s/%s/%s/%s%s", storageMpolicy, kodeCabang, dataSPAJ.getReg_spaj(), "Bukti_Transaksi", "Premium_Billing", mptId, ".pdf");
                        String path = String.format("%s/%s/%s/%s/%s", storageMpolicy, kodeCabang, dataSPAJ.getReg_spaj(), "Bukti_Transaksi", "Premium_Billing");
                        Boolean uploadFile = customResourceLoader.uploadFileToStorage(path, requestTopup.getBsb(),
                                String.format("%s.pdf", mptId), username, request.getServletPath(), null);

                        if (uploadFile.equals(true)) {
                            Topup topup = getData(mptId, dataSPAJ, requestTopup, nameFile, lsjb_id);
                            services.insertMstMpolTrans(topup); // Insert MPOL_TRANS
                            JSONArray billings = new JSONArray(requestTopup.getBillings());

                            for (int i = 0; i < billings.length(); i++) {
                                Integer tahun_ke = billings.getJSONObject(i).getInt("tahun_ke");
                                Integer premi_ke = billings.getJSONObject(i).getInt("premi_ke");
                                BigDecimal amount = billings.getJSONObject(i).getBigDecimal("amount");
                                Integer flag_bill = billings.getJSONObject(i).getInt("flag_bill");
                                DetailBillingRequest billingRequest = new DetailBillingRequest();
                                billingRequest.setMpt_id(topup.getMpt_id());
                                billingRequest.setReg_spaj(dataSPAJ.getReg_spaj());
                                billingRequest.setPremi_ke(premi_ke);
                                billingRequest.setTahun_ke(tahun_ke);
                                billingRequest.setAmount(amount);
                                billingRequest.setFlag_bill(flag_bill);
                                services.insertMstMpolTransBill(billingRequest);
                            }

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
                            data.put("mpt_id", mptId.toString());
                        } else {
                            data = null;
                            error = true;
                            message = "Failed upload file";
                            resultErr = "File PDF Corrupt, MPT_ID: " + mptId + ", Name File: " +  String.format("%s.pdf", mptId);
                            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                                    + resultErr);
                        }
                    } else {
                        error = true;
                        message = "Premium billing fail submit, detail billing not found";
                    }
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
        map.put("data", data);
        map.put("message", message);
        res = gson.toJson(map);
        // Update activity user table LST_USER_SIMULTANEOUS
        customResourceLoader.updateActivity(username);
        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 37, new Date(), req, res, 1, resultErr, start, username);

        return res;
    }

    @Override
    public String saveBuktiPembayaran(SummaryPayment requestBody, HttpServletRequest request) {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = new Gson();
        gson = builder.create();
        String res;
        HandleSuccessOrNot handleSuccessOrNot=null;
        String resultErr;
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();

        try {
            if (customResourceLoader.validateCredential(requestBody.getUsername(), requestBody.getKey())) {
                //idpayment if payment online is mspa_trx else if va and others is mpt_id
                SummaryPayment mpolTrans = services.getByMptId(requestBody.getId_payment());
                SummaryPayment paymentAggregator = services.getMspaTrxId(requestBody.getId_payment());
                if (mpolTrans != null) {
                    String path = String.format("%s/%s/%s/%s", storageMpolicy, mpolTrans.getSpaj_etc(), "Bukti_Transaksi", "Summary_Detail");
                    Boolean uploadFile = customResourceLoader.uploadFileToStorage(path, requestBody.getPath(),
                            String.format("%s.pdf", mpolTrans.getMpt_id()), requestBody.getUsername(), request.getServletPath(), null);
                    if (uploadFile.equals(true)) {
                        String nameFile = String.format("%s/%s/%s/%s/%s%s", storageMpolicy, mpolTrans.getSpaj_etc(), "Bukti_Transaksi", "Summary_Detail", mpolTrans.getMpt_id(), ".pdf");
                        services.updatePathSummaryDetail(mpolTrans.getMpt_id(), nameFile);
                        handleSuccessOrNot = new HandleSuccessOrNot(false, "Success Upload File");
                    } else {
                        handleSuccessOrNot = new HandleSuccessOrNot(true, "Failed upload file");
                        resultErr = "File PDF Corrupt, MPT_ID: " + mpolTrans.getMpt_id() + ", Name File: " + String.format("%s.pdf", mpolTrans.getMpt_id());
                        logger.error("Path: " + request.getServletPath() + " Username: " + requestBody.getUsername() + " Error: "
                                + resultErr);
                    }
                } else if (paymentAggregator != null) {
                    String path = String.format("%s/%s/%s/%s", storageMpolicy, paymentAggregator.getSpaj_etc(), "Bukti_Transaksi", "Summary_Detail");
                    Boolean uploadFile = customResourceLoader.uploadFileToStorage(path, requestBody.getPath(),
                            String.format("%s.pdf", paymentAggregator.getObjc_id()), requestBody.getUsername(), request.getServletPath(), null);
                    if (uploadFile.equals(true)) {
                        String nameFile = String.format("%s/%s/%s/%s/%s%s", storageMpolicy, paymentAggregator.getSpaj_etc(), "Bukti_Transaksi", "Summary_Detail", paymentAggregator.getObjc_id(), ".pdf");
                        services.updatePathSummaryDetail(paymentAggregator.getObjc_id(), nameFile);
                        handleSuccessOrNot = new HandleSuccessOrNot(false, "Success Upload File");
                    } else {
                        handleSuccessOrNot = new HandleSuccessOrNot(true, "Failed upload file");
                        resultErr = "File PDF Corrupt, MPT_ID: " + paymentAggregator.getObjc_id() + ", Name File: " + String.format("%s.pdf", paymentAggregator.getObjc_id());
                        logger.error("Path: " + request.getServletPath() + " Username: " + requestBody.getUsername() + " Error: "
                                + resultErr);
                    }
                }
            } else {
                // Handle username & key tidak cocok
                resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + requestBody.getUsername() + " & Key: " + requestBody.getKey() + ")";
                handleSuccessOrNot = new HandleSuccessOrNot(true, resultErr);
                logger.error("Path: " + request.getServletPath() + " Username: " +  requestBody.getUsername() + " Error: " + resultErr);
            }
        } catch (Exception e){
            resultErr = e.getMessage();
            handleSuccessOrNot = new HandleSuccessOrNot(true, ResponseMessage.ERROR_SYSTEM);
            logger.error("Path: " + request.getServletPath() + " Username: " + requestBody.getUsername() + " Error: " + resultErr);
        }

        map.put("error", handleSuccessOrNot != null && handleSuccessOrNot.isError());
        map.put("data", data);
        map.put("message", handleSuccessOrNot != null ? handleSuccessOrNot.getMessage() : null);
        res = gson.toJson(map);
        return res;
    }

    @Override
    public String downloadAttachmentHistory(DownloadAttachment requestBody, HttpServletRequest request, HttpServletResponse response) {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = new Gson();
        gson = builder.create();
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        HandleSuccessOrNot handleSuccessOrNot;
        String res;
        try {
            if (customResourceLoader.validateCredential(requestBody.getUsername(), requestBody.getKey())) {
                // Path file yang mau di download
                File file = new File(requestBody.getPath());
                try {
                    // Content-Disposition
                    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

                    // Content-Length
                    response.setContentLength((int) file.length());

                    BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
                    BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                    outStream.flush();
                    inStream.close();

                    handleSuccessOrNot = new HandleSuccessOrNot(false, "Success Download");
                } catch (Exception e) {
                    handleSuccessOrNot = new HandleSuccessOrNot(true, "Download Failed file not found");
                    logger.error("Path: " + request.getServletPath() + " Username: " + requestBody.getUsername() + " Error: " + e);
                }
            } else {
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Download Failed file not found");
                logger.error("Path: " + request.getServletPath() + " Username: " + requestBody.getUsername() + " Error: " + "Download Failed file not found");
            }
        } catch (Exception e) {
            handleSuccessOrNot = new HandleSuccessOrNot(true, ResponseMessage.ERROR_SYSTEM);
            String resultErr = "bad exception " + e;
            logger.error("Path: " + request.getServletPath() + " Username: " + requestBody.getUsername() + " Error: " + resultErr);
        }
        map.put("error", handleSuccessOrNot.isError());
        map.put("data", data);
        map.put("message", handleSuccessOrNot.getMessage());
        res = gson.toJson(map);
        return res;
    }

    private Topup getData(BigInteger mptId, Pemegang dataSPAJ, RequestTopup requestTopup, String nameFile, Integer lsjb_id) {
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
        topup.setLsjb_id(lsjb_id);
        topup.setFlag_source(93);
        return topup;
    }

    private HashMap<String, Object> getMapPaymentType(RequestTopup requestTopup){
        HashMap<String, Object> map = new HashMap<>();
        if (requestTopup.getTransfer_type() != null){
            String payMethod = null;
            Integer lsjb_id = null;
            if (requestTopup.getTransfer_type().equals("Bank Transfer")) {
                payMethod = "Bank Transfer";
                lsjb_id = 5;
            } else if (requestTopup.getTransfer_type().equals("VA")) {
                payMethod = "VA";
                lsjb_id = 32;
            }

            map.put("payment_type", payMethod);
            map.put("lsjb_id", lsjb_id);
        }
        return map;
    }
}
