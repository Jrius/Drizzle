/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.vault;

import shared.*;
import prpobjects.HsBitVector;
import prpobjects.Wpstr;
import prpobjects.Bstr;
import shared.m;

public class Node //implements NodeTypes.ImageType
{
    public HsBitVector bv1;
    public int idx;
    //public byte b3;
    public nodetype type;
    public int permissions;
    public int owner;
    public int grp;
    public Timestamp mod_time;
    public int creator;
    public Timestamp crt_time; //autotime
    public Timestamp age_time;
    public Wpstr age_name;
    //
    public int blob1;
    public int xu12;
    public int xu13;
    public int xu14;
    public int xu15;
    public int xu16;
    public int xu17;
    public int xu18;
    public int xu19;
    public Wpstr xu20;
    public Wpstr xu21;
    public Wpstr xu22;
    public Wpstr xu23;
    public Wpstr xu24;
    public Wpstr xu25;
    public Wpstr xu26;
    public Wpstr xu27;
    public Wpstr xu28;
    public Wpstr xu29;
    public Bstr xu30;
    public Bstr xu31;
    //
    //
    public int blob2;
    public int blob3;
    
    
    public Node(IBytestream c)
    {
        bv1 = new HsBitVector(c);
        e.ensure(bv1.count==2);
        e.ensure(bv1.get(0)==-1);
        e.ensure(bv1.get(1)==7);
        
        idx = c.readInt();
        //b3 = c.readByte(); //node type
        //type = nodetype.get(b3);
        type = nodetype.read(c);
        if(type==nodetype.PlayerInfoNode)
        {
            int dummy=0;
        }
        permissions = c.readInt();
        owner = c.readInt();
        grp = c.readInt();
        mod_time = new Timestamp(c);
        if(bv1.flag(6)) creator = c.readInt();
        if(bv1.flag(7)) crt_time = new Timestamp(c);
        if(bv1.flag(9)) age_time = new Timestamp(c);
        if(bv1.flag(10)) age_name = new Wpstr(c); //agename?
        //if(flag(11)) 
        if(bv1.flag(11))
        {
            blob1 = c.readInt(); //CreateAgeGUID: guid?
            int blob1b = c.readInt();
            //Bstr age_guid = new Bstr(c);
        }
        if(bv1.flag(12)) xu12 = c.readInt();
        if(bv1.flag(13)) xu13 = c.readInt();
        if(bv1.flag(14)) xu14 = c.readInt();
        if(bv1.flag(15)) xu15 = c.readInt();
        if(bv1.flag(16)) xu16 = c.readInt();
        if(bv1.flag(17)) xu17 = c.readInt();
        if(bv1.flag(18)) xu18 = c.readInt();
        if(bv1.flag(19)) xu19 = c.readInt();
        if(bv1.flag(20)) xu20 = new Wpstr(c); //D'ni Imager left.
        if(bv1.flag(21)) xu21 = new Wpstr(c);
        if(bv1.flag(22)) xu22 = new Wpstr(c);
        if(bv1.flag(23)) xu23 = new Wpstr(c);
        if(bv1.flag(24)) xu24 = new Wpstr(c);
        if(bv1.flag(25)) xu25 = new Wpstr(c);
        if(bv1.flag(26)) xu26 = new Wpstr(c);
        if(bv1.flag(27)) xu27 = new Wpstr(c);
        if(bv1.flag(28)) xu28 = new Wpstr(c);
        if(bv1.flag(29)) xu29 = new Wpstr(c);
        if(bv1.flag(30)) xu30 = new Bstr(c);
        if(bv1.flag(31)) xu31 = new Bstr(c);
        //if(flag(32)) 
        if(bv1.flag(32))
        {
            blob2 = c.readInt();
            int blob2b = c.readInt();
        }
        //if(flag(33)) 
        if(bv1.flag(32))
        {
            blob3 = c.readInt();
            int blob3b = c.readInt();
        }
        
     
        //this.printData();
        
    }

