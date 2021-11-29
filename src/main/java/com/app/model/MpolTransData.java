package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MpolTransData implements Serializable {

    private static final long serialVersionUID = -1418913770545014664L;

    private String mpt_id;
    private String req_date;
    private String reg_spaj;
    private Integer lt_id;
    private String lku_id;
    private BigDecimal mpt_jumlah;
    private Integer mpt_unit;
    private String mpt_status;
    private String created_date;
    private String modified_date;
    private Integer lus_id;
    private String payor_name;
    private String payor_occupation;
    private String payor_income;
    private String payor_source_income;
    private Float mpt_persen;
    private String path_bsb;
    private String reason_fu;
    private BigDecimal mpt_jumlah_process;
    private Integer unique_code;
    private Integer lsjb_id;
    private String rekening;
    private String bank_name;
    private Integer flag_source;

}
