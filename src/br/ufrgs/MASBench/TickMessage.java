package br.ufrgs.MASBench;

import br.ufrgs.MASBench.Comm.Message;

public class TickMessage implements Message {
	
	public final double value;
    public final NodeID sender;
    public final NodeID recipient;
    
    public TickMessage(double value, NodeID sender, NodeID recipient) {
        this.value = value;
        this.sender = sender;
        this.recipient = recipient;
    }
    
    @Override
	public int getBytes() {
        return Message.BYTES_UTILITY_VALUE;
    }

}
