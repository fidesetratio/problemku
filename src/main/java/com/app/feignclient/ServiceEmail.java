package com.app.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.model.request.RequestSendEmail;
import com.app.model.ResponseData;

@FeignClient(name = "service-email",url = "${cosmos.feign.url}/service-email", fallback = ServiceEmailCallback.class)
public interface ServiceEmail {

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public String check();

	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public ResponseData sendEmail(@RequestBody RequestSendEmail requestSendEmail);

}