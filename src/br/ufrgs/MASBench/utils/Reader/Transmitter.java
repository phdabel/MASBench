package br.ufrgs.MASBench.utils.Reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * File Reader
 * 
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public class Transmitter {

	static FileInputStream file;
	/**
	 * <ul>
	 * <li>load the file with properties</li>
	 * </ul>
	 * @param filename property file to be read
	 * @return property class
	 * @throws IOException exception
	 */
	protected static Properties getProp(String filename) throws IOException {
		
		Properties props = new Properties();
		file = new FileInputStream("./properties/"+filename);
		
		props.load(file);
		return props;
		
	}
	
	/**
	 * returns the specified property from the specified file
	 * 
	 * @param filename property file to be read
	 * @param property string
	 * @return boolean value of the property
	 */
	public static Boolean getBooleanConfigParameter(String filename, String property)
	{
		try{
			return new Boolean(getProp(filename).getProperty(property));
		}catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * returns the specified property from the specified file
	 * 
	 * @param filename property file
	 * @param property string
	 * @return double value of the property
	 */
	public static Double getDoubleConfigParameter(String filename, String property)
	{
		try{
			return new Double(getProp(filename).getProperty(property));
		}catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * returns the specified property from the specified file
	 * 
	 * @param filename property file
	 * @param property string
	 * @return int value of the property
	 */
	public static Integer getIntConfigParameter(String filename, String property)
	{
		try{
			return new Integer(getProp(filename).getProperty(property));
		}catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
		
	/**
	 * returns the specified property from the specified file
	 * 
	 * @param filename property file
	 * @param property string
	 * @return string value
	 */
	public static String getProperty(String filename, String property)
	{
		try{
			return getProp(filename).getProperty(property);
		}catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * <ul>
	 * <li>print a message of the specified file of properties</li>
	 * </ul>
	 * @param filename property file
	 * @param property string
	 */
	public static void message(String filename, String property)
	{
		try {
			System.out.println(getProp(filename).getProperty(property));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
