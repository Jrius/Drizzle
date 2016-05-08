/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3;

import shared.m;
import java.util.ArrayList;

public class stats
{
    public static boolean printcurtoken = true;
    public static boolean printDisassembly = false;
    public static boolean printStatistics = false;
    public static boolean printDecompilation = false;
    public static boolean printTiming = false;

    private static StringBuilder s = new StringBuilder();
    private static ArrayList<Integer> curtokens;// = new ArrayList();
    public static void reset()
    {
        s = new StringBuilder();
    }
    public static void efficiency(int curtoken, int numrounds, String message)
    {
        float effic = (float)curtoken/(float)numrounds;
        s.append("Efficiency: "+Float.toString(effic)+message+"\n");
    }
    public static void simplereduce(int rulenum, int tokennum)
    {
        //s.append("Token: "+Integer.toString(tokennum)+"  Rule: "+Integer.toString(rulenum)+"\n");
    }
    public static String getReport()
    {
        StringBuilder r = new StringBuilder();
        r.append("\n");
        r.append("********************** stats report ********************\n");
        r.append(s);
        return r.toString();
    }
    public static void printReport()
    {
        String report = getReport();
        m.msg(report);
    }
    public static void resetCurtoken()
    {
        curtokens = new ArrayList();
    }
    public static void addCurtoken(int token)
    {
        curtokens.add(token);
    }
    public static void printCurtokens()
    {
        for(Integer token: curtokens)
        {
            m.msg(Integer.toString(token));
        }
    }
}
