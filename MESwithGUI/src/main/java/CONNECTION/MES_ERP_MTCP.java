package CONNECTION;
import java.io.*;
import java.net.*;
import org.json.*;


public class MES_ERP_MTCP {

    public static void OrderCompleted(String host) {
        try {
            // Establish a TCP/IP connection to the remote host
            Socket socket = new Socket(host, 9999);

            // Create an output stream to send data to the remote host
            OutputStream outputStream = socket.getOutputStream();

            //int ProducedPiece = PieceProduction.pieceNr;
            JSONObject jsonObject;
            jsonObject = new JSONObject();
            /*
            switch (ProducedPiece) {
                case 3:
                    

                    jsonObject.put("OrderID", PieceProduction.orderID );
                    jsonObject.put("TimeLinePiece", Piece2to3.MediumTimePiece2to3);
                    jsonObject.put("TotalMachineTime", Piece2to3.Time2to3_ST3s);
                    jsonObject.put("ConclusionDay", Piece2to3.ConclusionDay);

                    break;

                case 4:
                    jsonObject = new JSONObject();
                    jsonObject.put("OrderID", PieceProduction.orderID );
                    jsonObject.put("TimeLinePiece", Piece2to4.MediumTimePiece2to4);
                    jsonObject.put("TotalMachineTime", Piece2to4.Time2to4_PT5s);
                    jsonObject.put("ConclusionDay", Piece2to4.ConclusionDay);

                    break;
                case 5:
                    jsonObject = new JSONObject();
                    jsonObject.put("OrderID", PieceProduction.orderID );
                    jsonObject.put("TimeLinePiece", Piece2to5.MediumTimePiece2to5);
                    jsonObject.put("TotalMachineTime", Piece2to5.TotalMachineTime);
                    jsonObject.put("ConclusionDay", Piece2to5.ConclusionDay);

                    break;
                case 6:
                    jsonObject = new JSONObject();
                    jsonObject.put("OrderID", PieceProduction.orderID );
                    jsonObject.put("TotalMachineTime", Piece1to6.Time1to6_ST5s);
                    jsonObject.put("TimeLinePiece", Piece1to6.MediumTimePiece1to6);
                    jsonObject.put("ConclusionDay", Piece1to6.ConclusionDay);

                    break;
                case 7:
                    jsonObject = new JSONObject();
                    jsonObject.put("OrderID", PieceProduction.orderID );
                    jsonObject.put("TotalMachineTime", Piece2to7.TotalMachineTime);
                    jsonObject.put("TimeLinePiece", Piece2to7.MediumTimePiece2to7);
                    jsonObject.put("ConclusionDay", Piece2to7.ConclusionDay);

                    break;
                case 8:
                    jsonObject = new JSONObject();
                    jsonObject.put("OrderID", PieceProduction.orderID );
                    jsonObject.put("TotalMachineTime", Piece1to8.TotalMachineTime);
                    jsonObject.put("TimeLinePiece", Piece1to8.MediumTimePiece1to8);
                    jsonObject.put("ConclusionDay", Piece1to8.ConclusionDay);

                    break;
                case 9:
                    System.out.println("I have entered: !!!!" +MyThreadCreator.DayTimer.getDay());
                    jsonObject = new JSONObject();
                    jsonObject.put("OrderID", PieceProduction.orderID );
                    jsonObject.put("TotalMachineTime", Piece2to9.TotalMachineTime);
                    jsonObject.put("TimeLinePiece", Piece2to9.MediumTimePiece2to9);
                    jsonObject.put("ConclusionDay", Piece2to9.ConclusionDay);

                    break;
                default:
                    throw new Exception("Invalid piece number: " + ProducedPiece);
            }
            */
            
            // Convert the JSON object to a JSON string
            String jsonString = jsonObject.toString();

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


   public static void main(String[] args) {

       try {
           // Establish a TCP/IP +ion to the remote host
           Socket socket = new Socket("localhost", 10000);

           // Create an output stream to send data to the remote host
           OutputStream outputStream = socket.getOutputStream();


           // Create a JSON object to store the data you want to send
           JSONObject jsonObject = new JSONObject();
           jsonObject.put("Piece", 9);
           jsonObject.put("OrderID", 1);
           jsonObject.put("Quantity", 2);
           jsonObject.put("ArrivingDay",1);
           jsonObject.put("Day",2);

           // Convert the JSON object to a JSON string
           String jsonString = jsonObject.toString();

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




