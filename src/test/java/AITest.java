import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AITest {

	static AI ai;
	static int xBound;
	static int yBound;
	
	@BeforeEach
	void init() {
		ai = new AI();
		xBound = Player.ROW_SIZE;
		yBound = Player.COL_SIZE;
	}
	
	@Test
	void constructorTest() {
		assertEquals("AI",ai.name,"Constructor failed");
		assertEquals(8,ai.numMoves,"Constructor failed");
		assertEquals(0,ai.points,"Constructor failed");
	}
	
	@Test
	void nextShotTest() {
		Point p = new Point(2,2);
		Boolean hit = true;
		ai.setLastShot(p, hit);
		Point n = ai.nextShot();
		assertEquals(n,p,"Next shot not set to previous on successful hit");
	}
	
	@Test
	void randPointTest() {
		Point p = new Point(2,2);
		Boolean hit = false;
		ai.setLastShot(p, hit);
		Point n;
		for(int i =0; i<100; i++) {
			n = ai.nextShot();
			if(n.x > xBound || n.x < 0) {
				fail("x coord is out of bounds");
			}
			if(n.y > yBound || n.y < 0) {
				fail("y coord is out of bounds");
			}
		}
		assertEquals(1,1,"");//pass the test
	}
	
	//since the ai inherits from the player class, we can use
	//the ai class to test player class methods as well
	
}
