package mwoj;

public class Main {


    public static void main(String[] args) throws Exception {

		new Thread(new Server()).start();

		Client client = new Client();
		client.downloadPDF();

    	/*
		Blockchain blockchain = new Blockchain();
		CoinDistributor coinDistributor = new CoinDistributor();

		WalletClient testWallet = new WalletClient("TestWallet");

		ArrayList<Coin> coins = coinDistributor.createCoins(55);

		//WalletClient testWallet = new WalletClient("TestWallet");
		blockchain.feedWallet(testWallet, 55, coins);
		System.out.println(testWallet.getName() + ": " + testWallet.getBalance());

		ArrayList<Coin> coins2 = coinDistributor.createCoins(20);
		WalletClient receiver = new WalletClient("receiver");
		blockchain.feedWallet(receiver, 20, coins2);
		System.out.println(receiver.getName() + ": " + receiver.getBalance());

		blockchain.validateBlockchain();
		*/
    }
}
