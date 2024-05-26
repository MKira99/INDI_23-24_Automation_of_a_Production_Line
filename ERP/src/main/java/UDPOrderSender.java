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
                         "  <Order Number=\"14\" WorkPiece=\"P9\" Quantity=\"5\" DueDate=\"10\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"15\" WorkPiece=\"P5\" Quantity=\"8\" DueDate=\"7\" LatePen=\"10\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"16\" WorkPiece=\"P6\" Quantity=\"1\" DueDate=\"14\" LatePen=\"10\" EarlyPen=\"10\"/>\n" +
                         "  <Order Number=\"17\" WorkPiece=\"P6\" Quantity=\"1\" DueDate=\"14\" LatePen=\"10\" EarlyPen=\"10\"/>\n" +
                         "  <Order Number=\"18\" WorkPiece=\"P6\" Quantity=\"1\" DueDate=\"14\" LatePen=\"10\" EarlyPen=\"10\"/>\n" +
                         
                         
                         "</DOCUMENT>";
                        //OrderID :="AA_42"
        try {
            byte[] sendData = xmlData.getBytes();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 12345;

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