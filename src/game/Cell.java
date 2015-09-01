package game;

/***
 * Represents a cell in the game board
 */
public class Cell {
	public int row;
	public int col;
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public String toString() {
		return String.format("Cell(%d, %d)", row, col);
	}

}
