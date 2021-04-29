package com.app.feignclient;

import org.springframework.stereotype.Component;

import com.app.model.request.RequestSendEmail;
import com.app.model.ResponseData;

@Component
public class ServiceNotificationCallback implements ServiceEmail {

	@Override
	public String check() {
		return "Mohon maaf system sedang error";
	}

	@Override
	public ResponseData sendEmail(RequestSendEmail requestSendEmail) {
		return null;
	}

}