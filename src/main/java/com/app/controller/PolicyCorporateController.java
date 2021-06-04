package com.app.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.services.VegaServices;
import com.app.model.Beneficiary;
import com.app.model.BenefitCorporate;
import com.app.model.ClaimCorporate;
import com.app.model.DetailClaimCorporate;
import com.app.model.EndorseHr;
import com.app.model.Upload;
import com.app.model.request.RequestBenefitCorporate;
import com.app.model.request.RequestDetailClaimCorporate;
import com.app.model.request.RequestDownloadEndorseHr;
import com.app.model.request.RequestDownloadReportHr;
import com.app.model.request.RequestListClaimCorporate;
import com.app.model.request.RequestListEndorseHr;
import com.app.model.request.RequestPrepareEndorseHr;
import com.app.model.request.RequestSubmitClaimSubmissionCorporate;
import com.app.model.request.RequestSubmitEndorseHr;
import com.app.model.request.RequestViewEndorseHr;
import com.app.utils.VegaCustomResourceLoader;
import com.app.utils.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class PolicyCorporateController {

	private static final Logger logger = LogManager.getLogger(PolicyCorporateController.class);

	@Autowired
	private VegaServices services;

	@Autowired
	private VegaCustomResourceLoader customResourceLoader;

	private DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
	private DateFormat df3 = new SimpleDateFormat("dd MMM yyyy");
	private NumberFormat nfZeroTwo = new DecimalFormat("#,##0.00;(#,##0.00)");
	
	@Value("${path.storage.mpolicydb}")
	private String storageMpolicyDB;
	
	@Value("${path.storage.mpolicy}")
	private String storageMpolicy;
	
	@Value("${path.storage.reporthr}")
	private String storageReportHr;
	
	@Value("${path.download.endorsehr}")
	private String downloadEndorseHr;

	@RequestMapping(value = "/listclaimcorporate", produces = "application/json", method = RequestMethod.POST)
	public String listClaimCorporate(@RequestBody RequestListClaimCorporate requestListClaimCorporate,
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

	@RequestMapping(value = "/detailclaimcorporate", produces = "application/json", method = RequestMethod.POST)
	public String detailClaimCorporate(@RequestBody RequestDetailClaimCorporate requestDetailClaimCorporate,
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
				String path = null;

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
						
						if(mce_klaim_admedika!=null) {
							path = storageMpolicyDB + "Ekamedicare" + "\\"  + tgl_input + "\\" + mbc_no +
									"\\" + "Kwitansi" + "\\" + mce_klaim_admedika + ".pdf";
						} else {
							path = null;
						}

						HashMap<String, Object> dataTemp = new HashMap<>();
						dataTemp.put("detail_claim", detail_claim);
						dataTemp.put("jml_claim", nfZeroTwo.format(jml_claim));
						dataTemp.put("jml_dibayar", nfZeroTwo.format(jml_dibayar));

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
					
					data.put("path", path);
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

	@RequestMapping(value = "/listbenefitcorporate", produces = "application/json", method = RequestMethod.POST)
	public String listBenefitCorporate(@RequestBody RequestBenefitCorporate requestBenefitCorporate,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestBenefitCorporate);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String username = requestBenefitCorporate.getUsername();
		String key = requestBenefitCorporate.getKey();
		String mste_insured = requestBenefitCorporate.getMste_insured();
		String reg_spaj = requestBenefitCorporate.getReg_spaj();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<BenefitCorporate> dataListBenefitCorporate = services.selectListBenefitCorporate(reg_spaj,
						mste_insured);

				if (dataListBenefitCorporate.isEmpty()) {
					// Handle List Benefit Corporate empty
					error = true;
					message = "List benefit corporate empty";
					resultErr = "List benefit corporate kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + ", Error: " + resultErr);
				} else {
					List<String> listPlan = new ArrayList<String>();
					for (int i = 0; i < dataListBenefitCorporate.size(); i++) {
						String distinctPlan = dataListBenefitCorporate.get(i).getNm_plan();
						listPlan.add(distinctPlan);
					}

					List<String> distinctPlan = listPlan.stream().distinct().collect(Collectors.toList());
					for (int x = 0; x < distinctPlan.size(); x++) {
						HashMap<String, Object> dataTemp = new HashMap<>();
						String nm_plan = distinctPlan.get(x);
						dataTemp.put("nm_plan", nm_plan);
						ArrayList<HashMap<String, Object>> detailsPlan = new ArrayList<>();
						for (int y = 0; y < dataListBenefitCorporate.size(); y++) {
							if (dataListBenefitCorporate.get(y).getNm_plan().equals(distinctPlan.get(x))) {
								HashMap<String, Object> dataPlanDetails = new HashMap<>();
								String jenis_jaminan = dataListBenefitCorporate.get(y).getFasilitas();
								String status_benefit = dataListBenefitCorporate.get(y).getReimburse();
								BigDecimal batas_benefit = dataListBenefitCorporate.get(y).getNaik_kelas();
								BigDecimal batas_benefit_tahunan = dataListBenefitCorporate.get(y)
										.getBatas_benefit_tahunan();
								BigDecimal limit_benefit = dataListBenefitCorporate.get(y).getLimit_benefit();
								BigDecimal jml_pemakaian = dataListBenefitCorporate.get(y).getJml_pemakaian();
								BigDecimal sisa_limit = dataListBenefitCorporate.get(y).getSisa_limit();

								String statusBenefitResult = null;

								if (status_benefit.equals("As Charged")) {
									statusBenefitResult = "As Charged";
								} else if (status_benefit.contains("%")) {
									statusBenefitResult = status_benefit;
								} else {
									statusBenefitResult = customResourceLoader.currencyRupiah(status_benefit);
								}

								dataPlanDetails.put("jenis_jaminan", jenis_jaminan);
								dataPlanDetails.put("status_benefit", statusBenefitResult);
								dataPlanDetails.put("batas_benefit", batas_benefit);
								dataPlanDetails.put("batas_benefit_tahunan", batas_benefit_tahunan);
								dataPlanDetails.put("limit_benefit", limit_benefit);
								dataPlanDetails.put("jml_pemakaian", jml_pemakaian);
								dataPlanDetails.put("sisa_limit", sisa_limit);

								detailsPlan.add(dataPlanDetails);
							}
						}

						dataTemp.put("details", detailsPlan);
						data.add(dataTemp);
					}

					error = false;
					message = "Successfully get list benefit corporate";
				}
			} else {
				error = true;
				message = "Failed get list benefit corporate";
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
		customResourceLoader.insertHistActivityWS(12, 72, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/submitendorsehr", produces = "application/json", method = RequestMethod.POST)
	public String submitendorsehr(@RequestBody RequestSubmitEndorseHr requestSubmitEndorseHr, HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestSubmitEndorseHr);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestSubmitEndorseHr.getUsername();
		String key = requestSubmitEndorseHr.getKey();
		String no_polis = requestSubmitEndorseHr.getNo_polis();
		Integer jenis_helpdesk = requestSubmitEndorseHr.getJenis_helpdesk();
		String subject = requestSubmitEndorseHr.getSubject();
		String description = requestSubmitEndorseHr.getDescription();
		ArrayList <Upload> upload = requestSubmitEndorseHr.getUpload();
		String pathFolder = null, directory = null;
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				
				try {
					// Get MSEN_ENDORSE_NO
					String id_ticket = services.selectGetIdTicket();
					Integer id_group = jenis_helpdesk;
					String nik_req = no_polis;
					subject = "Endorsement - " + subject;
					
					/*
					 	HARDCODE JENIS HELPDESK
						PERUBAHAN NAMA TERTANGGUNG (POLIS BERJALAN)	290
						PERUBAHAN PLAN (POLIS BERJALAN)				291
						PENAMBAHAN PESERTA (POLIS BERJALAN)			257
						PENGURANGAN PESERTA (POLIS BERJALAN)		258
						CEK ULANG KARTU								298
						FOLLOW UP CUSTOMER EB						324
					*/
					
					// Insert to hrd.hd_tickets
					services.insertSubmitEndorseHr(id_ticket, id_group, nik_req, subject, description);
					
					for(int i=0; i<upload.size(); i++) {
						String attachment = upload.get(i).getAttachment();
						String filename = upload.get(i).getFilename();
						String extension = upload.get(i).getExtension();
						
						pathFolder = downloadEndorseHr + File.separator + "EB Endorse" + File.separator + id_ticket;
						
						File folder = new File(pathFolder);
						if (!folder.exists()) {
							folder.mkdirs();
						}
					
						try {
							byte[] fileByte = Base64.getDecoder().decode(attachment);
							directory = pathFolder + File.separator + filename + "." + extension;
					
							FileOutputStream fos = new FileOutputStream(directory);
							fos.write(fileByte);
							fos.close();
							fos.flush();
						} catch (Exception e) {
							error = true;
							message = ResponseMessage.ERROR_SYSTEM;
							resultErr = "bad exception " + e;
							logger.error("Path: " + request.getServletPath() + " Username: " + username + ", Error: " + e);
						}
					}
				} catch (Exception e) {
					error = true;
					message = ResponseMessage.ERROR_SYSTEM;
					resultErr = "bad exception " + e;
					logger.error("Path: " + request.getServletPath() + " Username: " + username + ", Error: " + e);
				}
				
				error = false;
				message = "Successfully submit claim corporate";
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
		
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 79, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/listendorsehr", produces = "application/json", method = RequestMethod.POST)
	public String listEndorseHr(@RequestBody RequestListEndorseHr requestListEndorseHr,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestListEndorseHr);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<>();

		String username = requestListEndorseHr.getUsername();
		String key = requestListEndorseHr.getKey();
		String no_polis = requestListEndorseHr.getNo_polis();
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<EndorseHr> dataEndorseHr = services.selectListEndorseHr(no_polis);
				// Check List Claim empty or not
				if (dataEndorseHr.isEmpty()) {
					// Handle empty list claim
					error = false;
					message = "List endorse hr is empty";
				} else {
					for (int i = 0; i < dataEndorseHr.size(); i++) {
						String id_ticket = dataEndorseHr.get(i).getId_ticket();
						String subject = dataEndorseHr.get(i).getSubject();
						String create_date = dataEndorseHr.get(i).getCreate_date();
						String status = dataEndorseHr.get(i).getStatus();
						
						subject = subject.replace("Endorsement - ", "");

						HashMap<String, Object> dataTemp = new HashMap<>();
						dataTemp.put("id_ticket", id_ticket);
						dataTemp.put("subject", subject);
						dataTemp.put("create_date", create_date);
						dataTemp.put("status", status);

						data.add(dataTemp);
					}

					error = false;
					message = "Successfully get list endorse hr";
				}
			} else {
				error = true;
				message = "Failed get list endorse hr";
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
	
	@RequestMapping(value = "/viewendorsehr", produces = "application/json", method = RequestMethod.POST)
	public String viewEndorseHr(@RequestBody RequestViewEndorseHr requestViewEndorseHr,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewEndorseHr);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestViewEndorseHr.getUsername();
		String key = requestViewEndorseHr.getKey();
		String id_ticket = requestViewEndorseHr.getId_ticket();
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				EndorseHr endorseHr = services.selectViewEndorseHr(id_ticket);

				String no_polis = endorseHr.getNo_polis();
		        String nama_perusahaan = endorseHr.getNama_perusahaan();
		        Integer type_helpdesk = endorseHr.getType_helpdesk();
		        String subject = endorseHr.getSubject();
		        String description = endorseHr.getDescription();
		        String nama_file = null;
		        String file_type = null;
		        String path = null;
		        
		        String path_check = downloadEndorseHr + File.separator + "EB Endorse" + File.separator + id_ticket;
		        String path_display = storageMpolicyDB + "EB Endorse" + "\\" + id_ticket;
				
		        //cari file xls
				File dir = new File(path_check);
			      FilenameFilter filter = new FilenameFilter() {
			         public boolean accept (File dir, String name) { 
			            return name.endsWith("xls");
			         }
			      };
			      
			      String[] children = dir.list(filter);
			      if (children == null) {
			         //System.out.println("Either dir does not exist or is not a directory");
			    	  nama_file = null;					         
			      } else { 
			         for (int j = 0; j< children.length; j++) {
			        	 nama_file = children[j];
			        	 file_type = "xls";
			            //System.out.println(filename);
			         } 
			      }
			    
			    //cari file xlsx
			    if (nama_file == null) {
			    	File dir2 = new File(path_check);
				      FilenameFilter filter2 = new FilenameFilter() {
				         public boolean accept (File dir, String name) { 
				            return name.endsWith("xlsx");
				         }
				      };
				      
				      String[] children2 = dir2.list(filter2);
				      if (children2 == null) {
				         //System.out.println("Either dir2 does not exist or is not a directory");
				    	  nama_file = null;					         
				      } else { 
				         for (int j = 0; j< children2.length; j++) {
				        	 nama_file = children2[j];
				        	 file_type = "xlsx";
				            //System.out.println(filename);
				         } 
				      }
			    }
			    
			    if (nama_file!=null) {
			    	path = path_display + "\\" + nama_file;
			    } else {
			    	path = null;
			    }
			    
				data.put("no_polis", no_polis);
				data.put("nama_perusahaan", nama_perusahaan);
				data.put("type_helpdesk", type_helpdesk);
				data.put("subject", subject);
				data.put("description", description);
				data.put("nama_file", nama_file);
				data.put("path", path);
				data.put("file_type", file_type);

				error = false;
				message = "Successfully get endorse hr";
			} else {
				error = true;
				message = "Failed get list endorse hr";
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
	
	@RequestMapping(value = "/downloadendorsehr", produces = "application/json", method = RequestMethod.POST)
	public String downloadEndorseHr(@RequestBody RequestDownloadEndorseHr requestDownloadEndorseHr, 
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
			String pathWS = requestDownloadEndorseHr.getPath();
			String tempPathWS = pathWS.replace("\\", "/");
			tempPathWS = tempPathWS.replace("//", "/");
			String tempPath[] = tempPathWS.split("/");
			String id_ticket = tempPath[5].toString();
			String file_name = tempPath[6].toString();
			String newfilename = file_name.substring(0, file_name.lastIndexOf('.'));
			String NewPathWS = downloadEndorseHr + File.separator + "EB Endorse" + File.separator + id_ticket + File.separator + file_name;
			String file_type = requestDownloadEndorseHr.getFile_type();
			
			/*\\\\storage.sinarmasmsiglife.co.id\\pdfind\\m-Policytest\\EB Endorse\\148039\\tes.xls*/
			
			// path file yang mau di download
			File file = new File(NewPathWS);

			try {
				// Content-Disposition
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=" + newfilename.replace("  ", "_").replace(" ", "_") + "." + file_type);

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
	
	@RequestMapping(value = "/prepareendorsehr", produces = "application/json", method = RequestMethod.POST)
	public String prepareEndorseHr(@RequestBody RequestPrepareEndorseHr requestPrepareEndorseHr,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestPrepareEndorseHr);
		String res = null;
		String resultErr = null;
		String message = null;
		boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestPrepareEndorseHr.getUsername();
		String key = requestPrepareEndorseHr.getKey();
		String no_polis = requestPrepareEndorseHr.getNo_polis();
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				EndorseHr endorseHr = services.selectPrepareEndorseHr(no_polis);

				String no_polis_get = endorseHr.getNo_polis();
		        String nama_perusahaan = endorseHr.getNama_perusahaan();
			    
				data.put("no_polis", no_polis_get);
				data.put("nama_perusahaan", nama_perusahaan);

				error = false;
				message = "Successfully get endorse hr";
			} else {
				error = true;
				message = "Failed get endorse hr";
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
}