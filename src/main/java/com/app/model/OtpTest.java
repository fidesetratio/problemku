package com.app.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpTest implements Serializable {

    private static final long serialVersionUID = -3229059642976148445L;

    private String username;
    private String OTP_NO;

}
