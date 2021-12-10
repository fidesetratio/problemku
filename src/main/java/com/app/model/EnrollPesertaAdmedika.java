package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollPesertaAdmedika implements Serializable {

    private static final long serialVersionUID = 357806472847726991L;

    private String policy_number;
    private String no_kartu;
    private String mcl_id;
    private String participant_name;
    private String company_name;
    private Date dob;
    private String membertype;
    private String mspe_email;
}
