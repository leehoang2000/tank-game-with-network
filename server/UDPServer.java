package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

	public static void main(String[] args) throws Exception {
		int udp_port = 55000;

		try (DatagramSocket serverSocket = new DatagramSocket(udp_port)) {
			while (true) {
                byte[] receiveData = new byte[1024];
                byte[] sendData = new byte[1024];
                
				System.out.println("UDP Server is waiting for client data at port: " + udp_port);
				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				System.out.println("  - Received data from client: " + receivePacket.getAddress().getHostAddress() + ":"
						+ receivePacket.getPort());
				
				String sentence = new String(receivePacket.getData());
				System.out.println("  - Data: " + sentence);
				
//				InetAddress IPAddress = receivePacket.getAddress();
//				int port = receivePacket.getPort();
//				String capitalizedSentence = sentence.toUpperCase() + " hehehe :D\n";
//				
//				sendData = capitalizedSentence.getBytes();
//				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//				serverSocket.send(sendPacket);
			}
		}
	}
}