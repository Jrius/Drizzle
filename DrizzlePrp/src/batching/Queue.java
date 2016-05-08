/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package batching;

import java.util.ArrayDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Future;
import shared.m;

public class Queue
{
    //private ArrayDeque<QueueItem> queue = new ArrayDeque();
    ExecutorService a = java.util.concurrent.Executors.newSingleThreadExecutor();
    ThreadPoolExecutor b = new java.util.concurrent.ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>());
    //java.util.concurrent.

    public Future AddItem(QueueItem item) //returns right away.
    {
        //queue.addLast(item);
        //b.
        Future result = a.submit(item);
        return result;
    }

    /*public void waitFor()
    {
        try{
            a.awaitTermination(1000, TimeUnit.DAYS);
        }catch(Exception e){
            m.err("Exception while waiting for thread.");
        }
    }*/
    /*public void RunNextItem()
    {
        //queue
        a.
    }*/

    /*private static class WorkThread extends Thread
    {
        QueueItem item;

        public WorkThread(QueueItem item)
        {
            this.item = item;
        }

        @Override public void run()
        {
            item.run();
        }
    }*/
}
