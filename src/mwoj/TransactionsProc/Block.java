package mwoj.TransactionsProc;

import mwoj.Crypto.Hasher;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Block{

    private int index;
    private String hash;
    private String previousBlockHash;
    private String data;
    private String timestamp;

    public Transaction transaction;

    public Block(int _index, String _hash, String _previousBlockHash, String _data, String _timestamp){
        this.index = _index;
        this.hash = _hash;
        this.previousBlockHash = _previousBlockHash;
        this.data = _data;
        this.timestamp = _timestamp;
    }


    public Transaction getTransaction()
    {
        return transaction;
    }
    public String calculateHash() throws  NoSuchAlgorithmException {
        String text = previousBlockHash + data + timestamp + index;
        //System.out.println("MergedText: " + text);
        String hash = Hasher.getHashFromString(text);
        //System.out.println("Hash: " + hash);
        return hash;
    }

    //Add transactions to this block
    public boolean addTransaction(TransactionProcessor proc, Transaction tmp) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        if(tmp == null) return false;
        if((previousBlockHash != "")) {
            if((proc.processTransaction(tmp) != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transaction = tmp;
        System.out.println("Transaction Successfully added to Block");
        return true;
    }


    public String getHash() {
        return hash;
    }

    public int getIndex() {
        return index;
    }

    public String getData() {
        return data;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getTimestamp() {
        return timestamp;
    }
}