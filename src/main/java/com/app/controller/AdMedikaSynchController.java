package com.app.controller;

import com.app.model.AdMedikaRequest;
import com.app.services.AdMedikaSycnhSvc;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@RestController
public class AdMedikaSynchController {

    private static final Logger logger = LogManager.getLogger(AdMedikaSynchController.class);

    @Autowired
    private AdMedikaSycnhSvc adMedikaSycnhSvc;
    @Autowired
    private VegaCustomResourceLoader customResourceLoader;


    @RequestMapping(value = "/urladmedika", produces = "application/json", method = RequestMethod.POST)
    public String getUrl(@RequestBody AdMedikaRequest request, HttpServletRequest servletRequest){
        Date start = new Date();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = new Gson();
        gson = builder.create();
        String resultErr = null;
        String res = null;
        String req = gson.toJson(request);
        boolean error = false;
        String message = null;
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();

        try {
            String url = adMedikaSycnhSvc.getUrl(request, request.getUsername(), servletRequest);
            data.put("url", url);
        }catch (Exception e){
            error = true;
            message = ResponseMessage.ERROR_SYSTEM;
            resultErr = "bad exception " + e;
            logger.error("Path: " + servletRequest.getServletPath() + " Username: " + request.getUsername() + " Error: " + e);
        }
        map.put("error", error);
        map.put("message", message);
        map.put("data", data);
        res = gson.toJson(map);
        customResourceLoader.updateActivity(request.getUsername());
        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 86, new Date(), req, res, 1, resultErr, start, request.getUsername());
        return res;
    }
}
