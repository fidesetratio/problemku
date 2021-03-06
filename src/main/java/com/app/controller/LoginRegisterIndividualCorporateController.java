package com.app.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.app.model.*;
import com.app.services.LoginSvc;
import com.app.services.RegistrationIndividuSvc;
import com.app.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.services.VegaServices;
import com.app.model.request.RequestClearKey;
import com.app.model.request.RequestForgotPassword;
import com.app.model.request.RequestForgotUsername;
import com.app.model.request.RequestLinkAccount;
import com.app.model.request.RequestLogin;
import com.app.model.request.RequestLoginEasypin;
import com.app.model.request.RequestResendOTP;
import com.app.model.request.RequestSMSOTP;
import com.app.model.request.RequestSendOTP;
import com.app.model.request.RequestUpdatePassword;
import com.app.model.request.RequestValidateOTP;
import com.app.constant.AccountManagementCons;
import com.app.feignclient.ServiceEmail;
import com.app.feignclient.ServiceOTP;
import com.app.utils.CommonUtils;
import com.app.utils.VegaCustomResourceLoader;
import com.app.utils.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class LoginRegisterIndividualCorporateController {

	/* Controller login/ register untuk nasabah Individual & Corporate */

	private static final Logger logger = LogManager.getLogger(LoginRegisterIndividualCorporateController.class);

	private final DateUtils dateUtils;
	private final VegaServices services;
	private final VegaCustomResourceLoader customResourceLoader;
	private final AccountManagementCons constant;
	private final ServiceEmail serviceEmail;
	private final ServiceOTP serviceOTP;
	private final CommonUtils utils;
	private final RegistrationIndividuSvc registrationIndividuSvc;
	private final LoginSvc loginSvc;

	@Value("${link.fcm.google}")
	private String linkFcmGoogle;

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	public LoginRegisterIndividualCorporateController(DateUtils dateUtils,
													  VegaServices services, VegaCustomResourceLoader customResourceLoader,
													  AccountManagementCons constant, ServiceEmail serviceEmail, ServiceOTP serviceOTP,
													  CommonUtils utils, RegistrationIndividuSvc registrationIndividuSvc, LoginSvc loginSvc) {
		this.dateUtils = dateUtils;
		this.services = services;
		this.customResourceLoader = customResourceLoader;
		this.constant = constant;
		this.serviceEmail = serviceEmail;
		this.serviceOTP = serviceOTP;
		this.utils = utils;
		this.registrationIndividuSvc = registrationIndividuSvc;
		this.loginSvc = loginSvc;
	}

	@RequestMapping(value = "/login", produces = "application/json", method = RequestMethod.POST)
	public ResponseData loginNew(@RequestBody RequestLogin requestLogin, HttpServletRequest request) throws Exception {
		return loginSvc.login(requestLogin, request, false);
	}

	@RequestMapping(value = "/logineasypin", produces = "application/json", method = RequestMethod.POST)
	public ResponseData loginEasyPin(@RequestBody RequestLogin requestLogin, HttpServletRequest request)
			throws Exception {
		return loginSvc.login(requestLogin, request, true);
	}

	@RequestMapping(value = "/clearkey", produces = "application/json", method = RequestMethod.POST)
	public String destroyKey(@RequestBody RequestClearKey requestClearKey, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestClearKey);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestClearKey.getUsername();
		String key = requestClearKey.getKey();
		String token = requestClearKey.getToken();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				LstUserSimultaneous dataUser = services.selectDataLstUserSimultaneous(username);

				if (dataUser != null) {
					String last_login_device = dataUser.getLAST_LOGIN_DEVICE();
					if (last_login_device.equals(token)) {
						User paramUpdate = new User();
						paramUpdate.setUsername(username);
						services.updateKeyLoginClear(paramUpdate);

						error = false;
						message = "Success update";
					} else {
						error = false;
						message = "Tidak diupdate karena token berbeda";
					}
				} else {
					error = true;
					message = "Failed update";
					resultErr = "Username tidak ditemukan";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Failed update";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
		map.put("message", message);
		map.put("error", error);
		res = gson.toJson(map);
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 27, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/linkaccount", produces = "application/json", method = RequestMethod.POST)
	public String linkAccount(@RequestBody RequestLinkAccount requestLinkAccount, HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestLinkAccount);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();

		Integer type_register = requestLinkAccount.getType_register();
		String username = requestLinkAccount.getUsername();
		String no_polis = requestLinkAccount.getNo_polis();
		String mcl_id_employee = requestLinkAccount.getMcl_id_employee();
		String account_no_dplk = requestLinkAccount.getAccount_no_dplk();
		try {
			LstUserSimultaneous checkUsername = services.selectDataLstUserSimultaneous(username);

			if (checkUsername == null) {
				// Handle username not registered
				error = true;
				message = "Username not found";
				resultErr = "Username yang dimasukkan tidak terdaftar";
				logger.error("Path: " + request.getServletPath() + ", Username: " + username + ", Error: " + resultErr);
			} else {
				if (type_register.equals(1)) { // Individual
					// Get Reg SPAJ
					Pemegang paramGetSPAJ = new Pemegang();
					paramGetSPAJ.setMspo_policy_no(no_polis);
					Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);
					String reg_spaj = dataSPAJ.getReg_spaj();
					String id_simultan = dataSPAJ.getId_simultan();

					// Update reg_spaj & id_simultan
					services.updateLinkAccount(reg_spaj, id_simultan, null, null, username);

					error = false;
					message = "Successfully link account individual";
				} else if (type_register.equals(2)) { // Corporate
					// Update mcl_id_employee
					services.updateLinkAccount(null, null, mcl_id_employee, null, username);

					error = false;
					message = "Successfully link account corporate";
				} else if (type_register.equals(3)){ // Dplk account
					boolean isExistAccount = services.isExistAccount(account_no_dplk);
					if (isExistAccount){
						error = true;
						message = "account number already registered";
						resultErr = "account number already registered";
						logger.error("Path: " + request.getServletPath() + ", Account number: " + account_no_dplk + ", Error: " + resultErr);
					} else {
						// Update account no dplk
						services.updateLinkAccount(null, null, null, account_no_dplk, username);
						error = false;
						message = "Successfully link account dplk";
					}
				} else {
					error = true;
					message = "Type link account not found";
					resultErr = "Type link account tidak tersedia";
					logger.error("Path: " + request.getServletPath() + ", Username: " + username + ", Type: "
							+ type_register + ", Error: " + resultErr);
				}
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + ", Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 73, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	/**
	 * @description: Ini API untuk Send OTP
	 */
	@RequestMapping(value = "/sendsmsotp", produces = "application/json", method = RequestMethod.POST)
	public String sendOTP(@RequestBody RequestSMSOTP requestSMSOTP, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSMSOTP);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> data = new HashMap<>();
		HashMap<String, Object> map = new HashMap<>();

		String no_hp = requestSMSOTP.getPhone_no();
		String no_polis = requestSMSOTP.getNo_polis();
		Integer menu_id = requestSMSOTP.getMenu_id();
		try {
			String reg_spaj = null;
			String messagePost = null;
			String otpReleasePost = null;
			Boolean errorPost = false;
			HashMap<String, Object> dataJson = null;
			JSONObject myResponseData = null;

			if (no_polis != null) {
				// Find data Reg SPAJ
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataUser = services.selectGetSPAJ(paramGetSPAJ);

				if (dataUser != null) {
					reg_spaj = dataUser.getReg_spaj();
				}
			}

			//result = customResourceLoader.sendOTP(91, menu_id, no_hp, reg_spaj, no_polis);
			
			RequestSendOTP requestSendOTP = new RequestSendOTP();
			requestSendOTP.setJenis_id(91);
			requestSendOTP.setMenu_id(menu_id);
			requestSendOTP.setUsername(no_hp);
			requestSendOTP.setNo_polis(no_polis);
			requestSendOTP.setReg_spaj(reg_spaj);
			ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);
			
			errorPost = (Boolean) responseSendOTP.getError();
			messagePost = (String) responseSendOTP.getMessage();
			dataJson = responseSendOTP.getData();
			myResponseData = new JSONObject(dataJson);

			try {
				otpReleasePost = (String) myResponseData.get("date_blacklist");
			} catch (Exception e) {
				otpReleasePost = null;
			}

			if (errorPost == false) {
				error = false;
				message = messagePost;
				data.put("otp_release", otpReleasePost);
			} else {
				if (messagePost.equalsIgnoreCase("mohon maaf system sedang error")) {
					error = true;
					message = "Error Hit API OTP";
					data.put("otp_release", otpReleasePost);
				} else {
					error = true;
					message = messagePost;
					data.put("otp_release", otpReleasePost);
				}
				error = true;
				message = messagePost;
				data.put("otp_release", otpReleasePost);
				resultErr = messagePost + " (No HP: " + no_hp + " Menu ID: " + menu_id + ")";
				logger.error("Path: " + request.getServletPath() + " Error: " + resultErr);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + ", No. HP: " + no_hp + ", Menu ID: " + menu_id
					+ ", Error: " + e);
		}
		map.put("data", data);
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 47, new Date(), req, res, 1, resultErr, start, no_hp);

		return res;
	}

	/**
	 * @description: Ini API untuk Resend OTP
	 */
	@RequestMapping(value = "/sendotp", produces = "application/json", method = RequestMethod.POST)
	public String resendOTP(@RequestBody RequestSendOTP requestSendOTP, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSendOTP);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> data = new HashMap<>();
		HashMap<String, Object> map = new HashMap<>();

		String no_hp = requestSendOTP.getPhone_no();
		String no_polis = requestSendOTP.getNo_polis();
		Integer menu_id = requestSendOTP.getMenu_id();
		try {
			String reg_spaj = null;
			String messagePost = null;
			String otpReleasePost = null;
			HashMap<String, Object> dataJson = null;
			JSONObject myResponseData = null;
			Boolean errorPost = false;
			Integer attemptSentOTPPost = 0;

			if (no_polis != null) {
				// Find data Reg SPAJ
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataUser = services.selectGetSPAJ(paramGetSPAJ);

				if (dataUser != null) {
					reg_spaj = dataUser.getReg_spaj();
				}
			}

			//result = customResourceLoader.resendOTP(91, menu_id, no_hp, reg_spaj, no_polis);
			
			RequestResendOTP requestResendOTP = new RequestResendOTP();
			requestResendOTP.setJenis_id(91);
			requestResendOTP.setMenu_id(menu_id);
			requestResendOTP.setUsername(no_hp);
			requestResendOTP.setNo_polis(no_polis);
			requestResendOTP.setReg_spaj(reg_spaj);
			ResponseData responseResendOTP = serviceOTP.resendOTP(requestResendOTP);
			
			errorPost = (Boolean) responseResendOTP.getError();
			messagePost = (String) responseResendOTP.getMessage();
			dataJson = responseResendOTP.getData();
			myResponseData = new JSONObject(dataJson);

			try {
				otpReleasePost = (String) myResponseData.get("otp_release");
			} catch (Exception e) {
				otpReleasePost = null;
			}

			if (errorPost == false) {
				error = false;
				message = messagePost;
				data.put("attempt_sent_otp", attemptSentOTPPost);
				data.put("otp_release", otpReleasePost);
			} else {
				error = true;
				message = messagePost;
				data.put("attempt_sent_otp", attemptSentOTPPost);
				data.put("otp_release", otpReleasePost);
				resultErr = messagePost + " (No HP: " + no_hp + " Menu ID: " + menu_id + ")";
				logger.error("Path: " + request.getServletPath() + " Error: " + resultErr);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + ", No. HP: " + no_hp + ", Menu ID: " + menu_id
					+ ", Error: " + e);
		}
		map.put("data", data);
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 21, new Date(), req, res, 1, resultErr, start, no_hp);

		return res;
	}

	@RequestMapping(value = "/validateotp", produces = "application/json", method = RequestMethod.POST)
	public String validateOtp(@RequestBody RequestValidateOTP reqValidateOTP, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(reqValidateOTP);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		
		String username = reqValidateOTP.getPhone_no();
		Integer jenis_id = 91;
		Integer menu_id = reqValidateOTP.getMenu_id();
		Integer otpNumber = reqValidateOTP.getOtp_no();
		String no_hp = username;
		/*
		String no_hp = reqValidateOTP.getPhone_no();
		Integer otp_no = reqValidateOTP.getOtp_no();
		Integer menu_id = reqValidateOTP.getMenu_id();*/
		try {
			MstOTPSimultaneousDet paramSelectDetailData = new MstOTPSimultaneousDet();
			paramSelectDetailData.setJenis_id(jenis_id);
			paramSelectDetailData.setMenu_id(menu_id);

			MstOTPSimultaneous paramSelectData = new MstOTPSimultaneous();
			paramSelectData.setUsername(username);
			paramSelectData.setJenis_id(jenis_id);
			paramSelectData.setMenu_id(menu_id);

			MstOTPSimultaneousDet detailDataOTP = services.selectDetailOTP(paramSelectDetailData);
			if (detailDataOTP == null) {
				error = true;
				message = "Validate OTP Failed";
				data = null;
				resultErr = "Jenis id & menu id tidak terdaftar di database";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			} else {
				MstOTPSimultaneous dataOTP = services.selectDataOTP(paramSelectData);
				if (dataOTP != null) {
					if (dataOTP.getUsername().equalsIgnoreCase(username) && dataOTP.getOtp_no().equals(otpNumber)) {

						Date dateNow1 = customResourceLoader.getDateNow();
						if (!dateNow1.after(dataOTP.getDate_expired())) {
							if (dataOTP.getAttempt() < dataOTP.getMax_attempt()) {
								if (dataOTP.getStatus().equalsIgnoreCase("active")) {
									String formatDateJava = customResourceLoader.getDatetimeJava();
									MstOTPSimultaneous paramUpdateOtpUsed = new MstOTPSimultaneous();
									paramUpdateOtpUsed.setId_otp(dataOTP.getId_otp());
									paramUpdateOtpUsed.setJenis_id(jenis_id);
									paramUpdateOtpUsed.setDate_created_java(formatDateJava);

									services.updateOtpUsed(paramUpdateOtpUsed);
									error = false;
									message = "OTP number is correct";
									data.put("attempt", dataOTP.getAttempt());
								} else {
									// Handle klo status OTP sudah tidak aktif
									error = true;
									message = "OTP number not active";
									data.put("attempt", dataOTP.getAttempt());
									resultErr = "OTP number not active(OTP cocok tetapi status ditable OTP sudah tidak aktif lagi)";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + resultErr);
								}
							} else {
								// Handle klo OTP cocok tapi attempt sudah habis
								error = true;
								message = "OTP close attempt";
								data.put("attempt", dataOTP.getAttempt());
								resultErr = "OTP close attempt(OTP cocok tetapi sudah dicoba sesuai dengan batas attempt)";
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						} else {
							// Handle klo OTP cocok tapi waktu sudah expired
							error = true;
							message = "OTP timeout";
							data.put("attempt", dataOTP.getAttempt());
							resultErr = "OTP timeout(OTP cocok tetapi sudah melebihi batas expired)";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						// Handle klo OTP tidak cocok
						Date dateNow2 = customResourceLoader.getDateNow();
						if (!dateNow2.after(dataOTP.getDate_expired())) {
							if (dataOTP.getAttempt() < dataOTP.getMax_attempt()) {
								String formatDateJava = customResourceLoader.getDatetimeJava();
								MstOTPSimultaneous paramUpdate = new MstOTPSimultaneous();
								paramUpdate.setId_otp(dataOTP.getId_otp());
								paramUpdate.setJenis_id(jenis_id);
								paramUpdate.setDate_created_java(formatDateJava);

								if (dataOTP.getAttempt() == dataOTP.getMax_attempt() - 1) {
									services.updateAttemptOtp(paramUpdate);
									MstOTPSimultaneous dataOTP2 = services.selectDataOTP(paramSelectData);
									services.updateStatusAttemptOtp(paramUpdate);

									error = true;
									message = "OTP number is incorrect";
									data.put("attempt", dataOTP2.getAttempt());
									resultErr = "Username & OTP yang dimasukan tidak sesuai";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + resultErr);
								} else {
									services.updateAttemptOtp(paramUpdate);
									MstOTPSimultaneous dataOTP3 = services.selectDataOTP(paramSelectData);

									error = true;
									message = "OTP number is incorrect";
									data.put("attempt", dataOTP3.getAttempt());
									resultErr = "Username & OTP yang dimasukan tidak sesuai";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + resultErr);
								}
							} else {
								// Handle klo OTP tidak cocok dan attempt sudah habis
								error = true;
								message = "OTP close attempt";
								data.put("attempt", dataOTP.getAttempt());
								resultErr = "OTP close attempt(OTP tidak cocok & sudah dicoba sesuai dengan batas attempt)";
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						} else {
							// Handle klo OTP tidak cocok dan waktu sudah expired
							error = true;
							message = "OTP timeout";
							data.put("attempt", dataOTP.getAttempt());
							resultErr = "OTP timeout(Nomor OTP tidak cocok & sudah melebihi batas expired)";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					}
				} else {
					error = true;
					message = "Phone number is incorrect";
					data = null;
					resultErr = "Phone not registered in OTP(Mungkin OTP belum terkirim/ gagal saat pengiriman)";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			}
			
			
			/*
			Boolean errorPost = false;
			Integer attemptPost = 0;
			HashMap<String, Object> dataJson = null;
			String messagePost = null;
			JSONObject myResponseData = null;
			
			RequestValidateOTP requestValidateOTP = new RequestValidateOTP();
			requestValidateOTP.setJenis_id(91);
			requestValidateOTP.setMenu_id(menu_id);
			requestValidateOTP.setUsername(no_hp);
			requestValidateOTP.setOtp_number(otp_no);
			ResponseData responseValidateOTP = serviceOTP.validateOTP(requestValidateOTP);
			
			errorPost = (Boolean) responseValidateOTP.getError();
			messagePost = (String) responseValidateOTP.getMessage();
			dataJson = responseValidateOTP.getData();
			myResponseData = new JSONObject(dataJson);

			try {
				attemptPost = (Integer) myResponseData.get("attempt");
			} catch (Exception e) {
				attemptPost = null;
			}

			if (errorPost == false) {
				error = false;
				message = messagePost;
				data.put("attempt", attemptPost);
			} else {
				error = true;
				message = messagePost;
				data.put("attempt", attemptPost);
				resultErr = messagePost + " (No HP: " + no_hp + " Menu ID: " + menu_id + ")";
				logger.error("Path: " + request.getServletPath() + " Error: " + resultErr);
			}*/
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + ", No. HP: " + no_hp + ", Menu ID: " + menu_id
					+ ", Error: " + e);
		}
		map.put("data", data);
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 5, new Date(), req, res, 1, resultErr, start, no_hp);

		return res;
	}

	@RequestMapping(value = "/forgotpassword", produces = "application/json", method = RequestMethod.POST)
	public String forgotPassword(@RequestBody RequestForgotPassword requestForgotPassword, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestForgotPassword);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		HashMap<String, Object> sms = new HashMap<>();

		String username = requestForgotPassword.getUsername();
		try {

			LstUserSimultaneous checkIndividuOrCorporate = services.selectDataLstUserSimultaneous(username);

			// Check user exists or not on EKA.LST_USER_SIMULTANEOUS
			if (checkIndividuOrCorporate != null) {
				String reg_spaj = checkIndividuOrCorporate.getREG_SPAJ();
				String mcl_id_employee = checkIndividuOrCorporate.getMCL_ID_EMPLOYEE();
				boolean isAccountDplk = loginSvc.isAccountDplk(checkIndividuOrCorporate);
				boolean isIndividu = loginSvc.isIndividu(checkIndividuOrCorporate);
				boolean isIndividuMri = registrationIndividuSvc.isIndividuMri(checkIndividuOrCorporate.getID_SIMULTAN(), checkIndividuOrCorporate.getUSERNAME());
				boolean isIndividuCorporate = loginSvc.isIndividuCorporate(checkIndividuOrCorporate);
				boolean isCorporate = loginSvc.corporate(checkIndividuOrCorporate);

				if (isIndividu || isIndividuMri && !isCorporate && !isIndividuCorporate && !isAccountDplk) { // Forgot Password Individual
					User dataUserIndividual = services.selectUserIndividual(username);
					String no_hp = registrationIndividuSvc.noHpIndividuAndMri(dataUserIndividual);

					if (no_hp == null) { // Check No HP empty or not
						error = true;
						message = "Data phone number is empty";
						sms.put("sms", null);
						sms.put("no_polis", null);
						sms.put("account_no_dplk", null);
						data.put("contacts", sms);
						data.put("ishavingphonenumber", false);
						data.put("username", null);
						resultErr = "Data nomor telepon kosong";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else {
						/*String result = customResourceLoader.sendOTP(91, requestForgotPassword.getMenu_id(), no_hp,
								dataUserIndividual.getReg_spaj(), dataUserIndividual.getMspo_policy_no());*/
						
						RequestSendOTP requestSendOTP = new RequestSendOTP();
						requestSendOTP.setJenis_id(91);
						requestSendOTP.setMenu_id(requestForgotPassword.getMenu_id());
						requestSendOTP.setUsername(no_hp);
						requestSendOTP.setNo_polis(dataUserIndividual.getMspo_policy_no());
						requestSendOTP.setReg_spaj(dataUserIndividual.getReg_spaj());
						ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);
						
						Boolean errorPost = (Boolean) responseSendOTP.getError();
						
						if (errorPost == false) {
							error = false;
							message = "Account found in database";
							data.put("ishavingphonenumber", true);
							data.put("contacts", sms);
							data.put("username", username);
							data.put("policy_type", "individual");
							sms.put("sms", no_hp);
							sms.put("no_polis", dataUserIndividual.getMspo_policy_no());
							sms.put("account_no_dplk", null);
						} else {
							error = true;
							message = "Phone number is blacklisted";
							data.put("ishavingphonenumber", true);
							data.put("contacts", sms);
							data.put("username", null);
							sms.put("sms", no_hp);
							sms.put("no_polis", dataUserIndividual.getMspo_policy_no());
							sms.put("account_no_dplk", null);
							resultErr = "No telepon sedang dalam masa blacklist";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					}
				} else if (isIndividuCorporate || isCorporate && !isIndividu && !isIndividuMri){ // Forgot Password Corporate
					UserCorporate dataUserCorporate = services.selectUserCorporate(username);

					if (dataUserCorporate != null) {
						String no_hp = dataUserCorporate.getNo_hp();

						if (no_hp != null) { // Check No HP empty or not

							/*result = customResourceLoader.sendOTP(91, requestForgotPassword.getMenu_id(), no_hp,
									dataUserCorporate.getReg_spaj(), dataUserCorporate.getNo_polis());*/
							
							RequestSendOTP requestSendOTP = new RequestSendOTP();
							requestSendOTP.setJenis_id(91);
							requestSendOTP.setMenu_id(requestForgotPassword.getMenu_id());
							requestSendOTP.setUsername(no_hp);
							requestSendOTP.setNo_polis(dataUserCorporate.getNo_polis());
							requestSendOTP.setReg_spaj(dataUserCorporate.getReg_spaj());
							ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);
							
							Boolean errorPost = (Boolean) responseSendOTP.getError();

							if (errorPost == false) {
								error = false;
								message = "Account found in database";
								data.put("ishavingphonenumber", true);
								data.put("username", username);
								data.put("contacts", sms);
								data.put("policy_type", "corporate");
								sms.put("sms", no_hp);
								sms.put("no_polis", dataUserCorporate.getNo_polis());
								sms.put("account_no_dplk", null);
							} else {
								error = true;
								message = "Phone number is blacklisted";
								data.put("ishavingphonenumber", true);
								data.put("contacts", sms);
								data.put("username", null);
								sms.put("sms", no_hp);
								sms.put("no_polis", dataUserCorporate.getNo_polis());
								sms.put("account_no_dplk", null);
								resultErr = "No telepon sedang dalam masa blacklist";
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						} else {
							error = true;
							message = "Data phone number is empty";
							data.put("ishavingphonenumber", false);
							data.put("username", null);
							data.put("contacts", sms);
							sms.put("sms", null);
							sms.put("no_polis", null);
							sms.put("account_no_dplk", null);
							resultErr = "Nomor handphone kosong hubungi CS";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						error = true;
						message = "Account not found in database";
						data.put("ishavingphonenumber", false);
						data.put("username", null);
						data.put("contacts", sms);
						sms.put("sms", null);
						sms.put("no_polis", null);
						sms.put("account_no_dplk", null);
						resultErr = "Username tidak ditemukan";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (isAccountDplk && !isIndividuCorporate && !isCorporate && !isIndividu && !isIndividuMri){
					DPLKAccountModel dplkByAccNo = services.getInfoDplkByAccNo(checkIndividuOrCorporate.getAccount_no_dplk() != null ? checkIndividuOrCorporate.getAccount_no_dplk() : null);
					if (dplkByAccNo != null){
						String no_hp = dplkByAccNo.getNo_hp();
						if (no_hp == null){
							error = true;
							message = "Account not found in database";
							data.put("ishavingphonenumber", false);
							data.put("username", null);
							data.put("contacts", sms);
							sms.put("sms", null);
							sms.put("no_polis", null);
							sms.put("account_no_dplk", null);
							resultErr = "Username tidak ditemukan";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						} else {
							RequestSendOTP requestSendOTP = new RequestSendOTP();
							requestSendOTP.setJenis_id(91);
							requestSendOTP.setMenu_id(requestForgotPassword.getMenu_id());
							requestSendOTP.setUsername(no_hp);
							ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);

							Boolean errorPost = (Boolean) responseSendOTP.getError();

							if (errorPost == false) {
								error = false;
								message = "Account found in database";
								data.put("ishavingphonenumber", true);
								data.put("username", username);
								data.put("contacts", sms);
								data.put("policy_type", "dplk");
								sms.put("sms", no_hp);
								sms.put("no_polis", null);
								sms.put("account_no_dplk", dplkByAccNo.getNo_peserta());
							} else {
								error = true;
								message = "Phone number is blacklisted";
								data.put("ishavingphonenumber", true);
								data.put("contacts", sms);
								data.put("username", null);
								sms.put("sms", no_hp);
								sms.put("no_polis", null);
								sms.put("account_no_dplk", dplkByAccNo.getNo_peserta());
								resultErr = "No telepon sedang dalam masa blacklist";
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						}
					} else {
						error = true;
						message = "Account not found in database";
						data.put("ishavingphonenumber", false);
						data.put("username", null);
						data.put("contacts", sms);
						sms.put("sms", null);
						sms.put("no_polis", null);
						sms.put("account_no_dplk", null);
						resultErr = "Username tidak ditemukan";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				}
			} else {
				error = true;
				message = "Account not found in database";
				data.put("ishavingphonenumber", false);
				data.put("username", null);
				data.put("contacts", sms);
				sms.put("sms", null);
				sms.put("no_polis", null);
				resultErr = "Username tidak ditemukan";
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
		map.put("data", data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 19, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/updatepassword", produces = "application/json", method = RequestMethod.POST)
	public String updatePassword(@RequestBody RequestUpdatePassword requestUpdatePassword, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestUpdatePassword);
		String res = null;
		String resultErr = null;
		String message = null;
		Boolean error = true;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestUpdatePassword.getUsername();
		String newPass = requestUpdatePassword.getNew_password();
		Integer typeUpdatePassword = requestUpdatePassword.getTypeUpdatePassword();
		try {
			LstUserSimultaneous dataUser = services.selectDataLstUserSimultaneous(username);

			if (dataUser != null) {
				// Check apakah password yang diinputkan sama dengan sebelumnya
				if (!dataUser.getPASSWORD().equals(newPass)) {
					User paramUpdatePassword = new User();
					paramUpdatePassword.setPassword(newPass);
					paramUpdatePassword.setUsername(username);

					// Update password
					services.updatePassword(paramUpdatePassword);

					if (typeUpdatePassword.equals(2)) { // Sudah Login
						// Update activity user table LST_USER_SIMULTANEOUS
						customResourceLoader.updateActivity(username);
					}

					error = false;
					message = "Password has been changed";
				} else {
					// Error password yang dimasukkan sama dengan sebelumnya
					error = true;
					message = "The password is the same as before";
					resultErr = "Password yang dimasukkan sama dengan sebelumnya";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Error username yang dimasukkan tidak ada pada database
				error = true;
				message = "Update password failed";
				resultErr = "Username tidak terdaftar";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			map.put("error", true);
			map.put("message", "Maaf system sedang error, coba beberapa saat lagi atau hubungi administrator");
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 20, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/forgotusername", produces = "application/json", method = RequestMethod.POST)
	public String forgotUsername(@RequestBody RequestForgotUsername requestForgotUsername, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestForgotUsername);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		Integer type = requestForgotUsername.getType();
		String ktp_or_nopolis = requestForgotUsername.getKtp_or_nopolis();
		String dob = requestForgotUsername.getDob();
		String kode = requestForgotUsername.getKode();
		try {
			if (type.equals(1)) { // Individual
				User dataForgotUsername = services.selectForgotUsernameIndividual(ktp_or_nopolis, dob);

				if (dataForgotUsername == null) {
					error = true;
					message = "Data username not found";
					resultErr = "Hasil select username tidak menemukan data" + "(No. polis/ KTP: " + ktp_or_nopolis
							+ ", dob: " + dob + ")";
					logger.error("Path: " + request.getServletPath() + ", No. Polis/ KTP: " + ktp_or_nopolis
							+ " Error: " + resultErr);
				} else {
					String no_hp = registrationIndividuSvc.noHpIndividuAndMri(dataForgotUsername);

					if (no_hp == null) {
						error = true;
						message = "Data phone number is empty";
						resultErr = "Data nomor hp kosong";
						logger.error("Path: " + request.getServletPath() + ", No. Polis/ KTP: " + ktp_or_nopolis
								+ ", Error: " + resultErr);
					} else {
						/*String result = customResourceLoader.sendOTP(91, 2, no_hp, dataForgotUsername.getReg_spaj(),
								dataForgotUsername.getMspo_policy_no());*/
						
						RequestSendOTP requestSendOTP = new RequestSendOTP();
						requestSendOTP.setJenis_id(91);
						requestSendOTP.setMenu_id(2);
						requestSendOTP.setUsername(no_hp);
						requestSendOTP.setNo_polis(dataForgotUsername.getMspo_policy_no());
						requestSendOTP.setReg_spaj(dataForgotUsername.getReg_spaj());
						ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);
						
						Boolean errorPost = (Boolean) responseSendOTP.getError();					
						
						if (errorPost == true) {
							error = true;
							message = "Phone number is blacklisted";
							resultErr = "No telepon sedang dalam masa blacklist";
							logger.error("Path: " + request.getServletPath() + ", No. HP: " + no_hp + ", Error: "
									+ resultErr);
						} else {
							error = false;
							message = "OTP Send & Successfully get data username";
							data.put("username", dataForgotUsername.getUsername());
							data.put("no_handphone", no_hp);
							data.put("no_polis", dataForgotUsername.getMspo_policy_no());
							data.put("ktp", dataForgotUsername.getMspe_no_identity());
						}
					}
				}
			} else if (type.equals(2)) { // Corporate
				UserCorporate dataForgotUsername = services.selectForgotUsernameCorporate(ktp_or_nopolis,
						kode.replaceAll("[^0-9]", "").trim() + "A", dob);

				if (dataForgotUsername == null) {
					error = true;
					message = "Data username not found";
					resultErr = "Hasil select username tidak menemukan data" + "(No. polis/ KTP: " + ktp_or_nopolis
							+ ", dob: " + dob + ")";
					logger.error("Path: " + request.getServletPath() + ", No. Polis/ KTP: " + ktp_or_nopolis
							+ " Error: " + resultErr);
				} else {
					String no_hp = dataForgotUsername.getNo_hp();

					if (no_hp == null) {
						error = true;
						message = "Data phone number is empty";
						resultErr = "Data nomor hp kosong";
						logger.error("Path: " + request.getServletPath() + ", No. Polis/ KTP: " + ktp_or_nopolis
								+ ", Error: " + resultErr);
					} else {
						/*String result = customResourceLoader.sendOTP(91, 2, no_hp, dataForgotUsername.getReg_spaj(),
								dataForgotUsername.getNo_polis());*/
						
						RequestSendOTP requestSendOTP = new RequestSendOTP();
						requestSendOTP.setJenis_id(91);
						requestSendOTP.setMenu_id(2);
						requestSendOTP.setUsername(no_hp);
						requestSendOTP.setNo_polis(dataForgotUsername.getNo_polis());
						requestSendOTP.setReg_spaj(dataForgotUsername.getReg_spaj());
						ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);

						Boolean errorPost = (Boolean) responseSendOTP.getError();
						
						if (errorPost == true) {
							error = true;
							message = "Phone number is blacklisted";
							resultErr = "No telepon sedang dalam masa blacklist";
							logger.error("Path: " + request.getServletPath() + ", No. HP: " + no_hp + ", Error: "
									+ resultErr);
						} else {
							error = false;
							message = "OTP Send & Successfully get data username";
							data.put("username", dataForgotUsername.getUsername());
							data.put("no_handphone", no_hp);
							data.put("no_polis", dataForgotUsername.getNo_polis());
							data.put("ktp", null);
						}
					}
				}
			} else if (type.equals(3)){
				// dplk account
				User dplkByAccNo = services.findByAccountNoDplk(requestForgotUsername.getAccount_no_dplk() != null ? requestForgotUsername.getAccount_no_dplk() : null);
				boolean matchDob = false;
				if (dplkByAccNo != null && dplkByAccNo.getMspe_date_birth() != null){
					String dobCompare = dateUtils.getFormatterFormat(dplkByAccNo.getMspe_date_birth(), DateUtils.FORMAT_DAY_MONTH_YEAR_NO_STRIP, "GMT+7");
					if (dobCompare.equals(dob))
						matchDob = true;
				}
				if (matchDob){
					if (dplkByAccNo.getNo_hp() != null){
						String no_hp = dplkByAccNo.getNo_hp();
						/*String result = customResourceLoader.sendOTP(91, 2, no_hp, dataForgotUsername.getReg_spaj(),
								dataForgotUsername.getNo_polis());*/

						RequestSendOTP requestSendOTP = new RequestSendOTP();
						requestSendOTP.setJenis_id(91);
						requestSendOTP.setMenu_id(2);
						requestSendOTP.setUsername(no_hp);
						ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);

						Boolean errorPost = (Boolean) responseSendOTP.getError();

						if (errorPost == true) {
							error = true;
							message = "Phone number is blacklisted";
							resultErr = "No telepon sedang dalam masa blacklist";
							logger.error("Path: " + request.getServletPath() + ", No. HP: " + no_hp + ", Error: "
									+ resultErr);
						} else {
							error = false;
							message = "OTP Send & Successfully get data username";
							data.put("username", dplkByAccNo.getUsername());
							data.put("no_handphone", no_hp);
							data.put("no_polis", null);
							data.put("account_no_dplk", dplkByAccNo.getAccount_no_dplk());
							data.put("ktp", null);
						}
					} else {
						error = true;
						message = "Data phone number is empty";
						resultErr = "Data nomor hp kosong";
						logger.error("Path: " + request.getServletPath() + ", No. Polis/ KTP: " + ktp_or_nopolis
								+ ", Error: " + resultErr);
					}
				} else {
					error = true;
					message = "account not found";
					resultErr = "Hasil select username tidak menemukan data" + "(No. polis/ KTP / account no_dplk: " + requestForgotUsername.getAccount_no_dplk()
							+ ", dob: " + dob + ")";
					logger.error("Path: " + request.getServletPath() + ", No. Polis/ KTP: " + ktp_or_nopolis
							+ " Error: " + resultErr);
				}
			} else {
				// Type not available
				error = true;
				message = "Type not available";
				resultErr = "Type yang dimasukkan tidak terdaftar";
				logger.error("Path: " + request.getServletPath() + ", No. Polis/ KTP: " + ktp_or_nopolis + ", Error: "
						+ resultErr);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + ", No. Polis/ KTP: " + ktp_or_nopolis + ", Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 46, new Date(), req, res, 1, resultErr, start, ktp_or_nopolis);

		return res;
	}
}