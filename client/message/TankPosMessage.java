package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import TankGame.TankWorld;

public class TankPosMessage extends Message {
	
	private int tankID;
	private int tankCenterX;
	private int tankCenterY;
	
	public TankPosMessage(int tankID, int tankCenterX, int tankCenterY)
	{
		this.tankID = tankID;
		this.tankCenterX = tankCenterX;
		this.tankCenterY = tankCenterY;
	}

	@Override
	public void send(DatagramSocket ds) throws IOException {
		
		InetAddress IPAddress = InetAddress.getByName(server_ip);
		byte[] sendData = new byte[1024];
		String data = TANKPOS + "~" + tankID +"-"+ tankCenterX +"-"+ tankCenterY;
		sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, server_port);
		ds.send(sendPacket);

	}

}
