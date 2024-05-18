package org.example;

import java.io.*;
import java.net.*;
import org.json.*;



public class TCPClient {

    public static void main(String[] args) {

        try {
            // Establish a TCP/IP connection to the remote host
            Socket socket = new Socket("10.227.145.9", 4999);

            // Create an output stream to send data to the remote host
            OutputStream outputStream = socket.getOutputStream();

            // Create a JSON object to store the data you want to send
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Piece", 3);
            jsonObject.put("Quantity", 5);
            jsonObject.put("Day", 10);

            // Convert the JSON object to a JSON string
            String jsonString = jsonObject.toString();
            System.out.println(jsonString);

            // Send the JSON string over the connection
            outputStream.write(jsonString.getBytes());

            // Create an input stream to receive data from the remote host
            InputStream inputStream = socket.getInputStream();

            // Read the response from the remote host as a byte array
            byte[] responseBytes = new byte[1024];
            inputStream.read(responseBytes);

            // Convert the byte array to a JSON string
            String responseString = new String(responseBytes);

            // Parse the JSON string as a JSON object
            JSONObject responseJson = new JSONObject(responseString);

            // Print the response JSON object
            System.out.println(responseJson);

            // Close the streams and the socket
            outputStream.close();
            inputStream.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
