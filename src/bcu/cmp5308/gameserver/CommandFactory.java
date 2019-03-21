package bcu.cmp5308.gameserver;

/*
 * Factory to read the command identifier and return a Command class
 * that can be used to process the rest of the command
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;


/**
 * Factory to read the command identifier and return a Command class
 * that can be used to process the rest of the command
 */
public class CommandFactory 
{
	/**
	 * Read the command identifier and return a Command class
	 * @return Command The class that will process this particular command
	 */
	public static bcu.cmp5308.gameserver.Command getCommand(BufferedReader in, 
			PrintWriter out, 
			GameConnection serverConn) 
					throws IOException
	{
		String cmdString = null;
		try 
		{
			// Read the command identifier (HELLO, USERNAME, JOIN, TEST, MOVE, CHAT or CLOSE)
			cmdString = in.readLine();
			// convert the string to an integer
			//int command = Integer.parseInt(cmdString);
			String command = cmdString;
			// print out some logging information
			serverConn.userMsg("Read command " + command);
			// Now decide which command identifier we have just read...
			switch (command) 
			{
			case GameProtocol.HELLO: 
				return new StartCommand(in, out, serverConn); 

			case GameProtocol.USERNAME: 
				return new UsernameCommand(in, out, serverConn); 

			case GameProtocol.JOIN: 
				return new JoinCommand(in, out, serverConn); 

			case GameProtocol.TEST: 
				return new TestCommand(in, out, serverConn); 

			case GameProtocol.MOVE: 
				return new MoveCommand(in, out, serverConn); 

			case GameProtocol.CHAT: 
				return new ChatCommand(in, out, serverConn); 
				
			case GameProtocol.CLOSE: 
				return new CloseCommand(in, out, serverConn); 
				/*
				 * Add more case statements below this comment to process 
				 * the other commands
				 */
			case GameProtocol.MARK:
				return new MarkCommand(in,out,serverConn);
				
			case GameProtocol.SAVE:
				return new SaveCommand(in,out,serverConn);
				
			case GameProtocol.LOAD:
				return new LoadCommand(in,out,serverConn);

				/*
				 * Don't add anything below this line
				 */
			default: 
				// If the command is not listed above, we don't have such a command
				return new ErrorCommand(in, out, serverConn, 
						"No such command: " + command); 
			}
		} catch (NumberFormatException e) 
		{
			// The string sent as command identifier wasn't an integer!
			return new ErrorCommand(in, out, serverConn, 
					"Incorrect Command Identifier: " + 
							cmdString );
		}
	}
}
