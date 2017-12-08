package invariants;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import models.NodeAgentData;

public class maintainThresholdInvariantBehaviour extends OneShotBehaviour{
	
	private static final long serialVersionUID = -2399456552459121757L;
	NodeAgentData data = new NodeAgentData();

	public maintainThresholdInvariantBehaviour(Agent a, NodeAgentData data) {
		super(a);
		this.data = data;
	}

	@Override
	public void action() {
		System.out.println("[INV MTI    ] "+myAgent.getLocalName()+" starting maintain threshold invariant");
		
		if(data.getThreshold() < data.getLowerBound()) {
			data.setThreshold(data.getLowerBound());
		}
		if(data.getThreshold() > data.getUpperBound()) {
			data.setThreshold(data.getUpperBound());
		}
		
	}
}
