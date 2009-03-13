package log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import vanet.Configs;

public class Log 
{
	private static Connection conn;
	private static Statement stmt;
	
	private static boolean noLog = false;
	
	private static LogLevel minLevel = LogLevel.CRITICAL;
	
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
	
	private static void log( LogLevel level, Object object, String method, String message )
	{
		if ( noLog )
			return;
		
		if( conn == null )
			initLogger();
		
		if( level.compareTo( minLevel ) < 0 )
		{
			System.out.println( level + " " + minLevel );
			return;
		}
		
		try
		{
			stmt = conn.createStatement();	
			stmt.executeUpdate("INSERT INTO logs ( level, class_name, method_name, message ) VALUES ( '"+level+"', '"+object.getClass().getName()+"', '"+method+"', '"+message+"')");
			stmt.close();
		}
		catch( Exception e )
		{
			//TODO: to do
			e.printStackTrace();
		}
	}
	
	public static void debug( Object object, String method, String message )
	{
		log( LogLevel.DEBUG, object, method, message );
	}
	
	public static void finest( Object object, String method, String message  )
	{
		log( LogLevel.FINEST, object, method, message );
	}
	
	public static void fine( Object object, String method, String message )
	{
		log( LogLevel.FINE, object, method, message );
	}
	
	public static void warning( Object object, String method, String message )
	{
		log( LogLevel.WARNING, object, method, message );
	}
	
	public static void critical( Object object, String method, String message )
	{
		log( LogLevel.CRITICAL, object, method, message );
	}
	
	public static void high( Object object, String method, String message )
	{
		log( LogLevel.HIGH, object, method, message );
	}
	
	public static void highest( Object object, String method, String message )
	{
		log( LogLevel.HIGHEST, object, method, message );
	}
}
