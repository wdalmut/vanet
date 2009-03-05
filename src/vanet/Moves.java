package vanet;

/**
 * This function move the veichle on map
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public class Moves 
{	
	/**
	 * Move to another position your veichle. In case of NO MOVES mode this function not work.
	 * 
	 * @see vanet.Configs#NO_MOVES
	 * 
	 * @param actualPosition Your position
	 */
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
