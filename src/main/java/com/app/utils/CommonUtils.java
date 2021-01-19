package com.app.utils;

import org.springframework.stereotype.Component;

@Component
public class CommonUtils {

	public String convertPhoneNumber(String phone_number) {
		return phone_number.replace("-", "").replace(".", "").replace(",", "").replace("_", "").trim();
	}

}