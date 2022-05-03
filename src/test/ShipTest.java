import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ShipTest {
	
	Ship ship;
	@BeforeEach
	public void setup() {
		ship = new Ship();
	}

	// general flag test
	@Test
	void flagTest() {
		assertFalse(ship.getFlag());
		ship.setFlag(true);
		assertTrue(ship.getFlag());
		ship.setFlag(false);
		assertFalse(ship.getFlag());
	}
	
	@Test
	void basicHealthTest() {
		ship.setMaxHealth(50);
		ship.takeDamage(10);
		assertEquals(40, ship.getCurHealth(), "damage done wrong");
		assertEquals(50, ship.getMaxHealth(), "max health done wrong");
	}
	
	@Test
	void basicSizeTest() {
		ship.setLength(5);
		assertEquals(5, ship.getLength(), "length` done wrong");
	}
	
	@Test
	void damageTest() {
		ship.setMaxHealth(0);
		ship.takeDamage(10);
		assertEquals(0, ship.getCurHealth(), "damage done wrong");
		assertTrue(ship.isDamaged());
	}
	
	@Test
	void damageTest2() {
		ship.setMaxHealth(10);
		ship.takeDamage(10);
		assertEquals(0, ship.getCurHealth(), "damage done wrong");
		assertTrue(ship.isDamaged());
	}
	
	@Test
	void constructor2() {
		Weapon gun = new Weapon();
		Vector<Point> points = new Vector<>();
		points.add(new Point(1,0));
		Ship ship2 = new Ship(30, gun, points, 3, "undecided");
		
		assertEquals(30, ship2.getMaxHealth(), "max health done wrong");
		assertEquals(3, ship2.getLength(), "size done wrong");
		assertEquals("undecided", ship2.getShipType(), "Namind done wrong");


	}
	
	
}
