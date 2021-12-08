package com.app.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class LstTransaksi implements Serializable {

    private static final long serialVersionUID = -4674455438786838581L;


    private Integer lt_id;
    private String lt_transksi;
    private String type_transaksi;

}
