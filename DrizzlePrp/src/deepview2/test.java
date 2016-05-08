/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

/**
 *
 * @author user
 */
public class test {
    public static class testc
    {
        int a;
    }
    public static void test()
    {
        try{
            testc t = new testc();
            t.a = 1;
            java.lang.reflect.Field[] fields = t.getClass().getDeclaredFields();
            Object o = fields[0].get(t);
            Integer o2 = (Integer)o;
            o2 = 2;
            int wha = t.a;
        }catch(Exception e)
        {
            int dummy=0;
        }
    }
}
