package vanet;

public class Moves 
{		
	public static void moveMe( Position actualPosition )
	{
		if( Configs.NO_MOVES )
			return;
		else
		{
			actualPosition.setX( actualPosition.getX() + 1 );
		}
//		if( Math.round( Math.random() ) == 0 )
//		{
//			int x = (int)Math.round( (Math.random()*2)-1 );	//Negative position
//			int y = (int)Math.round( (Math.random()*2)-1 );
//			
//			actualPosition.setX( actualPosition.getX()+x );
//			actualPosition.setY( actualPosition.getY()+y );
//		}
//		else
//		{
//			//Force a move in a direction
//			int x =(int) Math.round( (Math.random()+0.3) );	//Negative position
//			int y = (int)Math.round( (Math.random())+0.3 );
//			
//			actualPosition.setX( actualPosition.getX()+x );
//			actualPosition.setY( actualPosition.getY()+y );
//		}
	}
}
