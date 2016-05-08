/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.Stack;

public class StateStack<T extends Object>
{
    private Stack<T> previousStates = new Stack<T>();
    public T curstate;
    private boolean clone;
    private boolean deepclone;
    
    public StateStack(T startingState, boolean clone, boolean deepclone)
    {
        curstate = startingState;
        this.clone = clone;
        this.deepclone = deepclone;
    }
    
    private T createNew()
    {
        T result;
        if(clone && !deepclone)
        {
            result = shared.generic.createShallowClone(curstate);
        }
        else if(clone && deepclone)
        {
            result = shared.generic.createSerializedClone(curstate);
        }
        else
        {
            result = (T)shared.generic.createObjectWithDefaultConstructor(curstate.getClass());
        }
        return result;
    }
    public void push()
    {
        previousStates.push(curstate);
        //creatable c = (creatable)curstate;
        //creatable c2 = c.create();
        
        //curstate = curstate.create();
        curstate = this.createNew();
    }
    public void pop()
    {
        curstate = previousStates.pop();
        //m.state = previousStates.pop();
    }

}
