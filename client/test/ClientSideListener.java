package client.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import client.message.PacketParser;

public class ClientSideListener extends Thread
{

	private DatagramSocket clientSocket;

	public ClientSideListener(DatagramSocket clientSocket)
	{
		this.clientSocket = clientSocket;
	}
	
	public void run()
	{
		while (true) {
            byte[] receiveData = new byte[1024];
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				clientSocket.receive(receivePacket);
				PacketParser.parse(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
//				System.out.println("  - Received data from client: " + receivePacket.getAddress().getHostAddress() + ":"
//						+ receivePacket.getPort());
			
			// TODO: Clients must send something to signify that it is not a new socket (do not
			//	 	 call addSocket on it)
			
//				String sentence = new String(receivePacket.getData());
//				System.out.println("  - Data: " + sentence);
			
//				InetAddress IPAddress = receivePacket.getAddress();
//				int port = receivePacket.getPort();
//				String capitalizedSentence = sentence.toUpperCase() + " hehehe :D\n";
//				
//				sendData = capitalizedSentence.getBytes();
//				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//				serverSocket.send(sendPacket);
		}
//		clientSocket.close();
	}
}