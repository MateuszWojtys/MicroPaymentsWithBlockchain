package mwoj.Blockchain;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class Hasher{

    private static MessageDigest messageDigest;


    public static String getHash(String data) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));
        String hashedData = Base64.getEncoder().encodeToString(hash);
        return hashedData;
    }


}
