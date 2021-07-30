package com.app.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.http.client.methods.HttpPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.app.model.DetailWithdraw;
import com.app.model.LstHistActivityWS;
import com.app.model.LstUserSimultaneous;
import com.app.model.NotifToken;
import com.app.model.Provinsi;
import com.app.model.PushNotif;
import com.app.model.User;
import com.app.services.VegaServices;
import com.google.gson.JsonObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

@Component
@ConditionalOnProperty(name = "kubernetes.platform", havingValue = "1")
public class VegaCustomResourceLoader implements ResourceLoaderAware {

	private static final Logger logger = LogManager.getLogger(CustomResourceLoader.class);

	private NumberFormat nfZeroTwo = new DecimalFormat("#,##0.00;(#,##0.00)");
	private NumberFormat nfZeroFour = new DecimalFormat("#,##0.0000;(#,##0.0000)");
	private DateFormat df = new SimpleDateFormat("dd MMM yyyy");

	@Autowired
	private VegaServices services;

	@Autowired
	private ResourceLoader resourceLoader;

	/*@Value("${link.redirect.linkRedirectSendOTP}")
	private String linkRedirectSendOTP;

	@Value("${link.redirect.linkRedirectValidateOTP}")
	private String linkRedirectValidateOTP;

	@Value("${link.redirect.linkRedirectResendOTP}")
	private String linkRedirectResendOTP;*/

	@Value("${link.send.email}")
	private String linkSendEmail;

	@Value("${path.direct.notification}")
	private String pathDirectNotification;

	@Value("${path.pdf.formwithdrawumum}")
	private String pathFormWithdrawUmum;

	@Value("${path.pdf.formwithdrawumumsyariah}")
	private String pathFormWithdrawUmumSyariah;

	@Value("${path.pdf.formwithdrawsimasmagnalink}")
	private String pathFormWithdrawSimasMagnaLink;

	@Value("${path.pdf.formwithdrawsimasmagnalinksyariah}")
	private String pathFormWithdrawSimasMagnaLinkSyariah;

	@Value("${path.log.submitclaimsubmission}")
	private String pathLogSubmitClaimSubmission;

	@Value("${path.log.submitclaimsubmissioncorporate}")
	private String pathLogSubmitClaimSubmissionCorp;

