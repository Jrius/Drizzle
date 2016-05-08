/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

/**
 *
 * @author user
 */
public class Python23 extends PythonVersions
{

    static final int Python23Magic = 0x0A0DF23B;
    static final String[] cmp_op = new String[]{"<", "<=", "==", "!=", ">", ">=", "in", "not in", "is", "is not", "exception match", "BAD"};

    public boolean recreateStructure()
    {
        return true;
    }
    public int MagicNumber()
    {
        return Python23Magic;
    }
    public String[] cmp_ops()
    {
        return cmp_op;
    }

}
