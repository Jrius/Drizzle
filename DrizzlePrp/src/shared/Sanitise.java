/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class Sanitise
{
    public static String SanitiseFilename(String infilename)
    {
        StringBuilder result = new StringBuilder();
        byte[] bytes = b.StringToBytes(infilename);
        for(byte c: bytes)
        {
            if(c<' ') continue; //ignore non-printable chars.
            //The characters windows doesn't like: /\:?"*<>|
            if(c=='~') result.append("~(TILDE)"); //tilde isn't really a problem, but we'll use it as an escape char.
            else if(c=='/') result.append("~(SLASH)");
            else if(c=='\\') result.append("~(BACKSLASH)");
            else if(c==':') result.append("~(COLON)");
            else if(c=='?') result.append("~(QUESTIONMARK)");
            else if(c=='"') result.append("~(DOUBLEQUOTES)");
            else if(c=='*') result.append("~(ASTERISK)");
            else if(c=='<') result.append("~(LEFTANGLE)");
            else if(c=='>') result.append("~(RIGHTANGLE)");
            else if(c=='|') result.append("~(PIPE)");
            else result.append((char)c);
        }
        
        return result.toString();
    }
}
