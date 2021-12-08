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
public class LstTransaksiDplk implements Serializable {

    private static final long serialVersionUID = 9003459211945063322L;

    private String invest;
    private Date tgl;
    private String transaksi;
    private BigDecimal peserta;
    private Integer pt;
    private BigDecimal nab;
    private BigDecimal unit_peserta;
    private BigDecimal unit_pt;
    private Integer lji_id;
    private String saldo_fund;
    private String dk;
    private BigDecimal awal1;
    private BigDecimal awal2;
    private Date tgl_nab;
    private Integer trx_ke;
    private Integer lt_id;
    private Integer ke;
    private BigDecimal akumulasi;

}
