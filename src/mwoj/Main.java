package mwoj;

import mwoj.Blockchain.Blockchain;
import mwoj.Blockchain.Wallet;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Main {

	public static Wallet walletA;
	public static Wallet walletB;
	public static  Wallet coinbase;

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		Blockchain blockchain = new Blockchain();

		Wallet testWallet = new Wallet("TestWallet");
		blockchain.feedWallet(testWallet, 55);
		System.out.println(testWallet.getName() + ": " + testWallet.getBalance());
		blockchain.validateBlockchain();

    }
}
