package com.app.services;

import com.app.exception.HandleSuccessOrNot;
import com.app.feignclient.ServiceOTP;
import com.app.model.*;
import com.app.model.request.RequestSendOTP;
import com.app.utils.DateUtils;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DPLKAccountSvcImpl implements DPLKAccountSvc {

    private static final Logger logger = LogManager.getLogger(DPLKAccountSvcImpl.class);

    private final DateUtils dateUtils;
    private final ServiceOTP serviceOTP;
    private final VegaServices services;
    private final VegaCustomResourceLoader customResourceLoader;

    @Autowired
    public DPLKAccountSvcImpl(DateUtils dateUtils, ServiceOTP serviceOTP,
                              VegaServices services,
                              VegaCustomResourceLoader customResourceLoader) {
        this.dateUtils = dateUtils;
        this.serviceOTP = serviceOTP;
        this.services = services;
        this.customResourceLoader = customResourceLoader;
    }


    @Override
    public ResponseData findAccountDPLK(DPLKAccountModel requestBody, HttpServletRequest request) {
        HandleSuccessOrNot handleSuccessOrNot;
        HashMap<String, Object> data = new HashMap<>();
        try {
            DPLKAccountModel account = services.findAccountDplk(requestBody.getAcc_no(), requestBody.getDob());
            if (account != null) {

                RequestSendOTP requestSendOTP = new RequestSendOTP();
                requestSendOTP.setJenis_id(91);
                requestSendOTP.setMenu_id(1);
                requestSendOTP.setUsername(account.getNo_hp());
                ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);

                Boolean errorPost = responseSendOTP.getError();
                String messagePost = responseSendOTP.getMessage();
                data.put("account_no", account.getAcc_no());
                data.put("dob", dateUtils.getFormatterFormat(account.getMspe_date_birth(), DateUtils.FORMAT_DAY_MONTH_YEAR, "GMT+7"));
                data.put("no_hp", account.getNo_hp());
                if (!errorPost) {
                    handleSuccessOrNot = new HandleSuccessOrNot(false, "Success find account");
                } else {
                    handleSuccessOrNot = new HandleSuccessOrNot(true,  "Phone number is blacklisted");
                    logger.error("Path: " + request.getServletPath() + " No Account DPLK: "
                            + account.getAcc_no() + " Error: " + messagePost);
                }
            } else {
                handleSuccessOrNot = new HandleSuccessOrNot(true, "account not found");
            }
        } catch (Exception e) {
            handleSuccessOrNot = new HandleSuccessOrNot(true, ResponseMessage.ERROR_SYSTEM);
            logger.error(String.format("%s: %s, %s: %s", "Path", request.getServletPath(), "error find account", e));
        }

        ResponseData responseData = new ResponseData();
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);

        return responseData;
    }

    @Override
    public ResponseData registerAccountDplk(DPLKAccountModel requestBody, HttpServletRequest request) {
        HandleSuccessOrNot handleSuccessOrNot;
        HashMap<String, Object> data = new HashMap<>();
        try {
            boolean isExistUsername = services.isExistUsername(requestBody.getUsername());
            boolean isExistAccount = services.isExistAccount(requestBody.getAcc_no());
            if (isExistUsername || isExistAccount) {
                handleSuccessOrNot = new HandleSuccessOrNot(true, "username or account is exists");
            } else {
                LstUserSimultaneous lstUserSimultaneous = new LstUserSimultaneous();
                Date date = new Date();
                String strDateFormat = "dd/MM/yyyy HH:mm:ss";
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                String formattedDate = dateFormat.format(date);
                lstUserSimultaneous.setUSERNAME(requestBody.getUsername());
                lstUserSimultaneous.setPASSWORD(requestBody.getPassword());
                lstUserSimultaneous.setFLAG_ACTIVE(1);
                lstUserSimultaneous.setCREATE_DATE(new Date());
                lstUserSimultaneous.setID_SIMULTAN(null);
                lstUserSimultaneous.setREG_SPAJ(null);
                lstUserSimultaneous.setDATE_CREATED_JAVA(formattedDate);
                lstUserSimultaneous.setMCL_ID_EMPLOYEE(null);
                lstUserSimultaneous.setAccount_no_dplk(requestBody.getAcc_no());
                services.insertNewuser(lstUserSimultaneous);

                handleSuccessOrNot = new HandleSuccessOrNot(false, "successfully register account dplk");
            }
        } catch (Exception e) {
            handleSuccessOrNot = new HandleSuccessOrNot(true, ResponseMessage.ERROR_SYSTEM);
            logger.error(String.format("%s: %s, %s: %s", "Path", request.getServletPath(), "error register account", e));
        }
        ResponseData responseData = new ResponseData();
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);
        return responseData;
    }

    @Override
    public ResponseData getGeneralInfoDplk(DPLKAccountModel requestBody, HttpServletRequest request) {
        HandleSuccessOrNot handleSuccessOrNot;
        HashMap<String, Object> data = new HashMap<>();
        try {
            if (customResourceLoader.validateCredential(requestBody.getUsername(), requestBody.getKey())) {
                LstUserSimultaneous userSimultaneous = services.selectDataLstUserSimultaneous(requestBody.getUsername());
                DPLKAccountModel dplkByAccNo = services.getInfoDplkByAccNo(userSimultaneous.getAccount_no_dplk() != null ? userSimultaneous.getAccount_no_dplk() : null);
                if (dplkByAccNo != null){
                    data.put("jenis_dplk", dplkByAccNo.getJenis_dplk());
                    data.put("company_name", dplkByAccNo.getNama_perusahaan());
                    data.put("no_peserta", dplkByAccNo.getNo_peserta());
                    data.put("join_dplk_date", dateUtils.getFormatterFormat(dplkByAccNo.getTgl_mulai_dplk(), DateUtils.FORMAT_DAY_MONTH_YEAR, "GMT+7"));
                    data.put("payment_method", dplkByAccNo.getPeriode_pembayaran());
                    data.put("lji_invest", dplkByAccNo.getLji_invest());
                    data.put("usia_pensiun_normal", dplkByAccNo.getUsia_pensiun());
                    data.put("participant_name", dplkByAccNo.getNama_peserta());
                    data.put("alamat", dplkByAccNo.getAlamat());
                    data.put("no_hp", dplkByAccNo.getNo_hp());
                    data.put("no_id", dplkByAccNo.getNo_id());
                    handleSuccessOrNot = new HandleSuccessOrNot(false, "Successfully get general info dplk");
                } else {
                    String resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + requestBody.getUsername() + " & Key: " + requestBody.getKey() + ")";
                    handleSuccessOrNot = new HandleSuccessOrNot(true, resultErr);
                    logger.error("Path: " + request.getServletPath() + " Username: " + requestBody.getUsername() + " Error: " + resultErr);
                }
            } else {
                String resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + requestBody.getUsername() + " & Key: " + requestBody.getKey() + ")";
                handleSuccessOrNot = new HandleSuccessOrNot(true, resultErr);
                logger.error("Path: " + request.getServletPath() + " Username: " + requestBody.getUsername() + " Error: " + resultErr);
            }
        } catch (Exception e){
            handleSuccessOrNot = new HandleSuccessOrNot(true, ResponseMessage.ERROR_SYSTEM);
            logger.error(String.format("%s: %s, %s: %s", "Path", request.getServletPath(), "error get info account", e));
        }
        ResponseData responseData = new ResponseData();
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);
        return responseData;
    }

    @Override
    public ResponseData getJenisFundDplk(HttpServletRequest request) {
        HandleSuccessOrNot handleSuccessOrNot;
        ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();
        HashMap<String, Object> data = new HashMap<>();
        try {
            List<JenisInvestDplk> jenisInvestDplks = services.getJenisInvestDplk();
            if (jenisInvestDplks != null && jenisInvestDplks.size() > 0){
                for (JenisInvestDplk jenisInvestDplk : jenisInvestDplks){
                    HashMap<String, Object> obj = new HashMap<>();
                    obj.put("lji_id", jenisInvestDplk.getLji_id());
                    obj.put("lji_invest", jenisInvestDplk.getLji_invest());
                    obj.put("lku_id", jenisInvestDplk.getLku_id());
                    obj.put("status", "Active");
                    dataList.add(obj);
                }
                data.put("jenis_invest", dataList);
                handleSuccessOrNot = new HandleSuccessOrNot(false, "Successfully get jenis invest");
            } else {
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Failed get jenis invest");
            }
        }catch (Exception e){
            handleSuccessOrNot = new HandleSuccessOrNot(true, ResponseMessage.ERROR_SYSTEM);
            logger.error(String.format("%s: %s, %s: %s", "Path", request.getServletPath(), "error get info account", e));
        }
        ResponseData responseData = new ResponseData();
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);
        return responseData;
    }

    @Override
    public ResponseData getDailyPriceFundDplk(Integer lji_id, HttpServletRequest request) {
        HandleSuccessOrNot handleSuccessOrNot;
        HashMap<String, Object> data = new HashMap<>();
        try {
            DailyPriceFundDplk dailyPriceFundDplk = services.getDailyPriceFundDplk(lji_id);
            if (dailyPriceFundDplk != null ){
                data.put("lji_id", dailyPriceFundDplk.getLji_id());
                data.put("lji_invest", dailyPriceFundDplk.getLji_invest());
                data.put("lku_id", dailyPriceFundDplk.getLku_id());
                data.put("price_date_now", dateUtils.getFormatterFormat(dailyPriceFundDplk.getLnu_tgl(), DateUtils.FORMAT_DAY_MONTH_YEAR, "GMT+7"));
                data.put("price_now", dailyPriceFundDplk.getLnu_nilai());
                data.put("price_date_before", dateUtils.getFormatterFormat(dailyPriceFundDplk.getLnu_tgl_sebelum(), DateUtils.FORMAT_DAY_MONTH_YEAR, "GMT+7"));
                data.put("price_before", dailyPriceFundDplk.getNilai_sebelum());
                data.put("selisih_nilai", dailyPriceFundDplk.getSelisih_nilai());
                data.put("persen_selisih", dailyPriceFundDplk.getPersen_selisih());
                handleSuccessOrNot = new HandleSuccessOrNot(false, "Successfully get jenis invest");
            } else {
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Failed get jenis invest");
            }
        }catch (Exception e){
            handleSuccessOrNot = new HandleSuccessOrNot(true, ResponseMessage.ERROR_SYSTEM);
            logger.error(String.format("%s: %s, %s: %s", "Path", request.getServletPath(), "error get info account", e));
        }
        ResponseData responseData = new ResponseData();
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);
        return responseData;
    }
}
