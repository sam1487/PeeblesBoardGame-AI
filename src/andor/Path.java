package andor;

import game.GameBoard;

import java.util.LinkedList;

class Path extends LinkedList<GameBoard> {
	public Path append(GameBoard... states) {
		Path appendedPath = new Path();
		appendedPath.addAll(this);
		for (int i = 0; i < states.length; i++) {
			appendedPath.add(states[i]);
		}
		return appendedPath;
	}
	
	public Path prepend(GameBoard state) {
        Path prependedPath = new Path();
        prependedPath.add(state);
        prependedPath.addAll(this);

        return prependedPath;
	}
}