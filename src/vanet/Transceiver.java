package vanet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import vanet.security.SecurityBox;

public class Transceiver implements Runnable 
{

  private SecurityBox securityBox;
  private static byte index = 20;
  private DatagramSocket socket;

  /** 
   *  Constructor of transceiver
   */
  	public Transceiver(SecurityBox securityBox) 
  	{
  		try
  		{
	  		byte[] addr = new byte[]{127,0,0,index};//Assign multiple IP address for emulate machine with IP
	  		this.socket = new DatagramSocket( Configs.PORT, InetAddress.getByAddress(addr) );
			index++;
			
			Thread t = new Thread( this );
			t.start();
  		}
  		catch( Exception e )
  		{
  			e.printStackTrace();
  		}
	  
		this.securityBox = securityBox;
	}
  	
  	/** 
  	 *  Send the payload to other veichles
  	 */
  	public void sendMessage(byte[] payload)
  	{
  		byte[] message = this.securityBox.securize(payload);
  		
  		try
  		{
  			if( message != null )
  				write( message );
  		}
  		catch( Exception e )
  		{
  			e.printStackTrace();
  		}
  	}

  	private void write( byte[] data ) throws IOException
	{
		InetAddress address = InetAddress.getByName(Configs.SERVER_BROADCAST_ADDRESS);
		this.socket.send(new DatagramPacket(data, data.length, address, Configs.PORT ));
	}
  
  /** 
   *  Receive message from other veichles
   */
  public void receivedMessage(Message message) 
  {
	  if( this.securityBox.verify(message) )
	  {
		  //TODO: message secure
	  }
	  else
	  {
		  //TODO: message insecure
	  }
  }

  	@Override
	public void run() 
  	{
  		while( true )
		{
			try
			{
				DatagramPacket packet = new DatagramPacket(new byte[600], 600);
				this.socket.receive( packet );
				
				byte[] data = packet.getData();
				
				int id = 0;
				byte[] payload = new byte[200];
				byte[] signature = new byte[128];
				byte[] certificate = new byte[ data.length-200-128];
				
				id = data[3]&0xFF;
				id += data[2]&0xFF << 8;
				id += data[1]&0xFF << 16;
				id += data[0]&0xFF << 32;
				
				for( int i=4; i<data.length; i++)
				{
					if( i>=4 && i<200+4)
						payload[i-4] = data[i];
					else if( i>=200+4 && i<4+200+128 )
						signature[i-4-200] = data[i];
					else
						certificate[i-4-200-128] = data[i];
				}
				
				Message m = null;
				if( certificate.length < 20 )
					m = new Message(id, payload, signature);	//Short message
				else
					m = new Message(id, payload, signature, certificate); //Long message
				
				receivedMessage( m );
			}
			catch( IOException exception )
			{
				System.out.println("IMPOSSIBLE receive UDP packet");
			}
		}
  	}
}