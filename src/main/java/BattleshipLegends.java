import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.lang.Math;

public class BattleshipLegends extends Application {
	
	private Map gameMap;
	private int rowSize;
	private int colSize;

	
	private boolean singlePlayerGame,firstTurn;
	

	// Menu Scene
	private BorderPane menuBorderPane;
	private Button startSingleplayer;
	private Button startMultiplayerLocal;
	private Button startMultiplayerOnline;
	private Button howToPlayButton;
	private Button exitButton;
	private VBox menuButtonVBox;	

	// Multiplayer (Online) Scenes
	private ListView<String> listview1, listview2;
	private Server serverConnection;
	private Client clientConnection;
	private Button chooseServer;
	private Button chooseClient;
	private HBox serverClientChoiceHBox;
	private BorderPane onlinePane;
	
	// Game End Scene
	private Text victoryDeclaration;
	private VBox endVBox;
	private BorderPane endPane;
	
	private Stage stage;
	private BorderPane borderPane, borderPane2;
	private GridPane gridPane;
	private Menu allShips, store;
	private MenuBar chooseShips;
	private MenuItem ship1, ship2, ship3, ship4;
	private Vector<MenuItem> powerups;
	private VBox showChosenShip, infoForPlayer, moveInfo, fireInfo;
	private HBox allInfo, upDown, rotation;
	private Text shipDescriptions, numMovesRem, Health, X, Y, curPlayerText, hitMiss;
	private Button endTurn, forward, reverse, CW, CCW, move, fire, viewShips;
	private TextField getMove, getXCoord, getYCoord;
	private EventHandler<ActionEvent> doMove;
	private HashMap<String, Scene> sceneMap;
	private int numMoves = 8;
	private Battleship bShip;
	private Carrier cShip;
	private Cruiser crShip;
	private Submarine sShip;
	private ImageView[][]bGrid;
	private ProgressBar healthBar;

	private Text shotInfo;

	private Vector<Button> moveButtons;

	private Button back;
	private Button placeShip;

	private String moveType;

	private Ship curShip;
	private Player curPlayer;
	private Player player1;
	private Player player2;
	private AI ai;

	private boolean chooseMode;	// flag indicating whether the game is in the ship placing phase or not
	private int countPoints;
	private Point[] points;
	private boolean flag; 		// flag indicating whether or not it is currently Player 1's first turn

	public static void main(String[] args) {
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {

		stage = primaryStage;
		stage.setTitle("BattleShip Legends");

		gameMap = new Map("/map.txt");
		rowSize = gameMap.getRowSize();
		colSize = gameMap.getColSize();
		
		moveType = null;
		chooseMode = true; 
		flag = true;
		countPoints = 0;
		points = new Point[5];
		bGrid = new ImageView[rowSize][colSize];

		doMove = new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				numMoves -= 2;
				numMovesRem.setText("Moves left: " + numMoves);
			}
		};

		

		sceneMap = new HashMap<String,Scene>();

		sceneMap.put("control", createControlGUI());
		sceneMap.put("battle", createMoveGUI());
		sceneMap.put("menu", createMenuGUI());
		sceneMap.put("instructions", createHowToPlayGUI());
		sceneMap.put("online", createOnlineGUI());
		sceneMap.put("server", createServerGUI());
		sceneMap.put("end", createEndGUI());

		shipDescriptions.setText(bShip.getShipType());
		curShip = bShip;

		player1 = new Player("Player 1");
		player2 = new Player("Player 2");
		ai = new AI();
		curPlayer = player1;
		
