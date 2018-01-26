package Commons;

public class MessageHandler {
	private final String clientWhoSent;
	private final String messageID;
	private final String commando;
	private final String message;
	
	public MessageHandler(byte[] messageBytes) {
		String[] arr = new String(messageBytes).split("::", 4);
		clientWhoSent = arr[0];
		messageID = arr[1];
		commando = arr[2];
		message = arr[4];
	}
	
	public String getSender() {
		return clientWhoSent;
	}
	
	public String getMessageID() {
		return messageID;
	}
	
	public String getCommando() {
		return commando;
	}
	
	public String getMessageAsString() {
		return message;
	}
	
	public byte[] getMessageAsBytes() {
		return (clientWhoSent + "::" + messageID + "::" + commando + "::" + message).getBytes();
	}
}
