/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

/**
 *
 * @author user
 */
public class NodeManager
{
    int rootIdx;

    public static NodeManager CreateNewNodeManager(int rootIdx)
    {
        //The idea here is to lock modifications to the tables while doing this.
        //The plan:
        //  Lock while reading all the refs and send the refs out asap.
        //  Send notifications even before the client has downloaded all the nodes.  Hopefully that's okay.
        //We could also implement security in here, if we move all vault accesses to this class.
        //Hmm... since the auth server is a single thread, there is no need to lock.

        NodeManager r = new NodeManager();
        r.rootIdx = rootIdx;
        return r;
    }
    private NodeManager(){}

    

    /*int rootNodeIdx;
    ConnectionState authCs;

    public NodeManager(int rootNodeIdx, ConnectionState authCs)
    {
        this.rootNodeIdx = rootNodeIdx;
        this.authCs = authCs;
    }

    public void DownloadCurrent()
    {
        RequestNode(rootNodeIdx);

        RequestRefs(rootNodeIdx);
    }

    private void RequestNode(int nodeIdx)
    {
        AuthServer.VaultNodeFetch request = new AuthServer.VaultNodeFetch();
        request.transId = MoulagainClient.getNextTransid();
        request.nodeId = nodeIdx;
        AuthServer.SendMsg(authCs,request);
    }

    private void RequestRefs(int nodeIdx)
    {
        AuthServer.VaultFetchNodeRefs req2 = new AuthServer.VaultFetchNodeRefs();
        req2.transId = MoulagainClient.getNextTransid();
        req2.nodeId = nodeIdx;
        AuthServer.SendMsg(authCs, req2);
    }*/
}
