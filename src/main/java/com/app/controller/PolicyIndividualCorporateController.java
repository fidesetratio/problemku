package com.app.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.services.VegaServices;
import com.app.feignclient.ServiceNotification;
import com.app.feignclient.ServiceOTP;
import com.app.model.Anggota;
import com.app.model.Article;
import com.app.model.Beneficiary;
import com.app.model.Data;
import com.app.model.DetailDestinationSwitching;
import com.app.model.DetailPolicyAlteration;
import com.app.model.DownloadReportHr;
import com.app.model.DropdownPolicyAlteration;
import com.app.model.Endorse;
import com.app.model.Inbox;
import com.app.model.Insured;
import com.app.model.LstUserSimultaneous;
import com.app.model.Nav;
import com.app.model.NotifToken;
import com.app.model.Payor;
import com.app.model.Pemegang;
import com.app.model.PolicyAlteration;
import com.app.model.PolicyHolder;
import com.app.model.Provider;
import com.app.model.PushNotif;
import com.app.model.ReportHr;
import com.app.model.SubmitPolicyAlteration;
import com.app.model.SwitchingRedirection;
import com.app.model.User;
import com.app.model.UserCorporate;
import com.app.model.UserHR;
import com.app.model.VersionCode;
import com.app.model.request.RequestBanner;
import com.app.model.request.RequestCountInboxUnread;
import com.app.model.request.RequestDeleteAllInbox;
import com.app.model.request.RequestDownloadArticle;
import com.app.model.request.RequestDownloadKwitansi;
import com.app.model.request.RequestDownloadReportHr;
import com.app.model.request.RequestFurtherClaimSubmission;
import com.app.model.request.RequestInbox;
import com.app.model.request.RequestListArticle;
import com.app.model.request.RequestListNAB;
import com.app.model.request.RequestListPolis;
import com.app.model.request.RequestNabchart;
import com.app.model.request.RequestPushNotif;
import com.app.model.request.RequestReadAllInbox;
import com.app.model.request.RequestReportHr;
import com.app.model.request.RequestSMSOTP;
import com.app.model.request.RequestSaveToken;
import com.app.model.request.RequestSendOTP;
import com.app.model.request.RequestUpdateInboxStatus;
import com.app.model.request.RequestVersionCode;
import com.app.model.request.RequestViewPolicyAlteration;
import com.app.model.request.RequestViewProvider;
import com.app.model.ResponseData;
import com.app.utils.VegaCustomResourceLoader;
import com.app.utils.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@RestController
public class PolicyIndividualCorporateController {

	private static final Logger logger = LogManager.getLogger(PolicyIndividualCorporateController.class);

	@Value("${path.news.mpolicy}")
	private String pathNewsMpolicy;

	@Value("${path.download.banner}")
	private String pathDownloadBanner;
	
	@Value("${path.download.article}")
	private String pathDownloadArticle;
	
	@Value("${path.download.reporthr}")
	private String pathDownloadReportHr;
	
	@Value("${path.storage.mpolicydb}")
	private String storageMpolicyDB;
	
	@Value("${path.storage.mpolicy}")
	private String storageMpolicy;
	
	@Value("${path.storage.reporthr}")
	private String storageReportHr;
	
	@Autowired
	private VegaServices services;
	
	@Autowired
	ServiceNotification serviceNotification;

	@Autowired
	private VegaCustomResourceLoader customResourceLoader;

	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");

