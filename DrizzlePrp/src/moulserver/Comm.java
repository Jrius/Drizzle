/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Concurrent;
import java.util.concurrent.ConcurrentLinkedQueue; //is more efficient
import java.util.concurrent.LinkedBlockingQueue; //lets you block while waiting for an element.
import java.util.concurrent.PriorityBlockingQueue; //lets you define an ordering to move elements up.
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet; //a set, because lists aren't efficient in concurrency land.
import java.util.Queue;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
//import moulserver.NetServer.ConnectionState;
import shared.*;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import moulserver.Server.ServerMsg;
import prpobjects.Guid;

//for communicating between threads.

public class Comm
{
    //let's see...
    //each connection should have a listener object that it can poll.
    //this listener object can be tied to multiple things, whether events or messages it should send over the network or whatever.
    

    private final DustMap nodeChangeRegistrees = new DustMap();

    public void registerForNodeChange(int nodeIdx, Queue<CommItem> queue)
    {
        nodeChangeRegistrees.addQueue(nodeIdx, queue);
    }
    public void triggerNodeChange(int nodeIdx, CommItem msg)
    {
        nodeChangeRegistrees.triggerIdx(nodeIdx, msg);
    }

    public static class DustMap extends ConcurrentHashMap<Integer,Set<WeakReference<Queue<CommItem>>>>
    {
        public void addQueue(int idx, Queue<CommItem> queue)
        {
            WeakReference<Queue<CommItem>> ref = new WeakReference(queue);
            Set<WeakReference<Queue<CommItem>>> curset = getOrCreateSet(idx);
            curset.add(ref);
        }

        public Set<Queue<CommItem>> getCurrentSet(int idx)
        {
            Set<WeakReference<Queue<CommItem>>> curset = get(idx);
            if(curset==null)
            {
                return java.util.Collections.emptySet();
            }
            else
            {
                Set<Queue<CommItem>> r = new java.util.HashSet();
                for(WeakReference<Queue<CommItem>> ref: curset)
                {
                    Queue<CommItem> listener = ref.get();
                    if(listener!=null) r.add(listener);
                }
                return r;
            }
        }

        private synchronized Set<WeakReference<Queue<CommItem>>> getOrCreateSet(int idx)
        {
            //synchronized so that there is no contention between sets.  This never blocks for any significant time, so no problem.
            Set<WeakReference<Queue<CommItem>>> curset = get(idx);
            if(curset==null)
            {
                curset = Concurrent.getConcurrentSet();
                put(idx, curset);
            }
            return curset;
        }

        public void triggerIdx(int idx, CommItem msg)
        {
            Set<Queue<CommItem>> curset = getCurrentSet(idx);
            for(Queue<CommItem> listener: curset)
            {
                listener.add(msg);
            }
        }
    }




    private final Map<Integer,Listener> playerIdxs = new ConcurrentHashMap<Integer,Listener>();
    private final Map<Integer,Set<Listener>> nodeChangeListeners = new ConcurrentHashMap<Integer,Set<Listener>>();
    private final Map<Integer,Set<Listener>> nodeLinkListeners = new ConcurrentHashMap<Integer,Set<Listener>>();
    public Iterable<Listener> getNodeChangeListeners(Integer nodeIdx)
    {
        return nodeChangeListeners.get(nodeIdx);
    }
    public Iterable<Listener> getNodeLinkListeners(Integer nodeIdx)
    {
        return nodeLinkListeners.get(nodeIdx);
    }
    public Listener getPlayerIdxListener(Integer playerIdx)
    {
        return playerIdxs.get(playerIdx);
    }
    private static Set<Listener> getNodeListeners(Map<Integer,Set<Listener>> listenersMap, Integer nodeIdx)
    {
        synchronized(listenersMap) //needs to be synchronized so we don't have two threads both putting a value.
        {
            Set<Listener> listeners = listenersMap.get(nodeIdx);
            if(listeners==null)
            {
                //listeners = new ConcurrentSkipListSet<Listener>();
                //listeners = new ConcurrentHashMap<Listener>();
                listeners = Concurrent.getConcurrentSet();
                listenersMap.put(nodeIdx, listeners);
            }
            return listeners;
        }
    }

    public Listener MakeListener()
    {
        Listener listener = new Listener(this);
        return listener;
    }

    public static class Listener
    {
        private Queue<CommItem> mainQueue = new ConcurrentLinkedQueue<CommItem>();
        private Comm comm;
        
        public Listener(Comm comm)
        {
            this.comm = comm;
        }

        public void registerForNodeChange(int nodeIdx)
        {
            Comm.getNodeListeners(comm.nodeChangeListeners,nodeIdx).add(this);
        }

