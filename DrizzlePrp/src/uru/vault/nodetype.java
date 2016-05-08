/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.vault;

import shared.IBytestream;
import shared.IBytedeque;

public enum nodetype
{
    ImageNode,
    FolderNode,
    PlayerInfoNode,
    AgeInfoNode,
    unknown,
    VNodeMgrAgeNode,
    PlayerInfoListNode,
    SDLNode,
    TextNoteNode,
    AgeLinkNode,
    VNodeMgrPlayerNode,
    ChronicleNode,
    MarkerListNode,
    MarkerNode,
    ;
    public static nodetype read(IBytestream c)
    {
        byte val = c.readByte();
        return get(val);
    }
    public static nodetype get(byte val)
    {
        String valstr = "0x"+Integer.toHexString(val);
        switch(val)
        {
            case 0x02: return VNodeMgrPlayerNode;
            case 0x03: return VNodeMgrAgeNode;
            case 0x16: return FolderNode;
            case 0x17: return PlayerInfoNode;
            //case 0x18: return unknown;
            case 0x19: return ImageNode;
            case 0x1A: return TextNoteNode;
            case 0x1B: return SDLNode;
            case 0x1C: return AgeLinkNode;
            case 0x1D: return ChronicleNode;
            case 0x1E: return PlayerInfoListNode;
            case 0x20: return MarkerNode;
            case 0x21: return AgeInfoNode;
            case 0x23: return MarkerListNode;
            default:
                return unknown;
        }
    }
}
