package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryPayment implements Serializable {

    private static final long serialVersionUID = -7409233166985150541L;

    private String mspa_trx;
    private String mpt_id;
    private String spaj_etc;
    private BigDecimal amount;
    private String status;
    private String payment_method;
    private String payment_channel;
    private Date paid_at;
    private Integer lt_id;
    private String currency;
    private BigDecimal paid_amount;
    private String objc_id;
    private String description_request;
    private String rekening;
    private String bank_name;
    private Integer lsjb_id;
    private String funds_topup;
    private Integer jenis_id_app;

    private String id_payment;
    private String path;
    private String username;
    private String key;
}
