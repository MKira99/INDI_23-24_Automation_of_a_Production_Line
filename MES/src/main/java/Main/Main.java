package Main;
import MES_Logic.*;
import OPC.opcua;


public class Main extends Thread {

    public static void main(String[] args) throws Exception {

        DeleteDB.main(args);
        
        opcua.connect("opc.tcp://LAPTOP-2VCQGOJI:4840");

        Thread VarPLC = new Thread(new Threader.VariablesReadRunnable());
        Thread DayUpdate = new Thread(new Threader.DayUpdateRunnable());
        Thread AcceptOrderERP = new Thread(new Threader.AcceptOrderRunnable());
        Thread GUIMES = new Thread(new Threader.GUI());
        
        VarPLC.start();
        DayUpdate.start();
        AcceptOrderERP.start();
        GUIMES.start();
        
    }
}