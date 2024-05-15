package MES_Logic;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import OPC.opcua;


public class Threader {
    private static Variant Load1_finished;
    private static Variant Load2_finished;
    private static Variant Load3_finished;
    private static Variant Load4_finished;

    private static Variant Cell0_finished;
    private static Variant Cell1_finished;
    private static Variant Cell2_finished;
    private static Variant Cell3_finished;
    private static Variant Cell4_finished;
    private static Variant Cell5_finished;
    private static Variant Cell6_finished;

    private static Variant Unload1_finished;
    private static Variant Unload2_finished;
    private static Variant Unload3_finished;
    private static Variant Unload4_finished;

    public static MillisTimer DayTimer;
    public static MillisTimer SecondsTimer;



/*
    public static class acceptOrdersRunnable implements Runnable {

        @Override
        public void run() {

            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(5000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Server started. Listening on port 5000...");
            while (true){
                try{


                    order Order1 = null;
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
                    //  int nextOrderNumber = DataBase.getNextOrderNumber();
                    // requestJson.getInt("Time of a Piece")

                    Order1 = new order();
                    Order1.setPieceNr(requestJson.getInt("Piece"));
                    Order1.setOrderID(requestJson.getInt("OrderID"));
                    Order1.setQuantity(requestJson.getInt("Quantity"));
                    Order1.setArrivingDay(requestJson.getInt("ArrivingDay"));
                    Order1.setStartingDay(requestJson.getInt("Day"));

                    outputStream.close();
                    inputStream.close();
                    clientSocket.close();

                    //  DataBase.getNextOrderNumber();
                    DataBase.insertOrder(Order1);
                    startPieceProduction();


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    */
    public static class DayUpdateRunnable implements Runnable
    {
       @Override
       public void run()
       {
           DayTimer = new MillisTimer();
           SecondsTimer = new MillisTimer();

           DayTimer.start_nano();
           SecondsTimer.start_nano_seconds();

           while (true)
           {
               try {
                   if (SecondsTimer.getSeconds() > 60.0) {
                       System.out.println("Server Day: "+DayTimer.getDay());
                       SecondsTimer.reset();
                       SecondsTimer.start_nano_seconds();
                   }
               } catch (Exception e) {
                   throw new RuntimeException(e);
               }
           }
        }
    }
    
    public static class AcceptOrderRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    // Accept orders
                    // Create a new order object
                    // Insert the order into the database
                    short quantity = 5;
                    short type = 2;
                    short tool1 = 0;
                    short tool2 = 0;
                    System.out.println("Accepting orders...\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load1", new Variant(true));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load1", new Variant(quantity));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load1", new Variant(type));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load1", new Variant(tool1));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load1", new Variant(tool2));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load1", new Variant("first_try_load1"));
                    this.wait(1000);
                    if(opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load1") == new Variant(true) && opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.unload1_finished") == new Variant(true) ){
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load1", new Variant(false));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }  
    }

    public static class VariablesReadRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Load1_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.load1_finished");
                    Load2_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.load2_finished");
                    Load3_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.load3_finished");
                    Load4_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.load4_finished");

                    Cell0_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.cell0_finished");
                    Cell1_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.cell1_finished");
                    Cell2_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.cell2_finished");
                    Cell3_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.cell3_finished");
                    Cell4_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.cell4_finished");
                    Cell5_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.cell5_finished");
                    Cell6_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.cell6_finished");

                    Unload1_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.unload1_finished");
                    Unload2_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.unload2_finished");
                    Unload3_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.unload3_finished");
                    Unload4_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.unload4_finished");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static Variant getLoad1_finished(){return Load1_finished;}
    public static Variant getLoad2_finished(){return Load2_finished;}
    public static Variant getLoad3_finished(){return Load3_finished;}
    public static Variant getLoad4_finished(){return Load4_finished;}

    public static Variant getCell0_finished(){return Cell0_finished;}
    public static Variant getCell1_finished(){return Cell1_finished;}
    public static Variant getCell2_finished(){return Cell2_finished;}
    public static Variant getCell3_finished(){return Cell3_finished;}
    public static Variant getCell4_finished(){return Cell4_finished;}
    public static Variant getCell5_finished(){return Cell5_finished;}
    public static Variant getCell6_finished(){return Cell6_finished;}

    public static Variant getUnload1_finished(){return Unload1_finished;}
    public static Variant getUnload2_finished(){return Unload2_finished;}
    public static Variant getUnload3_finished(){return Unload3_finished;}
    public static Variant getUnload4_finished(){return Unload4_finished;}
}
