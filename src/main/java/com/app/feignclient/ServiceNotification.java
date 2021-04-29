package com.app.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.model.request.RequestSaveToken;
import com.app.model.request.RequestInbox;
import com.app.model.ResponseData;

@FeignClient(name = "api-notification", url = "http://128.21.33.70:8687/api-notification", fallback = ServiceNotificationCallback.class)
public interface ServiceNotification {

	@RequestMapping(value = "/savetoken", method = RequestMethod.GET)
	public ResponseData saveToken(@RequestBody RequestSaveToken requestSaveToken);

	@RequestMapping(value = "/inbox", method = RequestMethod.POST)
	public ResponseData inbox(@RequestBody RequestInbox requestInbox);

}