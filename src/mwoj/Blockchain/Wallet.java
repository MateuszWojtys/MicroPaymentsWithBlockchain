package mwoj.Blockchain;

import mwoj.CoinDistributor.Coin;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    public HashMap<String,TransactionOutput> myUnspentTransactions;
    private String name;
    public ArrayList<Coin> myCoins;

    public Wallet(String _name){
        myCoins = new ArrayList<>();
        myUnspentTransactions = new HashMap<String,TransactionOutput>();
        generateKeyPair();
        name = _name;
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: Blockchain.allUnspentTransactions.entrySet()){
            TransactionOutput unspentTransaction = item.getValue();
            if(unspentTransaction.isMine(publicKey)) {
                myUnspentTransactions.put(unspentTransaction.id,unspentTransaction);
                total = total + unspentTransaction.value ;
            }
        }
        return total;
    }


    public Transaction createTransaction(PublicKey receiverPublicKey, float value ) {
        if(getName() != "Coinbase" && getName() != "Genesis")
        {
            if(getBalance() < value)
            {
                System.out.println("Not Enough funds to send transaction. Transaction Discarded.");
                return null;
            }
        }


        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: myUnspentTransactions.entrySet()){
            TransactionOutput unspentTransaction = item.getValue();
            total = total + unspentTransaction.value;
            inputs.add(new TransactionInput(unspentTransaction.id));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, receiverPublicKey , value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs){
            myUnspentTransactions.remove(input.transactionOutputId);
        }
        return newTransaction;
    }

    public String getName() {
        return name;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
