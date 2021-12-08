package com.app.controller;

import com.app.model.DPLKAccountModel;
import com.app.model.ResponseData;
import com.app.services.DPLKAccountSvc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DPLKController {

    private static final Logger logger = LogManager.getLogger(DPLKController.class);

    @Autowired
    private DPLKAccountSvc dplkAccountSvc;

    @RequestMapping(value = "/findaccountdplk", produces = "application/json", method = RequestMethod.POST)
    public ResponseData findAccountDplk(@RequestBody DPLKAccountModel requestBody, HttpServletRequest request){
        return dplkAccountSvc.findAccountDPLK(requestBody, request);
    }

    @RequestMapping(value = "/registeraccountdplk", produces = "application/json", method = RequestMethod.POST)
    public ResponseData registerAccountDplk(@RequestBody DPLKAccountModel requestBody, HttpServletRequest request){
        return dplkAccountSvc.registerAccountDplk(requestBody, request);
    }

    @RequestMapping(value = "/getinfodplk", produces = "application/json", method = RequestMethod.POST)
    public ResponseData getInfoDplk(@RequestBody DPLKAccountModel requestBody, HttpServletRequest request){
        return dplkAccountSvc.getGeneralInfoDplk(requestBody, request);
    }

    @RequestMapping(value = "/listjenisinvestdplk", produces = "application/json", method = RequestMethod.POST)
    public ResponseData getListJenisInvest(@RequestBody DPLKAccountModel requestBody, HttpServletRequest request){
        return dplkAccountSvc.getJenisFundDplk(requestBody, request);
    }

    @RequestMapping(value = "/detailjenisinvestdplk", produces = "application/json", method = RequestMethod.POST)
    public ResponseData getJenisInvest(@RequestBody DPLKAccountModel requestBody,  HttpServletRequest request){
        return dplkAccountSvc.getLstTransaksiFund(requestBody, request);
    }
}
