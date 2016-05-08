/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.io.PrintStream;
import java.io.PrintWriter;

public class nested extends java.lang.RuntimeException
{
    private static final boolean renest = false; //whether to embed a nestedexception in a nestedexception.

    public Exception e;
    public String msg;

    public nested(Exception e)
    {
        this(e,null);
    }

    public void printStackTrace(PrintStream s)
    {
        m.err("Nested stacktrace:");
        e.printStackTrace(s);
        m.err("Our stacktrace:");
        super.printStackTrace(s);
    }

    public void printStackTrace(PrintWriter s)
    {
        m.err("Nested stacktrace:");
        e.printStackTrace(s);
        m.err("Our stacktrace:");
        super.printStackTrace(s);
    }
    
    public nested(Exception e, String msg)
    {
        if(renest)
        {
            this.e = e;
        }
        else
        {
            if(e instanceof nested)
                this.e = ((nested)e).e;
            else
                this.e = e;
        }
        //m.err(e.getMessage());
        this.msg = msg;
        //m.err(msg);
    }
    
    public String getMessage()
    {
        return "NestedException: "+e.getMessage();
    }

    //returns the first non-nested exception.  So e doesn't have to be a nestedexception.
    public static Exception getRootOfException(Exception e)
    {
        Exception cure = e;
        while(cure instanceof nested)
        {
            cure = ((nested)cure).e;
        }
        return cure;
    }

    
}
