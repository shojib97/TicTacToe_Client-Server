package bcu.cmp5308.gameserver;;

/**
 * This class implements the Move Command 
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

public class GameProtocol 
{

	/* ---------------------------------------- */
	/* -------------- Commands ---------------- */
	/* ---------------------------------------- */

	/**
	 * client send HELLO
	 */
	public static final String HELLO = "HELLO";
	/**
	 * Client sends USERNAME command 
	 * followed by a username String 
	 */
	public static final String USERNAME = "USERNAME";
	public static final String MARK = "MARK";
	/**
	 * client requests to JOIN a session
	 * channel name should be unique to your group
	 */
	public static final String JOIN = "JOIN";
	/**
	 * Performs a test of the protocol
	 */
	public static final String TEST = "TEST";
	/**
	 * Allows a player to make a move
	 */
	public static final String MOVE = "MOVE";

	/**
	 * Enables chatting
	 */
	public static final String CHAT= "CHAT";
	/**
	 * Close Connection
	 */
	public static final String CLOSE= "CLOSE";
	/**
	 * allow game to be saved
	 */
	public static final String SAVE = "SAVE";
	/**
	 * allow game to be loaded
	 */
	public static final String LOAD = "LOAD";


	/* ---------------------------------------- */
	/* -------------- Responses --------------- */
	/* ---------------------------------------- */

	/**
	 * Server reponds by sending one or more messages
	 * The format of the message will depend on the command sent
	 */


	// Request succeeded
	public static final int OK = 200;

	//Connection closed by opponent
	public static final int OPPONENT_CLOSED = 400;

	// The server sends an error message
	public static final int ERROR = 500;
}
