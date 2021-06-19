
package TankGame.GameObject.Movable;

import TankGame.TankGameClient;
import TankGame.GameObject.Unmovable.BreakableWall;
import TankGame.GameObject.Unmovable.Wall;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

public class Projectile extends Movable implements Observer {

	private final BufferedImage bullet;
	private int theta;
	private int damage;
	private TankGameClient tankGame;
	public int xSize;
	public int ySize;
	public static Tank owner;
	public boolean visible;

	public Projectile(TankGameClient tw, BufferedImage img, int speed, Tank t, int dmg) {
		super(img, t.getTankCenterX(), t.getTankCenterY(), speed);
		bullet = img;
		damage = dmg;
		xSize = img.getWidth(null);
		ySize = img.getHeight(null);
		owner = t;
		theta = owner.getAngle();
		visible = true;

		this.tankGame = tw;
	}

	public void setTankWorld(TankGameClient tw) {
		this.tankGame = tw;
	}

	public static Tank getTank() {
		return owner;
	}

	@Override
	public void update(Observable o, Object arg) {
		update();
	}

	public void draw(ImageObserver iobs, Graphics2D g) {
		AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
		rotation.rotate(Math.toRadians(theta), 0, 0);
		g.drawImage(bullet, rotation, iobs);
	}

	public int getTheta() {
		return this.theta;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	// @Override
	public void update() {
		y += Math.round(speed * Math.sin(Math.toRadians(theta)));
		x += Math.round(speed * Math.cos(Math.toRadians(theta)));
		
		for (Tank player : tankGame.getPlayers().values()) {
			if (player.collision(this) && visible && owner != player && visible && player.coolDown <= 0) {
				if(Math.abs(player.getId() - owner.getId())%2 == 0) {
					continue;
				}
				
				if (visible) {
//					tankGame.playSound(3);// breakable collision sound
					tankGame.getSound(3).getClip().setFramePosition(0);
				}
				visible = false;
				player.bulletDamage(damage);
			} else {
				for (int i = 0; i < tankGame.getWallSize(); i++) {
					Wall tempWall = tankGame.getWalls().get(i);
					if ((tempWall.getWallRectangle().intersects(this.x, this.y, this.width, this.height)) && visible) {
						this.visible = false;
//						tankGame.playSound(2);// unbreakable collision sound
						tankGame.getSound(2).getClip().setFramePosition(0);
					}
					
					for (int j = 0; j < tankGame.getBreakableWallSize(); j++) {
						BreakableWall tempWall2 = tankGame.getBreakableWalls().get(j);
						if ((tempWall2.getWallRectangle().intersects(this.x, this.y, this.width, this.height)) && visible) {
							tankGame.getBreakableWalls().remove(j);
							tempWall2.breakWall();
							this.visible = false;
//							tankGame.playSound(3);// breakable collision sound
							tankGame.getSound(3).getClip().setFramePosition(0);
						}
					}
				}
			}
		}

	}
}
