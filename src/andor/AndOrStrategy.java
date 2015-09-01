package andor;

import game.Cell;
import game.GameBoard;
import game.GameUi;
import game.Player;
import game.Strategy;
import game.Player.PlayerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/* The And-Or playing strategy */
public class AndOrStrategy implements Strategy{
	int ply = 0;

	public AndOrStrategy() {				
	}
	
	@Override
	public Cell getNextMove(GameBoard board, Player player) {
		this.ply = GameUi.ply;
		Cell nextMove = null;
		
		/* Try out at least one single step that leads to a goal */
		Plan gamePlan = orSearch(board, new Path(), player, 0);
		
		/* If there's no guranteed plan, choose a move randomly */
		if(gamePlan == null) {
			nextMove = getRandomChoice(board, player);
		} 
		else {
			
			/* Nothing to do as the plan is empty because of reaching the goal */
			if(gamePlan.isEmpty()) return null;			
			
			/* Get the next best move */
			nextMove = getBestMove(gamePlan);
		}
		return nextMove;
	}

	private Cell getBestMove(Plan gamePlan) {
		Step step = gamePlan.get(0);
		if(step instanceof ActionStep) {
			ActionStep actionStep = (ActionStep) step;
			Cell action = actionStep.getAction();
			gamePlan.removeFirst();
			return action;			
		}		
		return null;
	}
	
	private Cell getRandomChoice(GameBoard board, Player player) {
		int row = board.getRowForPlayer(player);
		ArrayList<Cell> list = new ArrayList<Cell>();
		for(int col = 0; col < board.getSize(); ++col) {
			if(board.getPeeblesAt(row, col) > 0) 
				list.add(new Cell(row, col));
		}
		
		if(list.isEmpty()) return null;
		int index = new Random().nextInt(list.size());
		return list.get(index);
	}

	private Plan orSearch(GameBoard state, Path path, Player player, int curPly) {
  		
		if(isGoalState(state, player))
			return new Plan();
				
		if(curPly == this.ply) {
			if(isPseudoGoalStateAtPly(state, player)) return new Plan();
			else return null;
		}
	
		if(path.contains(state))
			return null;
		
		for(Cell action : nextActions(state, player)) {
			Plan plan = andSearch(
					actionResults(state, action, player), 
					path.prepend(state), player, curPly);
			
			if(plan != null) {
				return plan.prepend(new ActionStep(action));
			}
		}
		
		return null;
	}

	private Plan andSearch(Set<GameBoard> states, Path path, Player player, int curPly){
		GameBoard[] _states = states.toArray(new GameBoard[0]);
        Plan[] plans = new Plan[_states.length];
        
        for (int i = 0; i < _states.length; i++) {    
            plans[i] = orSearch(_states[i], path, player, curPly + 1);
            if (plans[i] == null) {
                    return null;
            }
        } 
        
        return constructPlan(_states, plans);      
	}

	private Plan constructPlan(GameBoard[] _states, Plan[] plans) {
		Step[] steps = new Step[plans.length];
        if (plans.length > 0) {
        	for (int i = 0; i < plans.length - 1; i++) {
        		steps[i] = new IfStateThenStep(_states[i], plans[i]);
        	}
        	steps[steps.length-1] = new IfStateThenStep(null, plans[plans.length - 1]);
        }
		return new Plan(steps);
	}

	private Set<GameBoard> actionResults(GameBoard state, Cell action, Player player) {
		int row = GameBoard.getRowForPlayer(player);
		
		Set<GameBoard> results = new HashSet<GameBoard>();
		GameBoard newState = new GameBoard(state);
		
		// Make the OR player move
		newState.makeMove(action.row, action.col);
		
		// get all possible states from opponent's play
		List<Cell> allOpponentMoves = getAllOpponentMoves(newState, player);
		for(Cell move : allOpponentMoves) {
			GameBoard nextState = new GameBoard(newState);
			nextState.makeMove(move.row, move.col);
			results.add(nextState);
		}
		
		return results;
	}

	private List<Cell> getAllOpponentMoves(GameBoard newState, Player player) {
		List<Cell> toret = new LinkedList<Cell>();
		int row = 1 - GameBoard.getRowForPlayer(player);
		for(int col = 0; col < newState.getSize(); ++col) {
			if(newState.getPeeblesAt(row, col) > 0) 
				toret.add(new Cell(row, col));
		}
		return toret;
	}

	/* Get a list of all actions possible at the given state */
	private List<Cell> nextActions(GameBoard state, Player player) {
		List<Cell> toret = new LinkedList<Cell>();
		int row = GameBoard.getRowForPlayer(player);
		
		for(int col = 0; col < state.getSize(); ++col) {
			if(state.getPeeblesAt(row, col) > 0)
				toret.add(new Cell(row, col));
		}

		return toret;
	}

	private boolean isGoalState(GameBoard state, Player player) {
		int row = 1 - GameBoard.getRowForPlayer(player);

		for(int col = 0; col < state.getSize(); ++col) {
			if(state.getPeeblesAt(row, col) > 0)
				return false;
		}
		return true;
	}
	
	/* When the ply is reached, some of the states are considered as pseudo-goal states.
	 * We consider any state with player having more peebles than opponent as favorable. 
	 */
	private boolean isPseudoGoalStateAtPly(GameBoard state, Player player) {
		int row = GameBoard.getRowForPlayer(player);
		int peeblesForPlayer = 0;
		int peeblesForOpponent = 0;
		
		for(int col = 0; col < state.getSize(); ++col) {
			peeblesForPlayer += state.getPeeblesAt(row, col);
			peeblesForOpponent += state.getPeeblesAt(1-row, col);
		}
		return peeblesForPlayer >= peeblesForOpponent;
	}	

	@Override
	public String toString() {
		return "AndOrStrategy";
	}
}
