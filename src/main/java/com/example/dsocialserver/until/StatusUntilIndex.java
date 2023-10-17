/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.until;

import com.example.dsocialserver.Models.CustomResponse;
import static com.example.dsocialserver.until.ParseJSon.ParseJSon;
import static com.example.dsocialserver.until.ParseJSon.ParseJSonCustomResponse;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author haidu
 */
public class StatusUntilIndex {
    private static final CustomResponse jsonRes= new CustomResponse();
    //500
    public static ResponseEntity showInternal( Exception e){
        System.out.println(e);
        jsonRes.setRes(false, "Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ParseJSonCustomResponse(jsonRes));
    }
    //401
    public static ResponseEntity showNotAuthorized(){
        jsonRes.setRes(false, "Not authorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ParseJSonCustomResponse(jsonRes));
    }
    //400
    public static ResponseEntity showMissing(){
        jsonRes.setRes(false, "Missing field");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSonCustomResponse(jsonRes));
    }
    //404
    public static ResponseEntity showNotFound(){
        jsonRes.setRes(false,"Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ParseJSonCustomResponse(jsonRes));
    }
}
