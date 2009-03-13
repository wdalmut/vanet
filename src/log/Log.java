package log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import vanet.Configs;

/**
 * Log class for understand Vanet Simulator comportaments
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public class Log 
{
	/** Connection to DBMS */
	private static Connection conn;
	/** Statament for DBMS */
	private static Statement stmt;
	/** Remove logging system */
	private static boolean noLog = false;
	/** Minimum log level */
	private static LogLevel minLevel = LogLevel.CRITICAL;
	
	/**
	 * Initialize log system
	 */
	public static void initLogger()
	{
		minLevel = Configs.logDebug;
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://"+Configs.host+"/"+Configs.database+"?" + "user="+Configs.username+"&password="+Configs.password);
		}
		catch( Exception e )
		{
			noLog = true;
			System.out.println( "WARINING - no database for logging" );
		}
	}
	
	/**
	 * Main log function
	 * 
	 * @param level Level which you want log
	 * @param object The object which log
	 * @param method The method which log
	 * @param message The message which you want log
	 */
	private static void log( LogLevel level, Object object, String method, String message )
	{
		if ( noLog )
			return;
		
		if( conn == null )
			initLogger();
		
		if( level.compareTo( minLevel ) > 0 )
		{
			return;
		}
		
		try
		{
			stmt = conn.createStatement();	
			stmt.executeUpdate("INSERT INTO logs ( level, class_name, method_name, message ) VALUES ( '"+level+"', '"+object.getClass().getName()+"', '"+method+"', '"+message+"')");
		}
		catch( Exception e )
		{
			//TODO: to do
			e.printStackTrace();
		}
	}
	
	/**
	 * Debug log
	 * 
	 * @param object The object which log
	 * @param method The method which log
	 * @param message The message which you want log
	 */
	public static void debug( Object object, String method, String message )
	{
		log( LogLevel.DEBUG, object, method, message );
	}
	
	/**
	 * Finest log information
	 * 
	 * @param object The object which log
	 * @param method The method which log
	 * @param message The message which you want log
	 */
	public static void finest( Object object, String method, String message  )
	{
		log( LogLevel.FINEST, object, method, message );
	}
	
	/**
	 * Fine log information
	 * 
	 * @param object The object which log
	 * @param method The method which log
	 * @param message The message which you want log
	 */
	public static void fine( Object object, String method, String message )
	{
		log( LogLevel.FINE, object, method, message );
	}
	
	/**
	 * Warning log information
	 * 
	 * @param object The object which log
	 * @param method The method which log
	 * @param message The message which you want log
	 */
	public static void warning( Object object, String method, String message )
	{
		log( LogLevel.WARNING, object, method, message );
	}
	
	/**
	 * Critical log information
	 * 
	 * @param object The object which log
	 * @param method The method which log
	 * @param message The message which you want log
	 */
	public static void critical( Object object, String method, String message )
	{
		log( LogLevel.CRITICAL, object, method, message );
	}
	
	/**
	 * High log information
	 * 
	 * @param object The object which log
	 * @param method The method which log
	 * @param message The message which you want log
	 */
	public static void high( Object object, String method, String message )
	{
		log( LogLevel.HIGH, object, method, message );
	}
	
	/**
	 * HIGHEST log information
	 * 
	 * @param object The object which log
	 * @param method The method which log
	 * @param message The message which you want log
	 */
	public static void highest( Object object, String method, String message )
	{
		log( LogLevel.HIGHEST, object, method, message );
	}
}
