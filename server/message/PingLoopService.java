package server.message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import server.player.Player;

public class PingLoopService extends Thread {
	
	private Map<InetSocketAddress, Player> Players;
	private Map<InetSocketAddress, Timeout> Timeouts;
	
	public PingLoopService(Map<InetSocketAddress, Player> Players) 
	{
		this.Players = Players;
		if(Timeouts == null)
			Timeouts = new ConcurrentHashMap<InetSocketAddress, Timeout>();
	}

	@Override
	public void run()
	{
//		TestThread tt = new TestThread();
//		tt.start();
		
		while(true)
		{
			try {		
//				playerSockets = UDPServer.roomManager.getPlayerSockets();
				for(Entry<InetSocketAddress, Player> entry : Players.entrySet())
				{
					
					Timeout timeout = new Timeout(entry.getValue().getID());
					Timeouts.put(entry.getKey(), timeout);
//					System.out.println("PingLoopService.run : timeouts array length: " + Timeouts.size());
					timeout.start();
					
					ServerSideSender.singleton().sendPingMessage(entry.getKey());
				}
				sleep(6000);
					
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Removes player after sleep
	private class Timeout extends Thread
	{
		private int playerID;
		
		public Timeout(int playerID) 
		{
			this.playerID = playerID;
		}
		
		@Override
		public void run()
		{
			try {
				sleep(3000);
				for (Entry<InetSocketAddress, Player> entry : Players.entrySet()) 
				{
			        if(entry.getValue().getID() == playerID)
			        {
			        	Players.remove(entry.getKey());
			        	System.out.println(playerID + " has left the locale");
			        	break;
			        }
			    }
				
			} catch (InterruptedException e) {
				
				
				
			}
		}
	}

	public void resetTimeout(InetSocketAddress client) {
//		System.out.println("Client interrupted: " + client.toString());
//		System.out.println("timeouts array length: " + Timeouts.size());
		for (Entry<InetSocketAddress, Timeout> entry : Timeouts.entrySet()) 
		{
//			System.out.println("Entry: Client: " + entry.getKey().toString());
		}
		
		Timeouts.get(client).interrupt();
		Timeouts.remove(client);
		
	}
	
	private class TestThread extends Thread
	{
		@Override
		public void run()
		{
			while(true)
			{
				System.out.println("TestThread.run : timeouts array length: " + Timeouts.size());
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
