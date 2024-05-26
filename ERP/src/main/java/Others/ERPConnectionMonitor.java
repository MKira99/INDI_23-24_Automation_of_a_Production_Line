package Others;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

import Others.OrderDatabase.*;

public class ERPConnectionMonitor implements Runnable {
    private static final int RECONNECT_INTERVAL = 2000; // Intervalo de verificação em milissegundos
    private String erpAddress;
    private int erpPort;

    public ERPConnectionMonitor(String erpAddress, int erpPort) {
        this.erpAddress = erpAddress;
        this.erpPort = erpPort;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Verificar se a conexão com ERP está ativa, senão esperar x segundos e verificar outra vez até estar ativa
                if (!isConnectedERP(erpAddress, erpPort)) {
                    System.out.println("Conexão ao ERP perdida, tentando reconectar...");
                    Thread.sleep(RECONNECT_INTERVAL);
                }

                // Quando a conexão estiver ativa, guardar os valores da database
                if (isConnectedERP(erpAddress, erpPort)) {
                    System.out.println("Reconectado ao ERP com sucesso.");
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

    public static boolean isConnectedERP(String address, int port) {
        try (Socket socket = new Socket()) {
            System.out.println("Tentando conectar ao ERP em " + address + ":" + port);
            socket.connect(new InetSocketAddress(address, port), 2000);
            System.out.println("Conectado");
            return true;
        } catch (IOException e) {
            System.out.println("Não conectado: " + e.getMessage());
            return false;
        }
    }

    private void processOrdersAfterReconnection() {
        try {
            ResultSet rs = DatabaseERP.getAllOrders();
            while (rs.next()) {
                String nameID = rs.getString("nameid");
                int orderNumber = rs.getInt("ordernumber");
                String workPiece = rs.getString("workpiece");
                int quantity = rs.getInt("quantity");
                int dueDate = rs.getInt("duedate");
                double latePenalty = rs.getDouble("latepen");
                double earlyPenalty = rs.getDouble("earlypen");
                double orderCost = rs.getDouble("ordercost");
                int startDate = rs.getInt("startdate");
                int endDate = rs.getInt("enddate");
                boolean sendedMes = rs.getBoolean("sendedmes");

                // Processar a ordem (exemplo, adicionar ao sistema de ordens)
                if(sendedMes==false){
                    OrderDb order = new OrderDb(nameID, orderNumber, workPiece, quantity, dueDate, latePenalty, earlyPenalty, orderCost, startDate, endDate, sendedMes);
                    OrderSystemDb.addOrderDb(order);
                }
                //System.out.println("Ordem recuperada: " + ordernumber);
            }
        } catch (SQLException e) {
            System.err.println("Falha ao recuperar ordens após a reconexão: " + e.getMessage());
        }
    }
    
}
