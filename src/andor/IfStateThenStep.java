package andor;

import game.GameBoard;

/* A conditional action step that is a part of a AND-OR plan */
class IfStateThenStep implements Step {
	GameBoard state;
	Plan plan;
	
	public IfStateThenStep(GameBoard state, Plan plan) {
		this.state = state;
		this.plan = plan;
	}
		
	public Plan getActionForState(GameBoard state) {
		if(this.state.equals(state)) return plan;
		return null;
	}
	@Override
	public String toString() {
		String toret = "";
		if(this.state != null) toret += "State: " + this.state;
		if(this.plan != null) toret += ", Plan: " + this.plan;
		return toret;
	}

}