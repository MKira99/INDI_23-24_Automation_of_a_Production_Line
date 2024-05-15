package MAIN;
import MES_Logic.Threader;
import OPC.opcua;


public class MES_PLC_OPC extends Thread {

    public static void main(String[] args) throws Exception {
        opcua.connect("opc.tcp://LAPTOP-2VCQGOJI:4840");

        Thread TimersThread = new Thread(new Threader.VariablesReadRunnable());
        Thread dayUpdate = new Thread(new Threader.DayUpdateRunnable());
        Thread AcceptOrderRunnable = new Thread(new Threader.AcceptOrderRunnable());
        

        dayUpdate.start();
        TimersThread.start();
        AcceptOrderRunnable.start();
    }
}
