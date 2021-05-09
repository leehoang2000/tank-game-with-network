package server.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import server.UDPServer;

public class PacketParser {

	public static void parse(DatagramPacket packet) throws IOException
	{
		String message = new String(packet.getData());
		System.out.println(message);
		int opcode = Integer.valueOf(message.substring(0, message.indexOf('~')));
		String data = message.substring(message.indexOf('~') + 1, message.length());
		InetSocketAddress client = (InetSocketAddress) packet.getSocketAddress();
		switch(opcode)
		{
			case Message.REQUEST_CONNECT:
			{
				if(UDPServer.roomManager.addSocket(client))
				{
					AckMessage ackMessage = new AckMessage(client);
					ackMessage.send();
				}
			}
		}
	}
	
}
