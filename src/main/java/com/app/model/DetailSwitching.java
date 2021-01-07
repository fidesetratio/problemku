package com.app.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailSwitching implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6816840170738782926L;
	ArrayList<DetailSourceSwitching> source;
	ArrayList<DetailDestSwitching> destination;

	public ArrayList<DetailSourceSwitching> getSource() {
		return source;
	}

	public ArrayList<DetailDestSwitching> getDestination() {
		return destination;
	}

	public void setSource(ArrayList<DetailSourceSwitching> source) {
		this.source = source;
	}

	public void setDestination(ArrayList<DetailDestSwitching> destination) {
		this.destination = destination;
	}
}