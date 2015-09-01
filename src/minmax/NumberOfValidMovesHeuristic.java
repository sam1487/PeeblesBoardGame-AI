package minmax;

import game.GameBoard;
import game.Player;
import game.Player.PlayerType;

/* The heuristic counts the number of non-zero cells for the player */
public class NumberOfValidMovesHeuristic implements Heuristic{

	@Override
	public int getUtility(GameBoard state, PlayerType player) {
		int row = GameBoard.getRowForPlayerType(player);
		int toret = 0;
		for(int col = 0; col < state.getSize(); ++col) {
			if(state.getPeeblesAt(row, col) > 0) toret++;
		}
		return toret;
	}
	
	@Override
	public String toString() {
		return "H2";
	}

}
