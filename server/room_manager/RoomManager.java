// Each room has one RoomManager

package server.room_manager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import server.UDPServer;
import server.message.PingLoopService;
import server.message.ServerSideSender;
import server.player.Player;

public class RoomManager extends Thread {
	private PingLoopService pls;

	int roomID;
	int team1AliveCount;
	int team2AliveCount;

	List<Integer> team1IDs;
	List<Integer> team2IDs;

	// Synchronized data structures
	Map<InetSocketAddress, Player> Players;
	Deque<Integer> IDStack;

	public RoomManager() {
		Players = new ConcurrentHashMap<InetSocketAddress, Player>();
		IDStack = new ConcurrentLinkedDeque<Integer>(Arrays.asList(3, 2, 1, 0));
		team1AliveCount = 0;
		team2AliveCount = 0;

		team1IDs = new ArrayList<Integer>();
		team2IDs = new ArrayList<Integer>();

		team1IDs.add(0);
		team1IDs.add(3);
		team2IDs.add(2);
		team2IDs.add(1);

		pls = new PingLoopService(Players);
		pls.start();
	}

	public boolean addSocket(InetSocketAddress newSocket) throws IOException {
		if (IDStack.isEmpty() == false) {
			int newID = IDStack.pollLast(); // pop
			Player newPlayer = new Player(newID);
			Players.put(newSocket, newPlayer);
			if (team1IDs.contains(newID)) {
				team1AliveCount++;
			} else if (team2IDs.contains(newID)) {
				team2AliveCount++;
			} else {
				System.out.println("Server.RoomManager.addSocket| Undefined ID: " + newID);
			}

			broadcastRoomMemberUpdate();

			return true;
		}
		return false;
	}

	public boolean removeSocket(InetSocketAddress oldSocket) throws IOException {
		if (IDStack.size() < 4) {
			// Get ID of socket (not synchronized)
			int oldID = 0;
			Players.remove(oldSocket);

			if (team1IDs.contains(oldID)) {
				team1AliveCount++;
			} else if (team2IDs.contains(oldID)) {
				team2AliveCount++;
			} else {
				System.out.println("Server.RoomManager.removeSocket| undefined ID: " + oldID);
			}

			if (oldID == 0)
				return false;

			if (IDStack.offerLast(oldID)) // push
			{
				broadcastRoomMemberUpdate();
				return true;
			}
		}
		return false;
	}

	public int getID(InetSocketAddress socket) {
		return Players.get(socket).getID();
	}

	public Set<InetSocketAddress> getPlayerSockets() {
		return Players.keySet();
	}

	public void resetTimeout(InetSocketAddress client) {

		pls.resetTimeout(client);

	}

	public void broadcast(String message) throws IOException {
		broadcast(message, -1);
	}

	public void broadcast(String message, int excludeID) throws IOException {
//		if(Integer.valueOf(message.substring(0, message.indexOf('~'))) == 6){
//			System.out.println("Server.RoomManger.broadcast| server broadcast shot! ");			
//		}
		for (Entry<InetSocketAddress, Player> entry : Players.entrySet()) {
			if (entry.getValue().getID() != excludeID) {
				ServerSideSender.singleton().sendRawMessage(entry.getKey(), message);
			}
		}
	}

	public ArrayList<Integer> getAllPlayerID() {
		ArrayList<Integer> returnList = new ArrayList<Integer>();
		for (Entry<InetSocketAddress, Player> entry : Players.entrySet()) {
			returnList.add(entry.getValue().getID());
		}

		return returnList;
	}

	public void broadcastRoomMemberUpdate() throws IOException {
		for (InetSocketAddress player : Players.keySet()) {
//			System.out.println("Server.RoomManager.broadcastRoomMemberUpdate| send to: " + Players.get(player));
			ServerSideSender.singleton().sendRoomMemberUpdateMessage(player, getAllPlayerID());
		}
	}

	public void updatePlayerHealth(int playerID, int healthValue) {
		for (Player player : Players.values()) {
			if (player.getID() == playerID) {
				// Prevent already dead player to enter this dead handling again
				if (player.handleHealthChange(healthValue)) {
					if (player.isDead()) {
						if (team1IDs.contains(player.getID())) {
							team1AliveCount--;
						} else if (team2IDs.contains(player.getID())) {
							team2AliveCount--;
						} else {
							System.out
									.println("Server.RoomManager.updatePlayerHealth| undefined ID: " + player.getID());
						}
						System.out
								.println("Server.RoomManager.updatePlayerHealth| team2AliveCount: " + team1AliveCount);
						checkWinCondition();
					}
				}
			}
		}
	}

	// Team 1 has ID 0 and 1, Team 2 has ID 2 and 3
	public void checkWinCondition() {
		if (team1AliveCount == 0) {
			announceGameResult(2);
		} else if (team2AliveCount == 0) {
			announceGameResult(1);
		}
	}

	// Announce winning for the team in param
	public void announceGameResult(int teamNumber) {
		switch (teamNumber) {
			case 1:
				for (Entry<InetSocketAddress, Player> entry : Players.entrySet()) {
					try {
						if (team1IDs.contains(entry.getValue().getID())) {
							ServerSideSender.singleton().sendGameWonMessage(entry.getKey());
						} else {
							ServerSideSender.singleton().sendGameLostMessage(entry.getKey());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			case 2:
				for (Entry<InetSocketAddress, Player> entry : Players.entrySet()) {
					try {
						if (team2IDs.contains(entry.getValue().getID())) {
							ServerSideSender.singleton().sendGameWonMessage(entry.getKey());
						} else {
							ServerSideSender.singleton().sendGameLostMessage(entry.getKey());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
		}
		resetRoom();
	}
	
	public void resetRoom() {
		UDPServer.singleton().roomManager = new RoomManager();
	}
	
	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
}
