
import java.util.Vector;

public class Shop extends Tile{

	private Vector<Powerups> inventory;
	private Point location; 
	Shop(){
		this.setType('S');
		inventory = new Vector<Powerups>();
		inventory.add(new repair());
		inventory.add(new doubleDamage());
		inventory.add(new VerticalLineGun());
		inventory.add(new HorizontalLineGun());
		inventory.add(new doubleShotGun());
		inventory.add(new Torpedo());
		inventory.add(new add10Moves());
		inventory.add(new addPoints());
		setLocation(new Point(0,0));//default location is top left
	}
	
	public Vector<Powerups> getItems(){
		return inventory;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	
}
