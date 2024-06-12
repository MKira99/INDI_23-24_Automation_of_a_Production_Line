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
                         "  <Client NameId=\"Client AB\"/>\n" +
                         "  <Order Number=\"18\" WorkPiece=\"P5\" Quantity=\"4\" DueDate=\"5\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"19\" WorkPiece=\"P6\" Quantity=\"4\" DueDate=\"5\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"20\" WorkPiece=\"P7\" Quantity=\"4\" DueDate=\"5\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                         "  <Order Number=\"21\" WorkPiece=\"P9\" Quantity=\"2\" DueDate=\"10\" LatePen=\"20\" EarlyPen=\"5\"/>\n" +
                        
                         "</DOCUMENT>";
                         
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