package br.ufrgs.MASBench.Solver;

import java.util.Collection;

import br.ufrgs.MASBench.Comm.CommunicationLayer;
import br.ufrgs.MASBench.Comm.Message;
import br.ufrgs.MASBench.WorldModel.Entity;

/**
 * external agent (it can be used to represent the environment)
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public interface ExternalAgent {
	
	public void initialize(Integer id, ProblemDefinition problem);
	
	public boolean improveAssignment();
	
	public Integer getID();
	
	public Entity getTarget();
	
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
