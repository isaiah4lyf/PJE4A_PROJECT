package SSS_SERVER_CLASSES_FOR_WEB_SERVICE;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Send_Packets_UDP {
	public Send_Packets_UDP()
	{
        DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(9876);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true)
        {
        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
            	System.out.println("Ready to receive");
            	serverSocket.receive(receivePacket);
                String sentence = new String( receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);
                  
                  
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                System.out.println(sendPacket.toString());
                serverSocket.send(sendPacket);
            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
              
         }
  
	}
}
