package Others;

// Para compilar: javac -cp "postgresql-42.7.3.jar." src/main/java/Others/DatabaseERP.java

import java.sql.*;


public class DatabaseERP {

    static Statement stmt;
    static ResultSet rs;
    static Connection connection;
    static String databaseUrl = "jdbc:postgresql://db.fe.up.pt:5432/infind202415";
    //static String user = "infind202419";
    //static String password = "m6Fhd32pLt";
    static String user = "infind202415";
    //static String password = "DedGdpdjej";
    static String password = "fi3Qo0ilr8";
    static String ordersactiveTable = "ordersactive";
    static String ordersfinishedTable = "ordersfinished";
    static String piecesTable = "pieces";


    public static void databaseConnection(String databaseUrl, String user, String password) throws SQLException {
        try {
            // Carregar o driver explicitamente (opcional)
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(databaseUrl, user, password);
            System.out.println("Connection: " + connection);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver não encontrado. Certifique-se de que o driver JDBC do PostgreSQL está no classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            throw e;
        }
    }

    // Method to check if the connection is active
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to reconnect and retrieve all orders
    /*public static void reconnectAndRetrieveOrders() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (isConnected()) {
                        ResultSet rs = getAllOrders();
                        List<Order> orders = new ArrayList<>();
                        while (rs.next()) {
                            Order order = new Order(
                                rs.getString("nameid"),
                                rs.getInt("ordernumber"),
                                rs.getString("workpiece"),
                                rs.getInt("quantity"),
                                rs.getInt("duedate"),
                                rs.getInt("latepen"),
                                rs.getInt("earlypen")
                            );
                            orders.add(order);
                        }
                        // Add orders to the system
                        for (Order order : orders) {
                            OrderSystem.addOrder(order);
                        }
                        System.out.println("Orders recovered and added to the system");
                        timer.cancel(); // Cancel the timer if reconnection is successful
                    }
                } catch (SQLException e) {
                    System.err.println("Reconnection failed, will retry...");
                }
            }
        }, 0, 5000); // Retry every 5 seconds
    }*/

    public static int newEntry(String SQLQuery, String databaseUrl, String user, String password) throws SQLException {

        databaseConnection(databaseUrl,user,password);

        try{
            stmt = connection.createStatement();
            int i = stmt.executeUpdate(SQLQuery);
            connection.close();
            return i;

        }catch(Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return -1;
        }
    }

    // Create method insertOrder
    public static int insertOrder(String orderId, String orderNumber, String nameID, String workPiece, String quantity, String dueDate, String latePenalty, String earlyPenalty) throws SQLException {
        String SQLQuery = "INSERT INTO ERP." + ordersactiveTable + " (orderid, nameid, ordernumber, workpiece, quantity, duedate, latepen, earlypen, sendedmes) VALUES ('" + orderId + "','" + nameID + "', " + orderNumber + ", '" + workPiece + "', " + quantity + ", " + dueDate + ", " + latePenalty + ", " + earlyPenalty + ", " + false + ");";
        System.out.println(SQLQuery);
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

    public static int insertChangedData(double orderCost, int startDate, int endDate, String orderId) throws SQLException {
        String SQLQuery = "UPDATE ERP." + ordersactiveTable + " SET ordercost = " + orderCost + ", startdate = " + startDate + ", enddate = " + endDate + " WHERE orderid = '" + orderId +"';";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

    /*public static int insertOrderCost(double orderCost, int orderNumber) throws SQLException {
        String SQLQuery = "UPDATE ERP." + ordersactiveTable + " SET ordercost = " + orderCost + " WHERE ordernumber = " + orderNumber + ";";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

    public static int insertFinaltDate(String endDate, int orderNumber) throws SQLException {
        String SQLQuery = "UPDATE ERP." + ordersactiveTable + " SET enddate = " + endDate + " WHERE ordernumber = " + orderNumber + ";";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }*/

    public static int updateSendedMes(String orderId) throws SQLException {
        String SQLQuery = "UPDATE ERP." + ordersactiveTable + " SET sendedmes = " + true + " WHERE orderid = '" + orderId + "';";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

    /*public static int insertPiece(String pieceName, String rawPiece, int orderNumber, double rawCost) throws SQLException {
        String SQLQuery = "INSERT INTO ERP." + piecesTable + " (piecetype, rawpiece, orderid, currenttype, rawcost) VALUES ('" + pieceName + "', '" + rawPiece + "', " + orderNumber + ", '" + rawPiece + "', " + rawCost + ");";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }*/

    public static ResultSet getPieceByOrderNumber(String orderNumber) {
        ResultSet resultSet = null;
        try {
            Connection connection = DriverManager.getConnection(databaseUrl, user, password);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String SQLQuery = "SELECT * FROM ERP.pieces WHERE orderid = '" + orderNumber + "' AND dispatchdate IS NOT NULL;";
            resultSet = statement.executeQuery(SQLQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getAllOrders() throws SQLException {
        ResultSet resultSet = null;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection(databaseUrl, user, password);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String SQLQuery = "SELECT * FROM ERP.ordersactive;";
            resultSet = statement.executeQuery(SQLQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to handle it in the calling method
        } finally {
            // Note: Do not close connection and statement here because we need the ResultSet
        }
        return resultSet;
    }

    public static int getResultSetSize(ResultSet resultSet) {
        int size = 0;
        try {
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static boolean isTableEmpty() throws SQLException {
        boolean isEmpty = false;
        String SQLQuery = "SELECT COUNT(*) AS rowcount FROM ERP.ordersactive;";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(databaseUrl, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQLQuery);

            if (resultSet.next()) {
                int rowCount = resultSet.getInt("rowcount");
                if (rowCount == 0) {
                    isEmpty = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return isEmpty;
    }

    public static String getUser() {
        return user;
    }

    public static String getDatabaseUrl() {
        return databaseUrl;
    }

    public static String getPassword() {
        return password;
    }
    
    public static String getOrdersActiveTable() {
        return ordersactiveTable;
    }
    
    public static String getOrdersFinishedTable() {
        return ordersfinishedTable;
    }
}