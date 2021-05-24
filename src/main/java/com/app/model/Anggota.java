package com.app.model;

import java.io.Serializable;

public class Anggota implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2356621085798776512L;	
	private DetailPolicyAlteration msaw_first;
	private DetailPolicyAlteration msaw_birth;
	private DetailPolicyAlteration msaw_persen;
	private DetailPolicyAlteration msaw_number;
    private DetailPolicyAlteration lsre_relation;
    private DetailPolicyAlteration msaw_sex;
    
	public DetailPolicyAlteration getMsaw_first() {
		return msaw_first;
	}
	public void setMsaw_first(DetailPolicyAlteration msaw_first) {
		this.msaw_first = msaw_first;
	}
	public DetailPolicyAlteration getMsaw_birth() {
		return msaw_birth;
	}
	public void setMsaw_birth(DetailPolicyAlteration msaw_birth) {
		this.msaw_birth = msaw_birth;
	}
	public DetailPolicyAlteration getMsaw_persen() {
		return msaw_persen;
	}
	public void setMsaw_persen(DetailPolicyAlteration msaw_persen) {
		this.msaw_persen = msaw_persen;
	}
	public DetailPolicyAlteration getMsaw_number() {
		return msaw_number;
	}
	public void setMsaw_number(DetailPolicyAlteration msaw_number) {
		this.msaw_number = msaw_number;
	}
	public DetailPolicyAlteration getLsre_relation() {
		return lsre_relation;
	}
	public void setLsre_relation(DetailPolicyAlteration lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	public DetailPolicyAlteration getMsaw_sex() {
		return msaw_sex;
	}
	public void setMsaw_sex(DetailPolicyAlteration msaw_sex) {
		this.msaw_sex = msaw_sex;
	}
    
    
}