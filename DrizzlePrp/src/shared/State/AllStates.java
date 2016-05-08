/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared.State;

import java.util.Vector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
//import java.util.Map;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.Stack;

public class AllStates //implements java.io.Serializable
{
    /*static class pair implements java.io.Serializable
    {
        String name;
        transient IState ref;
        Object val;
        
        pair(String name, IState ref, Object val)
        {
            this.name = name;
            this.ref = ref;
            this.val = val;
        }
    }*/
    
    //transient static Vector<AllStates> allStates;
    //static HashMap<String, Object> allStates;
    //static HashMap<String, IState> refs;
    //static HashMap<IState, Object> allStates;
    //static Vector<pair> states;
    static HashMap<String, Object> states;
    static Vector<IState> refs;
    
    static Stack<HashMap<String, Object>> stackedStates;
    
    static
    {
        //allStates = new Vector<AllStates>();
        //allStates = new HashMap<String, Object>();
        //refs = new HashMap<String, IState>();
        //allStates = new HashMap<IState, Object>();
        //states = new Vector<pair>();
        states = new HashMap<String, Object>();
        refs = new Vector<IState>();
        stackedStates = new Stack<HashMap<String, Object>>();
    }
    
    public static void push()
    {
        HashMap<String, Object> oldstates = (HashMap<String, Object>)states.clone();
        stackedStates.push(oldstates);
    }
    
    public static void pop()
    {
        HashMap<String, Object> oldstates = stackedStates.pop();
        states = oldstates;
    }
    
    //public AllStates() //called by inheritors. Finalizer will never be called, since it is in this list, at least.
    //{
    //    allStates.add(this);
    //}
    
    static Vector<shared.delegate> afterInitCallbacks = new Vector();
    public static void addCallbackAfterInit(shared.delegate d)
    {
        afterInitCallbacks.add(d);
    }
    
    public static void register(IState ref)
    {
        //states.add(new pair(null,ref,null));
        refs.add(ref);
    }
    
    //registers the name as having ref.
    /*public static void register(String name, IState ref)
    {
        boolean hasNameProblem = false;
        if(name==null||name.equals("")) hasNameProblem = true;
        for(pair p : states)
        {
            if(p.name.equals(name)) hasNameProblem = true;
            //{
                //p.ref = ref;
                //return;
            //}
        }
        if(!hasNameProblem)
        {
            states.add(new pair(name,ref,null));
        }
        else
        {
            shared.m.err("Programming Error: some state widgets have the same/no name.");
        }
        //refs.put(name, ref);
    }*/
    
    public static void setState(String name, Object value)
    {
        /*for(pair p: states)
        {
            if(p.name.equals(name))
            {
                p.val = value;
                return;
            }
        }
        states.add(new pair(name,null,value));*/
        //allStates.put(name, value);
        states.put(name, value);
    }
    
    public static Object getState(String name)
    {
        //return allStates.get(name);
        /*for(pair p: states)
        {
            if(p.name.equals(name)) return p.val;
        }
        return null;*/
        return states.get(name);
    }
    public static boolean getStateAsBoolean(String name)
    {
        Object obj = getState(name);
        if(obj instanceof Boolean) return (Boolean)obj;
        else return false;
    }
    public static String getStateAsString(String name)
    {
        Object obj = getState(name);
        if(obj instanceof String) return (String)obj;
        else return "";
    }
    public static int getStateAsInt(String name)
    {
        Object obj = getState(name);
        if(obj instanceof Integer) return (Integer)obj;
        else return 0;
    }
    public static boolean pullandsave(String filename)
    {
        pullInStates();
        return save(filename);
    }
    public static boolean loadandpush(String filename)
    {
        boolean result = load(filename);
        
        //hack
        //for(IState state: refs)
        //{
        //    state.initialise();
        //}

        if(result) pushOutStates();

        //loadandpush2();

        return result;
    }
    public static void loadandpush2()
    {
        for(shared.delegate d: afterInitCallbacks)
        {
            d.callback(null);
        }
    }
    public static boolean save(String filename)
    {
        ObjectOutputStream out = null;
        try
        {
            out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(states);
            out.close();
        }
        catch(Exception e)
        {
            try{ if(out!=null) out.close(); }catch(Exception e2){}
            //m.err("Unable to save state.");
            return false;
        }
        return true;
    }
    
    public static boolean load(String filename)
    {
        ObjectInputStream in = null;
        try
        {
            in = new ObjectInputStream(new FileInputStream(filename));
            //Vector<AllStates> newStates = (Vector<AllStates>)in.readObject();
            //HashMap<String, Object> newStates = (HashMap<String, Object>)in.readObject();
            //Vector<pair> newStates = (Vector<pair>)in.readObject();
            HashMap<String, Object> newStates = (HashMap<String, Object>)in.readObject();
            in.close();
            states = newStates;
        }
        catch(Exception e)
        {
            try{
                if(in!=null){
                    in.close();}
            } catch(Exception e2){}
            
            //m.err("Unable to load state.");
            File f = new File(filename);
            if(f.exists())
            {
                shared.m.warn("Unable to load state. Deleting old settings file...");
                resetSettings(filename);
            }
            else
            {
                shared.m.msg("Settings file doesn't exist yet; creating it.");
                pullInStates(); //get the current (default) states from widgets.
            }
            return false;
        }
        
        return true;
    }
    public static void pullInStates()
    {
        /*for(pair p: states)
        {
            if(p.ref!=null)
            {
                p.val = p.ref.getValue();
                p.name = p.ref.getStateName();
                if(p.name==null || p.name.equals("")) shared.m.err("Programming Error: some state widgets have the same/no name.");
            }
        }*/
        for(IState state: refs)
        {
            String name = state.getStateName();
            Object val = state.getStateValue();
            states.put(name, val);
        }
    }
    public static void pushOutStates()
    {
        //hack to initialize all registered states
        for(IState state: refs)
        {
            state.initialise();
        }
        
        /*for(pair p: states)
        {
            if(p.ref!=null && p.val!=null)
            {
                p.ref.setValue(p.val);
            }
        }*/
        HashSet<String> names = new HashSet();
        for(IState state: refs)
        {
            String name = state.getStateName();
            
            //check for naming problems...
            if(name==null||name.equals("")||names.contains(name))
            {
                shared.m.err("Programming Error: some state widgets have the same/no name.");
            }
            else
            {
                names.add(name);
            
                Object value = states.get(name);
                if(value!=null)
                {
                    state.putStateValue(value);
                }
                else
                {
                    //Do nothing, because this function is only called from loadAndPush() which hasn't set the default yet, but will.
                    //Alternatively, we could have loadAndPush() set the defaults before calling this; in which case this will just reassign the defaults.
                    state.putStateValue(state.getDefault());
                }
            }
        }
    }
    public static void revertToDefaults()
    {
        for(IState state: refs)
        {
            state.putStateValue(state.getDefault());
        }
    }
    public static void resetSettings(String filename)
    {
        File f = new File(filename);
        if(f.exists())
        {
            boolean success = f.delete();
            if(success) shared.m.msg("Deleted settings file.");
            else shared.m.msg("Unable to delete settings file.");
        }
        else
        {
            shared.m.msg("Settings file doesn't exist.");
        }
    }
    
    public static void test()
    {
        //StateInt a = new StateInt();
        //a.value = 42;
        AllStates.save("c:\\state2.dat");
    }
    public static void test2()
    {
        AllStates.load("c:\\state2.dat");
    }

    public static void update(IState state)
    {
        //shared.m.msg("updating");
        setState(state.getStateName(), state.getStateValue());
    }
    
}
