package com.app.services;

import com.app.model.DownloadAttachment;
import com.app.model.SummaryPayment;
import com.app.model.request.RequestTopup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TransactionSubscriptionSvc {

    String submitDataTransaction(RequestTopup requestTopup,  HttpServletRequest request);

    String saveBuktiPembayaran(SummaryPayment requestBody, HttpServletRequest request);

    String downloadAttachmentHistory(DownloadAttachment requestBody, HttpServletRequest request, HttpServletResponse response);
}
