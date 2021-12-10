package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DPLKAccountModel implements Serializable {

    private static final long serialVersionUID = 924890237728378410L;

    private String username;
    private String password;
    private String key;
    private String acc_no;
    private Date mspe_date_birth;
    private String dob;
    private String no_hp;
    private String jenis_dplk;
    private String nama_perusahaan;
    private String no_peserta;
    private Date tgl_mulai_dplk;
    private String periode_pembayaran;
    private String lji_invest;
    private String usia_pensiun;
    private String nama_peserta;
    private String alamat;
    private String no_id;

    private String start_date;
    private String end_date;
    private Integer lji_id;

}
