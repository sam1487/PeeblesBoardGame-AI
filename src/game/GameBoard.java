package game;

import game.Player.PlayerType;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;


/***
 * Represents a game board of two rows and N columns. Utility functions 
 * are provided to operate on the game board.
 */
public class GameBoard extends Object {
	int board[][];
	
	/* Copy constructor */
	public GameBoard(GameBoard anotherBoard) {
		this.board = new int[2][anotherBoard.getSize()];
		for(int i = 0; i < this.board[0].length; ++i) {
			this.board[0][i] = anotherBoard.board[0][i];
			this.board[1][i] = anotherBoard.board[1][i];
		}
	}
	
    public GameBoard(int columns) {
        setupBoard(columns, 2);
    }
    
    public GameBoard(int columns, int peebles) {
        setupBoard(columns, peebles);
    }

    private void setupBoard(int columns, int peebles) {
        board = new int[2][columns];
        Arrays.fill(board[0], peebles);
        Arrays.fill(board[1], peebles);
    }

    public int getSize() {
        return board[0].length;
    }

    public int getPeeblesAt(int row, int col) {
        return board[row][col];
    }

    public void setPeeblesAt(int row, int col, int peebles) {
        board[row][col] = peebles;
    }

    public void incrementPeeblesAt(int row, int col) {
        board[row][col]++;
    }

    public void decrementPeeblesAt(int row, int col) {
        board[row][col]--;
    }
    
    /* Distribute peebles clockwise from (row, col) */
    public boolean makeMove(int row, int col) {
    	if(board[row][col] == 0) return false;
    	int p = board[row][col];
    	
		board[row][col] = 0;
		distributePeebles(p, row, col);    		
		
		return true;
    }
	
    private void distributePeebles(int p, int r, int c) {
		int row = r, col = c;
		ArrayList<Cell> list = new ArrayList<>();
		if(row == 0) {
			for(col = col + 1; col < board[row].length; ++col) list.add(new Cell(0, col));
			for(col = col - 1; col >= 0; --col) list.add(new Cell(1, col));
			for(col = 0; col <= c; ++col) list.add(new Cell(0, col));
		}
		else {
			for(col = col - 1; col >= 0; --col) list.add(new Cell(1, col));
			for(col = 0; col < board[row].length; ++col) list.add(new Cell(0, col));
			for(col = col - 1; col >= c; --col) list.add(new Cell(1, col));
		}
		
		int index = 0;
		while(p > 0) {
			row = list.get(index).row;
			col = list.get(index).col;
			incrementPeeblesAt(row, col);
			
			index = (index + 1) % list.size();
			p--;
		}		
	}
	
    /* Get the board state */
    public boolean isGameOver() {
		int size = this.getSize();
		boolean alive[] = new boolean[2];
		for(int row = 0; row < 2; ++row) {
			alive[row] = false;			
			for(int col = 0; col < size; ++col) {
				if(this.board[row][col] > 0) alive[row] = true;
			}
		}
		return alive[0] ^ alive[1];
	}
	
    /* Get the winner of a finished game */
	public PlayerType getWinner() {
		for(int row = 0; row < 2; ++row)
			for(int col = 0; col < this.getSize(); ++col)
				if(this.board[row][col] > 0) {
					if(row == 0) return PlayerType.Player1;
					else return PlayerType.Player2; 
				}
		
		return null;
	}
	
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(board[0]) + Arrays.hashCode(board[1]);
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj) {		
		GameBoard otherBoard = (GameBoard) obj;
		if(this.getSize() != otherBoard.getSize()) return false;
		for(int row = 0; row < 2; ++row) {
			for(int col = 0; col < this.getSize(); ++col) {
				if(this.board[row][col] != otherBoard.board[row][col])
					return false;
			}
		}
		return true;
		
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		GameBoard board = new GameBoard(this.board.length);
		for(int i = 0; i < this.board[0].length; ++i) {
			board.board[0][i] = this.board[0][i];
			board.board[1][i] = this.board[1][i];
		}
		return board;
	}

	public static int getRowForPlayer(Player player) {
		if(player.type == PlayerType.Player1) return 0;
		return 1;
	}
	public static int getRowForPlayerType(PlayerType type) {
		if(type == PlayerType.Player1) return 0;
		return 1;
	}
	
	
	@Override
	public String toString() {
		return GameBoardWriter.convertToString(this);
	}
	
	
}


