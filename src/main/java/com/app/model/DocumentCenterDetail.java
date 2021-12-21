package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCenterDetail implements Serializable {

    private static final long serialVersionUID = 4264838155607025203L;

    public String nama_folder;
    public String url;
}
