/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shared;

/**
 *
 * @author user
 */
public class EscapeUtils
{
    public static String unescapeJavaString(String s)
    {
        StringBuilder r = new StringBuilder();
        int size = s.length();
        StringBuffer unicode = new StringBuffer(4);
        boolean hadSlash = false;
        boolean inUnicode = false;
        for (int i=0;i<size;i++)
        {
            char ch = s.charAt(i);
            if(inUnicode)
            {
                // if in unicode, then we're reading unicode
                // values in somehow
                unicode.append(ch);
                if (unicode.length() == 4) {
                    // unicode now contains the four hex digits
                    // which represents our unicode character
                    try
                    {
                        int value = Integer.parseInt(unicode.toString(), 16);
                        r.append((char)value);
                        unicode.setLength(0);
                        inUnicode = false;
                        hadSlash = false;
                    }catch(NumberFormatException nfe){
                        throw new shared.nested(nfe);
                    }
                }
                continue;
            }
            if(hadSlash)
            {
                // handle an escaped value
                hadSlash = false;
                switch (ch) {
                    case '\\':
                        r.append('\\');
                        break;
                    case '\'':
                        r.append('\'');
                        break;
                    case '\"':
                        r.append('"');
                        break;
                    case 'r':
                        r.append('\r');
                        break;
                    case 'f':
                        r.append('\f');
                        break;
                    case 't':
                        r.append('\t');
                        break;
                    case 'n':
                        r.append('\n');
                        break;
                    case 'b':
                        r.append('\b');
                        break;
                    case 'u':
                        {
                            // uh-oh, we're in unicode country....
                            inUnicode = true;
                            break;
                        }
                    default :
                        r.append(ch);
                        break;
                }
                continue;
            }else if(ch=='\\')
            {
                hadSlash = true;
                continue;
            }
            r.append(ch);
        }
        if (hadSlash) {
            // then we're in the weird case of a \ at the end of the
            // string, let's output it anyway.
            r.append('\\');
        }
        return r.toString();
    }

    public static String escapeHtmlString(String s)
    {
        StringBuffer r = new StringBuffer();
        for(int i = 0;i<s.length();i++)
        {
            char ch = s.charAt(i);
            switch(ch)
            {
                case '<':
                    r.append("&lt;");
                    break;
                case '>':
                    r.append("&gt;");
                    break;
                case '&':
                    r.append("&amp;");
                    break;
                case '"':
                    r.append("&quot;");
                    break;
                default:
                    r.append(ch);
                    break;
            }
        }
        return r.toString();
    }

    public static String escapeXmlString(String s)
    {
        if(s==null) s = "";
        StringBuffer r = new StringBuffer();
        for(int i = 0;i<s.length();i++)
        {
            char ch = s.charAt(i);
            switch(ch)
            {
                case '<':
                    r.append("&lt;");
                    break;
                case '>':
                    r.append("&gt;");
                    break;
                case '&':
                    r.append("&amp;");
                    break;
                case '"':
                    r.append("&quot;");
                    break;
                case '\'':
                    r.append("&apos;");
                    break;
                case 27: //esc
                    r.append("&#");
                    r.append(Integer.toString(ch));
                    r.append(";");
                    break;
                default:
                    r.append(ch);
                    break;
            }
        }
        return r.toString();

        //String result = s==null?"":s;
        //result = result.replace("&", "&amp;");
        //result = result.replace("<", "&lt;");
        //result = result.replace(">", "&gt;");
        //result = result.replace("\"", "&quot;");
        //result = result.replace("'", "&apos;");
        //result = result.replace("", "&#38;");
        //return result;
    }


}
