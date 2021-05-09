package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class RequestConnectMessage extends Message {
	
	public RequestConnectMessage(String stringData)
	{
		this.stringData = REQUEST_CONNECT + '~' + stringData;
	}

	@Override
	public void send() throws IOException {

		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(server_ip);
		byte[] sendData = new byte[1024];
		sendData = stringData.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, server_port);
		clientSocket.send(sendPacket);
		// TODO: Unclosed socket
		
	}

}
