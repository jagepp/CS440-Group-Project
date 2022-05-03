import java.io.Serializable;
import java.util.Vector;

public class Submarine extends Ship implements Serializable {

	//will inherit other default parameters
	
	private static final long serialVersionUID = 3690443239862915086L;

	Submarine(){
		setMaxHealth(75);
		setCurHealth(75);
		setLength(3);
		setShipType("Submarine");
		
	}
	
	//constructor with a specified location
	
	Submarine(Vector<Tile> tiles){
		setMaxHealth(75);
		setCurHealth(75);
		setLength(3);
		setShipType("Submarine");
		//setTilesOn(tiles);
	}
	
}
