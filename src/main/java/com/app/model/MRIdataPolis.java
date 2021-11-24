package com.app.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MRIdataPolis implements Serializable {

    private static final long serialVersionUID = -5651293893834037906L;

    private String nama_pemegang_polis;
    private String no_polis;
    private String peserta;
    private BigDecimal premi_dasar;
    private BigDecimal premi_extra;
    private BigDecimal total_premi;
    private String masa_berlaku_polis;
    private String mata_uang;
    private String cara_pembayaran;
    private String esert_baru;
    private String esert_lama;
    private String no_mou;

}
