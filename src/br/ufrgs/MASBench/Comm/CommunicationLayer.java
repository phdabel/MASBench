package br.ufrgs.MASBench.Comm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrgs.MASBench.WorldModel.Entity;

/**
 * Communication Layer with two types of messages
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class CommunicationLayer {
	
	Map<Entity, List<Message>> messageInboxes;
	Map<Integer, List<Message>> featureMessageInboxes;

    /**
     * Build a new communication layer.
     */
    public CommunicationLayer() {
        messageInboxes = new HashMap<>();
        featureMessageInboxes = new HashMap<>();
    }

    /**
     * This method memorizes a message in the messageInbox of the recipient.
     *
     * @param agentID: the id of the recipient
     * @param message: the message
     */
    public void send(Entity agentID, Message message) {
        // Fetch the inbox, creating it if it doesn't exist yet
        List<Message> inbox = messageInboxes.get(agentID);
        if (inbox == null) {
            inbox = new ArrayList<>();
            messageInboxes.put(agentID, inbox);
        }

        inbox.add(message);
    }
    
    public void send(Integer feature, Message message){
    	List<Message> inbox = featureMessageInboxes.get(feature);
    	if(inbox == null)
    	{
    		inbox = new ArrayList<>();
    		featureMessageInboxes.put(feature, inbox);
    	}
    }

    /**
     * This method memorizes a series of messages in the messageInbox of the
     * recipient
     * 
     * @param agentID id of the recipient agent
     * @param messages list of messages
     */
    public void send(Entity agentID, Collection<Message> messages) {
        for (Message message : messages) {
            send(agentID, message);
        }
    }
    
    public void send(Integer agentId, Collection<Message> messages)
    {
    	for(Message message : messages)
    	{
    		send(agentId, message);
    	}
    }

    /**
     * This method retrieves the messages from the inbox of an agent
     *
     * @param agentID: the id of the recipient
     * @return a list of alla the messages received
     */
    public List<Message> retrieveMessages(Entity agentID) {
        List<Message> mesageInbox = messageInboxes.remove(agentID);
        if (mesageInbox == null) {
            mesageInbox = new ArrayList<>();
        }
        return mesageInbox;
    }
    
    public List<Message> retrieveMessages(Integer agent)
    {
    	List<Message> messageInbox = featureMessageInboxes.remove(agent);
    	if(messageInbox == null)
    	{
    		messageInbox = new ArrayList<>();
    	}
    	
    	return messageInbox;
    }

}
