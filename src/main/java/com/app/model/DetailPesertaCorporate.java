package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailPesertaCorporate implements Serializable {

    private static final long serialVersionUID = -1856557650036655344L;

    private String reg_spaj;
    private String no_polis;
    private Date mspo_end_date;
    private Date mspo_beg_date;
    private String no_hp;
    private String mcl_id_employee;
    private String mste_no_reg;
    private String mste_insured_no;
    private String mste_insured;
    private Date mspe_date_birth;
    private String name;
}
