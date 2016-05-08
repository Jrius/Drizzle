/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package prpobjects;

import uru.Bytedeque;
import uru.context; import shared.readexception;
import shared.m;

public class plDirectMusicSound extends uruobj {
    
    public plWin32Sound.PlSound parent;
    public int flags;
    public Urustring filename;
    
    public plDirectMusicSound(context c) throws readexception
    {
        parent = new plWin32Sound.PlSound(c);
        flags = c.readInt();
        filename = new Urustring(c);
    }
    
    @Override
    public void compile(Bytedeque c)
    {
        m.warn("plDirectMusicSound: compile not implemented");
    }
}
