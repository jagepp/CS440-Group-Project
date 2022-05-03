import java.io.Serializable;
import java.util.Vector;

public class Ship implements Serializable {
	

	private static final long serialVersionUID = -7246818825188567327L;
	
	private int maxHealth;
	private int curHealth;
	private boolean damaged;
	private boolean sunk;
	private boolean recentHit;
	private Weapon weapon;
	private Vector<Point> pointsOn;
	private int length;
	private String shipType;
	private boolean pointsChoosen;
	
	
	//getters and setters for ship data	
	
	public void setFlag(boolean b) {
		pointsChoosen = b;
	}
	
	public boolean getFlag() {
		return this.pointsChoosen;
	}

	public int getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(int curHealth) {
		this.maxHealth = curHealth;
	}
	public int getCurHealth() {
		return curHealth;
	}
	public void setCurHealth(int curHealth) {
		this.curHealth = curHealth;
	}
	public boolean isDamaged() {
		return damaged;
	}
	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
	}
	public Weapon getWeapon() {
		return weapon;
	}
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	public Vector<Point> getPointsOn() {
		return pointsOn;
	}
	public void setPointsOn(Vector<Point> points) {		//this may or may not work depending on the Tile class
		if(points.size() != length) {
			return;
		}
		
		pointsOn = points;

	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getShipType() {
		return shipType;
	}
	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	public boolean isSunk() {
		return sunk;
	}

	public void setSunk(boolean sunk) {
		this.sunk = sunk;
	}
	
	public boolean isRecentHit() {
		return recentHit;
	}

	public void setRecentHit(boolean recentHit) {
		this.recentHit = recentHit;
	}
	
	
	//end getters and setters for ship data

	public void takeDamage(int d) {	//	standard damage deduction
		if(curHealth - d >= 0)	//	don't want negative values
			curHealth -= d;
		else
			curHealth = 0;
    	setDamaged(true);
    }
		
	Ship(){//standard ship without parameters 
		maxHealth = curHealth = 0;
		setDamaged(false);
		weapon = new Weapon();
		pointsOn = null;                  
		length = 0;
		shipType = "Default ship class";
		pointsOn = new Vector<Point>();
		pointsChoosen = false;
		sunk =false;
		setRecentHit(false);
	}
	
	//if we want to manually create a ship, but there will be default inherited classes for normal ships
	
	Ship(int health, Weapon weaponType, Vector<Point> points, int shipSize, String name){
		maxHealth = curHealth = health;
		setDamaged(false);
		weapon = weaponType;
		pointsOn = points;                  
		length = shipSize;
		shipType = name;
		pointsChoosen = false;
		sunk = false;
	}
}
