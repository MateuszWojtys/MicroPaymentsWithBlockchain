package mwoj;

import mwoj.Blockchain.Blockchain;
import mwoj.Blockchain.Hasher;
import mwoj.Blockchain.WalletClient;
import mwoj.CoinDistributor.Coin;
import mwoj.CoinDistributor.CoinDistributor;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws Exception {

		WalletClient testWallet = new WalletClient("TestWallet");
    	String test = "TEst";
		String encrypted = Hasher.encrypt(test, testWallet.getPublicKey());
		System.out.println(encrypted);
		String decrypted = Hasher.decrypt(encrypted, testWallet.getPrivateKey());
		System.out.println(decrypted);


		CoinDistributor coinDistributor = new CoinDistributor();

		ArrayList<Coin> testCoins = coinDistributor.createCoins(100, testWallet.getPublicKey());
		Blockchain blockchain = new Blockchain();

		//WalletClient testWallet = new WalletClient("TestWallet");
		blockchain.feedWallet(testWallet, 55);
		System.out.println(testWallet.getName() + ": " + testWallet.getBalance());


		WalletClient receiver = new WalletClient("receiver");
		blockchain.feedWallet(receiver, 20);
		System.out.println(receiver.getName() + ": " + receiver.getBalance());

		blockchain.validateBlockchain();
    }
}
