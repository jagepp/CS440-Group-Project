import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PlayerTest {
	
	Player p;
	
	@BeforeEach
	 void init() {		
		
		p = new Player("Player X");
	}
	
	@Test
	void constructorTest() {
		assertEquals("Player X", p.getName(), "Player named incorrectly");
		assertEquals(0, p.getPoints(), "Points not initialized correctly");
		assertEquals(4, p.getShips().size(), "Size of ships vector not set correctly");
		assertEquals(8, p.getMoves(), "Number of moves not initialized correctly");
	}
	
	@Test
	void testAllSunk() {
		Ship a = new Battleship();
		Ship b = new Carrier();
		Ship c = new Cruiser();
		Ship d = new Submarine();
		p.setShip(a, 0);
		p.setShip(b, 1);
		p.setShip(c, 2);
		p.setShip(d, 3);
		
		assertEquals(false, p.isAllSunk(), "False all ships sunk");
		
		a.setSunk(true);
		assertEquals(false, p.isAllSunk(), "False all ships sunk");

		b.setSunk(true);
		assertEquals(false, p.isAllSunk(), "False all ships sunk");

		c.setSunk(true);
		assertEquals(false, p.isAllSunk(), "False all ships sunk");

		d.setSunk(true);
		assertEquals(true, p.isAllSunk(), "All ships sunk but not recognized");
	}

}
