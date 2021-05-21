package com.app.model;

import java.io.Serializable;

public class DownloadReportHr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8216231010073285032L;
	private String mbc_no_format;
	private String type_claim;
	private String mbc_tgl_input;
	private String mbc_no;
	private String mce_klaim_admedika;
	
	public String getMbc_no_format() {
		return mbc_no_format;
	}
	public void setMbc_no_format(String mbc_no_format) {
		this.mbc_no_format = mbc_no_format;
	}
	public String getType_claim() {
		return type_claim;
	}
	public void setType_claim(String type_claim) {
		this.type_claim = type_claim;
	}
	public String getMbc_tgl_input() {
		return mbc_tgl_input;
	}
	public void setMbc_tgl_input(String mbc_tgl_input) {
		this.mbc_tgl_input = mbc_tgl_input;
	}
	public String getMbc_no() {
		return mbc_no;
	}
	public void setMbc_no(String mbc_no) {
		this.mbc_no = mbc_no;
	}
	public String getMce_klaim_admedika() {
		return mce_klaim_admedika;
	}
	public void setMce_klaim_admedika(String mce_klaim_admedika) {
		this.mce_klaim_admedika = mce_klaim_admedika;
	}
}