package com.app.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.model.request.RequestSaveToken;
import com.app.model.request.RequestInbox;
import com.app.model.ResponseData;

@FeignClient(name = "api-notif", url = "${cosmos.feign.url}/api-notif", fallback = ServiceNotificationCallback.class)
public interface ServiceNotification {

	@RequestMapping(value = "/savetoken")
	public ResponseData saveToken(@RequestBody RequestSaveToken requestSaveToken);

	@RequestMapping(value = "/inbox")
	public ResponseData inbox(@RequestBody RequestInbox requestInbox);

}