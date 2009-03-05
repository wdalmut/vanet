package vanet.security;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import vanet.Configs;

/** 
 *  This class rappresent the repositoty of all certificate in wifi area
 *  
 */
public class CertificateStore 
{
  /**
   * repository of certificate
   */
  public Map<Integer, Certificate>  certificates;
  
  /**
   * Constructor of Certificate Store
   */
  public CertificateStore()
  {
	  this.certificates = new HashMap<Integer, Certificate>();
  }
  
  /** 
   *  Add certificate to repository
   */
  public void addCertificate(int id, X509Certificate certificate) 
  {
	  
	  Certificate c = new Certificate( certificate );
	  
	  this.certificates.put( new Integer(id), c);
  }

  /** 
   *  Get certificate of repositiory
   *  
   *  This class provide a trash system for remove old certificates
   */
  public X509Certificate getCertificate(int id) 
  {
	  Certificate certificate = this.certificates.get( id );
	  if( certificate == null ) return null; 
	  
	  long time = System.currentTimeMillis();
	  
	  if( (time-this.certificates.get(id).getExpiry())/1000 > Configs.MAX_CERTIFICATE_VALIDITY_TIME )
	  {
		  this.certificates.remove( id );
		  return null;
	  }
		  
	  return this.certificates.get( id ).getCertificate();
  }
}