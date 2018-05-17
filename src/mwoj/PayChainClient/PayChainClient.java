package mwoj.PayChainClient;

import mwoj.Common.Logger;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class PayChainClient {
    private final String myName = "PayChainClient|||";
    private int SERVER_PORT = 0;
    private String SERVER_IP = null;

    public PayChainClient(String serverIP, int serverPort)
    {
        SERVER_IP = serverIP;
        SERVER_PORT = serverPort;
    }

    public JSONObject sendMessage(String header) {

        final JSONObject[] responseData = new JSONObject[1];
        Thread t = new Thread() {

            public void run() {
                Socket socket = null;
                try {
                    socket = new Socket(SERVER_IP, SERVER_PORT);
                    DataOutputStream dos = null;
                    dos = new DataOutputStream(socket.getOutputStream());
                    Logger.log(myName, "Wysyłam wiadomosc: " + header +
                    " do " + SERVER_IP + " " + SERVER_PORT);
                    dos.writeUTF(header);
                    dos.writeBoolean(false);
                    Logger.log(myName, "Odbieram wiadomosc...");
                    DataInputStream dis = new DataInputStream(socket.getInputStream());

                    //Nagłówek wiadomości
                    String response = dis.readUTF();
                    //Czy sa dane
                    boolean readData = dis.readBoolean();
                    Logger.log(myName, "Odebrana wiadomosc: " + response);
                    if(readData)
                    {
                        String data = dis.readUTF();
                        responseData[0] = new JSONObject(data);
                        Logger.log(myName, "Odebrane dane: " + data);
                    }
                    else
                    {
                        responseData[0] = new JSONObject();
                        Logger.log(myName, "Wiadomosc bez danych");
                    }

                    dos.flush();
                    dos.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
        return responseData[0];
    }

    public JSONObject sendMessageWithData(String header, JSONObject data) {

        final JSONObject[] responseData = new JSONObject[1];
        Thread t = new Thread() {

            public void run() {
                Socket socket = null;
                try {

                    socket = new Socket(SERVER_IP, SERVER_PORT);
                    DataOutputStream dos = null;
                    dos = new DataOutputStream(socket.getOutputStream());
                    Logger.log(myName, "Wysyłam wiadomosc: " + header + " do " + SERVER_IP + " " + SERVER_PORT);
                    dos.writeUTF(header);
                    dos.writeBoolean(true);
                    Logger.log(myName, "Wysyłam dane: " + data.toString() + " do " + SERVER_IP + " " + SERVER_PORT);
                    dos.writeUTF(data.toString());

                    Logger.log(myName, "Odbieram wiadomosc...");
                    DataInputStream dis = new DataInputStream(socket.getInputStream());

                    //Nagłówek wiadomości
                    String response = dis.readUTF();
                    //Czy sa dane
                    boolean readData = dis.readBoolean();
                    Logger.log(myName, "Odebrana wiadomosc: " + response);
                    if(readData)
                    {
                        String data = dis.readUTF();
                        responseData[0] = new JSONObject(data);
                        Logger.log(myName, "Odebrane dane: " + data);
                    }
                    else
                    {
                        responseData[0] = new JSONObject();
                        Logger.log(myName, "Wiadomosc bez danych");
                    }

                    dos.flush();
                    dos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        t.start();
        return responseData[0];
    }
}
