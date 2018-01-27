/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UDPChat.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

import Commons.MessageHandler;
import Commons.PacketHandler;
import Commons.PostOffice;

/**
 *
 * @author brom
 */
public class ServerConnection {

	// Artificial failure rate of 30% packet loss
	static double TRANSMISSION_FAILURE_RATE = 0.0;

	private DatagramSocket m_socket = null;
	private InetAddress m_serverAddress = null;
	private int m_serverPort = -1;
	private PacketHandler packetHandler = new PacketHandler();
	private int messageID = 0;

	// Construtor sets up variables to enable connection between the client and
	// server.
	public ServerConnection(String hostName, int port) {
		m_serverPort = port;
		m_serverAddress = createInetAddress(hostName);
		m_socket = createDatagramSocket();
	}

	public boolean handshake(String name) {
		// TODO:
		// * marshal connection message containing user name
		// * send message via socket
		// * receive response message from server
		// * unmarshal response message to determine whether connection was successful
		// * return false if connection failed (e.g., if user name was taken)

		sendChatMessage("/connect", name);
		receiveChatMessage().getBytes();

		return true;
	}

	/*
	 * Method used to receive new messages and return it. If a message is old 
	 * it will loop again.
	 */
	public String receiveChatMessage() {
		DatagramPacket dp = getDatagramToReceive();
		MessageHandler mh = null;
		do {
			try {
				m_socket.receive(dp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new MessageHandler(dp.getData());
		}while(!packetHandler.isANewMessage(mh.getSender(), mh.getMessageID()));
		
		packetHandler.markPacketAsReceived(mh.getSender(), mh.getMessageID());
		return mh.getMessageAsString();
	}

	/*method that is used to send message a number of times through the socket but only if a random value exceeds 
	* the transmission failure rate.
	*/
	public void sendChatMessage(String message, String name) {
		MessageHandler mh = new MessageHandler(formatMessageForSend(message, name).getBytes());
		System.out.println(mh.getMessageAsString());
		Random generator = new Random();
		double failure = generator.nextDouble();
		for (int i = 0; i < 10; ++i) {
			if (failure > TRANSMISSION_FAILURE_RATE) {
				try {
					m_socket.send(getDatagramToSend(mh.getWholeMessageAsBytes()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		++messageID;
	}

	/*
	 * -----------------------------------------------------------------------------
	 * -- Private functions used by the different public methods that enable the
	 * functionality of the program
	 * -----------------------------------------------------------------------------
	 * --
	 */
	// Used to return a String name as InetAddress, if exception is thrown NULL is
	// returned.
	private InetAddress createInetAddress(String hostName) {
		try {
			InetAddress address = InetAddress.getByName(hostName);
			return address;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// Returns a new DatagramSocket
	private DatagramSocket createDatagramSocket() {
		try {
			DatagramSocket socket = new DatagramSocket();
			return socket;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Method used to get a DatagramPacket that is ready to send. 
	 */
	private DatagramPacket getDatagramToSend(byte[] message) {
		DatagramPacket packetToSend = new DatagramPacket(message, message.length, m_serverAddress, m_serverPort);
		return packetToSend;
	}

	private DatagramPacket getDatagramToReceive() {
		byte[] buffer = new byte[1024];
		DatagramPacket receiveablePacket = new DatagramPacket(buffer, buffer.length);
		return receiveablePacket;
	}

	private String formatMessageForSend(String message, String name) {
		return name + "::" + messageID + "::" + message;
	}

}
