/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package shared;

//import gui.Main;
//import javax.swing.text.JTextComponent;
import javax.swing.JTextArea;
import java.io.OutputStream;
//import java.io.Writer;
import java.util.Vector;
import java.io.PrintStream;
import java.util.Stack;
import java.io.InputStream;

/**
 *
 * @author user
 */
public class m
{
    
    private static JTextArea _outputTextArea; //you must set this from the GUI.
    private static boolean justUseConsole = true;
    
    public static class stateclass implements java.io.Serializable //Serializable is for the deepclone, if you want.
    {
        public boolean showNormalMessages = true;
        public boolean showWarningMessages = true;
        public boolean showErrorMessages = true;
        public boolean showConsoleMessages = true;
        public boolean showStatusMessages = true;
        public boolean writeToFile = false;
        public boolean scrollOutput = true;
        public boolean translate = false;
        public String filename;
        
        public stateclass()
        {
            //this.filename = FileUtils.GetInitialWorkingDirectory()+"/Drizzle.output.txt";
        }
        
        public stateclass clone() //used for shallow clone, if you want.
        {
            stateclass result = new stateclass();
            result.showNormalMessages = this.showNormalMessages;
            result.showConsoleMessages = this.showConsoleMessages;
            result.showErrorMessages = this.showErrorMessages;
            result.showStatusMessages = this.showStatusMessages;
            result.showWarningMessages = this.showWarningMessages;
            result.writeToFile = this.writeToFile;
            result.scrollOutput = this.scrollOutput;
            result.translate = this.translate;
            result.filename = this.filename;
            return result;
        }
    }
    public static StateStack<stateclass> state = new StateStack<stateclass>(new stateclass(),true,true);
    
    public static void time()
    {
        long time = java.util.Calendar.getInstance().getTimeInMillis();
        m.msg("time: ",Long.toString(time));
    }
    public static void redirectStdOut()
    {
        if(justUseConsole==false)
        {
            System.setOut(new PrintStream(new Outstream("stdout:"), true));
        }
    }
    public static void redirectStdErr()
    {
        if(justUseConsole==false)
        {
            System.setErr(new PrintStream(new Outstream("stderr:"), true));
        }
    }
    
    public static class Outstream extends OutputStream
    {
        Vector<Character> unprinted=new Vector<Character>();
        String tag;
        public Outstream(String tag)
        {
            this.tag = tag;
        }
        /*public void close()
        {
        }*/
        @Override public void flush()
        {
            //super.flush();
            char[] CharString = new char[unprinted.size()];
            for(int i=0;i<CharString.length;i++)
            {
                CharString[i] = unprinted.elementAt(i);
            }
            unprinted.clear();
            String msg = new String(CharString);
            if(msg.equals("\r\n")) return;
            if(msg.equals("")) return;
            m.console(tag,msg);
            //m.msg("Console:"+tag+msg);
        }
        public void write(int b)
        {
            unprinted.add((char)b);
        }
        /*public void write(char[] buffer, int offset, int length)
        {
            //String msg = new String(buffer,offset,length);
            //m.msg(msg);
            for(int i=0;i<length;i++)
            {
                char c = buffer[offset+i];
                unprinted.add(c);
            }
        }*/
    }
    
    public static void setJTextArea(JTextArea newJTextArea)
    {
        if(newJTextArea==null)
        {
            justUseConsole = true;
        }
        else
        {
            justUseConsole = false;
            _outputTextArea = newJTextArea;
        }
    }
    
