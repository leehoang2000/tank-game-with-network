package TankGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import TankGame.GameObject.Movable.Projectile;
import TankGame.GameObject.Movable.Tank;
import TankGame.GameObject.Unmovable.BreakableWall;
import TankGame.GameObject.Unmovable.PowerUp;
import TankGame.GameObject.Unmovable.Wall;

/**
 *
 * @author motiveg,monal
 */
/**
 * A scene displays all current game objects (including the background);
 * everything is drawn here.
 */
public class Scene extends JPanel {

	private BufferedImage bgImg; // background image
	private BufferedImage lifeIcon1, lifeIcon2;

	private int mapWidth, mapHeight, windowWidth, windowHeight, minimapWidth, minimapHeight;

	private ArrayList<Wall> walls;
	private ArrayList<BreakableWall> bwalls;
	private ArrayList<PowerUp> pups;
	private ArrayList<Projectile> bullets;
	private boolean displayLoseText;
	private boolean displayWinText;

	private TankGameClient tankGame;

//    private Tank tank1;

	// player windows
	BufferedImage p1w, p2w;
	Image minimap;

	// player bound checking
	private int p1WindowBoundX, p1WindowBoundY;

	public Scene(TankGameClient tankGame) {
		this.tankGame = tankGame;
	}

	public Scene(int mapWidth, int mapHeight, int windowWidth, int windowHeight, String backgroundPath,
			String[] imgPaths, TankGameClient tankGame) {
		super();
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.minimapWidth = 200;
		this.minimapHeight = 200;

		this.setSize(mapWidth, mapHeight);
		this.setPreferredSize(new Dimension(mapWidth, mapHeight));
		this.bgImg = setImage(backgroundPath);

		walls = new ArrayList<>();
		bwalls = new ArrayList<>();
		pups = new ArrayList<>();
		bullets = new ArrayList<>();

		this.displayLoseText = false;
		this.displayWinText = false;
		this.Tanks = new ArrayList<Tank>();
		
		this.tankGame = tankGame;
	}

	@Override
	public void paintComponent(Graphics g) {
		getGameImage();
		super.paintComponent(g);

		// draw player 1 window
		g.drawImage(p1w, 0, 0, this); 
		drawHUD(g);
		// draw minimap
		g.drawImage(minimap, (windowWidth / 2) - (minimapWidth / 2), 0, this);

		if (displayLoseText) {
			g.setColor(Color.red);
			g.setFont(new Font(g.getFont().getFontName(), Font.CENTER_BASELINE, 84));
			g.drawString("FATALITY", windowWidth / 4, windowHeight / 2);
		}
		if (displayWinText) {
			g.setColor(Color.green);
			g.setFont(new Font(g.getFont().getFontName(), Font.CENTER_BASELINE, 84));
			g.drawString("WE WON!", windowWidth / 4, windowHeight / 2);
		}

	}

	public void getGameImage() {
		// create buffered image
		BufferedImage bimg = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bimg.createGraphics();

		// draw to g2
		drawBackground(g2);
		drawMapLayout(g2);
		drawTanks(g2);
		drawProjectiles(g2);

		// create subimages from g2
		if (tankGame.getCurrentID() >= 0) {
			playerViewBoundChecker(tankGame.getPlayers().get(tankGame.getCurrentID()));
		}

		p1w = bimg.getSubimage(this.p1WindowBoundX, this.p1WindowBoundY, windowWidth, windowHeight);
		minimap = bimg.getScaledInstance(minimapWidth, minimapHeight, Image.SCALE_SMOOTH);
	}

	// CREDIT
	private void playerViewBoundChecker(Tank tank) {
		if ((this.p1WindowBoundX = tank.getTankCenterX() - windowWidth / 2) < 0) {
			this.p1WindowBoundX = 0;
		} else if (this.p1WindowBoundX >= mapWidth - windowWidth) {
			this.p1WindowBoundX = (mapWidth - windowWidth);
		}

		if ((this.p1WindowBoundY = tank.getTankCenterY() - windowHeight / 2) < 0) {
			this.p1WindowBoundY = 0;
		} else if (this.p1WindowBoundY >= mapHeight - windowHeight) {
			this.p1WindowBoundY = (mapHeight - windowHeight);
		}
	}

