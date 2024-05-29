package MES_Logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.json.JSONObject;

import DataBase.DatabaseMES;
import DataBase.OrderDatabase;
import DataBase.OrderDatabase.OrderDb;
import MES_GUI.Screen;
import OPC.opcua;
import DataBase.*;


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
    public static boolean firstTime = true;


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
                   if (SecondsTimer.getSeconds() > 30.0) {
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
                        ServerSocket serverSocket = null;
                        try {
                            serverSocket = new ServerSocket(9999);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        
                        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

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
                        System.out.println("OrderID: " + requestJson.get("orderId") + "\nPieceType / Quantity :  " + requestJson.get("workPiece") + " / " + requestJson.get("quantity") + "\nDay to Start / Finish: " + requestJson.get("startDate") + " / " + requestJson.get("endDate") + "\n");

                        // Create a JSON object to store the response data
                        JSONObject responseJson = new JSONObject();
                        responseJson.put("status", "OK");



                        // Convert the response JSON object to a JSON string
                        String responseString = responseJson.toString();

                        // Create an output stream to send data to the client
                        OutputStream outputStream = clientSocket.getOutputStream();

                        // Send the response string to the client
                        outputStream.write(responseString.getBytes());

                        // Close the streams and the socket
                        inputStream.close();
                        outputStream.close();
                        clientSocket.close();
                        serverSocket.close();

                        System.out.println("Criou\n\n\n\n\n");

                        // Parse the request JSON object
                        String OrderID;
                        String PieceType;
                        short Quantity;
                        short DateStart;
                        short DateEnd;

                        OrderID = requestJson.getString("orderId");
                        PieceType = requestJson.getString("workPiece");
                        Quantity = (short) requestJson.getInt("quantity");
                        DateStart = (short) requestJson.getInt("startDate");
                        DateEnd = (short) requestJson.getInt("endDate");

                        System.out.println("Criou\n\n\n\n\n");

                        System.out.println("Starting the Following Order : OrderID: " + OrderID + "\nPieceType: " + PieceType + "\nQuantity: " + Quantity + "\nDateStart: " + DateStart + "\nDateEnd: " + DateEnd);

                        Order newOrder = new Order(OrderID, PieceType, Quantity, DateStart, DateEnd);

                        try {
                            System.out.println("Inserting order into database: " + newOrder);
                            DatabaseMES.insertOrder(newOrder.getOrderID(), newOrder.getPieceType(), newOrder.getQuantity(), newOrder.getStartDay(), newOrder.getEndDay());
                        } catch (SQLException e) {
                            e.printStackTrace();
                            continue; // Skip this iteration if the connection fails
                        }
                        
                        System.out.println("Order Accepted.\nStarting Logic Calculation ...\n\n\n");

                        OrderList orderList = new OrderList();
                        orderList.addOrder(newOrder);
                        Screen.updateActiveOrder(orderList);
                        MESLogic logic = new MESLogic(newOrder);
                        logic.main();

                        MESConnectionMonitor.processOrdersAfterReconnection();

                        Thread plc = new Thread(new PLCHandler(newOrder, logic.getCommand(), logic.getUsageOfCell_2()));
                        plc.start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
                
            /*
                String OrderID;
                String PieceType;
                short Quantity;
                short DateStart;
                short DateEnd;
                OrderID = "TEST1";
                PieceType = "P9";
                Quantity = 5;
                DateStart = 1;
                DateEnd = 6;

                Order newOrder = new Order(OrderID, PieceType, Quantity, DateStart, DateEnd);

                MESLogic logic = new MESLogic(newOrder);
                logic.main();

                Thread plc = new Thread(new PLCHandler(newOrder, logic.getCommand(), logic.getUsageOfCell_2()));
                plc.start();
            */


                
            
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
        private Order ReceivedOrder;
        private Command[] commands;
        boolean usageOfCell_2 = false;
        boolean processed_1 = false, processed_2 = false, processed_3 = false, processed_4 = false, processed_5 = false;
    
        // Constructor to initialize the Order
        
        public PLCHandler(Order ReceivedOrder, Command[] commands, boolean usageOfCell_2) {
            this.ReceivedOrder = ReceivedOrder;
            this.commands = commands;
            this.usageOfCell_2 = usageOfCell_2;
        }
    
        @Override
        public void run() {
            // Process the Order object here

            System.out.println("Order Accepted.\nStarting Production ...\n\n\n");

            try{
                //Machine
                if(commands[5].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_top_des_tool", new Variant(commands[5].tool1));}
                if(commands[5].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_bot_des_tool", new Variant(commands[5].tool2));}
                if(commands[6].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_top_des_tool", new Variant(commands[6].tool1));}
                if(commands[6].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_bot_des_tool", new Variant(commands[6].tool2));}
                if(commands[7].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_top_des_tool", new Variant(commands[7].tool1));}
                if(commands[7].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_bot_des_tool", new Variant(commands[7].tool2));}
                if(commands[8].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c4_top_des_tool", new Variant(commands[8].tool1));}
                if(commands[8].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c4_bot_des_tool", new Variant(commands[8].tool2));}
                if(commands[9].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c5_top_des_tool", new Variant(commands[9].tool1));}
                if(commands[9].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c5_bot_des_tool", new Variant(commands[9].tool2));}
                if(commands[10].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c6_top_des_tool", new Variant(commands[10].tool1));}
                if(commands[10].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c6_bot_des_tool", new Variant(commands[10].tool2));}

                int count=0;

                processed_1=true;
                try {
                    System.out.println("Inserting flag of PLC changing tools");
                    DatabaseMES.insertFlag_1(processed_1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                while(ReceivedOrder.getStartDay() > DayTimer.getDay()){
                    synchronized (this) {
                        count++;
                        if (count > 1000) {
                            System.out.println("Waiting for the right day to start ...\nCurrent Day: " + DayTimer.getDay() + "\nDay to Start: " + ReceivedOrder.getStartDay() + "\n");
                        }
                        this.wait(500);
                    }
                }
                


                //Load
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load1", new Variant(commands[0].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load2", new Variant(commands[1].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load3", new Variant(commands[2].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load4", new Variant(commands[3].id));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load1", new Variant(commands[0].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load2", new Variant(commands[1].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load3", new Variant(commands[2].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load4", new Variant(commands[3].quantity));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load1", new Variant(commands[0].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load2", new Variant(commands[1].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load3", new Variant(commands[2].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load4", new Variant(commands[3].type));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load1", new Variant(commands[0].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load2", new Variant(commands[1].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load3", new Variant(commands[2].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load4", new Variant(commands[3].tool1));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load1", new Variant(commands[0].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load2", new Variant(commands[1].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load3", new Variant(commands[2].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load4", new Variant(commands[3].tool2));

                synchronized (this) {
                    this.wait(3000);
                }

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load1", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load2", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load3", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load4", new Variant(true));

                //Cell
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell0", new Variant(commands[4].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell1", new Variant(commands[5].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell2", new Variant(commands[6].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell3", new Variant(commands[7].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell4", new Variant(commands[8].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell5", new Variant(commands[9].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell6", new Variant(commands[10].id));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell0", new Variant(commands[4].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell1", new Variant(commands[5].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell2", new Variant(commands[6].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell3", new Variant(commands[7].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell4", new Variant(commands[8].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell5", new Variant(commands[9].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell6", new Variant(commands[10].quantity));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell0", new Variant(commands[4].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell1", new Variant(commands[5].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell2", new Variant(commands[6].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell3", new Variant(commands[7].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell4", new Variant(commands[8].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell5", new Variant(commands[9].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell6", new Variant(commands[10].type));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell0", new Variant(commands[4].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell1", new Variant(commands[5].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell2", new Variant(commands[6].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell3", new Variant(commands[7].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell4", new Variant(commands[8].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell5", new Variant(commands[9].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell6", new Variant(commands[10].tool1));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell0", new Variant(commands[4].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell1", new Variant(commands[5].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell2", new Variant(commands[6].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell3", new Variant(commands[7].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell4", new Variant(commands[8].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell5", new Variant(commands[9].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell6", new Variant(commands[10].tool2));

                synchronized (this) {
                    this.wait(3000);
                }

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell0", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell1", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell2", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell3", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell4", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell5", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell6", new Variant(true));


                processed_2=true;
                try {
                    System.out.println("Inserting flag concerning the load and the cells");
                    DatabaseMES.insertFlag_2(processed_2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                synchronized (this) {
                    this.wait(1000);
                }

                while(getCell0_finished().equals(new Variant(false)) ||
                        getCell1_finished().equals(new Variant(false)) ||
                        getCell2_finished().equals(new Variant(false)) ||
                        getCell3_finished().equals(new Variant(false)) ||
                        getCell4_finished().equals(new Variant(false)) ||
                        getCell5_finished().equals(new Variant(false)) ||
                        getCell6_finished().equals(new Variant(false)))
                {
                    synchronized (this) {
                        this.wait(5000);
                        System.out.println("Waiting for the Cells_1 to finish ...\n");
                    }
                    
                }

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load1", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load2", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load3", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_load4", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell0", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell1", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell2", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell3", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell4", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell5", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell6", new Variant("NULL"));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_load4", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell0", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell4", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell5", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell6", new Variant(0));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load2", new Variant(0));  
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_load4", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell0", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell4", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell5", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell6", new Variant(0));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_load4", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell0", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell4", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell5", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell6", new Variant(0));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_load4", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell0", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell4", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell5", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell6", new Variant(0));
                
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load1", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load2", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load3", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_load4", new Variant(false));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell0", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell1", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell2", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell3", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell4", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell5", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell6", new Variant(false));



                if(usageOfCell_2 == true)
                {
                    if(commands[12].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_top_des_tool", new Variant(commands[12].tool1));}
                    if(commands[12].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c1_bot_des_tool", new Variant(commands[12].tool2));}
                    if(commands[13].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_top_des_tool", new Variant(commands[13].tool1));}
                    if(commands[13].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c2_bot_des_tool", new Variant(commands[13].tool2));}
                    if(commands[14].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_top_des_tool", new Variant(commands[14].tool1));}
                    if(commands[14].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c3_bot_des_tool", new Variant(commands[14].tool2));}
                    if(commands[15].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c4_top_des_tool", new Variant(commands[15].tool1));}
                    if(commands[15].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c4_bot_des_tool", new Variant(commands[15].tool2));}
                    if(commands[16].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c5_top_des_tool", new Variant(commands[16].tool1));}
                    if(commands[16].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c5_bot_des_tool", new Variant(commands[16].tool2));}
                    if(commands[17].tool1 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c6_top_des_tool", new Variant(commands[17].tool1));}
                    if(commands[17].tool2 != 0){opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.mach_c6_bot_des_tool", new Variant(commands[17].tool2));}
                    

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell0", new Variant(commands[11].id));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell1", new Variant(commands[12].id));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell2", new Variant(commands[13].id));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell3", new Variant(commands[14].id));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell4", new Variant(commands[15].id));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell5", new Variant(commands[16].id));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell6", new Variant(commands[17].id));

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell0", new Variant(commands[11].quantity));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell1", new Variant(commands[12].quantity));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell2", new Variant(commands[13].quantity));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell3", new Variant(commands[14].quantity));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell4", new Variant(commands[15].quantity));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell5", new Variant(commands[16].quantity));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell6", new Variant(commands[17].quantity));

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell0", new Variant(commands[11].type));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell1", new Variant(commands[12].type));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell2", new Variant(commands[13].type));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell3", new Variant(commands[14].type));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell4", new Variant(commands[15].type));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell5", new Variant(commands[16].type));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell6", new Variant(commands[17].type));

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell0", new Variant(commands[11].tool1));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell1", new Variant(commands[12].tool1));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell2", new Variant(commands[13].tool1));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell3", new Variant(commands[14].tool1));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell4", new Variant(commands[15].tool1));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell5", new Variant(commands[16].tool1));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell6", new Variant(commands[17].tool1));

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell0", new Variant(commands[11].tool2));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell1", new Variant(commands[12].tool2));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell2", new Variant(commands[13].tool2));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell3", new Variant(commands[14].tool2));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell4", new Variant(commands[15].tool2));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell5", new Variant(commands[16].tool2));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell6", new Variant(commands[17].tool2));

                    synchronized (this) {
                        this.wait(3000);
                    }

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell0", new Variant(true));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell1", new Variant(true));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell2", new Variant(true));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell3", new Variant(true));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell4", new Variant(true));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell5", new Variant(true));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell6", new Variant(true));

                    processed_3=true;
                    try {
                        System.out.println("Inserting flag concerning the general working of the PLC");
                        DatabaseMES.insertFlag_3(processed_3);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    synchronized (this) {
                        this.wait(1000);
                    }

                    while(getCell0_finished().equals(new Variant(false)) ||
                            getCell1_finished().equals(new Variant(false)) ||
                            getCell2_finished().equals(new Variant(false)) ||
                            getCell3_finished().equals(new Variant(false)) ||
                            getCell4_finished().equals(new Variant(false)) ||
                            getCell5_finished().equals(new Variant(false)) ||
                            getCell6_finished().equals(new Variant(false)))
                    {
                        synchronized (this) {
                            this.wait(5000);
                            System.out.println("Waiting for the Cells_2 to finish ...\n");
                        }
                    }

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell0", new Variant("NULL"));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell1", new Variant("NULL"));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell2", new Variant("NULL"));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell3", new Variant("NULL"));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell4", new Variant("NULL"));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell5", new Variant("NULL"));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_cell6", new Variant("NULL"));

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell0", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell1", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell2", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell3", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell4", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell5", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_cell6", new Variant(0));

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell0", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell1", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell2", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell3", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell4", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell5", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_cell6", new Variant(0));

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell0", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell1", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell2", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell3", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell4", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell5", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_cell6", new Variant(0));

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell0", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell1", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell2", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell3", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell4", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell5", new Variant(0));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_cell6", new Variant(0));

                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell0", new Variant(false));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell1", new Variant(false));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell2", new Variant(false));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell3", new Variant(false));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell4", new Variant(false));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell5", new Variant(false));
                    opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_cell6", new Variant(false));
                
                }

                //Unload

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload1", new Variant(commands[18].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload2", new Variant(commands[19].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload3", new Variant(commands[20].id));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload4", new Variant(commands[21].id));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload1", new Variant(commands[18].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload2", new Variant(commands[19].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload3", new Variant(commands[20].quantity));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload4", new Variant(commands[21].quantity));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload1", new Variant(commands[18].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload2", new Variant(commands[19].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload3", new Variant(commands[20].type));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload4", new Variant(commands[21].type));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload1", new Variant(commands[18].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload2", new Variant(commands[19].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload3", new Variant(commands[20].tool1));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload4", new Variant(commands[21].tool1));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload1", new Variant(commands[18].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload2", new Variant(commands[19].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload3", new Variant(commands[20].tool2));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload4", new Variant(commands[21].tool2));

                synchronized (this) {
                    this.wait(3000);
                }

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload1", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload2", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload3", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload4", new Variant(true));

                synchronized (this) {
                    this.wait(1000);
                }

                processed_4=true;
                try {
                    System.out.println("Inserting flag concerning the unload");
                    DatabaseMES.insertFlag_4(processed_4);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                while(getUnload1_finished().equals(new Variant(false)) ||
                        getUnload2_finished().equals(new Variant(false)) ||
                        getUnload3_finished().equals(new Variant(false)) ||
                        getUnload4_finished().equals(new Variant(false)))
                {
                    synchronized (this) {
                        this.wait(5000);
                        System.out.println("Waiting for the Unloading to finish ...\n");
                    }
                }

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload1", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload2", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload3", new Variant("NULL"));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.id_unload4", new Variant("NULL"));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.qty_unload4", new Variant(0));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.type_unload4", new Variant(0));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool1_unload4", new Variant(0));

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload1", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload2", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload3", new Variant(0));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.tool2_unload4", new Variant(0));


                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload1", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload2", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload3", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload4", new Variant(false));

                //if(DayTimer.getDay() > ReceivedOrder.getEndDay()){
                //TCPClient finished = new TCPClient(ReceivedOrder.getOrderID(), "Waiting", Integer.toString(DayTimer.getDay()));
                //finished.main();
                //}

                processed_5=true;
                try {
                    System.out.println("Inserting flag concerning the end of the order");
                    DatabaseMES.insertFlag_5(processed_5);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                while(DayTimer.getDay() < ReceivedOrder.getEndDay())
                {
                    synchronized (this) {
                        this.wait(5000);
                        System.out.println("Waiting for the right day to finish ...\n");
                    }
                }

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload1_clear", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload2_clear", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload3_clear", new Variant(true));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload4_clear", new Variant(true));

                synchronized (this) {
                    this.wait(5000);
                }

                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload1_clear", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload2_clear", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload3_clear", new Variant(false));
                opcua.write("|var|CODESYS Control Win V3 x64.Application.MAIN_SM.comm_unload4_clear", new Variant(false));

                synchronized (this) {
                    this.wait(1000);
                }


                System.out.println("Finished");

                try{
                    System.out.println("Clearing the database");
                    DatabaseMES.truncateTable();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                TCPClient finished2 = new TCPClient(ReceivedOrder.getOrderID(), "Finished", Integer.toString(DayTimer.getDay()));
                finished2.main();

            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            


            
        }
    }
    
    public static class GUI implements Runnable {
        public void run() {
            SwingUtilities.invokeLater(() -> {
            Screen screen = new Screen();
            screen.setVisible(true);
        });
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
