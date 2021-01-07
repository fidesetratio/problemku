package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class LstHistActivityWS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1258721810085225593L;

	private Integer CLIENT_ID;
	private Integer PROCESS_ID;
	private Date PROCESS_DATE; // End date
	private String PROCESS_DESC;
	private String PROCESS_RESULT;
	private Integer METHOD;
	private String MSAH_IP;
	private String ERR;
	private Date START_DATE; // Start date
	private String KEY;

	public LstHistActivityWS() {
	}

	public String getKEY() {
		return KEY;
	}

	public void setKEY(String kEY) {
		KEY = kEY;
	}

	public Integer getCLIENT_ID() {
		return CLIENT_ID;
	}

	/**
	 * 
	 * @param cLIENT_ID if set to null, it will give value 10 (Eksternal Print)
	 */
	public void setCLIENT_ID(Integer cLIENT_ID) {
		CLIENT_ID = cLIENT_ID;
	}

	public Integer getPROCESS_ID() {
		return PROCESS_ID;
	}

	public void setPROCESS_ID(Integer pROCESS_ID) {
		PROCESS_ID = pROCESS_ID;
	}

	public Date getPROCESS_DATE() {
		return PROCESS_DATE;
	}

	public void setPROCESS_DATE(Date pROCESS_DATE) {
		PROCESS_DATE = pROCESS_DATE;
	}

	public String getPROCESS_DESC() {
		return PROCESS_DESC;
	}

	public void setPROCESS_DESC(String pROCESS_DESC) {
		PROCESS_DESC = pROCESS_DESC;
	}

	public String getPROCESS_RESULT() {
		return PROCESS_RESULT;
	}

	public void setPROCESS_RESULT(String pROCESS_RESULT) {
		PROCESS_RESULT = pROCESS_RESULT;
	}

	public Integer getMETHOD() {
		return METHOD;
	}

	/**
	 * 0 : GET 1 : POST
	 * 
	 * @param mETHOD
	 */
	public void setMETHOD(Integer mETHOD) {
		METHOD = mETHOD;
	}

	public String getMSAH_IP() {
		return MSAH_IP;
	}

	public void setMSAH_IP(String mSAH_IP) {
		MSAH_IP = mSAH_IP;
	}

	public String getERR() {
		return ERR;
	}

	public void setERR(String eRR) {
		ERR = eRR;
	}

	public Date getSTART_DATE() {
		return START_DATE;
	}

	public void setSTART_DATE(Date sTART_DATE) {
		START_DATE = sTART_DATE;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}