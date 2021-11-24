package com.app.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.app.services.RegistrationIndividuSvc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private final AccountManagementCons constant;
	private final CommonUtils utils;
	private final RegistrationIndividuSvc registrationIndividuSvc;
	private final ServiceEmail serviceEmail;
	private final ServiceOTP serviceOTP;
	private final VegaCustomResourceLoader customResourceLoader;
	private final VegaServices services;


	@Value("${link.update.activity}")
	private String linkUpdateActivity;

	@Value("${link.fcm.google}")
	private String linkFcmGoogle;

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat df2 = new SimpleDateFormat("ddMMyyyy");

	@Autowired
	public LoginRegisterIndividualController(VegaServices services, VegaCustomResourceLoader customResourceLoader,
											 AccountManagementCons constant, ServiceEmail serviceEmail, ServiceOTP serviceOTP,
											 CommonUtils utils, RegistrationIndividuSvc registrationIndividuSvc) {
		this.services = services;
		this.customResourceLoader = customResourceLoader;
		this.constant = constant;
		this.serviceEmail = serviceEmail;
		this.serviceOTP = serviceOTP;
		this.utils = utils;
		this.registrationIndividuSvc = registrationIndividuSvc;
	}

	@RequestMapping(value = "/registerqr", produces = "application/json", method = RequestMethod.POST)
	public String registerQr(@RequestBody RequestRegisterQR requestRegisterQR, HttpServletRequest request) {
		return registrationIndividuSvc.register(requestRegisterQR, request);
	}

	@RequestMapping(value = "/findaccount", produces = "application/json", method = RequestMethod.POST)
	public String registerWithoutQr(@RequestBody RequestFindAccount requestFindAccount, HttpServletRequest request)
			throws Exception {
		return registrationIndividuSvc.findAccount(requestFindAccount, request);
	}

	@RequestMapping(value = "/validatepolicy", produces = "application/json", method = RequestMethod.POST)
	public String validatePolicy(@RequestBody RequestValidatePolicy requestValidatePolicy, HttpServletRequest request) {
		return registrationIndividuSvc.validatePolicy(requestValidatePolicy, request);
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