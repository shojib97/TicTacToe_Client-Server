package bcu.cmp5308.gameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class implements the Test Command
 * command is not fullly implemented 
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

public class TestCommand implements Command {

	//private PrintWriter out;
	private BufferedReader in;
	private GameConnection serverConn;
	GameServer server;


	public void execute() throws IOException
	{	
		String test = in.readLine();
		serverConn.testProtocol(test);
	}

	public TestCommand(BufferedReader in, PrintWriter out, 
			GameConnection serverConn)
	{
		//this.out = out;
		this.in = in;
		this.serverConn = serverConn;

	}



}
