/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

//import uru.server.Manifest;
import shared.Concurrent;
import prpobjects.Guid;
import shared.*;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import prpobjects.uruobj;
import uru.Bytedeque;
//import java.util.Vector;
import uru.server.*;

//import java.nio.channels.SocketChannel;
//import java.nio.channels.ServerSocketChannel;
//import java.nio.channels.Selector;
//import java.nio.channels.spi.SelectorProvider;
//import java.nio.channels.SelectionKey;
import java.net.InetSocketAddress;
//import java.util.Iterator;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Future;
//import java.util.concurrent.ConcurrentHashMap;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.net.SocketException;
import java.util.Queue;

public class NetServer extends Thread
{
    static final int msToSleep = 40; //was 40

    static final boolean readfirst = true;
    static final boolean allatonce = true; //if not, this screws up Cyan's code :P  It expects it to all be in a single packet.


    static final byte kNetCliCli2SrvConnect = 0;
    static final byte kNetCliSrv2CliEncrypt = 1;
    static final byte kNetCliSrv2CliError = 2;

    //ConcurrentHashMap<SocketChannel,ConnectionState> sockets = new ConcurrentHashMap();
    private Set<ConnectionState> connstates = Concurrent.getConcurrentSet();
    public void removeConnectionState(ConnectionState connstate)
    {
        connstates.remove(connstate);
    }

    AbstractManager manager;
    public NetServer(AbstractManager manager)
    {
        this.manager = manager;
    }

    public void run()
    {

        final int port = 14617;
        final int connectionRequestBacklog = 20; //the number of connection request that are allowed to be waiting.
        final long msBeforeTimeout = 200; //just wait a short time.

        m.msg("Starting Net Server...");


        try
        {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(port), connectionRequestBacklog);

            //object reading threadpool:
            //ExecutorService executor = Executors.newCachedThreadPool();

            while(true)
            {
                Socket sock = server.accept(); //blocks here, but calling server.close() from another thread will make this throw an exception.
                ConnectionState connstate = new ConnectionState(sock, manager);
                connstates.add(connstate);
                connstate.startReader();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }




    
}
