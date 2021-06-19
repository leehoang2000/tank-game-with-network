package client.message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;

import TankGame.TankGameClient;

public class PacketParser {

	private TankGameClient tankGame;
	private ClientSideSender clientSideSender;

	public PacketParser(TankGameClient tankGame, ClientSideSender clientSideSender) {
		super();
		this.tankGame = tankGame;
		this.clientSideSender = clientSideSender;
	}

	public void parse(DatagramPacket packet) throws IOException {
		String message = new String(packet.getData()).trim();
		
//		System.out.println("Client received data |" + message + "|Length: " + message.length());
		String[] messageComponents = message.split(Message.DELIMITER);
		int opcode = Integer.valueOf(messageComponents[0]);

		switch (opcode) {
			case Message.ACK: {
				HandleAcknowledgeThread a = new HandleAcknowledgeThread(messageComponents[1], messageComponents[2]);
				a.start();
				break;
			}
		
			case Message.PING: {
				clientSideSender.sendReplyPingMessage(tankGame.getRoomID());
				break;
			}

			case Message.REFUSE: {
				System.out.println("Server sends REFUSE");
				break;
			}

			case Message.ROOM_MEMBER_UPDATE: {
				HandleRoomMemberUpdateThread rmu = new HandleRoomMemberUpdateThread(
						Arrays.copyOfRange(messageComponents, 1, messageComponents.length));
				rmu.start();
				break;
			}
			case Message.TANKPOS: {
				HandleUpdateTankPosThread utp = new HandleUpdateTankPosThread(messageComponents[2], messageComponents[3], messageComponents[4],
						messageComponents[5]);
				utp.start();
				break;
			}
			case Message.SHOOT: {
//			System.out.println("Client.PacketParser.parse| Received Shot Message! ");
				HandleShootFromTankThread sft = new HandleShootFromTankThread(messageComponents[2]);
				sft.start();
				break;
			}
			
			case Message.DEATH: {
				break;
			}

			case Message.HEALTH_VALUE: {
				HandleSetHealthValueThread shv = new HandleSetHealthValueThread(messageComponents[2], messageComponents[3]);
				shv.start();
				break;
			}
			
			case Message.GAME_WON: {
				new HandleWinSceneThread().start();
				break;
			}
			case Message.GAME_LOST: {
				new HandleLoseSceneThread().start();
				break;
			}
			
			case Message.ANNOUNCE_ALLY:{
				int allyID = Integer.valueOf(messageComponents[1]);
				new HandleSetAllyThread(allyID).start();
				break;
			}
		}
	}

	private class HandleAcknowledgeThread extends Thread {
		private String roomID;
		private String playerID;

		public HandleAcknowledgeThread(String roomID, String playerID) {
			this.roomID = roomID;
			this.playerID = playerID;
		}

		@Override
		public void run() {
			tankGame.setCurrentID(Integer.valueOf(playerID));
			tankGame.setRoomID(Integer.valueOf(roomID));

			synchronized (TankGameClient.getIDFromServerLock) {
				TankGameClient.getIDFromServerLock.notify();
			}
		}
	}

	private class HandleRoomMemberUpdateThread extends Thread {
		private String[] roomData;

		public HandleRoomMemberUpdateThread(String[] roomData) {
			this.roomData = roomData;
		}

		@Override
		public void run() {
			ArrayList<Integer> ids = new ArrayList<Integer>();
			for (String id : roomData) {
				ids.add(Integer.valueOf(id));
			}

			try {
				while (!tankGame.isInitialized()) {
					sleep(100);
				}
				tankGame.updateRoomMember(ids);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.print("Client.PacketParser.RoomMemberUpdate.run | The room now has: ");
//			for (int id : tankGame.getPlayers().keySet()) {
//				System.out.print(id + " ");
//			}
//			System.out.println("");
		}
	}

	private class HandleUpdateTankPosThread extends Thread {
		private int id;
		private int tankCenterPosX;
		private int tankCenterPosY;
		private int tankAngle;

		public HandleUpdateTankPosThread(String id, String tankCenterPosX, String tankCenterPosY, String tankAngle) {
			this.id = Integer.valueOf(id);
			this.tankCenterPosX = Integer.valueOf(tankCenterPosX);
			this.tankCenterPosY = Integer.valueOf(tankCenterPosY);
			this.tankAngle = Integer.valueOf(tankAngle);
		}

		@Override
		public void run() {

			try {
				tankGame.getPlayers().get(id).setTankCenterX(tankCenterPosX);
				tankGame.getPlayers().get(id).setTankCenterY(tankCenterPosY);
				tankGame.getPlayers().get(id).setAngle(tankAngle);
			} catch (NullPointerException e) {
				System.out.println("Client.PacketParser.UpdateTankPos| ID not exist: " + id);
			}
		}
	}

	private class HandleShootFromTankThread extends Thread {
		int playerID;

		public HandleShootFromTankThread(String playerID) {
			this.playerID = Integer.valueOf(playerID);
		}

		@Override
		public void run() {
			tankGame.getPlayers().get(playerID).switchShootOn();
		}
	}

	private class HandleSetHealthValueThread extends Thread {
		private int playerID;
		private int healthValue;

		public HandleSetHealthValueThread(String playerID, String healthValue) {
			this.playerID = Integer.valueOf(playerID);
			this.healthValue = Integer.valueOf(healthValue);
		}

		@Override
		public void run() {
//			System.out.println("Client.PacketParser.SetCurrentHeath | setting health for " + playerID + " with health value: " + healthValue);
			tankGame.getPlayers().get(playerID).setHealthPoints(healthValue);
		}
	}

	private class HandleSetAllyThread extends Thread{
		private int allyID;

		public HandleSetAllyThread(int allyID) {
			super();
			this.allyID = allyID;
		}
		
		@Override
		public void run() {
			tankGame.setAllyID(allyID);
		}
	}
	
	private class HandleWinSceneThread extends Thread{
		@Override
		public void run() {
			System.out.println("TankGame winner state: ");
			try {
				sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tankGame.handleWinScene();
		}
	}
	
	private class HandleLoseSceneThread extends Thread{
		@Override
		public void run() {
			try {
				System.out.println("TankGame loser state: " );
				sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tankGame.handleLoseScene();
		}
	}
}
