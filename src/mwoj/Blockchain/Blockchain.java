package mwoj.Blockchain;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain {

    private ArrayList<Block> blocks;
    public static HashMap<String,TransactionOutput> allUnspentTransactions;
    public static int difficulty;
    WalletClient genesisWallet;
    Transaction genesisTransaction;

    public Blockchain() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        blocks = new ArrayList<>();
        genesisWallet = new WalletClient("Genesis");
        allUnspentTransactions = new HashMap<String,TransactionOutput>();
        difficulty = 2;
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

    public void feedWallet(Wallet toFeed, int value) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Block tmp = generateNewBlock("Block", difficulty);
        System.out.println("Feeding wallet " + toFeed.getName() + " with value " + value);
        tmp.addTransaction(genesisWallet.createTransaction(toFeed.getPublicKey(), value));
        addNewBlock(tmp);
    }

    private void createGenesisTransaction() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        genesisTransaction = new Transaction(genesisWallet.getPublicKey(), genesisWallet.getPublicKey(), 0, new ArrayList<TransactionInput>());
        genesisTransaction.generateSignature(genesisWallet.getPrivateKey());
        genesisTransaction.transactionId = "0";
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.receiver, genesisTransaction.value, genesisTransaction.transactionId));
        allUnspentTransactions.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = generateNewBlock("GenesisBlock", Blockchain.difficulty);
        genesis.addTransaction(genesisTransaction);
        addNewBlock(genesis);
    }

    public  Boolean validateBlockchain() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUnspentTransactions = new HashMap<String,TransactionOutput>();
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.receiver, genesisTransaction.value, genesisTransaction.transactionId));
        tempUnspentTransactions.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));


        for(int i=1; i < blocks.size(); i++) {

            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i-1);

            if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

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


            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempUnspentTransactions.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUnspentTransactions.remove(input.transactionOutputId);
                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUnspentTransactions.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).receiver != currentTransaction.receiver) {
                    System.out.println("#Transaction(" + t + ") output receiver is not who it should be");
                    return false;
                }
                if( currentTransaction.outputs.get(1).receiver != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public Block generateNewBlock(String dataForNewBlock, int difficulty) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        int index;
        String hash;
        String timestamp;
        String lastBlockHash;
        int proofOfWork = 0;

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

        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        hash = calculateBlockHash(lastBlockHash, dataForNewBlock,timestamp, index, proofOfWork);
        while(!hash.substring( 0, difficulty).equals(target)) {
            proofOfWork ++;
            hash = calculateBlockHash(lastBlockHash, dataForNewBlock,timestamp, index, proofOfWork);
        }

        System.out.println("Block Mined!!! : " + hash + " with PoW: " + proofOfWork + " " + dataForNewBlock);

        return new Block(index, hash, lastBlockHash, dataForNewBlock, timestamp, proofOfWork);
    }


    public String calculateBlockHash(String previousHash, String data, String timestamp, int index, int proofOfWork ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String text = previousHash + data + timestamp + index + proofOfWork;
        //System.out.println("MergedText: " + text);
        String hash = Hasher.getHashFromString(text);
        //System.out.println("Hash: " + hash);
        return hash;
    }

    public String calculateBlockHashFromBlock(Block block) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String text = block.getPreviousBlockHash() + block.getData() + block.getTimestamp() + block.getIndex() + block.getProofOfWork();
        //System.out.println("MergedText from Block: " + text);
        String hash = Hasher.getHashFromString(text);
        //System.out.println("Hash from Block: " + hash);
        return hash;
    }
}