	private void drawBackground(Graphics2D g) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				g.drawImage(this.bgImg, this.bgImg.getWidth() * i, this.bgImg.getHeight() * j, this);
			}
		} // end loops
	}

	private void drawMapLayout(Graphics2D g) {
		walls.forEach((curr) -> {
			curr.draw(g);
		});
		bwalls.forEach((curr) -> {
			curr.draw(g);
		});
		pups.forEach((curr) -> {
			curr.draw(g);
		});
	}

	private void drawTanks(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g;
		for (Tank tank : Tanks) {
			tank.draw(g2);
		}
	}

	private synchronized void drawProjectiles(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g;
		try {
			bullets.forEach((curr) -> {
				if (curr.isVisible()) {
					curr.draw(this, g2);
				}

			});
		} catch (ConcurrentModificationException e) {
		}
	}

	private void drawHUD(Graphics g) {

		int health_width = 150;
		int health_height = 20;
		int coord_offset = 4;

//        int coord_offset[] = new int[]{0,0,0,0};
		int size_offset = 8;

		for (Tank tank : Tanks) {
			int p1_lives = tank.getLife();
			
			Color healthColor = Color.GREEN;
			
			int	healthBarPosX = tank.getTankCenterX() - this.p1WindowBoundX -60;
			int	healthBarPosY = tank.getTankCenterY() - this.p1WindowBoundY +30;
			
			if(!(tank.id == tankGame.getCurrentID()) && !(tank.id == tankGame.getAllyID())) {
				healthColor = Color.RED;
			}

			// HEALTH FRAME
			g.setColor(Color.DARK_GRAY);
//			g.fillRect(p1_health_x[id], p1_health_y, health_width, health_height); // p1
			g.fillRect(healthBarPosX, healthBarPosY, health_width, health_height); // p1
			

			// HEALTH DEPLETED
			g.setColor(Color.GRAY);
			g.fillRect(healthBarPosX + coord_offset, healthBarPosY + coord_offset, health_width - size_offset,
					health_height - size_offset); // p1

			// HEALTH AVAILABLE
			g.setColor(healthColor);
			g.fillRect(healthBarPosX + coord_offset, healthBarPosY + coord_offset,
					(int) (((float) tank.getHealth() / Tank.MAX_HEALTH) * (health_width - size_offset)),
					health_height - size_offset); // p1

			// Player 1 lives
			int p1_life_x = 230;
			int p1_life_y = 748;
			int p1_life_offset = 40;
			for (int i = 0; i < p1_lives; i++) {
				g.drawImage(lifeIcon1, p1_life_x + (i * p1_life_offset), p1_life_y, this);
			}
		}
	}

	// SETTERS //
	private BufferedImage setImage(String filepath) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filepath));
		} catch (IOException e) {
			System.out.println("Error getting image. " + e.getMessage());
		}
		return img;
	}

	public void setBackgroundImage(BufferedImage img) {
		this.bgImg = img;
	}

	public void setMapObjects(ArrayList<Wall> w, ArrayList<BreakableWall> b, ArrayList<PowerUp> p) {
		this.walls = w;
		this.bwalls = b;
		this.pups = p;
	}

	private List<Tank> Tanks;

	public void addTank(Tank tank) {
		this.Tanks.add(tank);
	}

	public void setProjectiles(ArrayList<Projectile> p) {
		this.bullets = p;
	}

	public void setLifeIcons(BufferedImage img1) {
		this.lifeIcon1 = img1;
	}

	public void setupWinText() {
		this.displayWinText = true;
	}

	public void setupLoseText() {
		this.displayLoseText = true;
	}

	// GETTERS //
	public BufferedImage getBackgroundImage() {
		return this.bgImg;
	}
}