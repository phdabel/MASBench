package br.ufrgs.MASBench.WorldModel.ClassificationModel;

import java.util.Collection;
import java.util.HashMap;

import br.ufrgs.MASBench.Assignment.DCOP.DCOPAgent;
import br.ufrgs.MASBench.BinaryMaxSum.BMS.Factor;
import br.ufrgs.MASBench.BinaryMaxSum.Factor.CardinalityFactor;
import br.ufrgs.MASBench.BinaryMaxSum.Factor.SaturationFactor;
import br.ufrgs.MASBench.BinaryMaxSum.Factor.CardinalityFactor.CardinalityFunction;
import br.ufrgs.MASBench.Comm.CommunicationLayer;
import br.ufrgs.MASBench.Comm.Message;
import br.ufrgs.MASBench.FGMD.NodeID;
import br.ufrgs.MASBench.FGMD.TickDampedCommunicationAdapter;
import br.ufrgs.MASBench.FGMD.TickDampedCommunicationAdapter.TickMessage;
import br.ufrgs.MASBench.Solver.ProblemDefinition;
import br.ufrgs.MASBench.Solver.Operator.MaxOperator;
import br.ufrgs.MASBench.Solver.Operator.Minimize;
import br.ufrgs.MASBench.WorldModel.Entity;

/**
 * Agent that optimizes the instance value
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class InstanceAgent implements DCOPAgent {
	
	
	private Entity id;
	private Integer dimension;
	private ProblemDefinition problem;
	private Integer target;
	private TickDampedCommunicationAdapter comm;
	private static final MaxOperator MAX_OPERATOR = new Minimize();
	private long constraintChecks;
	
	//selector of this factorindex
	private SaturationFactor<NodeID> variable;
	//constraint/relation among subsets of variables
	//private HashMap<NodeID, ArrayList<CardinalityFactor<NodeID>>> groupCardinalityFactor = new HashMap<NodeID, ArrayList<CardinalityFactor<NodeID>>>();
	//factors and factor locations reffers to leaf node factors
	private HashMap<NodeID, Factor<NodeID>> factors = new HashMap<NodeID, Factor<NodeID>>();
	private HashMap<NodeID, Entity> factorLocations = new HashMap<NodeID, Entity>();
	private HashMap<NodeID, Integer> highOrderPotential = new HashMap<NodeID, Integer>();
	
	
	public InstanceAgent(Entity id) {
		super();
		this.id = id;
	}

	@Override
	public void initialize(Entity agentID, ProblemDefinition problem) {
		
		this.id = agentID;
		this.dimension = agentID.getDimension();
		this.problem = problem;
		this.target = null;
		
		this.comm = new TickDampedCommunicationAdapter();
		
		this.addSelector();
		
		this.addCardinalityFactor();
		
		this.computeFactorLocations();
		
	}
	
	private void addSelector()
	{
		
		this.variable = new SaturationFactor<NodeID>();
		
		Integer highestFeature = problem.getHighestTargetForAgent(this.id);
		double highestValue = problem.getClassUtility(this.id, highestFeature);
		for(Integer feature : id.getFeatureList().keySet())
		{
			NodeID nodeJ = new NodeID(null, feature, this.dimension);
			
			this.variable.addNeighbor(nodeJ);
			double val = problem.getClassUtility(this.id, feature);
			
			double value = -val * Math.exp(-(val/highestValue));
			this.variable.setPotential(nodeJ, value);
			
			
		}
		
		addFactor(new NodeID(id, null, this.dimension), this.variable);
						
		
	}
	
	private void addCardinalityFactor()
	{
		
		//final int nFeatures = problem.getFeatures().size();;
		//final int nInstances = problem.getInstances().get(this.dimension).size();
		//final int nInstance = problem.getInstances().get(this.dimension).indexOf(id);
		
		for(Integer feature : id.getFeatureList().keySet())
		{
			 
			final NodeID j = new NodeID(null, feature, this.dimension);
			final CardinalityFactor<NodeID> f = new CardinalityFactor<NodeID>();
			
			CardinalityFunction func = new CardinalityFunction(){

				@Override
                public double getCost(int n) {
                	if(n <= 1)
                		return 0.0;
                	double value = (2/(n*(n-1)));
                	return value;
                	
                }
				
			};
			
			f.setFunction(func);
			
			for(Entity instance : problem.getFeatures().get(feature))
			{
				f.addNeighbor(new NodeID(instance, null, this.dimension));
			}
			
			f.addNeighbor(new NodeID(null, feature, 0));			
			addFactor(j, f);
			
		}
		
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
		
		for(Entity i : problem.getInstances().get(this.dimension))
		{
			factorLocations.put(new NodeID(i, null, i.getDimension()), i);
		}
		
		for(Integer feature : problem.getFeatures().keySet())
		{
			for(Entity instance : problem.getFeatures().get(feature))
			{
				factorLocations.put(new NodeID(null, feature, this.dimension), instance);
			}			
		}
		
		for(Integer j : problem.getFeatures().keySet())
		{
			highOrderPotential.put(new NodeID(null, j, 0), j);
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
	public Entity getID() {
		return this.id;
	}

	@Override
	public Integer getTarget() {
		return this.target;
	}

	@Override
	public Collection<TickMessage> sendMessages(CommunicationLayer com) {
		
		Collection<TickMessage> messages = this.comm.flushMessages();
		for(TickMessage msg : messages)
		{
			if(factorLocations.containsKey(msg.recipient))
			{
				Entity recipient = factorLocations.get(msg.recipient);
				com.send(recipient, msg);
			}else{
				if(highOrderPotential.containsKey(msg.recipient))
				{
					Integer recipient = highOrderPotential.get(msg.recipient);
					com.send(recipient, msg);
				}
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
		if(factors.containsKey(msg.recipient))
		{
			Factor<NodeID> recipient = factors.get(msg.recipient);
			recipient.receive(msg.value, msg.sender);
		}
	}

	@Override
	public long getConstraintChecks() {
		return this.constraintChecks;
	}
	

}
