package com.app.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.mapper.VegaMapper;
import com.app.controller.TestController;
import com.app.model.Withdraw;
import com.app.model.request.RequestGeneratePdfWithdrawManual;
import com.app.services.VegaServices;
import com.app.utils.CustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.DocumentException;

import io.swagger.annotations.ApiOperation;

@RestController
public class TestController {
	
	private static final Logger logger = LogManager.getLogger(TestController.class);
	private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
	private DateFormat df3 = new SimpleDateFormat("yyyyMM");
	
	@Autowired
	private VegaServices vegaServices;
	
	@ApiOperation(value="Service untu melakukan test memastikan semua aplikasi berhubungan database jalan ")
	@RequestMapping(value = "/testhit", method = RequestMethod.GET)
	public String testHit() {
		String printHit = vegaServices.selectEncrypt("test");
		return printHit;
	}
	


	/*
	 * Controller test ini gue bikin buat ngetest apakah API nya jalan dan apakah
	 * koneksi database bisa diakses juga, ada juga potongan API yang gue test
	 * disini
	 */



	@Autowired
	private CustomResourceLoader customResourceLoader;

	@Value("${link.fcm.google}")
	private String linkFcmGoogle;

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private NumberFormat nfZeroTwo = new DecimalFormat("#,##0.00;(#,##0.00)");

	@RequestMapping(value = "/generatepdfwithdrawmanual", method = RequestMethod.POST)
	public String generatePdfWithdrawManual(
			@RequestBody RequestGeneratePdfWithdrawManual requestGeneratePdfWithdrawManual) {

		String message = "OK";
		Withdraw dataFormWithdraw = vegaServices.selectDataFormWithdraw(requestGeneratePdfWithdrawManual.getReg_spaj());

		BigDecimal lsbs_id_generate = dataFormWithdraw.getLsbs_id();
		BigDecimal lsdbs_number_generate = dataFormWithdraw.getLsdbs_number();
		String nm_product_generate = dataFormWithdraw.getNm_product() != null
				? dataFormWithdraw.getNm_product().toString()
				: "";
		String no_hp_generate = dataFormWithdraw.getNo_hp() != null ? dataFormWithdraw.getNo_hp().toString() : "";
		String email_generate = dataFormWithdraw.getEmail() != null ? dataFormWithdraw.getEmail().toString() : "";

		try {
			customResourceLoader.generatePdfWithdraw(lsbs_id_generate.intValue(), lsdbs_number_generate.intValue(),
					nm_product_generate, requestGeneratePdfWithdrawManual.getNo_polis(),
					requestGeneratePdfWithdrawManual.getPayor_name(), no_hp_generate, email_generate,
					requestGeneratePdfWithdrawManual.getData_fund(), requestGeneratePdfWithdrawManual.getPath_output());
		} catch (IOException e) {
			e.printStackTrace();
			message = e.getMessage();
		} catch (DocumentException e) {
			e.printStackTrace();
			message = e.getMessage();
		}

		return message;
	}


	@RequestMapping(value = "/test6", method = RequestMethod.GET)
	public String test6() {
		return df3.format(new Date());
	}

	@RequestMapping(value = "/test7", method = RequestMethod.GET)
	public String test7(@RequestParam String mataUang, @RequestParam String payMethod,
			@RequestParam BigDecimal mpt_jumlah) {

		HashMap<String, Object> dataConfiguration = vegaServices.configuration();
		String messagePushNotif = (String) dataConfiguration.get("KEYTEST");

		return messagePushNotif;
	}

	@RequestMapping(value = "/test5", method = RequestMethod.GET)
	public String test5() {
		String a = "123ASA";
		String b = "123AsA";
		String c = "123asa";
		String d = "123a";
		String e = "aa123a";

		System.out.println(a.replaceAll("[^0-9]", "").trim());
		System.out.println(b.replaceAll("[^0-9]", ""));
		System.out.println(c.replaceAll("[^0-9]", ""));
		System.out.println(d.replaceAll("[^0-9]", ""));
		System.out.println(e.replaceAll("[^0-9]", ""));

		return "OK";
	}

	@RequestMapping(value = "/checkotp", method = RequestMethod.GET)
	public String kodeOtp(@RequestParam String no_hp, @RequestParam Integer jenis_id, @RequestParam Integer menu_id) {
		String code = vegaServices.selectCheckOTP(no_hp, jenis_id, menu_id);

		return code;
	}

