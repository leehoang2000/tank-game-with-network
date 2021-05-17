package client.message;

import java.io.IOException;
import java.net.DatagramPacket;

import TankGame.TankWorld;

public class PacketParser {

	public static void parse(DatagramPacket packet) throws IOException
	{
		String message = new String(packet.getData());
		int opcode = Integer.valueOf(message.substring(0, message.indexOf('~')));
		String data = message.substring(message.indexOf('~') + 1, message.length()).trim();
		
		System.out.println("Client received data |" + data + "|Length: " + data.length());
		
		switch(opcode)
		{
			case Message.ACK:
			{
				TankWorld.setCurrentID(Integer.valueOf(data));
				
				synchronized(TankWorld.lock)
				{
					TankWorld.lock.notify();
				}
				break;
			}
			case Message.PING:
			{
				ClientSideSender.singleton().sendReplyPingMessage();
				break;
			}
			case Message.REFUSE:
			{
				System.out.println("Server sends REFUSE");
				
				break;
			}
		}
	}
	
}
