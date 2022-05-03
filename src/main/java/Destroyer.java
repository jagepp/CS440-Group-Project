import java.io.Serializable;
import java.util.Vector;

public class Destroyer extends Ship implements Serializable {
	
	//will inherit other default parameters
	
	private static final long serialVersionUID = -262455924058156718L;

	Destroyer(){
		setMaxHealth(50);
		setCurHealth(50);
		setLength(2);
		setShipType("Destroyer");
	}
	
	//constructor with a specified location
	
	Destroyer(Vector<Tile> tiles){
		setMaxHealth(50);
		setCurHealth(50);
		setLength(2);
		setShipType("Destroyer");
		
		//setTilesOn(tiles);
	}
	
}