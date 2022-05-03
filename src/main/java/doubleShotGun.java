import java.io.Serializable;

public class doubleShotGun extends Weapon implements Serializable {
	
	private static final long serialVersionUID = 122435373532318645L;
	// make a condition in fire statement that lets us shoot only if at least one of the two is true
	// if both are false, it means both shots have been fired
	boolean shotOnce = false;
	boolean shotTwice = false;
	
	doubleShotGun(){
		setWeaponName("Double Shot Gun");
		setWeaponDamage(25);
		setCost(75);
		setName("Double Shot");
	}
	
	// checks to see how many shots we can shoot left
	public int numShootLeft() {
		if(shotOnce == false) {
			return 2;
		}
		else if(shotTwice == false){
			return 1;
		}
		else {
			return 0;
		}
	}
	
	// do this function only if fired a shot
	public void hasShot() {
		if(shotOnce == false) {
			shotOnce = true;
			//return 1;
		}
		else if(shotTwice == false) {
			shotTwice = true;
			//return 0;
		}
		else {
			System.out.println("No more shots left\n");
			//return -1;
		}
	}
	
	// do this once we restart our turn
	public void resetShots() {
		shotOnce = false;
		shotTwice = false;
	}
	
	public void doAction(Ship ship,Player player) {
		ship.setWeapon(new doubleShotGun());
	}
}
