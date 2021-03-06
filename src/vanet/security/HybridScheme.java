package vanet.security;

import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;

import vanet.Configs;
import vanet.Message;
/** 
 *  The Hybrid Scheme implementation
 */
public class HybridScheme implements SecurityBox {

/**
 * Certificate store for third party vehicles. This system it's used for verify message with optimization
 */
	private CertificateStore certificateStore;

	
	private static org.apache.log4j.Logger log = Logger.getLogger(HybridScheme.class);
	/**;
	 * Timer for certificate validity for provide anonymity 
	 */
	private CertificateTimer timer;
	
	/**
	 * Key pair contents private and public key generated on the fly
	 */
	
	/**
	 * private group key for signing
	 */
	private PrivateKey prGrpKey  ;
	
	/**
	 * public group Key to sign and add to the certificate
	 */
    public byte[] pbGrpKey;
   
private Signature sign;
/**
 * The certificate which the system can use at particular time. The system must to check the validity of certificate before use it
 */
private PersonalCertificate personalCertificate;


 public Message message;

 private int beaconsSent = 0;
 private int lengthKey=0;
 /** 
  *  The Hybrid Scheme constructor 
  */
 public HybridScheme(int vehicleID){
	 this.certificateStore = new CertificateStore();
	 try {
		this.sign = Signature.getInstance("SHA1withECDSA");
	
	
	//Load the group Private key of a given car.
	String fId = new Integer( vehicleID ).toString();
	
	String folder = "security/hybribKey";
	
	File dir = new File( folder );
	
	FileInputStream fr=null;
	
	fr = new FileInputStream( folder+"/"+vehicleID+".key" );
	
	KeyFactory kf = KeyFactory.getInstance("ECDSA", "BC");
	
    byte[] key = new byte[fr.available()];
    
    fr.read(key, 0, fr.available());
    
    fr.close();
    PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec(key);
   prGrpKey = kf.generatePrivate(keysp);
	 } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
 /**
	 * Verify the self certificate using the private Group signature
	 * 
	 */
public boolean verifySelfCertificate( byte[] selfCertificate)  {
	 	 
	 try {
		Thread.sleep(668);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return true;
  }
/**
 * Generate the self certificate 
 * 
 */
public byte[]  genSelfCertificate(PublicKey pubKey) throws Exception {
	 byte[] signedKey;
	 	// System.out.print(pubKey);
	 byte[] pbkey=pubKey.getEncoded();
	 
	 byte[] certif= new byte[225+pbkey.length];
       // add the pubkey
	 signedKey= this.signedPubKey(pubKey);
	 for(int i=0;i<signedKey.length;i++)
		 certif[i]=signedKey[i];
	 
		// add the signed key
		for(int i=0;i<pbkey.length;i++)
			 certif[i+signedKey.length]=pbkey[i];
		
		
		return certif;
  }
 /**
	 *Generated a a signature on the key generated.
	 * 
	 */
private byte[] signedPubKey(PublicKey pubKey) {
	// i created the signature on the key with the 
	 byte[] bytes= new byte[225];
	 try {
		 // Use the group key to sign the message
			Thread.sleep(727);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Random r = new Random();
       r.nextBytes(bytes);
		return bytes;
}

private KeyPair genOnTheFlyKey()  {
	 
	  ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("c2pnb163v3");

		KeyPairGenerator g=null;
		
			try {
				g = KeyPairGenerator.getInstance("ECDSA");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			try {
				g.initialize(ecSpec, new SecureRandom());
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	
		return g.generateKeyPair();
		
	  
  }
	/**
	 * This method sign message
	 * 
	 * @param message Message which you want sign
	 * @param key The private key generated on the fly for signing message
	 * @return The signature of message
 * @throws SignatureException 
	 */

	 private byte[] signMessage( byte[] message, PrivateKey key ) throws SignatureException
	 { 
		 Signature sign=null;
		 try 
		 {
			sign = Signature.getInstance("SHA1withECDSA");
		 } 
		 catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		byte[] signature = null;
		try
		{
			sign.initSign(key);
			sign.update(message);
			signature = sign.sign();
			
		}
		catch( SignatureException e )
		{
			log.warn("Signature not executed"+e.getMessage() );
			signature = new byte[48];
		} 
		catch (InvalidKeyException e) 
		{
			log.warn("Invalid Key: "+e.getMessage() );
		}
		return signature;
	}
 	
 	/**
	 * Verify the self certificate using the private Group signature
	 * 
	 * @param c The certificate to test
	 * @return The validity of that certificate
	 */
	@SuppressWarnings("deprecation")
	@Override
	public byte[] securize(byte[] payload) 
	{
		byte[] message = null;
		
		KeyPair pair;
		//TODO: Reattach certificate every tot beacons
	    //if( timer != null ) timer.setValid(false );
		
		//if( timer == null || !timer.isValid() )
		if( timer == null || !timer.isValid() || (beaconsSent % Configs.REATTACH_CERTIFICATE) == 0 )
		{
			//LONG MODE
			timer = new CertificateTimer();
			pair= this.genOnTheFlyKey();
			
			// add the certificate in the certificate store
			
			this.certificateStore = new CertificateStore();
			
			try
			
			{
				// generate the new certificate on the generated key TODO modified the function using 
				//the Public key generated an the private group key of the vehicle.
				PrivateKey prGkey;
							
				byte[] certif = this.genSelfCertificate(pair.getPublic()); // bytes rapresenting selft Certificate
				
				// Random certificate id
				int certId = (int)(Math.random()*100000);
				// old version personalCertificate = new PersonalCertificate(certId,cert,pair.getPrivate());
				personalCertificate  = new PersonalCertificate(certId,certif,pair.getPrivate());
				
				message = new byte[ 4 + Configs.PAYLOAD_LENGTH];
				
				message[0] = (byte)((certId & 0xFF000000) >> 24);	//Put the id
				message[1] = (byte)((certId & 0x00FF0000) >> 16);	//In byte mode
				message[2] = (byte)((certId & 0x0000FF00) >> 8);	//Using shift
				message[3] = (byte) (certId & 0x000000FF);
				for( int i=0, j=4; i< Configs.PAYLOAD_LENGTH; i++, j++ )	//Payload copy
					message[j] = payload[i]; 
				
				byte[] signature = signMessage(message,pair.getPrivate());
				
				
				byte[ ] tmp = new byte[ Configs.PAYLOAD_LENGTH + signature.length + certif.length+4];
				// put the signature length
				
				message[4] = (byte)((signature.length & 0xFF000000) >> 24 ) ;
				message[5] = (byte)((signature.length & 0x00FF0000) >> 16 ) ;
				message[6] = (byte)((signature.length & 0x0000FF00) >> 8 ) ;
				message[7] = (byte)( signature.length & 0x000000FF ) ;
				
				// add the message
				for( int i=0; i<message.length; i++ )
					tmp[i] = message[i];
										
				//Attach the signature
				for( int i=4+Configs.PAYLOAD_LENGTH, j=0; j<signature.length; i++, j++ ) 
					
				tmp[i] = signature[j];
				
				//Attach the certificate
				for( int i=4+Configs.PAYLOAD_LENGTH+signature.length, j=0; j<certif.length; i++, j++ )	
					tmp[i] = certif[j];
				
				message = tmp;
				/*for(int m= 0; m<certif.length;m++)
				System.out.print("\ntrans : "+m+" "+certif[m]);*/
				
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
				//Retrive the ID of certificate to use
				int certId =  personalCertificate.getId();
				//System.out.print("\nShort way");
				message = new byte[ 4 + Configs.PAYLOAD_LENGTH ];
				
				message[0] = (byte)((certId & 0xFF000000) >> 24);	//Put the id
				message[1] = (byte)((certId & 0x00FF0000) >> 16);	//In byte mode
				message[2] = (byte)((certId & 0x0000FF00) >> 8);	//Using shift
				message[3] = (byte) (certId & 0x000000FF);
				for( int i=0, j=4; i< payload.length; i++, j++ )	//Payload copy
					message[j] = payload[i]; 
				
				byte[] signature = signMessage(message,personalCertificate.getPrivateKey() );
				
				byte[] tmp = new byte[Configs.PAYLOAD_LENGTH+signature.length+4];
				
				//Put the signature length
				message[4] = (byte)((signature.length & 0xFF000000) >> 24 ) ;
				message[5] = (byte)((signature.length & 0x00FF0000) >> 16 ) ;
				message[6] = (byte)((signature.length & 0x0000FF00) >> 8 ) ;
				message[7] = (byte)( signature.length & 0x000000FF ) ;
				// Add the message to tmp
				for( int i=0; i< message.length ; i++ )
					tmp[i] = message[i];
						
				//Attach the signature
				for( int i=4+Configs.PAYLOAD_LENGTH, j=0; j<signature.length; i++, j++ )
					tmp[i] = signature[j];
				
				//Change support
				message = tmp;	
				
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
			SelfCertify c = certificateStore.getSelfCertificate( message.getId() );
			
			if( c == null ){
				log.debug("Certificate NOT stored: "+message.getId());
				return false;}
			
			byte[] ID = new byte[4];
			int id = message.getId();
			ID[0] = (byte)((id&0xFF000000) >> 24);
			ID[1] = (byte)((id&0x00FF0000) >> 16); 
			ID[2] = (byte)((id&0x0000FF00) >> 8); 
			ID[3] = (byte) (id&0x000000FF); 
			try
			{
				sign.initVerify( c.getPubKey() );
				sign.update(ID);
				byte[] payload = message.getPayload();
				payload[0] = 0;
				payload[1] = 0;
				payload[2] = 0;
				payload[3] = 0;
				sign.update(payload);
				
				//sign.verify(message.getSignature()); -- check if the verification is right or not
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
			{// i should use another version of construct Certificate that should return an
			 // array of byte
				SelfCertify c = constructCertificate(message.getCertificate());
				
				if( c != null && this.verifyCertificate(c) )
	// 19.05.09 I should in this add self certificate which will be array of byte
					certificateStore.addCertificate( message.getId(), c); 
								
				else
				{
					System.out.print("certi null");
					return false;
				}
				sign.initVerify( c.getPubKey() );
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
				System.out.print(e.getMessage());
				return false;
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return true;
		}
	}

	private boolean verifyCertificate(SelfCertify c) {
		try {
			 // Use the group key to sign the message
				Thread.sleep(668);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return true;
	}

	private SelfCertify  constructCertificate(byte[] cert) {
					
		SelfCertify c = null;
		PublicKey pub = null;
		int lengthKey=cert.length-225;
		byte[] pbkey = new byte[lengthKey];
		byte[] signedKey=new byte[225];
		 // retrieve the pubkey
		for(int i=0;i<225;i++)
			signedKey[i]=cert[i];
		// signed key
		for(int i=0;i<lengthKey;i++)
			 pbkey[i]=cert[i+225];
		// construct the public key
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pbkey);
		 KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("ECDSA", "BC");
			 pub = keyFactory.generatePublic(pubKeySpec);
		
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		// retrieve the signature on the key
		
		
		c= new SelfCertify(pub,signedKey);
		return c;
}
}