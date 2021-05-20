package com.app.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
import com.app.model.LstUserSimultaneous;
import com.app.model.Pemegang;
import com.app.model.ResponseData;
import com.app.model.User;
import com.app.model.UserCorporate;
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

	@Value("${link.fcm.google}")
	private String linkFcmGoogle;

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping(value = "/login", produces = "application/json", method = RequestMethod.POST)
	public String loginNew(@RequestBody RequestLogin requestLogin, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestLogin);
		String res = null;
		String resultErr = null;
		String message = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		String username = requestLogin.getUsername();
		String password = requestLogin.getPassword();
		String lastLoginDevice = requestLogin.getLast_login_device();
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
				String reg_spaj = checkIndividuOrCorporate.getREG_SPAJ();
				String mcl_id_employee = checkIndividuOrCorporate.getMCL_ID_EMPLOYEE();
				String eb_hr_username = checkIndividuOrCorporate.getEB_HR_USERNAME();

				Boolean individu = false;
				Boolean corporate = false;
				Boolean hr_user = false;
				Boolean policy_corporate_notinforce = false;
				Boolean user_corporate_notactive = false;

				if ((reg_spaj != null) && (mcl_id_employee == null) && (eb_hr_username == null)) {
					individu = true;
					corporate = false;
					hr_user = false;
				} else if ((reg_spaj == null) && (mcl_id_employee != null) && (eb_hr_username == null)) {
					individu = false;
					corporate = true;
					hr_user = false;

					ArrayList<UserCorporate> listPolisCorporate = services.selectListPolisCorporate(mcl_id_employee);
					for (int x = 0; x < 1; x++) {
						Date endDate = listPolisCorporate.get(x).getMspo_end_date();
						BigDecimal flagActiveUserCorporate = listPolisCorporate.get(x).getMste_active();
						LocalDate now = LocalDate.now();
						LocalDate endDateParse = LocalDate.parse(df1.format(endDate));
						// Check no polis corporate active or not
						if (endDateParse.compareTo(now) < 0) {
							policy_corporate_notinforce = true;
						} else {
							policy_corporate_notinforce = false;
						}

						// Check user corporate active or not
						if (flagActiveUserCorporate.intValue() == 0) {
							user_corporate_notactive = true;
						}
					}
				} else if ((reg_spaj == null) && (mcl_id_employee == null) && (eb_hr_username != null)) {
					individu = false;
					corporate = false;
					hr_user = true;
				} else {
					individu = true;
					corporate = true;
					hr_user= false;

					ArrayList<UserCorporate> listPolisCorporate = services.selectListPolisCorporate(mcl_id_employee);
					for (int x = 0; x < 1; x++) {
						Date endDate = listPolisCorporate.get(x).getMspo_end_date();
						BigDecimal flagActiveUserCorporate = listPolisCorporate.get(x).getMste_active();
						LocalDate now = LocalDate.now();
						LocalDate endDateParse = LocalDate.parse(df1.format(endDate));
						// Check no polis corporate active or not
						if (endDateParse.compareTo(now) < 0) {
							policy_corporate_notinforce = true;
						} else {
							policy_corporate_notinforce = false;
						}

						// Check user corporate active or not
						if (flagActiveUserCorporate.intValue() == 0) {
							user_corporate_notactive = true;
						}
					}
				}

				if ((individu.equals(true) && corporate.equals(false) && hr_user.equals(false))
						|| (individu.equals(true) && corporate.equals(true) && hr_user.equals(false))) { // Login Individual
					User dataActivityUser = services.selectUserIndividual(username);
					// Check apakah username terdaftar/ tidak
					if (dataActivityUser != null) {
						Integer countDeathClaim = services.selectCountDeathClaim(username);
						// Check apakah username tersebut mengandung polis death claim
						if (countDeathClaim == 0) {
							// Check apakah token yang ada didatabase kosong/ tidak
							if (dataActivityUser.getLast_login_device() != null) {
								// Check karena token ada, apakah token yang dikirim sama dengan yang didb
								if (dataActivityUser.getLast_login_device().equals(lastLoginDevice)) {
									// Check password yang diinputkan benar atau salah
									if (user1.getPASSWORD().equals(password)) {
										ArrayList<User> list = services.selectDetailedPolis(username);
										// Check apakah username tersebut punya list polis/ tidak
										if (list.size() > 0) {
											String today = df.format(new Date());
											lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
											lstUserSimultaneous.setUPDATE_DATE_TIME(today);
											services.updateUserKeyName(lstUserSimultaneous);
											key = user1.getKEY();

											error = false;
											message = "Login success";
											data.put("individual", individu);
											data.put("corporate", corporate);
											data.put("hr_user", hr_user);
											data.put("policy_corporate_status", policy_corporate_notinforce);
											data.put("user_corporate_notactive", user_corporate_notactive);
											data.put("key", key);
											data.put("no_hp",
													dataActivityUser.getNo_hp() != null ? dataActivityUser.getNo_hp()
															: dataActivityUser.getNo_hp2());
										} else {
											// Error list polis kosong
											error = true;
											message = "List policy is empty";
											resultErr = "List Polis Kosong";
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + resultErr);
										}
									} else {
										// Error password yang dimasukkan salah
										error = true;
										message = "Login failed";
										resultErr = "Password yang dimasukkan salah";
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								} else {
									// Kondisi dimana token ada tetapi berbeda dengan yang dikirim
									Date dateActivity = dataActivityUser.getUpdate_date();
									Date dateNow = new Date();
									long diff = dateNow.getTime() - dateActivity.getTime();
									long diffSeconds = diff / 1000;
									// Check apakah lama perbedaan waktu token sudah melebihi 15 menit/ belum
									if (diffSeconds >= time_idle) {
										// Check apabila lebih dari 15 menit, password yang dimasukkan kosong/ ""
										if (user1.getPASSWORD() != null || user1.getPASSWORD() != "") {
											// Check password yang dimasukkan sama/ tidak dengan yang didb
											if (user1.getPASSWORD().equals(password)) {
												ArrayList<User> list = services.selectDetailedPolis(username);
												// Check apakah username tersebut memiliki list polis atau tidak
												if (list.size() > 0) {
													try {
														customResourceLoader.postGoogle(linkFcmGoogle, username,
																dataActivityUser.getLast_login_device());

														String today = df.format(new Date());
														lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
														lstUserSimultaneous.setUPDATE_DATE_TIME(today);
														services.updateUserKeyName(lstUserSimultaneous);
														key = user1.getKEY();

														error = false;
														message = "Login success";
														data.put("individual", individu);
														data.put("corporate", corporate);
														data.put("hr_user", hr_user);
														data.put("policy_corporate_status",
																policy_corporate_notinforce);
														data.put("user_corporate_notactive", user_corporate_notactive);
														data.put("key", key);
														data.put("no_hp",
																dataActivityUser.getNo_hp() != null
																		? dataActivityUser.getNo_hp()
																		: dataActivityUser.getNo_hp2());
													} catch (Exception e) {
														logger.error("Path: " + request.getServletPath() + " Username: "
																+ username + " Error: " + e);
													}
												} else {
													// Error list polis pada username tersebut kosong
													error = true;
													message = "List policy is empty";
													resultErr = "List Polis Kosong";
													logger.error("Path: " + request.getServletPath() + " Username: "
															+ username + " Error: " + resultErr);
												}
											} else {
												// Error password yang dimasukkan user salah
												error = true;
												message = "Login failed";
												resultErr = "Password yang dimasukkan salah";
												logger.error("Path: " + request.getServletPath() + " Username: "
														+ username + " Error: " + resultErr);
											}
										} else {
											// Error password kosong
											error = true;
											message = "Login failed";
											resultErr = "Format password incorect";
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + resultErr);
										}
									} else {
										// Error perbedaan lama waktu token belum lebih dari 15 menit
										error = true;
										message = "Session still active";
										resultErr = "Account sedang login ditempat lain atau ada session yang belum berakhir";
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								}
							} else {
								// Kondisi token pada database tidak ada bisa kerena logout/ user baru
								// Check apabila lebih dari 15 menit, password yang dimasukkan kosong/ ""
								if (user1.getPASSWORD() != null || user1.getPASSWORD() != "") {
									// Check password yang dimasukkan sama/ tidak dengan yang didb
									if (user1.getPASSWORD().equals(password)) {
										ArrayList<User> list = services.selectDetailedPolis(username);
										// Check apakah username tersebut memiliki list polis atau tidak
										if (list.size() > 0) {
											String today = df.format(new Date());
											lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
											lstUserSimultaneous.setUPDATE_DATE_TIME(today);
											services.updateUserKeyName(lstUserSimultaneous);
											key = user1.getKEY();

											error = false;
											message = "Login success";
											data.put("individual", individu);
											data.put("corporate", corporate);
											data.put("hr_user", hr_user);
											data.put("policy_corporate_status", policy_corporate_notinforce);
											data.put("user_corporate_notactive", user_corporate_notactive);
											data.put("key", key);
											data.put("no_hp",
													dataActivityUser.getNo_hp() != null ? dataActivityUser.getNo_hp()
															: dataActivityUser.getNo_hp2());
										} else {
											// Error list polis pada username tersebut kosong
											error = true;
											message = "List policy is empty";
											resultErr = "List Polis Kosong";
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + resultErr);
										}
									} else {
										// Error password yang dimasukkan user salah
										error = true;
										message = "Login failed";
										resultErr = "Password yang dimasukkan salah";
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								} else {
									// Error password kosong
									error = true;
									message = "Login failed";
									resultErr = "Format password incorect";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + resultErr);
								}
							}
						} else {
							// Error pada username tersebut mengandung polis death claim
							error = true;
							message = "Policy number exists containing death claims";
							resultErr = "No polis pada username ini ada yang mengandung death claim";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						// Error username yang dimasukkan tidak ada pada database
						error = true;
						message = "Login failed";
						resultErr = "Username tidak terdaftar";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if ((individu.equals(false) && corporate.equals(false) && hr_user.equals(true))) { // Login HR
					String today = df.format(new Date());
					lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
					lstUserSimultaneous.setUPDATE_DATE_TIME(today);
					services.updateUserKeyName(lstUserSimultaneous);
					key = user1.getKEY();

					error = false;
					message = "Login success";
					data.put("individual", individu);
					data.put("corporate", corporate);
					data.put("hr_user", hr_user);
					data.put("policy_corporate_status", policy_corporate_notinforce);
					data.put("user_corporate_notactive", user_corporate_notactive);
					data.put("key", key);
					data.put("no_hp", null);
				}
				else { // Login Corporate
					UserCorporate dataUserCorporate = services.selectUserCorporate(username);
					// Check apakah username terdaftar/ tidak
					if (dataUserCorporate != null) {
						// Check apakah token yang ada didatabase kosong/ tidak
						if (dataUserCorporate.getLast_login_device() != null) {
							// Check karena token ada, apakah token yang dikirim sama dengan yang didb
							if (dataUserCorporate.getLast_login_device().equals(lastLoginDevice)) {
								// Check password yang diinputkan benar atau salah
								if (user1.getPASSWORD().equals(password)) {
									ArrayList<UserCorporate> list = services.selectListPolisCorporate(mcl_id_employee);
									// Check apakah username tersebut punya list polis/ tidak
									if (list.size() > 0) {
										String today = df.format(new Date());
										lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
										lstUserSimultaneous.setUPDATE_DATE_TIME(today);
										services.updateUserKeyName(lstUserSimultaneous);
										key = user1.getKEY();

										error = false;
										message = "Login success";
										data.put("individual", individu);
										data.put("corporate", corporate);
										data.put("hr_user", hr_user);
										data.put("policy_corporate_status", policy_corporate_notinforce);
										data.put("user_corporate_notactive", user_corporate_notactive);
										data.put("key", key);
										data.put("no_hp", dataUserCorporate.getNo_hp());
									} else {
										// Error list polis kosong
										error = true;
										message = "List policy is empty";
										resultErr = "List Polis Kosong";
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								} else {
									// Error password yang dimasukkan salah
									error = true;
									message = "Login failed";
									resultErr = "Password yang dimasukkan salah";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + resultErr);
								}
							} else {
								// Kondisi dimana token ada tetapi berbeda dengan yang dikirim
								Date dateActivity = dataUserCorporate.getUpdate_date();
								Date dateNow = new Date();
								long diff = dateNow.getTime() - dateActivity.getTime();
								long diffSeconds = diff / 1000;
								// Check apakah lama perbedaan waktu token sudah melebihi 15 menit/ belum
								if (diffSeconds >= time_idle) {
									// Check apabila lebih dari 15 menit, password yang dimasukkan kosong/ ""
									if (user1.getPASSWORD() != null || user1.getPASSWORD() != "") {
										// Check password yang dimasukkan sama/ tidak dengan yang didb
										if (user1.getPASSWORD().equals(password)) {
											ArrayList<UserCorporate> list = services
													.selectListPolisCorporate(mcl_id_employee);
											// Check apakah username tersebut memiliki list polis atau tidak
											if (list.size() > 0) {
												try {
													customResourceLoader.postGoogle(linkFcmGoogle, username,
															dataUserCorporate.getLast_login_device());

													String today = df.format(new Date());
													lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
													lstUserSimultaneous.setUPDATE_DATE_TIME(today);
													services.updateUserKeyName(lstUserSimultaneous);
													key = user1.getKEY();

													error = false;
													message = "Login success";
													data.put("individual", individu);
													data.put("corporate", corporate);
													data.put("hr_user", hr_user);
													data.put("policy_corporate_status", policy_corporate_notinforce);
													data.put("user_corporate_notactive", user_corporate_notactive);
													data.put("key", key);
													data.put("no_hp", dataUserCorporate.getNo_hp());
												} catch (Exception e) {
													logger.error("Path: " + request.getServletPath() + " Username: "
															+ username + " Error: " + e);
												}
											} else {
												// Error list polis pada username tersebut kosong
												error = true;
												message = "List policy is empty";
												resultErr = "List Polis Kosong";
												logger.error("Path: " + request.getServletPath() + " Username: "
														+ username + " Error: " + resultErr);
											}
										} else {
											// Error password yang dimasukkan user salah
											error = true;
											message = "Login failed";
											resultErr = "Password yang dimasukkan salah";
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + resultErr);
										}
									} else {
										// Error password kosong
										error = true;
										message = "Login failed";
										resultErr = "Format password incorect";
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								} else {
									// Error perbedaan lama waktu token belum lebih dari 15 menit
									error = true;
									message = "Session still active";
									resultErr = "Account sedang login ditempat lain atau ada session yang belum berakhir";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + resultErr);
								}
							}
						} else {
							// Kondisi token pada database tidak ada bisa kerena logout/ user baru
							// Check apabila lebih dari 15 menit, password yang dimasukkan kosong/ ""
							if (user1.getPASSWORD() != null || user1.getPASSWORD() != "") {
								// Check password yang dimasukkan sama/ tidak dengan yang didb
								if (user1.getPASSWORD().equals(password)) {
									ArrayList<UserCorporate> list = services.selectListPolisCorporate(mcl_id_employee);
									// Check apakah username tersebut memiliki list polis atau tidak
									if (list.size() > 0) {
										String today = df.format(new Date());
										lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
										lstUserSimultaneous.setUPDATE_DATE_TIME(today);
										services.updateUserKeyName(lstUserSimultaneous);
										key = user1.getKEY();

										error = false;
										message = "Login success";
										data.put("individual", individu);
										data.put("corporate", corporate);
										data.put("hr_user", hr_user);
										data.put("policy_corporate_status", policy_corporate_notinforce);
										data.put("user_corporate_notactive", user_corporate_notactive);
										data.put("key", key);
										data.put("no_hp", dataUserCorporate.getNo_hp());
									} else {
										// Error list polis pada username tersebut kosong
										error = true;
										message = "List policy is empty";
										resultErr = "List Polis Kosong";
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								} else {
									// Error password yang dimasukkan user salah
									error = true;
									message = "Login failed";
									resultErr = "Password yang dimasukkan salah";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + resultErr);
								}
							} else {
								// Error password kosong
								error = true;
								message = "Login failed";
								resultErr = "Format password incorect";
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						}

					} else {
						// Error username yang dimasukkan tidak ada pada database
						error = true;
						message = "Login failed";
						resultErr = "Username tidak terdaftar";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				}
			} else {
				// Error username yang dimasukkan tidak ada pada database
				error = true;
				message = "Login failed";
				resultErr = "Username tidak terdaftar";
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
		customResourceLoader.insertHistActivityWS(12, 1, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/logineasypin", produces = "application/json", method = RequestMethod.POST)
	public String loginEasyPin(@RequestBody RequestLoginEasypin requestLoginEasypin, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestLoginEasypin);
		String res = null;
		String resultErr = null;
		String message = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestLoginEasypin.getUsername();
		String lastLoginDevice = requestLoginEasypin.getLast_login_device();
		try {
			String key = null;

			LstUserSimultaneous lstUserSimultaneous = new LstUserSimultaneous();
			lstUserSimultaneous.setUSERNAME(username);
			lstUserSimultaneous.setFLAG_ACTIVE(1);
			lstUserSimultaneous.setLAST_LOGIN_DEVICE(lastLoginDevice);
			LstUserSimultaneous user1 = services.selectLoginAuthenticate(lstUserSimultaneous);

			HashMap<String, Object> dataConfiguration = services.configuration();
			Integer time_idle = Integer.parseInt((String) dataConfiguration.get("TIME_IDLE"));

			LstUserSimultaneous checkIndividuOrCorporate = services.selectDataLstUserSimultaneous(username);

			if (checkIndividuOrCorporate != null) {
				String reg_spaj = checkIndividuOrCorporate.getREG_SPAJ();
				String mcl_id_employee = checkIndividuOrCorporate.getMCL_ID_EMPLOYEE();
				String eb_hr_username = checkIndividuOrCorporate.getEB_HR_USERNAME();

				Boolean individu = false;
				Boolean corporate = false;
				Boolean hr_user = false;
				Boolean policy_corporate_notinforce = false;
				Boolean user_corporate_notactive = false;

				if ((reg_spaj != null) && (mcl_id_employee == null) && (eb_hr_username == null)) {
					individu = true;
					corporate = false;
					hr_user = false;
					
				} else if ((reg_spaj == null) && (mcl_id_employee != null) && (eb_hr_username == null)) {
					individu = false;
					corporate = true;
					hr_user = false;

					ArrayList<UserCorporate> listPolisCorporate = services.selectListPolisCorporate(mcl_id_employee);
					for (int x = 0; x < 1; x++) {
						Date endDate = listPolisCorporate.get(x).getMspo_end_date();
						BigDecimal flagActiveUserCorporate = listPolisCorporate.get(x).getMste_active();
						LocalDate now = LocalDate.now();
						LocalDate endDateParse = LocalDate.parse(df1.format(endDate));
						// Check no polis corporate active or not
						if (endDateParse.compareTo(now) < 0) {
							policy_corporate_notinforce = true;
						} else {
							policy_corporate_notinforce = false;
						}

						// Check user corporate active or not
						if (flagActiveUserCorporate.intValue() == 0) {
							user_corporate_notactive = true;
						}
					}
				} else if((reg_spaj == null) && (mcl_id_employee == null) && (eb_hr_username != null)) {
					individu = false;
					corporate = false;
					hr_user = true;
				} else {
					individu = true;
					corporate = true;

					ArrayList<UserCorporate> listPolisCorporate = services.selectListPolisCorporate(mcl_id_employee);
					for (int x = 0; x < 1; x++) {
						Date endDate = listPolisCorporate.get(x).getMspo_end_date();
						BigDecimal flagActiveUserCorporate = listPolisCorporate.get(x).getMste_active();
						LocalDate now = LocalDate.now();
						LocalDate endDateParse = LocalDate.parse(df1.format(endDate));
						// Check no polis corporate active or not
						if (endDateParse.compareTo(now) < 0) {
							policy_corporate_notinforce = true;
						} else {
							policy_corporate_notinforce = false;
						}

						// Check user corporate active or not
						if (flagActiveUserCorporate.intValue() == 0) {
							user_corporate_notactive = true;
						}
					}
				}

				if ((individu.equals(true) && corporate.equals(false) && hr_user.equals(false))
						|| (individu.equals(true) && corporate.equals(true) && hr_user.equals(false))) { // Login Individual
					User dataActivityUser = services.selectUserIndividual(username);
					// Check username terdaftar/ tidak ditabel user M-Polis
					if (dataActivityUser != null) {
						Integer countDeathClaim = services.selectCountDeathClaim(username);
						// Check apakah terdapat polis death claim
						if (countDeathClaim == 0) {
							// Check token pada di eka.lst_user_simultaneous ada/ tidak
							if (dataActivityUser.getLast_login_device() != null) {
								// Check karena token ada, sama/ tidak token yang dikirim dgn yang didatabase
								if (dataActivityUser.getLast_login_device().equals(lastLoginDevice)) {
									ArrayList<User> list = services.selectDetailedPolis(username);
									// Check apakah polis pada username ini ada atau tidak
									if (list.size() > 0) {
										String today = df.format(new Date());
										lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
										lstUserSimultaneous.setUPDATE_DATE_TIME(today);
										// Update activity pada tabel lst_user_simultaneous
										services.updateUserKeyName(lstUserSimultaneous);
										key = user1.getKEY();

										error = false;
										message = "Login success";
										data.put("individual", individu);
										data.put("corporate", corporate);
										data.put("hr_user", hr_user);
										data.put("policy_corporate_status", policy_corporate_notinforce);
										data.put("user_corporate_notactive", user_corporate_notactive);
										data.put("key", key);
										data.put("no_hp",
												dataActivityUser.getNo_hp() != null ? dataActivityUser.getNo_hp()
														: dataActivityUser.getNo_hp2());
									} else {
										// Error No. polis kosong pada username tsb
										error = true;
										message = "List policy is empty";
										resultErr = "List Polis Kosong";
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								} else {
									// Kondisi jika token yang dikirim tidak sama dengan didb
									Date dateActivity = dataActivityUser.getUpdate_date();
									Date dateNow = new Date();
									long diff = dateNow.getTime() - dateActivity.getTime();
									long diffSeconds = diff / 1000;

									// Check apakah lama perbedaan waktu token sudah melebihi 15 menit/ belum
									if (diffSeconds >= time_idle) {
										ArrayList<User> list = services.selectDetailedPolis(username);
										// Check apakah pada username tersebut memiliki list polis/ tidak
										if (list.size() > 0) {
											try {
												// Send notif via FCM google
												customResourceLoader.postGoogle(linkFcmGoogle, username,
														dataActivityUser.getLast_login_device());

												String today = df.format(new Date());
												lstUserSimultaneous.setUPDATE_DATE_TIME(today);
												lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
												services.updateUserKeyName(lstUserSimultaneous);
												key = user1.getKEY();

												error = false;
												message = "Login success";
												data.put("individual", individu);
												data.put("corporate", corporate);
												data.put("hr_user", hr_user);
												data.put("policy_corporate_status", policy_corporate_notinforce);
												data.put("user_corporate_notactive", user_corporate_notactive);
												data.put("key", key);
												data.put("no_hp",
														dataActivityUser.getNo_hp() != null
																? dataActivityUser.getNo_hp()
																: dataActivityUser.getNo_hp2());
											} catch (Exception e) {
												logger.error("Path: " + request.getServletPath() + " Username: "
														+ username + " Error: " + e);
											}
										} else {
											// Error list polis pada username tersebut kosong
											error = true;
											message = "List policy is empty";
											resultErr = "List Polis Kosong";
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + resultErr);
										}
									} else {
										// Error perbedaan lama waktu token belum lebih dari 15 menit
										error = true;
										message = "Session still active";
										resultErr = "Account sedang login ditempat lain atau ada session yang belum berakhir";
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								}
							} else {
								// Kondisi token pada database tidak ada bisa kerena logout/ user baru
								ArrayList<User> list = services.selectDetailedPolis(username);
								// Check list polis pada username tersebut
								if (list.size() > 0) {
									String today = df.format(new Date());
									lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
									lstUserSimultaneous.setUPDATE_DATE_TIME(today);
									services.updateUserKeyName(lstUserSimultaneous);
									key = user1.getKEY();

									error = false;
									message = "Login success";
									data.put("individual", individu);
									data.put("corporate", corporate);
									data.put("hr_user", hr_user);
									data.put("policy_corporate_status", policy_corporate_notinforce);
									data.put("user_corporate_notactive", user_corporate_notactive);
									data.put("key", key);
									data.put("no_hp", dataActivityUser.getNo_hp() != null ? dataActivityUser.getNo_hp()
											: dataActivityUser.getNo_hp2());
								} else {
									// Error list polis kosong pada username tersebut
									error = true;
									message = "List policy is empty";
									resultErr = "List Polis Kosong";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + resultErr);
								}
							}
						} else {
							// Error pada username tersebut mengandung polis death claim
							error = true;
							message = "Policy number exists containing death claims";
							resultErr = "No polis pada username ini ada yang mengandung death claim";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						// Error username yang dimasukkan tidak ada pada database
						error = true;
						message = "Login failed";
						resultErr = "Username tidak terdaftar";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if ((individu.equals(false) && corporate.equals(false) && hr_user.equals(true))) { // Login HR
					String today = df.format(new Date());
					lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
					lstUserSimultaneous.setUPDATE_DATE_TIME(today);
					services.updateUserKeyName(lstUserSimultaneous);
					key = user1.getKEY();

					error = false;
					message = "Login success";
					data.put("individual", individu);
					data.put("corporate", corporate);
					data.put("hr_user", hr_user);
					data.put("policy_corporate_status", policy_corporate_notinforce);
					data.put("user_corporate_notactive", user_corporate_notactive);
					data.put("key", key);
					data.put("no_hp", null);
				} else { // Login Corporate
					UserCorporate dataUserCorporate = services.selectUserCorporate(username);
					// Check username terdaftar/ tidak ditabel user M-Polis
					if (dataUserCorporate != null) {
						// Check token pada di eka.lst_user_simultaneous ada/ tidak
						if (dataUserCorporate.getLast_login_device() != null) {
							// Check karena token ada, sama/ tidak token yang dikirim dgn yang didatabase
							if (dataUserCorporate.getLast_login_device().equals(lastLoginDevice)) {
								ArrayList<UserCorporate> list = services.selectListPolisCorporate(mcl_id_employee);
								// Check apakah polis pada username ini ada atau tidak
								if (list.size() > 0) {
									String today = df.format(new Date());
									lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
									lstUserSimultaneous.setUPDATE_DATE_TIME(today);
									// Update activity pada tabel lst_user_simultaneous
									services.updateUserKeyName(lstUserSimultaneous);
									key = user1.getKEY();

									error = false;
									message = "Login success";
									data.put("individual", individu);
									data.put("corporate", corporate);
									data.put("hr_user", hr_user);
									data.put("policy_corporate_status", policy_corporate_notinforce);
									data.put("user_corporate_notactive", user_corporate_notactive);
									data.put("key", key);
									data.put("no_hp", dataUserCorporate.getNo_hp());
								} else {
									// Error No. polis kosong pada username tsb
									error = true;
									message = "List policy is empty";
									resultErr = "List Polis Kosong";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + resultErr);
								}
							} else {
								// Kondisi jika token yang dikirim tidak sama dengan didb
								Date dateActivity = dataUserCorporate.getUpdate_date();
								Date dateNow = new Date();
								long diff = dateNow.getTime() - dateActivity.getTime();
								long diffSeconds = diff / 1000;

								// Check apakah lama perbedaan waktu token sudah melebihi 15 menit/ belum
								if (diffSeconds >= time_idle) {
									ArrayList<UserCorporate> list = services.selectListPolisCorporate(mcl_id_employee);
									// Check apakah pada username tersebut memiliki list polis/ tidak
									if (list.size() > 0) {
										try {
											// Send notif via FCM google
											customResourceLoader.postGoogle(linkFcmGoogle, username,
													dataUserCorporate.getLast_login_device());

											String today = df.format(new Date());
											lstUserSimultaneous.setUPDATE_DATE_TIME(today);
											lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
											services.updateUserKeyName(lstUserSimultaneous);
											key = user1.getKEY();

											error = false;
											message = "Login success";
											data.put("individual", individu);
											data.put("corporate", corporate);
											data.put("hr_user", hr_user);
											data.put("policy_corporate_status", policy_corporate_notinforce);
											data.put("user_corporate_notactive", user_corporate_notactive);
											data.put("key", key);
											data.put("no_hp", dataUserCorporate.getNo_hp());
										} catch (Exception e) {
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + e);
										}
									} else {
										// Error list polis pada username tersebut kosong
										error = true;
										message = "List policy is empty";
										resultErr = "List Polis Kosong";
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								} else {
									// Error perbedaan lama waktu token belum lebih dari 15 menit
									error = true;
									message = "Session still active";
									resultErr = "Account sedang login ditempat lain atau ada session yang belum berakhir";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + resultErr);
								}
							}
						} else {
							// Kondisi token pada database tidak ada bisa kerena logout/ user baru
							ArrayList<UserCorporate> list = services.selectListPolisCorporate(mcl_id_employee);
							// Check list polis pada username tersebut
							if (list.size() > 0) {
								String today = df.format(new Date());
								lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
								lstUserSimultaneous.setUPDATE_DATE_TIME(today);
								services.updateUserKeyName(lstUserSimultaneous);
								key = user1.getKEY();

								error = false;
								message = "Login success";
								data.put("individual", individu);
								data.put("corporate", corporate);
								data.put("hr_user", hr_user);
								data.put("policy_corporate_status", policy_corporate_notinforce);
								data.put("user_corporate_notactive", user_corporate_notactive);
								data.put("key", key);
								data.put("no_hp", dataUserCorporate.getNo_hp());
							} else {
								// Error list polis kosong pada username tersebut
								error = true;
								message = "List policy is empty";
								resultErr = "List Polis Kosong";
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						}
					} else {
						// Error username yang dimasukkan tidak ada pada database
						error = true;
						message = "Login failed";
						resultErr = "Username tidak terdaftar";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				}
			} else {
				// Error username yang dimasukkan tidak ada pada database
				error = true;
				message = "Login failed";
				resultErr = "Username tidak terdaftar";
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
		customResourceLoader.insertHistActivityWS(12, 40, new Date(), req, res, 1, resultErr, start, username);

		return res;
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
					services.updateLinkAccount(reg_spaj, id_simultan, null, username);

					error = false;
					message = "Successfully link account individual";
				} else if (type_register.equals(2)) { // Corporate
					// Update mcl_id_employee
					services.updateLinkAccount(null, null, mcl_id_employee, username);

					error = false;
					message = "Successfully link account corporate";
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

		String no_hp = reqValidateOTP.getPhone_no();
		Integer otp_no = reqValidateOTP.getOtp_no();
		Integer menu_id = reqValidateOTP.getMenu_id();
		try {
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

				Boolean individu = false;
				Boolean corporate = false;

				if ((reg_spaj != null) && (mcl_id_employee == null)) {
					individu = true;
					corporate = false;
				} else if ((reg_spaj == null) && (mcl_id_employee != null)) {
					individu = false;
					corporate = true;
				} else {
					individu = true;
					corporate = true;
				}

				if ((individu.equals(true) && corporate.equals(false))
						|| (individu.equals(true) && corporate.equals(true))) { // Forgot Password Individual
					User dataUserIndividual = services.selectUserIndividual(username);
					String no_hp = dataUserIndividual.getNo_hp() != null ? dataUserIndividual.getNo_hp()
							: dataUserIndividual.getNo_hp2();

					if (no_hp == null) { // Check No HP empty or not
						error = true;
						message = "Data phone number is empty";
						sms.put("sms", null);
						sms.put("no_polis", null);
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
							data.put("poicy_type", "individual");
							sms.put("sms", no_hp);
							sms.put("no_polis", dataUserIndividual.getMspo_policy_no());
						} else {
							error = true;
							message = "Phone number is blacklisted";
							data.put("ishavingphonenumber", true);
							data.put("contacts", sms);
							data.put("username", null);
							sms.put("sms", no_hp);
							sms.put("no_polis", dataUserIndividual.getMspo_policy_no());
							resultErr = "No telepon sedang dalam masa blacklist";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					}
				} else { // Forgot Password Corporate
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
								data.put("poicy_type", "corporate");
								sms.put("sms", no_hp);
								sms.put("no_polis", dataUserCorporate.getNo_polis());
							} else {
								error = true;
								message = "Phone number is blacklisted";
								data.put("ishavingphonenumber", true);
								data.put("contacts", sms);
								data.put("username", null);
								sms.put("sms", no_hp);
								sms.put("no_polis", dataUserCorporate.getNo_polis());
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
					String no_hp = dataForgotUsername.getNo_hp() != null ? dataForgotUsername.getNo_hp()
							: dataForgotUsername.getNo_hp2();

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