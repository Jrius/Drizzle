/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.vault;

import java.util.Vector;

public class Nodes
{
    public Vector<Node> nodes;
    
    public Nodes()
    {
        nodes = new Vector();
    }
    
    public void add(Node n)
    {
        nodes.add(n);
    }

    public static Nodes createFromNodeVector(Vector<Node> nodes)
    {
        Nodes result = new Nodes();
        result.nodes = nodes;
        return result;
    }

    public Vector<Node> findNodesOfType(nodetype type)
    {
        Vector<Node> result = new Vector();
        //for(vfile vf: vfiles)
        for(Node n: nodes)
        {
            if(n.type==type)
            {
                result.add(n);
            }
        }
        return result;
    }
    
    public String getAvatarName(int KInumber)
    {
        for(Node n: nodes)
        {
            if(n.type==nodetype.PlayerInfoNode)
            {
                if(n.PlayerInfoNode_GetKINumber()==KInumber)
                {
                    return n.PlayerInfoNode_GetAvatarName();
                }
            }
        }
        return null;
    }
    
    public Vector<Node> getMarkers(int KInumber, String age, int blob1)
    {
        Vector<Node> result = new Vector();
        for(Node n: nodes)
        {
            if(n.type==nodetype.MarkerNode)
            {
                if(n.owner==KInumber)
                {
                    if(n.blob1==blob1)
                    {
                        if(n.age_name.toString().equals(age))
                        {
                            result.add(n);
                        }
                    }
                }
            }
        }
        return result;
    }
    
    public void sortByCreationDate()
    {
        java.util.Collections.sort(nodes, new java.util.Comparator() {
            public int compare(Object o1, Object o2) {
                Node n1 = (Node)o1;
                Node n2 = (Node)o2;
                long l1 = n1.crt_time.toLong();
                long l2 = n2.crt_time.toLong();
                if(l1==l2) return 0;
                if(l1<l2) return -1;
                return 1;
            }
        });
    }
}
