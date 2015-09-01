package br.ufrgs.MASBench;

import java.util.HashMap;

public class Feature extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5635483259848026695L;
	
	//HashMap<instance,entityType>
	private HashMap<Entity, Integer> instanceList;
	
	public Feature()
	{
		super();
		this.instanceList = new HashMap<>();
	}
	
	public Feature(int id)
	{
		super(id);
		this.instanceList = new HashMap<Entity,Integer>();
	}
	
	public Feature(int id, int dimension)
	{
		super(id, dimension);
		this.instanceList = new HashMap<Entity,Integer>();
	}
	
	public void addInstance(Entity element, Integer entityType)
	{
		this.instanceList.put(element, entityType);
	}
	
	public HashMap<Entity, Integer> getInstanceList()
	{
		return this.instanceList;
	}
	
	

}
