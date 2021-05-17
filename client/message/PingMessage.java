package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PingMessage extends Message {
	
	public PingMessage()
	{
		
	}

	@Override
	public void send(DatagramSocket ds) throws IOException {
		
//		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(server_ip);
		byte[] sendData = new byte[1024];
		String data = String.valueOf(PING) + '~';
		sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, server_port);
		ds.send(sendPacket);
		// TODO: Unclosed socket

	}

}
