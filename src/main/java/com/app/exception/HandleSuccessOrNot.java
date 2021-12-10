package com.app.exception;

import lombok.Data;

@Data
public class HandleSuccessOrNot {

    private boolean error;
    private String message;

    public HandleSuccessOrNot(boolean error, String message){
        this.error = error;
        this.message = message;
    }
}
