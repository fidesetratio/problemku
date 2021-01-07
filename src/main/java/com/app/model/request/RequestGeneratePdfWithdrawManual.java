package com.app.model.request;

import java.io.Serializable;
import java.util.ArrayList;

import com.app.model.DetailWithdraw;

public class RequestGeneratePdfWithdrawManual implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1319522983039850033L;

	public String no_polis;
	public String reg_spaj;
	public String payor_name;
	public String path_output;
	public ArrayList<DetailWithdraw> data_fund;

	public String getNo_polis() {
		return no_polis;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public String getPayor_name() {
		return payor_name;
	}

	public String getPath_output() {
		return path_output;
	}

	public ArrayList<DetailWithdraw> getData_fund() {
		return data_fund;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setPayor_name(String payor_name) {
		this.payor_name = payor_name;
	}

	public void setPath_output(String path_output) {
		this.path_output = path_output;
	}

	public void setData_fund(ArrayList<DetailWithdraw> data_fund) {
		this.data_fund = data_fund;
	}
}