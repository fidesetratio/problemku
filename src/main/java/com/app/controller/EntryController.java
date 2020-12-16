package com.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Precision;
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
		
		List<JnBank> arrayList = new ArrayList<JnBank>();
		arrayList.add(new JnBank(1," Ini Demo aja sihhh"));
		arrayList.add(new JnBank(2," Ini Demo llalu aja sihhh"));
		arrayList =  jnServices.getAll();
		return arrayList;
	}

	@GetMapping("/biaya")
	public Double biaya() {
		Double number = 122003.90 * 0.05;
	   return Precision.round(number,2);
				
	}
	
	@GetMapping("/nett")
	public Double nett() {
		Double number = 122003.90 - 122003.90 *0.05;
	   return Precision.round(number,3);
				
	}
	@GetMapping("/nilai")
	public Double nilai() {
		Double number = 122003.90;
	   return Precision.round(number,2);
				
	}
}

