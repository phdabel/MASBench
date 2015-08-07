package br.ufrgs.MASBench;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.ufrgs.MASBench.Helpers.Utility.UtilityFactory;
import br.ufrgs.MASBench.Solver.Assignment;
import br.ufrgs.MASBench.Solver.ProblemDefinition;
import br.ufrgs.MASBench.Solver.Solver;
import br.ufrgs.MASBench.WorldModel.Entity;
import br.ufrgs.MASBench.WorldModel.ClassificationModel.FeatureAgent;
import br.ufrgs.MASBench.WorldModel.ClassificationModel.InstanceAgent;
import br.ufrgs.MASBench.utils.Reader.Transmitter;

/**
 * Center Agent, the one that executes the optimization.
 * 
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class CenterAgent {
	
	private static final Logger Logger = LogManager.getLogger(CenterAgent.class);
	
	/** Base config key to solver configurations */
    public static final String CONF_KEY_SOLVER= "solver";

    /** Config key of a solver class to run */
    public static final String CONF_KEY_CLASS = "class";

    /** Config key to the maximum time allowed for the main solver */
    public static final String CONF_KEY_TIME = "time";
    
    private static HashMap<Integer,ArrayList<Entity>> instances = new HashMap<>();
	private static HashMap<Integer, ArrayList<Entity>> features = new HashMap<>();
	private Solver solver = null;
	private Assignment assignment = new Assignment();
	private ProblemDefinition problem = null;
    
    public CenterAgent(ArrayList<InstanceAgent>instances, ArrayList<FeatureAgent> features)
    {
    	this.loadWorldModel();	
    }
    
    public CenterAgent()
    {
    	this.setProblem();
    	String utilityClass = Transmitter.getProperty(Constants.CONFIG_FILE, Constants.KEY_UTILITY_CLASS);
		UtilityFactory.setClass(utilityClass);
		this.solver = buildSolver();
		this.solver.initialize();
    }
    
    public void setProblem()
    {
    	instances.put(0, new ArrayList<Entity>());
    	Entity q1 = new Entity(1);
    	q1.addFeatureValue(1, 2.0);
    	q1.addFeatureValue(2, 3.0);
    	q1.setDimension(1);
    	instances.put(1, new ArrayList<Entity>());
    	instances.get(1).add(q1);
    	Entity q2 = new Entity(2);
    	q2.addFeatureValue(2, 2.0);
    	q2.addFeatureValue(3, 3.0);
    	q2.setDimension(1);
    	instances.get(1).add(q2);
    	features.put(1, new ArrayList<Entity>());
    	features.get(1).add(q1);
    	features.put(2, new ArrayList<Entity>());
    	features.get(2).add(q1);
    	features.get(2).add(q2);
    	features.put(3, new ArrayList<Entity>());
    	features.get(3).add(q2);
    	
    	
    	
    }
    
    //método que resolve
    public void think()
    {
    	//this.solver.solve(1, utility);
    }
    
    
    public String toString()
    {
        return "Center Agent";
    }
    
    private void loadWorldModel()
    {
    	this.initializeParameters();
    	solver = buildSolver();
    }
    
    private void initializeParameters() {
        
        // Set the utility function to use
        String utilityClass = Transmitter.getProperty(Constants.CONFIG_FILE, Constants.KEY_UTILITY_CLASS);
        UtilityFactory.setClass(utilityClass);

    }
    
    private Solver buildSolver()
	{
		this.solver = buildSolver(
                Transmitter.getProperty(Constants.CONFIG_FILE,CONF_KEY_SOLVER + "." + CONF_KEY_CLASS));
		
		return this.solver;
	}
    
    private Solver buildSolver(String clazz)
	{
		try {
            Class<?> c = Class.forName(clazz);
            Object s = c.newInstance();
            if (s instanceof Solver) {
                Solver newSolver = (Solver)s;
                return newSolver;
            }

        } catch (ClassNotFoundException ex) {
            Logger.fatal("Solver class {} not found!", ex.getMessage());
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.fatal("Unable to instantiate solver {}", ex);
        }

        System.exit(1);
        return null;
	}
    

}
