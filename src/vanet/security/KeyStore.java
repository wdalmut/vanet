package vanet.security;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Vector;


/** 
 *  This class rappresent the key store for veichle certificates
 */
public class KeyStore
{
	/** Personal certificates */
	private Vector<PersonalCertificate> personalCertificates;

	/**
	 * The key store implementation
	 * 
	 * @param id The id of veichle for load certificates and private keys
	 */
	public KeyStore( int id )
	{
		personalCertificates = new Vector<PersonalCertificate>();
		
		String fId = new Integer( id ).toString();
		String folder = "security/certificates/"+fId+"/c";
		String folder2 = "security/certificates/"+fId+"/p";
		
		System.out.println( folder );
		
		File dir = new File( folder );
		File dir2 = new File( folder2 );
		String[] certs = dir.list();
		String[] privates = dir2.list();
		try
		{
			CertificateFactory cf = CertificateFactory.getInstance("X509", "BC");
				
			for( int i=0; i<certs.length; i++ )
			{
				try
				{
					if( certs[i].indexOf(".crt") > 0 )
					{
						FileInputStream fr = new FileInputStream( folder+"/"+certs[i] );
						X509Certificate c = (X509Certificate) cf.generateCertificate( fr );
						
						if( privates[i].indexOf(".key") > 0 )
						{
							fr = new FileInputStream( folder2+"/"+privates[i] );
							KeyFactory kf = KeyFactory.getInstance("ECDSA", "BC");
						    byte[] key = new byte[fr.available()];
						    fr.read(key, 0, fr.available());
						    fr.close();
						
						    PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec(key);
						    PrivateKey p = kf.generatePrivate(keysp);
						    
							this.personalCertificates.add( new PersonalCertificate((int)(Math.random()*100000), c, p ) );
						}
					}
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	/** 
	 *  Get a random certificate certificate
	 *  
	 *  @param id The id of certificate which you want
	 */
	public PersonalCertificate getCertificate() 
	{
		int certNumber = (int)(Math.random()*this.personalCertificates.size());
		return this.personalCertificates.get(certNumber);
	}
	
}