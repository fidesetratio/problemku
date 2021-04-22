package com.app.model;

import java.io.Serializable;

public class DropdownPolicyAlteration implements Serializable {

	private static final long serialVersionUID = 2356621085798776512L;	
	
	private Integer lsst_id;
	private String lsst_name;
	
	private Integer lsag_id;
	private String lsag_name;
	
	private Integer lsne_id;
	private String lsne_note;
	
	private Integer lju_id;
	private String lju_usaha;
	
	private Integer lsp_id;
	private String lsp_name;
	
	private Integer lsre_id;
	private String lsre_relation;
	
	private Integer lsbp_id;
	private Integer lbn_id;
	private String nama_bank;
	private String cabang;
	
	public Integer getLsp_id() {
		return lsp_id;
	}
	public void setLsp_id(Integer lsp_id) {
		this.lsp_id = lsp_id;
	}
	public String getLsp_name() {
		return lsp_name;
	}
	public void setLsp_name(String lsp_name) {
		this.lsp_name = lsp_name;
	}
	public Integer getLsst_id() {
		return lsst_id;
	}
	public void setLsst_id(Integer lsst_id) {
		this.lsst_id = lsst_id;
	}
	public String getLsst_name() {
		return lsst_name;
	}
	public void setLsst_name(String lsst_name) {
		this.lsst_name = lsst_name;
	}
	public Integer getLsag_id() {
		return lsag_id;
	}
	public void setLsag_id(Integer lsag_id) {
		this.lsag_id = lsag_id;
	}
	public String getLsag_name() {
		return lsag_name;
	}
	public void setLsag_name(String lsag_name) {
		this.lsag_name = lsag_name;
	}
	public Integer getLsne_id() {
		return lsne_id;
	}
	public void setLsne_id(Integer lsne_id) {
		this.lsne_id = lsne_id;
	}
	public String getLsne_note() {
		return lsne_note;
	}
	public void setLsne_note(String lsne_note) {
		this.lsne_note = lsne_note;
	}
	public Integer getLju_id() {
		return lju_id;
	}
	public void setLju_id(Integer lju_id) {
		this.lju_id = lju_id;
	}
	public String getLju_usaha() {
		return lju_usaha;
	}
	public void setLju_usaha(String lju_usaha) {
		this.lju_usaha = lju_usaha;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public String getLsre_relation() {
		return lsre_relation;
	}
	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	public Integer getLsbp_id() {
		return lsbp_id;
	}
	public void setLsbp_id(Integer lsbp_id) {
		this.lsbp_id = lsbp_id;
	}
	public Integer getLbn_id() {
		return lbn_id;
	}
	public void setLbn_id(Integer lbn_id) {
		this.lbn_id = lbn_id;
	}
	public String getNama_bank() {
		return nama_bank;
	}
	public void setNama_bank(String nama_bank) {
		this.nama_bank = nama_bank;
	}
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
}