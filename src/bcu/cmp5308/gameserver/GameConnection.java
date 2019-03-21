
package bcu.cmp5308.gameserver;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class implements the Game Connection Thread A new game connection object
 * is created for each new user connection
 * 
 * @author Andrew Kay & Abdel-Rahman Tawil (c) Birmingham City University, March
 *         2018 You may modify this code and use it in your own programs
 */

public class GameConnection implements Runnable {
	private boolean verbose;
	String selectedCommand;

	Command command = null;

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	boolean gameIsWon = false;

	private GameServer server;
	private String username, channelName, mark, choice, mode;
	String computerMove = "";
	int computerMark;
	private GameConnection opponent;
	static Scanner sc = new Scanner(System.in);
	TicTacToeBoard board = new TicTacToeBoard();

	public GameConnection(GameServer server, Socket socket) throws IOException {
		this.server = server;
		this.socket = socket;

		InputStreamReader isr = new InputStreamReader(socket.getInputStream());
		this.in = new BufferedReader(isr);

		OutputStream os = socket.getOutputStream();
		this.out = new PrintWriter(os);

		// We want debugging messages
		verbose = true;
	}

	public String getUsername() {
		return username;
	}

	public String get_Mark() {
		return mark;
	}

	public void set_Mark(String mark) {
		this.mark = mark;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChannelName() {
		return channelName;
	}

	@Override
	public String toString() {
		if (username == null) {
			return String.format("@%x", hashCode());
		} else if (channelName == null) {
			return username;
		} else {
			return username + channelName;
		}
	}

	void send(String msg, Object... args) {
		if (!isClosed()) {
			if (args.length > 0) {
				msg = String.format(msg, args);
			}

			System.out.println(this + " >> " + msg);
			out.println(msg);
			out.flush();
		}
	}

	String receive() throws IOException {
		if (isClosed()) {
			return null;
		} else {
			String line = in.readLine();
			if (line == null) {
				throw new IOException("Received null from connection " + this + ".");
			}

			System.out.println(this + " << " + line);
			return line;
		}
	}

	public void close() {
		server.deregisterConnection(this);
		if (!isClosed()) {
			try {
				send(GameProtocol.OK + " >> " + "GOODBYE %s", username);
				socket.close();
			} catch (IOException e2) {
				System.err.println("While closing, connection " + this + " caused " + e2);
			}
		}
	}

	public boolean isClosed() {
		return socket.isClosed();
	}

	void setOpponent(GameConnection opponent) throws GameProtocolException {
		if (this.opponent != null) {
			throw new GameProtocolException("Connection " + this + " already has opponent.");
		}

		this.opponent = opponent;
	}

	@Override
	public void run() {

		try {
			System.out.println("Connection " + this + " opened.");
			send(GameProtocol.OK + " >> " + "HI");

			// get the appropriate Command class from the CommandFactory
			command = CommandFactory.getCommand(in, out, this);

			// Print logging information
			userMsg("CommandFactory returned a " + getCommandString(command));

			selectedCommand = getCommandString(command);
			while (!getCommand(selectedCommand).equals("StartCommand")) {
				send(GameProtocol.ERROR + " >> " + "ERROR Client must issue HELLO command.");
				command = CommandFactory.getCommand(in, out, this);
				selectedCommand = getCommandString(command);
			}

			// Execute the Command
			command.execute();

			send(GameProtocol.OK + " >> " + "CHOOSE_USERNAME");

			// Username Command
			command = CommandFactory.getCommand(in, out, this);

			userMsg("CommandFactory returned a " + getCommandString(command));

			selectedCommand = getCommandString(command);
			while (!getCommand(selectedCommand).equals("UsernameCommand")) {
				send(GameProtocol.ERROR + " >> " + "ERROR Client must issue USERNAME command.");
				command = CommandFactory.getCommand(in, out, this);
				selectedCommand = getCommandString(command);
			}

			// Execute the Command
			command.execute();

			send(GameProtocol.OK + " >> " + "WELCOME %s please choose your mark", username);
			command = CommandFactory.getCommand(in, out, this);

			userMsg("CommandFactory returned a " + getCommandString(command));
			selectedCommand = getCommandString(command);
			while (!getCommand(selectedCommand).equals("MarkCommand")) {
				send(GameProtocol.ERROR + " >> " + "ERROR Client must issue Mark command.");
				command = CommandFactory.getCommand(in, out, this);
				selectedCommand = getCommandString(command);
			}
			command.execute();

			send(GameProtocol.OK + " >>" + "Great you chose %s", mark);
			send(GameProtocol.OK + " >>" + "Would you like to play against a computer player");
			choice = in.readLine();
			while (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n")) {
				send("please enter either y or n");
				choice = in.readLine();
			}
			if (choice.equalsIgnoreCase("y")) {
				send(GameProtocol.OK + " >>" + "Please choose the difficulty mode");
				mode = in.readLine();
				while (!mode.equalsIgnoreCase("e") && !mode.equalsIgnoreCase("m") && !mode.equalsIgnoreCase("h")) {
					send("please enter 'e' for easy, 'm' for medium or 'h' for hard");
					mode = in.readLine();
				}
			}

			// JOIN or TEST Command
			command = CommandFactory.getCommand(in, out, this);

			userMsg("CommandFactory returned a " + getCommandString(command));

			// wait for JOIN or TEST command
			selectedCommand = getCommandString(command);

			while (!getCommand(selectedCommand).equals("JoinCommand")
					&& !getCommand(selectedCommand).equals("TestCommand")) {
				send(GameProtocol.ERROR + " >> " + "ERROR Client must issue JOIN or TEST command.");
				command = CommandFactory.getCommand(in, out, this);
				selectedCommand = getCommandString(command);

			}

			// Execute the Command

			command.execute();

		} catch (Exception e) {
			if (!isClosed()) {
				System.err.println("Connection " + this + " already closed, caused " + e);
			} else {
				System.err.println("Closing connection " + this + " due to " + e);
				send(GameProtocol.ERROR + " >> " + "ERROR %s", e);
			}
		} finally {
			if (opponent != null && !opponent.isClosed()) {
				opponent.send(GameProtocol.OK + " >> " + "OPPONENT_CLOSED %s", username);
			}
			close();
			System.out.println("Connection " + this + " closed.");
		}
	}

	public void echoLoop() throws IOException {
		while (!isClosed()) {
			// Move, CHAT, SAVE, LOAD or CLOSE Command
			command = CommandFactory.getCommand(in, out, this);

			userMsg("CommandFactory returned a " + getCommandString(command));

			// wait for Move, CHAT or CLOSE Command
			selectedCommand = getCommandString(command);

			while (!getCommand(selectedCommand).equals("MoveCommand")
					&& !getCommand(selectedCommand).equals("ChatCommand")
					&& !getCommand(selectedCommand).equals("SaveCommand")
					&& !getCommand(selectedCommand).equals("LoadCommand")
					&& !getCommand(selectedCommand).equals("CloseCommand")) {

				send(GameProtocol.ERROR + " >> " + "ERROR Client must issue MOVE, CHAT or a CLOSE command.");
				command = CommandFactory.getCommand(in, out, this);
				selectedCommand = getCommandString(command);

			}

			command.execute();
			humanplayerboard();

			if (choice.equalsIgnoreCase("y") && mode.equalsIgnoreCase("e")) {
				computerplayereasy();
			} else if (choice.equalsIgnoreCase("y") && mode.equalsIgnoreCase("m")) {
				computerplayermedium();
			} else if (choice.equalsIgnoreCase("y") && mode.equalsIgnoreCase("h")) {
				computerplayerhard();
			}

			if (getCommand(selectedCommand).equals("CloseCommand"))
				return;

		}
	}

	public GameServer returnServer() {
		return server;
	}

	public void humanplayerboard() { // update board for human players
		send("\n%s", board.gameBoard());
		send("\r\n");
		if (board.isGameWon() == true) {
			send("Player %s won the game\n", username.toString());
			close();
		} else if (board.checkdraw() == true) {
			send("Game is drawn");
			close();
		}

	}

	public void computerplayereasy() { // update board for computer player
		if (mark.equals("X")) {
			computerMark = 2;
		} else {
			computerMark = 1;
		}
		computerMove = board.getComputerMoveeasy();
		send("I am Thinking");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		send("I will play at " + computerMove);
		board.placeMark(computerMove, computerMark);

		send("\r\n");
		send("\n%s", board.gameBoard());
		send("\r\n");

		if (board.isGameWon() == true) {
			send("Computer Player Won");
			close();
		} else if (board.checkdraw() == true) {
			send("Game is drawn");
			close();
		}
	}

	public void computerplayermedium() { // update board for computer player
		if (mark.equals("X")) {
			computerMark = 2;
		} else {
			computerMark = 1;
		}
		computerMove = board.getComputerMoveMedium();
		send("I am Thinking");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		send("Alright, I will play at " + computerMove);
		board.placeMark(computerMove, computerMark);

		send("\r\n");
		send("\n%s", board.gameBoard());
		send("\r\n");

		if (board.isGameWon() == true) {
			send("Computer Player won the game");
			close();
		} else if (board.checkdraw() == true) {
			send("Game is drawn");
			close();
		}

	}

	public void computerplayerhard() { // update board for computer player
		if (mark.equals("X")) {
			computerMark = 2;
		} else {
			computerMark = 1;
		}
		computerMove = board.getComputerMoveHard();
		send("I am Thinking");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		send("Alright, I will play at " + computerMove);
		board.placeMark(computerMove, computerMark);

		send("\r\n");
		send("\n%s", board.gameBoard());
		send("\r\n");

		if (board.isGameWon() == true) {
			send("Computer Player Won");
			close();
		} else if (board.checkdraw() == true) {
			send("Game is drawn");
			close();
		}
	}

	public void testProtocol(String gameName) {
		// TODO: implement the TEST command
		send(GameProtocol.ERROR + " >> " + "ERROR The TEST command is not implemented.");
	}

	private String getCommand(String selectedCommand) {
		return selectedCommand.split("\\.")[3];
	}

	public GameConnection getOpponent() {
		return opponent;
	}

	void userMsg(String msg) {
		if (verbose) {
			System.out.println(msg);
		}
	}

	String getCommandString(Command command) {
		return ((Object) command).getClass().getName();
	}

	void sendone(Object resumeGame) {

	}

}
