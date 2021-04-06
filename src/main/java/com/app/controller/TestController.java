package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.services.VegaServices;

import io.swagger.annotations.ApiOperation;

@RestController
public class TestController {

	@Autowired
	private VegaServices vegaServices;
	
	@ApiOperation(value="Service untu melakukan test memastikan semua aplikasi berhubungan database jalan ")
	@RequestMapping(value = "/testhit", method = RequestMethod.GET)
	public String testHit() {
		String printHit = vegaServices.selectEncrypt("test");
		return printHit;
	}

}
