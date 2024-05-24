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
        static String ordersTable = "orders";
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
        

        public boolean isConnected() throws SQLException {
            try (Connection connection = DriverManager.getConnection(databaseUrl, user, password)) {
                return true; // Connection established if no exception
            } catch (SQLException e) {
                return false;
            }
        }

    /* To use databaseConnection
    Database db = new Database();
     try {
        db.databaseConnection(databaseUrl, user, password);
        // Connection logic would be inside the createConnection method (optional)
     } catch (SQLException e) {
        // Handle connection errors
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
    public static int insertOrder(String orderNumber, String workPiece, int quantity, int dueDate, double latePenalty, double earlyPenalty) throws SQLException {
        String SQLQuery = "INSERT INTO ERP." + ordersTable + " (ordernumber, workpiece, quantity, duedate, latepen, earlypen) VALUES ('" + orderNumber + "', '" + workPiece + "', " + quantity + ", " + dueDate + ", " + latePenalty + ", " + earlyPenalty + ");";
        System.out.println(SQLQuery);
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

    public static int insertOrderCost(double orderCost) throws SQLException {
        String SQLQuery = "UPDATE ERP." + ordersTable + " SET ordercost = "+ orderCost +" WHERE ordernumber = '124';";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

    public static int insertPiece(String pieceName, String rawPiece, int orderNumber, double rawCost) throws SQLException {
        String SQLQuery = "INSERT INTO ERP." + piecesTable + " (piecetype, rawpiece, orderid, currenttype, rawcost) VALUES ('" + pieceName + "', '" + rawPiece + "', " + orderNumber + ", '" + rawPiece + "', " + rawCost + ");";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

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
    public static String getUser() {
        return user;
    }

    public static String getDatabaseUrl() {
        return databaseUrl;
    }

    public static String getPassword() {
        return password;
    }
    public static String getTable() {
        return ordersTable;
    }
}
