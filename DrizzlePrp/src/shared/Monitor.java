/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class Monitor
{
    boolean done = false;
    
    public void waitCorrectly()
    {
        //if(true)return;
        synchronized(this)
        {
            while(!done)
            {
                try
                {
                    //m.msg("waiting");
                    this.wait();
                }            
                catch(InterruptedException e){}
            }
        }
    }
    public void notifyCorrectly()
    {
        synchronized(this)
        {
            //m.msg("notifying");
            done = true;
            this.notifyAll();
        }
    }
            
}