	@Value("${path.pdf.formclaimsubmission}")
	private String pathFormClaimSubmission;
	
	
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
	}

	public Boolean validateCredential(String username, String key) {
		User paramSelectDecryptPassword = new User();
		paramSelectDecryptPassword.setKey(key);
		User keyDecrypt = services.decryptPassword(paramSelectDecryptPassword);

		if (username.equals(keyDecrypt.getKey())) {
			return true;
		} else {
			return false;
		}
	}

	// show list all files in directory
	public Set<String> listFilesUsingJavaIO(String dir) {
		return Stream.of(new File(dir).listFiles()).sorted().filter(file -> !file.isDirectory()).map(File::getName)
				.collect(Collectors.toSet());
	}

	public List<String> listFilesUsingJavaIO2(String dir) {
		return Stream.of(new File(dir).listFiles()).sorted().filter(file -> !file.isDirectory()).map(File::getName)
				.collect(Collectors.toList());
	}

	public List<String> listFilesUsingJavaIO2CustomSorted(String dir) {
		return Stream.of(new File(dir).listFiles()).sorted(Comparator.comparingLong(File::lastModified).reversed())
				.filter(file -> !file.isDirectory()).map(File::getName).collect(Collectors.toList());
	}

	// show list all directory in directory
	public Set<String> listDirUsingJavaIO(String dir) {
		return Stream.of(new File(dir).listFiles()).filter(file -> !file.isFile()).map(File::getName)
				.sorted(Comparator.reverseOrder()).collect(Collectors.toSet());
	}

	public static void search(final String pattern, final File folder, List<String> result) {
		for (final File f : folder.listFiles()) {

			if (f.isDirectory()) {
				search(pattern, f, result);
			}

			if (f.isFile()) {
				if (f.getName().matches(pattern)) {
					result.add(f.getAbsolutePath());
				}
			}

		}
	}

	public String showResourceData(String path) throws IOException {
		// This line will be changed for all versions of other examples
		Resource banner = resourceLoader.getResource("file:" + path);

		InputStream in = banner.getInputStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		int i = 0;
		JSONObject valueBanner = new JSONObject();
		String body = "";
		while (true) {
			i++;
			String line = reader.readLine();
			if (line == null)
				break;

			if (i == 1) {
				valueBanner.put("title", line);
			} else {
				if (i == 2)
					body = line;
				else
					body = body + " " + line;

				valueBanner.put("body", body);
			}

//			System.out.println(valueBanner);
		}
		reader.close();

		return valueBanner.toString();
	}

	public Date getDateNow() {
		Calendar calNow = Calendar.getInstance();
		Date resultDateNow = calNow.getTime();
		return resultDateNow;
	}

	public String getDatetimeJava() {
		Date date = new Date();
		String strDateFormat = "dd/MM/yyyy HH:mm:ss";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String formattedDate = dateFormat.format(date);
		return formattedDate;
	}

	public String getDatetimeJava1() {
		Date date = new Date();
		String strDateFormat = "dd/MM/yyyy";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String formattedDate = dateFormat.format(date);
		return formattedDate;
	}

	public String getDatetimeJava2() {
		Date date = new Date();
		String strDateFormat = "yyHHmm";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String formattedDate = dateFormat.format(date);
		return formattedDate;
	}

	public Object getKey(JSONArray array, String key) {
		Object value = null;
		for (int i = 0; i < array.length(); i++) {
			JSONObject item = array.getJSONObject(i);
			if (item.keySet().contains(key)) {
				value = item.get(key);
				break;
			}
		}

		return value;
	}

	public static void readFromFile(String filename) {
		LineNumberReader lineNumberReader = null;
		try {
			// Construct the LineNumberReader object
			lineNumberReader = new LineNumberReader(new FileReader(filename));

			// Print initial line number
			System.out.println("Line " + lineNumberReader.getLineNumber());

			// Setting initial line number
			lineNumberReader.setLineNumber(5);

			// Get current line number
			System.out.println("Line " + lineNumberReader.getLineNumber());

			// Read all lines now; Every read increase the line number by 1
			String line = null;
			while ((line = lineNumberReader.readLine()) != null) {
				System.out.println("Line " + lineNumberReader.getLineNumber() + ": " + line);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close the LineNumberReader
			try {
				if (lineNumberReader != null) {
					lineNumberReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public Date addHoursToJavaUtilDate(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}

	public Integer generateRandomDigits(Integer n) {
		int m = (int) Math.pow(10, n - 1);
		return m + new Random().nextInt(9 * m);
	}

	@Async("asyncExecutor")
	public void postGoogle(String url, String username, String token) {
		try {
			Thread.sleep(1000L); // Intentional delay
			logger.info("----- Hit Google Start ----- " + '(' + getDatetimeJava() + ')');
			String jsonData = null;
			JSONObject paramNotif = new JSONObject();
			JSONObject dataNotif = new JSONObject();
			String restUrl = url;
			HttpPostReq httpPostReq = new HttpPostReq();
			HttpPost httpPost = httpPostReq.createConnectivityGoogle(restUrl);
			dataNotif.put("next_action_menu_id", 9);
			dataNotif.put("title", "Force logout");
			dataNotif.put("username", username);
			dataNotif.put("message", "Sorry. There is another device logged in using your account.");
			paramNotif.put("data", dataNotif);
			paramNotif.put("to", token);
			jsonData = paramNotif.toString();
			httpPostReq.executeReq(jsonData, httpPost);
			logger.info("----- Hit Google End ----- " + '(' + getDatetimeJava() + ')');
		} catch (InterruptedException e) {
			logger.error("Username: " + username + ", Exception hit API FCM Google: " + e);
		}
	}

	public void pushNotif(String username, String message, String no_polis, String reg_spaj, Integer next_action,
			Integer priority) {
		Date start = new Date();
		try {
			//HashMap<String, Object> dataConfiguration = services.configuration();
			//Integer enable_notification = Integer.parseInt((String) dataConfiguration.get("ENABLE_NOTIFICATION"));

			//if (enable_notification.equals(1)) {
				/*
				String jsonData = null;
				JSONObject paramNotif = new JSONObject();
				String restUrl = pathDirectNotification;
				HttpPostReq httpPostReq = new HttpPostReq();
				HttpPost httpPost = httpPostReq.createConnectivity(restUrl);
				paramNotif.put("type", 1);
				paramNotif.put("jenis_id", 93);
				paramNotif.put("userid", username);
				paramNotif.put("title", "VEGA");
				paramNotif.put("message", message);
				HashMap<String, Object> dataNotif = new HashMap<>();
				dataNotif.put("next_action_menu_id", next_action);
				dataNotif.put("policy_number", no_polis);
				paramNotif.put("data", dataNotif);
				paramNotif.put("priority", priority);
				paramNotif.put("reg_spaj", reg_spaj);
				paramNotif.put("flag_inbox", "Y");

				jsonData = paramNotif.toString();
				httpPostReq.executeReq(jsonData, httpPost);
				*/
				NotifToken notifToken = new NotifToken();
				notifToken = services.selectNotifToken(username);
				
				JSONObject dataTemp_temp = new JSONObject();
				dataTemp_temp.put("next_action_menu_id", next_action);
				dataTemp_temp.put("policy_number", no_polis);
				String dataTemp = dataTemp_temp.toString();
				
				InetAddress inetAddress = InetAddress.getLocalHost();
				PushNotif pushNotif = new PushNotif();
				pushNotif.setJenis_id("93");
				pushNotif.setUsername(username);
				pushNotif.setToken(notifToken.getToken());
				pushNotif.setTitle("VEGA");
				pushNotif.setMessage(message);
				pushNotif.setParameter(dataTemp);
				pushNotif.setPriority(priority);
				pushNotif.setReg_spaj(reg_spaj);
				pushNotif.setStatus("U");
				pushNotif.setFlag_inbox("Y");
				pushNotif.setCreate_date(start);
				pushNotif.setHost_name(inetAddress.getHostName());
				pushNotif.setHost_date(start);
				services.insertNotification(pushNotif);
			//}
		} catch (Exception e) {
			logger.error("Exception hit Post Notif, username: " + username + "message: " + message + "error: " + e);
		}
	}

	public Integer nilaiValNabchart(Integer nilai) {
		Integer nilaiVal = null;

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int doy = cal.get(Calendar.DAY_OF_YEAR);

		// Create a calendar with year and day of year.
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_YEAR, doy);

		int hour = cal.getTime().getHours();

		// Get the weekday and print it
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);

		// Get weekday name
		DateFormatSymbols dfs = new DateFormatSymbols();
		String day = dfs.getWeekdays()[weekday];

		if (nilai.equals(5)) {
			if ((day.equalsIgnoreCase("senin") || day.equalsIgnoreCase("monday")) && hour < 14) {
				nilaiVal = nilai + 1;
			} else if ((day.equalsIgnoreCase("senin") || day.equalsIgnoreCase("monday")) && hour >= 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("selasa") || day.equalsIgnoreCase("tuesday")) && hour < 14) {
				nilaiVal = nilai + 1;
			} else if ((day.equalsIgnoreCase("selasa") || day.equalsIgnoreCase("tuesday")) && hour >= 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("rabu") || day.equalsIgnoreCase("Wednesday")) && hour < 14) {
				nilaiVal = nilai + 1;
			} else if ((day.equalsIgnoreCase("rabu") || day.equalsIgnoreCase("Wednesday")) && hour >= 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("kamis") || day.equalsIgnoreCase("thursday")) && hour < 14) {
				nilaiVal = nilai + 1;
			} else if ((day.equalsIgnoreCase("kamis") || day.equalsIgnoreCase("thursday")) && hour >= 14) {
				nilaiVal = nilai - 2;
			} else if ((day.equalsIgnoreCase("jumat") || day.equalsIgnoreCase("friday")) && hour < 14) {
				nilaiVal = nilai - 1;
			} else if ((day.equalsIgnoreCase("jumat") || day.equalsIgnoreCase("friday")) && hour >= 14) {
				nilaiVal = nilai - 2;
			} else if ((day.equalsIgnoreCase("sabtu") || day.equalsIgnoreCase("saturday")) && hour < 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("sabtu") || day.equalsIgnoreCase("saturday")) && hour >= 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("minggu") || day.equalsIgnoreCase("sunday")) && hour < 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("minggu") || day.equalsIgnoreCase("sunday")) && hour >= 14) {
				nilaiVal = nilai;
			} else {
				nilaiVal = nilai;
			}
		} else if (nilai.equals(9)) {
			if ((day.equalsIgnoreCase("senin") || day.equalsIgnoreCase("monday")) && hour < 14) {
				nilaiVal = nilai + 3;
			} else if ((day.equalsIgnoreCase("senin") || day.equalsIgnoreCase("monday")) && hour >= 14) {
				nilaiVal = nilai + 2;
			} else if ((day.equalsIgnoreCase("selasa") || day.equalsIgnoreCase("tuesday")) && hour < 14) {
				nilaiVal = nilai + 3;
			} else if ((day.equalsIgnoreCase("selasa") || day.equalsIgnoreCase("tuesday")) && hour >= 14) {
				nilaiVal = nilai + 2;
			} else if ((day.equalsIgnoreCase("rabu") || day.equalsIgnoreCase("Wednesday")) && hour < 14) {
				nilaiVal = nilai + 1;
			} else if ((day.equalsIgnoreCase("rabu") || day.equalsIgnoreCase("Wednesday")) && hour >= 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("kamis") || day.equalsIgnoreCase("thursday")) && hour < 14) {
				nilaiVal = nilai + 1;
			} else if ((day.equalsIgnoreCase("kamis") || day.equalsIgnoreCase("thursday")) && hour >= 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("jumat") || day.equalsIgnoreCase("friday")) && hour < 14) {
				nilaiVal = nilai - 1;
			} else if ((day.equalsIgnoreCase("jumat") || day.equalsIgnoreCase("friday")) && hour >= 14) {
				nilaiVal = nilai - 2;
			} else if ((day.equalsIgnoreCase("sabtu") || day.equalsIgnoreCase("saturday")) && hour < 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("sabtu") || day.equalsIgnoreCase("saturday")) && hour >= 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("minggu") || day.equalsIgnoreCase("sunday")) && hour < 14) {
				nilaiVal = nilai;
			} else if ((day.equalsIgnoreCase("minggu") || day.equalsIgnoreCase("sunday")) && hour >= 14) {
				nilaiVal = nilai;
			} else {
				nilaiVal = nilai;
			}
		}

		return nilaiVal;
	}

	public String getStepTransaction(Integer language_id, String bank) {
		String value = null;
		if (language_id == 1 && bank.equalsIgnoreCase("bank sinarmas")) {
			value = "<h4>ATM Bank Sinarmas</h4> <ol> <li>Masukkan kartu ATM Anda</li> <li>Pilih jenis bahasa <strong>&ldquo;Indonesia&rdquo;</strong> atau <strong>&ldquo;Inggris&rdquo;</strong></li> <li>Masukkan <strong>6 digit PIN</strong> Anda</li> <li>Jenis transaksi : Pilih <strong>&ldquo;Transfer&rdquo;</strong></li> <li>Bank tujuan transfer : Pilih <strong>&ldquo;Rekening Bank Sinarmas&rdquo;</strong></li> <li>Bank tujuan transfer : Pilih <strong>&ldquo;Rekening Nasabah Lain&rdquo;</strong></li> <li>Masukkan nomor rekening tujuan : (Input No. Virtual account Polis tersebut)</li> <li>Masukkan jumlah uang yang akan ditransfer</li> <li>Masukkan no. Referensi (kosongkan) pilih <strong>&ldquo;Benar&rdquo;</strong></li> <li>&nbsp;Konfirmasi Transfer (Transaksi akan diproses) : Jika benar pilih <strong>&ldquo;Ya&rdquo;</strong></li> <li>&nbsp;Transaksi selesai</li> </ol> <h4>ATM Bersama/ALTO/PRIMA</h4> <ol> <li>Masukkan kartu ATM Anda</li> <li>Pilih jenis bahasa <strong>&ldquo;Indonesia&rdquo;</strong> atau <strong>&ldquo;Inggris&rdquo;</strong></li> <li>Masukkan <strong>6 digit PIN</strong> Anda</li> <li>Jenis transaksi : Pilih <strong>&ldquo;Transfer&rdquo;</strong></li> <li>Rekening Tujuan : Pilih <strong>&ldquo;Bank Lain&rdquo;</strong></li> <li>No rekening Tujuan : Input 153 dilanjutkan <strong>No Virtual Account </strong>polis tersebut</li> <li>Masukkan jumlah uang yang akan ditransfer</li> <li>Konfirmasi transfer, jika benar pilih <strong>&ldquo;Ya&rdquo;</strong></li> <li>Transaksi selesai</li> </ol> <h4>Pembayaran Premi Lanjutan menggunakan Internet banking (i-Bank Bank Sinarmas)</h4> <ol> <li>Login ke Internet Banking melalui portal Bank Sinarmas <a href=\"\\\\\\\\\\\\\\\\http://www.banksinarmas.com\\\\\\\\\\\\\\\\\">banksinarmas.com</a></li> <li>Pilih menu : Transfer dana / Fund transfer</li> <li>Pilih Sub menu : Rekening lain/to other account</li> <li>Klik &ldquo;Silahkan pilih account&rdquo; (nomor rekening dimana sumber dana akan diambil)</li> <li>Rekening tujuan, pilih &ldquo;rekening lainnya&rdquo; dan masukkan No. Virtual Account untuk polis yang akan dibayar</li> <li>Masukkan jumlah premi yang akan dibayar pada kolom jumlah</li> <li>Tekan tombol &ldquo;Submit&rdquo;</li> <li>Pada bagian konfirmasi, masukkan token ID atau Anda diminta untuk menjawab &ldquo;Secure Question&rdquo; pada kolom yang disediakan</li> <li>Tunggu sebentar, maka akan muncul keterangan &ldquo;Transaksi Berhasil&rdquo;</li> <li>&nbsp;Anda dapat memilih untuk mencetak atau menyimpan bukti transaksi tersebut</li> </ol> <h4>Setor Tunai/ pemindahbukuan melalui Teller Bank Sinarmas atau melalui Teller Bank Lain</h4> <p>Isi aplikasi Bank Sinarmas/Bank lain, dengan ditujukan kepada :</p> <p>(Bank Sinarmas)</p> <ol> <li>Nama Rekening : PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Nomor Rekening : No. Virtual&nbsp; Account untuk polis tersebut</li> <li>Jumlah : (isi jumlah premi)</li> </ol> <p>(Bank Lain)</p> <ol> <li>Nama Bank : Bank Sinarmas</li> <li>Pemegang Rekening : PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Nomor Rekening : No. Virtual&nbsp; Account untuk polis tersebut</li> <li>Jumlah : (isi jumlah premi)</li> </ol>";
		} else if (language_id == 2 && bank.equalsIgnoreCase("bank sinarmas")) {
			value = "<h4>ATM Bank Sinarmas</h4> <ol> <li>Insert your ATM card</li> <li>Choose type of language <strong>&ldquo;Indonesia&rdquo;</strong> or <strong>&ldquo;English&rdquo;</strong></li> <li>Enter your <strong>6 digit PIN</strong></li> <li>Select a transaction : Choose <strong>&ldquo;Transfer&rdquo;</strong></li> <li>Beneficiary account : Choose<strong>&ldquo;Sinarmas Bank Account&rdquo;</strong></li> <li>Beneficiary account : Choose <strong>&ldquo;Other Customer Account&rdquo;</strong></li> <li>Enter your account number : (Input Policy Virtual account Number)</li> <li>Enter amount of money to be transferred</li> <li>Enter Reference Number (leave it blank ) choose<strong>&ldquo;Right&rdquo;</strong></li> <li>&nbsp;Transfer confirmation (Transaction will be process) : If right choose <strong>&ldquo;Yes&rdquo;</strong></li> <li>&nbsp;Transaction finished</li> </ol> <h4>ATM Bersama/ALTO/PRIMA</h4> <ol> <li>Insert your ATM card</li> <li>Choose type of language <strong>&ldquo;Indonesia&rdquo;</strong> or <strong>&ldquo;English&rdquo;</strong></li> <li>Enter your <strong>6 digit PIN</strong></li> <li>Select a transaction : Choose <strong>&ldquo;Transfer&rdquo;</strong></li> <li>Beneficiary account : Choose <strong>&ldquo;Others Bank Account&rdquo;</strong></li> <li>Account number, enter 153 Please continue with your policy virtual account number</li> <li>Enter amount of money to be transferred</li> <li>Transfer confirmation (Transaction will be process) : If right choose <strong>&ldquo;Yes&rdquo;</strong></li> <li>Transaction finished</li> </ol> <h4><strong>Renewal Premium Payment with Internet Banking (i-Bank Bank Sinarmas)</strong></h4> <ol> <li>Login to Internet Banking <a href=\"\\\\\\\\http://www.banksinarmas.com\\\\\\\\\">banksinarmas.com</a></li> <li>Choose menu : Fund transfer</li> <li>Choose Sub menu : other account</li> <li>Click &ldquo;Please choose account&rdquo; (source account)</li> <li>Beneficiary account, choose &ldquo;other account&rdquo; and enter Virtual Account number for the policy to be paid</li> <li>Enter amount of the premium for the policy to be paid in the amount column</li> <li>Click Submit</li> <li>In the confirmation section, enter the ID token or you are asked to answer the &ldquo;Secure Question&rdquo; in the column provided</li> <li>Wait a minute, the information &ldquo;Successful Transaction&rdquo; will appear</li> <li>&nbsp;You can choose to print or save deposit slip</li> </ol> <h4>Cash Deposit/ overbooking via Bank Sinarmas Teller or other Bank Teller</h4> <p>Fill in Bank Sinarmas / other Bank applications to :</p> <p>(Bank Sinarmas)</p> <ol> <li>Account Name : PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Account Number : Virtual Account number for the policy to be paid</li> <li>Amount : (Enter amount of the premium)</li> </ol> <p>(Others Bank)</p> <ol> <li>Bank Name: Bank Sinarmas</li> <li>Account Name : PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Account Number : Virtual Account number for the policy to be paid</li> <li>Amount : (Enter amount of the premium)</li> </ol>";
		} else if (language_id == 1 && bank.equalsIgnoreCase("bank sinarmas syariah")) {
			value = "<h4>ATM Bank Sinarmas</h4> <ol> <li>Masukkan kartu ATM Anda</li> <li>Pilih jenis bahasa <strong>&ldquo;Indonesia&rdquo;</strong> atau <strong>&ldquo;Inggris&rdquo;</strong></li> <li>Masukkan <strong>6 digit PIN</strong> Anda</li> <li>Jenis transaksi : Pilih <strong>&ldquo;Transfer&rdquo;</strong></li> <li>Bank tujuan transfer : Pilih <strong>&ldquo;Rekening Bank Sinarmas&rdquo;</strong></li> <li>Bank tujuan transfer : Pilih <strong>&ldquo;Rekening Nasabah Lain&rdquo;</strong></li> <li>Masukkan nomor rekening tujuan : (Input No. Virtual account Polis tersebut)</li> <li>Masukkan jumlah uang yang akan ditransfer</li> <li>Masukkan no. Referensi (kosongkan) pilih <strong>&ldquo;Benar&rdquo;</strong></li> <li>&nbsp;Konfirmasi Transfer (Transaksi akan diproses) : Jika benar pilih <strong>&ldquo;Ya&rdquo;</strong></li> <li>&nbsp;Transaksi selesai</li> </ol> <h4>ATM Bersama/ALTO/PRIMA</h4> <ol> <li>Masukkan kartu ATM Anda</li> <li>Pilih jenis bahasa <strong>&ldquo;Indonesia&rdquo;</strong> atau <strong>&ldquo;Inggris&rdquo;</strong></li> <li>Masukkan <strong>6 digit PIN</strong> Anda</li> <li>Jenis transaksi : Pilih <strong>&ldquo;Transfer&rdquo;</strong></li> <li>Rekening Tujuan : Pilih <strong>&ldquo;Bank Lain&rdquo;</strong></li> <li>No rekening Tujuan : Input 153 dilanjutkan <strong>No Virtual Account </strong>polis tersebut</li> <li>Masukkan jumlah uang yang akan ditransfer</li> <li>Konfirmasi transfer, jika benar pilih <strong>&ldquo;Ya&rdquo;</strong></li> <li>Transaksi selesai</li> </ol> <h4>Pembayaran Premi Lanjutan menggunakan Internet banking (i-Bank Bank Sinarmas)</h4> <ol> <li>Login ke Internet Banking melalui portal Bank Sinarmas <a href=\"\\\\\\\\\\\\\\\\http://www.banksinarmas.com\\\\\\\\\\\\\\\\\">banksinarmas.com</a></li> <li>Pilih menu : Transfer dana / Fund transfer</li> <li>Pilih Sub menu : Rekening lain/to other account</li> <li>Klik &ldquo;Silahkan pilih account&rdquo; (nomor rekening dimana sumber dana akan diambil)</li> <li>Rekening tujuan, pilih &ldquo;rekening lainnya&rdquo; dan masukkan No. Virtual Account untuk polis yang akan dibayar</li> <li>Masukkan jumlah premi yang akan dibayar pada kolom jumlah</li> <li>Tekan tombol &ldquo;Submit&rdquo;</li> <li>Pada bagian konfirmasi, masukkan token ID atau Anda diminta untuk menjawab &ldquo;Secure Question&rdquo; pada kolom yang disediakan</li> <li>Tunggu sebentar, maka akan muncul keterangan &ldquo;Transaksi Berhasil&rdquo;</li> <li>&nbsp;Anda dapat memilih untuk mencetak atau menyimpan bukti transaksi tersebut</li> </ol> <h4>Setor Tunai/ pemindahbukuan melalui Teller Bank Sinarmas atau melalui Teller Bank Lain</h4> <p>Isi aplikasi Bank Sinarmas/Bank lain, dengan ditujukan kepada :</p> <p>(Bank Sinarmas)</p> <ol> <li>Nama Rekening : PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Nomor Rekening : No. Virtual&nbsp; Account untuk polis tersebut</li> <li>Jumlah : (isi jumlah premi)</li> </ol> <p>(Bank Lain)</p> <ol> <li>Nama Bank : Bank Sinarmas</li> <li>Pemegang Rekening : PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Nomor Rekening : No. Virtual&nbsp; Account untuk polis tersebut</li> <li>Jumlah : (isi jumlah premi)</li> </ol>";
		} else if (language_id == 2 && bank.equalsIgnoreCase("bank sinarmas syariah")) {
			value = "<h4>ATM Bank Sinarmas</h4> <ol> <li>Insert your ATM card</li> <li>Choose type of language <strong>&ldquo;Indonesia&rdquo;</strong> or <strong>&ldquo;English&rdquo;</strong></li> <li>Enter your <strong>6 digit PIN</strong></li> <li>Select a transaction : Choose <strong>&ldquo;Transfer&rdquo;</strong></li> <li>Beneficiary account : Choose<strong>&ldquo;Sinarmas Bank Account&rdquo;</strong></li> <li>Beneficiary account : Choose <strong>&ldquo;Other Customer Account&rdquo;</strong></li> <li>Enter your account number : (Input Policy Virtual account Number)</li> <li>Enter amount of money to be transferred</li> <li>Enter Reference Number (leave it blank ) choose<strong>&ldquo;Right&rdquo;</strong></li> <li>&nbsp;Transfer confirmation (Transaction will be process) : If right choose <strong>&ldquo;Yes&rdquo;</strong></li> <li>&nbsp;Transaction finished</li> </ol> <h4>ATM Bersama/ALTO/PRIMA</h4> <ol> <li>Insert your ATM card</li> <li>Choose type of language <strong>&ldquo;Indonesia&rdquo;</strong> or <strong>&ldquo;English&rdquo;</strong></li> <li>Enter your <strong>6 digit PIN</strong></li> <li>Select a transaction : Choose <strong>&ldquo;Transfer&rdquo;</strong></li> <li>Beneficiary account : Choose <strong>&ldquo;Others Bank Account&rdquo;</strong></li> <li>Account number, enter 153 Please continue with your policy virtual account number</li> <li>Enter amount of money to be transferred</li> <li>Transfer confirmation (Transaction will be process) : If right choose <strong>&ldquo;Yes&rdquo;</strong></li> <li>Transaction finished</li> </ol> <h4><strong>Renewal Premium Payment with Internet Banking (i-Bank Bank Sinarmas)</strong></h4> <ol> <li>Login to Internet Banking <a href=\"\\\\\\\\http://www.banksinarmas.com\\\\\\\\\">banksinarmas.com</a></li> <li>Choose menu : Fund transfer</li> <li>Choose Sub menu : other account</li> <li>Click &ldquo;Please choose account&rdquo; (source account)</li> <li>Beneficiary account, choose &ldquo;other account&rdquo; and enter Virtual Account number for the policy to be paid</li> <li>Enter amount of the premium for the policy to be paid in the amount column</li> <li>Click Submit</li> <li>In the confirmation section, enter the ID token or you are asked to answer the &ldquo;Secure Question&rdquo; in the column provided</li> <li>Wait a minute, the information &ldquo;Successful Transaction&rdquo; will appear</li> <li>&nbsp;You can choose to print or save deposit slip</li> </ol> <h4>Cash Deposit/ overbooking via Bank Sinarmas Teller or other Bank Teller</h4> <p>Fill in Bank Sinarmas / other Bank applications to :</p> <p>(Bank Sinarmas)</p> <ol> <li>Account Name : PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Account Number : Virtual Account number for the policy to be paid</li> <li>Amount : (Enter amount of the premium)</li> </ol> <p>(Others Bank)</p> <ol> <li>Bank Name: Bank Sinarmas</li> <li>Account Name : PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Account Number : Virtual Account number for the policy to be paid</li> <li>Amount : (Enter amount of the premium)</li> </ol>";
		} else if (language_id == 1 && bank.equalsIgnoreCase("bank bukopin")) {
			value = "<h4>ATM Bank BUKOPIN</h4> <ol><li>Masukan kartu ATM Anda</li> <li>Masukkan <strong>6 digit PIN</strong> Anda</li><li>Pilih menu &ldquo;Transfer&rdquo;</li><li>Masukkan kode Bank (441) atau pilih bank yang dituju yaitu Bank Bukopin</li><li>Masukan rekening tujuan 1024054019 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li><li>Masukkan jumlah uang yang akan ditransfer</li><li>Konfirmasi rincian Anda akan tampil di layar, cek dan apabila sudah sesuai silahkan lanjutkan transaksi sampai dengan selesai</li><li>Transaksi Berhasil</li> </ol>";
		} else if (language_id == 2 && bank.equalsIgnoreCase("bank bukopin")) {
			value = "<h4><strong>ATM Bank BUKOPIN</strong></h4> <ol> <li>Insert your ATM card</li><li>Enter your <strong>6 digit PIN</strong></li><li>Choose menu &ldquo;<strong>Transfer</strong>&rdquo;</li><li>Enter Bank Code ( 441 ) or choose Beneficiary account, Bank Bukopin</li><li>Enter account number 1024054019 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li><li>Enter amount of money to be transferred</li><li>Confirmation of your details will appear on the screen, check and if it is appropriate please continue to complete the transaction</li><li>Transaction finished</li> </ol>";
		} else if (language_id == 1 && bank.equalsIgnoreCase("bank bukopin syariah")) {
			value = "<h4>ATM BANK BUKOPIN SYARIAH</h4> <ol> <li>Masukan kartu ATM Anda</li> <li>Masukkan 6 digit PIN Anda</li> <li>Pilih menu &ldquo;Transfer&rdquo;</li> <li>Masukkan kode Bank (521) atau pilih bank yang dituju yaitu Bank Bukopin Syariah</li> <li>Masukan rekening tujuan 8802174102 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Masukkan jumlah uang yang akan ditransfer</li> <li>Konfirmasi rincian Anda akan tampil di layar, cek dan apabila sudah sesuai silahkan lanjutkan transaksi sampai dengan selesai</li> <li>Transaksi Berhasil</li> </ol>";
		} else if (language_id == 2 && bank.equalsIgnoreCase("bank bukopin syariah")) {
			value = "<h4>ATM BANK BUKOPIN SYARIAH</h4> <ol> <li>Insert your ATM card</li> <li>Enter your 6 digit PIN</li> <li>Choose menu &ldquo;Transfer&rdquo;</li> <li>Enter Bank Code ( 521 ) or choose Beneficiary account, Bank Bukopin Syariah</li> <li>Enter account number 8802174102 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Enter amount of money to be transferred</li> <li>Confirmation of your details will appear on the screen, check and if it is appropriate please continue to complete the transaction</li> <li>Transaction finished</li> </ol>";
		} else if (language_id == 1 && bank.equalsIgnoreCase("bank jatim")) {
			value = "<h4>ATM Bank JATIM</h4> <ol> <li>Masukan kartu ATM Anda</li><li>Masukkan <strong>6 digit PIN</strong> Anda</li><li>Pilih menu &ldquo;Transfer&rdquo;</li><li>Masukkan kode Bank Sinarmas (114) atau pilih bank yang dituju yaitu Bank Jatim</li><li>Masukan rekening tujuan 0011281087 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li><li>Masukkan jumlah uang yang akan ditransfer</li><li>Konfirmasi rincian Anda akan tampil di layar, cek dan apabila sudah sesuai silahkan lanjutkan transaksi sampai dengan selesai</li><li>Transaksi Berhasil</li> </ol>";
		} else if (language_id == 2 && bank.equalsIgnoreCase("bank jatim")) {
			value = "<h4>ATM Bank JATIM</h4> <ol> <li>Insert your ATM card</li><li>Enter your <strong>6 digit PIN</strong></li><li>Choose menu &ldquo;<strong>Transfer</strong>&rdquo;</li><li>Enter Bank Code ( 114 ) or choose Beneficiary account, Bank Jatim</li><li>Enter account number 0011281087 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li><li>Enter amount of money to be transferred</li><li>Confirmation of your details will appear on the screen, check and if it is appropriate please continue to complete the transaction</li><li>Transaction finished</li> </ol>";
		} else if (language_id == 1 && bank.equalsIgnoreCase("bank jatim syariah")) {
			value = "<h4>ATM Bank JATIM SYARIAH</h4><ol> <li>Masukan kartu ATM Anda</li><li>Masukkan <strong>6 digit PIN</strong> Anda</li> <li>Pilih menu &ldquo;Transfer&rdquo;</li> <li>Masukkan kode Bank (114) atau pilih bank yang dituju yaitu Bank Jatim</li> <li>Masukan rekening tujuan 6101006055 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Masukkan jumlah uang yang akan ditransfer</li> <li>Konfirmasi rincian Anda akan tampil di layar, cek dan apabila sudah sesuai silahkan lanjutkan transaksi sampai dengan selesai</li> <li>Transaksi Berhasil</li></ol>";
		} else if (language_id == 2 && bank.equalsIgnoreCase("bank jatim syariah")) {
			value = "<h4><strong>ATM Bank JATIM SYARIAH</strong></h4> <ol><li>Insert your ATM card</li> <li>Enter your <strong>6 digit PIN</strong></li><li>Choose menu &ldquo;<strong>Transfer</strong>&rdquo;</li><li>Enter Bank Code ( 114 ) or choose Beneficiary account, Bank Jatim</li><li>Enter account number 6101006055 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li><li>Enter amount of money to be transferred</li><li>Confirmation of your details will appear on the screen, check and if it is appropriate please continue to complete the transaction</li><li>Transaction finished</li> </ol>";
		} else if (language_id == 1 && bank.equalsIgnoreCase("bank btn syariah")) {
			value = "<h4>ATM Bank BTN SYARIAH</h4><ol><li>Masukan kartu ATM Anda</li><li>Masukkan <strong>6 digit PIN</strong> Anda</li><li>Pilih menu &ldquo;Transfer&rdquo;</li><li>Masukkan kode Bank (200) atau pilih bank yang dituju yaitu Bank BTN Syariah</li><li>Masukan rekening tujuan 711234566 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li><li>Masukkan nominal transfer sesuai tagihan atau kewajiban Anda. Nominal yang berbeda tidak dapat diproses.</li><li>Masukkan jumlah uang yang akan ditransfer</li><li>Konfirmasi rincian Anda akan tampil di layar, cek dan apabila sudah sesuai silahkan lanjutkan transaksi sampai dengan selesai</li><li>Transaksi Berhasil</li></ol>";
		} else if (language_id == 2 && bank.equalsIgnoreCase("bank btn syariah")) {
			value = "<h4>ATM Bank BTN SYARIAH</h4><ol><li>Insert your ATM card</li><li>Enter your <strong>6 digit PIN</strong></li><li>Choose menu &ldquo;<strong>Transfer</strong>&rdquo;</li><li>Enter Bank Code ( 200 ) or choose Beneficiary account, Bank BTN Syariah</li><li>Enter account number 711234566 PT Asuransi Jiwa Sinarmas MSIG Tbk.</li><li>Enter amount of money according to your billing. Different nominal cannot be processed</li><li>Enter amount of money to be transferred</li><li>Confirmation of your details will appear on the screen, check and if it is appropriate please continue to complete the transaction</li><li>Transaction finished</li></ol>";
		} else if (language_id == 1 && bank.equalsIgnoreCase("bank bjb")) {
			value = "<h4>ATM Bank BJB</h4> <ol> <li>Masukkan kartu ATM Anda</li> <li>Masukkan <strong>6 digit PIN</strong> Anda</li> <li>Pilih menu \"Transaksi lainnya\"</li> <li>Pilih menu \"Transfer\"</li> <li>Pilih menu \"Transfer ke bank lain\"</li> <li>Masukkan kode Bank (110) atau pilih bank yang dituju yaitu Bank bjb</li> <li>Masukan rekening tujuan 0074676456001 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Masukkan nominal transfer sesuai tagihan atau kewajiban Anda. Nominal yang berbeda tidak dapat diproses.</li> <li>Masukkan jumlah uang yang akan ditransfer</li> <li>&nbsp;Konfirmasi rincian Anda akan tampil di layar, cek dan apabila sudah sesuai silahkan lanjutkan transaksi sampai dengan selesai</li> <li>&nbsp;Pilih jenis rekening asal</li> <li>&nbsp;Transaksi Berhasil</li> </ol>";
		} else if (language_id == 2 && bank.equalsIgnoreCase("bank bjb")) {
			value = "<h4>ATM Bank BJB</h4> <ol> <li>Insert your ATM card</li> <li>Enter your <strong>6 digit PIN</strong></li> <li>Choose menu \"<strong>Others Transaction</strong>\"</li> <li>Select a transaction : Choose \"<strong>Transfer\"</strong></li> <li>Beneficiary account : Choose \"<strong>Others Bank Account\"</strong></li> <li>Enter Bank Code ( 110 ) or choose purpose Bank Transfer, Bank bjb</li> <li>Enter account number 0074676456001 an. PT Asuransi Jiwa Sinarmas MSIG Tbk.</li> <li>Enter amount of money according to your billing. Different nominal cannot be processed</li> <li>Enter amount of money to be transferred</li> <li>&nbsp;Confirmation of your details will appear on the screen, check and if it is appropriate please continue to complete the transaction</li> <li>&nbsp;Choose source account</li> <li>&nbsp;Transaction finished</li> </ol>";
		}

		return value;
	}

	public Boolean checkValidationTime(Integer valueFrom, Integer valueTo) {
		int from = valueFrom;
		int to = valueTo;
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
//	    System.out.println(t);
		boolean isBetween = to > from && t >= from && t <= to || to < from && (t >= from || t <= to);
//	    System.out.println(isBetween);

		return isBetween;
	}

	public void insertHistActivityWS(Integer client_id, Integer process_id, Date process_date, String process_desc,
			String process_result, Integer method, String err, Date start_date, String key) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();

			LstHistActivityWS histActivityWS = new LstHistActivityWS();
			histActivityWS.setSTART_DATE(start_date);
			histActivityWS.setMETHOD(method);
			histActivityWS.setCLIENT_ID(client_id);
			histActivityWS.setPROCESS_ID(process_id);
			histActivityWS.setMSAH_IP(inetAddress.getHostAddress());
			histActivityWS.setPROCESS_DESC(process_desc);
			histActivityWS.setKEY(key);
			histActivityWS.setPROCESS_RESULT(process_result);
			histActivityWS.setERR(err);
			histActivityWS.setPROCESS_DATE(process_date);

			services.insertLstHistActvWs(histActivityWS);
		} catch (Exception e) {
			logger.error("Username: " + key + " Exception Insert Hist Activity: " + e);
		}
	}

	/*public String sendOTP(Integer jenis_id, Integer menu_id, String username, String reg_spaj, String no_polis) {
		String jsonData = null;
		JSONObject param = new JSONObject();
		String restUrl = linkRedirectSendOTP;
		HttpPostReq httpPostReq = new HttpPostReq();
		HttpPost httpPost = httpPostReq.createConnectivity(restUrl);
		param.put("jenis_id", jenis_id);
		param.put("menu_id", menu_id);
		param.put("username", username);
		param.put("reg_spaj", reg_spaj);
		param.put("no_polis", no_polis);
		jsonData = param.toString();

		return httpPostReq.executeReq(jsonData, httpPost);
	}*/

	/*public String validateOTP(Integer jenis_id, Integer menu_id, String username, Integer otp_no) {
		String jsonData = null;
		JSONObject param = new JSONObject();
		String restUrl = linkRedirectValidateOTP;
		HttpPostReq httpPostReq = new HttpPostReq();
		HttpPost httpPost = httpPostReq.createConnectivity(restUrl);
		param.put("jenis_id", jenis_id);
		param.put("menu_id", menu_id);
		param.put("username", username);
		param.put("otp_number", otp_no);
		jsonData = param.toString();

		return httpPostReq.executeReq(jsonData, httpPost);
	}*/

	/*public String resendOTP(Integer jenis_id, Integer menu_id, String username, String reg_spaj, String no_polis) {
		String jsonData = null;
		JSONObject param = new JSONObject();
		String restUrl = linkRedirectResendOTP;
		HttpPostReq httpPostReq = new HttpPostReq();
		HttpPost httpPost = httpPostReq.createConnectivity(restUrl);
		param.put("jenis_id", jenis_id);
		param.put("menu_id", menu_id);
		param.put("username", username);
		param.put("reg_spaj", reg_spaj);
		param.put("no_polis", no_polis);
		jsonData = param.toString();

		return httpPostReq.executeReq(jsonData, httpPost);
	}*/

	public String sendEmail(String to, String cc, String bcc, String subject, String body_message, Boolean isHtml,
			String filepath) {
		String jsonData = null;
		JSONObject paramSendEmail = new JSONObject();
		String restUrl = linkSendEmail;
		HttpPostReq httpPostReq = new HttpPostReq();
		HttpPost httpPost = httpPostReq.createConnectivity(restUrl);
		paramSendEmail.put("to", to);
		paramSendEmail.put("cc", cc);
		paramSendEmail.put("bcc", bcc);
		paramSendEmail.put("subject", subject);
		paramSendEmail.put("body_message", body_message);
		paramSendEmail.put("isHtml", isHtml);
		paramSendEmail.put("filepath", filepath);
		paramSendEmail.put("showFooter", false);

		jsonData = paramSendEmail.toString();
		return httpPostReq.executeReq(jsonData, httpPost);
	}

	public void updateActivity(String username) {
		try {
			if (!username.equals("DevAjsmsig2019")) {
				User paramUpdateStatus = new User();
				paramUpdateStatus.setUsername(username);
				paramUpdateStatus.setDate_created_java(getDatetimeJava());
				services.updateActivityStatus(paramUpdateStatus);
			}
		} catch (Exception e) {
			logger.error("Username: " + username + " Exception update activity user: " + e);
		}
	}

	public String viewAddress(Integer lskl_id, Integer lskc_id, Integer lska_id, Integer lspr_id, String kodeposRmh) {
		Provinsi dataSelect = services.selectProvinsi2(lspr_id, lska_id, lskc_id, lskl_id);

		if (dataSelect == null) {
			return "";
		} else {
			String kelurahan = ", " + dataSelect.getKelurahan();
			String kecamatan = ", " + dataSelect.getKecamatan();
			String kabupaten = ", " + dataSelect.getKabupaten();
			String provinsi = ", " + dataSelect.getPropinsi();
			String kodepos = ", Kodepos: " + kodeposRmh;

			return kelurahan + kecamatan + kabupaten + provinsi + kodepos;
		}
	}

	public Boolean generatePdfWithdraw(Integer lsbs_id, Integer lsdbs_number, String param_product_name,
			String param_no_polis, String param_name, String param_phone_no, String param_email,
			ArrayList<DetailWithdraw> param_dataFund, String path_output) throws IOException, DocumentException {
		String no_polis = param_no_polis;
		String name = param_name;
		String phone_no = param_phone_no;
		String email = param_email;
		Integer font_size = 7;

		// Product Name
		Integer matrix_x_product_name = 0;
		Integer matrix_y_product_name = 0;

		// Policy Number
		Integer matrix_x_0_policy_number = 0;
		Integer matrix_x_1_policy_number = 0;
		Integer matrix_x_2_policy_number = 0;
		Integer matrix_x_3_policy_number = 0;
		Integer matrix_x_4_policy_number = 0;
		Integer matrix_x_5_policy_number = 0;
		Integer matrix_x_6_policy_number = 0;
		Integer matrix_x_7_policy_number = 0;
		Integer matrix_x_8_policy_number = 0;
		Integer matrix_x_9_policy_number = 0;
		Integer matrix_x_10_policy_number = 0;
		Integer matrix_x_11_policy_number = 0;
		Integer matrix_x_12_policy_number = 0;
		Integer matrix_x_13_policy_number = 0;
		Integer matrix_y_policy_number = 0;

		// Pemegang Polis
		Integer matrix_x_pemegang_polis = 0;
		Integer matrix_y_pemegang_polis = 0;

		// No Handphone
		Integer matrix_x_0_no_handphone = 0;
		Integer matrix_x_1_no_handphone = 0;
		Integer matrix_x_2_no_handphone = 0;
		Integer matrix_x_3_no_handphone = 0;
		Integer matrix_x_4_no_handphone = 0;
		Integer matrix_x_5_no_handphone = 0;
		Integer matrix_x_6_no_handphone = 0;
		Integer matrix_x_7_no_handphone = 0;
		Integer matrix_x_8_no_handphone = 0;
		Integer matrix_x_9_no_handphone = 0;
		Integer matrix_x_10_no_handphone = 0;
		Integer matrix_x_11_no_handphone = 0;
		Integer matrix_y_no_handphone = 0;

		// Email
		Integer matrix_x_email = 0;
		Integer matrix_y_email = 0;

		// Checklist Rekening
		Integer matrix_x_rekening = 0;
		Integer matrix_y_rekening = 0;

		// Checklist Withdraw
		Integer matrix_x_withdraw = 0;
		Integer matrix_y_withdraw = 0;

		// Tanda Tangan
		Integer matrix_x_ttd = 0;
		Integer matrix_y_ttd = 0;

		String checklist_withdraw = "X";

		PdfReader reader = null;

		if (lsbs_id.equals(213) && (lsdbs_number.equals(1) || lsdbs_number.equals(2))) {
			reader = new PdfReader(pathFormWithdrawSimasMagnaLink); // input PDF Simas Magna Link Umum

			// Product Name
			matrix_x_product_name = 163;
			matrix_y_product_name = 781;
			param_product_name = "";

			// Policy Number
			matrix_x_0_policy_number = 199;
			matrix_x_1_policy_number = 216;
			matrix_x_2_policy_number = 245;
			matrix_x_3_policy_number = 262;
			matrix_x_4_policy_number = 279;
			matrix_x_5_policy_number = 313;
			matrix_x_6_policy_number = 330;
			matrix_x_7_policy_number = 347;
			matrix_x_8_policy_number = 364;
			matrix_x_9_policy_number = 399;
			matrix_x_10_policy_number = 416;
			matrix_x_11_policy_number = 433;
			matrix_x_12_policy_number = 450;
			matrix_x_13_policy_number = 467;
			matrix_y_policy_number = 756;

			// Pemegang Polis
			matrix_x_pemegang_polis = 194;
			matrix_y_pemegang_polis = 745;

			// No Handphone
			matrix_x_0_no_handphone = 199;
			matrix_x_1_no_handphone = 216;
			matrix_x_2_no_handphone = 233;
			matrix_x_3_no_handphone = 250;

			matrix_x_4_no_handphone = 284;
			matrix_x_5_no_handphone = 301;
			matrix_x_6_no_handphone = 318;
			matrix_x_7_no_handphone = 335;
			matrix_x_8_no_handphone = 352;
			matrix_x_9_no_handphone = 369;
			matrix_x_10_no_handphone = 386;
			matrix_x_11_no_handphone = 403;
			matrix_y_no_handphone = 701;

			// Email
			matrix_x_email = 194;
			matrix_y_email = 689;

			// Checklist Rekening
			matrix_x_rekening = 98;
			matrix_y_rekening = 665;

			// Checklist Withdraw
			matrix_x_withdraw = 0;
			matrix_y_withdraw = 0;
			checklist_withdraw = "";

			// Tanda Tangan
			matrix_x_ttd = 74;
			matrix_y_ttd = 99;
		} else if (lsbs_id.equals(216) && lsdbs_number.equals(1)) {
			reader = new PdfReader(pathFormWithdrawSimasMagnaLinkSyariah); // input PDF Simas Magna Link Syariah

			// Product Name
			matrix_x_product_name = 191;
			matrix_y_product_name = 781;
			param_product_name = "";

			// Policy Number
			matrix_x_0_policy_number = 199;
			matrix_x_1_policy_number = 216;
			matrix_x_2_policy_number = 245;
			matrix_x_3_policy_number = 262;
			matrix_x_4_policy_number = 279;
			matrix_x_5_policy_number = 313;
			matrix_x_6_policy_number = 330;
			matrix_x_7_policy_number = 347;
			matrix_x_8_policy_number = 364;
			matrix_x_9_policy_number = 400;
			matrix_x_10_policy_number = 417;
			matrix_x_11_policy_number = 434;
			matrix_x_12_policy_number = 451;
			matrix_x_13_policy_number = 467;
			matrix_y_policy_number = 757;

			// Pemegang Polis
			matrix_x_pemegang_polis = 193;
			matrix_y_pemegang_polis = 745;

			// No Handphone
			matrix_x_0_no_handphone = 199;
			matrix_x_1_no_handphone = 216;
			matrix_x_2_no_handphone = 233;
			matrix_x_3_no_handphone = 250;

			matrix_x_4_no_handphone = 284;
			matrix_x_5_no_handphone = 301;
			matrix_x_6_no_handphone = 318;
			matrix_x_7_no_handphone = 335;
			matrix_x_8_no_handphone = 352;
			matrix_x_9_no_handphone = 369;
			matrix_x_10_no_handphone = 386;
			matrix_x_11_no_handphone = 403;
			matrix_y_no_handphone = 701;

			// Email
			matrix_x_email = 193;
			matrix_y_email = 690;

			// Checklist Rekening
			matrix_x_rekening = 98;
			matrix_y_rekening = 665;

			// Checklist Withdraw
			matrix_x_withdraw = 0;
			matrix_y_withdraw = 0;
			checklist_withdraw = "";

			// Tanda Tangan
			matrix_x_ttd = 74;
			matrix_y_ttd = 110;
		} else if (param_product_name.toLowerCase().contains("syariah")) {
			reader = new PdfReader(pathFormWithdrawUmumSyariah); // input PDF Unit Link Syariah

			// Product Name
			matrix_x_product_name = 109;
			matrix_y_product_name = 781;

			// Policy Number
			matrix_x_0_policy_number = 199;
			matrix_x_1_policy_number = 216;
			matrix_x_2_policy_number = 245;
			matrix_x_3_policy_number = 262;
			matrix_x_4_policy_number = 279;
			matrix_x_5_policy_number = 313;
			matrix_x_6_policy_number = 330;
			matrix_x_7_policy_number = 347;
			matrix_x_8_policy_number = 364;
			matrix_x_9_policy_number = 400;
			matrix_x_10_policy_number = 417;
			matrix_x_11_policy_number = 434;
			matrix_x_12_policy_number = 451;
			matrix_x_13_policy_number = 467;
			matrix_y_policy_number = 757;

			// Pemegang Polis
			matrix_x_pemegang_polis = 193;
			matrix_y_pemegang_polis = 745;

			// No Handphone
			matrix_x_0_no_handphone = 199;
			matrix_x_1_no_handphone = 216;
			matrix_x_2_no_handphone = 233;
			matrix_x_3_no_handphone = 250;

			matrix_x_4_no_handphone = 284;
			matrix_x_5_no_handphone = 301;
			matrix_x_6_no_handphone = 318;
			matrix_x_7_no_handphone = 335;
			matrix_x_8_no_handphone = 352;
			matrix_x_9_no_handphone = 369;
			matrix_x_10_no_handphone = 386;
			matrix_x_11_no_handphone = 403;
			matrix_y_no_handphone = 701;

			// Email
			matrix_x_email = 193;
			matrix_y_email = 690;

			// Checklist Rekening
			matrix_x_rekening = 98;
			matrix_y_rekening = 665;

			// Checklist Withdraw
			matrix_x_withdraw = 198;
			matrix_y_withdraw = 601;

			// Tanda Tangan
			matrix_x_ttd = 74;
			matrix_y_ttd = 108;
		} else {
			reader = new PdfReader(pathFormWithdrawUmum); // input PDF Unit Link Umum

			// Product Name
			matrix_x_product_name = 114;
			matrix_y_product_name = 784;

			// Policy Number
			matrix_x_0_policy_number = 185;
			matrix_x_1_policy_number = 202;
			matrix_x_2_policy_number = 237;
			matrix_x_3_policy_number = 254;
			matrix_x_4_policy_number = 271;
			matrix_x_5_policy_number = 305;
			matrix_x_6_policy_number = 322;
			matrix_x_7_policy_number = 339;
			matrix_x_8_policy_number = 356;
			matrix_x_9_policy_number = 391;
			matrix_x_10_policy_number = 408;
			matrix_x_11_policy_number = 425;
			matrix_x_12_policy_number = 442;
			matrix_x_13_policy_number = 459;
			matrix_y_policy_number = 760;

			// Pemegang Polis
			matrix_x_pemegang_polis = 180;
			matrix_y_pemegang_polis = 748;

			// No Handphone
			matrix_x_0_no_handphone = 185;
			matrix_x_1_no_handphone = 202;
			matrix_x_2_no_handphone = 219;
			matrix_x_3_no_handphone = 236;
			matrix_x_4_no_handphone = 271;
			matrix_x_5_no_handphone = 288;
			matrix_x_6_no_handphone = 305;
			matrix_x_7_no_handphone = 322;
			matrix_x_8_no_handphone = 339;
			matrix_x_9_no_handphone = 356;
			matrix_x_10_no_handphone = 373;
			matrix_x_11_no_handphone = 373;
			matrix_y_no_handphone = 703;

			// Email
			matrix_x_email = 180;
			matrix_y_email = 691;

			// Checklist Rekening
			matrix_x_rekening = 105;
			matrix_y_rekening = 667;

			// Checklist Withdraw
			matrix_x_withdraw = 186;
			matrix_y_withdraw = 601;

			// Tanda Tangan
			matrix_x_ttd = 80;
			matrix_y_ttd = 84;
		}

		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(path_output)); // output PDF
		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); // set font

		// loop on pages (1-based)
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {

			// get object for writing over the existing content;
			// you can also use getUnderContent for writing in the bottom layer
			PdfContentByte over = stamper.getOverContent(i);

			// Write Product Name
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(matrix_x_product_name, matrix_y_product_name); // set x,y position (0,0 is at the bottom
																				// left)
			over.showText(param_product_name + " (M-Polis)"); // set text number 0
			over.endText();

			// Write policy number
			for (int a = 0; a < no_polis.length(); a++) {
				over.beginText();
				over.setFontAndSize(bf, font_size); // set font and size
				if (a == 0) {
					over.setTextMatrix(matrix_x_0_policy_number, matrix_y_policy_number); // set x,y position (0,0 is at
																							// the bottom left)
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 0
				} else if (a == 1) {
					over.setTextMatrix(matrix_x_1_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 2
				} else if (a == 2) {
					over.setTextMatrix(matrix_x_2_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 3
				} else if (a == 3) {
					over.setTextMatrix(matrix_x_3_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 4
				} else if (a == 4) {
					over.setTextMatrix(matrix_x_4_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 5
				} else if (a == 5) {
					over.setTextMatrix(matrix_x_5_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 6
				} else if (a == 6) {
					over.setTextMatrix(matrix_x_6_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 7
				} else if (a == 7) {
					over.setTextMatrix(matrix_x_7_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 8
				} else if (a == 8) {
					over.setTextMatrix(matrix_x_8_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 9
				} else if (a == 9) {
					over.setTextMatrix(matrix_x_9_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 10
				} else if (a == 10) {
					over.setTextMatrix(matrix_x_10_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 11
				} else if (a == 11) {
					over.setTextMatrix(matrix_x_11_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 12
				} else if (a == 12) {
					over.setTextMatrix(matrix_x_12_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 13
				} else if (a == 13) {
					over.setTextMatrix(matrix_x_13_policy_number, matrix_y_policy_number);
					over.showText(String.valueOf(no_polis.charAt(a))); // set text number 14
				}

				over.endText();
			}

			// Write Pemegang Polis
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(matrix_x_pemegang_polis, matrix_y_pemegang_polis);
			over.showText(name);
			over.endText();

			// Write No Handphone
			for (int b = 0; b < phone_no.length(); b++) {
				over.beginText();
				over.setFontAndSize(bf, font_size); // set font and size
				if (b == 0) {
					over.setTextMatrix(matrix_x_0_no_handphone, matrix_y_no_handphone); // set x,y position (0,0 is at
																						// the bottom left)
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 1
				} else if (b == 1) {
					over.setTextMatrix(matrix_x_1_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 2
				} else if (b == 2) {
					over.setTextMatrix(matrix_x_2_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 3
				} else if (b == 3) {
					over.setTextMatrix(matrix_x_3_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 4
				} else if (b == 4) {
					over.setTextMatrix(matrix_x_4_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 5
				} else if (b == 5) {
					over.setTextMatrix(matrix_x_5_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 6
				} else if (b == 6) {
					over.setTextMatrix(matrix_x_6_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 7
				} else if (b == 7) {
					over.setTextMatrix(matrix_x_7_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 8
				} else if (b == 8) {
					over.setTextMatrix(matrix_x_8_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 9
				} else if (b == 9) {
					over.setTextMatrix(matrix_x_9_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 10
				} else if (b == 10) {
					over.setTextMatrix(matrix_x_10_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 11
				} else if (b == 11) {
					over.setTextMatrix(matrix_x_11_no_handphone, matrix_y_no_handphone);
					over.showText(String.valueOf(phone_no.charAt(b))); // set text number 12
				}

				over.endText();
			}

			// Write Email
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(matrix_x_email, matrix_y_email);
			over.showText(email);
			over.endText();

			// Write Checklist rekening
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(matrix_x_rekening, matrix_y_rekening);
			over.showText("X");
			over.endText();

			// Write Checklist withdraw
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(matrix_x_withdraw, matrix_y_withdraw);
			over.showText(checklist_withdraw);
			over.endText();

			// Write Fund2
			ArrayList<DetailWithdraw> dataFund = param_dataFund;
			if (dataFund != null) {
				if (lsbs_id.equals(213) && (lsdbs_number.equals(1) || lsdbs_number.equals(2))) { // Simas Magna Link
																									// Umum
					if (!dataFund.isEmpty()) {
						over.beginText();
						over.setFontAndSize(bf, font_size); // set font and size
						for (int x = 0; x < dataFund.size(); x++) {
							String lji_id = (String) dataFund.get(x).getLji_id();
							BigDecimal mpt_jumlah = (BigDecimal) dataFund.get(x).getMpt_jumlah_detail();
							BigDecimal mpt_unit = (BigDecimal) dataFund.get(x).getMpt_unit_detail();

							if (lji_id.equals("42")) {
								// Simas Stabile Fund (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 560);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 560);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("43")) {
								// Simas Balance Fund (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 549);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 549);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("44")) {
								// Simas Equity (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 538);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 538);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("47")) {
								// Excellink Stabile Fund (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 527);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 526);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("48")) {
								// Excellink Balance Fund (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 515);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 515);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("49")) {
								// Excellink Equity Fund (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 504);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 504);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("45")) {
								// Simas Stabile Dollar Fund (USD)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 493);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 493);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("46")) {
								// Simas Balance Dollar Fund (USD)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 482);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 482);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("56")) {
								// Excellink Stabile Dollar Fund (USD)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 471);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 471);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("57")) {
								// Excellink Balance Dollar Fund (USD)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 459);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 459);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							}
						}

						over.endText();
					}
				} else if (lsbs_id.equals(216) && lsdbs_number.equals(1)) { // Simas Magna Link Syariah
					if (!dataFund.isEmpty()) {
						over.beginText();
						over.setFontAndSize(bf, font_size); // set font and size
						for (int x = 0; x < dataFund.size(); x++) {
							String lji_id = (String) dataFund.get(x).getLji_id();
							BigDecimal mpt_jumlah = (BigDecimal) dataFund.get(x).getMpt_jumlah_detail();
							BigDecimal mpt_unit = (BigDecimal) dataFund.get(x).getMpt_unit_detail();

							if (lji_id.equals("50")) {
								// Simas Stabile Fund Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 560);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 560);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("51")) {
								// Simas Balance Fund Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 549);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 549);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("52")) {
								// Simas Equity Fund Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 538);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 538);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("53")) {
								// Excellink Stabile Fund Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 527);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 526);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("54")) {
								// Excellink Balance Fund Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 515);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 515);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("55")) {
								// Excellink Equity Fund Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 504);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 504);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							}
						}

						over.endText();
					}
				} else if (param_product_name.toLowerCase().contains("syariah")) { // Unit Link Syariah
					if (!dataFund.isEmpty()) {
						over.beginText();
						over.setFontAndSize(bf, font_size); // set font and size
						for (int x = 0; x < dataFund.size(); x++) {
							String lji_id = (String) dataFund.get(x).getLji_id();
							BigDecimal mpt_jumlah = (BigDecimal) dataFund.get(x).getMpt_jumlah_detail();
							BigDecimal mpt_unit = (BigDecimal) dataFund.get(x).getMpt_unit_detail();

							if (lji_id.equals("58")) {
								// Simas Fixed Income Fund Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 560);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 560);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("59")) {
								// Simas Dynamic Fund Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 549);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 549);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("60")) {
								// Simas Aggressive Fund Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 538);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 538);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("06")) {
								// Excellink Fixed Income Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 527);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 526);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("07")) {
								// Excellink Dynamic Syariah Fund (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 515);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 515);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("08")) {
								// Excellink Aggressive Syariah Fund (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 504);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 504);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							} else if (lji_id.equals("62")) {
								// Excellink Cash Fund Syariah (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(191, 493);
									over.showText(nfZeroTwo.format(mpt_jumlah));
								} else {
									over.setTextMatrix(327, 493);
									over.showText(nfZeroFour.format(mpt_unit));
								}
							}
						}

						over.endText();
					}
				} else { // Unit Link Umum
					if (!dataFund.isEmpty()) {
						over.beginText();
						over.setFontAndSize(bf, font_size); // set font and size
						for (int x = 0; x < dataFund.size(); x++) {
							String lji_id = (String) dataFund.get(x).getLji_id();
							BigDecimal mpt_jumlah = (BigDecimal) dataFund.get(x).getMpt_jumlah_detail();
							BigDecimal mpt_unit = (BigDecimal) dataFund.get(x).getMpt_unit_detail();

							if (lji_id.equals("35")) {
								// Simas Fixed Income (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(179, 565);
									over.showText(nfZeroTwo.format(mpt_jumlah)); // Simas Fixed Income (RP)
								} else {
									over.setTextMatrix(317, 565);
									over.showText(nfZeroFour.format(mpt_unit)); // Simas Fixed Income (Unit)
								}
							} else if (lji_id.equals("36")) {
								// Simas Dynamic (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(179, 553);
									over.showText(nfZeroTwo.format(mpt_jumlah)); // Simas Dynamic (RP)
								} else {
									over.setTextMatrix(317, 553);
									over.showText(nfZeroFour.format(mpt_unit)); // Simas Dynamic (Unit)
								}
							} else if (lji_id.equals("37")) {
								// Simas Aggressive (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(179, 540);
									over.showText(nfZeroTwo.format(mpt_jumlah)); // Simas Aggressive (RP)
								} else {
									over.setTextMatrix(317, 540);
									over.showText(nfZeroFour.format(mpt_unit)); // Simas Aggressive (Unit)
								}
							} else if (lji_id.equals("01")) {
								// Excellink Fixed Income (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(179, 528);
									over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Fixed Income (RP)
								} else {
									over.setTextMatrix(317, 528);
									over.showText(nfZeroFour.format(mpt_unit)); // Excellink Fixed Income (Unit)
								}
							} else if (lji_id.equals("02")) {
								// Excellink Dynamic (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(179, 515);
									over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Dynamic (RP)
								} else {
									over.setTextMatrix(317, 515);
									over.showText(nfZeroFour.format(mpt_unit)); // Excellink Dynamic (Unit)
								}
							} else if (lji_id.equals("03")) {
								// Excellink Aggressive (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(179, 503);
									over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Aggressive (RP)
								} else {
									over.setTextMatrix(317, 503);
									over.showText(nfZeroFour.format(mpt_unit)); // Excellink Aggressive (Unit)
								}
							} else if (lji_id.equals("61")) {
								// Excellink Cash (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(179, 491);
									over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Cash (RP)
								} else {
									over.setTextMatrix(317, 491);
									over.showText(nfZeroFour.format(mpt_unit)); // Excellink Cash (Unit)
								}
							} else if (lji_id.equals("63")) {
								// Excellink Equity Bakti Peduli (RP)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(179, 479);
									over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Equity Bakti Peduli (RP)
								} else {
									over.setTextMatrix(317, 479);
									over.showText(nfZeroFour.format(mpt_unit)); // Excellink Equity Bakti Peduli (Unit)
								}
							} else if (lji_id.equals("04")) {
								// Excellink Secure Dollar Income (USD)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(179, 467);
									over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Secure Dollar Income (USD)
								} else {
									over.setTextMatrix(317, 467);
									over.showText(nfZeroFour.format(mpt_unit)); // Excellink Secure Dollar Income (Unit)
								}
							} else if (lji_id.equals("05")) {
								// Excellink Dynamic Dollar (USD)
								if ((!mpt_jumlah.equals(BigDecimal.ZERO.setScale(1)))
										&& (!mpt_jumlah.equals(BigDecimal.ZERO))) {
									over.setTextMatrix(179, 455);
									over.showText(nfZeroTwo.format(mpt_jumlah)); // Excellink Dynamic Dollar (USD)
								} else {
									over.setTextMatrix(317, 455);
									over.showText(nfZeroFour.format(mpt_unit)); // Excellink Dynamic Dollar (Unit)
								}
							}
						}
						over.endText();
					}
				}
			}

			// Write Tanda tangan
			over.beginText();
			over.setFontAndSize(bf, 5); // set font and size
			over.setTextMatrix(matrix_x_ttd, matrix_y_ttd);
			over.showText(name);
			over.endText();
		}

		stamper.close();

		return true;
	}

	public Boolean generatePdfClaimSubmission(String mpc_id, String nm_pemegang_polis, String nama_pasien,
			String status_pasien, String alamat_nohp, Integer umur_pasien, String email, String lama_rawat,
			String path_output) throws IOException, DocumentException {
		Integer font_size = 7;

		String pathInputDokumen = pathFormClaimSubmission + File.separator + "FormRawatInapGenerate.pdf";

		Integer mpc_id_x = 275;
		Integer mpc_id_y = 698;

		Integer nm_pemegang_polis_x = 122;
		Integer nm_pemegang_polis_y = 668;

		Integer nm_pasien_x = 132;
		Integer nm_pasien_y = 642;

		Integer status_pasien_x = 209;
		Integer status_pasien_y = 614;

		Integer alamat_nohp_x = 103;
		Integer alamat_nohp_y = 586;

		Integer umur_pasien_x = 442;
		Integer umur_pasien_y = 641;

		Integer email_x = 385;
		Integer email_y = 587;

		Integer lama_rawat_x = 128;
		Integer lama_rawat_y = 513;

		Integer tanggal_x = 348;
		Integer tanggal_y = 182;

		Date newDate = new Date();
		String tanggal = df.format(newDate);

		PdfReader reader = null;
		reader = new PdfReader(pathInputDokumen);

		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(path_output)); // output PDF
		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); // set font

		// loop on pages (1-based)
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			PdfContentByte over = stamper.getOverContent(i);

			// Write MPC ID
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(mpc_id_x, mpc_id_y);
			over.showText(mpc_id);
			over.endText();

			// Write Nama Pemegang Polis
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(nm_pemegang_polis_x, nm_pemegang_polis_y);
			over.showText(": " + nm_pemegang_polis);
			over.endText();

			// Write Nama Pasien
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(nm_pasien_x, nm_pasien_y);
			over.showText(": " + nama_pasien);
			over.endText();

			// Write status Pasien
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(status_pasien_x, status_pasien_y);
			over.showText(": " + status_pasien);
			over.endText();

			// Write Alamat & No. HP
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(alamat_nohp_x, alamat_nohp_y);
			over.showText(": " + alamat_nohp);
			over.endText();

			// Write Umur Pasien
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(umur_pasien_x, umur_pasien_y);
			over.showText(": " + umur_pasien.toString() + " Tahun");
			over.endText();

			// Write Email
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(email_x, email_y);
			over.showText(": " + email);
			over.endText();

			// Write Lama Rawat
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(lama_rawat_x, lama_rawat_y);
			over.showText(": " + lama_rawat + " Hari");
			over.endText();

			// Write Tanggal
			over.beginText();
			over.setFontAndSize(bf, font_size); // set font and size
			over.setTextMatrix(tanggal_x, tanggal_y);
			over.showText(tanggal);
			over.endText();
		}

		stamper.close();

		return true;
	}

	public Boolean uploadFileToStorage(String pathFolder, String fileBase64, String fileName, String username,
			String urlPath, BigInteger mpc_id) throws FileNotFoundException, IOException {
		Boolean result = false;
		String nameFileNew = null;

		File folder = new File(pathFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf > -1) {
            nameFileNew = fileName.substring(0, lastIndexOf);
            System.out.println(fileName);
        }
		String file_type = fileName.substring(fileName.lastIndexOf(".")+1);
		
		if (file_type.equals("jpg")) {
			try {
				Document document = new Document();
				
				fileBase64 = fileBase64.replace("\n", "");
				String fileUpload = pathFolder + File.separator + nameFileNew + ".pdf";
				
				byte[] imageByte = Base64.getDecoder().decode(fileBase64);
				new FileOutputStream(fileUpload).write(imageByte);
				
				PdfWriter.getInstance(document, new FileOutputStream(fileUpload));
				document.open();
				byte[] decoded = Base64.getDecoder().decode(fileBase64.getBytes());
				Image image1 = Image.getInstance(decoded);

				int indentation = 0;
				float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
						- document.rightMargin() - indentation) / image1.getWidth()) * 100;

				image1.scalePercent(scaler);
				document.add(image1);
				document.close();
				result = true;
			} catch (Exception e) {
				logger.error("Path: " + urlPath + " Username: " + username + " Error: " + e);
				result = false;
			}
		} else {
			try {
				byte[] fileByte = Base64.getDecoder().decode(fileBase64);
				String directory = folder + File.separator + nameFileNew + ".pdf";

				FileOutputStream fos = new FileOutputStream(directory);
				fos.write(fileByte);
				fos.close();
				fos.flush();

				result = true;
			} catch (Exception e) {
				logger.error("Path: " + urlPath + " Username: " + username + " Error: " + e);
				result = false;
			}
		}

		return result;
	}

	public Boolean createTxt(String mpc_id, String text, String file_name, Integer type) throws IOException {
		// Type 1: Individual Type 2: Corporate
		String pathLog = pathLogSubmitClaimSubmission + File.separator + mpc_id;

		if (type.equals(2)) {
			pathLog = pathLogSubmitClaimSubmissionCorp + File.separator + mpc_id;
		}

		File folder = new File(pathLog);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		try {
			FileWriter writer = new FileWriter(pathLog + File.separator + file_name + ".txt", true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write(text);
			bufferedWriter.close();
		} catch (IOException e) {
			logger.error("MPC_ID: " + mpc_id + " Exception create txt, " + " Error: " + e);
		}

		return true;
	}

	public void pushTelegram(String chat_id, String message) {
		String token = "bot1011637808:AAHnBglsUnl4BZQ_ZKkG4nipve2K36naCCA";

		try {
			HashMap<String, Object> dataConfiguration = services.configuration();
			Integer enable_pushtelegram = Integer.parseInt((String) dataConfiguration.get("ENABLE_PUSHTELEGRAM"));

			if (enable_pushtelegram.equals(1)) {
				String jsonData = null;
				JSONObject paramPost = new JSONObject();
				String restUrl = "https://api.telegram.org/" + token + "/sendMessage";
				HttpPostReq sendHttp = new HttpPostReq();
				HttpPost httpPost = sendHttp.createConnectivity(restUrl);
				paramPost.put("chat_id", chat_id);
				paramPost.put("text", message);
				paramPost.put("parse_mode", "HTML");
				jsonData = paramPost.toString();
				sendHttp.executeReq(jsonData, httpPost);
			}
		} catch (Exception e) {
			logger.error("Exception post telegram: ", e);
		}
	}

	public void pushTelegramNotCheckDatabase(String chat_id, String message) {
		String token = "bot1011637808:AAHnBglsUnl4BZQ_ZKkG4nipve2K36naCCA";

		try {
			String jsonData = null;
			JSONObject paramPost = new JSONObject();
			String restUrl = "https://api.telegram.org/" + token + "/sendMessage";
			HttpPostReq sendHttp = new HttpPostReq();
			HttpPost httpPost = sendHttp.createConnectivity(restUrl);
			paramPost.put("chat_id", chat_id);
			paramPost.put("text", message);
			paramPost.put("parse_mode", "HTML");
			jsonData = paramPost.toString();
			sendHttp.executeReq(jsonData, httpPost);
		} catch (Exception e) {
			logger.error("Exception post telegram not check database: ", e);
		}
	}

	public Boolean validateFilePdf(String pathFile, String username, String mpc_id) {
		try {
			PdfReader pdfReader = new PdfReader(pathFile);

			String textFromPdfFilePageOne = PdfTextExtractor.getTextFromPage(pdfReader, 1);
			System.out.println("File PDF " + pathFile + " OK " + textFromPdfFilePageOne);
			pdfReader.close();
			return true;
		} catch (Exception e) {
			logger.error(
					"Username: " + username + ", MPC_ID: " + mpc_id + ", Name File: " + pathFile + ", Error: " + e);
			return false;
		}
	}

	public String getWorkingDate() {
		String dateValue = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		int x = 0;
		int y = 1;
		int days2 = 1;
		while (x < y) {
			LocalDate dateRedeemed2 = LocalDate.now();
//			LocalDate dateRedeemed2 = LocalDate.of(2020, 10, 28);
			LocalDate newDate2 = dateRedeemed2.plusDays(days2);
			String dateString2 = newDate2.format(formatter);
			Integer checkDate2 = services.selectCheckDate(dateString2);
			if (checkDate2.equals(1)) {
				dateValue = dateString2;
				x++;
			} else {
				x++;
				y++;
				System.out.println(dateString2);
				days2++;
			}
		}

		return dateValue;
	}

	public String getDateTransaction(Integer group_product) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		// Get tanggal hari ini
		LocalDate sysdate = LocalDate.now();
//		LocalDate sysdate = LocalDate.of(2020, 10, 28);
		String dateParse = sysdate.format(formatter);

		// Check Date libur apa ngga
		Integer checkDate = services.selectCheckDate(dateParse);

		// Get CutOff Link, Magna Link, Prime Link
		String cutOffLink = services.selectCutoffTransactionLink();
		String cutOffMagnaAndPrimeLink = services.selectCutoffTransactionMagnaAndPrimeLink();

		// Parse Date
		LocalTime cutOffLinkTime = LocalTime.parse(cutOffLink);
		LocalTime cutOffMagnaAndPrimeLinkTime = LocalTime.parse(cutOffMagnaAndPrimeLink);

		// Set dateResult menjadi hari ini sebelum dikasih kondisi
		String dateResult = dateParse;

		if (checkDate.equals(1)) {
			// Hari Kerja
			LocalTime timeNow = LocalTime.now();
			// LocalTime timeNow = LocalTime.parse("13:01:00");
			if (group_product.equals(0)) { // Kondisi Cuttoff Link 09:30
				System.out.println(timeNow);
				System.out.println(cutOffLinkTime);
				// Check cutoff Link
				if (timeNow.compareTo(cutOffLinkTime) > 0) {
					dateResult = getWorkingDate();
				}
			} else { // Kondisi CutOff Prime & Magna Link 12:00
				System.out.println(timeNow);
				System.out.println(cutOffMagnaAndPrimeLinkTime);
				// Check cutoff Prime Link & Magna Link
				if (timeNow.compareTo(cutOffMagnaAndPrimeLinkTime) > 0) {
					dateResult = getWorkingDate();
				}
			}
		} else {
			// Hari Libur
			dateResult = getWorkingDate();
		}

		return dateResult;
	}

	public String currencyRupiah(String nominal) {
		String hasil = "";
		DecimalFormat toRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
		DecimalFormatSymbols formatAngka = new DecimalFormatSymbols();
		formatAngka.setCurrencySymbol("");
		formatAngka.setGroupingSeparator('.');
		toRupiah.setDecimalFormatSymbols(formatAngka);
		toRupiah.setMaximumFractionDigits(0);
		hasil = toRupiah.format(Double.valueOf(nominal));
		return hasil;
	}

	public String formatRekening(String rekening) {
		Integer digit_rekening_before_replace_star = rekening.substring(0, rekening.length() - 4).length();
		String four_digit_rekening = rekening.substring(rekening.length() - 4, rekening.length());
		String digit_rekening_after_replace_star = IntStream.range(0, digit_rekening_before_replace_star)
				.mapToObj(i -> "*").collect(Collectors.joining());
		String result = digit_rekening_after_replace_star + four_digit_rekening;

		return result;
	}

	public String clearData(String value) {
		return value.replace(".", "").replace("-", "").replace(",", "").trim();
	}
	
	public void PolicyAlterationDirect(String reg_spaj, String msen_alasan, Integer lsje_id, String msde_old1, String msde_old2,
			String msde_old3, String msde_old4, String msde_old5, String msde_old6,String msde_old7,String msde_old8,String msde_new1, String msde_new2, String msde_new3,
			String msde_new4, String msde_new5, String msde_new6, String msde_new7,String msde_new8,String msde_new9, String kolom, Integer counter)
	{
		Integer lspd_id = 99;
		
		
		// Get MSEN_ENDORSE_NO
		String msen_endors_no = services.selectGetNoEndors();
		
		//INSERT ENDORSE
		services.insertEndorse(msen_endors_no, reg_spaj, msen_alasan, lspd_id);
		
		//INSERT DET ENDORSE
		 services.insertDetailEndorse(msen_endors_no, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,msde_old7, msde_old8,
					msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6, msde_new7, msde_new8,msde_new9);
			
	
		
		//INSERT LST ULANGAN
		services.insertLstUlangan(reg_spaj, msen_alasan,counter);
		
		
		
		
	}
	
	public void PolicyAlterationIndirect(String reg_spaj, String msen_alasan, Integer lsje_id, String msde_old1, String msde_old2,
			String msde_old3, String msde_old4, String msde_old5, String msde_old6, String msde_old7,String msde_old8, String msde_new1, String msde_new2, String msde_new3,
			String msde_new4, String msde_new5, String msde_new6, String msde_new7, String msde_new8,String msde_new9, String kolom)
	{
		Integer lspd_id = 13;
		
		// Get MSEN_ENDORSE_NO
		String msen_endors_no = services.selectGetNoEndors();
		
		//INSERT ENDORSE
		services.insertEndorse(msen_endors_no, reg_spaj, msen_alasan, lspd_id);
		
		//INSERT DET ENDORSE
	//	services.insertDetailEndorse(msen_endors_no, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,msde_old7, msde_old8,
	//			msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6, msde_new7, msde_new8);
	
    services.insertDetailEndorse(msen_endors_no, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,msde_old7, msde_old8,
					msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6, msde_new7, msde_new8,msde_new9);
			
		
		//UPDATE LSPD_ID IN MSPO_POLICY
		services.updateLspdId(reg_spaj);
	}
}
