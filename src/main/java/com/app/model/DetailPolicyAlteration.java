package com.app.model;

import java.io.Serializable;

public class DetailPolicyAlteration implements Serializable {

	private static final long serialVersionUID = 2356621085798776512L;	
	private Integer id_endors;
	private String old;
	private String new_;
	private Integer flag_direct;
	
	public Integer getId_endors() {
		return id_endors;
	}
	public void setId_endors(Integer id_endors) {
		this.id_endors = id_endors;
	}
	public String getOld() {
		return old;
	}
	public void setOld(String old) {
		this.old = old;
	}
	public String getNew_() {
		return new_;
	}
	public void setNew_(String new_) {
		this.new_ = new_;
	}
	public Integer getFlag_direct() {
		return flag_direct;
	}
	public void setFlag_direct(Integer flag_direct) {
		this.flag_direct = flag_direct;
	}	
}