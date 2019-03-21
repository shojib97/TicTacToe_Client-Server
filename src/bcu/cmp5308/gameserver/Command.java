package bcu.cmp5308.gameserver;

import java.io.IOException;

/*
 * Command Interface to implement by server commands
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 */

public interface Command 
{
  public void execute() throws IOException;
  
}
