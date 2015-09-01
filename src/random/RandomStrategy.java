package random;

import game.Cell;
import game.GameBoard;
import game.Player;
import game.Strategy;
import game.Player.PlayerType;

import java.util.ArrayList;
import java.util.Random;

/* A strategy that chooses a valid move randomly */
public class RandomStrategy implements Strategy {
	@Override
	public Cell getNextMove(GameBoard board, Player player) {
		int row; 
		if(player.type == PlayerType.Player1) {
			row = 0;
		}
		else row = 1;
		
		ArrayList<Cell> list = new ArrayList<Cell>();
		for(int col = 0; col < board.getSize(); ++col){
			if(board.getPeeblesAt(row, col) > 0)
				list.add(new Cell(row, col));
		}
		if(list.isEmpty()) return null;
			
		int index = new Random().nextInt(list.size());
		return list.get(index);
	}
	
	@Override
	public String toString() {
		return "RandomStrategy";
	}
}
