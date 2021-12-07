package com.app.services;

import com.app.model.LstUserSimultaneous;
import com.app.model.ResponseData;
import com.app.model.request.RequestLogin;

import javax.servlet.http.HttpServletRequest;

public interface LoginSvc {

    ResponseData login(RequestLogin requestLogin,  HttpServletRequest request, boolean easyPin);

    boolean isIndividu(LstUserSimultaneous checkIndividuOrCorporate);

    boolean isHrUser(LstUserSimultaneous checkIndividuOrCorporate);

    boolean isAccountDplk(LstUserSimultaneous checkIndividuOrCorporate);

    boolean isIndividuCorporate(LstUserSimultaneous checkIndividuOrCorporate);

    boolean corporate(LstUserSimultaneous checkIndividuOrCorporate);

}
