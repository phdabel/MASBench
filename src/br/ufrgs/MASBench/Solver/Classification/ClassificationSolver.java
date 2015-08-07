package br.ufrgs.MASBench.Solver.Classification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.ufrgs.MASBench.Comm.CommunicationLayer;
import br.ufrgs.MASBench.Comm.Message;
import br.ufrgs.MASBench.Solver.AbstractSolver;
import br.ufrgs.MASBench.Solver.Assignment;
import br.ufrgs.MASBench.Solver.ExternalAgent;
import br.ufrgs.MASBench.Solver.ProblemDefinition;
import br.ufrgs.MASBench.WorldModel.Entity;
import br.ufrgs.MASBench.WorldModel.ClassificationModel.FeatureAgent;
import br.ufrgs.MASBench.WorldModel.ClassificationModel.InstanceAgent;

/**
 * Classification by message passing solver
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public abstract class ClassificationSolver extends AbstractSolver {
	
	public static final int maxIterations = 3000;
	
	public static final boolean anytime = true;
	
	private List<InstanceAgent> instances;
	private List<FeatureAgent> features;
	private List<Double> utilities;

	
	public ClassificationSolver()
	{
		this.utilities = new ArrayList<>();
	}
	
	@Override
	public Assignment compute(ProblemDefinition problem) {
		long startTime = System.currentTimeMillis();
        boolean ranOutOfTime = false;
        CommunicationLayer comLayer = new CommunicationLayer();
        initializeAgents(problem);
        
        int totalNccc = 0;
        long bMessages = 0;
        int nMessages = 0;
        int MAX_ITERATIONS = maxIterations;
        boolean done = false;
        int iterations = 0;
        Assignment finalAssignment = null, bestAssignment = null;
        double bestAssignmentUtility = Double.POSITIVE_INFINITY;
        long iterationTime = System.currentTimeMillis();
        while (!done && iterations < MAX_ITERATIONS) {
        	finalAssignment = problem.getLastAssignment();
        	
        	// send messages
        	for(InstanceAgent instance : instances)
        	{
        		
        		Collection<? extends Message> messages = instance.sendMessages(comLayer);
        		nMessages = nMessages + messages.size();
                for (Message msg : messages) {
                    bMessages += msg.getBytes();
                }
        	}
        	
        	for(ExternalAgent feature : features)
        	{
        		
        		Collection<? extends Message> messages = feature.sendMessages(comLayer);
        		nMessages = nMessages + messages.size();
        		for (Message msg : messages)
        		{
        			bMessages += msg.getBytes();
        		}
        	}
        	
        	// receive messages
            for (InstanceAgent instance : instances) {
            	instance.receiveMessages(comLayer.retrieveMessages(instance.getID()));
            }
            
            for(ExternalAgent feature : features)
            {
            	feature.receiveMessages(comLayer.retrieveMessages(feature.getID()));
            }
            
            // try to improve assignment
            done = true;
            long nccc = 0;
            for(InstanceAgent instance : instances)
            {
            	boolean improved1 = instance.improveAssignment();
            	nccc = Math.max(nccc, instance.getConstraintChecks());
                done = done && !improved1;   
            }
            
            for(FeatureAgent feature : this.features)
            {
            	boolean improved2 = feature.improveAssignment();
            	nccc = Math.max(nccc, feature.getConstraintChecks());
            	done = done && !improved2;
            	
            }
            
            // Collect the best assignment visited
            double assignmentUtility = getUtility(problem, finalAssignment);
            utilities.add(assignmentUtility);
            totalNccc += nccc;
            iterations++;
            // Check the maximum time requirements
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= maxTime) {
                System.out.println("Solver "+getIdentifier()+" ran out of time (got "
                			+maxTime+", took "+elapsedTime+" to do "+iterations+" iterations)");
                ranOutOfTime = true;
                break;
            }
            
            //System.out.println("Assignment util: "+assignmentUtility+", values: "+finalAssignment);
            if ( assignmentUtility < bestAssignmentUtility || Double.isInfinite(bestAssignmentUtility)) {
                bestAssignmentUtility = assignmentUtility;
                bestAssignment = finalAssignment;
            }

            long time = System.currentTimeMillis();
            System.out.println("Iteration "+iterations+" took "+(time-iterationTime)+"ms.");
            iterationTime = time;
        }
        //System.out.println("Done with iterations. Needed "+iterations+" in "+(System.currentTimeMillis()-startTime)+"ms.");

        // Recompute this because its not saved from the solving loop
        double finalAssignmentUtility = getUtility(problem, finalAssignment);
        
        long algBMessages = bMessages;
        int  algNMessages = nMessages;
        
        for(InstanceAgent instance : instances)
        {
        	nMessages += 0; // This should be the number of messages sent to prune the problem,
            bMessages += 0; // provided by the ProblemDefinition class itself.
        }
        
        int  nOtherMessages = nMessages - algNMessages;
        long bOtherMessages = bMessages - algBMessages;
        
        if (anytime && bestAssignment != null) {
            return bestAssignment;
        }
        
		return finalAssignment;
	}
	
	protected void initializeAgents(ProblemDefinition problem)
	{
		this.instances = new ArrayList<>();
		this.features = new ArrayList<>();
		final long startTime = System.currentTimeMillis();
		for(Integer k : problem.getInstances().keySet()){
			for(Entity id : problem.getInstances().get(k))
			{
				InstanceAgent instance = buildAgent(id);
				instance.initialize(id, problem);
				instances.add(instance);
			}
		}
		
		for(Integer f : problem.getFeatures().keySet())
		{
			FeatureAgent feature = buildAgent(f);
			feature.initialize(f, problem);
			features.add(feature);
		}
	}
	
	protected abstract InstanceAgent buildAgent(Entity id);
	
	protected abstract FeatureAgent buildAgent(Integer id);

}
