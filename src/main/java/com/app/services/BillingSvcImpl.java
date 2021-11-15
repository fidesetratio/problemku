package com.app.services;

import com.app.model.Billing;
import com.app.model.Pemegang;
import com.app.model.request.RequestBilling;
import com.app.utils.PageUtils;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillingSvcImpl implements BillingSvc {

    private static final Logger logger = LogManager.getLogger(BillingSvcImpl.class);

    private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private VegaCustomResourceLoader customResourceLoader;
    @Autowired
    private VegaServices services;

    @Override
    public String getBilling(RequestBilling requestBilling, HttpServletRequest request) {
        Date start = new Date();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = new Gson();
        gson = builder.create();
        String req = gson.toJson(requestBilling);
        String res = null;
        Boolean error = true;
        String message = null;
        String resultErr = null;
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();

        String username = requestBilling.getUsername();
        String key = requestBilling.getKey();
        String no_polis = requestBilling.getNo_polis();
        Integer pageNumber = requestBilling.getPageNumber();
        Integer pageSize = requestBilling.getPageSize();
        String startDate = requestBilling.getStartDate();
        String endDate = requestBilling.getEndDate();
        String status = requestBilling.getStatus();


        try {
            if (customResourceLoader.validateCredential(username, key)) {
                Pemegang pemegang = new Pemegang();
                pemegang.setMspo_policy_no(no_polis);
                pemegang = services.selectPemegang(pemegang);
                if (pemegang != null) {
                    List<Billing> lisBill = services.selectBilling(pemegang.getReg_spaj(), pageNumber, pageSize, startDate, endDate);
                    List<Object> lisPay = new ArrayList<>();
                    if (lisBill != null) {
                        lisBill = PageUtils.getPage(lisBill, Optional.of(pageNumber).orElse(1), Optional.of(pageSize).orElse(20));
                        lisBill = lisBill.stream().filter(v -> v.getPaid().equals(status)).collect(Collectors.toList());
                        ListIterator<Billing> liter = lisBill.listIterator();
                        BigDecimal totalAmountTagihan = lisBill.stream()
                                .filter(v -> v.getFlag_jt_tempo() != null && v.getFlag_jt_tempo() == 1)
                                .filter(v -> v.getPaid().equals(Billing.BILLING_STATUS_OUTSTANDING))
                                .map(Billing::getTotal_premi).reduce(BigDecimal.ZERO, BigDecimal::add);
                        while (liter.hasNext()) {
                            try {
                                Billing m = liter.next();
                                HashMap<String, Object> pay = new HashMap<>();
                                pay.put("paid", mapStatus(m));
                                pay.put("lku_id", m.getLku_id() != null ? m.getLku_id() : null);
                                pay.put("kurs", m.getLku_symbol() != null ? m.getLku_symbol() : null);
                                pay.put("payment", m.getTotal_premi() != null ? m.getTotal_premi() : null);
                                pay.put("premi_ke", m.getPremi_ke() != null ? m.getPremi_ke() : null);
                                pay.put("tahun_ke", m.getTh_ke() != null ? m.getTh_ke() : null);
                                pay.put("paid_date", m.getTgl_bayar() != null ? df1.format(m.getTgl_bayar()) : null);
                                pay.put("period_date", m.getPeriode() != null ? df1.format(m.getPeriode()) : null);
                                pay.put("lku_symbol", m.getLku_symbol() != null ? m.getLku_symbol() : null);
                                pay.put("flag_jt_tempo", m.getFlag_jt_tempo() != null && m.getFlag_jt_tempo() == 1);

                                if (m.getPaid().equals("Paid")) {
                                    pay.put("status_billing_id", 1);
                                } else {
                                    pay.put("status_billing_id", 2);
                                }

                                lisPay.add(pay);
                            } catch (Exception e) {
                                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: "
                                        + e);
                            }
                        }

                        data.put("billing", lisPay);
                        data.put("totalResults", lisBill.size());
                        data.put("total_premi_tagihan", totalAmountTagihan);
                        error = false;
                        message = "Successfully get data billing";
                    } else {
                        error = false;
                        message = "Can't get data billing";
                        resultErr = "Data billing kosong";
                    }
                } else {
                    error = true;
                    message = "Can't get data billing";
                    resultErr = "Data pemegang kosong";
                    logger.error(
                            "Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
                }
            } else {
                error = true;
                message = "Can't get data billing";
                resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
                logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
            }
        } catch (Exception e) {
            error = true;
            message = ResponseMessage.ERROR_SYSTEM;
            resultErr = "bad exception " + e;
            logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);
        }
        map.put("error", error);
        map.put("message", message);
        map.put("data", data);
        res = gson.toJson(map);
        // Update activity user table LST_USER_SIMULTANEOUS
        customResourceLoader.updateActivity(username);
        // Insert Log LST_HIST_ACTIVITY_WS
        customResourceLoader.insertHistActivityWS(12, 11, new Date(), req, res, 1, resultErr, start, username);
        return res;
    }

    private String mapStatus(Billing billing) {
        String status;
        switch (billing.getPaid()) {
            case Billing.BILLING_STATUS_PAID:
                status = Billing.BILLING_STATUS_PAID;
                break;
            case Billing.BILLING_STATUS_OUTSTANDING:
                if (billing.getFlag_jt_tempo() != null && billing.getFlag_jt_tempo() == 1) {
                    status = Billing.BILLING_STATUS_OUTSTANDING;
                } else {
                    status = Billing.BILLING_STATUS_ACTIVE_BILLING;
                }
                break;
            default:
                status = billing.getPaid() != null ? billing.getPaid() : "";
        }
        return status;
    }
}
