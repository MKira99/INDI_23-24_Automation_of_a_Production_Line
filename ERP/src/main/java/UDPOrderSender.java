import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPOrderSender {
    public static void main(String[] args) {
        String xmlData = "<?xml version=\"1.0\"?>\n" +
                         "<!DOCTYPE PRODUCTION_ORDERS [\n" +
                         "<!ELEMENT DOCUMENT (Client, Order*)>\n" +
                         "<!ATTLIST Client\n" +
                         "          NameId    CDATA #REQUIRED\n" +
                         ">\n" +
                         "<!ATTLIST Order\n" +
                         "          Number    CDATA #REQUIRED\n" +
                         "          WorkPiece CDATA #REQUIRED\n" +
                         "          Quantity  CDATA #REQUIRED\n" +
                         "          DueDate   CDATA #REQUIRED\n" +
                         "          LatePen   CDATA #REQUIRED\n" +
                         "          EarlyPen  CDATA #REQUIRED\n" +
                         ">\n" +
                         "]>\n" +
                         "<DOCUMENT>\n" +
                         "  <Client NameId=\"Client AA\"/>\n" +
                         "  <Order Number=\"14\" WorkPiece=\"P2\" Quantity=\"10\" DueDate=\"4\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"15\" WorkPiece=\"P3\" Quantity=\"10\" DueDate=\"7\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"16\" WorkPiece=\"P4\" Quantity=\"10\" DueDate=\"10\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"17\" WorkPiece=\"P5\" Quantity=\"10\" DueDate=\"13\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"18\" WorkPiece=\"P6\" Quantity=\"10\" DueDate=\"16\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"19\" WorkPiece=\"P7\" Quantity=\"10\" DueDate=\"19\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"20\" WorkPiece=\"P8\" Quantity=\"10\" DueDate=\"22\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"21\" WorkPiece=\"P9\" Quantity=\"10\" DueDate=\"25\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         
                         
                         
                         "</DOCUMENT>";
                        //OrderID :="AA_42"
        try {
            byte[] sendData = xmlData.getBytes();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 24680;

            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            socket.send(packet);
            System.out.println("Order sent successfully!\n" + packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
}