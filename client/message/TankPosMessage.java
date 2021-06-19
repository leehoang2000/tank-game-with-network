package client.message;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TankPosMessage extends Message {
	public TankPosMessage(DatagramSocket senderSocket, InetSocketAddress destination, int playerID, int tankCenterX,
			int tankCenterY, int tankAngle, int roomID) throws SocketException, UnknownHostException {
		super(senderSocket, destination);
		data = TANKPOS + DELIMITER + roomID + DELIMITER + playerID + DELIMITER + tankCenterX + DELIMITER + tankCenterY
				+ DELIMITER + tankAngle;
	}
}
