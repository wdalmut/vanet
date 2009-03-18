package vanet.security;

import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import log.Log;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import vanet.Configs;
import vanet.Message;

/** 
 *  Implementation of Base Line Pseudonyms method
 */
public class BaseLinePseudonyms implements SecurityBox
{
	/**
	 * Key Store for baseline implementation which contains all certificates and private keys for securize messages
	 */
	private KeyStore keyStore;

	/**
	 * Certificate store for third party veichles. This system it's used for verify message with optimization
	 */
	private CertificateStore certificateStore;

	/**
	 * Timer for certificate validity for provide anonymization 
	 */
	private CertificateTimer timer;
	
	/**
	 * The certificate which the system can use at particoular time. The system must to check the validity of certificate before use it
	 */
	private PersonalCertificate personalCertificate;
	
	/**
	 * The signature system
	 */
	private Signature sign;
	
	/**
	 * Certificate of Certification Authority
	 */
	private X509Certificate ca;
	
	private int beaconsSent = 0;

	
	/**
	 * Constructor of BaseLinePseudonyms
	 * 
	 * @param veichleID The ID of veiche. This parameter it's used for load the certificates and private key of veichle.
	 */
	public BaseLinePseudonyms( int veichleID )
	{
		this.keyStore = new KeyStore( veichleID );
		this.certificateStore = new CertificateStore();
		
		try
		{
			this.sign = Signature.getInstance("SHA1withECDSA");
			
			FileInputStream fr = new FileInputStream( "security/ca/ca.crt" );
			CertificateFactory cf = CertificateFactory.getInstance("X509", "BC");
		    this.ca = (X509Certificate) cf.generateCertificate( fr );
		    fr.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method sign message
	 * 
	 * @param message Message which you want sign
	 * @param key The private key for signing message
	 * @return The signature of message
	 */
	private byte[] signMessage( byte[] message, PrivateKey key )
	{
		byte[] signature = null;
		try
		{
			sign.initSign( key );
			sign.update(message);
			signature = sign.sign();
		}
		catch( SignatureException e )
		{
			log.Log.critical(this, "signMessage", "Signature not executed"+e.getMessage() );
			signature = new byte[48];
		} 
		catch (InvalidKeyException e) 
		{
			log.Log.critical(this, "signMessage", "Invalid Key: "+e.getMessage() );
		}
		return signature;
	}
	
	@Override
	public byte[] securize( byte[] payload ) 
	{
		byte[] message = null;

		if( timer == null || !timer.isValid() || (beaconsSent % Configs.REATTACH_CERTIFICATE) == 0 )
		{
			X509Certificate certificate;
			PrivateKey privateKey;
			if( timer == null || !timer.isValid() )
			{
				//LONG MODE
				timer = new CertificateTimer();
				this.personalCertificate = this.keyStore.getCertificate();
				certificate = this.personalCertificate.getCertificate();
				privateKey = this.personalCertificate.getPrivateKey();
				
				log.Log.debug(this, "securize", "Message sent in LONG MODE. REATTACH FOR TIMING. Certificate ID: "+this.personalCertificate.getId());
			}
			else
			{	
				certificate = this.personalCertificate.getCertificate();
				privateKey = this.personalCertificate.getPrivateKey();
				
				log.Log.debug(this, "securize", "Message sent in LONG MODE. REATTACH FOR BEACONS SENT. Certificate ID: "+personalCertificate.getId());
			}
			
			try
			{
				byte[] cert = certificate.getEncoded();
				
				int certId = this.personalCertificate.getId();
				
				message = new byte[ 4 + Configs.PAYLOAD_LENGTH];
				
				message[0] = (byte)((certId & 0xFF000000) >> 24);	//Put the id
				message[1] = (byte)((certId & 0x00FF0000) >> 16);	//In byte mode
				message[2] = (byte)((certId & 0x0000FF00) >> 8);	//Using shift
				message[3] = (byte) (certId & 0x000000FF);
				for( int i=0, j=4; i< Configs.PAYLOAD_LENGTH; i++, j++ )	//Payload copy
					message[j] = payload[i]; 
				
				byte[] signature = signMessage(message, privateKey);
				byte[ ] tmp = new byte[ message.length + signature.length + cert.length ];
				//Put the signature length
				message[4] = (byte)((signature.length & 0xFF000000) >> 24 ) ;
				message[5] = (byte)((signature.length & 0x00FF0000) >> 16 ) ;
				message[6] = (byte)((signature.length & 0x0000FF00) >> 8 ) ;
				message[7] = (byte)( signature.length & 0x000000FF ) ;
				
				for( int i=0; i<message.length; i++ )
					tmp[i] = message[i];
				message = tmp;
				
				for( int i=4+Configs.PAYLOAD_LENGTH, j=0; j<signature.length; i++, j++ )//Attach the signature
					message[i] = signature[j];
				
				for( int i=4+Configs.PAYLOAD_LENGTH+signature.length, j=0; i<message.length; i++, j++ )	//Attach the certificate
					message[i] = cert[j];
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		else
		{
			log.Log.debug( this, "securize", "Message sent in SHORT MODE for certificate. Certificate ID: "+this.personalCertificate.getId() );
			try
			{
				//Retrive the ID of certificate to use
				int certId = personalCertificate.getId();
				
				message = new byte[ 4 + payload.length ];
				
				message[0] = (byte)((certId & 0xFF000000) >> 24);	//Put the id
				message[1] = (byte)((certId & 0x00FF0000) >> 16);	//In byte mode
				message[2] = (byte)((certId & 0x0000FF00) >> 8);	//Using shift
				message[3] = (byte) (certId & 0x000000FF);
				for( int i=0, j=4; i< payload.length; i++, j++ )	//Payload copy
					message[j] = payload[i]; 
				
				byte[] signature = signMessage(message, this.personalCertificate.getPrivateKey());
				
				byte[] tmp = new byte[message.length+signature.length];
				//Put the signature length
				message[4] = (byte)((signature.length & 0xFF000000) >> 24 ) ;
				message[5] = (byte)((signature.length & 0x00FF0000) >> 16 ) ;
				message[6] = (byte)((signature.length & 0x0000FF00) >> 8 ) ;
				message[7] = (byte)( signature.length & 0x000000FF ) ;

				for( int i=0; i< message.length ; i++ )
					tmp[i] = message[i];
				
				message = tmp;	//Change support
				
				for( int i=4+payload.length, j=0; j<signature.length; i++, j++ )//Attach the signature
					message[i] = signature[j];
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		
		beaconsSent++;
		
		return message;
	}

	@Override
	public boolean verify(Message message) throws VerifyMyMessageException 
	{
		if( message.getId() == this.personalCertificate.getId() )
			throw new VerifyMyMessageException();
		
		if( message.getCertificate() == null )//SHORT MODE
		{
			X509Certificate c = certificateStore.getCertificate( message.getId() );
			if( c == null )
			{
				Log.debug(this, "verify", "Certificate NOT stored: "+message.getId());
				return false;
			}
			
			byte[] ID = new byte[4];
			int id = message.getId();
			ID[0] = (byte)((id&0xFF000000) >> 24);
			ID[1] = (byte)((id&0x00FF0000) >> 16); 
			ID[2] = (byte)((id&0x0000FF00) >> 8); 
			ID[3] = (byte) (id&0x000000FF); 
			
			try
			{
				sign.initVerify( c );
				sign.update(ID);
				byte[] payload = message.getPayload();
				payload[0] = 0;
				payload[1] = 0;
				payload[2] = 0;
				payload[3] = 0;
				sign.update(payload);
				if( sign.verify(message.getSignature()) )
					return true;
				else
					return false;
			}
			catch( SignatureException e )
			{
				return false;
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return true;
		}
		else
		{
			byte[] ID = new byte[4];
			int id = message.getId();
			ID[0] = (byte)((id&0xFF000000) >> 24);
			ID[1] = (byte)((id&0x00FF0000) >> 16); 
			ID[2] = (byte)((id&0x0000FF00) >> 8); 
			ID[3] = (byte) (id&0x000000FF); 
			
			try
			{
				X509Certificate c = constructCertificate(message.getCertificate());
				if( c != null && verifyCertificate( c ) )
					certificateStore.addCertificate( message.getId(), c);
				else
					return false;
				
				sign.initVerify( c );
				sign.update(ID);
				
				byte[] payload = message.getPayload();
				payload[0] = 0;
				payload[1] = 0;
				payload[2] = 0;
				payload[3] = 0;
				sign.update(payload);
				if( sign.verify(message.getSignature()) )
					return true;
				else
					return false;
			}
			catch( SignatureException e )
			{
				return false;
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return true;
		}
	}
	
	/**
	 * Verify the certificate using the Certification Authority
	 * 
	 * @param c The certificate to test
	 * @return The validy of that certificate
	 */
	private boolean verifyCertificate(X509Certificate c) 
	{
		try
		{
			Signature sign = Signature.getInstance(c.getSigAlgName());
		    
			sign.initVerify(ca);
		    sign.update( c.getTBSCertificate() );
		    if( sign.verify( c.getSignature() ) )
		    	return true;
		    else
		    	return false;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Construct the certificate from byte rappresentation
	 * 
	 * @param cert The certificate in byte
	 * @return The x509 certificate
	 */
	private X509Certificate constructCertificate( byte[] cert )
	{
		ByteInputStream bis = new ByteInputStream( cert, cert.length );
		X509Certificate c = null;
		try
		{
			CertificateFactory cf = CertificateFactory.getInstance("X509", "BC");
			c = (X509Certificate) cf.generateCertificate(bis);
			bis.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return c;
	}
}