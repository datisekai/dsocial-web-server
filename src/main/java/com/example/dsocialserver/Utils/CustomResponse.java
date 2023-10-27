/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Utils;

/**
 *
 * @author haidu
 */
public class CustomResponse {
    private boolean success;
    private String message;
    public CustomResponse() {
    }

    public CustomResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public void setRes(boolean success, String message){
        setSuccess(success);
        setMessage(message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
