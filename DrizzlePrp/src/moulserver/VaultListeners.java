/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import java.util.Queue;
import moulserver.Comm.*;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashSet;
import shared.*;
import java.util.ArrayList;
import moulserver.Node.Ref;
import prpobjects.Guid;

//should only be run from within the AuthServer thread.
public class VaultListeners
{

    //maps from nodeIdx->set of queues
    private HashMap<Integer,LinkedHashSet<WeakReference<Queue<CommItem>>>> listeners = new HashMap();
    //maps from queue->info
    private HashMap<WeakReference<Queue<CommItem>>,ListenInfo> infos = new HashMap();

    public VaultListeners()
    {
    }

    private static class ListenInfo
    {
        Integer playerIdx;
        Integer currentAgeIdx;
    }

    public void SubscribeIfApplicable(int nodeIdx, WeakReference<Queue<CommItem>> items, ArrayList<Ref> refs)
    {
        Node root = Node.getNodeWithIndex(nodeIdx);
        int roottype = root.getType();
        if(roottype==Node.NodeType.kNodePlayer)
        {
            m.msg("Subscribing to PlayerNode: "+Integer.toString(nodeIdx));
            subscribe(items,nodeIdx,refs,1);
        }
        else if(roottype==Node.NodeType.kNodeAge)
        {
            m.msg("Subscribing to AgeNode: "+Integer.toString(nodeIdx));
            subscribe(items,nodeIdx,refs,2);
        }
        else
        {
            m.msg("Subscribing to children: "+Integer.toString(nodeIdx));
            subscribe(items,nodeIdx,refs,0);
        }
    }

    
    private void sendMsgToNodeListeners(int nodeIdx, CommItem msg)
    {
        LinkedHashSet<WeakReference<Queue<CommItem>>> set = listeners.get(nodeIdx);
        if(set!=null)
        {
            for(WeakReference<Queue<CommItem>> queueref: set)
            {
                Queue<CommItem> queue = queueref.get();
                if(queue!=null)
                {
                    queue.add(msg);
                }
            }
        }
    }
    /*private void sendMsgToNodeListenersAndAdd(int nodeIdx, CommItem msg)
    {
        LinkedHashSet<WeakReference<Queue<CommItem>>> set = listeners.get(nodeIdx);
        if(set!=null)
        {
            for(WeakReference<Queue<CommItem>> queueref: set)
            {
                Queue<CommItem> queue = queueref.get();
                if(queue!=null)
                {
                    //register children
                    queue.add(msg);
                }
            }
        }
    }*/

    public void SignalNodeChanged(int nodeIdx, Guid revision)
    {
        AuthServer.VaultNodeChanged msg = new AuthServer.VaultNodeChanged();
        msg.nodeId = nodeIdx;
        msg.revisionId = revision;
        sendMsgToNodeListeners(nodeIdx, Comm.CommItem.SendMessage(msg));
    }
    public void SignalNodeAdded(int parentIdx, int childIdx, int ownerIdx)
    {
        //do we need to sign up all the children now?
        //Probably not until the client requests the refs.

        //notify
        AuthServer.VaultNodeAdded msg = new AuthServer.VaultNodeAdded();
        msg.parentId = parentIdx;
        msg.childId = childIdx;
        msg.ownerId = ownerIdx;
        sendMsgToNodeListeners(parentIdx, Comm.CommItem.SendMessage(msg));
    }
    public void SignalNodeDeleted(int nodeIdx)
    {
        m.throwUncaughtException("unimplemented SignalNodeDeleted");
    }

    private void subscribe(WeakReference<Queue<CommItem>> items, int nodeIdx, ArrayList<Ref> refs, int type)
    {
        //get info for this queue (i.e. AuthServer connection)
        ListenInfo info = infos.get(items);

        //add to infos if we don't already have it
        if(info==null)
        {
            info = new ListenInfo();
            infos.put(items, info);
        }

        //handle old values
        if(type==1 && info.playerIdx!=null)
        {
            throw new uncaughtexception("Tried to subscribe to a second PlayerNode.");
        }
        else if(type==2 && info.currentAgeIdx!=null)
        {
            unsubscribe(items, info.currentAgeIdx);

        }
        else
        {
            if(type==1)
                info.playerIdx = nodeIdx;
            else if(type==2)
                info.currentAgeIdx = nodeIdx;

            //add root
            addlistener(nodeIdx,items);
            //add all children
            for(Ref ref: refs)
            {
                addlistener(ref.childIdx,items);
            }
        }
    }

    private void addlistener(int nodeIdx, WeakReference<Queue<CommItem>> items)
    {
        //add to listeners
        LinkedHashSet<WeakReference<Queue<CommItem>>> set = listeners.get(nodeIdx);
        //create if not already:
        if(set==null)
        {
            set = new LinkedHashSet();
            listeners.put(nodeIdx, set);
        }
        set.add(items);
    }

    private void unsubscribe(WeakReference<Queue<CommItem>> items, int nodeIdx)
    {
        m.warn("Unsubscribe not implemented yet.");
    }
}
