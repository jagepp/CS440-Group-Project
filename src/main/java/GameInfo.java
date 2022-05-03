import java.io.Serializable;

public class GameInfo implements Serializable {

	private static final long serialVersionUID = 5867735837149809485L;

	private Player player1;
	private Player player2;
	
	private boolean p1PlacedShips;
	private boolean p2PlacedShips;
	private boolean placeShipPhase;
	
	private boolean isPlayerOne; // True -> Player 1; False -> Player 2
	
	// Firing Variables
	private boolean hasFired; // For clients to check to see whether they've fired already or not.
	private boolean isFiring; // For server to check to see whether client is firing or ending turn
	private boolean gotHit; // For clients to check to see whether they are being informed about being hit
	private boolean didHit; // For clients to check to see whether they hit a ship after they Fire
	private Point firingPoint;
	private Weapon firingWeapon;
	
	// Game Ending Variables
	private boolean isGameOver; // For clients to check to see whether the game ended 
	
	public GameInfo()
	{
		setPlaceShipPhase(true); // Beginning of the game is the placeShipPhase
		setIsPlayerOne(true); // Initialize to Player 1
		setHasFired(false);
		setIsFiring(false);
		setGotHit(false);
		setDidHit(false);
		setGameOver(false);
	}

	public boolean isPlaceShipPhase() {
		return placeShipPhase;
	}

	public void setPlaceShipPhase(boolean placeShipPhase) {
		this.placeShipPhase = placeShipPhase;
	}

	public boolean isPlayerOne() {
		return isPlayerOne;
	}

	public void setIsPlayerOne(boolean isPlayerOne) {
		this.isPlayerOne = isPlayerOne;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public boolean p1PlacedShips() {
		return p1PlacedShips;
	}

	public void setP1PlacedShips(boolean p1PlacedShips) {
		this.p1PlacedShips = p1PlacedShips;
	}

	public boolean p2PlacedShips() {
		return p2PlacedShips;
	}

	public void setP2PlacedShips(boolean p2PlacedShips) {
		this.p2PlacedShips = p2PlacedShips;
	}

	public boolean hasFired() {
		return hasFired;
	}

	public void setHasFired(boolean hasFired) {
		this.hasFired = hasFired;
	}

	public boolean isFiring() {
		return isFiring;
	}

	public void setIsFiring(boolean isFiring) {
		this.isFiring = isFiring;
	}

	public Point getFiringPoint() {
		return firingPoint;
	}

	public void setFiringPoint(Point firingPoint) {
		this.firingPoint = firingPoint;
	}

	public Weapon getFiringWeapon() {
		return firingWeapon;
	}

	public void setFiringWeapon(Weapon firingWeapon) {
		this.firingWeapon = firingWeapon;
	}

	public boolean gotHit() {
		return gotHit;
	}

	public void setGotHit(boolean gotHit) {
		this.gotHit = gotHit;
	}

	public boolean didHit() {
		return didHit;
	}

	public void setDidHit(boolean didHit) {
		this.didHit = didHit;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}
}

