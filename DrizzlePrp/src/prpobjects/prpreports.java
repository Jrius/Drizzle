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

package prpobjects;

import uru.context;
import uru.Bytestream;
import shared.readexception;

public class prpreports
{
    public static String MakeFullReport(byte[] filecontents)
    {
        String sound = MakeSoundReport(filecontents);
        String python = MakePythonReport(filecontents);
        String result = "***Sound Report***\n"
                +sound
                +"***Python Report***\n"
                +python;
        return result;
    }
    public static String MakeSoundReport(byte[] filecontents)
    {
        StringBuilder result = new StringBuilder();
        context c = context.createFromBytestream(new Bytestream(filecontents));
        prpfile prp = prpprocess.ProcessAllObjects(c,false);
        PrpRootObject[] objs = prputils.FindAllObjectsOfType(prp, Typeid.plSoundBuffer);
        for(int i=0;i<objs.length;i++)
        {
            plSoundBuffer curobj = objs[i].castTo();//x0029SoundBuffer.class);
            result.append(curobj.oggfile.toString() + "\n");
            
        }
        return result.toString();
    }

    public static String MakePythonReport(byte[] filecontents)
    {
        StringBuilder result = new StringBuilder();
        context c = context.createFromBytestream(new Bytestream(filecontents));
        prpfile prp = prpprocess.ProcessAllObjects(c,false);
        PrpRootObject[] objs = prputils.FindAllObjectsOfType(prp, Typeid.plPythonFileMod);
        for(int i=0;i<objs.length;i++)
        {
            plPythonFileMod curobj = objs[i].castTo();//x0029SoundBuffer.class);
            result.append(curobj.pyfile.toString() + "\n");
            
        }
        return result.toString();
    }
}