	@RequestMapping(value = "/versioncodeapp", produces = "application/json", method = RequestMethod.POST)
	public String versionCodeApp(@RequestBody RequestVersionCode requestVersionCode, HttpServletRequest request) {
		// Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		// String req = gson.toJson(requestVersionCode);
		String res = null;
		Boolean error = true;
		String message = null;
		String resultErr = null;
		HashMap<String, Object> data = new HashMap<>();
		HashMap<String, Object> map = new HashMap<>();
		try {
			ArrayList<VersionCode> dataList = services.selectVersionCode(requestVersionCode.getFlag_app(),
					requestVersionCode.getFlag_platform());
			if (dataList.isEmpty()) {
				error = true;
				message = "Failed get application version";
				resultErr = "Data version tidak ada";
				logger.error("Path: " + request.getServletPath() + " Error: " + resultErr);
			} else {
				ListIterator<VersionCode> liter = dataList.listIterator();
				while (liter.hasNext()) {
					VersionCode m = liter.next();
					String app_name = m.getApp_name();
					String desc_app = m.getDesc_app();
					String version_name = m.getVersion_name();
					BigDecimal version_code = m.getVersion_code();

					data.put("app_name", app_name);
					data.put("desc_app", desc_app);
					data.put("version_name", version_name);
					data.put("version_code", version_code.intValue());
				}

				error = false;
				message = "Success get application version";
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Error: " + e);

			// Push Notification Telegram
			customResourceLoader.pushTelegramNotCheckDatabase("@mfajarsep_bot",
					"Path: " + request.getServletPath() + ", Apps: M-Polis" + ", Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		// customResourceLoader.insertHistActivityWS(12, 49, new Date(), req, res, 1,
		// resultErr, start,
		// requestVersionCode.getFlag_app().toString());

		return res;
	}

	@RequestMapping(value = "/listpolis", produces = "application/json", method = RequestMethod.POST)
	public String listPolis(@RequestBody RequestListPolis requestListPolis, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestListPolis);
		String res = null;
		String resultErr = null;
		Boolean error = true;
		String message = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestListPolis.getUsername();
		String key = requestListPolis.getKey();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<HashMap<String, Object>> corporate = new ArrayList<>();
				ArrayList<HashMap<String, Object>> individu = new ArrayList<>();
				Boolean is_individual = true;
				Boolean is_corporate = true;
				Boolean policy_corporate_notinforce = false;

				LstUserSimultaneous selectTypeUser = services.selectDataLstUserSimultaneous(username);
				String mcl_id_employee = selectTypeUser.getMCL_ID_EMPLOYEE();
				String reg_spaj_register = selectTypeUser.getREG_SPAJ();

				// Individual
				ArrayList<User> listPolisIndividu = services.selectDetailedPolis(username);

				if (reg_spaj_register != null) {
					if (!listPolisIndividu.isEmpty()) {
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
								}

								individu.add(mapper);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
						is_individual = true;
					} else {
						is_individual = false;
						individu = null;
					}
				} else {
					is_individual = false;
					individu = null;
				}

				// Corporate
				ArrayList<UserCorporate> listPolisCorporate = services.selectListPolisCorporate(mcl_id_employee);
				if (!listPolisCorporate.isEmpty()) {
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
//						LocalDate sysdate = LocalDate.parse("2020-02-01");

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
					is_corporate = true;
				} else {
					is_corporate = false;
					corporate = null;
				}

				if ((individu == null) && (corporate == null)) {
					error = true;
					message = "Can't get data list polis";
					data.put("corporate", corporate);
					data.put("individual", individu);
					data.put("is_individual", false);
					data.put("is_corporate", false);
					data.put("policy_corporate_notinforce", false);
					resultErr = "Data list polis individu & corporate kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					error = false;
					message = "Successfully get data list polis";
					data.put("corporate", corporate);
					data.put("individual", individu);
					data.put("is_individual", is_individual);
					data.put("is_corporate", is_corporate);
					data.put("policy_corporate_notinforce", policy_corporate_notinforce);
				}
			} else {
				error = true;
				message = "Can't get data list polis";
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
		customResourceLoader.insertHistActivityWS(12, 9, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/listpolishr", produces = "application/json", method = RequestMethod.POST)
	public String listPolisHR(@RequestBody RequestListPolis requestListPolis, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestListPolis);
		String res = null;
		String resultErr = null;
		Boolean error = true;
		String message = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestListPolis.getUsername();
		String key = requestListPolis.getKey();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<HashMap<String, Object>> hr_user = new ArrayList<>();
				Boolean policy_hr_user_notinforce = false;

				LstUserSimultaneous selectTypeUser = services.selectDataLstUserSimultaneous(username);
				String eb_hr_username = selectTypeUser.getEB_HR_USERNAME();

				ArrayList<UserHR> listPolisHRUser = services.selectListPolisHRUser(eb_hr_username);
				if (!listPolisHRUser.isEmpty()) {
					// Add no polis dalam satu list
					List<String> listNoPolis = new ArrayList<String>();
					for (int i = 0; i < listPolisHRUser.size(); i++) {
						String distinctNoPolis = listPolisHRUser.get(i).getNo_polis();
						listNoPolis.add(distinctNoPolis);
					}

					// Add masa pertanggungan pada satu list
					List<Date> listBegDate = new ArrayList<Date>();
					for (int i = 0; i < listPolisHRUser.size(); i++) {
						Date distinctBegDate = listPolisHRUser.get(i).getMspo_beg_date();
						if (distinctBegDate != null) {
							listBegDate.add(distinctBegDate);
						}
					}
					
					// Add no polis dalam satu list
					List<String> listMclFirst = new ArrayList<String>();
					for (int i = 0; i < listPolisHRUser.size(); i++) {
						String distinctMclFirst = listPolisHRUser.get(i).getMcl_first();
						listMclFirst.add(distinctMclFirst);
					}

					// Distinct no polis, masa pertanggungan, mspo_type_rek biar gak dobel2
					List<String> distinctNoPolis = listNoPolis.stream().distinct().collect(Collectors.toList());
					List<Date> distinctBegDate = listBegDate.stream().distinct().collect(Collectors.toList());
					List<String> distinctMclFirst = listMclFirst.stream().distinct().collect(Collectors.toList());

					// Check polis corporate yang keluar ada 1 atau 2 dilihat dari masa berlaku
					int z = 2;
					if (distinctBegDate.size() > 1) {
						LocalDate date = LocalDate.parse(df1.format(distinctBegDate.get(0)));
						LocalDate datePlus = date.plusMonths(3);

						LocalDate sysdate = LocalDate.now();
//						LocalDate sysdate = LocalDate.parse("2020-02-01");

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
						String mclfirst = distinctMclFirst.get(x);

						// Check no polis terakhir masih berlaku atau tidak
						if (x == 0) {
							UserCorporate dataEndDate = services
									.selectBegDateEndDateCorporate(customResourceLoader.clearData(no_polis));
							Date endDate = dataEndDate.getMspo_end_date();
							LocalDate now = LocalDate.now();
							LocalDate endDateParse = LocalDate.parse(df1.format(endDate));
							if (endDateParse.compareTo(now) < 0) {
								policy_hr_user_notinforce = true;
							} else {
								policy_hr_user_notinforce = false;
							}
						}

						dataTemp.put("no_polis", no_polis);
						dataTemp.put("masa_pertanggungan",
								masa_pertanggungan != null ? df2.format(masa_pertanggungan) : null);
						dataTemp.put("mcl_first", mclfirst);
						
						hr_user.add(dataTemp);
					}
				} else {
					hr_user = null;
				}

				if ((hr_user == null)) {
					error = true;
					message = "Can't get data list polis";
					data.put("hr_user", hr_user);
					data.put("policy_hr_user_notinforce", false);
					resultErr = "Data list polis HR kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					error = false;
					message = "Successfully get data list polis";
					data.put("hr_user", hr_user);
					data.put("policy_hr_user_notinforce", policy_hr_user_notinforce);
				}
			} else {
				error = true;
				message = "Can't get data list polis";
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
		customResourceLoader.insertHistActivityWS(12, 9, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/listpesertahr", produces = "application/json", method = RequestMethod.POST)
	public String listPesertaHR(@RequestBody RequestListPolis requestListPolis, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestListPolis);
		String res = null;
		String resultErr = null;
		Boolean error = true;
		String message = null;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestListPolis.getUsername();
		String key = requestListPolis.getKey();
		String policy_no = requestListPolis.getPolicy_no();
		String search_policy = requestListPolis.getSearch_policy();
		Integer search_type = requestListPolis.getSearch_type();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<HashMap<String, Object>> hr_user = new ArrayList<>();

				LstUserSimultaneous selectTypeUser = services.selectDataLstUserSimultaneous(username);
				String eb_hr_username = selectTypeUser.getEB_HR_USERNAME();

				ArrayList<UserHR> listPesertaHRUser = services.selectListPesertaHR(eb_hr_username, search_policy, search_type, policy_no);
				if (!listPesertaHRUser.isEmpty()) {
					for (int y = 0; y < listPesertaHRUser.size(); y++) {
						HashMap<String, Object> dataDetailsPolis = new HashMap<>();
						String reg_spaj = listPesertaHRUser.get(y).getReg_spaj();
						String mcl_first = listPesertaHRUser.get(y).getMcl_first();
						String mste_insured = listPesertaHRUser.get(y).getMste_insured();
						String kode_reg = listPesertaHRUser.get(y).getKode_reg().replaceAll("\\s", "").toUpperCase();

						dataDetailsPolis.put("reg_spaj", reg_spaj);
						dataDetailsPolis.put("mcl_first", mcl_first);
						dataDetailsPolis.put("mste_insured", mste_insured);
						dataDetailsPolis.put("kode_reg", kode_reg);

						hr_user.add(dataDetailsPolis);
					}
					map.put("data", hr_user);
					error = false;
					message = "Successfully get data list polis";
				} else {
					error = false;
					map.put("data", hr_user);
					message = "Can't get data list polis";
				}
			} else {
				error = true;
				message = "Can't get data list peserta";
				logger.error(
						"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
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
		customResourceLoader.insertHistActivityWS(12, 9, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/listarticle", produces = "application/json", method = RequestMethod.POST)
	public String listArticle(@RequestBody RequestListArticle requestListArticle, HttpServletRequest request) {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String res = null;
		Boolean error = true;
		String message = null;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();
		try {
			Integer pageNumber = requestListArticle.getPageNumber();
			Integer pageSize = requestListArticle.getPageSize();

			ArrayList<Article> dataListArticle = services.selectListArticle(pageNumber, pageSize);
			if (dataListArticle.isEmpty()) {
				error = false;
				message = "Data list article empty, page number: " + pageNumber + ", page size: " + pageSize;
			} else {
				for (int x = 0; x < dataListArticle.size(); x++) {
					Integer id = dataListArticle.get(x).getId();
					Date create_date = dataListArticle.get(x).getCreate_date();
					String path_file = dataListArticle.get(x).getPath_file();
					String title = dataListArticle.get(x).getTitle();
					String file_type = dataListArticle.get(x).getFile_type();

					HashMap<String, Object> dataTemp = new HashMap<>();
					dataTemp.put("id", id);
					dataTemp.put("create_date", create_date != null ? df.format(create_date) : null);
					dataTemp.put("path_file", path_file);
					dataTemp.put("title", title);
					dataTemp.put("file_type", file_type);

					data.add(dataTemp);
				}
				
				Article countArticle = services.selectCountListArticle();
				
				Integer total_data = countArticle.getCount();
				Integer page_size = 10;
				Integer total_page;
				
				if (total_data % page_size == 0) {
					total_page = total_data / page_size;
				} else {
					total_page = total_data / page_size + 1;
				}
				
				map.put("total_page", total_page);

				error = false;
				message = "Successfully get list article";
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			logger.error("Path: " + request.getServletPath() + ", Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);

		return res;
	}

	@RequestMapping(value = "/downloadarticle", produces = "application/json", method = RequestMethod.POST)
	public String downloadProofOfTransaction(@RequestBody RequestDownloadArticle requestDownloadArticle, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String res = null;
		String message = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		try {
			// path file
			String pathWS = requestDownloadArticle.getFile_path();
			String tempPathWS = pathWS.replace("\\", "/");
			tempPathWS = tempPathWS.replace("//", "/");
			String tempPath[] = tempPathWS.split("/");
			
			String file_download = tempPath[6].toString();
					
			String NewPathWS = pathDownloadArticle + File.separator + file_download;
			String file_name = requestDownloadArticle.getTitle();
			String file_type = requestDownloadArticle.getFile_type();

			// path file yang mau di download
			File file = new File(NewPathWS);

			try {
				// Content-Disposition
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=" + file_name.replace("  ", "_").replace(" ", "_") + "." + file_type);

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
				logger.error("Path: " + request.getServletPath() + " Error: " + e);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			logger.error("Path: " + request.getServletPath() + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);

		return res;
	}

	@RequestMapping(value = "/banner", produces = "application/json", method = RequestMethod.POST)
	public String getBanner(@RequestBody RequestBanner requestBanner, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestBanner);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> data = new ArrayList<>();

		String username = requestBanner.getUsername();
		String key = requestBanner.getKey();
		try {
//			if (customResourceLoader.checkValidationTime(200, 300) == true) {
//			requestBanner.setUsername("guest");
//			requestBanner.setKey("guest");
//		}
			if ((username.equalsIgnoreCase("guest") && key.equalsIgnoreCase("guest"))
					|| (username.equalsIgnoreCase("") && key.equalsIgnoreCase(""))
					|| (username.equalsIgnoreCase(null) && key.equalsIgnoreCase(null))) {
				String dir = pathNewsMpolicy;
				Set<String> listDirectory = customResourceLoader.listDirUsingJavaIO(dir);
				if (!listDirectory.isEmpty()) {
					// Sorting
					ArrayList<String> al = new ArrayList<String>();
					Set<String> s = new HashSet<>();
					for (String show : listDirectory)
						s.add(show);
					al.addAll(s);
					Collections.sort(al, Collections.reverseOrder());

					for (String showDir : al) {
						String dir2 = dir + File.separator + showDir;
						Set<String> listFiles = customResourceLoader.listFilesUsingJavaIO(dir2);
						if (!listFiles.isEmpty()) {
							HashMap<String, Object> data2 = new HashMap<>();
							for (String filename : listFiles) {
								String dir3 = dir2 + File.separator + filename;
								if (filename.endsWith(".jpg") == true) {
//									byte[] fileContent = FileUtils.readFileToByteArray(new File(dir3));
//									String strImg = Base64.getEncoder().encodeToString(fileContent);
									// Encode Path
//									String strImg = Base64.getEncoder().encodeToString(dir3.getBytes());
									data2.put("date", showDir.replace(".", ":").substring(0, 19));
									data2.put("image", pathDownloadBanner + showDir.replace(" ", "A"));
//									data2.put("image", "");
								} else if (filename.endsWith(".txt")) {
									String dataText = customResourceLoader.showResourceData(dir3);
									JSONObject jsonDataText = new JSONObject(dataText);
									data2.put("title", jsonDataText.get("title"));
									data2.put("body", jsonDataText.get("body"));
									data2.put("id", customResourceLoader.generateRandomDigits(4));
								}
							}
							error = false;
							message = "Successfully get banner list";
							data.add(data2);
						} else {
							error = false;
							message = "Failed get banner list";
							resultErr = "List file kosong";
						}
					}
				} else {
					error = true;
					message = "Failed get banner list";
					resultErr = "File dalam directory kosong";
					logger.error("Path: " + request.getServletPath() + " Error: " + resultErr);
				}
			} else {
				if (customResourceLoader.validateCredential(username, key)) {
					String dir = pathNewsMpolicy;
					Set<String> listDirectory = customResourceLoader.listDirUsingJavaIO(dir);
					if (!listDirectory.isEmpty()) {
						// Sorting
						ArrayList<String> al = new ArrayList<String>();
						Set<String> s = new HashSet<>();
						for (String show : listDirectory)
							s.add(show);
						al.addAll(s);
						Collections.sort(al, Collections.reverseOrder());

						for (String showDir : al) {
							String dir2 = dir + File.separator + showDir;
							Set<String> listFiles = customResourceLoader.listFilesUsingJavaIO(dir2);
							if (!listFiles.isEmpty()) {
								HashMap<String, Object> data2 = new HashMap<>();
								for (String filename : listFiles) {
									String dir3 = dir2 + File.separator + filename;
									if (filename.endsWith(".jpg") == true) {
//										byte[] fileContent = FileUtils.readFileToByteArray(new File(dir3));
//										String strImg = Base64.getEncoder().encodeToString(fileContent);
										// Encode Path
//											String strImg = Base64.getEncoder().encodeToString(dir3.getBytes());
										data2.put("date", showDir.replace(".", ":").substring(0, 19));
										data2.put("image", pathDownloadBanner + showDir.replace(" ", "A"));
//										data2.put("image", "");
									} else if (filename.endsWith(".txt")) {
										String dataText = customResourceLoader.showResourceData(dir3);
										JSONObject jsonDataText = new JSONObject(dataText);
										data2.put("title", jsonDataText.get("title"));
										data2.put("body", jsonDataText.get("body"));
										data2.put("id", customResourceLoader.generateRandomDigits(4));
									}
								}
								error = false;
								message = "Successfully get banner list";
								data.add(data2);
							} else {
								error = false;
								message = "Failed get banner list";
								resultErr = "List file kosong";
							}
						}
					} else {
						error = true;
						message = "Failed get banner list";
						resultErr = "File dalam directory kosong";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					error = true;
					message = "Failed get banner list";
					resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}

				// Update activity user table LST_USER_SIMULTANEOUS
				// customResourceLoader.updateActivity(username);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS

		HashMap<String, Object> mapInput = new HashMap<>();
		mapInput.put("error", error);
		mapInput.put("message", message);
		String resInput = gson.toJson(mapInput);

		customResourceLoader.insertHistActivityWS(12, 35, new Date(), req, resInput, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/downloadbanner", method = RequestMethod.GET)
	public ResponseEntity<String> downloadBanner(@RequestParam String value, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// Set path file
			String pathWS = pathNewsMpolicy + File.separator + value.replace("A", " ") + File.separator + "Image.jpg";

			// Path file yang mau di download
			File file = new File(pathWS);

			// Content-Disposition
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Image.jpg");

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
		} catch (Exception e) {
			logger.error("Path: " + request.getServletPath() + ", Error: " + e);
		}

		ResponseEntity<String> result = new ResponseEntity<String>("OK", HttpStatus.OK);
		return result;
	}

	@RequestMapping(value = "/viewprovider", produces = "application/json", method = RequestMethod.POST)
	public String viewProvider(@RequestBody RequestViewProvider requestViewProvider, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewProvider);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> data = new ArrayList<>();

		String username = requestViewProvider.getUsername();
		String key = requestViewProvider.getKey();
		try {
//			if (customResourceLoader.checkValidationTime(200, 300) == true) {
//				requestViewProvider.setUsername("guest");
//				requestViewProvider.setKey("guest");
//			}
			if ((username.equalsIgnoreCase("guest") && key.equalsIgnoreCase("guest"))
					|| (username.equalsIgnoreCase("") && key.equalsIgnoreCase(""))
					|| (username.equalsIgnoreCase(null) && key.equalsIgnoreCase(null))) {
				ArrayList<Provider> listProvider = services.selectProvider();
				if (listProvider != null) {
					ListIterator<Provider> liter = listProvider.listIterator();
					while (liter.hasNext()) {
						try {
							Provider m = liter.next();
							HashMap<String, Object> hasil = new HashMap<>();

							BigDecimal lskaId = m.getLska_id();
							BigDecimal RSTYPE = m.getRstype();
							BigDecimal rsId = m.getRsid();
							String lskaNote = m.getLska_note();
							String rsNama = m.getRsnama();
							String rsAlamat = m.getRsalamat();
							String rsTelepon = m.getRstelepon();
							String mappos = m.getMappos();
							String rsTypeLabel = m.getRs_type_label();
							String type_rs = m.getType_rs();
							if (mappos != null) {
								String latitude = mappos.substring(0, mappos.indexOf(","));
								String longitude = mappos.substring(mappos.lastIndexOf(",") + 1);
								Double latid = Double.parseDouble(latitude);
								Double longid = Double.parseDouble(longitude);

								hasil.put("LSKA_ID", lskaId);
								hasil.put("LSKA_NOTE", lskaNote);
								hasil.put("RSALAMAT", rsAlamat);
								hasil.put("RS_TYPE_LABEL", rsTypeLabel);
								hasil.put("RS_TYPE", RSTYPE);
								hasil.put("LATITUDE", latid);
								hasil.put("LONGITUDE", longid);
								hasil.put("RSTELEPON", rsTelepon);
								hasil.put("RSID", rsId);
								hasil.put("RSNAMA", rsNama);
								hasil.put("USER_TYPE", type_rs);
								data.add(hasil);
							} else {
								hasil.put("LSKA_ID", lskaId);
								hasil.put("LSKA_NOTE", lskaNote);
								hasil.put("RSALAMAT", rsAlamat);
								hasil.put("RS_TYPE", RSTYPE);
								hasil.put("RS_TYPE_LABEL", rsTypeLabel);
								hasil.put("LATITUDE", null);
								hasil.put("LONGITUDE", null);
								hasil.put("RSTELEPON", rsTelepon);
								hasil.put("RSID", rsId);
								hasil.put("RSNAMA", rsNama);
								hasil.put("USER_TYPE", type_rs);
								data.add(hasil);
							}
						} catch (Exception e) {
							logger.error("Path: " + request.getServletPath() + " Error: " + e);
						}
					}
					error = false;
					message = "Successfully get provider list";
				} else {
					error = false;
					message = "Sorry data temporary not available";
					resultErr = "Data list provider kosong";
				}
			} else {
				if (customResourceLoader.validateCredential(username, key)) {
					ArrayList<Provider> listProvider = services.selectProvider();
					if (listProvider != null) {
						ListIterator<Provider> liter = listProvider.listIterator();
						while (liter.hasNext()) {
							try {
								Provider m = liter.next();
								HashMap<String, Object> hasil = new HashMap<>();

								BigDecimal lskaId = m.getLska_id();
								BigDecimal RSTYPE = m.getRstype();
								BigDecimal rsId = m.getRsid();
								String lskaNote = m.getLska_note();
								String rsNama = m.getRsnama();
								String rsAlamat = m.getRsalamat();
								String rsTelepon = m.getRstelepon();
								String mappos = m.getMappos();
								String rsTypeLabel = m.getRs_type_label();
								String type_rs = m.getType_rs();
								if (mappos != null) {
									String latitude = mappos.substring(0, mappos.indexOf(","));
									String longitude = mappos.substring(mappos.lastIndexOf(",") + 1);
									Double latid = Double.parseDouble(latitude);
									Double longid = Double.parseDouble(longitude);

									hasil.put("LSKA_ID", lskaId);
									hasil.put("LSKA_NOTE", lskaNote);
									hasil.put("RSALAMAT", rsAlamat);
									hasil.put("RS_TYPE_LABEL", rsTypeLabel);
									hasil.put("RS_TYPE", RSTYPE);
									hasil.put("LATITUDE", latid);
									hasil.put("LONGITUDE", longid);
									hasil.put("RSTELEPON", rsTelepon);
									hasil.put("RSID", rsId);
									hasil.put("RSNAMA", rsNama);
									hasil.put("USER_TYPE", type_rs);
									data.add(hasil);
								} else {
									hasil.put("LSKA_ID", lskaId);
									hasil.put("LSKA_NOTE", lskaNote);
									hasil.put("RSALAMAT", rsAlamat);
									hasil.put("RS_TYPE", RSTYPE);
									hasil.put("RS_TYPE_LABEL", rsTypeLabel);
									hasil.put("LATITUDE", null);
									hasil.put("LONGITUDE", null);
									hasil.put("RSTELEPON", rsTelepon);
									hasil.put("RSID", rsId);
									hasil.put("RSNAMA", rsNama);
									hasil.put("USER_TYPE", type_rs);
									data.add(hasil);
								}
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
						error = false;
						message = "Successfully get provider list";
					} else {
						error = false;
						message = "Sorry data temporary not available";
						resultErr = "Data list provider kosong";
					}
				} else {
					error = true;
					message = "Sorry data temporary not available";
					resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}

				// Update activity user table LST_USER_SIMULTANEOUS
				// customResourceLoader.updateActivity(username);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 17, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/listnab", produces = "application/json", method = RequestMethod.POST)
	public String listNab(@RequestBody RequestListNAB requestListNAB, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestListNAB);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> data = new ArrayList<>();

		String username = requestListNAB.getUsername();
		String key = requestListNAB.getKey();
		try {
			if ((username.equalsIgnoreCase("guest") && key.equalsIgnoreCase("guest"))
					|| (username.equalsIgnoreCase("") && key.equalsIgnoreCase(""))
					|| (username.equalsIgnoreCase(null) && key.equalsIgnoreCase(null))) {
				ArrayList<Nav> lisNav = services.selectListNav();
				if (lisNav != null) {
					ListIterator<Nav> liter = lisNav.listIterator();
					while (liter.hasNext()) {
						try {
							Nav m = liter.next();
							String lji_Id = m.getLji_id();
							String lji_invest = m.getLji_invest();
							String jenis_invest = m.getJenis_invest();
							String lnu_tgl = m.getTgl_nab_0();
							String lnu_tgl_sebelum = m.getTgl_nab_1();
							BigDecimal lnu_nilai = m.getNab_0();
							BigDecimal nilai_sebelum = m.getNab_1();
							BigDecimal nilai_2hari_sebelum = m.getNab_2();
							BigDecimal nilai_3hari_sebelum = m.getNab_3();
							BigDecimal nilai_4hari_sebelum = m.getNab_4();
							BigDecimal selisih_nilai = m.getSelisih_nilai();
							BigDecimal persen_selisih = m.getPersen_selisih();

							HashMap<String, Object> chart = new HashMap<>();
							chart.put("lji_id", lji_Id);
							chart.put("jenis_invest", jenis_invest);
							chart.put("lnu_tgl", lnu_tgl);
							chart.put("lnu_tgl_sebelum", lnu_tgl_sebelum);
							chart.put("lnu_nilai", lnu_nilai);
							chart.put("nilai_sebelum", nilai_sebelum);
							chart.put("nilai_2hari_sebelum", nilai_2hari_sebelum.toString());
							chart.put("nilai 3hari_sebelum", nilai_3hari_sebelum.toString());
							chart.put("nilai_4hari_sebelum", nilai_4hari_sebelum.toString());
							chart.put("selisih_nilai", selisih_nilai);

							BigDecimal val1 = lnu_nilai;
							BigDecimal val2 = nilai_sebelum;
							BigDecimal val3 = nilai_2hari_sebelum;
							BigDecimal val4 = nilai_3hari_sebelum;
							BigDecimal val5 = nilai_4hari_sebelum;

							ArrayList<BigDecimal> listNilai = new ArrayList<>();
							listNilai.add(val1);
							listNilai.add(val2);
							listNilai.add(val3);
							listNilai.add(val4);
							listNilai.add(val5);

							chart.put("min_val", Collections.min(listNilai));
							chart.put("max_val", Collections.max(listNilai));

							HashMap<String, Object> current_value = new HashMap<>();
							current_value.put("selisih_nilai", selisih_nilai);
							current_value.put("persen_selisih", persen_selisih);

							HashMap<String, Object> tempData = new HashMap<String, Object>();
							tempData.put("lji_invest", lji_invest);
							tempData.put("lnu_nilai", lnu_nilai);
							tempData.put("lji_id", lji_Id);
							tempData.put("lnu_tgl", lnu_tgl);
							tempData.put("chart", chart);
							tempData.put("current_value", current_value);
							data.add(tempData);

							error = false;
							message = "Successfully get NAB list";
						} catch (Exception e) {
							logger.error("Path: " + request.getServletPath() + " Error: " + e);
						}
					}
				} else {
					error = false;
					message = "Unable to fetch NAB list";
					resultErr = "Data NAB kosong";
				}
			} else {
				if (customResourceLoader.validateCredential(username, key)) {
					ArrayList<Nav> lisNav = services.selectListNav();
					if (lisNav != null) {
						ListIterator<Nav> liter = lisNav.listIterator();
						while (liter.hasNext()) {
							try {
								Nav m = liter.next();
								String lji_Id = m.getLji_id();
								String lji_invest = m.getLji_invest();
								String jenis_invest = m.getJenis_invest();
								String lnu_tgl = m.getTgl_nab_0();
								String lnu_tgl_sebelum = m.getTgl_nab_1();
								BigDecimal lnu_nilai = m.getNab_0();
								BigDecimal nilai_sebelum = m.getNab_1();
								BigDecimal nilai_2hari_sebelum = m.getNab_2();
								BigDecimal nilai_3hari_sebelum = m.getNab_3();
								BigDecimal nilai_4hari_sebelum = m.getNab_4();
								BigDecimal selisih_nilai = m.getSelisih_nilai();
								BigDecimal persen_selisih = m.getPersen_selisih();

								HashMap<String, Object> chart = new HashMap<>();
								chart.put("lji_id", lji_Id);
								chart.put("jenis_invest", jenis_invest);
								chart.put("lnu_tgl", lnu_tgl);
								chart.put("lnu_tgl_sebelum", lnu_tgl_sebelum);
								chart.put("lnu_nilai", lnu_nilai);
								chart.put("nilai_sebelum", nilai_sebelum);
								chart.put("nilai_2hari_sebelum", nilai_2hari_sebelum.toString());
								chart.put("nilai 3hari_sebelum", nilai_3hari_sebelum.toString());
								chart.put("nilai_4hari_sebelum", nilai_4hari_sebelum.toString());
								chart.put("selisih_nilai", selisih_nilai);

								BigDecimal val1 = lnu_nilai;
								BigDecimal val2 = nilai_sebelum;
								BigDecimal val3 = nilai_2hari_sebelum;
								BigDecimal val4 = nilai_3hari_sebelum;
								BigDecimal val5 = nilai_4hari_sebelum;

								ArrayList<BigDecimal> listNilai = new ArrayList<>();
								listNilai.add(val1);
								listNilai.add(val2);
								listNilai.add(val3);
								listNilai.add(val4);
								listNilai.add(val5);

								chart.put("min_val", Collections.min(listNilai));
								chart.put("max_val", Collections.max(listNilai));

								HashMap<String, Object> current_value = new HashMap<>();
								current_value.put("selisih_nilai", selisih_nilai);
								current_value.put("persen_selisih", persen_selisih);

								HashMap<String, Object> tempData = new HashMap<String, Object>();
								tempData.put("lji_invest", lji_invest);
								tempData.put("lnu_nilai", lnu_nilai);
								tempData.put("lji_id", lji_Id);
								tempData.put("lnu_tgl", lnu_tgl);
								tempData.put("chart", chart);
								tempData.put("current_value", current_value);
								data.add(tempData);

								error = false;
								message = "Successfully get NAB list";
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
					} else {
						error = false;
						message = "Unable to fetch NAB list";
						resultErr = "Data NAB kosong";
					}
				} else {
					error = true;
					message = "Unable to fetch NAB list";
					resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}

				// Update activity user table LST_USER_SIMULTANEOUS
				// customResourceLoader.updateActivity(username);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 15, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/nabchart", produces = "application/json", method = RequestMethod.POST)
	public String nabChart(@RequestBody RequestNabchart requestNabchart, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestNabchart);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = true;
		BigDecimal min_value = null;
		BigDecimal max_value = null;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> data = new ArrayList<>();

		String username = requestNabchart.getUsername();
		String key = requestNabchart.getKey();
		try {
			if ((username.equalsIgnoreCase("guest") && key.equalsIgnoreCase("guest"))
					|| (username.equalsIgnoreCase("") && key.equalsIgnoreCase(""))
					|| (username.equalsIgnoreCase(null) && key.equalsIgnoreCase(null))) {
				Integer lji_id = requestNabchart.getId();
				Integer nilai = requestNabchart.getNilai();

				if (nilai.equals(5)) {
					nilai = customResourceLoader.nilaiValNabchart(nilai);
				} else if (nilai.equals(9)) {
					nilai = customResourceLoader.nilaiValNabchart(nilai);
				}

				ArrayList<Nav> lisNabChart = services.selectDetailNav(lji_id, nilai);
				ArrayList<BigDecimal> valueNilai = new ArrayList<>();
				if (!lisNabChart.isEmpty()) {
					ListIterator<Nav> liter = lisNabChart.listIterator();
					while (liter.hasNext()) {
						try {
							Nav m = liter.next();
							HashMap<String, Object> mapper = new HashMap<>();
							String lji_invest = m.getLji_invest();
							String lji_Id = m.getLji_id();
							String tgl = m.getTgl();
							BigDecimal lnu_nilai = m.getLnu_nilai();
							BigDecimal selisih = m.getSelisih();
							BigDecimal hmin1 = m.getPersen_hmin1();
							BigDecimal persenhke1 = m.getPersen_hke1();
							BigDecimal nilaihke1 = m.getNilai_hke1();

							mapper.put("tanggal", tgl);
							mapper.put("lji_invest", lji_invest);
							mapper.put("lji_id", lji_Id);
							mapper.put("lnu_nilai", lnu_nilai);
							mapper.put("selisih", selisih);
							mapper.put("persen_hmin1", hmin1);
							mapper.put("persen_hke1", persenhke1);
							mapper.put("nilai_hke1", nilaihke1);
							valueNilai.add(lnu_nilai);

							data.add(mapper);
							error = false;
							message = "Successfully get NAB Chart";
						} catch (Exception e) {
							logger.error("Path: " + request.getServletPath() + " Error: " + e);
						}
					}

					max_value = Collections.max(valueNilai);
					min_value = Collections.min(valueNilai);
				} else {
					error = false;
					message = "Unable to get NAB Chart";
					resultErr = "Data list nabchart kosong";
				}
			} else {
				if (customResourceLoader.validateCredential(username, key)) {
					Integer lji_id = requestNabchart.getId();
					Integer nilai = requestNabchart.getNilai();

					if (nilai.equals(5)) {
						nilai = customResourceLoader.nilaiValNabchart(nilai);
					} else if (nilai.equals(9)) {
						nilai = customResourceLoader.nilaiValNabchart(nilai);
					}

					ArrayList<Nav> lisNabChart = services.selectDetailNav(lji_id, nilai);
					ArrayList<BigDecimal> valueNilai = new ArrayList<>();
					if (!lisNabChart.isEmpty()) {
						ListIterator<Nav> liter = lisNabChart.listIterator();
						while (liter.hasNext()) {
							try {
								Nav m = liter.next();
								HashMap<String, Object> mapper = new HashMap<>();
								String lji_invest = m.getLji_invest();
								String lji_Id = m.getLji_id();
								String tgl = m.getTgl();
								BigDecimal lnu_nilai = m.getLnu_nilai();
								BigDecimal selisih = m.getSelisih();
								BigDecimal hmin1 = m.getPersen_hmin1();
								BigDecimal persenhke1 = m.getPersen_hke1();
								BigDecimal nilaihke1 = m.getNilai_hke1();

								mapper.put("tanggal", tgl);
								mapper.put("lji_invest", lji_invest);
								mapper.put("lji_id", lji_Id);
								mapper.put("lnu_nilai", lnu_nilai);
								mapper.put("selisih", selisih);
								mapper.put("persen_hmin1", hmin1);
								mapper.put("persen_hke1", persenhke1);
								mapper.put("nilai_hke1", nilaihke1);
								valueNilai.add(lnu_nilai);

								data.add(mapper);
								error = false;
								message = "Successfully get NAB Chart";
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}

						max_value = Collections.max(valueNilai);
						min_value = Collections.min(valueNilai);
					} else {
						error = false;
						message = "Unable to get NAB Chart";
						resultErr = "Data list nabchart kosong";
					}
				} else {
					error = true;
					message = "Unable to get NAB Chart";
					resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}

				// Update activity user table LST_USER_SIMULTANEOUS
				customResourceLoader.updateActivity(username);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			logger.error("Path: " + request.getServletPath() + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		map.put("min_value", min_value);
		map.put("max_value", max_value);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 16, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/countinboxunread", produces = "application/json", method = RequestMethod.POST)
	public String countMessageInbox(@RequestBody RequestCountInboxUnread requestCountInboxUnread,
			HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestCountInboxUnread);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestCountInboxUnread.getUsername();
		String key = requestCountInboxUnread.getKey();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Integer count = services.selectCountMessageInboxUnread(username);

				error = false;
				message = "Success get data";
				data.put("count", count);
			} else {
//				Handle username & key tidak cocok
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
		customResourceLoader.insertHistActivityWS(12, 44, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/deleteallinbox", produces = "application/json", method = RequestMethod.POST)
	public String deleteAllInbox(@RequestBody RequestDeleteAllInbox requestDeleteAllInbox, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDeleteAllInbox);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestDeleteAllInbox.getUsername();
		String key = requestDeleteAllInbox.getKey();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				User dataUser = new User();
				dataUser.setUsername(username);
				services.deleteAllInbox(dataUser);

				error = false;
				message = "Successfully delete all inbox";
			} else {
				error = true;
				message = "Failed delete all inbox";
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
		customResourceLoader.insertHistActivityWS(12, 29, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/updateinboxstatus", produces = "application/json", method = RequestMethod.POST)
	public String updateInboxStatus(@RequestBody RequestUpdateInboxStatus requestUpdateInboxStatus,
			HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestUpdateInboxStatus);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestUpdateInboxStatus.getUsername();
		String key = requestUpdateInboxStatus.getKey();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				User dataUser = new User();
				dataUser.setUsername(username);
				dataUser.setInbox_id(requestUpdateInboxStatus.getInbox_id());
				dataUser.setNew_status(requestUpdateInboxStatus.getNew_status());
				services.updateInboxStatus(dataUser);

				error = false;
				message = "Successfully update inbox";
			} else {
				error = true;
				message = "Failed update inbox";
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
		customResourceLoader.insertHistActivityWS(12, 30, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/readallinbox", produces = "application/json", method = RequestMethod.POST)
	public String readAllInbox(@RequestBody RequestReadAllInbox requestReadAllInbox, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestReadAllInbox);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestReadAllInbox.getUsername();
		String key = requestReadAllInbox.getKey();
		String new_status = requestReadAllInbox.getNew_status();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				User dataUser = new User();
				dataUser.setUsername(username);
				dataUser.setNew_status(new_status);
				dataUser.setInbox_id(null);
				services.updateInboxStatus(dataUser);

				error = false;
				message = "Successfully update all inbox";
			} else {
				error = true;
				message = "Failed update all inbox";
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
		customResourceLoader.insertHistActivityWS(12, 30, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	
	@RequestMapping(value = "/inbox", produces = "application/json", method = RequestMethod.POST)
	public String sendOTP(@RequestBody RequestInbox requestInbox, HttpServletRequest request) throws Exception {
		Gson gson = new Gson();
		String req = gson.toJson(requestInbox);
		String res = null;
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		gson = builder.create();
		String userid = null;
		String message = null;
		String resultErr = null;
		boolean error = false;
		Map<String, Object> hasil = new HashMap<>();
		ArrayList<Object> list = new ArrayList<>();
		String username = requestInbox.getUserid();
		Integer jenis_id = requestInbox.getJenis_id();
		userid = username;
		try {
			ArrayList<Inbox> listInbox = services.selectInbox(username);
			if (!listInbox.isEmpty()) {
				for (int i = 0; i < listInbox.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					
					Integer inbox_id = listInbox.get(i).getId();
					String title = listInbox.get(i).getTitle();
					String text = listInbox.get(i).getMessage();
					String parameter = listInbox.get(i).getParameter();
					String create_date = listInbox.get(i).getCreate_date();
					String flag_status = listInbox.get(i).getStatus();
					
					map.put("inbox_id", inbox_id);
					map.put("title", title);
					map.put("message", text);
					map.put("parameter", parameter != null ? new JSONObject(parameter) : null);
					map.put("created_date", create_date);
					map.put("flag_status", flag_status);
					list.add(map);
				}
				error = false;
				message = "Successfully get inbox, message found";
			} else {
				error = false;
				message = "Successfully get inbox, not message found";
			}
		} catch (Exception e) {
			error = true;
			resultErr = message + " (user id: " + userid + " Jenis ID: " + jenis_id + ")";
			message = "Unable to get inbox. " + (e);
		}
		hasil.put("error", error);
		hasil.put("message", message);
		hasil.put("data", list);
		res = gson.toJson(hasil);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 47, new Date(), req, res, 1, resultErr, start, userid);
		
		return res;
	}
	
	@RequestMapping(value = "/savetoken", produces = "application/json", method = RequestMethod.POST)
	public String saveToken(@RequestBody RequestSaveToken requestSaveToken, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSaveToken);
		String res = null;
		Map<String, Object> result = new HashMap<>();
		String resultErr = null;
		String message = null;
		boolean error = false;
		
 		String userid = requestSaveToken.getUserid();
		Integer jenis_id = requestSaveToken.getJenis_id();
		String token = requestSaveToken.getToken();
		
		NotifToken notifToken = new NotifToken();
		notifToken = services.selectNotifToken(userid);
		try {
			if ((notifToken != null) && (notifToken.getJenis_id() == 93)) {
				if (!notifToken.getToken().equals(token)){
					notifToken.setToken(token);
				}
				
				Date update_date = new Date();
				notifToken.setUpdate_date(update_date);
				services.updateNotifToken(notifToken);
				error = false;
				message = "Succesfully update data";
			} else {
				NotifToken notifToken_new = new NotifToken();
				notifToken_new.setUserid(userid);
				notifToken_new.setToken(token);
				notifToken_new.setJenis_id(jenis_id);
				notifToken_new.setFlag_active(1);
				notifToken_new.setCreate_date(start);
				services.insertNotifToken(notifToken_new);
				error = false;
				message = "Succesfully insert data";
			}
		} catch (Exception e) {
			error = true;
			message = "error bad exception : " + e;
		}
		result.put("error", error);
		result.put("message", message);
		res = gson.toJson(result);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 47, new Date(), req, res, 1, resultErr, start, userid);

		return res;
	}
	
	@RequestMapping(value = "/pushnotif", produces = "application/json", method = RequestMethod.POST)
	public String pushNotif(@RequestBody RequestPushNotif requestPushNotif, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestPushNotif);
		String res = null;
		Map<String, Object> result = new HashMap<>();
		String resultErr = null;
		String messageRes = null;
		boolean error = false;
		
		Integer type = requestPushNotif.getType();
		String jenis_id = requestPushNotif.getJenis_id();
		String userid = requestPushNotif.getUserid();
		String title = requestPushNotif.getTitle();
		String message = requestPushNotif.getMessage();
		Integer priority = requestPushNotif.getPriority();
		String reg_spaj = requestPushNotif.getReg_spaj();
		String flag_inbox = requestPushNotif.getFlag_inbox();
		Data data = requestPushNotif.getData();
		String dataTemp = new Gson().toJson(data);
		
		NotifToken notifToken = new NotifToken();
		notifToken = services.selectNotifToken(userid);
		try {
			if (notifToken != null) {
				if (type == 1) {
					InetAddress inetAddress = InetAddress.getLocalHost();
					PushNotif pushNotif = new PushNotif();
					pushNotif.setJenis_id(jenis_id);
					pushNotif.setUsername(notifToken.getUserid());
					pushNotif.setToken(notifToken.getToken());
					pushNotif.setTitle(title);
					pushNotif.setMessage(message);
					pushNotif.setParameter(dataTemp);
					pushNotif.setPriority(priority);
					pushNotif.setReg_spaj(reg_spaj);
					pushNotif.setStatus("U");
					pushNotif.setFlag_inbox(flag_inbox);
					pushNotif.setCreate_date(start);
					pushNotif.setHost_name(inetAddress.getHostName());
					pushNotif.setHost_date(start);
					services.insertNotification(pushNotif);
					error = false;
					messageRes = "Succesfully sending notification";
				} else {
					error = true;
					messageRes = "This type still undefined";
				}
			} else {
				error = true;
				messageRes = "Account not found in database";
			}
		} catch (Exception e) {
			error = true;
			messageRes = "error bad exception : " + e;
		}
		result.put("error", error);
		result.put("message", messageRes);
		res = gson.toJson(result);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 47, new Date(), req, res, 1, resultErr, start, userid);

		return res;
	}
	
	@RequestMapping(value = "/viewpolicyalteration", produces = "application/json", method = RequestMethod.POST)
	public String viewPolicyAleration(@RequestBody RequestViewPolicyAlteration requestViewPolicyAlteration,
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
		
		HashMap<String, Object> data_policyholder = new HashMap<>();
		HashMap<String, Object> data_insured = new HashMap<>();
		HashMap<String, Object> data_payor = new HashMap<>();
		
		String username = requestViewPolicyAlteration.getUsername();
		String key = requestViewPolicyAlteration.getKey();
		String no_polis = requestViewPolicyAlteration.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				
			//GET POLICY HOLDER
				PolicyAlteration policyHolder = services.selectPolicyHolder(no_polis);
				String nama_pp = policyHolder.getNama_pp();
				String jenis_produk = policyHolder.getJenis_produk();
				Integer status = policyHolder.getStatus();
				Integer agama = policyHolder.getAgama();
				Integer kewarganegaraan_pp = policyHolder.getKewarganegaraan_pp();
				String npwp = policyHolder.getNpwp();
				String nama_perusahaan_pp = policyHolder.getNama_perusahaan_pp();
				String jabatan_pp = policyHolder.getJabatan_pp();
				String uraian_pekerjaan = policyHolder.getUraian_pekerjaan();
				String alamat_kantor = policyHolder.getAlamat_kantor();
				Integer propinsi_kantor = policyHolder.getPropinsi_kantor();
				Integer kabupaten_kantor = policyHolder.getKabupaten_kantor();
				Integer kecamatan_kantor = policyHolder.getKecamatan_kantor();
				Integer kelurahan_kantor = policyHolder.getKelurahan_kantor();
				Integer kodepos_kantor = policyHolder.getKodepos_kantor();
				String area_code_rumah_pp = policyHolder.getArea_code_rumah_pp();
				String telpon_rumah_pp = policyHolder.getTelpon_rumah_pp();
				String alamat_rumah_pp = policyHolder.getAlamat_rumah_pp();
				Integer propinsi_rumah = policyHolder.getPropinsi_rumah();
				Integer kabupaten_rumah = policyHolder.getKabupaten_rumah();
				Integer kecamatan_rumah = policyHolder.getKecamatan_rumah();
				Integer kelurahan_rumah = policyHolder.getKelurahan_rumah();
				Integer kodepos_rumah = policyHolder.getKodepos_rumah();
				String alamat_tpt_tinggal = policyHolder.getAlamat_tpt_tinggal();
				Integer korespondensi_flag = policyHolder.getKorespondensi_flag();
				Integer nama_bank_pp = policyHolder.getNama_bank_pp();
				Integer cabang_bank_pp = policyHolder.getCabang_bank_pp();
				String kota_bank_pp = policyHolder.getKota_bank_pp();
				String no_rekening_pp = policyHolder.getNo_rekening_pp();
				String pemilik_rekening_pp = policyHolder.getPemilik_rekening_pp();
				String email = policyHolder.getEmail();
				String tipe_usaha_pp = policyHolder.getTipe_usaha_pp();
				
				HashMap<String, Object> _nama_pp = new HashMap<>();
				HashMap<String, Object> temp_nama_pp = new HashMap<>();
				_nama_pp.put("value", nama_pp);
				_nama_pp.put("id_endors", null);
				_nama_pp.put ("flag_direct", 3);
				temp_nama_pp.put("nama_pp", _nama_pp);
				data_policyholder.putAll(temp_nama_pp);
				
				HashMap<String, Object> _jenis_produk = new HashMap<>();
				HashMap<String, Object> temp_jenis_produk = new HashMap<>();
				_jenis_produk.put("value", jenis_produk);
				_jenis_produk.put("id_endors", null);
				_jenis_produk.put ("flag_direct", 3);
				temp_jenis_produk.put("jenis_produk", _jenis_produk);
				data_policyholder.putAll(temp_jenis_produk);
				
				HashMap<String, Object> _alamat_rumah_pp = new HashMap<>();
				HashMap<String, Object> temp_alamat_rumah_pp = new HashMap<>();
				_alamat_rumah_pp.put("value", alamat_rumah_pp);
				_alamat_rumah_pp.put("id_endors", 3);
				_alamat_rumah_pp.put ("flag_direct", 0);
				temp_alamat_rumah_pp.put("alamat_rumah_pp", _alamat_rumah_pp);
				data_policyholder.putAll(temp_alamat_rumah_pp);
				
				HashMap<String, Object> _propinsi_rumah = new HashMap<>();
				HashMap<String, Object> temp_propinsi_rumah = new HashMap<>();
				_propinsi_rumah.put("value", propinsi_rumah);
				_propinsi_rumah.put("id_endors", 3);
				_propinsi_rumah.put ("flag_direct", 0);
				temp_propinsi_rumah.put("propinsi_rumah", _propinsi_rumah);
				data_policyholder.putAll(temp_propinsi_rumah);

				HashMap<String, Object> _kabupaten_rumah = new HashMap<>();
				HashMap<String, Object> temp_kabupaten_rumah = new HashMap<>();
				_kabupaten_rumah.put("value", kabupaten_rumah);
				_kabupaten_rumah.put("id_endors", 3);
				_kabupaten_rumah.put ("flag_direct", 0);
				temp_kabupaten_rumah.put("kabupaten_rumah", _kabupaten_rumah);
				data_policyholder.putAll(temp_kabupaten_rumah);

				HashMap<String, Object> _kecamatan_rumah = new HashMap<>();
				HashMap<String, Object> temp_kecamatan_rumah = new HashMap<>();
				_kecamatan_rumah.put("value", kecamatan_rumah);
				_kecamatan_rumah.put("id_endors", 3);
				_kecamatan_rumah.put ("flag_direct", 0);
				temp_kecamatan_rumah.put("kecamatan_rumah", _kecamatan_rumah);
				data_policyholder.putAll(temp_kecamatan_rumah);

				HashMap<String, Object> _kelurahan_rumah = new HashMap<>();
				HashMap<String, Object> temp_kelurahan_rumah = new HashMap<>();
				_kelurahan_rumah.put("value", kelurahan_rumah);
				_kelurahan_rumah.put("id_endors", 3);
				_kelurahan_rumah.put ("flag_direct", 0);
				temp_kelurahan_rumah.put("kelurahan_rumah", _kelurahan_rumah);
				data_policyholder.putAll(temp_kelurahan_rumah);
				
				HashMap<String, Object> _kodepos_rumah = new HashMap<>();
				HashMap<String, Object> temp_kodepos_rumah = new HashMap<>();
				_kodepos_rumah.put("value", kodepos_rumah);
				_kodepos_rumah.put("id_endors", 3);
				_kodepos_rumah.put ("flag_direct", 0);
				temp_kodepos_rumah.put("kodepos_rumah", _kodepos_rumah);
				data_policyholder.putAll(temp_kodepos_rumah);
				
				HashMap<String, Object> _alamat_tpt_tinggal = new HashMap<>();
				HashMap<String, Object> temp_alamat_tpt_tinggal = new HashMap<>();
				_alamat_tpt_tinggal.put("value", alamat_tpt_tinggal);
				_alamat_tpt_tinggal.put("id_endors", 3);
				_alamat_tpt_tinggal.put ("flag_direct", 0);
				temp_alamat_tpt_tinggal.put("alamat_tpt_tinggal", _alamat_tpt_tinggal);
				data_policyholder.putAll(temp_alamat_tpt_tinggal);
				
				HashMap<String, Object> _korespondensi_flag = new HashMap<>();
				HashMap<String, Object> temp_korespondensi_flag = new HashMap<>();
				_korespondensi_flag.put("value", alamat_tpt_tinggal);
				_korespondensi_flag.put("id_endors", null);
				_korespondensi_flag.put ("flag_direct", 0);
				temp_korespondensi_flag.put("korespondensi_flag", _korespondensi_flag);
				data_policyholder.putAll(temp_alamat_tpt_tinggal);
				
				if(korespondensi_flag==1) {
					PolicyAlteration korespondensi = services.selectKorespondensi(no_polis);
					
					Integer propinsi_tinggal = korespondensi.getPropinsi_tinggal();
					Integer kabupaten_tinggal = korespondensi.getKabupaten_tinggal();
					Integer kecamatan_tinggal = korespondensi.getKecamatan_tinggal();
					Integer kelurahan_tinggal = korespondensi.getKelurahan_tinggal();
					Integer kodepos_tinggal = korespondensi.getKodepos_tinggal();
					Integer negara_tinggal = korespondensi.getNegara_tinggal();
					
					HashMap<String, Object> _propinsi_tinggal = new HashMap<>();
					HashMap<String, Object> temp_propinsi_tinggal = new HashMap<>();
					_propinsi_tinggal.put("value", propinsi_tinggal);
					_propinsi_tinggal.put("id_endors", 3);
					_propinsi_tinggal.put ("flag_direct", 0);
					temp_propinsi_tinggal.put("propinsi_tinggal", _propinsi_tinggal);
					data_policyholder.putAll(temp_propinsi_tinggal);
					
					HashMap<String, Object> _kabupaten_tinggal = new HashMap<>();
					HashMap<String, Object> temp_kabupaten_tinggal = new HashMap<>();
					_kabupaten_tinggal.put("value", kabupaten_tinggal);
					_kabupaten_tinggal.put("id_endors", 3);
					_kabupaten_tinggal.put ("flag_direct", 0);
					temp_kabupaten_tinggal.put("kabupaten_tinggal", _kabupaten_tinggal);
					data_policyholder.putAll(temp_kabupaten_tinggal);
					
					HashMap<String, Object> _kecamatan_tinggal = new HashMap<>();
					HashMap<String, Object> temp_kecamatan_tinggal = new HashMap<>();
					_kecamatan_tinggal.put("value", kecamatan_tinggal);
					_kecamatan_tinggal.put("id_endors", 3);
					_kecamatan_tinggal.put ("flag_direct", 0);
					temp_kecamatan_tinggal.put("kecamatan_tinggal", _kecamatan_tinggal);
					data_policyholder.putAll(temp_kecamatan_tinggal);
					
					HashMap<String, Object> _kelurahan_tinggal = new HashMap<>();
					HashMap<String, Object> temp_kelurahan_tinggal = new HashMap<>();
					_kelurahan_tinggal.put("value", kelurahan_tinggal);
					_kelurahan_tinggal.put("id_endors", 3);
					_kelurahan_tinggal.put ("flag_direct", 0);
					temp_kelurahan_tinggal.put("kelurahan_tinggal", _kelurahan_tinggal);
					data_policyholder.putAll(temp_kelurahan_tinggal);
					
					HashMap<String, Object> _kodepos_tinggal = new HashMap<>();
					HashMap<String, Object> temp_kodepos_tinggal = new HashMap<>();
					_kodepos_tinggal.put("value", kodepos_tinggal);
					_kodepos_tinggal.put("id_endors", 3);
					_kodepos_tinggal.put ("flag_direct", 0);
					temp_kodepos_tinggal.put("kodepos_tinggal", _kodepos_tinggal);
					data_policyholder.putAll(temp_kodepos_tinggal);
					
					HashMap<String, Object> _negara_tinggal = new HashMap<>();
					HashMap<String, Object> temp_negara_tinggal = new HashMap<>();
					_negara_tinggal.put("value", negara_tinggal);
					_negara_tinggal.put("id_endors", 3);
					_negara_tinggal.put ("flag_direct", 0);
					temp_negara_tinggal.put("negara_tinggal", _negara_tinggal);
					data_policyholder.putAll(temp_negara_tinggal);
					
				} else {
					HashMap<String, Object> _propinsi_tinggal = new HashMap<>();
					HashMap<String, Object> temp_propinsi_tinggal = new HashMap<>();
					_propinsi_tinggal.put("value", null);
					_propinsi_tinggal.put("id_endors", 3);
					_propinsi_tinggal.put ("flag_direct", 0);
					temp_propinsi_tinggal.put("propinsi_tinggal", _propinsi_tinggal);
					data_policyholder.putAll(temp_propinsi_tinggal);
					
					HashMap<String, Object> _kabupaten_tinggal = new HashMap<>();
					HashMap<String, Object> temp_kabupaten_tinggal = new HashMap<>();
					_kabupaten_tinggal.put("value", null);
					_kabupaten_tinggal.put("id_endors", 3);
					_kabupaten_tinggal.put ("flag_direct", 0);
					temp_kabupaten_tinggal.put("kabupaten_tinggal", _kabupaten_tinggal);
					data_policyholder.putAll(temp_kabupaten_tinggal);
					
					HashMap<String, Object> _kecamatan_tinggal = new HashMap<>();
					HashMap<String, Object> temp_kecamatan_tinggal = new HashMap<>();
					_kecamatan_tinggal.put("value", null);
					_kecamatan_tinggal.put("id_endors", 3);
					_kecamatan_tinggal.put ("flag_direct", 0);
					temp_kecamatan_tinggal.put("kecamatan_tinggal", _kecamatan_tinggal);
					data_policyholder.putAll(temp_kecamatan_tinggal);
					
					HashMap<String, Object> _kelurahan_tinggal = new HashMap<>();
					HashMap<String, Object> temp_kelurahan_tinggal = new HashMap<>();
					_kelurahan_tinggal.put("value", null);
					_kelurahan_tinggal.put("id_endors", 3);
					_kelurahan_tinggal.put ("flag_direct", 0);
					temp_kelurahan_tinggal.put("kelurahan_tinggal", _kelurahan_tinggal);
					data_policyholder.putAll(temp_kelurahan_tinggal);
					
					HashMap<String, Object> _kodepos_tinggal = new HashMap<>();
					HashMap<String, Object> temp_kodepos_tinggal = new HashMap<>();
					_kodepos_tinggal.put("value", null);
					_kodepos_tinggal.put("id_endors", 3);
					_kodepos_tinggal.put ("flag_direct", 0);
					temp_kodepos_tinggal.put("kodepos_tinggal", _kodepos_tinggal);
					data_policyholder.putAll(temp_kodepos_tinggal);
					
					HashMap<String, Object> _negara_tinggal = new HashMap<>();
					HashMap<String, Object> temp_negara_tinggal = new HashMap<>();
					_negara_tinggal.put("value", null);
					_negara_tinggal.put("id_endors", 3);
					_negara_tinggal.put ("flag_direct", 0);
					temp_negara_tinggal.put("negara_tinggal", _negara_tinggal);
					data_policyholder.putAll(temp_negara_tinggal);
				}

				HashMap<String, Object> _status = new HashMap<>();
				HashMap<String, Object> temp_status = new HashMap<>();
				_status.put("value", status);
				_status.put("id_endors", 67);
				_status.put ("flag_direct", 1);
				temp_status.put("status", _status);
				data_policyholder.putAll(temp_status);
				
				HashMap<String, Object> _agama = new HashMap<>();
				HashMap<String, Object> temp_agama = new HashMap<>();
				_agama.put("value", agama);
				_agama.put("id_endors", 61);
				_agama.put ("flag_direct", 1);
				temp_agama.put("agama", _agama);
				data_policyholder.putAll(temp_agama);
				
				HashMap<String, Object> _kewarganegaraan_pp = new HashMap<>();
				HashMap<String, Object> temp_kewarganegaraan_pp = new HashMap<>();
				_kewarganegaraan_pp.put("value", kewarganegaraan_pp);
				_kewarganegaraan_pp.put("id_endors", 39);
				_kewarganegaraan_pp.put ("flag_direct", 1);
				temp_kewarganegaraan_pp.put("kewarganegaraan_pp", _kewarganegaraan_pp);
				data_policyholder.putAll(temp_kewarganegaraan_pp);
				
				HashMap<String, Object> _nama_perusahaan_pp = new HashMap<>();
				HashMap<String, Object> temp_nama_perusahaan_pp = new HashMap<>();
				_nama_perusahaan_pp.put("value", nama_perusahaan_pp);
				_nama_perusahaan_pp.put("id_endors", 89);
				_nama_perusahaan_pp.put ("flag_direct", 1);
				temp_nama_perusahaan_pp.put("nama_perusahaan_pp", _nama_perusahaan_pp);
				data_policyholder.putAll(temp_nama_perusahaan_pp);
				
				HashMap<String, Object> _tipe_usaha_pp = new HashMap<>();
				HashMap<String, Object> temp_tipe_usaha_pp = new HashMap<>();
				_tipe_usaha_pp.put("value", tipe_usaha_pp);
				_tipe_usaha_pp.put("id_endors", 89);
				_tipe_usaha_pp.put ("flag_direct", 1);
				temp_tipe_usaha_pp.put("tipe_usaha_pp", _tipe_usaha_pp);
				data_policyholder.putAll(temp_tipe_usaha_pp);
				
				HashMap<String, Object> _jabatan_pp = new HashMap<>();
				HashMap<String, Object> temp_jabatan_pp = new HashMap<>();
				_jabatan_pp.put("value", jabatan_pp);
				_jabatan_pp.put("id_endors", 89);
				_jabatan_pp.put ("flag_direct", 0);
				temp_jabatan_pp.put("jabatan_pp", _jabatan_pp);
				data_policyholder.putAll(temp_jabatan_pp);
				
				HashMap<String, Object> _uraian_pekerjaan = new HashMap<>();
				HashMap<String, Object> temp_uraian_pekerjaan = new HashMap<>();
				_uraian_pekerjaan.put("value", uraian_pekerjaan);
				_uraian_pekerjaan.put("id_endors", 89);
				_uraian_pekerjaan.put ("flag_direct", 0);
				temp_uraian_pekerjaan.put("uraian_pekerjaan", _uraian_pekerjaan);
				data_policyholder.putAll(temp_uraian_pekerjaan);
				
				HashMap<String, Object> _alamat_kantor = new HashMap<>();
				HashMap<String, Object> temp_alamat_kantor = new HashMap<>();
				_alamat_kantor.put("value", alamat_kantor);
				_alamat_kantor.put("id_endors", 34);
				_alamat_kantor.put ("flag_direct", 1);
				temp_alamat_kantor.put("alamat_kantor", _alamat_kantor);
				data_policyholder.putAll(temp_alamat_kantor);
								
				HashMap<String, Object> _propinsi_kantor = new HashMap<>();
				HashMap<String, Object> temp_propinsi_kantor = new HashMap<>();
				_propinsi_kantor.put("value", propinsi_kantor);
				_propinsi_kantor.put("id_endors", 34);
				_propinsi_kantor.put ("flag_direct", 0);
				temp_propinsi_kantor.put("propinsi_kantor", _propinsi_kantor);
				data_policyholder.putAll(temp_propinsi_kantor);
				
				HashMap<String, Object> _kabupaten_kantor = new HashMap<>();
				HashMap<String, Object> temp_kabupaten_kantor = new HashMap<>();
				_kabupaten_kantor.put("value", kabupaten_kantor);
				_kabupaten_kantor.put("id_endors", 34);
				_kabupaten_kantor.put ("flag_direct", 0);
				temp_kabupaten_kantor.put("kabupaten_kantor", _kabupaten_kantor);
				data_policyholder.putAll(temp_kabupaten_kantor);
				
				HashMap<String, Object> _kecamatan_kantor = new HashMap<>();
				HashMap<String, Object> temp_kecamatan_kantor = new HashMap<>();
				_kecamatan_kantor.put("value", kecamatan_kantor);
				_kecamatan_kantor.put("id_endors", 34);
				_kecamatan_kantor.put ("flag_direct", 0);
				temp_kecamatan_kantor.put("kecamatan_kantor", _kecamatan_kantor);
				data_policyholder.putAll(temp_kecamatan_kantor);
				
				HashMap<String, Object> _kelurahan_kantor = new HashMap<>();
				HashMap<String, Object> temp_kelurahan_kantor = new HashMap<>();
				_kelurahan_kantor.put("value", kelurahan_kantor);
				_kelurahan_kantor.put("id_endors", 34);
				_kelurahan_kantor.put ("flag_direct", 0);
				temp_kelurahan_kantor.put("kelurahan_kantor", _kelurahan_kantor);
				data_policyholder.putAll(temp_kelurahan_kantor);
				
				HashMap<String, Object> _kodepos_kantor = new HashMap<>();
				HashMap<String, Object> temp_kodepos_kantor = new HashMap<>();
				_kodepos_kantor.put("value", kodepos_kantor);
				_kodepos_kantor.put("id_endors", 34);
				_kodepos_kantor.put ("flag_direct", 0);
				temp_kodepos_kantor.put("kodepos_kantor", _kodepos_kantor);
				data_policyholder.putAll(temp_kodepos_kantor);
				
				HashMap<String, Object> _email = new HashMap<>();
				HashMap<String, Object> temp_email = new HashMap<>();
				_email.put("value", email);
				_email.put("id_endors", null);
				_email.put ("flag_direct", 0);
				temp_email.put("email", _email);
				data_policyholder.putAll(temp_email);
				
				HashMap<String, Object> _area_code_rumah_pp = new HashMap<>();
				HashMap<String, Object> temp_area_code_rumah_pp = new HashMap<>();
				_area_code_rumah_pp.put("value", area_code_rumah_pp);
				_area_code_rumah_pp.put("id_endors", null);
				_area_code_rumah_pp.put ("flag_direct", 0);
				temp_area_code_rumah_pp.put("area_code_rumah_pp", _area_code_rumah_pp);
				data_policyholder.putAll(temp_area_code_rumah_pp);
				
				HashMap<String, Object> _telpon_rumah_pp = new HashMap<>();
				HashMap<String, Object> temp_telpon_rumah_pp = new HashMap<>();
				_telpon_rumah_pp.put("value", telpon_rumah_pp);
				_telpon_rumah_pp.put("id_endors", null);
				_telpon_rumah_pp.put ("flag_direct", 0);
				temp_telpon_rumah_pp.put("telpon_rumah_pp", _telpon_rumah_pp);
				data_policyholder.putAll(temp_telpon_rumah_pp);
				
				HashMap<String, Object> _npwp = new HashMap<>();
				HashMap<String, Object> temp_npwp = new HashMap<>();
				_npwp.put("value", npwp);
				_npwp.put("id_endors", null);
				_npwp.put ("flag_direct", 0);
				temp_npwp.put("npwp", _npwp);
				data_policyholder.putAll(temp_npwp);

				HashMap<String, Object> _nama_bank_pp = new HashMap<>();
				HashMap<String, Object> temp_nama_bank_pp = new HashMap<>();
				_nama_bank_pp.put("value", nama_bank_pp);
				_nama_bank_pp.put("id_endors", 93);
				_nama_bank_pp.put ("flag_direct", 0);
				temp_nama_bank_pp.put("nama_bank_pp", _nama_bank_pp);
				data_policyholder.putAll(temp_nama_bank_pp);
				
				HashMap<String, Object> _cabang_bank_pp = new HashMap<>();
				HashMap<String, Object> temp_cabang_bank_pp = new HashMap<>();
				_cabang_bank_pp.put("value", cabang_bank_pp);
				_cabang_bank_pp.put("id_endors", 93);
				_cabang_bank_pp.put ("flag_direct", 0);
				temp_cabang_bank_pp.put("cabang_bank_pp", _cabang_bank_pp);
				data_policyholder.putAll(temp_cabang_bank_pp);
				
				HashMap<String, Object> _kota_bank_pp = new HashMap<>();
				HashMap<String, Object> temp_kota_bank_pp = new HashMap<>();
				_kota_bank_pp.put("value", kota_bank_pp);
				_kota_bank_pp.put("id_endors", 93);
				_kota_bank_pp.put ("flag_direct", 0);
				temp_kota_bank_pp.put("kota_bank_pp", _kota_bank_pp);
				data_policyholder.putAll(temp_kota_bank_pp);
				
				HashMap<String, Object> _no_rekening_pp = new HashMap<>();
				HashMap<String, Object> temp_no_rekening_pp = new HashMap<>();
				_no_rekening_pp.put("value", no_rekening_pp);
				_no_rekening_pp.put("id_endors", 93);
				_no_rekening_pp.put ("flag_direct", 0);
				temp_no_rekening_pp.put("no_rekening_pp", _no_rekening_pp);
				data_policyholder.putAll(temp_no_rekening_pp);
				
				HashMap<String, Object> _pemilik_rekening_pp = new HashMap<>();
				HashMap<String, Object> temp_pemilik_rekening_pp = new HashMap<>();
				_pemilik_rekening_pp.put("value", pemilik_rekening_pp);
				_pemilik_rekening_pp.put("id_endors", 93);
				_pemilik_rekening_pp.put ("flag_direct", 0);
				temp_pemilik_rekening_pp.put("pemilik_rekening_pp", _pemilik_rekening_pp);
				data_policyholder.putAll(temp_pemilik_rekening_pp);
				
			//GET INSURED
				PolicyAlteration insured = services.selectInsured(no_polis);
				Integer status_tt = insured.getStatus_tt();
				Integer agama_tt = insured.getAgama_tt();
				Integer kewarganegaraan_tt = insured.getKewarganegaraan_tt();
				String nama_perusahaan_tt = insured.getNama_perusahaan_tt();
				String jabatan_tt = insured.getJabatan_tt();
				String tipe_usaha_tt = insured.getTipe_usaha_tt();
				
				HashMap<String, Object> _status_tt = new HashMap<>();
				HashMap<String, Object> temp_status_tt = new HashMap<>();
				_status_tt.put("value", status_tt);
				_status_tt.put("id_endors", 68);
				_status_tt.put ("flag_direct", 1);
				temp_status_tt.put("status_tt", _status_tt);
				data_insured.putAll(temp_status_tt);
				
				HashMap<String, Object> _agama_tt = new HashMap<>();
				HashMap<String, Object> temp_agama_tt = new HashMap<>();
				_agama_tt.put("value", agama_tt);
				_agama_tt.put("id_endors", 62);
				_agama_tt.put ("flag_direct", 1);
				temp_agama_tt.put("agama_tt", _agama_tt);
				data_insured.putAll(temp_agama_tt);
				
				HashMap<String, Object> _kewarganegaraan_tt = new HashMap<>();
				HashMap<String, Object> temp_kewarganegaraan_tt = new HashMap<>();
				_kewarganegaraan_tt.put("value", kewarganegaraan_tt);
				_kewarganegaraan_tt.put("id_endors", 40);
				_kewarganegaraan_tt.put ("flag_direct", 0);
				temp_kewarganegaraan_tt.put("kewarganegaraan_tt", _kewarganegaraan_tt);
				data_insured.putAll(temp_kewarganegaraan_tt);
				
				HashMap<String, Object> _nama_perusahaan_tt = new HashMap<>();
				HashMap<String, Object> temp_nama_perusahaan_tt = new HashMap<>();
				_nama_perusahaan_tt.put("value", nama_perusahaan_tt);
				_nama_perusahaan_tt.put("id_endors", 90);
				_nama_perusahaan_tt.put ("flag_direct", 1);
				temp_nama_perusahaan_tt.put("nama_perusahaan_tt", _nama_perusahaan_tt);
				data_insured.putAll(temp_nama_perusahaan_tt);
				
				HashMap<String, Object> _jabatan_tt = new HashMap<>();
				HashMap<String, Object> temp_jabatan_tt = new HashMap<>();
				_jabatan_tt.put("value", jabatan_tt);
				_jabatan_tt.put("id_endors", 90);
				_jabatan_tt.put ("flag_direct", 1);
				temp_jabatan_tt.put("jabatan_tt", _jabatan_tt);
				data_insured.putAll(temp_jabatan_tt);
				
				HashMap<String, Object> _tipe_usaha_tt = new HashMap<>();
				HashMap<String, Object> temp_tipe_usaha_tt = new HashMap<>();
				_tipe_usaha_tt.put("value", tipe_usaha_tt);
				_tipe_usaha_tt.put("id_endors", 90);
				_tipe_usaha_tt.put ("flag_direct", 1);
				temp_tipe_usaha_tt.put("tipe_usaha_tt", _tipe_usaha_tt);
				data_insured.putAll(temp_tipe_usaha_tt);
				
			//GET PAYOR
				PolicyAlteration payor = services.selectPayor(no_polis);
				String cara_bayar = payor.getCara_bayar();
				Integer nama_bank_payor = payor.getNama_bank_payor();
				Integer cabang_bank_payor = payor.getCabang_bank_payor();
				String kota_bank_payor = payor.getKota_bank_payor();
				String no_rekening_payor = payor.getNo_rekening_payor();
				String pemilik_rekening_payor = payor.getPemilik_rekening_payor();
				String masa_berlaku = payor.getMasa_berlaku();
				String hubungan_payor = payor.getHubungan_payor();
				String nama_payor = payor.getNama_payor();
				String nama_perusahaan = payor.getNama_perusahaan();
				String jabatan = payor.getJabatan();
				String alamat_rumah = payor.getAlamat_rumah();
				Integer negara = payor.getNegara();
				Integer propinsi = payor.getPropinsi();
				Integer kabupaten = payor.getKabupaten();
				Integer kecamatan = payor.getKecamatan();
				Integer kelurahan = payor.getKelurahan();
				String kodepos = payor.getKodepos();
				String area_code_rumah = payor.getArea_code_rumah();
				String telpon_rumah = payor.getTelpon_rumah();
				String no_hp = payor.getNo_hp();
				String tujuan = payor.getTujuan();
				String sumber_dana = payor.getSumber_dana();
				String mkl_kerja = payor.getMkl_kerja();
				String mkl_penghasilan = payor.getMkl_penghasilan();
				String mkl_smbr_penghasilan = payor.getMkl_smbr_penghasilan();
				
				HashMap<String, Object> _cara_bayar = new HashMap<>();
				HashMap<String, Object> temp_cara_bayar = new HashMap<>();
				_cara_bayar.put("value", cara_bayar);
				_cara_bayar.put("id_endors", null);
				_cara_bayar.put ("flag_direct", 3);
				temp_cara_bayar.put("cara_bayar", _cara_bayar);
				data_payor.putAll(temp_cara_bayar);
				
				HashMap<String, Object> _nama_perusahaan = new HashMap<>();
				HashMap<String, Object> temp_nama_perusahaan = new HashMap<>();
				_nama_perusahaan.put("value", nama_perusahaan);
				_nama_perusahaan.put("id_endors", null);
				_nama_perusahaan.put ("flag_direct", 0);
				temp_nama_perusahaan.put("nama_perusahaan", _nama_perusahaan);
				data_payor.putAll(temp_nama_perusahaan);
				
				HashMap<String, Object> _jabatan = new HashMap<>();
				HashMap<String, Object> temp_jabatan = new HashMap<>();
				_jabatan.put("value", jabatan);
				_jabatan.put("id_endors", null);
				_jabatan.put ("flag_direct", 3);
				temp_jabatan.put("jabatan", _jabatan);
				data_payor.putAll(temp_jabatan);
				
				HashMap<String, Object> _negara = new HashMap<>();
				HashMap<String, Object> temp_negara = new HashMap<>();
				_negara.put("value", negara);
				_negara.put("id_endors", null);
				_negara.put ("flag_direct", 3);
				temp_negara.put("negara", _negara);
				data_payor.putAll(temp_negara);
				
				HashMap<String, Object> _nama_bank_payor = new HashMap<>();
				HashMap<String, Object> temp_nama_bank_payor = new HashMap<>();
				_nama_bank_payor.put("value", nama_bank_payor);
				_nama_bank_payor.put("id_endors", 96);
				_nama_bank_payor.put ("flag_direct", 0);
				temp_nama_bank_payor.put("nama_bank_payor", _nama_bank_payor);
				data_payor.putAll(temp_nama_bank_payor);
				
				HashMap<String, Object> _cabang_bank_payor = new HashMap<>();
				HashMap<String, Object> temp_cabang_bank_payor = new HashMap<>();
				_cabang_bank_payor.put("value", cabang_bank_payor);
				_cabang_bank_payor.put("id_endors", 96);
				_cabang_bank_payor.put ("flag_direct", 0);
				temp_cabang_bank_payor.put("cabang_bank_payor", _cabang_bank_payor);
				data_payor.putAll(temp_cabang_bank_payor);
				
				HashMap<String, Object> _kota_bank_payor = new HashMap<>();
				HashMap<String, Object> temp_kota_bank_payor = new HashMap<>();
				_kota_bank_payor.put("value", kota_bank_payor);
				_kota_bank_payor.put("id_endors", 96);
				_kota_bank_payor.put ("flag_direct", 0);
				temp_kota_bank_payor.put("kota_bank_payor", _kota_bank_payor);
				data_payor.putAll(temp_kota_bank_payor);
				
				HashMap<String, Object> _no_rekening_payor = new HashMap<>();
				HashMap<String, Object> temp_no_rekening_payor = new HashMap<>();
				_no_rekening_payor.put("value", no_rekening_payor);
				_no_rekening_payor.put("id_endors", 96);
				_no_rekening_payor.put ("flag_direct", 0);
				temp_no_rekening_payor.put("no_rekening_payor", _no_rekening_payor);
				data_payor.putAll(temp_no_rekening_payor);
				
				HashMap<String, Object> _pemilik_rekening_payor = new HashMap<>();
				HashMap<String, Object> temp_pemilik_rekening_payor = new HashMap<>();
				_pemilik_rekening_payor.put("value", pemilik_rekening_payor);
				_pemilik_rekening_payor.put("id_endors", 96);
				_pemilik_rekening_payor.put ("flag_direct", 0);
				temp_pemilik_rekening_payor.put("pemilik_rekening_payor", _pemilik_rekening_payor);
				data_payor.putAll(temp_pemilik_rekening_payor);
				
				HashMap<String, Object> _masa_berlaku = new HashMap<>();
				HashMap<String, Object> temp_masa_berlaku = new HashMap<>();
				_masa_berlaku.put("value", masa_berlaku);
				_masa_berlaku.put("id_endors", 96);
				_masa_berlaku.put ("flag_direct", 0);
				temp_masa_berlaku.put("masa_berlaku", _masa_berlaku);
				data_payor.putAll(temp_masa_berlaku);
				
				HashMap<String, Object> _hubungan_payor = new HashMap<>();
				HashMap<String, Object> temp_hubungan_payor = new HashMap<>();
				_hubungan_payor.put("value", hubungan_payor);
				_hubungan_payor.put("id_endors", null);
				_hubungan_payor.put ("flag_direct", 0);
				temp_hubungan_payor.put("hubungan_payor", _hubungan_payor);
				data_payor.putAll(temp_hubungan_payor);

				HashMap<String, Object> _nama_payor = new HashMap<>();
				HashMap<String, Object> temp_nama_payor = new HashMap<>();
				_nama_payor.put("value", nama_payor);
				_nama_payor.put("id_endors", null);
				_nama_payor.put ("flag_direct", 0);
				temp_nama_payor.put("nama_payor", _nama_payor);
				data_payor.putAll(temp_nama_payor);

				HashMap<String, Object> _alamat_rumah = new HashMap<>();
				HashMap<String, Object> temp_alamat_rumah = new HashMap<>();
				_alamat_rumah.put("value", alamat_rumah);
				_alamat_rumah.put("id_endors", null);
				_alamat_rumah.put ("flag_direct", 0);
				temp_alamat_rumah.put("alamat_rumah", _alamat_rumah);
				data_payor.putAll(temp_alamat_rumah);

				HashMap<String, Object> _propinsi = new HashMap<>();
				HashMap<String, Object> temp_propinsi = new HashMap<>();
				_propinsi.put("value", propinsi);
				_propinsi.put("id_endors", null);
				_propinsi.put ("flag_direct", 0);
				temp_propinsi.put("propinsi", _propinsi);
				data_payor.putAll(temp_propinsi);

				HashMap<String, Object> _kabupaten = new HashMap<>();
				HashMap<String, Object> temp_kabupaten = new HashMap<>();
				_kabupaten.put("value", kabupaten);
				_kabupaten.put("id_endors", null);
				_kabupaten.put ("flag_direct", 0);
				temp_kabupaten.put("kabupaten", _kabupaten);
				data_payor.putAll(temp_kabupaten);

				HashMap<String, Object> _kecamatan = new HashMap<>();
				HashMap<String, Object> temp_kecamatan = new HashMap<>();
				_kecamatan.put("value", kecamatan);
				_kecamatan.put("id_endors", null);
				_kecamatan.put ("flag_direct", 0);
				temp_kecamatan.put("kecamatan", _kecamatan);
				data_payor.putAll(temp_kecamatan);

				HashMap<String, Object> _kelurahan = new HashMap<>();
				HashMap<String, Object> temp_kelurahan = new HashMap<>();
				_kelurahan.put("value", kelurahan);
				_kelurahan.put("id_endors", null);
				_kelurahan.put ("flag_direct", 0);
				temp_kelurahan.put("kelurahan", _kelurahan);
				data_payor.putAll(temp_kelurahan);

				HashMap<String, Object> _kodepos = new HashMap<>();
				HashMap<String, Object> temp_kodepos = new HashMap<>();
				_kodepos.put("value", kodepos);
				_kodepos.put("id_endors", null);
				_kodepos.put ("flag_direct", 0);
				temp_kodepos.put("kodepos", _kodepos);
				data_payor.putAll(temp_kodepos);

				HashMap<String, Object> _area_code_rumah = new HashMap<>();
				HashMap<String, Object> temp_area_code_rumah = new HashMap<>();
				_area_code_rumah.put("value", area_code_rumah);
				_area_code_rumah.put("id_endors", null);
				_area_code_rumah.put ("flag_direct", 0);
				temp_area_code_rumah.put("area_code_rumah", _area_code_rumah);
				data_payor.putAll(temp_area_code_rumah);

				HashMap<String, Object> _telpon_rumah = new HashMap<>();
				HashMap<String, Object> temp_telpon_rumah = new HashMap<>();
				_telpon_rumah.put("value", telpon_rumah);
				_telpon_rumah.put("id_endors", null);
				_telpon_rumah.put ("flag_direct", 0);
				temp_telpon_rumah.put("telpon_rumah", _telpon_rumah);
				data_payor.putAll(temp_telpon_rumah);

				HashMap<String, Object> _no_hp = new HashMap<>();
				HashMap<String, Object> temp_no_hp = new HashMap<>();
				_no_hp.put("value", no_hp);
				_no_hp.put("id_endors", null);
				_no_hp.put ("flag_direct", 0);
				temp_no_hp.put("no_hp", _no_hp);
				data_payor.putAll(temp_no_hp);

				HashMap<String, Object> _tujuan = new HashMap<>();
				HashMap<String, Object> temp_tujuan = new HashMap<>();
				_tujuan.put("value", tujuan);
				_tujuan.put("id_endors", null);
				_tujuan.put ("flag_direct", 0);
				temp_tujuan.put("tujuan", _tujuan);
				data_payor.putAll(temp_tujuan);

				HashMap<String, Object> _sumber_dana = new HashMap<>();
				HashMap<String, Object> temp_sumber_dana = new HashMap<>();
				_sumber_dana.put("value", sumber_dana);
				_sumber_dana.put("id_endors", null);
				_sumber_dana.put ("flag_direct", 0);
				temp_sumber_dana.put("sumber_dana", _sumber_dana);
				data_payor.putAll(temp_sumber_dana);

				HashMap<String, Object> _mkl_kerja = new HashMap<>();
				HashMap<String, Object> temp_mkl_kerja = new HashMap<>();
				_mkl_kerja.put("value", mkl_kerja);
				_mkl_kerja.put("id_endors", null);
				_mkl_kerja.put ("flag_direct", 0);
				temp_mkl_kerja.put("mkl_kerja", _mkl_kerja);
				data_payor.putAll(temp_mkl_kerja);

				HashMap<String, Object> _mkl_penghasilan = new HashMap<>();
				HashMap<String, Object> temp_mkl_penghasilan = new HashMap<>();
				_mkl_penghasilan.put("value", mkl_penghasilan);
				_mkl_penghasilan.put("id_endors", null);
				_mkl_penghasilan.put ("flag_direct", 0);
				temp_mkl_penghasilan.put("mkl_penghasilan", _mkl_penghasilan);
				data_payor.putAll(temp_mkl_penghasilan);

				HashMap<String, Object> _mkl_smbr_penghasilan = new HashMap<>();
				HashMap<String, Object> temp_mkl_smbr_penghasilan = new HashMap<>();
				_mkl_smbr_penghasilan.put("value", mkl_smbr_penghasilan);
				_mkl_smbr_penghasilan.put("id_endors", null);
				_mkl_smbr_penghasilan.put ("flag_direct", 0);
				temp_mkl_smbr_penghasilan.put("mkl_smbr_penghasilan", _mkl_smbr_penghasilan);
				data_payor.putAll(temp_mkl_smbr_penghasilan);
				
			//GET BENEFICIARY
				ArrayList<Beneficiary> beneficiary = services.selectListBeneficiary(no_polis);
				ArrayList<Object> array_beneficiary = new ArrayList<>();
				HashMap<String, Object> listsBeneficiary = new HashMap<>();
				for (int y = 0; y < beneficiary.size(); y++) {
					HashMap<String, Object> data_beneficiary = new HashMap<>();
					
					Integer msaw_number = beneficiary.get(y).getMsaw_number();
				    String msaw_first = beneficiary.get(y).getMsaw_first();
				    String msaw_birth = beneficiary.get(y).getMsaw_birth();
				    String lsre_relation = beneficiary.get(y).getLsre_relation();
				    Integer msaw_persen = beneficiary.get(y).getMsaw_persen();
				    Integer msaw_sex = beneficiary.get(y).getMsaw_sex();
				    
				    HashMap<String, Object> _msaw_number = new HashMap<>();
					HashMap<String, Object> temp_msaw_number = new HashMap<>();
					_msaw_number.put("value", msaw_number);
					_msaw_number.put("id_endors", 93);
					_msaw_number.put ("flag_direct", 0);
					temp_msaw_number.put("msaw_number", _msaw_number);
					data_beneficiary.putAll(temp_msaw_number);
					
					HashMap<String, Object> _msaw_first = new HashMap<>();
					HashMap<String, Object> temp_msaw_first = new HashMap<>();
					_msaw_first.put("value", msaw_first);
					_msaw_first.put("id_endors", 93);
					_msaw_first.put ("flag_direct", 0);
					temp_msaw_first.put("msaw_first", _msaw_first);
					data_beneficiary.putAll(temp_msaw_first);
					
					HashMap<String, Object> _msaw_birth = new HashMap<>();
					HashMap<String, Object> temp_msaw_birth = new HashMap<>();
					_msaw_birth.put("value", msaw_birth);
					_msaw_birth.put("id_endors", 93);
					_msaw_birth.put ("flag_direct", 0);
					temp_msaw_birth.put("msaw_birth", _msaw_birth);
					data_beneficiary.putAll(temp_msaw_birth);
					
					HashMap<String, Object> _lsre_relation = new HashMap<>();
					HashMap<String, Object> temp_lsre_relation = new HashMap<>();
					_lsre_relation.put("value", lsre_relation);
					_lsre_relation.put("id_endors", 93);
					_lsre_relation.put ("flag_direct", 0);
					temp_lsre_relation.put("lsre_relation", _lsre_relation);
					data_beneficiary.putAll(temp_lsre_relation);
					
					HashMap<String, Object> _msaw_persen = new HashMap<>();
					HashMap<String, Object> temp_msaw_persen = new HashMap<>();
					_msaw_persen.put("value", msaw_persen);
					_msaw_persen.put("id_endors", 93);
					_msaw_persen.put ("flag_direct", 0);
					temp_msaw_persen.put("msaw_persen", _msaw_persen);
					data_beneficiary.putAll(temp_msaw_persen);
					
					HashMap<String, Object> _msaw_sex = new HashMap<>();
					HashMap<String, Object> temp_msaw_sex = new HashMap<>();
					_msaw_sex.put("value", msaw_sex);
					_msaw_sex.put("id_endors", 93);
					_msaw_sex.put ("flag_direct", 0);
					temp_msaw_sex.put("msaw_sex", _msaw_sex);
					data_beneficiary.putAll(temp_msaw_sex);
					
					listsBeneficiary.put("anggota", data_beneficiary);
					
					array_beneficiary.add(listsBeneficiary);
				}
				
				data.put("policyholder", data_policyholder);
				data.put("insured", data_insured);
				data.put("payor", data_payor);
				data.put("beneficiary",array_beneficiary);
								
				error = false;
				message = "Successfully get policy alteration";
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
	
	@RequestMapping(value = "/submitpolicyalteration", produces = "application/json", method = RequestMethod.POST)
	public String submitPolicyAlteration(@RequestBody RequestViewPolicyAlteration requestViewPolicyAlteration,
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
		Insured insured = requestViewPolicyAlteration.getInsured();
		Payor payor = requestViewPolicyAlteration.getPayor();
		PolicyHolder policyholder = requestViewPolicyAlteration.getPolicyholder();
		ArrayList <Beneficiary> beneficiary = requestViewPolicyAlteration.getBeneficiary();
		ArrayList <Beneficiary> beneficiary_add = requestViewPolicyAlteration.getBeneficiary();
		
		String msde_old1 = null, msde_old2 = null, msde_old3 = null, msde_old4 = null, msde_old5 = null, msde_old6 = null,
				msde_new1 = null, msde_new2 = null, msde_new3 = null, msde_new4 = null, msde_new5 = null, msde_new6 = null;
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get SPAJ
				Pemegang paramSelectSPAJ = new Pemegang();
				paramSelectSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);
				String reg_spaj = dataSPAJ.getReg_spaj();
				
				//AMBIL INSURED
				if(insured!=null){
					DetailPolicyAlteration status_tt = insured.getStatus_tt();
					DetailPolicyAlteration agama_tt = insured.getAgama_tt();
					DetailPolicyAlteration Nama_perusahaan_tt = insured.getNama_perusahaan_tt();
					DetailPolicyAlteration Tipe_usaha_tt = insured.getTipe_usaha_tt();
					DetailPolicyAlteration Jabatan_tt = insured.getJabatan_tt();
					
					if(status_tt!=null) {
						Integer id_endors = status_tt.getId_endors();
						String old = status_tt.getOld();
						String new_ = status_tt.getNew_();
						Integer flag_direct = status_tt.getFlag_direct();
						
						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
						//String mcl_id_pp = services.selectMclId_PP(reg_spaj);
						//PolicyAlteration policyAlteration = new PolicyAlteration();
						
						//Integer mspe_sts_mrt_new = Integer.parseInt(new_);
						
						//policyAlteration.setMcl_id(mcl_id_pp);
						//policyAlteration.setMspe_sts_mrt(mspe_sts_mrt_new);
												
						//UPDATE STATUS
						//services.updateStatus(policyAlteration);
					} if(agama_tt!=null) {
						Integer id_endors = agama_tt.getId_endors();
						String old = agama_tt.getOld();
						String new_ = agama_tt.getNew_();
						Integer flag_direct = agama_tt.getFlag_direct();
						
						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(Nama_perusahaan_tt!=null) {
						Integer id_endors = Nama_perusahaan_tt.getId_endors();
						String old = Nama_perusahaan_tt.getOld();
						String new_ = Nama_perusahaan_tt.getNew_();
						Integer flag_direct = Nama_perusahaan_tt.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(Tipe_usaha_tt!=null) {
						Integer id_endors = Tipe_usaha_tt.getId_endors();
						String old = Tipe_usaha_tt.getOld();
						String new_ = Tipe_usaha_tt.getNew_();
						Integer flag_direct = Tipe_usaha_tt.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(Jabatan_tt!=null) {
						Integer id_endors = Jabatan_tt.getId_endors();
						String old = Jabatan_tt.getOld();
						String new_ = Jabatan_tt.getNew_();
						Integer flag_direct = Jabatan_tt.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					}
				}
				//AMBIL PAYOR
				if(payor!=null) {
					DetailPolicyAlteration cara_bayar = payor.getCara_bayar();
					DetailPolicyAlteration nama_bank_payor = payor.getNama_bank_payor();
					DetailPolicyAlteration cabang_bank_payor = payor.getCabang_bank_payor();
					DetailPolicyAlteration kota_bank_payor = payor.getKota_bank_payor();
					DetailPolicyAlteration no_rekening_payor = payor.getNo_rekening_payor();
					DetailPolicyAlteration pemilik_rekening_payor = payor.getPemilik_rekening_payor();
					DetailPolicyAlteration masa_berlaku = payor.getMasa_berlaku();
					DetailPolicyAlteration hubungan_payor = payor.getHubungan_payor();
					DetailPolicyAlteration nama_payor = payor.getNama_payor();
					DetailPolicyAlteration nama_perusahaan = payor.getNama_perusahaan();
					DetailPolicyAlteration jabatan = payor.getJabatan();
					DetailPolicyAlteration alamat_rumah = payor.getAlamat_rumah();
					DetailPolicyAlteration negara = payor.getNegara();
					DetailPolicyAlteration propinsi = payor.getPropinsi();
					DetailPolicyAlteration kabupaten = payor.getKabupaten();
					DetailPolicyAlteration kecamatan = payor.getKecamatan();
					DetailPolicyAlteration kelurahan = payor.getKelurahan();
					DetailPolicyAlteration kodepos = payor.getKodepos();
					DetailPolicyAlteration area_code_rumah = payor.getArea_code_rumah();
					DetailPolicyAlteration telpon_rumah = payor.getTelpon_rumah();
					DetailPolicyAlteration no_hp = payor.getNo_hp();
					DetailPolicyAlteration tujuan = payor.getTujuan();
					DetailPolicyAlteration sumber_dana = payor.getSumber_dana();
					DetailPolicyAlteration mkl_kerja = payor.getMkl_kerja();
					DetailPolicyAlteration mkl_penghasilan = payor.getMkl_penghasilan();
					DetailPolicyAlteration mkl_smbr_penghasilan = payor.getMkl_smbr_penghasilan();
					
					if(cara_bayar!=null) {
						Integer id_endors = cara_bayar.getId_endors();
						String old = cara_bayar.getOld();
						String new_ = cara_bayar.getNew_();
						Integer flag_direct = cara_bayar.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(nama_bank_payor!=null) {
						Integer id_endors = nama_bank_payor.getId_endors();
						String old = nama_bank_payor.getOld();
						String new_ = nama_bank_payor.getNew_();
						Integer flag_direct = nama_bank_payor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(cabang_bank_payor!=null) {
						Integer id_endors = cabang_bank_payor.getId_endors();
						String old = cabang_bank_payor.getOld();
						String new_ = cabang_bank_payor.getNew_();
						Integer flag_direct = cabang_bank_payor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kota_bank_payor!=null) {
						Integer id_endors = kota_bank_payor.getId_endors();
						String old = kota_bank_payor.getOld();
						String new_ = kota_bank_payor.getNew_();
						Integer flag_direct = kota_bank_payor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(no_rekening_payor!=null) {
						Integer id_endors = no_rekening_payor.getId_endors();
						String old = no_rekening_payor.getOld();
						String new_ = no_rekening_payor.getNew_();
						Integer flag_direct = no_rekening_payor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(pemilik_rekening_payor!=null) {
						Integer id_endors = pemilik_rekening_payor.getId_endors();
						String old = pemilik_rekening_payor.getOld();
						String new_ = pemilik_rekening_payor.getNew_();
						Integer flag_direct = pemilik_rekening_payor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(masa_berlaku!=null) {
						Integer id_endors = masa_berlaku.getId_endors();
						String old = masa_berlaku.getOld();
						String new_ = masa_berlaku.getNew_();
						Integer flag_direct = masa_berlaku.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(hubungan_payor!=null) {
						Integer id_endors = hubungan_payor.getId_endors();
						String old = hubungan_payor.getOld();
						String new_ = hubungan_payor.getNew_();
						Integer flag_direct = hubungan_payor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(nama_payor!=null) {
						Integer id_endors = nama_payor.getId_endors();
						String old = nama_payor.getOld();
						String new_ = nama_payor.getNew_();
						Integer flag_direct = nama_payor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(nama_perusahaan!=null) {
						Integer id_endors = nama_perusahaan.getId_endors();
						String old = nama_perusahaan.getOld();
						String new_ = nama_perusahaan.getNew_();
						Integer flag_direct = nama_perusahaan.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(jabatan!=null) {
						Integer id_endors = jabatan.getId_endors();
						String old = jabatan.getOld();
						String new_ = jabatan.getNew_();
						Integer flag_direct = jabatan.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(alamat_rumah!=null) {
						Integer id_endors = alamat_rumah.getId_endors();
						String old = alamat_rumah.getOld();
						String new_ = alamat_rumah.getNew_();
						Integer flag_direct = alamat_rumah.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(negara!=null) {
						Integer id_endors = negara.getId_endors();
						String old = negara.getOld();
						String new_ = negara.getNew_();
						Integer flag_direct = negara.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(propinsi!=null) {
						Integer id_endors = propinsi.getId_endors();
						String old = propinsi.getOld();
						String new_ = propinsi.getNew_();
						Integer flag_direct = propinsi.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kabupaten!=null) {
						Integer id_endors = kabupaten.getId_endors();
						String old = kabupaten.getOld();
						String new_ = kabupaten.getNew_();
						Integer flag_direct = kabupaten.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kecamatan!=null) {
						Integer id_endors = kecamatan.getId_endors();
						String old = kecamatan.getOld();
						String new_ = kecamatan.getNew_();
						Integer flag_direct = kecamatan.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kelurahan!=null) {
						Integer id_endors = kelurahan.getId_endors();
						String old = kelurahan.getOld();
						String new_ = kelurahan.getNew_();
						Integer flag_direct = kelurahan.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kodepos!=null) {
						Integer id_endors = kodepos.getId_endors();
						String old = kodepos.getOld();
						String new_ = kodepos.getNew_();
						Integer flag_direct = kodepos.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(area_code_rumah!=null) {
						Integer id_endors = area_code_rumah.getId_endors();
						String old = area_code_rumah.getOld();
						String new_ = area_code_rumah.getNew_();
						Integer flag_direct = area_code_rumah.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(telpon_rumah!=null) {
						Integer id_endors = telpon_rumah.getId_endors();
						String old = telpon_rumah.getOld();
						String new_ = telpon_rumah.getNew_();
						Integer flag_direct = telpon_rumah.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(no_hp!=null) {
						Integer id_endors = no_hp.getId_endors();
						String old = no_hp.getOld();
						String new_ = no_hp.getNew_();
						Integer flag_direct = no_hp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(tujuan!=null) {
						Integer id_endors = tujuan.getId_endors();
						String old = tujuan.getOld();
						String new_ = tujuan.getNew_();
						Integer flag_direct = tujuan.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(sumber_dana!=null) {
						Integer id_endors = sumber_dana.getId_endors();
						String old = sumber_dana.getOld();
						String new_ = sumber_dana.getNew_();
						Integer flag_direct = sumber_dana.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(mkl_kerja!=null) {
						Integer id_endors = mkl_kerja.getId_endors();
						String old = mkl_kerja.getOld();
						String new_ = mkl_kerja.getNew_();
						Integer flag_direct = mkl_kerja.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(mkl_penghasilan!=null) {
						Integer id_endors = mkl_penghasilan.getId_endors();
						String old = mkl_penghasilan.getOld();
						String new_ = mkl_penghasilan.getNew_();
						Integer flag_direct = mkl_penghasilan.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(mkl_smbr_penghasilan!=null) {
						Integer id_endors = mkl_smbr_penghasilan.getId_endors();
						String old = mkl_smbr_penghasilan.getOld();
						String new_ = mkl_smbr_penghasilan.getNew_();
						Integer flag_direct = mkl_smbr_penghasilan.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					}
				}
				//AMBIL POLICY HOLDER
				if(policyholder!=null) {
					DetailPolicyAlteration nama_pp = policyholder.getNama_pp();
					DetailPolicyAlteration jenis_produk = policyholder.getJenis_produk();
					DetailPolicyAlteration status = policyholder.getStatus();
					DetailPolicyAlteration agama = policyholder.getAgama();
					DetailPolicyAlteration kewarganegaraan_pp = policyholder.getKewarganegaraan_pp();
					DetailPolicyAlteration npwp = policyholder.getNpwp();
					DetailPolicyAlteration nama_perusahaan_pp = policyholder.getNama_perusahaan_pp();
					DetailPolicyAlteration jabatan_pp = policyholder.getJabatan_pp();
					DetailPolicyAlteration uraian_pekerjaan = policyholder.getUraian_pekerjaan();
					DetailPolicyAlteration alamat_kantor = policyholder.getAlamat_kantor();
					DetailPolicyAlteration propinsi_kantor = policyholder.getPropinsi_kantor();
					DetailPolicyAlteration kabupaten_kantor = policyholder.getKabupaten_kantor();
					DetailPolicyAlteration kecamatan_kantor = policyholder.getKecamatan_kantor();
					DetailPolicyAlteration kelurahan_kantor = policyholder.getKelurahan_kantor();
					DetailPolicyAlteration kodepos_kantor = policyholder.getKodepos_kantor();
					DetailPolicyAlteration area_code_rumah_pp = policyholder.getArea_code_rumah_pp();
					DetailPolicyAlteration telpon_rumah_pp = policyholder.getTelpon_rumah_pp();
					DetailPolicyAlteration alamat_rumah_pp = policyholder.getAlamat_rumah_pp();
					DetailPolicyAlteration propinsi_rumah = policyholder.getPropinsi_rumah();
					DetailPolicyAlteration kabupaten_rumah = policyholder.getKabupaten_rumah();
					DetailPolicyAlteration kecamatan_rumah = policyholder.getKecamatan_rumah();
					DetailPolicyAlteration kelurahan_rumah = policyholder.getKelurahan_rumah();
					DetailPolicyAlteration kodepos_rumah = policyholder.getKodepos_rumah();
					DetailPolicyAlteration alamat_tpt_tinggal = policyholder.getAlamat_tpt_tinggal();
					DetailPolicyAlteration korespondensi_flag = policyholder.getKorespondensi_flag();
					DetailPolicyAlteration nama_bank_pp = policyholder.getNama_bank_pp();
					DetailPolicyAlteration cabang_bank_pp = policyholder.getCabang_bank_pp();
					DetailPolicyAlteration kota_bank_pp = policyholder.getKota_bank_pp();
					DetailPolicyAlteration no_rekening_pp = policyholder.getNo_rekening_pp();
					DetailPolicyAlteration pemilik_rekening_pp = policyholder.getPemilik_rekening_pp();
					DetailPolicyAlteration email = policyholder.getEmail();
					DetailPolicyAlteration tipe_usaha_pp = policyholder.getTipe_usaha_pp();
					DetailPolicyAlteration negara_tinggal = policyholder.getNegara_tinggal();
					DetailPolicyAlteration propinsi_tinggal = policyholder.getPropinsi_tinggal();
					DetailPolicyAlteration kabupaten_tinggal = policyholder.getKabupaten_tinggal();
					DetailPolicyAlteration kecamatan_tinggal = policyholder.getKecamatan_tinggal();
					DetailPolicyAlteration kelurahan_tinggal = policyholder.getKelurahan_tinggal();
					DetailPolicyAlteration kodepos_tinggal = policyholder.getKodepos_tinggal();
					
					if(nama_pp!=null) {
						Integer id_endors = nama_pp.getId_endors();
						String old = nama_pp.getOld();
						String new_ = nama_pp.getNew_();
						Integer flag_direct = nama_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(jenis_produk!=null) {
						Integer id_endors = jenis_produk.getId_endors();
						String old = jenis_produk.getOld();
						String new_ = jenis_produk.getNew_();
						Integer flag_direct = jenis_produk.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(status!=null) {
						Integer id_endors = status.getId_endors();
						String old = status.getOld();
						String new_ = status.getNew_();
						Integer flag_direct = status.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(agama!=null) {
						Integer id_endors = agama.getId_endors();
						String old = agama.getOld();
						String new_ = agama.getNew_();
						Integer flag_direct = agama.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kewarganegaraan_pp!=null) {
						Integer id_endors = kewarganegaraan_pp.getId_endors();
						String old = kewarganegaraan_pp.getOld();
						String new_ = kewarganegaraan_pp.getNew_();
						Integer flag_direct = kewarganegaraan_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(npwp!=null) {
						Integer id_endors = npwp.getId_endors();
						String old = npwp.getOld();
						String new_ = npwp.getNew_();
						Integer flag_direct = npwp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(nama_perusahaan_pp!=null) {
						Integer id_endors = nama_perusahaan_pp.getId_endors();
						String old = nama_perusahaan_pp.getOld();
						String new_ = nama_perusahaan_pp.getNew_();
						Integer flag_direct = nama_perusahaan_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(jabatan_pp!=null) {
						Integer id_endors = jabatan_pp.getId_endors();
						String old = jabatan_pp.getOld();
						String new_ = jabatan_pp.getNew_();
						Integer flag_direct = jabatan_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(uraian_pekerjaan!=null) {
						Integer id_endors = uraian_pekerjaan.getId_endors();
						String old = uraian_pekerjaan.getOld();
						String new_ = uraian_pekerjaan.getNew_();
						Integer flag_direct = uraian_pekerjaan.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(alamat_kantor!=null) {
						Integer id_endors = alamat_kantor.getId_endors();
						String old = alamat_kantor.getOld();
						String new_ = alamat_kantor.getNew_();
						Integer flag_direct = alamat_kantor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(propinsi_kantor!=null) {
						Integer id_endors = propinsi_kantor.getId_endors();
						String old = propinsi_kantor.getOld();
						String new_ = propinsi_kantor.getNew_();
						Integer flag_direct = propinsi_kantor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kabupaten_kantor!=null) {
						Integer id_endors = kabupaten_kantor.getId_endors();
						String old = kabupaten_kantor.getOld();
						String new_ = kabupaten_kantor.getNew_();
						Integer flag_direct = kabupaten_kantor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kecamatan_kantor!=null) {
						Integer id_endors = kecamatan_kantor.getId_endors();
						String old = kecamatan_kantor.getOld();
						String new_ = kecamatan_kantor.getNew_();
						Integer flag_direct = kecamatan_kantor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kelurahan_kantor!=null) {
						Integer id_endors = kelurahan_kantor.getId_endors();
						String old = kelurahan_kantor.getOld();
						String new_ = kelurahan_kantor.getNew_();
						Integer flag_direct = kelurahan_kantor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kodepos_kantor!=null) {
						Integer id_endors = kodepos_kantor.getId_endors();
						String old = kodepos_kantor.getOld();
						String new_ = kodepos_kantor.getNew_();
						Integer flag_direct = kodepos_kantor.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(area_code_rumah_pp!=null) {
						Integer id_endors = area_code_rumah_pp.getId_endors();
						String old = area_code_rumah_pp.getOld();
						String new_ = area_code_rumah_pp.getNew_();
						Integer flag_direct = area_code_rumah_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(telpon_rumah_pp!=null) {
						Integer id_endors = telpon_rumah_pp.getId_endors();
						String old = telpon_rumah_pp.getOld();
						String new_ = telpon_rumah_pp.getNew_();
						Integer flag_direct = telpon_rumah_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(alamat_rumah_pp!=null) {
						Integer id_endors = alamat_rumah_pp.getId_endors();
						String old = alamat_rumah_pp.getOld();
						String new_ = alamat_rumah_pp.getNew_();
						Integer flag_direct = alamat_rumah_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(propinsi_rumah!=null) {
						Integer id_endors = propinsi_rumah.getId_endors();
						String old = propinsi_rumah.getOld();
						String new_ = propinsi_rumah.getNew_();
						Integer flag_direct = propinsi_rumah.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kabupaten_rumah!=null) {
						Integer id_endors = kabupaten_rumah.getId_endors();
						String old = kabupaten_rumah.getOld();
						String new_ = kabupaten_rumah.getNew_();
						Integer flag_direct = kabupaten_rumah.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kecamatan_rumah!=null) {
						Integer id_endors = kecamatan_rumah.getId_endors();
						String old = kecamatan_rumah.getOld();
						String new_ = kecamatan_rumah.getNew_();
						Integer flag_direct = kecamatan_rumah.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kelurahan_rumah!=null) {
						Integer id_endors = kelurahan_rumah.getId_endors();
						String old = kelurahan_rumah.getOld();
						String new_ = kelurahan_rumah.getNew_();
						Integer flag_direct = kelurahan_rumah.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kodepos_rumah!=null) {
						Integer id_endors = kodepos_rumah.getId_endors();
						String old = kodepos_rumah.getOld();
						String new_ = kodepos_rumah.getNew_();
						Integer flag_direct = kodepos_rumah.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(alamat_tpt_tinggal!=null) {
						Integer id_endors = alamat_tpt_tinggal.getId_endors();
						String old = alamat_tpt_tinggal.getOld();
						String new_ = alamat_tpt_tinggal.getNew_();
						Integer flag_direct = alamat_tpt_tinggal.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(korespondensi_flag!=null) {
						Integer id_endors = korespondensi_flag.getId_endors();
						String old = korespondensi_flag.getOld();
						String new_ = korespondensi_flag.getNew_();
						Integer flag_direct = korespondensi_flag.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(nama_bank_pp!=null) {
						Integer id_endors = nama_bank_pp.getId_endors();
						String old = nama_bank_pp.getOld();
						String new_ = nama_bank_pp.getNew_();
						Integer flag_direct = nama_bank_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(cabang_bank_pp!=null) {
						Integer id_endors = cabang_bank_pp.getId_endors();
						String old = cabang_bank_pp.getOld();
						String new_ = cabang_bank_pp.getNew_();
						Integer flag_direct = cabang_bank_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kota_bank_pp!=null) {
						Integer id_endors = kota_bank_pp.getId_endors();
						String old = kota_bank_pp.getOld();
						String new_ = kota_bank_pp.getNew_();
						Integer flag_direct = kota_bank_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(no_rekening_pp!=null) {
						Integer id_endors = no_rekening_pp.getId_endors();
						String old = no_rekening_pp.getOld();
						String new_ = no_rekening_pp.getNew_();
						Integer flag_direct = no_rekening_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(pemilik_rekening_pp!=null) {
						Integer id_endors = pemilik_rekening_pp.getId_endors();
						String old = pemilik_rekening_pp.getOld();
						String new_ = pemilik_rekening_pp.getNew_();
						Integer flag_direct = pemilik_rekening_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(email!=null) {
						Integer id_endors = email.getId_endors();
						String old = email.getOld();
						String new_ = email.getNew_();
						Integer flag_direct = email.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(tipe_usaha_pp!=null) {
						Integer id_endors = tipe_usaha_pp.getId_endors();
						String old = tipe_usaha_pp.getOld();
						String new_ = tipe_usaha_pp.getNew_();
						Integer flag_direct = tipe_usaha_pp.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(negara_tinggal!=null) {
						Integer id_endors = negara_tinggal.getId_endors();
						String old = negara_tinggal.getOld();
						String new_ = negara_tinggal.getNew_();
						Integer flag_direct = negara_tinggal.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(propinsi_tinggal!=null) {
						Integer id_endors = propinsi_tinggal.getId_endors();
						String old = propinsi_tinggal.getOld();
						String new_ = propinsi_tinggal.getNew_();
						Integer flag_direct = propinsi_tinggal.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kabupaten_tinggal!=null) {
						Integer id_endors = kabupaten_tinggal.getId_endors();
						String old = kabupaten_tinggal.getOld();
						String new_ = kabupaten_tinggal.getNew_();
						Integer flag_direct = kabupaten_tinggal.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kecamatan_tinggal!=null) {
						Integer id_endors = kecamatan_tinggal.getId_endors();
						String old = kecamatan_tinggal.getOld();
						String new_ = kecamatan_tinggal.getNew_();
						Integer flag_direct = kecamatan_tinggal.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kelurahan_tinggal!=null) {
						Integer id_endors = kelurahan_tinggal.getId_endors();
						String old = kelurahan_tinggal.getOld();
						String new_ = kelurahan_tinggal.getNew_();
						Integer flag_direct = kelurahan_tinggal.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					} if(kodepos_tinggal!=null) {
						Integer id_endors = kodepos_tinggal.getId_endors();
						String old = kodepos_tinggal.getOld();
						String new_ = kodepos_tinggal.getNew_();
						Integer flag_direct = kodepos_tinggal.getFlag_direct();

						Integer lsje_id = id_endors;
						Endorse endors = services.selectListJenisEndors(lsje_id);
						String msen_alasan = endors.getLsje_jenis();
						msde_old1 = old;
						msde_new1 = new_;
						
						if(flag_direct==1) {
							customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						} else if (flag_direct==2) {
							customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
									msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						}
						
					}
				}
				
				if(beneficiary!=null) {
					for(int i=0; i<beneficiary.size(); i++) {
						Anggota anggota = beneficiary.get(i).getAnggota();
						
						DetailPolicyAlteration msaw_first = anggota.getMsaw_first();
						DetailPolicyAlteration msaw_birth = anggota.getMsaw_birth();
						DetailPolicyAlteration msaw_persen = anggota.getMsaw_persen();
					    DetailPolicyAlteration lsre_relation = anggota.getLsre_relation();
					    DetailPolicyAlteration msaw_sex = anggota.getMsaw_sex();
					    
					    if(msaw_first!=null) {
							Integer id_endors = msaw_first.getId_endors();
							String old = msaw_first.getOld();
							String new_ = msaw_first.getNew_();
							Integer flag_direct = msaw_first.getFlag_direct();

							Integer lsje_id = id_endors;
							Endorse endors = services.selectListJenisEndors(lsje_id);
							String msen_alasan = endors.getLsje_jenis();
							msde_old1 = old;
							msde_new1 = new_;
							
							if(flag_direct==1) {
								customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							} else if (flag_direct==2) {
								customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							}
							
						} if(msaw_birth!=null) {
							Integer id_endors = msaw_birth.getId_endors();
							String old = msaw_birth.getOld();
							String new_ = msaw_birth.getNew_();
							Integer flag_direct = msaw_birth.getFlag_direct();

							Integer lsje_id = id_endors;
							Endorse endors = services.selectListJenisEndors(lsje_id);
							String msen_alasan = endors.getLsje_jenis();
							msde_old1 = old;
							msde_new1 = new_;
							
							if(flag_direct==1) {
								customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							} else if (flag_direct==2) {
								customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							}
							
						} if(msaw_persen!=null) {
							Integer id_endors = msaw_persen.getId_endors();
							String old = msaw_persen.getOld();
							String new_ = msaw_persen.getNew_();
							Integer flag_direct = msaw_persen.getFlag_direct();

							Integer lsje_id = id_endors;
							Endorse endors = services.selectListJenisEndors(lsje_id);
							String msen_alasan = endors.getLsje_jenis();
							msde_old1 = old;
							msde_new1 = new_;
							
							if(flag_direct==1) {
								customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							} else if (flag_direct==2) {
								customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							}
							
						} if(lsre_relation!=null) {
							Integer id_endors = lsre_relation.getId_endors();
							String old = lsre_relation.getOld();
							String new_ = lsre_relation.getNew_();
							Integer flag_direct = lsre_relation.getFlag_direct();

							Integer lsje_id = id_endors;
							Endorse endors = services.selectListJenisEndors(lsje_id);
							String msen_alasan = endors.getLsje_jenis();
							msde_old1 = old;
							msde_new1 = new_;
							
							if(flag_direct==1) {
								customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							} else if (flag_direct==2) {
								customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							}
							
						} if(msaw_sex!=null) {
							Integer id_endors = msaw_sex.getId_endors();
							String old = msaw_sex.getOld();
							String new_ = msaw_sex.getNew_();
							Integer flag_direct = msaw_sex.getFlag_direct();

							Integer lsje_id = id_endors;
							Endorse endors = services.selectListJenisEndors(lsje_id);
							String msen_alasan = endors.getLsje_jenis();
							msde_old1 = old;
							msde_new1 = new_;
							
							if(flag_direct==1) {
								customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							} else if (flag_direct==2) {
								customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							}
							
						}
					}
				}
				
				if(beneficiary_add!=null) {
					for(int i=0; i<beneficiary_add.size(); i++) {
						Anggota anggota = beneficiary_add.get(i).getAnggota();
						
						DetailPolicyAlteration msaw_first = anggota.getMsaw_first();
						DetailPolicyAlteration msaw_birth = anggota.getMsaw_birth();
						DetailPolicyAlteration msaw_persen = anggota.getMsaw_persen();
					    DetailPolicyAlteration lsre_relation = anggota.getLsre_relation();
					    DetailPolicyAlteration msaw_sex = anggota.getMsaw_sex();
					    
					    if(msaw_first!=null) {
							Integer id_endors = msaw_first.getId_endors();
							String old = msaw_first.getOld();
							String new_ = msaw_first.getNew_();
							Integer flag_direct = msaw_first.getFlag_direct();

							Integer lsje_id = id_endors;
							Endorse endors = services.selectListJenisEndors(lsje_id);
							String msen_alasan = endors.getLsje_jenis();
							msde_old1 = old;
							msde_new1 = new_;
							
							if(flag_direct==1) {
								customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							} else if (flag_direct==2) {
								customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							}
							
						} if(msaw_birth!=null) {
							Integer id_endors = msaw_birth.getId_endors();
							String old = msaw_birth.getOld();
							String new_ = msaw_birth.getNew_();
							Integer flag_direct = msaw_birth.getFlag_direct();

							Integer lsje_id = id_endors;
							Endorse endors = services.selectListJenisEndors(lsje_id);
							String msen_alasan = endors.getLsje_jenis();
							msde_old1 = old;
							msde_new1 = new_;
							
							if(flag_direct==1) {
								customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							} else if (flag_direct==2) {
								customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							}
							
						} if(msaw_persen!=null) {
							Integer id_endors = msaw_persen.getId_endors();
							String old = msaw_persen.getOld();
							String new_ = msaw_persen.getNew_();
							Integer flag_direct = msaw_persen.getFlag_direct();

							Integer lsje_id = id_endors;
							Endorse endors = services.selectListJenisEndors(lsje_id);
							String msen_alasan = endors.getLsje_jenis();
							msde_old1 = old;
							msde_new1 = new_;
							
							if(flag_direct==1) {
								customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							} else if (flag_direct==2) {
								customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							}
							
						} if(lsre_relation!=null) {
							Integer id_endors = lsre_relation.getId_endors();
							String old = lsre_relation.getOld();
							String new_ = lsre_relation.getNew_();
							Integer flag_direct = lsre_relation.getFlag_direct();

							Integer lsje_id = id_endors;
							Endorse endors = services.selectListJenisEndors(lsje_id);
							String msen_alasan = endors.getLsje_jenis();
							msde_old1 = old;
							msde_new1 = new_;
							
							if(flag_direct==1) {
								customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							} else if (flag_direct==2) {
								customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							}
							
						} if(msaw_sex!=null) {
							Integer id_endors = msaw_sex.getId_endors();
							String old = msaw_sex.getOld();
							String new_ = msaw_sex.getNew_();
							Integer flag_direct = msaw_sex.getFlag_direct();

							Integer lsje_id = id_endors;
							Endorse endors = services.selectListJenisEndors(lsje_id);
							String msen_alasan = endors.getLsje_jenis();
							msde_old1 = old;
							msde_new1 = new_;
							
							if(flag_direct==1) {
								customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							} else if (flag_direct==2) {
								customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
										msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
							}
							
						}
					}
				}
				
				/*
				listPolicyAlteration = requestViewPolicyAlteration.getPolicy_alteration();
				
				//Endorse endors = new Endorse();
				
				for(int i=0;i<listPolicyAlteration.size();i++) {
					id_endors = listPolicyAlteration.get(i).getId_endors();
					flag_direct = listPolicyAlteration.get(i).getFlag_direct();
					policyAlterationNew = listPolicyAlteration.get(i).getPolicyholder_new();
					policyAlterationOld = listPolicyAlteration.get(i).getPolicyholder_old();
					lsje_id = id_endors;
					
					Endorse endors = services.selectListJenisEndors(lsje_id);
					String msen_alasan = endors.getLsje_jenis();
					String msde_old1 = null, msde_old2 = null, msde_old3 = null, msde_old4 = null, msde_old5 = null, msde_old6 = null,
					msde_new1 = null, msde_new2 = null, msde_new3 = null, msde_new4 = null, msde_new5 = null, msde_new6 = null;
					/*
					if(id_endors==61) {
						String mcl_id_pp = services.selectMclId_PP(reg_spaj);
						PolicyAlteration policyAlteration = new PolicyAlteration();
						
						Integer agama = policyAlterationOld.getAgama();
						Integer agama_new = policyAlterationNew.getAgama();
						Integer lsag_id = null;
						
						msde_old1 = "asd";
						msde_new1 = "asd";
						
						if(agama_new.equals("ISLAM")) {
							lsag_id = 1;
						} else if(agama_new.equals("KRISTEN PROTESTAN")) {
							lsag_id = 2;
						} else if(agama_new.equals("KRISTEN KATOLIK")) {
							lsag_id = 3;
						} else if(agama_new.equals("BUDHA")) {
							lsag_id = 4;
						} else if(agama_new.equals("HINDU")) {
							lsag_id = 5;
						} else if(agama_new.equals("[NON]")) {
							lsag_id = 0;
						} else if(agama_new.equals("LAIN - LAIN")) {
							lsag_id = 6;
						} 
						
						policyAlteration.setLsag_id(lsag_id);
						policyAlteration.setMcl_id(mcl_id_pp);
						
						//UPDATE AGAMA
						services.updateAgama(policyAlteration);
					} else if(id_endors==34) {
						String mcl_id_pp = services.selectMclId_PP(reg_spaj);
						PolicyAlteration policyAlteration = new PolicyAlteration();
						
						String alamat_kantor = policyAlterationOld.getAlamat_kantor();
						String alamat_kantor_new = policyAlterationNew.getAlamat_kantor();
						
						msde_old1 = alamat_kantor;
						msde_new1 = alamat_kantor_new;
						
						policyAlteration.setMcl_id(mcl_id_pp);
						policyAlteration.setAlamat_kantor(alamat_kantor_new);
						
						//UPDATE ALAMAT KANTOR
						services.updateAlamatKantor(policyAlteration);
					} else if(id_endors==39) {
						String mcl_id_pp = services.selectMclId_PP(reg_spaj);
						PolicyAlteration policyAlteration = new PolicyAlteration();
						
						Integer kewarganegaraan = policyAlterationOld.getKewarganegaraan_pp();
						Integer kewarganegaraan_new = policyAlterationNew.getKewarganegaraan_pp();
						Integer lsne_id_new = null;
						
						msde_old1 = "asd";
						msde_new1 = "asd";
						
						ArrayList<DropdownPolicyAlteration> getListNegara = services.selectListNegara();
						
						for (int x = 0; x < getListNegara.size(); x++) {
							
							Integer lsne_id = getListNegara.get(x).getLsne_id();
							String lsne_name = getListNegara.get(x).getLsne_note();
							
							if(lsne_name.equalsIgnoreCase("asd")) {
								lsne_id_new = lsne_id;
							}
						}
						
						policyAlteration.setMcl_id(mcl_id_pp);
						policyAlteration.setLsne_id(lsne_id_new);
						
						//UPDATE KEWARGANEGARAAN
						services.updateKewarganegaraan(policyAlteration);
					} else if(id_endors==67) {
						String mcl_id_pp = services.selectMclId_PP(reg_spaj);
						PolicyAlteration policyAlteration = new PolicyAlteration();
						
						Integer status = policyAlterationOld.getStatus();
						Integer status_new = policyAlterationNew.getStatus();
						Integer mspe_sts_mrt_new = null;
						
						msde_old1 = "asd";
						msde_new1 = "asd";
						
						ArrayList<DropdownPolicyAlteration> getListPernikahan = services.selectListPernikahan();
						
						for (int x = 0; x < getListPernikahan.size(); x++) {
							
							Integer lsst_id = getListPernikahan.get(x).getLsst_id();
							String lsst_name = getListPernikahan.get(x).getLsst_name();
							
							if(lsst_name.equalsIgnoreCase("asd")) {
								mspe_sts_mrt_new = lsst_id;
							}
						}
						
						policyAlteration.setMcl_id(mcl_id_pp);
						policyAlteration.setMspe_sts_mrt(mspe_sts_mrt_new);
						
						//UPDATE STATUS
						services.updateStatus(policyAlteration);
					} else if(id_endors==89) {
						String mcl_id_pp = services.selectMclId_PP(reg_spaj);
						PolicyAlteration policyAlteration = new PolicyAlteration();
						
						String nama_perusahaan_pp = null;
						String nama_perusahaan_pp_new = null;
						String tipe_usaha_pp = null;
						String tipe_usaha_pp_new = null;
						
						nama_perusahaan_pp = policyAlterationOld.getNama_perusahaan_pp();
						nama_perusahaan_pp_new = policyAlterationNew.getNama_perusahaan_pp();
						tipe_usaha_pp = policyAlterationOld.getTipe_usaha_pp();
						tipe_usaha_pp_new = policyAlterationNew.getTipe_usaha_pp();
						
						if((nama_perusahaan_pp!=null) && (tipe_usaha_pp==null)) {
							msde_old1 = nama_perusahaan_pp;
							msde_new1 = nama_perusahaan_pp_new;
						} else if((nama_perusahaan_pp!=null) && (tipe_usaha_pp!=null)) {
							msde_old1 = nama_perusahaan_pp;
							msde_old2 = tipe_usaha_pp;
							msde_new1 = nama_perusahaan_pp_new;
							msde_new2 = tipe_usaha_pp_new;
						} else if((nama_perusahaan_pp!=null) && (tipe_usaha_pp==null)) {
							msde_old1 = tipe_usaha_pp;
							msde_new1 = tipe_usaha_pp_new;
						}
						
						policyAlteration.setMcl_id(mcl_id_pp);
						policyAlteration.setNama_perusahaan_pp(nama_perusahaan_pp_new);
						policyAlteration.setTipe_usaha_pp(tipe_usaha_pp_new);
						
						//UPDATE STATUS
						services.updateJenisPekerjaan(policyAlteration);
					} 
					
					if(flag_direct==1) {	
						Integer lspd_id = 99;
						
						// Get MSEN_ENDORSE_NO
						String msen_endors_no = services.selectGetNoEndors();
						
						//INSERT ENDORSE
						services.insertEndorse(msen_endors_no, reg_spaj, msen_alasan, lspd_id);
						
						//INSERT DET ENDORSE
						services.insertDetailEndorse(msen_endors_no, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
								msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						
						//INSERT LST ULANGAN
						services.insertLstUlangan(reg_spaj, msen_alasan);
					} else {
						Integer lspd_id = 13;
						
						// Get MSEN_ENDORSE_NO
						String msen_endors_no = services.selectGetNoEndors();
						
						//INSERT ENDORSE
						services.insertEndorse(msen_endors_no, reg_spaj, msen_alasan, lspd_id);
						
						//INSERT DET ENDORSE
						services.insertDetailEndorse(msen_endors_no, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
								msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6);
						
						//UPDATE LSPD_ID IN MSPO_POLICY
						services.updateLspdId(reg_spaj);
					}
				}*/
				
				error = false;
				message = "Successfully submit policy alteration";
			} else {
				// Handle username & key not match
				error = true;
				message = "Failed submit policy alteration";
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
	
	@RequestMapping(value = "/listpolicyalteration", produces = "application/json", method = RequestMethod.POST)
	public String listPolicyAlteration(@RequestBody RequestViewPolicyAlteration requestViewPolicyAlteration,
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
		ArrayList<HashMap<String, Object>> listPolAlt = new ArrayList<>();

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
					ArrayList<Endorse> listPolicyAlteration = services.selectListPolicyAlteration(reg_spaj);
					
					if (listPolicyAlteration==null) {
						// Data List Kosong
						error = false;
						message = "Data list premium holiday empty";
					} else {
						for(int i = 0; i<listPolicyAlteration.size();i++) {
							HashMap<String, Object> dataTemp = new HashMap<>();
							
							String msen_endors_no = listPolicyAlteration.get(i).getMsen_endors_no();
							Integer lsje_id = listPolicyAlteration.get(i).getLsje_id();
							String input_date = listPolicyAlteration.get(i).getInput_date();
							String status = listPolicyAlteration.get(i).getStatus();
							String lsje_status = listPolicyAlteration.get(i).getLsje_status();
							String grouping = listPolicyAlteration.get(i).getGrouping();
							
							dataTemp.put("msen_endors_no", msen_endors_no);
							dataTemp.put("lsje_id", lsje_id);
							dataTemp.put("input_date", input_date);
							dataTemp.put("status", status);
							dataTemp.put("lsje_status", lsje_status);
							dataTemp.put("grouping", grouping);
							
							listPolAlt.add(dataTemp);
						}
						data.put("list_policy_alteration", listPolAlt);
						error = false;
						message = "Successfully get data";
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
	
	@RequestMapping(value = "/listreporthr", produces = "application/json", method = RequestMethod.POST)
	public String listRerportHr(@RequestBody RequestReportHr requestReportHr,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestReportHr);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		ArrayList<HashMap<String, Object>> listsReportHr = new ArrayList<>();

		String username = requestReportHr.getUsername();
		String key = requestReportHr.getKey();
		String no_polis = requestReportHr.getNo_polis();
		String no_batch = requestReportHr.getNo_batch();
		String tgl_input = requestReportHr.getTgl_input();
		Integer pageNumber = requestReportHr.getPageNumber();
		Integer pageSize = requestReportHr.getPageSize();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<ReportHr> listReportHr = services.selectListReportHr(no_polis, no_batch, tgl_input, pageNumber, pageSize);
				
				if (listReportHr.size()==0) {
					// Data List Kosong
					data = null;
					error = false;
					message = "Data list report hr empty";
				} else {
					Integer count = services.selectCountReportHr(no_polis);
					String path = null;
					String path_check = null;
					String path_display = null;
					String filename = null;
					
					for(int i = 0; i<listReportHr.size();i++) {
						HashMap<String, Object> dataTemp = new HashMap<>();

						String no_batch_ = listReportHr.get(i).getNo_batch();
						String tipe_batch = listReportHr.get(i).getTipe_batch();
						String tgl_input_ = listReportHr.get(i).getTgl_input();
						String tgl_terima = listReportHr.get(i).getTgl_terima();
						String tgl_bayar_ = listReportHr.get(i).getTgl_bayar();
						String mspo_policy_no = listReportHr.get(i).getMspo_policy_no();
						String lspd_position = listReportHr.get(i).getLspd_position();
						String mbc_kwitansi = listReportHr.get(i).getMbc_kwitansi();
						String group_claim = listReportHr.get(i).getGroup_claim();
						String tgl_input_format = listReportHr.get(i).getTgl_input_format();
						
						path_check = storageMpolicyDB + "Ekamedicare" + "\\" + tgl_input_format + "\\" + no_batch;
						path_display = storageMpolicyDB + "Ekamedicare" + "\\" + tgl_input_format + "\\" + no_batch;
						System.out.println(path_check);
						System.out.println(path_display);
						
						File dir = new File(path_check);
					      FilenameFilter filter = new FilenameFilter() {
					         public boolean accept (File dir, String name) { 
					            return name.startsWith(group_claim);
					         } 
					      }; 
					      String[] children = dir.list(filter);
					      if (children == null) {
					         //System.out.println("Either dir does not exist or is not a directory");
					         filename = null;					         
					      } else { 
					         for (int j = 0; j< children.length; j++) {
					            filename = children[j];
					            //System.out.println(filename);
					         } 
					      }
						
					    if (filename!=null) {
					    	path = path_display + "\\" + filename;
					    } else {
					    	path = null;
					    }
					    
					    //System.out.println(path);
						dataTemp.put("no_batch", no_batch_);
						dataTemp.put("tipe_batch", tipe_batch);
						dataTemp.put("tgl_input", tgl_input_);
						dataTemp.put("tgl_terima", tgl_terima);
						dataTemp.put("tgl_bayar", tgl_bayar_);
						dataTemp.put("mspo_policy_no", mspo_policy_no);
						dataTemp.put("lspd_position", lspd_position);
						dataTemp.put("mbc_kwitansi", mbc_kwitansi);	
						dataTemp.put("path", path);
						
						listsReportHr.add(dataTemp);
					}
					data.put("list_report_batch", listsReportHr);
					data.put("count", count);
					error = false;
					message = "Successfully get data";
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
	
	@RequestMapping(value = "/downloadreporthr", produces = "application/json", method = RequestMethod.POST)
	public String downloadReportHr(@RequestBody RequestDownloadReportHr requestDownloadReportHr, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String res = null;
		String message = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		try {
			// path file
			String pathWS = requestDownloadReportHr.getPath();
			String tempPathWS = pathWS.replace("\\", "/");
			tempPathWS = tempPathWS.replace("//", "/");
			String tempPath[] = tempPathWS.split("/");
			String tgl_input = tempPath[5].toString();
			String no_batch = tempPath[6].toString();
			String file_name = tempPath[7].toString();
			String NewPathWS = pathDownloadReportHr + File.separator + tgl_input + File.separator + no_batch + File.separator + file_name;
			String file_type = "pdf";

			// path file yang mau di download
			File file = new File(NewPathWS);

			try {
				// Content-Disposition
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=" + file_name.replace("  ", "_").replace(" ", "_") + "." + file_type);

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
				logger.error("Path: " + request.getServletPath() + " Error: " + e);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			logger.error("Path: " + request.getServletPath() + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);

		return res;
	}
	
	@RequestMapping(value = "/downloadkwitansi", produces = "application/json", method = RequestMethod.POST)
	public String downloadKwitansi(@RequestBody RequestDownloadKwitansi requestDownloadKwitansi, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String res = null;
		String message = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		try {
			// path file
			String pathWS = requestDownloadKwitansi.getPath();
			String tempPathWS = pathWS.replace("\\", "/");
			tempPathWS = tempPathWS.replace("//", "/");
			String tempPath[] = tempPathWS.split("/");
			String tgl_input = tempPath[5].toString();
			String no_batch = tempPath[6].toString();
			String file_name = tempPath[8].toString();
			String NewPathWS = pathDownloadReportHr + File.separator + tgl_input + File.separator + no_batch + File.separator + "Kwitansi" + File.separator + file_name;
			String file_type = "pdf";

			// path file yang mau di download
			File file = new File(NewPathWS);

			try {
				// Content-Disposition
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=" + file_name.replace("  ", "_").replace(" ", "_") + "." + file_type);

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
				logger.error("Path: " + request.getServletPath() + " Error: " + e);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			logger.error("Path: " + request.getServletPath() + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		map.put("data", data);
		res = gson.toJson(map);

		return res;
	}
}