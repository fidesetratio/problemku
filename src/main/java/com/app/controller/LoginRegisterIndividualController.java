package com.app.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

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
import com.app.constant.AccountManagementCons;
import com.app.feignclient.ServiceEmail;
import com.app.feignclient.ServiceOTP;
import com.app.model.LstUserSimultaneous;
import com.app.model.Pemegang;
import com.app.model.User;
import com.app.model.request.RequestCheckActivity;
import com.app.model.request.RequestFindAccount;
import com.app.model.request.RequestLastActivity;
import com.app.model.request.RequestRegisterQR;
import com.app.model.request.RequestValidatePolicy;
import com.app.model.request.RequestSendOTP;
import com.app.model.ResponseData;
import com.app.utils.CommonUtils;
import com.app.utils.VegaCustomResourceLoader;
import com.app.utils.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class LoginRegisterIndividualController {

	/* Controller login/ register untuk nasabah Individual */

	private static final Logger logger = LogManager.getLogger(LoginRegisterIndividualController.class);

	@Autowired
	private VegaServices services;

	@Autowired
	private VegaCustomResourceLoader customResourceLoader;

	@Autowired
	AccountManagementCons constant;

	@Autowired
	ServiceEmail serviceEmail;

	@Autowired
	ServiceOTP serviceOTP;
	
	@Autowired
	CommonUtils utils;

	@Value("${link.update.activity}")
	private String linkUpdateActivity;

	@Value("${link.fcm.google}")
	private String linkFcmGoogle;

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat df2 = new SimpleDateFormat("ddMMyyyy");

	@RequestMapping(value = "/registerqr", produces = "application/json", method = RequestMethod.POST)
	public String registerQr(@RequestBody RequestRegisterQR requestRegisterQR, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestRegisterQR);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestRegisterQR.getUsername();
		String no_polis = requestRegisterQR.getNo_polis();
		String password = requestRegisterQR.getPassword();
		try {
			// Cari id_simultan dan REG SPAJ
			Pemegang pemegang = new Pemegang();
			pemegang.setMspo_policy_no(no_polis);
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

	@RequestMapping(value = "/findaccount", produces = "application/json", method = RequestMethod.POST)
	public String registerWithoutQr(@RequestBody RequestFindAccount requestFindAccount, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestFindAccount);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String ktp_or_nopolis = requestFindAccount.getKtp_or_nopolis();
		Date dob = df2.parse(requestFindAccount.getDob());
		try {
			Pemegang pemegang = new Pemegang();
			pemegang.setMspo_policy_no(ktp_or_nopolis);

			// Check menggunakan No Polis
			pemegang = services.selectNomorPolisNotRegister(pemegang);
//			Date dob = df2.parse(requestFindAccount.getDob());
			// Check data inputan No. Polis/ KTP & DOB ada gak didatabse
			if (pemegang != null && pemegang.getMspe_date_birth().equals(dob)) {
				Boolean checkSimultanPolis = services.selectCountIdSimultanByIdSimultan(pemegang.getId_simultan());
				// Check ID Simultan apakah sudah pernah daftar pada M-Polis
				if (checkSimultanPolis == false) {
					String no_hp_polis = pemegang.getNo_hp() != null ? pemegang.getNo_hp() : pemegang.getNo_hp2();
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
								requestSendOTP.setNo_polis(pemegang.getReg_spaj());
								requestSendOTP.setReg_spaj(pemegang.getMspo_policy_no());
								ResponseData responseSendOTP = serviceOTP.sendOTP(requestSendOTP);
								
								String result = responseSendOTP.toString();
								
								JSONObject myResponse = new JSONObject(result.toString());
								Boolean errorPost = (Boolean) myResponse.get("error");
								String messagePost = (String) myResponse.get("message");

								// Check kondisi yang didapatkan setelah HIT API OTP
								if (errorPost == false) {
									error = false;
									message = "ktp/policy found in database";
									data.put("phone_no", no_hp_polis);
									data.put("no_polis", pemegang.getMspo_policy_no());
								} else {
									error = true;
									message = "Phone number is blacklisted";
									data.put("phone_no", no_hp_polis);
									data.put("no_polis", pemegang.getMspo_policy_no());
									resultErr = messagePost;
									logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: "
											+ ktp_or_nopolis + " Error: " + resultErr);
								}
							} else {
								// Error no handphone sudah digunakan polis lain
								error = true;
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
							error = true;
							message = "Policy number exists containing death claims";
							data.put("phone_no", no_hp_polis);
							data.put("no_polis", pemegang.getMspo_policy_no());
							resultErr = "No polis ada yang mengandung death claim";
							logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
									+ " Error: " + resultErr);
						}
					} else {
						// Error no handphone kosong/ tidak sesuai format
						error = true;
						message = "Phone number is not valid";
						data.put("phone_no", null);
						data.put("no_polis", null);
						resultErr = "Nomor telepon tidak ada/ format nomor telepon salah";
						logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
								+ " Error: " + resultErr);
					}
				} else {
					// Error hasil id_simultan ada pada tabel lst_user_simultaneous
					error = true;
					message = "Already has an account that associated with this policy";
					data.put("phone_no", null);
					data.put("no_polis", null);
					resultErr = "User sudah pernah register menggunakan no polis tersebut/ no polis lain";
					logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis + " Error: "
							+ resultErr);
				}
			} else {
				Pemegang resultKTP = new Pemegang();
				// Check menggunakan KTP
				resultKTP = services.selectKtp(ktp_or_nopolis);
				// Check data inputan No. Polis/ KTP & DOB ada gak didatabse
				if (resultKTP != null && resultKTP.getMspe_date_birth().equals(dob)) {
					Boolean checkSimultanKTP = services.selectCountIdSimultanByIdSimultan(resultKTP.getId_simultan());
					// Check ID Simultan apakah sudah pernah daftar pada M-Polis
					if (checkSimultanKTP == false) {
						String no_hp_ktp = resultKTP.getNo_hp() != null ? resultKTP.getNo_hp() : resultKTP.getNo_hp2();
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

									String result = responseSendOTP.toString();
									
									JSONObject myResponse = new JSONObject(result.toString());
									Boolean errorPost = (Boolean) myResponse.get("error");
									String messagePost = (String) myResponse.get("message");

									// Check kondisi yang didapatkan setelah HIT API OTP
									if (errorPost == false) {
										error = false;
										message = "ktp/policy found in database";
										data.put("phone_no", no_hp_ktp);
										data.put("no_polis", resultKTP.getMspo_policy_no());
									} else {
										error = true;
										message = "Phone number is blacklisted";
										data.put("phone_no", no_hp_ktp);
										data.put("no_polis", resultKTP.getMspo_policy_no());
										resultErr = messagePost;
										logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: "
												+ ktp_or_nopolis + " Error: " + resultErr);
									}
								} else {
									// Error no handphone sudah digunakan polis lain
									error = true;
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
								error = true;
								message = "Policy number exists containing death claims";
								data.put("phone_no", no_hp_ktp);
								data.put("no_polis", resultKTP.getMspo_policy_no());
								resultErr = "No polis ada yang mengandung death claim";
								logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
										+ " Error: " + resultErr);
							}
						} else {
							// Error no handphone kosong/ tidak sesuai format
							error = true;
							message = "Phone number is not valid";
							data.put("phone_no", null);
							data.put("no_polis", null);
							resultErr = "Nomor telepon tidak ada/ format nomor telepon salah";
							logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
									+ " Error: " + resultErr);
						}
					} else {
						// Error hasil id_simultan ada pada tabel lst_user_simultaneous
						error = true;
						message = "Already has an account that associated with this policy";
						data.put("phone_no", null);
						data.put("no_polis", null);
						resultErr = "User sudah pernah register menggunakan no polis tersebut/ no polis lain";
						logger.error("Path: " + request.getServletPath() + " No. Polis/ KTP: " + ktp_or_nopolis
								+ " Error: " + resultErr);
					}
				} else {
					// Error No polis/ KTP & DOB yang diinput tidak ada didatabase
					error = true;
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

	@RequestMapping(value = "/validatepolicy", produces = "application/json", method = RequestMethod.POST)
	public String validatePolicy(@RequestBody RequestValidatePolicy requestValidatePolicy, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestValidatePolicy);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String no_polis = requestValidatePolicy.getNo_polis();
		try {
			Pemegang pemegang = new Pemegang();
			pemegang.setMspo_policy_no(no_polis);
			pemegang = services.selectNomorPolisNotRegister(pemegang);
			// Check apakah no polis ada/ tidak didatabase
			if (pemegang != null) {
				String idSimultanNotRegister = pemegang.getId_simultan();
				Boolean resultIdSimultan = services.selectCountIdSimultanByIdSimultan(idSimultanNotRegister);
				// Check menggunakan id_simultan apakah ada polis yang sudah terdaftar
				if (resultIdSimultan == false) {
					String no_hp = pemegang.getNo_hp() != null ? pemegang.getNo_hp() : pemegang.getNo_hp2();
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
								
								String result = responseSendOTP.toString();
								// Check kondisi yang didapatkan setelah HIT API OTP
								
								JSONObject myResponse = new JSONObject(result.toString());
								Boolean errorPost = (Boolean) myResponse.get("error");
								String messagePost = (String) myResponse.get("message");

								if (errorPost == false) {
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

	@RequestMapping(value = "/checkactivity", produces = "application/json", method = RequestMethod.POST)
	public String checkActivity(@RequestBody RequestCheckActivity requestCheckActivity, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestCheckActivity);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestCheckActivity.getUsername();
		try {
			User dataActivityUser = services.selectUserIndividual(username);
			if (dataActivityUser != null) {
				User paramUpdateStatus = new User();
				paramUpdateStatus.setUsername(username);
				paramUpdateStatus.setDate_created_java(customResourceLoader.getDatetimeJava());
				services.updateActivityStatus(paramUpdateStatus);

				error = false;
				message = "Success Update Status";
			} else {
				error = true;
				message = "Username not registered";
				resultErr = "Username tidak ada didatabase";
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
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 39, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/lastactivity", produces = "application/json", method = RequestMethod.POST)
	public String lastActivity(@RequestBody RequestLastActivity requestLastActivity, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestLastActivity);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestLastActivity.getUsername();
		String token = requestLastActivity.getToken();
		try {
			User dataActivityUser = services.selectUserIndividual(username);
			if (dataActivityUser != null) {
				if (dataActivityUser.getLast_login_device().equals(token)) {
					error = false;
					message = "Successfully get user last activity";
					data.put("last_activity", df.format(dataActivityUser.getUpdate_date()));
				} else {
					error = false;
					message = "Successfully get user last activity";
					data.put("last_activity", df.format(dataActivityUser.getUpdate_date()));
				}
			} else {
				error = true;
				message = "Username not registered";
				resultErr = "Username tidak ada didatabase";
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
		map.put("data", data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 41, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
}