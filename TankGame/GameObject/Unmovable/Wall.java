
package TankGame.GameObject.Unmovable;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import TankGame.TankGameClient;
import TankGame.GameObject.Movable.Tank;

public class Wall extends Unmovable implements Observer {
	Rectangle wallRect;
	private int height, width;
	private TankGameClient tankGame;

	public Wall(int x, int y, int width, int height, BufferedImage img, TankGameClient tankGame) {
		super(x, y, width, height, img);
		this.height = img.getHeight();
		this.width = img.getWidth();
		this.tankGame = tankGame;
		wallRect = new Rectangle(x, y, width, height);
	}

	public void draw(Graphics g) {
		g.drawImage(this.img, this.x, this.y, this);
	}

	@Override
	public void update(Observable o, Object arg) {
		update();
	}

	public Rectangle getWallRectangle() {
		return wallRect;
	}

	public void update() {
//        Tank p1 = TankWorld.getTank(1);
		for (Tank p1 : tankGame.getPlayers().values()) {
			if (p1.collision(this)) {
				if (p1.x > (x)) {
					p1.x += 3;
				} else if (p1.x < (this.x)) {
					p1.x -= 3;
				}
				if (p1.y > (this.y)) {
					p1.y += 3;
				} else if (p1.y < this.y) {
					p1.y -= 3;
				}
			}
		}
	}
}
