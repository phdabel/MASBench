package br.ufrgs.MASBench.Solver;

import java.util.ArrayList;

import br.ufrgs.MASBench.FGMD.NodeID;

/**
 * Used to represent some value to be evaluated 
 * 
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class Assignment {
		
	private ArrayList<NodeID> scope;
	private Integer dimension;
	
	/**
     * Build a new agents assignments
     */
    public Assignment() {
        scope = new ArrayList<NodeID>();
    }
    
    public Integer getDomain()
    {
    	return this.dimension;
    }
    
    public ArrayList<NodeID> getScope()
    {
    	return this.scope;
    }
    
    public void setDimension(Integer k)
    {
    	this.dimension = k;
    }
    
    public void setScope(NodeID query)
    {
    	this.scope.add(query);
    }
    
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n Scope:");
        for(NodeID node : this.scope)
        {
        	buffer.append("Node: ").append(node.toString());
        }
        buffer.append("Node Degree: ").append(this.dimension);
        
        return buffer.toString();
    }
    

}
