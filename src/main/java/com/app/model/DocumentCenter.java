package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCenter implements Serializable {

    private static final long serialVersionUID = -4989569780383990573L;

    public String username;
    public String key;

    public List<DocumentCenterDetail> data;
}
