package client.message;

import java.io.IOException;
import java.net.DatagramPacket;

public class PacketParser {

	public static void parse(DatagramPacket packet) throws IOException
	{
		String message = new String(packet.getData());
		int opcode = Integer.valueOf(message.substring(0, message.indexOf('~')));
		String data = message.substring(message.indexOf('~') + 1, message.length());
		switch(opcode)
		{
			case Message.ACK:
			{
				
			}
			case Message.PING:
			{
				PingMessage pingMessage = new PingMessage();
				pingMessage.send();
			}
		}
	}
	
}
