/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dsocialserver.Utils;

import com.example.dsocialserver.Controllers.UserController;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author haidu
 */
public class MD5 {
    public static String MD5(String password){
        String hashPassword = "";
        try {
                MessageDigest msd = MessageDigest.getInstance("MD5");
                byte[] srcTextBytes = password.getBytes("UTF-8");
                byte[] enrTextBytes = msd.digest(srcTextBytes);

                BigInteger bigInt = new BigInteger(1, enrTextBytes);
                hashPassword = bigInt.toString(16);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                StatusUntilIndex.showInternal(ex);
            }
        return hashPassword;
    }
}
