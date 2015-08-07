package br.ufrgs.MASBench.Solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.ufrgs.MASBench.Constants;
import br.ufrgs.MASBench.Assignment.DCOP.DCOPAgent;
import br.ufrgs.MASBench.Comm.CommunicationLayer;
import br.ufrgs.MASBench.Comm.Message;
import br.ufrgs.MASBench.WorldModel.Entity;
import br.ufrgs.MASBench.utils.Reader.Transmitter;

/**
 * DCOP Solver
 */
public abstract class DCOPSolver extends AbstractSolver {
	
	/**
     * The number of iterations max that an algorithm can perform before the
     * agents take a definitive decision for each timestep.
     */
    public static final String KEY_DCOP_ITERATIONS = "dcop.iterations";

    /** Configuration key to enable/disable usage of anytime assignments. */
    public static final String KEY_ANYTIME = "dcop.anytime";

    /**
     * Configuration key to enable/disable the sequential greedy correction of
     * assignments.
     */
    public static final String KEY_GREEDY_CORRECTION = "dcop.greedy_correction";

    private static final Logger Logger = LogManager.getLogger(DCOPSolver.class);
    private List<DCOPAgent> agents;
    private List<Double> utilities;

    public DCOPSolver() {
        utilities = new ArrayList<>();
    }

    @Override
    public List<String> getUsedConfigurationKeys() {
        List<String> keys = super.getUsedConfigurationKeys();
        keys.add(KEY_DCOP_ITERATIONS);
        keys.add(KEY_ANYTIME);
        keys.add(KEY_GREEDY_CORRECTION);
        return keys;
    }

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMaxTime(int time) {
		// TODO Auto-generated method stub
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
		
        int MAX_ITERATIONS = Transmitter.getIntConfigParameter(Constants.CONFIG_FILE, KEY_DCOP_ITERATIONS);
        boolean done = false;
        int iterations = 0;
        Assignment finalAssignment = null, bestAssignment = null;
        double bestAssignmentUtility = Double.NEGATIVE_INFINITY;
        long iterationTime = System.currentTimeMillis();
        
        while (!done && iterations < MAX_ITERATIONS) {
            finalAssignment = new Assignment();

            // send messages
            for (DCOPAgent agent : agents) {
                Collection<? extends Message> messages = agent.sendMessages(comLayer);
                //collect the byte size of the messages exchanged between agents
                nMessages = nMessages + messages.size();
                for (Message msg : messages) {
                    bMessages += msg.getBytes();
                }
            }

            // receive messages
            for (DCOPAgent agent : agents) {
                agent.receiveMessages(comLayer.retrieveMessages(agent.getID()));
            }

            // try to improve assignment
            done = true;
            long nccc = 0;
            for (DCOPAgent agent : agents) {
                boolean improved = agent.improveAssignment();
                nccc = Math.max(nccc, agent.getConstraintChecks());
                done = done && !improved;

                // Collect assignment
                //finalAssignment.assign(agent.getID(), agent.getTarget());
            }

        
        }        
		return null;
	}
	
	/**
     * This method initializes the agents for the simulation (it calls the
     * initialize method of the specific DCOP algorithm used for the
     * computation)
     *
     * @param problem the problem definition.
     */
    protected void initializeAgents(ProblemDefinition problem) {
        agents = new ArrayList<>();
        final long startTime = System.currentTimeMillis();
        initializeAgentType(problem, problem.getInstances());
        Logger.debug("Initialized {} {} agents in {}ms.",
                agents.size(), getIdentifier(), System.currentTimeMillis() - startTime);
    }
    
    private void initializeAgentType(ProblemDefinition problem, HashMap<Integer,ArrayList<Entity>> ids) {
        for (Entity agentID : ids.get(1)) {
            DCOPAgent agent = buildAgent();
            // @TODO: if required give only local problem view to each agent!
            agent.initialize(agentID, problem);
            agents.add(agent);
        }
    }

	protected abstract DCOPAgent buildAgent();
	

	@Override
	public double getUtility(ProblemDefinition problem, Assignment solution) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Assignment solve(Assignment assignment, ProblemDefinition utility) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAssignment(Assignment assignment) {
		// TODO Auto-generated method stub
		
	}


}
