package vanet;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import vanet.security.BaseLinePseudonyms;
import vanet.security.HybridScheme;

/**
 * This class rappresent the vehicle into area
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public class Vehicle extends TimerTask 
{
	/**
	 * Speed of vehicle in m/s
	 */
	private double speed = 0;
	/**
	 * ID of vehicle
	 */
	private int id = 0;
	/**
	 * Actual position of Vehicle
	 */
    private Position position = null;
    /**
     * Vehicle transceiver for send and receive messages
     */
    private Transceiver transceiver = null;
    /**
     * Timer for schedule beacon send
     */
    private Timer time;
    
    /**
     * Logger
     */
    private static org.apache.log4j.Logger log = Logger.getLogger(Vehicle.class);

    /**
     * Create new Vehicle
     * 
     * @param id ID of vehicle
     * @param speed Speed of vehicle in km/h
     * @param x X position of vehicle
     * @param y Y position of vehicle
     */
    public Vehicle( int id, int speed, int x, int y )
    {
    	this.setId(id);
		this.setSpeed(speed / 3.6);
		this.position = new Position( x, y );
		
		boostrap();
		
		//Timer task for activate the transmission
		this.time = new Timer( "Vehicle - "+this.toString() );
		this.time.scheduleAtFixedRate(this, 1000, (long)(1000D/Configs.BEACONS_SEC) );
    }
    
    /** 
     *  Get the vehicle payload.
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
  	 * Move the vehicle into the area
  	 * @throws IOException
  	 */
  	private void move() throws IOException
	{
		Moves.moveMe(this.position, this.speed);
	}
  	
  	/**
  	 * Send beacons thread routine
  	 */
	@Override
	public void run() 
	{
		try 
		{
			this.move();
			log.debug("Vehicle "+this.getId()+" position, x="+this.position.getX()+" y="+this.position.getY());
			if( this.position.getX() <= Configs.wifiCover && this.position.getY() <= Configs.wifiCover )
			{
				log.debug("Vehicle "+this.getId()+" send new message");
				transceiver.sendMessage( getPayload() );
			}
			else
			{
				log.info("Vehicle "+this.getId()+" is not in the WIFI area, set x position equal to 0");
				this.position.setX(0);
			}
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
	
	/**
	 * Convert vehicle in string representation
	 */
	public String toString()
	{
		return new Integer(getId()).toString();
	}
}