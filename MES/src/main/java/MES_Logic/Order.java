package MES_Logic;
public class Order {
    public int OrderID = 0;
    public int PieceNr = 0;
    public int Quantity = 0;
    public int ArrivingDay = 0;
    public int StartingDay = 0;


    public int OrderID() {
        return OrderID;}
    public int PieceNr() {
        return PieceNr;
    }
    public int Quantity() {
        return Quantity;
    }

    public int ArrivingDay() {
        return ArrivingDay;
    }

    public int StartingDay(){
        return  StartingDay;
    }


    public void setOrderID(int OrderID){this.OrderID = OrderID;}
    public void setPieceNr(int PieceNr) {
        this.PieceNr = PieceNr;
    }
    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }
    public void setArrivingDay(int ArrivingDay) {
        this.ArrivingDay = ArrivingDay;
    }
    public void setStartingDay(int StartingDay) {
        this.StartingDay = StartingDay;
    }




    public int getOrderID(){return  this.OrderID();}
    public int getPieceNr() {
        return this.PieceNr;
    }
    public int getQuantity() {
        return this.Quantity;
    }
    public int getArrivingDay() {
        return this.ArrivingDay;
    }
    public int getStartingDay() {
        return this.StartingDay;
    }



    @Override
    public String toString() {
        return "order{" +
                "OrderID='" + OrderID + '\'' +
                ", PieceNr=" + PieceNr +
                ", Quantity=" + Quantity +
                ", ArrivingDay=" + ArrivingDay +
                ", StartingDay=" + StartingDay +
                '}';
    }

}
