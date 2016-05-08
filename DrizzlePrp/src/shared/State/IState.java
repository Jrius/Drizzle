/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared.State;

/**
 *
 * @author user
 */
public interface IState
{
    //void setDefault(Object obj);
    Object getDefault();
    void putStateValue(Object obj);
    Object getStateValue();
    String getStateName();
    void initialise();
}
