package com.app.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
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
import com.app.model.Article;
import com.app.model.Beneficiary;
import com.app.model.DropdownPolicyAlteration;
import com.app.model.Endorse;
import com.app.model.Inbox;
import com.app.model.LstUserSimultaneous;
import com.app.model.Nav;
import com.app.model.NotifToken;
import com.app.model.Pemegang;
import com.app.model.PolicyAlteration;
import com.app.model.Provider;
import com.app.model.SubmitPolicyAlteration;
import com.app.model.SwitchingRedirection;
import com.app.model.User;
import com.app.model.UserCorporate;
import com.app.model.VersionCode;
import com.app.model.request.RequestBanner;
import com.app.model.request.RequestCountInboxUnread;
import com.app.model.request.RequestDeleteAllInbox;
import com.app.model.request.RequestDownloadArticle;
import com.app.model.request.RequestFurtherClaimSubmission;
import com.app.model.request.RequestInbox;
import com.app.model.request.RequestListArticle;
import com.app.model.request.RequestListNAB;
import com.app.model.request.RequestListPolis;
import com.app.model.request.RequestNabchart;
import com.app.model.request.RequestReadAllInbox;
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

@RestController
public class PolicyIndividualCorporateController {

	private static final Logger logger = LogManager.getLogger(PolicyIndividualCorporateController.class);

	@Value("${path.news.mpolicy}")
	private String pathNewsMpolicy;

	@Value("${path.download.banner}")
	private String pathDownloadBanner;
	
