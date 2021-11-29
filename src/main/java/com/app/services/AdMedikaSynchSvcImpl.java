package com.app.services;

import com.app.model.AdMedikaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class AdMedikaSynchSvcImpl implements AdMedikaSycnhSvc{

    private static final Logger logger = LogManager.getLogger(AdMedikaSynchSvcImpl.class);

    @Autowired
    private VegaServices vegaServices;

    @Value("${link.ad_medika:''}")
    private String urlAdmedika;



    @Override
    public String generateToken(AdMedikaRequest request) {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getUrl(AdMedikaRequest request, String username, HttpServletRequest servletRequest) {
        String token = generateToken(request);
        String url = "";
        try {
            AdMedikaRequest config = getConfigAdmedika(servletRequest, username);
            if (config != null){
                url = String.format("%s?token=%s&projectid=%s", urlAdmedika, generateToken(request), config.getProject_id());
            }
        }catch (Exception e){
            String resultErr  = e.getMessage();
            logger.error("Path: " + servletRequest.getServletPath() + " Username: " + username + ", Error: " + resultErr);
        }

        return url;
    }

    @Override
    public AdMedikaRequest getConfigAdmedika(HttpServletRequest servletRequest, String username) {
        AdMedikaRequest dataConfig = null;
        try {
            String admedikaConfig = vegaServices.getConfigAdmedika();
            ObjectMapper objectMapper = new ObjectMapper();
            dataConfig = objectMapper.readValue(admedikaConfig, AdMedikaRequest.class);
        }catch (Exception e){
            String resultErr  = e.getMessage();
            logger.error("Path: " + servletRequest.getServletPath() + " Username: " + username + ", Error: " + resultErr);
        }
        return dataConfig;
    }
}
