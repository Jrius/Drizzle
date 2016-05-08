/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3;

/**
 *
 * @author user
 */
public class options
{
    public static final boolean demangle = false; //we can demangle member names that look like __whatever, but there is no need to really.
    public static final boolean writeModuleGlobals = true; //shouldn't be needed since STORE_NAME and STORE_GLOBAL are functionally the same in the module's pycode, but doing this keeps the pycs identical.
    public static final boolean writeLoadGlobals = true; //use advanced method for determining if a var should be declared global, even if it is only loaded. (i.e. not written to or deleted)

    //attempts to handle python 2.3:
    public static final boolean jumpFalls = false; //used to handle the transitive jump optimisation from Python 2.3
    public static final boolean regenerateStructure = true; //used to reorder the LANDs in the token list so that it looks correct like Python 2.2.

    //optimizations:
    public static final boolean simpleLists = true; //creates a fake token to represent a simple list or tuple.

    //formatting:
    public static final boolean breakDictionariesOntoMultipleLines = false;
}
