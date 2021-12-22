package com.app.services;

import com.app.exception.HandleSuccessOrNot;
import com.app.model.*;
import com.app.model.request.RequestListPolis;
import com.app.model.request.RequestLogin;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.models.auth.In;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.app.services.RegistrationIndividuImpl.TYPE_INDIVIDU_MRI;

@Service
public class LoginSvcImpl implements LoginSvc {

    private static final Logger logger = LogManager.getLogger(LoginSvcImpl.class);

    private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");

    @Value("${link.fcm.google}")
    private String linkFcmGoogle;

    @Autowired
    private VegaServices services;
    @Autowired
    private VegaCustomResourceLoader customResourceLoader;
    @Autowired
    private RegistrationIndividuSvc registrationIndividuSvc;

    @Override
    public ResponseData login(RequestLogin requestLogin, HttpServletRequest request, boolean easyPin) {
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
                lstUserSimultaneous.setAccount_no_dplk(checkIndividuOrCorporate.getAccount_no_dplk());

                if (isIndividu) {
                    return loginIndividu(isIndividu, isIndividuMri, username, password, data, user1, lstUserSimultaneous,
                            request, key, lastLoginDevice, time_idle, req, easyPin);
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
                            lstUserSimultaneous, request, key, lastLoginDevice, time_idle, checkIndividuOrCorporate.getMCL_ID_EMPLOYEE(), req, easyPin);
                } else if (isHrUser) {
                    return loginHr(password, lstUserSimultaneous, user1, key, data, username, request, responseData, req, easyPin, lastLoginDevice, time_idle);
                } else if (isAccountDplk) {
                    User dplkAccount = services.findByUsernameDplk(username);
                    return loginDplk(dplkAccount, responseData, lstUserSimultaneous, key, user1, data, request, username, req, easyPin, lastLoginDevice, time_idle, password);
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
            if (e.getStackTrace().length > 0){
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
            }
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
        return checkIndividuOrCorporate != null && checkIndividuOrCorporate.getREG_SPAJ() == null && checkIndividuOrCorporate.getMCL_ID_EMPLOYEE() == null &&
                checkIndividuOrCorporate.getEB_HR_USERNAME() != null && checkIndividuOrCorporate.getAccount_no_dplk() == null;
    }

