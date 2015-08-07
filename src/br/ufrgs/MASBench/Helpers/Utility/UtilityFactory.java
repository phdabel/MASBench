package br.ufrgs.MASBench.Helpers.Utility;


public final class UtilityFactory {

	@SuppressWarnings("rawtypes")
	private static Class utilityFunctionClass;
    
    public static UtilityFunction buildFunction() {
    	
    	try {
            Object instance = utilityFunctionClass.newInstance();
            if (instance instanceof UtilityFunction) {
                return (UtilityFunction)instance;
            }
            //Logger.fatal("The specified utility function is not a UtilityFunction!");
        } catch (InstantiationException | IllegalAccessException ex) {
            //Logger.fatal("Unable to instantiate the utility function.", ex);
        }
        System.exit(1);
        return null;
    }

    /**
     * Set the class name of the utility function to use.
     * 
     * @param utilityClassName utility class to use.
     */
    public static void setClass(String utilityClassName) {
        try {
            utilityFunctionClass = Class.forName(utilityClassName);
        } catch (ClassNotFoundException ex) {
            //Logger.fatal("Unable to find class " + utilityClassName, ex);
        }
    }
	
}
