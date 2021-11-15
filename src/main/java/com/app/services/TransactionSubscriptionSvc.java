package com.app.services;

import com.app.model.request.RequestTopup;

import javax.servlet.http.HttpServletRequest;

public interface TransactionSubscriptionSvc {

    String submitDataTransaction(RequestTopup requestTopup,  HttpServletRequest request);
}
