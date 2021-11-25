package com.app.services;

import com.app.feignclient.ServiceOTP;
import com.app.model.LstUserSimultaneous;
import com.app.model.Pemegang;
import com.app.model.ResponseData;
import com.app.model.User;
import com.app.model.request.RequestFindAccount;
import com.app.model.request.RequestRegisterQR;
import com.app.model.request.RequestSendOTP;
import com.app.model.request.RequestValidatePolicy;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RegistrationIndividuImpl implements RegistrationIndividuSvc{

    private static final Logger logger = LogManager.getLogger(RegistrationIndividuImpl.class);

    private final DateFormat df2 = new SimpleDateFormat("ddMMyyyy");
    public static final String TYPE_INDIVIDU_MRI = "INDIVIDU_MRI";

    private final VegaServices services;
    private final ServiceOTP serviceOTP;
    private final VegaCustomResourceLoader customResourceLoader;

    @Autowired
    public RegistrationIndividuImpl(VegaServices services, ServiceOTP serviceOTP,
                                    VegaCustomResourceLoader customResourceLoader) {
        this.services = services;
        this.serviceOTP = serviceOTP;
        this.customResourceLoader = customResourceLoader;
    }

    @Override
    public String findAccount(RequestFindAccount requestFindAccount, HttpServletRequest request) throws ParseException {
        Date start = new Date();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        new Gson();
        Gson gson;
        gson = builder.create();
        String req = gson.toJson(requestFindAccount);
        String res;
        String resultErr = null;
        String message;
        boolean error = true;
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();

        String ktp_or_nopolis = requestFindAccount.getKtp_or_nopolis();
        Date dob = df2.parse(requestFindAccount.getDob());
        try {
            Pemegang pemegang = new Pemegang();
            pemegang.setMspo_policy_no(ktp_or_nopolis);

            // Check menggunakan No Polis
            List<Pemegang> cekPemegang = services.filterIndividuAndMri(ktp_or_nopolis, ktp_or_nopolis);
            Optional<Pemegang> optPemegang = cekPemegang.stream().filter(v -> v.getType_individu() != null).findFirst();
            Integer lstb_id = null;
            Integer mssm_pemegang = null;
            String type_individu = null;
            if (optPemegang.isPresent()){
                lstb_id = optPemegang.get().getLstb_id();
                mssm_pemegang = optPemegang.get().getMssm_pemegang();
                type_individu = optPemegang.get().getType_individu();
            }
            pemegang.setLstb_id(lstb_id);
            pemegang.setMssm_pemegang(mssm_pemegang);
            pemegang = services.selectNomorPolisNotRegister(pemegang);
            // Check data inputan No. Polis/ KTP & DOB ada gak didatabse
            if (pemegang != null && pemegang.getMspe_date_birth().equals(dob)) {
                pemegang.setType_individu(type_individu);
                boolean checkSimultanPolis = services.selectCountIdSimultanByIdSimultan(pemegang.getId_simultan());
                // Check ID Simultan apakah sudah pernah daftar pada M-Polis
                if (!checkSimultanPolis) {
                    String no_hp_polis;
                    if (pemegang.getType_individu().equals(TYPE_INDIVIDU_MRI)){
                        no_hp_polis = pemegang.getMsap_phone1() != null ? pemegang.getMsap_phone1() : pemegang.getMsap_phone2();
                    } else {
                        no_hp_polis = pemegang.getNo_hp() != null ? pemegang.getNo_hp() : pemegang.getNo_hp2();
                    }
                    // Check No Telepon apakah kosong atau kurang dari 6 digit
                    if (no_hp_polis != null && no_hp_polis.length() > 6) {
                        Integer checkDeathClaim = services.selectCheckDeathClaim(pemegang.getId_simultan());
                        // Check apakah pemegang mempunyai polis death claim
                        if (checkDeathClaim == 0) {
                            // Check apakah no handphone sudah pernah terdaftar atau belum
                            Integer checkPhoneNumber = services.selectCountPhoneNumber(no_hp_polis);
                            if (checkPhoneNumber == 0) {
                                // Send OTP
								/*String result = customResourceLoader.sendOTP(91, 1, no_hp_polis, pemegang.getReg_spaj(),
										pemegang.getMspo_policy_no());*/
                                RequestSendOTP requestSendOTP = new RequestSendOTP();
                                requestSendOTP.setJenis_id(91);
                                requestSendOTP.setMenu_id(1);
                                requestSendOTP.setUsername(no_hp_polis);
                                requestSendOTP.setNo_polis(pemegang.getMspo_policy_no());
                                requestSendOTP.setReg_spaj(pemegang.getReg_spaj());
                                ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);

                                Boolean errorPost = responseSendOTP.getError();
                                String messagePost = responseSendOTP.getMessage();

                                // Check kondisi yang didapatkan setelah HIT API OTP
                                if (!errorPost) {
                                    error = false;
                                    message = "ktp/policy found in database";
                                    data.put("phone_no", no_hp_polis);
                                    data.put("no_polis", pemegang.getMspo_policy_no());
                                } else {
                                    message = "Phone number is blacklisted";
                                    data.put("phone_no", no_hp_polis);
                                    data.put("no_polis", pemegang.getMspo_policy_no());
                                    resultErr = messagePost;
                                    logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: "
                                            + ktp_or_nopolis + " Error: " + resultErr);
                                }
                            } else {
                                // Error no handphone sudah digunakan polis lain
                                message = "The mobile number has already been used on another policy";
                                data.put("phone_no", no_hp_polis);
                                data.put("no_polis", pemegang.getMspo_policy_no());
                                resultErr = "Nomor telepon sudah pernah digunakan pada polis lain (" + no_hp_polis
                                        + ")";
                                logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
                                        + " Error: " + resultErr);
                            }
                        } else {
                            // Error apabila ada salah satu polisnya mengandung death claim
                            message = "Policy number exists containing death claims";
                            data.put("phone_no", no_hp_polis);
                            data.put("no_polis", pemegang.getMspo_policy_no());
                            resultErr = "No polis ada yang mengandung death claim";
                            logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
                                    + " Error: " + resultErr);
                        }
                    } else {
                        // Error no handphone kosong/ tidak sesuai format
                        message = "Phone number is not valid";
                        data.put("phone_no", null);
                        data.put("no_polis", null);
                        resultErr = "Nomor telepon tidak ada/ format nomor telepon salah";
                        logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
                                + " Error: " + resultErr);
                    }
                } else {
                    // Error hasil id_simultan ada pada tabel lst_user_simultaneous
                    message = "Already has an account that associated with this policy";
                    data.put("phone_no", null);
                    data.put("no_polis", null);
                    resultErr = "User sudah pernah register menggunakan no polis tersebut/ no polis lain";
                    logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis + " Error: "
                            + resultErr);
                }
            } else {
                Pemegang resultKTP = new Pemegang();
                resultKTP.setLstb_id(lstb_id);
                resultKTP.setMssm_pemegang(mssm_pemegang);
                resultKTP.setType_individu(type_individu);
                // Check menggunakan KTP
                resultKTP = services.selectKtp(resultKTP);
                // Check data inputan No. Polis/ KTP & DOB ada gak didatabse
                if (resultKTP != null && resultKTP.getMspe_date_birth().equals(dob)) {
                    boolean checkSimultanKTP = services.selectCountIdSimultanByIdSimultan(resultKTP.getId_simultan());
                    // Check ID Simultan apakah sudah pernah daftar pada M-Polis
                    if (!checkSimultanKTP) {
                        String no_hp_ktp;
                        if (resultKTP.getType_individu().equals(TYPE_INDIVIDU_MRI)){
                            no_hp_ktp = resultKTP.getMsap_phone1() != null ? resultKTP.getMsap_phone1() : resultKTP.getMsap_phone2();
                        } else {
                            no_hp_ktp = resultKTP.getNo_hp() != null ? resultKTP.getNo_hp() : resultKTP.getNo_hp2();
                        }
                        // Check No Telepon apakah kosong atau kurang dari 6 digit
                        if (no_hp_ktp != null && no_hp_ktp.length() > 6) {
                            Integer checkDeathClaim = services.selectCheckDeathClaim(resultKTP.getId_simultan());
                            // Check apakah pemegang mempunyai polis death claim
                            if (checkDeathClaim == 0) {
                                // Check apakah no handphone sudah pernah terdaftar atau belum
                                Integer checkPhoneNumber = services.selectCountPhoneNumber(no_hp_ktp);
                                if (checkPhoneNumber == 0) {
                                    // Send OTP
									/*String result = customResourceLoader.sendOTP(91, 1, no_hp_ktp,
											resultKTP.getReg_spaj(), resultKTP.getMspo_policy_no());*/

                                    RequestSendOTP requestSendOTP = new RequestSendOTP();
                                    requestSendOTP.setJenis_id(91);
                                    requestSendOTP.setMenu_id(1);
                                    requestSendOTP.setUsername(no_hp_ktp);
                                    requestSendOTP.setNo_polis(resultKTP.getMspo_policy_no());
                                    requestSendOTP.setReg_spaj(resultKTP.getReg_spaj());
                                    ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);

                                    Boolean errorPost = responseSendOTP.getError();
                                    String messagePost = responseSendOTP.getMessage();

                                    // Check kondisi yang didapatkan setelah HIT API OTP
                                    if (!errorPost) {
                                        error = false;
                                        message = "ktp/policy found in database";
                                        data.put("phone_no", no_hp_ktp);
                                        data.put("no_polis", resultKTP.getMspo_policy_no());
                                    } else {
                                        message = "Phone number is blacklisted";
                                        data.put("phone_no", no_hp_ktp);
                                        data.put("no_polis", resultKTP.getMspo_policy_no());
                                        resultErr = messagePost;
                                        logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: "
                                                + ktp_or_nopolis + " Error: " + resultErr);
                                    }
                                } else {
                                    // Error no handphone sudah digunakan polis lain
                                    message = "The mobile number has already been used on another policy";
                                    data.put("phone_no", no_hp_ktp);
                                    data.put("no_polis", resultKTP.getMspo_policy_no());
                                    resultErr = "Nomor telepon sudah pernah digunakan pada polis lain (" + no_hp_ktp
                                            + ")";
                                    logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: "
                                            + ktp_or_nopolis + " Error: " + resultErr);
                                }
                            } else {
                                // Error apabila ada salah satu polisnya mengandung death claim
                                message = "Policy number exists containing death claims";
                                data.put("phone_no", no_hp_ktp);
                                data.put("no_polis", resultKTP.getMspo_policy_no());
                                resultErr = "No polis ada yang mengandung death claim";
                                logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
                                        + " Error: " + resultErr);
                            }
                        } else {
                            // Error no handphone kosong/ tidak sesuai format
                            message = "Phone number is not valid";
                            data.put("phone_no", null);
                            data.put("no_polis", null);
                            resultErr = "Nomor telepon tidak ada/ format nomor telepon salah";
                            logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
                                    + " Error: " + resultErr);
                        }
                    } else {
                        // Error hasil id_simultan ada pada tabel lst_user_simultaneous
                        message = "Already has an account that associated with this policy";
                        data.put("phone_no", null);
                        data.put("no_polis", null);
                        resultErr = "User sudah pernah register menggunakan no polis tersebut/ no polis lain";
                        logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
                                + " Error: " + resultErr);
                    }
                } else {
                    // Error No polis/ KTP & DOB yang diinput tidak ada didatabase
                    message = "ktp/policy not found in database";
                    data.put("phone_no", null);
                    data.put("no_polis", null);
                    resultErr = "ktp/ policy not found in database";
                    logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis + " DOB: "
                            + requestFindAccount.getDob() + " Error: " + resultErr);
                }
            }
        } catch (Exception e) {
            error = true;
            message = ResponseMessage.ERROR_SYSTEM;
            resultErr = "bad exception " + e;
            logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis + " Error: " + e);
        }
        map.put("error", error);
        map.put("message", message);
        map.put("data", data);
        res = gson.toJson(map);
        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 3, new Date(), req, res, 1, resultErr, start, ktp_or_nopolis);

        return res;
    }

    @Override
    public String validatePolicy(RequestValidatePolicy policy, HttpServletRequest request) {
        Date start = new Date();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        new Gson();
        Gson gson;
        gson = builder.create();
        String req = gson.toJson(policy);
        String res;
        String resultErr = null;
        String message;
        boolean error;
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();

        String no_polis = policy.getNo_polis();
        try {
            Pemegang pemegang = new Pemegang();
            pemegang.setMspo_policy_no(no_polis);

            // Check apakah no polis ada/ tidak didatabase
            List<Pemegang> cekPemegang = services.filterIndividuAndMri(no_polis, no_polis);
            Optional<Pemegang> optPemegang = cekPemegang.stream().filter(v -> v.getType_individu() != null).findFirst();
            Integer lstb_id = null;
            Integer mssm_pemegang = null;
            String type_individu = null;
            if (optPemegang.isPresent()){
                lstb_id = optPemegang.get().getLstb_id();
                mssm_pemegang = optPemegang.get().getMssm_pemegang();
                type_individu = optPemegang.get().getType_individu();
            }
            pemegang.setLstb_id(lstb_id);
            pemegang.setMssm_pemegang(mssm_pemegang);
            pemegang = services.selectNomorPolisNotRegister(pemegang);
            if (pemegang != null) {
                pemegang.setType_individu(type_individu);
                String idSimultanNotRegister = pemegang.getId_simultan();
                boolean resultIdSimultan = services.selectCountIdSimultanByIdSimultan(idSimultanNotRegister);
                // Check menggunakan id_simultan apakah ada polis yang sudah terdaftar
                if (!resultIdSimultan) {
                    String no_hp;
                    if (pemegang.getType_individu().equals(TYPE_INDIVIDU_MRI)){
                        no_hp = pemegang.getMsap_phone1() != null ? pemegang.getMsap_phone1() : pemegang.getMsap_phone2();
                    } else {
                        no_hp = pemegang.getNo_hp() != null ? pemegang.getNo_hp() : pemegang.getNo_hp2();
                    }
                    // Check No Telepon apakah kosong atau kurang dari 6 digit
                    if (no_hp != null && no_hp.length() > 6) {
                        Integer checkDeathClaim = services.selectCheckDeathClaim(idSimultanNotRegister);
                        // Check apakah pemegang mempunyai polis death claim
                        if (checkDeathClaim == 0) {
                            // Check apakah no handphone sudah pernah terdaftar atau belum
                            Integer checkPhoneNumber = services.selectCountPhoneNumber(no_hp);
                            if (checkPhoneNumber == 0) {
                                // Send OTP
								/*String result = customResourceLoader.sendOTP(91, 1, no_hp, pemegang.getReg_spaj(),
										no_polis);*/

                                RequestSendOTP requestSendOTP = new RequestSendOTP();
                                requestSendOTP.setJenis_id(91);
                                requestSendOTP.setMenu_id(1);
                                requestSendOTP.setUsername(no_hp);
                                requestSendOTP.setNo_polis(no_polis);
                                requestSendOTP.setReg_spaj(pemegang.getReg_spaj());
                                ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);

                                Boolean errorPost = responseSendOTP.getError();
                                String messagePost = responseSendOTP.getMessage();

                                // Check kondisi yang didapatkan setelah HIT API OTP
                                if (!errorPost) {
                                    error = false;
                                    message = "Policy found in database";
                                    data.put("phone_number", no_hp);
                                } else {
                                    error = true;
                                    message = "Phone number is blacklisted";
                                    data.put("phone_number", no_hp);
                                    resultErr = messagePost;
                                    logger.error("Path: " + request.getServletPath() + ", No. Polis: " + no_polis
                                            + ", Error: " + resultErr);
                                }
                            } else {
                                // Error no handphone sudah digunakan polis lain
                                error = true;
                                message = "The mobile number has already been used on another policy (" + no_hp + ")";
                                data.put("phone_no", no_hp);
                                data.put("no_polis", pemegang.getMspo_policy_no());
                                resultErr = "Nomor telepon sudah pernah digunakan pada polis lain (" + no_hp + ")";
                                logger.error("Path: " + request.getServletPath() + " No. Polis: " + no_polis
                                        + " Error: " + resultErr);
                            }
                        } else {
                            // Error apabila ada salah satu polisnya mengandung death claim
                            error = true;
                            message = "Policy number exists containing death claims";
                            data.put("phone_number", no_hp);
                            resultErr = "No polis ada yang mengandung death claim";
                            logger.error("Path: " + request.getServletPath() + ", No. Polis: " + no_polis + ", Error: "
                                    + resultErr);
                        }
                    } else {
                        // Error no handphone kosong/ tidak sesuai format
                        error = true;
                        message = "Phone number is not valid";
                        data.put("phone_number", null);
                        resultErr = "Nomor telepon tidak ada/ format nomor telepon salah";
                        logger.error("Path: " + request.getServletPath() + ", No. Polis: " + no_polis + ", Error: "
                                + resultErr);
                    }
                } else {
                    // Error hasil id_simultan ada pada tabel lst_user_simultaneous
                    error = true;
                    message = "Already has an account that associated with this policy";
                    data.put("phone_number", null);
                    resultErr = "User sudah pernah daftar menggunakan no polis lain";
                    logger.error(
                            "Path: " + request.getServletPath() + ", No. Polis: " + no_polis + ", Error: " + resultErr);
                }
            } else {
                // Error No polis tidak ditemukan pada database
                error = true;
                message = "Policy not found in database";
                data.put("phone_number", null);
                resultErr = "No polis yang dimasukkan tidak ada didatabase";
                logger.error(
                        "Path: " + request.getServletPath() + ", No. Polis: " + no_polis + ", Error: " + resultErr);
            }
        } catch (Exception e) {
            error = true;
            message = ResponseMessage.ERROR_SYSTEM;
            resultErr = "bad exception " + e;
            logger.error("Path: " + request.getServletPath() + ", No. Polis: " + no_polis + ", Error: " + e);

        }
        map.put("error", error);
        map.put("message", message);
        map.put("data", data);
        res = gson.toJson(map);
        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 4, new Date(), req, res, 1, resultErr, start, no_polis);
        return res;
    }

    @Override
    public String register(RequestRegisterQR requestRegisterQR, HttpServletRequest request) {
        Date start = new Date();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        new Gson();
        Gson gson;
        gson = builder.create();
        String req = gson.toJson(requestRegisterQR);
        String res;
        String resultErr = null;
        String message;
        boolean error;
        HashMap<String, Object> map = new HashMap<>();

        String username = requestRegisterQR.getUsername();
        String no_polis = requestRegisterQR.getNo_polis();
        String password = requestRegisterQR.getPassword();
        try {
            // Cari id_simultan dan REG SPAJ
            Pemegang pemegang = new Pemegang();
            pemegang.setMspo_policy_no(no_polis);
            List<Pemegang> cekPemegang = services.filterIndividuAndMri(no_polis, no_polis);
            Optional<Pemegang> optPemegang = cekPemegang.stream().filter(v -> v.getType_individu() != null).findFirst();
            Integer lstb_id = null;
            Integer mssm_pemegang = null;
            if (optPemegang.isPresent()){
                lstb_id = optPemegang.get().getLstb_id();
                mssm_pemegang = optPemegang.get().getMssm_pemegang();
            }
            pemegang.setLstb_id(lstb_id);
            pemegang.setMssm_pemegang(mssm_pemegang);
            Pemegang pemegang1 = services.selectNomorPolisNotRegister(pemegang);
            if (pemegang1 != null) {
                LstUserSimultaneous lstUserSimultaneous = new LstUserSimultaneous();
                lstUserSimultaneous.setID_SIMULTAN(pemegang1.getId_simultan());

                // Check id_simultan sudah pernah daftar atau belum
                LstUserSimultaneous user1 = services.selectLoginAuthenticate(lstUserSimultaneous);
                if (user1 == null) {
                    Date date = new Date();
                    String strDateFormat = "dd/MM/yyyy HH:mm:ss";
                    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                    String formattedDate = dateFormat.format(date);
                    lstUserSimultaneous.setUSERNAME(username);
                    lstUserSimultaneous.setPASSWORD(password);
                    lstUserSimultaneous.setFLAG_ACTIVE(1);
                    lstUserSimultaneous.setCREATE_DATE(new Date());
                    lstUserSimultaneous.setID_SIMULTAN(pemegang1.getId_simultan());
                    lstUserSimultaneous.setREG_SPAJ(pemegang1.getReg_spaj());
                    lstUserSimultaneous.setDATE_CREATED_JAVA(formattedDate);
                    lstUserSimultaneous.setMCL_ID_EMPLOYEE(null);

                    // Check username sudah terpakai atau belum
                    Integer dataUsername = services.selectCheckUsername(username.toLowerCase());
                    if (dataUsername.equals(0)) {
                        try {
                            // Insert new user
                            services.insertNewuser(lstUserSimultaneous);
                            error = false;
                            message = "Register succes";
                        } catch (Exception e) {
                            // Error username sudah ada didatabase
                            error = true;
                            message = "Username already exist in database. Please choose another username";
                            resultErr = "bad exception " + e;
                            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                                    + resultErr);
                        }
                    } else {
                        // Error username sudah ada didatabase
                        error = true;
                        message = "Username already exist in database. Please choose another username";
                        resultErr = "Username telah digunakan";
                        logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                                + resultErr);
                    }
                } else {
                    // Error No Polis lainnya sudah pernah digunakan untuk mendaftar
                    error = true;
                    message = "No polis already exist in database. Please choose another polis";
                    resultErr = "No. Polis lain sudah terdaftar dengan id simultannya";
                    logger.error(
                            "Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
                }
            } else {
                // Error No Polis sesuai kondisi tidak ada didatabase
                error = true;
                message = "No polis not found. Please choose another polis";
                resultErr = "No. Polis tidak ditemukan dengan kondisi query";
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
            }
        } catch (Exception e) {
            error = true;
            message = ResponseMessage.ERROR_SYSTEM;
            resultErr = "bad exception " + e;
            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
        }
        map.put("error", error);
        map.put("message", message);
        res = gson.toJson(map);
        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 2, new Date(), req, res, 1, resultErr, start, username);
        return res;
    }

    @Override
    public boolean isIndividuMri(String id_simultan, String username) {
        User user = services.selectUserIndividual(username);
        List<Pemegang> lst = services.filterByIdSimultanRegSpajNoPolis(id_simultan, user.getMspo_policy_no(), user.getReg_spaj());
        Optional<Pemegang> optionalPemegang = lst.stream().filter(v -> v.getType_individu() != null).findFirst();
        if (optionalPemegang.isPresent()){
            String typeIndividu = optionalPemegang.get().getType_individu();
            return typeIndividu.equals(TYPE_INDIVIDU_MRI);
        } else {
            return false;
        }
    }

    @Override
    public String esertMri(Path path, HttpServletRequest request, String username) {
        String encode = null;
        try {
            ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
            encode = Base64.getEncoder().encodeToString(byteArrayResource.getByteArray());
        }catch (Exception e){
            logger.error("Path: " + request.getServletPath() + " Username: " +username + " Error: " + e);
        }
        return encode;
    }

    @Override
    public String noHpIndividuAndMri(User dataUserIndividual) {
        String no_hp = "";
        if (dataUserIndividual.getNo_hp() != null || dataUserIndividual.getNo_hp2() != null){
            no_hp = dataUserIndividual.getNo_hp() != null ? dataUserIndividual.getNo_hp() : dataUserIndividual.getNo_hp2();
        } else {
            no_hp = dataUserIndividual.getMsap_phone1() != null ? dataUserIndividual.getMsap_phone1() : dataUserIndividual.getMsap_phone2();
        }
        return no_hp;
    }
}
