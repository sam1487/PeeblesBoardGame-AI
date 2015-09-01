package game;

/* Represents a player in the game. Each player has a playing Strategy */
public class Player {
	public enum PlayerType {Player1, Player2};
	
	public PlayerType type;
	public Strategy strategy;
	
	public Player() {}
	public Player(PlayerType type) {
		this.type = type;
	}
	
	public void setType(PlayerType type) {
		this.type = type;
	}
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	public Cell getNextMove(GameBoard board) {
		return this.strategy.getNextMove(board, this);
	}
	
	@Override
	public String toString() {
		return String.format("Player %d [%s]", GameBoard.getRowForPlayer(this) + 1, this.strategy );
	}
}
