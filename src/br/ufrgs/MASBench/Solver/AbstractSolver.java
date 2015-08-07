package br.ufrgs.MASBench.Solver;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.ufrgs.MASBench.Constants;
import br.ufrgs.MASBench.Solver.Operator.MaxOperator;
import br.ufrgs.MASBench.Solver.Operator.Maximize;

/**
 * Abstract Solver similar to the RMASBench abstract solver
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public abstract class AbstractSolver implements Solver {
	
	private static final Logger Logger = LogManager.getLogger(AbstractSolver.class);
	
	protected long maxTime = 300000;
	public MaxOperator operator = new Maximize();
	
	@Override
	public void initialize() {
       
		
        Logger.info("Solver {} initialized.", getIdentifier());
    }
			
	public void setMaxTicks(int maxTime)
	{
		this.maxTime = maxTime;		
	}
	
	public int getMaxTime() {
        return (int)maxTime;
    }
	
	public abstract Assignment compute(ProblemDefinition problem);
	
	public Assignment solve(Assignment assignment, ProblemDefinition problem)
	{
		final long start = System.currentTimeMillis();
        Assignment solution = compute(problem);
        long cputime = System.currentTimeMillis() - start;
        
        return solution;
	}
	
	@Override
    public List<String> getUsedConfigurationKeys() {
        List<String> keys = new ArrayList<>();
        keys.add(Constants.KEY_AGENT_ONLY_ASSIGNED);
        keys.add(Constants.KEY_UTILITY_CLASS);
        keys.add(Constants.KEY_UTIL_HYSTERESIS);
        keys.add(Constants.KEY_PROBLEM_PRUNE);
        return keys;
    }
	
	/**
	 * 
	 * Get the utility obtained by the given solution.
     * This utility function must be defined at the solver level.
     * It can be modeled to represent the distribution probability of the solution given the problem
	 * 
	 * @param problem definition of the problem
	 * @param solution instance to evaluate
	 * @return the utility of the problem given the assignment 
	 */
	public abstract double getUtility(ProblemDefinition problem, Assignment solution);
	

}
