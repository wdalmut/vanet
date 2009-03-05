package vanet.security;

import java.security.cert.X509Certificate;

/** 
 *  This class rappresent the certificate of third veichle
 */
public class Certificate 
{
	/** 
	 *  The certificate
	 */
	private X509Certificate certificate;

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
	 * @param expiry the expiry to set
	 */
	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

	/**
	 * @return the expiry
	 */
	public long getExpiry() {
		return expiry;
	}
}