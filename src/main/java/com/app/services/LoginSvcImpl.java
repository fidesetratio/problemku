package com.app.services;

import com.app.exception.HandleSuccessOrNot;
import com.app.model.*;
import com.app.model.request.RequestLogin;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class LoginSvcImpl implements LoginSvc {

    private static final Logger logger = LogManager.getLogger(LoginSvcImpl.class);

    private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${link.fcm.google}")
    private String linkFcmGoogle;

    @Autowired
    private VegaServices services;
    @Autowired
    private VegaCustomResourceLoader customResourceLoader;
    @Autowired
    private RegistrationIndividuSvc registrationIndividuSvc;

    @Override
    public ResponseData login(RequestLogin requestLogin, HttpServletRequest request) {
        Gson gson = new Gson();
        String req = gson.toJson(requestLogin);
        String username = requestLogin.getUsername();
        String password = requestLogin.getPassword();
        String lastLoginDevice = requestLogin.getLast_login_device();
        HandleSuccessOrNot handleSuccessOrNot = null;
        HashMap<String, Object> data = new HashMap<>();
        ResponseData responseData = new ResponseData();

        try {
            String key = null;

            LstUserSimultaneous lstUserSimultaneous = new LstUserSimultaneous();
            lstUserSimultaneous.setUSERNAME(username);
            lstUserSimultaneous.setLAST_LOGIN_DEVICE(lastLoginDevice);
            lstUserSimultaneous.setFLAG_ACTIVE(1);
            LstUserSimultaneous user1 = services.selectLoginAuthenticate(lstUserSimultaneous);

            HashMap<String, Object> dataConfiguration = services.configuration();
            Integer time_idle = Integer.parseInt((String) dataConfiguration.get("TIME_IDLE"));

            LstUserSimultaneous checkIndividuOrCorporate = services.selectDataLstUserSimultaneous(username);

            if (checkIndividuOrCorporate != null) {

                boolean isIndividu = isIndividu(checkIndividuOrCorporate);
                boolean isHrUser = isHrUser(checkIndividuOrCorporate);
                boolean isIndividuCorporate = isIndividuCorporate(checkIndividuOrCorporate);
                boolean corporate = corporate(checkIndividuOrCorporate);
                boolean isAccountDplk = isAccountDplk(checkIndividuOrCorporate);
                boolean isIndividuMri = registrationIndividuSvc.isIndividuMri(checkIndividuOrCorporate.getID_SIMULTAN(), checkIndividuOrCorporate.getUSERNAME());
                boolean policy_corporate_notinforce = false;
                boolean user_corporate_notactive = false;

                if (isIndividu) {
                   return loginIndividu(isIndividu, isIndividuMri, username, password, data, user1, lstUserSimultaneous,
                            request, key, lastLoginDevice, time_idle, req);
                } else if (isIndividuCorporate || corporate) {
                    ArrayList<UserCorporate> listPolisCorporate = services.selectListPolisCorporate(checkIndividuOrCorporate.getMCL_ID_EMPLOYEE());
                    for (int x = 0; x < 1; x++) {
                        Date endDate = listPolisCorporate.get(x).getMspo_end_date();
                        BigDecimal flagActiveUserCorporate = listPolisCorporate.get(x).getMste_active();
                        LocalDate now = LocalDate.now();
                        LocalDate endDateParse = LocalDate.parse(df1.format(endDate));
                        // Check no polis corporate active or not
                        policy_corporate_notinforce = endDateParse.compareTo(now) < 0;

                        // Check user corporate active or not
                        if (flagActiveUserCorporate.intValue() == 0) {
                            user_corporate_notactive = true;
                        }
                    }
                    return loginCorporate(isIndividuCorporate, policy_corporate_notinforce, user_corporate_notactive, username, password, data, user1,
                            lstUserSimultaneous, request, key, lastLoginDevice, time_idle, checkIndividuOrCorporate.getMCL_ID_EMPLOYEE(), req);
                } else if (isHrUser){
                    return loginHr(user1, password, lstUserSimultaneous, key, data, username, request, responseData, req);
                } else if (isAccountDplk){
                    User dplkAccount = services.findByUsernameDplk(username);
                    return loginDplk(dplkAccount, responseData, lstUserSimultaneous, key, user1, data, request, username, req);
                }
            } else {
                // Error username yang dimasukkan tidak ada pada database
                String resultErr = "Username tidak terdaftar";
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
            }
        } catch (Exception e) {
            handleSuccessOrNot = new HandleSuccessOrNot(true, ResponseMessage.ERROR_SYSTEM);
            String resultErr = "bad exception " + e;
            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
        }
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);

        return responseData;
    }

    @Override
    public boolean isIndividu(LstUserSimultaneous checkIndividuOrCorporate) {
        return checkIndividuOrCorporate != null && checkIndividuOrCorporate.getREG_SPAJ() != null && checkIndividuOrCorporate.getMCL_ID_EMPLOYEE() == null
                && checkIndividuOrCorporate.getEB_HR_USERNAME() == null;
    }

    @Override
    public boolean isHrUser(LstUserSimultaneous checkIndividuOrCorporate) {
        return checkIndividuOrCorporate != null && checkIndividuOrCorporate.getEB_HR_USERNAME() != null;
    }

    @Override
    public boolean isAccountDplk(LstUserSimultaneous checkIndividuOrCorporate) {
        return checkIndividuOrCorporate != null && checkIndividuOrCorporate.getAccount_no_dplk() != null;
    }

    @Override
    public boolean isIndividuCorporate(LstUserSimultaneous checkIndividuOrCorporate) {
        return checkIndividuOrCorporate != null && checkIndividuOrCorporate.getMCL_ID_EMPLOYEE() != null && checkIndividuOrCorporate.getREG_SPAJ() != null &&
                checkIndividuOrCorporate.getEB_HR_USERNAME() == null;
    }

    @Override
    public boolean corporate(LstUserSimultaneous checkIndividuOrCorporate) {
        return checkIndividuOrCorporate != null && checkIndividuOrCorporate.getMCL_ID_EMPLOYEE() != null && checkIndividuOrCorporate.getREG_SPAJ() == null &&
                checkIndividuOrCorporate.getEB_HR_USERNAME() == null;
    }

    private ResponseData loginIndividu(boolean isIndividu, boolean isIndividuMri, String username, String password, HashMap<String, Object> data,
                                       LstUserSimultaneous user1, LstUserSimultaneous lstUserSimultaneous,
                                       HttpServletRequest request, String key, String lastLoginDevice, Integer timeIdle, String req) {
        ResponseData responseData = new ResponseData();
        HandleSuccessOrNot handleSuccessOrNot = null;
        if (isIndividu) {
            User dataActivityUser = services.selectUserIndividual(username);
            // Check apakah username terdaftar/ tidak
            if (dataActivityUser != null) {
                Integer countDeathClaim = services.selectCountDeathClaim(username);
                if (countDeathClaim == 0) {
                    if (dataActivityUser.getLast_login_device() != null && dataActivityUser.getLast_login_device().equals(lastLoginDevice)) {
                        if (user1.getPASSWORD().equals(password)) {
                            ArrayList<User> list = services.selectDetailedPolis(username);
                            // Check apakah username tersebut punya list polis/ tidak
                            if (list.size() > 0 || isIndividuMri) {
                                String today = df.format(new Date());
                                lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
                                lstUserSimultaneous.setUPDATE_DATE_TIME(today);
                                services.updateUserKeyName(lstUserSimultaneous);
                                key = user1.getKEY();

                                data = getMapObjc(true, false, false, isIndividuMri, false,
                                        false, false, key, dataActivityUser.getNo_hp() != null ? dataActivityUser.getNo_hp()
                                                : dataActivityUser.getNo_hp2(), data);
                                handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
                            } else {
                                // Error list polis kosong
                                String resultErr = "List Polis Kosong";
                                handleSuccessOrNot = new HandleSuccessOrNot(true, "List policy is empty");
                                logger.error("Path: " + request.getServletPath() + " Username: " + username
                                        + " Error: " + resultErr);
                            }
                        } else {
                            // Error password yang dimasukkan salah
                            String resultErr = "Password yang dimasukkan salah";
                            handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
                            logger.error("Path: " + request.getServletPath() + " Username: " + username
                                    + " Error: " + resultErr);
                        }
                    } else {
                        // Kondisi dimana token ada tetapi berbeda dengan yang dikirim
                        Date dateActivity = dataActivityUser.getUpdate_date();
                        Date dateNow = new Date();
                        long diff = dateNow.getTime() - dateActivity.getTime();
                        long diffSeconds = diff / 1000;
                        if (diffSeconds >= timeIdle || dataActivityUser.getLast_login_device() == null) {
                            // Check apabila lebih dari 15 menit, password yang dimasukkan kosong/ ""
                            // Check password yang dimasukkan sama/ tidak dengan yang didb
                            if (user1.getPASSWORD().equals(password)) {
                                ArrayList<User> list = services.selectDetailedPolis(username);
                                if (list.size() > 0 || isIndividuMri) {
                                    customResourceLoader.postGoogle(linkFcmGoogle, username,
                                            dataActivityUser.getLast_login_device());
                                    String today = df.format(new Date());
                                    lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
                                    lstUserSimultaneous.setUPDATE_DATE_TIME(today);
                                    services.updateUserKeyName(lstUserSimultaneous);
                                    key = user1.getKEY();

                                    data = getMapObjc(true, false, false, isIndividuMri, false,
                                            false, false, key, dataActivityUser.getNo_hp() != null ? dataActivityUser.getNo_hp()
                                                    : dataActivityUser.getNo_hp2(), data);
                                    handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
                                } else {
                                    // Error list polis kosong
                                    String resultErr = "List Polis Kosong";
                                    handleSuccessOrNot = new HandleSuccessOrNot(true, "List policy is empty");
                                    logger.error("Path: " + request.getServletPath() + " Username: " + username
                                            + " Error: " + resultErr);
                                }
                            } else {
                                // Error password yang dimasukkan salah
                                String resultErr = "Password yang dimasukkan salah";
                                handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
                                logger.error("Path: " + request.getServletPath() + " Username: " + username
                                        + " Error: " + resultErr);
                            }
                        } else {
                            // Error perbedaan lama waktu token belum lebih dari 15 menit
                            handleSuccessOrNot = new HandleSuccessOrNot(true, "Session still active");
                            String resultErr = "Account sedang login ditempat lain atau ada session yang belum berakhir";
                            logger.error("Path: " + request.getServletPath() + " Username: " + username
                                    + " Error: " + resultErr);
                        }
                    }
                } else {
                    handleSuccessOrNot = new HandleSuccessOrNot(true, "Policy number exists containing death claims");
                    String resultErr = "No polis pada username ini ada yang mengandung death claim";
                    logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                            + resultErr);
                }
            } else {
                // Error username yang dimasukkan tidak ada pada database
                String resultErr = "Username tidak terdaftar";
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                        + resultErr);
            }

        }
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);

        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 1, new Date(), req , responseData.toString(), 1, null, new Date(), username);

        return responseData;
    }

    private ResponseData loginCorporate(boolean individuCorporate, boolean policy_corporate_notinforce, boolean user_corporate_notactive, String username, String password, HashMap<String, Object> data,
                                        LstUserSimultaneous user1, LstUserSimultaneous lstUserSimultaneous,
                                        HttpServletRequest request, String key, String lastLoginDevice, Integer timeIdle, 
                                        String mcl_id_employee, String req) {
        ResponseData responseData = new ResponseData();
        HandleSuccessOrNot handleSuccessOrNot = null;
        UserCorporate dataUserCorporate = services.selectUserCorporate(username);
        // Check apakah username terdaftar/ tidak
        if (dataUserCorporate != null) {
            if (dataUserCorporate.getLast_login_device() != null && dataUserCorporate.getLast_login_device().equals(lastLoginDevice)) {
                if (user1.getPASSWORD().equals(password)) {
                    ArrayList<UserCorporate> list = services.selectListPolisCorporate(mcl_id_employee);
                    // Check apakah username tersebut punya list polis/ tidak
                    if (list.size() > 0) {
                        String today = df.format(new Date());
                        lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
                        lstUserSimultaneous.setUPDATE_DATE_TIME(today);
                        services.updateUserKeyName(lstUserSimultaneous);
                        key = user1.getKEY();
                        data = getMapObjc(individuCorporate, true, false, false, policy_corporate_notinforce,
                                user_corporate_notactive, false, key, dataUserCorporate.getNo_hp(), data);
                        handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
                    } else {
                        // Error list polis kosong
                        handleSuccessOrNot = new HandleSuccessOrNot(true, "List policy is empty");
                        String resultErr = "List Polis Kosong";
                        logger.error("Path: " + request.getServletPath() + " Username: " + username
                                + " Error: " + resultErr);
                    }
                } else {
                    // Error password yang dimasukkan salah
                    handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
                    String resultErr = "Password yang dimasukkan salah";
                    logger.error("Path: " + request.getServletPath() + " Username: " + username
                            + " Error: " + resultErr);
                }
            } else {
                Date dateActivity = dataUserCorporate.getUpdate_date();
                Date dateNow = new Date();
                long diff = dateNow.getTime() - dateActivity.getTime();
                long diffSeconds = diff / 1000;
                // Check apakah lama perbedaan waktu token sudah melebihi 15 menit/ belum
                if (diffSeconds >= timeIdle || dataUserCorporate.getLast_login_device() == null) {
                    if (user1.getPASSWORD().equals(password)) {
                        String today = df.format(new Date());
                        lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
                        lstUserSimultaneous.setUPDATE_DATE_TIME(today);
                        services.updateUserKeyName(lstUserSimultaneous);
                        key = user1.getKEY();
                        data = getMapObjc(individuCorporate, true, false, false, policy_corporate_notinforce,
                                user_corporate_notactive, false, key, dataUserCorporate.getNo_hp(), data);
                        handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
                    } else {
                        // Error password kosong
                        handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
                        String resultErr = "Format password incorect";
                        logger.error("Path: " + request.getServletPath() + " Username: " + username
                                + " Error: " + resultErr);
                    }
                } else {
                    // Error perbedaan lama waktu token belum lebih dari 15 menit
                    handleSuccessOrNot = new HandleSuccessOrNot(true, "Session still active");
                    String resultErr = "Account sedang login ditempat lain atau ada session yang belum berakhir";
                    logger.error("Path: " + request.getServletPath() + " Username: " + username
                            + " Error: " + resultErr);
                }
            }
        } else {
            // Error username yang dimasukkan tidak ada pada database
            String resultErr = "Username tidak terdaftar";
            handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                    + resultErr);
        }
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);

        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 1, new Date(), req , responseData.toString(), 1, null, new Date(), username);
        return responseData;
    }

    private ResponseData loginDplk(User dplkAccount, ResponseData responseData, LstUserSimultaneous lstUserSimultaneous, String key,
                                   LstUserSimultaneous user1, HashMap<String, Object> data, HttpServletRequest request,
                                   String username, String req){
        HandleSuccessOrNot handleSuccessOrNot = null;
        if (dplkAccount != null){
            String today = df.format(new Date());
            lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
            lstUserSimultaneous.setUPDATE_DATE_TIME(today);
            services.updateUserKeyName(lstUserSimultaneous);
            key = user1.getKEY();
            getMapObjc(false, false, false, false, false,
                    false, true, key, null, data);
            handleSuccessOrNot = new HandleSuccessOrNot(true, "Login success");
        } else {
            // Error username yang dimasukkan tidak ada pada database
            handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
            String resultErr = "Username tidak terdaftar";
            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                    + resultErr);
        }
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);

        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 1, new Date(), req , responseData.toString(), 1, null, new Date(), username);
        return responseData;
    }

    private ResponseData loginHr(LstUserSimultaneous user1, String password, LstUserSimultaneous lstUserSimultaneous, String key, HashMap<String, Object> data, String username,
                                 HttpServletRequest request, ResponseData responseData, String req){
        HandleSuccessOrNot handleSuccessOrNot = null;
        if (user1.getPASSWORD().equals(password)) {
            String today = df.format(new Date());
            lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
            lstUserSimultaneous.setUPDATE_DATE_TIME(today);
            services.updateUserKeyName(lstUserSimultaneous);
            key = user1.getKEY();
            getMapObjc(false, false, true, false, false,
                    false, false, key, null, data);
            handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
        } else {
            // Error username yang dimasukkan tidak ada pada database
            handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
            String resultErr = "Username tidak terdaftar";
            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                    + resultErr);
        }
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);

        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 1, new Date(), req , responseData.toString(), 1, null, new Date(), username);
        return responseData;
    }

    private HashMap<String, Object> getMapObjc(boolean individu, boolean corporate, boolean hr_user, boolean individu_mri,
                                               boolean policy_corporate_notinforce, boolean user_corporate_notactive, boolean accountDplk, String key,
                                               String no_hp, HashMap<String, Object> data) {
        data.put("individual", individu);
        data.put("corporate", corporate);
        data.put("hr_user", hr_user);
        data.put("individu_mri", individu_mri);
        data.put("account_dplk", accountDplk);
        data.put("policy_corporate_status", policy_corporate_notinforce);
        data.put("user_corporate_notactive", user_corporate_notactive);
        data.put("key", key);
        data.put("no_hp", no_hp);
        return data;
    }

