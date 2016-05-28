import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerHandler extends Thread {
	String mark;
	PlayerHandler opponent;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	Boardgame game;
	
	public PlayerHandler(Socket socket, String mark,Boardgame game) {
		this.socket = socket;
		this.mark = mark;
		this.game=game;
		try {
			in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("Start " + mark);
			out.println("Meddelande Väntar på motståndare");
		} catch (IOException e) {
			System.out.println("Player died: " + e);
		}
	}
	public void setOpponent(PlayerHandler opponent) {
		this.opponent = opponent;
	}
	public PlayerHandler getOpponent() {
		return opponent;
	}

	public String getMark(){
		return mark;
	}
	public void otherPlayerMoved(int i,int j) {
		//System.out.println("otherPlayerMoved i: "+i+"j: "+j);
		out.println("OPPONENT_MOVED " + i+ " "+j);
		if(game.getMessage().startsWith("Vinst ")){
			out.println(game.getMessage());
		}
	
		//out.println(hasWinner);
	}
	
	public void run(){
		try {
			System.out.println("Start");
			// The thread is only started after everyone connects.
			out.println("Meddelande Nu kan ni börja spela");

			// Tell the first player that it is her turn.
			if (mark.equals("X")) {
				 //Thread.sleep(4000); //tråden väntar lite innan den skickar nästa meddelande
				//System.out.print("your Move");
				out.println("Meddelande Din tur");
			}
			while (true) {
				String input = in.readLine();
				if (input.startsWith("MOVE")) {
					String placement = input.substring(5);
					String [] placements = placement.split(" ");
					int locationHorizontal = Integer.parseInt(placements[0]);
					int locationVertical = Integer.parseInt(placements[1]);
					boolean move = game.move(locationHorizontal,locationVertical, this);
					if(move==true){ //är draget godkänt?
						out.println("Ok!");
						out.println(game.getMessage());
					}else{
						out.println(game.getMessage());
					}
				} else if (input.startsWith("GAME OVER")) {
					return;
				}
			}
		} catch (IOException e) {
			//Skicka message till den andra spelaren, går det?
			out.println("Meddelande andra spelaren blev nedkopplad");
			System.out.println("Player died: " + e);
		} finally {
			try {socket.close();} catch (IOException e) {}
		}
	}
}
