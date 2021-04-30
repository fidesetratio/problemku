package com.app.feignclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.constant.AccountManagementCons;
import com.app.model.ResponseData;
import com.app.model.request.RequestInbox;
import com.app.model.request.RequestSaveToken;

@Component
public class ServiceNotificationCallback implements ServiceNotification {

	@Autowired
	AccountManagementCons constant;

	@Override
	public ResponseData saveToken(RequestSaveToken requestSaveToken) {
		ResponseData response = new ResponseData();
		response.setError(true);
		response.setMessage(constant.error_message);
		response.setData(null);

		return response;
	}

	@Override
	public ResponseData inbox(RequestInbox requestInbox) {
		ResponseData response = new ResponseData();
		response.setError(true);
		response.setMessage(constant.error_message);
		response.setData(null);

		return response;
	}

}