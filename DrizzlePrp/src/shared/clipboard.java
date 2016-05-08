/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

public class clipboard
{
    public static void SetString(String str)
    {
        StringSelection s = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, null);
    }

}
