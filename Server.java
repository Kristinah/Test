import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	public static void main(String[] args) throws Exception{
		ServerSocket listener = new ServerSocket(9090);
		System.out.println("Tic Tac Toe Server is Running");
		try {
			while(true){
				Boardgame game = new TicTacToeGame();
				PlayerHandler playerX = new PlayerHandler(listener.accept(), "X",game);
				PlayerHandler playerO = new PlayerHandler(listener.accept(), "O",game);
				playerX.setOpponent(playerO);
				playerO.setOpponent(playerX);
				game.setPlayer(playerX);
				playerX.start();
				playerO.start();
			}
			
				
				
				
			
		}
		finally { //Kolla upp
			listener.close();
		}


	}
}



