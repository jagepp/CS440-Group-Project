import java.io.Serializable;
import java.util.Vector;

public class Carrier extends Ship implements Serializable {
	
	private static final long serialVersionUID = -5563980837009636097L;

	//will inherit other default parameters
	Carrier(){
		setMaxHealth(125);
		setCurHealth(125);
		setLength(5);
		setShipType("Carrier");
	}
	
	//constructor with a specified location
	Carrier(Vector<Tile> tiles){
		setMaxHealth(125);
		setCurHealth(125);
		setLength(5);
		setShipType("Carrier");
		//setTilesOn(tiles);
	}
}