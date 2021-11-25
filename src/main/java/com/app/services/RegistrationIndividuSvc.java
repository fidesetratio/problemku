package com.app.services;

import com.app.model.User;
import com.app.model.request.RequestFindAccount;
import com.app.model.request.RequestRegisterQR;
import com.app.model.request.RequestValidatePolicy;
import com.netflix.ribbon.proxy.annotation.Http;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.text.ParseException;

public interface RegistrationIndividuSvc {

    String findAccount(RequestFindAccount requestFindAccount, HttpServletRequest httpServletRequest) throws ParseException;

    String validatePolicy(RequestValidatePolicy policy, HttpServletRequest request);

    String register(RequestRegisterQR requestRegisterQR, HttpServletRequest request);

    boolean isIndividuMri(String id_simultan, String username);

    String esertMri(Path path, HttpServletRequest request, String username);

    String noHpIndividuAndMri(User dataUserIndividual);
}
