package com.app.controller;

import com.app.exception.HandleSuccessOrNot;
import com.app.model.*;
import com.app.services.LoginSvc;
import com.app.services.VegaServices;
import com.app.utils.VegaCustomResourceLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class TestController {

    private static final Logger logger = LogManager.getLogger(TestController.class);

    @Autowired
    private VegaCustomResourceLoader customResourceLoader;
    @Autowired
    private LoginSvc loginSvc;

    @Autowired
    private VegaServices vegaServices;

    @RequestMapping(value = "/testhit", method = RequestMethod.GET)
    public String testHit() {
        String printHit = vegaServices.selectEncrypt("test");
        return printHit;
    }

    @RequestMapping(value = "/checkotp", method = RequestMethod.GET)
    public String kodeOtp(@RequestParam String no_hp, @RequestParam Integer jenis_id, @RequestParam Integer menu_id) {
        String code = vegaServices.selectCheckOTP(no_hp, jenis_id, menu_id);

        return code;
    }

    @RequestMapping(value = "/findotp", method = RequestMethod.GET)
    public String kodeOtp(@RequestParam(value = "username", required = false) String username, @RequestParam(value = "no_hp", required = false) String no_hp) {
        Gson gson = new Gson();
        String res;

        OtpTest code = null;
        if (username != null){
            LstUserSimultaneous checkIndividuOrCorporate = vegaServices.selectDataLstUserSimultaneous(username);
            if (checkIndividuOrCorporate != null){
                boolean isIndividu = loginSvc.isIndividu(checkIndividuOrCorporate);
                boolean isIndividuCorporate = loginSvc.isIndividuCorporate(checkIndividuOrCorporate);
                boolean corporate = loginSvc.corporate(checkIndividuOrCorporate);
                if (isIndividu){
                    User dataActivityUser = vegaServices.selectUserIndividual(username);
                    code = vegaServices.selectTopActiveOtp(dataActivityUser.getNo_hp());
                } else if (isIndividuCorporate || corporate){
                    UserCorporate dataUserCorporate = vegaServices.selectUserCorporate(username);
                    code = vegaServices.selectTopActiveOtp(dataUserCorporate.getNo_hp());
                } else {
                    code = vegaServices.selectTopActiveOtp();
                }
            }
        } else if (no_hp != null){
            code = vegaServices.selectTopActiveOtp(no_hp);
        } else {
          code = vegaServices.selectTopActiveOtp();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", code != null ? code.getUsername() : "");
        map.put("otp_code", code != null ? code.getOTP_NO() : "");
        res = gson.toJson(map);
        return res;
    }

    @RequestMapping(value = "/documentcenter", method = RequestMethod.POST)
    public ResponseData getDocument(@RequestBody DocumentCenter payloads) {
        HandleSuccessOrNot handleSuccessOrNot;
        String username = payloads.getUsername();
        String key = payloads.getKey();
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> data = new HashMap<>();
        boolean isGranted;
        if (username.equalsIgnoreCase("guest") && key.equalsIgnoreCase("guest")
                || username.equalsIgnoreCase("") && key.equalsIgnoreCase("")) {
            isGranted = true;
        } else if (customResourceLoader.validateCredential(username, key)){
            isGranted = true;
        } else {
            isGranted = false;
        }

        if (isGranted){
            String configDoc = vegaServices.getConfigDocument();
            DocumentCenter documentCenter = parseDocumentConfig(configDoc);
            data.put("config_docs", list);
            if (documentCenter != null) {
                for (DocumentCenterDetail detail : documentCenter.getData()){
                    HashMap<String, Object> det = new HashMap<>();
                    det.put("nama_folder", detail.getNama_folder());
                    det.put("url", detail.getUrl());
                    list.add(det);
                }
                handleSuccessOrNot = new HandleSuccessOrNot(false, "success get config document");
            } else {
                handleSuccessOrNot = new HandleSuccessOrNot(true, "failed get config document");
            }
        } else {
            handleSuccessOrNot = new HandleSuccessOrNot(true, "failed get config document");
        }
        ResponseData responseData = new ResponseData();
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);

        return responseData;

    }

    private DocumentCenter parseDocumentConfig(String config) {
        DocumentCenter data = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            data = mapper.readValue(config, DocumentCenter.class);
        } catch (JsonProcessingException e) {
            logger.debug("error parse json description request" + e.getMessage());
        }
        return data;
    }

}
