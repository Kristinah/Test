import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;


class TicTacToeClient extends JFrame implements ActionListener {

	private Boardgame game;
	private int size;
	private Square[][] board;        // Square är subklass till JButton
	private JLabel mess = new JLabel();
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	int locationHorizontal;
	int locationVertical;
	private String mark;
	private String opponentmark;

	public TicTacToeClient (int n) throws UnknownHostException, IOException{ 
		socket = new Socket("localhost", 9090);
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);


		size=n;
		mess.setText("");
		board = new Square[n][n]; 
		JLabel welcome = new JLabel("Välkommen till Tic Tac Toe! Spela genom att klicka på rutorna. Om spelbrädet är fullt får du flytta på en av dina markörer");

		setSize(500, 500);
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(welcome,BorderLayout.NORTH);
		container.add(mess,BorderLayout.SOUTH);
		JPanel gamePanel = createBoard();	
		container.add(gamePanel, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Tic Tac Toe");
		setVisible(true);	
	}

	public JPanel createBoard(){ //Spelbräde fylls med knappar
		JPanel gamePanel = new JPanel();
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++){
				String status = " ";
				Square mySquare = new Square(status);
				mySquare.setActionCommand((Integer.toString(i))+(Integer.toString(j))); //Bättre sätt att hämta i och j?
				mySquare.addActionListener(this);
				board[i][j]=mySquare;
				gamePanel.setLayout(new GridLayout(size,size));
				gamePanel.add(board[i][j]);
			}

		}
		return gamePanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) { //gör om, skicka info till servern som ändrar!!

		String squarePlacement = e.getActionCommand(); //Knappens placering på spelbrädet hämtas
		String[] str_array = squarePlacement.split("");
		locationHorizontal =Integer.parseInt(str_array[0]);
		locationVertical = Integer.parseInt(str_array[1]);
		out.println("MOVE " + locationHorizontal +" " + locationVertical);
		//game.move(locationHorizontal,locationHorizontal, game.getPlayer());
		//out.println(game.getMessage());



		//mess.setText(game.getMessage());

	}

	public void play() {
		String response;
		try {
			response = in.readLine();
			if (response.startsWith("Start ")) {
				mark = response.substring(6);
				if(mark.equals("X")){
					opponentmark="O";
				}else{
					opponentmark="X";
				}
				setTitle("Tic Tac Toe: Du är spelare " + mark);
			}
			while (true) {
				response = in.readLine();
				if (response.startsWith("Ok!")) {
					mess.setText("Godkänt drag");
					Square mySquare = board[locationHorizontal][locationVertical];
					mySquare.setText(mark); //Knappens status uppdateras

				} else if (response.startsWith("OPPONENT_MOVED")) {
					String placement = response.substring(15);
					String [] placements = placement.split(" ");
					int i = Integer.parseInt(placements[0]);
					int j = Integer.parseInt(placements[1]);
					
					Square mySquare =  board[i][j];
					mySquare.setText(opponentmark);

					mess.setText("Din tur");
				} else if (response.startsWith("Vinst ")) {
					String respmark = response.substring(6);
					System.out.println(respmark);
					if(respmark.equals(mark)){
						mess.setText("Du vann!");
						break;
					}else{
						mess.setText("Du förlorade!");
						break;
					}
					
					//break;
				} else if (response.startsWith("Fullplan")) {
					mess.setText("Du får nu byta plats på en ruta");
				} else if (response.startsWith("Meddelande")) {
					mess.setText(response.substring(11));
				}else if(response.startsWith("Spelet är över.")){
					mess.setText(response);
					break;
				}
			}
			out.println("GAME OVER");
			socket.close();
		}catch(IOException e){
			System.err.println(e);
		}
	}


	public static void main(String[] args) throws Exception {
			int size=3;
			TicTacToeClient TicTacToeclient = new TicTacToeClient (size);
			TicTacToeclient.play();
		
	}
}
