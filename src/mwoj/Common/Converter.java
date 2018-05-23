package mwoj.Common;

import mwoj.TransactionsProc.Block;
import mwoj.TransactionsProc.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Converter {

    public static byte[] getBytesFromJSONObject(JSONObject json)
    {
        return json.toString().getBytes();
    }

    public static JSONObject getJSONObjectFromBytes(byte[] data)
    {
        String jsonString = new String(data);
        JSONObject json = new JSONObject(jsonString);
        return json;
    }

    public static JSONObject getJSONFromTransaction(Transaction transaction)
    {
        JSONObject object = new JSONObject();
        object.put("id", transaction.transactionId);
        object.put("value", transaction.value);
        object.put("sender",getJSONFromPublicKey(transaction.sender));
        object.put("receiver", getJSONFromPublicKey(transaction.receiver));
        return object;
    }
    public static JSONObject getJSONFromBlock(Block block)
    {
        JSONObject object = new JSONObject();
        object.put("index",block.getIndex());
        object.put("prevBlockHash", block.getPreviousBlockHash());
        object.put("data", block.getData());
        object.put("hash", block.getHash());
        object.put("timestamp", block.getTimestamp());
        object.put("transaction", getJSONFromTransaction(block.getTransaction()));
        return object;
    }

    public static Block getBlockFromJSON(JSONObject object)
    {

        Block block = new Block(object.getInt("index"),object.getString("hash"), object.getString("prevBlockHash"),
                object.getString("data"), object.getString("timestamp"));
        //block.transaction = getTransactionFromJSON(new JSONObject(object.getString("transaction")));
        return block;
    }

    public static Transaction getTransactionFromJSON(JSONObject object) throws InvalidKeySpecException, NoSuchAlgorithmException {

        JSONObject jsonSender = object.getJSONObject("sender");
        JSONObject jsonReceiver = object.getJSONObject("receiver");
        PublicKey sender = getPublicKeyFromJSON(jsonSender);
        PublicKey receiver = getPublicKeyFromJSON(jsonReceiver);
        Transaction transaction = new Transaction(sender, receiver, object.getInt("value"));
        transaction.transactionId = object.getString("id");

        return transaction;
    }

    public static JSONObject getJSONFromPublicKey(PublicKey publicKey)
    {
        byte[] key = publicKey.getEncoded();
        JSONArray array = new JSONArray(key);
        JSONObject object = new JSONObject();
        String tmp = Base64.getEncoder().encodeToString(key);
        object.put("key", tmp);
        return object;
    }

    public static PublicKey getPublicKeyFromJSON(JSONObject object) throws NoSuchAlgorithmException, InvalidKeySpecException {

        //JSONArray array = object.getJSONArray("key");

        String tmp = object.getString("key");
        byte[] byteKey = Base64.getDecoder().decode(tmp);
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");


        return kf.generatePublic(X509publicKey);
    }
}
