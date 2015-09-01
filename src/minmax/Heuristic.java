package minmax;

import game.GameBoard;
import game.Player;
import game.Player.PlayerType;

/* Interface for the heuristics for MinMax */
public interface Heuristic {
	public int getUtility(GameBoard state, PlayerType player) ;
}
