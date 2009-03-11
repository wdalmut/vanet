package vanet.security;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.x509.X509V1CertificateGenerator;

import vanet.Configs;
import vanet.Message;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
/** 
 *  The Hybrid Scheme implementation
 */
public class HybridScheme implements SecurityBox {

/**
 * Certificate store for third party vehicles. This system it's used for verify message with optimization
 */
	private CertificateStore certificateStore;

	/**
	 * Timer for certificate validity for provide anonymity 
	 */
	private CertificateTimer timer;
	
	/**
	 * Key pair contents private and public key generated on the fly
	 */
	


private Signature sign;
/**
 * The certificate which the system can use at particular time. The system must to check the validity of certificate before use it
 */
private PersonalCertificate personalCertificate;


 public Message message;

 private int lengthKey=0;
 /** 
  *  The Hybrid Scheme constructor 
  */
 public HybridScheme(int veichleID){
	 this.certificateStore = new CertificateStore();
	 try {
		this.sign = Signature.getInstance("SHA1withECDSA");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
 }
 /**
	 * Verify the self certificate using the private Group signature
	 * 
	 */
public boolean verifySelfCertificate( byte[] selfCertificate)  {
	 
	 /*try {
		sign.initVerify( this.pair.getPublic() );
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		try {
			sign.update(signedKey);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if( sign.verify(selfCertificate) )
				return true;
							
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	 try {
		Thread.sleep(5000);
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
	PublicKey pubKey2;
	 KeyPair pair;
	byte[] signedKey;
	 byte[] certif= new byte[115];
	 System.out.print(pubKey);
	 byte[] pbkey=pubKey.getEncoded(); 
	 for(int i=0;i<pbkey.length;i++)
		 certif[i]=pbkey[i];
	 lengthKey=pbkey.length;
		signedKey= this.signedPubKey( pubKey);
			
		pair=this.genOnTheFlyKey();
		for(int i=0;i<signedKey.length;i++)
			 certif[i+pbkey.length]=signedKey[i];
		return certif;
  }
 /**
	 *Generated a Key signature 
	 * 
	 */
private byte[] signedPubKey(PublicKey pubKey) {
	// i created the signature on the key with the 
	 byte[] bytes= new byte[46];
	 try {
		 // Use the group key to sign the message
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Random r = new Random();
       r.nextBytes(bytes);
		return bytes;
}
public X509Certificate  genSelfCertificate(KeyPair pair) throws Exception {
	 
	Date startDate = new Date();              // time from which certificate is valid
	
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.YEAR, 10);
	Date expiryDate = cal.getTime();
	 BigInteger serialNumber = new BigInteger("13512376512376581376");
	 X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
		X509Principal dnName = new X509Principal("CN=Vanet Simulator CA");
		X509Principal subDN = new X509Principal("CN=Walter Dal Mut");

		certGen.setSerialNumber(serialNumber);
		certGen.setIssuerDN(dnName);
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
		certGen.setSubjectDN(subDN);                       // note: same as issuer
		certGen.setPublicKey(pair.getPublic());
		certGen.setSignatureAlgorithm("SHA1withECDSA");
       
		X509Certificate cert = certGen.generate(pair.getPrivate(), "BC");
		
		return cert;
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
	{ Signature sign=null;
	try {
		sign = Signature.getInstance("SHA1withECDSA");
	} catch (NoSuchAlgorithmException e) {
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
			System.out.println( "NON FATTA LA SIGNATURE:"+e.getMessage());
			signature = new byte[48];
		} 
		catch (InvalidKeyException e) 
		{
			e.printStackTrace();
		}
		return signature;
	}
 /**
	 * Verify the self certificate using the private Group signature
	 * 
	 * @param c The certificate to test
	 * @return The validity of that certificate
	 */
 
@Override
public byte[] securize(byte[] payload) 
{
	byte[] message = null;
	
	KeyPair pair;
	//TODO: Reattach certificate every tot beacons
    //if( timer != null ) timer.setValid(false );
	
	if( timer == null || !timer.isValid() )
	{
		System.out.print("long way");
		//LONG MODE
		timer = new CertificateTimer();
		pair= this.genOnTheFlyKey();
			
		// add the certificate in the certificate store
		
		this.certificateStore = new CertificateStore();
		
		try
		
		{
			// generate the new certificate on the generated key
			X509Certificate cert= this.genSelfCertificate(pair); 
			byte[] certif = cert.getEncoded();
			
			// Random certificate id
			int certId = (int)(Math.random()*100000);
			//
			personalCertificate= new PersonalCertificate(certId,certif,pair.getPrivate());
			
			message = new byte[ 4 + Configs.PAYLOAD_LENGTH];
			
			message[0] = (byte)((certId & 0xFF000000) >> 24);	//Put the id
			message[1] = (byte)((certId & 0x00FF0000) >> 16);	//In byte mode
			message[2] = (byte)((certId & 0x0000FF00) >> 8);	//Using shift
			message[3] = (byte) (certId & 0x000000FF);
			for( int i=0, j=4; i< Configs.PAYLOAD_LENGTH; i++, j++ )	//Payload copy
				message[j] = payload[i]; 
			
			byte[] signature = signMessage(message,pair.getPrivate());
			
			//System.out.print("signature:"+signature[12]+"\n");
			// here i will add the certificate length after the payload not inside the payload after have already signed the message
			byte[ ] tmp = new byte[ message.length + signature.length + certif.length+4];
			// add the message
			for( int i=0; i<message.length; i++ )
				tmp[i] = message[i];
			
			tmp[message.length] = (byte)((signature.length & 0xFF000000) >> 24 ) ;
			tmp[message.length+1] = (byte)((signature.length & 0x00FF0000) >> 16 ) ;
			tmp[message.length+2] = (byte)((signature.length & 0x0000FF00) >> 8 ) ;
			tmp[message.length+3] = (byte)( signature.length & 0x000000FF ) ;
						
			//Attach the signature
			for( int i=4+message.length, j=0; j<signature.length; i++, j++ )
				
			tmp[i] = signature[j];
			// i specified that i have attached the certificate
			//tmp[4+message.length+signature.length]=1;
			//Attach the certificate
			for( int i=4+message.length+signature.length, j=0; j<certif.length; i++, j++ )	
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
			System.out.print("Short way");
			message = new byte[ 4 + payload.length ];
			
			message[0] = (byte)((certId & 0xFF000000) >> 24);	//Put the id
			message[1] = (byte)((certId & 0x00FF0000) >> 16);	//In byte mode
			message[2] = (byte)((certId & 0x0000FF00) >> 8);	//Using shift
			message[3] = (byte) (certId & 0x000000FF);
			for( int i=0, j=4; i< payload.length; i++, j++ )	//Payload copy
				message[j] = payload[i]; 
			
			byte[] signature = signMessage(payload,personalCertificate.getPrivateKey() );
			
			byte[] tmp = new byte[message.length+signature.length+4+1];
			// Add the message to tmp
			for( int i=0; i< message.length ; i++ )
				tmp[i] = message[i];
			//Put the signature length
			tmp[message.length] = (byte)((signature.length & 0xFF000000) >> 24 ) ;
			tmp[message.length+1] = (byte)((signature.length & 0x00FF0000) >> 16 ) ;
			tmp[message.length+2] = (byte)((signature.length & 0x0000FF00) >> 8 ) ;
			tmp[message.length+3] = (byte)( signature.length & 0x000000FF ) ;
			
			
			//Attach the signature
			for( int i=4+message.length, j=0; j<signature.length; i++, j++ )
				tmp[i] = signature[j];
			// i specified that i haven't attached the certificate
			//tmp[4+message.length+signature.length]=0;
			//Change support
			message = tmp;	
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
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
			return false;
		
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
		ID[0] = (byte)((id&0xFF000000) >> 24);
		ID[1] = (byte)((id&0x00FF0000) >> 16); 
		ID[2] = (byte)((id&0x0000FF00) >> 8); 
		ID[3] = (byte) (id&0x000000FF); 
		
		try
		{
			X509Certificate c = constructCertificate(message.getCertificate());
			
			if( c != null  )
				certificateStore.addCertificate( message.getId(), c); 
							
			else
			{System.out.print("certi null");
				return false;
			}
			sign.initVerify( c );
			sign.update(ID);
			
			byte[] payload = message.getPayload();
			
			sign.update(payload);
			
			boolean test= sign.verify(message.getSignature());
			
			if(test )
			
				return true;
			else{
				System.out.print("test failed");
				return false;
			}
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
	private boolean verifyCertificate(X509Certificate c) {
		// TODO Auto-generated method stub
		return true;
	}

	private X509Certificate  constructCertificate(byte[] cert) {
		ByteInputStream bis = new ByteInputStream( cert, cert.length );
			
		X509Certificate c = null;
		
		try
		{
			CertificateFactory cf = CertificateFactory.getInstance("X509", "BC");
			
			c =(X509Certificate) cf.generateCertificate(bis);
			if(c==null)
				System.out.println( "\n mess null");
			bis.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return c;
}
}