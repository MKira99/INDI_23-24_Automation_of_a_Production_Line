import Others.*;

public class Main extends Thread {

    public static void main(String[] args) throws Exception {

        Thread ERPGUI = new Thread(new Threader.GUI());
        Thread XMLReceive = new Thread(new Threader.UDPServer());
        Thread TCPReceive = new Thread(new Threader.TCPServer());
        
        ERPGUI.start();
        XMLReceive.start();
        TCPReceive.start();

        

    }
}