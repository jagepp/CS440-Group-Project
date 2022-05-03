

public class HitDetection {
	public static boolean fireAtEnemy(Player enemy, Point p, Weapon weap) {
		//Vector<Ship> enemyShips = enemy.getShips();//get enemy ships
		if(enemy == null || enemy.getShips() == null ) {
			System.out.print("Failed to fire, enemy or enemy ships NULL\n");
			return false;
		}
		for(int i =0; i<enemy.getShips().size();i++) {
			if(enemy.getShips().get(i)==null) {
				System.out.print("Failed to fire, enemy ship "+ i +" is NULL");
				return false;
			}
			int size = (enemy.getShips().get(i).getPointsOn()).size();//ships->points->size of points
			for(int j=0; j<size;j++) {//ships->curShip->curShip point[j]-> either x or y
				if((p.x ==(enemy.getShips().get(i).getPointsOn()).get(j).x)&&(p.y == (enemy.getShips().get(i).getPointsOn()).get(j).y)) {
					return damageShip(enemy.getShips().get(i), weap);
					//returns true if ship isnt already sunk
					//otherwise, no extra damage taken, no hit detected
				}
			}
		}
		
		return false;
	}
	
	private static boolean damageShip(Ship s, Weapon w) {


		if(s.getCurHealth()<=0) {
			System.out.print("Hit ship that is already sunk!");
			return false;
		}
		
		if(s.getCurHealth() <= w.getWeaponDamage()) {
			s.setCurHealth(0);
			s.setSunk(true);
		}
		else {
			s.setDamaged(true);
			int hp = s.getCurHealth()- w.getWeaponDamage();
			s.setCurHealth(hp);
		}
		
		return true;
	}
}
