import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;




public class HitDetectionTest {
	
	Player enemyPlayer;
	
	@BeforeEach
	 void init() {		
		
		Vector<Point> vec1 = new Vector<Point>();
		vec1.add(new Point (1,2));
		vec1.add(new Point (2,2));
		vec1.add(new Point (3,2));
		vec1.add(new Point (4,2));
		
		Vector<Point> vec2 = new Vector<Point>();
		vec2.add(new Point (5,7));
		vec2.add(new Point (5,6));
		vec2.add(new Point (5,5));
		vec2.add(new Point (5,4));
		vec2.add(new Point (5,3));
		
		Vector<Point> vec3 = new Vector<Point>();
		vec3.add(new Point (9,11));
		vec3.add(new Point (9,12));
		vec3.add(new Point (9,13));
		
		Vector<Point> vec4 = new Vector<Point>();
		vec4.add(new Point (5,10));
		vec4.add(new Point (4,10));
		vec4.add(new Point (3,10));
		
		Ship ship1 = new Ship(100, null,  vec1, vec1.size(), "ship1");
		Ship ship2 = new Ship(100, null,  vec2, vec2.size(), "ship2");
		Ship ship3 = new Ship(100, null,  vec3, vec3.size(), "ship3");
		Ship ship4 = new Ship(100, null,  vec4, vec4.size(), "ship4");
		
		enemyPlayer = new Player("player");
		enemyPlayer.setShip(ship1, 0);
		enemyPlayer.setShip(ship2, 1);
		enemyPlayer.setShip(ship3, 2);
		enemyPlayer.setShip(ship4, 3);

	
	}
	

	
	@Test
	void HitDectectionTest1() {
		
		
		
		boolean b = HitDetection.fireAtEnemy(enemyPlayer, new Point(1,2), new Weapon());
		assertEquals(b, true);
		assertEquals(enemyPlayer.getShips().get(0).getCurHealth(), 75);
		
		boolean a = HitDetection.fireAtEnemy(enemyPlayer, new Point(5,5), new Weapon());
		assertEquals(a, true);
		assertEquals(enemyPlayer.getShips().get(1).getCurHealth(), 75);
		
		boolean d = HitDetection.fireAtEnemy(enemyPlayer, new Point(9,11), new Weapon());
		assertEquals(d, true);
		assertEquals(enemyPlayer.getShips().get(2).getCurHealth(), 75);
		
		boolean c = HitDetection.fireAtEnemy(enemyPlayer, new Point(5,10), new Weapon());
		assertEquals(c, true);
		assertEquals(enemyPlayer.getShips().get(3).getCurHealth(), 75);

		
	}
	
	

	@Test
	void HitDectectionTest2() {
		
		
		boolean b = HitDetection.fireAtEnemy(enemyPlayer, new Point(3,4), new Weapon());
		assertEquals(b, false);
		assertEquals(enemyPlayer.getShips().get(0).getCurHealth(), 100);
		
		boolean a = HitDetection.fireAtEnemy(enemyPlayer, new Point(10,5), new Weapon());
		assertEquals(a, false);
		assertEquals(enemyPlayer.getShips().get(1).getCurHealth(), 100);
		
		boolean d = HitDetection.fireAtEnemy(enemyPlayer, new Point(2,10), new Weapon());
		assertEquals(d, false);
		assertEquals(enemyPlayer.getShips().get(2).getCurHealth(), 100);
		
		boolean c = HitDetection.fireAtEnemy(enemyPlayer, new Point(2,3), new Weapon());
		assertEquals(c, false);
		assertEquals(enemyPlayer.getShips().get(3).getCurHealth(), 100);

		
	}
	
	

}
