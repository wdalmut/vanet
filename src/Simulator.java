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
	/**
	 * Constructor for the simulator
	 * 
	 * This method load your configuration and upload the static utility class Configs
	 * with configuration picked up from files...
	 * 
	 * @see vanet.Configs
	 */
	public Simulator()
	{
		Security.addProvider( new BouncyCastleProvider() );
		
		try
		{			
			//TODO: check change certificate every time...
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
			System.exit(1);
		} 
	}
	
	/**
	 * Main fuction for launch the simulator
	 * 
	 * @param args Command ARGS -not used-
	 */
	public static void main( String[] args )
	{
		new Simulator();
	}
}
