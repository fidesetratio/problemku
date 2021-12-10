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
public class DownloadAttachment implements Serializable {

    private static final long serialVersionUID = -1601426802317313972L;

    private String transaction_id;
    private String path;
    private String username;
    private String key;
}
