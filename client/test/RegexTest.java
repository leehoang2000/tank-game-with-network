package client.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RegexTest {

	public static void main (String[] args) throws IOException
	{
		int server_port = 55000;
		String server_ip = "localhost";
		
		String sentence = "4~6-300-300";
		System.out.println("Sentence sent: " + sentence);

		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(server_ip);
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, server_port);
		clientSocket.send(sendPacket);
		clientSocket.close();
	}
	
}
