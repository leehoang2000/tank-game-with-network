package client.message;

public class MessageSendService extends Thread{
	protected ClientSideSender clientSideSender;

	public MessageSendService(ClientSideSender clientSideSender) {
		super();
		this.clientSideSender = clientSideSender;
	}
	 
	

}
