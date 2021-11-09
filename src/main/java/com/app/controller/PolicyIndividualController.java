package com.app.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.utils.PageUtils;
import org.apache.commons.lang.StringUtils;
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
import com.app.model.Billing;
import com.app.model.DataUsulan;
import com.app.model.KlaimKesehatan;
import com.app.model.PembayarPremi;
import com.app.model.Pemegang;
import com.app.model.PenerimaManfaat;
import com.app.model.PowerSave;
import com.app.model.ProductRider;
import com.app.model.Provinsi;
import com.app.model.Sales;
import com.app.model.StableLink;
import com.app.model.StableSave;
import com.app.model.Tertanggung;
import com.app.model.TertanggungTambahan;
import com.app.model.Topup;
import com.app.model.TrackingPolis;
import com.app.model.UnitLink;
import com.app.model.User;
import com.app.model.request.RequestBilling;
import com.app.model.request.RequestCurrentInvestasiTransaksi;
import com.app.model.request.RequestDataAsuransi;
import com.app.model.request.RequestDataAsuransiDownload;
import com.app.model.request.RequestDetailInvestasiTransaksi;
import com.app.model.request.RequestDetailStableLink;
import com.app.model.request.RequestDownloadPolisAll;
import com.app.model.request.RequestEmailCSMergeSimultan;
import com.app.model.request.RequestPembayarPremi;
import com.app.model.request.RequestPemegangPolis;
import com.app.model.request.RequestPenerimaManfaat;
import com.app.model.request.RequestProvinsi;
import com.app.model.request.RequestStatement;
import com.app.model.request.RequestStatementDownload;
import com.app.model.request.RequestStatusEmailCS;
import com.app.model.request.RequestTertanggung;
import com.app.model.request.RequestTrackingPolis;
import com.app.model.request.RequestUpdatePemegangPolis;
import com.app.model.request.RequestViewBeneficiary;
import com.app.model.request.RequestViewClaim;
import com.app.utils.VegaCustomResourceLoader;
import com.app.utils.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class PolicyIndividualController {

	private static final Logger logger = LogManager.getLogger(PolicyIndividualController.class);

	@Value("${path.storage.mpolicy}")
	private String storageMpolicy;
	
	@Value("${path.download.polisall}")
	private String pathDownloadPolisAll;

	@Value("${path.manfaatpdf.mpolicy}")
	private String manfaatpdfMpolicy;

	@Value("${path.logoBank.mpolicy}")
	private String pathLogoBankMpolicy;

	@Value("${path.pdf.faq}")
	private String pathPdfFAQ;

	@Value("${path.direct.notification}")
	private String pathDirectNotification;

	@Value("${link.update.activity}")
	private String linkUpdateActivity;

	@Value("${link.send.email}")
	private String linkSendEmail;

	@Autowired
	private VegaServices services;

	@Autowired
	private VegaCustomResourceLoader customResourceLoader;

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
	private DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
	private NumberFormat nfZeroTwo = new DecimalFormat("#,##0.00;(#,##0.00)");
	private NumberFormat nfZeroFour = new DecimalFormat("#,##0.0000;(#,##0.0000)");

	@RequestMapping(value = "/pemegangpolis", produces = "application/json", method = RequestMethod.POST)
	public String pemegangPolis(@RequestBody RequestPemegangPolis requestPemegangPolis, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String message = null;
		boolean error = true;
		String resultErr = null;
		String req = gson.toJson(requestPemegangPolis);
		String res = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestPemegangPolis.getUsername();
		String key = requestPemegangPolis.getKey();
		String no_polis = requestPemegangPolis.getNo_polis();
		
		String key_orion = key;
		if(key_orion.equalsIgnoreCase("orion")) {
			key = services.selectEncrypt(username);
		}
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang pemegang = new Pemegang();
				pemegang.setMspo_policy_no(no_polis);
				pemegang = services.selectPemegang(pemegang);
				if (pemegang != null) {
					// try catch karena data provinsi kadang double, cek di log aja ada
					// error apa ngga
					try {
						String viewProvinsi = customResourceLoader.viewAddress(pemegang.getLskl_id(),
								pemegang.getLskc_id(), pemegang.getLska_id(), pemegang.getLspr_id(),
								pemegang.getKd_pos_rumah());
						data.put("alamat_pp", pemegang.getAlamat_rumah() + viewProvinsi);
					} catch (Exception e) {
						logger.error("Path: " + request.getServletPath() + " Exception view provinsi, username: "
								+ username + " Error: " + e);
					}

					error = false;
					message = "Successfully get pemegang polis details";
					data.put("nama_pp", pemegang.getMcl_first());
					data.put("gender", pemegang.getMspe_sex2());
					data.put("dob",pemegang.getMspe_birth());
					data.put("agama", pemegang.getLsag_name());
					data.put("no_telepon", pemegang.getNo_hp() != null ? pemegang.getNo_hp() : pemegang.getNo_hp2());
					data.put("alamat", pemegang.getAlamat_rumah());
					data.put("email", pemegang.getEmail());
					data.put("bank", pemegang.getLsbp_nama());
					data.put("no_rekening",
							pemegang.getMrc_no_ac() != null ? customResourceLoader.clearData(pemegang.getMrc_no_ac())
									: null);
					data.put("tahun_lahir", pemegang.getYear_birth_date());
				} else {
					error = true;
					message = "Policy is not active";
					resultErr = "Data pemegang kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Policy is not active";
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
		customResourceLoader.insertHistActivityWS(12, 6, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/checksoftcopypolis", produces = "application/json", method = RequestMethod.POST)
	public String checkSoftCopyPolis(@RequestBody RequestDataAsuransi requestDataAsuransi, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String message = null;
		boolean error = true;
		String resultErr = null;
		String req = gson.toJson(requestDataAsuransi);
		String res = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestDataAsuransi.getUsername();
		String key = requestDataAsuransi.getKey();
		String no_polis = requestDataAsuransi.getNo_polis();

		String key_orion = key;
		if(key_orion.equalsIgnoreCase("orion")) {
			key = services.selectEncrypt(username);
		}
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang pemegang = new Pemegang();
				pemegang.setMspo_policy_no(no_polis);
				pemegang = services.selectPemegang(pemegang);
				if (pemegang != null) {
					DataUsulan dataUsulan = new DataUsulan();
					dataUsulan.setReg_spaj(pemegang.getReg_spaj());
					dataUsulan = services.selectDataUsulan(dataUsulan);
					
					String lca_id = dataUsulan.getLca_id();
					String reg_spaj = dataUsulan.getReg_spaj();
					
					String title = "softcopy";
					String file_type = "pdf";
					String file_name = title + "." + file_type;
					String file_path_check = pathDownloadPolisAll + File.separator + lca_id + File.separator + reg_spaj + File.separator
							+ file_name;
					String file_path = "\\\\storage\\pdfind\\Polis_Testing\\" + lca_id + "\\" + reg_spaj + "\\" + file_name;
						   
					File checkPolisAll = new File(file_path_check);
					if(checkPolisAll.exists() && !checkPolisAll.isDirectory()) { 
						data.put("file_path", file_path);
						data.put("title", title);
						data.put("file_type", file_type);
						data.put("is_softcopy_enable", true);
						
						error = false;
						message = "Successfully get soft copy polis info";
					} else {
						data.put("file_path", null);
						data.put("title", null);
						data.put("file_type", null);
						data.put("is_softcopy_enable", false);
						
						error = false;
						message = "Successfully get soft copy polis info";
					}
				} else {
					error = true;
					message = "Policy is not active";
					resultErr = "Data pemegang kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Policy is not active";
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
		customResourceLoader.insertHistActivityWS(12, 6, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/dataasuransi", produces = "application/json", method = RequestMethod.POST)
	public String dataAsuransi(@RequestBody RequestDataAsuransi requestDataAsuransi, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDataAsuransi);
		String res = null;
		String resultErr = null;
		Boolean error = true;
		String message = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		ArrayList<Object> product_rider = new ArrayList<>();

		String username = requestDataAsuransi.getUsername();
		String key = requestDataAsuransi.getKey();
		String no_polis = requestDataAsuransi.getNo_polis();
		
		String key_orion = key;
		if(key_orion.equalsIgnoreCase("orion")) {
			key = services.selectEncrypt(username);
		}
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang pemegang = new Pemegang();
				pemegang.setMspo_policy_no(no_polis);
				pemegang = services.selectPemegang(pemegang);
				if (pemegang != null) {
					DataUsulan dataUsulan = new DataUsulan();
					dataUsulan.setReg_spaj(pemegang.getReg_spaj());
					dataUsulan = services.selectDataUsulan(dataUsulan);
					String mspo_ao = null;
					Sales sales = new Sales();
					ArrayList<Object> fund = new ArrayList<>();
					
					if(key_orion.equalsIgnoreCase("orion")) {
						mspo_ao = username;
					} else {
						mspo_ao = dataUsulan.getMspo_ao();
					}
					
					String lca_id = dataUsulan.getLca_id();
					String reg_spaj = dataUsulan.getReg_spaj();
					
					String title = "polis_all";
					String file_type = "pdf";
					String file_name = title + "." + file_type;
					String file_path_check = pathDownloadPolisAll + File.separator + lca_id + File.separator + reg_spaj + File.separator
							+ file_name;
					String file_path = "\\\\storage\\pdfind\\Polis_Testing\\" + lca_id + "\\" + reg_spaj + "\\" + file_name;
					System.out.println(mspo_ao);
					HashMap<String, Object> dataSales = new HashMap<>();
					if (mspo_ao != null) {
						String msag_id = mspo_ao;
						sales = services.selectSales(msag_id);
						
						dataSales.put("nama_sales", sales.getMcl_first());
						dataSales.put("no_hp_sales", sales.getMsag_smart_no());
						dataSales.put("email_sales", sales.getMspe_email());
						dataSales.put("nama_leader", sales.getMcl_first_leader());
						dataSales.put("no_hp_leader", sales.getMsag_smart_no_leader());
						dataSales.put("email_leader", sales.getMspe_email_leader());
						dataSales.put("agent_code", sales.getAgent_code());
					} else {
						sales = null;
					}
					
					BigDecimal BD_uang_pertanggungan = new BigDecimal(dataUsulan.getMspr_tsi(), MathContext.DECIMAL64);
					
					data.put("produk", dataUsulan.getNewname());
					data.put("masa_berlaku_awal",
							dataUsulan.getMste_beg_date() == null ? null : df3.format(dataUsulan.getMste_beg_date()));
					data.put("masa_berlaku_akhir",
							dataUsulan.getMste_end_date() == null ? null : df3.format(dataUsulan.getMste_end_date()));
					data.put("mata_uang_premi", dataUsulan.getLku_symbol());
					data.put("premi_dasar", dataUsulan.getMspr_premium());
					data.put("premi_berkala", dataUsulan.getMu_jlh_tu() == null ? null : dataUsulan.getMu_jlh_tu());
					data.put("total_premi", dataUsulan.getMspr_premium()
							+ (dataUsulan.getMu_jlh_tu() == null ? 0 : dataUsulan.getMu_jlh_tu()));
					data.put("cuti_premi",
							(dataUsulan.getMspo_installment() == null ? null : (dataUsulan.getMspo_installment())));
					data.put("mata_uang_pertanggungan", dataUsulan.getLku_symbol());
					data.put("uang_pertanggungan", BD_uang_pertanggungan);
					data.put("frekuensi_bayar", dataUsulan.getLscb_pay_mode());
					data.put("cara_bayar", dataUsulan.getCara_bayar());
					data.put("lsbp_nama", dataUsulan.getLsbp_nama());
					data.put("expired_date_cc",
							dataUsulan.getMar_expired() == null ? null : df3.format(dataUsulan.getMar_expired()));
					data.put("commencement_date",
							dataUsulan.getMste_beg_date() == null ? null : df3.format(dataUsulan.getMste_beg_date()));
					data.put("last_premi",
							dataUsulan.getLast_premi() == null ? null : df3.format(dataUsulan.getLast_premi()));
					data.put("next_premi",
							dataUsulan.getNext_premi() == null ? null : df3.format(dataUsulan.getNext_premi()));
					data.put("status_polis", dataUsulan.getStatus());
					data.put("tertanggung", dataUsulan.getTertanggung());
					data.put("nama_bank", dataUsulan.getNama_bank());
					data.put("cabang_bank", dataUsulan.getCabang_bank());
					data.put("pemilik_rekening", dataUsulan.getPemilik_rekening());
					data.put("periode_premi_terhutang", dataUsulan.getPeriode_premi_terhutang());
					data.put("lama_pembayaran", dataUsulan.getLama_pembayaran());
					data.put("product_rider", product_rider);
					data.put("alokasi_dana", fund);
					data.put("data_sales", dataSales);
					
					File checkPolisAll = new File(file_path_check);
					if(checkPolisAll.exists() && !checkPolisAll.isDirectory()) { 
						data.put("file_path", file_path);
						data.put("title", title);
						data.put("file_type", file_type);
					} else {
						data.put("file_path", null);
						data.put("title", null);
						data.put("file_type", null);
					}
					
					String spaj = dataUsulan.getReg_spaj();
					ArrayList<ProductRider> dataRider = services.selectProductRider(spaj);
					ListIterator<ProductRider> liter = dataRider.listIterator();
					while (liter.hasNext()) {
						try {
							ProductRider m = liter.next();
							HashMap<String, Object> dataResult = new HashMap<>();
							String nama_rider = m.getNewname() != null ? m.getNewname() : null;
							String masa_berlaku_awal = m.getMspr_beg_date() != null ? m.getMspr_beg_date() : null;
							String masa_berlaku_akhir = m.getMspr_end_date() != null ? m.getMspr_end_date() : null;
							BigDecimal uang_pertanggungan = m.getMspr_tsi() != null ? m.getMspr_tsi() : null;
							String mata_uang = m.getLku_symbol() != null ? m.getLku_symbol() : null;
							Integer lsbs_id = m.getLsbs_id() != null ? m.getLsbs_id() : null;
							String tertanggung = m.getTertanggung() != null ? m.getTertanggung() : null;
							
							if(lsbs_id == 804) {
								nama_rider = nama_rider.replaceAll("\\s+","");
							}

							dataResult.put("tertanggung", tertanggung);
							dataResult.put("nama_rider", nama_rider);
							dataResult.put("masa_berlaku_awal", masa_berlaku_awal);
							dataResult.put("masa_berlaku_akhir", masa_berlaku_akhir);
							dataResult.put("uang_pertanggungan", uang_pertanggungan);
							dataResult.put("mata_uang", mata_uang);
							product_rider.add(dataResult);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}
					
					ArrayList<Topup> list = services.selectListInvestasi(no_polis);

					ListIterator<Topup> liter2 = list.listIterator();
					while (liter2.hasNext()) {
						try {
							Topup m = liter2.next();
							HashMap<String, Object> listFund = new HashMap<>();
							
							String lji_id = m.getLji_id();
							String name = m.getLji_invest();
							Float percentage = m.getMdu_persen();
							
							if (percentage != 0) {
								listFund.put("lji_id", lji_id);
								listFund.put("name", name);
								listFund.put("percentage", percentage.intValue());
								fund.add(listFund);
							}
						} catch (Exception e) {
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ e);
						}
					}
					
					error = false;
					message = "Successfully get data asuransi details";
					
				} else {
					error = true;
					message = "Policy is not active";
					resultErr = "Data pemegang kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
				
			} else {
				error = true;
				message = "Username not register";
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
		customResourceLoader.insertHistActivityWS(12, 7, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/downloadpolisall", produces = "application/json", method = RequestMethod.POST)
	public String downloadPolisAll(@RequestBody RequestDownloadPolisAll requestDownloadPolisAll, 
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
			String pathWS = requestDownloadPolisAll.getFile_path();
			String tempPathWS = pathWS.replace("\\", "/");
			tempPathWS = tempPathWS.replace("//", "/");
			String tempPath[] = tempPathWS.split("/");
			
			String cabang = tempPath[4].toString();
			String reg_spaj = tempPath[5].toString();
			String file_download = tempPath[6].toString();
			
			
			
					
			String NewPathWS = pathDownloadPolisAll + File.separator + cabang + File.separator + reg_spaj + File.separator + file_download;
			String file_name = requestDownloadPolisAll.getTitle();
			String file_type = requestDownloadPolisAll.getFile_type();

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

	@RequestMapping(value = "/tertanggung", produces = "application/json", method = RequestMethod.POST)
	public String tertanggung(@RequestBody RequestTertanggung requestTertanggung, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestTertanggung);
		String res = null;
		String resultErr = null;
		Boolean error = true;
		String message = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		ArrayList<Object> ahliWaris = new ArrayList<>();

		String username = requestTertanggung.getUsername();
		String key = requestTertanggung.getKey();
		String no_polis = requestTertanggung.getNo_polis();

		String key_orion = key;
		if(key_orion.equalsIgnoreCase("orion")) {
			key = services.selectEncrypt(username);
		}
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Tertanggung tertanggung = new Tertanggung();
				tertanggung.setMspo_policy_no(no_polis);
				tertanggung = services.selectTertanggung(tertanggung);
				if (tertanggung != null) {
					error = false;
					message = "Successfully get tertanggung data details";
					data.put("nama_ttg", tertanggung.getMcl_first());
					data.put("no_telepon_ttg",
							tertanggung.getNo_hp() != null ? tertanggung.getNo_hp() : tertanggung.getNo_hp2());

					// Pasangin try catch karena data provinsi kadang dobel2, cek di log aja ada
					// error apa ngga
					try {
						String viewProvinsi = customResourceLoader.viewAddress(tertanggung.getLskl_id(),
								tertanggung.getLskc_id(), tertanggung.getLska_id(), tertanggung.getLspr_id(),
								tertanggung.getKd_pos_rumah());

						data.put("alamat_rumah_ttg", tertanggung.getAlamat_rumah() + viewProvinsi);
					} catch (Exception e) {
						logger.error("Path: " + request.getServletPath() + " Exception view provinsi, username: "
								+ username + " Error: " + e);
					}

					data.put("email_ttg", tertanggung.getEmail());
					data.put("hubungan_pmg_ttg", tertanggung.getLsre_relation());
					data.put("tahun_lahir", tertanggung.getTahun_lahir());
					data.put("tertanggung_tambahan", ahliWaris);
					data.put("gender", tertanggung.getMspe_sex2());
					data.put("dob", tertanggung.getMspe_birth());
					String spaj = tertanggung.getReg_spaj();
					ArrayList<TertanggungTambahan> dataBenef = services.selectTertanggungTambahan(spaj);
					ListIterator<TertanggungTambahan> liter = dataBenef.listIterator();
					while (liter.hasNext()) {
						try {
							TertanggungTambahan m = liter.next();
							HashMap<String, Object> ditunjuk = new HashMap<>();
							String nama = m.getNama() != null ? m.getNama() : null;
							String relasi = m.getLsre_relation() != null ? m.getLsre_relation() : null;
							String dob = m.getTgl_lahir() != null ? m.getTgl_lahir() : null;
							String gender = m.getKelamin() != null ? m.getKelamin() : null;

							ditunjuk.put("nama", nama);
							ditunjuk.put("relasi", relasi);
							ditunjuk.put("tahun_lahir", dob);
							ditunjuk.put("gender", gender);
							ahliWaris.add(ditunjuk);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}
				} else {
					error = true;
					message = "Failed get tertanggung data details";
					resultErr = "Data tertanggung kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Failed get tertanggung data details";
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
		customResourceLoader.insertHistActivityWS(12, 8, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/pembayarpremi", produces = "application/json", method = RequestMethod.POST)
	public String pembayarPremi(@RequestBody RequestPembayarPremi requestPembayarPremi, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String message = null;
		boolean error = true;
		String resultErr = null;
		String req = gson.toJson(requestPembayarPremi);
		String res = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		HashMap<String, Object> pembayar_premi = new HashMap<>();

		String username = requestPembayarPremi.getUsername();
		String key = requestPembayarPremi.getKey();
		String no_polis = requestPembayarPremi.getNo_polis();

		String key_orion = key;
		if(key_orion.equalsIgnoreCase("orion")) {
			key = services.selectEncrypt(username);
		}
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				PembayarPremi pembayarPremi = services.selectPembayarPremi(no_polis);
				
				if (pembayarPremi!=null) {
					try {
						String nama = pembayarPremi.getNama() != null ? pembayarPremi.getNama() : null;
						String hubungan = pembayarPremi.getHubungan() != null ? pembayarPremi.getHubungan() : null;
						String dob = pembayarPremi.getDob() != null ? pembayarPremi.getDob() : null;
						String jenis_kelamin = pembayarPremi.getJenis_kelamin() != null ? pembayarPremi.getJenis_kelamin() : null;
						String alamat = pembayarPremi.getAlamat() != null ? pembayarPremi.getAlamat() : null;
						String no_hp = pembayarPremi.getNo_hp() != null ? pembayarPremi.getNo_hp() : null;
						String no_rekening = pembayarPremi.getNo_rekening() != null ? pembayarPremi.getNo_rekening() : null;
						String cara_bayar = pembayarPremi.getCara_bayar() != null ? pembayarPremi.getCara_bayar() : null;
						String nama_bank = pembayarPremi.getNama_bank() != null ? pembayarPremi.getNama_bank() : null;
						String no_cc = pembayarPremi.getNo_cc() != null ? pembayarPremi.getNo_cc() : null;

						pembayar_premi.put("nama", nama);
						pembayar_premi.put("hubungan", hubungan);
						pembayar_premi.put("tahun_lahir", dob);
						pembayar_premi.put("jenis_kelamin", jenis_kelamin);
						pembayar_premi.put("alamat", alamat);
						pembayar_premi.put("no_hp", no_hp);
						pembayar_premi.put("no_rekening", no_rekening);
						pembayar_premi.put("cara_bayar", cara_bayar);
						pembayar_premi.put("nama_bank", nama_bank);
						pembayar_premi.put("no_cc", no_cc);
					} catch (Exception e) {
						logger.error(
								"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
					}
					data.put("pembayar_premi", pembayar_premi);
					error = false;
					message = "Successfully get pembayar premi details";
				} else {
					error = false;
					data.put("pembayar_premi", null);
					message = "Premium Payer data is empty";
					resultErr = "Data pembayar premi kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Policy is not active";
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
		customResourceLoader.insertHistActivityWS(12, 6, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	@RequestMapping(value = "/penerimamanfaat", produces = "application/json", method = RequestMethod.POST)
	public String penerimaManfaat(@RequestBody RequestPenerimaManfaat requestPenerimaManfaat, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String message = null;
		boolean error = true;
		String resultErr = null;
		String req = gson.toJson(requestPenerimaManfaat);
		String res = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		ArrayList<Object> ahliWaris = new ArrayList<>();

		String username = requestPenerimaManfaat.getUsername();
		String key = requestPenerimaManfaat.getKey();
		String no_polis = requestPenerimaManfaat.getNo_polis();

		String key_orion = key;
		if(key_orion.equalsIgnoreCase("orion")) {
			key = services.selectEncrypt(username);
		}
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				ArrayList<PenerimaManfaat> dataBenef = services.selectPenerimaManfaat(no_polis);
				ListIterator<PenerimaManfaat> liter = dataBenef.listIterator();
				if (!dataBenef.isEmpty()) {
					while (liter.hasNext()) {
						try {
							PenerimaManfaat m = liter.next();
							HashMap<String, Object> ditunjuk = new HashMap<>();
							String nama = m.getMsaw_first() != null ? m.getMsaw_first() : null;
							String relasi = m.getLsre_relation() != null ? m.getLsre_relation() : null;
							String dob = m.getMsaw_birth2() != null ? m.getMsaw_birth2() : null;
							Integer persen = m.getMsaw_persen() != null ? m.getMsaw_persen() : null;

							ditunjuk.put("nama", nama);
							ditunjuk.put("relasi", relasi);
							ditunjuk.put("tahun_lahir", dob);
							ditunjuk.put("persen", persen);
							ahliWaris.add(ditunjuk);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}
					data.put("penerima_manfaat", ahliWaris);
					error = false;
					message = "Successfully get penerima manfaat details";
				} else {
					error = true;
					message = "Data penerima manfaat kosong";
					resultErr = "Data penerima manfaat kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Policy is not active";
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
		customResourceLoader.insertHistActivityWS(12, 6, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/billing", produces = "application/json", method = RequestMethod.POST)
	public String billing(@RequestBody RequestBilling requestBilling, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestBilling);
		String res = null;
		Boolean error = true;
		String message = null;
		String resultErr = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestBilling.getUsername();
		String key = requestBilling.getKey();
		String no_polis = requestBilling.getNo_polis();
		Integer pageNumber = requestBilling.getPageNumber();
		Integer pageSize = requestBilling.getPageSize();
		String startDate = requestBilling.getStartDate();
		String endDate = requestBilling.getEndDate();
		String status = requestBilling.getStatus();

		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang pemegang = new Pemegang();
				pemegang.setMspo_policy_no(no_polis);
				pemegang = services.selectPemegang(pemegang);
				if (pemegang != null) {
					List<Billing> lisBill = services.selectBilling(pemegang.getReg_spaj(), pageNumber, pageSize, startDate, endDate);
					List<Object> lisPay = new ArrayList<>();
					if (lisBill != null) {
						lisBill = PageUtils.getPage(lisBill, Optional.of(pageNumber).orElse(1), Optional.of(pageSize).orElse(20));
						lisBill = lisBill.stream().filter(v -> v.getPaid().equals(status)).collect(Collectors.toList());
						ListIterator<Billing> liter = lisBill.listIterator();
						BigDecimal totalAmountTagihan = lisBill.stream()
								.filter(v -> v.getFlag_jt_tempo() != null && v.getFlag_jt_tempo() == 0)
								.filter(v -> v.getPaid().equals("OutS"))
								.map(Billing::getTotal_premi).reduce(BigDecimal.ZERO, BigDecimal::add);
						while (liter.hasNext()) {
							try {
								Billing m = liter.next();
								HashMap<String, Object> pay = new HashMap<>();
								pay.put("paid", m.getPaid() != null ? m.getPaid() : null);
								pay.put("lku_id", m.getLku_id() != null ? m.getLku_id() : null);
								pay.put("kurs", m.getLku_symbol() != null ? m.getLku_symbol() : null);
								pay.put("payment", m.getTotal_premi() != null ? m.getTotal_premi() : null);
								pay.put("premi_ke", m.getPremi_ke() != null ? m.getPremi_ke() : null);
								pay.put("tahun_ke", m.getTh_ke() != null ? m.getTh_ke() : null);
								pay.put("paid_date", m.getTgl_bayar() != null ? df1.format(m.getTgl_bayar()) : null);
								pay.put("period_date", m.getPeriode() != null ? df1.format(m.getPeriode()) : null);
								pay.put("lku_symbol", m.getLku_symbol() != null ? m.getLku_symbol() : null);
								pay.put("flag_jt_tempo", m.getFlag_jt_tempo() != null && m.getFlag_jt_tempo() == 1);

								if (m.getPaid().equals("Paid")) {
									pay.put("status_billing_id", 1);
								} else {
									pay.put("status_billing_id", 2);
								}

								lisPay.add(pay);
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}

						data.put("billing", lisPay);
						data.put("totalResults", lisBill.size());
						data.put("total_premi_tagihan", totalAmountTagihan);
						error = false;
						message = "Successfully get data billing";
					} else {
						error = false;
						message = "Can't get data billing";
						resultErr = "Data billing kosong";
					}
				} else {
					error = true;
					message = "Can't get data billing";
					resultErr = "Data pemegang kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Can't get data billing";
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
		customResourceLoader.insertHistActivityWS(12, 11, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/currentinvestasitransaksi", produces = "application/json", method = RequestMethod.POST)
	public String currentInvestasiTransaksi(
			@RequestBody RequestCurrentInvestasiTransaksi requestCurrentInvestasiTransaksi, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestCurrentInvestasiTransaksi);
		String res = null;
		Boolean error = true;
		String message = null;
		String resultErr = null;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestCurrentInvestasiTransaksi.getUsername();
		String key = requestCurrentInvestasiTransaksi.getKey();
		String no_polis = requestCurrentInvestasiTransaksi.getNo_polis();

		String key_orion = key;
		if(key_orion.equalsIgnoreCase("orion")) {
			key = services.selectEncrypt(username);
		}
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang pemegang = new Pemegang();
				pemegang.setMspo_policy_no(no_polis);
				pemegang = services.selectPemegang(pemegang);
				if (pemegang != null) {
					String reg_spaj = pemegang.getReg_spaj();
					if (requestCurrentInvestasiTransaksi.getStatus_product() == 4) { // Unit Link
						HashMap<String, Object> unit_link = new HashMap<>();
						ArrayList<UnitLink> ulink = services.selectUnitLink(reg_spaj);
						if (!ulink.isEmpty()) {
							ListIterator<UnitLink> liter = ulink.listIterator();
							ArrayList<HashMap<String, Object>> investment = new ArrayList<HashMap<String, Object>>();
							Integer a = ulink.size();
							while (liter.hasNext()) {
								try {
									UnitLink m = liter.next();
									HashMap<String, Object> tempData = new HashMap<>();
									tempData.put("lji_id", m.getLji_id());
									tempData.put("fund", m.getLji_invest());
									tempData.put("currency", m.getLku_symbol());
									tempData.put("date", df1.format(m.getLnu_tgl()));
									tempData.put("unit_price", nfZeroFour.format(m.getHarga_Unit()));
									tempData.put("total_unit", nfZeroFour.format(m.getTotal_Unit()));
									tempData.put("policy_value", nfZeroTwo.format(m.getNilai_polis()));
									
									
									investment.add(tempData);
									unit_link.put("investment", investment);
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + e);
								}
							}

							Integer i = 0;
							ArrayList<UnitLink> tester = services.selectUnitLink(reg_spaj);
							List<BigDecimal> results = new ArrayList<>();
							ListIterator<UnitLink> iter = tester.listIterator();
							while (iter.hasNext()) {
								BigDecimal sum = BigDecimal.ZERO;
								while (i < a && iter.hasNext()) {
									try {
										UnitLink m = iter.next();
										BigDecimal resultNilaiPolis = m.getNilai_polis();
										sum = sum.add(resultNilaiPolis);
									} catch (Exception e) {
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + e);
									}
								}
								results.add(sum);
								unit_link.put("total", nfZeroTwo.format(results.get(0)));
								
							}
							
							Double totalTungakanUnitLink = services.selectTotalTunggakanUnitLink(reg_spaj);
							unit_link.put("total_tunggakan", nfZeroTwo.format(totalTungakanUnitLink) );
							data.put("unit_link", unit_link);
							data.put("powersave", null);
							data.put("stable_save", null);
							data.put("stable_link", null);

							error = false;
							message = "Successfully get data ringkasan";
						} else {
							error = false;
							message = "Data investasi kosong, product: "
									+ requestCurrentInvestasiTransaksi.getStatus_product() + " Page Number: "
									+ requestCurrentInvestasiTransaksi.getPageNumber() + " REG_SPAJ: " + reg_spaj;
						}
					} else if (requestCurrentInvestasiTransaksi.getStatus_product() == 1) { // Power Save
						HashMap<String, Object> power_save = new HashMap<>();
						User paramPowerSave = new User();
						paramPowerSave.setReg_spaj(reg_spaj);
						paramPowerSave.setPageNumber(requestCurrentInvestasiTransaksi.getPageNumber());
						paramPowerSave.setPageSize(requestCurrentInvestasiTransaksi.getPageSize());
						paramPowerSave.setStartDate(requestCurrentInvestasiTransaksi.getStartDate());
						paramPowerSave.setEndDate(requestCurrentInvestasiTransaksi.getEndDate());
						ArrayList<PowerSave> powerSave = services.selectPowerSave(paramPowerSave);
						if (!powerSave.isEmpty()) {
							ArrayList<HashMap<String, Object>> investment = new ArrayList<HashMap<String, Object>>();
							ListIterator<PowerSave> liter = powerSave.listIterator();
							while (liter.hasNext()) {
								try {
									PowerSave m = liter.next();
									HashMap<String, Object> maper = new HashMap<>();
									maper.put("id_power_save", m.getId());
									maper.put("begin_period", df1.format(m.getBeg_date()));
									maper.put("end_period", df1.format(m.getEnd_date()));
									maper.put("begin_period_format", m.getBeg_date_format());
									maper.put("end_period_format", m.getEnd_date_format());
									maper.put("rate", m.getRate());
									maper.put("deposit_premium", m.getDeposit());
									maper.put("interest_premium", m.getInterest());
									maper.put("investment_guarantee_period", m.getMpr_jangka_invest());
									maper.put("currency", m.getLku_symbol());
									maper.put("jenis", m.getJenis());

									investment.add(maper);
									power_save.put("investment", investment);

									data.put("unit_link", null);
									data.put("powersave", power_save);
									data.put("stable_save", null);
									data.put("stable_link", null);

									error = false;
									message = "Successfully get data powersave";
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + e);
								}
							}
						} else {
							error = false;
							message = "Data investasi kosong, product: "
									+ requestCurrentInvestasiTransaksi.getStatus_product() + " Page Number: "
									+ requestCurrentInvestasiTransaksi.getPageNumber() + " REG_SPAJ: " + reg_spaj;
						}
					} else if (requestCurrentInvestasiTransaksi.getStatus_product() == 2) { // Stable Save
						HashMap<String, Object> stable_save = new HashMap<>();
						User paramStableSave = new User();
						paramStableSave.setReg_spaj(reg_spaj);
						paramStableSave.setPageNumber(requestCurrentInvestasiTransaksi.getPageNumber());
						paramStableSave.setPageSize(requestCurrentInvestasiTransaksi.getPageSize());
						paramStableSave.setStartDate(requestCurrentInvestasiTransaksi.getStartDate());
						paramStableSave.setEndDate(requestCurrentInvestasiTransaksi.getEndDate());
						ArrayList<StableSave> stableSave = services.selectStableSave(paramStableSave);
						if (!stableSave.isEmpty()) {
							ArrayList<HashMap<String, Object>> investment = new ArrayList<HashMap<String, Object>>();
							ListIterator<StableSave> liter = stableSave.listIterator();
							while (liter.hasNext()) {
								try {
									StableSave m = liter.next();
									HashMap<String, Object> maper = new HashMap<>();
									maper.put("id_stable_save", m.getId());
									maper.put("period_start_date", df1.format(m.getMss_bdate()));
									maper.put("period_end_date", df1.format(m.getMss_edate()));
									maper.put("mgi", m.getMss_mgi());
									maper.put("rate", m.getMss_rate());
									maper.put("deposit", m.getDeposit());
									maper.put("interest", m.getMss_bunga());
									maper.put("currency", m.getLku_symbol());
									maper.put("jenis", m.getTransactions());

									investment.add(maper);
									stable_save.put("investment", investment);

									data.put("unit_link", null);
									data.put("powersave", null);
									data.put("stable_save", stable_save);
									data.put("stable_link", null);

									error = false;
									message = "Successfully get data stablesave";
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + e);
								}
							}
						} else {
							error = false;
							message = "Data investasi kosong, product: "
									+ requestCurrentInvestasiTransaksi.getStatus_product() + " Page Number: "
									+ requestCurrentInvestasiTransaksi.getPageNumber() + " REG_SPAJ: " + reg_spaj;
						}
					} else if (requestCurrentInvestasiTransaksi.getStatus_product() == 3) { // Stable Link
						HashMap<String, Object> stable_link = new HashMap<>();
						User paramStableLink = new User();
						paramStableLink.setReg_spaj(reg_spaj);
						paramStableLink.setPageNumber(requestCurrentInvestasiTransaksi.getPageNumber());
						paramStableLink.setPageSize(requestCurrentInvestasiTransaksi.getPageSize());
						paramStableLink.setStartDate(requestCurrentInvestasiTransaksi.getStartDate());
						paramStableLink.setEndDate(requestCurrentInvestasiTransaksi.getEndDate());
						ArrayList<StableLink> stableLink = services.selectStableLink(paramStableLink);
						if (!stableLink.isEmpty()) {
							ArrayList<HashMap<String, Object>> investment = new ArrayList<HashMap<String, Object>>();
							ListIterator<StableLink> liter = stableLink.listIterator();
							while (liter.hasNext()) {
								try {
									StableLink m = liter.next();
									HashMap<String, Object> maper = new HashMap<>();
									maper.put("id_stable_link", m.getMsl_tu_ke());
									maper.put("transaction", m.getMsl_desc());
									maper.put("total_unit", m.getMsl_unit());
									maper.put("unit_price", m.getMsl_nab());
									maper.put("premi_bunga", m.getJumlah_premi_bunga());
									maper.put("currency", m.getLku_symbol());

									investment.add(maper);
									stable_link.put("investment", investment);

									data.put("unit_link", null);
									data.put("powersave", null);
									data.put("stable_save", null);
									data.put("stable_link", stable_link);
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + e);
								}
							}

							Integer i = 0;
							ArrayList<StableLink> tester = services.selectStableLink(paramStableLink);
							Integer a = tester.size();
							List<BigDecimal> results = new ArrayList<>();
							ListIterator<StableLink> iter = tester.listIterator();
							while (iter.hasNext()) {
								BigDecimal sum = BigDecimal.ZERO;
								while (i < a && iter.hasNext()) {
									try {
										StableLink m = iter.next();
										BigDecimal mslKode = m.getMsl_kode();
										BigDecimal resultNilaiPolis = m.getJumlah_premi_bunga();
										if (mslKode.intValue() != 8) {
											sum = sum.add(resultNilaiPolis);
										}
									} catch (Exception e) {
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + e);
									}
								}
								results.add(sum);
								stable_link.put("policy_value", nfZeroTwo.format(results.get(0)));
							}

							error = false;
							message = "Successfully get data stablelink";
							data.isEmpty();
						} else {
							error = false;
							message = "Data investasi kosong, product: "
									+ requestCurrentInvestasiTransaksi.getStatus_product() + " Page Number: "
									+ requestCurrentInvestasiTransaksi.getPageNumber() + " REG_SPAJ: " + reg_spaj;
						}
					} else {
						error = true;
						message = "Can't get current investation transaction data";
						resultErr = "Untuk status product lain belum ada mock up nya";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					error = true;
					message = "Can't get current investation transaction data";
					resultErr = "Data pemegang kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Can't get current investation transaction data";
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
		customResourceLoader.insertHistActivityWS(12, 13, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	/**
	 * DESC: Untuk cek detail investasi Unit Link
	 */
	@RequestMapping(value = "/detailinvestasitransaksi", produces = "application/json", method = RequestMethod.POST)
	public String detailInvestasiTransaksi(@RequestBody RequestDetailInvestasiTransaksi requestDetailInvestasiTransaksi,
			HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDetailInvestasiTransaksi);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		String username = requestDetailInvestasiTransaksi.getUsername();
		String key = requestDetailInvestasiTransaksi.getKey();
		String no_polis = requestDetailInvestasiTransaksi.getNo_polis();
		String lji_id = requestDetailInvestasiTransaksi.getLji_id();
		Integer pageNumber = requestDetailInvestasiTransaksi.getPageNumber();
		Integer pageSize = requestDetailInvestasiTransaksi.getPageSize();
		String startDate = requestDetailInvestasiTransaksi.getStartDate();
		String endDate = requestDetailInvestasiTransaksi.getEndDate();
		
		String key_orion = key;
		if(key_orion.equalsIgnoreCase("orion")) {
			key = services.selectEncrypt(username);
		}
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang pemegang = new Pemegang();
				pemegang.setMspo_policy_no(no_polis);
				pemegang = services.selectPemegang(pemegang);
				if (pemegang != null) {
					String spaj = pemegang.getReg_spaj();
					ArrayList<UnitLink> ulink = services.selectDetailUnitLink(spaj, lji_id, pageNumber, pageSize,
							startDate, endDate);
					if (!ulink.isEmpty()) {
						ListIterator<UnitLink> liter = ulink.listIterator();
						while (liter.hasNext()) {
							try {
								UnitLink m = liter.next();
								BigDecimal jumlah_Unit = m.getMtu_unit() != null ? m.getMtu_unit() : null;
								String harga_Unit = nfZeroFour.format(m.getMtu_nab());
								String tgl_nab = m.getMtu_tgl_nab() != null ? m.getMtu_tgl_nab() : null;
								String transaksi = m.getMtu_desc() != null ? m.getMtu_desc() : null;
								String jumlah = nfZeroTwo.format(m.getMtu_jumlah());

								HashMap<String, Object> tempData = new HashMap<>();
								tempData.put("jumlah_unit", jumlah_Unit);
								tempData.put("harga_unit", harga_Unit);
								tempData.put("tgl_nab", tgl_nab);
								tempData.put("transaksi", transaksi);
								tempData.put("jumlah", jumlah);
								data.add(tempData);

								error = false;
								message = "Successfully get detail investation transaction";
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
					} else {
						error = false;
						message = "Detail investation transaction not found";
						resultErr = "Data ulink kosong";
					}
				} else {
					error = true;
					message = "Can't get investation transaction data";
					resultErr = "Data pemegang kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Can't get investation transaction data";
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
		customResourceLoader.insertHistActivityWS(12, 14, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/detailstablelink", produces = "application/json", method = RequestMethod.POST)
	public String detailStableLinkUser(@RequestBody RequestDetailStableLink requestDetailStableLink,
			HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDetailStableLink);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		String username = requestDetailStableLink.getUsername();
		String key = requestDetailStableLink.getKey();
		String no_polis = requestDetailStableLink.getNo_polis();
		
		String key_orion = key;
		if(key_orion.equalsIgnoreCase("orion")) {
			key = services.selectEncrypt(username);
		}
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang paramGetSPAJ = new Pemegang();
				paramGetSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramGetSPAJ);
				if (dataSPAJ != null) {
					User paramDetailStableLink = new User();
					paramDetailStableLink.setReg_spaj(dataSPAJ.getReg_spaj());
					paramDetailStableLink.setMsl_tu_ke(requestDetailStableLink.getMsl_tu_ke());
					paramDetailStableLink.setPageNumber(requestDetailStableLink.getPageNumber());
					paramDetailStableLink.setPageSize(requestDetailStableLink.getPageSize());
					paramDetailStableLink.setStartDate(requestDetailStableLink.getStartDate());
					paramDetailStableLink.setEndDate(requestDetailStableLink.getEndDate());
					ArrayList<StableLink> stableLink = services.selectDetailStableLink(paramDetailStableLink);
					if (!stableLink.isEmpty()) {
						ListIterator<StableLink> liter = stableLink.listIterator();
						while (liter.hasNext()) {
							try {
								StableLink m = liter.next();
								HashMap<String, Object> tempData = new HashMap<>();
								tempData.put("id", m.getId());
								tempData.put("periode_start", df1.format(m.getMsl_bdate()));
								tempData.put("periode_end", df1.format(m.getMsl_edate()));
								tempData.put("premi", m.getMsl_premi());
								tempData.put("bunga", m.getMsl_bunga());
								tempData.put("rate", m.getMsl_rate());
								tempData.put("harga_unit", m.getMsl_nab());
								tempData.put("total_unit", m.getMsl_unit());
								tempData.put("nilai_polis", m.getMsl_nilai_polis());
								tempData.put("mti", m.getMsl_mgi());
								tempData.put("jenis", m.getJenis());
								data.add(tempData);

								error = false;
								message = "Successfully get detail investation transaction";
							} catch (Exception e) {
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
					} else {
						error = false;
						message = "Detail investation transaction not found";
						resultErr = "Data detail stable link kosong";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					error = true;
					message = "Failed get data";
					resultErr = "Data SPAJ tidak ada";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
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
		customResourceLoader.insertHistActivityWS(12, 38, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/viewclaim", produces = "application/json", method = RequestMethod.POST)
	public String viewClaim(@RequestBody RequestViewClaim requestViewClaim, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewClaim);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> data = new ArrayList<>();

		String username = requestViewClaim.getUsername();
		String key = requestViewClaim.getKey();
		String no_polis = requestViewClaim.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get REG_SPAJ
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);

				ArrayList<KlaimKesehatan> listklaim = services.selectKlaimkesehatan(dataSpaj.getReg_spaj(),
						requestViewClaim.getPageNumber(), requestViewClaim.getPageSize(),
						requestViewClaim.getStartDate(), requestViewClaim.getEndDate());
				if (!listklaim.isEmpty()) {
					ListIterator<KlaimKesehatan> liter = listklaim.listIterator();
					while (liter.hasNext()) {
						try {
							KlaimKesehatan m = liter.next();
							HashMap<String, Object> mapper = new HashMap<>();

							Date tglRawat = m.getRegapldate();
							Date tanggalStatus = m.getClm_paid_date();
							String jenisKlaim = m.getJenis_claim();
							String diagnosa = m.getNm_diagnos();
							String lkuSymbol = m.getLku_symbol();
							BigDecimal JumlahKlaim = m.getAmt_claim();
							BigDecimal jumlahBayar = m.getPay_claim();
							BigDecimal id_status = m.getId_status_accept();

							mapper.put("jumlah_klaim", JumlahKlaim);
							mapper.put("tanggal_rawat", tglRawat != null ? df.format(tglRawat) : null);
							mapper.put("jenis_klaim", jenisKlaim);
							mapper.put("diagnosa", diagnosa);
							mapper.put("jumlah_bayar", jumlahBayar);
							mapper.put("tanggal", tanggalStatus != null ? df.format(tanggalStatus) : null);
							mapper.put("currency", lkuSymbol);

							if ((id_status.intValue() == 1 || id_status.intValue() == 2
									|| id_status.intValue() == 16)) {
								mapper.put("status_paid_id", 1);
							} else if ((id_status.intValue() == 10 || id_status.intValue() == 20)) {
								mapper.put("status_paid_id", 2);
							} else if (id_status.intValue() == 5 || id_status.intValue() == 15) {
								mapper.put("status_paid_id", 3);
							}

							error = false;
							message = "Successfully get data claim";
							data.add(mapper);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}
					
					KlaimKesehatan countKlaim = services.selectCountKlaimkesehatan(dataSpaj.getReg_spaj());
							
					Integer total_data = countKlaim.getCount();
					Integer page_size = 10;
					Integer total_page;
					
					if (total_data % page_size == 0) {
						total_page = total_data / page_size;
					} else {
						total_page = total_data / page_size + 1;
					}
					
					map.put("total_page", total_page);
				} else {
					error = false;
					message = "Data claim kosong";
					resultErr = "Data claim kosong";
				}
			} else {
				error = true;
				message = "Can't get data claim";
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
		customResourceLoader.insertHistActivityWS(12, 22, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/statement", produces = "application/json", method = RequestMethod.POST)
	public String m_statement(@RequestBody RequestStatement requestStatement, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestStatement);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		String username = requestStatement.getUsername();
		String key = requestStatement.getKey();
		String no_polis = requestStatement.getNo_polis();
		Integer code = requestStatement.getRequest_code();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get REG_SPAJ
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);

				if (dataSpaj != null) {
					String spaj = dataSpaj.getReg_spaj();
					// String spaj = "091234567";
					String kodeCabang = services.getKodeCabang(no_polis);
					String basePath = storageMpolicy;
					File folder = new File(basePath + kodeCabang + '\\' + spaj);

					if (!folder.exists()) {
						folder.mkdirs();
						error = false;
						message = "Successfully get data m-Statment";
					}

					if (folder.isDirectory()) {
						if (code == 100) {
							File folderLaporanBulanan = new File(
									basePath + kodeCabang + '\\' + spaj + '\\' + "6Bulanan");
							if (!folderLaporanBulanan.exists()) {
								folderLaporanBulanan.mkdirs();
								error = false;
								message = "Successfully get data m-Statment";
							}

							if (folderLaporanBulanan.isDirectory()) {
								File filesLaporanBulanan[] = folderLaporanBulanan.listFiles();
								Arrays.sort(filesLaporanBulanan);
								if (filesLaporanBulanan.length > 0) {
									for (File f : filesLaporanBulanan) {
										try {
											if (f.getName().toLowerCase().contains("laporan_6bulanan")) {
												char ch = '-';
												String str = f.getName().toLowerCase().replace("_", " ")
														.replace("laporan 6bulanan", "M-Statement").replace(".pdf", "");

												StringBuilder string = new StringBuilder(str);
												string.setCharAt(18, ch);
												String dateYearOne = string.substring(string.indexOf(" ") + 1,
														string.indexOf(" ") + 5);
												String dateMonthOne = string.substring(string.indexOf(" ") + 5,
														string.indexOf(" ") + 7);

												String dateYearTwo = string.substring(string.indexOf(" ") + 8,
														string.indexOf(" ") + 12);
												String dateMonthTwo = string.substring(string.indexOf(" ") + 12,
														string.indexOf(" ") + 14);

												String nameVal = string.substring(0, string.indexOf(" ")) + ' '
														+ dateMonthOne + '/' + dateYearOne + '-' + dateMonthTwo + '/'
														+ dateYearTwo;

												error = false;
												message = "Successfully get data m-Statment";
												HashMap<String, Object> tempData = new HashMap<>();
												tempData.put("name_before", f.getName());
												tempData.put("name_after", nameVal.toUpperCase());
												tempData.put("date_created", df2.format(new Date(f.lastModified())));
												data.add(tempData);
											} else {
												error = false;
												message = "Successfully get data m-Statment";
											}
										} catch (Exception e) {
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + e);
										}
									}
								} else {
									error = false;
									message = "Successfully get data m-Statment";
								}
							} else {
								error = false;
								message = "Successfully get data m-Statment";
							}
						} else if (code == 200) {
							File folderLaporanTransaksi = new File(
									basePath + kodeCabang + '\\' + spaj + '\\' + "surat_transaksi");
							if (!folderLaporanTransaksi.exists()) {
								folderLaporanTransaksi.mkdirs();
								error = false;
								message = "Successfully get data m-Statment";
							}

							if (folderLaporanTransaksi.isDirectory()) {
								File filesLaporanTransaksi[] = folderLaporanTransaksi.listFiles();
								Arrays.sort(filesLaporanTransaksi,
										Comparator.comparingLong(File::lastModified).reversed());
								if (filesLaporanTransaksi.length > 0) {
									for (File f : filesLaporanTransaksi) {
										try {
											if ((f.getName().toLowerCase().contains("topup"))
													|| (f.getName().toLowerCase().contains("switching"))
													|| (f.getName().toLowerCase().contains("withdraw"))) {
												String nameFinal = f.getName().replace("_", " ").replace(".pdf", "")
														.replace("-", "/").replace(".", ":");

												error = false;
												message = "Successfully get data m-Statment";
												HashMap<String, Object> tempData = new HashMap<>();
												tempData.put("name_before", f.getName());
												tempData.put("name_after", nameFinal.toUpperCase());
												tempData.put("date_created", df2.format(new Date(f.lastModified())));
												data.add(tempData);
											} else {
												error = false;
												message = "Successfully get data m-Statment";
											}
										} catch (Exception e) {
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + e);
										}
									}
								} else {
									error = false;
									message = "Successfully get data m-Statment";
								}
							} else {
								error = false;
								message = "Successfully get data m-Statment";
							}
						} else if (code == 300) {
							File folderLaporanAkanLapse = new File(
									basePath + kodeCabang + '\\' + spaj + '\\' + "akan_lapse");
							if (!folderLaporanAkanLapse.exists()) {
								folderLaporanAkanLapse.mkdirs();
								error = false;
								message = "Successfully get data m-Statment";
							}

							if (folderLaporanAkanLapse.isDirectory()) {
								File filesLaporanAkanLapse[] = folderLaporanAkanLapse.listFiles();
								Arrays.sort(filesLaporanAkanLapse);
								if (filesLaporanAkanLapse.length > 0) {
									for (File f : filesLaporanAkanLapse) {
										try {
											String name = null;
											if (f.getName().toLowerCase().contains("prelapsed")) {
												name = f.getName().replace("_", " ").replace(".pdf", "");
												String dateYear = name.substring(name.indexOf(" ") + 1,
														name.indexOf(" ") + 5);
												String dateMonth = name.substring(name.indexOf(" ") + 5,
														name.indexOf(" ") + 7);
												String dateDay = name.substring(name.indexOf(" ") + 7,
														name.indexOf(" ") + 9);
												String nameFinal = name.substring(0, name.indexOf(" ")) + " " + dateDay
														+ '/' + dateMonth + '/' + dateYear;

												error = false;
												message = "Successfully get data m-Statment";
												HashMap<String, Object> tempData = new HashMap<>();
												tempData.put("name_before", f.getName());
												tempData.put("name_after", nameFinal.toUpperCase());
												tempData.put("date_created", df2.format(new Date(f.lastModified())));
												data.add(tempData);
											} else {
												error = false;
												message = "Successfully get data m-Statment";
											}
										} catch (Exception e) {
											logger.error("Path: " + request.getServletPath() + " Username: " + username
													+ " Error: " + e);
										}
									}
								} else {
									error = false;
									message = "Successfully get data m-Statment";
								}
							} else {
								error = false;
								message = "Successfully get data m-Statment";
							}
						} else if (code == 400) {
							File folderLaporanLapse = new File(basePath + kodeCabang + '\\' + spaj + '\\' + "lapse");
							if (!folderLaporanLapse.exists()) {
								folderLaporanLapse.mkdirs();
								error = false;
								message = "Successfully get data m-Statment";
							}

							if (folderLaporanLapse.isDirectory()) {
								File filesLaporanLapse[] = folderLaporanLapse.listFiles();
								Arrays.sort(filesLaporanLapse);
								if (filesLaporanLapse.length > 0) {
									try {
										for (File f : filesLaporanLapse) {
											String name = null;
											if (f.getName().substring(0, 6).equalsIgnoreCase("lapsed")) {
												name = f.getName().replace("_", " ").replace(".pdf", "");
												String dateYear = name.substring(name.indexOf(" ") + 1,
														name.indexOf(" ") + 5);
												String dateMonth = name.substring(name.indexOf(" ") + 5,
														name.indexOf(" ") + 7);
												String dateDay = name.substring(name.indexOf(" ") + 7,
														name.indexOf(" ") + 9);
												String nameFinal = name.substring(0, name.indexOf(" ")) + " " + dateDay
														+ '/' + dateMonth + '/' + dateYear;

												error = false;
												message = "Successfully get data m-Statment";
												HashMap<String, Object> tempData = new HashMap<>();
												tempData.put("name_before", f.getName());
												tempData.put("name_after", nameFinal.toUpperCase());
												tempData.put("date_created", df2.format(new Date(f.lastModified())));
												data.add(tempData);
											} else {
												error = false;
												message = "Successfully get data m-Statment";
											}
										}
									} catch (Exception e) {
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + e);
									}
								} else {
									error = false;
									message = "Successfully get data m-Statment";
								}
							} else {
								error = false;
								message = "Successfully get data m-Statment";
							}
						} else if (code == 500) {
							File folderSuratClaim = new File(basePath + kodeCabang + File.separator + spaj + File.separator + "SuratClaim");
							if (!folderSuratClaim.exists()) {
								folderSuratClaim.mkdirs();
								error = false;
								message = "Successfully get data m-Statment";
							}

							if (folderSuratClaim.isDirectory()) {
								File filesSuratClaim[] = folderSuratClaim.listFiles();
								Arrays.sort(filesSuratClaim);
								if (filesSuratClaim.length > 0) {
									try {
										for (File f : filesSuratClaim) {
											String name = null;
											if (f.getName().toLowerCase().contains("suratclaim")) {
												name = f.getName().replace("-", " ").replace(".pdf", "").replace("_",
														" ");

												String nameFinal = name.substring(10, name.length());

												error = false;
												message = "Successfully get data m-Statment";
												HashMap<String, Object> tempData = new HashMap<>();
												tempData.put("name_before", f.getName());
												tempData.put("name_after", nameFinal.toUpperCase());
												tempData.put("date_created", df2.format(new Date(f.lastModified())));
												data.add(tempData);
											} else {
												error = false;
												message = "Successfully get data m-Statment";
											}
										}
									} catch (Exception e) {
										logger.error("Path: " + request.getServletPath() + " Username: " + username
												+ " Error: " + e);
									}
								} else {
									error = false;
									message = "Successfully get data m-Statment";
								}
							} else {
								error = false;
								message = "Successfully get data m-Statment";
							}
						} else {
							error = true;
							message = "Failed get data m-Statment";
							resultErr = "Request code " + code + " belum terdaftar di system";
							HashMap<String, Object> tempData = new HashMap<>();
							tempData.put("name_before", null);
							tempData.put("name_after", null);
							tempData.put("date_created", null);
							data.add(tempData);
							logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
									+ resultErr);
						}
					} else {
						error = true;
						message = "Failed get data m-Statment";
						resultErr = "Directory SPAJ tidak ada";
						HashMap<String, Object> tempData = new HashMap<>();
						tempData.put("name_before", null);
						tempData.put("name_after", null);
						tempData.put("date_created", null);
						data.add(tempData);
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					error = true;
					message = "Failed get data m-Statment";
					resultErr = "SPAJ tidak ada";
					HashMap<String, Object> tempData = new HashMap<>();
					tempData.put("name_before", null);
					tempData.put("name_after", null);
					tempData.put("date_created", null);
					data.add(tempData);
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Failed get data m-Statment";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				HashMap<String, Object> tempData = new HashMap<>();
				tempData.put("name_before", null);
				tempData.put("name_after", null);
				tempData.put("date_created", null);
				data.add(tempData);
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
		map.put("data", data);
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 23, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/statement/download", method = RequestMethod.POST)
	public void m_statement_download(@RequestBody RequestStatementDownload requestStatementDownload,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestStatementDownload);
		String res = null;
		String message = null;
		String resultErr = null;
		boolean error = true;
		JSONObject resultStatement = new JSONObject();

		String username = requestStatementDownload.getUsername();
		String key = requestStatementDownload.getKey();
		String no_polis = requestStatementDownload.getNo_polis();
		Integer requestCode = requestStatementDownload.getRequest_code();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get REG_SPAJ
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);

				if (dataSpaj != null) {
					String spaj = dataSpaj.getReg_spaj();
					// String spaj = "091234567";
					String kodeCabang = services.getKodeCabang(no_polis);
					// path directory
					String basePath = storageMpolicy;
					File folder = new File(basePath + kodeCabang + '\\' + spaj);

					if (folder.isDirectory()) {
						String dynamicPath = null;
						if (requestCode == 100) {
							dynamicPath = "6Bulanan";
						} else if (requestCode == 200) {
							dynamicPath = "surat_transaksi";
						} else if (requestCode == 300) {
							dynamicPath = "akan_lapse";
						} else if (requestCode == 400) {
							dynamicPath = "lapse";
						} else if (requestCode == 500) {
							dynamicPath = "SuratClaim";
						} else {
							dynamicPath = null;
						}

						// path file
						String pathWS = folder.toString() + '\\' + dynamicPath + '\\'
								+ requestStatementDownload.getFile_name();

						// path file yang mau di download
						File file = new File(pathWS);

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
						message = "Success download";
					} else {
						error = true;
						message = "Failed download";
						resultErr = "Directory not found";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					error = true;
					message = "Failed download";
					resultErr = "SPAJ not found";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Failed download";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}

		resultStatement.put("error", error);
		resultStatement.put("message", message);
		res = resultStatement.toString();

		try {
			// Update activity user table LST_USER_SIMULTANEOUS
			customResourceLoader.updateActivity(username);
			// Insert Log LST_HIST_ACTIVITY_WS
			customResourceLoader.insertHistActivityWS(12, 24, new Date(), req, res, 1, resultErr, start, username);
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
	}

	@RequestMapping(value = "/dataasuransi/download/manfaat", method = RequestMethod.POST)
	public void dataAsuransiDownloadManfaat(@RequestBody RequestDataAsuransiDownload requestDataAsuransiDownload,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestDataAsuransiDownload);
		String res = null;
		String message = null;
		String resultErr = null;
		boolean error = true;
		JSONObject resultManfaat = new JSONObject();

		String username = requestDataAsuransiDownload.getUsername();
		String key = requestDataAsuransiDownload.getKey();
		String no_polis = requestDataAsuransiDownload.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get REG_SPAJ
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);

				if (dataSpaj != null) {
					String spaj = dataSpaj.getReg_spaj();
					// String spaj = "091234567";
					String kodeCabang = services.getKodeCabang(no_polis);
					// path directory
					File folder = new File(manfaatpdfMpolicy + kodeCabang + '\\' + spaj);

					if (folder.isDirectory()) {
						File files[] = folder.listFiles();

						for (File f : files) {
							if (f.getName().toLowerCase().contains("manfaat.pdf")) {
								// path file
								String pathWS = folder.toString() + '\\' + f.getName();

								// path file yang mau di download
								File file = new File(pathWS);

								// Content-Disposition
								response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
										"attachment;filename=" + file.getName());

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
								message = "Success download";
							} else {
								error = true;
								message = "Failed download";
								resultErr = "File not found";
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ resultErr);
							}
						}
					} else {
						error = true;
						message = "Failed download";
						resultErr = "Directory not found";
					}
				} else {
					error = true;
					message = "Failed download";
					resultErr = "SPAJ not found";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Failed download";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
		resultManfaat.put("error", error);
		resultManfaat.put("message", message);

		try {
			// Update activity user table LST_USER_SIMULTANEOUS
			customResourceLoader.updateActivity(username);
			// Insert Log LST_HIST_ACTIVITY_WS
			customResourceLoader.insertHistActivityWS(12, 26, new Date(), req, res, 1, resultErr, start, username);
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
		}
	}

	@RequestMapping(value = "/pemegangpolis/update", produces = "application/json", method = RequestMethod.POST)
	public String updateData(@RequestBody RequestUpdatePemegangPolis requestUpdatePemegangPolis,
			HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestUpdatePemegangPolis);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestUpdatePemegangPolis.getUsername();
		String key = requestUpdatePemegangPolis.getKey();
		String noPolis = requestUpdatePemegangPolis.getNo_polis();
		String alamatRumah = requestUpdatePemegangPolis.getAlamat_rumah();
		String email = requestUpdatePemegangPolis.getEmail();
		String kota_rumah = requestUpdatePemegangPolis.getKabupaten();
		String kd_pos_rumah = requestUpdatePemegangPolis.getKd_pos_rumah();
		Integer lspr_id = requestUpdatePemegangPolis.getLspr_id();
		Integer lska_id = requestUpdatePemegangPolis.getLska_id();
		Integer lskc_id = requestUpdatePemegangPolis.getLskc_id();
		Integer lskl_id = requestUpdatePemegangPolis.getLskl_id();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang updateMstAddressNew = new Pemegang();
				updateMstAddressNew.setMspo_policy_no(noPolis);
				Pemegang checkNopolis = services.selectNomorPolis(updateMstAddressNew);
				if (checkNopolis != null) {
					String mcl_id = checkNopolis.getMcl_id();
					if (mcl_id != null) {
						updateMstAddressNew.setAlamat_rumah(alamatRumah);
						updateMstAddressNew.setKota_rumah(kota_rumah);
						updateMstAddressNew.setKd_pos_rumah(kd_pos_rumah);
						updateMstAddressNew.setLspr_id(lspr_id);
						updateMstAddressNew.setLska_id(lska_id);
						updateMstAddressNew.setLskc_id(lskc_id);
						updateMstAddressNew.setLskl_id(lskl_id);
						updateMstAddressNew.setMcl_id(mcl_id);
						services.updateDataMstAddressNew(updateMstAddressNew);

						Pemegang updateMstAddressBilling = new Pemegang();
						updateMstAddressBilling.setEmail(email);
						updateMstAddressBilling.setReg_spaj(checkNopolis.getReg_spaj());
						services.updateDataMstAddressBilling(updateMstAddressBilling);

						error = false;
						message = "Success update data";
					} else {
						error = true;
						message = "Failed update data";
						resultErr = "MCL ID tidak ada";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					error = true;
					message = "Failed update data";
					resultErr = "Username tidak terdaftar";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Failed update data";
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
		customResourceLoader.insertHistActivityWS(12, 25, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/provinsi", produces = "application/json", method = RequestMethod.POST)
	public String getListPropinsi(@RequestBody RequestProvinsi requestProvinsi, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestProvinsi);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> data = new ArrayList<>();

		String username = requestProvinsi.getUsername();
		String key = requestProvinsi.getKey();
		Integer requestCode = requestProvinsi.getRequestCode();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				if (requestCode == 10) { // Provinsi
					ArrayList<Provinsi> listProvinsi = services.selectListProvinsi();
					if (!listProvinsi.isEmpty()) {
						ListIterator<Provinsi> liter = listProvinsi.listIterator();
						while (liter.hasNext()) {
							Provinsi m = liter.next();
							HashMap<String, Object> mapper = new HashMap<>();

							try {
								String PROPINSI = m.getPropinsi();
								BigDecimal LSPR_ID = m.getLspr_id();
								mapper.put("NAME", PROPINSI);
								mapper.put("ID", LSPR_ID);
								data.add(mapper);

								error = false;
								message = "Successfully get data provinsi";
							} catch (Exception e) {
								error = false;
								message = "Failed get data provinsi";
								resultErr = "Data provinsi kosong : " + e;
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
					} else {
						error = true;
						message = "Failed get data provinsi";
						resultErr = "Data provinsi kosong";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (requestCode == 20) { // Kabupaten
					ArrayList<Provinsi> listKabupaten = services.selectListKabupaten(requestProvinsi.getLspr_id());
					if (!listKabupaten.isEmpty()) {
						ListIterator<Provinsi> liter = listKabupaten.listIterator();
						while (liter.hasNext()) {
							Provinsi m = liter.next();
							HashMap<String, Object> mapper = new HashMap<>();

							try {
								String KABUPATEN = m.getKabupaten();
								BigDecimal LSKA_ID = m.getLska_id();
								mapper.put("NAME", KABUPATEN);
								mapper.put("ID", LSKA_ID);
								data.add(mapper);

								error = false;
								message = "Successfully get data kabupaten";
							} catch (Exception e) {
								error = true;
								message = "Failed get data kabupaten";
								resultErr = "Data kabupaten kosong";
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
					} else {
						error = true;
						message = "Failed get data kabupaten";
						resultErr = "Data kabupaten kosong";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (requestCode == 30) { // Kecamatan
					ArrayList<Provinsi> listKecamatan = services.selectListKecamatan(requestProvinsi.getLspr_id(),
							requestProvinsi.getLska_id());
					if (!listKecamatan.isEmpty()) {
						ListIterator<Provinsi> liter = listKecamatan.listIterator();
						while (liter.hasNext()) {
							Provinsi m = liter.next();
							HashMap<String, Object> mapper = new HashMap<>();

							try {
								String KECAMATAN = m.getKecamatan();
								BigDecimal LSKC_ID = m.getLskc_id();
								mapper.put("NAME", KECAMATAN);
								mapper.put("ID", LSKC_ID);
								data.add(mapper);

								error = false;
								message = "Successfully get data kecamatan";
							} catch (Exception e) {
								error = false;
								message = "Failed get data kecamatan";
								resultErr = "Data kecamatan kosong";
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
					} else {
						error = true;
						message = "Failed get data kecamatan";
						resultErr = "Data kecamatan kosong";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (requestCode == 40) { // Kelurahan
					ArrayList<Provinsi> listKelurahan = services.selectListKelurahan(requestProvinsi.getLspr_id(),
							requestProvinsi.getLska_id(), requestProvinsi.getLskc_id());
					if (!listKelurahan.isEmpty()) {
						ListIterator<Provinsi> liter = listKelurahan.listIterator();
						while (liter.hasNext()) {
							Provinsi m = liter.next();
							HashMap<String, Object> mapper = new HashMap<>();

							try {
								String KELURAHAN = m.getKelurahan();
								BigDecimal LSKL_ID = m.getLskl_id();
								mapper.put("NAME", KELURAHAN);
								mapper.put("ID", LSKL_ID);
								data.add(mapper);

								error = false;
								message = "Successfully get data kelurahan";
							} catch (Exception e) {
								error = false;
								message = "Failed get data kelurahan";
								resultErr = "Data kelurahan kosong";
								logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
										+ e);
							}
						}
					} else {
						error = true;
						message = "Failed get data kelurahan";
						resultErr = "Data kelurahan kosong";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else if (requestCode == 50) { // Kodepos
					ArrayList<Provinsi> listKodepos = services.selectListKodePos(requestProvinsi.getLspr_id(),
							requestProvinsi.getLska_id(), requestProvinsi.getLskc_id(), requestProvinsi.getLskl_id());
					if (!listKodepos.isEmpty()) {
						ListIterator<Provinsi> liter = listKodepos.listIterator();
						while (liter.hasNext()) {
							Provinsi m = liter.next();
							HashMap<String, Object> mapper = new HashMap<>();

							if (m != null) {
								try {
									BigDecimal KODEPOS = m.getKodepos();
									mapper.put("NAME", null);
									mapper.put("ID", KODEPOS);

									error = false;
									message = "Successfully get data kodepos";
									data.add(mapper);
								} catch (Exception e) {
									error = false;
									message = "Failed get data kodepos";
									resultErr = "Data kodepos kosong";
									logger.error("Path: " + request.getServletPath() + " Username: " + username
											+ " Error: " + e);
								}
							} else {
								error = false;
								message = "Failed get data kodepos";
								resultErr = "Data kodepos kosong";
							}
						}
					} else {
						error = true;
						message = "Failed get data kodepos";
						resultErr = "Data kodepos kosong";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				} else {
					error = true;
					message = "Request Failed";
					resultErr = "Request code tidak ada dalam pilihan";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				}
			} else {
				error = true;
				message = "Request Failed";
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
		customResourceLoader.insertHistActivityWS(12, 28, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/downloadfaqpdf", method = RequestMethod.GET)
	public String downloadFaqPdf(HttpServletResponse response, HttpServletRequest request) throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson("");
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		try {
			// path file
			String pathWS = pathPdfFAQ;

			// path file yang mau di download
			File file = new File(pathWS);

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
			
		} catch (Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Error: " + e);
		}
		map.put("error", error);
		map.put("message", message);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 42, new Date(), req, res, 0, resultErr, start, null);

		return res;
	}

	@RequestMapping(value = "/trackingpolis", produces = "application/json", method = RequestMethod.POST)
	public String trackingPolis(@RequestBody RequestTrackingPolis requestTrackingPolis, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestTrackingPolis);
		String res = null;
		String message = null;
		String resultErr = null;
		String key = null;
		Integer count_data = 0;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		try {
			String noBlanko = requestTrackingPolis.getNo_blanko();
			String noPolis = requestTrackingPolis.getNo_polis();
			String regSpaj = requestTrackingPolis.getReg_spaj();

			if (noBlanko != null) {
				ArrayList<TrackingPolis> arrayList = services.selectCountDataTrackingPolis(requestTrackingPolis);
				if (!arrayList.isEmpty()) {
					for (Integer i = 0; i < arrayList.size(); i++) {
						try {
							TrackingPolis dataTemp = arrayList.get(i);
							String dataRegSPAJ = dataTemp.getReg_spaj();
							RequestTrackingPolis paramSelectTrackingPolis = new RequestTrackingPolis();
							paramSelectTrackingPolis.setReg_spaj(dataRegSPAJ);
							ArrayList<TrackingPolis> list = services.selectTrackingPolis(paramSelectTrackingPolis);
							ArrayList<Object> dataArray = new ArrayList<>();
							HashMap<String, Object> dataList = new HashMap<>();
							for (Integer j = 0; j < list.size(); j++) {
								try {
									HashMap<String, Object> mapper = new HashMap<>();
									TrackingPolis dataTemp2 = list.get(j);

									String no_polis = dataTemp2.getMspo_policy_no();
									Date tanggal = dataTemp2.getMsps_date();
									String posisi_dokumen = dataTemp2.getLspd_position();
									String status_polis = dataTemp2.getLssp_status();
									String status_accept = dataTemp2.getStatus_accept();
									String keterangan = dataTemp2.getMsps_desc();

									mapper.put("no_polis", no_polis);
									mapper.put("tanggal", tanggal != null ? df.format(tanggal) : null);
									mapper.put("posisi_dokumen", posisi_dokumen);
									mapper.put("status_polis", status_polis);
									mapper.put("status_aksep", status_accept);
									mapper.put("keterangan", keterangan);

									dataArray.add(mapper);
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath() + " Error: " + e);
								}
							}

							error = false;
							message = "Successfully get data";
							dataList.put("reg_spaj", dataRegSPAJ.trim());
							dataList.put("list_process", dataArray);
							data.put("list " + i, dataList);
							count_data = arrayList.size();
							key = noBlanko;
						} catch (Exception e) {
							logger.error("Path: " + request.getServletPath() + " Error: " + e);
						}
					}
				} else {
					error = false;
					message = "No Blanko tidak ditemukan";
				}
			} else if (noPolis != null) {
				ArrayList<TrackingPolis> arrayList = services.selectCountDataTrackingPolis(requestTrackingPolis);
				if (!arrayList.isEmpty()) {
					for (Integer i = 0; i < arrayList.size(); i++) {
						try {
							TrackingPolis dataTemp = arrayList.get(i);
							String dataRegSPAJ = dataTemp.getReg_spaj();
							RequestTrackingPolis paramSelectTrackingPolis = new RequestTrackingPolis();
							paramSelectTrackingPolis.setReg_spaj(dataRegSPAJ);
							ArrayList<TrackingPolis> list = services.selectTrackingPolis(paramSelectTrackingPolis);
							ArrayList<Object> dataArray = new ArrayList<>();
							HashMap<String, Object> dataList = new HashMap<>();
							for (Integer j = 0; j < list.size(); j++) {
								try {
									HashMap<String, Object> mapper = new HashMap<>();
									TrackingPolis dataTemp2 = list.get(j);

									String no_polis = dataTemp2.getMspo_policy_no();
									Date tanggal = dataTemp2.getMsps_date();
									String posisi_dokumen = dataTemp2.getLspd_position();
									String status_polis = dataTemp2.getLssp_status();
									String status_accept = dataTemp2.getStatus_accept();
									String keterangan = dataTemp2.getMsps_desc();

									mapper.put("no_polis", no_polis);
									mapper.put("tanggal", tanggal != null ? df.format(tanggal) : null);
									mapper.put("posisi_dokumen", posisi_dokumen);
									mapper.put("status_polis", status_polis);
									mapper.put("status_aksep", status_accept);
									mapper.put("keterangan", keterangan);

									dataArray.add(mapper);
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath() + " Error: " + e);
								}
							}

							error = false;
							message = "Successfully get data";
							dataList.put("reg_spaj", dataRegSPAJ.trim());
							dataList.put("list_process", dataArray);
							data.put("list " + i, dataList);
							count_data = arrayList.size();
							key = noPolis;
						} catch (Exception e) {
							logger.error("Path: " + request.getServletPath() + " Error: " + e);
						}
					}
				} else {
					error = false;
					message = "No Polis tidak ditemukan";
				}
			} else if (regSpaj != null) {
				ArrayList<TrackingPolis> arrayList = services.selectCountDataTrackingPolis(requestTrackingPolis);
				if (!arrayList.isEmpty()) {
					for (Integer i = 0; i < arrayList.size(); i++) {
						try {
							TrackingPolis dataTemp = arrayList.get(i);
							String dataRegSPAJ = dataTemp.getReg_spaj();
							RequestTrackingPolis paramSelectTrackingPolis = new RequestTrackingPolis();
							paramSelectTrackingPolis.setReg_spaj(dataRegSPAJ);
							ArrayList<TrackingPolis> list = services.selectTrackingPolis(paramSelectTrackingPolis);
							ArrayList<Object> dataArray = new ArrayList<>();
							HashMap<String, Object> dataList = new HashMap<>();
							for (Integer j = 0; j < list.size(); j++) {
								try {
									HashMap<String, Object> mapper = new HashMap<>();
									TrackingPolis dataTemp2 = list.get(j);

									String no_polis = dataTemp2.getMspo_policy_no();
									Date tanggal = dataTemp2.getMsps_date();
									String posisi_dokumen = dataTemp2.getLspd_position();
									String status_polis = dataTemp2.getLssp_status();
									String status_accept = dataTemp2.getStatus_accept();
									String keterangan = dataTemp2.getMsps_desc();

									mapper.put("no_polis", no_polis);
									mapper.put("tanggal", tanggal != null ? df.format(tanggal) : null);
									mapper.put("posisi_dokumen", posisi_dokumen);
									mapper.put("status_polis", status_polis);
									mapper.put("status_aksep", status_accept);
									mapper.put("keterangan", keterangan);

									dataArray.add(mapper);
								} catch (Exception e) {
									logger.error("Path: " + request.getServletPath() + " Error: " + e);
								}
							}

							error = false;
							message = "Successfully get data";
							dataList.put("reg_spaj", dataRegSPAJ.trim());
							dataList.put("list_process", dataArray);
							data.put("list " + i, dataList);
							count_data = arrayList.size();
							key = regSpaj;
						} catch (Exception e) {
							logger.error("Path: " + request.getServletPath() + " Error: " + e);
						}
					}
				} else {
					error = false;
					message = "REG SPAJ tidak ditemukan";
				}
			} else {
				error = true;
				message = "Maaf pencarian dengan metode lain belum tersedia";
				resultErr = "Maaf pencarian dengan metode lain belum tersedia";
				logger.error("Path: " + request.getServletPath() + " Error: " + resultErr);
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
		map.put("count_data", count_data);
		res = gson.toJson(map);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 43, new Date(), req, res, 0, resultErr, start, key);

		return res;
	}

	@RequestMapping(value = "/emailmergesimultan", produces = "application/json", method = RequestMethod.POST)
	public String emailCSMergeSimultan(@RequestBody RequestEmailCSMergeSimultan requestEmailCSMergeSimultan,
			HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestEmailCSMergeSimultan);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();

		String username = requestEmailCSMergeSimultan.getUsername();
		String key = requestEmailCSMergeSimultan.getKey();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				User dataUser = services.selectDataForEmailCS(username);
				if (dataUser == null) {
					error = true;
					message = "Send Email to CS Failed";
					resultErr = "Gagal mengirim email karena data username kosong";
					logger.error(
							"Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
				} else {
					String name = (String) dataUser.getMcl_first();
					String dob = (String) df2.format(dataUser.getMspe_date_birth());
					String reg_spaj = (String) dataUser.getReg_spaj();
					String no_hp1 = (String) dataUser.getNo_hp();
					String no_hp2 = (String) dataUser.getNo_hp2();
					String no_hp = no_hp1 != null ? no_hp1 : no_hp2;
					String messageHTML = "<span style='font-family: helvetica; font-size: 14px;'>Dear CS,</span><br/><br/><br/><span style='font-family: helvetica; font-size: 14px;'>Mohon lakukan merge id simultan menggunakan patokan nomor SPAJ dibawah:</span></br> <table style='width: 50%; font-family: helvetica; font-size: 14px;'> <tbody> <tr> <td style='width: 50.0000%;'>Username <br> </td> <td style='width: 50.0000%;'>: "
							+ username
							+ " <br> </td> </tr> <tr> <td style='width: 50.0000%;'>REG SPAJ <br> </td> <td style='width: 50.0000%;'>: "
							+ reg_spaj
							+ " <br> </td> </tr> <tr> <td style='width: 50.0000%;'>Nama Pemegang Polis <br> </td> <td style='width: 50.0000%;'>: "
							+ name
							+ " <br> </td> </tr> <tr> <td style='width: 50.0000%;'>Tanggal Lahir <br> </td> <td style='width: 50.0000%;'>: "
							+ dob
							+ " <br> </td> </tr> <tr> <td style='width: 50.0000%;'>Nomor Handphone <br> </td> <td style='width: 50.0000%;'>: "
							+ no_hp
							+ " <br> </td> </tr> </tbody> </table><br/><span style='font-family: helvetica; font-size: 14px;'>Email ini terkirim secara otomatis oleh sistem, harap jangan mereply email ini.</span> </br><br/><br/><span style='font-family: helvetica; font-size: 14px;'>Terima Kasih</span>";

					String result = customResourceLoader.sendEmail("marisya.islamy@sinarmasmsiglife.co.id;", "", "",
							"Merge id Simultan M-Polis SPAJ: " + reg_spaj, messageHTML, true, "");

					JSONObject myResponse = new JSONObject(result.toString());
					Boolean errorPost = (Boolean) myResponse.get("error");
					if (errorPost == false) {
						error = false;
						message = "Send Email to CS Success";
					} else {
						error = true;
						message = "Send Email to CS Failed";
						resultErr = "Error hit API Send Email";
						logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
								+ resultErr);
					}
				}
			} else {
				error = true;
				message = "Send Email to CS Failed";
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
		customResourceLoader.insertHistActivityWS(12, 45, new Date(), req, res, 0, resultErr, start, username);

		return res;
	}

	@RequestMapping(value = "/statusemailcs", produces = "application/json", method = RequestMethod.POST)
	public String enableEmailCS(@RequestBody RequestStatusEmailCS requestStatusEmailCS, HttpServletRequest request) {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestStatusEmailCS);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();

		String username = requestStatusEmailCS.getUsername();
		String key = requestStatusEmailCS.getKey();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				User dataUser = services.selectDataUser(username, null, null);

				Date mspe_date_birth_date = dataUser.getMspe_date_birth();
				String mcl_first = dataUser.getMcl_first();
				String mspe_date_birth = df2.format(mspe_date_birth_date);
				Integer countLstUlangan = services.selectCountLstUlangan(username);
				Integer countIdSimultan = services.selectCountIdSimultanByUsername(mcl_first, mspe_date_birth);
				Boolean enable_emailcs = true;
				Boolean show_button_emailcs = false;

				if (countIdSimultan > 1) {
					show_button_emailcs = true;
				}

				if (countLstUlangan > 0) {
					enable_emailcs = false;
				}

				error = false;
				message = "Successfully get data";
				data.put("enable_emailcs", enable_emailcs);
				data.put("show_button_emailcs", show_button_emailcs);
			} else {
				error = true;
				message = "Can't get data";
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
		customResourceLoader.insertHistActivityWS(12, 48, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}
	
	/*@RequestMapping(value = "/viewbeneficiary", produces = "application/json", method = RequestMethod.POST)
	public String viewBeneficiary(@RequestBody RequestViewBeneficiary requestViewBeneficiary, HttpServletRequest request)
			throws Exception {
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = gson.toJson(requestViewBeneficiary);
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = true;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> data = new ArrayList<>();

		String username = requestViewBeneficiary.getUsername();
		String key = requestViewBeneficiary.getKey();
		String no_polis = requestViewBeneficiary.getNo_polis();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				// Get REG_SPAJ
				Pemegang paramCheckSpaj = new Pemegang();
				paramCheckSpaj.setMspo_policy_no(no_polis);
				Pemegang dataSpaj = services.selectGetSPAJ(paramCheckSpaj);

				ArrayList<Beneficiary> listbeneficiary = services.selectKlaimkesehatan(dataSpaj.getReg_spaj(),
						requestViewClaim.getPageNumber(), requestViewClaim.getPageSize(),
						requestViewClaim.getStartDate(), requestViewClaim.getEndDate());
				if (!listklaim.isEmpty()) {
					ListIterator<KlaimKesehatan> liter = listklaim.listIterator();
					while (liter.hasNext()) {
						try {
							KlaimKesehatan m = liter.next();
							HashMap<String, Object> mapper = new HashMap<>();

							Date tglRawat = m.getRegapldate();
							Date tanggalStatus = m.getClm_paid_date();
							String jenisKlaim = m.getJenis_claim();
							String diagnosa = m.getNm_diagnos();
							String lkuSymbol = m.getLku_symbol();
							BigDecimal JumlahKlaim = m.getAmt_claim();
							BigDecimal jumlahBayar = m.getPay_claim();
							BigDecimal id_status = m.getId_status_accept();

							mapper.put("jumlah_klaim", JumlahKlaim);
							mapper.put("tanggal_rawat", tglRawat != null ? df.format(tglRawat) : null);
							mapper.put("jenis_klaim", jenisKlaim);
							mapper.put("diagnosa", diagnosa);
							mapper.put("jumlah_bayar", jumlahBayar);
							mapper.put("tanggal", tanggalStatus != null ? df.format(tanggalStatus) : null);
							mapper.put("currency", lkuSymbol);

							if ((id_status.intValue() == 1 || id_status.intValue() == 2
									|| id_status.intValue() == 16)) {
								mapper.put("status_paid_id", 1);
							} else if ((id_status.intValue() == 10 || id_status.intValue() == 20)) {
								mapper.put("status_paid_id", 2);
							} else if (id_status.intValue() == 5 || id_status.intValue() == 15) {
								mapper.put("status_paid_id", 3);
							}

							error = false;
							message = "Successfully get data claim";
							data.add(mapper);
						} catch (Exception e) {
							logger.error(
									"Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
						}
					}
				} else {
					error = false;
					message = "Data claim kosong";
					resultErr = "Data claim kosong";
				}
			} else {
				error = true;
				message = "Can't get data claim";
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
		customResourceLoader.insertHistActivityWS(12, 22, new Date(), req, res, 1, resultErr, start, username);

		return res;
	}*/
	
}