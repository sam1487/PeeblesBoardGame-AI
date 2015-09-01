package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import org.hamcrest.core.IsInstanceOf;

import random.RandomStrategy;

import minmax.MinMaxStrategy;
import minmax.NumberOfValidMovesHeuristic;
import minmax.TotalPeeblesAtHandHeuristic;

import andor.AndOrStrategy;

import game.Player.PlayerType;
import human.HumanStrategy;

/* Represents the main UI for the game */
public class GameUi {
	enum GameMode {STEP, RUN};
	static String outputFile = "board.txt";
	static String movesFile = "moves.txt";
	static boolean interruptForLongGame = true;
	
	private static HashMap<PlayerType, HashSet<GameBoard>> seenStates = new HashMap<>();
	static {		
		seenStates.put(PlayerType.Player1, new HashSet<GameBoard>());
		seenStates.put(PlayerType.Player2, new HashSet<GameBoard>());		
	}
	
	Player player1;
	Player player2;
	GameBoard board;
	public static int ply;
	GameMode gameMode;
	int turn;
	
	Scanner scanner = new Scanner(System.in);
	public PlayerType winner;
	
	public static void main(String[] args) {
		String welcome = "Game started. Output file is " + outputFile + "\n";
		System.out.println(welcome);
		
		GameUi ui = new GameUi();
		ui.start();
	}

	/* Game Lifecycle */
	private void start() {
		cleanFiles();
		preparePlayers();
		prepareBoard();
		getGameSettings();
		runGameLoop();
	}

