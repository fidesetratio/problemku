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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.services.VegaServices;
import com.app.model.Article;
import com.app.model.LstUserSimultaneous;
import com.app.model.Nav;
import com.app.model.Provider;
import com.app.model.User;
import com.app.model.UserCorporate;
import com.app.model.VersionCode;
import com.app.model.request.RequestBanner;
import com.app.model.request.RequestCountInboxUnread;
import com.app.model.request.RequestDeleteAllInbox;
import com.app.model.request.RequestDownloadArticle;
import com.app.model.request.RequestListArticle;
import com.app.model.request.RequestListNAB;
import com.app.model.request.RequestListPolis;
import com.app.model.request.RequestNabchart;
import com.app.model.request.RequestReadAllInbox;
import com.app.model.request.RequestUpdateInboxStatus;
import com.app.model.request.RequestVersionCode;
import com.app.model.request.RequestViewProvider;
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

	@Autowired
	private VegaServices services;

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

									mapper.put("policy_number", polis);
									mapper.put("policy_holder",
											dataTemp.getNm_pp() != null ? dataTemp.getNm_pp() : null);
									mapper.put("insured", dataTemp.getNm_tt() != null ? dataTemp.getNm_tt() : null);
									mapper.put("policy_status_label",
											dataTemp.getStatus() != null ? dataTemp.getStatus() : null);
									mapper.put("policy_status_id",
											dataTemp.getLms_id() != null ? dataTemp.getLms_id() : null);
									mapper.put("policy_type", gproudId.intValue());
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
					List<BigDecimal> distinctMspoTypeRek = listMspoTypeRek.stream().distinct()
							.collect(Collectors.toList());

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
			String file_name = requestDownloadArticle.getTitle();
			String file_type = requestDownloadArticle.getFile_type();

			// path file yang mau di download
			File file = new File(pathWS);

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
}