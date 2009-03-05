import java.io.FileInputStream;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Main class for simulating WIFI area network
 * 
 * @author Walter Dal Mut
 *
 */
public class Simulator 
{
	/** Veichles that exists */
//	private Vector<Veichle> veichles;
	
	/**
	 * Constructor for the area network
	 */
	public Simulator()
	{
		Security.addProvider( new BouncyCastleProvider() );
		
		try
		{			
			FileInputStream fis = new FileInputStream( "properties/base.properties" );
			System.out.println( ".:: Boostrap ::." );
			System.out.println( "Loading base properties" );
			Boostrap.getBaseProperties( fis );
			System.out.println( "Base properties loaded" );
			
			System.out.println( "Loading veichles configuration" );
			Boostrap.getVeichlesOnTheRoad( "veichles/veichles.xml" );
			System.out.println( "Veichles configuration loaded" );
			System.out.println( ".:: Boostrap end ::." );
			
		}
		catch (Exception e) 
		{
			System.out.println( "ERROR!!! Veichle file not propertly configured" );
		} 
	}
	
	/**
	 * Main fuction for add new veichle
	 * 
	 * @param args Command ARGS
	 */
	public static void main( String[] args )
	{
		new Simulator();
	}
}
