package vanet.security;

import java.util.Timer;
import java.util.TimerTask;

import vanet.Configs;

/**
 * This class rappresent a timer for exclude a certificate after a period of time.
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public class CertificateTimer extends TimerTask 
{
	/**
	 * Validity of certificate
	 */
	private boolean valid = true;
	/**
	 * The timer
	 */
	private Timer timer;
	
	/**
	 * Constructor for class Certificate Timer
	 * 
	 * This class create the timer and schedule the certificate expiry
	 * 
	 * @see vanet.Configs#CHANGE_CERTIFICATE
	 */
	public CertificateTimer()
	{
		timer = new Timer();
		timer.schedule(this, 1000*Configs.CHANGE_CERTIFICATE );
	}
	
	/**
	 * This function it's an override for set the validy in multi-thread operation
	 */
	@Override
	public void run() 
	{
		setValid(false);	//Certificate invalid
	}

	/**
	 * Set the validy of certificate
	 * 
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) 
	{
		this.valid = valid;
	}

	/**
	 * Check if the certificates it's already valid or not
	 * 
	 * @return the validity of certificate
	 */
	public boolean isValid() 
	{
		return valid;
	}
	
	
}
