package bcu.cmp5308.gameserver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;



public class LoadCommand implements Command {
	private GameConnection serverConn;
	private BufferedReader in;
	GameServer server;
	GameConnection opponent;
	TicTacToeBoard board = new TicTacToeBoard();

	@Override
	public void execute() throws IOException {
		loadGame();
		
		serverConn.send(GameProtocol.OK + " >> "+ "GAME LOADED");
		this.opponent = serverConn.getOpponent();

		if (opponent != null)

			opponent.send(GameProtocol.OK + " >> "+ "GAME LOADED");
			opponent.send(serverConn.board.gameBoard());

	}
	private void loadGame()
	{
		board.reset();
		BufferedReader in = getFile();
		try
		{
			String line = in.readLine();
			while (line != null)
			{
				String square = line.substring(0,2);
				String player = line.substring(3,4);
				if (player.equals("1"))
					board.placeMark(square, 1);
				else if (player.equals("2"))
					board.placeMark(square, 2);
				line = in.readLine();
			}
			in.close();
		}
		catch (IOException ex)
		{
			System.err.println(ex);
		}

	
	}
	
	private BufferedReader getFile()
	{
		BufferedReader in = null;
		try
		{
			in = new BufferedReader(new FileReader("game.txt"));
		}
		catch (FileNotFoundException ex)
		{
			System.err.println(ex);
		}
		return in;
	}
	
	public LoadCommand(BufferedReader in, PrintWriter out, GameConnection serverConn) {
		this.in = in;
		this.serverConn = serverConn;
		this.server = serverConn.returnServer();
	}

}
