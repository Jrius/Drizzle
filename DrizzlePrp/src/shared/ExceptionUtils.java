/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.io.StringWriter;
import java.io.PrintWriter;

public class ExceptionUtils
{
    public static String ExceptionToString(Throwable t)
    {
        StringWriter result = new StringWriter();
        PrintWriter printer = new PrintWriter(result);
        t.printStackTrace(printer);
        return result.toString();
    }

}
