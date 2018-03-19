package mwoj.Blockchain;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

public class Blockchain {


    public Block generateNewBlock(Block lastBlock, String dataForNewBlock) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        int index = lastBlock.getIndex() + 1;
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        String hash = calculateBlockHash(lastBlock.getHash(), dataForNewBlock,timestamp, index);
        return new Block(index, hash, lastBlock.getHash(), dataForNewBlock, timestamp);
    }

    public String calculateBlockHash(String previousHash, String data, String timestamp, int index ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String text = previousHash + data + timestamp + index;
        System.out.println("MergedText: " + text);
        String hash = Hasher.getHash(text);
        System.out.println("Hash: " + hash);
        return hash;
    }
}
