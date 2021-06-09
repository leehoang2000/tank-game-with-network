package server.player;

public class Player {
	private int id;
	private int health;
	private boolean isDead;
	
	public Player(int id, int health) {
		super();
		this.id = id;
		this.health = health;
		this.isDead = false;
	}
	
	public Player(int id) {
		super();
		this.id = id;
		//Default value
		this.health = 100;
		//Default value
		this.isDead = false;
	}

	public int getID() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	
	public boolean handleHealthChange(int newHealthValue) {
		if(newHealthValue <=0) {
			this.isDead = true;
		}
		if(newHealthValue != this.health){
			this.health = newHealthValue;
			return true;
		}
		return false;
	}
	
	public boolean isDead() {
		return isDead;
	}
}
