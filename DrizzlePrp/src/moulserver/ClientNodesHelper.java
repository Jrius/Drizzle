/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import shared.*;
import java.util.Map;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClientNodesHelper
{

    public static class NodesInfo
    {
        public NodeManager mgr;
    }

    private static Set<Integer> UniqueIndexes(int rootIdx, ArrayList<Node.Ref> refs)
    {
        LinkedHashSet<Integer> r = new LinkedHashSet();
        r.add(rootIdx);
        for(Node.Ref ref: refs)
        {
            r.add(ref.childIdx);
        }
        return r;
    }
    public static ArrayList<byte[]> GetRawNodes(Client.AuthConnection authconn, int rootNode)
    {
        ArrayList<Node.Ref> refs = authconn.GetNodeRefs(rootNode);

        //wait for all nodes to be downloaded.
        Set<Integer> idxes = UniqueIndexes(rootNode,refs);
        final ArrayList<byte[]> rawnodes = new ArrayList();

        final CountDownLatch latch = new CountDownLatch(idxes.size());
        for(int idx: idxes)
        {
            //mgr.AddNode(authconn.GetNode(idx));
            authconn.GetNodeWithTransidCallback(idx, new Concurrent.Callback<Server.ServerMsg>(){
                public void callback(Server.ServerMsg msg)
                {
                    //This will take place in the read thread, so be careful.
                    AuthServer.VaultNodeFetched msg2 = msg.cast();
                    //Node node = Node.getNode(msg2.data);
                    //add this node, and decrement the latch counter.
                    rawnodes.add(msg2.data);
                    latch.countDown();
                }
            });
        }
        try{
            latch.await();
        }catch(Exception e){
            throw new nested(e);
        }

        return rawnodes;
    }

    public static NodesInfo GetNodes(Client.AuthConnection authconn, int rootNode)
    {
        //This isn't the fastest, because we're waiting for each reply before proceeding.  But it's safe and simple.
        //Node.PlayerNode player = authconn.GetNode(playermsg.playerId);
        ArrayList<Node.Ref> refs = authconn.GetNodeRefs(rootNode);
        //for(Node.Ref ref: refs)
        //{
        //    m.msg(ref.toString());
        //}
        final NodeManager mgr = new NodeManager(rootNode,refs);
        //mgr.AddNode(player);

        //wait for all nodes to be downloaded.
        final CountDownLatch latch = new CountDownLatch(mgr.allIdxes.size());
        for(int idx: mgr.allIdxes)
        {
            //mgr.AddNode(authconn.GetNode(idx));
            authconn.GetNodeWithTransidCallback(idx, new Concurrent.Callback<Server.ServerMsg>(){
                public void callback(Server.ServerMsg msg)
                {
                    //This will take place in the read thread, so be careful.
                    AuthServer.VaultNodeFetched msg2 = msg.cast();
                    Node node = Node.getNode(msg2.data);
                    //add this node, and decrement the latch counter.
                    mgr.AddNode(node);
                    latch.countDown();
                }
            });
        }
        try{
            latch.await();
        }catch(Exception e){
            throw new nested(e);
        }

        NodesInfo result = new NodesInfo();
        result.mgr = mgr;
        return result;
        //mgr.report();
    }

    public static class NodeManager
    {
        MgrNode root;
        private Map<Integer,MgrNode> idxLookup = Concurrent.getThreadsafeHashMap();
        public HashSet<Integer> allIdxes = new HashSet();
        //public HashMap<Integer,Node> nodes = new HashMap();

        public NodeManager(int rootNodeIdx, ArrayList<Node.Ref> treeRefs)
        {
            //root = new MgrNode(rootNodeIdx);
            root = Get(rootNodeIdx);
            for(Node.Ref ref: treeRefs)
            {
                MgrNode parent = Get(ref.parentIdx);
                MgrNode child = Get(ref.childIdx);
                parent.children.add(child);
            }
            //there could in principle be lost nodes here, but the server shouldn't have send such a message.
        }
        public void AddNode(Node node)
        {
            MgrNode mgrnode = idxLookup.get(node.getIdx());
            mgrnode.node = node;
        }

        private MgrNode Get(int idx)
        {
            MgrNode node = idxLookup.get(idx);
            if(node==null)
            {
                node = new MgrNode(idx);
                idxLookup.put(idx, node);
            }
            return node;
        }

        public class MgrNode
        {
            int idx;
            Node node;
            ArrayList<MgrNode> children = new ArrayList();

            public MgrNode(int idx)
            {
                this.idx = idx;
                idxLookup.put(idx, this);
                allIdxes.add(idx);
            }
        }

        public void report()
        {
            report(root,0);
        }
        private void report(MgrNode mnode, int depth)
        {
            StringBuilder s = new StringBuilder();
            for(int i=0;i<depth;i++) s.append("  ");
            s.append(mnode.node.toString());
            m.msg(s.toString());

            for(MgrNode child: mnode.children)
            {
                report(child,depth+1);
            }
        }
    }

}
