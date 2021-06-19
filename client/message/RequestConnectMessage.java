package client.message;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RequestConnectMessage extends Message {
	
	public RequestConnectMessage(DatagramSocket senderSocket, InetSocketAddress destination) throws SocketException, UnknownHostException
	{
		super(senderSocket, destination);
		this.data = REQUEST_CONNECT + DELIMITER;
	}
}
