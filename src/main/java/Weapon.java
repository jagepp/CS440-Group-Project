import java.io.Serializable;

public class Weapon extends Powerups implements Serializable {

	private static final long serialVersionUID = 8337087366137632198L;
	
	private int weaponDamage;
	private String weaponName;
		
	public String getWeaponName() {
		return weaponName;
	}
	public void setWeaponName(String weaponName) {
		this.weaponName = weaponName;
	}
	public int getWeaponDamage() {
		return weaponDamage;
	}
	public void setWeaponDamage(int weaponDamage) {
		this.weaponDamage = weaponDamage;
	}
	
	
	//defaults to a standard barrage attack
	Weapon(){
		weaponName = "Artillery Barrage";
		weaponDamage = 25;
		
	}
	
	Weapon(int damage, String name){
		weaponName = name;
		weaponDamage = damage;
	}

		// is it a valid point to attack
	public boolean validPoint(Point p) {
		if(p.x < 0 || p.x > 10 || p.y < 0 || p.y > 0) {
			return false;
		}
		return true;
	}
}