	@RequestMapping(value = "/test3", method = RequestMethod.GET)
	public String testing3() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		HashMap<String, Object> map = new HashMap<>();

		String res = null;

		LocalDate sysdate = LocalDate.now();
//		LocalDate sysdate = LocalDate.of(2020, 9, 12);
		String dateParse = sysdate.format(formatter);
		Integer checkDate = vegaServices.selectCheckDate(dateParse);

		String cutOffLink = vegaServices.selectCutoffTransactionLink();
		String cutOffMagnaAndPrimeLink = vegaServices.selectCutoffTransactionMagnaAndPrimeLink();

		LocalTime cutOffLinkTime = LocalTime.parse(cutOffLink);
		LocalTime cutOffMagnaAndPrimeLinkTime = LocalTime.parse(cutOffMagnaAndPrimeLink);

		Integer group_product = 1;

		String dateResult = dateParse;
		if (checkDate.equals(1)) {
			// Hari Kerja
			if (group_product.equals(1)) {
				LocalTime timeNow = LocalTime.now();
				// LocalTime timeNow = LocalTime.parse("08:00:00");
				System.out.println(timeNow);
				// Check cutoff Link
				if (timeNow.compareTo(cutOffLinkTime) > 0) {
					dateResult = customResourceLoader.getWorkingDate();
				}
			} else {
				LocalTime timeNow = LocalTime.now();
				System.out.println(timeNow);
				// Check cutoff Prime Link & Magna Link
				if (timeNow.compareTo(cutOffMagnaAndPrimeLinkTime) > 0) {
					dateResult = customResourceLoader.getWorkingDate();
				}
			}
		} else {
			// Hari Libur
			dateResult = customResourceLoader.getWorkingDate();
		}

		map.put("data", dateResult);
		res = gson.toJson(map);

		return res;
	}

	@RequestMapping(value = "/test4", method = RequestMethod.GET)
	public String testing4() {
		String lsbs_idToStr = "213";
		String lsdbs_numberToStr = "5";
		String combinationProductCode = lsbs_idToStr + lsdbs_numberToStr;
		System.out.println(combinationProductCode);
		Integer group_product = 0;

		if (lsbs_idToStr.equals("213") || lsbs_idToStr.equals("216")) {
			group_product = 1;
		} else if (combinationProductCode.equals("1345") || combinationProductCode.equals("13410")
				|| combinationProductCode.equals("13411") || combinationProductCode.equals("13412")
				|| combinationProductCode.equals("2151")) {
			group_product = 1;
		}

		String dateTransaction = customResourceLoader.getDateTransaction(group_product);

		return dateTransaction;
	}

//	@RequestMapping(value = "/test2", method = RequestMethod.GET)
//	public String testing() {
//
//		GsonBuilder builder = new GsonBuilder();
//		builder.serializeNulls();
//		Gson gson = new Gson();
//		gson = builder.create();
//		HashMap<String, Object> map = new HashMap<>();
//		String reg_spaj_result = "09180235837";
//		String res = null;
//
//		ArrayList<UnitLink> listSourceFund = services.selectUnitLink(reg_spaj_result);
//		ListIterator<UnitLink> liter2 = listSourceFund.listIterator();
//		ArrayList<HashMap<String, Object>> investment = new ArrayList<HashMap<String, Object>>();
//		while (liter2.hasNext()) {
//			try {
//				UnitLink m = liter2.next();
//				String lji_id = m.getLji_id();
//				String lji_invest = m.getLji_invest();
//				String lnu_tgl = df1.format(m.getLnu_tgl());
//				String lku_symbol = m.getLku_symbol();
//				BigDecimal harga_Unit = new BigDecimal(m.getHarga_Unit());
//				BigDecimal nilai = new BigDecimal(m.getNilai_polis());
//				BigDecimal total_Unit = new BigDecimal(m.getTotal_Unit());
//
//				if (nilai != BigDecimal.ZERO) {
//					HashMap<String, Object> tempData = new HashMap<>();
//					tempData.put("lji_id", lji_id);
//					tempData.put("fund", lji_invest);
//					tempData.put("date", lnu_tgl);
//					tempData.put("currency", lku_symbol);
//					tempData.put("policy_value", nilai.setScale(2));
//					tempData.put("unit_price", harga_Unit.setScale(4).doubleValue());
//					tempData.put("total_unit", total_Unit.setScale(4).doubleValue());
//
//					investment.add(tempData);
//				}
//			} catch (Exception e) {
//				System.out.println(e);
//			}
//		}
//
//		map.put("data", investment);
//		res = gson.toJson(map);
//
//		return res;
//	}

	/*
	 * @RequestMapping(value = "/testfunction", method = RequestMethod.GET) public
	 * String testFunction() { int days = 1; LocalDate dateRedeemed =
	 * LocalDate.now(); // LocalDate dateRedeemed = LocalDate.of(2020, 07, 30);
	 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	 * LocalDate newDate = dateRedeemed.plusDays(days); String dateString =
	 * newDate.format(formatter);
	 * 
	 * Integer checkDate = services.selectCheckDate(dateString); String dateValue =
	 * null;
	 * 
	 * if (checkDate.equals(1)) { dateValue = dateString; } else { int x = 0; int y
	 * = 1; int days2 = 2;
	 * 
	 * while (x < y) { LocalDate dateRedeemed2 = LocalDate.now(); // LocalDate
	 * dateRedeemed2 = LocalDate.of(2020, 07, 30); LocalDate newDate2 =
	 * dateRedeemed2.plusDays(days2); String dateString2 =
	 * newDate2.format(formatter); Integer checkDate2 =
	 * services.selectCheckDate(dateString2); if (checkDate2.equals(1)) { dateValue
	 * = dateString2; x++; } else { x++; y++; System.out.println(dateString2);
	 * days2++; } } }
	 * 
	 * return dateValue; }
	 */

