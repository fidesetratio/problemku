package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JenisInvestDplk implements Serializable {

    private static final long serialVersionUID = 7687298146916950134L;

    private Integer lji_id;
    private String lji_invest;
    private Integer lku_id;
    private Integer active;
}
