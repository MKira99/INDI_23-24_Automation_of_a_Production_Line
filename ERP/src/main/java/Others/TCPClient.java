package Others;


import java.io.*;
import java.net.*;
import org.json.*;



public class TCPClient {

    public static void main(JSONObject args) {

        try {
            JSONObject response2 = new JSONObject();
            response2.put("OrderID", "AA_01");
            response2.put("PieceType", "P3");
            response2.put("Quantity", 9);
            response2.put("DateStart", 1);
            response2.put("DateEnd", 3);

            // Establish a TCP/IP connection to the remote host
            Socket socket = new Socket("localhost", 9999);

            // Create an output stream to send data to the remote host
            OutputStream outputStream = socket.getOutputStream();

            // Convert the JSON object to a JSON string
            String jsonString = response2.toString();
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