        public void registerForNodeLink(int nodeIdx)
        {
            Comm.getNodeListeners(comm.nodeLinkListeners,nodeIdx).add(this);
        }

        public void registerForPlayerIdx(int playerIdx)
        {
            comm.playerIdxs.put(playerIdx, this); //we *should* be the only one registering this player.
        }

        public boolean hasItem()
        {
            return (mainQueue.peek()!=null);
        }

        public CommItem getNextItem() //returns null if empty.
        {
            return mainQueue.poll();
        }

        public void addItem(CommItem item)
        {
            mainQueue.add(item);
        }

        public void unregister()
        {
            //unregisters everyting it has registered so far.
            m.err("unimplemented");
        }
    }

    /*public static abstract class CommItem
    {
        public <T> T cast()
        {
            return (T)this;
        }
    }*/

    public static class CommItem//_Old extends CommItem
    {
        CommItemType type;

        ServerMsg msg; //network message
        ConnectionState connstate;

        Integer nodeIdx;
        byte[] data;

        prpobjects.Guid guid;
        Node node;

        /*public CommItem(Object msg)
        {
            this.type = CommItemType.AuthMsg;
            this.msg = msg;
        }*/
        private CommItem(){}
        public static CommItem SendMessage(ServerMsg msg/*, ConnectionState connstate*/)
        {
            CommItem r = new CommItem();
            r.type = CommItemType.SendMessage;
            r.msg = msg;
            //r.connstate = connstate;
            return r;
        }
        public static CommItem HandleMessage(ServerMsg msg, ConnectionState connstate)
        {
            CommItem r = new CommItem();
            r.type = CommItemType.HandleMessage;
            r.msg = msg;
            r.connstate = connstate;
            return r;
        }
        public static CommItem NodeChange(AuthServer.VaultNodeChanged msg, ConnectionState sender)
        {
            CommItem r = new CommItem();
            r.type = CommItemType.NodeChange;
            //r.nodeIdx = nodeIdx;
            r.msg = msg;
            r.connstate = sender;
            return r;
        }
        public static CommItem Terminate()
        {
            CommItem r = new CommItem();
            r.type = CommItemType.Terminate;
            return r;
        }
        public static CommItem GetAgeSdl(Guid ageInstanceGuid)
        {
            CommItem r = new CommItem();
            r.type = CommItemType.GetAgeSdl;
            r.guid = ageInstanceGuid;
            return r;
        }
        public static CommItem ResponseNode(Node node)
        {
            CommItem r = new CommItem();
            r.type = CommItemType.ResponseNode;
            r.node = node;
            return r;
        }
        public static CommItem SaveNode(Node node)
        {
            CommItem r = new CommItem();
            r.type = CommItemType.SaveNode;
            r.node = node;
            return r;
        }
        //public static CommItem SendMsgBytes(byte[] data)
        //{
        //    CommItem r = new CommItem();
        //    r.type
        //}
        public CommItemType getType(){return type;}
    }

    public static enum CommItemType
    {
        //GateMsg,
        SendMessage,
        HandleMessage,

        //SendMsgBytes, //used for di
        Terminate,

        //vault triggers
        NodeChange,

        //messages to/from other servers
        GetAgeSdl,
        ResponseNode,
        SaveNode,
    }

    ConcurrentHashMap<CommItem,CommTrigger> callers = Concurrent.getConcurrentHashMap();
    public static class CommTrigger
    {
        private java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        private CommItem response;
        public CommTrigger()
        {
        }
        public void trigger(CommItem response)
        {
            this.response = response;
            latch.countDown();
        }
        public CommItem await()
        {
            while(true)
            {
                try{
                    latch.await();
                    break; //we'll only return if the trigger has been called properly.
                }catch(InterruptedException e){}
            }
            return response;
        }
    }
    public <T extends CommItem> T SendAndWait(CommItem msg, Queue<CommItem> receiver)
    {
        T r = (T) SendAndWait2(msg, receiver);
        return r;
    }
    public CommItem SendAndWait2(CommItem msg, Queue<CommItem> receiver)
    {
        CommTrigger trigger = new CommTrigger();
        callers.put(msg, trigger);
        receiver.add(msg);
        CommItem result = trigger.await();
        return result;
    }
    public void Respond(CommItem caller, CommItem response)
    {
        CommTrigger trigger = callers.remove(caller);
        trigger.trigger(response);
    }

    /*public static class CommItem_NodeChange extends CommItem
    {
        AuthServer.VaultNodeChanged msg;
        public CommItem_NodeChange(AuthServer.VaultNodeChanged msg)
        {
            this.msg = msg;
        }
    }*/


}
