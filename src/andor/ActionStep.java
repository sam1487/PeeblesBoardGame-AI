package andor;

import game.Cell;

/* Step that represent a specific move to take in an AND-OR plan*/
class ActionStep implements Step {
	Cell action;
	
	public ActionStep(Cell action) {
		this.action = action;
	}
	
	public Cell getAction() {
		return this.action;
	} 
	
	@Override
	public String toString() {
		return String.format("Move(%d, %d", action.row, action.col);
	}
}