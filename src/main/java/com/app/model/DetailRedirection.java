package com.app.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailRedirection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6046279584374086272L;
	ArrayList<DetailSourceRedirection> source;
	ArrayList<DetailDestRedirection> destination;

	public ArrayList<DetailSourceRedirection> getSource() {
		return source;
	}

	public ArrayList<DetailDestRedirection> getDestination() {
		return destination;
	}

	public void setSource(ArrayList<DetailSourceRedirection> source) {
		this.source = source;
	}

	public void setDestination(ArrayList<DetailDestRedirection> destination) {
		this.destination = destination;
	}
}