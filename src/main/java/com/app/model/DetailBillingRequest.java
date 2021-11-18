package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailBillingRequest implements Serializable {

    private static final long serialVersionUID = 6172109693616907233L;

    private String mpt_id;
    private String reg_spaj;
    private Integer tahun_ke;
    private Integer premi_ke;
    private BigDecimal amount;
    private Integer flag_bill;


}
