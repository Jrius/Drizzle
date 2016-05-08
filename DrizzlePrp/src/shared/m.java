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
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import java.io.OutputStream;
//import java.io.Writer;
import java.util.Vector;
import java.io.PrintStream;
import java.util.Stack;
import java.io.InputStream;
import javax.swing.text.JTextComponent;
import shared.State.LogBoxStateless;
import javax.swing.JLabel;

/**
 *
 * @author user
 */
public class m
{
    public static Integer debugCount = 0;

    //private static JTextArea _outputTextArea; //you must set this from the GUI.
    private static LogBoxStateless _outputTextArea;
    private static JProgressBar _outputProgressBar; //you must set this from the GUI.
    private static boolean justUseConsole = true;
    private static int tab = 0;
    private static final String tabsp = "  ";
    private static int numerrors = 0;  //keeps track of number of errors in log.
    private static int numwarnings = 0;
    private static JLabel errorlabel;
    private static JLabel warninglabel;
    
    public static final Color warningColor = new java.awt.Color(0x00FF7011);
    public static final Color errorColor = Color.RED;

    
    public static enum MessageType
    {
        normal,warning,error,console,status;

        public String getIndicator()
        {
            switch(this)
            {
                case warning:
                    return shared.translation.translate("Warning: ");
                case error:
                    return shared.translation.translate("Error: ");
                case console:
                    return shared.translation.translate("Console: ");
                case normal:
                case status:
                default:
                    return "";
            }
        }
    }

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
            this.filename = FileUtils.GetInitialWorkingDirectory()+"/Drizzle.output.txt";
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

