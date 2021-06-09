package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ShootMessage extends Message{
	
	int playerID;
	public ShootMessage(int playerID) {
		this.playerID = playerID;
	}

	@Override
	public void send(DatagramSocket clientSocket) throws IOException {
		// TODO Auto-generated method stub
		InetAddress IPAddress = InetAddress.getByName(server_ip);
		byte[] sendData = new byte[1024];
		stringData = Message.SHOOT + "~" + playerID;
		sendData = stringData.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, server_port);
		clientSocket.send(sendPacket);
	}
}
