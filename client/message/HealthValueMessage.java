package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HealthValueMessage extends Message{
	int playerID;
	int currentHealth;
	
	public HealthValueMessage(int playerID, int currentHealth) {
		super();
		this.playerID = playerID;
		this.currentHealth = currentHealth;
	}

	@Override
	public void send(DatagramSocket clientSocket) throws IOException {
		// TODO Auto-generated method stub
		InetAddress IPAddress = InetAddress.getByName(server_ip);
		byte[] sendData = new byte[1024];
		stringData = Message.HEALTH_VALUE + "~" + playerID + "~" + this.currentHealth;
		sendData = stringData.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, server_port);
		clientSocket.send(sendPacket);
	}
	
}
