import java.io.Serializable;
import java.util.Vector;

public class Cruiser extends Ship implements Serializable {
	//will inherit other default parameters
	
	private static final long serialVersionUID = 1288206311412279936L;

	Cruiser(){
		setMaxHealth(75);
		setCurHealth(75);
		setLength(3);
		setShipType("Cruiser");
	}
	
	//constructor with a specified location
	Cruiser(Vector<Tile> tiles){
		setMaxHealth(75);
		setCurHealth(75);
		setLength(3);
		setShipType("Cruiser");
		//setTilesOn(tiles);
	}
}
