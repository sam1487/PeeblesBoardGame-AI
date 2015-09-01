package human;

import game.Cell;
import game.GameBoard;
import game.Player;
import game.Strategy;

import java.util.Scanner;


/* Represents a human playing the game */
public class HumanStrategy implements Strategy {
	Scanner sc;
	public HumanStrategy(Scanner sc) {
		this.sc = sc;
	}
	
	@Override
	public Cell getNextMove(GameBoard board, Player player) {
		int playerId = GameBoard.getRowForPlayer(player);
		System.out.print(String.format("\n? %s, Pick your column[1-%d]: ", player, board.getSize()));
		
		int row = playerId;
		int col = 0;
		try { 
			 col = sc.nextInt() - 1;
			 if(board.getPeeblesAt(row, col) == 0) {
					System.out.println("\n! Can't choose an empty cell, try again.\n");
					return getNextMove(board, player);
			 }
			 return new Cell(row, col);				
		}
		catch(Exception e) {
			System.out.println("\n! Enter a valid integer. Try again.\n");
			return getNextMove(board, player);
		}		
	}
	@Override
	public String toString() {
		return "HumanStrategy";
	}
}
