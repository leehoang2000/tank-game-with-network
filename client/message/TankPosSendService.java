package client.message;

import java.io.IOException;

import TankGame.TankGameClient;
import TankGame.GameObject.Movable.Tank;

public class TankPosSendService extends Thread {
	
	private Tank tank;
	private TankGameClient tankGame;
	private ClientSideSender clientSideSender;
	
	public TankPosSendService(Tank tank,ClientSideSender clientSideSender, TankGameClient tankGame) {
		this.tank = tank;
		this.clientSideSender = clientSideSender;
		this.tankGame = tankGame;
	}

	@Override
	public void run()
	{
		while(tankGame.isRunning())
		{
			try {
				sleep(30);
//				System.out.println("Sending tankPos Message");
				clientSideSender.sendTankPosMessage(
						tank.id, tank.getTankCenterX(), tank.getTankCenterY(), tank.getAngle()
				);
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
