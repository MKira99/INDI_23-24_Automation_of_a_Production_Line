package MAIN;
import MES_Logic.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MES_DB_SQL {
    private static boolean ordercompleted = false;
    public static MillisTimer ConnectingDatabase;

    public static void deleteDB() throws SQLException {
        Connection con;
        PreparedStatement pstmt;

        try {
        con = DriverManager.getConnection("jdbc:postgresql://10.227.240.130:5432/up202200600", "up202200600", "v7Tn2Rtkv");

        String sql1 = "DELETE FROM \"Order\" ";
        pstmt = con.prepareStatement(sql1);
        pstmt.executeUpdate();

        String sql2 = "DELETE FROM \"ProducedOrder\"";
        pstmt = con.prepareStatement(sql2);
        pstmt.executeUpdate();

        System.out.println("DB Deleted successfully.");
    }catch(Exception ex){
        System.out.println(ex);
    }
    }
    public static void insertOrder(Order Order1) {
        Connection con;
        PreparedStatement pstmt;

        try {
            con = DriverManager.getConnection("jdbc:postgresql://10.227.240.130:5432/up202200600", "up202200600", "v7Tn2Rtkv");

            String sql = "INSERT INTO \"Order\" (orderID,piecenr,quantity,arrivingDay, status, startingDay,conclusionDay) Values(?,?,?,?,?,?,?)";

            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Order1.getOrderID()); // set OrderNr}
            pstmt.setInt(2, Order1.getPieceNr()); // set Piece Nr
            pstmt.setInt(3, Order1.getQuantity()); // set Quantity of Pieces
            pstmt.setInt(4, Order1.getArrivingDay()); //set Day of delivery
            pstmt.setInt(5, 1);
            pstmt.setInt(6, Order1.getStartingDay());
            pstmt.setInt(7,0);
            pstmt.executeUpdate();

            }catch(Exception ex){
                System.out.println(ex);
            }
    }

    public static void orderinterface() {
        Connection con;
        PreparedStatement pstmt;
        try {
            con = DriverManager.getConnection("jdbc:postgresql://10.227.240.130:5432/up202200600", "up202200600", "v7Tn2Rtkv");
            String sql = "INSERT INTO \"ProducedOrder\" (orderid,startingday,currentday, timept5,timept6, timest5,timest3,mediumtimepiece,ordercompleted) Values(?,?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, PieceProduction.orderID);
            pstmt.setInt(2, PieceProduction.startingDay);
            pstmt.setInt(3, Threader.DayTimer.getDay());
            pstmt.setInt(4, Piece.timePT5);
            pstmt.setInt(5, Piece.timePT6);
            pstmt.setInt(6, Piece.timeST5);
            pstmt.setInt(7, Piece.timeST3);
            pstmt.setInt(8, Piece.TotalPieceTime);
            pstmt.setInt(9, 2);
            pstmt.executeUpdate();

        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
    public static void orderProcessed(){
        Connection con;
        PreparedStatement pstmt;

        try {
            con = DriverManager.getConnection("jdbc:postgresql://10.227.240.130:5432/up202200600", "up202200600", "v7Tn2Rtkv");

            String sql = "UPDATE  \"Order\" SET Status = ? WHERE Orderid = ?  ";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,2); //New Status value
            pstmt.setInt(2, PieceProduction.orderID);
            pstmt.executeUpdate();
            pstmt.executeUpdate();
        }catch(Exception ex){
            System.out.println(ex);
        }

    }
    public static void orderCompleted(){ //Mudar para ordem completa
        Connection con;
        PreparedStatement pstmt;
        try {
            con = DriverManager.getConnection("jdbc:postgresql://10.227.240.130:5432/up202200600", "up202200600", "v7Tn2Rtkv");

            String sql = "UPDATE  \"Order\" SET Status = ? , Conclusionday = ?  WHERE Orderid = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,3); //New Status value
            pstmt.setInt(2,Piece.Conclusionday);
            pstmt.setInt(3, PieceProduction.orderID);
            pstmt.executeUpdate();


        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public static void ProducedOrderCompleted(){
    Connection con;
        PreparedStatement updatepstmt;
        try {
            con = DriverManager.getConnection("jdbc:postgresql://10.227.240.130:5432/up202200600", "up202200600", "v7Tn2Rtkv");

            String sql = "UPDATE  \"ProducedOrder\" SET Ordercompleted = ? , Startingday = ? , Currentday = ? , Timept5 = ? , Timept6 = ? , Timest5 = ? , Timest3 = ? , Mediumtimepiece = ?, Pecascompletas = ?  WHERE (Orderid,Ordercompleted) = (?,?)  ";
            updatepstmt = con.prepareStatement(sql);;
            updatepstmt.setInt(1, 3); //New ordercompleted value
            updatepstmt.setInt(2, +PieceProduction.startingDay); //NewStartingday value
            updatepstmt.setInt(3, +Piece.Conclusionday); //New currentday value
            updatepstmt.setInt(4, +Piece.timePT5); //New timept5
            updatepstmt.setInt(5, +Piece.timePT6);//New timept6
            updatepstmt.setInt(6, +Piece.timeST5); //New timest5
            updatepstmt.setInt(7, +Piece.timeST3); //New timest3
            updatepstmt.setInt(8, +Piece.TotalPieceTime); //medium time
            updatepstmt.setInt(9, +PieceProduction.quantity); //replace with quantidade de peças
            updatepstmt.setInt(10, PieceProduction.orderID);  // previous orderid
            updatepstmt.setInt(11, 2);// Existing Status value
            updatepstmt.executeUpdate();

        }catch(Exception ex){
            System.out.println(ex);
        }

    }
    public static void UnloadedPieces(int pecascompletas){
        Connection con;
        PreparedStatement updatepstmt;
        try {
            con = DriverManager.getConnection("jdbc:postgresql://10.227.240.130:5432/up202200600", "up202200600", "v7Tn2Rtkv");

            String sql = "UPDATE  \"ProducedOrder\" SET  Currentday = ? , Timept5 = ? , Timept6 = ? , Timest5 = ? , Timest3 = ? , Pecascompletas = ?  WHERE (Orderid,Ordercompleted) = (?,?)  ";
            updatepstmt = con.prepareStatement(sql);;
            updatepstmt.setInt(1, +Piece.Conclusionday); //NewStartingday value
            updatepstmt.setInt(2, +Piece.timePT5); //New timept5
            updatepstmt.setInt(3, +Piece.timePT6);//New timept6
            updatepstmt.setInt(4, +Piece.timeST5); //New timest5
            updatepstmt.setInt(5, +Piece.timeST3); //New timest3
            updatepstmt.setInt(6, +pecascompletas); //medium time
            updatepstmt.setInt(7, PieceProduction.orderID);  // previous orderid
            updatepstmt.setInt(8, 2);// Existing Status value
            updatepstmt.executeUpdate();

        }catch(Exception ex){
            System.out.println(ex);
        }

    }

    public static void deleteOrder() {
        Connection con;
        PreparedStatement pstmt;

        try {
            con = DriverManager.getConnection("jdbc:postgresql://10.227.240.130:5432/up202200600", "up202200600", "v7Tn2Rtkv");

                String sql = "DELETE FROM \"Order\"  WHERE (Orderid ,Status) = (?,?)";
              //  String sql = "DELETE FROM \"ProducedOrder\" WHERE (OrderID, OrderCompleted) = (?,?)";
             //  String sql = "DELETE FROM \"ProducedOrder\" ";
                // String sql = "DELETE FROM \"Order\" ";
                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, +PieceProduction.orderID);
                pstmt.setInt(2, 1);
                pstmt.executeUpdate();


        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    public static int getNextOrderNumber() {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;
        int nextOrderNumber = 1;

        try {
            con = DriverManager.getConnection("jdbc:postgresql://10.227.240.130:5432/up202200600", "up202200600", "v7Tn2Rtkv");

            String sql = "SELECT MAX(OrderID) FROM \"Order\"";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                nextOrderNumber = rs.getInt(1) + 1;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return nextOrderNumber;
    }

    public static List<Order> getOrders() {

          List<Order> orders = new ArrayList<>();
            Order Order2 = null;
            Connection con = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                con = DriverManager.getConnection("jdbc:postgresql://10.227.240.130:5432/up202200600", "up202200600", "v7Tn2Rtkv");
                String sql = "SELECT * FROM  \"Order\" WHERE status = 1 ORDER BY startingDay LIMIT 2";

                statement = con.prepareStatement(sql);
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int orderID = resultSet.getInt("orderid");
                    int piece = resultSet.getInt("piecenr");
                    int quantity = resultSet.getInt("quantity");
                    int ArrivingDay = resultSet.getInt("arrivingday");
                    int StartingDay = resultSet.getInt("startingday");

                    Order2 = new Order();
                    Order2.setOrderID(orderID);
                    Order2.setPieceNr(piece);
                    Order2.setQuantity(quantity);
                    Order2.setArrivingDay(ArrivingDay);
                    Order2.setStartingDay(StartingDay);
                    orders.add(Order2);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return (orders);
        }

    public static boolean isOrdercompleted() {
    return ordercompleted;
    }


    public static void setNextOrder(){
        ordercompleted = true;
        }
    public static void setFlagFalse() {
        ordercompleted = false;}


}





