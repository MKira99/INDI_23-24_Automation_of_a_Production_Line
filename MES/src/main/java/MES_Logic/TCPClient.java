package MES_Logic;


import java.io.*;
import java.net.*;
import org.json.*;



public class TCPClient {
    public static String args[] = new String[3];

    public TCPClient(String orderid, String status, String time) {
        args[0] = orderid;
        args[1] = status;
        args[2] = time;
    }



    public static void main() { //Order Completed

        try {
            String host = "localhost";
            int port = 4999;
            int retryInterval = 2000;
            // Establish a TCP/IP connection to the remote host
            while (true) {
                try {
                    System.out.println("Attempting to connect to the server...");
                    Socket socket = new Socket(host, port);
                    System.out.println("Connected to the server.");
                    // Proceed with your logic after a successful connection
                    OutputStream outputStream = socket.getOutputStream();

                    // Create a JSON object to store the data you want to send
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("OrderID", args[0]);
                    jsonObject.put("Status", args[1]);
                    jsonObject.put("Time", args[2]);

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
                    break;
                } catch (UnknownHostException e) {
                    System.err.println("Unknown host: " + host);
                    break; // Exit the loop if the host is unknown
                } catch (IOException e) {
                    System.err.println("Failed to connect to the server. Retrying in " + retryInterval / 1000 + " seconds...");
                    try {
                        Thread.sleep(retryInterval); // Wait before retrying
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.err.println("Thread interrupted. Exiting.");
                        break;
                    }
                }
            }

            // Create an output stream to send data to the remote host
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
