import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	public static void main (String [ ] args){
		try {
			Socket socket=new Socket("localhost",9090);
			BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter ut=new PrintWriter(socket.getOutputStream());

			BufferedReader userReader =new BufferedReader(new InputStreamReader(System.in));

			String userInput;
			while (!(userInput = userReader.readLine()).equals("") ) {
				//System.out.println("user:"+userInput);
				ut.println(userInput);
				ut.flush();
				System.out.println(in.readLine());
				//System.out.println("echo: " + in.readLine());
			}
			socket.close();
			//ut.println("Charlotta"); 
			//ut.flush();
			//System.out.println(in.readLine());
		}catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error");
		}
	}

}
