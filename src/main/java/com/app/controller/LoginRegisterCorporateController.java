package com.app.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.services.VegaServices;
import com.app.model.LstUserSimultaneous;
import com.app.model.UserCorporate;
import com.app.model.request.RequestCreateUserCorporate;
import com.app.model.request.RequestSignUpCorporate;
import com.app.model.request.RequestUnlinkAccount;
import com.app.utils.CustomResourceLoader;
import com.app.utils.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class LoginRegisterCorporateController {

	private static final Logger logger = LogManager.getLogger(LoginRegisterCorporateController.class);

	@Autowired
	private VegaServices services;

	@Autowired
	private CustomResourceLoader customResourceLoader;

	private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping(value = "/signupcorporate", produces = "application/json", method = RequestMethod.POST)
	public String signUpCorporate(@RequestBody RequestSignUpCorporate requestSignUpCorporate,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSignUpCorporate);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String no_polis = requestSignUpCorporate.getNo_polis();
		String kode = requestSignUpCorporate.getKode();
		String dob = requestSignUpCorporate.getDob();
		try {
			Boolean checkNoPolis = no_polis.substring(0, 1).matches("[0-9]");

			UserCorporate checkUserCorporateRegister = services.selectCheckUserCorporateRegister(no_polis.trim(),
					kode.replaceAll("[^0-9]", "").trim() + "A", dob);

			if (checkNoPolis == false) {
				// Validasi No. Polis
				error = true;
				message = "Wrong policy number format";
				resultErr = "Karakter pertama harus angka tidak boleh huruf";
				logger.error("Path: " + request.getServletPath() + " No. Polis: " + no_polis + ", Kode: " + kode
						+ ", Error: " + resultErr);
			} else if (checkUserCorporateRegister == null) {
				// User not found
				error = true;
				message = "User not found in database";
				resultErr = "User tidak ditemukan didatabase";
				logger.error("Path: " + request.getServletPath() + " No. Polis: " + no_polis + ", Kode: " + kode
						+ ", DOB: " + dob + ", Error: " + resultErr);
			} else {
				String reg_spaj = checkUserCorporateRegister.getReg_spaj();
				String mcl_id_employee = checkUserCorporateRegister.getMcl_id_employee();
				Date mspo_end_date = checkUserCorporateRegister.getMspo_end_date();
				Integer checkUserCorporateExistsMpolis = services.selectCheckUserCorporateExistsMpolis(mcl_id_employee);
				ArrayList<UserCorporate> checkListPolisCorporate = services.selectListPolisCorporate(mcl_id_employee);

				// Check Polis inforce or not
				Boolean policy_corporate_notinforce = false;
				LocalDate now = LocalDate.now();
				LocalDate endDateParse = LocalDate.parse(df1.format(mspo_end_date));
				if (endDateParse.compareTo(now) < 0) {
					policy_corporate_notinforce = true;
				} else {
					policy_corporate_notinforce = false;
				}

				if (checkUserCorporateExistsMpolis.equals(1)) {
					// User registered M-Polis
					error = true;
					message = "The user is already registered with M-Polis";
					resultErr = "User sudah terdaftar pada M-Polis";
					logger.error("Path: " + request.getServletPath() + " No. Polis: " + no_polis + ", Kode: " + kode
							+ ", Error: " + resultErr);
				} else if (checkListPolisCorporate.isEmpty()) {
					// List Polis Empty
					error = true;
					message = "List policy empty";
					resultErr = "List polis kosong";
					logger.error("Path: " + request.getServletPath() + " No. Polis: " + no_polis + ", Kode: " + kode
							+ ", Error: " + resultErr);
				} else if (policy_corporate_notinforce.equals(true)) {
					// Polis corporate not inforce
					error = true;
					message = "Policy corporate not inforce";
					resultErr = "Polis corporate sudah tidak aktif";
					logger.error("Path: " + request.getServletPath() + " No. Polis: " + no_polis + ", Kode: " + kode
							+ ", Error: " + resultErr);
				} else {
					error = false;
					message = "No. Polis found in database";
					data.put("no_polis", no_polis);
					data.put("reg_spaj", reg_spaj);
					data.put("mcl_id_employee", mcl_id_employee);
				}
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " No. Polis: " + no_polis + ", Kode: " + kode
					+ ", Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 68, new Date(), req, res, 1, resultErr, start, no_polis);

		return res;
	}

	@RequestMapping(value = "/createusercorporate", produces = "application/json", method = RequestMethod.POST)
	public String createUserCorporate(@RequestBody RequestCreateUserCorporate requestCreateUserCorporate,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestCreateUserCorporate);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestCreateUserCorporate.getUsername();
		String password = requestCreateUserCorporate.getPassword();
		String mcl_id_employee = requestCreateUserCorporate.getMcl_id_employee();
		try {
			Integer checkUserCorporateExistsMpolis = services.selectCheckUserCorporateExistsMpolis(mcl_id_employee);
			Integer checkUsername = services.selectCheckUsername(username.toLowerCase());

			if (checkUserCorporateExistsMpolis > 0) {
				error = true;
				message = "The user is already registered with M-Polis";
				resultErr = "User sudah terdaftar pada M-Polis";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + ", MCL_ID_EMPLOYEE: "
						+ mcl_id_employee + ", Error: " + resultErr);
			} else if (checkUsername.equals(1)) {
				error = true;
				message = "Username already exist in database. Please choose another username";
				resultErr = "Username sudah terpakai di database M-Polis";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + ", MCL_ID_EMPLOYEE: "
						+ mcl_id_employee + ", Error: " + resultErr);
			} else {
				try {
					Date date = new Date();
					String strDateFormat = "dd/MM/yyyy HH:mm:ss";
					DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
					String formattedDate = dateFormat.format(date);
					LstUserSimultaneous lstUserSimultaneous = new LstUserSimultaneous();
					lstUserSimultaneous.setUSERNAME(username);
					lstUserSimultaneous.setPASSWORD(password);
					lstUserSimultaneous.setFLAG_ACTIVE(1);
					lstUserSimultaneous.setCREATE_DATE(new Date());
					lstUserSimultaneous.setID_SIMULTAN(null);
					lstUserSimultaneous.setREG_SPAJ(null);
					lstUserSimultaneous.setDATE_CREATED_JAVA(formattedDate);
					lstUserSimultaneous.setMCL_ID_EMPLOYEE(mcl_id_employee);

					// Insert new user
					services.insertNewuser(lstUserSimultaneous);
					error = false;
					message = "Register Success";
				} catch (Exception e) {
					// Error username sudah ada didatabase
					error = true;
					message = "Username already exist in database. Please choose another username";
					resultErr = "bad exception " + e;
					logger.error("Path: " + request.getServletPath() + " Username: " + username + ", MCL_ID_EMPLOYEE: "
							+ mcl_id_employee + ", Error: " + resultErr);
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
		customResourceLoader.insertHistActivityWS(12, 69, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/unlinkaccount", produces = "application/json", method = RequestMethod.POST)
	public String unlinkAccount(@RequestBody RequestUnlinkAccount requestUnlinkAccount, HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestUnlinkAccount);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestUnlinkAccount.getUsername();
		String key = requestUnlinkAccount.getKey();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				LstUserSimultaneous checkUsername = services.selectDataLstUserSimultaneous(username);

				if (checkUsername == null) {
					// Handle username not registered
					error = true;
					message = "Username not found";
					resultErr = "Username yang dimasukkan tidak terdaftar";
					logger.error(
							"Path: " + request.getServletPath() + ", Username: " + username + ", Error: " + resultErr);
				} else {
					services.updateUnlinkAccountCorporate(username);

					error = false;
					message = "Successfully unlink policy corporate";
				}
			} else {
				error = true;
				message = "Failed unlink account corporate";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
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
		customResourceLoader.insertHistActivityWS(12, 74, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
}