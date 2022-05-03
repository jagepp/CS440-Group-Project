import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.function.Consumer;

public class Server{

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	GameInfo gameInfo; // Master GameInfo object used for server logic
	
	Server(Consumer<Serializable> call){
	
		callback = call;
		server = new TheServer();
		gameInfo = new GameInfo();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
		    
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();
				
				count++;
				//if(count == 1)
				//	c.updatePlayerOne(gameInfo);
				//else
				//	c.updatePlayerTwo(gameInfo);
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			
		
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}
			
			public void updatePlayerOne(GameInfo gameInfo) {
				
				gameInfo.setIsPlayerOne(true);
				
				// Don't want to send sensitive Player 2 info to Player 1
				Player temp = gameInfo.getPlayer2(); 
				gameInfo.setPlayer2(null);
				
				ClientThread t = clients.get(0);
				try {
				 t.out.writeObject(gameInfo);
				 t.out.reset();
				}
				catch(Exception e) {}
				
				gameInfo.setPlayer2(temp);
			}
			
			public void updatePlayerTwo(GameInfo gameInfo) {
				
				gameInfo.setIsPlayerOne(false);
				
				// Don't want to send sensitive Player 1 info to Player 2
				Player temp = gameInfo.getPlayer1(); 
				gameInfo.setPlayer1(null);
				
				ClientThread t = clients.get(1);
				try {
				 t.out.writeObject(gameInfo);
				 t.out.reset();
				}
				catch(Exception e) {}
				
				gameInfo.setPlayer1(temp);
			}
			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
									
				// New ClientThread has started. If the client count is 1, then this is client 1 (aka Player 1). Else it's player 2
				if(count == 1)
					updatePlayerOne(gameInfo);
				else
					updatePlayerTwo(gameInfo);
				
				 while(true) {
					    try {
					    	GameInfo newGameInfo  = (GameInfo) in.readObject();
					    	callback.accept("client: " + count + " sent data");
					    	
					    	if(newGameInfo.isPlaceShipPhase())
					    	{
					    		if(newGameInfo.isPlayerOne())
					    		{
					    			
					    			gameInfo.setPlayer1(newGameInfo.getPlayer1());
					    			gameInfo.setP1PlacedShips(true);
					    			
					    			System.out.println("Start p1 ship positions");
					    			printVec(gameInfo.getPlayer1().getShips().get(0).getPointsOn());
					    			printVec(gameInfo.getPlayer1().getShips().get(1).getPointsOn());
					    			printVec(gameInfo.getPlayer1().getShips().get(2).getPointsOn());
					    			printVec(gameInfo.getPlayer1().getShips().get(3).getPointsOn());
					    			System.out.println("End");

					    		}
					    		else // Player 2
					    		{
					    			gameInfo.setPlayer2(newGameInfo.getPlayer2());
					    			gameInfo.setP2PlacedShips(true);
					    			
					    			System.out.println("Start p2 ship positions");
					    			printVec(gameInfo.getPlayer2().getShips().get(0).getPointsOn());
					    			printVec(gameInfo.getPlayer2().getShips().get(1).getPointsOn());
					    			printVec(gameInfo.getPlayer2().getShips().get(2).getPointsOn());
					    			printVec(gameInfo.getPlayer2().getShips().get(3).getPointsOn());
					    			System.out.println("End");
					    		}
					    		
					    		// Check if both players have placed ships
					    		if(gameInfo.p1PlacedShips() && gameInfo.p2PlacedShips())
					    		{
					    			gameInfo.setPlaceShipPhase(false);
					    			updatePlayerOne(gameInfo); // Begin the game with Player 1's turn
					    		}
					    	}
					    	else // We are receiving fire or end turn
					    	{
					    		if(newGameInfo.isPlayerOne())
					    		{
					    			if(newGameInfo.isFiring()) // receiving fire
					    			{
					    				gameInfo.setHasFired(true);

					    				if(HitDetection.fireAtEnemy(gameInfo.getPlayer2(), newGameInfo.getFiringPoint(), newGameInfo.getFiringWeapon()))
					    				{
					    					// hit on p2
				    						callback.accept("Client 1 hit Client 2's ship!");

				    						// Check for game ending
					    					if(gameInfo.getPlayer2().isAllSunk())
					    					{
					    						gameInfo.setGameOver(true);
					    						callback.accept("Client 1 has won!");
					    					}
					    					
					    					gameInfo.setFiringPoint(newGameInfo.getFiringPoint());
					    					
					    					gameInfo.setGotHit(true);
					    					updatePlayerTwo(gameInfo);
					    					gameInfo.setGotHit(false);
					    					
					    					gameInfo.setDidHit(true);
					    					updatePlayerOne(gameInfo);
					    					gameInfo.setDidHit(false);
					    					

					    					
					    				}
					    				else
					    				{
					    					callback.accept("Client 1 missed Client 2's ship!");
					    					gameInfo.setDidHit(false);
					    					updatePlayerOne(gameInfo);
					    				}
					    				
					    			}
					    			else // end turn sent from player 1
					    			{
						    			gameInfo.setPlayer1(newGameInfo.getPlayer1());
						    			gameInfo.setHasFired(false);
						    			updatePlayerTwo(gameInfo);
					    			}
					    		}
					    		else // Player 2
					    		{
					    			if(newGameInfo.isFiring()) // receiving fire
					    			{
					    				gameInfo.setHasFired(true);

					    				if(HitDetection.fireAtEnemy(gameInfo.getPlayer1(), newGameInfo.getFiringPoint(), newGameInfo.getFiringWeapon()))
					    				{
					    					// hit on p1
				    						callback.accept("Client 2 hit Client 1's ship!");

				    						// Check for game ending
					    					if(gameInfo.getPlayer1().isAllSunk())
					    					{
					    						gameInfo.setGameOver(true);
					    						callback.accept("Client 2 has won!");
					    					}
					    					
					    					gameInfo.setFiringPoint(newGameInfo.getFiringPoint());
					    					
					    					gameInfo.setGotHit(true);
					    					updatePlayerOne(gameInfo);
					    					gameInfo.setGotHit(false);
					    					
					    					gameInfo.setDidHit(true);
					    					updatePlayerTwo(gameInfo);
					    					gameInfo.setDidHit(false);
					    				}
					    				else
					    				{
					    					callback.accept("Client 2 missed Client 1's ship!");
					    					gameInfo.setDidHit(false);
					    					updatePlayerTwo(gameInfo);
					    				}
					    				
					    			}
					    			else // end turn sent from player 2
					    			{
						    			gameInfo.setPlayer2(newGameInfo.getPlayer2());
						    			gameInfo.setHasFired(false);
						    			updatePlayerOne(gameInfo);
					    			}
					    		}
					    	}
					    	}
					    catch(Exception e) {
					    	callback.accept("Exception within client: " + count + " thread. Closing thread.");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run
			
			public void printVec(Vector<Point> vec) {
				for(int i = 0; i < vec.size(); i++) {
					Point p1 = vec.get(i);
					String s = "(" + p1.x + ", " + p1.y + ")";
					System.out.print(s + " ");
				}
				System.out.println();
			}
			
		}//end of client thread
}