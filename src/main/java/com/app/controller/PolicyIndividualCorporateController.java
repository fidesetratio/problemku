package com.app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.services.VegaServices;
import com.app.model.request.RequestBanner;
import com.app.utils.CustomResourceLoader;
import com.app.utils.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PolicyIndividualCorporateController {
	
	private static final Logger logger = LogManager.getLogger(PolicyIndividualCorporateController.class);

	@Value("${path.news.mpolicy}")
	private String pathNewsMpolicy;

	@Value("${path.download.banner}")
	private String pathDownloadBanner;

	@Autowired
	private VegaServices services;

	@Autowired
	private CustomResourceLoader customResourceLoader;

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
						String dir2 = dir + "\\" + showDir;
						Set<String> listFiles = customResourceLoader.listFilesUsingJavaIO(dir2);
						if (!listFiles.isEmpty()) {
							HashMap<String, Object> data2 = new HashMap<>();
							for (String filename : listFiles) {
								String dir3 = dir2 + "\\" + filename;
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
							String dir2 = dir + "\\" + showDir;
							Set<String> listFiles = customResourceLoader.listFilesUsingJavaIO(dir2);
							if (!listFiles.isEmpty()) {
								HashMap<String, Object> data2 = new HashMap<>();
								for (String filename : listFiles) {
									String dir3 = dir2 + "\\" + filename;
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
}
