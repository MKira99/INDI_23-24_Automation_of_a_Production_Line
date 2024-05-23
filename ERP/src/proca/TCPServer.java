package proca;

import java.io.*;
import java.net.*;
import java.net.InetAddress;

import org.example.OrderToMES;
import org.json.*;

public class TCPServer {

    public static void main(String[] args) {

        try {
            // Create a server socket that listens on port 8080
            ServerSocket serverSocket = new ServerSocket(4999);
            System.out.println("Server started. Listening on port 4999...");

            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            System.out.println("Your IP address is: " + ipAddress);

            // Listen for incoming connections and handle them
            while (true) {
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
                //System.out.println("Received request: " + requestJson);

                // Create a JSON object to store the response data
                JSONObject responseJson = new JSONObject();
                responseJson.put("status", "OK");

                // Convert the response JSON object to a JSON string
                String responseString = responseJson.toString();

                // Create an output stream to send data to the client
                OutputStream outputStream = clientSocket.getOutputStream();

                // Send the response string to the client
                outputStream.write(responseString.getBytes());

                OrderToMES myOrder = new OrderToMES();
                myOrder.setField1(requestJson.getInt("Piece"));
                myOrder.setField2(requestJson.getInt("Quantity"));
                myOrder.setField3(requestJson.getInt("Day"));


                System.out.println(myOrder.getField1() + " " + myOrder.getField2() + " " + myOrder.getField3());
                //System.out.println(myOrder.getField2());
                //System.out.println(myOrder.getField3());
                // Close the streams and the client socket
                outputStream.close();
                inputStream.close();
                clientSocket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}