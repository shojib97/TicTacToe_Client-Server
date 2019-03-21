package bcu.cmp5308.gameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class implements the Move Command
 * 
 * @author Abdel-Rahman Tawil (c) Birmingham City University, March 2018 You may
 *         modify this code and use it in your own programs
 */

public class MoveCommand implements Command {
	// private PrintWriter out;
	private BufferedReader in;
	private GameConnection serverConn;
	static TicTacToeBoard board = new TicTacToeBoard();
	GameServer server;
	String humanMove = "";
	String computerMove = "";
	boolean gameIsWon = false;

	GameConnection opponent;

	public void execute() throws IOException {
		String move = "";
		int player = 0;
		if (serverConn.get_Mark().equalsIgnoreCase("x")) {
			player = 1;
		} else {
			player = 2;
		}

		try {

			humanMove = getMove(move);
			board.placeMark(humanMove, player);

		} catch (IOException e) {
			System.err.println(e);
		}

		this.opponent = serverConn.getOpponent();

		if (opponent != null)

			opponent.send(GameProtocol.OK + " >> " + "MOVE %s %s", serverConn.getUsername(), humanMove);
		opponent.send("\n%s", board.gameBoard());
		opponent.send("\r\n");

	}

	public String getMove(String move) throws IOException {
		String play;
		System.out.print(move);
		do {
			play = in.readLine();
			if (!board.isMoveValid(play)) {
				serverConn.send("Invalid move! Please try again.");
			}
		} while (!board.isMoveValid(play));
		return play;
	}

	public MoveCommand(BufferedReader in, PrintWriter out, GameConnection serverConn) {
		// this.out = out;
		this.in = in;
		this.serverConn = serverConn;
		this.server = serverConn.returnServer();

	}
}
