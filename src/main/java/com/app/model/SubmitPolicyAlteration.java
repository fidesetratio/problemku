package com.app.model;

import java.io.Serializable;

public class SubmitPolicyAlteration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2356621085798776512L;	
	private Integer id_endors;
    private Integer flag_direct;
    private PolicyAlteration policyholder_old;
    private PolicyAlteration policyholder_new;
    
	public Integer getId_endors() {
		return id_endors;
	}
	public void setId_endors(Integer id_endors) {
		this.id_endors = id_endors;
	}
	public Integer getFlag_direct() {
		return flag_direct;
	}
	public void setFlag_direct(Integer flag_direct) {
		this.flag_direct = flag_direct;
	}
	public PolicyAlteration getPolicyholder_old() {
		return policyholder_old;
	}
	public void setPolicyholder_old(PolicyAlteration policyholder_old) {
		this.policyholder_old = policyholder_old;
	}
	public PolicyAlteration getPolicyholder_new() {
		return policyholder_new;
	}
	public void setPolicyholder_new(PolicyAlteration policyholder_new) {
		this.policyholder_new = policyholder_new;
	}
}