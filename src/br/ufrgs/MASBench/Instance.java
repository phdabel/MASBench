package br.ufrgs.MASBench;

import java.util.HashMap;

public class Instance extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6441316659849741668L;
	
	private HashMap<Entity, Double> featureList;
	
	public Instance()
	{
		super();
		this.featureList = new HashMap<Entity,Double>();
	}
	
	public Instance(int id)
	{
		super(id);
		this.featureList = new HashMap<Entity,Double>();
	}
	
	public Instance(int id, int dimension)
	{
		super(id, dimension);
		this.featureList = new HashMap<Entity,Double>();
	}
	
	public void addFeatureValue(Entity j, Double v)
	{
		this.featureList.put(j, v);
	}
	
	public HashMap<Entity,Double> getFeatureList()
	{
		return this.featureList;
	}

}
