package game;

/* Interface for all strategies to implement. */
public interface Strategy {
	public Cell getNextMove(GameBoard board, Player player);
}
