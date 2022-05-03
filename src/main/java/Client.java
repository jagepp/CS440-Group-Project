import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;


public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	GameInfo gameInfo; // No need to initialize because the Client will receive a gameInfo from Server upon connection
	
	private Consumer<Serializable> callback;
	
	Client(Consumer<Serializable> call){
	
		callback = call;
	}
	
	public void run() {
		
		try {
		socketClient= new Socket("127.0.0.1",5555);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
		while(true) {
			 
			try {
			gameInfo = (GameInfo) in.readObject();
			if(gameInfo.isPlaceShipPhase())
				callback.accept("Ship picking phase: Please place ships and end turn"); // Go to GUI
			else if(gameInfo.gotHit()) // Being informed of being hit by enemy
			{
				Point p1 = gameInfo.getFiringPoint();
				String s = "(" + p1.x + ", " + p1.y + ")";
				callback.accept("Ship at " + s + " was hit!"); // Go to GUI
			}
			else if(gameInfo.hasFired()) // Being informed of result of our fire
			{
				Point p1 = gameInfo.getFiringPoint();
				String s = "(" + p1.x + ", " + p1.y + ")";
				if(gameInfo.didHit())
					callback.accept("Hit enemy ship at " + s + "!"); // Go to GUI
				else
					callback.accept("Did not hit enemy ship at " + s + "!"); // Go to GUI
			}
			else // New turn starting
				callback.accept("Your turn"); // Go to GUI
			}
			catch(Exception e) {}
		}
	
    }
	
	public void send() { // gameInfo is modified directly from GUI via clientConnection.gameInfo
		
		try {
			out.writeObject(gameInfo);
			out.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
