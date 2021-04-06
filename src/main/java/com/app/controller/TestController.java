package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.services.VegaServices;

import io.swagger.annotations.ApiOperation;

@RestController
public class TestController {

	@Autowired
	private VegaServices vegaServices;

	@RequestMapping(value = "/testhit", method = RequestMethod.GET)
	public String testHit() {
		String printHit = vegaServices.selectEncrypt("test");
		return printHit;
	}
	
	@RequestMapping(value = "/checkotp", method = RequestMethod.GET)
	public String kodeOtp(@RequestParam String no_hp, @RequestParam Integer jenis_id, @RequestParam Integer menu_id) {
		String code = vegaServices.selectCheckOTP(no_hp, jenis_id, menu_id);

		return code;
	}

}
