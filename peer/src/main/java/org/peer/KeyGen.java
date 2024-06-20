package org.peer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class KeyGen {

    public static String keysGenerator(String filename) {
        try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try{    
            byte[] fileBytes = Files.readAllBytes(Paths.get(filename));
            md.update(fileBytes);   
         } catch(IOException e){
            //e.printStackTrace();
        } 
            byte[] digest = md.digest();
            StringBuilder myChecksum = new StringBuilder();
            for(byte b : digest){
                myChecksum.append(String.format("%02x", b));
            }
            return myChecksum.toString();
        } catch(NoSuchAlgorithmException aE){
            aE.printStackTrace();
        }
        return null;

    }

}
