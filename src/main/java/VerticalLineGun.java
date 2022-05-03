import java.io.Serializable;
import java.util.Vector;

public class VerticalLineGun extends Weapon implements Serializable {

	private static final long serialVersionUID = -4224432739355566637L;

	VerticalLineGun(){
		setWeaponName("Vertical Line Gun");
		setWeaponDamage(25);
		setCost(75);
		setName("Vertical Line Gun");
	}
	
	// The vector of points to check for a hit
	public Vector<Point> getAllCoordinates(Point p){
		Vector<Point> vect = new Vector<>();
		for(int i = 0; i < 10; i++) {
			Point newPoint = new Point(i, p.y);
			if(validPoint(newPoint)) {
				vect.add(newPoint);
			}
		}
		return vect;
	}
	
	public void doAction(Ship ship,Player player) {
		ship.setWeapon(new VerticalLineGun());
	}
}
