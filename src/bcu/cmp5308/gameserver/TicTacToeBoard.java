package bcu.cmp5308.gameserver;

import java.util.Random;

public class TicTacToeBoard {
	Random rand = new Random();
	private static int board[];

	private int vectors[][] = { 
			{ 0, 1, 2 }, // Row 1
			{ 3, 4, 5 }, // Row 2
			{ 6, 7, 8 }, // Row 3
			{ 0, 3, 6 }, // Column 1
			{ 1, 4, 7 }, // Column 2
			{ 2, 5, 8 }, // Column 3
			{ 0, 4, 8 }, // Diagonal 1
			{ 2, 4, 6 } // Diagonal 2
	};

	public TicTacToeBoard() {
		this.reset();
	}

	public void reset() {
		board = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2 };
	}

	private int getSquare(int index) {
		if (index < 0 | index > 8)
			throw new IllegalArgumentException("index must be 0-9");

		return board[index];
	}

	public int getSquare(String square) {
		int index = assignSquareToIndex(square);
		if (index == -1)
			throw new IllegalArgumentException("Invalid square");
		switch (getSquare(index)) {
		case 3:
			return 1;
		case 5:
			return 2;
		default:
			return 0;
		}
	}

	private int assignSquareToIndex(String square) {
		switch (square) {
		case "A1":
			return 0;
		case "A2":
			return 1;
		case "A3":
			return 2;
		case "B1":
			return 3;
		case "B2":
			return 4;
		case "B3":
			return 5;
		case "C1":
			return 6;
		case "C2":
			return 7;
		case "C3":
			return 8;
		default:
			return -1;
		}
	}

	private String assignIndexToSquare(int index) {
		switch (index) {
		case 0:
			return "A1";
		case 1:
			return "A2";
		case 2:
			return "A3";
		case 3:
			return "B1";
		case 4:
			return "B2";
		case 5:
			return "B3";
		case 6:
			return "C1";
		case 7:
			return "C2";
		case 8:
			return "C3";
		default:
			return "";
		}
	}

	public boolean isMoveValid(String play) {
		if (play.equalsIgnoreCase("A1") & board[0] == 2)
			return true;
		if (play.equalsIgnoreCase("A2") & board[1] == 2)
			return true;
		if (play.equalsIgnoreCase("A3") & board[2] == 2)
			return true;
		if (play.equalsIgnoreCase("B1") & board[3] == 2)
			return true;
		if (play.equalsIgnoreCase("B2") & board[4] == 2)
			return true;
		if (play.equalsIgnoreCase("B3") & board[5] == 2)
			return true;
		if (play.equalsIgnoreCase("C1") & board[6] == 2)
			return true;
		if (play.equalsIgnoreCase("C2") & board[7] == 2)
			return true;
		if (play.equalsIgnoreCase("C3") & board[8] == 2)
			return true;
		return false;
	}

	public void placeMark(String square, int player) {
		int index = assignSquareToIndex(square);

		this.getPlayer(index, player);
	}

	private void getPlayer(int index, int player) {
		if (player == 1)
			board[index] = 3;
		else
			board[index] = 5;
	}

	public  boolean isGameWon()
	{
		if (checkwin(board[0], board[1], board[2]))//win by Row 1
			return true;
		if (checkwin(board[3], board[4], board[5])) //win by Row 2
			return true;
		if (checkwin(board[6], board[7], board[8])) //win by Row 3
			return true;
		if (checkwin(board[0], board[3], board[6]))//win by column 1
			return true;
		if (checkwin(board[1], board[4], board[7])) //win by column 2
			return true;
		if (checkwin(board[2], board[5], board[8])) //win by column 3
			return true;
		if (checkwin(board[0], board[4], board[8])) //win by diagonal 1
			return true;
		if (checkwin(board[2], board[4], board[6])) //win by diagonal 2
			return true;
		return false;
	}

	public  boolean checkwin(int a, int b, int c)
	{
		return ((a == b) & (a == c) & (a != 2));
	}
	
	public boolean checkdraw() {
	int emptySquare = 0;
	for (int i = 0; i < 9; i++)
		if (board[i] == 2)
			emptySquare++;
	if (emptySquare == 0)
		return true;
	return false;
	}
	
	public String canPlayerWin(int player) {
		if (player < 1 | player > 2)
			throw new IllegalArgumentException("player must be 1 or 2");

		boolean playerCanWin = false;

		for (int v = 0; v < 8; v++) {
			int p = getVectorProduct(v);
			if ((player == 1 & p == 18) | (player == 2 & p == 50)) {
				if (board[vectors[v][0]] == 2)
					return assignIndexToSquare(vectors[v][0]);
				if (board[vectors[v][1]] == 2)
					return assignIndexToSquare(vectors[v][1]);
				if (board[vectors[v][2]] == 2)
					return assignIndexToSquare(vectors[v][2]);
			}
		}
		return "";

	}

	private int getVectorProduct(int vector) {
		return board[vectors[vector][0]] * board[vectors[vector][1]] * board[vectors[vector][2]];
	}

	public String getComputerMoveeasy() {

		for (int i = 0; i < 9; i++) {
			int value = rand.nextInt(9);
			if (board[value] == 2) {
				return assignIndexToSquare(value);
			}
		}
		return "";
	}

	public String getComputerMoveMedium() {
		// puts on centre first
		if (board[4] == 2)
			return "B2";
		
		//puts on corner
		if (board[0] == 2)
			return "A1";
		if (board[2] == 2)
			return "A3";
		if (board[6] == 2)
			return "C1";
		if (board[8] == 2)
			return "C3";
		
		//puts on edges
		if (board[1] == 2)
			return "A2";
		if (board[3] == 2)
			return "B1";
		if (board[5] == 2)
			return "B3";
		if (board[7] == 2)
			return "C2";
		return "";
	}

	public String getComputerMoveHard() {
		String bestMove;

		// Win if possible
		bestMove = this.canPlayerWin(2);
		if (bestMove != "")
			return bestMove;

		// Block if necessary
		bestMove = this.canPlayerWin(1);
		if (bestMove != "")
			return bestMove;

		if (board[4] == 2)
			return "B2";
		if (board[0] == 2)
			return "A1";
		if (board[2] == 2)
			return "A3";
		if (board[6] == 2)
			return "C1";
		if (board[8] == 2)
			return "C3";
		if (board[1] == 2)
			return "A2";
		if (board[3] == 2)
			return "B1";
		if (board[5] == 2)
			return "B3";
		if (board[7] == 2)
			return "C2";

		return ""; // The board is full
	}

	public String gameBoard() {
		return " " + getMark(board[0]) + " | " + getMark(board[1]) + " | " + getMark(board[2]) + "\r\n-----------\r\n"
				+ " " + getMark(board[3]) + " | " + getMark(board[4]) + " | " + getMark(board[5])
				+ "\r\n-----------\r\n" + " " + getMark(board[6]) + " | " + getMark(board[7]) + " | "
				+ getMark(board[8]);
	}

	private String getMark(int status) {
		if (status == 3)
			return "X";
		if (status == 5)
			return "O";
		return " ";
	}

}
