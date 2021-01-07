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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.utils.ResponseMessage;
import com.app.utils.CustomResourceLoader;
import com.app.services.VegaServices;
import com.app.model.request.RequestLogin;
import com.app.model.LstUserSimultaneous;
import com.app.model.User;
import com.app.model.UserCorporate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.swagger.annotations.ApiOperation;

@RestController
public class LoginController {
	
	private static final Logger logger = LogManager.getLogger(LoginController.class);

	@Autowired
	private VegaServices vegaServices;

	@Autowired
	private CustomResourceLoader customResourceLoader;

	@Value("${link.fcm.google}")
	private String linkFcmGoogle;

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	
	@ApiOperation(value="Service Login")
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
			LstUserSimultaneous user1 = vegaServices.selectLoginAuthenticate(lstUserSimultaneous);

			HashMap<String, Object> dataConfiguration = vegaServices.configuration();
			Integer time_idle = Integer.parseInt((String) dataConfiguration.get("TIME_IDLE"));

			LstUserSimultaneous checkIndividuOrCorporate = vegaServices.selectDataLstUserSimultaneous(username);

			if (checkIndividuOrCorporate != null) {
				String reg_spaj = checkIndividuOrCorporate.getREG_SPAJ();
				String mcl_id_employee = checkIndividuOrCorporate.getMCL_ID_EMPLOYEE();

				Boolean individu = false;
				Boolean corporate = false;
				Boolean policy_corporate_notinforce = false;
				Boolean user_corporate_notactive = false;

				if ((reg_spaj != null) && (mcl_id_employee == null)) {
					individu = true;
					corporate = false;
				} else if ((reg_spaj == null) && (mcl_id_employee != null)) {
					individu = false;
					corporate = true;

					ArrayList<UserCorporate> listPolisCorporate = vegaServices.selectListPolisCorporate(mcl_id_employee);
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
				} else {
					individu = true;
					corporate = true;

					ArrayList<UserCorporate> listPolisCorporate = vegaServices.selectListPolisCorporate(mcl_id_employee);
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

				if ((individu.equals(true) && corporate.equals(false))
						|| (individu.equals(true) && corporate.equals(true))) { // Login Individual
					User dataActivityUser = vegaServices.selectUserIndividual(username);
					// Check apakah username terdaftar/ tidak
					if (dataActivityUser != null) {
						Integer countDeathClaim = vegaServices.selectCountDeathClaim(username);
						// Check apakah username tersebut mengandung polis death claim
						if (countDeathClaim == 0) {
							// Check apakah token yang ada didatabase kosong/ tidak
							if (dataActivityUser.getLast_login_device() != null) {
								// Check karena token ada, apakah token yang dikirim sama dengan yang didb
								if (dataActivityUser.getLast_login_device().equals(lastLoginDevice)) {
									// Check password yang diinputkan benar atau salah
									if (user1.getPASSWORD().equals(password)) {
										ArrayList<User> list = vegaServices.selectDetailedPolis(username);
										// Check apakah username tersebut punya list polis/ tidak
										if (list.size() > 0) {
											String today = df.format(new Date());
											lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
											lstUserSimultaneous.setUPDATE_DATE_TIME(today);
											vegaServices.updateUserKeyName(lstUserSimultaneous);
											key = user1.getKEY();

											error = false;
											message = "Login success";
											data.put("individual", individu);
											data.put("corporate", corporate);
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
												ArrayList<User> list = vegaServices.selectDetailedPolis(username);
												// Check apakah username tersebut memiliki list polis atau tidak
												if (list.size() > 0) {
													try {
														customResourceLoader.postGoogle(linkFcmGoogle, username,
																dataActivityUser.getLast_login_device());

														String today = df.format(new Date());
														lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
														lstUserSimultaneous.setUPDATE_DATE_TIME(today);
														vegaServices.updateUserKeyName(lstUserSimultaneous);
														key = user1.getKEY();

														error = false;
														message = "Login success";
														data.put("individual", individu);
														data.put("corporate", corporate);
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
										ArrayList<User> list = vegaServices.selectDetailedPolis(username);
										// Check apakah username tersebut memiliki list polis atau tidak
										if (list.size() > 0) {
											String today = df.format(new Date());
											lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
											lstUserSimultaneous.setUPDATE_DATE_TIME(today);
											vegaServices.updateUserKeyName(lstUserSimultaneous);
											key = user1.getKEY();

											error = false;
											message = "Login success";
											data.put("individual", individu);
											data.put("corporate", corporate);
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
				} else { // Login Corporate
					UserCorporate dataUserCorporate = vegaServices.selectUserCorporate(username);
					// Check apakah username terdaftar/ tidak
					if (dataUserCorporate != null) {
						// Check apakah token yang ada didatabase kosong/ tidak
						if (dataUserCorporate.getLast_login_device() != null) {
							// Check karena token ada, apakah token yang dikirim sama dengan yang didb
							if (dataUserCorporate.getLast_login_device().equals(lastLoginDevice)) {
								// Check password yang diinputkan benar atau salah
								if (user1.getPASSWORD().equals(password)) {
									ArrayList<UserCorporate> list = vegaServices.selectListPolisCorporate(mcl_id_employee);
									// Check apakah username tersebut punya list polis/ tidak
									if (list.size() > 0) {
										String today = df.format(new Date());
										lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
										lstUserSimultaneous.setUPDATE_DATE_TIME(today);
										vegaServices.updateUserKeyName(lstUserSimultaneous);
										key = user1.getKEY();

										error = false;
										message = "Login success";
										data.put("individual", individu);
										data.put("corporate", corporate);
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
											ArrayList<UserCorporate> list = vegaServices
													.selectListPolisCorporate(mcl_id_employee);
											// Check apakah username tersebut memiliki list polis atau tidak
											if (list.size() > 0) {
												try {
													customResourceLoader.postGoogle(linkFcmGoogle, username,
															dataUserCorporate.getLast_login_device());

													String today = df.format(new Date());
													lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
													lstUserSimultaneous.setUPDATE_DATE_TIME(today);
													vegaServices.updateUserKeyName(lstUserSimultaneous);
													key = user1.getKEY();

													error = false;
													message = "Login success";
													data.put("individual", individu);
													data.put("corporate", corporate);
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
									ArrayList<UserCorporate> list = vegaServices.selectListPolisCorporate(mcl_id_employee);
									// Check apakah username tersebut memiliki list polis atau tidak
									if (list.size() > 0) {
										String today = df.format(new Date());
										lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
										lstUserSimultaneous.setUPDATE_DATE_TIME(today);
										key = user1.getKEY();

										error = false;
										message = "Login success";
										data.put("individual", individu);
										data.put("corporate", corporate);
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

}
