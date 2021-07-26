package com.app.model;

import java.io.Serializable;

import org.json.JSONObject;

public class DetailPolicyAlteration implements Serializable {

	private static final long serialVersionUID = 2356621085798776512L;	
	private Integer id_endors;
	private String old;
	private String new_;
	private String new_value;
	private Integer flag_direct;
	
	
	public static JSONObject convertToJson(DetailPolicyAlteration data) {
		JSONObject object = new JSONObject();
		object.put("id_endors", (data.getId_endors()==null ?JSONObject.NULL:data.getId_endors()));
		object.put("old", (data.getOld() == null?JSONObject.NULL:data.getOld()));
		object.put("new_", (data.getNew_() == null?JSONObject.NULL:data.getNew_()));
		object.put("flag_direct", (data.getFlag_direct()==null?JSONObject.NULL:data.getFlag_direct()));
		object.put("status", (data.getStatus()==null?JSONObject.NULL:data.getStatus()));
		if(data.getNew_value() != null) {
		object.put("new_value", (data.getNew_value()==null?JSONObject.NULL:data.getNew_value()));
		};
		return object;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	private String group;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String status;
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

	public String getNew_value() {
		return new_value;
	}
	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}
	

}