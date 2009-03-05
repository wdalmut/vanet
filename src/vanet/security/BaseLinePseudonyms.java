package vanet.security;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import vanet.Configs;
import vanet.Message;

/** 
 *  Implementation of Base Line Pseudonyms method
 */
public class BaseLinePseudonyms implements SecurityBox
{

	private KeyStore keyStore;
	private CertificateStore certificateStore;
	private CertificateTimer timer;
	private X509Certificate certificate;
	private PrivateKey privateKey;
	private Signature sign;
	
	private X509Certificate ca;

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
	
	private byte[] signMessage( byte[] message, PrivateKey key ) throws Exception
	{
		sign.initSign( privateKey );
		sign.update(message);
		
		return sign.sign();
	}
	
	@Override
	public byte[] securize( byte[] payload ) 
	{
		byte[] message = null;
		
		if( timer == null || !timer.isValid() )
		{
			//LONG MODE
			timer = new CertificateTimer();
			PersonalCertificate pc = this.keyStore.getCertificate();
			this.certificate = pc.getCertificate();
			this.privateKey = pc.getPrivateKey();
			
			try
			{
				byte[] cert = this.certificate.getEncoded();
				
				int certId = pc.getId();
				int certLen = cert.length;
				
				message = new byte[ 4 + Configs.PAYLOAD_LENGTH + 128 + certLen];
				
				message[0] = (byte)((certId & 0xFF000000) >> 32);	//Put the id
				message[1] = (byte)((certId & 0x00FF0000) >> 16);	//In byte mode
				message[2] = (byte)((certId & 0x0000FF00) >> 8);	//Using shift
				message[3] = (byte) (certId & 0x000000FF);
				for( int i=0, j=4; i< Configs.PAYLOAD_LENGTH; i++, j++ )	//Payload copy
					message[j] = payload[i]; 
				
				byte[] signature = signMessage(message, this.privateKey);
				
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
			try
			{
				PersonalCertificate pc = this.keyStore.getCertificate();
				
				int certId = pc.getId();
				
				message = new byte[ 4 + payload.length + 128 ];
				
				message[0] = (byte)((certId & 0xFF000000) >> 32);	//Put the id
				message[1] = (byte)((certId & 0x00FF0000) >> 16);	//In byte mode
				message[2] = (byte)((certId & 0x0000FF00) >> 8);	//Using shift
				message[3] = (byte) (certId & 0x000000FF);
				for( int i=0, j=4; i< payload.length; i++, j++ )	//Payload copy
					message[j] = payload[i]; 
				
				byte[] signature = signMessage(message, this.privateKey);
				
				for( int i=4+payload.length, j=0; j<signature.length; i++, j++ )//Attach the signature
					message[i] = signature[j];
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		
		return message;
	}

	@Override
	public boolean verify(Message message) 
	{
		if( message.getCertificate() == null )//SHORT MODE
		{
			X509Certificate c = certificateStore.getCertificate( message.getId() );
			if( c == null )
				return false;
			
			byte[] ID = new byte[4];
			int id = message.getId();
			ID[0] = (byte)((id&0xFF000000) >> 32);
			ID[1] = (byte)((id&0x00FF0000) >> 16); 
			ID[2] = (byte)((id&0x0000FF00) >> 8); 
			ID[3] = (byte) (id&0x000000FF); 
			
			try
			{
				sign.initVerify( c );
				sign.update(ID);
				sign.update(message.getPayload());
				sign.verify(message.getSignature());
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
			ID[0] = (byte)((id&0xFF000000) >> 32);
			ID[1] = (byte)((id&0x00FF0000) >> 16); 
			ID[2] = (byte)((id&0x0000FF00) >> 8); 
			ID[3] = (byte) (id&0x000000FF); 
			
			try
			{
				X509Certificate c = constructCertificate(message.getCertificate());
				if( verifyCertificate( c ) )
					certificateStore.addCertificate( message.getId(), c);
				else
					return false;
				
				sign.initVerify( c );
				sign.update(ID);
				sign.update(message.getPayload());
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
	
	private boolean verifyCertificate(X509Certificate c) 
	{
		try
		{
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