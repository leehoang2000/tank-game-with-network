package client.message;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class AckMessage extends Message {
	
	public AckMessage(DatagramSocket senderSocket, InetSocketAddress destination, String data, int roomID) throws SocketException, UnknownHostException
	{
		super(senderSocket, destination);
		this.data = ACK + DELIMITER + roomID;
	}

}
