package vanet.security;

import vanet.Message;

public interface SecurityBox {

  
  /** 
   *  Securize the payload
   */
  public byte[] securize(byte[] payload);

  /** 
   *  Verify the message which the veichle have receive by other veichles
   */
  public boolean verify(Message message);
}