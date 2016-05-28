
public class TicTacToeGame implements Boardgame {
	private String currentMessage = "Tic Tac Toe";
	private String[][] status = new String[3][3];  // spelplanen
	PlayerHandler currentPlayer;
	private boolean winner = false;
	private int drag = 1;

	public void setPlayer(PlayerHandler player){
		currentPlayer=player;
	}
	@Override
	public boolean move(int i, int j, PlayerHandler player) {
		String markPlayer = currentPlayer.getMark();
		if(player==currentPlayer){
			while(!winner==true){
				if(drag>8){ //Om drag är över 8 och ingen har vunnit så får spelarna byta plats på en ruta
					if(!status[i][j].equals(markPlayer)){
						status[i][j]=markPlayer;
						currentMessage="Fullplan"; //Ska skrivas ut innan
						currentPlayer = currentPlayer.opponent;
						
						if(checkHorizontal()==true||checkVertical()==true||checkAcross()==true){
							currentMessage="Vinst "+ currentPlayer.opponent.getMark();
							//currentMessage="Spelare "+status[i][j]+" har vunnit!"; //Skicka ut till alla, när ska jag kolla vinst?
							winner=true;
						}
						currentPlayer.otherPlayerMoved(i,j);
						drag++;
						return true;

					}else{
						currentMessage="Meddelande Välj inte din egna ruta!";
						return false;
					}

				}
				else if(status[i][j].equals(" ")){
					status[i][j]=markPlayer;
					currentMessage="Ok!";
					currentPlayer = currentPlayer.opponent;
					
					if(drag>4){//Om drag är större än fyra så kan en av spelarna ha vunnit, detta kollas nedanför
						if(checkHorizontal()==true||checkVertical()==true||checkAcross()==true){
							//currentMessage="Spelare "+status[i][j]+" har vunnit!";
							currentMessage="Vinst "+currentPlayer.opponent.getMark();
							winner=true;
						}
					}currentPlayer.otherPlayerMoved(i,j);
					drag++;
					return true;
				}

				else{
					currentMessage="Meddelande Välj en tom ruta!";
					return false;
				}


			}

			currentMessage="Spelet är över.";
			return false;


		}else{
			currentMessage="Meddelande Vänta på din tur!";
			return false;
		}

	}

	@Override
	public String getStatus(int i, int j) { //Ska skriva ut planen
		return status[i][j];
	}

	@Override
	public String getMessage() {
		return currentMessage;
	} 



	private boolean checkHorizontal(){ //kollar horisontellt om det finns en vinnare, går igenom alla rader
		for (int i = 0; i < 3; i++) {
			if (checkWinner(status[i][0], status[i][1], status[i][2]) == true) {
				return true;
			}
		}
		return false;
	}


	private boolean checkVertical(){ //kollar vertikalt om det finns en vinnare, går igenom alla kolumner
		for (int i = 0; i < 3; i++) {
			if (checkWinner(status[0][i], status[1][i], status[2][i]) == true) {
				return true;
			}
		}
		return false;
	}
	private boolean checkAcross(){ //kollar om det finns någon vinnare diagonalt
		if ((checkWinner(status[0][0], status[1][1], status[2][2]) == true) || (checkWinner(status[0][2], status[1][1], status[2][0]) == true )) {
			return true;
		}
		return false;
	}
	private boolean checkWinner(String s1, String s2, String s3) { //kollar om tre strängar är samma, för att hitta en vinnare
		if((!s1.equals(" ")) && (s1.equals(s2)) && (s2.equals(s3))){
			return true;
		}
		return false;
	}

	public TicTacToeGame(){ //Statusen för brickorna i spelbrädet sätts
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				status[i][j] = " ";
			}

		}
	}

}

