/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package batching;

/**
 *
 * @author user
 */
public abstract class QueueItem implements Runnable
{
    public boolean isModal = true;
    public boolean waitTilDone = true;

    public abstract void run();
}
