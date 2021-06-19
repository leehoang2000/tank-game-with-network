package client.message;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class HealthValueMessage extends Message {

	public HealthValueMessage(DatagramSocket senderSocket, InetSocketAddress destination, int playerID, 
			int currentHealth, int roomID) throws SocketException, UnknownHostException {
		super(senderSocket, destination);
		this.data = HEALTH_VALUE + DELIMITER + roomID + DELIMITER + playerID + DELIMITER + currentHealth;
	}

}
