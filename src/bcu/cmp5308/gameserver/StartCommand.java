package bcu.cmp5308.gameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

//import msgServer.MsgSvrConnection;

/**
 * This class implements the Start Command 
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

public class StartCommand implements Command {
	//private PrintWriter out;


	public void execute() throws IOException
	{
	 //output to send at the start of game
	}

	public StartCommand(BufferedReader in, PrintWriter out, 
			GameConnection serverConn)
	{
		//this.out = out;

	}
}
