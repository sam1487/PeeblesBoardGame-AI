package minmax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import game.Cell;
import game.GameBoard;
import game.GameUi;
import game.Player;
import game.Strategy;
import game.Player.PlayerType;


/* Implements the aB-MinMax game playing strategy */
public class MinMaxStrategy implements  Strategy {	
	int ply;
	Heuristic heuristic;
	Player curPlayer;
	public static enum FunctionType {Max, Min} ;
	public static class Key {
		FunctionType type;
		GameBoard state;
		public Key(FunctionType type, GameBoard state) {
			this.type = type;
			this.state = state;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result ;
			result = prime * result
					+ ((type == null) ? 0 : type.hashCode());
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (type != other.type)
				return false;
			if (state == null) {
				if (other.state != null)
					return false;
			} else if (!state.equals(other.state))
				return false;
			return true;
		}
	
	}
	
	HashMap<Key, Integer> transTable = new HashMap<>();
	
	public MinMaxStrategy() {
		this.heuristic = new TotalPeeblesAtHandHeuristic();
	}
	public MinMaxStrategy(Heuristic heuristic) {
		this.heuristic  = heuristic;
	}
	public void setHeuristic(Heuristic heuristic) {
		this.heuristic = heuristic;
	}
	
	/* Swap turns */
	public PlayerType turnPlayer(PlayerType player){
		if(player == PlayerType.Player1) return PlayerType.Player2;
		return PlayerType.Player1;
	}
	
	@Override
	public Cell getNextMove(GameBoard board, Player player) {
		this.ply = GameUi.ply;
		this.curPlayer = player;
		this.transTable = new HashMap<>();
		
		Set<GameBoard> visited = new HashSet<>();
		
		List<Cell> nextActions = nextActions(board, player.type);
		
		int maxV = Integer.MIN_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		Cell nextAction = null;
		
		for(Cell action : nextActions) {
			GameBoard nextState = new GameBoard(board);
			nextState.makeMove(action.row, action.col);
			
			if(visited.contains(nextState)) {
				continue;
			}
			visited.add(nextState);
			
			int v = MIN(nextState, turnPlayer(player.type), visited, alpha, beta, 0);
		
			if(v >= maxV) {
				maxV = v;
				nextAction = action;
			}
			if(maxV >= beta) return nextAction;
			if(maxV > alpha) alpha = maxV;			
		}
		
		return nextAction;		
	}
	
	private int MIN(GameBoard state, PlayerType player, Set<GameBoard> visited, 
			int alpha, int beta, int ply) {
		
		if(cutOffTest(state, player, ply)) {
			return getUtility(state, player);
		}
		
		Key hashKey = new Key(FunctionType.Min, state);
		if(this.transTable.containsKey(hashKey)) {
			System.out.println("\nFound in transTable\n");
			return this.transTable.get(hashKey);
		}
		
		int minV = Integer.MAX_VALUE;
		List<Cell> nextActions = nextActions(state, player);
		
		for(Cell action : nextActions) {
			GameBoard nextState = new GameBoard(state);
			nextState.makeMove(action.row, action.col);
			
			if(visited.contains(nextState)) continue;
			
			visited.add(nextState);
			
			int v = MAX(nextState, turnPlayer(player), visited, alpha, beta, ply + 1);
			if(v < minV) {
				minV = v;				
			}
			if(minV <= alpha) {
				this.transTable.put(hashKey, minV);
				return minV;
			}
			if(minV < beta) beta = minV;
		}
		
		this.transTable.put(hashKey, minV);
		
		return minV;
	}
	
	private int MAX(GameBoard state, PlayerType player, Set<GameBoard> visited,
			int alpha, int beta, int ply) {
		
		if(cutOffTest(state, player, ply)) {
			return getUtility(state, player);
		}
		

		Key hashKey = new Key(FunctionType.Max, state);
		if(this.transTable.containsKey(hashKey)) {
			System.out.println("\nFound in transTable\n");
			return this.transTable.get(hashKey);
		}
		
		
		int maxV = Integer.MIN_VALUE;
		List<Cell> nextActions = nextActions(state, player);
		
		for(Cell action : nextActions) {
			GameBoard nextState = new GameBoard(state);
			nextState.makeMove(action.row, action.col);
			
			if(visited.contains(nextState)) continue;
			
			visited.add(nextState);
			
			int v = MIN(nextState, player, visited, alpha, beta, ply);
			if(v > maxV) {
				maxV = v;
			}
			
			if(maxV >= beta) {
				this.transTable.put(hashKey, maxV);
				return maxV;
			}
			if(maxV > alpha) alpha = maxV;
		}
		
		this.transTable.put(hashKey, maxV);
		return maxV;
	}
	
	private int getUtility(GameBoard state, PlayerType player) {
		return this.heuristic.getUtility(state, this.curPlayer.type);
	}

	private boolean cutOffTest(GameBoard state, PlayerType player, int ply) {
		if(ply == this.ply || isGoalState(state, player)) 
			return true;
		return false;
	}

	private boolean isGoalState(GameBoard state, PlayerType player) {
		int row = 1 - GameBoard.getRowForPlayerType(player);

		for(int col = 0; col < state.getSize(); ++col) {
			if(state.getPeeblesAt(row, col) > 0)
				return false;
		}
		return true;
	}
	
	private List<Cell> nextActions(GameBoard state, PlayerType player) {		
		List<Cell> toret = new LinkedList<Cell>();
		int row = GameBoard.getRowForPlayerType(player);
		
		if(player == PlayerType.Player1) {
			/* Move ordering for player 1, choose from left to right */
			
			for(int col = 0; col < state.getSize(); ++col) {
				if(state.getPeeblesAt(row, col) > 0)
					toret.add(new Cell(row, col));
			}				
		}		
		else if(player == PlayerType.Player2) {
			/* Move ordering for player 2, choose from right to left */
			
			for(int col = state.getSize() - 1; col >= 0 ; --col) {
				if(state.getPeeblesAt(row, col) > 0)
					toret.add(new Cell(row, col));
			}
		}

		return toret;
	}

	@Override
	public String toString() {
		return "aB-MiniMax(" + this.heuristic + ")";
	};
}