//    private void backupLogin(){
//        Date start = new Date();
//        GsonBuilder builder = new GsonBuilder();
//        builder.serializeNulls();
//        Gson gson = new Gson();
//        gson = builder.create();
//        String req = gson.toJson(requestLogin);
//        String res = null;
//        String resultErr = null;
//        String message = null;
//        Boolean error = false;
//        HashMap<String, Object> map = new HashMap<>();
//        HashMap<String, Object> data = new HashMap<>();
//        String username = requestLogin.getUsername();
//        String password = requestLogin.getPassword();
//        String lastLoginDevice = requestLogin.getLast_login_device();
//        try {
//            String key = null;
//
//            LstUserSimultaneous lstUserSimultaneous = new LstUserSimultaneous();
//            lstUserSimultaneous.setUSERNAME(username);
//            lstUserSimultaneous.setLAST_LOGIN_DEVICE(lastLoginDevice);
//            lstUserSimultaneous.setFLAG_ACTIVE(1);
//            LstUserSimultaneous user1 = services.selectLoginAuthenticate(lstUserSimultaneous);
//
//            HashMap<String, Object> dataConfiguration = services.configuration();
//            Integer time_idle = Integer.parseInt((String) dataConfiguration.get("TIME_IDLE"));
//
//            LstUserSimultaneous checkIndividuOrCorporate = services.selectDataLstUserSimultaneous(username);
//
//            if (checkIndividuOrCorporate != null) {
//                String reg_spaj = checkIndividuOrCorporate.getREG_SPAJ();
//                String mcl_id_employee = checkIndividuOrCorporate.getMCL_ID_EMPLOYEE();
//                String eb_hr_username = checkIndividuOrCorporate.getEB_HR_USERNAME();
//                String id_simultan = checkIndividuOrCorporate.getID_SIMULTAN();
//                String account_no_dplk = checkIndividuOrCorporate.getAccount_no_dplk();
//
//                Boolean individu = false;
//                Boolean corporate = false;
//                Boolean hr_user = false;
//                Boolean individu_mri = false;
//                Boolean is_account_dplk = false;
//                Boolean policy_corporate_notinforce = false;
//                Boolean user_corporate_notactive = false;
//
//                if ((reg_spaj != null) && (mcl_id_employee == null) && (eb_hr_username == null)) {
//                    individu = true;
//                    corporate = false;
//                    hr_user = false;
//                    individu_mri = registrationIndividuSvc.isIndividuMri(id_simultan, username);
//                } else if ((reg_spaj == null) && (mcl_id_employee != null) && (eb_hr_username == null)) {
//                    individu = false;
//                    corporate = true;
//                    hr_user = false;
//                    individu_mri = registrationIndividuSvc.isIndividuMri(id_simultan, username);
//
//                    ArrayList<UserCorporate> listPolisCorporate = services.selectListPolisCorporate(mcl_id_employee);
//                    for (int x = 0; x < 1; x++) {
//                        Date endDate = listPolisCorporate.get(x).getMspo_end_date();
//                        BigDecimal flagActiveUserCorporate = listPolisCorporate.get(x).getMste_active();
//                        LocalDate now = LocalDate.now();
//                        LocalDate endDateParse = LocalDate.parse(df1.format(endDate));
//                        // Check no polis corporate active or not
//                        policy_corporate_notinforce = endDateParse.compareTo(now) < 0;
//
//                        // Check user corporate active or not
//                        if (flagActiveUserCorporate.intValue() == 0) {
//                            user_corporate_notactive = true;
//                        }
//                    }
//                } else if ((reg_spaj == null) && (mcl_id_employee == null) && (eb_hr_username != null)) {
//                    individu = false;
//                    corporate = false;
//                    hr_user = true;
//                    individu_mri = registrationIndividuSvc.isIndividuMri(id_simultan, username);
//                } else if (reg_spaj == null && mcl_id_employee == null && account_no_dplk != null) {
//                    individu = false;
//                    corporate = false;
//                    hr_user = false;
//                    individu_mri = false;
//                    is_account_dplk = true;
//                } else {
//                    individu = true;
//                    corporate = true;
//                    hr_user= false;
//                    individu_mri = registrationIndividuSvc.isIndividuMri(id_simultan, username);
//
//                    ArrayList<UserCorporate> listPolisCorporate = services.selectListPolisCorporate(mcl_id_employee);
//                    if (listPolisCorporate != null && listPolisCorporate.size() > 0){
//                        for (int x = 0; x < 1; x++) {
//                            Date endDate = listPolisCorporate.get(x).getMspo_end_date();
//                            BigDecimal flagActiveUserCorporate = listPolisCorporate.get(x).getMste_active();
//                            LocalDate now = LocalDate.now();
//                            LocalDate endDateParse = LocalDate.parse(df1.format(endDate));
//                            // Check no polis corporate active or not
//                            policy_corporate_notinforce = endDateParse.compareTo(now) < 0;
//
//                            // Check user corporate active or not
//                            if (flagActiveUserCorporate.intValue() == 0) {
//                                user_corporate_notactive = true;
//                            }
//                        }
//                    }
//                }
//
//                if (individu.equals(true)) { // Login Individual
//                    User dataActivityUser = services.selectUserIndividual(username);
//                    // Check apakah username terdaftar/ tidak
//                    if (dataActivityUser != null) {
//                        Integer countDeathClaim = services.selectCountDeathClaim(username);
//                        // Check apakah username tersebut mengandung polis death claim
//                        if (countDeathClaim == 0) {
//                            // Check apakah token yang ada didatabase kosong/ tidak
//                            if (dataActivityUser.getLast_login_device() != null) {
//                                // Check karena token ada, apakah token yang dikirim sama dengan yang didb
//                                if (dataActivityUser.getLast_login_device().equals(lastLoginDevice)) {
//                                    // Check password yang diinputkan benar atau salah
//                                    if (user1.getPASSWORD().equals(password)) {
//                                        ArrayList<User> list = services.selectDetailedPolis(username);
//                                        // Check apakah username tersebut punya list polis/ tidak
//                                        if (list.size() > 0 || individu_mri) {
//                                            String today = df.format(new Date());
//                                            lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
//                                            lstUserSimultaneous.setUPDATE_DATE_TIME(today);
//                                            services.updateUserKeyName(lstUserSimultaneous);
//                                            key = user1.getKEY();
//
//                                            error = false;
//                                            message = "Login success";
//                                            data.put("individual", individu);
//                                            data.put("corporate", corporate);
//                                            data.put("hr_user", hr_user);
//                                            data.put("individu_mri", individu_mri);
//                                            data.put("policy_corporate_status", policy_corporate_notinforce);
//                                            data.put("user_corporate_notactive", user_corporate_notactive);
//                                            data.put("key", key);
//                                            data.put("no_hp",
//                                                    dataActivityUser.getNo_hp() != null ? dataActivityUser.getNo_hp()
//                                                            : dataActivityUser.getNo_hp2());
//                                        } else {
//                                            // Error list polis kosong
//                                            error = true;
//                                            message = "List policy is empty";
//                                            resultErr = "List Polis Kosong";
//                                            logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                                    + " Error: " + resultErr);
//                                        }
//                                    } else {
//                                        // Error password yang dimasukkan salah
//                                        error = true;
//                                        message = "Login failed";
//                                        resultErr = "Password yang dimasukkan salah";
//                                        logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                                + " Error: " + resultErr);
//                                    }
//                                } else {
//                                    // Kondisi dimana token ada tetapi berbeda dengan yang dikirim
//                                    Date dateActivity = dataActivityUser.getUpdate_date();
//                                    Date dateNow = new Date();
//                                    long diff = dateNow.getTime() - dateActivity.getTime();
//                                    long diffSeconds = diff / 1000;
//                                    // Check apakah lama perbedaan waktu token sudah melebihi 15 menit/ belum
//                                    if (diffSeconds >= time_idle) {
//                                        // Check apabila lebih dari 15 menit, password yang dimasukkan kosong/ ""
//                                        // Check password yang dimasukkan sama/ tidak dengan yang didb
//                                        if (user1.getPASSWORD().equals(password)) {
//                                            ArrayList<User> list = services.selectDetailedPolis(username);
//                                            // Check apakah username tersebut memiliki list polis atau tidak
//                                            if (list.size() > 0 || individu_mri) {
//                                                try {
//                                                    customResourceLoader.postGoogle(linkFcmGoogle, username,
//                                                            dataActivityUser.getLast_login_device());
//
//                                                    String today = df.format(new Date());
//                                                    lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
//                                                    lstUserSimultaneous.setUPDATE_DATE_TIME(today);
//                                                    services.updateUserKeyName(lstUserSimultaneous);
//                                                    key = user1.getKEY();
//
//                                                    error = false;
//                                                    message = "Login success";
//                                                    data.put("individual", individu);
//                                                    data.put("corporate", corporate);
//                                                    data.put("hr_user", hr_user);
//                                                    data.put("individu_mri", individu_mri);
//                                                    data.put("policy_corporate_status",
//                                                            policy_corporate_notinforce);
//                                                    data.put("user_corporate_notactive", user_corporate_notactive);
//                                                    data.put("key", key);
//                                                    data.put("no_hp",
//                                                            dataActivityUser.getNo_hp() != null
//                                                                    ? dataActivityUser.getNo_hp()
//                                                                    : dataActivityUser.getNo_hp2());
//                                                } catch (Exception e) {
//                                                    logger.error("Path: " + request.getServletPath() + " Username: "
//                                                            + username + " Error: " + e);
//                                                }
//                                            } else {
//                                                // Error list polis pada username tersebut kosong
//                                                error = true;
//                                                message = "List policy is empty";
//                                                resultErr = "List Polis Kosong";
//                                                logger.error("Path: " + request.getServletPath() + " Username: "
//                                                        + username + " Error: " + resultErr);
//                                            }
//                                        } else {
//                                            // Error password yang dimasukkan user salah
//                                            error = true;
//                                            message = "Login failed";
//                                            resultErr = "Password yang dimasukkan salah";
//                                            logger.error("Path: " + request.getServletPath() + " Username: "
//                                                    + username + " Error: " + resultErr);
//                                        }
//                                    } else {
//                                        // Error perbedaan lama waktu token belum lebih dari 15 menit
//                                        error = true;
//                                        message = "Session still active";
//                                        resultErr = "Account sedang login ditempat lain atau ada session yang belum berakhir";
//                                        logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                                + " Error: " + resultErr);
//                                    }
//                                }
//                            } else {
//                                // Kondisi token pada database tidak ada bisa kerena logout/ user baru
//                                // Check apabila lebih dari 15 menit, password yang dimasukkan kosong/ ""
//                                // Check password yang dimasukkan sama/ tidak dengan yang didb
//                                if (user1.getPASSWORD().equals(password)) {
//                                    ArrayList<User> list = services.selectDetailedPolis(username);
//                                    // Check apakah username tersebut memiliki list polis atau tidak
//                                    if (list.size() > 0 || individu_mri) {
//                                        String today = df.format(new Date());
//                                        lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
//                                        lstUserSimultaneous.setUPDATE_DATE_TIME(today);
//                                        services.updateUserKeyName(lstUserSimultaneous);
//                                        key = user1.getKEY();
//
//                                        error = false;
//                                        message = "Login success";
//                                        data.put("individual", individu);
//                                        data.put("corporate", corporate);
//                                        data.put("hr_user", hr_user);
//                                        data.put("individu_mri", individu_mri);
//                                        data.put("policy_corporate_status", policy_corporate_notinforce);
//                                        data.put("user_corporate_notactive", user_corporate_notactive);
//                                        data.put("key", key);
//                                        data.put("no_hp",
//                                                dataActivityUser.getNo_hp() != null ? dataActivityUser.getNo_hp()
//                                                        : dataActivityUser.getNo_hp2());
//                                    } else {
//                                        // Error list polis pada username tersebut kosong
//                                        error = true;
//                                        message = "List policy is empty";
//                                        resultErr = "List Polis Kosong";
//                                        logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                                + " Error: " + resultErr);
//                                    }
//                                } else {
//                                    // Error password yang dimasukkan user salah
//                                    error = true;
//                                    message = "Login failed";
//                                    resultErr = "Password yang dimasukkan salah";
//                                    logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                            + " Error: " + resultErr);
//                                }
//                            }
//                        } else {
//                            // Error pada username tersebut mengandung polis death claim
//                            error = true;
//                            message = "Policy number exists containing death claims";
//                            resultErr = "No polis pada username ini ada yang mengandung death claim";
//                            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
//                                    + resultErr);
//                        }
//                    } else {
//                        // Error username yang dimasukkan tidak ada pada database
//                        error = true;
//                        message = "Login failed";
//                        resultErr = "Username tidak terdaftar";
//                        logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
//                                + resultErr);
//                    }
//                } else if (corporate.equals(false) && hr_user.equals(true)) { // Login HR
//                    if (user1.getPASSWORD().equals(password)) {
//
//                        String today = df.format(new Date());
//                        lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
//                        lstUserSimultaneous.setUPDATE_DATE_TIME(today);
//                        services.updateUserKeyName(lstUserSimultaneous);
//                        key = user1.getKEY();
//                        error = false;
//                        message = "Login success";
//                        data.put("individual", individu);
//                        data.put("corporate", corporate);
//                        data.put("hr_user", hr_user);
//                        data.put("individu_mri", false);
//                        data.put("policy_corporate_status", policy_corporate_notinforce);
//                        data.put("user_corporate_notactive", user_corporate_notactive);
//                        data.put("key", key);
//                        data.put("no_hp", null);
//                    }else {
//                        // Error username yang dimasukkan tidak ada pada database
//                        error = true;
//                        message = "Login failed";
//                        resultErr = "Username tidak terdaftar";
//                        logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
//                                + resultErr);
//
//
//                    }
//                } else if (is_account_dplk){
//                    User dplkAccount = services.findByUsernameDplk(username);
//                    if (dplkAccount != null && dplkAccount.getLast_login_device().equals(lastLoginDevice)){
//                        if (user1.getPASSWORD().equals(password)) {
//                            ArrayList<User> list = services.selectDetailedPolis(username);
//                            // Check apakah username tersebut punya list polis/ tidak
//                            if (list.size() > 0 || individu_mri) {
//                                String today = df.format(new Date());
//                                lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
//                                lstUserSimultaneous.setUPDATE_DATE_TIME(today);
//                                services.updateUserKeyName(lstUserSimultaneous);
//                                key = user1.getKEY();
//
//                                error = false;
//                                message = "Login success";
//                                data.put("individual", false);
//                                data.put("corporate", false);
//                                data.put("hr_user", false);
//                                data.put("individu_mri", false);
//                                data.put("is_account_dplk", true);
//                                data.put("policy_corporate_status", false);
//                                data.put("user_corporate_notactive", false);
//                                data.put("key", key);
//                                data.put("no_hp",
//                                        dplkAccount.getNo_hp() != null ? dplkAccount.getNo_hp()
//                                                : dplkAccount.getNo_hp2());
//                            } else {
//                                // Error list polis kosong
//                                error = true;
//                                message = "List policy is empty";
//                                resultErr = "List Polis Kosong";
//                                logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                        + " Error: " + resultErr);
//                            }
//                        } else {
//                            // Error password yang dimasukkan salah
//                            error = true;
//                            message = "Login failed";
//                            resultErr = "Password yang dimasukkan salah";
//                            logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                    + " Error: " + resultErr);
//                        }
//                    } else {
//                        Date dateActivity = dplkAccount.getUpdate_date();
//                        Date dateNow = new Date();
//                        long diff = dateNow.getTime() - dateActivity.getTime();
//                        long diffSeconds = diff / 1000;
//
//                    }
//                } else { // Login Corporate
//                    UserCorporate dataUserCorporate = services.selectUserCorporate(username);
//                    // Check apakah username terdaftar/ tidak
//                    if (dataUserCorporate != null) {
//                        // Check apakah token yang ada didatabase kosong/ tidak
//                        if (dataUserCorporate.getLast_login_device() != null) {
//                            // Check karena token ada, apakah token yang dikirim sama dengan yang didb
//                            if (dataUserCorporate.getLast_login_device().equals(lastLoginDevice)) {
//                                // Check password yang diinputkan benar atau salah
//                                if (user1.getPASSWORD().equals(password)) {
//                                    ArrayList<UserCorporate> list = services.selectListPolisCorporate(mcl_id_employee);
//                                    // Check apakah username tersebut punya list polis/ tidak
//                                    if (list.size() > 0) {
//                                        String today = df.format(new Date());
//                                        lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
//                                        lstUserSimultaneous.setUPDATE_DATE_TIME(today);
//                                        services.updateUserKeyName(lstUserSimultaneous);
//                                        key = user1.getKEY();
//
//                                        error = false;
//                                        message = "Login success";
//                                        data.put("individual", individu);
//                                        data.put("corporate", corporate);
//                                        data.put("hr_user", hr_user);
//                                        data.put("individu_mri", false);
//                                        data.put("policy_corporate_status", policy_corporate_notinforce);
//                                        data.put("user_corporate_notactive", user_corporate_notactive);
//                                        data.put("key", key);
//                                        data.put("no_hp", dataUserCorporate.getNo_hp());
//                                    } else {
//                                        // Error list polis kosong
//                                        error = true;
//                                        message = "List policy is empty";
//                                        resultErr = "List Polis Kosong";
//                                        logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                                + " Error: " + resultErr);
//                                    }
//                                } else {
//                                    // Error password yang dimasukkan salah
//                                    error = true;
//                                    message = "Login failed";
//                                    resultErr = "Password yang dimasukkan salah";
//                                    logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                            + " Error: " + resultErr);
//                                }
//                            } else {
//                                // Kondisi dimana token ada tetapi berbeda dengan yang dikirim
//                                Date dateActivity = dataUserCorporate.getUpdate_date();
//                                Date dateNow = new Date();
//                                long diff = dateNow.getTime() - dateActivity.getTime();
//                                long diffSeconds = diff / 1000;
//                                // Check apakah lama perbedaan waktu token sudah melebihi 15 menit/ belum
//                                if (diffSeconds >= time_idle) {
//                                    // Check apabila lebih dari 15 menit, password yang dimasukkan kosong/ ""
//                                    if (user1.getPASSWORD() != null || user1.getPASSWORD() != "") {
//                                        // Check password yang dimasukkan sama/ tidak dengan yang didb
//                                        if (user1.getPASSWORD().equals(password)) {
//                                            ArrayList<UserCorporate> list = services
//                                                    .selectListPolisCorporate(mcl_id_employee);
//                                            // Check apakah username tersebut memiliki list polis atau tidak
//                                            if (list.size() > 0) {
//                                                try {
//                                                    customResourceLoader.postGoogle(linkFcmGoogle, username,
//                                                            dataUserCorporate.getLast_login_device());
//
//                                                    String today = df.format(new Date());
//                                                    lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
//                                                    lstUserSimultaneous.setUPDATE_DATE_TIME(today);
//                                                    services.updateUserKeyName(lstUserSimultaneous);
//                                                    key = user1.getKEY();
//
//                                                    error = false;
//                                                    message = "Login success";
//                                                    data.put("individual", individu);
//                                                    data.put("corporate", corporate);
//                                                    data.put("hr_user", hr_user);
//                                                    data.put("policy_corporate_status", policy_corporate_notinforce);
//                                                    data.put("user_corporate_notactive", user_corporate_notactive);
//                                                    data.put("key", key);
//                                                    data.put("no_hp", dataUserCorporate.getNo_hp());
//                                                } catch (Exception e) {
//                                                    logger.error("Path: " + request.getServletPath() + " Username: "
//                                                            + username + " Error: " + e);
//                                                }
//                                            } else {
//                                                // Error list polis pada username tersebut kosong
//                                                error = true;
//                                                message = "List policy is empty";
//                                                resultErr = "List Polis Kosong";
//                                                logger.error("Path: " + request.getServletPath() + " Username: "
//                                                        + username + " Error: " + resultErr);
//                                            }
//                                        } else {
//                                            // Error password yang dimasukkan user salah
//                                            error = true;
//                                            message = "Login failed";
//                                            resultErr = "Password yang dimasukkan salah";
//                                            logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                                    + " Error: " + resultErr);
//                                        }
//                                    } else {
//                                        // Error password kosong
//                                        error = true;
//                                        message = "Login failed";
//                                        resultErr = "Format password incorect";
//                                        logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                                + " Error: " + resultErr);
//                                    }
//                                } else {
//                                    // Error perbedaan lama waktu token belum lebih dari 15 menit
//                                    error = true;
//                                    message = "Session still active";
//                                    resultErr = "Account sedang login ditempat lain atau ada session yang belum berakhir";
//                                    logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                            + " Error: " + resultErr);
//                                }
//                            }
//                        } else {
//                            // Kondisi token pada database tidak ada bisa kerena logout/ user baru
//                            // Check apabila lebih dari 15 menit, password yang dimasukkan kosong/ ""
//                            if (user1.getPASSWORD() != null || user1.getPASSWORD() != "") {
//                                // Check password yang dimasukkan sama/ tidak dengan yang didb
//                                if (user1.getPASSWORD().equals(password)) {
//                                    ArrayList<UserCorporate> list = services.selectListPolisCorporate(mcl_id_employee);
//                                    // Check apakah username tersebut memiliki list polis atau tidak
//                                    if (list.size() > 0) {
//                                        String today = df.format(new Date());
//                                        lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
//                                        lstUserSimultaneous.setUPDATE_DATE_TIME(today);
//                                        services.updateUserKeyName(lstUserSimultaneous);
//                                        key = user1.getKEY();
//
//                                        error = false;
//                                        message = "Login success";
//                                        data.put("individual", individu);
//                                        data.put("corporate", corporate);
//                                        data.put("hr_user", hr_user);
//                                        data.put("policy_corporate_status", policy_corporate_notinforce);
//                                        data.put("user_corporate_notactive", user_corporate_notactive);
//                                        data.put("key", key);
//                                        data.put("no_hp", dataUserCorporate.getNo_hp());
//                                    } else {
//                                        // Error list polis pada username tersebut kosong
//                                        error = true;
//                                        message = "List policy is empty";
//                                        resultErr = "List Polis Kosong";
//                                        logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                                + " Error: " + resultErr);
//                                    }
//                                } else {
//                                    // Error password yang dimasukkan user salah
//                                    error = true;
//                                    message = "Login failed";
//                                    resultErr = "Password yang dimasukkan salah";
//                                    logger.error("Path: " + request.getServletPath() + " Username: " + username
//                                            + " Error: " + resultErr);
//                                }
//                            } else {
//                                // Error password kosong
//                                error = true;
//                                message = "Login failed";
//                                resultErr = "Format password incorect";
//                                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
//                                        + resultErr);
//                            }
//                        }
//
//                    } else {
//                        // Error username yang dimasukkan tidak ada pada database
//                        error = true;
//                        message = "Login failed";
//                        resultErr = "Username tidak terdaftar";
//                        logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
//                                + resultErr);
//                    }
//                }
//            } else {
//                // Error username yang dimasukkan tidak ada pada database
//                error = true;
//                message = "Login failed";
//                resultErr = "Username tidak terdaftar";
//                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
//            }
//        } catch (Exception e) {
//            error = true;
//            message = ResponseMessage.ERROR_SYSTEM;
//            resultErr = "bad exception " + e;
//            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
//        }
//        map.put("error", error);
//        map.put("message", message);
//        map.put("data", data);
//        res = gson.toJson(map);
//    }
}
