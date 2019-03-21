package bcu.cmp5308.gameserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class implements the Join Command 
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

public class JoinCommand implements Command{

	private PrintWriter out;
	private BufferedReader in;
	private GameConnection serverConn;
	GameServer server;

	String channelName;

	public void execute() throws IOException
	{	
		server = serverConn.returnServer();

		try{
			channelName = in.readLine();
		}catch(IOException e){
			System.err.println(e);
		}

		while(!validChannelName(channelName))	{
			out.write(GameProtocol.ERROR + " >> " + "ERROR Channel name is invalid.\r\n");
			out.flush();
			try{
				channelName = in.readLine();
			}catch(IOException e){
				System.err.println(e);
			}
		}

		try{
			server.requestMatchup(serverConn, channelName);
		}catch(GameProtocolException e){
			System.err.println(e);
		}


		serverConn.echoLoop();
	}

	public JoinCommand(BufferedReader in, PrintWriter out, 
			GameConnection serverConn)
	{
		this.out = out;
		this.in = in;
		this.serverConn = serverConn;

	}

	public static boolean validChannelName(String channelName) {
		return channelName.matches("#[a-zA-Z0-9\\-_]{3,20}");
	}
}
