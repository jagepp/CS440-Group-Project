import java.util.Random;
import java.util.Vector;

public class AI extends Player {
	
	
	
	private class lastShot{
		public Point location;
		public boolean hit;//true for hit,false otherwise
		
		lastShot(){
			location = new Point(-1,-1);
			hit = false;
		}
		
	}
	
	private lastShot last;
	
	AI() {
		name = "AI";
		ships = new Vector<>();
		ships.setSize(4); 
		target_grid = new int[SIZE][SIZE]; 
		numMoves = 8;
		points = 0;		
		last = new lastShot();
	}

	
	public void setLastShot(Point p, Boolean b) {
		last.hit = b;
		last.location = p;
	}
	
	
	Point nextShot() {
		if(last.hit == true) {
			return last.location;
		}
		int rowMax = ROW_SIZE;
		int colMax = COL_SIZE;
		Random rand = new Random();
		int row = rand.nextInt(rowMax);
		int col = rand.nextInt(colMax);
		return new Point(row,col);
	
	}
	
}
