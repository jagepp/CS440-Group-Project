import java.io.Serializable;
import java.util.Vector;

public class Torpedo extends Weapon implements Serializable {
	
	private static final long serialVersionUID = -1213695383155479441L;

	Torpedo(){
		setWeaponName("Torpedo");
		setWeaponDamage(25);
		setCost(75);
		setName("Torpedo");
	}
	
	public void doAction(Ship ship,Player player) {
		ship.setWeapon(new Torpedo());
	}
	
	// Will have to work with hitDetection in a speical way
	// have hitDetection detect for all of these
	Vector<Point> pointToHit(Point attackPoint){
		Vector<Point> vec = new Vector<>();
		vec.add(attackPoint);
		
		Point newPoint1 = new Point((attackPoint.x)-1, attackPoint.y);
		Point newPoint2 = new Point((attackPoint.x)-1, (attackPoint.y)-1);
		Point newPoint3 = new Point((attackPoint.x), (attackPoint.y)-1);
		Point newPoint4 = new Point((attackPoint.x)+1, (attackPoint.y)-1);
		Point newPoint5 = new Point((attackPoint.x)+1, (attackPoint.y));
		Point newPoint6 = new Point((attackPoint.x)+1, (attackPoint.y)+1);
		Point newPoint7 = new Point((attackPoint.x), (attackPoint.y)+1);
		Point newPoint8 = new Point((attackPoint.x)-1, (attackPoint.y)+1);


		if(validPoint(newPoint1)) {
			vec.add(newPoint1);
		}
		if(validPoint(newPoint2)) {
			vec.add(newPoint2);	
		}
		if(validPoint(newPoint3)) {
			vec.add(newPoint3);
		}
		if(validPoint(newPoint4)) {
			vec.add(newPoint4);
		}
		if(validPoint(newPoint5)) {
			vec.add(newPoint5);
		}
		if(validPoint(newPoint6)) {
			vec.add(newPoint6);
		}
		if(validPoint(newPoint7)) {
			vec.add(newPoint7);
		}
		if(validPoint(newPoint8)) {
			vec.add(newPoint8);
		}
		
		return vec;
		
	}
	
	
}
