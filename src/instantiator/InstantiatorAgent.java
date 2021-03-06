package instantiator;

import jade.core.Agent;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

/*
 * Creates the agents that will be used in the DCOP.
 * In the moment uses hardcoded values, but should be
 * able to create agents from the DFS tree in the future.
 */
public class InstantiatorAgent extends Agent {

	private static final long serialVersionUID = -7764996125444199018L;
	
	
	public void dfs(Node n, int pre) {
		if(n.wasVisited()) {
			return;
		}
		
		pre++;
		n.setPre(pre);
		n.setVisited(true);
		
		for(Node v : n.getAdjacence()) {
			if(v.getParent() == null && !v.isRoot()) {
				n.addChild(v);
				n.addLowerNeighbour(v);
				v.addUpperNeighbour(n);
				v.setParent(n);
			}

			if(v.wasVisited() && n.getParent() != v) {
				if(v.getPre() < n.getPre()) {
					v.addLowerNeighbour(n);
					n.addUpperNeighbour(v);
					System.out.println("pseudo aresta: "+n.getName()+ "-" + v.getName());
				}
			}
			
			dfs(v, pre);
		}
		
		//create agent
		try {
            getContainerController().createNewAgent(n.getName(), "node.NodeAgent", n.getArgs()).start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
	}

	/*
     * Each created agent contains a list with the names of its children.
     */
    @Override
    protected void setup() {

        /*
         * For some unfortunate reason, nothing happens when passing the
         * ArrayList as an argument in the agent's creation. So we convert it
         * to a regular array, then convert it back once inside the DcopAgent.
         */
        
    	List<Integer> domain = new ArrayList<>(); // In the example, the domain's range is [0, 1]
        domain.add(0);
        domain.add(1);
        
        Node x1 = new Node("x1", domain);
        Node x2 = new Node("x2", domain);
        Node x3 = new Node("x3", domain);
        Node x4 = new Node("x4", domain);
        Node x5 = new Node("x5", domain);
        List<Node> nodes = new ArrayList<Node>();
        nodes.add(x1);
        nodes.add(x2);
        nodes.add(x3);
        nodes.add(x4);
        nodes.add(x5);
        
        Edge a = new Edge(x1, x2, "nothing");
        Edge b = new Edge(x2, x3, "nothing");
        Edge c = new Edge(x2, x5, "nothing");
        Edge d = new Edge(x1, x4, "nothing");
        Edge e = new Edge(x5, x4, "nothing");
        Edge f = new Edge(x3, x4, "nothing");
        Edge g = new Edge(x2, x4, "nothing");
        List<Edge> edges = new ArrayList<Edge>();
        edges.add(a);
        edges.add(b);
        edges.add(c);
        edges.add(d);
        edges.add(e);
        edges.add(f);
        edges.add(g);
        
        Graph graph = new Graph(nodes, edges);
        
        for(Edge edge : graph.getEdges()) {
        	Node first = edge.getFirst();
        	Node second = edge.getSecond();
        	second.addAdjacentNode(first);
        	first.addAdjacentNode(second);
        }
        
        int pre = 0;
        x1.setRoot();
        graph.resetVisited();
        this.dfs(x1, pre);

        // We won't be needing this agent during the actual algorithm's execution
        doDelete();
    }
}