	/* All game states are output into files. Clean all before starting the game */ 
	private void cleanFiles() {
		try {			
			new File(outputFile).delete();
			new File(movesFile).delete();
			new File(outputFile).createNewFile();
			new File(movesFile).createNewFile();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* Input Player information */
	private void preparePlayers() {
		System.out.println("Choose first player");
		player1 = choosePlayer();
		
		System.out.println("Choose second player");
		player2 = choosePlayer();
		
		player1.setType(PlayerType.Player1);
		player2.setType(PlayerType.Player2);
	}
	
	private Player choosePlayer() {
		String msg = "\n" 
				+ "1. Human Player \n" 
				+ "2. And-OR \n"
				+ "3. MiniMax-H1 \n"
				+ "4. MiniMax-H2 \n"
				+ "5. Random \n\n"
				+ "Enter choice (1-5): ";
		
		System.out.print(msg);
		int choice = this.scanner.nextInt();
		
		Player toret = new Player();
		
		if(choice == 1){
			toret.setStrategy(new HumanStrategy(this.scanner));	
		}
		else if(choice == 2) {
			toret.setStrategy(new AndOrStrategy());
		}
		else if(choice == 3) {
			toret.setStrategy(new MinMaxStrategy(new TotalPeeblesAtHandHeuristic()));
		}
		else if(choice == 4) {
			toret.setStrategy(new MinMaxStrategy(new NumberOfValidMovesHeuristic()));
		}
		else if(choice == 5) {
			toret.setStrategy(new RandomStrategy());
		}
		else {
			System.out.println("\n! Choice must be in the range 1-4");
			return choosePlayer();
		}
				
		return toret;
	}
	
	private void prepareBoard() {
		int boardSize = chooseBoardSize();
		int defaultPeebles = chooseDefaultPeebles();
		this.board = new  GameBoard(boardSize, defaultPeebles);
	}

	private int chooseDefaultPeebles() {
		System.out.print("\n? Initial pebbles (1-99): ");
		int size = scanner.nextInt();
		if(size < 1 || size > 99) {
			System.out.println("\n! Count must be in the range 1-99");
			return chooseDefaultPeebles();
		}
		
		return size;
	}

	private int chooseBoardSize() {
		System.out.print("\n? Board size (2-10): ");
		int size = scanner.nextInt();
		if(size < 2 || size > 10) {
			System.out.println("\n! Size must be in the range 2-10");
			return chooseBoardSize();
		}
		
		return size;
		
	}
	
	private void getGameSettings() {
		this.ply = choosePly();
		this.gameMode = chooseGameMode();
	}

	private GameMode chooseGameMode() {
		System.out.print("\n? Game Mode: (S)tep / (R)un: ");
		String mode = scanner.next();		
		
		mode = mode.toLowerCase().trim();
		
		
		if(!mode.equals("s") && !mode.equals("r")) {
			System.out.println("\n! Enter either S or R");
			return chooseGameMode();
		}
		
		if(mode.equals("s")) 
			return GameMode.STEP;
		else 
			return GameMode.RUN;
	}

	private int choosePly() {
		System.out.print("\n? Ply: ");
		int size = scanner.nextInt();		
		return size;
	}

	/* The Game Loop! Game quits automatically once the game is over */
	private void runGameLoop() {
		turn = 0;
		Player player;
		
		printBoard();
		while(true) {
			if(board.isGameOver()) {
				reportGameOver();
				break;
			}
			
			Cell next;
					
			if(turn % 2 == 0)
				player = player1;
			else 
				player = player2;
			
			/* When a human player is playing, prompt the user for an input 
			 * Otherwise, this method does nothing. 
			 */
			getNextPromopt();
			
			next = getNextMoveFromPlayer(player, board);
			++turn;
			
			/* We don't to make moves that generate the same board. This is
			 * where we force into playing another move for the player if it
			 * generates an already seen board. Note that this is ignored if 
			 * a human has made the move. 
			 */
			next = redirectIfAlreadySeen(board, player, next);
			
			
			/* Print the move info into moves.txt */
			writeMove(next, player, turn);			
			makeMove(next);						
			
			/* Print the board into board.txt */
			printBoard();
			
			/* This blocks infinite loops, and gives the user a chance to quit at every 5000th turn.*/
			if(interruptForLongGame && (turn % 5000 == 0)) {
				System.out.println("There has already been " + turn + " turns. Do you want to continue? (y/n)");
				String inp = this.scanner.next();
				if(!inp.toLowerCase().equals("y")) break; 
			}
		}
	}
	
	/* Guards moves against generating repeated states. If this happens
	 * a random valid move is chosen that is guranteed to generate a new state. 
	 */
	private Cell redirectIfAlreadySeen(GameBoard board, Player player,
			Cell next) {
		if(player.strategy instanceof HumanStrategy) return next;
		
		HashSet<GameBoard> seen = seenStates.get(player.type);
		GameBoard newBoard = new GameBoard(board);
		newBoard.makeMove(next.row, next.col);
		
		if(seen.contains(newBoard)) {
			System.out.println("\n! Repeated state reached by " + player + ", redirecting... 	\n");
			ArrayList<Cell> nextMoves = new ArrayList<>();
			int row = GameBoard.getRowForPlayer(player);
			for(int col = 0; col < board.getSize(); ++col) {
				if(board.getPeeblesAt(row, col) > 0) {
					Cell possibleMove = new Cell(row, col);
					if(moveGeneratesANewBoard(board, possibleMove, seen)) {
						nextMoves.add(possibleMove);
					}
				}
			}
			
			if(!nextMoves.isEmpty()) {
				int index = new Random().nextInt(nextMoves.size());
				next = nextMoves.get(index);
			}
		}
		
		newBoard = new GameBoard(board);
		newBoard.makeMove(next.row, next.col);		
		seen.add(newBoard);
		
		return next;
	}


	private boolean moveGeneratesANewBoard(GameBoard board, Cell possibleMove, 
			HashSet<GameBoard> seen) {
		GameBoard newBoard = new GameBoard(board);
		newBoard.makeMove(possibleMove.row, possibleMove.col);
		return !seen.contains(newBoard);		
	}


	private void reportGameOver() {
		String msg = "\n\nGAME OVER!\n\n";
		msg += "Total Turns: " + this.turn + "\n\n";
		//msg += "Winner: > " + board.getWinner() + " <\n";
		PlayerType winner = board.getWinner();
		Player player;
		if(winner == PlayerType.Player1) 
			player = this.player1;
		else 
			player = this.player2;
		
		msg += "Winner: > " + player + " <\n";
		
		this.winner = board.getWinner();
		System.out.println(msg);
		
		FileWriter fw;
		try {
			fw = new FileWriter(outputFile, true);
			fw.write(msg);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private void printTurnInfo(Player player, Cell next) {
		System.out.println(String.format("%s moved column %d that had %d pebbles", 
				player, next.col + 1, this.board.getPeeblesAt(next.row, next.col)));
	}


	private String generateStats() {
		String output = "";
		int peeblesForPlayer1 = 0;
		int peeblesForPlayer2 = 0;
		
		for(int col = 0; col < this.board.getSize(); ++col)
			peeblesForPlayer1 += board.getPeeblesAt(0, col) ;
		
		for(int col = 0; col < this.board.getSize(); ++col)
			peeblesForPlayer2 += board.getPeeblesAt(1, col);
		
		
		output += String.format("\n");
		output += String.format("Pebbles:\n");
		
		if(turn % 2 == 0) output += "\t> ";
		else output += "\t  ";
		output += String.format("Player 1 [%10s]: %d\n", player1.strategy, peeblesForPlayer1);
		
		if(turn % 2 != 0) output += "\t> ";
		else output += "\t  ";
		output += String.format("Player 2 [%10s]: %d\n", player2.strategy, peeblesForPlayer2);
		return output;
	}


	private void printBoard() {
		String boardtxt = GameBoardWriter.convertToString(board);
		String stats = generateStats();
		try {
			OutputStreamWriter output = new OutputStreamWriter(
					new FileOutputStream(outputFile));
			
			output.write(boardtxt);
			output.write(stats);
			output.close();
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void getNextPromopt() {
			
		if(this.gameMode == GameMode.RUN) return;
		
		System.out.println("? Step next: Enter N");
		String inp = scanner.next();
		inp = inp.toLowerCase().trim();
		if(!inp.equals("n")) {
			System.out.println("! Enter N only\n");
			getNextPromopt();
		}		
	}


	private void makeMove(Cell next) {
		this.board.makeMove(next.row, next.col);		
	}

	private Cell getNextMoveFromPlayer(Player player, GameBoard board) {
		return player.getNextMove(board);	
	}

	private void writeMove(Cell move, Player player, int turn) {
		try {
			FileWriter fw = new FileWriter(movesFile, true);
			String msg = String.format("\n%d.  %s moved column %d that had %d pebbles\n", 
					turn, player, move.col + 1, this.board.getPeeblesAt(move.row, move.col));
			
			msg += board;
			
			fw.write(msg);
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
