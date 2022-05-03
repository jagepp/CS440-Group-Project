import java.io.Serializable;

public class Powerups implements Serializable {
	
	private static final long serialVersionUID = 2535289541281688564L;
	
	private int cost;
	private String name;
	private String description;
	
	

	Powerups(){
		setCost(0);
		setName("Default Name");
		setDescription("Default Description");
	}
	
	Powerups(int cost, String name, String description){
		setCost(cost);
		setName(name);
		setDescription(description);
	}

	public void doAction(Ship ship, Player curPlayer) {
		//inherited classes will overload this
	}

	public int getCost() {
		return cost;
	}


	public void setCost(int cost) {
		this.cost = cost;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}

}

class repair extends Powerups{

	repair(){
		setCost(150);
		setName("Repair");
		setDescription("Fully restore your ships health and fix any status");
	}
	
	public void doAction(Ship ship,Player player){

		ship.setCurHealth(ship.getMaxHealth());
		ship.setDamaged(false);
	}
	
}

class doubleDamage extends Powerups{
	
	doubleDamage(){
		setCost(50);
		setName("Double Damage");
		setDescription("The selected ship will do double damage with its current weapon");
	}
	
	public void doAction(Ship s,Player player) {
		int damage = s.getWeapon().getWeaponDamage();
		damage = damage * 2;
		s.getWeapon().setWeaponDamage(damage);
	}
}

class add10Moves extends Powerups{
	add10Moves(){
		setCost(100);
		setName("Add 10 moves");
		setDescription("The player will be awarded an additional 10 moves");
	}
	
	public void doAction(Ship s,Player player) {
		int cur = player.getMoves();
		cur += 10;
		player.setMoves(cur);
		
	}
	
}

class addPoints extends Powerups{
	addPoints(){
		setCost(0);
		setName("Test: add points");
	}
	
	public void doAction(Ship ship, Player player) {
		player.addPoints(200);
	}
}





