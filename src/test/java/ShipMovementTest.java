import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ShipMovementTest {
	
	private int rowSize = 15;
	private int colSize = 15;
	
	
	public Vector<Point> canMove(Ship ship, int amount) {
		Vector<Point> points = ship.getPointsOn();

		Point p1 = points.get(0);
		Point p2 = points.get(1);

		if(p1.x == p2.x) {   // vertical

			if(amount > 0 ) {   // moving forward

				int x = p1.y - p2.y; // used to determine head orientation

				if(x > 0 && ((p1.y + amount) < rowSize)  ) {  // head facing down								
					return movedShipPoints(ship, true, true, amount);
				}else if(x < 0 && ( (p1.y - amount) >= 0) ) {   // head facing up 									
					return movedShipPoints(ship, true, false, amount);
				}


			}else {     // moving backward

				int x = p1.y - p2.y;
				Point pL = points.lastElement();

				if(x > 0 && ((pL.y + amount)  >= 0 ) ) {       // head facing down 						
					return movedShipPoints(ship, true, true, amount);
				}else if(x < 0 && ( (pL.y - amount) < rowSize) ) {   // head facing up 
					return movedShipPoints(ship, true, false, amount);

				}

			}


		}else if(p1.y == p2.y) {  // horizontal

			if(amount > 0) {    // moving forward


				int x = p1.x - p2.x;

				if(x > 0 && ((p1.x + amount) < colSize)  ) {   // head facing right 								
					return movedShipPoints(ship, false, true, amount);
				}else if(x < 0 && ( (p1.x - amount) >= 0) ) {    // head facing left									
					return movedShipPoints(ship, false, false, amount);
				}


			}else {          // moving backward

				int x = p1.x - p2.x;
				Point pL = points.lastElement();

				if(x > 0 && ((pL.x + amount) >= 0)  ) {    // head facing right 
													
					return movedShipPoints(ship, false, true, amount);
				}else if(x < 0 && ( (pL.x - amount) < colSize ) ) {    // heading facing left
											
					return movedShipPoints(ship, false, false, amount);
				}
			}

		}

		return null;
	}
	

	public Vector<Point> movedShipPoints(Ship s, boolean ver, boolean add, int amount){

		Vector<Point> vec = new Vector<Point>();
		Vector<Point> pointsOn = s.getPointsOn();

		for(int i = 0; i < pointsOn.size(); i++) {

			Point p1 = pointsOn.get(i);

			if(ver) {  // vertical ship position

				if(add) {
					vec.add(new Point(p1.x, p1.y + amount));		 			
				}else {
					vec.add(new Point(p1.x, p1.y - amount));					
				}

			}else {  // horizontal ship position

				if(add) {
					vec.add(new Point(p1.x + amount, p1.y ));					
				}else {
					vec.add(new Point(p1.x - amount, p1.y ));					
				}		

			}

		}

		return vec;
	}
	
	
	// Given a list of ship points and rotation direction, this  method rotates the ship
	// 90 degree towards the given direction.
	
	public Vector<Point> flipShip(Vector<Point> pointsOn, boolean Clockwise){
		
		Vector<Point> shipPoints = new Vector<Point>();
		//Vector<Point> pointsOn = s.getPointsOn();
		boolean ver = (pointsOn.get(0).x == pointsOn.get(1).x) ? true  : false; 
		int mid = (int) (Math.floor(pointsOn.size()/2));
		Point pMid = pointsOn.get(mid);
		int increment = -1;
		Point p1 = pointsOn.get(0);
		Point p2 = pointsOn.get(1);
				
		if(ver) { 	// vertical
			
			int x = p1.y - p2.y;

			if(x > 0) {      // head facing down
				
				if(Clockwise) {   // clockwise										
					increment = -1;					
				}else {   // counterclockwise								
					increment = 1;
				}
					
			}else {          // head facing up	
				
				if(Clockwise) {   // clockwise
					increment = 1;					
				}else {   // counterclockwise
					increment = -1;
				}							
			}	
			
		}else {     // horizontal
			
			int x = p1.x - p2.x;

			if(x > 0) {     // head facing right
				
				if(Clockwise) {   // clockwise
					increment = 1;
				}else {   // counterclockwise								
					increment = -1;
				}
				
			}else {        // head facing left
				
				if(Clockwise) {   // clockwise
					increment = -1;				
				}else {   // counterclockwise
					increment = 1;
				}
				
			}
			
		}
		
		// This code does the actual rotation by rotating the points around the middle ship point
		
		for(int i = 0; i < pointsOn.size(); i++ ) {
			 
			if(i == (mid + 1)) {       // change increment since we want to "grow" points in other direction
				increment = -1 * increment;
			}
			
			Point p = pointsOn.get(i);
		
			if(ver) { 
				int dist = Math.abs(p.y - pMid.y);    // get distance between y coordinates
				int x = pMid.x + dist*(increment);     // get the changed x coordinate
				shipPoints.add(new Point(x,pMid.y));   //  add point
			}else {
				int dist = Math.abs(p.x - pMid.x);    // get distance between x coordinates
				int y = pMid.y + dist*(increment);    // get the changed y coordinate
				shipPoints.add(new Point(pMid.x, y));  // add point
			}
		
		}
		
		return shipPoints;
	}
	
	
	public Vector<Point> canRotate(Ship s, int degrees, boolean clockwise){
		Vector<Point> pointsOn = s.getPointsOn();
		//boolean clockwise = (moveType.equals("CW")) ? true : false;
		Vector<Point> newPoints = null;
		
		int count = 0;
		
		while(count < degrees) {
			
			if(newPoints == null) {
				newPoints = flipShip(pointsOn, clockwise);
			}else {
				newPoints = flipShip(newPoints, clockwise);
			}
			
			if(!pointsWithinRange(newPoints)) {
				return null;
			}
			
			count += 90;
		}
		
		return newPoints;
	}
	
	public boolean pointsWithinRange(Vector<Point> pointsOn) {
		
		for(int i = 0; i < pointsOn.size(); i++) {
			
			Point p1 = pointsOn.get(i);
			
			if(p1.x < 0 || p1.x >= colSize || p1.y < 0 || p1.y >= rowSize) {
				return false;
			}
			
		}	
		
		return true;
	}
	
	public boolean comparePoint(Point p1, Point p2) {
		return p1.x == p2.x && p1.y == p2.y;
	}
	
	
	
	// Moving forward with head facing down the map in vertical position
	
	
	@Test
	public void test1() {
		Ship ship = new Ship();
		Vector<Point> shipPoints = ship.getPointsOn();
		
		shipPoints.add(new Point(3,4));
		shipPoints.add(new Point(3,3));
		shipPoints.add(new Point(3,2));

		
		Vector<Point> newPoints = canMove(ship,4 );
		
		assertEquals(comparePoint(newPoints.get(0), new Point(3,8)), true);
		assertEquals(comparePoint(newPoints.get(1), new Point(3,7)), true);
		assertEquals(comparePoint(newPoints.get(2), new Point(3,6)), true);
	}
	
	// Moving backward with head facing down the map in vertical position

	
	@Test
	public void test2() {
		Ship ship = new Ship();
		Vector<Point> shipPoints = ship.getPointsOn();
		
		shipPoints.add(new Point(3,4));
		shipPoints.add(new Point(3,3));
		shipPoints.add(new Point(3,2));

		Vector<Point> newPoints = canMove(ship,-2 );
		
		assertEquals(comparePoint(newPoints.get(0), new Point(3,2)), true);
		assertEquals(comparePoint(newPoints.get(1), new Point(3,1)), true);
		assertEquals(comparePoint(newPoints.get(2), new Point(3,0)), true);
	}
	
	// Moving forward with head facing up the map in vertical position

	@Test
	public void test3() {
		Ship ship = new Ship();
		Vector<Point> shipPoints = ship.getPointsOn();
		
		shipPoints.add(new Point(3,6));
		shipPoints.add(new Point(3,7));
		shipPoints.add(new Point(3,8));
	
		Vector<Point> newPoints = canMove(ship,6 );
		
		assertEquals(comparePoint(newPoints.get(0), new Point(3,0)), true);
		assertEquals(comparePoint(newPoints.get(1), new Point(3,1)), true);
		assertEquals(comparePoint(newPoints.get(2), new Point(3,2)), true);
	}
	

	
	// Moving backward with head facing up the map in vertical position

	@Test
	public void test4() {
		Ship ship = new Ship();
		Vector<Point> shipPoints = ship.getPointsOn();
		
		shipPoints.add(new Point(3,6));
		shipPoints.add(new Point(3,7));
		shipPoints.add(new Point(3,8));
	
		Vector<Point> newPoints = canMove(ship,-3 );
		
		assertEquals(comparePoint(newPoints.get(0), new Point(3,9)), true);
		assertEquals(comparePoint(newPoints.get(1), new Point(3,10)), true);
		assertEquals(comparePoint(newPoints.get(2), new Point(3,11)), true);
	}
		
	
	// Moving forward with head facing right the map in horizontal position
	
	
	@Test
	public void test5() {
		Ship ship = new Ship();
		Vector<Point> shipPoints = ship.getPointsOn();
		
		shipPoints.add(new Point(4,4));
		shipPoints.add(new Point(3,4));
		shipPoints.add(new Point(2,4));

		
		Vector<Point> newPoints = canMove(ship,6 );
		
		assertEquals(comparePoint(newPoints.get(0), new Point(10,4)), true);
		assertEquals(comparePoint(newPoints.get(1), new Point(9,4)), true);
		assertEquals(comparePoint(newPoints.get(2), new Point(8,4)), true);
	}


	// Moving backward with head facing right the map in horizontal position
	
	@Test
	public void test6() {
		Ship ship = new Ship();
		Vector<Point> shipPoints = ship.getPointsOn();
		
		shipPoints.add(new Point(4,4));
		shipPoints.add(new Point(3,4));
		shipPoints.add(new Point(2,4));

		
		Vector<Point> newPoints = canMove(ship,-2 );
		
		assertEquals(comparePoint(newPoints.get(0), new Point(2,4)), true);
		assertEquals(comparePoint(newPoints.get(1), new Point(1,4)), true);
		assertEquals(comparePoint(newPoints.get(2), new Point(0,4)), true);
	}

	

	// Moving forward with head facing left the map in horizontal position
	
	@Test
	public void test7() {
		Ship ship = new Ship();
		Vector<Point> shipPoints = ship.getPointsOn();
		
		shipPoints.add(new Point(7,6));
		shipPoints.add(new Point(8,6));
		shipPoints.add(new Point(9,6));

		
		Vector<Point> newPoints = canMove(ship,6 );
		
		assertEquals(comparePoint(newPoints.get(0), new Point(1,6)), true);
		assertEquals(comparePoint(newPoints.get(1), new Point(2,6)), true);
		assertEquals(comparePoint(newPoints.get(2), new Point(3,6)), true);
	}
	
	// Moving backward with head facing left the map in horizontal position
	
	
	@Test
	public void test8() {
		Ship ship = new Ship();
		Vector<Point> shipPoints = ship.getPointsOn();
		
		shipPoints.add(new Point(7,6));
		shipPoints.add(new Point(8,6));
		shipPoints.add(new Point(9,6));

		
		Vector<Point> newPoints = canMove(ship,-4 );
		
		assertEquals(comparePoint(newPoints.get(0), new Point(11,6)), true);
		assertEquals(comparePoint(newPoints.get(1), new Point(12,6)), true);
		assertEquals(comparePoint(newPoints.get(2), new Point(13,6)), true);
	}
	
	// Rotation clockwise with vertical ship position and head facing down

	@Test
	public void test9() {
		
		Ship ship = new Ship();
		Vector<Point> shipPoints = ship.getPointsOn();
		
		shipPoints.add(new Point(5,6));
		shipPoints.add(new Point(5,5));
		shipPoints.add(new Point(5,4));
		
		Vector<Point> newPoints = canRotate(ship, 90, true );
		
		assertEquals(comparePoint(newPoints.get(0), new Point(4,5)), true);
		assertEquals(comparePoint(newPoints.get(1), new Point(5,5)), true);
		assertEquals(comparePoint(newPoints.get(2), new Point(6,5)), true);	
		
	}
	
	
	// Rotation counter-clockwise with vertical ship position and head facing down

	@Test
	public void test10() {
		
		Ship ship = new Ship();
		Vector<Point> shipPoints = ship.getPointsOn();
		
		shipPoints.add(new Point(5,6));
		shipPoints.add(new Point(5,5));
		shipPoints.add(new Point(5,4));
		
		Vector<Point> newPoints = canRotate(ship, 90, false );
		
		assertEquals(comparePoint(newPoints.get(0), new Point(6,5)), true);
		assertEquals(comparePoint(newPoints.get(1), new Point(5,5)), true);
		assertEquals(comparePoint(newPoints.get(2), new Point(4,5)), true);	
		
	}
	
	
	// Rotation clockwise with vertical ship position and head facing up

		@Test
		public void test11() {
			
			Ship ship = new Ship();
			Vector<Point> shipPoints = ship.getPointsOn();
			
			shipPoints.add(new Point(5,4));
			shipPoints.add(new Point(5,5));
			shipPoints.add(new Point(5,6));
			
			Vector<Point> newPoints = canRotate(ship, 270, true );
			
			assertEquals(comparePoint(newPoints.get(0), new Point(4,5)), true);
			assertEquals(comparePoint(newPoints.get(1), new Point(5,5)), true);
			assertEquals(comparePoint(newPoints.get(2), new Point(6,5)), true);	
			
		}
		
		
		// Rotation counter-clockwise with vertical ship position and head facing up

		@Test
		public void test12() {
			
			Ship ship = new Ship();
			Vector<Point> shipPoints = ship.getPointsOn();
			
			shipPoints.add(new Point(5,4));
			shipPoints.add(new Point(5,5));
			shipPoints.add(new Point(5,6));
			
			Vector<Point> newPoints = canRotate(ship, 180, false );
			
			assertEquals(comparePoint(newPoints.get(0), new Point(5,6)), true);
			assertEquals(comparePoint(newPoints.get(1), new Point(5,5)), true);
			assertEquals(comparePoint(newPoints.get(2), new Point(5,4)), true);	
			
		}
		
	
		// Rotation clockwise with horizontal ship position and head facing right

		@Test
		public void test13() {
			
			Ship ship = new Ship();
			Vector<Point> shipPoints = ship.getPointsOn();
			
			shipPoints.add(new Point(11,7));
			shipPoints.add(new Point(10,7));
			shipPoints.add(new Point(9,7));
			shipPoints.add(new Point(8,7));

			
			Vector<Point> newPoints = canRotate(ship, 90, true );
			
			assertEquals(comparePoint(newPoints.get(0), new Point(9,9)), true);
			assertEquals(comparePoint(newPoints.get(1), new Point(9,8)), true);
			assertEquals(comparePoint(newPoints.get(2), new Point(9,7)), true);	
			assertEquals(comparePoint(newPoints.get(3), new Point(9,6)), true);	
			
		}
		
		// Rotation counter-clockwise with horizontal ship position and head facing right

		@Test
		public void test14() {
			
			Ship ship = new Ship();
			Vector<Point> shipPoints = ship.getPointsOn();
			
			shipPoints.add(new Point(11,7));
			shipPoints.add(new Point(10,7));
			shipPoints.add(new Point(9,7));
			shipPoints.add(new Point(8,7));

			
			Vector<Point> newPoints = canRotate(ship, 90, false );
			
			assertEquals(comparePoint(newPoints.get(0), new Point(9,5)), true);
			assertEquals(comparePoint(newPoints.get(1), new Point(9,6)), true);
			assertEquals(comparePoint(newPoints.get(2), new Point(9,7)), true);	
			assertEquals(comparePoint(newPoints.get(3), new Point(9,8)), true);	
			
		}
		
		// Rotation clockwise with horizontal ship position and head facing left

		@Test
		public void test15() {
			
			Ship ship = new Ship();
			Vector<Point> shipPoints = ship.getPointsOn();
			
			shipPoints.add(new Point(8,7));
			shipPoints.add(new Point(9,7));
			shipPoints.add(new Point(10,7));
			shipPoints.add(new Point(11,7));

			
			Vector<Point> newPoints = canRotate(ship, 90, true );
			
			assertEquals(comparePoint(newPoints.get(0), new Point(10,5)), true);
			assertEquals(comparePoint(newPoints.get(1), new Point(10,6)), true);
			assertEquals(comparePoint(newPoints.get(2), new Point(10,7)), true);	
			assertEquals(comparePoint(newPoints.get(3), new Point(10,8)), true);	
			
		}
		
		
		// Rotation clockwise with horizontal ship position and head facing left

		@Test
		public void test16() {
			
			Ship ship = new Ship();
			Vector<Point> shipPoints = ship.getPointsOn();
			
			shipPoints.add(new Point(8,7));
			shipPoints.add(new Point(9,7));
			shipPoints.add(new Point(10,7));
			shipPoints.add(new Point(11,7));

			
			Vector<Point> newPoints = canRotate(ship, 90, false );
			
			assertEquals(comparePoint(newPoints.get(0), new Point(10,9)), true);
			assertEquals(comparePoint(newPoints.get(1), new Point(10,8)), true);
			assertEquals(comparePoint(newPoints.get(2), new Point(10,7)), true);	
			assertEquals(comparePoint(newPoints.get(3), new Point(10,6)), true);	
			
		}
				
		
	
	
	
}






