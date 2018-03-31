package mwoj.Blockchain;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.ArrayList;

public class Transaction {
    public String transactionId;
    public PublicKey sender;
    public PublicKey receiver;
    public float value;
    public byte[] signature;
    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; // a rough count of how many transactions have been generated.

    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.receiver = to;
        this.value = value;
        this.inputs = inputs;
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = Hasher.getStringFromKey(sender) + Hasher.getStringFromKey(receiver) + Float.toString(value)	;
        signature = Hasher.createSignatureWithPrivate(privateKey, data);
    }

    public boolean verifySignature() {
        String data = Hasher.getStringFromKey(sender) + Hasher.getStringFromKey(receiver) + Float.toString(value)	;
        return Hasher.verifySignature(sender, data, signature);
    }


    private String calulateHash() throws  NoSuchAlgorithmException {
        sequence++;
        return Hasher.getHashFromString(Hasher.getStringFromKey(sender) + Hasher.getStringFromKey(receiver) + Float.toString(value) + sequence);
    }

    public boolean processTransaction() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        if(verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }


        for(TransactionInput i : inputs) {
            i.UTXO = Blockchain.allUnspentTransactions.get(i.transactionOutputId);
        }

        float leftOver = getInputsValue() - value;
        transactionId = calulateHash();
        outputs.add(new TransactionOutput( this.receiver, value,transactionId));
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId));

        for(TransactionOutput o : outputs) {
            Blockchain.allUnspentTransactions.put(o.id , o);
        }

        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue;
            Blockchain.allUnspentTransactions.remove(i.UTXO.id);
        }
        return true;
    }

    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return total;
    }

    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

}
