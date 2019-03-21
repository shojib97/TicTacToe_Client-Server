package bcu.cmp5308.gameserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * This class implements the Chat Command 
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

public class ChatCommand implements Command{
	//private PrintWriter out;
	private BufferedReader in;
	private GameConnection serverConn;
	GameServer server;

	GameConnection opponent;

	public void execute() throws IOException
	{	
		String chatMsg="";
		
		try{
			chatMsg = in.readLine();
		}catch(IOException e){
			System.err.println(e);
		}
		this.opponent = serverConn.getOpponent();
		if (opponent != null)
			opponent.send(GameProtocol.OK + " >> " + "CHAT %s %s", serverConn.getUsername(), chatMsg);
	}

	public ChatCommand(BufferedReader in, PrintWriter out, 
			GameConnection serverConn)
	{
		//this.out = out;
		this.in = in;
		this.serverConn = serverConn;

	}
}
