package mwoj.PayChainServer;

import mwoj.Common.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PayChainServer implements Runnable {
    private final String myName = "PayChainServer|||";
    public int SOCKET_PORT = 0;

    public PayChainServer(int socketPort)
    {
        SOCKET_PORT = socketPort;

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

                    switch (header)
                    {


                        default:
                            String data = null;
                            if(readData)
                            {
                                 data = dis.readUTF();
                            }
                            Logger.log(myName,"Sending echo...");
                            DataOutputStream dos = null;
                            dos = new DataOutputStream(clientSocket.getOutputStream());
                            dos.writeUTF(header);
                            dos.writeBoolean(readData);
                            if(readData)
                            {
                                dos.writeUTF(data);
                            }
                            Logger.log(myName,"Response echo sent...");
                            break;
                    }

                }

                finally {

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
