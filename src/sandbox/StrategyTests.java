package sandbox;

import java.util.HashSet;

import random.RandomStrategy;

import minmax.MinMaxStrategy;
import minmax.MinMaxStrategy.FunctionType;
import minmax.TotalPeeblesAtHandHeuristic;
import andor.AndOrStrategy;
import game.Cell;
import game.GameBoard;
import game.GameBoardWriter;
import game.Player;
import game.Player.PlayerType;
import junit.framework.TestCase;

public class StrategyTests extends TestCase{
	public void testPlanIsConstructed() throws Exception {
//		Player p1 = new Player(PlayerType.Player1);
//		Player p2 = new Player(PlayerType.Player2);
//		
//		p2.setStrategy(new AndOrStrategy());
//		p1.setStrategy(new RandomStrategy());
//		
//		GameBoard board = new GameBoard(5, 2);
//		
//		Cell move = null;
//		PlayerType winner = null;
//		
//		while(true) {
//			Cell move1 = p1.getNextMove(board);
//			
//			if(move1 != null) {
//				board.makeMove(move1.row, move1.col);
//				System.out.println(board);
//				
//			}
//			
//			if(board.isGameOver()) {
//				winner = board.getWinner();
//				break;
//			}
//			
//			System.out.println("");
//			System.out.println("  |  ");
//			System.out.println("  |  ");
//			System.out.println("  V  ");
//			System.out.println("");
//			
//			Cell move2 = p2.getNextMove(board);
//			
//			if(move2 != null) {
//				board.makeMove(move2.row, move2.col);
//				System.out.println(board);
//			}
//			if(board.isGameOver()) {
//				winner = board.getWinner();
//				break;
//			}
//						
//		}
//		
//		if(winner == PlayerType.Player1) System.out.println("Player 1 wins");
//		else if(winner == PlayerType.Player2) System.out.println("Player 2 wins");
//		
		//String k = move.toString();
		
	}
	
	public void testAndOr() throws Exception {
//		AndOrStrategy strategy = new AndOrStrategy(3);
//		GameBoard board = new GameBoard(2, 0);
//		board.setPeeblesAt(0, 0, 1);
//		board.setPeeblesAt(0, 1, 1);
//		board.setPeeblesAt(1, 0, 1);
//		
//		Cell move = strategy.getNextMove(board, new Player(PlayerType.Player1));
//		int k = 2;
		
	}
	
//	public void testMinMaxCanMakeWinningMove() throws Exception {
//		Player p1 = new Player(PlayerType.Player1);
//		Player p2 = new Player(PlayerType.Player2);
//		
//		p2.setStrategy(new AndOrStrategy());
//		p1.setStrategy(new MinMaxStrategy(new TotalPeeblesAtHandHeuristic()));
//		
//		GameBoard board = new GameBoard(2, 0);
//		board.setPeeblesAt(0, 0, 1);
//		
//		Cell move = p1.getNextMove(board);
//		
//		
//	}
	
	public void testMinMaxOnLoosingGame() throws Exception {
		Player p1 = new Player(PlayerType.Player1);
		Player p2 = new Player(PlayerType.Player2);
		
		p1.setStrategy(new AndOrStrategy());
		p2.setStrategy(new MinMaxStrategy(new TotalPeeblesAtHandHeuristic()));
		
//				  +--+--+--+--+
//		Player 1  | 1| 4| 0|10|
//		          +--+--+--+--+
//		Player 2  | 0| 0| 1| 0|
//		          +--+--+--+--+
//		          
		GameBoard board = new GameBoard(4, 0);
		board.setPeeblesAt(0, 0, 1);
		board.setPeeblesAt(0, 1, 4);
		board.setPeeblesAt(0, 2, 0);
		board.setPeeblesAt(0, 3, 10);
		board.setPeeblesAt(1, 0, 0);
		board.setPeeblesAt(1, 1, 0);
		board.setPeeblesAt(1, 2, 1);
		board.setPeeblesAt(1, 3, 0);
		
		
		Cell move = p2.getNextMove(board);
		
		
	}
	
//	public void testBoardsAreEqual() throws Exception {
//		HashSet<GameBoard> set = new HashSet<>();
//		GameBoard board1 = new GameBoard(2,2);
//		GameBoard board2 = new GameBoard(2,2);
//		
//		assertTrue(board1.equals(board2));
//		
//		set.add(board1);
//		set.add(board2);
//		
//		assertEquals(1, set.size());
//		
//		GameBoard board3 = new GameBoard(2, 0);
//		
//		assertFalse(board1.equals(board3));
//		
//		set.add(board3);
//		
//		assertEquals(set.size(), 2);
//		
//		
//		GameBoard board4 = new GameBoard(2, 0);
//		
//		board3.setPeeblesAt(0, 0, 2);
//		board3.setPeeblesAt(0, 1, 2);
//		board3.setPeeblesAt(1, 0, 2);
//		board3.setPeeblesAt(1, 1, 2);
//		
//		set.add(board4);
//		
//		assertEquals(set.size(), 2);
//	}
	public void testKeyWorks() throws Exception{
		MinMaxStrategy.Key key1 = new MinMaxStrategy.Key(FunctionType.Min, new GameBoard(2));
		MinMaxStrategy.Key key2 = new MinMaxStrategy.Key(FunctionType.Max, new GameBoard(2));
		MinMaxStrategy.Key key3 = new MinMaxStrategy.Key(FunctionType.Min, new GameBoard(2));
		MinMaxStrategy.Key key4 = new MinMaxStrategy.Key(FunctionType.Min, new GameBoard(3));
		
		assertFalse(key1.equals(key2));
		assertFalse(key1.equals(key4));
		assertTrue(key1.equals(key3));
		
	}
}
