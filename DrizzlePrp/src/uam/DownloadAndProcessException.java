/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uam;

import shared.*;

public class DownloadAndProcessException extends RuntimeException
{
    public DownloadAndProcessException(String msg)
    {
        super(msg);
        m.err(msg);
    }

}
