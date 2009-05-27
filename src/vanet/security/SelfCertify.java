package vanet.security;

import java.security.*;

public class SelfCertify {
	
	private PublicKey pk;
	private byte[] signedkey;

	public SelfCertify(PublicKey pk,byte[] signedkey){
		
		this.pk=pk;
		this.signedkey=signedkey;
	}
	
	public PublicKey getPubKey(){
		return this.pk;
	}
	
	public byte[] getSignedKey(){
		return this.signedkey;
			}
}
