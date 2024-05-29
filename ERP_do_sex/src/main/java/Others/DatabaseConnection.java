package Others;

//Para compilar: javac -cp "postgresql-42.7.3.jar." src/main/java/Others/DatabaseConnection.java
//Para dar run: java -cp "postgresql-42.7.3.jar." src/main/java/Others/DatabaseConnection.java

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static Connection connection;
    static String databaseUrl = "jdbc:postgresql://db.fe.up.pt:5432/infind202415";
    //static String user = "infind202419";
    //static String password = "m6Fhd32pLt";
    static String user = "infind202415";
    //static String password = "DedGdpdjej";
    static String password = "fi3Qo0ilr8";
    public static void main(String[] args)throws SQLException {
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
}