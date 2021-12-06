package com.app.services;

import com.app.exception.HandleSuccessOrNot;
import com.app.model.DPLKAccountModel;
import com.app.model.LstUserSimultaneous;
import com.app.model.ResponseData;
import com.app.utils.DateUtils;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Service
public class DPLKAccountSvcImpl implements DPLKAccountSvc {

    private static final Logger logger = LogManager.getLogger(DPLKAccountSvcImpl.class);

    @Autowired
    private VegaServices services;
    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private VegaCustomResourceLoader customResourceLoader;

    @Override
    public ResponseData findAccountDPLK(DPLKAccountModel requestBody, HttpServletRequest request) {
        HandleSuccessOrNot handleSuccessOrNot;
        HashMap<String, Object> data = new HashMap<>();
        try {
            DPLKAccountModel account = services.findAccountDplk(requestBody.getAcc_no(), requestBody.getDob());
            if (account != null) {
                data.put("account_no", account.getAcc_no());
                data.put("dob", dateUtils.getFormatterFormat(account.getMspe_date_birth(), DateUtils.FORMAT_DAY_MONTH_YEAR, "GMT+7"));
                data.put("no_hp", account.getNo_hp());
                handleSuccessOrNot = new HandleSuccessOrNot(false, "Success find account");
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
            if (isExistUsername) {
                handleSuccessOrNot = new HandleSuccessOrNot(true, "username is exists");
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
}
