package server.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.UDPServer;

public class PacketParser {

	public static void parse(DatagramPacket packet) throws IOException {
		String message = new String(packet.getData()).trim();
//		System.out.println("Message received: " + message);
		int opcode = Integer.valueOf(message.substring(0, message.indexOf('~')));
		String data = message.substring(message.indexOf('~') + 1, message.length());
		InetSocketAddress client = (InetSocketAddress) packet.getSocketAddress();
		switch (opcode) {
			case Message.REQUEST_CONNECT: {
				if (UDPServer.singleton().roomManager.addSocket(client)) {
					AckMessage ackMessage = new AckMessage(client);
					ackMessage.send();
					int id = UDPServer.singleton().roomManager.getID(client);
				}
				break;
			}
			case Message.PING: {
				UDPServer.singleton().roomManager.resetTimeout(client);
				break;
			}
			case Message.TANKPOS: {
				int id = Integer.valueOf(data.substring(0, data.indexOf('-')));
				// Broadcast to room, exclude id
//			System.out.println("Server.packetparser.parse | The id is" + id);
//			System.out.println("Server.packetparser.parse | The message is" + message);
				UDPServer.singleton().roomManager.broadcast(message, id);
				break;
			}
			case Message.SHOOT: {
				int id = Integer.valueOf(data);
//			System.out.println("Server.PacketParser.parse| player " + id + "shot!");
//			System.out.println("Server.PacketParser.parse| server broadcast shot!");
				UDPServer.singleton().roomManager.broadcast(message, id);
				message = "";
				break;
			}
			case Message.DEATH: {
				break;
			}
			case Message.HEALTH_VALUE: {
//			System.out.println("Server.PacketParser.parse| sending current Health!");
				int id = Integer.valueOf(data.substring(0, data.indexOf('~')));
				data = data.substring(data.indexOf('~') + 1, data.length());
				int newHealthValue = Integer.valueOf(data);
				UDPServer.singleton().roomManager.broadcast(message, id);
				UDPServer.singleton().roomManager.updatePlayerHealth(id, newHealthValue);
			}
		}
	}

}
