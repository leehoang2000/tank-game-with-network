package server.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class RawMessage extends Message {
	
	private String message;
	
	public RawMessage(InetSocketAddress socket, String message)
	{
		this.client = socket;
		this.message = message;
	}

	@Override
	public void send() throws IOException {
		
		DatagramSocket serverSocket = new DatagramSocket();
		InetAddress IPAddress = client.getAddress();
		byte[] sendData = new byte[1024];
		sendData = message.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, client.getPort());
		serverSocket.send(sendPacket);

	}

}
