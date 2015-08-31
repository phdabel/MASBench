package test;

import java.util.HashMap;

import br.ufrgs.MASBench.Entity;
import br.ufrgs.MASBench.NodeID;
import br.ufrgs.MASBench.TickDampedCommunicationAdapter;
import br.ufrgs.MASBench.BinaryMaxSum.Factor.SelectorFactor;
import br.ufrgs.MASBench.BinaryMaxSum.Factor.WeightingFactor;
import br.ufrgs.MASBench.Solver.Operator.Minimize;


public class testAlgo {

	public static void main(String[] args) {
		
		TickDampedCommunicationAdapter instance = new TickDampedCommunicationAdapter();
		instance.tick();
		
		SelectorFactor<NodeID> s1 = new SelectorFactor<NodeID>();
		NodeID me = new NodeID(new Entity(1),null,null);
		
		NodeID task1 = new NodeID(null, new Entity(10), null);
		NodeID task2 = new NodeID(null, new Entity(2), null);
		NodeID task3 = new NodeID(null, new Entity(3), null);
		
		WeightingFactor<NodeID> potential = new WeightingFactor<NodeID>(s1);
		potential.addNeighbor(task1);
		potential.setPotential(task1, 10.0);
		potential.addNeighbor(task2);
		potential.setPotential(task2, 12.0);
		potential.addNeighbor(task3);
		potential.setPotential(task3, 10.8);
		
		potential.setCommunicationAdapter(instance);
		potential.setIdentity(me);
		potential.setMaxOperator(new Minimize());
				
		
		s1.run();
		
		System.out.println(s1.select().toString());
		HashMap<NodeID, Boolean> m = new HashMap<>();
		m.put(task1, false);
		m.put(task2, true);
		m.put(task3, false);
		System.out.println(potential.evaluate(m));
		
		
		
	}

}
