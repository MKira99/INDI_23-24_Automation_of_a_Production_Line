package MES_Logic;


import java.io.*;
import java.net.*;
import org.json.*;



public class TCPClient {

    public static void main(String[] args) { //Order Completed

        try {
            // Establish a TCP/IP connection to the remote host
            Socket socket = new Socket("localhost", 4999);

            // Create an output stream to send data to the remote host
            OutputStream outputStream = socket.getOutputStream();

            // Create a JSON object to store the data you want to send
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("OrderID", args[0]);

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

            // Convert the byte array to a string
            String responseString = new String(responseBytes);

            JSONObject responseJson = new JSONObject(responseString);

            // Print the response JSON object
            responseJson.keys().forEachRemaining(key -> {
                System.out.println(key + ": " + responseJson.get(key));
            });

            // Close the streams and the socket
            outputStream.close();
            inputStream.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
