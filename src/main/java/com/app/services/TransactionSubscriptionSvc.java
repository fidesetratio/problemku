package com.app.services;

import com.app.model.SummaryPayment;
import com.app.model.request.RequestTopup;

import javax.servlet.http.HttpServletRequest;

public interface TransactionSubscriptionSvc {

    String submitDataTransaction(RequestTopup requestTopup,  HttpServletRequest request);

    String saveBuktiPembayaran(SummaryPayment requestBody, HttpServletRequest request);

    String getPaymentType(Integer lsjb_id);

}
