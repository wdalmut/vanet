package log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

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
	public static void initLogger()
	{
		Properties logProperties = new Properties();
		try 
		{
			switch( Configs.logSystem )
			{
			case 0:
				logProperties.load(new FileInputStream("properties/mysql.properties"));
				break;
			case 1:
				logProperties.load(new FileInputStream("properties/file.properties"));
				String date = new Date().toString();
				date = date.replace(' ', '_');
				date = date.replace(':', '_');
				logProperties.setProperty("log4j.appender.R.File", "logs/"+date+".log");
				break;
			case 2:
				logProperties.load(new FileInputStream("properties/stdout.properties"));
				break;
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		PropertyConfigurator.configure(logProperties);
	}
}
