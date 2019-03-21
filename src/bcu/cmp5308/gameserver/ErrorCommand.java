package bcu.cmp5308.gameserver;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

/*
 * Command class to be used when there is some kind of error
 * This class implements the Error Command 
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */


public class ErrorCommand implements Command 
{
	private PrintWriter out;

	private String message;

	/**
	 * Construct a new ErrorCommand object.  Errors are also commands so that
	 * they can be executed just like any other command.  An ErrorCommand will
	 * send a 500 response to the client.
	 * @param BufferedReader in A reader connected to the InputStream
	 * @param BufferedWriter out A writer connected to the OutputStream
	 * @param MsgSvrConnection serverConn The class modelling the connection
	 * @param String message The message describing the error
	 */
	public ErrorCommand(BufferedReader in, PrintWriter out, 
			GameConnection serverConn, String message)
	{
		this.out = out;
		this.message = message;
	}

	/**
	 * Execute the command, which will send a 500 response and an error message
	 */
	public void execute() throws IOException
	{
		try 
		{
			// send the response ID
			out.write(GameProtocol.ERROR + " >> "); 
			// Send the message
			out.write(message + "\r\n"); 
			// flush the output
			out.flush();
		} catch (Exception e) { throw new IOException(e.getMessage());}
	}
}
