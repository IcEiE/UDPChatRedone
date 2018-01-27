package Commons;

public class MessageHandler {
	private final String clientWhoSent;
	private final int messageID;
	private final String commando;
	private final String message;
	
	public MessageHandler(byte[] messageBytes) {
		String[] arr = new String(messageBytes).split("::", 3);
		clientWhoSent = arr[0];
		messageID = Integer.parseInt(arr[1]);
		String[] typeAndMessage = arr[2].split("\\s+", 2);
		commando = typeAndMessage[0];
		message = typeAndMessage[1];
	}
	
	public String getSender() {
		return clientWhoSent;
	}
	
	public int getMessageID() {
		return messageID;
	}
	
	public String getCommando() {
		return commando;
	}
	
	public String getMessageAsString() {
		return message;
	}
	
	public byte[] getWholeMessageAsBytes() {
		return (clientWhoSent + "::" + messageID + "::" + commando + "::" + message).getBytes();
	}
}
