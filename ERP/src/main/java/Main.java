import Others.*;

public class Main extends Thread {

    public static void main(String[] args) throws Exception {

        Thread XMLReceive = new Thread(new Threader.UDPServer());
        Thread TCPSend = new Thread(new Threader.TCPServer());
        
        XMLReceive.start();
        TCPSend.start();
    }
}