		stage.setScene(sceneMap.get("menu"));
		stage.show();
	}

	public Scene createMenuGUI() {
		menuBorderPane			= new BorderPane();
		startSingleplayer		= new Button("Singleplayer");
		startMultiplayerLocal 	= new Button("Multiplayer (Local)");
		startMultiplayerOnline 	= new Button("Multiplayer (Online)");
		howToPlayButton 		= new Button("How To Play");
		exitButton		 		= new Button("Exit");
		menuButtonVBox			= new VBox(50);

		startSingleplayer.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent action) {
				//set scene to singleplayer scene
				singlePlayerGame = true;
				firstTurn = true;
				stage.setScene(sceneMap.get("control"));
			}
		});

		startMultiplayerLocal.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent action) {
				singlePlayerGame = false;
				stage.setScene(sceneMap.get("control"));
			}
		});

		startMultiplayerOnline.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent action) {
				stage.setScene(sceneMap.get("online"));
			}
		});
		
		howToPlayButton.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent action) {
				// go to "how to play" screen
				stage.setScene(sceneMap.get("instructions"));
			}
		});
		
		exitButton.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent action) {
				Platform.exit();
				System.exit(0); 
			}
		});

		menuButtonVBox.getChildren().addAll(startSingleplayer, startMultiplayerLocal, startMultiplayerOnline, howToPlayButton, exitButton);
		menuButtonVBox.setAlignment(Pos.CENTER);
		menuBorderPane.setCenter(menuButtonVBox);
		return new Scene(menuBorderPane, 400, 400);
	}

	public Scene createHowToPlayGUI() {
		
		Button backToMainMenu = new Button("Back");
		backToMainMenu.setOnAction((e->{
			stage.setScene(sceneMap.get("menu"));
		}));
		
		BorderPane pane = new BorderPane();
		VBox howToPlayVBox = new VBox(50);
		Text headerText = new Text("How to Play");
		headerText.setStyle("-fx-font: 34 arial;");
		Text instructions = new Text(
				"Each player has four ships: one of length 4, one of length 5, and two of length 3.\n"
				+ "The goal of the game is to destroy all of your opponents ships; once a player is without ships, the game will end.\n"
				+ "\n"
				+ "The first stage of the game is the ship placing phase; both players must place all of their ships before the game continues.\n"
				+ "To do so, each player must click on the 'Show Ships' button. There, they can click on the ocean tiles to place their ships.\n"
				+ "When placing ships, the first square, signified by the dot, represents the head, also known as the bow, of the ship.\n"
				+ "Note that ships can be placed neither on nor directly adjacent to the green and beige Island tiles.\n"
				+ "Use the ships drop-down to select and place all ships.\n"
				+ "After all ships are placed, turn-based gameplay will begin, wherein players can move their ships and fire at enemy ships.\n\n"
				+ "Each player can move their ships a total of 8 times per turn.\n"
				+ "Forward movement, CW turning, and CCW turning count as 1 move, while reverse movement counts as 2 moves.\n\n"
				+ "Each player can also fire once per turn, independent of movement.\n\n"
				+ "Each player can also accumulate points to spend (via the Shop drop-down) for powerups and upgraded weapons.\n"
				+ "Points are gotten from hitting enemy ships, as well as from moving into a square directly adjacent to an Island (once per Island)."
				);
		instructions.setStyle("-fx-font: 20 arial;");
		howToPlayVBox.getChildren().addAll(headerText, instructions);
		howToPlayVBox.setAlignment(Pos.CENTER);
		pane.setTop(howToPlayVBox);
		BorderPane.setAlignment(backToMainMenu, Pos.CENTER);
		pane.setCenter(backToMainMenu);
		pane.setPadding(new Insets(50));		
		pane.setBackground(new Background(new BackgroundFill(Color.NAVAJOWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		return new Scene(pane, 1300, 700);
	}
	
	public Scene createOnlineGUI() {

		listview1 = new ListView<String>();
		listview2 = new ListView<String>();


		chooseServer = new Button("Start Server");
		chooseClient = new Button("Connect Client");


		chooseServer.setOnAction(e->{ 
			stage.setScene(sceneMap.get("server"));
			stage.setTitle("Server");
			serverConnection = new Server(data -> {
				Platform.runLater(()->{
					listview1.getItems().add(data.toString());
				});

			});

		});
		
		
		chooseClient.setOnAction(e-> {
			stage.setScene(sceneMap.get("control"));
			stage.setTitle("Client");
			
			move.setDisable(true);
			fire.setDisable(true);
			VBox clientBox = new VBox(10, listview2);
			borderPane.setBottom(clientBox);

			endTurn.setOnAction(f-> {
				endTurn.setDisable(true); // Don't want the players clicking End Turn when it's not their turn
				move.setDisable(true);
				fire.setDisable(true);
				
				saveData(player1);
				if(clientConnection.gameInfo.isPlayerOne())
					clientConnection.gameInfo.setPlayer1(player1);
				else
					clientConnection.gameInfo.setPlayer2(player1);
				clientConnection.send();
			});
			
			fire.setOnAction(g-> {
				
				if(curShip.getCurHealth()==0) {
					hitMiss.setText("CAN'T FIRE, THIS SHIP IS SUNK!");
					return;
				}
				
				int xCoord =  Integer.parseInt(getXCoord.getText());
				int yCoord = Integer.parseInt(getYCoord.getText());
				Point tempP = new Point(xCoord,yCoord);
				clientConnection.gameInfo.setFiringPoint(tempP);
				clientConnection.gameInfo.setFiringWeapon(curShip.getWeapon());
				clientConnection.gameInfo.setIsFiring(true);
				
				fire.setDisable(true);
				clientConnection.send();
				
			});
			
			clientConnection = new Client(data->{
				Platform.runLater(()->{
					listview2.getItems().add(data.toString());
					
					if(clientConnection.gameInfo.gotHit()) // If being informed of hit, check if game is over. Do nothing else.
					{
						if(clientConnection.gameInfo.isGameOver())
						{
							stage.setScene(sceneMap.get("end"));
							victoryDeclaration.setText("All your ships have been destroyed.\nYou have lost.");
						}
					}
					// If in ship placing phase, do nothing. 
					else if(!clientConnection.gameInfo.isPlaceShipPhase()) 
					{
						
						endTurn.setDisable(false);
						move.setDisable(false);
						if(clientConnection.gameInfo.hasFired()) // If we're receiving a response after firing.
						{
							hitMiss.setText(data.toString());
							if(clientConnection.gameInfo.didHit()) // If fired and just successfully hit a ship
							{
								player1.addPoints(25); //each hit rewards 25 points
								curPlayerText.setText("Player 1: " + player1.getPoints() + " Points");
								if(clientConnection.gameInfo.isGameOver())
								{
									stage.setScene(sceneMap.get("end"));
									victoryDeclaration.setText("All enemy ships have been destroyed.\nYou have won!");
								}
							}
							// else do nothing else, since hitMiss already updated
						}
						else // We have not fired yet, it's simply a new turn starting
						{
							fire.setDisable(false);
							// load player data
							if(clientConnection.gameInfo.isPlayerOne())
								player1 = clientConnection.gameInfo.getPlayer1();
							else
								player1 = clientConnection.gameInfo.getPlayer2();
						}
					}
				});
			});
			clientConnection.start();
			
		});
				
		serverClientChoiceHBox = new HBox(50, chooseServer, chooseClient);
		serverClientChoiceHBox.setAlignment(Pos.CENTER);
		onlinePane = new BorderPane();
		onlinePane.setPadding(new Insets(70));
		onlinePane.setCenter(serverClientChoiceHBox);
		return new Scene(onlinePane, 450, 300);
	}
	
	public Scene createServerGUI()
	{
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(50));		
		pane.setCenter(listview1);
		pane.setBackground(new Background(new BackgroundFill(Color.NAVAJOWHITE, CornerRadii.EMPTY, Insets.EMPTY)));

		return new Scene(pane, 400, 300);
	}
	
	public Scene createEndGUI()
	{
		victoryDeclaration = new Text(); // Text will be set outside this function, depending on the winner
		endVBox = new VBox(50, victoryDeclaration, exitButton);
		endVBox.setAlignment(Pos.CENTER);
		endPane = new BorderPane();
		endPane.setCenter(endVBox);
		return new Scene(endPane, 400, 300);
	}
	
	public Scene createControlGUI() {

		borderPane		 = new BorderPane();
		allShips 		 = new Menu("All ships");
		store 			 = new Menu("Store");
		chooseShips		 = new MenuBar();
		showChosenShip	 = new VBox();
		infoForPlayer	 = new VBox(50);
		moveInfo		 = new VBox(15);
		fireInfo		 = new VBox();
		allInfo 		 = new HBox(40);
		upDown 			 = new HBox();
		rotation 		 = new HBox();
		shipDescriptions = new Text("Info");
		numMovesRem      = new Text("Moves left: " + numMoves);
		Health           = new Text("HP 100");
		X 				 = new Text("X Coord: ");
		Y 				 = new Text("Y Coord: ");
		hitMiss			 = new Text("HIT/MISS");
		shotInfo 		= new Text("Shot Info : ");
		curPlayerText 	 = new Text("Player 1: 0 Points");
		endTurn			 = new Button("End Turn");
		forward			 = new Button("Forward");
		reverse			 = new Button("Reverse");
		CW				 = new Button("CW");
		CCW				 = new Button("CCW");
		move			 = new Button("Move");
		fire			 = new Button("Fire");
		getMove			 = new TextField();
		getXCoord		 = new TextField();
		getYCoord		 = new TextField();

		chooseShips.getMenus().add(allShips);
		chooseShips.getMenus().add(store);

		ship1 = new MenuItem("Ship #: 1");
		ship2 = new MenuItem("Ship #: 2");
		ship3 = new MenuItem("Ship #: 3");
		ship4 = new MenuItem("Ship #: 4");

		allShips.getItems().addAll(ship1);
		allShips.getItems().addAll(ship2);
		allShips.getItems().addAll(ship3);
		allShips.getItems().addAll(ship4);
		
		Shop theStore = new Shop();
		Vector<Powerups> inventory = theStore.getItems();
		powerups = new Vector<MenuItem>();
		for(int i=0;i<inventory.size();i++) {
			powerups.add(new MenuItem(inventory.get(i).getName() + " - $" + 
					inventory.get(i).getCost()));
			store.getItems().add(powerups.get(i));
		}
		
		for(int i=0;i<powerups.size();i++) {
			Powerups curPU = inventory.get(i);
			powerups.get(i).setOnAction(e->{
				if(curPlayer.getPoints()>=curPU.getCost()) {
					curPlayer.addPoints(-1*curPU.getCost());
					curPU.doAction(curShip,curPlayer);
					System.out.println("Bought item "+ curPU.getName() + " for ship: "+ curShip.getShipType());
				}
				numMoves = curPlayer.getMoves();
				numMovesRem.setText("Moves left: " + numMoves);
				if(curPlayer == player2) {
					curPlayerText.setText("Player 2: " + player2.getPoints() + " Points");
				}
				else {
					curPlayerText.setText("Player 1: " + player1.getPoints() + " Points");
				}
				Health.setText("HP: " + curShip.getCurHealth());
			});
		}

		bShip = new Battleship();
		cShip = new Carrier();
		crShip = new Cruiser();
		sShip = new Submarine();

		curPlayerText.setStyle("-fx-font-family : Helvetica Neue;");
		curPlayerText.setFont(new Font(18));

		ship1.setOnAction(e->{
			shipDescriptions.setText(bShip.getShipType());
			Health.setText("HP: " + bShip.getCurHealth());
			curShip = bShip;
			double health = ((double)bShip.getCurHealth())/100;
			healthBar.setProgress(health);
		});

		ship2.setOnAction(e->{
			shipDescriptions.setText(cShip.getShipType());
			Health.setText("HP: " + cShip.getCurHealth());
			curShip = cShip;
			double health = ((double)cShip.getCurHealth())/100;
			healthBar.setProgress(health);
		});

		ship3.setOnAction(e->{
			shipDescriptions.setText(crShip.getShipType());
			Health.setText("HP: " + crShip.getCurHealth());
			curShip = crShip;
			double k = 1.33333333333333333333333333;
			double health = (k)*(((double)crShip.getCurHealth())/100);
			System.out.println(health);
			healthBar.setProgress(health);
		});

		ship4.setOnAction(e->{
			shipDescriptions.setText(sShip.getShipType());
			Health.setText("HP: " + sShip.getCurHealth());
			curShip = sShip;
			double k = 1.33333333333333333333333333;
			double health = (k)*(((double)sShip.getCurHealth())/100);
			System.out.println(health);
			healthBar.setProgress(health);
		});

		move.setOnAction(e->{

			clearShips();

			if(!curShip.getFlag() || moveType == null) { // If ship not placed or moveType not chosen
				return;
			}
			
			if(curShip.getCurHealth()==0) {
				hitMiss.setText("CAN'T MOVE, THIS SHIP IS SUNK!");
				return;
			}

			if((moveType.equals("Forward") || moveType.equals("Reverse")) && getMove.getText().equals("")) // If forward or reverse AND no amount input
				return;
			
			int moveAmount = Integer.parseInt(getMove.getText());

			if(moveType.equals("Forward")) {

				if(move(curShip, moveAmount)) {
					numMoves -= 1;
				}

			}else if(moveType.equals("Reverse") ) {

				if(move(curShip, (moveAmount * -1))) {
					numMoves -= 2;
				}

			}else if(moveType.equals("CW") || moveType.equals("CCW")) {
				
				if(move(curShip, moveAmount)) {
					numMoves -= 1;
				}
			}
			
			//move(curShip, Integer.parseInt(getMove.getText()));

			checkForIslandPoints();
			numMovesRem.setText("Moves left: " + numMoves);
			
	
			
		});

		viewShips = new Button("Show ships");
		HBox topBox = new HBox(endTurn, viewShips);
		topBox.setSpacing(20);

		viewShips.setOnAction(e->{
			drawShips();
			stage.setScene(sceneMap.get("battle"));

		});



		endTurn.setOnAction(e->{

			
			if(!singlePlayerGame) {
				clearShips();
	
				if(curPlayer.getName().equals("Player 1")) {
					saveData(player1);
	
					if(!flag) {
						loadData(player2);
					}
	
					curPlayer = player2;
					curPlayerText.setText("Player 2: " + player2.getPoints() + " Points");
					player1.setMoves(numMoves);
					numMoves = player2.getMoves();
	
				}else {
					saveData(player2);
					loadData(player1);
					curPlayer = player1;
					curPlayerText.setText("Player 1: "+ player1.getPoints() + " Points");
					chooseMode = false; // ship placing phase has ended
					player2.setMoves(numMoves);
					numMoves = player1.getMoves();
				}
	
				if(chooseMode && flag) { // if in ship placing phase AND player 1's first turn
					countPoints = 0;
					bShip = new Battleship();
					cShip = new Carrier();
					crShip = new Cruiser();
					sShip = new Submarine();
					flag = false; // player 1's first turn has ended
				}	
	
				shipDescriptions.setText(bShip.getShipType());
				Health.setText("" + bShip.getCurHealth());		
				curShip = bShip;
				numMovesRem.setText("Moves left: " + numMoves);
	
	
				if(moveType != null) {
	
					for(int i = 0 ; i < moveButtons.size(); i++) {
						moveButtons.get(i).setStyle("-fx-font-family : Helvetica Neue;");
					}
	
	
					moveType = null;
				}
	
	
				getMove.clear();
				getXCoord.clear();
				getYCoord.clear();
				drawShips();
				fire.setDisable(false);
				hitMiss.setText("HIT/MISS");
				double health = ((double)bShip.getCurHealth())/100.0;
				healthBar.setProgress(health);
			}//end localMulti endturn
			else {//single player game, so no need to switch player data
				chooseMode = false;
				clearShips();
				getMove.clear();
				getXCoord.clear();
				getYCoord.clear();
				fire.setDisable(false);
				player1.setMoves(8);
				numMoves = player1.getMoves();
				numMovesRem.setText("Moves left: " + numMoves);
				
				if(firstTurn) {
					Vector<Ship> v = new Vector<Ship>();
					v.add(bShip);
					v.add(cShip);
					v.add(crShip);
					v.add(sShip);
					
					player1.setShip(bShip, 0);
					player1.setShip(cShip, 1);
					player1.setShip(crShip, 2);
					player1.setShip(sShip, 3);
				
					Battleship aiB = new Battleship();
					Carrier aiC = new Carrier();
					Cruiser aiCr = new Cruiser();
					Submarine aiS = new Submarine();
					
					/*
					setShipAtRandom(aiB);
					setShipAtRandom(aiC);
					setShipAtRandom(aiCr);
					setShipAtRandom(aiS);
					*/
					
					Vector<Point> temp = new Vector<Point>();
					Point p1 = new Point(1,1);
					Point p2 = new Point(1,2);
					Point p3 = new Point(1,3);
					Point p4 = new Point(1,4);					
					
					temp.add(p1);
					temp.add(p2);
					temp.add(p3);
					temp.add(p4);
					aiB.setPointsOn(temp);
					
					temp = new Vector<Point>();
					p1 = new Point(2,1);
					p2 = new Point(2,2);
					p3 = new Point(2,3);
					p4 = new Point(2,4);
					Point p5 = new Point(2,5);
					
					temp.add(p1);
					temp.add(p2);
					temp.add(p3);
					temp.add(p4);
					temp.add(p5);
					aiC.setPointsOn(temp);
					
					temp = new Vector<Point>();
					p1 = new Point(3,1);
					p2 = new Point(3,2);
					p3 = new Point(3,3);
					
					temp.add(p1);
					temp.add(p2);
					temp.add(p3);
					aiCr.setPointsOn(temp);
					
					temp = new Vector<Point>();
					p1 = new Point(4,1);
					p2 = new Point(4,2);
					p3 = new Point(4,3);
					
					temp.add(p1);
					temp.add(p2);
					temp.add(p3);
					aiS.setPointsOn(temp);
					
					
					Vector<Ship> aiShips = new Vector<>();
					aiShips.add(aiB);
					aiShips.add(aiC);
					aiShips.add(aiCr);
					aiShips.add(aiS);
					
					ai.setShip(aiB, 0);
					ai.setShip(aiC, 1);
					ai.setShip(aiCr, 2);
					ai.setShip(aiS, 3);
					/*
					for(int i =0; i<aiShips.size();i++) {
						for(int j=0;j<aiShips.get(i).getPointsOn().size();j++) {//shipVector->ship->pointVector->size
							System.out.print("Ship " + i + " is on " + aiShips.get(i).getPointsOn().get(j).x + " " + aiShips.get(i).getPointsOn().get(j).y );
						}
					}
					*/
					
					hitMiss.setText("AI has set their ships");
					firstTurn =false;
				}
				else {//ai's turn to shot
					Point aiShot = ai.nextShot();
					Ship aiCurShip = new Ship();
					int i=0;
					while(i<ai.getShips().size()) {//check if ai has any non sunk ships
						if(ai.getShips().get(i).isSunk()) {
							i++;
						}
						else {
							aiCurShip = ai.getShips().get(i);
							i = ai.getShips().size()+1;
			
							break;
						}
					}
					
					if(i == ai.getShips().size()) {//no un-sunk ships found, p1 wins
						stage.setScene(sceneMap.get("end"));
						victoryDeclaration.setText("Player 1 has won!");
					}
					else {//found a ship to use, fires at p1
						if(HitDetection.fireAtEnemy(player1,aiShot,aiCurShip.getWeapon())) {
							hitMiss.setText("YOUR SHIP WAS HIT AT (" + aiShot.x + ", "+aiShot.y+")");
							ai.setLastShot(aiShot, true);
						}
						else {
							hitMiss.setText("THE AI MISSED WITH SHOT (" + aiShot.x + ", "+aiShot.y+")");
							ai.setLastShot(aiShot, false);
						}
					}
				}//end ai shot
				
				drawShips();
				double health = ((double)curShip.getCurHealth())/100.0;
				healthBar.setProgress(health);
				
			}//end ai endturn


			
			
		});

		endTurn.setDisable(true);

		fire.setOnAction(e->{
			int xCoord =  Integer.parseInt(getXCoord.getText());
			int yCoord = Integer.parseInt(getYCoord.getText());
			Point tempP = new Point(xCoord,yCoord);
			
			String pointString = "(" + Integer.toString(xCoord) + "," + Integer.toString(yCoord) + ")";
			
			
			if(curShip.getCurHealth()==0) {
				hitMiss.setText("CAN'T FIRE, THIS SHIP IS SUNK!");
				return;
			}
			
			if(!singlePlayerGame) {
				if(curPlayer.getName().equals("Player 1")) {
					if(HitDetection.fireAtEnemy(player2,tempP,curShip.getWeapon())) {
						hitMiss.setText("HIT ENEMY SHIP");
						player1.addPoints(25);//each hit rewards 25 points
						shotInfo.setText("Shot Info : Player 1 hits at " + pointString);
						curPlayerText.setText("Player 1: " + player1.getPoints() + " Points");
					}
					else {
						hitMiss.setText("MISSED ENEMY SHIPS");
						shotInfo.setText("Shot Info : Player 1 missed at " + pointString);
					}
				}
				else {
					if(HitDetection.fireAtEnemy(player1,tempP,curShip.getWeapon())) {
						hitMiss.setText("HIT ENEMY SHIP");
						player2.addPoints(25);
						shotInfo.setText("Shot Info : Player 2 hits at " + pointString);
						curPlayerText.setText("Player 2: " + player2.getPoints() + " Points");
					}
					else {
						hitMiss.setText("MISSED ENEMY SHIPS");
						shotInfo.setText("Shot Info : Player 2 missed at " + pointString);
					}
				}

				fire.setDisable(true);				
				String health = Integer.toString(player2.getShips().get(0).getCurHealth());				
				System.out.println("Health : " + health);

			}//end multiLocal
			
			else {//singleplayer game
				if(HitDetection.fireAtEnemy(ai, tempP, curShip.getWeapon())) {
					hitMiss.setText("HIT ENEMY SHIP");
					player1.addPoints(25);//each hit rewards 25 points
					shotInfo.setText("Shot Info : Player 1 hits at " + pointString);
					curPlayerText.setText("Player 1: " + player1.getPoints() + " Points");
				}
				else {
					hitMiss.setText("MISSED ENEMY SHIP");
					shotInfo.setText("Shot Info : Player 1 missed at " + pointString);
				}
				fire.setDisable(true);
			}//end singleplayer


		/*
			if(curShip.getWeapon() == "Torpedo"){
				Vector<Point> toHit = curShip.getWeapon().pointToHit(tempP);
				for(int i = 0; i < toHit.size(); i++){
					// do Hit detection for each of the points
				}
				fire.setDisable(true);
			}
			else if(curShip.getWeapon() == "doubleShotGun"){
				// do regular hit detection on tempP
				curShip.getWeapon.hasShot();
				int left = curShip.getWeapon.numShootLeft();
				if(Left == 0){
					fire.setDisable(true);
				}
				// set both shotOnce and shotTwice back to default at start of turn
			}
			else if(curShip.getWeapon() == "HorizontalLineGun"){
				Vector<Point> toHit = curShip.getWeapon().getAllCoordinates(tempP);
				for(int i = 0; i < toHit.size(); i++){
					// do Hit detection for each of the points
				}
				fire.setDisable(true);
			}
			else if(curShip.getWeapon() == "VerticalLineGun"){
				Vector<Point> toHit = curShip.getWeapon().getAllCoordinates(tempP);
				for(int i = 0; i < toHit.size(); i++){
					// do Hit detection for each of the points
				}
				fire.setDisable(true);
			}
			else{
				//do regular hit detection
			}
			*/
			
			
			// Check if the game has ended
			if(player1.isAllSunk())
			{
				stage.setScene(sceneMap.get("end"));
				victoryDeclaration.setText("Player 2 has won!");
			}
			else if(player2.isAllSunk())
			{
				stage.setScene(sceneMap.get("end"));
				victoryDeclaration.setText("Player 1 has won!");
			}
		});



		moveButtons = new  Vector<Button>();

		moveButtons.add(forward);
		moveButtons.add(reverse);
		moveButtons.add(CW);
		moveButtons.add(CCW);

		for(int i = 0; i < moveButtons.size(); i++) {

			Button b1 = moveButtons.get(i);
			b1.setOnAction(e->{
				b1.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-background-color: #ff9933;"); // color the selected button
				moveType = b1.getText();

				for(int x = 0; x < moveButtons.size(); x++) {   // clear all buttons besides the selected button
					if(moveButtons.get(x) != b1) {
						moveButtons.get(x).setStyle("-fx-font-family : Helvetica Neue;");

					}
				}

			});

		}


		shipDescriptions.setStyle("-fx-font-family : Helvetica Neue;");
		shipDescriptions.setFont(new Font(18));

		//showChosenShip.getChildren().addAll(shipDescriptions);
		//infoForPlayer.getChildren().addAll(topBox, numMovesRem, Health);
		allInfo.getChildren().addAll(shipDescriptions);
		//borderPane.setTop(allInfo);

		fireInfo.setAlignment(Pos.CENTER);
		moveInfo.setAlignment(Pos.CENTER);

		upDown.getChildren().addAll(forward, reverse);
		rotation.getChildren().addAll(CW,CCW);

		healthBar = new ProgressBar();
		healthBar.setProgress(1.0f);
		healthBar.setPrefSize(100, 25);
		healthBar.setStyle("-fx-accent :  #008000;" );

		moveInfo.getChildren().addAll(allInfo, shotInfo, rotation, upDown, getMove, move);
		moveInfo.setSpacing(30);
		fireInfo.getChildren().addAll(curPlayerText,numMovesRem, healthBar,  X, getXCoord, Y, getYCoord, fire, hitMiss);
		fireInfo.setSpacing(15);

		upDown.setAlignment(Pos.CENTER);
		rotation.setAlignment(Pos.CENTER);
		allInfo.setAlignment(Pos.CENTER);

		borderPane.setTop(chooseShips);

		HBox bottomBox = new HBox(viewShips, endTurn);
		endTurn.setStyle("-fx-font-family : Helvetica Neue;" +  "-fx-background-color : #808080;" + "-fx-font-size: 15px;" + "-fx-text-fill: #FFD700;");


		HBox mainBox = new HBox(moveInfo, fireInfo);
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(50);
		VBox centerBox = new VBox(mainBox, bottomBox);
		centerBox.setAlignment(Pos.CENTER);
		borderPane.setCenter(centerBox);
		centerBox.setSpacing(30);

		shipDescriptions.setTextAlignment(TextAlignment.LEFT);

		move.setStyle("-fx-font-family : Helvetica Neue;" +  "-fx-background-color : #808080;" + "-fx-font-size: 15px;" + "-fx-text-fill: #FFD700;");
		viewShips.setStyle("-fx-font-family : Helvetica Neue;" +  "-fx-background-color : #808080;" + "-fx-font-size: 15px;" + "-fx-text-fill: #FFD700;");
		fire.setStyle("-fx-font-family : Helvetica Neue;" +  "-fx-background-color : #808080;" + "-fx-font-size: 15px;" + "-fx-text-fill: #FFD700;");



		//borderPane.setLeft(moveInfo);
		//borderPane.setRight(fireInfo);
		//borderPane.setBottom(viewShips);
		viewShips.setAlignment(Pos.TOP_CENTER);
		bottomBox.setAlignment(Pos.TOP_CENTER);
		borderPane.setBackground(new Background(new BackgroundFill(Color.NAVAJOWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		bottomBox.setSpacing(30);

		return new Scene(borderPane, 600, 600);
	}


	// Create scene to show the location of the ships on GUI

	public Scene createMoveGUI() {

		gridPane = new GridPane();
		borderPane2 = new BorderPane();

		placeShip = new Button("Place Ship");
		back = new Button("Back");

		gridPane.setHgap(3);
		gridPane.setVgap(3);
		gridPane.setAlignment(Pos.CENTER);
		
		for(int i = 0; i < rowSize; i++) {

			for(int j = 0; j < colSize; j++) {

				int x = (i * rowSize) + j ;
				ImageView b1;
				if(gameMap.getTile(i, j).getType() == 'O')	// If tile is an Ocean Tile, fill with ocean.jpg
					b1 = new ImageView("ocean.png");
				else // 
					b1 = new ImageView("island.png");		// If not Ocean Tile, then it must be an Island Tile
				b1.setFitHeight(30);
				b1.setFitWidth(30);

				bGrid[i][j] = b1;	

				b1.setOnMouseClicked(e -> {

					if(!chooseMode) {   // the game is not in ship placing phase
						return;
					}

					if(!curShip.getFlag()) {  // the positions of the current ship has not been picked

						int[] p1 = numToCord(x);
						System.out.println("Cord : (" + Integer.toString(p1[0]) + "," + Integer.toString(p1[1]) + ")"  ) ;

						if(shipsContainsPoint(p1[0], p1[1])){   // Clicked point was already chosen for another ship 
							return;
						}				

						for(int index = 0; index < countPoints; index++) { // Clicked point was already chosen for this ship

							if(points[index].x == p1[0] && points[index].y == p1[1]) {
								return;
							}

						}
						
						if(!checkPoint(p1[0], p1[1])) {   // check if the chosen point was a valid choice - whether we restart the process or not

							clearChosenShip();
							countPoints = 0;
						}

						if(gameMap.isIsland(p1[0], p1[1]) || isAdjacentToIsland(p1[0], p1[1])) // if clicked point is an island or adjacent to one, return
							return;
						
						points[countPoints] = new Point(p1[0],p1[1]);   // add the new point to the array
						countPoints += 1;   							// increase the size of the array
						drawChoosingShip();	   							// draw the current ship being chosen				

						if(countPoints == curShip.getLength()) {    // if number of chosen points equals length of ship, so done

							placeShip.setDisable(false);         // can place ships
						}else {	
							placeShip.setDisable(true);    // cannot place ships
						}		

					}

				});

				//b1.setOnAction(myHandler);
				gridPane.add(b1, i, j);

			}
		}

		borderPane2.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));


		HBox buttonBox = new HBox(back, placeShip);
		buttonBox.setSpacing(10); 
		buttonBox.setAlignment(Pos.CENTER);
		back.setAlignment(Pos.TOP_CENTER);
		placeShip.setAlignment(Pos.TOP_CENTER);
		VBox mainBox = new VBox(gridPane, buttonBox);
		mainBox.setSpacing(30);
		mainBox.setAlignment(Pos.CENTER);
		borderPane2.setCenter(mainBox);


		back.setOnAction(e->{	
			stage.setScene(sceneMap.get("control"));
		});

		placeShip.setDisable(true);
		placeShip.setOnAction(e->{
			Vector<Point> vec = arrToVec();
			curShip.setPointsOn(vec);
			curShip.setFlag(true);
			countPoints = 0;
			placeShip.setDisable(true);

			if(bShip.getFlag() && crShip.getFlag() && cShip.getFlag() && sShip.getFlag()) { // if all ships placed

				endTurn.setDisable(false);
			}

		});

		back.setStyle("-fx-font-family : Helvetica Neue;" +  "-fx-background-color : #808080;" + "-fx-font-size: 15px;" + "-fx-text-fill: #FFD700;");
		placeShip.setStyle("-fx-font-family : Helvetica Neue;" +  "-fx-background-color : #808080;" + "-fx-font-size: 15px;" + "-fx-text-fill: #FFD700;");


		return new Scene(borderPane2, 700, 700);
	}

	// This methods checks that given the point x and y chosen by the user is in the correct directions since the 
	// ship points will be either in horizontal or vertical direction and building below the head of the ship

	public boolean checkPoint(int x, int y) {

		if(isNearIsland(x, y)) // if they've chosen a point on or adjacent to an Island, bad point, restart placement
		{
			System.out.println("NEAR ISLAND!!!");
			return false;
		}
		
		if(countPoints == 0) {   // Chosen point is first point so no checking needed
			return true;

		}else if( countPoints == 1) {    // Chosen point is second point so check if either x or y matches and distance is one from last point

			return ( ((x == points[0].x) && isNear(points[0].y, y )) || ((y == points[0].y)  && (isNear(points[0].x,x))) ) ;

		}else if (countPoints < curShip.getLength()) {		   // if number of chosen points are less than ships length

			boolean ver = (points[0].x == points[1].x) ? true  : false;  // true if ship position is vertical false if horizontal

			for(int i = 0; i < (countPoints - 1); i++) {   // loop through the list of chosen ship points

				// check if the x coordinate matches if vertical or y coordinate matches if horizontal return false if not
				// also check if points are within one unit distance of each other

				if(ver &&  ((points[i].x != points[i+1].x) || !isNear(points[i].y, points[i+1].y)) ) {
					return false;
				}else if(!ver && ( (points[i].y != points[i+1].y)  ||  !isNear(points[i].x, points[i+1].x)   ) ) {
					return false;
				}

			}

			// perform the same check as in the above for loop for the parameter point

			if(ver &&  ((points[countPoints-1].x != x) || !isNear(points[countPoints-1].y, y)) ) {
				return false;
			}else if(!ver && ( (points[countPoints-1].y != y)  ||  !isNear(points[countPoints-1].x, x)   ) ) {
				return false;
			}

			return true;   // ship has valid position 
		}


		return false;
	}
	

	// check whether any  ship is on a given coordinate

	private boolean shipsContainsPoint(int x, int y) {

		return shipContainsPoint(bShip, x, y) || shipContainsPoint(cShip, x, y) || shipContainsPoint(crShip, x, y) || shipContainsPoint(sShip, x, y);
	}
	
	private boolean isAdjacentToIsland(int x, int y)
	{
		// If adjacent coordinate is in bounds and an island, return true  (assumed x and y are in bounds)
		
		if((x - 1) > -1 && gameMap.isIsland(x - 1, y)) 		// Island to the left?
			return true;
		if((x + 1) < rowSize && gameMap.isIsland(x + 1, y))	// Island to the right?
			return true;
		if((y - 1) > -1 && gameMap.isIsland(x, y - 1))		// Island below?
			return true;
		if((y + 1) < colSize && gameMap.isIsland(x, y + 1)) // Island above?
			return true;
		
		return false;
	}
	
	private boolean isNearIsland(int x, int y) {
		return (gameMap.isIsland(x, y) || isAdjacentToIsland(x, y)); // if clicked point is an island or adjacent to one, return
	}
	// Save the data of the appropriate variables to the given player object

	private void saveData(Player p) {
		p.setShip(bShip, 0);
		p.setShip(cShip, 1);
		p.setShip(crShip, 2);
		p.setShip(sShip, 3);
	}

	// Load the data of given player to the appropriate variables


	private void loadData(Player p) {
		Vector<Ship>  ships = p.getShips();
		bShip = (Battleship) ships.get(0);
		cShip = (Carrier) ships.get(1);
		crShip = (Cruiser) ships.get(2);
		sShip = (Submarine) ships.get(3);
	}

	// Method to check if a given ship is on  a given coordinate

	private boolean shipContainsPoint(Ship s, int x, int y) {
		Vector<Point> PointsOn = s.getPointsOn();

		for(int i = 0; i < PointsOn.size(); i++) {

			Point p1 = PointsOn.get(i);

			if(p1.x == x && p1.y == y) {
				return true;
			}

		}

		return false;
	}

	// Method to draw all the different types of ships

	private void drawShips() {
		drawShip(bShip);
		drawShip(crShip);
		drawShip(sShip);
		drawShip(cShip);
	}


	// Method to draw a given ship

	private void drawShip(Ship s) {
		Vector<Point> PointsOn = s.getPointsOn();


		for(int i = 0; i < PointsOn.size(); i++) {

			Point p1 = PointsOn.get(i);

			if(i == 0) {
				bGrid[p1.x][p1.y].setImage(new Image("head.png"));
			}else {
				bGrid[p1.x][p1.y].setImage(new Image("part.png"));
			}

		}
	}

	// Method to clear all ships from the GUI

	private void clearShips() {
		clearShip(bShip);
		clearShip(crShip);
		clearShip(sShip);
		clearShip(cShip);
	}

	// Method to clear a given ship from the GUI

	private void clearShip(Ship s) {
		Vector<Point> PointsOn = s.getPointsOn();

		for(int i = 0; i < PointsOn.size(); i++) {

			Point p1 = PointsOn.get(i);
			bGrid[p1.x][p1.y].setImage(new Image("ocean.png"));	

		}
	}


	// This methods draws the position of the ship points chosen by the user

	private void drawChoosingShip() {

		for(int i = 0; i < this.countPoints; i++) {

			if(i == 0) {
				bGrid[points[0].x][points[0].y].setImage(new Image("head.png"));
			}else {
				bGrid[points[i].x][points[i].y].setImage(new Image("part.png"));
			}


		}

	}

	// This methods clears the GUI when the player is choosing ship points to place ship on and decides to choose another position

	private void clearChosenShip() {
		for(int i = 0; i < this.countPoints; i++) {	

			bGrid[points[i].x][points[i].y].setImage(new Image("ocean.png"));

		}


	}

	// Converts the current array of points clicked to vector, this will be used to get the points of the ship selected by the player

	private Vector<Point> arrToVec(){

		Vector<Point> vec = new Vector<Point> ();

		for(int i = 0; i < countPoints; i++) {
			vec.add(points[i]);
		}

		return vec;
	}


	private boolean isNear(int n, int m) {
		return (   ((n-m) == 1) || ((n-m) == -1)    );
	}


	// given a number it returns the coordinates the number belongs to. This method is useful for grids.
	private int[] numToCord(int x) {
		int row = x / rowSize;
		int col = x % rowSize;

		return new int[] {row,col};
	}

	// Method checks if player can correctly move up and down and returns vector of points if it can, 
	// else null is returned.  

	public Vector<Point> canMove(Ship ship, int amount) {
		Vector<Point> points = ship.getPointsOn();

		Point p1 = points.get(0);
		Point p2 = points.get(1);

		if(p1.x == p2.x) {   // vertical

			if(amount > 0 ) {   // moving forward

				int x = p1.y - p2.y; // used to determine head orientation

				if(x > 0 && ((p1.y + amount) < rowSize)  ) {  // head facing down
					
					if(!isMovingClear(ship, true, true , true, amount)) {
						return null;
					}
					
					
					return movedShipPoints(ship, true, true, amount);
				}else if(x < 0 && ( (p1.y - amount) >= 0) ) {   // head facing up 
					
					if(!isMovingClear(ship, true, false , true,amount)) {
						return null;
					}
					
					return movedShipPoints(ship, true, false, amount);
				}


			}else {     // moving backward

				int x = p1.y - p2.y;
				Point pL = points.lastElement();

				if(x > 0 && ((pL.y + amount)  >= 0 ) ) {       // head facing down 
					
					if(!isMovingClear(ship, true,false , false,amount)) {
						return null;
					}
					
					return movedShipPoints(ship, true, true, amount);
				}else if(x < 0 && ( (pL.y - amount) < rowSize) ) {   // head facing up 
					
					if(!isMovingClear(ship, true, true , false,amount)) {
						return null;
					}
					
					return movedShipPoints(ship, true, false, amount);

				}


			}


		}else if(p1.y == p2.y) {  // horizontal

			if(amount > 0) {    // moving forward


				int x = p1.x - p2.x;

				if(x > 0 && ((p1.x + amount) < colSize)  ) {   // head facing right 
					
					if(!isMovingClear(ship, false, true , true,amount)) {
						return null;
					}
					
					return movedShipPoints(ship, false, true, amount);
				}else if(x < 0 && ( (p1.x - amount) >= 0) ) {    // head facing left
					
					if(!isMovingClear(ship, false, false , true,amount)) {
						return null;
					}
					
					
					return movedShipPoints(ship, false, false, amount);
				}


			}else {          // moving backward

				int x = p1.x - p2.x;
				Point pL = points.lastElement();

				if(x > 0 && ((pL.x + amount) >= 0)  ) {    // head facing right 
					
					if(!isMovingClear(ship, false, false , false,amount)) {
						return null;
					}
					
					
					return movedShipPoints(ship, false, true, amount);
				}else if(x < 0 && ( (pL.x - amount) < colSize ) ) {    // heading facing left
					
					if(!isMovingClear(ship, false, true , false,amount)) {
						return null;
					}
					
					
					return movedShipPoints(ship, false, false, amount);
				}

			}

		}


		return null;
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
	
	
	public Vector<Point> canRotate(Ship s, int degrees){
		Vector<Point> pointsOn = s.getPointsOn();
		boolean clockwise = (moveType.equals("CW")) ? true : false;
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
	
	

	// Method returns a vector of points by manipulating x or y coordinates of ship points by given amount
	// used for moving up and down. 

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
	
	public boolean isMovingClear(Ship s, boolean ver, boolean add, boolean first, int amount1) {
		
		Vector<Point> pointsOn = s.getPointsOn();
		Point p1;

		if(first) {
			p1 = pointsOn.get(0);
		}else {
			p1 = pointsOn.lastElement();
		}
		
		int amount = Math.abs(amount1);

		for(int i = 1; i <= amount ; i++) {
			
			Vector<Point> v1 = new Vector<Point>();
			Point p2;
			
			if(ver) {		
				if(add) {
					p2 = new Point(p1.x, p1.y + i); 				
				}else {
					p2 = new Point(p1.x, p1.y - i);			
				}
				
			}else {		
				if(add) {
					p2 = new Point(p1.x + i, p1.y );					
				}else {
					p2 = new Point(p1.x - i, p1.y );					
				}				
			}
				
			v1.add(p2);
			if(!checkOtherShipPoints(curShip, v1) || gameMap.isIsland(p2.x, p2.y) ){
				return false;
			}
									
		}
	
		
		return true;
	}
	
	public boolean containsIslandPoints(Vector<Point> listPoints) {
		
		for(int i = 0; i < listPoints.size(); i++) {
			Point p1 = listPoints.get(i);
			
			if(gameMap.isIsland(p1.x, p1.y)) {
				return true;
			}
			
		}
		
		return false;
	}


	/*   * * * * * = horizontal, same y-coordinates

	 *
	 *
	 *
	 *
								   = vertical, same x-coordinates
	 */



	// method to determine whether a ships position is horizontal or vertical
	// loop through ship coordinates and determine whether they have same y-coordinates or x-coordinates 


	public static boolean shipPos(Ship ship) {

		Vector<Point> points = ship.getPointsOn();

		Point p1 = points.get(0);
		Point p2 = points.get(1);

		return (p1.x == p2.x );
	}

	// Returns false if any point in given vector of points is on any ships besides the given ship,
	// returns true otherwise

	public boolean checkOtherShipPoints(Ship s, Vector<Point> vec) {

		for(int i = 0; i < vec.size(); i++) {

			Point p1 = vec.get(i);

			if(!s.getShipType().equals(bShip.getShipType()) && shipContainsPoint(bShip, p1.x, p1.y)   ) {
				return false;
			}

			if(!s.getShipType().equals(cShip.getShipType()) && shipContainsPoint(cShip, p1.x, p1.y)   ) {
				return false;
			}

			if(!s.getShipType().equals(crShip.getShipType()) && shipContainsPoint(crShip, p1.x, p1.y)   ) {
				return false;
			}

			if(!s.getShipType().equals(sShip.getShipType()) && shipContainsPoint(sShip, p1.x, p1.y)   ) {
				return false;
			}

		}


		return true;
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

	// This method  will check whether ship can move up or down by the given amount.

	public boolean move(Ship ship, int amount) {
		
		if(moveType.equals("CW") || moveType.equals("CCW")) {
			
			Vector<Point> newPoints = canRotate(curShip, amount);
			
			if(newPoints != null && checkOtherShipPoints(ship, newPoints) && !containsIslandPoints(newPoints) ) {
				ship.setPointsOn(newPoints);
				System.out.println(newPoints);
				printVec(newPoints);
			}else {
				return false;
			}
			
		}else {
			
			Vector<Point> vec = canMove(ship, amount);   // call canMove to get new list of points of moved ship if ship can move

			if(vec == null) { 		// ship cannot move
				return false;
			}			
			
			if(checkOtherShipPoints(ship,vec) && !containsIslandPoints(vec)) {  // check if points ship will move to are not occupied by other ships

				ship.setPointsOn(vec);   // set the new ship points
				System.out.println(vec.toString());
				printVec(vec);
			}
			
		}
			
		return true;
	}
	
	private void checkForIslandPoints()
	{
		Vector<Point> shipPoints = curShip.getPointsOn();
		shipPoints.forEach((n) -> 
		{
			if(isAdjacentToIsland(n.x, n.y))
			{
				Vector<Point> adjacentIslands = getAdjacentIslands(n.x, n.y);
				adjacentIslands.forEach((p) -> 
				{
					if(curPlayer.getName().equals("Player 1"))
					{
						if(!((Island) gameMap.getTile(p.x, p.y)).p1Visited())
						{
							((Island) gameMap.getTile(p.x, p.y)).setP1Visited(true);
							player1.addPoints(((Island) gameMap.getTile(p.x, p.y)).getPoints());
							curPlayerText.setText("Player 1: " + player1.getPoints() + " Points");

						}
					}
					else // Player 2
					{
						if(!((Island) gameMap.getTile(p.x, p.y)).p2Visited())
						{
							((Island) gameMap.getTile(p.x, p.y)).setP2Visited(true);
							player2.addPoints(((Island) gameMap.getTile(p.x, p.y)).getPoints()); 	// add points
							curPlayerText.setText("Player 2: " + player2.getPoints() + " Points"); 	// update points text
						}
					}
				});
			}
				
		});
	}

	private Vector<Point> getAdjacentIslands(int x, int y) {
		Vector<Point> adjacentIslands = new Vector<Point>();
	
		if((x - 1) > -1 && gameMap.isIsland(x - 1, y)) 		// Island to the left?
			adjacentIslands.add(new Point(x - 1, y));
		if((x + 1) < rowSize && gameMap.isIsland(x + 1, y))	// Island to the right?
			adjacentIslands.add(new Point(x + 1, y));
		if((y - 1) > -1 && gameMap.isIsland(x, y - 1))		// Island below?
			adjacentIslands.add(new Point(x, y - 1));
		if((y + 1) < colSize && gameMap.isIsland(x, y + 1)) // Island above?
			adjacentIslands.add(new Point(x, y + 1));
		
		return adjacentIslands;
	}

	public void printVec(Vector<Point> vec) {
		for(int i = 0; i < vec.size(); i++) {
			Point p1 = vec.get(i);
			String s = "(" + Integer.toString(p1.x) + "," + Integer.toString(p1.y) + ")";
			System.out.print(s + " ");
		}
		System.out.println();
	}

		// This method allows for a single ship to be placed randomly on the board
	// Helpful for when computer is playing
	public void setShipAtRandom(Ship s) {
		double randNum = Math.random();
		boolean shipVert = false;
		int lengthS = s.getLength();
		
		// decides whether the ship should be place vertically or horizontally
		if(randNum < 0.5) {
			shipVert = true;
		}
		else {
			shipVert = false;
		}
		boolean allValid = false;
		Point shipPoints2[] = new Point[lengthS];
		
		// stuff
		
		while(allValid == false) {
			allValid = true;
			//System.out.print("got here");
			// should return a random value from 0 to 100
			int headPos = (int) Math.random() * 100;
			int newPoint[] = numToCord(headPos);
			Point shipPoints[] = new Point[lengthS]; 
			shipPoints[0] = new Point(newPoint[0], newPoint[1]);
			
			
			// get a set of points that could be ship
			if(shipVert == true) {
				int numToAddVert = 1;
				if(newPoint[1] >= 5) {
					numToAddVert = -1;
				}
				for(int i = 1; i < lengthS; i++) {
					shipPoints[i] = new Point(newPoint[i-1], newPoint[i-1]+numToAddVert);
					
					if(isValidPoint(shipPoints[i]) == false) {
						allValid = false;
						break;
					}
				}
			} // if
			else {
				int numToAddHor = 1;
				if(newPoint[0] >= 5) {
					numToAddHor = -1;
				}
				for(int i = 1; i < lengthS; i++) {
					shipPoints[i] = new Point(newPoint[i-1]+numToAddHor, newPoint[i-1]);
					if(isValidPoint(shipPoints[i]) == false) {
						allValid = false;
						break;
					}
					
				}
			}// else
			shipPoints2 = shipPoints;
		}// while

		Vector<Point> allShipPoints = turnArrToVec(shipPoints2);
		s.setPointsOn(allShipPoints);
		
	}// end of setShipRandome

	// draw all ships for random
	public void setAllShipsAtRandom() {
		setShipAtRandom(bShip);
		setShipAtRandom(cShip);
		setShipAtRandom(crShip);
		setShipAtRandom(sShip);
		
		drawShips();
	}
	
	// return true if the point is on the board
	// returns false if point is not on the board
	public boolean pointWithinRange(Point p) {

		if(p.x < 0 || p.x >= colSize || p.y < 0 || p.y >= rowSize) {
			return false;
		}
		return true;
	}
	
	// checks if the point is already taken on the grid or not
	// will eventually check if it is on island or not
	public boolean isValidPoint(Point p) {
		boolean onBoard = pointWithinRange(p);
		boolean onShip = shipsContainsPoint(p.x, p.y);
		//boolean onIsland;
		
		if(onBoard == true && onShip == true) {
			return true;
		}
		return false;
	} // isValidPoint
	
	public Vector<Point> turnArrToVec(Point[] p){
		Vector<Point> v = new Vector<>();
		for(int i = 0; i< p.length; i++) {
			v.add(p[i]);
		}
		return v;
	}// end of vector<Point>

}

