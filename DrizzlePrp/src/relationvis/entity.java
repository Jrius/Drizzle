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

package relationvis;

import java.util.Vector;

/**
 *
 * @author user
 */
public class entity
{
    boolean ismarked = false;
    String name;
    String group = "default";
    Vector<entity> rightlinks = new Vector<entity>();
    Vector<entity> leftlinks = new Vector<entity>();
    
    int xpos;
    int ypos;
    
    //temp vars
    boolean temp1;
    
    public entity(String name2)
    {
        name = name2;
    }
    
    public String toString()
    {
        return name;
    }

}
