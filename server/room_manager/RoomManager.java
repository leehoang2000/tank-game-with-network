// Each room has one RoomManager

package server.room_manager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import server.message.PingLoopService;
import server.message.ServerSideSender;

public class RoomManager extends Thread
{
	private PingLoopService pls;
	
	public RoomManager()
	{
		pls = new PingLoopService(Players);
		pls.start();
	}
	
	// Synchronized data structures
	Map<InetSocketAddress, Integer> Players = new ConcurrentHashMap<InetSocketAddress, Integer>();
	Deque<Integer> IDStack = new ConcurrentLinkedDeque<Integer>(Arrays.asList(3,2,1,0));
	
	public boolean addSocket(InetSocketAddress newSocket)
	{	
		if( IDStack.isEmpty() == false )
		{
			int newID = IDStack.pollLast(); // pop
			Players.put(newSocket, newID);
			System.out.println("A socket claimed ID: " + newID); // DEBUG
			return true;
		}
		return false;
	}
	
	public boolean removeSocket(InetSocketAddress oldSocket)
	{
		if( IDStack.size() < 4 )
		{
			// Get ID of socket (not synchronized)
			int oldID = 0;
			
			Players.remove(oldSocket);
			
			if(oldID == 0)
				return false;
			
			if(IDStack.offerLast(oldID)) // push
			{
				System.out.println("A socket gave up ID: " + oldID); // DEBUG
				return true;
			}
		}
		return false;
	}
	
	public int getID(InetSocketAddress socket)
	{
		return Players.get(socket);
	}

	public Set<InetSocketAddress> getPlayerSockets()
	{
		return Players.keySet();
	}

	public void resetTimeout(InetSocketAddress client) {
		
		pls.resetTimeout(client);
		
	}
	
	public void broadcast(String message) throws IOException
	{
		broadcast(message, -1);
	}
	
	public void broadcast(String message, int excludeID) throws IOException
	{
		for(Entry<InetSocketAddress, Integer> entry : Players.entrySet())
		{
			if(entry.getValue() != excludeID)
			{
				ServerSideSender.singleton().sendRawMessage(entry.getKey(),message);
			}
		}
	}
	
}
// Call RoomManager.start() later