//	@RequestMapping(value = "/generatepdfsubmit", method = RequestMethod.GET)
//	public String generatePdfMagnaLink() throws IOException, DocumentException {
//
//		String path_output = "D:\\FormRawatInap-edited.pdf";
//
//		String mpc_id = "0000000000";
//		String nm_pemegang_polis = "Muh. Fajar Sofyana Eka Putra";
//		String nama_pasien = "Muh Putra";
//		String status_pasien = "Diri Sendiri";
//		String alamat_nohp = "Jl. Batu Ampar 3, No. 76, Jakarta Timur/ 087886328859";
//		Integer umur_pasien = 17;
//		String email = "mfajarsep@gmail.com";
//		String lama_rawat = "3 Hari";
//
//		customResourceLoader.generatePdfClaimSubmission(mpc_id, nm_pemegang_polis, nama_pasien, status_pasien, alamat_nohp,
//				umur_pasien, email, lama_rawat, path_output);
//
//		return "OK";
//	}

//	@RequestMapping(value = "/generatepdf", method = RequestMethod.GET)
//	public String generatePdf() throws IOException, DocumentException {
//
//		String no_polis = "09134201701028";
//		String name = "Muhamad Fajar Sofyana Eka Putra";
//		String phone_no = "087886328859";
//		String email = "mfajarsep@gmail.com";
//
//		PdfReader reader = new PdfReader("D:/Data/Kantor/M-Polis/Document_Withdraw/Before/Form.pdf"); // input PDF
//		PdfStamper stamper = new PdfStamper(reader,
//				new FileOutputStream("D:/Data/Kantor/M-Polis/Document_Withdraw/After/Form_edited.pdf")); // output PDF
//		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); // set font
//
//		// loop on pages (1-based)
//		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
//
//			// get object for writing over the existing content;
//			// you can also use getUnderContent for writing in the bottom layer
//			PdfContentByte over = stamper.getOverContent(i);
//
//			// Write Product Name
//			over.beginText();
//			over.setFontAndSize(bf, 7); // set font and size
//			over.setTextMatrix(114, 784); // set x,y position (0,0 is at the bottom left)
//			over.showText("FAJAR LINK"); // set text number 0
//			over.endText();
//
//			// Write policy number
//			for (int a = 0; a < no_polis.length(); a++) {
//				over.beginText();
//				over.setFontAndSize(bf, 7); // set font and size
//				if (a == 0) {
//					over.setTextMatrix(185, 760); // set x,y position (0,0 is at the bottom left)
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 0
//				} else if (a == 1) {
//					over.setTextMatrix(202, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 2
//				} else if (a == 2) {
//					over.setTextMatrix(237, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 3
//				} else if (a == 3) {
//					over.setTextMatrix(254, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 4
//				} else if (a == 4) {
//					over.setTextMatrix(271, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 5
//				} else if (a == 5) {
//					over.setTextMatrix(305, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 6
//				} else if (a == 6) {
//					over.setTextMatrix(322, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 7
//				} else if (a == 7) {
//					over.setTextMatrix(339, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 8
//				} else if (a == 8) {
//					over.setTextMatrix(356, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 9
//				} else if (a == 9) {
//					over.setTextMatrix(391, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 10
//				} else if (a == 10) {
//					over.setTextMatrix(408, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 11
//				} else if (a == 11) {
//					over.setTextMatrix(425, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 12
//				} else if (a == 12) {
//					over.setTextMatrix(442, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 13
//				} else if (a == 13) {
//					over.setTextMatrix(459, 760);
//					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 14
//				}
//
//				over.endText();
//			}
//
//			// Write Pemegang Polis
//			over.beginText();
//			over.setFontAndSize(bf, 7); // set font and size
//			over.setTextMatrix(180, 748);
//			over.showText(name);
//			over.endText();
//
//			// Write No Handphone
//			for (int b = 0; b < phone_no.length(); b++) {
//				over.beginText();
//				over.setFontAndSize(bf, 7); // set font and size
//				if (b == 0) {
//					over.setTextMatrix(185, 703); // set x,y position (0,0 is at the bottom left)
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 1
//				} else if (b == 1) {
//					over.setTextMatrix(202, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 2
//				} else if (b == 2) {
//					over.setTextMatrix(219, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 3
//				} else if (b == 3) {
//					over.setTextMatrix(236, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 4
//				} else if (b == 4) {
//					over.setTextMatrix(271, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 5
//				} else if (b == 5) {
//					over.setTextMatrix(288, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 6
//				} else if (b == 6) {
//					over.setTextMatrix(305, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 7
//				} else if (b == 7) {
//					over.setTextMatrix(322, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 8
//				} else if (b == 8) {
//					over.setTextMatrix(339, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 9
//				} else if (b == 9) {
//					over.setTextMatrix(356, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 10
//				} else if (b == 10) {
//					over.setTextMatrix(373, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 11
//				} else if (b == 11) {
//					over.setTextMatrix(390, 703);
//					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 12
//				}
//
//				over.endText();
//			}
//
//			// Write Email
//			over.beginText();
//			over.setFontAndSize(bf, 7); // set font and size
//			over.setTextMatrix(180, 691);
//			over.showText(email);
//			over.endText();
//
//			// Write Checklist rekening
//			over.beginText();
//			over.setFontAndSize(bf, 7); // set font and size
//			over.setTextMatrix(105, 667);
//			over.showText("X");
//			over.endText();
//
//			// Write Checklist withdraw
//			over.beginText();
//			over.setFontAndSize(bf, 7); // set font and size
//			over.setTextMatrix(186, 601);
//			over.showText("X");
//			over.endText();
//
//			// Write Fund2
//			ArrayList<HashMap<String, Object>> dataFund = new ArrayList<>();
//			if (!dataFund.isEmpty()) {
//				over.beginText();
//				over.setFontAndSize(bf, 7); // set font and size
//				for (int x = 0; x < dataFund.size(); x++) {
//					String lji_id = (String) dataFund.get(x).get("LJI_ID");
//					BigDecimal mpt_jumlah = (BigDecimal) dataFund.get(x).get("MPT_JUMLAH");
//					BigDecimal mpt_unit = (BigDecimal) dataFund.get(x).get("MPT_UNIT");
//
//					if (lji_id.equals("35")) {
//						// Simas Fixed Income (RP)
//						if (mpt_jumlah != BigDecimal.ZERO) {
//							over.setTextMatrix(179, 565);
//							over.showText(nfZeroTwo.format(mpt_jumlah)); // Simas Fixed Income (RP)
//						} else {
//							over.setTextMatrix(317, 565);
//							over.showText(nfZeroTwo.format(mpt_unit)); // Simas Fixed Income (Unit)
//						}
//					} else if (lji_id.equals("36")) {
//						// Simas Dynamic (RP)
//						if (mpt_jumlah != BigDecimal.ZERO) {
//							over.setTextMatrix(179, 553);
//							over.showText(nfZeroTwo.format(mpt_jumlah)); // Simas Dynamic (RP)
//						} else {
//							over.setTextMatrix(317, 553);
//							over.showText(nfZeroTwo.format(mpt_unit)); // Simas Dynamic (Unit)
//						}
//					} else if (lji_id.equals("37")) {
//						// Simas Aggressive (RP)
//						if (mpt_jumlah != BigDecimal.ZERO) {
//							over.setTextMatrix(179, 540);
//							over.showText(nfZeroTwo.format(mpt_jumlah)); // Simas Aggressive (RP)
//						} else {
//							over.setTextMatrix(317, 540);
//							over.showText(nfZeroTwo.format(mpt_unit)); // Simas Aggressive (Unit)
//						}
//					} else if (lji_id.equals("01")) {
//						// Excellink Fixed Income (RP)
//						if (mpt_jumlah != BigDecimal.ZERO) {
//							over.setTextMatrix(179, 528);
//							over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Fixed Income (RP)
//						} else {
//							over.setTextMatrix(317, 528);
//							over.showText(nfZeroTwo.format(mpt_unit)); // Excellink Fixed Income (Unit)
//						}
//					} else if (lji_id.equals("02")) {
//						// Excellink Dynamic (RP)
//						if (mpt_jumlah != BigDecimal.ZERO) {
//							over.setTextMatrix(179, 515);
//							over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Dynamic (RP)
//						} else {
//							over.setTextMatrix(317, 515);
//							over.showText(nfZeroTwo.format(mpt_unit)); // Excellink Dynamic (Unit)
//						}
//					} else if (lji_id.equals("03")) {
//						// Excellink Aggressive (RP)
//						if (mpt_jumlah != BigDecimal.ZERO) {
//							over.setTextMatrix(179, 503);
//							over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Aggressive (RP)
//						} else {
//							over.setTextMatrix(317, 503);
//							over.showText(nfZeroTwo.format(mpt_unit)); // Excellink Aggressive (Unit)
//						}
//					} else if (lji_id.equals("61")) {
//						// Excellink Cash (RP)
//						if (mpt_jumlah != BigDecimal.ZERO) {
//							over.setTextMatrix(179, 491);
//							over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Cash (RP)
//						} else {
//							over.setTextMatrix(317, 491);
//							over.showText(nfZeroTwo.format(mpt_unit)); // Excellink Cash (Unit)
//						}
//					} else if (lji_id.equals("63")) {
//						// Excellink Equity Bakti Peduli (RP)
//						if (mpt_jumlah != BigDecimal.ZERO) {
//							over.setTextMatrix(179, 479);
//							over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Equity Bakti Peduli (RP)
//						} else {
//							over.setTextMatrix(317, 479);
//							over.showText(nfZeroTwo.format(mpt_unit)); // Excellink Equity Bakti Peduli (Unit)
//						}
//					} else if (lji_id.equals("04")) {
//						// Excellink Secure Dollar Income (USD)
//						if (mpt_jumlah != BigDecimal.ZERO) {
//							over.setTextMatrix(179, 467);
//							over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Secure Dollar Income (USD)
//						} else {
//							over.setTextMatrix(317, 467);
//							over.showText(nfZeroTwo.format(mpt_unit)); // Excellink Secure Dollar Income (Unit)
//						}
//					} else if (lji_id.equals("05")) {
//						// Excellink Dynamic Dollar (USD)
//						if (mpt_jumlah != BigDecimal.ZERO) {
//							over.setTextMatrix(179, 455);
//							over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Dynamic Dollar (USD)
//						} else {
//							over.setTextMatrix(317, 455);
//							over.showText(nfZeroTwo.format(mpt_unit)); // Excellink Dynamic Dollar (Unit)
//						}
//					}
//				}
//				over.endText();
//			}
//
//			// Write Tanda tangan
//			over.beginText();
//			over.setFontAndSize(bf, 5); // set font and size
//			over.setTextMatrix(80, 84);
//			over.showText(name);
//			over.endText();
//		}
//
//		stamper.close();
//
//		return "OK";
//	}

	// LOGIN
	/*
	 * @RequestMapping(value = "/login", produces = "application/json", method =
	 * RequestMethod.POST) public String loginNew(@RequestBody RequestLogin
	 * requestLogin, HttpServletRequest request) throws Exception { Date start = new
	 * Date(); GsonBuilder builder = new GsonBuilder(); builder.serializeNulls();
	 * Gson gson = new Gson(); gson = builder.create(); String req =
	 * gson.toJson(requestLogin); String res = null; String resultErr = null; String
	 * message = null; Boolean error = false; HashMap<String, Object> map = new
	 * HashMap<>(); HashMap<String, Object> data = new HashMap<>(); try { String key
	 * = null; String lastLoginDevice = requestLogin.getLast_login_device();
	 * LstUserSimultaneous lstUserSimultaneous = new LstUserSimultaneous();
	 * lstUserSimultaneous.setUSERNAME(requestLogin.getUsername());
	 * lstUserSimultaneous.setLAST_LOGIN_DEVICE(lastLoginDevice);
	 * lstUserSimultaneous.setFLAG_ACTIVE(1); LstUserSimultaneous user1 =
	 * services.selectLoginAuthenticate(lstUserSimultaneous); User dataActivityUser
	 * = services.selectActivityUser(requestLogin.getUsername()); if
	 * (dataActivityUser != null) { Integer countDeathClaim =
	 * services.selectCountDeathClaim(requestLogin.getUsername()); if
	 * (countDeathClaim > 0) { error = true; message =
	 * "Policy number exists containing death claims"; resultErr =
	 * "No polis pada username ini ada yang mengandung death claim";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLogin.getUsername() + " Error: " + resultErr); } else { if
	 * (dataActivityUser.getLast_login_device() != null) { if
	 * (dataActivityUser.getLast_login_device().equals(lastLoginDevice)) { if
	 * (user1.getPASSWORD().equals(requestLogin.getPassword())) { // check the
	 * password ArrayList<Object> list = MapperServices
	 * .serializableList(services.selectDetailedPolis(requestLogin.getUsername()));
	 * if (list.size() > 0) { String today = df.format(new Date());
	 * lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
	 * lstUserSimultaneous.setUPDATE_DATE_TIME(today);
	 * services.updateUserKeyName(lstUserSimultaneous); key = user1.getKEY();
	 * 
	 * error = false; message = "Login success"; data.put("key", key);
	 * data.put("no_hp", dataActivityUser.getNo_hp() != null ?
	 * dataActivityUser.getNo_hp() : dataActivityUser.getNo_hp2()); } else { error =
	 * true; message = "List policy is empty"; resultErr = "List Polis Kosong";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLogin.getUsername() + " Error: " + resultErr); } } else { error =
	 * true; message = "Login failed"; resultErr = "Password yang dimasukkan salah";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLogin.getUsername() + " Error: " + resultErr); } } else { Date
	 * dateActivity = dataActivityUser.getUpdate_date(); Date dateNow = new Date();
	 * long diff = dateNow.getTime() - dateActivity.getTime(); long diffSeconds =
	 * diff / 1000; if (diffSeconds <= 900) { error = true; message =
	 * "Session still active"; resultErr =
	 * "Account sedang login ditempat lain atau ada session yang belum berakhir";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLogin.getUsername() + " Error: " + resultErr); } else { if
	 * (user1.getPASSWORD() == null || user1.getPASSWORD() == "") { error = true;
	 * message = "Login failed"; resultErr = "Format password incorect";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLogin.getUsername() + " Error: " + resultErr); } else { if
	 * (user1.getPASSWORD().equals(requestLogin.getPassword())) { // check the
	 * password ArrayList<Object> list = MapperServices.serializableList(
	 * services.selectDetailedPolis(requestLogin.getUsername())); if (list.size() >
	 * 0) { try { customResourceLoader.postGoogle(linkFcmGoogle,
	 * requestLogin.getUsername(), dataActivityUser.getLast_login_device());
	 * 
	 * String today = df.format(new Date());
	 * lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
	 * lstUserSimultaneous.setUPDATE_DATE_TIME(today);
	 * services.updateUserKeyName(lstUserSimultaneous); key = user1.getKEY();
	 * 
	 * error = false; message = "Login success"; data.put("key", key);
	 * data.put("no_hp", dataActivityUser.getNo_hp() != null ?
	 * dataActivityUser.getNo_hp() : dataActivityUser.getNo_hp2()); } catch
	 * (Exception e) { logger.error("Path: " + request.getServletPath() +
	 * " Username: " + requestLogin.getUsername() + " Error: " + e); } } else {
	 * error = true; message = "List policy is empty"; resultErr =
	 * "List Polis Kosong"; logger.error("Path: " + request.getServletPath() +
	 * " Username: " + requestLogin.getUsername() + " Error: " + resultErr); } }
	 * else { error = true; message = "Login failed"; resultErr =
	 * "Password yang dimasukkan salah"; logger.error("Path: " +
	 * request.getServletPath() + " Username: " + requestLogin.getUsername() +
	 * " Error: " + resultErr); } } } } } else { if (user1.getPASSWORD() == null ||
	 * user1.getPASSWORD() == "") { error = true; message = "Login failed";
	 * resultErr = "Format password incorect"; logger.error("Path: " +
	 * request.getServletPath() + " Username: " + requestLogin.getUsername() +
	 * " Error: " + resultErr); } else { if
	 * (user1.getPASSWORD().equals(requestLogin.getPassword())) { // check the
	 * password ArrayList<Object> list = MapperServices
	 * .serializableList(services.selectDetailedPolis(requestLogin.getUsername()));
	 * if (list.size() > 0) { String today = df.format(new Date());
	 * lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
	 * lstUserSimultaneous.setUPDATE_DATE_TIME(today);
	 * services.updateUserKeyName(lstUserSimultaneous); key = user1.getKEY();
	 * 
	 * error = false; message = "Login success"; data.put("key", key);
	 * data.put("no_hp", dataActivityUser.getNo_hp() != null ?
	 * dataActivityUser.getNo_hp() : dataActivityUser.getNo_hp2()); } else { error =
	 * true; message = "List policy is empty"; resultErr = "List Polis Kosong";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLogin.getUsername() + " Error: " + resultErr); } } else { error =
	 * true; message = "Login failed"; resultErr = "Password yang dimasukkan salah";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLogin.getUsername() + " Error: " + resultErr); } } } } } else { error
	 * = true; message = "Login failed"; resultErr = "Username tidak terdaftar";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLogin.getUsername() + " Error: " + resultErr); } } catch (Exception e)
	 * { error = true; message = ResponseMessage.ERROR_SYSTEM; resultErr =
	 * "bad exception " + e; logger.error( "Path: " + request.getServletPath() +
	 * " Username: " + requestLogin.getUsername() + " Error: " + e); }
	 * map.put("error", error); map.put("message", message); map.put("data", data);
	 * res = gson.toJson(map); // Insert Log LST_HIST_ACTIVITY_WS
	 * customResourceLoader.insertHistActivityWS(12, 1, new Date(), req, res, 1,
	 * resultErr, start, requestLogin.getUsername());
	 * 
	 * return res; }
	 */

	// LOGIN EASYPIN
	/*
	 * @RequestMapping(value = "/logineasypin", produces = "application/json",
	 * method = RequestMethod.POST) public String loginEasyPin(@RequestBody
	 * RequestLoginEasypin requestLoginEasypin, HttpServletRequest request) throws
	 * Exception { Date start = new Date(); GsonBuilder builder = new GsonBuilder();
	 * builder.serializeNulls(); Gson gson = new Gson(); gson = builder.create();
	 * String req = gson.toJson(requestLoginEasypin); String res = null; String
	 * resultErr = null; String message = null; Boolean error = false;
	 * HashMap<String, Object> map = new HashMap<>(); HashMap<String, Object> data =
	 * new HashMap<>(); try { String key = null; String lastLoginDevice =
	 * requestLoginEasypin.getLast_login_device(); LstUserSimultaneous
	 * lstUserSimultaneous = new LstUserSimultaneous();
	 * lstUserSimultaneous.setUSERNAME(requestLoginEasypin.getUsername());
	 * lstUserSimultaneous.setFLAG_ACTIVE(1);
	 * lstUserSimultaneous.setLAST_LOGIN_DEVICE(lastLoginDevice);
	 * LstUserSimultaneous user1 =
	 * services.selectLoginAuthenticate(lstUserSimultaneous); User dataActivityUser
	 * = services.selectActivityUser(requestLoginEasypin.getUsername()); if
	 * (dataActivityUser != null) { Integer countDeathClaim =
	 * services.selectCountDeathClaim(requestLoginEasypin.getUsername()); if
	 * (countDeathClaim > 0) { error = true; message =
	 * "Policy number exists containing death claims"; resultErr =
	 * "No polis pada username ini ada yang mengandung death claim";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLoginEasypin.getUsername() + " Error: " + resultErr); } else { if
	 * (dataActivityUser.getLast_login_device() != null) { if
	 * (dataActivityUser.getLast_login_device().equals(lastLoginDevice)) {
	 * ArrayList<Object> list = MapperServices
	 * .serializableList(services.selectDetailedPolis(requestLoginEasypin.
	 * getUsername())); if (list.size() > 0) { String today = df.format(new Date());
	 * lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
	 * lstUserSimultaneous.setUPDATE_DATE_TIME(today);
	 * services.updateUserKeyName(lstUserSimultaneous); key = user1.getKEY();
	 * 
	 * error = false; message = "Login success"; data.put("key", key);
	 * data.put("no_hp", dataActivityUser.getNo_hp() != null ?
	 * dataActivityUser.getNo_hp() : dataActivityUser.getNo_hp2()); } else { error =
	 * true; message = "List policy is empty"; resultErr = "List Polis Kosong";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLoginEasypin.getUsername() + " Error: " + resultErr); } } else { Date
	 * dateActivity = dataActivityUser.getUpdate_date(); Date dateNow = new Date();
	 * long diff = dateNow.getTime() - dateActivity.getTime(); long diffSeconds =
	 * diff / 1000; if (diffSeconds <= 900) { error = true; message =
	 * "Session still active"; resultErr =
	 * "Account sedang login ditempat lain atau ada session yang belum berakhir";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLoginEasypin.getUsername() + " Error: " + resultErr); } else {
	 * ArrayList<Object> list = MapperServices.serializableList(
	 * services.selectDetailedPolis(requestLoginEasypin.getUsername())); if
	 * (list.size() > 0) { try { customResourceLoader.postGoogle(linkFcmGoogle,
	 * requestLoginEasypin.getUsername(), dataActivityUser.getLast_login_device());
	 * 
	 * String today = df.format(new Date());
	 * lstUserSimultaneous.setUPDATE_DATE_TIME(today);
	 * lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
	 * services.updateUserKeyName(lstUserSimultaneous); key = user1.getKEY();
	 * 
	 * error = false; message = "Login success"; data.put("key", key);
	 * data.put("no_hp", dataActivityUser.getNo_hp() != null ?
	 * dataActivityUser.getNo_hp() : dataActivityUser.getNo_hp2()); } catch
	 * (Exception e) { logger.error("Path: " + request.getServletPath() +
	 * " Username: " + requestLoginEasypin.getUsername() + " Error: " + e); } } else
	 * { error = true; message = "List policy is empty"; resultErr =
	 * "List Polis Kosong"; logger.error("Path: " + request.getServletPath() +
	 * " Username: " + requestLoginEasypin.getUsername() + " Error: " + resultErr);
	 * } } } } else { ArrayList<Object> list = MapperServices
	 * .serializableList(services.selectDetailedPolis(requestLoginEasypin.
	 * getUsername())); if (list.size() > 0) { String today = df.format(new Date());
	 * lstUserSimultaneous.setLAST_LOGIN_DATE_TIME(today);
	 * lstUserSimultaneous.setUPDATE_DATE_TIME(today);
	 * services.updateUserKeyName(lstUserSimultaneous); key = user1.getKEY();
	 * 
	 * error = false; message = "Login success"; data.put("key", key);
	 * data.put("no_hp", dataActivityUser.getNo_hp() != null ?
	 * dataActivityUser.getNo_hp() : dataActivityUser.getNo_hp2()); } else { error =
	 * true; message = "List policy is empty"; resultErr = "List Polis Kosong";
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLoginEasypin.getUsername() + " Error: " + resultErr); } } } } else {
	 * error = true; message = "Login failed"; resultErr =
	 * "Username tidak terdaftar"; logger.error("Path: " + request.getServletPath()
	 * + " Username: " + requestLoginEasypin.getUsername() + " Error: " +
	 * resultErr); } } catch (Exception e) { error = true; message =
	 * ResponseMessage.ERROR_SYSTEM; resultErr = "bad exception " + e;
	 * logger.error("Path: " + request.getServletPath() + " Username: " +
	 * requestLoginEasypin.getUsername() + " Error: " + e); } map.put("error",
	 * error); map.put("message", message); map.put("data", data); res =
	 * gson.toJson(map); // Insert Log LST_HIST_ACTIVITY_WS
	 * customResourceLoader.insertHistActivityWS(12, 40, new Date(), req, res, 1,
	 * resultErr, start, requestLoginEasypin.getUsername());
	 * 
	 * return res; }
	 */

}
