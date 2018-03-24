package mwoj.Blockchain;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Block{

    private int index;
    private String hash;
    private String previousBlockHash;
    private String data;
    private String timestamp;
    private int proofOfWork;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public Block(int _index, String _hash, String _previousBlockHash, String _data, String _timestamp, int _proofOfWork){
        this.index = _index;
        this.hash = _hash;
        this.previousBlockHash = _previousBlockHash;
        this.data = _data;
        this.timestamp = _timestamp;
        this.proofOfWork = _proofOfWork;
    }


    public String calculateHash() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String text = previousBlockHash + data + timestamp + index + proofOfWork;
        //System.out.println("MergedText: " + text);
        String hash = Hasher.getHashFromString(text);
        //System.out.println("Hash: " + hash);
        return hash;
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((previousBlockHash != "")) {
            if((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
    public int getProofOfWork() {
        return proofOfWork;
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