package vanet.security;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class PersonalCertificate 
{
	private int id;
	private X509Certificate certificate;
	private PrivateKey privateKey;
	
	public PersonalCertificate( int id, X509Certificate certificate, PrivateKey privateKey )
	{
		this.id = id;
		this.certificate = certificate;
		this.privateKey = privateKey;
	}

	/**
	 * @param certificate the certificate to set
	 */
	public void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}

	/**
	 * @return the certificate
	 */
	public X509Certificate getCertificate() {
		return certificate;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(PrivateKey privateKey) 
	{
		this.privateKey = privateKey;
	}

	/**
	 * @return the privateKey
	 */
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	

}