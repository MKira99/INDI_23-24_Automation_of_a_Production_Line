//import java.net.InetAddress;

import Others.*;

public class Main extends Thread {

    public static void main(String[] args) throws Exception {

        /*InetAddress inetAddress = InetAddress.getLocalHost();
        String erpAddress = inetAddress.getHostAddress();
        int erpPort = 4999; // Porta do ERP

        

        if(!DatabaseERP.isTableEmpty()){
            Thread connectionMonitor = new Thread(new ERPConnectionMonitor(erpAddress, erpPort));
            connectionMonitor.start();
        }*/
        Thread ERPGUI = new Thread(new Threader.GUI());
        Thread XMLReceive = new Thread(new Threader.UDPServer());
        Thread TCPReceive = new Thread(new Threader.TCPServer());
        
        ERPGUI.start();
        XMLReceive.start();
        TCPReceive.start();

    }
}