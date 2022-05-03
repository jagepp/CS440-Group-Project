import java.io.Serializable;
import java.util.Vector;

public class HorizontalLineGun extends Weapon implements Serializable {

	private static final long serialVersionUID = -4649617150352016609L;

	HorizontalLineGun(){
		setWeaponName("Horizontal Line Gun");
		setWeaponDamage(25);
		setCost(75);
		setName("Horizontal Line Gun");
	}
	
	// The vector of points to check for a hit
	public Vector<Point> getAllCoordinates(Point p){
		Vector<Point> vect = new Vector<>();
		for(int i = 0; i < 10; i++) {
			Point newPoint = new Point(p.x, i);
			if(validPoint(newPoint)) {
				vect.add(newPoint);
			}
		}
		return vect;
	}
	
	public void doAction(Ship ship,Player player) {
		ship.setWeapon(new HorizontalLineGun());
	}
}
