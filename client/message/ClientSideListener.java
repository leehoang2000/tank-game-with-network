package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientSideListener extends Thread
{
	private DatagramSocket clientSocket;
	private PacketParser packetParser;

	public ClientSideListener(DatagramSocket clientSocket,PacketParser packetParser)
	{
		this.clientSocket = clientSocket;
		this.packetParser = packetParser;
	}

	public void run()
	{
		while (true) {
            byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				clientSocket.receive(receivePacket);
				packetParser.parse(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
}