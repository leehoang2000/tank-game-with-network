package TankGame.GameObject.Movable;

import TankGame.GameObject.GameObject;
import client.message.ClientSideSender;
import TankGame.TankGameClient;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;


public class Tank extends Movable implements Observer {

    protected int coolDown = 0;
    protected int score = 0;
    protected int health = 100;
    public static int MAX_HEALTH = 100;
    protected int life = 1;
    private int angle = 0;
    private int mapSizeX, mapSizeY;
    protected int spawnPointX, spawnPointY;
    private int left, right, up, down;
    private int shootKey;
    private int shootCoolDown = 0;
    private boolean moveLeft, moveRight, moveUp, moveDown, shoot;
    private TankGameClient obj;
    private boolean isDead;
    
    public int id;

    public int getId() {
		return id;
	}

	public Tank() {
    }

    public Tank(TankGameClient obj, BufferedImage img, int x, int y, int speed, int left, int right, int up, int down, int shootKey, int id) {
        super(img, x, y, speed);
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.shootKey = shootKey;
        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;
        this.shoot = false;
        this.isDead = false;
        this.spawnPointX = x;
        this.spawnPointY = y;
        this.obj = obj;
        this.setBounds(8, 10, 49, 44);
        this.mapSizeX = obj.getMapWidth();
        this.mapSizeY = obj.getMapHeight();
        this.id = id;
    }

    public boolean collision(GameObject go) {
        objectRectangle = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle objectRectangle2 = new Rectangle(go.getX(), go.getY(), go.getWidth(), go.getHeight());
        if ((this.objectRectangle.intersects(objectRectangle2)) & (!isDead)) {
            return true;
        }
        return false;
    }

    //SETTERS
    public void setAngle(int a) {
        this.angle = a;
    }
    
    public void setScore(int s) {
        this.score += s;
    }

    public void setHealthPoints(int hp) {
        this.health = hp;
    }
    

    public void bulletDamage(int dmg) {
//    	System.out.println("Tank.bulletDamage| tank Health: " + this.health);
        if (coolDown <= 0) // originally not used
            this.health -= dmg;
    }

    public void healthUp() {
        if (this.health < 100){
            this.health = 100;
        }
    }

    public void switchUpOn() {
        this.moveUp = true;
    }

    public void switchDownOn() {
        this.moveDown = true;
    }

    public void switchLeftOn() {
        this.moveLeft = true;
    }

    public void switchRightOn() {
        this.moveRight = true;
    }

    public void switchUpOff() {
        this.moveUp = false;
    }

    public void switchDownOff() {
        this.moveDown = false;
    }

    public void switchLeftOff() {
        this.moveLeft = false;
    }

    public void switchRightOff() {
        this.moveRight = false;
    }

    public void switchShootOn() {
        this.shoot = true;
    }

    public void switchShootOff() {
        this.shoot = false;
    }
    
    public void setTankCenterX(int tankCenterX) {
    	this.x = tankCenterX - img.getWidth(null)/2;
    }
    
    public void setTankCenterY(int tankCenterY) {
    	this.y = tankCenterY - img.getHeight(null)/2;
    }

    //GETTERS
    public int getTankCenterX() {
        return x + img.getWidth(null) / 2;
    }

    public int getTankCenterY() {
        return y + img.getHeight(null) / 2;
    }

    public int getHealth() {
        return health;
    }

    public int getAngle() {
        return this.angle;
    }

    public int getLife() {
        return this.life;
    }

    public int getScore() {
        return this.score;
    }

    public int getUpKey() {
        return this.up;
    }

    public int getDownKey() {
        return this.down;
    }

    public int getLeftKey() {
        return this.left;
    }

    public int getRightKey() {
        return this.right;
    }

    public int getShootKey() {
        return this.shootKey;
    }

    public boolean isDead() {
        if (this.life <= 0) {
            return true;
        }
        return false;
    }


    public void draw(Graphics2D g) {
//        Tank p1 = TankWorld.getTank(1);
        
        this.shootCoolDown -= 1;
        if (this.health <= 0) {
            isDead = true;
            
            //STFU
//            if (life <= 0)
//                obj.playSound(1);
        }
        if ((health > 0) && (coolDown == 0) && (life > 0)) {
            isDead = false;
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate(Math.toRadians(angle), img.getWidth(null) / 2, img.getHeight(null) / 2);
            g.drawImage(img, rotation, null);
            for(Tank p1 : obj.getPlayers().values()) {
            	for(Tank p2 : obj.getPlayers().values()) {
                	if(!p1.equals(p2)) {
						if ((p1.collision(p2))) {
							if (p1.x > x) {
								p1.x += speed * 2;
								p2.x -= speed * 2;
							} else if (p1.x < x) {
								p1.x -= speed * 2;
								p2.x += speed * 2;
							}
							if (p1.y > y) {
								p1.y += speed * 2;
								p2.y -= speed * 2;
							} else if (p1.y < y) {
								p1.y -= speed * 2;
								p2.y += speed * 2;
							}
						}
                	}
                }
            }
        } else if ((isDead == true) && (coolDown == 0) && (life > 0)) {        	
            coolDown = 20; // original: 180
            if (life > 1) {
                obj.playSound(0); // normal death sound
                obj.getSound(0).getClip().setFramePosition(0);
            }
            if (--life >= 0) {
                if (life > 0) // FIXME: wasteful
                    health = 100; // 4
            }
            isDead = false;
            x = spawnPointX;
            y = spawnPointY;
        } else {
            coolDown -= 1;
        }
    }

    @Override
    public void update(Observable obj, Object arg) {
        try {
			shoot(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        update();
    }

    private void shoot(Tank a) throws SocketException, IOException {
        if (shoot && shootCoolDown <= 0 && coolDown <= 0 && life > 0) { 
        	//Send Shoot Message here!
//        	System.out.println("Tank.shoot| player " + this.id +"shootin!");

        	// added coolDown check => fixes shooting when spawning
            Projectile newBullet = new Projectile(this.obj, obj.getProjectileImg(), 5, this, 10);
            obj.getProjectile().add(newBullet);
            obj.addBulletToObservable(newBullet);
            this.shootCoolDown = 10;
            this.switchShootOff();
        }
    }

    private void checkLimit() {
        if (x < 0) {
            x = 0;
        }
        if (x >= mapSizeX) {
            x = mapSizeX;
        }
        if (y < 0) {
            y = 0;
        }
        if (y >= mapSizeY) {
            y = mapSizeY;
        }
    }


    public void update() {
        if (moveLeft == true){
            angle -= 3;
        }
        if (moveRight == true){
            angle += 3;
        }
        if (moveUp == true){
            x = ((int) (x + Math.round(speed * Math.cos(Math.toRadians(angle)))));
            y = ((int) (y + Math.round(speed * Math.sin(Math.toRadians(angle)))));
            checkLimit();
        }
        if (moveDown == true){
            x = ((int) (x - Math.round(speed * Math.cos(Math.toRadians(angle)))));
            y = ((int) (y - Math.round(speed * Math.sin(Math.toRadians(angle)))));
            checkLimit();
        }

        if (angle == -1) {
            angle = 359;
        } else if (angle == 361) {
            angle = 1;
        }

        if (coolDown > 0) {
            moveLeft = false;
            moveRight = false;
            moveUp = false;
            moveDown = false;
        }

    }

    public void printTankData() {
        System.out.printf("X: %d\tY: %d\tAngle: %d\n", this.x, this.y, this.angle);
    }
}