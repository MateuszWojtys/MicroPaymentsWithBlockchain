package mwoj;

import mwoj.Blockchain.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Main {

	public static Wallet walletA;
	public static Wallet walletB;
	public static  Wallet coinbase;

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		Blockchain blockchain = new Blockchain();

		walletA = new Wallet();
		walletB = new Wallet();
		coinbase = new Wallet();

		Transaction genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), 100, new ArrayList<TransactionInput>());
		genesisTransaction.generateSignature(coinbase.getPrivateKey());	 //manually createSignature the genesis transaction
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.receiver, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		blockchain.allUnspentTransactions.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the myUnspentTransactions list.

		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = blockchain.generateNewBlock("GenesisBlock", 2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		genesis.addTransaction(genesisTransaction);
		blockchain.addNewBlock(genesis);

		System.out.println("\nWalletA's balance is: " + walletA.getBalance());

		Block block1 = blockchain.generateNewBlock("Block1", 2);

		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.createTransaction(walletB.getPublicKey(), 40f));
		blockchain.addNewBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		Block block2 = blockchain.generateNewBlock("Block2", 2);
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.createTransaction(walletB.getPublicKey(), 1000));
		blockchain.addNewBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());


		Block block3 = blockchain.generateNewBlock("Block3", 2);
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.createTransaction( walletA.getPublicKey(), 20));
		blockchain.addNewBlock(block3);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		blockchain.validateBlockchain(genesisTransaction);


    }
}
