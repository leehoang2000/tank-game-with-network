package server.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class GameWonMessage extends Message{
	
	public GameWonMessage(InetSocketAddress client) {
		this.client = client;
	}

	@Override
	public void send() throws IOException {
		
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = client.getAddress();
		byte[] sendData = new byte[1024];
		String data = String.valueOf(GAME_WON) + '~';
		sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, client.getPort());
		clientSocket.send(sendPacket);
		
	}

}