    private static long lasttime = -1;
    private static String lasttimemsg;
    public static void increaseindentation()
    {
        tab++;
    }
    public static void decreaseindentation()
    {
        tab--;
    }
    public static void SetErrorLabel(JLabel label)
    {
        errorlabel = label;
        label.putClientProperty("trans", false);
        UpdateErrorCount();
    }
    public static void SetWarningLabel(JLabel label)
    {
        warninglabel = label;
        label.putClientProperty("trans", false);
        UpdateErrorCount();
    }
    public static void ResetWarningsAndErrors()
    {
        numerrors = 0;
        numwarnings = 0;
        UpdateErrorCount();
    }
    public static void time()
    {
        long time = gettime();
        m.msg("time(ms): ",Long.toString(time));
    }
    public static long gettime()
    {
        long time = java.util.Calendar.getInstance().getTimeInMillis();
        return time;
    }
    /*
     * Starts a timer or displays the time since the last call to marktime.
     */
    public static void marktime(String msg)
    {
        long time = gettime();
        if(lasttime==-1)
        {
            m.msg(msg+": (Starting timer)");
        }
        else
        {
            m.msg(msg+": "+Long.toString(time-lasttime)+"ms (since '"+lasttimemsg+"')");
        }
        lasttime = time;
        lasttimemsg = msg;
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
    
    //public static void setJTextArea(JTextArea newJTextArea)
    public static void setLogbox(LogBoxStateless newJTextArea)
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
    public static void setProgressBar(JProgressBar newProgressBar)
    {
        _outputProgressBar = newProgressBar;
    }
    
    private static void UpdateErrorCount()
    {
        /*if(errorlabel!=null)
        {
            String s = "<html>";
            s += shared.translation.translate("Errors: ");
            if(numerrors>0)
            {
                s += "<font color='red'>";
                s += Integer.toString(numerrors);
                s += "</font>";
            }
            else
            {
                s += Integer.toString(numerrors);
            }
            s += "  ";
            s += shared.translation.translate("Warnings: ");
            if(numwarnings>0)
            {
                s += "<font color='orange'>";
                s += Integer.toString(numwarnings);
                s += "</font>";
                javax.swing.JLabel a;
                //a.getcl
            }
            else
            {
                s += Integer.toString(numwarnings);
            }
            s += "</html>";
            errorlabel.setText(s);
        }*/
        
        if(errorlabel!=null)
        {
            errorlabel.setText(Integer.toString(numerrors));
            if(numerrors>0) errorlabel.setForeground(m.errorColor);
            else errorlabel.setForeground(null);
        }
        if(warninglabel!=null)
        {
            warninglabel.setText(Integer.toString(numwarnings));
            if(numwarnings>0) warninglabel.setForeground(m.warningColor);
            else warninglabel.setForeground(null);
        }
    }
    private static void message(final MessageType type, String... ss)
    {
        //update error and warning counts
        switch(type)
        {
            case error:
                numerrors++;
                UpdateErrorCount();
                break;
            case warning:
                numwarnings++;
                UpdateErrorCount();
                break;
        }
        
        //translate if necessary.
        if(state.curstate.translate)
        {
            for(int i=0;i<ss.length;i++)
            {
                ss[i] = shared.translation.translate(ss[i]);
            }
        }

        //merge the string parts
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<tab;i++)
        {
            sb.append(tabsp);
        }
        for(String spart: ss)
        {
            sb.append(spart);
        }
        String s = sb.toString();

        if(justUseConsole)
        {
            String indicator = type.getIndicator();
            System.out.println(indicator+s);
        }
        else if(_outputTextArea!=null)
        {
            //javax.swing.JScrollPane a;
            //javax.swing.JTextArea b;
            final String s2 = s;
            javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
                public void run() {
                    //_outputTextArea.append(s2+"\n",type);
                    String indicator = type.getIndicator();
                    _outputTextArea.append(indicator,type);
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
            String indicator = type.getIndicator();
            String errormsg = "Programming Error: shared.m messages are being generated before the output TextArea is set, or there is no output TextArea.\nThe error is: " + indicator + s;
            System.out.println(errormsg);
            //javax.swing.JFrame frame = new javax.swing.JFrame();
            javax.swing.JOptionPane.showMessageDialog(null,errormsg);
            
        }
        
        if(state.curstate.writeToFile)
        {
            FileUtils.AppendText(state.curstate.filename, s+"\n");
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
            message(MessageType.normal, s);
    }

    public static void err(String... s)
    {
        //Main.message(s);
        if(state.curstate.showErrorMessages)
            //message(shared.generic.prependToArray("Error: ", s, String.class));
            message(MessageType.error, s);
            //message("Error: "+s);
        //throw new Exception(s);
    }
    
    public static void warn(String... s)
    {
        if(state.curstate.showWarningMessages)
            //message(shared.generic.prependToArray("Warning: ", s, String.class));
            message(MessageType.warning, s);
            //message("Warning: "+s);
    }
    
    public static void console(String... s)
    {
        if(state.curstate.showConsoleMessages)
            //message(shared.generic.prependToArray("Console: ", s, String.class));
            message(MessageType.console, s);
            //message("Console:"+s);
    }

    public static void status(String... s)
    {
        if(state.curstate.showStatusMessages)
            //message(s);
            message(MessageType.status, s);
    }

    public static void throwUncaughtException(String... s)
    {
        //if(debugCount!=null) m.msg("CurPos: "+Integer.toString(debugCount));
        String msg = m.trans(s);
        throw new shared.uncaughtexception(msg);
    }
    public static void cancel()
    {
        cancel("");
    }
    public static void cancel(String s)
    {
        throw new shared.cancel(s);
    }

    //input doesn't quite work: it is correctly received but isn't passed on quite correctly.
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

    public static void setWorking(boolean isWorking)
    {
        if(_outputProgressBar!=null)
        {
            if(isWorking){
                javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
                    public void run() {
                        _outputProgressBar.setIndeterminate(true);
                        //_outputProgressBar.setValue(50);
                    }
                });
            }else{
                javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
                    public void run() {
                        _outputProgressBar.setIndeterminate(false);
                    }
                });
            }
        }
    }

    public static String trans(String... ss)
    {
        String r = "";
        for(int i=0;i<ss.length;i++)
        {
            r += shared.translation.translate(ss[i]);
        }
        return r;
    }
}
