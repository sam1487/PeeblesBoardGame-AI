package andor;

import java.util.LinkedList;

class Plan extends LinkedList<Step> {
    LinkedList<Step> steps = new LinkedList<Step>();
    public Plan() {
    }

    public Plan(Step... steps) {
    	for (int i = 0; i < steps.length; i++) {
    		add(steps[i]);
   		}
    }

    public Plan prepend(Step action) {
        this.offerFirst(action);
        return this;
    } 
}