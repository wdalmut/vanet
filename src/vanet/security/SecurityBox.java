package vanet.security;

import vanet.Message;

/**
 * Interface for security implementations
 * 
 * This interface provide a screen for implementation because in this simulator you can
 * use two different mode: BaseLine Pseudonyms and Hybrid Scheme
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public interface SecurityBox {

  
  /** 
   *  Securize the payload and the ID of message which you want send on broadcast node
   *  
   *  @param payload The payload that you want securize
   */
  public byte[] securize(byte[] payload);

  /** 
   *  Verify the message which the veichle have receive by other veichles
   *  
   *  @param message Message which you want test for security reasons
   *  
   *  @see vanet.Message
   */
  public boolean verify(Message message) throws VerifyMyMessageException ;
}