/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.server;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

public class TextMapFile
{
    private static final String sep = "{->}";
    private static final String comment = "//";

    //public final Map<String,String> entries = new LinkedHashMap();
    public final ArrayList<Item> entries = new ArrayList();

    public static class Item
    {
        String left;
        String right;
        public Item(String left, String right)
        {
            this.left = left;
            this.right = right;
        }
    }

    public TextMapFile(String text)
    {
        text = text.replace("\r", ""); //hack for windows eol
        String[] lines = text.split("\n");
        for(int i=0;i<lines.length;i++)
        {
            String line = lines[i];
            //if(index==-1 && (line.startsWith(comment)||line.equals(""))) continue;  //skip comments and empty lines.
            if(line.equals("")||line.startsWith(comment)) continue;
            int index = line.indexOf(sep);
            if(index==-1) throw new shared.uncaughtexception("TextMapFile contains an invalid line at line: "+Integer.toString(i));
            String left = line.substring(0, index);
            String right = line.substring(index+sep.length());
            if(left.length()==0 || right.length()==0)
            {
                throw new shared.uncaughtexception("Invalid language file at line "+Integer.toString(i+1));
            }

            entries.add(new Item(left,right));
        }
    }
}
