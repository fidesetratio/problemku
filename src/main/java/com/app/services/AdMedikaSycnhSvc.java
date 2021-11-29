package com.app.services;

import com.app.model.AdMedikaRequest;

import javax.servlet.http.HttpServletRequest;

public interface AdMedikaSycnhSvc {

    String generateToken(AdMedikaRequest request);

    String getUrl(AdMedikaRequest request, String username, HttpServletRequest servletRequest);

    AdMedikaRequest getConfigAdmedika(HttpServletRequest servletRequest, String username);
}
