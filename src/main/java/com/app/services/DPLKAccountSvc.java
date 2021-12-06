package com.app.services;

import com.app.model.DPLKAccountModel;
import com.app.model.ResponseData;

import javax.servlet.http.HttpServletRequest;

public interface DPLKAccountSvc {

    ResponseData findAccountDPLK(DPLKAccountModel requestBody, HttpServletRequest request);

    ResponseData registerAccountDplk(DPLKAccountModel requestBody, HttpServletRequest request);

    ResponseData getGeneralInfoDplk(DPLKAccountModel requestBody, HttpServletRequest request);
}
