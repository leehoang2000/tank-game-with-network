package client.message;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class PingMessage extends Message {
	
	public PingMessage(DatagramSocket senderSocket, InetSocketAddress destination, int roomID) throws SocketException, UnknownHostException
	{
		super(senderSocket, destination);
		this.data = String.valueOf(PING) + DELIMITER + String.valueOf(roomID);
	}

}
