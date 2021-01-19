package com.app.feignclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.constant.AccountManagementCons;
import com.app.model.request.RequestResendOTP;
import com.app.model.request.RequestSendOTP;
import com.app.model.request.RequestValidateOTP;
import com.app.model.ResponseData;

@Component
public class ServiceOTPCallback implements ServiceOTP {

	@Autowired
	AccountManagementCons constant;

	@Override
	public ResponseData sendOTP(RequestSendOTP requestSendOTP) {
		ResponseData response = new ResponseData();
		response.setError(true);
		response.setMessage(constant.error_message);
		response.setData(null);

		return response;
	}

	@Override
	public ResponseData resendOTP(RequestResendOTP requestResendOTP) {
		ResponseData response = new ResponseData();
		response.setError(true);
		response.setMessage(constant.error_message);
		response.setData(null);

		return response;
	}

	@Override
	public ResponseData validateOTP(RequestValidateOTP requestValidateOTP) {
		ResponseData response = new ResponseData();
		response.setError(true);
		response.setMessage(constant.error_message);
		response.setData(null);

		return response;
	}
}