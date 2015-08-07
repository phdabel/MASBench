package br.ufrgs.MASBench.FGMD;


import br.ufrgs.MASBench.Solver.Assignment;
import br.ufrgs.MASBench.Solver.ProblemDefinition;
import br.ufrgs.MASBench.Solver.Classification.ClassificationSolver;
import br.ufrgs.MASBench.WorldModel.Entity;
import br.ufrgs.MASBench.WorldModel.ClassificationModel.FeatureAgent;
import br.ufrgs.MASBench.WorldModel.ClassificationModel.InstanceAgent;

/**
 * Formation of Groups by Minimization of the Divergences
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class FGMD extends ClassificationSolver {
	
	/**
     * The damping factor to employ.
     */
    public static final String KEY_MAXSUM_DAMPING = "maxsum.damping";

	@Override
	public String getIdentifier() {
		return "FGDMD - Formation of Groups by Minimization of Divergences";
	}

	@Override
	public void setAssignment(Assignment assignment) {
		
	}

	@Override
	protected InstanceAgent buildAgent(Entity id) {		
		return new InstanceAgent(id);
	}

	@Override
	protected FeatureAgent buildAgent(Integer id) {
		return new FeatureAgent(id);
	}
	
	@Override
	public double getUtility(ProblemDefinition problem, Assignment solution) {
		double u = 0;
		for(Integer feature : problem.getFeatures().keySet())
		{
			u += problem.getClassUtility(feature);
		}
		return u/problem.getFeatures().size();
	}



}
