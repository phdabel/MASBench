package br.ufrgs.MASBench.Solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrgs.MASBench.FGMD.Pair;
import br.ufrgs.MASBench.Helpers.Utility.UtilityFactory;
import br.ufrgs.MASBench.Helpers.Utility.UtilityFunction;
import br.ufrgs.MASBench.WorldModel.Entity;

/**
 * problem definition of the FGMD
 * 
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class ProblemDefinition {
	
	private UtilityFunction utilityFunction;
	private static HashMap<Integer, ArrayList<Entity>> instances = new HashMap<Integer, ArrayList<Entity>>();
	private static HashMap<Integer, ArrayList<Entity>> features = new HashMap<Integer, ArrayList<Entity>>();
	
	
	//<entidade, <index,domain>>
	private Map<Entity, Pair<Integer,Integer>> id2idxEntity = new HashMap<>();
	private Map<Integer, Integer> id2idxFeature = new HashMap<>();
	//classe,instancia,feature
	private double[][][] agentUtilityMatrix;
	
	private Assignment lastAssignment;
	
	public ProblemDefinition(HashMap<Integer,ArrayList<Entity>> inst, HashMap<Integer, ArrayList<Entity>> features2, Assignment lastAssignment)
	{
		
		this.lastAssignment = lastAssignment;
		
		long initialTime = System.currentTimeMillis();
		instances = inst;
		features = features2;
		utilityFunction = UtilityFactory.buildFunction();
        utilityFunction.setInstances(instances);
        buildAgentUtilityMatrix(this.lastAssignment);
        
        long elapsedTime = System.currentTimeMillis() - initialTime;
		
	}
	
	public HashMap<Integer,ArrayList<Entity>> getInstances()
	{
		return instances;
	}
	
	public HashMap<Integer, ArrayList<Entity>> getFeatures()
	{
		return features;
	}
	
	
	/**
     * Get the assignment selected in the last iteration
     * @return assignment selected in the last iteration
     */
    public Assignment getLastAssignment() {
        return lastAssignment;
    }
    
    public void setLastAssignment(Assignment assignment)
    {
    	this.lastAssignment = assignment;
    }
    
    private void buildAgentUtilityMatrix(Assignment assignment)
    {
    	
        final int nFeatures = features.keySet().size();
        final int nDimension = instances.keySet().size();
        final int nInstances = instances.get(0).size();
        
        agentUtilityMatrix = new double[nDimension][nInstances][nFeatures];
        
        for(Integer k : instances.keySet())
        {
        	
        	for(int i = 0; i < nInstances; i++)
        	{
        		Entity instance = instances.get(k).get(i);
        		if(!id2idxEntity.containsKey(instance))
        			id2idxEntity.put(instance,new Pair<Integer,Integer>(i,instance.getDimension()));
        		
        		for(Integer feature : features.keySet())
        		{
        			int j = 0;
        	        
        			if(!id2idxFeature.containsKey(feature))
        				id2idxFeature.put(feature, j++);
        			
        		}
        	}
        }
        
        for(Integer k : instances.keySet()){
        	for(Entity instance : instances.get(k)){
        		for(Integer feat : instance.getFeatureList().keySet()){
        			if(k != id2idxEntity.get(instance).second()){
        				double utility = utilityFunction.getUtility(instance,feat);
        				if (Double.isInfinite(utility)) {
                            utility = 1e15;
                        }
        				agentUtilityMatrix[id2idxEntity.get(instance).second()][id2idxEntity.get(instance).first()][id2idxFeature.get(feat)] = -utility;
        			}else{
        				double utility = utilityFunction.getUtility(instance,feat);
        				if (Double.isInfinite(utility)) {
                            utility = 1e15;
                        }
        				agentUtilityMatrix[k][id2idxEntity.get(instance).first()][id2idxFeature.get(feat)] = utility;
        			}
        		}
        	}
        }
        
        	     
    }
    
    public double getClassUtility(Integer feature)
    {
    	final int j = id2idxFeature.get(feature);
    	
    	double uJ0 = 0.0;
    	double uJ1 = 0.0;
    	
    	for(Integer k : instances.keySet())
    	{
    		for(Entity instance : features.get(feature))
    		{
    			int i = id2idxEntity.get(instance).first();
    			if(k == id2idxEntity.get(instance).second())
    			{
    				uJ0 += agentUtilityMatrix[k][i][j]; 
    			}else{
    				uJ1 += agentUtilityMatrix[k][i][j];
    			}
    		}
    	}
    	
    	return uJ1 - uJ0;   	
    	
    	
    }
    
    public double getClassUtility(Entity instance, Integer feature)
    {	
    	final int i;
    	final int j;
    	final int k;
    	i = id2idxEntity.get(instance).first();
    	j = id2idxFeature.get(feature);
    	k = id2idxEntity.get(instance).second();
    	
    	return agentUtilityMatrix[k][i][j];
    	
    }
    
    public int getNumAgents()
    {
    	int a = 0;
    	for(Integer k : instances.keySet())
    	{
    		a += instances.get(k).size();
    	}
    	return a;
    }
    
    public int getNumTargets()
    {
    	return features.size();
    }
    
    /**
     * Sorts the keys according to the doubles
     *
     * @param m - map
     * @return The sorted list
     */
    public static List<Integer> sortByValue(final Map<Integer, Double> m) {
        List<Integer> keys = new ArrayList<>();
        keys.addAll(m.keySet());
        Collections.sort(keys, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                Double v1 = m.get(o1);
                Double v2 = m.get(o2);
                if (v1 == null) {
                    return (v2 == null) ? 0 : 1;
                } else {
                    return -1 * v1.compareTo(v2);
                }
            }
        });
        return keys;
    }
    
    public Integer getHighestTargetForAgent(Entity agent) {
    	ArrayList<Integer> domain = new ArrayList<Integer>();
    	double best = Double.MIN_VALUE;
    	Integer candidate = null;
    	for(Integer j : agent.getFeatureList().keySet())
    	{
    		if(candidate == null)
    		{
    			candidate = j;
    			best = getClassUtility(agent,j);
    		}else if(getClassUtility(agent,j) > best)
    		{
    			best = getClassUtility(agent,j);
    			candidate = j;
    		}
    	}
    		
    	
        return candidate;
    }
    
    /**
     * returns the id of the highest feature of the agent
     * 
     * @param instance a given agent
     * @param candidateClasses list of features of the agent
     * @return id of the highest feature
     */
    public Integer getHighestTargetForAgent(Entity instance, ArrayList<Integer> candidateClasses) {
        double best = Double.MAX_VALUE;
        Integer candidate = null;
        for (Integer t : candidateClasses) {
            final double util = getClassUtility(instance, t);
            if (util > best) {
                best = util;
                candidate = t;
            }
        }
        return candidate;
    }
    
    private Map<Entity, List<Integer>> acceptedNeighbors = new HashMap<>();
    private Map<Integer, HashMap<Integer,ArrayList<Entity>>> acceptedEntities = new HashMap<>();
    private void pruneProblem() {
        final int maxAllowedNeighbors = 4;
        System.out.println("Pruning problem down to " + maxAllowedNeighbors + " max neighbors.");

        // Create and sort a list of edges
        ArrayList<AgentClassCost> edges = new ArrayList<>();
        for(Integer k : instances.keySet()){
	        for (Entity instance : instances.get(k)) {
	            for (Integer feature : features.keySet()) {
	                edges.add(new AgentClassCost(instance, feature, k));
	            }
	        }
        }
        Collections.sort(edges, Collections.reverseOrder());
        //Collections.shuffle(edges, config.getRandom());

        // Boilerplate: initialize the map of accepted neighbors to avoid creating lists within
        // the following loop
        for(Integer k : instances.keySet()){
        	for (Entity instance : instances.get(k)) {
            	acceptedNeighbors.put(instance, new ArrayList<Integer>());
        	}
        }
        
        for (Integer feature : features.keySet()) {
        	HashMap<Integer, ArrayList<Entity>> featureMap = new HashMap<Integer,ArrayList<Entity>>();
        	for(Integer k : instances.keySet())
        	{
        		featureMap.put(k, new ArrayList<Entity>());
        	}
        	acceptedEntities.put(feature, featureMap);
        }
        // Pick them in order so long as neither the degree of the agent nor the degree of the fire
        // would be higher than what is allowed.
        for (AgentClassCost edge : edges) {
            if (acceptedNeighbors.get(edge.instance).size() >= maxAllowedNeighbors) {
                continue;
            }
            for(Integer k : acceptedEntities.get(edge.feature).keySet())
            {
            	if(acceptedEntities.get(edge.feature).get(k).size() >= maxAllowedNeighbors)
            		continue;            	
            }
            acceptedNeighbors.get(edge.instance).add(edge.feature);
            acceptedEntities.get(edge.feature).get(edge.dim).add(edge.instance);
        }

        // Report unassigned agents/fires
        int nEmptyAgents = 0, nEmptyClasses = 0;
        for(Integer k : instances.keySet())
        {
        	for (Entity agent : instances.get(k)) {
        		if (acceptedNeighbors.get(agent).isEmpty()) {
                	nEmptyAgents++;
            	}
        	}
        }
        for (Integer feature : features.keySet()) {
        	for(Integer k : acceptedEntities.get(feature).keySet()){
	            if (acceptedEntities.get(feature).get(k).isEmpty()) {
	                nEmptyClasses++;
	            }
        	}
        }
        if (nEmptyAgents > 0 || nEmptyClasses > 0) {
            System.out.println("There are "+nEmptyAgents+" unlinked agents and "+nEmptyClasses+" unlinked classes.");
        }
    }
    
    /**
     * Helper class to facilitate problem prunning by sorting the Fire-FireAgent pairs according
     * to their unary utilities.
     */
    private class AgentClassCost implements Comparable<AgentClassCost> {
        public final Entity instance;
        public final Integer feature;
        public final Integer dim;
        public final double cost;

        public AgentClassCost(Entity instance, Integer feature, int k) {
            this.instance = instance;
            this.feature = feature;
            this.dim = k;
            
            this.cost = agentUtilityMatrix[id2idxEntity.get(instance).first()][id2idxFeature.get(feature)][id2idxEntity.get(instance).second()];
        }

        @Override
        public int compareTo(AgentClassCost o) {
            final int result = Double.compare(cost, o.cost);
            if (result == 0) {
                return Integer.compare(instance.hashCode(), o.instance.hashCode());
            }
            return result;
        }
    }
    
    
	
}
