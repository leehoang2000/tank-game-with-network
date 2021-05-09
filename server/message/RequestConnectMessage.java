package server.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import server.UDPServer;

public class RequestConnectMessage extends Message {
	
	public RequestConnectMessage(InetSocketAddress client)
	{
		this.client = client;
	}

	@Override
	public void send() throws IOException {

		// TODO
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(server_ip);
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, server_port);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.close();
		
	}

}
