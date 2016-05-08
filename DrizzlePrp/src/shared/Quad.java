/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class Quad<S, T, R, Q>
{
    public S val1;
    public T val2;
    public R val3;
    public Q val4;
    
    public Quad(S val1, T val2, R val3, Q val4)
    {
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.val4 = val4;
    }
    
    public static <S,T,R,Q> Quad<S,T,R,Q> create(S val1, T val2, R val3, Q val4)
    {
        Quad<S,T,R,Q> result = new Quad<S,T,R,Q>(val1, val2, val3,val4);
        return result;
    }
    
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof Quad)) return false;
        Quad p = (Quad)o;
        if(!val1.equals(p.val1)) return false;
        if(!val2.equals(p.val2)) return false;
        if(!val3.equals(p.val3)) return false;
        if(!val4.equals(p.val4)) return false;
        return true;
    }
}
