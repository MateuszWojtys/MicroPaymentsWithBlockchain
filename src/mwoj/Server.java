package mwoj;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    public final static int SOCKET_PORT = 13267;  // you may change this
    public final static String FILE_TO_SEND = "C:/Users/mateusz.wojtys/Desktop/source.pdf";  // you may change this

    public void start() throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        try {

            servsock = new ServerSocket(SOCKET_PORT);
            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);

                    DataInputStream dis = new DataInputStream(sock.getInputStream());
                    String requestType = dis.readUTF();
                    System.out.println("Request received: " + requestType);
                    if(requestType.equals("Test"))
                    {
                        String string = "Example response";
                        System.out.println("Sending response...");
                        DataOutputStream dos = null;
                        dos = new DataOutputStream(sock.getOutputStream());
                        dos.writeUTF(string);
                        System.out.println("Response sent...");
                    }
                    else
                    {

                    }
                    }

                finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock!=null) sock.close();
                }
            }
        }
        finally {
            if (servsock != null) servsock.close();
        }
    }

    @Override
    public void run() {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/* // send file
                        File myFile = new File (FILE_TO_SEND);
                        byte[] mybytearray  = new byte [(int)myFile.length()];
                        fis = new FileInputStream(myFile);
                        bis = new BufferedInputStream(fis);
                        bis.read(mybytearray,0,mybytearray.length);
                        os = sock.getOutputStream();
                        System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                        os.write(mybytearray,0,mybytearray.length);
                        os.flush();
                        System.out.println("Done.");
                        */