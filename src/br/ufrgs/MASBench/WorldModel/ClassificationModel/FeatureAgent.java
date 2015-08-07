package br.ufrgs.MASBench.WorldModel.ClassificationModel;

import java.util.Collection;
import java.util.HashMap;

import br.ufrgs.MASBench.BinaryMaxSum.BMS.Factor;
import br.ufrgs.MASBench.BinaryMaxSum.Factor.ConditionedSelectorFactor;
import br.ufrgs.MASBench.Comm.CommunicationLayer;
import br.ufrgs.MASBench.Comm.Message;
import br.ufrgs.MASBench.FGMD.NodeID;
import br.ufrgs.MASBench.FGMD.TickDampedCommunicationAdapter;
import br.ufrgs.MASBench.FGMD.TickDampedCommunicationAdapter.TickMessage;
import br.ufrgs.MASBench.Solver.ExternalAgent;
import br.ufrgs.MASBench.Solver.ProblemDefinition;
import br.ufrgs.MASBench.Solver.Operator.MaxOperator;
import br.ufrgs.MASBench.Solver.Operator.Minimize;
import br.ufrgs.MASBench.WorldModel.Entity;

/**
 * Agent that optimize the feature value
 * 
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class FeatureAgent implements ExternalAgent {

	private Integer id;
	private ProblemDefinition problem;
	private Entity target;
	private TickDampedCommunicationAdapter comm;
	private static final MaxOperator MAX_OPERATOR = new Minimize();
	private long constraintChecks;
	
	//selector of this factorindex
	private ConditionedSelectorFactor<NodeID> variable;
	//constraint/relation among subsets of variables
	//private HashMap<NodeID, ArrayList<CardinalityFactor<NodeID>>> groupCardinalityFactor = new HashMap<NodeID, ArrayList<CardinalityFactor<NodeID>>>();
	//factors and factor locations reffers to leaf node factors
	private HashMap<NodeID, Factor<NodeID>> factors = new HashMap<NodeID, Factor<NodeID>>();
	private HashMap<NodeID, Integer> factorLocations = new HashMap<NodeID, Integer>();
	private HashMap<NodeID, Entity> highOrderFactor = new HashMap<NodeID, Entity>();
	
	public FeatureAgent(Integer j)
	{
		this.id = j;
	}
	
	@Override
	public void initialize(Integer agentID, ProblemDefinition problem) {
		
		this.id = agentID;
		
		this.problem = problem;
		this.target = null;
		
		this.comm = new TickDampedCommunicationAdapter();
		
		this.addConditionedSelector();
		
		this.computeFactorLocations();
		
	}
	
	public void addConditionedSelector()
	{
		this.variable = new ConditionedSelectorFactor<NodeID>();
		NodeID j = new NodeID(null, this.id, 0);
		this.variable.addNeighbor(j);
		this.variable.setConditionNeighbor(j);
		for(Integer k : problem.getInstances().keySet())
		{
			for(Entity o : problem.getInstances().get(k))
			{
				if(o.getFeatureList().containsKey(this.id))
				{
					NodeID oJ = new NodeID(null, this.id, k);
					this.variable.addNeighbor(oJ);
					break;
				}
			}
		}
		addFactor(j, this.variable);
		
	}
	
	
	private void addFactor(NodeID node, Factor<NodeID> factor)
	{
		factors.put(node, factor);
		factor.setMaxOperator(MAX_OPERATOR);
		factor.setIdentity(node);
		factor.setCommunicationAdapter(this.comm);
		
	}
	
	private void computeFactorLocations()
	{
		
		int nFeatures = problem.getFeatures().keySet().size();
		
		for(Integer j : problem.getFeatures().keySet())
		{
			factorLocations.put(new NodeID(null, j, 0), j);
		}
		
		for(Integer j : problem.getFeatures().keySet())
		{
			
			int nInstances = problem.getFeatures().get(j).size();
			if(nInstances <= 0)
				continue;
			for(int idx = 0; idx < nFeatures; idx++)
			{
				Entity instance = problem.getFeatures().get(j).get(idx % nInstances);
				highOrderFactor.put(new NodeID(null, j, instance.getDimension()), instance);		
			}
			
		}
		
	}
	
	
	@Override
	public boolean improveAssignment() {
		this.constraintChecks = 0;
		
		for(NodeID f : factors.keySet())
		{
			this.constraintChecks += factors.get(f).run();
		}
		
		return !this.comm.isConverged();
	}

	@Override
	public Integer getID() {
		return this.id;
	}

	@Override
	public Entity getTarget() {
		return this.target;
	}

	@Override
	public Collection<TickMessage> sendMessages(CommunicationLayer com) {
		Collection<TickMessage> messages = this.comm.flushMessages();
		for(TickMessage msg : messages)
		{
			if(highOrderFactor.containsKey(msg.recipient))
			{
				Entity recipient = highOrderFactor.get(msg.recipient);
				com.send(recipient, msg);
			}else{
				Integer recipient = factorLocations.get(msg.recipient);
				com.send(recipient, msg);
			}
			
		}
		return messages;
	}

	@Override
	public void receiveMessages(Collection<Message> messages) {
		if(messages == null)
			return;
		for(Message amessage : messages)
		{
			if(amessage == null)
				continue;
			
			receiveMessage(amessage);
		}
		
	}
	
	private void receiveMessage(Message message)
	{
		if(!(message instanceof TickMessage))
			throw new IllegalArgumentException("This implementation uses TickMessage of the TickDampedCommunicationAdapter.");
		
		TickMessage msg = (TickMessage)message;
		if(factors.containsKey(msg.recipient)){
			Factor<NodeID> recipient = factors.get(msg.recipient);
			recipient.receive(msg.value, msg.sender);
		}
	}

	@Override
	public long getConstraintChecks() {
		return this.constraintChecks;
	}
	

}
