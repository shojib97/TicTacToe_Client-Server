package bcu.cmp5308.gameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class implements the UsernameCommand 
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

public class UsernameCommand implements Command {
	private PrintWriter out;
	private BufferedReader in;
	private GameConnection serverConn;
	GameServer server;

	String username;

	public void execute() throws IOException
	{	
		server = serverConn.returnServer();

		try{
			username = in.readLine();
		}catch(IOException e){
			System.err.println(e);
		}
		
		while (!validUsername(username) || !server.usernameIsAvailable(username)){
			if(!validUsername(username)) {
				serverConn.send(GameProtocol.ERROR + " >> " + "ERROR Username is invalid.");
			} else if(!server.usernameIsAvailable(username)) {
				serverConn.send(GameProtocol.ERROR + " >> " + "ERROR Username in use.");
			} 
			try{
				username = in.readLine();
			}catch(IOException e){
				System.err.println(e);
			}
		}
		
		System.out.println("Connection " + serverConn + " chose username " + username + ".");
		try{
			serverConn.setUsername(username);
			server.registerUsername(serverConn);
		}catch(GameProtocolException e){
			System.err.println(e);


		}
	}

	public UsernameCommand(BufferedReader in, PrintWriter out, 
			GameConnection serverConn)
	{
		this.out = out;
		this.in = in;
		this.serverConn = serverConn;

	}

	public static boolean validUsername(String username) {
		return username.matches("[a-zA-Z]{3,20}");
	}
}
