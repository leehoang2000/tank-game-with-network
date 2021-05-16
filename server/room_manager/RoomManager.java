// Each room has one RoomManager

package server.room_manager;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RoomManager extends Thread
{
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

}
// Call RoomManager.start() later