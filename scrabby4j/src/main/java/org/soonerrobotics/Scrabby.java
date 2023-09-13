package org.soonerrobotics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import org.soonerrobotics.ROSTypes.ROSMessage;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Scrabby extends Thread {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private JsonReader reader;

    private static Gson gson = new Gson();

    private HashMap<String, ScrabbySubscriber> subscribers;

    public Scrabby(String ip, int port) throws UnknownHostException, IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        reader = new JsonReader(in);
        
        subscribers = new HashMap<String, ScrabbySubscriber>();
    }
    
    @Override
    public synchronized void run() {
        while (!Thread.interrupted()) {
            ROSMessage message = gson.fromJson(reader, ROSMessage.class);

            if (message.op.equals("publish") && subscribers.containsKey(message.topic)) {
                subscribers.get(message.topic).onReceive(message);
            }
        }
    }

    public <T> void subscribe(String topic, ScrabbySubscriber subscriber) {
        subscribers.put(topic, subscriber);
    }

    public static <T> T parseData(ROSMessage message, Class<T> type) {
        return gson.fromJson(message.data, type);
    }

    public <T> void publish(String topic, T data, Class<T> type) {
        ROSMessage message = new ROSMessage();
        message.op = "publish";
        message.topic = topic;
        message.msg = gson.toJsonTree(data, type);

        out.write(gson.toJson(message) + "\n\n");
        // System.out.println(gson.toJson(message));
    }
        
}
