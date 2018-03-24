package mwoj;

import mwoj.Blockchain.Blockchain;
import mwoj.Blockchain.WalletClient;
import mwoj.CoinDistributor.CoinDistributor;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Main {


    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		CoinDistributor coinDistributor = new CoinDistributor();

		Blockchain blockchain = new Blockchain();

		WalletClient testWallet = new WalletClient("TestWallet");
		blockchain.feedWallet(testWallet, 55);
		System.out.println(testWallet.getName() + ": " + testWallet.getBalance());


		WalletClient receiver = new WalletClient("receiver");
		blockchain.feedWallet(receiver, 20);
		System.out.println(receiver.getName() + ": " + receiver.getBalance());

		blockchain.validateBlockchain();
    }
}
