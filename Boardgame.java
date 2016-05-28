
public interface Boardgame {
   public boolean move(int i, int j, PlayerHandler player); //ger true om draget gick bra, annars false 
   public String getStatus(int i, int j);      
   public String getMessage();

public void setPlayer(PlayerHandler playerX);
   
 
}
