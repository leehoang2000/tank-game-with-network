package server.message;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

// TODO: Refactor

public class ServerSideSender {

	private static ServerSideSender singleton;
	
	private DatagramSocket clientSocket;
	
	private ServerSideSender() throws SocketException
	{
		clientSocket = new DatagramSocket();
	}
	
	public static ServerSideSender singleton() throws SocketException
	{
		if(singleton == null)
			singleton = new ServerSideSender();
		return singleton;
	}
	
	public static void init() throws SocketException
	{
		
	}
	
	public DatagramSocket getClientSocket()
	{
		return clientSocket;
	}

	public void sendPingMessage(InetSocketAddress socket) throws IOException {
		
		PingMessage pm = new PingMessage(socket);
		pm.send();
	}
	
	public void sendRawMessage(InetSocketAddress socket, String message) throws IOException
	{
		RawMessage rm = new RawMessage(socket, message);
		rm.send();
	}
	
}
