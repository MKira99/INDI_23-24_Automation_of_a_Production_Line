package MES_Logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.json.JSONObject;

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

    private static Variant mach_c1_top_finished;
    private static Variant mach_c1_bot_finished;
    private static Variant mach_c2_top_finished;
    private static Variant mach_c2_bot_finished;
    private static Variant mach_c3_top_finished;
    private static Variant mach_c3_bot_finished;
    private static Variant mach_c4_top_finished;
    private static Variant mach_c4_bot_finished;
    private static Variant mach_c5_top_finished;
    private static Variant mach_c5_bot_finished;
    private static Variant mach_c6_top_finished;
    private static Variant mach_c6_bot_finished;

    public static MillisTimer DayTimer;
    public static MillisTimer SecondsTimer;


    public static class DayUpdateRunnable implements Runnable {
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
                    System.out.println("OrderID: " + requestJson.get("OrderID") + "\nPieceType / Quantity :  " + requestJson.get("PieceType") + " / " + requestJson.get("Quantity") + "\nDay to Start / Finish: " + requestJson.get("DateStart") + " / " + requestJson.get("DateEnd") + "\n");

                    // Create a JSON object to store the response data
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("status", "OK");

                    // Convert the response JSON object to a JSON string
                    String responseString = responseJson.toString();

                    // Create an output stream to send data to the client
                    OutputStream outputStream = clientSocket.getOutputStream();

                    // Send the response string to the client
                    outputStream.write(responseString.getBytes());

                    /*
                    Order1 = new order();
                    Order1.setPieceNr(requestJson.getInt("Piece"));
                    Order1.setOrderID(requestJson.getInt("OrderID"));
                    Order1.setQuantity(requestJson.getInt("Quantity"));
                    Order1.setArrivingDay(requestJson.getInt("ArrivingDay"));
                    Order1.setStartingDay(requestJson.getInt("Day"));
                    */

                    //  DataBase.getNextOrderNumber();
                    //DataBase.insertOrder(Order1);
                    //startPieceProduction();
                    
                    System.out.println("TEST : " + requestJson.get("OrderID").equals("Testing Order"));
                    if(requestJson.get("PieceType").equals("P4") && requestJson.get("Quantity").equals("9") && requestJson.get("DateStart").equals("1") && requestJson.get("DateEnd").equals("5") && requestJson.get("OrderID").equals("Testing Order"))
                    {
                        boolean test;
                        test = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_bot_finished") != new Variant(true);
                        System.out.println("TEST : " + test);
                        System.out.println("Order Accepted.\nStarting Production ...\n\n\n");
                        String dateStartString = requestJson.getString("DateStart");
                        int dateStart = Integer.parseInt(dateStartString);

                        synchronized (this) {
                            int elapsedTime = 0; // Counter to track elapsed time
                            System.out.println("Waiting for the right day to start ...\nCurrent Day: " + DayTimer.getDay() + "\nDay to Start: " + dateStart + "\n");
                            while (DayTimer.getDay() < 0) {
                                try {
                                    this.wait(100); // Wait for 100 milliseconds
                                    elapsedTime += 100; // Increment elapsed time by 100  milliseconds
                        
                                    if (elapsedTime >= 30000) { // Print message every 30 seconds (30000 milliseconds)
                                        System.out.println("Still waiting for the right day to start ...\nCurrent Day: " + DayTimer.getDay() + "\nDay to Start: " + dateStart + "\n");
                                        elapsedTime = 0; // Reset elapsed time
                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    // Handle interruption
                                }
                            }
                        }
                        

                        System.out.println("Order Accepted.\nStarting Production ...\n\n\n");
                        System.out.println("Putting Machines with the right tools ...\n");
                        short value1 = 1;
                        short value2 = 2;
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_bot_des_tool", new Variant(value1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_bot_des_tool", new Variant(value2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_top_des_tool", new Variant(value1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_bot_des_tool", new Variant(value2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_top_des_tool", new Variant(value1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_bot_des_tool", new Variant(value2));

                        synchronized (this) {
                            this.wait(1000); // Wait for 1000 milliseconds (1 second)
                            int elapsedTime = 0; // Counter to track elapsed time
                            System.out.println("Waiting for Machines to be ready ...\n");
                            test = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_bot_finished") == new Variant(false);
                            System.out.println("TEST : " + test + "    BUT     " + opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_bot_finished") + "      " + new Variant(false));
                            while (opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_bot_finished").equals(new Variant(false)) ||
                                    opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_top_finished").equals(new Variant(false)) ||
                                    opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_bot_finished").equals(new Variant(false)) ||
                                    opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_top_finished").equals(new Variant(false)) ||
                                    opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_bot_finished").equals(new Variant(false)) ||
                                    opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_top_finished").equals(new Variant(false))) {      
                                try {
                                    this.wait(1000); // Wait for 1000 milliseconds (1 second)
                                    elapsedTime += 1000; // Increment elapsed time by 1000 milliseconds
                                    
                                    if (elapsedTime >= 10000) { // Print message every 10 seconds (10000 milliseconds)
                                        System.out.println("Still waiting for Machines to be ready ...\n");
                                        System.out.println("Values : " + opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_bot_finished") != new Variant(true) + " " + opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_top_finished")  + " " + opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_bot_finished") + " " + opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_top_finished") + " " + opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_bot_finished") + " " + opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_top_finished") + "\n");
                                        elapsedTime = 0; // Reset elapsed time
                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    // Handle interruption
                                }
                            }
                        }
                        
                        System.out.println("Loading Raw Material ...\n");
                        
                        String idload1 = "load1_test1";
                        String idload2 = "load2_test1";
                        String idload3 = "load3_test1";
                        String idload4 = "load4_test1";
                        
                        short qty1 = 3;
                        short qty2 = 2;
                        short qty3 = 2;
                        short qty4 = 2;
                        
                        short rawtype = 1;
                        
                        short tool1 = 0;
                        short tool2 = 0;
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load1", new Variant(idload1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load2", new Variant(idload2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load3", new Variant(idload3));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load4", new Variant(idload4));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load1", new Variant(qty1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load2", new Variant(qty2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load3", new Variant(qty3));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load4", new Variant(qty4));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load1", new Variant(rawtype));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load2", new Variant(rawtype));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load3", new Variant(rawtype));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load4", new Variant(rawtype));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load1", new Variant(tool1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load2", new Variant(tool1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load3", new Variant(tool1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load4", new Variant(tool1));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load1", new Variant(tool2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load2", new Variant(tool2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load3", new Variant(tool2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load4", new Variant(tool2));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load1", new Variant(true));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load2", new Variant(true));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load3", new Variant(true));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load4", new Variant(true));
                        
                        synchronized (this) {
                            this.wait(5000); // Wait for 1000 milliseconds (1 second)
                            int elapsedTime = 0; // Counter to track elapsed time
                            System.out.println("Waiting for Loading all Raw Materials ...\n");
                            while (getLoad1_finished().equals(new Variant(false)) ||
                                    getLoad2_finished().equals(new Variant(false)) ||
                                    getLoad3_finished().equals(new Variant(false)) ||
                                    getLoad4_finished().equals(new Variant(false))) {
                        
                                try {
                                    this.wait(1000); // Wait for 1000 milliseconds (1 second)
                                    elapsedTime += 1000; // Increment elapsed time by 1000 milliseconds
                                    
                                    if (elapsedTime >= 10000) { // Print message every 10 seconds (10000 milliseconds)
                                        System.out.println("Still waiting for Loading all Raw Materials ...\n");
                                        elapsedTime = 0; // Reset elapsed time
                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    // Handle interruption
                                }
                            }
                        }
                        
                        System.out.println("Starting Transformation ...\n");
                        
                        String idcell1 = "load1_test1";
                        String idcell2 = "load2_test1";
                        String idcell3 = "load3_test1";
                        
                        qty1 = 3;
                        qty2 = 3;
                        qty3 = 4;
                        
                        short type1 = 1;
                        short type2 = 1;
                        short type3 = 1;
                        
                        tool1 = 1;
                        tool2 = 2;
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell1", new Variant(idcell1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell2", new Variant(idcell2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell3", new Variant(idcell3));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell1", new Variant(qty1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell2", new Variant(qty2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell3", new Variant(qty3));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell1", new Variant(type1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell2", new Variant(type2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell3", new Variant(type3));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell1", new Variant(tool1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell2", new Variant(tool1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell3", new Variant(tool1));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell1", new Variant(tool2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell2", new Variant(tool2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell3", new Variant(tool2));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell1", new Variant(true));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell2", new Variant(true));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell3", new Variant(true));
                        
                        synchronized (this) {
                            this.wait(1000); // Wait for 1000 milliseconds (1 second)
                            int elapsedTime = 0; // Counter to track elapsed time
                            System.out.println("Waiting for Transformation to be done ...\n");
                            while (getCell1_finished().equals(new Variant(false)) ||
                                    getCell2_finished().equals(new Variant(false)) ||
                                    getCell3_finished().equals(new Variant(false))) {
                                try {
                                    this.wait(1000); // Wait for 1000 milliseconds (1 second)
                                    elapsedTime += 1000; // Increment elapsed time by 1000 milliseconds
                                    
                                    if (elapsedTime >= 10000) { // Print message every 10 seconds (10000 milliseconds)
                                        System.out.println("Still waiting for Transformation to be done ...\n");
                                        elapsedTime = 0; // Reset elapsed time
                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    // Handle interruption
                                }
                            }
                        }
                        
                        System.out.println("Unloading Finished Products ...\n");
                        String idunload1 = "load1_test1";
                        String idunload2 = "load2_test1";
                        String idunload3 = "load3_test1";
                        String idunload4 = "load4_test1";
                        
                        
                        qty1 = 3;
                        qty2 = 2;
                        qty3 = 2;
                        qty4 = 2;
                        
                        short finishtype = 4;
                        
                        tool1 = 0;
                        tool2 = 0;
                        
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload1", new Variant(idunload1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload2", new Variant(idunload2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload3", new Variant(idunload3));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload4", new Variant(idunload4));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload1", new Variant(qty1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload2", new Variant(qty2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload3", new Variant(qty3));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload4", new Variant(qty4));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload1", new Variant(finishtype));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload2", new Variant(finishtype));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload3", new Variant(finishtype));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload4", new Variant(finishtype));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload1", new Variant(tool1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload2", new Variant(tool1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload3", new Variant(tool1));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload4", new Variant(tool1));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload1", new Variant(tool2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload2", new Variant(tool2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload3", new Variant(tool2));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload4", new Variant(tool2));
                        
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload1", new Variant(true));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload2", new Variant(true));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload3", new Variant(true));
                        opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload4", new Variant(true));
                        
                        String dateEndStr = (String) requestJson.get("DateEnd");
                        int dateEnd = Integer.parseInt(dateEndStr);

                        
                        synchronized (this) {
                            this.wait(1000); // Wait for 1000 milliseconds (1 second)
                            int elapsedTime = 0; // Counter to track elapsed time
                            System.out.println("Waiting for Unloading all Finished Products ...\n");
                            while (getUnload1_finished().equals(new Variant(false)) ||
                                    getUnload2_finished().equals(new Variant(false)) ||
                                    getUnload3_finished().equals(new Variant(false)) ||
                                    getUnload4_finished().equals(new Variant(false)) ||
                                    DayTimer.getDay() < dateEnd) {
                                try {
                                    this.wait(1000); // Wait for 1000 milliseconds (1 second)
                                    elapsedTime += 1000; // Increment elapsed time by 1000 milliseconds
                                    
                                    if (elapsedTime >= 10000) { // Print message every 10 seconds (10000 milliseconds)
                                        System.out.println("Still waiting for Unloading all Finished Products ...\n");
                                        elapsedTime = 0; // Reset elapsed time
                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    // Handle interruption
                                }
                            }
                        }
                        
                        

                        System.out.println("Order Completed, thanks for working with us.\n");

                    }
                    else
                    {
                        System.out.println("Order Rejected");

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

                    mach_c1_bot_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_bot_finished");
                    mach_c1_top_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_top_finished");
                    mach_c2_bot_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_bot_finished");
                    mach_c2_top_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_top_finished");
                    mach_c3_bot_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_bot_finished");
                    mach_c3_top_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_top_finished");
                    mach_c4_bot_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c4_bot_finished");
                    mach_c4_top_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c4_top_finished");
                    mach_c5_bot_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c5_bot_finished");
                    mach_c5_top_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c5_top_finished");
                    mach_c6_bot_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c6_bot_finished");
                    mach_c6_top_finished = opcua.read("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c6_top_finished");


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class PLCHandler implements Runnable {
        @Override
        public void run() {
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

    public static Variant getmach_c1_top_finished(){return mach_c1_top_finished;}
    public static Variant getmach_c1_bot_finished(){return mach_c1_bot_finished;}
    public static Variant getmach_c2_top_finished(){return mach_c2_top_finished;}
    public static Variant getmach_c2_bot_finished(){return mach_c2_bot_finished;}
    public static Variant getmach_c3_top_finished(){return mach_c3_top_finished;}
    public static Variant getmach_c3_bot_finished(){return mach_c3_bot_finished;}
    public static Variant getmach_c4_top_finished(){return mach_c4_top_finished;}
    public static Variant getmach_c4_bot_finished(){return mach_c4_bot_finished;}
    public static Variant getmach_c5_top_finished(){return mach_c5_top_finished;}
    public static Variant getmach_c5_bot_finished(){return mach_c5_bot_finished;}
    public static Variant getmach_c6_top_finished(){return mach_c6_top_finished;}
    public static Variant getmach_c6_bot_finished(){return mach_c6_bot_finished;}
}
