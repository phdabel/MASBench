package br.ufrgs.MASBench.Helpers.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import br.ufrgs.MASBench.WorldModel.Entity;

/**
 * Utility function of classification
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class ImprovemeUtility implements UtilityFunction {
	
	private static HashMap<Entity,Integer> world = new HashMap<Entity, Integer>();

	@Override
	public double getUtility(Entity instance, Integer feature) {
		
		
		if(instance.getFeatureList().containsKey(feature)){
			return instance.getFeatureList().get(feature)/this.sumFeature(instance);
		}else{
			return -this.averageFeature(instance);
		}
		
		
	}
	
	public double sumFeature(Entity instance)
	{
		double sum = 0d;
		for(Integer j : instance.getFeatureList().keySet())
		{
			sum += instance.getFeatureList().get(j);
		}
		
		return sum;
	}
	
	public double averageFeature(Entity instance)
	{
		double avg = 0d;
		for(Integer j : instance.getFeatureList().keySet())
		{
			avg += instance.getFeatureList().get(j);
		}
		
		return avg/instance.getFeatureList().keySet().size();
	}

	@Override
	public void setInstances(HashMap<Integer, ArrayList<Entity>> instances) {
		for(Integer k : instances.keySet())
		{
			for(Entity i : instances.get(k))
			{
				world.put(i, k);
			}
		}
	}

}
