
import MES_Logic.Threader;
import OPC.opcua;


public class Main extends Thread {

    public static void main(String[] args) throws Exception {
        opcua.connect("opc.tcp://LAPTOP-2VCQGOJI:4840");

        Thread VarPLC = new Thread(new Threader.VariablesReadRunnable());
        Thread DayUpdate = new Thread(new Threader.DayUpdateRunnable());
        Thread AcceptOrderERP = new Thread(new Threader.AcceptOrderRunnable());
        
        VarPLC.start();
        DayUpdate.start();
        AcceptOrdersERP.start();
    }
}
