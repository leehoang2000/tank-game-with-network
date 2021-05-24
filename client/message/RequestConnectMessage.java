package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class RequestConnectMessage extends Message {
	
	public RequestConnectMessage()
	{
		this.stringData = REQUEST_CONNECT + "~";
	}

	@Override
	public void send(DatagramSocket clientSocket) throws IOException {

		InetAddress IPAddress = InetAddress.getByName(server_ip);
		byte[] sendData = new byte[1024];
		sendData = stringData.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, server_port);
		clientSocket.send(sendPacket);
		// TODO: Unclosed socket
		
	}

}
