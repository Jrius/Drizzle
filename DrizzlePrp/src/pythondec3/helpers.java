/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3;

import pythondec.*;
import shared.b;
import pythondec3.ast.*;

public class helpers
{
    public static String escapePythonString(PyString str)
    {
        StringBuilder r = new StringBuilder();
        for(byte by: str.rawstr)
        {
            switch(by)
            {
                case '\n':
                    r.append("\\n");
                    break;
                case '\r':
                    r.append("\\r");
                    break;
                case '\'':
                    r.append("\\'");
                    break;
                case '"':
                    r.append("\\\"");
                    break;
                case '\\':
                    r.append("\\\\");
                    break;
                case '\t':
                    r.append("\\t");
                    break;
                default:
                    if(by>=32 && by<=126)
                    {
                        r.append((char)by); //strait out
                    }
                    else
                    {
                        r.append("\\x"+b.ByteToHexString(by)); //escaped
                    }
                    break;
            }
        }
        String r2 = r.toString();
        return r2;
    }
    public static String escapeUnicodeString(String str)
    {
        StringBuilder r = new StringBuilder();
        for(char by: str.toCharArray())
        {
            switch(by)
            {
                case '\n':
                    r.append("\\n");
                    break;
                case '\r':
                    r.append("\\r");
                    break;
                case '\'':
                    r.append("\\'");
                    break;
                case '"':
                    r.append("\\\"");
                    break;
                case '\\':
                    r.append("\\\\");
                    break;
                case '\t':
                    r.append("\\t");
                    break;
                default:
                    if(by>=32 && by<=126)
                    {
                        r.append((char)by); //strait out
                    }
                    else
                    {
                        r.append("\\u"+b.CharToHexString(by)); //escaped
                    }
                    break;
            }
        }
        String r2 = r.toString();
        return r2;
    }


    public static String demangleName(sgen s, String funcname)
    {
        //this is perhaps a little inefficient, since we search the stack when we have a function invocation or definition.
        //we should demangle directly inside the LOAD_NAME and LOAD_FAST and LOAD_GLOBAL(?) tokens actually.
        //confirmed cases: STORE_NAME, STORE_ATTR, LOAD_ATTR.  I guess LOAD_NAME, STORE_FAST, and LOAD_FAST would cover it.

        //boolean demangle = true;
        if(options.demangle)
        {
            /*Stmt.Classdef cd = s.getFirstAncestorOfClass(Stmt.Classdef.class);
            if(cd!=null)
            {
                String classname = cd.nametostoreas.getGenString();
                String mangle = "_"+classname;
                int pos = funcname.lastIndexOf(mangle+"__");
                if(pos!=-1)
                {
                    //possible name mangling
                    String unmangledname = funcname.replace(mangle+"__", "__");
                    return unmangledname;
                }
            }
            return funcname;*/

            //this is a problem as it can't see far enough back to get the classname.
            Stmt.Classdef cd = s.getFirstAncestorOfClass(Stmt.Classdef.class);
            if(cd!=null)
            {
                String classname = cd.nametostoreas.getGenString();
                String mangle = "_"+classname;
                //int pos = funcname.lastIndexOf(mangle+"__");
                if(funcname.startsWith(mangle+"__"))
                {
                    //possible name mangling
                    //String unmangledname = funcname.replace(mangle+"__", "__");
                    String unmangledname = funcname.substring(mangle.length());
                    return unmangledname;
                }
            }
            return funcname;
        }
        else
        {
            return funcname;
        }
    }
}
