/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;
import shared.uncaughtexception;
import shared.b;
import shared.e;
import shared.m;

public class Demarshal
{

    public int magicnum;
    public int timestamp;
    public PyCode code;

    public PythonVersions p;

    public Demarshal(IBytestream c/*, PythonXX p*/)
    {
        magicnum = c.readInt();
        if(magicnum==PythonVersions.Python22().MagicNumber())
        {
            p = PythonVersions.Python22();
            c.set("pyver", 22);
        }
        else if(magicnum==PythonVersions.Python23().MagicNumber())
        {
            p = PythonVersions.Python23();
            c.set("pyver", 23);
        }
        else
        {
            m.throwUncaughtException("Unexpected/no magic number.");
        }
        timestamp = c.readInt();
        code = PyObject.read(c).cast();
        
    }
}
