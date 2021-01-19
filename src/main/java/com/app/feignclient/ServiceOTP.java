package com.app.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.model.request.RequestResendOTP;
import com.app.model.request.RequestSendOTP;
import com.app.model.request.RequestValidateOTP;
import com.app.model.ResponseData;

@FeignClient(name = "api-otp", fallback = ServiceOTPCallback.class)
public interface ServiceOTP {

	@RequestMapping("/send")
	public ResponseData sendOTP(@RequestBody RequestSendOTP requestsSendOTP);

	@RequestMapping("/resend")
	public ResponseData resendOTP(@RequestBody RequestResendOTP requestsResendOTP);

	@RequestMapping("/Validate")
	public ResponseData validateOTP(@RequestBody RequestValidateOTP requestsValidateOTP);
}