package client.message;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

//Use the same clientsocket for all message sent to server in 1 session
public class ClientSideSender {

	private static ClientSideSender singleton;
	
	private DatagramSocket clientSocket;
	
	private ClientSideSender() throws SocketException
	{
		clientSocket = new DatagramSocket();
	}
	
	public static ClientSideSender singleton() throws SocketException
	{
		if(singleton == null)
			singleton = new ClientSideSender();
		return singleton;
	}
	
	public static void init()
	{
		
	}
	
	public DatagramSocket getClientSocket()
	{
		return clientSocket;
	}
	
	public void sendRequestConnectMessage() throws IOException
	{
		RequestConnectMessage rcm = new RequestConnectMessage();
		rcm.send(clientSocket);
	}

	public void sendReplyPingMessage() throws IOException {
		
		PingMessage pm = new PingMessage();
		pm.send(clientSocket);
//		System.out.println("Client replied ping");
	}
	
	public void sendTankPosMessage(int tankID, int tankCenterX, int tankCenterY, int tankAngle) throws IOException
	{
		TankPosMessage tpm = new TankPosMessage(tankID, tankCenterX, tankCenterY, tankAngle);
		tpm.send(clientSocket);
	}

	public void sendShootMessage(int playerID) throws IOException {	
		// TODO Auto-generated method stub
		ShootMessage sm = new ShootMessage(playerID);
		sm.send(clientSocket);
			
	}

	public void sendDeathMessage(int playerID) {
		// TODO Auto-generated method stub
		
	}
	
	public void sendCurrentHealthMessage(int playerID, int currentHealth) throws IOException {
		HealthValueMessage chm = new HealthValueMessage(playerID, currentHealth);
		chm.send(clientSocket);
	}
	
}
