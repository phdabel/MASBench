package br.ufrgs.MASBench;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.ufrgs.MASBench.BinaryMaxSum.BMS.CommunicationAdapter;

/**
 * Communication Adaptter
 * Tick adapter + damping factor
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class TickDampedCommunicationAdapter implements CommunicationAdapter<NodeID> {
	
	/** Threshold below which messages are considered equal. */
    public static final double EPSILON = 1e-5;
    
    /** The damping factor to use when sending messages */
    private final double DAMPING_FACTOR;
    
    private ArrayList<TickMessage> buffer;
    private Map<Pair<NodeID, NodeID>, Double> oldMessages;
    private boolean converged;
    
    public TickDampedCommunicationAdapter()
    {
    	DAMPING_FACTOR = 0.0;
    	buffer = new ArrayList<>();
        oldMessages = new HashMap<>();
        converged = true;
    }
    
    public Collection<TickMessage> flushMessages()
    {
    	Collection<TickMessage> result = buffer;
    	buffer = new ArrayList<>();
    	converged = true;
    	return result;
    }
    
    /**
     * Messages are buffered until the channel is ticked, when it delivers all
     * of the messages sent since the last tick.
     */
    public void tick() {
        
    }
    
    @Override
    public void send(double message, NodeID sender, NodeID recipient) {
    	if (Double.isNaN(message)) {
        	System.out.println("Factor "+ sender.toString() +" tried to send "+ message +" to factor "+recipient.toString()+"!");
            throw new RuntimeException("Invalid message sent!");
        	//message = 1.0;//Double.MAX_VALUE;
        }

        
        // The algorithm has converged unless there is at least one message
        // different from the previous iteration
        Pair<NodeID,NodeID> sr = new Pair<NodeID,NodeID>(sender, recipient);
        Double oldMessage = oldMessages.get(sr);
        
        if (oldMessage != null && !Double.isInfinite(message)) {
            message = oldMessage * DAMPING_FACTOR + message * (1 - DAMPING_FACTOR);
        }
        if (oldMessage == null || isDifferent(oldMessage, message)) {
            converged = false;
        }
        
        oldMessages.put(sr, message);

        buffer.add(new TickMessage(message, sender, recipient));
    }
    
    /**
     * Returns true if all the messages sent in the current iteration are
     * <em>equal</em> to the messages sent in the previous one.
     *
     * @see #EPSILON
     * @return true if the algorithm has converged, or false otherwise.
     */
    public boolean isConverged() {
        return converged;
    }

    private boolean isDifferent(double m1, double m2) {
        return Math.abs(m1 - m2) > EPSILON;
    }
    

}
