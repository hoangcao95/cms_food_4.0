package vn.vano.cms.common;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.text.NumberFormat;
import java.util.Locale;

public class GenPassEncrypt {
    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("jasypt");
        String username = encryptor.encrypt("root");
        String password = encryptor.encrypt("123456");
        System.out.println("User: " + username );
        System.out.println("Pass: " + password );

//        System.out.println("User: " + encryptor.decrypt("AKIAI34FFX6JWDVRLBTA") );
//        System.out.println("Pass: " + encryptor.decrypt("ArkLcjEeNLS1OAmSn3ToBQU+uGie84hF/InqfcfjhjbC"));

        System.out.println(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(Long.parseLong("500000")));
    }

}
