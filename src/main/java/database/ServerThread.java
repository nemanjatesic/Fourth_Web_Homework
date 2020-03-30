package database;

import com.google.gson.Gson;
import model.Assistant;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerThread implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private SQliteDB db;
    private static Gson gson = new Gson();

    public ServerThread(Socket sock, SQliteDB db) {
        this.client = sock;
        this.db = db;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String komanda = in.readLine();
            if (komanda.equals("GET")) {
                List<Assistant> list = db.getAllAssistant();
                list.forEach(System.out::println);
                String response = gson.toJson(list);
                out.println(response);
            }else if (komanda.equals("POST")) {
                String assistentStr = in.readLine();
                Assistant assistant = gson.fromJson(assistentStr, Assistant.class);
                assistant.setName(capitalize(assistant.getName().toLowerCase()));
                assistant.setLastName(capitalize(assistant.getLastName().toLowerCase()));
                if (assistant.getName().equals("Aleksandar")) {
                    assistant.setPoints(0);
                }
                if (assistant.getPoints() >= 0 && assistant.getPoints() <= 10) {
                    db.addAssistant(assistant);
                }
            }
            in.close();
            out.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}