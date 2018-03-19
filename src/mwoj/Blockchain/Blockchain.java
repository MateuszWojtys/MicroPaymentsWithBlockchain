package mwoj.Blockchain;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Blockchain {

    private ArrayList<Block> blocks;

    public Blockchain() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        blocks = new ArrayList<>();

          blocks.add(generateNewBlock("GenesisBlock"));
    }

    public Block getLastBlock()
    {
        return blocks.get(blocks.size()-1);
    }

    public void addNewBlock(Block newBlock)
    {
        blocks.add(newBlock);
    }

    public Boolean isChainValid() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Block currentBlock;
        Block previousBlock;


        for(int i=1; i < blocks.size(); i++) {
            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i-1);

            if(!currentBlock.getPreviousBlockHash().equals(calculateBlockHashFromBlock(previousBlock)) ) {
                System.out.println("Chain is not valid! Currentblock: " + i );
                return false;
            }
        }

        return true;
    }
    public Block generateNewBlock(String dataForNewBlock) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        int index;
        String hash;
        String timestamp;
        String lastBlockHash;
        if(blocks.size() == 0)
        {
            index = 0;
            timestamp = new Timestamp(System.currentTimeMillis()).toString();
            lastBlockHash = "";
        }
        else
        {
            Block lastBlock = getLastBlock();
            index = lastBlock.getIndex() + 1;
            timestamp = new Timestamp(System.currentTimeMillis()).toString();
            lastBlockHash = lastBlock.getHash();
        }
        hash = calculateBlockHash(lastBlockHash, dataForNewBlock,timestamp, index);
        return new Block(index, hash, lastBlockHash, dataForNewBlock, timestamp);
    }

    public String calculateBlockHash(String previousHash, String data, String timestamp, int index ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String text = previousHash + data + timestamp + index;
        System.out.println("MergedText: " + text);
        String hash = Hasher.getHash(text);
        System.out.println("Hash: " + hash);
        return hash;
    }

    public String calculateBlockHashFromBlock(Block block) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String text = block.getPreviousBlockHash() + block.getData() + block.getTimestamp() + block.getIndex();
        System.out.println("MergedText from Block: " + text);
        String hash = Hasher.getHash(text);
        System.out.println("Hash from Block: " + hash);
        return hash;
    }
}
