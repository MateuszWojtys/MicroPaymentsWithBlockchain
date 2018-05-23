package mwoj;


import mwoj.Common.Converter;
import mwoj.PayChainClient.PayChainClient;
import mwoj.PayChainServer.PayChainServer;
import mwoj.TransactionsProc.Block;
import mwoj.TransactionsProc.Blockchain;
import mwoj.Wallet.Wallet;
import org.json.JSONObject;

public class Main {

    private final static String myName = "Main|||";
    public static void main(String[] args) throws Exception {



		Blockchain blockchain = new Blockchain();
        new Thread(new PayChainServer(15000)).start();
        PayChainClient client = new PayChainClient("127.0.0.1", 15000);

		Wallet testWallet = new Wallet("TestWallet");
		Block tmp = blockchain.feedWallet(testWallet, 15);
		JSONObject obj = Converter.getJSONFromBlock(tmp);
		client.sendMessageWithData("NewTransaction", obj);

		System.out.println(testWallet.getName() + ": " + testWallet.getBalance());


		Wallet receiver = new Wallet("receiver");
		Block tmp2 = blockchain.feedWallet(receiver, 20);
		JSONObject obj2 = Converter.getJSONFromBlock(tmp2);
		client.sendMessageWithData("NewTransaction", obj2);
		System.out.println(receiver.getName() + ": " + receiver.getBalance());


		/*Transaction deal = new Transaction(testWallet.getPublicKey(), receiver.getPublicKey(), 10);
		JSONObject tmp = new JSONObject();
		tmp.put("TMP", "tmp");
		Block block = blockchain.generateNewBlock(tmp);
		block.addTransaction(blockchain.transactionProcessor,deal);
		System.out.println(testWallet.getName() + ": " + testWallet.getBalance());
		System.out.println(receiver.getName() + ": " + receiver.getBalance());
		boolean test = blockchain.validateBlockchain();
		Logger.log("Main", "Wynik "+test);*/
    }
}
