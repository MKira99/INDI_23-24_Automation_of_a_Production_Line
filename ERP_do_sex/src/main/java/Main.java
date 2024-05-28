import java.net.InetAddress;
import java.util.List;

import Others.*;
import Others.DataOrder.OrderResult;

public class Main extends Thread {

    public static void main(String[] args) throws Exception {

        List<OrderResult> ordersResult = Threader.UDPServer.processedOrders;
        InetAddress inetAddress = InetAddress.getLocalHost();
        String erpAddress = inetAddress.getHostAddress();
        int erpPort = 4999; // Porta do ERP

        if(!DatabaseERP.isTableEmpty()){
            Thread connectionMonitor = new Thread(new ERPConnectionMonitor(erpAddress, erpPort));
            connectionMonitor.start();
        }
        Thread ERPGUI = new Thread(new Threader.GUI());
        Thread XMLReceive = new Thread(new Threader.UDPServer());
        Thread TCPReceive = new Thread(new Threader.TCPServer());
        
        ERPGUI.start();
        XMLReceive.start();
        TCPReceive.start();
        
        
        if(ordersResult!=null){
            System.out.println("ordersResult\n\n\n" + ordersResult);
            //
        }

    }
}