package client.message;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DeathMessage extends Message{
	
	public DeathMessage(DatagramSocket senderSocket, InetSocketAddress destination, int playerID, int roomID) 
			throws SocketException, UnknownHostException {
		super(senderSocket, destination);
		this.data = DEATH + DELIMITER + roomID + DELIMITER + playerID;
	}

}
