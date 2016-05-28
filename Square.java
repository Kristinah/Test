import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Square extends JButton{
	public String Text;
	
	public Square(String in){
		super.setText(in);
		super.setContentAreaFilled(false);
		setOpaque(true);
		setBackground(Color.LIGHT_GRAY);
		
	}



}
