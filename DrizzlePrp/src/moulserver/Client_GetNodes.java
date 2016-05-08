/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Concurrent;
import moulserver.GateServer.*;
import shared.*;
import java.util.ArrayList;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.Map;
import moulserver.ClientNodesHelper.*;

public class Client_GetNodes extends Client
{
    String username;
    String password;
    String outputpath;
    String playername;
    public Client_GetNodes(String username, String password, String playername, String outputpath)
    {
        this.username = username;
        this.password = password;
        this.playername = playername;
        this.outputpath = outputpath;
    }

    public void GetPlayerNodes()
    {
        //the shard version
        Version ver = Version.currentMoulagain;

        //connect to the auth server
        Client.AuthConnection authconn = new Client.AuthConnection(ver, ver.authserver);

        //login
        boolean loginsucceeded = authconn.Login(username,password);

        AuthServer.AcctPlayerInfo playermsg = authconn.GetPlayer(playername);

        NodesInfo nodes = authconn.GetNodes(playermsg.playerId);

        authconn.Disconnect();

        //all done!
        m.msg("All done!");

    }

    public void GetNodes(int rootNodeIdx)
    {
        //the shard version
        Version ver = Version.currentMoulagain;

        //connect to the auth server
        Client.AuthConnection authconn = new Client.AuthConnection(ver, ver.authserver);

        //login
        boolean loginsucceeded = authconn.Login(username,password);

        AuthServer.AcctPlayerInfo playermsg = authconn.GetPlayer(playername);

        NodesInfo nodes = authconn.GetNodes(rootNodeIdx);

        //all done!
        m.msg("All done!");

    }


}
