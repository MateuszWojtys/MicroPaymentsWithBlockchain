package mwoj.Blockchain;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;


public class Hasher{


    public static String getHashFromString(String data) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));
        String hashedData = Base64.getEncoder().encodeToString(hash);
        return hashedData;
    }

    public static byte[] createSignature(PrivateKey privateKey, String input) {
        Signature rsa;
        byte[] output;
        try {
            rsa = Signature.getInstance("SHA256withRSA");
            rsa.initSign(privateKey);
            byte[] inputStringAsBytes = input.getBytes();
            rsa.update(inputStringAsBytes);
            byte[] realSig = rsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature rsa = Signature.getInstance("SHA256withRSA");
            rsa.initVerify(publicKey);
            rsa.update(data.getBytes());
            return rsa.verify(signature);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}
