package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class LstUserSimultaneous implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4485798948247079852L;

	private String USERNAME;
	private String PASSWORD;
	private String ID_SIMULTAN;
	private int FLAG_ACTIVE;
	private Date CREATE_DATE;
	private Date UPDATE_DATE;
	private Date LAST_LOGIN_DATE;
	private String LAST_LOGIN_DATE_TIME;
	private String UPDATE_DATE_TIME;
	private String LAST_LOGIN_DEVICE;
	private String REG_SPAJ;
	private String KEY;
	private String DATE_CREATED_JAVA;
	private String MCL_ID_EMPLOYEE;
	private String EB_HR_USERNAME;

	public String getEB_HR_USERNAME() {
		return EB_HR_USERNAME;
	}

	public void setEB_HR_USERNAME(String eB_HR_USERNAME) {
		EB_HR_USERNAME = eB_HR_USERNAME;
	}

	public LstUserSimultaneous() {
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getID_SIMULTAN() {
		return ID_SIMULTAN;
	}

	public void setID_SIMULTAN(String iD_SIMULTAN) {
		ID_SIMULTAN = iD_SIMULTAN;
	}

	public int getFLAG_ACTIVE() {
		return FLAG_ACTIVE;
	}

	public void setFLAG_ACTIVE(int fLAG_ACTIVE) {
		FLAG_ACTIVE = fLAG_ACTIVE;
	}

	public Date getCREATE_DATE() {
		return CREATE_DATE;
	}

	public void setCREATE_DATE(Date cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}

	public Date getUPDATE_DATE() {
		return UPDATE_DATE;
	}

	public void setUPDATE_DATE(Date uPDATE_DATE) {
		UPDATE_DATE = uPDATE_DATE;
	}

	public Date getLAST_LOGIN_DATE() {
		return LAST_LOGIN_DATE;
	}

	public void setLAST_LOGIN_DATE(Date lAST_LOGIN_DATE) {
		LAST_LOGIN_DATE = lAST_LOGIN_DATE;
	}

	public String getLAST_LOGIN_DEVICE() {
		return LAST_LOGIN_DEVICE;
	}

	public String getLAST_LOGIN_DATE_TIME() {
		return LAST_LOGIN_DATE_TIME;
	}

	public void setLAST_LOGIN_DATE_TIME(String lAST_LOGIN_DATE_TIME) {
		LAST_LOGIN_DATE_TIME = lAST_LOGIN_DATE_TIME;
	}

	public void setLAST_LOGIN_DEVICE(String lAST_LOGIN_DEVICE) {
		LAST_LOGIN_DEVICE = lAST_LOGIN_DEVICE;
	}

	public String getREG_SPAJ() {
		return REG_SPAJ;
	}

	public void setREG_SPAJ(String rEG_SPAJ) {
		REG_SPAJ = rEG_SPAJ;
	}

	public String getKEY() {
		return KEY;
	}

	public void setKEY(String kEY) {
		KEY = kEY;
	}

	public String getDATE_CREATED_JAVA() {
		return DATE_CREATED_JAVA;
	}

	public void setDATE_CREATED_JAVA(String dATE_CREATED_JAVA) {
		DATE_CREATED_JAVA = dATE_CREATED_JAVA;
	}

	public String getUPDATE_DATE_TIME() {
		return UPDATE_DATE_TIME;
	}

	public void setUPDATE_DATE_TIME(String uPDATE_DATE_TIME) {
		UPDATE_DATE_TIME = uPDATE_DATE_TIME;
	}

	public String getMCL_ID_EMPLOYEE() {
		return MCL_ID_EMPLOYEE;
	}

	public void setMCL_ID_EMPLOYEE(String mCL_ID_EMPLOYEE) {
		MCL_ID_EMPLOYEE = mCL_ID_EMPLOYEE;
	}
}
