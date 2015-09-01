package sandbox;

import game.GameBoard;
import game.GameBoardWriter;
import junit.framework.TestCase;


public class GameBoardWriterTests extends TestCase {
	
	public void testPrintsBoardForSize2() throws Exception {
		GameBoard board = new GameBoard(2, 2);
		String actual = GameBoardWriter.convertToString(board);
		String expected = "\n+--+--+\n" 
						+ "| 2| 2|\n"
						+ "+--+--+\n"
						+ "| 2| 2|\n"
						+ "+--+--+\n";
		
		assertEquals(expected, actual);
	}
	public void testPrintsBoardForSize3() throws Exception {
		GameBoard board = new GameBoard(3, 2);
		String actual = GameBoardWriter.convertToString(board);
		String expected = "\n+--+--+--+\n" 
						  + "| 2| 2| 2|\n"
						  + "+--+--+--+\n"
						  + "| 2| 2| 2|\n"
						  + "+--+--+--+\n";
		
		assertEquals(expected, actual);
	}
	
	public void testPrintBoardForSize10() throws Exception {
		GameBoard board = new GameBoard(10, 99);
		String actual = GameBoardWriter.convertToString(board);
		System.out.println(actual);
	}
}