    @Override
    public boolean isAccountDplk(LstUserSimultaneous checkIndividuOrCorporate) {
        return checkIndividuOrCorporate != null && checkIndividuOrCorporate.getREG_SPAJ() == null && checkIndividuOrCorporate.getMCL_ID_EMPLOYEE() == null &&
                checkIndividuOrCorporate.getEB_HR_USERNAME() == null  && checkIndividuOrCorporate.getAccount_no_dplk() != null;
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


    @Override
    public ResponseData switchAccount(RequestListPolis requestLogin, HttpServletRequest request) {
        Gson gson = new Gson();
        String req = gson.toJson(requestLogin);
        String username = requestLogin.getUsername();
        String key = requestLogin.getKey();
        HandleSuccessOrNot handleSuccessOrNot = null;
        HashMap<String, Object> data = new HashMap<>();
        ResponseData responseData = new ResponseData();

        try {
            if (customResourceLoader.validateCredential(username, key)) {
                ArrayList<HashMap<String, Object>> corporate = null;
                ArrayList<HashMap<String, Object>> individu = null;
                ArrayList<HashMap<String, Object>> individuMri = null;
                HashMap<String, Object> dataDplk = new HashMap<>();
                LstUserSimultaneous selectTypeUser = services.selectDataLstUserSimultaneous(username);

                boolean is_individual = isIndividu(selectTypeUser);
                boolean is_corporate = corporate(selectTypeUser) || isIndividuCorporate(selectTypeUser);
                boolean is_dplk = isAccountDplk(selectTypeUser);
                boolean is_individu_mri = registrationIndividuSvc.isIndividuMri(selectTypeUser.getID_SIMULTAN(), selectTypeUser.getUSERNAME());
                Boolean policy_corporate_notinforce = false;

                String mcl_id_employee = selectTypeUser.getMCL_ID_EMPLOYEE();
                String reg_spaj_register = selectTypeUser.getREG_SPAJ();

                // Individual
                ArrayList<User> listPolisIndividu = services.selectDetailedPolis(username);
                ArrayList<PolisMri> listPolisMri = services.getListPolisMri(username);
                if (reg_spaj_register != null){
                    if (!listPolisIndividu.isEmpty()){
                        individu = polisIndividu(listPolisIndividu, request, username);
                        is_individual = true;
                    }
                    if (!listPolisMri.isEmpty()){
                        listPolisMri = (ArrayList<PolisMri>) listPolisMri.stream().filter(v -> v.getType_individu().equals(TYPE_INDIVIDU_MRI)).collect(Collectors.toList());
                        individuMri = polisIndividuMri(listPolisMri);
                        is_individual = true;
                    }
                } else {
                    is_individual = false;
                    individu = null;
                    individuMri = null;
                    is_individu_mri = false;
                }
                ArrayList<UserCorporate> listPolisCorporate = services.selectListPolisCorporate(mcl_id_employee);
                if (!listPolisCorporate.isEmpty()){
                    corporate = polisCorporate(listPolisCorporate, policy_corporate_notinforce);
                    is_corporate = true;
                }
                DPLKAccountModel dplkByAccNo = services.getInfoDplkByAccNo(selectTypeUser.getAccount_no_dplk() != null ? selectTypeUser.getAccount_no_dplk() : null);
                if (dplkByAccNo != null){
                    dataDplk.put("account_no", dplkByAccNo.getNo_peserta());
                    dataDplk.put("participant_name", dplkByAccNo.getNama_peserta());
                    is_dplk = true;
                }

                if ((individu == null) && (corporate == null) && (individuMri == null) && (dataDplk == null)) {
                    data = getMapPolis(policy_corporate_notinforce, is_individu_mri,is_individual,is_corporate,is_dplk,
                            corporate, individu,individuMri, dataDplk);
                    handleSuccessOrNot = new HandleSuccessOrNot(true,  "Can't get data list polis");
                    String resultErr = "Data list polis individu & corporate kosong";
                    logger.error(
                            "Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
                } else {
                    data = getMapPolis(policy_corporate_notinforce, is_individu_mri,is_individual,is_corporate,is_dplk,
                            corporate, individu,individuMri, dataDplk);
                    handleSuccessOrNot = new HandleSuccessOrNot(false, "Successfully get data list polis");
                }
            } else {
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Can't get data list polis");
                String resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
            }
        } catch (Exception e){
            handleSuccessOrNot = new HandleSuccessOrNot(true, ResponseMessage.ERROR_SYSTEM);
            String resultErr = "bad exception " + e;
            if (e.getStackTrace().length > 0){
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
            }
        }

        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);

        // Update activity user table LST_USER_SIMULTANEOUS
        customResourceLoader.updateActivity(username);
        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 9, new Date(), req, responseData.getData().toString(), 1, handleSuccessOrNot.getMessage(), new Date(), username);

        return responseData;
    }

