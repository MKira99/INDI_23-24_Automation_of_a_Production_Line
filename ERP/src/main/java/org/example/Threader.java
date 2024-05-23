package org.example;

import java.io.*;
import java.net.*;
import org.json.*;



public class Threader {



    public static class AcceptOrderRunnable implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(9999);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Server started. Listening on port 9999...");
            while (true) {
                try {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    // Accept orders
                    // Create a new order object
                    // Insert the order into the database
                    // order Order1 = null;
                    // Listen for incoming connections and handle them
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                    // Create an input stream to receive data from the client
                    InputStream inputStream = clientSocket.getInputStream();

                    // Read the request from the client as a byte array
                    byte[] requestBytes = new byte[1024];
                    inputStream.read(requestBytes);

                    // Convert the byte array to a JSON string
                    String requestString = new String(requestBytes);
                    // Parse the JSON string as a JSON object
                    JSONObject requestJson = new JSONObject(requestString);

                    // Print the request JSON object
                    System.out.println("Received request: " + requestJson.get("Piece") + " " + requestJson.get("Quantity") + " " + requestJson.get("Day"));

                    // Create a JSON object to store the response data
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("status", "OK");

                    // Convert the response JSON object to a JSON string
                    String responseString = responseJson.toString();

                    // Create an output stream to send data to the client
                    OutputStream outputStream = clientSocket.getOutputStream();

                    // Send the response string to the client
                    outputStream.write(responseString.getBytes());
                    //  int nextOrderNumber = DataBase.getNextOrderNumber();
                    // requestJson.getInt("Time of a Piece")
                    serverSocket.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }  
    }

}
