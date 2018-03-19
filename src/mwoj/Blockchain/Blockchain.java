package mwoj.Blockchain;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Blockchain {

    private ArrayList<Block> blocks;

    public Blockchain() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        blocks = new ArrayList<>();
        String hash = Hasher.getHash("startBlock");
        Block startBlock = new Block(0, hash, "", "Data", new Timestamp(System.currentTimeMillis()).toString() );
        blocks.add(startBlock);
    }

    public Block getLastBlock()
    {
        return blocks.get(blocks.size()-1);
    }

    public Block generateNewBlock(String dataForNewBlock) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Block lastBlock = getLastBlock();
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
