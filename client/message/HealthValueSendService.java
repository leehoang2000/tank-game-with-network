package client.message;

import java.io.IOException;

import TankGame.TankGameClient;
import TankGame.GameObject.Movable.Tank;

public class HealthValueSendService extends MessageSendService{
	private Tank tank;
	private TankGameClient tankGame;
	
	public HealthValueSendService(Tank tank, TankGameClient tankGame, ClientSideSender clientSideSender) {
		super(clientSideSender);
		this.tank = tank;
		this.tankGame = tankGame;
	}

	@Override
	public void run()
	{
		while(tankGame.isRunning())
		{
			try {
				sleep(200);
//				System.out.println("Client.HealthValueSendService| sending health message");
				this.clientSideSender.sendCurrentHealthMessage(tank.id, tank.getHealth());
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
