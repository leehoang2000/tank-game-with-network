package client.message;

import java.io.IOException;

import TankGame.TankWorld;
import TankGame.GameObject.Movable.Tank;

public class TankPosSendService extends Thread {
	
	private Tank tank;
	
	public TankPosSendService(Tank tank) {
		this.tank = tank;
	}

	@Override
	public void run()
	{
		while(TankWorld.singleton().isRunning())
		{
			try {
				sleep(30);
//				System.out.println("Sending tankPos Message");
				ClientSideSender.singleton().sendTankPosMessage(
						tank.id, tank.getTankCenterX(), tank.getTankCenterY(), tank.getAngle()
				);
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
