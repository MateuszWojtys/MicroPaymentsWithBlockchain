package mwoj.Common;

import mwoj.TransactionsProc.Block;
import mwoj.TransactionsProc.Blockchain;
import mwoj.Crypto.Hasher;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class Tests {

    public void createTransaction_TEST() throws NoSuchAlgorithmException {
        String textToHash = "Test";
        String hash = Hasher.getHashFromString(textToHash);
        Logger.log("Tests", "Created hash from: " + textToHash + " is: " + hash);
    }


    public void blockSignatures_TEST() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        KeyPair testKeys = Hasher.generateKeyPair();
        PrivateKey privateKey = testKeys.getPrivate();
        PublicKey publicKey = testKeys.getPublic();

        Block block = new Block(1, "hash1", "hash2", "data",new Date().toString());
        Blockchain blockchain = new Blockchain();

        byte[] signature = blockchain.generateBlockSignature(privateKey, block);
        boolean result = blockchain.verifyBlockSignature(publicKey, block, signature);
        Logger.log("Tests", "Result of verifying signature: " + signature + " is: " + result);
    }
}
