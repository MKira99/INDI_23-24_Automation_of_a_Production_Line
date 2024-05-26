import Others.*;

public class Main extends Thread {

    public static void main(String[] args) throws Exception {

        Thread XMLReceive = new Thread(new Threader.UDPServer());
        Thread ERPGUI = new Thread(new Threader.GUI());
        Thread TCPReceive = new Thread(new Threader.TCPServer());
        
        XMLReceive.start();
        TCPReceive.start();
        ERPGUI.start();
    }
}