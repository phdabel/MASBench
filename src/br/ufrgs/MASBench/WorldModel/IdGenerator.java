package br.ufrgs.MASBench.WorldModel;

/**
 * Id Generator
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class IdGenerator {
	
private static IdGenerator instance;
	
	private Integer id = 0;
	
	private IdGenerator(){
		
	}
	
	public static IdGenerator getInstance(){
		if(instance == null)
		{
			instance = new IdGenerator();
		}
		return instance;
	}
	
	/**
	 * return new id for objects
	 *  
	 * @return Integer
	 */
	public Integer getId(){
		return this.id++;
	}
	
	/**
	 * return total of objects
	 * 
	 * @return Integer
	 */
	public Integer totalObjects(){
		return this.id;
	}

}
