package vanet;

/**
 * Utility class for configurations.
 * 
 * This class contains all configuration of your simulator
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public class Configs 
{
	/**
	 * Cover of WIFI 802.11 for computing map
	 */
	public static double wifiCover = 200D;
	/**
	 * Max veichle speed in m/s
	 */
	public static double maxVeichleSpeed = 140D/3.6D;
	
	/**
	 * Address of broadcast server
	 */
	public static String SERVER_BROADCAST_ADDRESS = "127.255.255.255";
	
	/**
	 * Listening port
	 */
	public static int PORT = 55055; 
	
	/**
	 * Beacons/seconds
	 */
	public static double BEACONS_SEC = 0.1;
	
	/**
	 * Lock moves of veichle for simulate only transmission and security implementation
	 */
	public static boolean NO_MOVES = false;
	
	/**
	 * Choose your simulator.
	 * 
	 * Values:
	 * bp -> BaseLine Pseudonyms
	 * gs -> Group Signature
	 */
	public static String SIMULATOR = "bp";
	
	/**
	 * Length of payload in bytes
	 */
	public static final int PAYLOAD_LENGTH = 200;
	
	//TODO: config file add this configurations
	/**
	 * This parameter force the certificate reattach every tot beacons
	 */
	public static int REATTACH_CERTIFICATE = 15;
	
	/**
	 * Max validity of certificate into wireless area
	 */
	public static int MAX_CERTIFICATE_VALIDITY_TIME = 5;
}
