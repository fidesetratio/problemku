package com.app.services;

import com.app.model.request.RequestBilling;

import javax.servlet.http.HttpServletRequest;

public interface BillingSvc {

    String getBilling(RequestBilling requestBilling, HttpServletRequest request);
}
