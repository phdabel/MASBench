package br.ufrgs.MASBench.Assignment.DCOP;

import java.util.Collection;

import br.ufrgs.MASBench.Comm.CommunicationLayer;
import br.ufrgs.MASBench.Comm.Message;
import br.ufrgs.MASBench.Solver.ProblemDefinition;
import br.ufrgs.MASBench.WorldModel.Entity;


public interface DCOPAgent {
	
	/**
	 * this method initializes the agent
	 * 
	 * @param agentID instance
	 * @param problem utility matrix
	 */
    public void initialize(Entity agentID, ProblemDefinition problem);

    /**
     * Considering all the messages received from other agents, tries to find
     * an improvement over the previous assignment of the agent.
     * @return true, if the assignment of this agent changed, false otherwise.
     */
    public boolean improveAssignment();

    /**
     * Returns the ID of the agent.
     * @return the ID of the agent.
     */
    public Entity getID();

    /**
     * Returns the ID  of the currently assigned target.
     * @return the ID of the target.
     */
    public Integer getTarget();

    /**
     * Sends a set of messages from an agent to all the recipients.
     * @param com: a communication simulator.
     * @return The set of messages that have been sent.
     */
    public Collection<? extends Message> sendMessages(CommunicationLayer com);

    /**
     * Receives a set of messages sent by some other agents.
     * @param messages: colletcion of messages received from other agents.
     */
    public void receiveMessages(Collection<Message> messages);

    /**
     * Returns the number of constraint checks performed during the latest
     * iteration.
     *
     * @return number of constraint checks
     */
    public long getConstraintChecks();

}
