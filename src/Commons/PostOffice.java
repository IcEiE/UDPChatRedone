package Commons;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class PostOffice {
	
	public DatagramPacket getDatagramToSend(String message, InetAddress serverAdress, int serverPort) {
		String stringToSend = message;
		byte[] bytedString = stringToSend.getBytes();
		DatagramPacket packetToSend = new DatagramPacket(bytedString, bytedString.length, serverAdress, serverPort);
		return packetToSend;
	}
	
	
	
}
