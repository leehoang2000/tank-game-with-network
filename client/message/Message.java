package client.message;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class Message {
	
	static final int REQUEST_CONNECT = 0;
	static final int ACK = 1;
	static final int REFUSE = 2; // gtfo
	static final int PING = 3;

	static final int TANKPOS = 4;
	
	static int server_port = 55000;
	static String server_ip = "localhost";
	
	String stringData;
	
	public abstract void send(DatagramSocket clientSocket) throws IOException;
	
}