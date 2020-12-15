package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.JnBank;
import com.app.services.JnBankServices;

@RestController
public class EntryController {
	@Autowired
	private JnBankServices jnServices;
	
	
	@GetMapping("/logs")
	public String logs() {
		return "Logs Entry Controller";
	}
	
	@GetMapping("/jn_banks")
	public List<JnBank> jn_banks() {
		return jnServices.getAll();
	}

}
