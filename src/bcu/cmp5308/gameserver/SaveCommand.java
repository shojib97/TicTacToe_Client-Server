package bcu.cmp5308.gameserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveCommand implements Command {
	private GameConnection serverConn;
	private BufferedReader in;
	GameServer server;
	BufferedWriter writer = null;
	GameConnection opponent;

	@Override
	public void execute() throws IOException {
		try {
			SaveGame();
		} catch (IOException e) {
			System.err.println(e);
		}
		serverConn.send(GameProtocol.OK + " >> " + "GAME SAVED");
		this.opponent = serverConn.getOpponent();

		if (opponent != null)

			opponent.send(GameProtocol.OK + " >>" + "%S " + "Has saved the game", opponent.getUsername());

	}

	private void SaveGame() throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("game.txt")));
		WriteLine(out, "A1");
		WriteLine(out, "A2");
		WriteLine(out, "A3");
		WriteLine(out, "B1");
		WriteLine(out, "B2");
		WriteLine(out, "B3");
		WriteLine(out, "C1");
		WriteLine(out, "C2");
		WriteLine(out, "C3");
		out.close();

	}

	private void WriteLine(PrintWriter out, String square) {
		int value = serverConn.board.getSquare(square);
		if (value != 0)
			out.println(square + "=" + value);
	}

	public SaveCommand(BufferedReader in, PrintWriter out, GameConnection serverConn) {
		this.in = in;
		this.serverConn = serverConn;
		this.server = serverConn.returnServer();
	}

}
