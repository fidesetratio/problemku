package com.app.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.model.request.RequestResendOTP;
import com.app.model.request.RequestSendOTP;
import com.app.model.request.RequestValidateOTP;
import com.app.model.ResponseData;

@FeignClient(name = "api-otp", url = "http://128.21.33.70:8687/api-otp", fallback = ServiceOTPCallback.class)
public interface ServiceOTP {

	@RequestMapping("/send")
	public ResponseData sendOTP(@RequestBody RequestSendOTP requestSendOTP);

	@RequestMapping("/resend")
	public ResponseData resendOTP(@RequestBody RequestResendOTP requestResendOTP);

	@RequestMapping("/validate")
	public ResponseData validateOTP(@RequestBody RequestValidateOTP requestValidateOTP);
}
