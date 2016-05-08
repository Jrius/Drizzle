/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package alcugsinterface;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketException;
import shared.m;

public class client
{
    public static void listen()
    {
        int localport = 1027;
        int bufsize = 256;
        int remoteport = 5000;
        InetAddress add = null;
        try
        {
            add = java.net.InetAddress.getByName("shard.example.com");
        }
        catch(UnknownHostException e)
        {
            m.err("Unknown host!");
        }
        
        try
        {
            DatagramSocket socket = new DatagramSocket(localport);
        }
        catch(SocketException e)
        {
            m.err("Socket exception!");
        }
        byte[] buf = new byte[bufsize];
        DatagramPacket packet = new java.net.DatagramPacket(buf, bufsize);
        packet.setAddress(add);
        packet.setPort(remoteport);
    }
}
