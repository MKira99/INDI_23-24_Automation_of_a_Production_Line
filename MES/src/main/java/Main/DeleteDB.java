package Main;

import DataBase.*;

public class DeleteDB extends Thread {

    public static void main(String[] args) throws Exception {

        DatabaseMES.truncateTable();

    }
}