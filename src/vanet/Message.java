package vanet;

/**
 * This class rappresent a message
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public class Message {

  /** 
   *  The ID of certificate into message or in case of optimization, the certificate which the vehicle have to use for verify the signature
   */
  private int id;

  /** 
   *  The payload of message
   */
  private byte[] payload;

  /** 
   *  Signature of message
   */
  private byte[] signature;

  /** 
   *  The certificate for verify the message
   */
  private byte[] certificate;

  
  /** 
   *  Create new message using the optimization
   */
  public Message(int id, byte[] payload, byte[] signature)
  {
	  this.id = id;
	  this.payload = payload;
	  this.signature = signature;
	  
	  this.certificate = null;
  }

  /** 
   *  Create the message without optimization
   */
  public Message(int id, byte[] payload, byte[] signature, byte[] certificate) 
  {
	  this.id = id;
	  this.payload = payload;
	  this.signature = signature;
	  this.certificate = certificate;
  }

  /** 
   *  Retrive the ID of the message
   */
  public int getId()
  {
	  return this.id;
  }

  /** 
   *  Retrive the payload of the message
   */
  public byte[] getPayload() 
  {
	  return this.payload;
  }

  /** 
   *  Retrive the signature of the message
   */
  public byte[] getSignature()
  {
	  return this.signature;
  }

  /** 
   *  Retrive the certificate of the message, NULL in case of optimization
   */
  public byte[] getCertificate() 
  {
	  return this.certificate;
  }
}