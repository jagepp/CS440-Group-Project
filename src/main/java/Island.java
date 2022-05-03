
public class Island extends Tile{

	private boolean p1Visited;
	private boolean p2Visited;
	private int points;
	
	Island()
	{
		this.setType('I');
		setP1Visited(false);
		setP2Visited(false);
		
		int multiplier = (int) (Math.random() * 3 + 1); // Random int from 1-3
		points = 25 * multiplier;
	}
	
	public boolean p1Visited() {
		return p1Visited;
	}
	
	public void setP1Visited(boolean p1Visited) {
		this.p1Visited = p1Visited;
	}
	
	public boolean p2Visited() {
		return p2Visited;
	}
	
	public void setP2Visited(boolean p2Visited) {
		this.p2Visited = p2Visited;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
