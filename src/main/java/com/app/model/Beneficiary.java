package com.app.model;

import java.io.Serializable;

public class Beneficiary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2356621085798776512L;	
	private String reg_spaj;
	private Integer msaw_number;
    private String msaw_first;
    private String msaw_birth;
    private String lsre_relation;
    private Integer msaw_persen;
    
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getMsaw_number() {
		return msaw_number;
	}
	public void setMsaw_number(Integer msaw_number) {
		this.msaw_number = msaw_number;
	}
	public String getMsaw_first() {
		return msaw_first;
	}
	public void setMsaw_first(String msaw_first) {
		this.msaw_first = msaw_first;
	}
	public String getMsaw_birth() {
		return msaw_birth;
	}
	public void setMsaw_birth(String msaw_birth) {
		this.msaw_birth = msaw_birth;
	}
	public String getLsre_relation() {
		return lsre_relation;
	}
	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	public Integer getMsaw_persen() {
		return msaw_persen;
	}
	public void setMsaw_persen(Integer msaw_persen) {
		this.msaw_persen = msaw_persen;
	}
    
    
}