package com.app.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.services.VegaServices;
import com.app.model.ClaimCorporate;
import com.app.model.ClaimLimit;
import com.app.model.ClaimSubmission;
import com.app.model.ClaimSubmissionCorporate;
import com.app.model.CostFinancialTransaction;
import com.app.model.DetailClaimCorporate;
import com.app.model.DetailDestSwitching;
import com.app.model.DetailRedirection;
import com.app.model.DetailSwitching;
import com.app.model.DetailWithdraw;
import com.app.model.DropdownPolicyAlteration;
import com.app.model.Endorse;
import com.app.model.Fund;
import com.app.model.Pemegang;
import com.app.model.ProductUtama;
import com.app.model.Rekening;
import com.app.model.SwitchingRedirection;
import com.app.model.Topup;
import com.app.model.UnitLink;
import com.app.model.User;
import com.app.model.UserCorporate;
import com.app.model.ViewMclFirst;
import com.app.model.Withdraw;
import com.app.model.request.RequestCheckPhoneNumberNasabah;
import com.app.model.request.RequestCheckRekeningNasabah;
import com.app.model.request.RequestCheckStatusTransaction;
import com.app.model.request.RequestClaimLimit;
import com.app.model.request.RequestClaimSubmission;
import com.app.model.request.RequestClaimSubmissionCorporate;
import com.app.model.request.RequestCostWithdraw;
import com.app.model.request.RequestDetailClaimCorporate;
import com.app.model.request.RequestDocumentClaimSubmissionCorporate;
import com.app.model.request.RequestDownloadFileClaimSubmission;
import com.app.model.request.RequestDownloadProofTransaction;
import com.app.model.request.RequestDropdownClaimsubmission;
import com.app.model.request.RequestDropdownPolicyAlteration;
import com.app.model.request.RequestFurtherClaimSubmission;
import com.app.model.request.RequestGetInfoTopup;
import com.app.model.request.RequestGetTopupList;
import com.app.model.request.RequestListClaimCorporate;
import com.app.model.request.RequestListClaimSubmission;
import com.app.model.request.RequestListClaimSubmissionCorporate;
import com.app.model.request.RequestListClaimHr;
import com.app.model.request.RequestListSwitching;
import com.app.model.request.RequestListSwitchingRedirection;
import com.app.model.request.RequestListWithdraw;
import com.app.model.request.RequestRekening;
import com.app.model.request.RequestSubmitClaimSubmission;
import com.app.model.request.RequestSubmitClaimSubmissionCorporate;
import com.app.model.request.RequestSubmitSwitching;
import com.app.model.request.RequestSubmitSwitchingRedirection;
import com.app.model.request.RequestSubmitWithdraw;
import com.app.model.request.RequestSwitchingRedirection;
import com.app.model.request.RequestTopup;
import com.app.model.request.RequestUploadDeleteFileClaimSub;
import com.app.model.request.RequestUploadDeleteFileClaimSubCorp;
import com.app.model.request.RequestValidateSwitchingRedirection;
import com.app.model.request.RequestViewClaimHr;
import com.app.model.request.RequestViewClaimSubmission;
import com.app.model.request.RequestViewClaimSubmissionCorporate;
import com.app.model.request.RequestViewMclFirst;
import com.app.model.request.RequestViewPolicyAlteration;
import com.app.model.request.RequestViewSwitching;
import com.app.model.request.RequestViewSwitchingRedirection;
import com.app.model.request.RequestViewUserInputTopup;
import com.app.model.request.RequestViewWithdraw;
import com.app.model.request.RequestWithdraw;
import com.app.utils.VegaCustomResourceLoader;
import com.app.utils.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
public class FinancialTransactionController {

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

