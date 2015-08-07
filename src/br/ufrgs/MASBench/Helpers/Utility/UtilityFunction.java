package br.ufrgs.MASBench.Helpers.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import br.ufrgs.MASBench.WorldModel.Entity;

/**
 * Interface utilityFunction based on the interface of RMASBench
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public interface UtilityFunction {
		
	/**
	 * get the value of the feature given the instance (agent)
	 * @param instance represented by one agent
	 * @param feature of the agent
	 * @return value of the feature
	 */
	public double getUtility(Entity instance, Integer feature);

	public void setInstances(HashMap<Integer, ArrayList<Entity>> instances);

}
