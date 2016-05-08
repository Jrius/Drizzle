/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.vault;

import shared.*;
import uru.UruCrypt;
import java.util.Vector;

public class vaultdatfile
{
    public Vector<Node> nodes;

    private vaultdatfile(){}
    
    public static vaultdatfile createFromFilename(String filename)
    {
        vaultdatfile result = new vaultdatfile();
        
        byte[] dataenc = FileUtils.ReadFile(filename);
        byte[] data = UruCrypt.DecryptWhatdoyousee(dataenc);
        IBytestream c = ByteArrayBytestream.createFromByteArray(data);
        
        byte b1  = c.readByte();
        int u2 = c.readInt();
        int u3 = c.readInt();
        byte b4 = c.readByte();
        int u5 = c.readInt();
        int u6 = c.readInt();
        int u7 = c.readInt(); //index of last node
        int u8 = c.readInt(); //number of nodes
        
        result.nodes = new Vector();
        for(int curnode=0;curnode<u8;curnode++)
        {
            Node node = new Node(c);
            result.nodes.add(node);
        }
        
        int bytesleft = c.getBytesRemaining();
        if(bytesleft!=0)
        {
            //still have to parse the table.
            m.warn("Not parsing final table in vault.dat");
            int dummy=0;
        }
        
        return result;
    }
}
