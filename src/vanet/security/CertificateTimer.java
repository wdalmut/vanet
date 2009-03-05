package vanet.security;

import java.util.Timer;
import java.util.TimerTask;

public class CertificateTimer extends TimerTask 
{
	private boolean valid = true;
	private Timer timer;
	
	public CertificateTimer()
	{
		timer = new Timer();
		timer.schedule(this, 1*1000*60); //After one minute
	}
	
	@Override
	public void run() 
	{
		setValid(false);	//Certificate invalid
	}

	/**
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) 
	{
		this.valid = valid;
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() 
	{
		return valid;
	}
	
	
}
