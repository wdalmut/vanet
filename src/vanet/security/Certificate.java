package vanet.security;

import java.security.cert.X509Certificate;

/** 
 *  This class rappresent the certificate of third veichle
 */
public class Certificate 
{
	/** 
	 *  The certificate for base line
	 */
	private X509Certificate certificate;
	/** 
	 *  The certificate for base line
	 */
	private SelfCertify selfCertify;
	/** 
	 *  The validity of certificate
	 */
	private long expiry = System.currentTimeMillis();
	
	/**
	 * 
	 * @param certificate
	 */
	public Certificate(X509Certificate certificate )
	{
		this.setCertificate(certificate);
	}
// second constructor
	public Certificate(SelfCertify certificate2 )
	{
		this.setSelfCerty(certificate2);
	}
	/**
	 * @param set the self certify
	 */
	private void setSelfCerty(SelfCertify certificate2) {
	this.selfCertify=certificate2;
	
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
	 * @return the self certificate
	 */
	public SelfCertify getSelfCertify() {
		return this.selfCertify;
	}
	/**
	 * @param expiry the expire to set
	 */
	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

	/**
	 * @return the expire
	 */
	public long getExpiry() {
		return expiry;
	}
}