package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdMedikaRequest implements Serializable {

    private static final long serialVersionUID = 733149413827394872L;

    private String card_no;
    private String profile_name;
    private String dob;
    private String member_type;
    private String email;
    private String signature;
    private String project_id;
    private String app_id;
    private Timestamp timestamp;
    private Boolean using_idcard;
    private Boolean using_selfie;
    private boolean using_pin = false;

    private String username;
    private String key;

}
