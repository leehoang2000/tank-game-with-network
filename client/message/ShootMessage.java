package client.message;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ShootMessage extends Message{
	
	public ShootMessage(DatagramSocket senderSocket, InetSocketAddress destination, int playerID, int roomID) 
			throws SocketException, UnknownHostException {
		super(senderSocket, destination);
		this.data = SHOOT + DELIMITER + roomID + DELIMITER + playerID;
	}
}
