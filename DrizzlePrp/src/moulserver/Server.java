/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import prpobjects.uruobj;

public abstract class Server
{
    public static abstract class ServerMsg extends uruobj
    {
        public byte[] originalBytes; //may or may not be set.

        public abstract byte[] GetMsgBytes();

        public <T extends ServerMsg> T cast()
        {
            T r = (T)this;
            return r;
        }

        public Integer transid() //returns null by default
        {
            return null; //should be overridden in subclasses with a transid.
        }

        public String dump() //returns debugging info, possibly on multiple lines.
        {
            return this.getClass().getSimpleName();
        }
    }

    //public static interface Transaction
    //{
    //    public int transid();
    //}

    /*public enum ServerType
    {
        gate,
        file,
        auth,
        game,
    }*/
}
