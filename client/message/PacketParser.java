package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.ArrayList;

import TankGame.TankWorld;

public class PacketParser {

	private static PacketParser singleton;

	public static PacketParser singleton() {
		if (singleton == null) {
			singleton = new PacketParser();
		}
		return singleton;
	}

	public void parse(DatagramPacket packet) throws IOException {
		String message = new String(packet.getData());
		int opcode = Integer.valueOf(message.substring(0, message.indexOf('~')));
		String data = message.substring(message.indexOf('~') + 1, message.length()).trim();

//		System.out.println("Client received data |" + data + "|Length: " + data.length());

		switch (opcode) {
			case Message.ACK: {
				Acknowledge a = new Acknowledge(data);
				a.start();
				break;
			}

			case Message.PING: {
				ClientSideSender.singleton().sendReplyPingMessage();
				break;
			}

			case Message.REFUSE: {
				System.out.println("Server sends REFUSE");
				break;
			}

			case Message.ROOM_MEMBER_UPDATE: {
				RoomMemberUpdate rmu = new RoomMemberUpdate(data);
				rmu.start();
				break;
			}
			case Message.TANKPOS: {
				UpdateTankPos utp = new UpdateTankPos(data);
				utp.start();
				break;
			}
			case Message.SHOOT: {
//			System.out.println("Client.PacketParser.parse| Received Shot Message! ");
				int playerID = Integer.valueOf(data);
				ShootFromTank sft = new ShootFromTank(playerID);
				sft.start();
				break;
			}
			case Message.DEATH: {
				break;
			}

			case Message.HEALTH_VALUE: {
				SetHealthValue shv = new SetHealthValue(data);
				shv.start();
				break;
			}

			case Message.GAME_WON: {
				TankWorld.singleton().handleWinScene();
				System.out.println("We won!");
				
				break;
			}
			case Message.GAME_LOST: {
				TankWorld.singleton().handleLoseScene();
				System.out.println("We lost!");
				break;
			}
		}
	}

	private class Acknowledge extends Thread {
		private String data;

		public Acknowledge(String data) {
			this.data = data;
		}

		@Override
		public void run() {
			TankWorld.setCurrentID(Integer.valueOf(data));

			synchronized (TankWorld.getIDFromServerLock) {
				TankWorld.getIDFromServerLock.notify();
			}
		}
	}

	private class RoomMemberUpdate extends Thread {
		private String roomData;

		public RoomMemberUpdate(String data) {
			this.roomData = data;
		}

		@Override
		public void run() {
			ArrayList<Integer> ids = new ArrayList<Integer>();
			while (roomData.length() > 0) {
				if (roomData.contains("-")) {
					ids.add(Integer.valueOf(roomData.substring(0, roomData.indexOf('-'))));
					roomData = roomData.substring(roomData.indexOf('-') + 1, roomData.length());
				} else {
					ids.add(Integer.valueOf(roomData));
					break;
				}
			}

			try {
				while (!TankWorld.singleton().isInitialized) {
					sleep(20);
				}
				TankWorld.singleton().updateRoomMember(ids);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.print("Client.PacketParser.RoomMemberUpdate.run | The room now has: ");
			for (int id : TankWorld.singleton().players.keySet()) {
				System.out.print(id + " ");
			}
			System.out.println("");
		}
	}

	private class UpdateTankPos extends Thread {
		private String data;

		public UpdateTankPos(String data) {
			this.data = data;
		}

		@Override
		public void run() {
//			System.out.println("Client.PacketParser.UpdateTankPos| The tankpos data is: " + data);
			int id, tankCenterPosX, tankCenterPosY, tankAngle;
			id = Integer.valueOf(data.substring(0, data.indexOf('-')));
			data = data.substring(data.indexOf('-') + 1, data.length());

			tankCenterPosX = Integer.valueOf(data.substring(0, data.indexOf('-')));
			data = data.substring(data.indexOf('-') + 1, data.length());

			tankCenterPosY = Integer.valueOf(data.substring(0, data.indexOf('-')));
			data = data.substring(data.indexOf('-') + 1, data.length());

			tankAngle = Integer.valueOf(data);

//			System.out.print("Client.PacketParser.UpdateTankPos|  | The room now has: ");
//			for (int tankid : TankWorld.players.keySet()) {
//				System.out.print(tankid + " ");
//			}
//			System.out.println("");

			try {
				TankWorld.singleton().players.get(id).setTankCenterX(tankCenterPosX);
				TankWorld.singleton().players.get(id).setTankCenterY(tankCenterPosY);
				TankWorld.singleton().players.get(id).setAngle(tankAngle);
			} catch (NullPointerException e) {
				System.out.println("Client.PacketParser.UpdateTankPos| ID not exist: " + id);
				// TODO: handle exception
			}
		}
	}

	private class ShootFromTank extends Thread {
		int playerID;

		public ShootFromTank(int playerID) {
			this.playerID = playerID;
		}

		@Override
		public void run() {
			TankWorld.players.get(playerID).switchShootOn();
		}
	}

	private class SetHealthValue extends Thread {
		String data;

		public SetHealthValue(String data) {
			this.data = data;
		}

		@Override
		public void run() {
			int playerID = Integer.valueOf(data.substring(0, data.indexOf('~')));
			data = data.substring(data.indexOf('~') + 1, data.length());
			int healthValue = Integer.valueOf(data);
//			System.out.println("Client.PacketParser.SetCurrentHeath | setting health for " + playerID + " with health value: " + healthValue);
			TankWorld.players.get(playerID).setHealthPoints(healthValue);
		}
	}
	
	private class DelayedShutdown extends Thread {
		@Override
		public void run() {
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			TankWorld.singleton().turnOff();
		}
	}
}
