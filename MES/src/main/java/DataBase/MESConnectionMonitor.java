package DataBase;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;

import DataBase.OrderDatabase.OrderDb;
import DataBase.OrderDatabase.OrderSystemDb;


public class MESConnectionMonitor implements Runnable {
    private static final int RECONNECT_INTERVAL = 2000; // Intervalo de verificação em milissegundos
    private String mesAddress;
    private int mesPort;

    public MESConnectionMonitor(String mesAddress, int mesPort) {
        this.mesAddress = mesAddress;
        this.mesPort = mesPort;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Verificar se a conexão com MES está ativa, senão esperar x segundos e verificar outra vez até estar ativa
                if (!isConnectedMES(mesAddress, mesPort)) {
                    System.out.println("Conexão ao MES perdida, tentando reconectar...");
                    Thread.sleep(RECONNECT_INTERVAL);
                }

                // Quando a conexão estiver ativa, guardar os valores da database
                if (isConnectedMES(mesAddress, mesPort)) {
                    System.out.println("Reconectado ao MES com sucesso.");
                    processOrdersAfterReconnection();
                    /*ArrayList<OrderDb> orders = (ArrayList<OrderDb>) OrderSystemDb.getAllOrders();
                    for (OrderDb order : orders) {
                        System.out.println(order);
                    }*/
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnectedMES(String address, int port) {
        try (Socket socket = new Socket()) {
            System.out.println("Tentando conectar ao MES em " + address + ":" + port);
            socket.connect(new InetSocketAddress(address, port), 2000);
            System.out.println("Conectado");
            return true;
        } catch (IOException e) {
            System.out.println("Não conectado: " + e.getMessage());
            return false;
        }
    }

    public static void processOrdersAfterReconnection() {
        try {
            ResultSet rs = DatabaseMES.getAllOrders();
            while (rs.next()) {
                String orderId = rs.getString("orderid");
                String pieceType = rs.getString("piecetype");
                String quantity = rs.getString("quantity");
                String startDay = rs.getString("startday");
                String endDay = rs.getString("endday");
                Boolean processed_1 = rs.getBoolean("processed_1");
                Boolean processed_2 = rs.getBoolean("processed_2");
                Boolean processed_3 = rs.getBoolean("processed_3");
                Boolean processed_4 = rs.getBoolean("processed_4");
                Boolean processed_5 = rs.getBoolean("processed_5");

                // Processar a ordem (exemplo, adicionar ao sistema de ordens)
                if(processed_1==false || processed_2==false || processed_3==false || processed_4==false || processed_5==false){
                    OrderDb order = new OrderDb(orderId, pieceType, quantity, startDay, endDay, processed_1, processed_2, processed_3, processed_4, processed_5);
                    OrderSystemDb.addOrderDb(order);
                }
                //System.out.println("Ordem recuperada: " + ordernumber);
            }
        } catch (SQLException e) {
            System.err.println("Falha ao recuperar ordens após a reconexão: " + e.getMessage());
        }
    }
    
}