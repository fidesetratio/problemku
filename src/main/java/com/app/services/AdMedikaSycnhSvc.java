package com.app.services;

import com.app.model.AdMedikaRequest;
import com.app.model.ResponseData;

import javax.servlet.http.HttpServletRequest;

public interface AdMedikaSycnhSvc {

    String getUrl(AdMedikaRequest request, String username, HttpServletRequest servletRequest);

    AdMedikaRequest getConfigAdmedika(HttpServletRequest servletRequest, String username);

    ResponseData getEnrollPeserta(AdMedikaRequest adMedikaRequest, HttpServletRequest httpServletRequest);

}
