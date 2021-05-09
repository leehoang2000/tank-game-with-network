package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class AckMessage extends Message {
	
	public AckMessage(String data)
	{
		this.stringData = data;
	}

	@Override
	public void send() throws IOException {

	}

}
