/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mystProxy;

import shared.m;
import java.io.IOException;

public class handlingException extends IOException
{
    public handlingException(String message)
    {
        m.warn(message);
    }
}
