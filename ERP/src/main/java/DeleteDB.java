//import java.net.InetAddress;

import Others.*;

public class DeleteDB extends Thread {

    public static void main() throws Exception {

        DatabaseERP.truncateTable();
        DatabaseERP.truncateTableHistory();

    }
}