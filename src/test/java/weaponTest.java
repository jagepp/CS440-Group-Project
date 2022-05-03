import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class weaponTest {
	// general flag test
	@Test
	void constructorTest() {
		Weapon newWeapon = new Weapon();
		assertEquals("Artillery Barrage", newWeapon.getWeaponName(), "Naming done incorrectly");
		assertEquals(25, newWeapon.getWeaponDamage(), "damage done incorrectly");
	}
	
	@Test
	void constructorTest2() {
		Weapon newWeapon = new Weapon(50, "gun");
		assertEquals("gun", newWeapon.getWeaponName(), "Naming done incorrectly");
		assertEquals(50, newWeapon.getWeaponDamage(), "damage done incorrectly");
	}
}
