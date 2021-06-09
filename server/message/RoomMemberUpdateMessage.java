package server.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import server.UDPServer;

public class RoomMemberUpdateMessage extends Message {
	ArrayList<Integer> roomMemberIDs;
	
	public RoomMemberUpdateMessage (InetSocketAddress client, ArrayList<Integer> newPlayerID) {
		this.client = client;
		this.roomMemberIDs = newPlayerID;
	}

	@Override
	public void send() throws IOException {
//		System.out.println("sending RoomMemberUpdateMessage");
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = client.getAddress();
		byte[] sendData = new byte[1024];
		String data = String.valueOf(ROOM_MEMBER_UPDATE); 
		data = data + "~";
		for(int id : roomMemberIDs) {
			data = data + id + "-" ;
		}
		System.out.println("Sending :" + data);
		sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, client.getPort());
		clientSocket.send(sendPacket);
		// TODO: Unclosed socket
	}

}
