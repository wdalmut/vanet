package vanet.security;

import vanet.Message;

/** 
 *  The Hybrid Scheme implementation
 */
public class HybridScheme implements SecurityBox {

  public Message message;

    public CertificateStore certificateStore;

  public void verifySignKey() {
  }

  public void signKey() {
  }

  public void genOnTheFlyKey() {
  }

@Override
public byte[] securize(byte[] payload) 
{
	// TODO Auto-generated method stub
	return null;
}

@Override
public boolean verify(Message message) 
{
	// TODO Auto-generated method stub
	return false;
}
}