package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolisMri implements Serializable {

    private static final long serialVersionUID = 7960358866941069722L;

    private String mspo_policy_no_format;
    private String nm_pp;
    private String nm_tt;
    private String status;
    private String username;
    private String key;
    private String type_individu;

    private String pathEsertBaru;
    private String pathEsertLama;
    private String essert;
    private String policy_no;
}
