/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author FITIA ARIVONY
 */
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SHA1 {
    String message;
    String sha1;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSha1() throws Exception {
        this.sha1();
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }
    public SHA1(String message) throws Exception{
        this.setMessage(message);
        this.sha1();
    }
    public void sha1()throws Exception{
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
     digest.reset();
     digest.update(this.getMessage().getBytes("utf8"));
    this.setSha1(String.format("%040x", new BigInteger(1, digest.digest())));
    }
    
//	public static void main(String[] argv){
//        try {
//            SHA1 sha=new SHA1("this is a test");
//            System.out.println( "The sha1 of \""+ sha.getMessage() + "\" is:");
//            System.out.println(sha.getSha1());
//            System.out.println();
//        } catch (Exception ex) {
//            Logger.getLogger(SHA1.class.getName()).log(Level.SEVERE, null, ex);
//        }	
//
//     }
}
