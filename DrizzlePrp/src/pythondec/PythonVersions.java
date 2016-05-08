/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

/**
 *
 * @author user
 */
public abstract class PythonVersions
{
    abstract int MagicNumber();
    abstract String[] cmp_ops();
    abstract boolean recreateStructure();

    private static PythonVersions _python22;
    private static PythonVersions _python23;

    public static PythonVersions Python22()
    {
        if(_python22==null) _python22 = new Python22();
        return _python22;
    }

    public static PythonVersions Python23()
    {
        if(_python23==null) _python23 = new Python23();
        return _python23;
    }
}
