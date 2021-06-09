package client.message;

import java.io.IOException;

import TankGame.TankWorld;
import TankGame.GameObject.Movable.Tank;

public class HealthValueSendService extends Thread{
	private Tank tank;
	
	public HealthValueSendService(Tank tank) {
		this.tank = tank;
	}

	@Override
	public void run()
	{
		while(TankWorld.singleton().isRunning())
		{
			try {
				sleep(200);
//				System.out.println("Client.HealthValueSendService| sending health message");
				ClientSideSender.singleton().sendCurrentHealthMessage(tank.id, tank.getHealth());
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
