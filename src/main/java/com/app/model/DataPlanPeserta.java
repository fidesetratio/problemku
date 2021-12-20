package com.app.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPlanPeserta implements Serializable {

    private static final long serialVersionUID = 4314602798573398578L;

    private String nama_plan;

}
