
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vanet.Configs;
import vanet.Veichle;

/**
 * Use boostrap class for create the simulator
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public class Boostrap 
{
	
	/**
	 * Load base system. All information are picked up from file or generic input stream
	 * 
	 * @param in Properties which you want load.
	 */
	public static void getBaseProperties( InputStream in )
	{
		if( in == null  )
			throw new NullPointerException( "InputStream is null" );
		try
		{
			Properties properties = new Properties();
			properties.load( in );
			
			if( properties.get( "no_moves").equals("true") )
				Configs.NO_MOVES = true;
			else
				Configs.NO_MOVES = false;
			
			String vS;
			if( (vS = (String)properties.get( "max_speed" )) != null )
				Configs.maxVeichleSpeed = Double.valueOf(vS).doubleValue();
			if( (vS = (String)properties.getProperty( "wifi_cover" )) != null )
				Configs.wifiCover = Double.valueOf(vS).doubleValue();
			if( (vS = (String)properties.getProperty( "server_broadcast_point" )) != null )
				Configs.SERVER_BROADCAST_ADDRESS = vS;
			if( (vS = (String)properties.getProperty( "server_port" )) != null )
				Configs.PORT = Integer.valueOf( vS ).intValue();
			if( (vS = (String)properties.getProperty( "beacons_sec" )) != null )
				Configs.BEACONS_SEC = Double.valueOf( vS ).doubleValue();
			if( (vS = (String)properties.getProperty( "simulator" )) != null )
				Configs.SIMULATOR = vS;
			if( (vS = (String)properties.getProperty( "maxCertificateValidityTime" )) != null )
				Configs.MAX_CERTIFICATE_VALIDITY_TIME = Integer.valueOf( vS ).intValue();
			if( (vS = (String)properties.getProperty( "reattachCertificate" )) != null )
				Configs.REATTACH_CERTIFICATE = Integer.valueOf( vS ).intValue();
			
			properties = null;	//to garbage collector 
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This function retrive veichle on the road
	 * 
	 * @param dom The DOM object for analyze it
	 * 
	 * @return A Stack of veichle
	 * 
	 */
	private static Stack<Veichle> getVeichlesOnTheRoad( Document dom )
	{
		if( dom == null )
			throw new NullPointerException( "Impossibile to parse file" );
		
		Stack<Veichle> veichles = new Stack<Veichle>();
		
		NodeList veichlesNode = dom.getElementsByTagName("Veichle");
		for( int i=0; i<veichlesNode.getLength(); i++ )
		{
			Node node = veichlesNode.item(i);
			int speed = Integer.valueOf(node.getAttributes().getNamedItem("speed").getFirstChild().getNodeValue()).intValue();
			int x = Integer.valueOf(node.getAttributes().getNamedItem("x").getFirstChild().getNodeValue()).intValue();
			int y = Integer.valueOf(node.getAttributes().getNamedItem("y").getFirstChild().getNodeValue()).intValue();
			int id = Integer.valueOf(node.getAttributes().getNamedItem("id").getFirstChild().getNodeValue()).intValue();
			
			try
			{
				veichles.push( new Veichle( id, speed, x, y ) );
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return veichles;
	}
	
	/**
	 * This function fill road with veichle configuration file in XML
	 * 
	 * @param url The position of XML file which contains veichles data
	 * 
	 * @return A Stack of veichle Object
	 * 
	 * @throws ParseException In case of parsing problem 
	 * 
	 */
	public static Stack<Veichle> getVeichlesOnTheRoad( String url ) throws ParseException
	{
		Document doc;
		Stack<Veichle> v;
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse( url );
			v = getVeichlesOnTheRoad( doc );
		} 
		catch (Exception e) 
		{
			throw new ParseException( e.getMessage(), -1 );
		}

		return v;
	}
}
