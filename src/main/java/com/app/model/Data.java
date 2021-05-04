package com.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Data implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 713869903523320781L;

	private Integer next_action_menu_id;
	private String policy_number;
	
	public Integer getNext_action_menu_id() {
		return next_action_menu_id;
	}
	public void setNext_action_menu_id(Integer next_action_menu_id) {
		this.next_action_menu_id = next_action_menu_id;
	}
	public String getPolicy_number() {
		return policy_number;
	}
	public void setPolicy_number(String policy_number) {
		this.policy_number = policy_number;
	}
}
