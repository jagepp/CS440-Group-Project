import java.io.Serializable;
import java.util.Vector;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 7042959260388661968L;
	
	public static final int SIZE =  10;
	public static final int ROW_SIZE = 15;
	public static final int COL_SIZE = 15;
	
	protected String name; // name to identify unique  player
	protected Vector<Ship> ships;
	//private int[][] board;  // board to store ship positions
	protected int[][] target_grid;  // store  hit/miss at each coordinate
	protected int numMoves;
	protected int points;
	private Vector<Powerups> powerups;
	
	public Player(String name1){
		target_grid = new int[SIZE][SIZE];
		ships = new Vector<>();
		ships.setSize(4);
		this.name = name1;
		numMoves = 8;
		points = 0;
	}
	Player(){}
	
	// method to get ship that was attacked if there was any
	
	
	// method to store hits and misses of players attacks
	
	public void addPoints(int p) {
		this.points += p;
	}
	public int getPoints() {
		return points;
	}
	public void shot_info(int x, int y, boolean hit) {
		target_grid[x][y] = (hit) ? 1 : -1;
	}
	
	public Vector<Ship> getShips() {
		return ships;
	}
	
	public void setShip(Ship s, int i) {
		ships.set(i, s);
	}
	
	public String getName() {
		return this.name;
	}

	public int getMoves() {
		return this.numMoves;
	}
	
	public void setMoves(int moves) {
		this.numMoves = moves;
	}
	
	public Vector<Powerups> getPowerups() {
		return powerups;
	}

	public void setPowerups(Vector<Powerups> powerups) {
		this.powerups = powerups;
	}

	public boolean isAllSunk() // Function used to check whether all the player's ships have been sunk
	{
		for (Ship x : ships)
		{
			if(!(x.isSunk())) // If a ship is not sunk
				return false;
		}
		return true;
	}
}
