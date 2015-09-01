package minmax;

import game.GameBoard;
import game.Player;
import game.Player.PlayerType;

/* This heuristic counts the total number of peebles for the player at
 * the current stage. 
 */
public class TotalPeeblesAtHandHeuristic implements Heuristic {

	@Override
	public int getUtility(GameBoard state, PlayerType player) {
		int row = GameBoard.getRowForPlayerType(player);
		int toret = 0;
		
		for(int col = 0; col < state.getSize(); ++col) {
			toret += state.getPeeblesAt(row, col);
		}
		
		return toret;
	}
	
	@Override
	public String toString() {
		return "H1";
	}

}