	@Value("${path.download.article}")
	private String pathDownloadArticle;

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
	public String sendOTP(@RequestBody RequestSaveToken requestSaveToken, HttpServletRequest request) throws Exception {
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
			if (notifToken != null) {
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
		ArrayList<Object> listsBeneficiary = new ArrayList<>();
		HashMap<String, Object> data = new HashMap<>();
		HashMap<String, Object> data_payor = new HashMap<>();
		HashMap<String, Object> data_insured = new HashMap<>();
		HashMap<Object, Object> data_policyholder = new HashMap<>();
		HashMap<String, Object> data_korespondensi = new HashMap<>();
		
		ArrayList<Object> array_policyholder = new ArrayList<>();

		String username = requestViewPolicyAlteration.getUsername();
		String key = requestViewPolicyAlteration.getKey();
		String no_polis = requestViewPolicyAlteration.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				
				HashMap<String, Object> temp_data_payor = new HashMap<>();
				HashMap<String, Object> temp_data_insured = new HashMap<>();
				HashMap<String, Object> temp_data_korespondensi = new HashMap<>();

				
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
				
				HashMap<String, Object> id_3 = new HashMap<>();
				id_3.put("id_endors", 3);
				id_3.put ("flag_direct", 0);
				id_3.put("alamat_tpt_tinggal", alamat_tpt_tinggal);
				
				HashMap<String, Object> _alamat_rumah_pp = new HashMap<>();
				HashMap<String, Object> temp_alamat_rumah_pp = new HashMap<>();
				_alamat_rumah_pp.put("value", alamat_rumah_pp);
				_alamat_rumah_pp.put("id_endors", 3);
				_alamat_rumah_pp.put ("flag_direct", 0);
				temp_alamat_rumah_pp.put("alamat_rumah_pp", _alamat_rumah_pp);
				array_policyholder.add(temp_alamat_rumah_pp);
				
				HashMap<String, Object> _propinsi_rumah = new HashMap<>();
				HashMap<String, Object> temp_propinsi_rumah = new HashMap<>();
				_propinsi_rumah.put("value", propinsi_rumah);
				_propinsi_rumah.put("id_endors", 3);
				_propinsi_rumah.put ("flag_direct", 0);
				temp_propinsi_rumah.put("propinsi_rumah", _propinsi_rumah);
				array_policyholder.add(temp_propinsi_rumah);

				HashMap<String, Object> _kabupaten_rumah = new HashMap<>();
				HashMap<String, Object> temp_kabupaten_rumah = new HashMap<>();
				_kabupaten_rumah.put("value", kabupaten_rumah);
				_kabupaten_rumah.put("id_endors", 3);
				_kabupaten_rumah.put ("flag_direct", 0);
				temp_kabupaten_rumah.put("kabupaten_rumah", _kabupaten_rumah);
				array_policyholder.add(temp_kabupaten_rumah);

				HashMap<String, Object> _kecamatan_rumah = new HashMap<>();
				HashMap<String, Object> temp_kecamatan_rumah = new HashMap<>();
				_kecamatan_rumah.put("value", kecamatan_rumah);
				_kecamatan_rumah.put("id_endors", 3);
				_kecamatan_rumah.put ("flag_direct", 0);
				temp_kecamatan_rumah.put("kecamatan_rumah", _kecamatan_rumah);
				array_policyholder.add(temp_kecamatan_rumah);

				HashMap<String, Object> _kelurahan_rumah = new HashMap<>();
				HashMap<String, Object> temp_kelurahan_rumah = new HashMap<>();
				_kelurahan_rumah.put("value", kelurahan_rumah);
				_kelurahan_rumah.put("id_endors", 3);
				_kelurahan_rumah.put ("flag_direct", 0);
				temp_kelurahan_rumah.put("kelurahan_rumah", _kelurahan_rumah);
				array_policyholder.add(temp_kelurahan_rumah);
				
				HashMap<String, Object> _kodepos_rumah = new HashMap<>();
				HashMap<String, Object> temp_kodepos_rumah = new HashMap<>();
				_kodepos_rumah.put("value", kodepos_rumah);
				_kodepos_rumah.put("id_endors", 3);
				_kodepos_rumah.put ("flag_direct", 0);
				temp_kodepos_rumah.put("kodepos_rumah", _kodepos_rumah);
				array_policyholder.add(temp_kodepos_rumah);
				
				HashMap<String, Object> _alamat_tpt_tinggal = new HashMap<>();
				HashMap<String, Object> temp_alamat_tpt_tinggal = new HashMap<>();
				_alamat_tpt_tinggal.put("value", alamat_tpt_tinggal);
				_alamat_tpt_tinggal.put("id_endors", 3);
				_alamat_tpt_tinggal.put ("flag_direct", 0);
				temp_alamat_tpt_tinggal.put("alamat_tpt_tinggal", _alamat_tpt_tinggal);
				array_policyholder.add(temp_alamat_tpt_tinggal);
				
				HashMap<String, Object> _ = new HashMap<>();
				HashMap<String, Object> temp_ = new HashMap<>();
				_alamat_tpt_tinggal.put("value", alamat_tpt_tinggal);
				_alamat_tpt_tinggal.put("id_endors", 3);
				_alamat_tpt_tinggal.put ("flag_direct", 0);
				temp_alamat_tpt_tinggal.put("alamat_tpt_tinggal", _alamat_tpt_tinggal);
				array_policyholder.add(temp_alamat_tpt_tinggal);
				
				
				
				//GET INSURED
				PolicyAlteration insured = services.selectInsured(no_polis);
				String status_tt = insured.getStatus_tt();
				Integer agama_tt = insured.getAgama_tt();
				Integer kewarganegaraan_tt = insured.getKewarganegaraan_tt();
				String nama_perusahaan_tt = insured.getNama_perusahaan_tt();
				String jabatan_tt = insured.getJabatan_tt();
				String tipe_usaha_tt = insured.getTipe_usaha_tt();
				
				data_insured.put("status_tt", status_tt);
				data_insured.put("agama_tt", agama_tt);
				data_insured.put("kewarganegaraan_tt", kewarganegaraan_tt);
				data_insured.put("nama_perusahaan_tt", nama_perusahaan_tt);
				data_insured.put("jabatan_tt", jabatan_tt);
				data_insured.put("tipe_usaha_tt", tipe_usaha_tt);
				
				//GET PAYOR
				PolicyAlteration payor = services.selectPayor(no_polis);
				String cara_bayarString = payor.getCara_bayar();
				String nama_bank_payor = payor.getNama_bank_payor();
				String cabang_bank_payor = payor.getCabang_bank_payor();
				String kota_bank_payor = payor.getKota_bank_payor();
				String no_rekening_payor = payor.getNo_rekening_payor();
				String pemilik_rekening_payor = payor.getPemilik_rekening_payor();
				String masa_berlaku = payor.getMasa_berlaku();
				String hubungan_payor = payor.getHubungan_payor();
				String nama_payor = payor.getNama_payor();
				String nama_perusahaan = payor.getNama_perusahaan();
				String jabatan = payor.getJabatan();
				String alamat_rumah = payor.getAlamat_rumah();
				String negara = payor.getNegara();
				String propinsi = payor.getPropinsi();
				String kabupaten = payor.getKabupaten();
				String kecamatan = payor.getKecamatan();
				String kelurahan = payor.getKelurahan();
				String kodepos = payor.getKodepos();
				String area_code_rumah = payor.getArea_code_rumah();
				String telpon_rumah = payor.getTelpon_rumah();
				String no_hp = payor.getNo_hp();
				String tujuan = payor.getTujuan();
				String sumber_dana = payor.getSumber_dana();
				String mkl_kerja = payor.getMkl_kerja();
				String mkl_penghasilan = payor.getMkl_penghasilan();
				String mkl_smbr_penghasilan = payor.getMkl_smbr_penghasilan();
				
				data_payor.put("cara_bayarString", cara_bayarString);
				data_payor.put("nama_bank_payor", nama_bank_payor);
				data_payor.put("cabang_bank_payor", cabang_bank_payor);
				data_payor.put("kota_bank_payor", kota_bank_payor);
				data_payor.put("no_rekening_payor", no_rekening_payor);
				data_payor.put("pemilik_rekening_payor", pemilik_rekening_payor);
				data_payor.put("masa_berlaku", masa_berlaku);
				data_payor.put("hubungan_payor", hubungan_payor);
				data_payor.put("nama_payor", nama_payor);
				data_payor.put("nama_perusahaan", nama_perusahaan);
				data_payor.put("jabatan", jabatan);
				data_payor.put("alamat_rumah", alamat_rumah);
				data_payor.put("negara", negara);
				data_payor.put("propinsi", propinsi);
				data_payor.put("kabupaten", kabupaten);
				data_payor.put("kecamatan", kecamatan);
				data_payor.put("kelurahan", kelurahan);
				data_payor.put("kodepos", kodepos);
				data_payor.put("area_code_rumah", area_code_rumah);
				data_payor.put("telpon_rumah", telpon_rumah);
				data_payor.put("no_hp", no_hp);
				data_payor.put("tujuan", tujuan);
				data_payor.put("sumber_dana", sumber_dana);
				data_payor.put("mkl_kerja", mkl_kerja);
				data_payor.put("mkl_penghasilan", mkl_penghasilan);
				data_payor.put("mkl_smbr_penghasilan", mkl_smbr_penghasilan);
				
				//GET BENEFICIARY
				ArrayList<Beneficiary> beneficiary = services.selectListBeneficiary(no_polis);
				for (int y = 0; y < beneficiary.size(); y++) {
					HashMap<String, Object> listBeneficiary = new HashMap<>();
					String reg_spaj = beneficiary.get(y).getReg_spaj();
					Integer msaw_number = beneficiary.get(y).getMsaw_number();
				    String msaw_first = beneficiary.get(y).getMsaw_first();
				    String msaw_birth = beneficiary.get(y).getMsaw_birth();
				    String lsre_relation = beneficiary.get(y).getLsre_relation();
				    Integer msaw_persen = beneficiary.get(y).getMsaw_persen();
				    Integer msaw_sex = beneficiary.get(y).getMsaw_sex();

				    listBeneficiary.put("reg_spaj", reg_spaj);
				    listBeneficiary.put("msaw_number", msaw_number);
				    listBeneficiary.put("msaw_first", msaw_first);
				    listBeneficiary.put("msaw_birth", msaw_birth);
				    listBeneficiary.put("lsre_relation", lsre_relation);
				    listBeneficiary.put("msaw_persen", msaw_persen);
				    listBeneficiary.put("msaw_sex", msaw_sex);
				    listsBeneficiary.add(listBeneficiary);
				}
				
				//data.put("policyholder", data_policyholder);
				data.put("policyholder", array_policyholder);
				data.put("insured", data_insured);
				data.put("payor", data_payor);
				data.put("beneficiary",listsBeneficiary);
				
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
		ArrayList<SubmitPolicyAlteration> listPolicyAlteration = new ArrayList<>();
		PolicyAlteration policyAlterationNew = new PolicyAlteration();
		PolicyAlteration policyAlterationOld = new PolicyAlteration();
		
		Integer id_endors, flag_direct, lsje_id;
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get SPAJ
				Pemegang paramSelectSPAJ = new Pemegang();
				paramSelectSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);
				String reg_spaj = dataSPAJ.getReg_spaj();
				
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
				}
				
				/*
								
				
				
				*/
				
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
}