    public void printData()
    {
        nodetype[] skips = {
            nodetype.ImageNode,
            nodetype.PlayerInfoNode,
            nodetype.VNodeMgrPlayerNode,
            nodetype.TextNoteNode,
            nodetype.SDLNode,
            nodetype.MarkerNode,
            nodetype.MarkerListNode,
            nodetype.FolderNode,
        };
        for(nodetype nt: skips) if(nt==type) return;
        
        switch(this.type)
        {
            case ImageNode:
                m.msg("ImageNode:");
                //m.msg("  idx="+Integer.toString(idx));
                m.msg("  agename:",this.ImageNode_GetAgeName());
                m.msg("  caption:",this.ImageNode_GetCaption());
                m.msg("  owner:",Integer.toString(owner));
                //this.ImageNode_GetImageData()
                break;
            case PlayerInfoNode:
                m.msg("PlayerInfoNode:");
                m.msg("  avatar name:",this.PlayerInfoNode_GetAvatarName());
                m.msg("  owner:",Integer.toString(owner));
                m.msg("  ki number:",Integer.toString(this.PlayerInfoNode_GetKINumber()));
                break;
            case TextNoteNode:
                m.msg("TextNoteNode:");
                m.msg("  owner:",Integer.toString(owner));
                m.msg("  creator:",Integer.toString(creator));
                m.msg("  agename:",this.TextNoteNode_GetAgename());
                m.msg("  title:",this.TextNoteNode_GetTitle());
                m.msg("  text:",this.TextNoteNode_GetText());
                break;
            case VNodeMgrPlayerNode:
                m.msg("VNodeMgrPlayerNode:");
                m.msg("  xu17:",Integer.toString(xu17));
                m.msg("  gender?:",xu20.toString());
                m.msg("  avatar name:",xu26.toString());
                m.msg("  login id:",xu28.toString());
                m.msg("  id???:",xu27.toString());
                m.msg("  xu18:",Integer.toString(xu18));
                break;
            case SDLNode:
                m.msg("SDLNode:");
                m.msg("  agename:",this.age_name.toString());
                m.msg("  xu12:",Integer.toString(xu12));
                m.msg("  xu30 size:",Integer.toString(xu30.strlen));
                if(this.age_name.toString().toLowerCase().equals("myst"))
                {
                    int dummy=0;
                }
                if(xu12==0)
                {
                    int dummy=0;
                }
                if(xu30.strlen==0)
                {
                    int dummy=0;
                }
                else
                {
                    ByteArrayBytestream sdldat = ByteArrayBytestream.createFromByteArray(xu30.string);
                    VaultSDL sdl2 = new VaultSDL(sdldat,3); //Pots specific code because of 3.
                }
                //FileUtils.WriteFile("c:/out.sdldat", xu30.string);
                break;
            case MarkerListNode:
                m.msg("MarkerListNode:");
                m.msg("  agename:",this.age_name.toString());
                m.msg("  creator:",Integer.toString(creator));
                m.msg("  gamename:",this.xu20.toString());
                m.msg("  avatarname:",this.xu21.toString());
                m.msg("  blob1(id):",Integer.toString(blob1));
                break;
            case MarkerNode:
                m.msg("MarkerNode:");
                m.msg("  agename:",this.age_name.toString());
                m.msg("  creator:",Integer.toString(creator));
                Flt f16 = Flt.createFromData(xu16);
                Flt f17 = Flt.createFromData(xu17);
                Flt f18 = Flt.createFromData(xu18);
                m.msg("  f16:",f16.toString());
                m.msg("  f17:",f17.toString());
                m.msg("  f18:",f18.toString());
                m.msg("  markertext:",xu28.toString());
                m.msg("  blob1(id):",Integer.toString(blob1));
                //agename, creator, and blob1 together seem to identify which game this belongs to.
                break;
            case FolderNode:
                m.msg("FolderNode:");
                m.msg("  Folder type: ",Integer.toString((xu12)));
                /*if(xu20!=null && !xu20.toString().equals(""))
                {
                    m.msg(xu20.toString());
                }*/
                break;
            case ChronicleNode:
                m.msg("ChronicleNode:");
                m.msg("  Agename: ",age_name.toString());
                m.msg("  Varname: ",xu20.toString());
                m.msg("  Varvalue: ",xu28.toString());
                break;
        }
    }
    /*public Typeid gettype()
    {
        switch(b3)
        {
            case 25: return Typeid.ImageNode;
            default: return Typeid.unknown;
        }
    }*/
    public int PlayerInfoNode_GetKINumber()
    {
        return xu16;
    }
    public String PlayerInfoNode_GetAgeIDOfOwnersCurrentLocation()
    {
        return xu21.toString();
    }
    public String PlayerInfoNode_GetAvatarName()
    {
        return xu26.toString();
    }
    public String TextNoteNode_GetAgename()
    {
        return age_name.toString();
    }
    public String TextNoteNode_GetText()
    {
        return xu30.toString();
    }
    public String TextNoteNode_GetTitle()
    {
        return xu20.toString();
    }
    /*public <T> T castTo(Class<T> cls)
    {
        if(cls==NodeTypes.ImageType.class && b3==25) return (T)this;
        
        return null;
    }*/
    public String ImageNode_GetAgeName()
    {
        return age_name.toString();
    }
    public String ImageNode_GetCaption()
    {
        return xu20.toString();
    }
    public byte[] ImageNode_GetImageData()
    {
        byte[] result = xu30.string;
        result = b.substr(result,4,result.length-4);
        return result;
    }
    
}
