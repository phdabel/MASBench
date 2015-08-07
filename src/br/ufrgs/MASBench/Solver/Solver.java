package br.ufrgs.MASBench.Solver;

import java.util.List;

public interface Solver {
	
	/**
     * Initializes the solver to operate on the specified world, and with the
     * specified config.
     */
	public void initialize();
    
    /**
     * Get the identifier of this solver.
     * 
     * The identifier should contain both the solver type and the particular
     * solving algorithm being employed. I.e.: <em>DCOP_MaxSum</em>
     * @return  string identifier of the solver
     */
    public String getIdentifier();
    
    
    /**
     * 
     * Solves the given problem description.
     * 
     * @param assignment a given instance, or a set of instances 
     * @param utility matrix of the domain
     * @return the distribution value of the instances given the assignment
     */
    public Assignment solve(Assignment assignment, ProblemDefinition utility);
    
    /**
     * Get the list of configuration keys employed by this solver.
     *
     * This list will be used to output the configuration values when writing
     * the results file.
     * @return list of configuration keys used by this solver.
     */
    public List<String> getUsedConfigurationKeys();
    
    
    /**
     * it can be a instance to be compared with the entire domain.
     * 
     * @param assignment possible solution
     */
    public void setAssignment(Assignment assignment);



}
