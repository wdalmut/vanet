package vanet.security;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * This class contains  personal certificates and keys
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public class PersonalCertificate 
{
	/**
	 * The ID of certificate
	 */
	private int id;
	/**
	 * The certificate
	 */
	private X509Certificate certificate;
	/**
	 * Private key linked with the certificate
	 */
	private PrivateKey privateKey;
	
	/**
	 * Self generated certificate 
	 * 
	 * @deprecated unuseful variable
	 */
	private byte[] selfCertificate;
	
	/**
	 * Constructor of personal certificate class
	 * 
	 * @param id The ID of couple certificate-private key
	 * 
	 * @param certificate The x509 certificate
	 * @param privateKey The private key
	 */
	public PersonalCertificate( int id, X509Certificate certificate, PrivateKey privateKey )
	{
		this.id = id;
		this.certificate = certificate;
		this.privateKey = privateKey;
	}
	
	/**
	 * Constructor of personal certificate class
	 * 
	 * @deprecated Don't use this constructor
	 * 
	 * @param id The certificate ID
	 * @param selfCertificate The Self certiticate in byte reperesetation
	 * @param privateKey The private key
	 */
	public PersonalCertificate( int id, byte[] selfCertificate, PrivateKey privateKey)
	{
		this.id = id;
		this.setSelfCertificate(selfCertificate);
		this.privateKey = privateKey;
	}
	/**
	 * This function set the certificate
	 * 
	 * @deprecated You have to use the certificate constructor
	 * 
	 * @param certificate the certificate to set
	 */
	public void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}

	/**
	 * Retrive the certificate
	 * 
	 * @return the certificate
	 */
	public X509Certificate getCertificate() {
		return certificate;
	}

	/**
	 * Set the private key
	 * 
	 * @deprecated Do not use, you have to set it using the constructor
	 * 
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(PrivateKey privateKey) 
	{
		this.privateKey = privateKey;
	}

	/**
	 * Retrive the private key
	 * 
	 * @return the privateKey
	 */
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * Set the ID of certificate
	 * @deprecated Do not use, you have to use the constructor of class
	 * 
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Retrive the ID of certificate
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public void setSelfCertificate(byte[] selfCertificate) {
		this.selfCertificate = selfCertificate;
	}

	public byte[] getSelfCertificate() {
		return selfCertificate;
	}
	
	

}