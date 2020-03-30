package database;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int TCP_PORT = 8113;

    public static void main(String[] args) {
        SQliteDB db = new SQliteDB();
        try {
            ServerSocket ss = new ServerSocket(TCP_PORT);
            while (true) {
                Socket sock = ss.accept();
                new Thread(new ServerThread(sock, db)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            db.closeConnection();
        }
    }
}