package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public abstract class Message {

	static final int REQUEST_CONNECT = 0;
	static final int ACK = 1;
	static final int REFUSE = 2; // gtfo
	static final int PING = 3;

	static final int TANKPOS = 4;

	static final int ROOM_MEMBER_UPDATE = 5;

	static final int SHOOT = 6;

	static final int DEATH = 7;

	static final int HEALTH_VALUE = 8;
	
	static final int ANNOUNCE_ALLY = 9;

	static final int GAME_LOST = 99;
	static final int GAME_WON = 100;

	static final String DELIMITER = "~";

	protected InetSocketAddress destination;
	protected DatagramSocket senderSocket;
	protected byte[] sendData;
	protected String data;

	public Message(DatagramSocket senderSocket, InetSocketAddress destination) throws UnknownHostException {
		this.senderSocket = senderSocket;
		this.destination = destination;
	}

	public void send() throws IOException {
		sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destination.getAddress(), 
				destination.getPort());
		senderSocket.send(sendPacket);
	}
}