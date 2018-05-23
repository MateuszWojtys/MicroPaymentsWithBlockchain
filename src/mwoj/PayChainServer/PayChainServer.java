package mwoj.PayChainServer;

import mwoj.Common.Converter;
import mwoj.Common.Logger;
import mwoj.TransactionsProc.Block;
import mwoj.TransactionsProc.Blockchain;
import mwoj.TransactionsProc.Transaction;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PayChainServer implements Runnable {
    private final String myName = "PayChainServer|||";
    public int SOCKET_PORT = 0;
    Blockchain blockchain;
    public PayChainServer(int socketPort) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SOCKET_PORT = socketPort;
        blockchain = new Blockchain();
    }

    public void startServer() throws IOException {

        OutputStream os = null;
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {

            serverSocket = new ServerSocket(SOCKET_PORT);
            while (true) {
                Logger.log(myName,"Waiting for client...");
                try {
                    clientSocket = serverSocket.accept();
                    Logger.log(myName,"Accepted connection : " + clientSocket);

                    DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                    String header = dis.readUTF();
                    boolean readData = dis.readBoolean();
                    Logger.log(myName,"Request received: " + header + "readData=" + readData);
                    DataOutputStream dos = null;
                    String data = null;
                    JSONObject jsonData;
                    switch (header)
                    {
                        case "FeedWallet":

                            if(readData)
                            {
                                data = dis.readUTF();
                                Logger.log(myName,"Data from message: " + data);
                            }
                            jsonData = new JSONObject(data);
                            Block block = Converter.getBlockFromJSON(jsonData);
                            JSONObject deal = jsonData.getJSONObject("transaction");
                            Transaction transaction = Converter.getTransactionFromJSON(deal);
                            block.addTransaction(blockchain.transactionProcessor,transaction);
                            blockchain.addNewBlock(block);
                            dos = new DataOutputStream(clientSocket.getOutputStream());
                            dos.writeUTF("GenesisBlockAdded");
                            dos.writeBoolean(false);
                            break;

                        case "NewTransaction":
                            if(readData)
                            {
                                data = dis.readUTF();
                                Logger.log(myName,"Data from message: " + data);
                            }
                            jsonData = new JSONObject(data);
                            Block block2 = Converter.getBlockFromJSON(jsonData);
                            JSONObject deal2 = jsonData.getJSONObject("transaction");
                            Transaction transaction2 = Converter.getTransactionFromJSON(deal2);
                            block2.addTransaction(blockchain.transactionProcessor,transaction2);
                            blockchain.addNewBlock(block2);
                            if(blockchain.validateBlockchain())
                            {
                                dos = new DataOutputStream(clientSocket.getOutputStream());
                                dos.writeUTF("BlockAdded");
                                dos.writeBoolean(false);
                            }
                            else
                            {
                                blockchain.removeLastBlock();
                                dos = new DataOutputStream(clientSocket.getOutputStream());
                                dos.writeUTF("FakeBlock");
                                dos.writeBoolean(false);
                            }

                            break;
                        default:

                            if(readData)
                            {
                                 data = dis.readUTF();
                            }

                            dos = new DataOutputStream(clientSocket.getOutputStream());
                            dos.writeUTF("Wrong header");
                            dos.writeBoolean(false);
                            break;
                    }

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } finally {

                    if (os != null) os.close();
                    if (clientSocket!=null) clientSocket.close();
                }
            }
        }
        finally {
            if (serverSocket != null) serverSocket.close();
        }
    }



    public void run() {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
