package TankGame;

import TankGame.GameObject.Movable.Projectile;
import TankGame.GameObject.Movable.Tank;
import TankGame.GameObject.Unmovable.BreakableWall;
import TankGame.GameObject.Unmovable.PowerUp;
import TankGame.GameObject.Unmovable.Wall;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

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

    private int mapWidth, mapHeight,
            windowWidth, windowHeight,
            minimapWidth, minimapHeight;

    private ArrayList<Wall> walls;
    private ArrayList<BreakableWall> bwalls;
    private ArrayList<PowerUp> pups;
    private ArrayList<Projectile> bullets;

    private Tank tank1;

    // player windows
    BufferedImage p1w, p2w;
    Image minimap;

    // player bound checking
    private int p1WindowBoundX, p1WindowBoundY, p2WindowBoundX, p2WindowBoundY;
    
    public Scene() {
    }

    public Scene(int mapWidth, int mapHeight, int windowWidth, int windowHeight,
            String backgroundPath, String[] imgPaths) {
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
    }

    @Override
    public void paintComponent(Graphics g) {
        getGameImage();
        super.paintComponent(g);

        g.drawImage(p1w, 0, 0, this); // draw player 1 window
//        g.drawImage(p2w, windowWidth / 2, 0, this); // draw player 2 window
        // old minimap place
        drawPlayerStatus(g);
        
        // borders
//        g.setColor(Color.YELLOW);
//        g.draw3DRect(0, 0, (windowWidth/2)-1, windowHeight-22, true);
//        g.draw3DRect(windowWidth/2, 0, (windowWidth/2)-1, windowHeight-2, true);
        
        g.drawImage(minimap, (windowWidth / 2) - (minimapWidth / 2), 0, this); // draw minimap
//        g.draw3DRect((windowWidth / 2) - (minimapWidth / 2), 0, minimapWidth, minimapHeight, true);
        
        // victory text
        if (tank1.getLife() == 0) {
            g.setFont(new Font(g.getFont().getFontName(), Font.CENTER_BASELINE, 84));
            g.drawString("PLAYER 2 WINS", 64, windowHeight/2);
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
        playerViewBoundChecker();
        p1w = bimg.getSubimage(this.p1WindowBoundX, this.p1WindowBoundY, windowWidth, windowHeight);
        minimap = bimg.getScaledInstance(minimapWidth, minimapHeight, Image.SCALE_SMOOTH);
    }

    // CREDIT
    private void playerViewBoundChecker() {
        if ((this.p1WindowBoundX = tank1.getTankCenterX() - windowWidth / 2) < 0) {
            this.p1WindowBoundX = 0;
        } else if (this.p1WindowBoundX >= mapWidth - windowWidth) {
            this.p1WindowBoundX = (mapWidth - windowWidth);
        }

        if ((this.p1WindowBoundY = tank1.getTankCenterY() - windowHeight / 2) < 0) {
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
        this.tank1.draw(g2);
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

    private void drawPlayerStatus(Graphics g) {

        int p1_health = this.tank1.getHealth() * 2;

        int p1_lives = this.tank1.getLife();

        int p1_health_x = 22;
        int p1_health_y = 758;

        int health_width = 200;
        int health_height = 20;

        int coord_offset = 4;
        int size_offset = 8;

        // HEALTH FRAME
        g.setColor(Color.DARK_GRAY);
        g.fillRect(p1_health_x, p1_health_y, health_width, health_height); // p1

        // HEALTH DEPLIETED
        g.setColor(Color.GRAY);
        g.fillRect(p1_health_x + coord_offset, p1_health_y + coord_offset,
                health_width - size_offset, health_height - size_offset); // p1

        // HEALTH AVAILABLE
        g.setColor(Color.GREEN);
        g.fillRect(p1_health_x + coord_offset, p1_health_y + coord_offset,
                p1_health - size_offset, health_height - size_offset); // p1

        // Player 1 lives
        int p1_life_x = 230;
        int p1_life_y = 748;
        int p1_life_offset = 40;
        for (int i = 0; i < p1_lives; i++) {
            g.drawImage(lifeIcon1, p1_life_x + (i * p1_life_offset), p1_life_y, this);
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

    public void setTanks(Tank tank1) {
        this.tank1 = tank1;
    }

    public void setProjectiles(ArrayList<Projectile> p) {
        this.bullets = p;
    }

    public void setLifeIcons(BufferedImage img1) {
        this.lifeIcon1 = img1;
    }

    // GETTERS //
    public BufferedImage getBackgroundImage() {
        return this.bgImg;
    }
}