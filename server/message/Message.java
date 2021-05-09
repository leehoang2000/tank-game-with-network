package server.message;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class Message {
	
	static final int REQUEST_CONNECT = 0;
	static final int ACK = 1;
	static final int REFUSE = 2; // gtfo
	static final int PING = 3;
	
	static int server_port = 55000;
	static String server_ip = "localhost";
	
	String data;
	InetSocketAddress client;
	
	public abstract void send() throws IOException;
	
}