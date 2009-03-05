package vanet;

/** 
 *  This class rappresent a point into map
 */
public class Position {

  /** 
   *  The x position of the veichle inside wireless area
   */
  public int x;

  /** 
   *  The y position of the veichle inside wireless area
   */
  public int y;

  
  /** 
   *  Retrive X position of the veichle
   */
  public int getX() 
  {
	  return this.x;
  }

  /** 
   *  Retrive Y position of the veichle
   */
  public int getY() 
  {
	  return this.y;
  }

  /** 
   *  Create new Position object
   */
  public Position(int x, int y) 
  {
	  this.x = x;
	  this.y = y;
  }

  /** 
   *  Set the X position
   */
  public void setX(int x)
  {
	  this.x = x;
  }

  /** 
   *  Set the Y position
   */
  public void setY(int y) 
  {
	  this.y = y;
  }
}