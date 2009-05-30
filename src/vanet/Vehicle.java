package vanet;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import vanet.security.BaseLinePseudonyms;
import vanet.security.HybridScheme;

/** 
 *  This class rappresent the veichle into area
 */
public class Vehicle extends TimerTask 
{
	private double speed = 0;
	private int id = 0;
    private Position position = null;
    private Transceiver transceiver = null;
    private Timer time;

    public Vehicle( int id, int speed, int x, int y )
    {
    	this.setId(id);
		this.setSpeed(speed / 3.6);
		this.position = new Position( x, y );
		
		boostrap();
		loadkeys();
		
		//Timer task for activate the transmission
		this.time = new Timer( "Veichle - "+this.toString() );
		this.time.scheduleAtFixedRate(this, 1000, (long)(1000D/Configs.BEACONS_SEC) );
    }
    
    /** 
     *  Get the veichle payload.
     */
    private byte[] getPayload() 
    {
    	return new byte[200];
    }

  	/** 
  	 *  Boostrap the system, load the appropriate security system and initialize all peripherals
  	 */
  	private void boostrap() 
  	{  		
		if( Configs.SIMULATOR.equals("bp"))
			this.transceiver = new Transceiver( new BaseLinePseudonyms( id ) );
		else
			this.transceiver = new Transceiver( new HybridScheme(id) );
  	}

  	/** 
   	 *  Load keys with the appropriate method, it is base line or hybrid or others
   	 */
  	public void loadkeys() 
  	{
  		//TODO: Not interesting
  	}

  	private void move() throws IOException
	{
		Moves.moveMe(this.position);
	}
  	
	@Override
	public void run() 
	{
		try 
		{
			this.move();
			transceiver.sendMessage( getPayload() );
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	public String toString()
	{
		return new Integer(getId()).toString();
	}
}