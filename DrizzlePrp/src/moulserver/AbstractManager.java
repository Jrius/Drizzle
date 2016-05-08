/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

/**
 *
 * @author user
 */
public abstract class AbstractManager
{
    public abstract void HandleMessage(ServerType servertype, Server.ServerMsg msg, ConnectionState cs);

    public String getPathToCleanFiles()
    {
        throw new shared.uncaughtexception("unimplemented");
    }
    public void removeConnectionState(ConnectionState cs)
    {
        throw new shared.uncaughtexception("unimplemented");
    }
    public AgesInfo getagesinfo()
    {
        throw new shared.uncaughtexception("unimplemented");
    }
}
