package bcu.cmp5308.gameserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * This class implements the Close Command 
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

public class CloseCommand implements Command {

	private GameConnection serverConn;
	GameServer server;

	String channelName;

	public void execute() throws IOException
	{	
		serverConn.close();

	}

	public CloseCommand(BufferedReader in, PrintWriter out, 
			GameConnection serverConn)
	{
		this.serverConn = serverConn;

	}

}
