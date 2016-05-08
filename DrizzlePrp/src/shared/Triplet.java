/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class Triplet<S, T, R>
{
    public S val1;
    public T val2;
    public R val3;
    
    public Triplet(S val1, T val2, R val3)
    {
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }
    
    public static <S,T,R> Triplet<S,T,R> create(S val1, T val2, R val3)
    {
        Triplet<S,T,R> result = new Triplet<S,T,R>(val1, val2, val3);
        return result;
    }
    
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof Triplet)) return false;
        Triplet p = (Triplet)o;
        if(!val1.equals(p.val1)) return false;
        if(!val2.equals(p.val2)) return false;
        if(!val3.equals(p.val3)) return false;
        return true;
    }
}
