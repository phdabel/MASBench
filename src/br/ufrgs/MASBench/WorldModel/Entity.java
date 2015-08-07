package br.ufrgs.MASBench.WorldModel;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Formation of Groups by Minimization of the Divergences Entity
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class Entity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private int dimension;
	
	private HashMap<Integer, Double> featureList;	
		
	/**
	 * <ul>
	 * <li>constructor generates a new id number to each entity</li>
	 * <li>id number follows an order</li>
	 * <li>there is no two entities with the same id</li>
	 * </ul>
	 */
	public Entity(){
		this.setId(IdGenerator.getInstance().getId());
		this.featureList = new HashMap<Integer,Double>();
	}
	
	/**
	 * <ul>
	 * <li>generates a new Entity with the specified id</li>
	 * <li>usually, this constructor is used to search objects in list</li>
	 * </ul>
	 * @param id of the new entity
	 */
	public Entity(Integer id)
	{
		this.setId(id);
		this.featureList = new HashMap<Integer,Double>();
	}
	
	public Entity(Integer id, Integer dim)
	{
		this.setId(id);
		this.setDimension(dim);
		this.featureList = new HashMap<Integer,Double>();
	}
	
	public void addFeatureValue(Integer j, Double v)
	{
		this.featureList.put(j, v);
	}
	
	public HashMap<Integer,Double> getFeatureList()
	{
		return this.featureList;
	}
	
	/**
	 * <ul>
	 * <li>returns the id of the entity</li>
	 * </ul>
	 * @return id of the entity
	 */
	public int getId() {
		return id;
	}

	/**
	 * <ul>
	 * <li>set the id of the entity</li>
	 * </ul>
	 * @param id of the entity
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	public int getDimension()
	{
		return this.dimension;
	}
	
	public void setDimension(int dim)
	{
		this.dimension = dim;
	}
	
	/**
	 * <ul>
	 * <li>returns the hashCode of the entity</li>
	 * <li>the same id</li>
	 * </ul>
	 */
	public int hashCode()
	{
		return this.id;
	}
	
	/**
	 * compare two entities
	 */
	public boolean equals(Object obj)
	{
		if(obj instanceof Entity && ((Entity)obj).equals(this.id))
			return true;
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(this.getClass() != obj.getClass())
			return false;
		Entity other = (Entity)obj;
		if(this.getId() != other.getId())
			return false;
		
		return true;
	}

}
