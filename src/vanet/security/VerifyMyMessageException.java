package vanet.security;

/**
 * This exception it's used for not verify messages in network for veichles.
 * 
 * Infact the system use the broadcast node for transmit and receive messages, during transmission the system receive the message which it have send, this exception it's launched for not verify personal messages.
 * 
 * @author Walter Dal Mut
 * @date 2009
 *
 */
public class VerifyMyMessageException extends Exception 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
