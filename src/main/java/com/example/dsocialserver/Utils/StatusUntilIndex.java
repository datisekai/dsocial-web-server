/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Utils;

import static com.example.dsocialserver.Utils.ParseJSon.ParseJSon;
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ParseJSon(jsonRes));
    }
    //401
    public static ResponseEntity showNotAuthorized(){
        jsonRes.setRes(false, "Not authorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ParseJSon(jsonRes));
    }
    //400
    public static ResponseEntity showMissing(){
        jsonRes.setRes(false, "Missing field");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ParseJSon(jsonRes));
    }
    //404
    public static ResponseEntity showNotFound(){
        jsonRes.setRes(false,"Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ParseJSon(jsonRes));
    }
}
