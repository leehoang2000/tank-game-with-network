package client.message;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import TankGame.TankGameClient;

//Use the same clientsocket for all message sent to server in 1 session
public class ClientSideSender {

	private DatagramSocket clientSocket;
	private InetSocketAddress serverAddress;
	private TankGameClient tankGame;

	public ClientSideSender(String server_ip, int server_port) throws SocketException {
		this.clientSocket = new DatagramSocket();
		this.serverAddress = new InetSocketAddress(server_ip, server_port);
	}

	public void setTankGameClient(TankGameClient tankGame) {
		this.tankGame = tankGame;
	}

	public static void init() {

	}

	public DatagramSocket getClientSocket() {
		return clientSocket;
	}

	public void sendRequestConnectMessage() throws IOException {
		RequestConnectMessage rcm = new RequestConnectMessage(clientSocket, serverAddress);
		rcm.send();
	}

	public void sendReplyPingMessage(int roomID) throws IOException {

		PingMessage pm = new PingMessage(clientSocket, serverAddress, roomID);
		pm.send();
//		System.out.println("Client replied ping");
	}

	public void sendTankPosMessage(int tankID, int tankCenterX, int tankCenterY, int tankAngle) throws IOException {
		TankPosMessage tpm = new TankPosMessage(clientSocket, serverAddress, tankID, tankCenterX, tankCenterY,
				tankAngle, tankGame.getRoomID());
		tpm.send();
	}

	public void sendShootMessage(int playerID) throws IOException {
		// TODO Auto-generated method stub
		ShootMessage sm = new ShootMessage(clientSocket, serverAddress, playerID, tankGame.getRoomID());
		sm.send();

	}

	public void sendDeathMessage(int playerID) {
		// TODO Auto-generated method stub

	}

	public void sendCurrentHealthMessage(int playerID, int currentHealth) throws IOException {
		HealthValueMessage chm = new HealthValueMessage(clientSocket, serverAddress, playerID, currentHealth,
				tankGame.getRoomID());
		chm.send();
	}

}