    private ResponseData loginIndividu(boolean isIndividu, boolean isIndividuMri, String username, String password, HashMap<String, Object> data,
                                       LstUserSimultaneous user1, LstUserSimultaneous lstUserSimultaneous,
                                       HttpServletRequest request, String key, String lastLoginDevice, Integer timeIdle, String req, boolean easyPin) {
        ResponseData responseData = new ResponseData();
        HandleSuccessOrNot handleSuccessOrNot = null;
        if (isIndividu) {
            User dataActivityUser = services.selectUserIndividual(username);
            // Check apakah username terdaftar/ tidak
            if (dataActivityUser != null) {
                Integer countDeathClaim = services.selectCountDeathClaim(username);
                if (countDeathClaim == 0) {
                    if (dataActivityUser.getLast_login_device() != null && dataActivityUser.getLast_login_device().equals(lastLoginDevice)) {
                        ArrayList<User> list = services.selectDetailedPolis(username);
                        responseData = cekEasyLoginInvidu(easyPin, list, isIndividuMri, lstUserSimultaneous, key, user1, dataActivityUser, data, username, password, request, responseData, req);
                    } else {
                        ArrayList<User> list = services.selectDetailedPolis(username);
                        // Kondisi dimana token ada tetapi berbeda dengan yang dikirim
                        Date dateActivity = dataActivityUser.getUpdate_date() != null ? dataActivityUser.getUpdate_date() : new Date();
                        Date dateNow = new Date();
                        long diff = dateNow.getTime() - dateActivity.getTime();
                        long diffSeconds = diff / 1000;
                        if (diffSeconds >= timeIdle || dataActivityUser.getLast_login_device() == null) {
                            // Check apabila lebih dari 15 menit, password yang dimasukkan kosong/ ""
                            // Check password yang dimasukkan sama/ tidak dengan yang didb
                            responseData = cekEasyLoginInvidu(easyPin, list, isIndividuMri, lstUserSimultaneous, key, user1, dataActivityUser, data, username, password, request, responseData, req);
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
        responseData.setError(handleSuccessOrNot != null ? handleSuccessOrNot.isError() : responseData.getError());
        responseData.setMessage(handleSuccessOrNot != null ? handleSuccessOrNot.getMessage() : responseData.getMessage());
        responseData.setData(data);

        return responseData;
    }

    private ResponseData loginCorporate(boolean individuCorporate, boolean policy_corporate_notinforce, boolean user_corporate_notactive, String username, String password, HashMap<String, Object> data,
                                        LstUserSimultaneous user1, LstUserSimultaneous lstUserSimultaneous,
                                        HttpServletRequest request, String key, String lastLoginDevice, Integer timeIdle,
                                        String mcl_id_employee, String req, boolean easyPin) {
        ResponseData responseData = new ResponseData();
        HandleSuccessOrNot handleSuccessOrNot = null;
        UserCorporate dataUserCorporate = services.selectUserCorporate(username);
        // Check apakah username terdaftar/ tidak
        if (dataUserCorporate != null) {
            if (dataUserCorporate.getLast_login_device() != null && dataUserCorporate.getLast_login_device().equals(lastLoginDevice)) {
                ArrayList<UserCorporate> list = services.selectListPolisCorporate(mcl_id_employee);
                responseData = cekLoginCorporate(easyPin, list, lstUserSimultaneous, user1, key, individuCorporate, policy_corporate_notinforce, user_corporate_notactive, dataUserCorporate.getNo_hp(), data,
                        password, username, request, responseData, req);
            } else {
                ArrayList<UserCorporate> list = services.selectListPolisCorporate(mcl_id_employee);
                Date dateActivity = dataUserCorporate.getUpdate_date() != null ? dataUserCorporate.getUpdate_date() : new Date();
                Date dateNow = new Date();
                long diff = dateNow.getTime() - dateActivity.getTime();
                long diffSeconds = diff / 1000;
                // Check apakah lama perbedaan waktu token sudah melebihi 15 menit/ belum
                if (diffSeconds >= timeIdle || dataUserCorporate.getLast_login_device() == null) {
                    responseData = cekLoginCorporate(easyPin, list, lstUserSimultaneous, user1, key, individuCorporate, policy_corporate_notinforce, user_corporate_notactive, dataUserCorporate.getNo_hp(), data,
                            password, username, request, responseData, req);
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
        responseData.setError(handleSuccessOrNot != null ? handleSuccessOrNot.isError() : responseData.getError());
        responseData.setMessage(handleSuccessOrNot != null ? handleSuccessOrNot.getMessage() : responseData.getMessage());
        responseData.setData(data);
        return responseData;
    }

    private ResponseData loginDplk(User dplkAccount, ResponseData responseData, LstUserSimultaneous lstUserSimultaneous, String key,
                                   LstUserSimultaneous user1, HashMap<String, Object> data, HttpServletRequest request,
                                   String username, String req, boolean easyPin, String lastLoginDevice, Integer timeIdle, String password) {
        HandleSuccessOrNot handleSuccessOrNot = null;
        if (user1.getLAST_LOGIN_DEVICE() != null && user1.getLAST_LOGIN_DEVICE().equals(lastLoginDevice)) {
            responseData = cekLoginEasyPinDplk(easyPin, lstUserSimultaneous, key, user1, password, username, dplkAccount, request, responseData, data, req);
        } else {
            Date dateActivity = dplkAccount.getUpdate_date() != null ? dplkAccount.getUpdate_date() : new Date();
            Date dateNow = new Date();
            long diff = dateNow.getTime() - dateActivity.getTime();
            long diffSeconds = diff / 1000;
            // Check apakah lama perbedaan waktu token sudah melebihi 15 menit/ belum
            if (diffSeconds >= timeIdle || user1.getLAST_LOGIN_DEVICE() == null) {
                responseData = cekLoginEasyPinDplk(easyPin, lstUserSimultaneous, key, user1, password, username, dplkAccount, request, responseData, data, req);
            } else {
                // Error perbedaan lama waktu token belum lebih dari 15 menit
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Session still active");
                String resultErr = "Account sedang login ditempat lain atau ada session yang belum berakhir";
                logger.error("Path: " + request.getServletPath() + " Username: " + username
                        + " Error: " + resultErr);
            }
        }
        responseData.setError(handleSuccessOrNot != null ? handleSuccessOrNot.isError() : responseData.getError());
        responseData.setMessage(handleSuccessOrNot != null ? handleSuccessOrNot.getMessage() : responseData.getMessage());
        responseData.setData(data);
        return responseData;
    }

    private ResponseData loginHr(String password, LstUserSimultaneous lstUserSimultaneous, LstUserSimultaneous user1, String key, HashMap<String, Object> data, String username,
                                 HttpServletRequest request, ResponseData responseData, String req, boolean easyPin, String lastLoginDevice, Integer timeIdle) {
        HandleSuccessOrNot handleSuccessOrNot = null;
        if (user1.getLAST_LOGIN_DEVICE() != null && user1.getLAST_LOGIN_DEVICE().equals(lastLoginDevice)) {
            responseData = cekLoginEasyPinHr(easyPin, lstUserSimultaneous, user1, password, key, username, data, request, responseData, req);
        } else {
            Date dateActivity = user1.getUPDATE_DATE() != null ? user1.getUPDATE_DATE() : new Date();
            Date dateNow = new Date();
            long diff = dateNow.getTime() - dateActivity.getTime();
            long diffSeconds = diff / 1000;
            // Check apakah lama perbedaan waktu token sudah melebihi 15 menit/ belum
            if (diffSeconds >= timeIdle || user1.getLAST_LOGIN_DEVICE() == null) {
                responseData = cekLoginEasyPinHr(easyPin, lstUserSimultaneous, user1, password, key, username, data, request, responseData, req);
            } else {
                // Error perbedaan lama waktu token belum lebih dari 15 menit
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Session still active");
                String resultErr = "Account sedang login ditempat lain atau ada session yang belum berakhir";
                logger.error("Path: " + request.getServletPath() + " Username: " + username
                        + " Error: " + resultErr);
            }
        }
        responseData.setError(handleSuccessOrNot != null ? handleSuccessOrNot.isError() : responseData.getError());
        responseData.setMessage(handleSuccessOrNot != null ? handleSuccessOrNot.getMessage() : responseData.getMessage());
        responseData.setData(data);

        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 1, new Date(), req, responseData.toString(), 1, null, new Date(), username);
        return responseData;
    }

    private ResponseData cekEasyLoginInvidu(boolean easyPin, ArrayList<User> list, boolean isIndividuMri, LstUserSimultaneous lstUserSimultaneous, String key, LstUserSimultaneous user1, User dataActivityUser,
                                            HashMap<String, Object> data, String username, String password, HttpServletRequest request, ResponseData responseData, String req) {
        HandleSuccessOrNot handleSuccessOrNot;
        boolean account_dplk  = false;
        DPLKAccountModel dplkByAccNo = services.getInfoDplkByAccNo(lstUserSimultaneous.getAccount_no_dplk() != null ? lstUserSimultaneous.getAccount_no_dplk() : null);
        if (dplkByAccNo != null){
            account_dplk = true;
        }
        if (easyPin) {
            // Check apakah username tersebut punya list polis/ tidak
            if (list.size() > 0 || isIndividuMri) {
                String today = df.format(new Date());
                lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
                lstUserSimultaneous.setUPDATE_DATE_TIME(today);
                services.updateUserKeyName(lstUserSimultaneous);
                key = user1.getKEY();

                data = getMapObjc(true, false, false, isIndividuMri, false,
                        false, account_dplk, key, dataActivityUser.getNo_hp() != null ? dataActivityUser.getNo_hp()
                                : dataActivityUser.getNo_hp2(), data);
                handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
            } else {
                // Error list polis kosong
                String resultErr = "List Polis Kosong";
                handleSuccessOrNot = new HandleSuccessOrNot(true, "List policy is empty");
                logger.error("Path: " + request.getServletPath() + " Username: " + username
                        + " Error: " + resultErr);
            }
            customResourceLoader.insertHistActivityWS(12, 40, new Date(), req, responseData.toString(), 1, handleSuccessOrNot.getMessage(), new Date(), username);
        } else {
            if (user1.getPASSWORD().equals(password)) {
                // Check apakah username tersebut punya list polis/ tidak
                if (list.size() > 0 || isIndividuMri) {
                    String today = df.format(new Date());
                    lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
                    lstUserSimultaneous.setUPDATE_DATE_TIME(today);
                    services.updateUserKeyName(lstUserSimultaneous);
                    key = user1.getKEY();

                    data = getMapObjc(true, false, false, isIndividuMri, false,
                            false, account_dplk, key, dataActivityUser.getNo_hp() != null ? dataActivityUser.getNo_hp()
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

            // Insert Log LST_HIST_ACTIVITY_WS
            customResourceLoader.insertHistActivityWS(12, 1, new Date(), req, responseData.toString(), 1, handleSuccessOrNot.getMessage(), new Date(), username);
        }
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);
        return responseData;
    }

    private ResponseData cekLoginCorporate(boolean easyPin, ArrayList<UserCorporate> list, LstUserSimultaneous lstUserSimultaneous, LstUserSimultaneous user1, String key, boolean individuCorporate,
                                           boolean policy_corporate_notinforce, boolean user_corporate_notactive, String no_hp, HashMap<String, Object> data, String password, String username,
                                           HttpServletRequest request, ResponseData responseData, String req) {
        HandleSuccessOrNot handleSuccessOrNot;
        boolean account_dplk  = false;
        DPLKAccountModel dplkByAccNo = services.getInfoDplkByAccNo(lstUserSimultaneous.getAccount_no_dplk() != null ? lstUserSimultaneous.getAccount_no_dplk() : null);
        if (dplkByAccNo != null){
            account_dplk = true;
        }
        if (easyPin) {
            if (list.size() > 0) {
                String today = df.format(new Date());
                lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
                lstUserSimultaneous.setUPDATE_DATE_TIME(today);
                services.updateUserKeyName(lstUserSimultaneous);
                key = user1.getKEY();
                data = getMapObjc(individuCorporate, true, false, false, policy_corporate_notinforce,
                        user_corporate_notactive, account_dplk, key, no_hp, data);
                handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
            } else {
                // Error list polis kosong
                handleSuccessOrNot = new HandleSuccessOrNot(true, "List policy is empty");
                String resultErr = "List Polis Kosong";
                logger.error("Path: " + request.getServletPath() + " Username: " + username
                        + " Error: " + resultErr);
            }
            customResourceLoader.insertHistActivityWS(12, 40, new Date(), req, responseData.toString(), 1, handleSuccessOrNot.getMessage(), new Date(), username);
        } else {
            if (user1.getPASSWORD().equals(password)) {
                // Check apakah username tersebut punya list polis/ tidak
                if (list.size() > 0) {
                    String today = df.format(new Date());
                    lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
                    lstUserSimultaneous.setUPDATE_DATE_TIME(today);
                    services.updateUserKeyName(lstUserSimultaneous);
                    key = user1.getKEY();
                    data = getMapObjc(individuCorporate, true, false, false, policy_corporate_notinforce,
                            user_corporate_notactive, account_dplk, key, no_hp, data);
                    handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");

                    // Insert Log LST_HIST_ACTIVITY_WS
                    customResourceLoader.insertHistActivityWS(12, 1, new Date(), req, responseData.toString(), 1, null, new Date(), username);
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

            // Insert Log LST_HIST_ACTIVITY_WS
            customResourceLoader.insertHistActivityWS(12, 1, new Date(), req, responseData.toString(), 1, handleSuccessOrNot.getMessage(), new Date(), username);
        }
        responseData.setError(handleSuccessOrNot != null ? handleSuccessOrNot.isError() :responseData.getError());
        responseData.setMessage(handleSuccessOrNot != null ? handleSuccessOrNot.getMessage() : responseData.getMessage());
        responseData.setData(data);
        return responseData;
    }

    private ResponseData cekLoginEasyPinHr(boolean easyPin, LstUserSimultaneous lstUserSimultaneous, LstUserSimultaneous user1, String password, String key,
                                           String username, HashMap<String, Object> data, HttpServletRequest request, ResponseData responseData, String req) {
        HandleSuccessOrNot handleSuccessOrNot;
        if (easyPin) {
            String today = df.format(new Date());
            lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
            lstUserSimultaneous.setUPDATE_DATE_TIME(today);
            services.updateUserKeyName(lstUserSimultaneous);
            key = user1.getKEY();
            data = getMapObjc(false, false, true, false, false,
                    false, false, key, null, data);
            handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
            customResourceLoader.insertHistActivityWS(12, 40, new Date(), req, responseData.toString(), 1, handleSuccessOrNot.getMessage(), new Date(), username);
        } else {
            if (user1.getPASSWORD().equals(password)) {
                String today = df.format(new Date());
                lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
                lstUserSimultaneous.setUPDATE_DATE_TIME(today);
                services.updateUserKeyName(lstUserSimultaneous);
                key = user1.getKEY();
                data = getMapObjc(false, false, true, false, false,
                        false, false, key, null, data);
                handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
            } else {
                // Error username yang dimasukkan tidak ada pada database
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
                String resultErr = "Username tidak terdaftar";
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                        + resultErr);
            }
            customResourceLoader.insertHistActivityWS(12, 1, new Date(), req, responseData.toString(), 1, handleSuccessOrNot.getMessage(), new Date(), username);
        }
        responseData.setError(handleSuccessOrNot != null ? handleSuccessOrNot.isError() : responseData.getError());
        responseData.setMessage(handleSuccessOrNot != null ? handleSuccessOrNot.getMessage() : responseData.getMessage());
        responseData.setData(data);
        return responseData;
    }

    private ResponseData cekLoginEasyPinDplk(boolean easyPin, LstUserSimultaneous lstUserSimultaneous, String key, LstUserSimultaneous user1, String password,
                                             String username, User dplkAccount, HttpServletRequest request, ResponseData responseData, HashMap<String, Object> data, String req) {
        HandleSuccessOrNot handleSuccessOrNot;
        if (easyPin) {
            String today = df.format(new Date());
            lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
            lstUserSimultaneous.setUPDATE_DATE_TIME(today);
            services.updateUserKeyName(lstUserSimultaneous);
            key = user1.getKEY();
            data = getMapObjc(false, false, false, false, false,
                    false, true, key, dplkAccount.getNo_hp(), data);
            handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
            customResourceLoader.insertHistActivityWS(12, 40, new Date(), req, responseData.toString(), 1, handleSuccessOrNot.getMessage(), new Date(), username);
        } else {
            if (user1.getPASSWORD().equals(password)) {
                if (dplkAccount != null) {
                    String today = df.format(new Date());
                    lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
                    lstUserSimultaneous.setUPDATE_DATE_TIME(today);
                    services.updateUserKeyName(lstUserSimultaneous);
                    key = user1.getKEY();
                    data = getMapObjc(false, false, false, false, false,
                            false, true, key, dplkAccount.getNo_hp(), data);
                    handleSuccessOrNot = new HandleSuccessOrNot(false, "Login success");
                } else {
                    // Error username yang dimasukkan tidak ada pada database
                    handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
                    String resultErr = "Username tidak terdaftar";
                    logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                            + resultErr);
                }
            } else {
                // Error username yang dimasukkan tidak ada pada database
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
                String resultErr = "Username tidak terdaftar";
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                        + resultErr);
            }
            customResourceLoader.insertHistActivityWS(12, 1, new Date(), req, responseData.toString(), 1, handleSuccessOrNot.getMessage(), new Date(), username);
        }
        responseData.setError(handleSuccessOrNot != null ? handleSuccessOrNot.isError() : responseData.getError());
        responseData.setMessage(handleSuccessOrNot != null ? handleSuccessOrNot.getMessage() : responseData.getMessage());
        responseData.setData(data);
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

    private ArrayList<HashMap<String, Object>> polisIndividu(ArrayList<User> listPolisIndividu,
                                                             HttpServletRequest request, String username){
        ArrayList<HashMap<String, Object>> individu = new ArrayList<>();
        for (Integer i = 0; i < listPolisIndividu.size(); i++) {
            try {
                HashMap<String, Object> mapper = new HashMap<>();
                User dataTemp = listPolisIndividu.get(i);
                String polis = dataTemp.getMspo_policy_no_format() != null
                        ? dataTemp.getMspo_policy_no_format()
                        : null;
                BigDecimal gproudId = dataTemp.getGprod_id() != null ? dataTemp.getGprod_id() : null;
                BigDecimal isHealth = dataTemp.getIshealth() != null ? dataTemp.getIshealth() : null;
                if (polis != null) {
                    if (isHealth.intValue() != 0) {
                        mapper.put("enable_claim_menu", true);
                    } else {
                        mapper.put("enable_claim_menu", false);
                    }

                    if (gproudId.intValue() == 4) {
                        mapper.put("enable_topup_menu", true);
                    } else {
                        mapper.put("enable_topup_menu", false);
                    }

                    String enable_cuti_premi = dataTemp.getEnable_cuti_premi();

                    mapper.put("policy_number", polis);
                    mapper.put("policy_holder",
                            dataTemp.getNm_pp() != null ? dataTemp.getNm_pp() : null);
                    mapper.put("insured", dataTemp.getNm_tt() != null ? dataTemp.getNm_tt() : null);
                    mapper.put("policy_status_label",
                            dataTemp.getStatus() != null ? dataTemp.getStatus() : null);
                    mapper.put("policy_status_id",
                            dataTemp.getLms_id() != null ? dataTemp.getLms_id() : null);
                    mapper.put("policy_type", gproudId.intValue());
                    if(enable_cuti_premi.equalsIgnoreCase("true")) {
                        mapper.put("enable_cuti_premi", true);
                    } else {
                        mapper.put("enable_cuti_premi", false);
                    }

                    mapper.put("account_type", "individual");
                    mapper.put("reg_spaj", dataTemp.getReg_spaj());
                    mapper.put("tipe_polis", dataTemp.getTipe_polis());
                }

                individu.add(mapper);
            } catch (Exception e) {
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                        + e);
            }
        }
        return individu;
    }

    private ArrayList<HashMap<String, Object>> polisIndividuMri (ArrayList<PolisMri> listPolisMri){
        ArrayList<HashMap<String, Object>> individuMri = new ArrayList<>();
        for (PolisMri polis : listPolisMri){
            HashMap<String, Object> mapper = new HashMap<>();
            String no_polis = polis.getMspo_policy_no_format() != null
                    ? polis.getMspo_policy_no_format()
                    : null;
            mapper.put("policy_number", no_polis);
            mapper.put("policy_holder",
                    polis.getNm_pp() != null ? polis.getNm_pp() : null);
            mapper.put("insured", polis.getNm_tt() != null ? polis.getNm_tt() : null);
            mapper.put("policy_status_label",
                    polis.getStatus() != null ? polis.getStatus() : null);
            individuMri.add(mapper);
        }
        return individuMri;
    }

    private ArrayList<HashMap<String, Object>> polisCorporate(ArrayList<UserCorporate> listPolisCorporate,
                                                              boolean policy_corporate_notinforce){
        ArrayList<HashMap<String, Object>> corporate = new ArrayList<>();

        // Add no polis dalam satu list
        List<String> listNoPolis = new ArrayList<String>();
        for (int i = 0; i < listPolisCorporate.size(); i++) {
            String distinctNoPolis = listPolisCorporate.get(i).getNo_polis();
            listNoPolis.add(distinctNoPolis);
        }

        // Add masa pertanggungan pada satu list
        List<Date> listBegDate = new ArrayList<Date>();
        for (int i = 0; i < listPolisCorporate.size(); i++) {
            Date distinctBegDate = listPolisCorporate.get(i).getMspo_beg_date();
            if (distinctBegDate != null) {
                listBegDate.add(distinctBegDate);
            }
        }

        // Add mspo type rek
        List<BigDecimal> listMspoTypeRek = new ArrayList<BigDecimal>();
        for (int i = 0; i < listPolisCorporate.size(); i++) {
            BigDecimal distinctMspoTypeRek = listPolisCorporate.get(i).getMspo_type_rek();
            listMspoTypeRek.add(distinctMspoTypeRek);
        }

        // Distinct no polis, masa pertanggungan, mspo_type_rek biar gak dobel2
        List<String> distinctNoPolis = listNoPolis.stream().distinct().collect(Collectors.toList());
        List<Date> distinctBegDate = listBegDate.stream().distinct().collect(Collectors.toList());
        List<BigDecimal> distinctMspoTypeRek = listMspoTypeRek.stream().collect(Collectors.toList());

        // Check polis corporate yang keluar ada 1 atau 2 dilihat dari masa berlaku
        int z = 2;
        if (distinctBegDate.size() > 1) {
            LocalDate date = LocalDate.parse(df1.format(distinctBegDate.get(0)));
            LocalDate datePlus = date.plusMonths(3);

            LocalDate sysdate = LocalDate.now();
            if (datePlus.compareTo(sysdate) < 0) {
                z = 1;
            } else {
                z = 2;
            }
        } else {
            z = 1;
        }

        // Get data dari polis2 yang sudah di distinct
        for (int x = 0; x < z; x++) {
            HashMap<String, Object> dataTemp = new HashMap<>();
            String no_polis = distinctNoPolis.get(x);
            Date masa_pertanggungan = distinctBegDate.get(x);
            BigDecimal mspo_type_rek = distinctMspoTypeRek.get(x);

            // Check no polis terakhir masih berlaku atau tidak
            if (x == 0) {
                UserCorporate dataEndDate = services
                        .selectBegDateEndDateCorporate(customResourceLoader.clearData(no_polis));
                Date endDate = dataEndDate.getMspo_end_date();
                LocalDate now = LocalDate.now();
                LocalDate endDateParse = LocalDate.parse(df1.format(endDate));
                if (endDateParse.compareTo(now) < 0) {
                    policy_corporate_notinforce = true;
                } else {
                    policy_corporate_notinforce = false;
                }
            }

            dataTemp.put("no_polis", no_polis);
            dataTemp.put("masa_pertanggungan",
                    masa_pertanggungan != null ? df2.format(masa_pertanggungan) : null);
            dataTemp.put("enable_claim_corporate", mspo_type_rek.intValue() == 2 ? true : false);

            ArrayList<HashMap<String, Object>> detailsPolis = new ArrayList<>();
            for (int y = 0; y < listPolisCorporate.size(); y++) {
                if (listPolisCorporate.get(y).getNo_polis().equals(distinctNoPolis.get(x))) {
                    HashMap<String, Object> dataDetailsPolis = new HashMap<>();
                    String reg_spaj = listPolisCorporate.get(y).getReg_spaj();
                    String mcl_first = listPolisCorporate.get(y).getMcl_first();
                    String mste_insured = listPolisCorporate.get(y).getMste_insured();
                    BigDecimal lsre_id = listPolisCorporate.get(y).getLsre_id();

                    dataDetailsPolis.put("reg_spaj", reg_spaj);
                    dataDetailsPolis.put("mcl_first", mcl_first);
                    dataDetailsPolis.put("mste_insured", mste_insured);
                    dataDetailsPolis.put("lsre_id", lsre_id.intValue());
                    dataDetailsPolis.put("account_type", "corporate");

                    detailsPolis.add(dataDetailsPolis);
                }
            }

            dataTemp.put("details", detailsPolis);
            corporate.add(dataTemp);
        }
        return corporate;
    }

    private HashMap<String, Object> getMapPolis(boolean policy_corporate_notinforce, boolean is_individu_mri, boolean is_individual, boolean is_corporate, boolean is_dplk,
                                                ArrayList<HashMap<String, Object>> corporate, ArrayList<HashMap<String, Object>> individu, ArrayList<HashMap<String, Object>> individuMri,
                                                HashMap<String, Object> data_dplk){
        HashMap<String, Object> data = new HashMap<>();
        data.put("corporate", corporate != null && !corporate.isEmpty() ? corporate : null);
        data.put("individual", individu != null && !individu.isEmpty() ? individu : null);
        data.put("data_mri", individuMri != null && !individuMri.isEmpty() ? individuMri : null);
        data.put("data_dplk", data_dplk != null ? data_dplk : null);
        data.put("individu_mri", is_individu_mri);
        data.put("is_individual", is_individual);
        data.put("is_corporate", is_corporate);
        data.put("is_dplk", is_dplk);
        data.put("policy_corporate_notinforce", policy_corporate_notinforce);
        return data;
    }
}
