package com.app.services;

import com.app.model.request.RequestTopup;

import javax.servlet.http.HttpServletRequest;

public interface TransactionProcessSvc {

    String submitDataTransaction(RequestTopup requestTopup,  HttpServletRequest request);
}
