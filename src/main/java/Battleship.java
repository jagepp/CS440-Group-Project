import java.io.Serializable;
import java.util.Vector;

public class Battleship extends Ship implements Serializable{
	
	private static final long serialVersionUID = 1335237655442611215L;

	//will inherit other default parameters
	Battleship(){
		setMaxHealth(100);
		setCurHealth(100);
		setLength(4);
		setShipType("Battleship");
		
	}
	
	//constructor with a specified location
	Battleship(Vector<Tile> tiles){
		setMaxHealth(100);
		setCurHealth(100);
		setLength(4);
		setShipType("Battleship");
		//setTilesOn(tiles);
	}
}
