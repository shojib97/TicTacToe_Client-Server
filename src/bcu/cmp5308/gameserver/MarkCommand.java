package bcu.cmp5308.gameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MarkCommand implements Command {
	private PrintWriter out;
	private BufferedReader in;
	private GameConnection serverConn;
	GameServer server;

	String mark;

	public void execute() throws IOException {
		server = serverConn.returnServer();

		try {
			mark = in.readLine();
		} catch (IOException e) {
			System.err.println(e);
		}

		while (!validMark(mark) || !server.markIsAvailable(mark)) {
			if (!validMark(mark)) {
				serverConn.send(GameProtocol.ERROR + " >> " + "ERROR Mark is invalid. Choose either X or O");
			} else if (!server.markIsAvailable(mark)) {
				serverConn.send(GameProtocol.ERROR + " >> " + "ERROR Mark in use.");
			}
			try {
				mark = in.readLine();
			} catch (IOException e) {
				System.err.println(e);
			}
		}

		System.out.println("Connection " + serverConn + " chose mark " + mark + ".");
		try {
			serverConn.set_Mark(mark);
			server.registerMark(serverConn);
		} catch (GameProtocolException e) {
			System.err.println(e);

		}
	}

	public MarkCommand(BufferedReader in, PrintWriter out, GameConnection serverConn)
	{
		this.out = out;
		this.in = in;
		this.serverConn = serverConn;

	}

	public static boolean validMark(String mark) {
		return mark.matches("X|O");
	}

}