    private static void message(String... ss)
    {
        //translate if necessary.
        if(state.curstate.translate)
        {
            for(int i=0;i<ss.length;i++)
            {
                //ss[i] = shared.translation.translate(ss[i]);
            }
        }

        //merge the string parts
        StringBuilder sb = new StringBuilder();
        for(String spart: ss)
        {
            sb.append(spart);
        }
        String s = sb.toString();

        if(justUseConsole)
        {
            System.out.println(s);
        }
        else if(_outputTextArea!=null)
        {
            //javax.swing.JScrollPane a;
            //javax.swing.JTextArea b;
            final String s2 = s;
            javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
                public void run() {
                    _outputTextArea.append(s2+"\n");
                    if(state.curstate.scrollOutput)
                    {
                        //set the view position to the height, plus a little extra.
                        int h = _outputTextArea.getHeight();
                        _outputTextArea.scrollRectToVisible(new java.awt.Rectangle(0, h+40, 0, 0));
                    }
                }
            });
            /*_outputTextArea.append(s+"\n");
            if(state.curstate.scrollOutput)
            {
                //set the view position to the height, plus a little extra.
                int h = _outputTextArea.getHeight();
                _outputTextArea.scrollRectToVisible(new java.awt.Rectangle(0, h+40, 0, 0));
            }*/
        }
        else
        {
            String errormsg = "Programming Error: shared.m messages are being generated before the output TextArea is set, or there is no output TextArea.\nThe error is: " + s;
            System.out.println(errormsg);
            //javax.swing.JFrame frame = new javax.swing.JFrame();
            javax.swing.JOptionPane.showMessageDialog(null,errormsg);
            
        }
        
        if(state.curstate.writeToFile)
        {
            //FileUtils.AppendText(state.curstate.filename, s+"\n");
        }

        String[] trapmessages = {"compile not implemented"};
        for(int i=0;i<trapmessages.length;i++)
        {
            if(s.toLowerCase().startsWith(trapmessages[i].toLowerCase()))
            {
                int trapbreak = 0;
            }
        }
    }
    
    /*public static void msgsafe(String... s)
    {
        //final String s2 = s;
        //javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
        //    public void run() {
        //        msg(s2);
        //    }
        //});
        msg(s);
    }*/
    public static void msg(String... s)
    {
        //Main.message(s);
        if(state.curstate.showNormalMessages)
            message(s);
    }

    public static void err(String... s)
    {
        //Main.message(s);
        if(state.curstate.showErrorMessages)
            message(shared.generic.prependToArray("Error: ", s, String.class));
            //message("Error: "+s);
        //throw new Exception(s);
    }
    
    public static void warn(String... s)
    {
        if(state.curstate.showWarningMessages)
            message(shared.generic.prependToArray("Warning: ", s, String.class));
            //message("Warning: "+s);
    }
    
    public static void console(String... s)
    {
        if(state.curstate.showConsoleMessages)
            message(shared.generic.prependToArray("Console: ", s, String.class));
            //message("Console:"+s);
    }

    public static void status(String... s)
    {
        if(state.curstate.showStatusMessages)
            message(s);
    }

    public static void throwUncaughtException(String s)
    {
        throw new shared.uncaughtexception(s);
    }

    public static class StreamRedirector
    {
        public static void Redirect(Process p)
        {
            Poller poller = new Poller(p);
            poller.start();
        }
        public static class Poller extends Thread
        {
            private static final int polltime = 100; //in milliseconds.

            InputStream parentStdin;
            PrintStream parentStdout;
            PrintStream parentStderr;

            OutputStream childStdin;
            InputStream childStdout;
            InputStream childStderr;

            private boolean quit = false;

            public Poller(Process p)
            {
                parentStdin = System.in;
                parentStdout = System.out;
                parentStderr = System.err;

                childStdin = p.getOutputStream();
                childStdout = p.getInputStream();
                childStderr = p.getErrorStream();

                this.setDaemon(true); //so it won't stop the application from terminating.
            }
            @Override public void run()
            {
                while(!quit)
                {
                    poll();
                    try{
                        sleep(polltime);
                    }catch(Exception e){}
                }
            }

            private void poll()
            {
                //int available = childStdout.available();
                try{
                    while(childStdout.available()!=0)
                    {
                        int b = childStdout.read();
                        parentStdout.write(b);
                        parentStdout.flush();
                    }

                    while(childStderr.available()!=0)
                    {
                        int b = childStderr.read();
                        parentStderr.write(b);
                        parentStderr.flush();
                    }
                    //input doesn't quite work.
                    while(parentStdin.available()!=0)
                    {
                        int b = parentStdin.read();
                        //parentStdout.print("hi");
                        parentStdout.write(b);
                        parentStdout.flush();
                        childStdin.write(b);
                        childStdin.flush();
                    }
                }catch(Exception e){
                    int dummy=0;
                    //no sense writing an error message :P
                }
            }
        }
    }

}
