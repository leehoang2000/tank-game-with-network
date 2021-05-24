package client.message;

import java.io.IOException;

import TankGame.GameObject.Movable.Tank;

public class TankPosSendService extends Thread {
	
	private Tank tank;
	
	public TankPosSendService(Tank tank) {
		
		this.tank = tank;
	}

	@Override
	public void run()
	{
		while(true)
		{
			try {
				sleep(10);
				ClientSideSender.singleton().sendTankPosMessage(
						tank.id, tank.getTankCenterX(), tank.getTankCenterY()
				);
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
