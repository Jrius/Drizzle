/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class Pair<S, T>
{
    public S left;
    public T right;
    
    public Pair(S left, T right)
    {
        this.left = left;
        this.right = right;
    }
    
    public static <S,T> Pair<S,T> create(S left, T right)
    {
        Pair<S,T> result = new Pair<S,T>(left, right);
        return result;
    }
    
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof Pair)) return false;
        Pair p = (Pair)o;
        if(!left.equals(p.left)) return false;
        if(!right.equals(p.right)) return false;
        return true;
    }

    public int hashCode()
    {
        return left.hashCode()<<16 + right.hashCode();
    }
}
