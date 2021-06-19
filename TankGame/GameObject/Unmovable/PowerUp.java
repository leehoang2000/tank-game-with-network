
package TankGame.GameObject.Unmovable;

import TankGame.GameObject.Movable.Tank;
import TankGame.TankGameClient;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public class PowerUp extends Unmovable implements Observer {

	boolean pickedUp = false;
	private TankGameClient tankGame;

	public PowerUp(int x, int y, int width, int height, BufferedImage img, TankGameClient tankGame) {
		super(x, y, width, height, img);
		this.tankGame = tankGame;
		
	}

	public void draw(Graphics g) {
		if (!pickedUp)
			g.drawImage(this.img, this.x, this.y, this);
	}

	@Override
	public void update(Observable o, Object arg) {
		update();
	}

	public void update() {
//		Tank p1 = TankWorld.getTank(1);
		for (Tank p1 : tankGame.getPlayers().values()) {
			if (p1.collision(this)) {
				p1.healthUp();
				pickedUp = true;
			}
		}
	}

}
