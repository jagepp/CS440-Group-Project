
public class Ocean extends Tile{
	// An Ocean tile is the default 
	private boolean hasMine = false;
	
	Ocean()
	{
		this.setType('O');
	}
	public boolean isMined() 	// hasMine getter
	{
		return hasMine;
	}
	public void addMine()   	// hasMine add setter
	{
		hasMine = true;
	}
	public void explodeMine() 	// hasMine remove setter
	{
		hasMine = false;
	}
}
