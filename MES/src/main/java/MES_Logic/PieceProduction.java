package MES_Logic;

import MAIN.MES_DB_SQL;

import java.util.Comparator;
import java.util.List;


public class PieceProduction {
    public static int pieceNr;
    public static int orderID;

    public static int quantity;
    public static int quantity2;
    public static int startingDay;
    public static int arrivingDay;
    public static int arrivingDay2;
    public static Order firstorder;
    public static Order secondorder;
    public static int Flag; //Useless


    public static int Daytogo = 0;
    public static int onhold;

    static String ArmazemCount = "Variant{value=0}";

    public static void startPieceProduction(){
        try {
            List<Order> OrderToProduce = MES_DB_SQL.getOrders(); //Apanha 2 ordens c dias mais baixos
            OrderToProduce.sort(Comparator.comparingInt(Order::getStartingDay)); //Confirma se vieram ordenadas

            if(OrderToProduce.isEmpty()){
                return;
            }else {
                int numOrders = OrderToProduce.size();
                int maxOrders = 2; // Maximum number of orders to process
                if (numOrders >= maxOrders) {
                    firstorder = OrderToProduce.get(0);     // Primeira ordem a começar
                    secondorder = OrderToProduce.get(1);    // Segunda ordem a começar
                } else if (numOrders == 1) {
                    firstorder = OrderToProduce.get(0);     // Only order available
                    secondorder = null;                     // Set second order to null
                }
            }
        while (onhold == 0) {
            if (MES_DB_SQL.isOrdercompleted() || Threader.DayTimer.getDay() == 0) {
                //Se flag false fica preso num loop
                onhold = 1;
                if (secondorder == null){
                    orderID = firstorder.getOrderID();
                    quantity = firstorder.getQuantity();
                    pieceNr = firstorder.getPieceNr();
                    arrivingDay = firstorder.getArrivingDay();
                    startingDay = firstorder.getStartingDay();
                }else

                {
                    orderID = firstorder.getOrderID();
                    quantity = firstorder.getQuantity();
                    pieceNr = firstorder.getPieceNr();
                    arrivingDay = firstorder.getArrivingDay();
                    startingDay = firstorder.getStartingDay();
                    arrivingDay2 = secondorder.ArrivingDay();
                    quantity2 = secondorder.Quantity;
                }
                for (Order ordem : OrderToProduce) {
                    System.out.println(ordem);
                }
                    PieceProduction pieceProduction = new PieceProduction();
                    pieceProduction.RealstartPieceProduction();
                    break;
            }break;
        }
            //RealstartPieceProduction();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public  void RealstartPieceProduction() throws Exception {
        int loop = 0;
        while(loop == 0){
           if ((startingDay-Threader.DayTimer.getDay())== 0 || Threader.DayTimer.getDay() > startingDay) {  //É igual
                    loop = 1;
                    MES_DB_SQL.setFlagFalse(); //Ordem a ser processada, METO FLAG A FALSE
                    System.out.println("Order To Produce " + firstorder);
                    MES_DB_SQL.orderProcessed(); //Coloco na db "ORDER" status a 2 (EM PROCESSAMENTO)
                    MES_DB_SQL.orderinterface(); //Coloco na db "PRODUCEDORDER"  p/interface
                    Piece.ProducePiece();//Começo o programa
                }
        }

}
}