	@RequestMapping(value = "/checkstatustransaction", produces = "application/json", method = RequestMethod.POST)
	public String checkStatusTransaction(@RequestBody RequestCheckStatusTransaction requestCheckStatusTransaction,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestCheckStatusTransaction);
		String res = null;
		Boolean error = true;
		String resultErr = null;
		String message = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestCheckStatusTransaction.getUsername();
		String key = requestCheckStatusTransaction.getKey();
		String no_polis = requestCheckStatusTransaction.getNo_polis();
		Integer menu_id_transaction = requestCheckStatusTransaction.getMenu_id_transaction();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get REG_SPAJ
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);

				Integer dataCheckStatus = services.selectCheckStatusTransaction(dataSpaj.getReg_spaj());
				if (dataCheckStatus == null) {
					error = false;
					message = "Successfully get data";
					data.put("enable_button_submit", true);
				} else {
					// Cek bila user ingin topup
					if (menu_id_transaction.equals(2) || menu_id_transaction.equals(20)) {
						error = false;
						message = "Successfully get data";
						data.put("enable_button_submit", true);

						// Cek user apabila ingin switching/ redirection/ switching & redirection
					} else if (menu_id_transaction.equals(4) || menu_id_transaction.equals(3)
							|| menu_id_transaction.equals(19)) {
						// Cek status transaksi terakhirnya
						if (dataCheckStatus > 0) {
							error = false;
							message = "Successfully get data";
							data.put("enable_button_submit", false);
						} else {
							error = false;
							message = "Successfully get data";
							data.put("enable_button_submit", true);
						}
					}
				}
			} else {
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 54, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/rekening", produces = "application/json", method = RequestMethod.POST)
	public String rekening(@RequestBody RequestRekening requestRekening, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestRekening);
		String res = null;
		Boolean error = true;
		String resultErr = null;
		String message = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestRekening.getUsername();
		String key = requestRekening.getKey();
		String no_polis = requestRekening.getNo_polis();
		Integer language_id = requestRekening.getLanguage_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);
				if (dataSpaj != null) {
					Rekening dataRekening = services.selectRekeningForTopup(dataSpaj.getReg_spaj());
					if (dataRekening != null) {
						data.put("bank", dataRekening.getBank());
						data.put("rekening", dataRekening.getRekening());

						if (dataRekening.getBank().equalsIgnoreCase("bank sinarmas")) {
							data.put("transfer_type ", 0);
						} else {
							data.put("transfer_type", 1);
						}

						if (language_id == 1) {
							Integer checkPercentage = services.selectPersentaseBiayaTopup(dataSpaj.getReg_spaj());
							if (checkPercentage != null) {
								data.put("wording_nav", "Biaya Top Up sebesar " + checkPercentage
										+ "% dari Premi Top Up.\r\n"
										+ "Tanggal yang digunakan untuk Harga NAV penempatan adalah tanggal dilakukannya proses penempatan di Kantor Pusat.");
							} else {
								data.put("wording_nav", "Biaya Top Up sebesar 5% dari Premi Top Up.\r\n"
										+ "Tanggal yang digunakan untuk Harga NAV penempatan adalah tanggal dilakukannya proses penempatan di Kantor Pusat.");
							}
						} else {
							Integer checkPercentage = services.selectPersentaseBiayaTopup(dataSpaj.getReg_spaj());
							if (checkPercentage != null) {
								data.put("wording_nav", "Top up fee of " + checkPercentage
										+ "% from Top up Premium.\r\n"
										+ "The price used for placement NAV date is do the assignment at Head Office.");
							} else {
								data.put("wording_nav", "Top up fee of 5% from Top up Premium.\r\n"
										+ "The price used for placement NAV date is do the assignment at Head Office.");
							}
						}

						File folder = new File(pathLogoBankMpolicy);
						File files[] = folder.listFiles();
						for (File f : files) {
							if (dataRekening.getBank().equalsIgnoreCase("bank sinarmas")) {
								if (f.getName().equalsIgnoreCase("sinarmas.png")) {
									String dir3 = folder + File.separator + f.getName();
									byte[] fileContent = FileUtils.readFileToByteArray(new File(dir3));
									String strImg = Base64.getEncoder().encodeToString(fileContent);
									data.put("image", strImg);
									data.put("wording", customResourceLoader.getStepTransaction(language_id,
											dataRekening.getBank()));
								}
							} else if (dataRekening.getBank().equalsIgnoreCase("bank sinarmas syariah")) {
								if (f.getName().equalsIgnoreCase("sinarmassyariah.png")) {
									String dir3 = folder + File.separator + f.getName();
									byte[] fileContent = FileUtils.readFileToByteArray(new File(dir3));
									String strImg = Base64.getEncoder().encodeToString(fileContent);
									data.put("image", strImg);
									data.put("wording", customResourceLoader.getStepTransaction(language_id,
											dataRekening.getBank()));
								}
							} else if (dataRekening.getBank().equalsIgnoreCase("bank bukopin")) {
								if (f.getName().equalsIgnoreCase("bukopin.png")) {
									String dir3 = folder + File.separator + f.getName();
									byte[] fileContent = FileUtils.readFileToByteArray(new File(dir3));
									String strImg = Base64.getEncoder().encodeToString(fileContent);
									data.put("image", strImg);
									data.put("wording", customResourceLoader.getStepTransaction(language_id,
											dataRekening.getBank()));
								}
							} else if (dataRekening.getBank().equalsIgnoreCase("bank bukopin syariah")) {
								if (f.getName().equalsIgnoreCase("bukopinsyariah.png")) {
									String dir3 = folder + File.separator + f.getName();
									byte[] fileContent = FileUtils.readFileToByteArray(new File(dir3));
									String strImg = Base64.getEncoder().encodeToString(fileContent);
									data.put("image", strImg);
									data.put("wording", customResourceLoader.getStepTransaction(language_id,
											dataRekening.getBank()));
								}
							} else if (dataRekening.getBank().equalsIgnoreCase("bank jatim")) {
								if (f.getName().equalsIgnoreCase("jatim.png")) {
									String dir3 = folder + File.separator + f.getName();
									byte[] fileContent = FileUtils.readFileToByteArray(new File(dir3));
									String strImg = Base64.getEncoder().encodeToString(fileContent);
									data.put("image", strImg);
									data.put("wording", customResourceLoader.getStepTransaction(language_id,
											dataRekening.getBank()));
								}
							} else if (dataRekening.getBank().equalsIgnoreCase("bank btn syariah")) {
								if (f.getName().equalsIgnoreCase("jatimsyariah.png")) {
									String dir3 = folder + File.separator + f.getName();
									byte[] fileContent = FileUtils.readFileToByteArray(new File(dir3));
									String strImg = Base64.getEncoder().encodeToString(fileContent);
									data.put("image", strImg);
									data.put("wording", customResourceLoader.getStepTransaction(language_id,
											dataRekening.getBank()));
								}
							} else if (dataRekening.getBank().equalsIgnoreCase("bank bjb")) {
								if (f.getName().equalsIgnoreCase("bjb.png")) {
									String dir3 = folder + File.separator + f.getName();
									byte[] fileContent = FileUtils.readFileToByteArray(new File(dir3));
									String strImg = Base64.getEncoder().encodeToString(fileContent);
									data.put("image", strImg);
									data.put("wording", customResourceLoader.getStepTransaction(language_id,
											dataRekening.getBank()));
								}
							} else if (dataRekening.getBank().equalsIgnoreCase("bank jatim syariah")) {
								if (f.getName().equalsIgnoreCase("jatimsyariah.png")) {
									String dir3 = folder + File.separator + f.getName();
									byte[] fileContent = FileUtils.readFileToByteArray(new File(dir3));
									String strImg = Base64.getEncoder().encodeToString(fileContent);
									data.put("image", strImg);
									data.put("wording", customResourceLoader.getStepTransaction(language_id,
											dataRekening.getBank()));
								}
							} else {
								data.put("image", "");
								data.put("wording", "");
							}
						}

						error = false;
						message = "Successfully get data rekening";
					} else {
						// Handle data rekening kosong
						error = true;
						message = "Can't get data rekening";
						resultErr = "Data rekening kosong";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					// Handle data SPAJ Kosong
					error = true;
					message = "Can't get data rekening";
					resultErr = "SPAJ kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username dan key tidak cocok
				error = true;
				message = "Can't get data rekening";
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
		customResourceLoader.insertHistActivityWS(12, 12, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/gettopuplist", produces = "application/json", method = RequestMethod.POST)
	public String listTopup(@RequestBody RequestGetTopupList requestGetTopupList, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestGetTopupList);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestGetTopupList.getUsername();
		String key = requestGetTopupList.getKey();
		String no_polis = requestGetTopupList.getNo_polis();
		Integer pageNumber = requestGetTopupList.getPageNumber();
		Integer pageSize = requestGetTopupList.getPageSize();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang paramGetSpaj = new Pemegang();
				paramGetSpaj.setMspo_policy_no(no_polis);
				Pemegang regspaj = services.selectGetSPAJ(paramGetSpaj);
				ArrayList<HashMap<String, Object>> data1 = new ArrayList<HashMap<String, Object>>();
				if (regspaj != null) {
					ArrayList<Topup> list = services.selectListTopup(regspaj.getReg_spaj(), pageNumber, pageSize);
					if (list.size() > 0) {
						ListIterator<Topup> liter = list.listIterator();
						while (liter.hasNext()) {
							try {
								Topup m = liter.next();
								HashMap<String, Object> listTopup = new HashMap<>();

								listTopup.put("mpt_id", m.getMpt_id());
								listTopup.put("payment_date", df1.format(m.getCreated_date()));
								listTopup.put("reg_spaj", m.getReg_spaj());
								listTopup.put("lku_symbol", m.getLku_symbol());
								listTopup.put("total_payment", m.getMpt_jumlah());
								listTopup.put("status", m.getDescription());

								if (m.getDescription().equals("On progress")) {
									listTopup.put("status_id", 1);
									listTopup.put("status_proses_date", df1.format(m.getCreated_date()));
								} else if (m.getDescription().equals("Accepted")) {
									listTopup.put("status_id", 2);
									listTopup.put("status_proses_date", df1.format(m.getDate_status()));
								} else {
									listTopup.put("status_id", 3);
									listTopup.put("status_proses_date", df1.format(m.getDate_status()));
								}

								data1.add(listTopup);
								data.put("list_topup", data1);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}

						long valIDR1 = 49999000000L;
						long valUSD1 = 4999900;
						long valIDR2 = 99999000000L;
						long valUSD2 = 9999900;
						HashMap<String, Object> mapTemp = new HashMap<>();
						Topup dataStartEndActiveUser = services.selectStartEndActivePolicy(regspaj.getReg_spaj());
						BigDecimal totalTopupUser = services.selectSumJumlahTopup(regspaj.getReg_spaj());
						if (totalTopupUser != null) {
							if (!customResourceLoader.getDateNow().after(dataStartEndActiveUser.getOne_year())) {
								if (regspaj.getLku_id().equals("01")) {
									if (totalTopupUser.compareTo(new BigDecimal(valIDR1)) <= 0) {
										mapTemp.put("is_enable_topup", true);
									} else {
										mapTemp.put("is_enable_topup", false);
									}
								} else if (regspaj.getLku_id().equals("02")) {
									if (totalTopupUser.compareTo(new BigDecimal(valUSD1)) <= 0) {
										mapTemp.put("is_enable_topup", true);
									} else {
										mapTemp.put("is_enable_topup", false);
									}
								} else {
									mapTemp.put("is_enable_topup", true);
								}
							} else if (!customResourceLoader.getDateNow()
									.after(dataStartEndActiveUser.getEnd_active())) {
								if (regspaj.getLku_id().equals("01")) {
									if (totalTopupUser.compareTo(new BigDecimal(valIDR2)) <= 0) {
										mapTemp.put("is_enable_topup", true);
									} else {
										mapTemp.put("is_enable_topup", false);
									}
								} else if (regspaj.getLku_id().equals("02")) {
									if (totalTopupUser.compareTo(new BigDecimal(valUSD2)) <= 0) {
										mapTemp.put("is_enable_topup", true);
									} else {
										mapTemp.put("is_enable_topup", false);
									}
								} else {
									mapTemp.put("is_enable_topup", true);
								}
							} else {
								mapTemp.put("is_enable_topup", true);
							}
						} else {
							mapTemp.put("is_enable_topup", true);
						}

						data.put("other_information", mapTemp);

						error = false;
						message = "Success get top up list";
					} else {
						// Handle bila list topup kosong
						long valIDR1 = 49999000000L;
						long valUSD1 = 4999900;
						long valIDR2 = 99999000000L;
						long valUSD2 = 9999900;
						HashMap<String, Object> mapTemp = new HashMap<>();
						Topup dataStartEndActiveUser = services.selectStartEndActivePolicy(regspaj.getReg_spaj());
						BigDecimal totalTopupUser = services.selectSumJumlahTopup(regspaj.getReg_spaj());
						if (totalTopupUser != null) {
							if (!customResourceLoader.getDateNow().after(dataStartEndActiveUser.getOne_year())) {
								if (regspaj.getLku_id().equals("01")) {
									if (totalTopupUser.compareTo(new BigDecimal(valIDR1)) <= 0) {
										mapTemp.put("is_enable_topup", true);
									} else {
										mapTemp.put("is_enable_topup", false);
									}
								} else if (regspaj.getLku_id().equals("02")) {
									if (totalTopupUser.compareTo(new BigDecimal(valUSD1)) <= 0) {
										mapTemp.put("is_enable_topup", true);
									} else {
										mapTemp.put("is_enable_topup", false);
									}
								} else {
									mapTemp.put("is_enable_topup", true);
								}
							} else if (!customResourceLoader.getDateNow()
									.after(dataStartEndActiveUser.getEnd_active())) {
								if (regspaj.getLku_id().equals("01")) {
									if (totalTopupUser.compareTo(new BigDecimal(valIDR2)) <= 0) {
										mapTemp.put("is_enable_topup", true);
									} else {
										mapTemp.put("is_enable_topup", false);
									}
								} else if (regspaj.getLku_id().equals("02")) {
									if (totalTopupUser.compareTo(new BigDecimal(valUSD2)) <= 0) {
										mapTemp.put("is_enable_topup", true);
									} else {
										mapTemp.put("is_enable_topup", false);
									}
								} else {
									mapTemp.put("is_enable_topup", true);
								}
							} else {
								mapTemp.put("is_enable_topup", true);
							}
						} else {
							mapTemp.put("is_enable_topup", true);
						}

						data.put("other_information", mapTemp);
						data.put("list_topup", data1);

						error = false;
						message = "Failed get top up list";
					}
				} else {
					// Handle bila no spaj kosong
					error = true;
					message = "Failed get top up list";
					resultErr = "Reg SPAJ kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username and key not match
				error = true;
				message = "Failed get top up list";
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
		customResourceLoader.insertHistActivityWS(12, 31, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/viewuserinputtopup", produces = "application/json", method = RequestMethod.POST)
	public String viewTopup(@RequestBody RequestViewUserInputTopup requestViewUserInputTopup,
			HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewUserInputTopup);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestViewUserInputTopup.getUsername();
		String key = requestViewUserInputTopup.getKey();
		String no_polis = requestViewUserInputTopup.getNo_polis();
		String mpt_id = requestViewUserInputTopup.getMpt_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang paramSelectPolis = new Pemegang();
				paramSelectPolis.setMspo_policy_no(no_polis);
				if (services.selectNomorPolis(paramSelectPolis) != null) {
					Topup dataTopup = services.selectListTopupUsingID(mpt_id);
					// M-Polis
					if (dataTopup != null) {
						HashMap<String, Object> transaction = new HashMap<>();
						transaction.put("mpt_id", dataTopup.getMpt_id());
						transaction.put("current_status", dataTopup.getDescription());
						transaction.put("reason_fu", dataTopup.getReason_fu());
						transaction.put("topup_date", df2.format(dataTopup.getDate_status()));
						transaction.put("enable_button_download", true);
						data.put("transaction", transaction);

						HashMap<String, Object> payor_info = new HashMap<>();
						payor_info.put("product_name", dataTopup.getNewname());
						payor_info.put("policy_number", dataTopup.getMspo_policy_no());
						payor_info.put("policy_holder", dataTopup.getMcl_first());
						data.put("payor_info", payor_info);

						ArrayList<Object> fund = new ArrayList<>();
						ArrayList<Topup> list = services.selectListInvestasiTopup(mpt_id);

						ListIterator<Topup> liter = list.listIterator();
						while (liter.hasNext()) {
							try {
								Topup m = liter.next();
								HashMap<String, Object> listFund = new HashMap<>();

								String name = m.getLji_invest();
								Float percentage = m.getMpt_persen();
								listFund.put("name", name);
								listFund.put("percentage", percentage.intValue());
								listFund.put("mpt_jumlah", 0);
								fund.add(listFund);
								data.put("fund", fund);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}

						HashMap<String, Object> payment_method = new HashMap<>();
						if (dataTopup.getLku_symbol().equals("Rp.")) {
							payment_method.put("currency", "Rupiah");
						} else if (dataTopup.getLku_symbol().equals("US$")) {
							payment_method.put("currency", "Dollar");
						} else {
							payment_method.put("currency", "");
						}

						Rekening dataPaymentMethod = services.selectRekeningForTopup(dataTopup.getReg_spaj());
						// Check no rekening exists or not
						if (dataPaymentMethod != null) {
							payment_method.put("payment_method", dataPaymentMethod.getBank());
						} else {
							payment_method.put("payment_method", "-");
						}
						
						payment_method.put("unique_code", dataTopup.getUnique_code());
						payment_method.put("total_topup", nfZeroTwo.format(dataTopup.getMpt_jumlah()));
						payment_method.put("total_topup_process", nfZeroTwo.format(dataTopup.getMpt_jumlah_process()));
						data.put("payment_method", payment_method);

						error = false;
						message = "Get data user input success";
					} else {
						// Get SPAJ
						Pemegang paramGetSPAJ = new Pemegang();
						paramGetSPAJ.setMspo_policy_no(no_polis);
						Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

						Topup dataViewTopupPaper = services.selectViewTopupPaper(mpt_id, dataSPAJ.getReg_spaj());

						// Paper
						if (dataViewTopupPaper != null) {
							HashMap<String, Object> transaction = new HashMap<>();
							transaction.put("mpt_id", mpt_id);
							transaction.put("current_status", dataViewTopupPaper.getDescription());
							transaction.put("reason_fu", dataViewTopupPaper.getReason_fu());
							transaction.put("topup_date", df2.format(dataViewTopupPaper.getDate_status()));
							transaction.put("enable_button_download", false);
							data.put("transaction", transaction);

							HashMap<String, Object> payor_info = new HashMap<>();
							payor_info.put("product_name", dataViewTopupPaper.getNewname());
							payor_info.put("policy_number", dataViewTopupPaper.getMspo_policy_no());
							payor_info.put("policy_holder", dataViewTopupPaper.getMcl_first());
							data.put("payor_info", payor_info);

							ArrayList<Object> fund = new ArrayList<>();
							ArrayList<Topup> list = services.selectInvestasiTopupPaper(mpt_id,
									dataViewTopupPaper.getReg_spaj());
							ListIterator<Topup> liter = list.listIterator();
							while (liter.hasNext()) {
								try {
									Topup m = liter.next();
									HashMap<String, Object> listFund = new HashMap<>();

									String name = m.getLji_invest();
									BigDecimal mpt_jumlah = m.getMtu_jumlah();
									listFund.put("name", name);
									listFund.put("percentage", 0);
									listFund.put("mpt_jumlah", mpt_jumlah);
									fund.add(listFund);
									data.put("fund", fund);
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + e);
								}
							}

							HashMap<String, Object> payment_method = new HashMap<>();
							if (dataViewTopupPaper.getLku_symbol().equals("Rp.")) {
								payment_method.put("currency", "Rupiah");
							} else if (dataViewTopupPaper.getLku_symbol().equals("US$")) {
								payment_method.put("currency", "Dollar");
							} else {
								payment_method.put("currency", "");
							}

							Rekening dataPaymentMethod = services
									.selectRekeningForTopup(dataViewTopupPaper.getReg_spaj());
							// Check no rekening exists or not
							if (dataPaymentMethod != null) {
								payment_method.put("payment_method", dataPaymentMethod.getBank());
							} else {
								payment_method.put("payment_method", "-");
							}

							payment_method.put("total_topup", nfZeroTwo.format(dataViewTopupPaper.getMpt_jumlah()));
							payment_method.put("total_topup_process",
									nfZeroTwo.format(dataViewTopupPaper.getMpt_jumlah()));
							data.put("payment_method", payment_method);

							error = false;
							message = "Get data user input success";
						} else {
							error = true;
							message = "Get data user input failed";
							resultErr = "Id tidak ada pada tabel";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					}
				} else {
//					Handle nomor polis tidak ada di lst_user_simultaneous
					error = true;
					message = "Get data user input failed";
					resultErr = "Nomor polis tidak terdaftar di database user";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
//				Handle username & key tidak cocok
				error = true;
				message = "Get data user input failed";
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
		customResourceLoader.insertHistActivityWS(12, 32, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/getinfotopup", produces = "application/json", method = RequestMethod.POST)
	public String infoTopup(@RequestBody RequestGetInfoTopup requestGetInfoTopup, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestGetInfoTopup);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestGetInfoTopup.getUsername();
		String key = requestGetInfoTopup.getKey();
		String no_polis = requestGetInfoTopup.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Topup dataProduct = services.selectProductforTopup(no_polis);
				if (dataProduct != null) {
					error = false;
					message = "Get data success";
					data.put("product_name", dataProduct.getNewname());
					data.put("policy_holder", dataProduct.getMcl_first());
					data.put("currency", dataProduct.getMata_uang());
					data.put("currency_symbol", dataProduct.getLku_symbol());
					data.put("lku_id", dataProduct.getLku_id());
					data.put("topup_payor", dataProduct.getTopup_payor());

					if (dataProduct.getMata_uang().equalsIgnoreCase("rupiah")) {
						data.put("minimum_topup", 1000000);
					} else if (dataProduct.getMata_uang().equalsIgnoreCase("dollar")) {
						data.put("minimum_topup", 100);
					}

					long valIDR1 = 50000000000L;
					long valUSD1 = 5000000;
					long valIDR2 = 100000000000L;
					long valUSD2 = 10000000;
					Topup dataStartEndActiveUser = services.selectStartEndActivePolicy(dataProduct.getReg_spaj());
					BigDecimal totalTopupUser = services.selectSumJumlahTopup(dataProduct.getReg_spaj());
					if (totalTopupUser != null) {
						if (!customResourceLoader.getDateNow().after(dataStartEndActiveUser.getOne_year())) {
							if (dataProduct.getLku_id().equals("01")) {
								BigDecimal bigDecimal = new BigDecimal(valIDR1);
								data.put("remaining_topup", bigDecimal.subtract(totalTopupUser));
							} else if (dataProduct.getLku_id().equals("02")) {
								BigDecimal bigDecimal = new BigDecimal(valUSD1);
								data.put("remaining_topup", bigDecimal.subtract(totalTopupUser));
							} else {
								data.put("remaining_topup", 0);
							}
						} else if (!customResourceLoader.getDateNow().after(dataStartEndActiveUser.getEnd_active())) {
							if (dataProduct.getLku_id().equals("01")) {
								BigDecimal bigDecimal = new BigDecimal(valIDR2);
								data.put("remaining_topup", bigDecimal.subtract(totalTopupUser));
							} else if (dataProduct.getLku_id().equals("02")) {
								BigDecimal bigDecimal = new BigDecimal(valUSD2);
								data.put("remaining_topup", bigDecimal.subtract(totalTopupUser));
							} else {
								data.put("remaining_topup", 0);
							}
						} else {
							data.put("remaining_topup", 0);
						}
					} else {
						if (!customResourceLoader.getDateNow().after(dataStartEndActiveUser.getOne_year())) {
							if (dataProduct.getLku_id().equals("01")) {
								BigDecimal bigDecimal = new BigDecimal(valIDR1);
								data.put("remaining_topup", bigDecimal.subtract(BigDecimal.ZERO));
							} else if (dataProduct.getLku_id().equals("02")) {
								BigDecimal bigDecimal = new BigDecimal(valUSD1);
								data.put("remaining_topup", bigDecimal.subtract(BigDecimal.ZERO));
							} else {
								data.put("remaining_topup", 0);
							}
						} else if (!customResourceLoader.getDateNow().after(dataStartEndActiveUser.getEnd_active())) {
							if (dataProduct.getLku_id().equals("01")) {
								BigDecimal bigDecimal = new BigDecimal(valIDR2);
								data.put("remaining_topup", bigDecimal.subtract(BigDecimal.ZERO));
							} else if (dataProduct.getLku_id().equals("02")) {
								BigDecimal bigDecimal = new BigDecimal(valUSD2);
								data.put("remaining_topup", bigDecimal.subtract(BigDecimal.ZERO));
							} else {
								data.put("remaining_topup", 0);
							}
						} else {
							data.put("remaining_topup", 0);
						}
					}

					// Check Funds Default User
					ArrayList<Object> fundDefault = new ArrayList<>();
					ArrayList<Fund> listFundDefault = services.selectFundDefaultUser(dataProduct.getReg_spaj());
					if (listFundDefault.isEmpty()) {
						data.put("funds_default", fundDefault);
					} else {
						for (Integer i = 0; i < listFundDefault.size(); i++) {
							try {
								Fund m = listFundDefault.get(i);
								HashMap<String, Object> dataListFundDefault = new HashMap<>();

								String lji_id = m.getLji_id();
								String lji_invest = m.getLji_invest();
								BigDecimal mdu_persen = m.getMdu_persen();

								if (mdu_persen.intValue() != 0) {
									dataListFundDefault.put("lji_id", lji_id);
									dataListFundDefault.put("lji_invest", lji_invest);
									dataListFundDefault.put("mdu_persen", mdu_persen);
									fundDefault.add(dataListFundDefault);
								}
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}

						data.put("funds_default", fundDefault);
					}

					// Check Fund yang tersedia untuk user topup
					Topup topup = new Topup();
					topup.setLsbs_id(dataProduct.getLsbs_id());
					topup.setLku_id(dataProduct.getLku_id());
					topup.setReg_spaj(dataProduct.getReg_spaj());

					ArrayList<Object> fund = new ArrayList<>();
					ArrayList<Fund> listFund = services.selectFundOfProduct(topup);

					if (!listFund.isEmpty()) {
						ListIterator<Fund> liter = listFund.listIterator();
						while (liter.hasNext()) {
							try {
								Fund m = liter.next();
								HashMap<String, Object> datalistFund = new HashMap<>();

								String id = m.getFund_id();
								String name = m.getFund_invest();
								datalistFund.put("id", id);
								datalistFund.put("name", name);

								fund.add(datalistFund);
								data.put("funds", fund);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
					} else {
						data.put("funds", fund);
					}
				} else {
					error = true;
					message = "Get data failed";
					data = null;
					resultErr = "Data topup tidak ada";
				}
			} else {
//				Handle username & key tidak cocok
				error = true;
				message = "Get data user input failed";
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
		customResourceLoader.insertHistActivityWS(12, 33, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@SuppressWarnings("resource")
	@RequestMapping(value = "/submitdatatransaction", produces = "application/json", method = RequestMethod.POST)
	public String uploadProofOfTransaction(@RequestBody RequestTopup requestTopup, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestTopup);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestTopup.getUsername();
		String key = requestTopup.getKey();
		String no_polis = requestTopup.getNo_polis();
		Integer language_id = requestTopup.getLanguage_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get MPT_ID
				BigInteger mptId = services.selectGetMptId();

				// Get SPAJ
				Pemegang paramSelectSPAJ = new Pemegang();
				paramSelectSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);

				String kodeCabang = services.getKodeCabang(no_polis);

				// Check jumlah fund yang dimasukkan (jumlah fund harus 100)
				List<Float> sumPercentageFund = new ArrayList<>();
				JSONArray fundsCheck = new JSONArray(requestTopup.getFunds());
				float sum = 0;
				for (int i = 0; i < fundsCheck.length(); i++) {
					try {
						Float percentage = fundsCheck.getJSONObject(i).getFloat("percentage");
						sumPercentageFund.add(percentage);
						sum += sumPercentageFund.get(i);
					} catch (Exception e) {
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
					}
				}

				Integer sumInt = (int) sum;
				if (sumInt.equals(100)) {
					String mataUang = null;
					if (requestTopup.getLku_id().equals("01")) {
						mataUang = "IDR";
					} else if (requestTopup.getLku_id().equals("02")) {
						mataUang = "USD";
					}

					String payMethod = null;
					if (requestTopup.getTransfer_type().equals(0)) {
						payMethod = "VA";
					} else if (requestTopup.getTransfer_type().equals(1)) {
						payMethod = "Transfer";
					}

					// Insert MPOL_TRANS
					Topup paramInsert1 = new Topup();
					paramInsert1.setMpt_id(mptId.toString());
					paramInsert1.setDate_created_java1(customResourceLoader.getDatetimeJava1());
					paramInsert1.setReg_spaj(dataSPAJ.getReg_spaj());
					paramInsert1.setLt_id(requestTopup.getLt_id());
					paramInsert1.setLku_id(requestTopup.getLku_id());
					paramInsert1.setMpt_jumlah(requestTopup.getMpt_jumlah());
					paramInsert1.setDate_created_java2(customResourceLoader.getDatetimeJava());
					paramInsert1.setMpt_unit(null);
					paramInsert1.setLus_id(null);
					paramInsert1.setPayor_name(requestTopup.getPayor_name());
					paramInsert1.setPayor_occupation(requestTopup.getPayor_occupation());
					paramInsert1.setPayor_income(requestTopup.getPayor_income());
					paramInsert1.setPayor_source_income(requestTopup.getPayor_source_income());
					paramInsert1.setPath_bsb(storageMpolicy + File.separator + kodeCabang + File.separator + dataSPAJ.getReg_spaj() + File.separator
							+ "Bukti_Transaksi" + File.separator + mptId + ".pdf");
					paramInsert1.setUnique_code(requestTopup.getUnique_code());
					services.insertMstMpolTrans(paramInsert1);

					// Insert MPOL_TRANS_DET
					JSONArray funds = new JSONArray(requestTopup.getFunds());
					for (int i = 0; i < funds.length(); i++) {
						try {
							String lji_id = funds.getJSONObject(i).getString("lji_id");
							Float percentage = funds.getJSONObject(i).getFloat("percentage");

							Float percenVal = percentage / 100;
							BigDecimal newPercelVal = new BigDecimal(percenVal).add(BigDecimal.ZERO);
							BigDecimal mpt_jumlah = requestTopup.getMpt_jumlah().multiply(newPercelVal);

							Topup paramInsert2 = new Topup();
							paramInsert2.setMpt_id(mptId.toString());
							paramInsert2.setLji_id(lji_id);
							paramInsert2.setMpt_persen(percentage);
							paramInsert2.setMpt_jumlah_det(mpt_jumlah);
							paramInsert2.setMpt_unit(null);
							services.insertMstMpolTransDet(paramInsert2);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}

					// Upload Proof Transaction
					String basePath = storageMpolicy;
					File folder = new File(
							basePath + File.separator + kodeCabang + File.separator + dataSPAJ.getReg_spaj() + File.separator + "Bukti_Transaksi");
					if (!folder.exists()) {
						folder.mkdirs();
					}

					byte[] imageByte = Base64.getDecoder().decode(requestTopup.getBsb());
					String directory = folder + File.separator + mptId.toString() + ".pdf";
					new FileOutputStream(directory).write(imageByte);

					try {
						Document document = new Document();

						PdfWriter.getInstance(document, new FileOutputStream(directory));
						document.open();
						byte[] decoded = Base64.getDecoder().decode(requestTopup.getBsb().getBytes());
						Image image1 = Image.getInstance(decoded);

						int indentation = 0;
						float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
								- document.rightMargin() - indentation) / image1.getWidth()) * 100;

						image1.scalePercent(scaler);
						document.add(image1);
						document.close();
					} catch (Exception e) {
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
					}

					// Push Notification
					String messagePushNotif = null;

					if (language_id.equals(1)) {
						messagePushNotif = "Nasabah Yth, Bukti Pembayaran Premi Top Up Single sebesar " + mataUang + " "
								+ nfZeroTwo.format(requestTopup.getMpt_jumlah()) + " melalui " + payMethod
								+ " telah diterima";
					} else {
						messagePushNotif = "Dear Customer, Single Top Up Premium Payment Slip of " + mataUang + " "
								+ nfZeroTwo.format(requestTopup.getMpt_jumlah()) + " via " + payMethod
								+ " has been received";
					}

					customResourceLoader.pushNotif(username, messagePushNotif, no_polis, dataSPAJ.getReg_spaj(), 5, 0);

					error = false;
					message = "Top up submitted successfully";
				} else {
					// Handle total fund tidak 100%
					error = true;
					message = "Top up submitted failed";
					resultErr = "Total fund tidak 100%, tetapi yang diinput user adalah: " + sumInt;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key tidak cocok
				error = true;
				message = "Top up submitted failed";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			// Push Notification Telegram
			customResourceLoader.pushTelegram("@mfajarsep_bot",
					"Path: " + request.getServletPath() + " username: " + username + "," + " Error: " + e);

			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 37, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/downloadprooftransaction", produces = "application/json", method = RequestMethod.POST)
	public String downloadProofOfTransaction(
			@RequestBody RequestDownloadProofTransaction requestDownloadProofTransaction, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDownloadProofTransaction);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestDownloadProofTransaction.getUsername();
		String key = requestDownloadProofTransaction.getKey();
		String no_polis = requestDownloadProofTransaction.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				String pathFolder = storageMpolicy;
				String kodeCabang = services.getKodeCabang(no_polis);
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);
				File folder = new File(
						pathFolder + File.separator + kodeCabang + File.separator + dataSPAJ.getReg_spaj() + File.separator + "Bukti_Transaksi");

				// Path file
				String pathWS = folder.toString() + File.separator + requestDownloadProofTransaction.getMpt_id() + ".pdf";

				// Path file yang mau di download
				File file = new File(pathWS);

				try {
					// Content-Disposition
					response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

					// Content-Length
					response.setContentLength((int) file.length());

					BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
					BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

					byte[] buffer = new byte[1024];
					int bytesRead = 0;
					while ((bytesRead = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, bytesRead);
					}
					outStream.flush();
					inStream.close();

					error = false;
					message = "Download Success";
				} catch (Exception e) {
					error = true;
					message = "Download Failed";
					resultErr = "File tidak ada";
					logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
				}
			} else {
				error = true;
				message = "Download Failed";
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
		customResourceLoader.insertHistActivityWS(12, 36, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/redirection", produces = "application/json", method = RequestMethod.POST)
	public String switchingRedirection(@RequestBody RequestSwitchingRedirection requestSwitchingRedirection,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSwitchingRedirection);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> dataJenis = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestSwitchingRedirection.getUsername();
		String key = requestSwitchingRedirection.getKey();
		String no_polis = customResourceLoader.clearData(requestSwitchingRedirection.getNo_polis());
		Integer type = requestSwitchingRedirection.getType();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get basic information
				HashMap<String, Object> hashMapBasicInformation = new HashMap<>();
				User dataBasicInformation = services.selectBasicInformationForFinancialTransaction(no_polis);
				if (dataBasicInformation == null) {
					// Data basic information empty
					error = true;
					message = "Data customer empty";
					resultErr = "Data customer kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					String reg_spaj_result = dataBasicInformation.getReg_spaj();
					String lku_id_result = dataBasicInformation.getLku_id();
					Integer lsbs_id_result = dataBasicInformation.getLsbs_id();

					hashMapBasicInformation.put("no_polis", dataBasicInformation.getNo_polis());
					hashMapBasicInformation.put("reg_spaj", dataBasicInformation.getReg_spaj());
					hashMapBasicInformation.put("nm_pemegang", dataBasicInformation.getNm_pemegang());
					hashMapBasicInformation.put("status_polis", dataBasicInformation.getStatus_polis());
					hashMapBasicInformation.put("nama_product", dataBasicInformation.getNm_product());
					hashMapBasicInformation.put("lku_id", dataBasicInformation.getLku_id());
					if (type.equals(20)) { // Redirection
						// Get MPT ID
						BigInteger mptId = services.selectGetMptId();
						dataJenis.put("mpt_id_redirection", mptId.toString());

						// List Fund
						Topup topup = new Topup();
						topup.setLsbs_id(lsbs_id_result);
						topup.setLku_id(lku_id_result);
						topup.setReg_spaj(reg_spaj_result);

						ArrayList<Object> fund = new ArrayList<>();
						ArrayList<Fund> listFund = services.selectFundOfProduct(topup);

						ListIterator<Fund> liter = listFund.listIterator();
						while (liter.hasNext()) {
							try {
								Fund m = liter.next();
								HashMap<String, Object> datalistFund = new HashMap<>();

								String id = m.getFund_id();
								String name = m.getFund_invest();
								datalistFund.put("id", id);
								datalistFund.put("name", name);

								fund.add(datalistFund);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}

						// Investasi saat ini
						ArrayList<Object> fundDefault = new ArrayList<>();
						ArrayList<Fund> listFundDefault = services.selectFundDefaultUser(reg_spaj_result);
						for (Integer i = 0; i < listFundDefault.size(); i++) {
							try {
								Fund m = listFundDefault.get(i);
								HashMap<String, Object> dataListFundDefault = new HashMap<>();

								String lji_id = m.getLji_id();
								String lji_invest = m.getLji_invest();
								String lku_symbol = m.getLku_symbol();
								BigDecimal mdu_persen = m.getMdu_persen();

								if (mdu_persen.intValue() != 0) {
									dataListFundDefault.put("lji_id", lji_id);
									dataListFundDefault.put("lji_invest", lji_invest);
									dataListFundDefault.put("lku_symbol", lku_symbol);
									dataListFundDefault.put("mdu_persen", mdu_persen);

									fundDefault.add(dataListFundDefault);
								}
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}

						// Keterangan Biaya
						HashMap<String, Object> keterangan = new HashMap<>();
						keterangan.put("biaya", null);

						error = false;
						message = "Successfully get data";
						dataJenis.put("formulir_redirection", hashMapBasicInformation);
						dataJenis.put("fund_default", fundDefault);

						if (fund.isEmpty()) {
							dataJenis.put("list_fund", null);
						} else {
							dataJenis.put("list_fund", fund);
						}

						dataJenis.put("keterangan", keterangan);
						data.put("redirection", dataJenis);
					} else {
						// Type tidak terdaftar
						error = true;
						message = "Failed get data";
						resultErr = "Type yang dimasukkan tidak terdaftar";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				}
			} else {
				// USERNAME & KEY not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 50, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/switching", produces = "application/json", method = RequestMethod.POST)
	public String switching(@RequestBody RequestSwitchingRedirection requestSwitchingRedirection,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSwitchingRedirection);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> dataJenis = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestSwitchingRedirection.getUsername();
		String key = requestSwitchingRedirection.getKey();
		String no_polis = customResourceLoader.clearData(requestSwitchingRedirection.getNo_polis());
		Integer type = requestSwitchingRedirection.getType();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get basic information
				HashMap<String, Object> hashMapBasicInformation = new HashMap<>();
				User dataBasicInformation = services.selectBasicInformationForFinancialTransaction(no_polis);
				if (dataBasicInformation == null) {
					// Data basic information empty
					error = true;
					message = "Data customer empty";
					resultErr = "Data customer kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					String reg_spaj_result = dataBasicInformation.getReg_spaj();
					String lku_id_result = dataBasicInformation.getLku_id();
					Integer lsbs_id_result = dataBasicInformation.getLsbs_id();
					String lku_symbol_result = dataBasicInformation.getLku_symbol();

					hashMapBasicInformation.put("no_polis", dataBasicInformation.getNo_polis());
					hashMapBasicInformation.put("reg_spaj", dataBasicInformation.getReg_spaj());
					hashMapBasicInformation.put("nm_pemegang", dataBasicInformation.getNm_pemegang());
					hashMapBasicInformation.put("status_polis", dataBasicInformation.getStatus_polis());
					hashMapBasicInformation.put("nama_product", dataBasicInformation.getNm_product());
					hashMapBasicInformation.put("lku_id", dataBasicInformation.getLku_id());
					if (type.equals(4)) { // Switching
						// Get MPT ID
						BigInteger mptId = services.selectGetMptId();
						dataJenis.put("mpt_id_switching", mptId.toString());

						// List Fund of Product Switching
						Topup topup = new Topup();
						topup.setLsbs_id(lsbs_id_result);
						topup.setLku_id(lku_id_result);
						topup.setReg_spaj(reg_spaj_result);

						ArrayList<Object> fund = new ArrayList<>();
						ArrayList<Fund> listFund = services.selectFundOfProduct(topup);

						ListIterator<Fund> liter = listFund.listIterator();
						while (liter.hasNext()) {
							try {
								Fund m = liter.next();
								HashMap<String, Object> datalistFund = new HashMap<>();

								String id = m.getFund_id();
								String name = m.getFund_invest();
								datalistFund.put("id", id);
								datalistFund.put("name", name);

								fund.add(datalistFund);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}

						// Bentuk Pengalihan
						ArrayList<HashMap<String, Object>> bentukPengalihan = new ArrayList<>();
						HashMap<String, Object> hashMapBentukPengalihan1 = new HashMap<>();
						hashMapBentukPengalihan1.put("id", 1);
						hashMapBentukPengalihan1.put("value", "Nominal");
						HashMap<String, Object> hashMapBentukPengalihan2 = new HashMap<>();
						hashMapBentukPengalihan2.put("id", 2);
						hashMapBentukPengalihan2.put("value", "Percentage");
						HashMap<String, Object> hashMapBentukPengalihan3 = new HashMap<>();
						hashMapBentukPengalihan3.put("id", 3);
						hashMapBentukPengalihan3.put("value", "Unit");

						bentukPengalihan.add(hashMapBentukPengalihan1);
						bentukPengalihan.add(hashMapBentukPengalihan2);
						bentukPengalihan.add(hashMapBentukPengalihan3);

						// Source Fund
						ArrayList<UnitLink> listSourceFund = services.selectUnitLink(reg_spaj_result);
						ListIterator<UnitLink> liter2 = listSourceFund.listIterator();
						ArrayList<HashMap<String, Object>> investment = new ArrayList<HashMap<String, Object>>();
						while (liter2.hasNext()) {
							try {
								UnitLink m = liter2.next();
								String lji_id = m.getLji_id();
								String lji_invest = m.getLji_invest();
								String lnu_tgl = df1.format(m.getLnu_tgl());
								String lku_symbol = m.getLku_symbol();
								BigDecimal harga_Unit = m.getHarga_Unit();
								BigDecimal nilai = m.getNilai_polis();
								BigDecimal total_Unit = m.getTotal_Unit();

								if (nilai != BigDecimal.ZERO) {
									HashMap<String, Object> tempData = new HashMap<>();
									tempData.put("lji_id", lji_id);
									tempData.put("fund", lji_invest);
									tempData.put("date", lnu_tgl);
									tempData.put("currency", lku_symbol);
									tempData.put("policy_value", nilai.setScale(2));
									tempData.put("unit_price", harga_Unit.setScale(5).doubleValue());
									tempData.put("total_unit", total_Unit.setScale(5).doubleValue());
									tempData.put("bentuk_pengalihan", bentukPengalihan);

									investment.add(tempData);
								}
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}

						// Total nilai polis
						List<BigDecimal> results = new ArrayList<>();
						ListIterator<UnitLink> iter = listSourceFund.listIterator();
						Integer a = listSourceFund.size();
						while (iter.hasNext()) {
							Integer i = 0;
							BigDecimal sum = BigDecimal.ZERO;
							while (i < a && iter.hasNext()) {
								try {
									UnitLink m = iter.next();
									BigDecimal resultNilaiPolis = m.getNilai_polis();
									sum = sum.add(resultNilaiPolis.setScale(2));
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + e);
								}
							}
							results.add(sum);
							hashMapBasicInformation.put("total_policy_value", results.get(0));
						}

						// Keterangan Biaya
						String biaya = null;
						Integer flag_insert = 2;
						BigDecimal amount = BigDecimal.ZERO;
						services.storedProcedureGetBiaya(reg_spaj_result, mptId.toString(), type, amount, 0,
								flag_insert);
						ArrayList<CostFinancialTransaction> resultSelect = services
								.selectBiayaForFinancialTransaction(reg_spaj_result, mptId.toString());
						if (!resultSelect.isEmpty()) {
							CostFinancialTransaction m = resultSelect.get(0);
							BigDecimal jumlahBiaya = m.getJumlah();
							BigDecimal persenBiaya = m.getPersen();

							if (persenBiaya.intValue() == 0 && jumlahBiaya.intValue() == 0) {
								biaya = lku_symbol_result + " " + nfZeroTwo.format(jumlahBiaya);
							} else if (persenBiaya.intValue() != 0 && jumlahBiaya.intValue() == 0) {
								biaya = persenBiaya.intValue() + "%";
							} else {
								biaya = lku_symbol_result + " " + nfZeroTwo.format(jumlahBiaya);
							}

							HashMap<String, Object> keterangan = new HashMap<>();
							keterangan.put("biaya", biaya);

							error = false;
							message = "Successfully get data";
							dataJenis.put("formulir_switching", hashMapBasicInformation);
							dataJenis.put("soure_jenis_dana_investasi", investment);

							if (fund.isEmpty()) {
								hashMapBasicInformation.put("list_fund_destination", null);
							} else {
								hashMapBasicInformation.put("list_fund_destination", fund);
							}

							dataJenis.put("keterangan", keterangan);

							data.put("switching", dataJenis);
						} else {
							error = true;
							message = "Failed get data (Cost empty)";
							resultErr = "Hasil store procedure kosong";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);

							dataJenis.put("formulir_switching", null);
							dataJenis.put("bentuk_pengalihan", null);
							dataJenis.put("soure_jenis_dana_investasi", null);
							dataJenis.put("list_fund", null);
							dataJenis.put("keterangan", null);

							data.put("switching", null);
						}
					} else {
						// Type tidak terdaftar
						error = true;
						message = "Failed get data";
						resultErr = "Type yang dimasukkan tidak terdaftar";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				}
			} else {
				// USERNAME & KEY not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 50, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/listredirection", produces = "application/json", method = RequestMethod.POST)
	public String listRedirection(@RequestBody RequestListSwitchingRedirection requestListSwitchingRedirection,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestListSwitchingRedirection);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String username = requestListSwitchingRedirection.getUsername();
		String key = requestListSwitchingRedirection.getKey();
		String no_polis = customResourceLoader.clearData(requestListSwitchingRedirection.getNo_polis());
		Integer pageNumber = requestListSwitchingRedirection.getPageNumber();
		Integer pageSize = requestListSwitchingRedirection.getPageSize();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get SPAJ
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

				ArrayList<SwitchingRedirection> arrayList = services
						.selectListRedirection(dataSPAJ.getReg_spaj(), pageNumber, pageSize);
				if (arrayList.isEmpty()) {
					error = false;
					message = "List redirection empty";
				} else {
					error = false;
					message = "Successfully get data redirection";

					for (Integer i = 0; i < arrayList.size(); i++) {
						try {
							HashMap<String, Object> dataTemp = new HashMap<>();
							SwitchingRedirection m = arrayList.get(i);

							String mpt_id = m.getMpt_id();
							String description = m.getDescription();
							String jenis_transaksi = m.getJenis_transaksi();
							Date req_date = m.getReq_date();
							Date date_status = m.getDate_status();
							String req_date_format = m.getReq_date_format();
							String date_status_format = m.getDate_status_format();

							dataTemp.put("mpt_id", mpt_id);
							dataTemp.put("date_req", req_date != null ? df1.format(req_date) : null);
							dataTemp.put("date_status", date_status != null ? df1.format(date_status) : null);
							dataTemp.put("description", description);
							dataTemp.put("jenis_transaksi", jenis_transaksi);
							dataTemp.put("date_req_format", req_date_format != null ? req_date_format : null);
							dataTemp.put("date_status_format", date_status_format != null ? date_status_format : null);

							data.add(dataTemp);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 51, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/listswitching", produces = "application/json", method = RequestMethod.POST)
	public String listSwitchingRedirection(@RequestBody RequestListSwitching requestListSwitching,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestListSwitching);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String username = requestListSwitching.getUsername();
		String key = requestListSwitching.getKey();
		String no_polis = customResourceLoader.clearData(requestListSwitching.getNo_polis());
		Integer pageNumber = requestListSwitching.getPageNumber();
		Integer pageSize = requestListSwitching.getPageSize();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get SPAJ
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

				ArrayList<SwitchingRedirection> arrayList = services
						.selectListSwitching(dataSPAJ.getReg_spaj(), pageNumber, pageSize);
				if (arrayList.isEmpty()) {
					error = false;
					message = "List switching empty";
				} else {
					error = false;
					message = "Successfully get data switching ";

					for (Integer i = 0; i < arrayList.size(); i++) {
						try {
							HashMap<String, Object> dataTemp = new HashMap<>();
							SwitchingRedirection m = arrayList.get(i);

							String mpt_id = m.getMpt_id();
							String description = m.getDescription();
							String jenis_transaksi = m.getJenis_transaksi();
							Date req_date = m.getReq_date();
							Date date_status = m.getDate_status();
							String req_date_format = m.getReq_date_format();
							String date_status_format = m.getDate_status_format();

							dataTemp.put("mpt_id", mpt_id);
							dataTemp.put("date_req", req_date != null ? df1.format(req_date) : null);
							dataTemp.put("date_status", date_status != null ? df1.format(date_status) : null);
							dataTemp.put("date_req_format", req_date_format != null ? req_date_format : null);
							dataTemp.put("date_status_format", date_status_format != null ? date_status_format : null);
							dataTemp.put("description", description);
							dataTemp.put("jenis_transaksi", jenis_transaksi);
							
							if(jenis_transaksi.equalsIgnoreCase("switching dan redirection")) {
								dataTemp.put("flag_trans", "old");
							} else {
								String flag_trans = services.selectFlagTrans(mpt_id);
								dataTemp.put("flag_trans", flag_trans);
							}

							data.add(dataTemp);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 51, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/viewswitchingold", produces = "application/json", method = RequestMethod.POST)
	public String viewSwitchingOld(@RequestBody RequestViewSwitchingRedirection requestViewSwitchingRedirection,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewSwitchingRedirection);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestViewSwitchingRedirection.getUsername();
		String key = requestViewSwitchingRedirection.getKey();
		String no_polis = customResourceLoader.clearData(requestViewSwitchingRedirection.getNo_polis());
		ArrayList<String> arrayData = requestViewSwitchingRedirection.getMpt_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				SwitchingRedirection dataViewSwitchingRedirection = services.selectViewSwitchingRedirection1_1(arrayData,
						no_polis);

				// Get SPAJ
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

				// M-POLIS
				if (dataViewSwitchingRedirection != null) {
					String id = dataViewSwitchingRedirection.getId().trim();
					String mspo_policy_no_format = dataViewSwitchingRedirection.getMspo_policy_no_format();
					String payor_name = dataViewSwitchingRedirection.getPayor_name();
					String status_polis = dataViewSwitchingRedirection.getStatus_polis();
					String nm_product = dataViewSwitchingRedirection.getNm_product();
					String jenis_transaksi = dataViewSwitchingRedirection.getLt_transksi();
					String reason_fu = dataViewSwitchingRedirection.getReason_fu();
					String description = dataViewSwitchingRedirection.getDescription();
					String lku_symbol_result = dataViewSwitchingRedirection.getLku_symbol();
					Date req_date = dataViewSwitchingRedirection.getReq_date();
					Date status_date = dataViewSwitchingRedirection.getDate_status();

					data.put("id", id);
					data.put("no_polis", mspo_policy_no_format);
					data.put("name", payor_name);
					data.put("status_polis", status_polis);
					data.put("nm_product", nm_product);
					data.put("jenis_transaksi", jenis_transaksi);
					data.put("reason_fu", reason_fu);
					data.put("description", description);
					data.put("date_req", req_date != null ? df1.format(req_date) : null);
					data.put("date_status", status_date != null ? df1.format(status_date) : null);

					// DATA DETAIL TRANSACTION
					ArrayList<SwitchingRedirection> dataArray = services.selectViewSwitchingRedirection2_2(arrayData,
							no_polis);
					HashMap<String, Object> hashMapSwitching = new HashMap<>();
					HashMap<String, Object> hashMapRedirection = new HashMap<>();
					for (Integer a = 0; a < dataArray.size(); a++) {
						String categoryType = dataArray.get(a).getJenis_transaksi();
						String mpt_id = dataArray.get(a).getMpt_id();
						BigDecimal lt_id = dataArray.get(a).getLt_id();

						if (lt_id.intValue() == 19) { // 19: Switching & Redirection
							if (categoryType.equalsIgnoreCase("switching")) {
								ArrayList<SwitchingRedirection> dataSwitching = services
										.selectViewSwitchingRedirection3(mpt_id, no_polis);
								ArrayList<HashMap<String, Object>> sourceFundSwitching = new ArrayList<>();
								ArrayList<HashMap<String, Object>> destFundSwitching = new ArrayList<>();
								for (Integer b = 0; b < dataSwitching.size(); b++) {
									HashMap<String, Object> dataTempSwitchingD = new HashMap<>();
									HashMap<String, Object> dataTempSwitchingK = new HashMap<>();
									String lji_id = dataSwitching.get(b).getLji_id();
									String lji_invest = dataSwitching.get(b).getLji_invest();
									String lku_symbol = dataSwitching.get(b).getLku_symbol();
									String mpt_dk = dataSwitching.get(b).getMpt_dk();
									Integer mpt_persen = dataSwitching.get(b).getMpt_persen().intValue();
									BigDecimal mpt_jumlah = dataSwitching.get(b).getMpt_jumlah();
									BigDecimal mpt_unit = dataSwitching.get(b).getMpt_unit();

									if (mpt_dk.equalsIgnoreCase("k")) {
										dataTempSwitchingK.put("lji_id", lji_id);
										dataTempSwitchingK.put("mpt_jumlah", mpt_jumlah);
										dataTempSwitchingK.put("mpt_unit", mpt_unit);
										dataTempSwitchingK.put("mpt_dk", mpt_dk);
										dataTempSwitchingK.put("lji_invest", lji_invest);
										dataTempSwitchingK.put("lku_symbol", lku_symbol);

										sourceFundSwitching.add(dataTempSwitchingK);
									} else {
										dataTempSwitchingD.put("lji_id", lji_id);
										dataTempSwitchingD.put("mpt_persen", mpt_persen);
										dataTempSwitchingD.put("mpt_jumlah", mpt_jumlah);
										dataTempSwitchingD.put("mpt_unit", mpt_unit);
										dataTempSwitchingD.put("mpt_dk", mpt_dk);
										dataTempSwitchingD.put("lji_invest", lji_invest);
										dataTempSwitchingD.put("lku_symbol", lku_symbol);

										destFundSwitching.add(dataTempSwitchingD);
									}
								}

								// GET ADMIN FEE & PERCENTAGE ADMIN FEE
								String biaya = null;
								ArrayList<CostFinancialTransaction> resultSelect = services
										.selectBiayaForFinancialTransaction(dataSPAJ.getReg_spaj(), mpt_id);
								if (!resultSelect.isEmpty()) {
									CostFinancialTransaction m = resultSelect.get(0);
									BigDecimal jumlahBiaya = m.getJumlah();
									BigDecimal persenBiaya = m.getPersen();

									if (persenBiaya.intValue() == 0 && jumlahBiaya.intValue() == 0) {
										biaya = lku_symbol_result + " " + nfZeroTwo.format(jumlahBiaya);
									} else if (persenBiaya.intValue() != 0 && jumlahBiaya.intValue() == 0) {
										biaya = persenBiaya.intValue() + "%";
									} else {
										biaya = lku_symbol_result + " " + nfZeroTwo.format(jumlahBiaya);
									}
								}

								hashMapSwitching.put("admin_fee_switching", biaya);
								hashMapSwitching.put("sourceFund", sourceFundSwitching);
								hashMapSwitching.put("destFund", destFundSwitching);
							} else {
								ArrayList<SwitchingRedirection> dataRedirection = services
										.selectViewSwitchingRedirection3(mpt_id, no_polis);
								ArrayList<HashMap<String, Object>> sourceFundRedirection = new ArrayList<>();
								ArrayList<HashMap<String, Object>> destFundRedirection = new ArrayList<>();
								for (Integer b = 0; b < dataRedirection.size(); b++) {
									HashMap<String, Object> dataTempRedirectionD = new HashMap<>();
									HashMap<String, Object> dataTempRedirectionK = new HashMap<>();
									String lji_id = dataRedirection.get(b).getLji_id();
									String lji_invest = dataRedirection.get(b).getLji_invest();
									String lku_symbol = dataRedirection.get(b).getLku_symbol();
									String mpt_dk = dataRedirection.get(b).getMpt_dk();
									Integer mpt_persen = dataRedirection.get(b).getMpt_persen().intValue();

									if (mpt_dk.equalsIgnoreCase("k")) {
										dataTempRedirectionK.put("lji_id", lji_id);
										dataTempRedirectionK.put("mpt_persen", mpt_persen);
										dataTempRedirectionK.put("mpt_dk", mpt_dk);
										dataTempRedirectionK.put("lji_invest", lji_invest);
										dataTempRedirectionK.put("lku_symbol", lku_symbol);

										sourceFundRedirection.add(dataTempRedirectionK);
									} else {
										dataTempRedirectionD.put("lji_id", lji_id);
										dataTempRedirectionD.put("mpt_persen", mpt_persen);
										dataTempRedirectionD.put("mpt_dk", mpt_dk);
										dataTempRedirectionD.put("lji_invest", lji_invest);
										dataTempRedirectionD.put("lku_symbol", lku_symbol);

										destFundRedirection.add(dataTempRedirectionD);
									}
								}

								hashMapRedirection.put("sourceFund", sourceFundRedirection);
								hashMapRedirection.put("destFund", destFundRedirection);
							}
						} else if (lt_id.intValue() == 4) { // 4: Switching
							ArrayList<SwitchingRedirection> dataSwitching = services
									.selectViewSwitchingRedirection3(mpt_id, no_polis);
							ArrayList<HashMap<String, Object>> sourceFundSwitching = new ArrayList<>();
							ArrayList<HashMap<String, Object>> destFundSwitching = new ArrayList<>();
							for (Integer b = 0; b < dataSwitching.size(); b++) {
								HashMap<String, Object> dataTempSwitchingD = new HashMap<>();
								HashMap<String, Object> dataTempSwitchingK = new HashMap<>();
								String lji_id = dataSwitching.get(b).getLji_id();
								String lji_invest = dataSwitching.get(b).getLji_invest();
								String lku_symbol = dataSwitching.get(b).getLku_symbol();
								String mpt_dk = dataSwitching.get(b).getMpt_dk();
								Integer mpt_persen = dataSwitching.get(b).getMpt_persen().intValue();
								BigDecimal mpt_jumlah = dataSwitching.get(b).getMpt_jumlah();
								BigDecimal mpt_unit = dataSwitching.get(b).getMpt_unit();

								if (mpt_dk.equalsIgnoreCase("k")) {
									dataTempSwitchingK.put("lji_id", lji_id);
									dataTempSwitchingK.put("mpt_jumlah", mpt_jumlah);
									dataTempSwitchingK.put("mpt_unit", mpt_unit);
									dataTempSwitchingK.put("mpt_dk", mpt_dk);
									dataTempSwitchingK.put("lji_invest", lji_invest);
									dataTempSwitchingK.put("lku_symbol", lku_symbol);

									sourceFundSwitching.add(dataTempSwitchingK);
								} else {
									dataTempSwitchingD.put("lji_id", lji_id);
									dataTempSwitchingD.put("mpt_persen", mpt_persen);
									dataTempSwitchingD.put("mpt_jumlah", mpt_jumlah);
									dataTempSwitchingD.put("mpt_unit", mpt_unit);
									dataTempSwitchingD.put("mpt_dk", mpt_dk);
									dataTempSwitchingD.put("lji_invest", lji_invest);
									dataTempSwitchingD.put("lku_symbol", lku_symbol);

									destFundSwitching.add(dataTempSwitchingD);
								}
							}

							// GET ADMIN FEE & PERCENTAGE ADMIN FEE
							String biaya = null;
							ArrayList<CostFinancialTransaction> resultSelect = services
									.selectBiayaForFinancialTransaction(dataSPAJ.getReg_spaj(), mpt_id);
							if (!resultSelect.isEmpty()) {
								CostFinancialTransaction m = resultSelect.get(0);
								BigDecimal jumlahBiaya = m.getJumlah();
								BigDecimal persenBiaya = m.getPersen();

								if (persenBiaya.intValue() == 0 && jumlahBiaya.intValue() == 0) {
									biaya = lku_symbol_result + " " + nfZeroTwo.format(jumlahBiaya);
								} else if (persenBiaya.intValue() != 0 && jumlahBiaya.intValue() == 0) {
									biaya = persenBiaya.intValue() + "%";
								} else {
									biaya = lku_symbol_result + " " + nfZeroTwo.format(jumlahBiaya);
								}
							}

							hashMapSwitching.put("admin_fee_switching", biaya);
							hashMapSwitching.put("sourceFund", sourceFundSwitching);
							hashMapSwitching.put("destFund", destFundSwitching);
							hashMapRedirection = null;
						}
					}

					error = false;
					message = "Successfully get data";
					data.put("switching", hashMapSwitching);
					data.put("redirection", hashMapRedirection);
				} else {
					SwitchingRedirection dataViewSwitchingPaper = services.selectViewSwitchingPaper_1(arrayData,
							dataSPAJ.getReg_spaj());
					// Paper
					if (dataViewSwitchingPaper != null) {
						String id = dataViewSwitchingPaper.getId().trim();
						String mspo_policy_no_format = dataViewSwitchingPaper.getMspo_policy_no_format();
						String payor_name = dataViewSwitchingPaper.getPayor_name();
						String status_polis = dataViewSwitchingPaper.getStatus_polis();
						String nm_product = dataViewSwitchingPaper.getNm_product();
						String jenis_transaksi = dataViewSwitchingPaper.getLt_transksi();
						String reason_fu = null;
						String description = dataViewSwitchingPaper.getDescription();
						String reg_spaj = dataViewSwitchingPaper.getReg_spaj();
						String lku_symbol_result = dataViewSwitchingPaper.getLku_symbol();
						BigDecimal biaya = dataViewSwitchingPaper.getBiaya();
						Date req_date = dataViewSwitchingPaper.getReq_date();
						Date status_date = dataViewSwitchingPaper.getDate_status();

						data.put("id", id);
						data.put("no_polis", mspo_policy_no_format);
						data.put("name", payor_name);
						data.put("status_polis", status_polis);
						data.put("nm_product", nm_product);
						data.put("jenis_transaksi", jenis_transaksi);
						data.put("reason_fu", reason_fu);
						data.put("description", description);
						data.put("date_req", req_date != null ? df1.format(req_date) : null);
						data.put("date_status", status_date != null ? df1.format(status_date) : null);

						HashMap<String, Object> hashMapSwitching = new HashMap<>();
						HashMap<String, Object> hashMapRedirection = new HashMap<>();

						ArrayList<SwitchingRedirection> dataSwitchingPaper = services.selectViewDetailSwitchingPaper(id,
								reg_spaj);
						ArrayList<HashMap<String, Object>> sourceFundSwitching = new ArrayList<>();
						ArrayList<HashMap<String, Object>> destFundSwitching = new ArrayList<>();
						for (int x = 0; x < dataSwitchingPaper.size(); x++) {
							HashMap<String, Object> dataTempSwitchingD = new HashMap<>();
							HashMap<String, Object> dataTempSwitchingK = new HashMap<>();
							String lji_id = dataSwitchingPaper.get(x).getLji_id();
							String lji_invest = dataSwitchingPaper.get(x).getLji_invest();
							String lku_symbol = dataSwitchingPaper.get(x).getLku_symbol();
							String mpt_dk = dataSwitchingPaper.get(x).getMpt_dk();
							BigDecimal mpt_jumlah = dataSwitchingPaper.get(x).getMpt_jumlah();
							BigDecimal mpt_unit = dataSwitchingPaper.get(x).getMpt_unit();
							String type_penarikan = dataSwitchingPaper.get(x).getType_penarikan();

							if (mpt_dk.equalsIgnoreCase("k")) {
								if (type_penarikan.equalsIgnoreCase("unit")) {
									dataTempSwitchingK.put("mpt_jumlah", 0);
									dataTempSwitchingK.put("mpt_unit", mpt_unit);
								} else {
									dataTempSwitchingK.put("mpt_jumlah", mpt_jumlah);
									dataTempSwitchingK.put("mpt_unit", 0);
								}

								dataTempSwitchingK.put("lji_id", lji_id);
								dataTempSwitchingK.put("mpt_dk", mpt_dk);
								dataTempSwitchingK.put("lji_invest", lji_invest);
								dataTempSwitchingK.put("lku_symbol", lku_symbol);

								sourceFundSwitching.add(dataTempSwitchingK);
							} else {
								dataTempSwitchingD.put("mpt_persen", 0);
								dataTempSwitchingD.put("mpt_jumlah", 0);
								dataTempSwitchingD.put("mpt_unit", mpt_unit != null ? mpt_unit : 0);
								dataTempSwitchingD.put("lji_id", lji_id);
								dataTempSwitchingD.put("mpt_dk", mpt_dk);
								dataTempSwitchingD.put("lji_invest", lji_invest);
								dataTempSwitchingD.put("lku_symbol", lku_symbol);

								destFundSwitching.add(dataTempSwitchingD);
							}
						}

						String biayaResult = biaya.toString();
						String admin_fee = null;

						if (lku_symbol_result.equalsIgnoreCase("rp.")) {
							if (biayaResult.equals("0")) {
								admin_fee = lku_symbol_result + " " + nfZeroTwo.format(new BigDecimal(biayaResult));
							} else if (!biayaResult.equals("0") && biayaResult.length() <= 3) {
								admin_fee = biayaResult + "%";
							} else {
								admin_fee = lku_symbol_result + " " + nfZeroTwo.format(new BigDecimal(biayaResult));
							}
						} else {
							admin_fee = lku_symbol_result + " " + biayaResult;
						}

						hashMapSwitching.put("admin_fee_switching", admin_fee);
						hashMapSwitching.put("sourceFund", sourceFundSwitching);
						hashMapSwitching.put("destFund", destFundSwitching);
						hashMapRedirection = null;

						error = false;
						message = "Successfully get data";
						data.put("switching", hashMapSwitching);
						data.put("redirection", hashMapRedirection);
					} else {
						error = true;
						message = "MPT_ID or No. polis incorrect";
						resultErr = "Data mpt_id atau no. polis yang dimasukkan salah";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 52, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/viewswitchingnew", produces = "application/json", method = RequestMethod.POST)
	public String viewSwitchingnew(@RequestBody RequestViewSwitching requestViewSwitching,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewSwitching);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestViewSwitching.getUsername();
		String key = requestViewSwitching.getKey();
		String no_polis = customResourceLoader.clearData(requestViewSwitching.getNo_polis());
		String mpt_id = requestViewSwitching.getMpt_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				SwitchingRedirection dataViewSwitchingRedirection = services.selectViewSwitchingRedirection1(mpt_id,
						no_polis);

				// Get SPAJ
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

				// M-POLIS
				if (dataViewSwitchingRedirection != null) {
					String id = dataViewSwitchingRedirection.getId().trim();
					String mspo_policy_no_format = dataViewSwitchingRedirection.getMspo_policy_no_format();
					String mpt_jumlah_mst = dataViewSwitchingRedirection.getMpt_jumlah_mst();
					String mpt_unit_mst = dataViewSwitchingRedirection.getMpt_unit_mst();
					String payor_name = dataViewSwitchingRedirection.getPayor_name();
					String status_polis = dataViewSwitchingRedirection.getStatus_polis();
					String nm_product = dataViewSwitchingRedirection.getNm_product();
					String jenis_transaksi = dataViewSwitchingRedirection.getLt_transksi();
					String reason_fu = dataViewSwitchingRedirection.getReason_fu();
					String description = dataViewSwitchingRedirection.getDescription();
					String lku_symbol_result = dataViewSwitchingRedirection.getLku_symbol();
					Date req_date = dataViewSwitchingRedirection.getReq_date();
					Date status_date = dataViewSwitchingRedirection.getDate_status();

					data.put("id", id);
					data.put("no_polis", mspo_policy_no_format);
					data.put("mpt_jumlah_mst", mpt_jumlah_mst);
					data.put("mpt_unit_mst", mpt_unit_mst);
					data.put("name", payor_name);
					data.put("status_polis", status_polis);
					data.put("nm_product", nm_product);
					data.put("jenis_transaksi", jenis_transaksi);
					data.put("reason_fu", reason_fu != null ? reason_fu : null);
					data.put("description", description);
					data.put("date_req", req_date != null ? df1.format(req_date) : null);
					data.put("date_status", status_date != null ? df1.format(status_date) : null);

					// DATA DETAIL TRANSACTION
					ArrayList<SwitchingRedirection> dataArray = services.selectViewSwitchingRedirection2(mpt_id,
							no_polis);
					HashMap<String, Object> hashMapSwitching = new HashMap<>();
					HashMap<String, Object> sourceHashMapSwitching = new HashMap<>();
					Integer flag_double = 0;
					
					for (Integer a = 0; a < dataArray.size(); a++) {
						String mpt_id2 = dataArray.get(a).getMpt_id();
						BigDecimal lt_id = dataArray.get(a).getLt_id();

						if (lt_id.intValue() == 4) { // 4: Switching
							ArrayList<SwitchingRedirection> dataSwitching = services
									.selectViewSwitchingRedirection3(mpt_id2, no_polis);
							
							//GET DESTINATION
							ArrayList<HashMap<String, Object>> destinationSwitching = new ArrayList<>();
							for (int i = 0; i < dataSwitching.size(); i++) {
								HashMap<String, Object> dataTempSwitchingD = new HashMap<>();
								
								String lji_id = dataSwitching.get(i).getLji_id();
								String lji_id_ke = dataSwitching.get(i).getLji_id_ke();
								if(i > 0) {
									String lji_id_bfr = dataSwitching.get(i-1).getLji_id();
									if(lji_id_bfr.equals(lji_id)) {
										flag_double = 1;
									}
								}
								Integer persen_ke = dataSwitching.get(i).getPersen_ke().intValue();
								BigDecimal jumlah_ke = dataSwitching.get(i).getJumlah_ke();
								BigDecimal unit_ke = dataSwitching.get(i).getUnit_ke();
								String mpt_dk = dataSwitching.get(i).getMpt_dk();
								String lji_invest_dest = dataSwitching.get(i).getLji_invest_dest();
								String lku_symbol = dataSwitching.get(i).getLku_symbol();
								
								dataTempSwitchingD.put("lji_id", lji_id);
								dataTempSwitchingD.put("lji_id_ke", lji_id_ke);
								dataTempSwitchingD.put("persen_ke", persen_ke);
								dataTempSwitchingD.put("jumlah_ke", jumlah_ke);
								dataTempSwitchingD.put("unit_ke", unit_ke);
								dataTempSwitchingD.put("mpt_dk", mpt_dk);
								dataTempSwitchingD.put("lji_invest_dest", lji_invest_dest);
								dataTempSwitchingD.put("lku_symbol", lku_symbol);
								
								destinationSwitching.add(dataTempSwitchingD);
								
							}
							
							if(flag_double == 1) {
								//GET SOURCE
								ArrayList<HashMap<String, Object>> sourceSwitching = new ArrayList<>();
								for (int i = 0; i < dataSwitching.size(); i++) {
									HashMap<String, Object> dataTempSwitchingK = new HashMap<>();
									
									String lji_id = dataSwitching.get(i).getLji_id();
									Integer mpt_persen = dataSwitching.get(i).getMpt_persen().intValue();
									BigDecimal mpt_jumlah = dataSwitching.get(i).getMpt_jumlah();
									BigDecimal mpt_unit = dataSwitching.get(i).getMpt_unit();
									String mpt_dk = dataSwitching.get(i).getMpt_dk();
									String lji_invest_source = dataSwitching.get(i).getLji_invest_source();
									String lku_symbol = dataSwitching.get(i).getLku_symbol();
									
									dataTempSwitchingK.put("lji_id", lji_id);
									dataTempSwitchingK.put("mpt_jumlah", mpt_jumlah);
									dataTempSwitchingK.put("mpt_unit", mpt_unit);
									dataTempSwitchingK.put("mpt_persen", mpt_persen);
									dataTempSwitchingK.put("mpt_dk", mpt_dk);
									dataTempSwitchingK.put("lji_invest_source", lji_invest_source);
									dataTempSwitchingK.put("lku_symbol", lku_symbol);
									
									sourceSwitching.add(dataTempSwitchingK);
									
									//MAPPING DEST TO SOURCE
									HashMap<String, Object> hashmapS = sourceSwitching.get(i);
									String lji_id1 = (String) hashmapS.get("lji_id");
									
									for(int j = 0; j < destinationSwitching.size(); j++) {
										HashMap<String, Object> hashmapK = destinationSwitching.get(j);
										String lji_id2 = (String) hashmapK.get("lji_id");
										
										if(lji_id1.equals(lji_id2)) {
											dataTempSwitchingK.put("destFund", destinationSwitching);
										}
									}
								}
								
								List<HashMap<String, Object>> newSource = sourceSwitching.stream().distinct().collect(Collectors.toList());
								sourceHashMapSwitching.put("sourceFund", newSource);

							}else {
								//List<HashMap<String, Object>> newDest = destinationSwitching.stream().distinct().collect(Collectors.toList());
								Integer count = 0;
								//GET SOURCE
								ArrayList<HashMap<String, Object>> sourceSwitching = new ArrayList<>();
								for (int i = 0; i < dataSwitching.size(); i++) {
									HashMap<String, Object> dataTempSwitchingK = new HashMap<>();
									ArrayList<HashMap<String, Object>> dataTemp = new ArrayList<>();
									
									String lji_id = dataSwitching.get(i).getLji_id();
									String lji_id_bfr = null;
									if(i > 0) {
										lji_id_bfr = dataSwitching.get(i-1).getLji_id();
									}
									
									Integer mpt_persen = dataSwitching.get(i).getMpt_persen().intValue();
									BigDecimal mpt_jumlah = dataSwitching.get(i).getMpt_jumlah();
									BigDecimal mpt_unit = dataSwitching.get(i).getMpt_unit();
									String mpt_dk = dataSwitching.get(i).getMpt_dk();
									String lji_invest_source = dataSwitching.get(i).getLji_invest_source();
									String lku_symbol = dataSwitching.get(i).getLku_symbol();
									
									
									dataTempSwitchingK.put("lji_id", lji_id);
									dataTempSwitchingK.put("mpt_jumlah", mpt_jumlah);
									dataTempSwitchingK.put("mpt_unit", mpt_unit);
									dataTempSwitchingK.put("mpt_persen", mpt_persen);
									dataTempSwitchingK.put("mpt_dk", mpt_dk);
									dataTempSwitchingK.put("lji_invest_source", lji_invest_source);
									dataTempSwitchingK.put("lku_symbol", lku_symbol);
									
									sourceSwitching.add(dataTempSwitchingK);
									
									if(lji_id_bfr != null && !lji_id_bfr.isEmpty()) {
										if(lji_id.equals(lji_id_bfr)) {
											sourceSwitching.remove(i-count);
											count = count + 1;
											
											HashMap<String, Object> hashmapS = sourceSwitching.get(i-count);
											String lji_id1 = (String) hashmapS.get("lji_id");
											HashMap<String, Object> hashmapK = destinationSwitching.get(i);
											String lji_id2 = (String) hashmapK.get("lji_id");
											
											
											if(lji_id1.equals(lji_id2)) {
												dataTemp.add(destinationSwitching.get(i));
											}
										} else {
											HashMap<String, Object> hashmapS = sourceSwitching.get(i-count);
											String lji_id1 = (String) hashmapS.get("lji_id");
											HashMap<String, Object> hashmapK = destinationSwitching.get(i);
											String lji_id2 = (String) hashmapK.get("lji_id");
											
											
											if(lji_id1.equals(lji_id2)) {
												dataTemp.add(destinationSwitching.get(i));
											}
										}
									} else {
										HashMap<String, Object> hashmapS = sourceSwitching.get(i-count);
										String lji_id1 = (String) hashmapS.get("lji_id");
										HashMap<String, Object> hashmapK = destinationSwitching.get(i);
										String lji_id2 = (String) hashmapK.get("lji_id");
										
										
										if(lji_id1.equals(lji_id2)) {
											dataTemp.add(destinationSwitching.get(i));
										}
									}
									
									dataTempSwitchingK.put("destFund", dataTemp);
									
									/*HashMap<String, Object> hashmapS = sourceSwitching.get(i);
									String lji_id1 = (String) hashmapS.get("lji_id");
									HashMap<String, Object> hashmapK = newDest.get(i);
									String lji_id2 = (String) hashmapK.get("lji_id");
									
									
									if(lji_id1.equals(lji_id2)) {
										dataTempSwitchingK.put("destFund", newDest.get(i));
									}*/
								}
								
								List<HashMap<String, Object>> newSource = sourceSwitching.stream().distinct().collect(Collectors.toList());
								sourceHashMapSwitching.put("sourceFund", newSource);
							}

							// GET ADMIN FEE & PERCENTAGE ADMIN FEE
							String biaya = null;
							ArrayList<CostFinancialTransaction> resultSelect = services
									.selectBiayaForFinancialTransaction(dataSPAJ.getReg_spaj(), mpt_id2);
							if (!resultSelect.isEmpty()) {
								CostFinancialTransaction m = resultSelect.get(0);
								BigDecimal jumlahBiaya = m.getJumlah();
								BigDecimal persenBiaya = m.getPersen();

								if (persenBiaya.intValue() == 0 && jumlahBiaya.intValue() == 0) {
									biaya = lku_symbol_result + " " + nfZeroTwo.format(jumlahBiaya);
								} else if (persenBiaya.intValue() != 0 && jumlahBiaya.intValue() == 0) {
									biaya = persenBiaya.intValue() + "%";
								} else {
									biaya = lku_symbol_result + " " + nfZeroTwo.format(jumlahBiaya);
								}
							}

							hashMapSwitching.put("admin_fee_switching", biaya);
							hashMapSwitching.putAll(sourceHashMapSwitching);
							//hashMapSwitching.put("sourceFund", sourceFundSwitching);
							//hashMapSwitching.put("destFund", destFundSwitching);
						}
					}

					error = false;
					message = "Successfully get data";
					data.put("switching", hashMapSwitching);
				} else {
					SwitchingRedirection dataViewSwitchingPaper = services.selectViewSwitchingPaper(mpt_id,
							dataSPAJ.getReg_spaj());
					// Paper
					if (dataViewSwitchingPaper != null) {
						String id = dataViewSwitchingPaper.getId().trim();
						String mspo_policy_no_format = dataViewSwitchingPaper.getMspo_policy_no_format();
						String payor_name = dataViewSwitchingPaper.getPayor_name();
						String status_polis = dataViewSwitchingPaper.getStatus_polis();
						String nm_product = dataViewSwitchingPaper.getNm_product();
						String jenis_transaksi = dataViewSwitchingPaper.getLt_transksi();
						String reason_fu = null;
						String description = dataViewSwitchingPaper.getDescription();
						String reg_spaj = dataViewSwitchingPaper.getReg_spaj();
						String lku_symbol_result = dataViewSwitchingPaper.getLku_symbol();
						BigDecimal biaya = dataViewSwitchingPaper.getBiaya();
						Date req_date = dataViewSwitchingPaper.getReq_date();
						Date status_date = dataViewSwitchingPaper.getDate_status();

						data.put("id", id);
						data.put("no_polis", mspo_policy_no_format);
						data.put("name", payor_name);
						data.put("status_polis", status_polis);
						data.put("nm_product", nm_product);
						data.put("jenis_transaksi", jenis_transaksi);
						data.put("reason_fu", reason_fu);
						data.put("description", description);
						data.put("date_req", req_date != null ? df1.format(req_date) : null);
						data.put("date_status", status_date != null ? df1.format(status_date) : null);

						HashMap<String, Object> hashMapSwitching = new HashMap<>();

						ArrayList<SwitchingRedirection> dataSwitchingPaper = services.selectViewDetailSwitchingPaper(id,
								reg_spaj);
						ArrayList<HashMap<String, Object>> sourceFundSwitching = new ArrayList<>();
						ArrayList<HashMap<String, Object>> destFundSwitching = new ArrayList<>();
						for (int x = 0; x < dataSwitchingPaper.size(); x++) {
							HashMap<String, Object> dataTempSwitchingD = new HashMap<>();
							HashMap<String, Object> dataTempSwitchingK = new HashMap<>();
							String lji_id = dataSwitchingPaper.get(x).getLji_id();
							String lji_invest = dataSwitchingPaper.get(x).getLji_invest();
							String lku_symbol = dataSwitchingPaper.get(x).getLku_symbol();
							String mpt_dk = dataSwitchingPaper.get(x).getMpt_dk();
							BigDecimal mpt_jumlah = dataSwitchingPaper.get(x).getMpt_jumlah();
							BigDecimal mpt_unit = dataSwitchingPaper.get(x).getMpt_unit();
							String type_penarikan = dataSwitchingPaper.get(x).getType_penarikan();

							if (mpt_dk.equalsIgnoreCase("k")) {
								if (type_penarikan.equalsIgnoreCase("unit")) {
									dataTempSwitchingK.put("mpt_jumlah", 0);
									dataTempSwitchingK.put("mpt_unit", mpt_unit);
								} else {
									dataTempSwitchingK.put("mpt_jumlah", mpt_jumlah);
									dataTempSwitchingK.put("mpt_unit", 0);
								}

								dataTempSwitchingK.put("lji_id", lji_id);
								dataTempSwitchingK.put("mpt_dk", mpt_dk);
								dataTempSwitchingK.put("lji_invest", lji_invest);
								dataTempSwitchingK.put("lku_symbol", lku_symbol);

								sourceFundSwitching.add(dataTempSwitchingK);
							} else {
								dataTempSwitchingD.put("mpt_persen", 0);
								dataTempSwitchingD.put("mpt_jumlah", 0);
								dataTempSwitchingD.put("mpt_unit", mpt_unit != null ? mpt_unit : 0);
								dataTempSwitchingD.put("lji_id", lji_id);
								dataTempSwitchingD.put("mpt_dk", mpt_dk);
								dataTempSwitchingD.put("lji_invest", lji_invest);
								dataTempSwitchingD.put("lku_symbol", lku_symbol);

								destFundSwitching.add(dataTempSwitchingD);
							}
						}

						String biayaResult = biaya.toString();
						String admin_fee = null;

						if (lku_symbol_result.equalsIgnoreCase("rp.")) {
							if (biayaResult.equals("0")) {
								admin_fee = lku_symbol_result + " " + nfZeroTwo.format(new BigDecimal(biayaResult));
							} else if (!biayaResult.equals("0") && biayaResult.length() <= 3) {
								admin_fee = biayaResult + "%";
							} else {
								admin_fee = lku_symbol_result + " " + nfZeroTwo.format(new BigDecimal(biayaResult));
							}
						} else {
							admin_fee = lku_symbol_result + " " + biayaResult;
						}

						hashMapSwitching.put("admin_fee_switching", admin_fee);
						hashMapSwitching.put("sourceFund", sourceFundSwitching);
						hashMapSwitching.put("destFund", destFundSwitching);

						error = false;
						message = "Successfully get data";
						data.put("switching", hashMapSwitching);
					} else {
						error = true;
						message = "MPT_ID or No. polis incorrect";
						resultErr = "Data mpt_id atau no. polis yang dimasukkan salah";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 52, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/viewredirection", produces = "application/json", method = RequestMethod.POST)
	public String viewRedirection(@RequestBody RequestViewSwitching requestViewSwitching,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewSwitching);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestViewSwitching.getUsername();
		String key = requestViewSwitching.getKey();
		String no_polis = customResourceLoader.clearData(requestViewSwitching.getNo_polis());
		String mpt_id = requestViewSwitching.getMpt_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				SwitchingRedirection dataViewSwitchingRedirection = services.selectViewSwitchingRedirection1(mpt_id,
						no_polis);

				// Get SPAJ
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

				// M-POLIS
				if (dataViewSwitchingRedirection != null) {
					String id = dataViewSwitchingRedirection.getId().trim();
					String mspo_policy_no_format = dataViewSwitchingRedirection.getMspo_policy_no_format();
					String payor_name = dataViewSwitchingRedirection.getPayor_name();
					String status_polis = dataViewSwitchingRedirection.getStatus_polis();
					String nm_product = dataViewSwitchingRedirection.getNm_product();
					String jenis_transaksi = dataViewSwitchingRedirection.getLt_transksi();
					String reason_fu = dataViewSwitchingRedirection.getReason_fu();
					String description = dataViewSwitchingRedirection.getDescription();
					Date req_date = dataViewSwitchingRedirection.getReq_date();
					Date status_date = dataViewSwitchingRedirection.getDate_status();

					data.put("id", id);
					data.put("no_polis", mspo_policy_no_format);
					data.put("name", payor_name);
					data.put("status_polis", status_polis);
					data.put("nm_product", nm_product);
					data.put("jenis_transaksi", jenis_transaksi);
					data.put("reason_fu", reason_fu);
					data.put("description", description);
					data.put("date_req", req_date != null ? df1.format(req_date) : null);
					data.put("date_status", status_date != null ? df1.format(status_date) : null);

					// DATA DETAIL TRANSACTION
					ArrayList<SwitchingRedirection> dataArray = services.selectViewSwitchingRedirection2(mpt_id,
							no_polis);
					HashMap<String, Object> hashMapSwitching = new HashMap<>();
					HashMap<String, Object> hashMapRedirection = new HashMap<>();
					for (Integer a = 0; a < dataArray.size(); a++) {
						BigDecimal lt_id = dataArray.get(a).getLt_id();

						if (lt_id.intValue() == 20) { // 20: Redirection
							ArrayList<SwitchingRedirection> dataRedirection = services
									.selectViewSwitchingRedirection3(mpt_id, no_polis);
							ArrayList<HashMap<String, Object>> sourceFundRedirection = new ArrayList<>();
							ArrayList<HashMap<String, Object>> destFundRedirection = new ArrayList<>();
							for (Integer b = 0; b < dataRedirection.size(); b++) {
								HashMap<String, Object> dataTempRedirectionD = new HashMap<>();
								HashMap<String, Object> dataTempRedirectionK = new HashMap<>();
								String lji_id = dataRedirection.get(b).getLji_id();
								Integer mpt_persen = dataRedirection.get(b).getMpt_persen().intValue();
								String mpt_dk = dataRedirection.get(b).getMpt_dk();
								String lji_invest = dataRedirection.get(b).getLji_invest();
								String lku_symbol = dataRedirection.get(b).getLku_symbol();

								if (mpt_dk.equalsIgnoreCase("k")) {
									dataTempRedirectionK.put("lji_id", lji_id);
									dataTempRedirectionK.put("mpt_persen", mpt_persen);
									dataTempRedirectionK.put("mpt_dk", mpt_dk);
									dataTempRedirectionK.put("lji_invest", lji_invest);
									dataTempRedirectionK.put("lku_symbol", lku_symbol);

									sourceFundRedirection.add(dataTempRedirectionK);
								} else {
									dataTempRedirectionD.put("lji_id", lji_id);
									dataTempRedirectionD.put("mpt_persen", mpt_persen);
									dataTempRedirectionD.put("mpt_dk", mpt_dk);
									dataTempRedirectionD.put("lji_invest", lji_invest);
									dataTempRedirectionD.put("lku_symbol", lku_symbol);

									destFundRedirection.add(dataTempRedirectionD);
								}
							}

							hashMapRedirection.put("sourceFund", sourceFundRedirection);
							hashMapRedirection.put("destFund", destFundRedirection);
							hashMapSwitching = null;
						}
					}

					error = false;
					message = "Successfully get data";
					data.put("switching", hashMapSwitching);
					data.put("redirection", hashMapRedirection);
				} else {
					SwitchingRedirection dataViewSwitchingPaper = services.selectViewSwitchingPaper(mpt_id,
							dataSPAJ.getReg_spaj());
					// Paper
					if (dataViewSwitchingPaper != null) {
						String id = dataViewSwitchingPaper.getId().trim();
						String mspo_policy_no_format = dataViewSwitchingPaper.getMspo_policy_no_format();
						String payor_name = dataViewSwitchingPaper.getPayor_name();
						String status_polis = dataViewSwitchingPaper.getStatus_polis();
						String nm_product = dataViewSwitchingPaper.getNm_product();
						String jenis_transaksi = dataViewSwitchingPaper.getLt_transksi();
						String reason_fu = null;
						String description = dataViewSwitchingPaper.getDescription();
						String reg_spaj = dataViewSwitchingPaper.getReg_spaj();
						String lku_symbol_result = dataViewSwitchingPaper.getLku_symbol();
						BigDecimal biaya = dataViewSwitchingPaper.getBiaya();
						Date req_date = dataViewSwitchingPaper.getReq_date();
						Date status_date = dataViewSwitchingPaper.getDate_status();

						data.put("id", id);
						data.put("no_polis", mspo_policy_no_format);
						data.put("name", payor_name);
						data.put("status_polis", status_polis);
						data.put("nm_product", nm_product);
						data.put("jenis_transaksi", jenis_transaksi);
						data.put("reason_fu", reason_fu);
						data.put("description", description);
						data.put("date_req", req_date != null ? df1.format(req_date) : null);
						data.put("date_status", status_date != null ? df1.format(status_date) : null);

						HashMap<String, Object> hashMapSwitching = new HashMap<>();
						HashMap<String, Object> hashMapRedirection = new HashMap<>();

						ArrayList<SwitchingRedirection> dataSwitchingPaper = services.selectViewDetailSwitchingPaper(id,
								reg_spaj);
						ArrayList<HashMap<String, Object>> sourceFundSwitching = new ArrayList<>();
						ArrayList<HashMap<String, Object>> destFundSwitching = new ArrayList<>();
						for (int x = 0; x < dataSwitchingPaper.size(); x++) {
							HashMap<String, Object> dataTempSwitchingD = new HashMap<>();
							HashMap<String, Object> dataTempSwitchingK = new HashMap<>();
							String lji_id = dataSwitchingPaper.get(x).getLji_id();
							String lji_invest = dataSwitchingPaper.get(x).getLji_invest();
							String lku_symbol = dataSwitchingPaper.get(x).getLku_symbol();
							String mpt_dk = dataSwitchingPaper.get(x).getMpt_dk();
							BigDecimal mpt_jumlah = dataSwitchingPaper.get(x).getMpt_jumlah();
							BigDecimal mpt_unit = dataSwitchingPaper.get(x).getMpt_unit();
							String type_penarikan = dataSwitchingPaper.get(x).getType_penarikan();

							if (mpt_dk.equalsIgnoreCase("k")) {
								if (type_penarikan.equalsIgnoreCase("unit")) {
									dataTempSwitchingK.put("mpt_jumlah", 0);
									dataTempSwitchingK.put("mpt_unit", mpt_unit);
								} else {
									dataTempSwitchingK.put("mpt_jumlah", mpt_jumlah);
									dataTempSwitchingK.put("mpt_unit", 0);
								}

								dataTempSwitchingK.put("lji_id", lji_id);
								dataTempSwitchingK.put("mpt_dk", mpt_dk);
								dataTempSwitchingK.put("lji_invest", lji_invest);
								dataTempSwitchingK.put("lku_symbol", lku_symbol);

								sourceFundSwitching.add(dataTempSwitchingK);
							} else {
								dataTempSwitchingD.put("mpt_persen", 0);
								dataTempSwitchingD.put("mpt_jumlah", 0);
								dataTempSwitchingD.put("mpt_unit", mpt_unit != null ? mpt_unit : 0);
								dataTempSwitchingD.put("lji_id", lji_id);
								dataTempSwitchingD.put("mpt_dk", mpt_dk);
								dataTempSwitchingD.put("lji_invest", lji_invest);
								dataTempSwitchingD.put("lku_symbol", lku_symbol);

								destFundSwitching.add(dataTempSwitchingD);
							}
						}

						String biayaResult = biaya.toString();
						String admin_fee = null;

						if (lku_symbol_result.equalsIgnoreCase("rp.")) {
							if (biayaResult.equals("0")) {
								admin_fee = lku_symbol_result + " " + nfZeroTwo.format(new BigDecimal(biayaResult));
							} else if (!biayaResult.equals("0") && biayaResult.length() <= 3) {
								admin_fee = biayaResult + "%";
							} else {
								admin_fee = lku_symbol_result + " " + nfZeroTwo.format(new BigDecimal(biayaResult));
							}
						} else {
							admin_fee = lku_symbol_result + " " + biayaResult;
						}

						hashMapSwitching.put("admin_fee_switching", admin_fee);
						hashMapSwitching.put("sourceFund", sourceFundSwitching);
						hashMapSwitching.put("destFund", destFundSwitching);
						hashMapRedirection = null;

						error = false;
						message = "Successfully get data";
						data.put("switching", hashMapSwitching);
						data.put("redirection", hashMapRedirection);
					} else {
						error = true;
						message = "MPT_ID or No. polis incorrect";
						resultErr = "Data mpt_id atau no. polis yang dimasukkan salah";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 52, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	/*@RequestMapping(value = "/validateswitchingandredirection", produces = "application/json", method = RequestMethod.POST)
	public String validateSwitchingRedirection(
			@RequestBody RequestValidateSwitchingRedirection requestValidateSwitchingRedirection,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestValidateSwitchingRedirection);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> data = new HashMap<>();
		HashMap<String, Object> map = new HashMap<>();

		String username = requestValidateSwitchingRedirection.getUsername();
		String key = requestValidateSwitchingRedirection.getKey();
		String no_polis = requestValidateSwitchingRedirection.getNo_polis();
		Integer lt_id = requestValidateSwitchingRedirection.getLt_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				if (lt_id.equals(4)) { // 4: Switching
					String mpt_id_switching = requestValidateSwitchingRedirection.getSwitching().getMpt_id_switching();
					String validate_switching = null;
					String validate_redirection = null;
					message = "Successfully validate";

					// Check detail switching empty or not
					DetailSwitching listDetailSwitching = requestValidateSwitchingRedirection.getSwitching()
							.getDetail_switching();

					if (listDetailSwitching != null) {
						List<String> listIdFundSource = new ArrayList<>();

						if ((!listDetailSwitching.getSource().isEmpty())
								&& (!listDetailSwitching.getDestination().isEmpty())) {
							for (Integer a = 0; a < listDetailSwitching.getSource().size(); a++) {
								try {
									String lji_id = requestValidateSwitchingRedirection.getSwitching()
											.getDetail_switching().getSource().get(a).getLji_id();

									listIdFundSource.add(lji_id);
								} catch (Exception e) {
									logger.error(
											"Path: " + request.getServletPath() + ", case: looping source switching, "
													+ " Username: " + username + " Error: " + e.getMessage());
								}
							}

							validate_switching = "Validate Success";
							for (Integer a = 0; a < listDetailSwitching.getDestination().size(); a++) {
								try {
									String lji_id = requestValidateSwitchingRedirection.getSwitching()
											.getDetail_switching().getDestination().get(a).getLji_id();

									if (listIdFundSource.contains(lji_id)) {
										error = true;
										validate_switching = "Some destination funds have the same source";
										resultErr = "Destination fund ada yang sama dengan source fund, " + no_polis
												+ ", LJI_ID: " + lji_id + ", MPT_ID_SWITCHING: " + mpt_id_switching;
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								} catch (Exception e) {
									logger.error(
											"Path: " + request.getServletPath() + ", case: looping dest switching, "
													+ " Username: " + username + " Error: " + e.getMessage());
								}
							}
						} else {
							error = true;
							validate_switching = "Source Fund or Destination Fund is empty";
							resultErr = "Source Fund or Destination Fund is empty, MPT_ID_SWITCHING: "
									+ mpt_id_switching;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}

						data.put("validate_switching", validate_switching);
						data.put("validate_redirection", validate_redirection);
					} else {
						error = true;
						message = "Data detail switching empty";
						resultErr = "Data detail switching tidak boleh kosong, mpt_id: " + mpt_id_switching;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (lt_id.equals(20)) { // 20: Redirection
					String mpt_id_redirection = requestValidateSwitchingRedirection.getRedirection()
							.getMpt_id_redirection();

					String validate_switching = null;
					String validate_redirection = null;
					message = "Successfully validate";

					// Check detail redirection empty or not
					DetailRedirection listDetailRedirection = requestValidateSwitchingRedirection.getRedirection()
							.getDetail_redirection();

					if (listDetailRedirection != null) {
						ArrayList<String> listIdFundSource = new ArrayList<>();
						ArrayList<Integer> listPersenFundSource = new ArrayList<>();

						if ((!listDetailRedirection.getSource().isEmpty())
								&& (!listDetailRedirection.getDestination().isEmpty())) {
							for (Integer a = 0; a < listDetailRedirection.getSource().size(); a++) {
								try {
									String lji_id = requestValidateSwitchingRedirection.getRedirection()
											.getDetail_redirection().getSource().get(a).getLji_id();
									Float mpt_persen = requestValidateSwitchingRedirection.getRedirection()
											.getDetail_redirection().getSource().get(a).getMpt_persen();

									listIdFundSource.add(lji_id);
									listPersenFundSource.add(mpt_persen.intValue());
								} catch (Exception e) {
									logger.error(
											"Path: " + request.getServletPath() + "case: looping source redirection"
													+ " Username: " + username + " Error: " + e.getMessage());
								}
							}

							validate_switching = null;
							validate_redirection = "Validate Success";
							for (Integer a = 0; a < listDetailRedirection.getDestination().size(); a++) {
								try {
									String lji_id = requestValidateSwitchingRedirection.getRedirection()
											.getDetail_redirection().getDestination().get(a).getLji_id();
									Float mpt_persen = requestValidateSwitchingRedirection.getRedirection()
											.getDetail_redirection().getDestination().get(a).getMpt_persen();

									if (listIdFundSource.contains(lji_id)) {
										if (listPersenFundSource.get(listIdFundSource.indexOf(lji_id))
												.equals(mpt_persen.intValue())) {
											error = true;
											validate_redirection = "Some destination funds have the same source";
											resultErr = "Destination fund ada yang sama dengan source fund, " + no_polis
													+ ", LJI_ID: " + lji_id + ", MPT_ID_REDIRECTION: "
													+ mpt_id_redirection;
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + resultErr);
										}
									}
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath()
											+ "case: looping destination redirection" + " Username: " + username
											+ " Error: " + e.getMessage());
								}
							}
						} else {
							error = true;
							validate_redirection = "Source Fund or Destination Fund is empty";
							resultErr = "Source Fund or Destination Fund is empty, MPT_ID_REDIRECTION: "
									+ mpt_id_redirection;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}

						data.put("validate_switching", validate_switching);
						data.put("validate_redirection", validate_redirection);
					} else {
						error = true;
						message = "Data detail redirection empty";
						resultErr = "Data detail redirection tidak boleh kosong, mpt_id: " + mpt_id_redirection;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (lt_id.equals(19)) { // 19: Switching & Redirection
					String mpt_id_switching = requestValidateSwitchingRedirection.getSwitching().getMpt_id_switching();
					String mpt_id_redirection = requestValidateSwitchingRedirection.getRedirection()
							.getMpt_id_redirection();

					String validate_switching = null;
					String validate_redirection = null;
					message = "Successfully Validate";

					// Check detail switching empty or not
					DetailSwitching listDetailSwitching = requestValidateSwitchingRedirection.getSwitching()
							.getDetail_switching();

					// Check detail redirection empty or not
					DetailRedirection listDetailRedirection = requestValidateSwitchingRedirection.getRedirection()
							.getDetail_redirection();

					if (listDetailSwitching != null) {
						List<String> listIdFundSource = new ArrayList<>();

						if ((!listDetailSwitching.getSource().isEmpty())
								&& (!listDetailSwitching.getDestination().isEmpty())) {
							for (Integer a = 0; a < listDetailSwitching.getSource().size(); a++) {
								try {
									String lji_id = requestValidateSwitchingRedirection.getSwitching()
											.getDetail_switching().getSource().get(a).getLji_id();

									listIdFundSource.add(lji_id);
								} catch (Exception e) {
									logger.error(
											"Path: " + request.getServletPath() + ", case: looping source switching, "
													+ " Username: " + username + " Error: " + e.getMessage());
								}
							}

							validate_switching = "Validate Success";
							for (Integer a = 0; a < listDetailSwitching.getDestination().size(); a++) {
								try {
									String lji_id = requestValidateSwitchingRedirection.getSwitching()
											.getDetail_switching().getDestination().get(a).getLji_id();

									if (listIdFundSource.contains(lji_id)) {
										error = true;
										validate_switching = "Some destination funds have the same source";
										resultErr = "Destination fund ada yang sama dengan source fund, " + no_polis
												+ ", LJI_ID: " + lji_id + ", MPT_ID_SWITCHING: " + mpt_id_switching;
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								} catch (Exception e) {
									logger.error(
											"Path: " + request.getServletPath() + ", case: looping dest switching, "
													+ " Username: " + username + " Error: " + e.getMessage());
								}
							}
						} else {
							error = true;
							validate_switching = "Source Fund or Destination Fund is empty";
							resultErr = "Source Fund or Destination Fund is empty, MPT_ID_SWITCHING: "
									+ mpt_id_switching;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						error = true;
						message = "Data detail switching empty";
						resultErr = "Data detail switching tidak boleh kosong, mpt_id: " + mpt_id_switching;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}

					if (listDetailRedirection != null) {
						ArrayList<String> listIdFundSource = new ArrayList<>();
						ArrayList<Integer> listPersenFundSource = new ArrayList<>();

						if ((!listDetailRedirection.getSource().isEmpty())
								&& (!listDetailRedirection.getDestination().isEmpty())) {
							for (Integer a = 0; a < listDetailRedirection.getSource().size(); a++) {
								try {
									String lji_id = requestValidateSwitchingRedirection.getRedirection()
											.getDetail_redirection().getSource().get(a).getLji_id();
									Float mpt_persen = requestValidateSwitchingRedirection.getRedirection()
											.getDetail_redirection().getSource().get(a).getMpt_persen();

									listIdFundSource.add(lji_id);
									listPersenFundSource.add(mpt_persen.intValue());
								} catch (Exception e) {
									logger.error(
											"Path: " + request.getServletPath() + "case: looping source redirection"
													+ " Username: " + username + " Error: " + e.getMessage());
								}
							}

							validate_redirection = "Validate Success";
							for (Integer a = 0; a < listDetailRedirection.getDestination().size(); a++) {
								try {
									String lji_id = requestValidateSwitchingRedirection.getRedirection()
											.getDetail_redirection().getDestination().get(a).getLji_id();
									Float mpt_persen = requestValidateSwitchingRedirection.getRedirection()
											.getDetail_redirection().getDestination().get(a).getMpt_persen();

									if (listIdFundSource.contains(lji_id)) {
										if (listPersenFundSource.get(listIdFundSource.indexOf(lji_id))
												.equals(mpt_persen.intValue())) {
											error = true;
											validate_redirection = "Some destination funds have the same source";
											resultErr = "Destination fund ada yang sama dengan source fund, " + no_polis
													+ ", LJI_ID: " + lji_id + ", MPT_ID_REDIRECTION: "
													+ mpt_id_redirection;
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + resultErr);
										}
									}
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath()
											+ "case: looping destination redirection" + " Username: " + username
											+ " Error: " + e.getMessage());
								}
							}
						} else {
							error = true;
							validate_redirection = "Source Fund or Destination Fund is empty";
							resultErr = "Source Fund or Destination Fund is empty, MPT_ID_REDIRECTION: "
									+ mpt_id_redirection;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}

						data.put("validate_switching", validate_switching);
						data.put("validate_redirection", validate_redirection);
					} else {
						error = true;
						message = "Data detail redirection empty";
						resultErr = "Data detail redirection tidak boleh kosong, mpt_id: " + mpt_id_redirection;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					error = true;
					message = "Failed get data";
					resultErr = "Type submit yang dimasukkan tidak sesuai";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			// Push Notification Telegram
			customResourceLoader.pushTelegram("@mfajarsep_bot",
					"Path: " + request.getServletPath() + " username: " + username + "," + " Error: " + e);

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
		customResourceLoader.insertHistActivityWS(12, 66, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/validateswitching", produces = "application/json", method = RequestMethod.POST)
	public String validateSwitching(
			@RequestBody RequestValidateSwitchingRedirection requestValidateSwitchingRedirection,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestValidateSwitchingRedirection);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> data = new HashMap<>();
		HashMap<String, Object> map = new HashMap<>();

		String username = requestValidateSwitchingRedirection.getUsername();
		String key = requestValidateSwitchingRedirection.getKey();
		String no_polis = requestValidateSwitchingRedirection.getNo_polis();
		Integer lt_id = requestValidateSwitchingRedirection.getLt_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				if (lt_id.equals(4)) { // 4: Switching
					String mpt_id_switching = requestValidateSwitchingRedirection.getSwitching().getMpt_id_switching();
					String validate_switching = null;
					String validate_redirection = null;
					message = "Successfully validate";

					// Check detail switching empty or not
					DetailSwitching listDetailSwitching = requestValidateSwitchingRedirection.getSwitching()
							.getDetail_switching();

					if (listDetailSwitching != null) {
						List<String> listIdFundSource = new ArrayList<>();

						if ((!listDetailSwitching.getSource().isEmpty())
								&& (!listDetailSwitching.getDestination().isEmpty())) {
							for (Integer a = 0; a < listDetailSwitching.getSource().size(); a++) {
								try {
									String lji_id = requestValidateSwitchingRedirection.getSwitching()
											.getDetail_switching().getSource().get(a).getLji_id();

									listIdFundSource.add(lji_id);
								} catch (Exception e) {
									logger.error(
											"Path: " + request.getServletPath() + ", case: looping source switching, "
													+ " Username: " + username + " Error: " + e.getMessage());
								}
							}

							validate_switching = "Validate Success";
							for (Integer a = 0; a < listDetailSwitching.getDestination().size(); a++) {
								try {
									String lji_id = requestValidateSwitchingRedirection.getSwitching()
											.getDetail_switching().getDestination().get(a).getLji_id();

									if (listIdFundSource.contains(lji_id)) {
										error = true;
										validate_switching = "Some destination funds have the same source";
										resultErr = "Destination fund ada yang sama dengan source fund, " + no_polis
												+ ", LJI_ID: " + lji_id + ", MPT_ID_SWITCHING: " + mpt_id_switching;
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + resultErr);
									}
								} catch (Exception e) {
									logger.error(
											"Path: " + request.getServletPath() + ", case: looping dest switching, "
													+ " Username: " + username + " Error: " + e.getMessage());
								}
							}
						} else {
							error = true;
							validate_switching = "Source Fund or Destination Fund is empty";
							resultErr = "Source Fund or Destination Fund is empty, MPT_ID_SWITCHING: "
									+ mpt_id_switching;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}

						data.put("validate_switching", validate_switching);
						data.put("validate_redirection", validate_redirection);
					} else {
						error = true;
						message = "Data detail switching empty";
						resultErr = "Data detail switching tidak boleh kosong, mpt_id: " + mpt_id_switching;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					error = true;
					message = "Failed get data";
					resultErr = "Type submit yang dimasukkan tidak sesuai";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			// Push Notification Telegram
			customResourceLoader.pushTelegram("@mfajarsep_bot",
					"Path: " + request.getServletPath() + " username: " + username + "," + " Error: " + e);

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
		customResourceLoader.insertHistActivityWS(12, 66, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}*/
	
	@RequestMapping(value = "/submitredirection", produces = "application/json", method = RequestMethod.POST)
	public String submitSwitchingRedirection(
			@RequestBody RequestSubmitSwitchingRedirection requestSubmitSwitchingRedirection,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSubmitSwitchingRedirection);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestSubmitSwitchingRedirection.getUsername();
		String key = requestSubmitSwitchingRedirection.getKey();
		String no_polis = requestSubmitSwitchingRedirection.getNo_polis();
		Integer lt_id = requestSubmitSwitchingRedirection.getLt_id();
		Integer language_id = requestSubmitSwitchingRedirection.getLanguage_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {

				// Select Configuration M-Polis
				HashMap<String, Object> dataConfiguration = services.configuration();

				if (lt_id.equals(20)) { // 20: Redirection
					String mpt_id_redirection = requestSubmitSwitchingRedirection.getRedirection()
							.getMpt_id_redirection();
					String lku_id = requestSubmitSwitchingRedirection.getRedirection().getLku_id();
					String payor_name = requestSubmitSwitchingRedirection.getRedirection().getPayor_name();

					// Check detail redirection empty or not
					DetailRedirection listDetailRedirection = requestSubmitSwitchingRedirection.getRedirection()
							.getDetail_redirection();
					Integer checkTransId = services.selectCountTransId(mpt_id_redirection.toString());

					// Check jumlah fund yang dimasukkan source (jumlah fund harus 100)
					List<Float> sumPercentageFundSource = new ArrayList<>();
					JSONArray fundsCheckSource = new JSONArray(
							requestSubmitSwitchingRedirection.getRedirection().getDetail_redirection().getSource());
					float sumSource = 0;
					for (int i = 0; i < fundsCheckSource.length(); i++) {
						try {
							float percentage = fundsCheckSource.getJSONObject(i).getFloat("mpt_persen");
							sumPercentageFundSource.add(percentage);
							sumSource += sumPercentageFundSource.get(i);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}

					// Check jumlah fund yang dimasukkan destination (jumlah fund harus 100)
					List<Float> sumPercentageFundDest = new ArrayList<>();
					JSONArray fundsCheckDest = new JSONArray(requestSubmitSwitchingRedirection.getRedirection()
							.getDetail_redirection().getDestination());
					float sumDestination = 0;
					for (int i = 0; i < fundsCheckDest.length(); i++) {
						try {
							float percentage = fundsCheckDest.getJSONObject(i).getFloat("mpt_persen");
							sumPercentageFundDest.add(percentage);
							sumDestination += sumPercentageFundDest.get(i);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}

					Integer sumIntSource = (int) sumSource;
					Integer sumIntDest = (int) sumDestination;

					if (listDetailRedirection == null) {
						error = true;
						message = "Data detail redirection empty";
						resultErr = "Data detail redirection tidak boleh kosong";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else if (checkTransId > 0) {
						error = true;
						message = "MPT_ID has been used";
						resultErr = "MPT_ID yang disubmit sudah pernah digunakan, MPT_ID: " + mpt_id_redirection;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else if (!sumIntSource.equals(100)) {
						error = true;
						message = "Percentage source fund not 100%";
						resultErr = "Persentase fund source tidak 100%, MPT_ID: " + mpt_id_redirection;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else if (!sumIntDest.equals(100)) {
						error = true;
						message = "Percentage destination fund not 100%";
						resultErr = "Persentase fund destination tidak 100%, MPT_ID: " + mpt_id_redirection;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else {
						// Get SPAJ
						Pemegang paramGetSPAJ = new Pemegang();
						paramGetSPAJ.setMspo_policy_no(no_polis);
						Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

						// Insert EKA.MST_MPOL_TRANS
						services.insertRedirection(mpt_id_redirection, customResourceLoader.getDatetimeJava1(),
								dataSPAJ.getReg_spaj(), lt_id, lku_id, customResourceLoader.getDatetimeJava(),
								payor_name, "0");

						for (Integer a = 0; a < listDetailRedirection.getSource().size(); a++) {
							try {
								String lji_id = requestSubmitSwitchingRedirection.getRedirection()
										.getDetail_redirection().getSource().get(a).getLji_id();
								Float mpt_persen = requestSubmitSwitchingRedirection.getRedirection()
										.getDetail_redirection().getSource().get(a).getMpt_persen();
								String mpt_dk = requestSubmitSwitchingRedirection.getRedirection()
										.getDetail_redirection().getSource().get(a).getMpt_dk();

								// Insert EKA.MST_MPOL_TRANS_DET
								services.insertDetailRedirection(mpt_id_redirection, lji_id, mpt_persen, mpt_dk);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + ", case: looping source redirection"
										+ ", Username: " + username + " Error: " + e.getMessage());
							}
						}

						for (Integer a = 0; a < listDetailRedirection.getDestination().size(); a++) {
							try {
								String lji_id = requestSubmitSwitchingRedirection.getRedirection()
										.getDetail_redirection().getDestination().get(a).getLji_id();
								Float mpt_persen = requestSubmitSwitchingRedirection.getRedirection()
										.getDetail_redirection().getDestination().get(a).getMpt_persen();
								String mpt_dk = requestSubmitSwitchingRedirection.getRedirection()
										.getDetail_redirection().getDestination().get(a).getMpt_dk();

								// Insert EKA.MST_MPOL_TRANS_DET
								services.insertDetailRedirection(mpt_id_redirection, lji_id, mpt_persen, mpt_dk);
							} catch (Exception e) {
								logger.error(
										"Path: " + request.getServletPath() + ", case: looping destination redirection "
												+ ", Username: " + username + " Error: " + e.getMessage());
							}
						}

						// Hit SP Bang Billi Jalanin transaksi
						services.storedProcedureSubmitFinancialTransaction(dataSPAJ.getReg_spaj(), mpt_id_redirection);

						// Send Notification
						String messageNotif = null;

						if (language_id.equals(1)) {
							messageNotif = (String) dataConfiguration.get("NOTIFICATION_REDIRECTION_IDN");
						} else {
							messageNotif = (String) dataConfiguration.get("NOTIFICATION_REDIRECTION_ENG");
						}

						customResourceLoader.pushNotif(username, messageNotif, no_polis, dataSPAJ.getReg_spaj(), 11, 0);

						error = false;
						message = "Successfully submit redirection";
					}
				} else { // Type not found
					error = true;
					message = "Failed get data";
					resultErr = "Type submit yang dimasukkan tidak sesuai";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			// Push Notification Telegram
			customResourceLoader.pushTelegram("@mfajarsep_bot",
					"Path: " + request.getServletPath() + " username: " + username + "," + " Error: " + e);

			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 53, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/submitswitching", produces = "application/json", method = RequestMethod.POST)
	public String submitSwitching(
			@RequestBody RequestSubmitSwitching requestSubmitSwitching,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSubmitSwitching);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestSubmitSwitching.getUsername();
		String key = requestSubmitSwitching.getKey();
		String no_polis = requestSubmitSwitching.getNo_polis();
		Integer lt_id = requestSubmitSwitching.getLt_id();
		Integer language_id = requestSubmitSwitching.getLanguage_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {

				// Select Configuration M-Polis
				HashMap<String, Object> dataConfiguration = services.configuration();

				if (lt_id.equals(4)) { // 4: Switching
					String mpt_id_switching = requestSubmitSwitching.getSwitching().getMpt_id_switching();
					String lku_id = requestSubmitSwitching.getSwitching().getLku_id();
					BigDecimal mpt_jumlah = requestSubmitSwitching.getSwitching().getMpt_jumlah();
					BigDecimal mpt_unit = requestSubmitSwitching.getSwitching().getMpt_unit();
					String payor_name = requestSubmitSwitching.getSwitching().getPayor_name();
					
					
					// Check detail switching empty or not
					DetailSwitching listDetailSwitching = requestSubmitSwitching.getSwitching()
							.getDetail_switching();
					Integer checkTransId = services.selectCountTransId(mpt_id_switching.toString());
					/*
					// AMBIL LIST SOURCE
					ArrayList<HashMap<String, Object>> sourceSwitching = new ArrayList<>();
					JSONArray fundsCheck = new JSONArray(
							requestSubmitSwitching.getSwitching().getDetail_switching().getSource());
					for (int i = 0; i < fundsCheck.length(); i++) {
						HashMap<String, Object> sourceFund = new HashMap<>();						
						sourceFund.put("source",fundsCheck.get(i));
						sourceSwitching.add(sourceFund);
					}
					
					System.out.println(sourceSwitching);
					
					// AMBIL LIST DESTINATION
					ArrayList<DetailDestSwitching> destSwitching = new ArrayList<>();
					//JSONArray destCheck = new JSONArray(fundsCheck);
					
					for (int i = 0; i < listDetailSwitching.getSource().size(); i++) {
						//HashMap<String, Object> destFund = new HashMap<>();						
						//destFund.get("destination");
						//destSwitching.add(destFund);
						//Destination dest = new Destination();
						
						destSwitching.addAll(listDetailSwitching.getSource().get(i).getDestination());
					}
					
					System.out.println(gson.toJson(destSwitching));
					*/
					
					// Check jumlah fund yang dimasukkan (jumlah fund harus 100)
					/*List<Integer> sumPercentageFund = new ArrayList<>();
					float sum = 0;
					for (int i = 0; i < listDetailSwitching.getSource().size(); i++) {
						try {
							ArrayList<DetailDestSwitching> destFund = listDetailSwitching.getSource().get(i).getDestination();
							Integer percentage = destFund.get(i).getMpt_persen();
							
							sumPercentageFund.add(percentage);
							sum += sumPercentageFund.get(i);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}
					
					Integer sumInt = (int) sum;*/

					if (listDetailSwitching == null) {
						error = true;
						message = "Data detail switching empty";
						resultErr = "Data detail switching tidak boleh kosong, mpt_id: " + mpt_id_switching;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else if (checkTransId > 0) {
						error = true;
						message = "MPT_ID has been used";
						resultErr = "MPT_ID yang disubmit sudah pernah digunakan, MPT_ID: " + mpt_id_switching;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}/* else if (!sumInt.equals(100)) {
						error = true;
						message = "Percentage destination fund not 100%";
						resultErr = "Persentase fund destination tidak 100%, MPT_ID: " + mpt_id_switching;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}*/ else {
						// Get SPAJ
						Pemegang paramGetSPAJ = new Pemegang();
						paramGetSPAJ.setMspo_policy_no(no_polis);
						Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

						// Get tanggal transaksi
						ProductUtama dataProductCode = services.selectProductCode(dataSPAJ.getReg_spaj());
						BigDecimal lsbs_id = dataProductCode.getLsbs_id();
						BigDecimal lsdbs_number = dataProductCode.getLsdbs_number();

						String lsbs_idToStr = lsbs_id.toString();
						String lsdbs_numberToStr = lsdbs_number.toString();
						String combinationProductCode = lsbs_idToStr + lsdbs_numberToStr;
						Integer group_product = 0;

						if (lsbs_idToStr.equals("213") || lsbs_idToStr.equals("216")) {
							group_product = 1;
						} else if (combinationProductCode.equals("1345") || combinationProductCode.equals("13410")
								|| combinationProductCode.equals("13411") || combinationProductCode.equals("13412")
								|| combinationProductCode.equals("2151")) {
							group_product = 1;
						}

						String dateTransaction = customResourceLoader.getDateTransaction(group_product);

						// Insert EKA.MST_MPOL_TRANS
						services.insertSwitching(mpt_id_switching, customResourceLoader.getDatetimeJava1(),
								dataSPAJ.getReg_spaj(), lt_id, lku_id, mpt_jumlah, mpt_unit,
								customResourceLoader.getDatetimeJava(), payor_name, dateTransaction);

						/*for (Integer a = 0; a < listDetailSwitching.getSource().size(); a++) {
							try {
								String lji_id = requestSubmitSwitching.getSwitching().getDetail_switching()
										.getSource().get(a).getLji_id();
								BigDecimal mpt_jumlah_det = requestSubmitSwitching.getSwitching()
										.getDetail_switching().getSource().get(a).getMpt_jumlah();
								BigDecimal mpt_unit_det = requestSubmitSwitching.getSwitching()
										.getDetail_switching().getSource().get(a).getMpt_unit();
								String mpt_dk = requestSubmitSwitching.getSwitching().getDetail_switching()
										.getSource().get(a).getMpt_dk();

								// Insert EKA.MST_MPOL_TRANS_DET
								services.insertDetailSwitching(mpt_id_switching, lji_id, 0, mpt_jumlah_det,
										mpt_unit_det, mpt_dk);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + ", case: looping source switching"
										+ ", Username: " + username + " Error: " + e.getMessage());
							}
						}*/

						for (Integer a = 0; a < listDetailSwitching.getSource().size(); a++) {
							for (Integer b = 0; b < listDetailSwitching.getSource().get(a).getDestination().size(); b++) {
								try {
									String lji_id = requestSubmitSwitching.getSwitching().getDetail_switching()
											.getSource().get(a).getLji_id();
									BigDecimal mpt_jumlah_det = requestSubmitSwitching.getSwitching()
											.getDetail_switching().getSource().get(a).getMpt_jumlah();
									BigDecimal mpt_unit_det = requestSubmitSwitching.getSwitching()
											.getDetail_switching().getSource().get(a).getMpt_unit();
									Integer mpt_persen = requestSubmitSwitching.getSwitching()
											.getDetail_switching().getSource().get(a).getMpt_persen();
									String mpt_dk = requestSubmitSwitching.getSwitching().getDetail_switching()
											.getSource().get(a).getMpt_dk();
									String lji_id_ke = requestSubmitSwitching.getSwitching().getDetail_switching()
											.getSource().get(a).getDestination().get(b).getLji_id_ke();
									BigDecimal jumlah_ke = requestSubmitSwitching.getSwitching()
											.getDetail_switching().getSource().get(a).getDestination().get(b).getJumlah_ke();
									BigDecimal unit_ke = requestSubmitSwitching.getSwitching()
											.getDetail_switching().getSource().get(a).getDestination().get(b).getUnit_ke();
									Integer persen_ke = requestSubmitSwitching.getSwitching()
											.getDetail_switching().getSource().get(a).getDestination().get(b).getPersen_ke();
	
									// Insert EKA.MST_MPOL_TRANS_DET
									services.insertDetailSwitching(mpt_id_switching, lji_id, mpt_persen, mpt_jumlah_det,
											mpt_unit_det, mpt_dk, lji_id_ke, persen_ke, jumlah_ke, unit_ke);
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath() + ", case: looping dest switching"
											+ ", Username: " + username + " Error: " + e.getMessage());
								}
							}
						}

						// Hit SP Bang Billi Jalanin transaksi
						services.storedProcedureSubmitFinancialTransaction(dataSPAJ.getReg_spaj(), mpt_id_switching);

						// Send Notification
						String messageNotif = null;

						if (language_id.equals(1)) {
							messageNotif = (String) dataConfiguration.get("NOTIFICATION_SWITCHING_IDN");
						} else {
							messageNotif = (String) dataConfiguration.get("NOTIFICATION_SWITCHING_ENG");
						}

						customResourceLoader.pushNotif(username, messageNotif, no_polis, dataSPAJ.getReg_spaj(), 11, 0);

						error = false;
						message = "Successfully submit switching";
					}
				} else { // Type not found
					error = true;
					message = "Failed get data";
					resultErr = "Type submit yang dimasukkan tidak sesuai";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			// Push Notification Telegram
			customResourceLoader.pushTelegram("@mfajarsep_bot",
					"Path: " + request.getServletPath() + " username: " + username + "," + " Error: " + e);

			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 53, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/withdraw", produces = "application/json", method = RequestMethod.POST)
	public String withdraw(@RequestBody RequestWithdraw requestWithdraw, HttpServletRequest request,
			HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestWithdraw);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestWithdraw.getUsername();
		String key = requestWithdraw.getKey();
		String no_polis = customResourceLoader.clearData(requestWithdraw.getNo_polis());
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get MPT ID
				BigInteger mptId = services.selectGetMptId();

				// Get basic information
				HashMap<String, Object> hashMapBasicInformation = new HashMap<>();
				User dataBasicInformation = services.selectBasicInformationForFinancialTransaction(no_polis);
				if (dataBasicInformation == null) {
					// Data basic information empty
					error = true;
					message = "Data customer empty";
					resultErr = "Data customer kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					String reg_spaj_result = dataBasicInformation.getReg_spaj();

					// Basic Information
					hashMapBasicInformation.put("mpt_id_withdraw", mptId.toString());
					hashMapBasicInformation.put("no_polis", dataBasicInformation.getNo_polis());
					hashMapBasicInformation.put("reg_spaj", dataBasicInformation.getReg_spaj());
					hashMapBasicInformation.put("nm_pemegang", dataBasicInformation.getNm_pemegang());
					hashMapBasicInformation.put("status_polis", dataBasicInformation.getStatus_polis());
					hashMapBasicInformation.put("nama_product", dataBasicInformation.getNm_product());
					hashMapBasicInformation.put("lku_id", dataBasicInformation.getLku_id());
					hashMapBasicInformation.put("lku_symbol", dataBasicInformation.getLku_symbol());
					hashMapBasicInformation.put("lt_id", 3);
					hashMapBasicInformation.put("no_hp",
							dataBasicInformation.getNo_hp() != null ? dataBasicInformation.getNo_hp().trim() : null);

					// Bentuk Pengalihan
					ArrayList<HashMap<String, Object>> bentukPengalihan = new ArrayList<>();
					HashMap<String, Object> hashMapBentukPengalihan1 = new HashMap<>();
					hashMapBentukPengalihan1.put("id", 1);
					hashMapBentukPengalihan1.put("value", "Nominal");
					HashMap<String, Object> hashMapBentukPengalihan2 = new HashMap<>();
					hashMapBentukPengalihan2.put("id", 2);
					hashMapBentukPengalihan2.put("value", "Percentage");
					HashMap<String, Object> hashMapBentukPengalihan3 = new HashMap<>();
					hashMapBentukPengalihan3.put("id", 3);
					hashMapBentukPengalihan3.put("value", "Unit");

					bentukPengalihan.add(hashMapBentukPengalihan1);
					bentukPengalihan.add(hashMapBentukPengalihan2);
					bentukPengalihan.add(hashMapBentukPengalihan3);

					// Source Fund
					ArrayList<UnitLink> listSourceFund = services.selectUnitLink(reg_spaj_result);
					ListIterator<UnitLink> liter2 = listSourceFund.listIterator();
					ArrayList<HashMap<String, Object>> investment = new ArrayList<HashMap<String, Object>>();
					while (liter2.hasNext()) {
						try {
							UnitLink m = liter2.next();
							String lji_id = m.getLji_id();
							String lji_invest = m.getLji_invest();
							String lnu_tgl = df1.format(m.getLnu_tgl());
							String lku_symbol = m.getLku_symbol();
							BigDecimal harga_Unit = m.getHarga_Unit();
							BigDecimal nilai = m.getNilai_polis();
							BigDecimal total_Unit = m.getTotal_Unit();

							if (nilai != BigDecimal.ZERO) {
								HashMap<String, Object> tempData = new HashMap<>();
								tempData.put("lji_id", lji_id);
								tempData.put("fund", lji_invest);
								tempData.put("date", lnu_tgl);
								tempData.put("currency", lku_symbol);
								tempData.put("policy_value", nilai.setScale(2));
								tempData.put("unit_price", harga_Unit.setScale(5).doubleValue());
								tempData.put("total_unit", total_Unit.setScale(5).doubleValue());

								investment.add(tempData);
							}
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}

					// Total Nilai Polis
					List<BigDecimal> results = new ArrayList<>();
					ListIterator<UnitLink> iter = listSourceFund.listIterator();
					Integer a = listSourceFund.size();
					while (iter.hasNext()) {
						Integer i = 0;
						BigDecimal sum = BigDecimal.ZERO;
						while (i < a && iter.hasNext()) {
							try {
								UnitLink m = iter.next();
								BigDecimal resultNilaiPolis = m.getNilai_polis();
								sum = sum.add(resultNilaiPolis.setScale(2));
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
						results.add(sum);
						hashMapBasicInformation.put("total_policy_value", results.get(0));
					}

					// Premi Pokok & Premi Topup
					ArrayList<Withdraw> dataPremi = services.selectSaldoWithdraw(reg_spaj_result);
					hashMapBasicInformation.put("premi_pokok", dataPremi.get(0).getPremi_pokok());
					hashMapBasicInformation.put("premi_topup", dataPremi.get(0).getPremi_topup());

					// Rekening
					try {
						String rekeningClient = dataBasicInformation.getRekening() != null
								? customResourceLoader.clearData(dataBasicInformation.getRekening())
								: null;
						String bank = dataBasicInformation.getBank();

						hashMapBasicInformation.put("bank", bank);
						hashMapBasicInformation.put("rekening_original", rekeningClient);

						String rekening = null;

						if (rekeningClient != null) {
							rekening = customResourceLoader.formatRekening(rekeningClient);
						}

						hashMapBasicInformation.put("rekening", rekening);

						error = false;
						message = "Successfully get data withdraw";
						data.put("formulir_withdraw", hashMapBasicInformation);
						data.put("bentuk_withdraw", bentukPengalihan);
						data.put("source_withdraw", investment);
					} catch (Exception e) {
						logger.error("Path: " + request.getServletPath() + " Username: " + username
								+ " Error get rekening & bank withdraw: " + e);
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 55, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/costwithdraw", produces = "application/json", method = RequestMethod.POST)
	public String costWithdraw(@RequestBody RequestCostWithdraw requestCostWithdraw, HttpServletRequest request,
			HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestCostWithdraw);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String username = requestCostWithdraw.getUsername();
		String key = requestCostWithdraw.getKey();
		String no_polis = requestCostWithdraw.getNo_polis();
		String lku_symbol = requestCostWithdraw.getLku_symbol();
		BigDecimal amount = requestCostWithdraw.getAmount();
		String mpt_id = requestCostWithdraw.getMpt_id();
		Integer kode_trans = requestCostWithdraw.getLt_id();
		Integer proses = requestCostWithdraw.getProses();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get REG SPAJ
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

				// Get Biaya
				String biaya = null;
				Integer flag_insert = 2;
				services.storedProcedureGetBiaya(dataSPAJ.getReg_spaj(), mpt_id, kode_trans, amount, proses,
						flag_insert);
				ArrayList<CostFinancialTransaction> resultSelect = services
						.selectBiayaForFinancialTransaction(dataSPAJ.getReg_spaj(), mpt_id.toString());
				if (!resultSelect.isEmpty()) {
					HashMap<String, Object> dataTemp = new HashMap<>();
					for (Integer i = 0; i < resultSelect.size(); i++) {
						CostFinancialTransaction hashMapTemp = resultSelect.get(i);
						String label_biaya = hashMapTemp.getLjb_biaya();
						BigDecimal jumlahBiaya = hashMapTemp.getJumlah();
						BigDecimal persenBiaya = hashMapTemp.getPersen();

						if (persenBiaya.intValue() == 0 && jumlahBiaya.intValue() == 0) {
							biaya = lku_symbol + " " + nfZeroTwo.format(jumlahBiaya);
						} else if (persenBiaya.intValue() != 0 && jumlahBiaya.intValue() == 0) {
							biaya = persenBiaya.intValue() + "%";
						} else {
							biaya = lku_symbol + " " + nfZeroTwo.format(jumlahBiaya);
						}

						dataTemp.put("biaya", biaya);
						dataTemp.put("label_biaya", label_biaya.equalsIgnoreCase("other") ? "Biaya" : label_biaya);

						data.add(dataTemp);
					}

					error = false;
					message = "Successfully get data cost withdraw";
				} else {
					error = true;
					message = "Failed get data cost withdraw";
					resultErr = "Data biaya kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 56, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/listwithdraw", produces = "application/json", method = RequestMethod.POST)
	public String listWithdraw(@RequestBody RequestListWithdraw requestListWithdraw, HttpServletRequest request,
			HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestListWithdraw);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String username = requestListWithdraw.getUsername();
		String key = requestListWithdraw.getKey();
		String no_polis = customResourceLoader.clearData(requestListWithdraw.getNo_polis());
		Integer pageNumber = requestListWithdraw.getPageNumber();
		Integer pageSize = requestListWithdraw.getPageSize();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get SPAJ
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

				ArrayList<Withdraw> arrayList = services.selectListWithdraw(dataSPAJ.getReg_spaj(), pageNumber,
						pageSize);
				if (arrayList.isEmpty()) {
					error = false;
					message = "List withdraw empty";
				} else {
					error = false;
					message = "Successfully get data withdraw";

					for (Integer i = 0; i < arrayList.size(); i++) {
						try {
							HashMap<String, Object> dataTemp = new HashMap<>();
							Withdraw m = arrayList.get(i);

							String mpt_id = m.getMpt_id();
							String description = m.getDescription();
							String type_penarikan = m.getType_penarikan();
							String lku_symbol = m.getLku_symbol();
							BigDecimal mpt_jumlah = m.getMpt_jumlah_detail();
							BigDecimal mpt_unit = m.getMpt_unit_detail();
							BigDecimal count_value = m.getCount_value();
							Date req_date = m.getReq_date();
							Date date_status = m.getDate_status();
							String req_date_format = m.getReq_date_format();
							String date_status_format = m.getDate_status_format();
							String amount = null;

							dataTemp.put("mpt_id", mpt_id);
							dataTemp.put("date_req", req_date != null ? req_date_format : null);
							dataTemp.put("date_status", date_status != null ? date_status_format : null);
							dataTemp.put("description", description);
							
							if (type_penarikan.equalsIgnoreCase("unit")) {
								if (count_value.intValue() == 1) {
									String mpt_unit_format = nfZeroFour.format(mpt_unit);
									mpt_unit_format = mpt_unit_format.toString().replace('.', '+');
									mpt_unit_format = mpt_unit_format.toString().replace(',', '.');
									mpt_unit_format = mpt_unit_format.toString().replace('+', ',');
									
									amount = mpt_unit_format;
									dataTemp.put("amount", amount);
								} else {
									String mpt_unit_format = nfZeroFour.format(mpt_unit);
									mpt_unit_format = mpt_unit_format.toString().replace('.', '+');
									mpt_unit_format = mpt_unit_format.toString().replace(',', '.');
									mpt_unit_format = mpt_unit_format.toString().replace('+', ',');
									
									amount = mpt_unit_format + " (+" + Integer.toString((count_value.intValue() - 1)) + ")";
									dataTemp.put("amount", amount);
								}
							} else {
								if (count_value.intValue() == 1) {
									String mpt_jumlah_format = nfZeroTwo.format(mpt_jumlah);
									mpt_jumlah_format = mpt_jumlah_format.toString().replace('.', '+');
									mpt_jumlah_format = mpt_jumlah_format.toString().replace(',', '.');
									mpt_jumlah_format = mpt_jumlah_format.toString().replace('+', ',');
									
									amount = lku_symbol + " " + mpt_jumlah_format;
									dataTemp.put("amount", amount);
								} else {
									String mpt_jumlah_format = nfZeroTwo.format(mpt_jumlah);
									mpt_jumlah_format = mpt_jumlah_format.toString().replace('.', '+');
									mpt_jumlah_format = mpt_jumlah_format.toString().replace(',', '.');
									mpt_jumlah_format = mpt_jumlah_format.toString().replace('+', ',');
									
									amount = lku_symbol + " " + mpt_jumlah_format + " (+"
											+ Integer.toString((count_value.intValue() - 1)) + ")";
									dataTemp.put("amount", amount);
								}
							}

							data.add(dataTemp);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 57, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/viewwithdraw", produces = "application/json", method = RequestMethod.POST)
	public String viewWithdraw(@RequestBody RequestViewWithdraw requestViewWithdraw, HttpServletRequest request,
			HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewWithdraw);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestViewWithdraw.getUsername();
		String key = requestViewWithdraw.getKey();
		String mpt_id = requestViewWithdraw.getMpt_id();
		String no_polis = customResourceLoader.clearData(requestViewWithdraw.getNo_polis());
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Withdraw dataViewWithdraw = services.selectViewWithdraw(mpt_id, no_polis);

				// Get SPAJ
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);

				// M-Polis
				if (dataViewWithdraw != null) {
					Date req_date = dataViewWithdraw.getReq_date();
					Date date_status = dataViewWithdraw.getDate_status();
					String description = dataViewWithdraw.getDescription();
					BigDecimal total_penarikan = dataViewWithdraw.getTotal_penarikan();
					String payor_name = dataViewWithdraw.getPayor_name();
					String rekening = dataViewWithdraw.getRekening() != null
							? customResourceLoader.clearData(dataViewWithdraw.getRekening())
							: null;
					String bank_name = dataViewWithdraw.getBank_name();
					String mspo_policy_no_format = dataViewWithdraw.getMspo_policy_no_format();
					String reason_fu = dataViewWithdraw.getReason_fu();
					String lku_symbol = dataViewWithdraw.getLku_symbol();
					String status_polis = dataViewWithdraw.getStatus_polis();
					String nm_product = dataViewWithdraw.getNm_product();

					data.put("mpt_id", mpt_id);
					data.put("req_date", df1.format(req_date));
					data.put("date_status", df1.format(date_status));
					data.put("description", description);
					data.put("total_penarikan", total_penarikan);
					data.put("payor_name", payor_name);
					data.put("bank_name", bank_name);
					data.put("mspo_policy_no_format", mspo_policy_no_format);
					data.put("reason_fu", reason_fu);
					data.put("lku_symbol", lku_symbol);
					data.put("status_polis", status_polis);
					data.put("nm_product", nm_product);

					// Convert rekening
					String rekening_result = customResourceLoader.formatRekening(rekening);

					data.put("rekening", rekening_result);

					ArrayList<DetailWithdraw> dataViewDetailWithdraw = (ArrayList<DetailWithdraw>) services
							.selectViewDetailWithdrawMPolis(mpt_id, dataSPAJ.getReg_spaj());
					ArrayList<HashMap<String, Object>> dataDetailWithdrawTemp = new ArrayList<>();

					for (Integer i = 0; i < dataViewDetailWithdraw.size(); i++) {
						String lji_id = dataViewDetailWithdraw.get(i).getLji_id();
						String lji_invest = dataViewDetailWithdraw.get(i).getLji_invest();
						String lku_symbol_detail = dataViewDetailWithdraw.get(i).getLku_symbol();
						BigDecimal nav_process = dataViewDetailWithdraw.get(i).getNav_process();
						BigDecimal mpt_unit_detail = dataViewDetailWithdraw.get(i).getMpt_unit_detail();
						BigDecimal mpt_jumlah_detail = dataViewDetailWithdraw.get(i).getMpt_jumlah_detail();

						HashMap<String, Object> dataHashMapTemp = new HashMap<>();
						dataHashMapTemp.put("lji_id", lji_id);
						dataHashMapTemp.put("lji_invest", lji_invest);
						dataHashMapTemp.put("lku_symbol_detail", lku_symbol_detail);
						dataHashMapTemp.put("nav_process", nav_process);
						dataHashMapTemp.put("mpt_jumlah_detail",
								mpt_jumlah_detail == BigDecimal.ZERO ? null : mpt_jumlah_detail);
						dataHashMapTemp.put("mpt_unit_detail",
								mpt_unit_detail == BigDecimal.ZERO ? null : mpt_unit_detail);

						dataDetailWithdrawTemp.add(dataHashMapTemp);
					}

					data.put("detail_withdraw", dataDetailWithdrawTemp);

					// Get Biaya Withdraw
					String biaya = null;
					ArrayList<HashMap<String, Object>> dataBiaya = new ArrayList<>();
					ArrayList<CostFinancialTransaction> resultSelect = services
							.selectBiayaForFinancialTransaction(dataSPAJ.getReg_spaj(), mpt_id);
					if (!resultSelect.isEmpty()) {
						HashMap<String, Object> dataTemp = new HashMap<>();
						for (Integer i = 0; i < resultSelect.size(); i++) {
							CostFinancialTransaction hashMapTemp = resultSelect.get(i);
							String label_biaya = hashMapTemp.getLjb_biaya();
							BigDecimal jumlahBiaya = hashMapTemp.getJumlah();
							BigDecimal persenBiaya = hashMapTemp.getPersen();

							if (persenBiaya.intValue() == 0 && jumlahBiaya.intValue() == 0) {
								biaya = lku_symbol + " " + nfZeroTwo.format(jumlahBiaya);
							} else if (persenBiaya.intValue() != 0 && jumlahBiaya.intValue() == 0) {
								biaya = persenBiaya.intValue() + "%";
							} else {
								biaya = lku_symbol + " " + nfZeroTwo.format(jumlahBiaya);
							}

							dataTemp.put("biaya", biaya);
							dataTemp.put("label_biaya", label_biaya.equalsIgnoreCase("other") ? "Biaya" : label_biaya);

							dataBiaya.add(dataTemp);
						}
					}

					error = false;
					message = "Successfully get data view withdraw";
					data.put("cost_withdraw", dataBiaya);
				} else {
					Withdraw dataViewWithdrawPaper = services.selectViewWithdrawPaper(mpt_id, dataSPAJ.getReg_spaj());

					// Paper
					if (dataViewWithdrawPaper != null) {
						Date req_date = dataViewWithdrawPaper.getReq_date();
						Date status_date = dataViewWithdrawPaper.getDate_status();
						BigDecimal total_penarikan = dataViewWithdrawPaper.getTotal_penarikan();
						BigDecimal biaya = dataViewWithdrawPaper.getBiaya();
						String description = dataViewWithdrawPaper.getDescription();
						String payor_name = dataViewWithdrawPaper.getPayor_name();
						String mspo_policy_no_format = dataViewWithdrawPaper.getMspo_policy_no_format();
						String lku_symbol = dataViewWithdrawPaper.getLku_symbol();
						String status_polis = dataViewWithdrawPaper.getStatus_polis();
						String nm_product = dataViewWithdrawPaper.getNm_product();
						String rekening = dataViewWithdrawPaper.getRekening();
						String bank_name = dataViewWithdrawPaper.getBank_name();
						String reason_fu = null;

						data.put("mpt_id", mpt_id);
						data.put("req_date", df1.format(req_date));
						data.put("status_date", df1.format(status_date));
						data.put("description", description);
						data.put("total_penarikan", total_penarikan);
						data.put("payor_name", payor_name);
						data.put("bank_name", bank_name);
						data.put("mspo_policy_no_format", mspo_policy_no_format);
						data.put("reason_fu", reason_fu);
						data.put("lku_symbol", lku_symbol);
						data.put("status_polis", status_polis);
						data.put("nm_product", nm_product);

						// Convert rekening
						String rekening_result = customResourceLoader.formatRekening(rekening);

						data.put("rekening", rekening_result);

						// Detail Withdraw Paper
						ArrayList<DetailWithdraw> dataViewDetailWithdrawPaper = services
								.selectViewDetailWithdrawPaper(mpt_id, no_polis);
						ArrayList<HashMap<String, Object>> dataDetailWithdrawTemp = new ArrayList<>();
						if (dataViewDetailWithdrawPaper.isEmpty()) {
							dataDetailWithdrawTemp = null;
						} else {
							for (Integer i = 0; i < dataViewDetailWithdrawPaper.size(); i++) {
								String lji_id = dataViewDetailWithdrawPaper.get(i).getLji_id();
								String lji_invest = dataViewDetailWithdrawPaper.get(i).getLji_invest();
								String lku_symbol_detail = dataViewDetailWithdrawPaper.get(i).getLku_symbol();
								BigDecimal nav_process = dataViewDetailWithdrawPaper.get(i).getNav_process();
								BigDecimal mpt_unit_detail = dataViewDetailWithdrawPaper.get(i).getMpt_unit_detail();
								BigDecimal mpt_jumlah_detail = dataViewDetailWithdrawPaper.get(i)
										.getMpt_jumlah_detail();

								HashMap<String, Object> dataHashMapTemp = new HashMap<>();
								dataHashMapTemp.put("lji_id", lji_id);
								dataHashMapTemp.put("lji_invest", lji_invest);
								dataHashMapTemp.put("lku_symbol_detail", lku_symbol_detail);
								dataHashMapTemp.put("nav_process", nav_process);
								dataHashMapTemp.put("mpt_jumlah_detail",
										mpt_jumlah_detail == BigDecimal.ZERO ? null : mpt_jumlah_detail);
								dataHashMapTemp.put("mpt_unit_detail",
										mpt_unit_detail == BigDecimal.ZERO ? null : mpt_unit_detail);

								dataDetailWithdrawTemp.add(dataHashMapTemp);
							}

							data.put("detail_withdraw", dataDetailWithdrawTemp);
						}

						// Cost Withdraw
						ArrayList<HashMap<String, Object>> dataBiaya = new ArrayList<>();
						HashMap<String, Object> dataTemp = new HashMap<>();
						String biayaResult = biaya.toString();
						String admin_fee = null;

						if (lku_symbol.equalsIgnoreCase("rp.")) {
							if (biayaResult.equals("0")) {
								admin_fee = lku_symbol + " " + nfZeroTwo.format(new BigDecimal(biayaResult));
							} else if (!biayaResult.equals("0") && biayaResult.length() <= 3) {
								admin_fee = biayaResult + "%";
							} else {
								admin_fee = lku_symbol + " " + nfZeroTwo.format(new BigDecimal(biayaResult));
							}
						} else {
							admin_fee = lku_symbol + " " + biayaResult;
						}

						dataTemp.put("biaya", admin_fee);
						dataTemp.put("label_biaya", "Biaya");
						dataBiaya.add(dataTemp);

						error = false;
						message = "Successfully get data view withdraw";
						data.put("cost_withdraw", dataBiaya);
					} else {
						error = true;
						message = "Failed get data withdraw";
						resultErr = "MPT_ID tidak ditemukan";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 58, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/submitwithdraw", produces = "application/json", method = RequestMethod.POST)
	public String submitWithdraw(@RequestBody RequestSubmitWithdraw requestSubmitWithdraw, HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSubmitWithdraw);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestSubmitWithdraw.getUsername();
		String key = requestSubmitWithdraw.getKey();
		Integer language_id = requestSubmitWithdraw.getLanguage_id();
		String no_polis = customResourceLoader.clearData(requestSubmitWithdraw.getNo_polis());
		String mpt_id = requestSubmitWithdraw.getWithdraw().getMpt_id();
		BigDecimal mpt_jumlah = requestSubmitWithdraw.getWithdraw().getMpt_jumlah();
		BigDecimal mpt_unit = requestSubmitWithdraw.getWithdraw().getMpt_unit();
		Integer lt_id = requestSubmitWithdraw.getWithdraw().getLt_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<DetailWithdraw> arrayList = requestSubmitWithdraw.getWithdraw().getDetail_withdraw();

				// Get SPAJ
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);
				String reg_spaj = dataSpaj.getReg_spaj();

				// Check mpt id sudah digunakan atau belum
				Integer checkTransId = 0;
				Integer checkBiayaTransaksi = 0;

				if (mpt_id != null) {
					checkTransId = services.selectCountTransId(mpt_id);
					checkBiayaTransaksi = services.selectCheckBiayaTransaksi(mpt_id, reg_spaj, lt_id);
				} else {
					checkTransId = 1;
				}

				if (arrayList.isEmpty()) {
					error = true;
					message = "Data detail withdraw empty";
					resultErr = "Data detail withdraw tidak boleh kosong, MPT_ID: " + mpt_id;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else if ((mpt_jumlah == BigDecimal.ZERO && mpt_unit == BigDecimal.ZERO) || (mpt_jumlah == null)
						|| (mpt_unit == null)) {
					error = true;
					message = "MPT_JUMLAH & MPT_UNIT empty";
					resultErr = "MPT_JUMLAH & MPT_UNIT Kosong/ ada data yang null, MPT_ID: " + mpt_id;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else if (checkTransId > 0) {
					error = true;
					message = "MPT_ID has been used/ MPT_ID is null";
					resultErr = "MPT_ID yang disubmit sudah pernah digunakan/ MPT_ID null, MPT_ID: " + mpt_id;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else if (checkBiayaTransaksi.equals(0)) {
					error = true;
					message = "Data cost empty";
					resultErr = "Data biaya kosong, mungkin tidak ter hit/ SP error, MPT_ID: " + mpt_id;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					String lku_id = requestSubmitWithdraw.getWithdraw().getLku_id();
					String payor_name = requestSubmitWithdraw.getWithdraw().getPayor_name();
					String rekening = requestSubmitWithdraw.getWithdraw().getRekening();
					String bank_name = requestSubmitWithdraw.getWithdraw().getBank_name();

					// Generate Dokumen Withdraw
					Withdraw dataFormWithdraw = services.selectDataFormWithdraw(reg_spaj);
					String kodeCabang = services.getKodeCabang(no_polis);

					String basePath = storageWithdraw;
					File folder = new File(basePath + File.separator + kodeCabang + File.separator + reg_spaj + File.separator + "Dokumen_Withdraw");
					if (!folder.exists()) {
						folder.mkdirs();
					}
					
					String folderDB = storageWithdrawDB + kodeCabang + '\\' + reg_spaj + '\\' + "Dokumen_Withdraw";

					// Get Path File
					String pathFile = folder + File.separator + mpt_id + ".pdf";
					String pathFileDB = folderDB + '\\' + mpt_id + ".pdf";

					// Get tanggal transaksi
					ProductUtama dataProductCode = services.selectProductCode(reg_spaj);
					BigDecimal lsbs_id = dataProductCode.getLsbs_id();
					BigDecimal lsdbs_number = dataProductCode.getLsdbs_number();

					String lsbs_idToStr = lsbs_id.toString();
					String lsdbs_numberToStr = lsdbs_number.toString();
					String combinationProductCode = lsbs_idToStr + lsdbs_numberToStr;
					Integer group_product = 0;

					if (lsbs_idToStr.equals("213") || lsbs_idToStr.equals("216")) {
						group_product = 1;
					} else if (combinationProductCode.equals("1345") || combinationProductCode.equals("13410")
							|| combinationProductCode.equals("13411") || combinationProductCode.equals("13412")
							|| combinationProductCode.equals("2151")) {
						group_product = 1;
					}

					String dateTransaction = customResourceLoader.getDateTransaction(group_product);

					// Insert EKA.MST_MPOL_TRANS
					services.insertWithdraw(mpt_id, customResourceLoader.getDatetimeJava1(), reg_spaj, lt_id, lku_id,
							mpt_jumlah, mpt_unit, customResourceLoader.getDatetimeJava(), payor_name, rekening,
							bank_name, pathFileDB, dateTransaction);

					for (Integer i = 0; i < arrayList.size(); i++) {
						String lji_id = requestSubmitWithdraw.getWithdraw().getDetail_withdraw().get(i).getLji_id();
						BigDecimal mpt_jumlah_detail = requestSubmitWithdraw.getWithdraw().getDetail_withdraw().get(i)
								.getMpt_jumlah_detail();
						BigDecimal mpt_unit_detail = requestSubmitWithdraw.getWithdraw().getDetail_withdraw().get(i)
								.getMpt_unit_detail();

						// Insert EKA.MST_MPOL_TRANS_DET
						services.insertDetailWithdraw(mpt_id, lji_id, mpt_jumlah_detail, mpt_unit_detail);
					}

					System.out.println(pathFile);

					BigDecimal lsbs_id_generate = dataFormWithdraw.getLsbs_id();
					BigDecimal lsdbs_number_generate = dataFormWithdraw.getLsdbs_number();
					String nm_product_generate = dataFormWithdraw.getNm_product() != null
							? dataFormWithdraw.getNm_product().toString()
							: "";
					String no_hp_generate = dataFormWithdraw.getNo_hp() != null ? dataFormWithdraw.getNo_hp().toString()
							: "";
					String email_generate = dataFormWithdraw.getEmail() != null ? dataFormWithdraw.getEmail().toString()
							: "";

					customResourceLoader.generatePdfWithdraw(lsbs_id_generate.intValue(),
							lsdbs_number_generate.intValue(), nm_product_generate, no_polis, payor_name, no_hp_generate,
							email_generate, arrayList, pathFile);

					// Hit SP Bang Billi Jalanin transaksi
					services.storedProcedureSubmitFinancialTransaction(reg_spaj, mpt_id);

					// Select Configuration M-Polis
					HashMap<String, Object> dataConfiguration = services.configuration();

					// Send Notification
					String messageNotif = null;

					if (language_id.equals(1)) {
						messageNotif = (String) dataConfiguration.get("NOTIFICATION_WITHDRAW_IDN");
					} else {
						messageNotif = (String) dataConfiguration.get("NOTIFICATION_WITHDRAW_ENG");
					}

					customResourceLoader.pushNotif(username, messageNotif, no_polis, reg_spaj, 12, 0);

					error = false;
					message = "Successfully submit data withdraw";
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			// Push Notification Telegram
			customResourceLoader.pushTelegram("@mfajarsep_bot",
					"Path: " + request.getServletPath() + " username: " + username + "," + " Error: " + e);

			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 59, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/claimsubmission", produces = "application/json", method = RequestMethod.POST)
	public String claimSubmission(@RequestBody RequestClaimSubmission requestClaimSubmission,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestClaimSubmission);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestClaimSubmission.getUsername();
		String key = requestClaimSubmission.getKey();
		String no_polis = customResourceLoader.clearData(requestClaimSubmission.getNo_polis());
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get basic information
				HashMap<String, Object> hashMapBasicInformation = new HashMap<>();
				HashMap<String, Object> info_bank = new HashMap<>();
				ClaimSubmission dataBasicInformation = services.selectBasicInformationForClaimSubmission(no_polis);
				if (dataBasicInformation == null) {
					// Data basic information empty
					error = true;
					message = "Data customer empty";
					resultErr = "Data customer kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					BigInteger mpc_id = services.selectGetMpcId();

					String kode_cabang = services.getKodeCabang(no_polis);

					// Basic Information
					hashMapBasicInformation.put("mpc_id", mpc_id);
					hashMapBasicInformation.put("no_polis", dataBasicInformation.getNo_polis());
					hashMapBasicInformation.put("reg_spaj", dataBasicInformation.getReg_spaj());
					hashMapBasicInformation.put("mste_insured_no", dataBasicInformation.getMste_insured_no());
					hashMapBasicInformation.put("nm_pemegang", dataBasicInformation.getNm_pemegang());
					hashMapBasicInformation.put("lssp_id", dataBasicInformation.getLssp_id());
					hashMapBasicInformation.put("status_polis", dataBasicInformation.getStatus_polis());
					hashMapBasicInformation.put("no_hp", dataBasicInformation.getNo_hp());
					hashMapBasicInformation.put("email", dataBasicInformation.getEmail());
					hashMapBasicInformation.put("lku_id", dataBasicInformation.getLku_id());
					hashMapBasicInformation.put("lku_symbol", dataBasicInformation.getLku_symbol());
					hashMapBasicInformation.put("kode_cabang", kode_cabang);
					hashMapBasicInformation.put("no_hp", dataBasicInformation.getNo_hp());

					info_bank.put("mrc_nama", dataBasicInformation.getAtas_nama_rekening());
					info_bank.put("lsbp_id", dataBasicInformation.getLsbp_id());
					info_bank.put("mrc_cabang", dataBasicInformation.getMrc_cabang());

					// Bentuk Jenis Claim
					ArrayList<ClaimSubmission> bentukClaimSubmission = services
							.selectChooseJenisClaim(dataBasicInformation.getReg_spaj());
					ArrayList<HashMap<String, Object>> arrayTemp = new ArrayList<>();
					if (!bentukClaimSubmission.isEmpty()) {
						for (int x = 0; x < bentukClaimSubmission.size(); x++) {
							try {
								HashMap<String, Object> hashMapTemp = new HashMap<>();
								String id = bentukClaimSubmission.get(x).getLjp_id();
								String name = bentukClaimSubmission.get(x).getJenisclaim();

								hashMapTemp.put("id", id);
								hashMapTemp.put("name", name);

								arrayTemp.add(hashMapTemp);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username
										+ " No. Polis: " + no_polis + " Exception jenis claim: " + e);
							}
						}
					}

					// Rekening
					try {
						String rekeningClient = dataBasicInformation.getRekening() != null
								? customResourceLoader.clearData(dataBasicInformation.getRekening())
								: null;
						String bank = dataBasicInformation.getBank();

						info_bank.put("bank", bank);
						info_bank.put("rekening_original", rekeningClient);

						String rekening = null;

						if (rekeningClient != null) {
							rekening = customResourceLoader.formatRekening(rekeningClient);
						}

						info_bank.put("rekening", rekening);

						error = false;
						message = "Successfully get data claim submission";
						data.put("formulir_claimsubmission", hashMapBasicInformation);
						data.put("bentuk_claimsubmission", arrayTemp);
						data.put("info_bank", info_bank);
					} catch (Exception e) {
						logger.error("Path: " + request.getServletPath() + " Username: " + username
								+ " Error get rekening & bank claim submission: " + e);
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 60, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/dropdownclaimsubmission", produces = "application/json", method = RequestMethod.POST)
	public String dropdownClaimsubmission(@RequestBody RequestDropdownClaimsubmission requestDropdownClaimsubmission,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDropdownClaimsubmission);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestDropdownClaimsubmission.getUsername();
		String key = requestDropdownClaimsubmission.getKey();
		Integer type = requestDropdownClaimsubmission.getType();
		String nama = requestDropdownClaimsubmission.getNama_tertanggung();
		String no_polis = requestDropdownClaimsubmission.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get REG_SPAJ
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);

				ArrayList<HashMap<String, Object>> tertanggung_data = new ArrayList<>();
				ArrayList<HashMap<String, Object>> product_data = new ArrayList<>();

				if (type.equals(1)) {
					ArrayList<ClaimSubmission> dataTertanggungClaimSubmission = services
							.selectAllTertanggungAndProductClaimSubmission(dataSpaj.getReg_spaj(), type, null);

					if (dataTertanggungClaimSubmission.isEmpty()) {
						// Handle data tertanggung empty
						error = true;
						message = "Failed get data";
						resultErr = "Data tertanggung kosong, username: " + username + ", No. Polis: " + no_polis;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else {
						for (int x = 0; x < dataTertanggungClaimSubmission.size(); x++) {
							HashMap<String, Object> hashMapTemp = new HashMap<>();
							try {
								String nama_tertanggung = dataTertanggungClaimSubmission.get(x).getNama_tertanggung();
								hashMapTemp.put("id", x);
								hashMapTemp.put("nama_tertanggung", nama_tertanggung);

								tertanggung_data.add(hashMapTemp);
							} catch (Exception e) {
								logger.error("Exception get nama tertanggung claim submission, username: " + username
										+ ", Error " + e);
							}
						}

						error = false;
						message = "Successfully get data";
						product_data = null;
						data.put("tertanggung_data", tertanggung_data);
						data.put("product_data", product_data);
					}
				} else if (type.equals(2)) {
					ArrayList<ClaimSubmission> dataProductClaimSubmission = services
							.selectAllTertanggungAndProductClaimSubmission(dataSpaj.getReg_spaj(), type, nama);

					if (dataProductClaimSubmission.isEmpty()) {
						// Handle data product empty
						error = true;
						message = "Failed get data";
						resultErr = "Data product kosong, username: " + username + ", No. Polis: " + no_polis;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else {
						for (int x = 0; x < dataProductClaimSubmission.size(); x++) {
							HashMap<String, Object> hashMapTemp = new HashMap<>();
							try {
								String nama_product = dataProductClaimSubmission.get(x).getNm_product();
								BigInteger lsbs_id = dataProductClaimSubmission.get(x).getLsbs_id();
								BigInteger lsdbs_number = dataProductClaimSubmission.get(x).getLsdbs_number();
								String groupclaimjenis = dataProductClaimSubmission.get(x).getGroupclaimjenis();
								String jenis_tertanggung = dataProductClaimSubmission.get(x).getJenis_tertanggung();

								hashMapTemp.put("id", x);
								hashMapTemp.put("nama_product", nama_product);
								hashMapTemp.put("lsbs_id", lsbs_id);
								hashMapTemp.put("lsdbs_number", lsdbs_number);
								hashMapTemp.put("groupclaimjenis", groupclaimjenis);
								hashMapTemp.put("jenis_tertanggung", jenis_tertanggung);

								product_data.add(hashMapTemp);
							} catch (Exception e) {
								logger.error("Exception get nama product claim submission, username: " + username
										+ ", Error " + e);
							}
						}

						error = false;
						message = "Successfully get data";
						tertanggung_data = null;
						data.put("tertanggung_data", tertanggung_data);
						data.put("product_data", product_data);
					}
				} else {
					// Handle type not found
					error = true;
					message = "Failed get data";
					resultErr = "Type yang dimasukkan tidk ditemukan, username: " + username + ", No. Polis: "
							+ no_polis + ", Type: " + type;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 64, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/listclaimsubmission", produces = "application/json", method = RequestMethod.POST)
	public String listClaimSubmission(@RequestBody RequestListClaimSubmission requestListClaimSubmission,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestListClaimSubmission);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String username = requestListClaimSubmission.getUsername();
		String key = requestListClaimSubmission.getKey();
		String no_polis = requestListClaimSubmission.getNo_polis();
		Integer pageNumber = requestListClaimSubmission.getPageNumber();
		Integer pageSize = requestListClaimSubmission.getPageSize();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get REG_SPAJ
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);

				ArrayList<ClaimSubmission> arrayList = services.selectListClaimSubmission(dataSpaj.getReg_spaj(),
						pageNumber, pageSize);
				if (arrayList.isEmpty()) {
					error = false;
					message = "List claim submission empty";
				} else {
					for (int i = 0; i < arrayList.size(); i++) {
						BigInteger mpc_id = arrayList.get(i).getKode_trans();
						Date regapldate = arrayList.get(i).getRegapldate();
						String jenisclaim = arrayList.get(i).getJenisclaim();
						String status = arrayList.get(i).getStatus();
						Date date_update_status = arrayList.get(i).getDate_update_status();
						BigDecimal amt_claim = arrayList.get(i).getAmt_claim();
						BigDecimal flag_susulan = arrayList.get(i).getFlag_susulan();
						BigDecimal id_status = arrayList.get(i).getId_status();
						String reason_further = arrayList.get(i).getReason_further();
						String lca_id = arrayList.get(i).getLca_id();

						HashMap<String, Object> hashMapTemp = new HashMap<>();
						hashMapTemp.put("mpc_id", mpc_id);
						hashMapTemp.put("reg_apldate", regapldate != null ? df1.format(regapldate) : null);
						hashMapTemp.put("jenis_claim", jenisclaim);
						hashMapTemp.put("status", status);
						hashMapTemp.put("date_update_status",
								date_update_status != null ? df1.format(date_update_status) : null);
						hashMapTemp.put("amt_claim", amt_claim);
						hashMapTemp.put("reg_spaj", dataSpaj.getReg_spaj());
						hashMapTemp.put("kode_cabang", lca_id);
						hashMapTemp.put("reason_further", reason_further);

						if (id_status.intValue() == 3 && flag_susulan.intValue() == 0) {
							hashMapTemp.put("enable_further", true);
						} else {
							hashMapTemp.put("enable_further", false);
						}

						data.add(hashMapTemp);
					}

					error = false;
					message = "Successfully get list data claim submission";
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 62, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/viewclaimsubmission", produces = "application/json", method = RequestMethod.POST)
	public String viewClaimSubmission(@RequestBody RequestViewClaimSubmission requestViewClaimSubmission,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewClaimSubmission);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestViewClaimSubmission.getUsername();
		String key = requestViewClaimSubmission.getKey();
		String no_polis = requestViewClaimSubmission.getNo_polis();
		BigInteger mpc_id = requestViewClaimSubmission.getMpc_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get REG_SPAJ
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);

				if (dataSpaj != null) {
					ClaimSubmission dataViewClaimSubmission = services.selectViewClaimsubmission(dataSpaj.getReg_spaj(),
							mpc_id);

					if (dataViewClaimSubmission != null) {
						BigInteger kode_trans = dataViewClaimSubmission.getKode_trans();
						String mspo_policy_no_format = dataViewClaimSubmission.getNo_polis();
						String nm_pemegang = dataViewClaimSubmission.getNm_pemegang();
						String status_polis = dataViewClaimSubmission.getStatus_polis();
						String nm_product = dataViewClaimSubmission.getNm_product();
						String patienname = dataViewClaimSubmission.getPatienname();
						String nm_product_claim = dataViewClaimSubmission.getNm_product_claim();
						String jenis_claim = dataViewClaimSubmission.getJenisclaim();
						Date date_ri_1 = dataViewClaimSubmission.getDate_ri_1();
						Date date_ri_2 = dataViewClaimSubmission.getDate_ri_2();
						BigDecimal amt_claim = dataViewClaimSubmission.getAmt_claim();
						String rekeningClient = dataViewClaimSubmission.getRekening() != null
								? customResourceLoader.clearData(dataViewClaimSubmission.getRekening())
								: null;
						String bank = dataViewClaimSubmission.getBank();
						String path_claim = dataViewClaimSubmission.getPath_claim();
						String reason = dataViewClaimSubmission.getReason();
						Integer double_cover_claim = dataViewClaimSubmission.getDouble_cover_claim();
						Date date_insert = dataViewClaimSubmission.getRegapldate();
						String status = dataViewClaimSubmission.getStatus();
						String date_status = dataViewClaimSubmission.getDate_status();
						
						/*\\storage.sinarmasmsiglife.co.id\pdfind\m-Policytest\09\09170016255\DocumentClaimSubmission\2020000410*/
						
						String tempPathClaim = path_claim.replace("\\", "/");
						tempPathClaim = tempPathClaim.replace("//", "/");
						String tempPathArray[] = tempPathClaim.split("/");
						
						String tempPathClaimJoin = tempPathArray[4].toString() + "/" + tempPathArray[5].toString() + "/" 
								+ tempPathArray[6].toString() + "/" + tempPathArray[7].toString();
						
						tempPathClaimJoin = storageClaimMpolicy + "/" + tempPathClaimJoin;

						Boolean boolean_double_cover_claim = false;

						if (double_cover_claim.equals(1)) {
							boolean_double_cover_claim = true;
						}

						// Rekening
						String rekening = customResourceLoader.formatRekening(rekeningClient);

						data.put("kode_trans", kode_trans);
						data.put("mspo_policy_no_format", mspo_policy_no_format);
						data.put("nm_pemegang", nm_pemegang);
						data.put("status_polis", status_polis);
						data.put("nm_product", nm_product);
						data.put("patienname", patienname);
						data.put("nm_product_claim", nm_product_claim);
						data.put("jenis_claim", jenis_claim);
						data.put("date_ri_1", df1.format(date_ri_1));
						data.put("date_ri_2", date_ri_2 != null ? df1.format(date_ri_2) : null);
						data.put("amt_claim", amt_claim);
						data.put("rekening", rekening);
						data.put("bank", bank);
						data.put("double_cover_claim", boolean_double_cover_claim);
						data.put("reason", reason);
						data.put("date_insert", df.format(date_insert));
						data.put("status", status);
						data.put("date_status", date_status);

						// List file in folder claim
						ArrayList<HashMap<String, Object>> arrayTemp = new ArrayList<>();
						List<String> pathFileClaim = customResourceLoader.listFilesUsingJavaIO2CustomSorted(tempPathClaimJoin);
						for (String name : pathFileClaim) {
							HashMap<String, Object> hashMapPathClaim = new HashMap<>();
							if ((!name.toLowerCase().contains("form_rawat_inap.pdf"))
									&& (!name.toLowerCase().substring(0, 1).equals("."))
									&& (!name.toLowerCase().contains("formrawatinapgenerate.pdf"))) {
								hashMapPathClaim.put("name", name.replace("MPOLIS_", "").replace("_", " "));
								hashMapPathClaim.put("path_file", path_claim + "\\" + name);

								arrayTemp.add(hashMapPathClaim);
							}
						}

						data.put("data_claim", arrayTemp);

						error = false;
						message = "Successfully get view claim submission";
					} else {
						// Kode trans tidak ditemukan
						error = true;
						message = "Transaction code not found";
						resultErr = "Kode transaksi tidak ditemukan (" + mpc_id + ")";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					// SPAJ tidak ditemukan
					error = true;
					message = "Failed get data";
					resultErr = "REG SPAJ tidak ditemukan, No polis: " + no_polis;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 63, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/downloadfileclaimsubmission", produces = "application/json", method = RequestMethod.POST)
	public String downloadBerkasClaimSubmission(
			@RequestBody RequestDownloadFileClaimSubmission requestDownloadFileClaimSubmission,
			HttpServletRequest request, HttpServletResponse response) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDownloadFileClaimSubmission);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestDownloadFileClaimSubmission.getUsername();
		String key = requestDownloadFileClaimSubmission.getKey();
		Integer typeFile = requestDownloadFileClaimSubmission.getType();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				String pathFolder = pathFormClaimSubmission;
				File folder = new File(pathFolder);
				String pathFile = null;

				// Path File
				// 1 Rawat Inap
				if (typeFile.equals(1)) {
					pathFile = folder.toString() + File.separator + "FormRawatInap" + ".pdf";
				// 2 Rawat Jalan
				} else if (typeFile.equals(2)) {
					pathFile = folder.toString() + File.separator + "FormRawatJalan" + ".pdf";
				// 3 Personal Accident
				} else if (typeFile.equals(3)) {
					pathFile = folder.toString() + File.separator + "FormPersonalAccident" + ".pdf";
				} else if (typeFile.equals(4)) {
				// 4 Document yang diupload user
					pathFile = requestDownloadFileClaimSubmission.getPathFile();
					/*\\\\storage.sinarmasmsiglife.co.id\\pdfind\\m-Policytest\\09\\09170016255\\DocumentClaimSubmission\\2020000410\\MPOLIS_Photocopy_of_prescription_and_diagnostic_test_reading_results.pdf*/
					String tempPathFile = pathFile.replace("\\", "/");
					tempPathFile = tempPathFile.replace("//", "/");
					String tempPathArray[] = tempPathFile.split("/");
					
					String tempPathClaimJoin = tempPathArray[4].toString() + "/" + tempPathArray[5].toString() + "/" 
							+ tempPathArray[6].toString() + "/" + tempPathArray[7].toString() + "/" + tempPathArray[8].toString();
					
					pathFile = storageClaimMpolicy + "/" + tempPathClaimJoin;
				} else if (typeFile.equals(5)) {
				// 5 Rawat Jalan Corporate
					pathFile = folder.toString() + File.separator + "FormRawatJalanCorporate" + ".pdf";
				} else if (typeFile.equals(6)) {
				// 6 Rawat Inap Corporate
					pathFile = folder.toString() + File.separator + "FormRawatInapCorporate" + ".pdf";
				}

				// Path File yang mau di download
				File file = new File(pathFile);

				try {
					// Content-Disposition
					response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

					// Content-Length
					response.setContentLength((int) file.length());

					BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
					BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

					byte[] buffer = new byte[1024];
					int bytesRead = 0;
					while ((bytesRead = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, bytesRead);
					}
					outStream.flush();
					inStream.close();

					error = false;
					message = "Download Success";
				} catch (Exception e) {
					error = true;
					message = "Download Failed";
					resultErr = "File tidak ada";
					logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
				}
			} else {
				error = true;
				message = "Download Failed";
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
		customResourceLoader.insertHistActivityWS(12, 61, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/submitclaimsubmission", produces = "application/json", method = RequestMethod.POST)
	public String submitClaimSubmission(@RequestBody RequestSubmitClaimSubmission requestSubmitClaimSubmission,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSubmitClaimSubmission);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestSubmitClaimSubmission.getUsername();
		String key = requestSubmitClaimSubmission.getKey();
		BigInteger mpc_id = requestSubmitClaimSubmission.getMpc_id();
		String no_polis = requestSubmitClaimSubmission.getNo_polis();
		Integer mste_insured_no = requestSubmitClaimSubmission.getMste_insured_no();
		String nm_pemegang = requestSubmitClaimSubmission.getNm_pemegang();
		String patienname = requestSubmitClaimSubmission.getPatienname();
		String lku_id = requestSubmitClaimSubmission.getLku_id();
		Integer lsbs_id = requestSubmitClaimSubmission.getLsbs_id();
		Integer lsdbs_number = requestSubmitClaimSubmission.getLsdbs_number();
		String date_ri_1 = requestSubmitClaimSubmission.getDate_ri_1();
		String date_ri_2 = requestSubmitClaimSubmission.getDate_ri_2();
		Integer amount_ri = requestSubmitClaimSubmission.getAmount_ri();
		Integer jenisclaim = requestSubmitClaimSubmission.getJenisclaim();
		String no_hp = requestSubmitClaimSubmission.getNo_hp();
		String email = requestSubmitClaimSubmission.getEmail();
		Integer lssp_id = requestSubmitClaimSubmission.getLssp_id();
		String groupclaimjenis = requestSubmitClaimSubmission.getGroupclaimjenis();
		String accountno = requestSubmitClaimSubmission.getAccountno();
		Integer lsbp_id = requestSubmitClaimSubmission.getLsbp_id();
		String nama_cabang = requestSubmitClaimSubmission.getNama_cabang();
		String atasnama = requestSubmitClaimSubmission.getAtas_nama();
		Boolean double_cover_claim = requestSubmitClaimSubmission.getDouble_cover_claim();
		String jenis_tertanggung = requestSubmitClaimSubmission.getJenis_tertanggung();
		Integer language_id = requestSubmitClaimSubmission.getLanguage_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Check Trans ID
				Integer countTransId = 1;
				if (mpc_id != null) {
					countTransId = services.selectCountTransIdClaim(mpc_id.toString());
				}

				if ((mpc_id == null) || (no_polis == null) || (patienname == null) || (jenisclaim == null)) {
					// Handle data mandatory kosong
					error = true;
					message = "Failed submit data";
					resultErr = "Data mandatory kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else if (countTransId > 0) {
					// Handle mpc_id sudah pernah digunakan
					error = true;
					message = "Failed submit data, mpc_id has already been used";
					resultErr = "MPC_ID sudah pernah digunakan, mpc_id: " + mpc_id;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					// Get REG_SPAJ
					Pemegang paramCheckSpaj = new Pemegang();
					paramCheckSpaj.setMspo_policy_no(no_polis);
					Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);

					String reg_spaj = dataSpaj.getReg_spaj();

					// Get Kode cabang
					String kodeCabang = services.getKodeCabang(no_polis);

					Integer double_cover_claim_res = 0;
					if (double_cover_claim.equals(true)) {
						double_cover_claim_res = 1;
					}

					String path_claim = storageClaimMpolicy + File.separator + kodeCabang + File.separator + reg_spaj + File.separator + "DocumentClaimSubmission"
							+ File.separator + mpc_id;
					
					String path_claim_db = storageMpolicyDB + kodeCabang + "\\" + reg_spaj + "\\" + "DocumentClaimSubmission"
							+ "\\" + mpc_id;
					
					if(date_ri_2 == null) {
						date_ri_2 = date_ri_1;
					}

					// Insert Claim Trans
					services.insertClaimSubmissionTrans(mpc_id, reg_spaj, mste_insured_no, patienname, lku_id, lsbs_id,
							lsdbs_number, date_ri_1, date_ri_2, amount_ri, jenisclaim, no_hp, email, path_claim_db,
							lssp_id, groupclaimjenis, double_cover_claim_res);

					// Insert Detail Claim Trans
					services.insertDetailClaimSubmissionTrans(mpc_id, reg_spaj, accountno, lsbp_id, nama_cabang,
							atasnama);

					// Jika Rawat Inap Generate Dokumen
					if (jenisclaim.equals(2)) {
						Integer typeSelect = 1;
						if (jenis_tertanggung.equalsIgnoreCase("tambahan")) {
							typeSelect = 2;
						}

						File folder = new File(path_claim);
						if (!folder.exists()) {
							folder.mkdirs();
						}

						String path_output = folder + File.separator + "Form_rawat_inap" + ".pdf";

						ClaimSubmission dataGeneratePdf = services.selectGeneratePdfClaim(typeSelect, reg_spaj,
								patienname);
						String nama_pasien = dataGeneratePdf.getNama() != null ? dataGeneratePdf.getNama() : "";
						String status_pasien = dataGeneratePdf.getRelasi() != null ? dataGeneratePdf.getRelasi() : "";
						String alamat = dataGeneratePdf.getAlamat_rumah() != null ? dataGeneratePdf.getAlamat_rumah()
								: "";
						String kota_rumah = dataGeneratePdf.getKota_rumah() != null ? dataGeneratePdf.getKota_rumah()
								: "";
						BigDecimal umur_pasien = dataGeneratePdf.getUmur() != null ? dataGeneratePdf.getUmur() : null;

						String alamat_result = null;
						if (kota_rumah != null) {
							alamat_result = alamat + ", " + kota_rumah;
						} else {
							alamat_result = alamat;
						}

						String alamat_nohp = alamat_result + "/ " + no_hp;

						customResourceLoader.generatePdfClaimSubmission(mpc_id.toString(), nm_pemegang, nama_pasien,
								status_pasien, alamat_nohp, umur_pasien.intValue(), email, amount_ri.toString(),
								path_output);
					}

					// Select Configuration M-Polis
					HashMap<String, Object> dataConfiguration = services.configuration();

					// Send Notification
					String messageNotif = null;

					if (language_id.equals(1)) {
						messageNotif = (String) dataConfiguration.get("NOTIFICATION_CLAIM_INDIVIDUAL_IDN");
					} else {
						messageNotif = (String) dataConfiguration.get("NOTIFICATION_CLAIM_INDIVIDUAL_ENG");
					}

					customResourceLoader.pushNotif(username, messageNotif, no_polis, reg_spaj, 13, 0);

					error = false;
					message = "Successfully submit data claim";
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed submit data";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			// Push Notification Telegram
			customResourceLoader.pushTelegram("@mfajarsep_bot",
					"Path: " + request.getServletPath() + " username: " + username + "," + " Error: " + e);

			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 65, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/uploaddeletefileclaimsubmission", produces = "application/json", method = RequestMethod.POST)
	public String uploadDeleteFile(@RequestBody RequestUploadDeleteFileClaimSub requestUploadDeleteFileClaimSub,
			HttpServletRequest request) throws IOException {
		System.out.println("Start: " + new Date());
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestUploadDeleteFileClaimSub);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestUploadDeleteFileClaimSub.getUsername();
		String key = requestUploadDeleteFileClaimSub.getKey();
		String reg_spaj = requestUploadDeleteFileClaimSub.getReg_spaj();
		String kodeCabang = requestUploadDeleteFileClaimSub.getKode_cabang();
		String name_file = requestUploadDeleteFileClaimSub.getName_file();
		String file_base64 = requestUploadDeleteFileClaimSub.getFile_base64();
		Integer type = requestUploadDeleteFileClaimSub.getType();
		BigInteger mpc_id = requestUploadDeleteFileClaimSub.getMpc_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				String nameFileNew = null;
				
				int lastIndexOf = name_file.lastIndexOf(".");
		        if (lastIndexOf > -1) {
		            nameFileNew = name_file.substring(0, lastIndexOf);
		            System.out.println(nameFileNew);
		        }
				
				if (type.equals(1)) {
					if ((name_file != null) && (file_base64 != null) && (reg_spaj != null) && (mpc_id != null)
							&& (name_file != "") && (file_base64 != "") && (reg_spaj != "")) {
						String path_claim = storageClaimMpolicy + File.separator + kodeCabang + File.separator + reg_spaj + File.separator
								+ "DocumentClaimSubmission" + File.separator + mpc_id;

						System.out.println("Upload Start: " + new Date());
						Boolean uploadFile = customResourceLoader.uploadFileToStorage(path_claim, file_base64,
								name_file, username, request.getServletPath(), mpc_id);
						System.out.println("Upload Done: " + new Date());

						if (uploadFile.equals(true)) {
							String filePath = path_claim + File.separator + nameFileNew + ".pdf";
							Boolean validateFilePdf = customResourceLoader.validateFilePdf(filePath, username,
									mpc_id.toString());
							if (validateFilePdf.equals(true)) {
								error = false;
								message = "Successfully upload file";
								data.put("name_file", nameFileNew + ".pdf");
							} else {
								data = null;
								error = true;
								message = "Failed upload file";
								resultErr = "File PDF Corrupt, MPC_ID: " + mpc_id.toString() + ", Name File: "
										+ name_file;
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						} else {
							data = null;
							error = true;
							message = "Failed upload file";
							resultErr = "Upload file error, REG SPAJ: " + reg_spaj;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						data = null;
						error = true;
						message = "Failed upload file, key must not be null/ empty";
						resultErr = "Upload file error, REG SPAJ: " + reg_spaj;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (type.equals(2)) {
					if ((name_file != null) && (reg_spaj != null) && (mpc_id != null) && (name_file != "")
							&& (reg_spaj != "")) {
						String path_claim = storageClaimMpolicy + File.separator + kodeCabang + File.separator + reg_spaj + File.separator
								+ "DocumentClaimSubmission" + File.separator + mpc_id + File.separator + nameFileNew + ".pdf";

						String pathLogClaimRequest = pathLogSubmitClaimSubmission + File.separator + mpc_id + File.separator + "request-"
								+ nameFileNew + ".txt";
						String pathLogClaimResponse = pathLogSubmitClaimSubmission + File.separator + mpc_id + File.separator + "response-"
								+ nameFileNew + ".txt";

						try {
							File file = new File(path_claim);
							if (file.delete()) {
								Path path2 = Paths.get(pathLogClaimRequest);
								Files.walk(path2).sorted(Comparator.reverseOrder()).map(Path::toFile)
										.forEach(File::delete);

								Path path3 = Paths.get(pathLogClaimResponse);
								Files.walk(path3).sorted(Comparator.reverseOrder()).map(Path::toFile)
										.forEach(File::delete);

								error = false;
								message = "Successfully delete file";
							} else {
								error = true;
								message = "Failed delete file";
								resultErr = "Gagal delete file, path_file: " + path_claim;
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						} catch (Exception e) {
							error = true;
							message = "Failed delete file";
							resultErr = "Gagal delete file, path_file: " + path_claim + ", error: " + e;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						error = true;
						message = "Failed delete file, key must not be null/ empty";
						resultErr = "Delete file error, REG SPAJ: " + reg_spaj;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (type.equals(3)) {
					if ((reg_spaj != null) && (mpc_id != null) && (reg_spaj != "")) {
						String path_claim = storageClaimMpolicy + File.separator + kodeCabang + File.separator + reg_spaj + File.separator
								+ "DocumentClaimSubmission" + File.separator + mpc_id;

						String pathLogClaimSubmission = pathLogSubmitClaimSubmission + mpc_id;

						try {
							Path path1 = Paths.get(path_claim);
							Files.walk(path1).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);

							Path path2 = Paths.get(pathLogClaimSubmission);
							Files.walk(path2).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);

							error = false;
							message = "Successfully delete folder";
						} catch (Exception e) {
							error = true;
							message = "Failed delete folder";
							resultErr = "Gagal delete folder, path_folder: " + path_claim + ", error: " + e;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						// Handle key request null
						error = true;
						message = "Failed delete folder, key must not be null/ empty";
						resultErr = "Delete folder error, REG SPAJ: " + reg_spaj;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					// Handle type not found
					error = true;
					message = "Failed upload/ delete data";
					resultErr = "Type yang dimasukkan tidak tersedia, type: " + type;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed submit data";
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

		if (type.equals(1)) {
			System.out.println("Create txt Start: " + new Date());
			// Generate log .txt diserver M-Polis
			customResourceLoader.createTxt(mpc_id.toString(), req, "request-" + name_file, 1);
			customResourceLoader.createTxt(mpc_id.toString(), res, "response-" + name_file, 1);
			System.out.println("Create txt Done: " + new Date());

			// Insert Log LST_HIST_ACTIVITY_WS
			customResourceLoader.insertHistActivityWS(12, 67, new Date(), null, null, 1, resultErr, start, username);
		} else if ((type.equals(2)) || (type.equals(3))) {
			// Insert Log LST_HIST_ACTIVITY_WS
			customResourceLoader.insertHistActivityWS(12, 67, new Date(), req, res, 1, resultErr, start, username);
		} else {
			// Insert Log LST_HIST_ACTIVITY_WS
			customResourceLoader.insertHistActivityWS(12, 67, new Date(), null, null, 1, resultErr, start, username);
		}
		System.out.println("End: " + new Date());

		return res;
	}
	
	@RequestMapping(value = "/claimlimit", produces = "application/json", method = RequestMethod.POST)
	public String claimLimit(@RequestBody RequestClaimLimit requestClaimLimit,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestClaimLimit);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String username = requestClaimLimit.getUsername();
		String key = requestClaimLimit.getKey();
		String no_polis = requestClaimLimit.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<ClaimLimit> arrayList = services.selectClaimLimit(no_polis);
				if (arrayList.isEmpty()) {
					error = false;
					message = "Data claim limit is empty";
				} else {
					List<String> listClaim = new ArrayList<String>();
					for (int i = 0; i < arrayList.size(); i++) {
						String distinctClaim = arrayList.get(i).getNama_produk();
						listClaim.add(distinctClaim);
					}

					List<String> distinctClaim = listClaim.stream().distinct().collect(Collectors.toList());
					for (int x = 0; x < distinctClaim.size(); x++) {
						HashMap<String, Object> dataTemp = new HashMap<>();
						String nm_produk = distinctClaim.get(x);
						dataTemp.put("lsdbs_name", nm_produk);
						ArrayList<HashMap<String, Object>> detailsClaim = new ArrayList<>();
						for (int y = 0; y < arrayList.size(); y++) {
							if (arrayList.get(y).getNama_produk().equals(distinctClaim.get(x))) {
								HashMap<String, Object> dataClaimDetails = new HashMap<>();
								String regclaim = arrayList.get(y).getRegclaim();
								String mspo_policy_no = arrayList.get(y).getMspo_policy_no();
								String reg_spaj = arrayList.get(y).getReg_spaj();
								String nama_produk = arrayList.get(y).getNama_produk();
								Integer lsbs_id = arrayList.get(y).getLsbs_id();
								Integer lsdbs_number = arrayList.get(y).getLsdbs_number();
								String beg_date = arrayList.get(y).getBeg_date();
								String now = arrayList.get(y).getNow();
								String end_date = arrayList.get(y).getEnd_date();
								String jenis_jaminan = arrayList.get(y).getJenis_jaminan();
								String limit_per = arrayList.get(y).getLimit_per();
								String per_jaminan = arrayList.get(y).getPer_jaminan();
								String limit = arrayList.get(y).getLimit();
								String as_charge = arrayList.get(y).getAs_charge();
								String bayar_klaim = arrayList.get(y).getBayar_klaim();
								String tgl_akseptasi = arrayList.get(y).getTgl_akseptasi();
								String sisa_limit = arrayList.get(y).getSisa_limit();
																
								dataClaimDetails.put("regclaim", regclaim);
								dataClaimDetails.put("mspo_policy_no", mspo_policy_no);
								dataClaimDetails.put("reg_spaj", reg_spaj);
								dataClaimDetails.put("nama_produk", nama_produk);
								dataClaimDetails.put("lsbs_id", lsbs_id);
								dataClaimDetails.put("lsdbs_number", lsdbs_number);
								dataClaimDetails.put("beg_date", beg_date);
								dataClaimDetails.put("now", now);
								dataClaimDetails.put("end_date", end_date);
								dataClaimDetails.put("jenis_jaminan", jenis_jaminan);
								dataClaimDetails.put("limit_per", limit_per);
								dataClaimDetails.put("per_jaminan", per_jaminan);
								dataClaimDetails.put("limit_per", limit);
								dataTemp.put("limit", limit);
								dataClaimDetails.put("as_charge", as_charge);
								dataClaimDetails.put("bayar_klaim", bayar_klaim);
								dataClaimDetails.put("tgl_akseptasi", tgl_akseptasi);
								dataTemp.put("sisa_limit", sisa_limit);								

								detailsClaim.add(dataClaimDetails);
							}
						}

						dataTemp.put("details", detailsClaim);
						data.add(dataTemp);
					}

					error = false;
					message = "Successfully get list data claim submission";
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 62, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/claimsubmissioncorporate", produces = "application/json", method = RequestMethod.POST)
	public String claimSubmissionCorporate(@RequestBody RequestClaimSubmissionCorporate requestClaimSubmissionCorporate,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestClaimSubmissionCorporate);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestClaimSubmissionCorporate.getUsername();
		String key = requestClaimSubmissionCorporate.getKey();
		String mste_insured = requestClaimSubmissionCorporate.getMste_insured();
		String reg_spaj = requestClaimSubmissionCorporate.getReg_spaj();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get data claim submission corporate
				ClaimSubmissionCorporate dataClaimCorporate = services.selectClaimSubmissionCorporate(reg_spaj,
						mste_insured);
				// Get Jenis Claim Corporate
				ArrayList<ClaimSubmissionCorporate> dataJenisClaimCorporate = services
						.selectJenisClaimSubmissionCorporate(reg_spaj, mste_insured);

				if (dataClaimCorporate == null || dataJenisClaimCorporate.isEmpty()) {
					// Data claim & jenis claim corporate kosong
					error = true;
					message = "Data claim or jenis claim empty";
					resultErr = "Data claim/ jenis claim kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else if (dataClaimCorporate.getMspo_type_rek().intValue() != 2) {
					error = true;
					message = "Data no rekening perusahaan";
					resultErr = "Data no rekening ke perusahaan";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					String mpcc_id = services.selectGetMpccId();

					String mspo_policy_no_format = dataClaimCorporate.getMspo_policy_no_format();
					String mcl_first = dataClaimCorporate.getMcl_first();
					String mste_no_reg = dataClaimCorporate.getMste_no_reg().trim();
					String msbc_bank = dataClaimCorporate.getMsbc_bank();
					String msbc_acc_no = dataClaimCorporate.getMsbc_acc_no() != null
							? customResourceLoader.clearData(dataClaimCorporate.getMsbc_acc_no())
							: null;
					String rekening = null;
					Integer lssp_id = dataClaimCorporate.getLssp_id().intValue();
					String mspe_mobile = dataClaimCorporate.getMspe_mobile() != null
							? dataClaimCorporate.getMspe_mobile().trim()
							: null;

					if (msbc_acc_no != null) {
						rekening = customResourceLoader.formatRekening(msbc_acc_no);
					}

					ArrayList<HashMap<String, Object>> arrayTemp = new ArrayList<>();
					for (int x = 0; x < dataJenisClaimCorporate.size(); x++) {
						String jenis_claim_corporate = dataJenisClaimCorporate.get(x).getLabel();
						String alias = dataJenisClaimCorporate.get(x).getLgc_description();

						HashMap<String, Object> dataTemp = new HashMap<>();
						dataTemp.put("jenis_claim_corporate", jenis_claim_corporate);
						dataTemp.put("alias", alias.trim());

						arrayTemp.add(dataTemp);
					}

					error = false;
					message = "Successfully get data";
					data.put("mpcc_id", Integer.parseInt(mpcc_id));
					data.put("mspo_policy_no_format", mspo_policy_no_format);
					data.put("mcl_first", mcl_first);
					data.put("mste_no_reg", mste_no_reg);
					data.put("msbc_bank", msbc_bank);
					data.put("rekening_original", msbc_acc_no);
					data.put("rekening", rekening);
					data.put("lssp_id", lssp_id);
					data.put("mspe_mobile", mspe_mobile);
					data.put("jenis_claim", arrayTemp);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 75, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/namedocumentclaimsubmissioncorporate", produces = "application/json", method = RequestMethod.POST)
	public String jenisClaimSubmissionCorporate(
			@RequestBody RequestDocumentClaimSubmissionCorporate requestDocumentClaimSubmissionCorporate,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDocumentClaimSubmissionCorporate);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String username = requestDocumentClaimSubmissionCorporate.getUsername();
		String key = requestDocumentClaimSubmissionCorporate.getKey();
		String jenis = requestDocumentClaimSubmissionCorporate.getJenis_claim();
		Integer language_id = requestDocumentClaimSubmissionCorporate.getLanguage_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<ClaimSubmissionCorporate> dataDocument = services.selectDocumentJenisClaimCorporate(jenis,
						language_id);

				if (dataDocument.isEmpty()) {
					// Data jenis tidak ada di table mapping
					error = true;
					message = "List document not found";
					resultErr = "List dokumen tidak ditemukan pada jenis tsb";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					error = false;
					message = "Successfully get data";

					for (int x = 0; x < dataDocument.size(); x++) {
						String document_name = dataDocument.get(x).getDocument_name();

						HashMap<String, Object> hashMap = new HashMap<>();
						hashMap.put("id", x + 1);
						hashMap.put("value", document_name);
						data.add(hashMap);
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 76, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/listclaimsubmissioncorporate", produces = "application/json", method = RequestMethod.POST)
	public String listClaimSubmissionCorporate(
			@RequestBody RequestListClaimSubmissionCorporate requestListClaimSubmissionCorporate,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestListClaimSubmissionCorporate);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String username = requestListClaimSubmissionCorporate.getUsername();
		String key = requestListClaimSubmissionCorporate.getKey();
		String mste_insured = requestListClaimSubmissionCorporate.getMste_insured();
		String reg_spaj = requestListClaimSubmissionCorporate.getReg_spaj();
		Integer pageNumber = requestListClaimSubmissionCorporate.getPageNumber();
		Integer pageSize = requestListClaimSubmissionCorporate.getPageSize();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<ClaimSubmissionCorporate> dataClaimSubmissionCorporate = services
						.selectListClaimSubmissionCorporate(reg_spaj, mste_insured, pageNumber, pageSize);

				if (dataClaimSubmissionCorporate.isEmpty()) {
					// Data List Kosong
					error = false;
					message = "Data list claim corporate empty";
				} else {
					error = false;
					message = "Successfully get data";

					for (int x = 0; x < dataClaimSubmissionCorporate.size(); x++) {
						String mpcc_id = dataClaimSubmissionCorporate.get(x).getMpcc_id();
						Date request_date = dataClaimSubmissionCorporate.get(x).getCreated_date();
						Date status_date = dataClaimSubmissionCorporate.get(x).getDate_update_status();
						BigDecimal amount = dataClaimSubmissionCorporate.get(x).getAmount_dibayar();
						BigDecimal flag_susulan = dataClaimSubmissionCorporate.get(x).getFlag_susulan();
						BigDecimal id_status = dataClaimSubmissionCorporate.get(x).getId_status();
						String status = dataClaimSubmissionCorporate.get(x).getStatus();
						String jenis_claim = dataClaimSubmissionCorporate.get(x).getJenis_claim();
						String further_document = dataClaimSubmissionCorporate.get(x).getReason_further();

						HashMap<String, Object> hashMap = new HashMap<>();
						hashMap.put("mpcc_id", mpcc_id);
						hashMap.put("request_date", request_date != null ? df1.format(request_date) : null);
						hashMap.put("status_date",
								status_date != null ? df1.format(status_date) : df1.format(request_date));
						hashMap.put("amount", amount);
						hashMap.put("status", status);
						hashMap.put("jenis_claim", jenis_claim);
						hashMap.put("further_document", further_document);

						if (id_status.intValue() == 2 && flag_susulan.intValue() == 0) {
							hashMap.put("enable_further", true);
						} else {
							hashMap.put("enable_further", false);
						}

						data.add(hashMap);
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 77, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/viewclaimsubmissioncorporate", produces = "application/json", method = RequestMethod.POST)
	public String viewClaimSubmissionCorporate(
			@RequestBody RequestViewClaimSubmissionCorporate requestViewClaimSubmissionCorporate,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewClaimSubmissionCorporate);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestViewClaimSubmissionCorporate.getUsername();
		String key = requestViewClaimSubmissionCorporate.getKey();
		String mste_insured = requestViewClaimSubmissionCorporate.getMste_insured();
		String reg_spaj = requestViewClaimSubmissionCorporate.getReg_spaj();
		String mpcc_id = requestViewClaimSubmissionCorporate.getMpcc_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ClaimSubmissionCorporate dataClaimSubmissionCorporate = services
						.selectViewClaimSubmissionCorporate(reg_spaj, mste_insured, mpcc_id);

				if (dataClaimSubmissionCorporate == null) {
					error = true;
					message = "Data mpcc_id empty";
					resultErr = "Data mppc_id yang dimasukkan kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					Date created_date = dataClaimSubmissionCorporate.getCreated_date();
					Date update_status_date = dataClaimSubmissionCorporate.getDate_update_status();
					Date start_date = dataClaimSubmissionCorporate.getStart_date();
					Date end_date = dataClaimSubmissionCorporate.getEnd_date();
					String jenis_claim = dataClaimSubmissionCorporate.getJenis_claim();
					String status = dataClaimSubmissionCorporate.getStatus();
					String mspo_policy_no_format = dataClaimSubmissionCorporate.getMspo_policy_no_format();
					String mcl_first = dataClaimSubmissionCorporate.getMcl_first();
					String bank = dataClaimSubmissionCorporate.getBank();
					String no_rekening = dataClaimSubmissionCorporate.getNo_rekening();
					String path_storage = dataClaimSubmissionCorporate.getPath_storage();
					String lms_status = dataClaimSubmissionCorporate.getLms_status();
					String reason_decline = dataClaimSubmissionCorporate.getReason_decline();
					BigDecimal amount_dibayar = dataClaimSubmissionCorporate.getAmount_dibayar();
					Integer double_cover_claim = dataClaimSubmissionCorporate.getDouble_cover_claim();

					error = false;
					message = "Successfully get data";
					data.put("mpcc_id", mpcc_id);
					data.put("created_date", created_date != null ? df1.format(created_date) : null);
					data.put("update_status_date",
							update_status_date != null ? df1.format(update_status_date) : df1.format(created_date));
					data.put("amount_dibayar", amount_dibayar);
					data.put("jenis_claim", jenis_claim);
					data.put("jenis_claim", jenis_claim);
					data.put("status", status);
					data.put("mspo_policy_no_format", mspo_policy_no_format);
					data.put("mcl_first", mcl_first);
					data.put("start_date", start_date != null ? df1.format(start_date) : null);
					data.put("end_date", end_date != null ? df1.format(end_date) : null);
					data.put("double_cover_claim", double_cover_claim.equals(0) ? false : true);
					data.put("bank", bank);
					data.put("no_rekening", customResourceLoader.formatRekening(no_rekening));
					data.put("reason_decline", reason_decline);

					// List file in folder claim corporate
					ArrayList<HashMap<String, Object>> arrayTemp = new ArrayList<>();
					List<String> pathFileClaim = customResourceLoader.listFilesUsingJavaIO2CustomSorted(path_storage);
					for (String name : pathFileClaim) {
						HashMap<String, Object> hashMapPathClaim = new HashMap<>();
						if ((!name.toLowerCase().contains("form_rawat_inap.pdf"))
								&& (!name.toLowerCase().substring(0, 1).equals("."))
								&& (!name.toLowerCase().contains("formrawatinapgenerate.pdf"))) {
							hashMapPathClaim.put("name", name.replace("MPOLIS_", "").replace("_", " "));
							hashMapPathClaim.put("path_file", path_storage + "\\" + name);

							arrayTemp.add(hashMapPathClaim);
						}
					}

					data.put("data_claim_corporate", arrayTemp);
					data.put("lms_status", lms_status);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 78, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/submitclaimsubmissioncorporate", produces = "application/json", method = RequestMethod.POST)
	public String submitClaimSubmissionCorporate(
			@RequestBody RequestSubmitClaimSubmissionCorporate requestSubmitClaimSubmissionCorporate,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSubmitClaimSubmissionCorporate);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestSubmitClaimSubmissionCorporate.getUsername();
		String key = requestSubmitClaimSubmissionCorporate.getKey();
		String mpcc_id = requestSubmitClaimSubmissionCorporate.getMpcc_id();
		String mste_insured = requestSubmitClaimSubmissionCorporate.getMste_insured();
		String reg_spaj = requestSubmitClaimSubmissionCorporate.getReg_spaj();
		String no_reg = requestSubmitClaimSubmissionCorporate.getNo_reg();
		Integer lssp_id = requestSubmitClaimSubmissionCorporate.getLssp_id();
		String jenis_claim = requestSubmitClaimSubmissionCorporate.getJenis_claim();
		String start_date = requestSubmitClaimSubmissionCorporate.getStart_date();
		String end_date = requestSubmitClaimSubmissionCorporate.getEnd_date();
		Boolean double_cover_claim = requestSubmitClaimSubmissionCorporate.getDouble_cover_claim();
		String bank = requestSubmitClaimSubmissionCorporate.getBank();
		String no_rekening = requestSubmitClaimSubmissionCorporate.getNo_rekening();
		Integer language_id = requestSubmitClaimSubmissionCorporate.getLanguage_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Check key null or not
				if (mpcc_id == null || no_reg == null || jenis_claim == null || bank == null || no_rekening == null) {
					error = true;
					message = "Failed submit claim corporate";
					resultErr = "Key pada API ada yang null";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					// Check mpcc_id exists or not
					Integer check = services.selectCheckMpccId(mpcc_id);

					if (check > 0) {
						error = true;
						message = "MPCC_ID already used";
						resultErr = "MPCC_ID sudah digunakan";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else {
						error = false;
						message = "Successfully submit claim corporate";

						String path_storage = pathDocumentClaimcorporate + reg_spaj + "\\" + mste_insured + "\\"
								+ mpcc_id;

						Integer double_cover_claim_val = 0;

						if (double_cover_claim.equals(true)) {
							double_cover_claim_val = 1;
						}

						// Insert data to eka.mst_mpol_claim_corporate_trans
						services.insertClaimSubmissionCorporate(mpcc_id, reg_spaj, mste_insured, no_reg, lssp_id,
								jenis_claim, start_date, end_date, double_cover_claim_val, path_storage, bank,
								no_rekening);

						// Select Configuration M-Polis
						HashMap<String, Object> dataConfiguration = services.configuration();

						// Send Notification
						String messageNotif = null;

						if (language_id.equals(1)) {
							messageNotif = (String) dataConfiguration.get("NOTIFICATION_CLAIM_CORPORATE_IDN");
						} else {
							messageNotif = (String) dataConfiguration.get("NOTIFICATION_CLAIM_CORPORATE_ENG");
						}

						customResourceLoader.pushNotif(username, messageNotif, null, reg_spaj, 14, 0);
					}
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed submit claim corporate";
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
		customResourceLoader.insertHistActivityWS(12, 79, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/uploaddeletefileclaimsubmissioncorp", produces = "application/json", method = RequestMethod.POST)
	public String uploadDeleteFileClaimCorporate(
			@RequestBody RequestUploadDeleteFileClaimSubCorp requestUploadDeleteFileClaimSubCorp,
			HttpServletRequest request) throws IOException {
		System.out.println("Start: " + new Date());
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestUploadDeleteFileClaimSubCorp);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestUploadDeleteFileClaimSubCorp.getUsername();
		String key = requestUploadDeleteFileClaimSubCorp.getKey();
		String reg_spaj = requestUploadDeleteFileClaimSubCorp.getReg_spaj();
		String mste_insured = requestUploadDeleteFileClaimSubCorp.getMste_insured();
		String name_file = requestUploadDeleteFileClaimSubCorp.getName_file();
		String file_base64 = requestUploadDeleteFileClaimSubCorp.getFile_base64();
		String mpcc_id = requestUploadDeleteFileClaimSubCorp.getMpcc_id();
		Integer type = requestUploadDeleteFileClaimSubCorp.getType();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				if (type.equals(1)) {
					if ((name_file != null) && (file_base64 != null) && (reg_spaj != null) && (mpcc_id != null)
							&& (name_file != "") && (file_base64 != "") && (reg_spaj != "")) {
						// Path Upload
						String path_claim = pathDocumentClaimcorporate + reg_spaj + "\\" + mste_insured + "\\"
								+ mpcc_id;

						System.out.println("Upload Start: " + new Date());
						Boolean uploadFile = true;/*customResourceLoader.uploadFileToStorage(path_claim, file_base64,
								name_file, username, request.getServletPath());*/
						System.out.println("Upload Done: " + new Date());

						if (uploadFile.equals(true)) {
							String filePath = path_claim + "\\" + name_file + ".pdf";
							Boolean validateFilePdf = customResourceLoader.validateFilePdf(filePath, username, mpcc_id);
							if (validateFilePdf.equals(true)) {
								error = false;
								message = "Successfully upload file";
								data.put("name_file", name_file + ".pdf");
							} else {
								data = null;
								error = true;
								message = "Failed upload file";
								resultErr = "File PDF Corrupt, MPCC_ID: " + mpcc_id + ", Name File: " + name_file;
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						} else {
							data = null;
							error = true;
							message = "Failed upload file";
							resultErr = "Upload file error, REG SPAJ: " + reg_spaj;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						data = null;
						error = true;
						message = "Failed upload file, key must not be null/ empty";
						resultErr = "Upload file error, REG SPAJ: " + reg_spaj;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (type.equals(2)) {
					if ((name_file != null) && (reg_spaj != null) && (mpcc_id != null) && (name_file != "")
							&& (reg_spaj != "")) {
						// Path Delete File
						String path_claim = pathDocumentClaimcorporate + reg_spaj + "\\" + mste_insured + "\\" + mpcc_id
								+ "\\" + name_file + ".pdf";

						// Path Log
						String pathLogClaimRequest = pathLogSubmitClaimSubmissionCorp + mpcc_id + "\\" + "request-"
								+ name_file + ".txt";
						String pathLogClaimResponse = pathLogSubmitClaimSubmissionCorp + mpcc_id + "\\" + "response-"
								+ name_file + ".txt";

						try {
							File file = new File(path_claim);
							if (file.delete()) {
								Path path2 = Paths.get(pathLogClaimRequest);
								Files.walk(path2).sorted(Comparator.reverseOrder()).map(Path::toFile)
										.forEach(File::delete);

								Path path3 = Paths.get(pathLogClaimResponse);
								Files.walk(path3).sorted(Comparator.reverseOrder()).map(Path::toFile)
										.forEach(File::delete);

								error = false;
								message = "Successfully delete file";
							} else {
								error = true;
								message = "Failed delete file";
								resultErr = "Gagal delete file, path_file: " + path_claim;
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						} catch (Exception e) {
							error = true;
							message = "Failed delete file";
							resultErr = "Gagal delete file, path_file: " + path_claim + ", error: " + e;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						error = true;
						message = "Failed delete file, key must not be null/ empty";
						resultErr = "Delete file error, REG SPAJ: " + reg_spaj;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (type.equals(3)) {
					if ((reg_spaj != null) && (mpcc_id != null) && (reg_spaj != "")) {
						// Path Delete Folder
						String path_claim = pathDocumentClaimcorporate + reg_spaj + "\\" + mste_insured + "\\"
								+ mpcc_id;

						// Path Log
						String pathLogClaimSubmission = pathLogSubmitClaimSubmissionCorp + mpcc_id;

						try {
							Path path1 = Paths.get(path_claim);
							Files.walk(path1).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);

							Path path2 = Paths.get(pathLogClaimSubmission);
							Files.walk(path2).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);

							error = false;
							message = "Successfully delete folder";
						} catch (Exception e) {
							error = true;
							message = "Failed delete folder";
							resultErr = "Gagal delete folder, path_folder: " + path_claim + ", error: " + e;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						// Handle key request null
						error = true;
						message = "Failed delete folder, key must not be null/ empty";
						resultErr = "Delete folder error, REG SPAJ: " + reg_spaj;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					// Handle type not found
					error = true;
					message = "Failed upload/ delete data";
					resultErr = "Type yang dimasukkan tidak tersedia, type: " + type;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed upload/ delete data";
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

		if (type.equals(1)) {
			System.out.println("Create txt Start: " + new Date());
			// Generate log .txt diserver M-Polis
			customResourceLoader.createTxt(mpcc_id, req, "request-" + name_file, 2);
			customResourceLoader.createTxt(mpcc_id, res, "response-" + name_file, 2);
			System.out.println("Create txt Done: " + new Date());

			// Insert Log LST_HIST_ACTIVITY_WS
			customResourceLoader.insertHistActivityWS(12, 80, new Date(), null, null, 1, resultErr, start, username);
		} else if ((type.equals(2)) || (type.equals(3))) {
			// Insert Log LST_HIST_ACTIVITY_WS
			customResourceLoader.insertHistActivityWS(12, 80, new Date(), req, res, 1, resultErr, start, username);
		} else {
			// Insert Log LST_HIST_ACTIVITY_WS
			customResourceLoader.insertHistActivityWS(12, 80, new Date(), null, null, 1, resultErr, start, username);
		}
		System.out.println("End: " + new Date());

		return res;
	}

	@RequestMapping(value = "/checkrekeningnasabah", produces = "application/json", method = RequestMethod.POST)
	public String checkRekeningNasabah(@RequestBody RequestCheckRekeningNasabah requestCheckRekeningNasabah,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestCheckRekeningNasabah);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestCheckRekeningNasabah.getUsername();
		String key = requestCheckRekeningNasabah.getKey();
		Integer type = requestCheckRekeningNasabah.getType();
		String no_polis = requestCheckRekeningNasabah.getNo_polis();
		String reg_spaj = requestCheckRekeningNasabah.getReg_spaj();
		String mste_insured = requestCheckRekeningNasabah.getMste_insured();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				if (type.equals(1)) { // Individu
					Rekening dataRekeningIndividu = services
							.selectCheckRekeningNasabahIndividu(customResourceLoader.clearData(no_polis));

					if (dataRekeningIndividu == null) {
						error = true;
						message = "Data rekening individu empty";
						data = null;
						resultErr = "SPAJ tidak ditemukan, REG_SPAJ: " + reg_spaj;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else {
						String rekening_original = dataRekeningIndividu.getMrc_no_ac();
						String bank = dataRekeningIndividu.getLsbp_nama();

						if (rekening_original == null || bank == null) {
							error = true;
							message = "Data rekening individu empty";
							data = null;
							resultErr = "Data rekening atau bank ada yang kosong (Individu)";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						} else {
							error = false;
							message = "Successfully get data";
							data.put("rekening_original", rekening_original);
							data.put("rekening_alias", customResourceLoader.formatRekening(rekening_original));
							data.put("bank", bank);
						}
					}
				} else if (type.equals(2)) { // Corporate
					Rekening dataRekeningCorporate = services.selectCheckRekeningNasabahCorporate(reg_spaj,
							mste_insured);

					if (dataRekeningCorporate == null) {
						error = true;
						message = "Data rekening corporate empty";
						data = null;
						resultErr = "SPAJ & MSTE_INSURED tidak ditemukan, REG_SPAJ: " + reg_spaj + ", MSTE_INSURED: "
								+ mste_insured;
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else {
						String rekening_original = dataRekeningCorporate.getMsbc_acc_no();
						String bank = dataRekeningCorporate.getMsbc_bank();

						if (rekening_original == null || bank == null) {
							error = true;
							message = "Data rekening corporate empty";
							data = null;
							resultErr = "Data rekening atau bank ada yang kosong (Corporate)";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						} else {
							error = false;
							message = "Successfully get data";
							data.put("rekening_original", rekening_original);
							data.put("rekening_alias", customResourceLoader.formatRekening(rekening_original));
							data.put("bank", bank);
						}
					}
				} else {
					// Handle type not found
					error = true;
					message = "Type not found";
					data = null;
					resultErr = "Type yang diinputkan tidak tersedia";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 81, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/checkphonenumbernasabah", produces = "application/json", method = RequestMethod.POST)
	public String checkPhoneNumberNasabah(@RequestBody RequestCheckPhoneNumberNasabah requestCheckPhoneNumberNasabah,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestCheckPhoneNumberNasabah);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestCheckPhoneNumberNasabah.getUsername();
		String key = requestCheckPhoneNumberNasabah.getKey();
		Integer type = requestCheckPhoneNumberNasabah.getType();
		String no_polis = requestCheckPhoneNumberNasabah.getNo_polis();
		String reg_spaj = requestCheckPhoneNumberNasabah.getReg_spaj();
		String mste_insured = requestCheckPhoneNumberNasabah.getMste_insured();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				if (type.equals(1)) { // Individual
					User dataUserIndividu = services
							.selectCheckPhoneNumberIndividu(customResourceLoader.clearData(no_polis));

					if (dataUserIndividu == null) {
						error = true;
						message = "Data phone number null";
						data = null;
						resultErr = "Data no polis tidak ditemukan";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else {
						String no_hp = dataUserIndividu.getNo_hp();

						if (no_hp == null) {
							error = true;
							message = "Data phone number null";
							data = null;
							resultErr = "Data no handphone kosong";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						} else {
							error = false;
							message = "Successfully get data";
							data.put("no_hp", no_hp);
						}
					}
				} else if (type.equals(2)) { // Corporate
					UserCorporate dataUserCorporate = services.selectCheckPhoneNumberCorporate(reg_spaj, mste_insured);

					if (dataUserCorporate == null) {
						error = true;
						message = "Data phone number null";
						data = null;
						resultErr = "Data reg_spaj & mste_insured tidak ditemukan";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					} else {
						String no_hp = dataUserCorporate.getNo_hp();

						if (no_hp == null) {
							error = true;
							message = "Data phone number null";
							data = null;
							resultErr = "Data no handphone kosong";
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						} else {
							error = false;
							message = "Successfully get data";
							data.put("no_hp", no_hp);
						}
					}
				} else {
					// Handle type not found
					error = true;
					message = "Type not found";
					data = null;
					resultErr = "Data type yang dimasukan tidak tersedia";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 82, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/submitfurtherclaimsubmission", produces = "application/json", method = RequestMethod.POST)
	public String submitFurtherClaimSubmission(@RequestBody RequestFurtherClaimSubmission requestFurtherClaimSubmission,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestFurtherClaimSubmission);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestFurtherClaimSubmission.getUsername();
		String key = requestFurtherClaimSubmission.getKey();
		String mpc_id = requestFurtherClaimSubmission.getMpc_id();
		String mpcc_id = requestFurtherClaimSubmission.getMpcc_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				if (mpc_id != null && mpcc_id == null) { // Individual
					services.updateFurtherClaimIndividu(mpc_id);
					error = false;
					message = "Successfully submit further individual";
				} else if (mpc_id == null && mpcc_id != null) { // Corporate
					services.updateFurtherClaimCorporate(mpcc_id);
					error = false;
					message = "Successfully submit further corporate";
				} else {
					error = true;
					message = "Key MPC_ID or MPCC_ID null";
					resultErr = "Key mpc_id & mpcc_id kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed submit further";
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
		res = gson.toJson(map);
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 83, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/submitpremiumholiday", produces = "application/json", method = RequestMethod.POST)
	public String submitPremiumHoliday(@RequestBody RequestViewPolicyAlteration requestViewPolicyAlteration,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewPolicyAlteration);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestViewPolicyAlteration.getUsername();
		String key = requestViewPolicyAlteration.getKey();
		String no_polis = requestViewPolicyAlteration.getNo_polis();
		String tanggal_awal = requestViewPolicyAlteration.getTanggal_awal();
		String tanggal_akhir = requestViewPolicyAlteration.getTanggal_akhir();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get SPAJ
				Pemegang paramSelectSPAJ = new Pemegang();
				paramSelectSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);

				if (dataSPAJ != null) {
					String reg_spaj = dataSPAJ.getReg_spaj();
					String msen_alasan = "CUTI PREMI";
					
					Integer lspd_id = 13;
					
					// Get MSEN_ENDORSE_NO
					String msen_endors_no = services.selectGetNoEndors();
					
					//INSERT ENDORSE
					services.insertEndorse(msen_endors_no, reg_spaj, msen_alasan, lspd_id);
					
					//INSERT DET ENDORSE
					services.insertDetailEndorse(msen_endors_no, 108, null, null, null, null, null, null, tanggal_awal, tanggal_akhir, null, null, null, null);
					
					//UPDATE LSPD_ID IN MSPO_POLICY
					services.updateLspdId(reg_spaj);
					
					error = false;
					message = "Successfully submit premium holiday";
				} else {
					// SPAJ tidak ditemukan
					error = true;
					message = "Failed get data";
					resultErr = "REG SPAJ tidak ditemukan, No polis: " + no_polis;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 63, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/viewpremiumholiday", produces = "application/json", method = RequestMethod.POST)
	public String viewPremiumHoliday(@RequestBody RequestViewPolicyAlteration requestViewPolicyAlteration,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewPolicyAlteration);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestViewPolicyAlteration.getUsername();
		String key = requestViewPolicyAlteration.getKey();
		String no_polis = requestViewPolicyAlteration.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get SPAJ
				Pemegang paramSelectSPAJ = new Pemegang();
				paramSelectSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);

				if (dataSPAJ != null) {
					String reg_spaj = dataSPAJ.getReg_spaj();
					Integer lspd_id = services.selectGetLspdId(reg_spaj);
					Boolean is_tgl_awal_submitted;

					String tgl_awal;
					String tgl_akhir;
					
					if(lspd_id==13) {
						is_tgl_awal_submitted = true;
						Endorse endorse = new Endorse();
						endorse.setReg_spaj(reg_spaj);
						endorse = services.selectGetPremiumHolidayDate(reg_spaj);
						
						tgl_awal = endorse.getTgl_awal();
						tgl_akhir = endorse.getTgl_akhir();
						
						data.put("tgl_awal", tgl_awal);
						data.put("tgl_akhir", tgl_akhir != null ? tgl_akhir : null);
					} else if (lspd_id==99){
						Endorse endorse = new Endorse();
						endorse.setReg_spaj(reg_spaj);
						endorse = services.selectGetPremiumHolidayDate(reg_spaj);
						
						tgl_awal = endorse.getTgl_awal();
						tgl_akhir = endorse.getTgl_akhir();
						
						data.put("tgl_awal", tgl_awal);
						data.put("tgl_akhir", tgl_akhir != null ? tgl_akhir : null);
						is_tgl_awal_submitted = true;
					}
					else {
						is_tgl_awal_submitted = false;
					}
					
					data.put("is_tgl_awal_submitted", is_tgl_awal_submitted);
					data.put("lspd_id", lspd_id);
					
					error = false;
					message = "Successfully get premium holiday";
				} else {
					// SPAJ tidak ditemukan
					error = true;
					message = "Failed get data";
					resultErr = "REG SPAJ tidak ditemukan, No polis: " + no_polis;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 63, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/listpremiumholiday", produces = "application/json", method = RequestMethod.POST)
	public String listPremiumHoliday(@RequestBody RequestViewPolicyAlteration requestViewPolicyAlteration,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewPolicyAlteration);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestViewPolicyAlteration.getUsername();
		String key = requestViewPolicyAlteration.getKey();
		String no_polis = requestViewPolicyAlteration.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get SPAJ
				Pemegang paramSelectSPAJ = new Pemegang();
				paramSelectSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);

				if (dataSPAJ != null) {
					String reg_spaj = dataSPAJ.getReg_spaj();
					Endorse premiumHoliday = services.selectListPremiumHoliday(reg_spaj);
					Boolean enable_submit_button = true;
					String status = null;
					
					if (premiumHoliday==null) {
						// Data List Kosong
						status = "Not Submitted";
						data.put("status", status);
						data.put("enable_submit_button", enable_submit_button);
						error = false;
						message = "Data list premium holiday empty";
					} else {
						error = false;
						message = "Successfully get data";
						
						
						
						String tgl_awal = premiumHoliday.getTgl_awal();
						String tgl_akhir = premiumHoliday.getTgl_akhir();
						String msen_endors_no = premiumHoliday.getMsen_endors_no();
						String input_date = premiumHoliday.getInput_date();
						Integer lspd_id = premiumHoliday.getLspd_id();
						
						if(lspd_id==13) {
							status = "In Progress";
							enable_submit_button = false;
						} else if (lspd_id==99) {
							status = "Accepted";
							enable_submit_button = false;
						}
						
						data.put("tgl_awal", tgl_awal != null ? tgl_awal : null);
						data.put("tgl_akhir", tgl_akhir != null ? tgl_akhir : null);
						data.put("msen_endors_no", msen_endors_no);
						data.put("input_date", input_date);
						data.put("enable_submit_button", enable_submit_button);
						data.put("status", status);
					}
				} else {
					// SPAJ tidak ditemukan
					error = true;
					message = "Failed get data";
					resultErr = "REG SPAJ tidak ditemukan, No polis: " + no_polis;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 63, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/dropdownpolicyalteration", produces = "application/json", method = RequestMethod.POST)
	public String dropdownPolicyAlteration(@RequestBody RequestDropdownPolicyAlteration requestDropdownPolicyAlteration,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDropdownPolicyAlteration);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestDropdownPolicyAlteration.getUsername();
		String key = requestDropdownPolicyAlteration.getKey();
		Integer type = requestDropdownPolicyAlteration.getType();
		Integer lsbp_id_req = requestDropdownPolicyAlteration.getLsbp_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {

				ArrayList<HashMap<String, Object>> listPernikahan = new ArrayList<>();
				ArrayList<HashMap<String, Object>> listAgama = new ArrayList<>();
				ArrayList<HashMap<String, Object>> listNegara = new ArrayList<>();
				ArrayList<HashMap<String, Object>> listJenisUsaha = new ArrayList<>();
				ArrayList<HashMap<String, Object>> listPekerjaan = new ArrayList<>();
				ArrayList<HashMap<String, Object>> listBank = new ArrayList<>();
				ArrayList<HashMap<String, Object>> listRelation = new ArrayList<>();

				if (type.equals(1)) {
					ArrayList<DropdownPolicyAlteration> getListPernikahan = services.selectListPernikahan();
					
					for (int x = 0; x < getListPernikahan.size(); x++) {
						HashMap<String, Object> hashMapTemp = new HashMap<>();
						
						Integer lsst_id = getListPernikahan.get(x).getLsst_id();
						String lsst_name = getListPernikahan.get(x).getLsst_name();
						
						hashMapTemp.put("lsst_id", lsst_id);
						hashMapTemp.put("lsst_name", lsst_name);

						listPernikahan.add(hashMapTemp);
					}
						error = false;
						message = "Successfully get list pernikahan";
						data.put("listPernikahan", listPernikahan);
				} else if (type.equals(2)) {
					ArrayList<DropdownPolicyAlteration> getListAgama = services.selectListAgama();
					
					for (int x = 0; x < getListAgama.size(); x++) {
						HashMap<String, Object> hashMapTemp = new HashMap<>();
						
						Integer lsag_id = getListAgama.get(x).getLsag_id();
						String lsag_name = getListAgama.get(x).getLsag_name();
						
						hashMapTemp.put("lsag_id", lsag_id);
						hashMapTemp.put("lsag_name", lsag_name);

						listAgama.add(hashMapTemp);
					}
						error = false;
						message = "Successfully get list agama";
						data.put("listAgama", listAgama);
				} else if (type.equals(3)) {
					ArrayList<DropdownPolicyAlteration> getListNegara = services.selectListNegara();
					
					for (int x = 0; x < getListNegara.size(); x++) {
						HashMap<String, Object> hashMapTemp = new HashMap<>();
						
						Integer lsne_id = getListNegara.get(x).getLsne_id();
						String lsne_name = getListNegara.get(x).getLsne_note();
						
						hashMapTemp.put("lsne_id", lsne_id);
						hashMapTemp.put("lsne_name", lsne_name);

						listNegara.add(hashMapTemp);
					}
						error = false;
						message = "Successfully get list negara";
						data.put("listNegara", listNegara);
				} else if (type.equals(4)) {
					ArrayList<DropdownPolicyAlteration> getListJenisUsaha = services.selectListJenisUsaha();
					
					for (int x = 0; x < getListJenisUsaha.size(); x++) {
						HashMap<String, Object> hashMapTemp = new HashMap<>();
						
						Integer lju_id = getListJenisUsaha.get(x).getLju_id();
						String lju_usaha = getListJenisUsaha.get(x).getLju_usaha();
						
						hashMapTemp.put("lju_id", lju_id);
						hashMapTemp.put("lju_usaha", lju_usaha);

						listJenisUsaha.add(hashMapTemp);
					}
						error = false;
						message = "Successfully get list jenis usaha";
						data.put("listJenisUsaha", listJenisUsaha);
				} else if (type.equals(5)) {
					ArrayList<DropdownPolicyAlteration> getListPekerjaan = services.selectListPekerjaan();
					
					for (int x = 0; x < getListPekerjaan.size(); x++) {
						HashMap<String, Object> hashMapTemp = new HashMap<>();
						
						Integer lsp_id = getListPekerjaan.get(x).getLsp_id();
						String lsp_name = getListPekerjaan.get(x).getLsp_name();
						
						hashMapTemp.put("lsp_id", lsp_id);
						hashMapTemp.put("lsp_name", lsp_name);

						listPekerjaan.add(hashMapTemp);
					}
						error = false;
						message = "Successfully get list agama";
						data.put("listPekerjaan", listPekerjaan);
				} else if (type.equals(6)) {
					ArrayList<DropdownPolicyAlteration> getListBank = services.selectListBank();
					
					for (int x = 0; x < getListBank.size(); x++) {
						HashMap<String, Object> hashMapTemp = new HashMap<>();
						
						Integer lsbp_id = getListBank.get(x).getLsbp_id();
				        String nama_bank = getListBank.get(x).getNama_bank();
						
						hashMapTemp.put("lsbp_id", lsbp_id);
						hashMapTemp.put("nama_bank", nama_bank);

						listBank.add(hashMapTemp);
					}
						error = false;
						message = "Successfully get list bank";
						data.put("listBank", listBank);
				} else if (type.equals(7)) {
					ArrayList<DropdownPolicyAlteration> getListRelation = services.selectListRelation();
					
					for (int x = 0; x < getListRelation.size(); x++) {
						HashMap<String, Object> hashMapTemp = new HashMap<>();
						
						Integer lsre_id = getListRelation.get(x).getLsre_id();
				        String lsre_relation = getListRelation.get(x).getLsre_relation();
				        
						hashMapTemp.put("lsre_id", lsre_id);
						hashMapTemp.put("lsre_relation", lsre_relation);

						listRelation.add(hashMapTemp);
					}
						error = false;
						message = "Successfully get list relation";
						data.put("listRelation", listRelation);
				} else if (type.equals(8)) {
					ArrayList<DropdownPolicyAlteration> getListCabangBank = services.selectCabangBank(lsbp_id_req);
					
					for (int x = 0; x < getListCabangBank.size(); x++) {
						HashMap<String, Object> hashMapTemp = new HashMap<>();
						
						Integer lbn_id = getListCabangBank.get(x).getLbn_id();
				        String cabang = getListCabangBank.get(x).getCabang();
				        
						hashMapTemp.put("lbn_id", lbn_id);
						hashMapTemp.put("cabang", cabang);

						listRelation.add(hashMapTemp);
					}
						error = false;
						message = "Successfully get list relation";
						data.put("listRelation", listRelation);
				} else {
					// Handle type not found
					error = true;
					message = "Failed get data";
					resultErr = "Type yang dimasukkan tidk ditemukan, username: " + username + ", Type: " + type;
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
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
		customResourceLoader.insertHistActivityWS(12, 64, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/listclaimhr", produces = "application/json", method = RequestMethod.POST)
			public String listClaimHr(@RequestBody RequestListClaimCorporate requestListClaimCorporate,
					HttpServletRequest request) {
				Date start = new Date();
				GsonBuilder builder = new GsonBuilder();
				builder.serializeNulls();
				Gson gson = new Gson();
				gson = builder.create();
				String req = gson.toJson(requestListClaimCorporate);
				String res = null;
				String resultErr = null;
				String message = null;
				boolean error = true;
				HashMap<String, Object> map = new HashMap<>();
				ArrayList<HashMap<String, Object>> data = new ArrayList<>();

				String username = requestListClaimCorporate.getUsername();
				String key = requestListClaimCorporate.getKey();
				String mste_insured = requestListClaimCorporate.getMste_insured();
				String reg_spaj = requestListClaimCorporate.getReg_spaj();
				Integer pageSize = requestListClaimCorporate.getPageSize();
				Integer pageNumber = requestListClaimCorporate.getPageNumber();
				try {
					if (customResourceLoader.validateCredential(username, key)) {
						ArrayList<ClaimCorporate> dataClaimCorporate = services.selectListClaimCorporate(reg_spaj, mste_insured,
								pageNumber, pageSize);
						// Check List Claim empty or not
						if (dataClaimCorporate.isEmpty()) {
							// Handle empty list claim
							error = false;
							message = "List claim corporate empty";
						} else {
							for (int i = 0; i < dataClaimCorporate.size(); i++) {
								String no_claim = dataClaimCorporate.get(i).getNo_klaim();
								Date tgl_rawat = dataClaimCorporate.get(i).getTgl_rawat();
								Date tgl_klaim = dataClaimCorporate.get(i).getTgl_klaim();
								Date tgl_status = dataClaimCorporate.get(i).getTgl_status();
								Date tgl_bayar = dataClaimCorporate.get(i).getTgl_bayar();
								Date tgl_input = dataClaimCorporate.get(i).getTgl_input();
								BigDecimal jumlah_claim = dataClaimCorporate.get(i).getKlaim_diajukan();
								BigDecimal jumlah_dibayarkan = dataClaimCorporate.get(i).getJml_dibayarkan();
								String status_claim = dataClaimCorporate.get(i).getStatus();
								String nm_plan = dataClaimCorporate.get(i).getNm_plan();
								String diagnosis = dataClaimCorporate.get(i).getDiagnosis();

								HashMap<String, Object> dataTemp = new HashMap<>();
								dataTemp.put("no_claim", no_claim);
								dataTemp.put("tgl_input", tgl_input != null ? df3.format(tgl_input) : null);
								dataTemp.put("tgl_rawat", tgl_rawat != null ? df3.format(tgl_rawat) : null);
								dataTemp.put("tgl_status", tgl_status != null ? df3.format(tgl_status) : null);
								dataTemp.put("tgl_klaim", tgl_klaim != null ? df3.format(tgl_klaim) : null);
								dataTemp.put("tgl_bayar", tgl_bayar != null ? df3.format(tgl_bayar) : null);
								dataTemp.put("jumlah_claim", nfZeroTwo.format(jumlah_claim));
								dataTemp.put("jumlah_dibayarkan",
										jumlah_dibayarkan != null ? nfZeroTwo.format(jumlah_dibayarkan) : null);
								dataTemp.put("status_claim", status_claim);
								dataTemp.put("nm_plan", nm_plan.trim());
								dataTemp.put("diagnosis", diagnosis);

								data.add(dataTemp);
							}

							error = false;
							message = "Successfully get list claim corporate";
						}
					} else {
						error = true;
						message = "Failed get list claim corporate";
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
				map.put("data", data);
				res = gson.toJson(map);
				// Insert Log LST_HIST_ACTIVITY_WS
				customResourceLoader.insertHistActivityWS(12, 70, new Date(), req, res, 1, resultErr, start, username);

				return res;
			}
	
	@RequestMapping(value = "/viewclaimhr", produces = "application/json", method = RequestMethod.POST)
	public String viewClaimHr(@RequestBody RequestDetailClaimCorporate requestDetailClaimCorporate,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDetailClaimCorporate);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestDetailClaimCorporate.getUsername();
		String key = requestDetailClaimCorporate.getKey();
		String no_claim = requestDetailClaimCorporate.getNo_claim();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<DetailClaimCorporate> dataDetailClaimCorporate = services
						.selectDetailClaimCorporate(no_claim);

				if (dataDetailClaimCorporate.isEmpty()) {
					// Handle detail claim corporate empty
					error = true;
					message = "Detail claim corporate empty";
					resultErr = "Data claim corporate kosong";
					logger.error("Path: " + request.getServletPath() + " Username: " + username + ", No. Claim: "
							+ no_claim + ", Error: " + resultErr);
				} else {
					ArrayList<HashMap<String, Object>> arrayDetails = new ArrayList<>();
					for (int i = 0; i < dataDetailClaimCorporate.size(); i++) {
						String detail_claim = dataDetailClaimCorporate.get(i).getDetail();
						BigDecimal jml_claim = dataDetailClaimCorporate.get(i).getJml_klaim();
						BigDecimal jml_dibayar = dataDetailClaimCorporate.get(i).getJml_dibayar();
						String tgl_input = dataDetailClaimCorporate.get(i).getTgl_input();
						String mbc_no = dataDetailClaimCorporate.get(i).getMbc_no();
						String mce_klaim_admedika = dataDetailClaimCorporate.get(i).getMce_klaim_admedika();
						String path = null;
						
						if(mce_klaim_admedika!=null) {
							path = storageMpolicyDB + "Ekamedicare" + tgl_input + File.separator + mbc_no +
									File.separator + "Kwitansi" + File.separator + mce_klaim_admedika + ".pdf";
						} else {
							path = null;
						}
						
						HashMap<String, Object> dataTemp = new HashMap<>();
						dataTemp.put("detail_claim", detail_claim);
						dataTemp.put("jml_claim", nfZeroTwo.format(jml_claim));
						dataTemp.put("jml_dibayar", nfZeroTwo.format(jml_dibayar));
						dataTemp.put("path", path);

						arrayDetails.add(dataTemp);
					}

					// Sum jumlah claim
					Integer i = 0;
					List<BigDecimal> results1 = new ArrayList<>();
					ListIterator<DetailClaimCorporate> iter1 = dataDetailClaimCorporate.listIterator();
					while (iter1.hasNext()) {
						BigDecimal sum = BigDecimal.ZERO;
						while (i < dataDetailClaimCorporate.size() && iter1.hasNext()) {
							try {
								DetailClaimCorporate m = iter1.next();
								BigDecimal resultNilaiPolis = m.getJml_klaim();
								sum = sum.add(resultNilaiPolis);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
						results1.add(sum);
						data.put("total_claim", nfZeroTwo.format(results1.get(0)));
					}

					// Sum jumlah dibayar
					Integer x = 0;
					List<BigDecimal> results2 = new ArrayList<>();
					ListIterator<DetailClaimCorporate> iter2 = dataDetailClaimCorporate.listIterator();
					while (iter2.hasNext()) {
						BigDecimal sum = BigDecimal.ZERO;
						while (x < dataDetailClaimCorporate.size() && iter2.hasNext()) {
							try {
								DetailClaimCorporate m = iter2.next();
								BigDecimal resultNilaiPolis = m.getJml_dibayar();
								sum = sum.add(resultNilaiPolis);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
						results2.add(sum);
						data.put("total_dibayar", nfZeroTwo.format(results2.get(0)));
					}

					data.put("details", arrayDetails);
					error = false;
					message = "Successfully get data detail claim corporate";
				}
			} else {
				error = true;
				message = "Failed get list claim corporate";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + ", No. Claim: " + no_claim
					+ ", Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 71, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/viewmclfirst", produces = "application/json", method = RequestMethod.POST)
	public String viewMclFirst(
			@RequestBody RequestViewMclFirst requestViewMclFirst,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewMclFirst);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String msag_id = requestViewMclFirst.getMsag_id();
		String mspo_policy_no = requestViewMclFirst.getMspo_policy_no();
		String mcl_first = requestViewMclFirst.getMcl_first();
		try {
			if (msag_id!=null) {
				Integer both = 0;
				if((mspo_policy_no!=null) && (mcl_first!=null)) {
					both = 1;
				} else {
					both = 0;
				}
				
				ArrayList <ViewMclFirst> viewMclFirst = services.selectViewMclFirst(msag_id, mspo_policy_no, mcl_first, both);

				if (viewMclFirst == null) {
					error = true;
					message = "Data mcl first empty";
					resultErr = "Data mccl first kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Msag_id: " + msag_id + " Error: " + resultErr);
				} else {
					for(int i=0; i<viewMclFirst.size(); i++) {
						String no_polis = viewMclFirst.get(i).getNo_polis();
						String nama = viewMclFirst.get(i).getNama();
						
						HashMap<String, Object> hashMap = new HashMap<>();
						hashMap.put("no_polis", no_polis);
						hashMap.put("nama", nama);
						
						data.add(hashMap);
					}
					error = false;
					message = "Successfully get data";
				}
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed get data";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Msag_id: " + msag_id + ")";
				logger.error("Path: " + request.getServletPath() + " Msag_id: " + msag_id + " Error: " + resultErr);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " msag_id: " + msag_id + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		//customResourceLoader.insertHistActivityWS(12, 78, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
}