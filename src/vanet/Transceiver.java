package vanet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.log4j.Logger;

import vanet.security.SecurityBox;
import vanet.security.VerifyMyMessageException;

/**
 * This class rappresent the transceiver of vehicle
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public class Transceiver implements Runnable 
{

	/**
	 * Security Box Implementation
	 */
	private SecurityBox securityBox;
	/**
	 * Index for generate IP
	 */
	private static byte index = 1;
	/**
	 * The datagram socket for send and receive messages
	 */
	private DatagramSocket socket;
	
	private static org.apache.log4j.Logger log = Logger.getLogger(Transceiver.class);

  /** 
   *  Constructor of transceiver
   *  
   *  @param securityBox the security implementation which you want use for sign and verify messages
   *  
   *  @see vanet.Configs#SIMULATOR
   */
  	public Transceiver(SecurityBox securityBox) 
  	{
  		try
  		{
	  		byte[] addr = new byte[]{127,0,0,index};//Assign multiple IP address for emulate machine with IP
	  		this.socket = new DatagramSocket( Configs.PORT, InetAddress.getByAddress(addr) );
			index++;
			
			Thread t = new Thread( this, "Transceiver For Vehicle" );
			t.start();
  		}
  		catch( Exception e )
  		{
  			e.printStackTrace();
  		}
	  
		this.securityBox = securityBox;
	}
  	
  	/** 
  	 *  Send the payload to other vehicles
  	 *  
  	 *  This function provide security to payload using the securize method of security box
  	 *  
  	 *  @see vanet.security.SecurityBox#securize
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

  	/**
  	 * This function write on socket the data
  	 * 
  	 * @param data The data which you want send
  	 * 
  	 * @throws IOException In case of problems throw a IOException
  	 */
  	private void write( byte[] data ) throws IOException
	{
		InetAddress address = InetAddress.getByName(Configs.SERVER_BROADCAST_ADDRESS);
		this.socket.send(new DatagramPacket(data, data.length, address, Configs.PORT ));
	}
  
  /** 
   *  Receive message from other vehicles
   */
  public void receivedMessage(Message message) 
  {
	  if( message == null )
		  return;
	  
	  try
	  {
		  boolean result = this.securityBox.verify(message);
		  if( result )
		  {
			  // System.out.print("\nMessage secure: "+message.getId());
			  log.info("Message secure: "+message.getId());
		  }
		  else
		  {
			  //System.out.print("\nMessage insecure: "+message.getId());
			  log.warn("Message insecure: "+message.getId());
		  }
	  }
	  catch( VerifyMyMessageException e )
	  {
		  log.info("Skip auto-verification for message: "+message.getId() );
	  }
  }

  	/**
  	 * Method in multi-thread mode for receive new messages from network
  	 */
  	@Override
	public void run() 
  	{
  		while( true )
		{
			try
			{
				DatagramPacket packet = new DatagramPacket(new byte[1000], 1000);
				this.socket.receive( packet );
				
				byte[] data = packet.getData();
				int dataLength = packet.getLength();
				
				int id = 0;			
				id = data[3]&0xFF;
				id += (data[2]&0xFF) << 8;
				id += (data[1]&0xFF) << 16;
				id += (data[0]&0xFF) << 24;
				
				byte[] payload = new byte[200];
				int signatureLength;
				signatureLength = data[7]&0xFF;
				signatureLength += (data[6]&0xFF) << 8;
				signatureLength += (data[5]&0xFF) << 16;
				signatureLength += (data[4]&0xFF) << 24;
				
				byte[] signature = new byte[signatureLength];				
				for( int i=4, j=0; i<200; i++, j++ )
					payload[j] = data[i];
				for( int i=4+200, j=0; j<signatureLength; i++, j++ )
					signature[j] = data[i];
				
				Message m = null;
				
				int certificateLength = dataLength-4-200-signatureLength;
				
				if( certificateLength > 10 )
				{
					byte[] certificate = new byte[ certificateLength ];
					for( int i=4+200+signatureLength, j=0; j<certificateLength; i++, j++ )
						certificate[j] = data[i];
					
					m = new Message(id, payload, signature, certificate); //Long message
					
				}
				else
				{
					m = new Message(id, payload, signature);	
					
				}//Short message				
				receivedMessage( m );
			}
			catch( IOException exception )
			{
				System.out.println("IMPOSSIBLE receive UDP packet");
			}
		}
  	}
}