//import java.net.InetAddress;

import Others.*;

public class DeleteDB extends Thread {

    public static void main(String[] args) throws Exception {

        DatabaseERP.truncateTable();

    }
}