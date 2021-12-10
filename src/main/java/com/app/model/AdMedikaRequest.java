package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdMedikaRequest implements Serializable {

    private static final long serialVersionUID = 733149413827394872L;

    private String card_no;
    private String nama_peserta;
    private String dob;
    private String member_type;
    private String username;
    private String key;

    private String project_id;
    private Boolean using_idcard;
    private Boolean using_selfie;
    private boolean using_pin = false;
    private String signature;
}
