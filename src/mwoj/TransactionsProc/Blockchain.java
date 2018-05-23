package mwoj.TransactionsProc;


import mwoj.Crypto.Hasher;
import mwoj.Wallet.Wallet;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain {

    private ArrayList<Block> blocks;
    public static HashMap<String,TransactionOutput> allUnspentTransactions;
    public TransactionProcessor transactionProcessor;
    Wallet genesisWallet;
    Transaction genesisTransaction;

    public Blockchain() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        blocks = new ArrayList<>();
        genesisWallet = new Wallet("Genesis");
        allUnspentTransactions = new HashMap<String,TransactionOutput>();
        transactionProcessor = new TransactionProcessor();
        createGenesisTransaction();
    }

    public Block getLastBlock()
    {
        return blocks.get(blocks.size()-1);
    }

    public void addNewBlock(Block newBlock)
    {
        blocks.add(newBlock);
    }

    public Block feedWallet(Wallet toFeed, int value) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        JSONObject blockData = new JSONObject();
        blockData.put("Data", "Block");
        Block tmp = generateNewBlock(blockData);
        System.out.println("Feeding wallet " + toFeed.getName() + " with value " + value);
        tmp.addTransaction(transactionProcessor, genesisWallet.createTransaction(toFeed.getPublicKey(), value));
        addNewBlock(tmp);
        return tmp;
    }


    private void createGenesisTransaction() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        genesisTransaction = new Transaction(genesisWallet.getPublicKey(), genesisWallet.getPublicKey(), 0);

        genesisTransaction.transactionId = "0";
        TransactionOutput output = new TransactionOutput(genesisTransaction.receiver, genesisTransaction.value, genesisTransaction.transactionId);
        allUnspentTransactions.put(output.id, output);
        System.out.println("Creating and Mining Genesis block... ");
        JSONObject blockData = new JSONObject();
        blockData.put("Data", "Genesis");
        Block genesis = generateNewGenesisBlock(blockData);
        genesis.addTransaction(transactionProcessor,genesisTransaction);
        addNewBlock(genesis);
    }

    public void removeLastBlock()
    {
        blocks.remove(blocks.size()-1);
    }
    public  Boolean validateBlockchain() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Block currentBlock;
        Block previousBlock;

        for(int i=1; i < blocks.size(); i++) {

            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i-1);


            if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }

            if(!currentBlock.getPreviousBlockHash().equals(calculateBlockHashFromBlock(previousBlock)) ) {
                System.out.println("Chain is not valid! Currentblock: " + i );
                return false;
            }

            if(!previousBlock.getHash().equals(currentBlock.getPreviousBlockHash()) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }

        }
        System.out.println("Wallet is valid");
        return true;
    }

    public byte[] generateBlockSignature(PrivateKey privateKey, Block blockToSign) {
        JSONObject blockData = new JSONObject();
        blockData.put("Sender", blockToSign.transaction.sender);
        blockData.put("Receiver", blockToSign.transaction.receiver);
        blockData.put("Receiver", blockToSign.transaction.value);
        blockData.put("PreviousBlockHash", blockToSign.getPreviousBlockHash());
        blockData.put("Timestamp", blockToSign.getTimestamp());
        return Hasher.createSignature(privateKey, blockData);
    }

    public boolean verifyBlockSignature(PublicKey publicKey, Block block, byte[] blockSignature) {
        JSONObject blockData = new JSONObject();
        blockData.put("Sender", block.transaction.sender);
        blockData.put("Receiver", block.transaction.receiver);
        blockData.put("Receiver", block.transaction.value);
        blockData.put("PreviousBlockHash", block.getPreviousBlockHash());
        blockData.put("Timestamp", block.getTimestamp());
        return Hasher.verifySignature(publicKey, blockData, blockSignature);
    }

    public Block generateNewBlock(JSONObject dataForNewBlock) throws  NoSuchAlgorithmException {
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

        hash = calculateBlockHash(lastBlockHash, dataForNewBlock.toString(),timestamp, index);
        return new Block(index, hash, lastBlockHash, dataForNewBlock.toString(), timestamp);
    }

    public Block generateNewGenesisBlock(JSONObject dataForNewBlock) throws  NoSuchAlgorithmException {
        int index =0;
        String timestamp = "2018-01-01 00:00:00";
        String lastBlockHash ="";



        String hash = calculateBlockHash(lastBlockHash, dataForNewBlock.toString(),timestamp, index);
        return new Block(index, hash, lastBlockHash, dataForNewBlock.toString(), timestamp);
    }

    public String calculateBlockHash(String previousHash, String data, String timestamp, int index ) throws NoSuchAlgorithmException {
        String text = previousHash + data + timestamp + index;
        return Hasher.getHashFromString(text);
    }

    public String calculateBlockHashFromBlock(Block block) throws NoSuchAlgorithmException {
        String text = block.getPreviousBlockHash() + block.getData() + block.getTimestamp() + block.getIndex();
        return Hasher.getHashFromString(text);
    }
}
