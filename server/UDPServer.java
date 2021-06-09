package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import server.message.PacketParser;
import server.room_manager.RoomManager;

public class UDPServer {
	
	private static UDPServer singleton;
	
	public static UDPServer singleton() {
		if (singleton == null)
			singleton = new UDPServer();
		return singleton;
	}
	public RoomManager roomManager = new RoomManager();

	public static void main(String[] args) throws Exception {
		int udp_port = 55000;

		try (DatagramSocket serverSocket = new DatagramSocket(udp_port)) {
			System.out.println("The server is running!");
			while (true) {
                byte[] receiveData = new byte[1024];
                byte[] sendData = new byte[1024];
             			
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				PacketParser.parse(receivePacket);
				
			}
		}
	}
}