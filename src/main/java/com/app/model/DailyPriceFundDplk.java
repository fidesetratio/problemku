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
public class DailyPriceFundDplk implements Serializable {

    private static final long serialVersionUID = 7840389518166357861L;

    private Integer lji_id;
    private String lji_invest;
    private String lku_id;
    private Date lnu_tgl;
    private BigDecimal lnu_nilai;
    private Date lnu_tgl_sebelum;
    private BigDecimal nilai_sebelum;
    private BigDecimal selisih_nilai;
    private BigDecimal persen_selisih;
}
