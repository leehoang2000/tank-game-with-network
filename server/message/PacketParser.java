package server.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.UDPServer;

public class PacketParser {

	public static void parse(DatagramPacket packet) throws IOException
	{
		String message = new String(packet.getData()).trim();
		System.out.println("Message received: "+ message);
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
					
					// TODO: Register this client for spawning tank
					
					int id = UDPServer.roomManager.getID(client);
				}
				break;
			}
			case Message.PING:
			{
				UDPServer.roomManager.resetTimeout(client);
				break;
			}
			case Message.TANKPOS:
			{
				// data: ID-X-Y
				String pattern = "^([0-9]+)-([0-9]+)-([0-9]+)$";
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(data);
				int id,x,y;
				if(m.find())
				{
					id = Integer.valueOf(m.group(1));
					x = Integer.valueOf(m.group(2));
					y = Integer.valueOf(m.group(3));
					
					System.out.println("ID: " + id + "; X: " + x + "; Y: " + y);
					
					// Broadcast to room, exclude id
					UDPServer.roomManager.broadcast(message,id);
				}
			}
		}
	}
	
}
