package com.app.services;

import com.app.exception.HandleSuccessOrNot;
import com.app.model.AdMedikaRequest;
import com.app.model.EnrollPesertaAdmedika;
import com.app.model.LstUserSimultaneous;
import com.app.model.ResponseData;
import com.app.utils.DateUtils;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AdMedikaSynchSvcImpl implements AdMedikaSycnhSvc {

    private static final Logger logger = LogManager.getLogger(AdMedikaSynchSvcImpl.class);

    private final VegaServices vegaServices;
    private final LoginSvc loginSvc;
    private final VegaCustomResourceLoader customResourceLoader;
    private final DateUtils dateUtils;

    @Value("${link.ad_medika:''}")
    private String urlAdmedika;

    @Autowired
    public AdMedikaSynchSvcImpl(VegaServices vegaServices, LoginSvc loginSvc, VegaCustomResourceLoader customResourceLoader,
                                DateUtils dateUtils) {
        this.vegaServices = vegaServices;
        this.loginSvc = loginSvc;
        this.customResourceLoader = customResourceLoader;
        this.dateUtils = dateUtils;
    }


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
            if (config != null) {
                url = String.format("%s?token=%s&projectid=%s", urlAdmedika, generateToken(request), config.getProject_id());
            }
        } catch (Exception e) {
            String resultErr = e.getMessage();
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
        } catch (Exception e) {
            String resultErr = e.getMessage();
            logger.error("Path: " + servletRequest.getServletPath() + " Username: " + username + ", Error: " + resultErr);
        }
        return dataConfig;
    }

    @Override
    public ResponseData getEnrollPeserta(AdMedikaRequest adMedikaRequest, HttpServletRequest httpServletRequest) {
        Date start = new Date();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = new Gson();
        gson = builder.create();
        String req = gson.toJson(adMedikaRequest);
        String username = adMedikaRequest.getUsername();
        String key = adMedikaRequest.getKey();

        HandleSuccessOrNot handleSuccessOrNot;
        HashMap<String, Object> data = new HashMap<>();
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();

        try {

            if (customResourceLoader.validateCredential(username, key)) {
                LstUserSimultaneous selectTypeUser = vegaServices.selectDataLstUserSimultaneous(username);

                boolean isIndividu = loginSvc.isIndividu(selectTypeUser);
                boolean corporate = loginSvc.isIndividuCorporate(selectTypeUser) || loginSvc.corporate(selectTypeUser);
                if (corporate) {
                    List<EnrollPesertaAdmedika> enrollPesertaAdmedika = vegaServices.getEnrollPesertaAdmedikaCorporate(username);
                    for (EnrollPesertaAdmedika pesertaAdmedika : enrollPesertaAdmedika){
                        arrayList.add(getMapEnrollPeserta(pesertaAdmedika));
                    }
                    data.put("corporate", arrayList);
                } else if (isIndividu){
                    //TODO query peserta individu admedika enroll
                    List<EnrollPesertaAdmedika> enrollPesertaAdmedika = vegaServices.getEnrollPesertaAdmedikaCorporate(username);
                    for (EnrollPesertaAdmedika pesertaAdmedika : enrollPesertaAdmedika){
                        arrayList.add(getMapEnrollPeserta(pesertaAdmedika));
                    }
                    data.put("individu", arrayList);
                }
                handleSuccessOrNot = new HandleSuccessOrNot(false, "Success get data enroll peserta admedika");
            } else {
                String resultErr = "Username tidak terdaftar";
                handleSuccessOrNot = new HandleSuccessOrNot(true, "Login failed");
                logger.error("Path: " + httpServletRequest.getServletPath() + " Username: " + username + " Error: " + resultErr);
            }
        } catch (Exception e) {
            handleSuccessOrNot = new HandleSuccessOrNot(true, ResponseMessage.ERROR_SYSTEM);
            logger.error(String.format("%s: %s, %s: %s", "Path", httpServletRequest.getServletPath(), "error get info account", e));
        }
        ResponseData responseData = new ResponseData();
        responseData.setError(handleSuccessOrNot.isError());
        responseData.setMessage(handleSuccessOrNot.getMessage());
        responseData.setData(data);

        customResourceLoader.updateActivity(username);
        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 86, new Date(), req, responseData.getData().toString(), 1, responseData.getMessage(), start, username);

        return responseData;
    }

    private HashMap<String, Object> getMapEnrollPeserta(EnrollPesertaAdmedika enrollPesertaAdmedika) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("policy_number", enrollPesertaAdmedika.getPolicy_number());
        data.put("no_kartu", enrollPesertaAdmedika.getNo_kartu());
        data.put("mcl_id", enrollPesertaAdmedika.getMcl_id());
        data.put("participant_name", enrollPesertaAdmedika.getParticipant_name());
        data.put("company_name", enrollPesertaAdmedika.getCompany_name());
        data.put("dob", dateUtils.getFormatterFormat(enrollPesertaAdmedika.getDob(), DateUtils.FORMAT_DAY_MONTH_YEAR, "GMT+7"));
        data.put("member_type", enrollPesertaAdmedika.getMembertype());
        data.put("email", enrollPesertaAdmedika.getMspe_email());
        return data;
    }
}
