/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.until;

import com.example.dsocialserver.Models.CustomResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 *
 * @author haidu
 */
public class ParseJSon {
    public static String ParseJSon(Map<String, Object> responseData){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Chuyển Map thành JSON và trả về chuỗi JSON
            return objectMapper.writeValueAsString(responseData);
        } catch (JsonProcessingException e) {
            return "Error occurred while converting to JSON";
        }
    }
    public static String ParseJSonCustomResponse(CustomResponse responseData){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Chuyển Map thành JSON và trả về chuỗi JSON
            return objectMapper.writeValueAsString(responseData);
        } catch (JsonProcessingException e) {
            return "Error occurred while converting to JSON";
        }
    }
}
