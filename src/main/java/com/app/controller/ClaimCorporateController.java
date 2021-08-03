package com.app.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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

import com.app.model.request.RequestCheckEnableClaimCorp;
import com.app.services.VegaServices;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class ClaimCorporateController {
	
	private static final Logger logger = LogManager.getLogger(FinancialTransactionController.class);

	@Value("${path.storage.mpolicy}")
	private String storageMpolicy;
	
	@Value("${path.storage.withdraw}")
	private String storageWithdraw;
	
	@Value("${path.storage.withdrawdb}")
	private String storageWithdrawDB;
	
	@Value("${path.storage.mpolicydb}")
	private String storageMpolicyDB;
	
	@Value("${path.storage.claim}")
	private String storageClaimMpolicy;

	@Value("${path.manfaatpdf.mpolicy}")
	private String manfaatpdfMpolicy;

	@Value("${path.logoBank.mpolicy}")
	private String pathLogoBankMpolicy;

	@Value("${path.news.mpolicy}")
	private String pathNewsMpolicy;

	@Value("${path.pdf.faq}")
	private String pathPdfFAQ;

	@Value("${path.direct.notification}")
	private String pathDirectNotification;

	@Value("${link.update.activity}")
	private String linkUpdateActivity;

	@Value("${link.send.email}")
	private String linkSendEmail;

	@Value("${path.pdf.formclaimsubmission}")
	private String pathFormClaimSubmission;

	@Value("${path.log.submitclaimsubmission}")
	private String pathLogSubmitClaimSubmission;

	@Value("${path.log.submitclaimsubmissioncorporate}")
	private String pathLogSubmitClaimSubmissionCorp;

	@Value("${path.document.claimcorporate}")
	private String pathDocumentClaimcorporate;
	
	@Value("${path.download.endorsehr}")
	private String downloadEndorseHr;
	

	@Value("${path.ekamedicare.path}")
	private String ekamedicarepath;

	@Autowired
	private VegaServices services;

	@Autowired
	private VegaCustomResourceLoader customResourceLoader;

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
	private DateFormat df3 = new SimpleDateFormat("dd MMM yyyy");
	private NumberFormat nfZeroTwo = new DecimalFormat("#,##0.00;(#,##0.00)");
	private NumberFormat nfZeroFour = new DecimalFormat("#,##0.0000;(#,##0.0000)");


	
	
	@RequestMapping(value = "/checkenableclaimcorp", produces = "application/json", method = RequestMethod.POST)
	public String checkEnableClaimCorp(@RequestBody RequestCheckEnableClaimCorp requestCheckEnableClaimCorp,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestCheckEnableClaimCorp);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestCheckEnableClaimCorp.getUsername();
		String key = requestCheckEnableClaimCorp.getKey();
		String no_polis = requestCheckEnableClaimCorp.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				BigDecimal checkData = services.selectCheckEnableClaimCorp(no_polis);

				if (checkData == null) {
					data.put("enable_claim_corp", false);
				} else if (checkData.intValue() == 2) {
					data.put("enable_claim_corp", true);
				} else {
					data.put("enable_claim_corp", false);
				}

				error = false;
				message = "Success get data";
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed check";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
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
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 84, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

}
