package client.message;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ClientSideSender {

	private static ClientSideSender singleton;
	
	private DatagramSocket clientSocket;
	
	private ClientSideSender() throws SocketException
	{
		clientSocket = new DatagramSocket();
	}
	
	public static ClientSideSender singleton() throws SocketException
	{
		if(singleton == null)
			singleton = new ClientSideSender();
		return singleton;
	}
	
	public static void init() throws SocketException
	{
		
	}
	
	public void sendRequestConnectMessage() throws IOException
	{
		RequestConnectMessage rcm = new RequestConnectMessage();
		rcm.send(clientSocket);
	}
	
	public DatagramSocket getClientSocket()
	{
		return clientSocket;
	}
	
}
