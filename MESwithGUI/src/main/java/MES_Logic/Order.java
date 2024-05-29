package MES_Logic;
public class Order {
    private String OrderID = "";
    private String PieceType = "";
    private short Quantity = 0;
    private short StartDay = 0;
    private short EndDay = 0;

    public Order(String OrderID, String PieceNr, short Quantity, short StartDay, short EndDay) {
        this.OrderID = OrderID;
        this.PieceType = PieceNr;
        this.Quantity = Quantity;
        this.StartDay = StartDay;
        this.EndDay = EndDay;
    }

    public String getOrderID() {
        return OrderID;
    }
    public void setOrderID(String OrderID) {
        this.OrderID = OrderID;
    }

    public String getPieceType() {
        return PieceType;
    }
    public void setPieceType(String PieceType) {
        this.PieceType = PieceType;
    }

    public short getQuantity() {
        return Quantity;
    }
    public void setQuantity(short Quantity) {
        this.Quantity = Quantity;
    }

    public short getStartDay() {
        return StartDay;
    }
    public void setStartDay(short StartDay) {
        this.StartDay = StartDay;
    }

    public short getEndDay() {
        return EndDay;
    }
    public void setEndDay(short EndDay) {
        this.EndDay = EndDay;
    }
}
