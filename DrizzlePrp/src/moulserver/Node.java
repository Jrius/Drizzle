/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Str;
import prpobjects.Guid;
import java.sql.Timestamp;
import java.sql.ResultSet;
import shared.*;
import moulserver.Results;
import java.util.Vector;
import java.io.File;
import uru.server.*;
//import moulserver.Database.*;
import java.util.Set;
//import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public abstract class Node
{
    public static class NodeType extends Enums.Int
    {
        public static final int kNodeInvalid = 0;
        public static final int kNodeVNodeMgrLow = 1;
        public static final int kNodePlayer = 2;
        public static final int kNodeAge = 3;
        public static final int kNodeGameServer = 4;
        public static final int kNodeAdmin = 5;
        public static final int kNodeVaultServer = 6;
        public static final int kNodeCCR = 7;
        public static final int kNodeVNodeMgrHigh = 21;
        public static final int kNodeFolder = 22;
        public static final int kNodePlayerInfo = 23;
        public static final int kNodeSystem = 24;
        public static final int kNodeImage = 25;
        public static final int kNodeTextNote = 26;
        public static final int kNodeSDL = 27;
        public static final int kNodeAgeLink = 28;
        public static final int kNodeChronicle = 29;
        public static final int kNodePlayerInfoList = 30;
        public static final int kNodeUNUSED = 31;
        public static final int kNodeMarker = 32;
        public static final int kNodeAgeInfo = 33;
        public static final int kNodeAgeInfoList = 34;
        public static final int kNodeMarkerList = 35;
    }


    //folder types
    public static class FolderType extends Enums.Int
    {
        public static final int kUserDefinedNode = 0;
        public static final int kInboxFolder = 1;
        public static final int kBuddyListFolder = 2;
        public static final int kIgnoreListFolder = 3;
        public static final int kPeopleIKnowAboutFolder = 4;
        public static final int kVaultMgrGlobalDataFolder = 5;
        public static final int kChronicleFolder = 6;
        public static final int kAvatarOutfitFolder = 7;
        public static final int kAgeTypeJournalFolder = 8;
        public static final int kSubAgesFolder = 9;
        public static final int kDeviceInboxFolder = 10;
        public static final int kHoodMembersFolder = 11;
        public static final int kAllPlayersFolder = 12;
        public static final int kAgeMembersFolder = 13;
        public static final int kAgeJournalsFolder = 14;
        public static final int kAgeDevicesFolder = 15;
        public static final int kAgeInstanceSDLNode = 16;
        public static final int kAgeGlobalSDLNode = 17;
        public static final int kCanVisitFolder = 18;
        public static final int kAgeOwnersFolder = 19;
        public static final int kAllAgeGlobalSDLNodesFolder = 20;
        public static final int kPlayerInfoNode = 21;
        public static final int kPublicAgesFolder = 22;
        public static final int kAgesIOwnFolder = 23;
        public static final int kAgesICanVisitFolder = 24;
        public static final int kAvatarClosetFolder = 25;
        public static final int kAgeInfoNode = 26;
        public static final int kSystemNode = 27;
        public static final int kPlayerInviteFolder = 28;
        public static final int kCCRPlayersFolder = 29;
        public static final int kGlobalInboxFolder = 30;
        public static final int kChildAgesFolder = 31;
        public static final int kGameScoresFolder = 32;
        public static final int kLastStandardNode = 33;
    }

    //notes
    public static final int kNoteGeneric = 0;
    public static final int kNoteCCRPetition = 1;
    public static final int kNoteDevice = 2;
    public static final int kNoteInvite = 3;
    public static final int kNoteVisit = 4;
    public static final int kNoteUnVisit = 5;
    public static final int kNumNoteTypes = 6;

    //images
    public static final int kNone = 0;
    public static final int kJPEG = 1;

    //permissions
    public static final int kOwnerRead = 0x1;
    public static final int kOwnerWrite = 0x2;
    public static final int kGroupRead = 0x4;
    public static final int kGroupWrite = 0x8;
    public static final int kOtherRead = 0x10;
    public static final int kOtherWrite = 0x20;
    public static final int kDefaultPermissions = kOtherRead|kGroupRead|kOwnerRead|kOwnerWrite;


    RawNode n;
    //byte[] nodedata; //may be null


    //public abstract static class Node //in table 'vault'
    //{
//        int idx;
//        int type;
//        int permissions;
//        int owner;
//        int grp;
//        Timestamp mod_time;
//        int creator;
//        Timestamp crt_time;
//        Timestamp age_time;
//        String age_name;
//        byte[] age_guid;
//        int int_1;
//        int int_2;
//        int int_3;
//        int int_4;
//        int uint_1;
//        int uint_2;
//        int uint_3;
//        int uint_4;
//        String str_1;
//        String str_2;
//        String str_3;
//        String str_4;
//        String str_5;
//        String str_6;
//        String lstr_1;
//        String lstr_2;
//        String text_1;
//        String text_2;
//        byte[] blob_1; //might have to change type, if we use an actual BLOB instead of BINARY
//        byte[] blob_2; //might have to change type, if we use an actual BLOB instead of BINARY

        //public void addParent(Node parent)
        //{
        //}

    /*public static <T extends Node> T createEmpty()
    {
        RawNode rn = RawNode.createEmpty();
        Node.getNode2(rn);
    }*/

    public <T extends Node> T cast()
    {
        return (T) this;
    }

    public static class SystemNode extends Node
    {
        public SystemNode()
        {
            n = new RawNode(NodeType.kNodeSystem);

            n.insert();
        }
        public SystemNode(RawNode r)
        {
            n = r;
        }
        public static SystemNode findIfExists()
        {
            Results results = Manager.manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.type+"=?", NodeType.kNodeSystem);
            if(!results.first()) return null;
            else return (SystemNode) getNode(results);
        }
    }

    public static class TextNoteNode extends Node
    {
        public Str getTitle2(){return n.string64_1;}
        public String getTitle(){return (n.string64_1==null)?"":n.string64_1.toString();}
        public void setTitle(String val){n.string64_1 = new Str(val);}
        public String getContents(){return n.text_1b.toString();}
        public void setContents(String val){n.text_1b = new Str(val);}
        public TextNoteNode(String title, String contents)
        {
            n = new RawNode(NodeType.kNodeTextNote);

            setTitle(title); //can be null
            setContents(contents);

            n.insert();
        }
        public TextNoteNode(RawNode r)
        {
            n = r;
        }
    }

    public static class AgeInfoListNode extends Node
    {
        public int getFolderType(){return n.int32_1;}
        public void setFolderType(int val){n.int32_1 = val;}
        public AgeInfoListNode(int type)
        {
            n = new RawNode(NodeType.kNodeAgeInfoList);

            setFolderType(type);

            n.insert();
        }
        public AgeInfoListNode(RawNode r)
        {
            n = r;
        }
    }

    public static class AgeLinkNode extends Node
    {
        public byte[] getSpawnpointInfo(){return n.blob_1b.toBytes();}
        public void setSpawnpointInfo(byte[] val){n.blob_1b = new Blob(val);}
        public AgeLinkNode(String spawnpointInfo)
        {
            n = new RawNode(NodeType.kNodeAgeLink);

            if(spawnpointInfo!=null) setSpawnpointInfo(b.StringToBytes(spawnpointInfo));

            n.insert();
        }
        public AgeLinkNode(RawNode r)
        {
            n = r;
        }
    }

    public int getType(){return n.nodeType;}
    public void setType(int val){n.nodeType = val;}
    public int getIdx(){return n.nodeIdx;}
    public void setIdx(int val){n.nodeIdx = val;}
    public byte[] getAllBytes()
    {
        return n.getBytes();
    }
    public String toString()
    {
        return n.toString();
    }

    private static Node root;
    public static Node getRoot()
    {
        if(root==null)
        {
            //look for it
            //try{
                Results results = Manager.manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.type+"=? LIMIT 1", NodeType.kNodeVaultServer);
                if(results.first())
                {
                    //read it
                    root = new VaultServerNode(new RawNode(results));
                }
                else
                {
                    //create one.
                    root = new VaultServerNode();
                }
            //}catch(Exception e){
            //    throw new shared.uncaughtnestedexception(e);
            //}
        }
        return root;
    }

    public void addChild(Node child)
    {
        //try{
            //Results results = Manager.manager.database.sqlquery("SELECT * FROM ref_vault WHERE parent_idx=? AND child_idx=? LIMIT 1", n.nodeIdx,child.n.nodeIdx);
            //if(!results.first()) //if no results, i.e. if this is not already present.
            //{
            //    Ref ref = new Ref(n.nodeIdx, child.n.nodeIdx, 0, (byte)0); //setting owner=0 and seen=0 for now.
            //}
        addChild(child.getIdx());
        //}catch(Exception e){
        //    throw new shared.uncaughtnestedexception(e);
        //}
    }
    public void addChild(int childIdx)
    {
        /*Results results = Manager.manager.database.sqlquery("SELECT * FROM ref_vault WHERE parent_idx=? AND child_idx=? LIMIT 1", n.nodeIdx,childIdx);
        if(!results.first()) //if no results, i.e. if this is not already present.
        {
            Ref ref = new Ref(n.nodeIdx, childIdx, 0, (byte)0); //setting owner=0 and seen=0 for now.
        }*/
        createLink(n.nodeIdx,childIdx,0,(byte)0);
    }
    public static boolean createLink(int parentIdx, int childIdx, int ownerIdx, byte seen)
    {
        Results results = Manager.manager.database.sqlquery("SELECT * FROM ref_vault WHERE parent_idx=? AND child_idx=? LIMIT 1", parentIdx, childIdx);
        if(!results.first()) //if no results, i.e. if this is not already present.
        {
            Ref ref = new Ref(parentIdx, childIdx, ownerIdx, seen); //setting owner=0 and seen=0 for now.

            //if the parentIdx is a playerNode and they are online, notify them.
            m.msg("Adding link: "+Integer.toString(parentIdx));
            /*Iterable<Comm.Listener> listeners = Manager.manager.comm.getNodeLinkListeners(parentIdx);
            if(listeners!=null)
            {
                AuthServer.VaultNodeAdded msg = new AuthServer.VaultNodeAdded();
                msg.parentId = parentIdx;
                msg.childId = childIdx;
                msg.ownerId = ownerIdx;
                for(Comm.Listener listener: listeners )
                {
                    listener.addItem(Comm.CommItem.SendMessage(msg)); //fix this null
                }
            }*/

            //Notify Listeners:
            Manager.manager.vaultlistener.SignalNodeAdded(parentIdx, childIdx, ownerIdx);

            return true;
        }
        return false;
    }
    /*public static void createGrandparentLink(int parentIdx, int childIdx, int ownerIdx, byte seen)
    {
        Vector<Integer> newchildren = new Vector();

        //create the link
        boolean wasnew = createLink(parentIdx,childIdx,ownerIdx,seen);

        if(wasnew)
        {
            newchildren.add(childIdx);

            //link the children
            for(Ref ref: Node.getChildrenOfIdx(childIdx))
            {
                boolean wasnew2 = createLink(parentIdx,ref.childIdx,ownerIdx,seen);
                if(wasnew2) newchildren.add(ref.childIdx);
            }
            
            //do the parents all the way up.
            for(Ref ref: Node.getParentsOfIdx(parentIdx))
            {
                notifyParents(ref.parentIdx,newchildren);
            }
        }

    }
    private static void notifyParents(int parentIdx, Vector<Integer> newchildren)
    {
        //add these children
        for(int newChildIdx: newchildren)
        {
            boolean wasnew = createLink(parentIdx,newChildIdx,0,(byte)0);
        }

        //do the parents all the way up.
        for(Ref ref: Node.getParentsOfIdx(parentIdx))
        {
            notifyParents(ref.parentIdx,newchildren);
        }
    }*/
    /*public void addChildTree(Node child)
    {
        addChildTree(child.getIdx());
    }
    private void addChildTree(int childIdx)
    {
        //check if this one is already added, and if not, add it.
        addChild(childIdx);

        //handle subtrees.
        ArrayList<Ref> refs = Node.getChildrenOfIdx(childIdx);
        for(Ref ref: refs)
        {
            int subchildIdx = ref.childIdx;
            addChildTree(subchildIdx);
        }
    }*/
    /*public void addGrandchildren(Node child)
    {
        addGrandchildren(child.getIdx());
    }
    public void addGrandchildren(int childIdx)
    {
        //add child
        addChild(childIdx);

        //add grandchildren
        ArrayList<Ref> refs = Node.getChildrenOfIdx(childIdx);
        for(Ref ref: refs)
        {
            int grandchildIdx = ref.childIdx;
            addChild(grandchildIdx);
        }

    }*/
    public static <T extends Node> T getNodeWithIndex(int idx)
    {
        //try{
            Results results = Manager.manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.idx+"=?", idx);
            if(!results.first()) m.throwUncaughtException("SQL query found not results!");
            return (T) getNode(results);
            //return ret;
            /*int type = results.getInt("type");
            switch(type)
            {
                case Node.kNodeVaultServer:
                    T ret = (T) new VaultServerNode(results);
                    return ret;
                case Node.kNodePlayer:
                    T ret2 = (T) new PlayerNode(results);
                    return ret2;
                default:
                    throw new shared.uncaughtexception("Oops; add this node to the list: "+Integer.toString(type));
            }*/

        //}catch(Exception e){
        //    throw new shared.uncaughtnestedexception(e);
        //}
    }

    //public static Node getNodeWithIndex2(int idx)
    //{
    //    Results results = Manager.manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.idx+"=?", idx);
    //    if(!results.first()) m.throwUncaughtException("SQL query found not results!");
    //    return getNode2(results);
    //}
    public static <T extends Node> T getNode(Results r)
    {
        RawNode rawnode = new RawNode(r);
        return (T) getNode2(rawnode);
    }
    public static <T extends Node> T getNode(byte[] rawdata)
    {
        RawNode rawnode = new RawNode(rawdata);
        return (T) getNode2(rawnode);
    }
    public static Node getNode2(RawNode r)
    {
        try{
            //int type = r.getInt(Database.type);
            int type = r.nodeType;
            switch(type)
            {
                case NodeType.kNodeVaultServer:
                    return new VaultServerNode(r);
                case NodeType.kNodePlayer:
                    return new PlayerNode(r);
                case NodeType.kNodePlayerInfo:
                    return new PlayerInfoNode(r);
                case NodeType.kNodeAgeInfo:
                    return new AgeInfoNode(r);
                case NodeType.kNodeAgeLink:
                    return new AgeLinkNode(r);
                case NodeType.kNodeChronicle:
                    return new ChronicleNode(r);
                case NodeType.kNodeFolder:
                    return new FolderNode(r);
                case NodeType.kNodeSDL:
                    return new SDLNode(r);
                case NodeType.kNodeAgeInfoList:
                    return new AgeInfoListNode(r);
                case NodeType.kNodePlayerInfoList:
                    return new PlayerInfoListNode(r);
                case NodeType.kNodeTextNote:
                    return new TextNoteNode(r);
                case NodeType.kNodeSystem:
                    return new SystemNode(r);
                case NodeType.kNodeAge:
                    return new AgeNode(r);
                case NodeType.kNodeImage:
                    return new ImageNode(r);
                case NodeType.kNodeMarkerList:
                    return new MarkerListNode(r);
                default:
                    throw new shared.uncaughtexception("Oops; add this node to the list: "+Integer.toString(type));
            }
        }catch(Exception e){
            throw new shared.nested(e);
        }
    }

    public static ArrayList<Ref> FindTreeRefs(int rootNodeIdx)
    {
        /*LinkedHashSet<Ref> r = new LinkedHashSet<Ref>();
        FindTreeRefs(rootNodeIdx,r);
        ArrayList<Ref> r2 = new ArrayList(r);
        return r2;*/

        ArrayList<Ref> r = new ArrayList();
        //HashSet<Long> done = new HashSet();
        FindTreeRefs(rootNodeIdx,r);
        return r;
    }
    private static void FindTreeRefs(int idx, ArrayList<Ref> r)//, HashSet<Long> done*/ LinkedHashSet<Ref> r)
    {
        //oops, this is wrong, because it may include refs more than once.
        /*Results results = Manager.manager.database.sqlquery("SELECT * FROM ref_vault WHERE parent_idx=?", idx);
        ArrayList<Ref> children = results.castAsRefs();
        r.addAll(children);
        for(Ref child: children)
        {
            FindTreeRefs(child.childIdx, r);
        }*/
        Results results = Manager.manager.database.sqlquery("SELECT * FROM ref_vault WHERE parent_idx=?", idx);
        ArrayList<Ref> children = results.castAsRefs();
        r.addAll(children);
        /*boolean[] handle = new boolean[children.size()];
        for(int i = 0;i<children.size();i++)
        {
            Ref child = children.get(i);
            handle[i] = r.add(child);
        }
        for(int i = 0;i<handle.length;i++)
        {
            //boolean wasadded = r.add(child);
            //if(wasadded) FindTreeRefs(child.childIdx, r);
            if(handle[i])
            {
                Ref child = children.get(i);
                FindTreeRefs(child.childIdx,r);
            }
        }*/
        for(Ref child: children)
        {
            FindTreeRefs(child.childIdx,r);
        }
    }

    public static ArrayList<Ref> getChildrenOfIdx(int idx)
    {
        Results results = Manager.manager.database.sqlquery("SELECT * FROM ref_vault WHERE parent_idx=?", idx);
        ArrayList<Ref> r = results.castAsRefs();
        return r;
    }
    public static ArrayList<Ref> getParentsOfIdx(int idx)
    {
        Results results = Manager.manager.database.sqlquery("SELECT * FROM ref_vault WHERE child_idx=?", idx);
        ArrayList<Ref> r = results.castAsRefs();
        return r;
    }

    public Node(){}

    public ArrayList<Ref> getChildrenRefs()
    {
        return getChildrenOfIdx(this.getIdx());
    }
    public ArrayList<Node> getChildrenNodes()
    {
        ArrayList<Node> r = new ArrayList();
        for(Ref ref: getChildrenRefs())
        {
            Node node = Node.getNodeWithIndex(ref.childIdx);
            r.add(node);
        }
        return r;
    }



    public static class ChronicleNode extends Node
    {
        public int getChronicleType(){return n.int32_1;}
        public void setChronicleType(int val){n.int32_1 = val;}
        public String getChronicleName(){return n.string64_1.toString();}
        public void setChronicleName(String val){n.string64_1 = new Str(val);}
        public String getChronicleValue(){return n.text_1b.toString();}
        public void setChronicleValue(String val){n.text_1b = new Str(val);}

        public ChronicleNode(int type, String name, String value)
        {
            n = new RawNode(NodeType.kNodeChronicle);

            setChronicleType(type);
            setChronicleName(name);
            setChronicleValue(value);

            n.insert();
        }
        public ChronicleNode(RawNode r)
        {
            n = r;
        }
    }

    public static class AgeNode extends Node
    {
        public Guid getAgeInstanceGuid(){return n.uuid_1;} //unsure
        public void setAgeInstanceGuid(Guid val){n.uuid_1 = val;}
        public String getAgeFilename(){return n.string64_1.toString();}
        public void setAgeFilename(String val){n.string64_1 = new Str(val);}
        /*public AgeNode(int playerId, String ageFilename)
        {
            this.n = new RawNode(NodeType.kNodeAge);
            //this.setType(NodeType.kNodeAge);

            int prefix = Manager.manager.agesinfo.getSequencePrefixForAge(ageFilename);
            //int playerId = player.getIdx();
            Guid ageguid = Guid.newAgeInstanceGuid(prefix,playerId);
            setAgeInstanceGuid(ageguid);
            setAgeFilename(ageFilename);
            

            //player.addChild(ageinfonode); //the AgeInfoNode is added to the player.
            
            n.insert();
            //player.addChild(this); //this isn't actually directly attached to the player.
        }*/
        public AgeNode(int playerId, Guid ageInstanceGuid, String ageFilename)
        {
            this.n = new RawNode(NodeType.kNodeAge);

            setAgeInstanceGuid(ageInstanceGuid);
            setAgeFilename(ageFilename);

            //attempt to fix age vault problem:
            this.n.creatorUuid = ageInstanceGuid;

            n.insert();
        }
        /*public AgeNode(int playerId, Guid ageInstanceGuid)
        {
            this.n = new RawNode(NodeType.kNodeAge);

            setAgeInstanceGuid(ageInstanceGuid);

            n.insert();
        }*/
        public static AgeNode findFromGuid(Guid guid)
        {
            Results r = Manager.manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.type+"=? AND "+Database.uuid_1+"=? LIMIT 1", NodeType.kNodeAge, guid.toBytes());
            if(!r.first()) m.throwUncaughtException("AgeNode not found: "+guid.toString());
            return new AgeNode(new RawNode(r));
        }
        public AgeNode(RawNode r)
        {
            n = r;
        }
    }

    public static class AgeInfoNode extends Node
    {
        public int getAgeId(){return n.uint32_1;}
        public void setAgeId(int val){n.uint32_1 = val;}
        public int getAgeCzarId(){return n.uint32_2;} //untested
        public void setAgeCzarId(int val){n.uint32_2 = val;}
        public String getAgeFilename(){return n.string64_2.toString();}
        public void setAgeFilename(String val){n.string64_2 = new Str(val);}
        public String getAgeInstancename(){return n.string64_3.toString();}
        public void setAgeInstancename(String val){n.string64_3 = new Str(val);}
        public String getAgeUserDefinedName(){return n.string64_4.toString();}
        public void setAgeUserDefinedName(String val){n.string64_4 = new Str(val);}
        public String getAgeDescription(){return n.text_1b.toString();}
        public void setAgeDescription(String val){n.text_1b = new Str(val);}
        public Guid getAgeInstanceGuid(){return n.uuid_1;}
        public void setAgeInstanceGuid(Guid val){n.uuid_1 = val;}
        public AgeInfoNode(AgeNode agenode, String ageFilename, String ageInstancename, String ageUserDefinedName, String ageDescription)
        {
            this.n = new RawNode(NodeType.kNodeAgeInfo);
            //this.setType(NodeType.kNodeAgeInfo);

            n.int32_3 = -1; //don't know why :P
            n.int32_1 = 0; //also unknown
            n.uint32_3 = 0; //also unknown
            setAgeId(agenode.getIdx());
            setAgeCzarId(0); //no czar (parent age?)
            setAgeFilename(ageFilename);
            setAgeInstancename(ageInstancename);
            if(ageUserDefinedName!=null) setAgeUserDefinedName(ageUserDefinedName);
            setAgeDescription(ageDescription);
            setAgeInstanceGuid(agenode.getAgeInstanceGuid());

            //attempt to fix age vault problem:
            n.creatorIdx = agenode.getIdx();
            n.creatorUuid = agenode.n.creatorUuid;

            n.insert();
        }
        public AgeInfoNode(RawNode r)
        {
            n = r;
        }
        public static AgeInfoNode getFromAgeInstanceGuid(Guid guid)
        {
            Results r = Manager.manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.type+"=? AND "+Database.uuid_1+"=? LIMIT 1", NodeType.kNodeAgeInfo, guid.toBytes());
            if(!r.first()) m.throwUncaughtException("AgeInfo not found: "+guid.toString());
            return (AgeInfoNode) Node.getNode(r);
        }
    }

    public static class FolderNode extends Node
    {
        public int getFolderType(){return n.int32_1;}
        public void setFolderType(int val){n.int32_1 = val;}
        public String getFolderName(){return n.string64_1.toString();}
        public void setFolderName(String val){n.string64_1 = new Str(val);}
        public FolderNode(int type)
        {
            n = new RawNode(NodeType.kNodeFolder);
            //this.setType(NodeType.kNodeFolder);

            setFolderType(type);

            n.insert();
        }
        public FolderNode(int type, String name)
        {
            n = new RawNode(NodeType.kNodeFolder);
            //this.setType(NodeType.kNodeFolder);

            setFolderType(type);
            setFolderName(name);

            n.insert();
        }
        public FolderNode(RawNode r)
        {
            n = r;
        }
    }
    public static class PlayerInfoListNode extends Node
    {
        public int getFolderType(){return n.int32_1;}
        public void setFolderType(int val){n.int32_1 = val;}
        public String getFolderName(){return n.string64_1.toString();}
        public void setFolderName(String val){n.string64_1 = new Str(val);}
        public PlayerInfoListNode(int type)
        {
            n = new RawNode(NodeType.kNodePlayerInfoList);
            //this.setType(NodeType.kNodePlayerInfoList);

            setFolderType(type);

            n.insert();
        }
        public PlayerInfoListNode(RawNode r)
        {
            n = r;
        }
    }

    public static class SDLNode extends Node
    {
        //Node n;
        public SDLNode(RawNode n)
        {
            this.n = n;
        }
        public SDLNode(String statedescname, Guid creatorGuid, int creatorIdx)
        {
            this.n = new RawNode(NodeType.kNodeSDL);
            //this.setType(NodeType.kNodeSDL);

            setStatedescName(statedescname);
            n.creatorUuid = creatorGuid;
            n.creatorIdx = creatorIdx;

            n.insert();
        }
        //public SDLNode(Results r)
        //{
        //    n = new RawNode(r);
        //}
        public Integer xu1(){return n.int32_1;} //0?
        public String getStatedescName(){return n.string64_1.toString();}
        public void setStatedescName(String val){n.string64_1 = new Str(val);}
        public byte[] getSdlBytecode(){return n.blob_1b.toBytes();}
        public void setSdlBytecode(byte[] val){n.blob_1b = new Blob(val);}
        public byte[] sdlData()
        {
            if(n.blob_1b==null)
            {
                return null;
            }
            return n.blob_1b.blob;
        }
        /*public static SDLNode getFromAgeInfoIdx(int ageInfoIdx)
        {
            Results r = Manager.manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.type+"=? AND "+Database.uuid_1+"=? LIMIT 1", NodeType.kNodeAgeInfo, guid.toBytes());
            if(!r.first()) m.throwUncaughtException("AgeInfo not found: "+guid.toString());
            return (AgeInfoNode) Node.getNode(r);
        }*/
    }

    public static class Blob
    {
        byte[] blob;

        public Blob(IBytestream c)
        {
            int size = c.readInt();
            blob = c.readBytes(size);
        }
        public Blob(byte[] data)
        {
            blob = data;
        }
        public void write(IBytedeque c)
        {
            c.writeInt(blob.length);
            c.writeBytes(blob);
        }
        public String toString()
        {
            return b.BytesToString(blob); //pretend it's just text, which it sometimes is.
        }
        public byte[] toBytes()
        {
            return blob;
        }
    }

    public static class PlayerInfoNode extends Node
    {
        public String getPlayerName(){return n.iString64_1.toString();}
        public int getPlayerId(){return n.uint32_1;}
        public Guid getCurrentAgeGuid(){return n.uuid_1;}
        public String getCurrentAgeFilename(){return n.string64_1.toString();}
        public int getCurrentLinkStatus(){return n.int32_1;}
        public void setCurrentLinkStatus(int val){n.int32_1 = val;}
        //LinkStatus: 0=offline, 1=online, 2=leftButWillBeBack?

        public PlayerInfoNode(int playerId)
        {
            PlayerNode player = PlayerNode.getNodeWithIndex(playerId);

            n = new RawNode(NodeType.kNodePlayerInfo);
            //n.nodeIdx = Manager.manager.database.getNextIdx();
            //n.nodeType = NodeType.kNodePlayerInfo;
            n.iString64_1 = new Str(player.getPlayerName());
            n.uint32_1 = playerId;
            n.uuid_1 = Guid.none2();
            n.string64_1 = new Str("");
            n.int32_1 = 0; //client tries to set this to 1 if not already set, hmm...

            //Manager.manager.database.sqlupdate("INSERT INTO vault(nodeIdx,nodeType,iString64_1,uint32_1) VALUES(?,?,?,?)", n.nodeIdx,n.nodeType,n.iString64_1,n.uint32_1);
            n.insert();
            //parent.addChild(this);

        }
        public PlayerInfoNode(RawNode r)
        {
            n = r;
        }
        public static PlayerInfoNode getFromPlayerId(int playerId)
        {
            Results r = Manager.manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.type+"=? AND "+Database.uint_1+"=? LIMIT 1", NodeType.kNodePlayerInfo,playerId);
            if(!r.first()) m.throwUncaughtException("PlayerInfo not found: "+Integer.toString(playerId));
            return (PlayerInfoNode) Node.getNode(r);
        }
    }

    public static class PlayerNode extends Node
    {
        public String getPlayerName(){return n.iString64_1.toString();}
        public String getAvatarShape(){return n.string64_1.toString();}
        public Guid getAccountGuid(){return n.uuid_1;}
        public int getExplorer(){return n.int32_2;} //is this right? It was the only one set to 1.
        public PlayerNode(String playerName, String avatarShape, Guid accountGuid, int explorer)
        {
            n = new RawNode(NodeType.kNodePlayer);
            //n.nodeIdx = Manager.manager.database.getNextIdx();
            //n.nodeType = NodeType.kNodePlayer;
            n.iString64_1 = new Str(playerName);
            n.string64_1 = new Str(avatarShape);
            n.uuid_1 = accountGuid;
            n.int32_2 = explorer;

            //Manager.manager.database.sqlupdate("INSERT INTO vault(nodeIdx,nodeType,iString64_1,string64_1,blob_1,int32_1) VALUES(?,?,?,?,?,?)", n.nodeIdx,n.nodeType,n.iString64_1,n.string64_1,n.blob_1b,n.int32_1);
            n.insert();
            //parent.addChild(this);
        }
        //public PlayerNode(Results r)
        //{
        //    n = new RawNode(r);
            //try{
                //this.idx = r.getInt("idx");
                //this.type = r.getInt("type");
                //this.lstr_1 = r.getString("lstr_1");
                //this.str_1 = r.getString("str_1");
                //this.blob_1 = r.getBytes("blob_1");
                //this.int_1 = r.getInt("int_1");
            //}catch(Exception e){
            //    throw new shared.uncaughtnestedexception(e);
            //}
        //}
        public PlayerNode(RawNode r)
        {
            n = r;
        }
        public static PlayerNode findByName(String playerName)
        {
            //try{
                Results results = Manager.manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.type+"=? AND "+Database.lstr_1+"=? LIMIT 1", NodeType.kNodePlayer,playerName);
                if(results.first()) return new PlayerNode(new RawNode(results));
                return null;
            //}catch(Excep)
        }
        public static Vector<PlayerNode> findAll()
        {
            Vector<PlayerNode> r = new Vector();
            Results results = Manager.manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.type+"=?",NodeType.kNodePlayer);
            boolean hasrow = results.first();
            while(hasrow)
            {
                r.add(new PlayerNode(new RawNode(results)));
                hasrow = results.next();
            }
            return r;
        }
    }

    public static class VaultServerNode extends Node
    {
        public VaultServerNode()
        {
            n = new RawNode(NodeType.kNodeVaultServer);
            //n.nodeIdx = Manager.manager.database.getNextIdx();
            //n.nodeType = NodeType.kNodeVaultServer;

            //try{
            //Manager.manager.database.sqlupdate("INSERT INTO vault(nodeIdx,nodeType) VALUES(?,?)", n.nodeIdx,n.nodeType);
            n.insert();
                //parent.addChild(this);
            //}catch(Exception e){
            //    throw new shared.uncaughtnestedexception(e);
            //}
        }
        public VaultServerNode(RawNode r)
        {
            n = r;
            //try{
                //this.idx = r.getInt("idx");
                //this.type = r.getInt("type");
            //}catch(Exception e){
            //    throw new shared.uncaughtnestedexception(e);
            //}
        }
    }
    public static class ImageNode extends Node
    {
        //public static int typeInvalid = 0;
        public static int typeJpeg = 1;

        public int getType(){return n.int32_1;} //always 1?
        public void setType(int val){n.int32_1 = val;}
        //public String getTitle(){return n.string64_1.toString();}
        public String getTitle(){return (n.string64_1==null)?"":n.string64_1.toString();}
        public void setTitle(String val){n.string64_1 = new Str(val);}
        public byte[] getImageData(){return n.blob_1b.blob;}
        public void setImageData(byte[] val){n.blob_1b.blob = val;}
        public ImageNode(String title, byte[] imageData)
        {
            n = new RawNode(NodeType.kNodeImage);

            setType(1);
            setTitle(title);
            setImageData(imageData);

            n.insert();
        }
        public ImageNode(RawNode r)
        {
            n = r;
        }
    }
    public static class MarkerListNode extends Node
    {
        public String getGamename(){return n.text_1b.toString();}
        public void setGamename(String val){n.text_1b = new Str(val);}
        public MarkerListNode()
        {
            n = new RawNode(NodeType.kNodeMarkerList);
            n.insert();
        }
        public MarkerListNode(RawNode r)
        {
            n = r;
        }
    }

    public static class Ref extends mystobj
    {
        int parentIdx;
        int childIdx;
        int ownerIdx;
        byte seen;
        //this 'seen' was either 1 or 0 in Alcugs, but it Moulagain it changes on some of them, even without starting the regular client.
        //(Even the 0 was sometimes changed.) So I'm thinking it means nothing to the client, and is used by the server to optimize or keep track of something, such as changed children.
        //But it appears to depend on the parent and *only* the parent. Perhaps it's used to notify children that their parent has changed? They just look at the ref to their parent and can tell?


        public Ref(IBytestream c)
        {
            parentIdx = c.readInt();
            childIdx = c.readInt();
            ownerIdx = c.readInt();
            seen = c.readByte();
        }
        public Ref(){}
        public Ref(Results r)
        {
            parentIdx = r.getInt("parent_idx");
            childIdx = r.getInt("child_idx");
            ownerIdx = r.getInt("owner_idx");
            seen = r.getByte("seen");
        }
        public Ref(int parentIdx, int childIdx, int ownerIdx, byte seen)
        {
            this.parentIdx = parentIdx;
            this.childIdx = childIdx;
            this.ownerIdx = ownerIdx;
            this.seen = seen;

            Manager.manager.database.sqlupdate("INSERT INTO ref_vault(parent_idx,child_idx,owner_idx,seen) VALUES(?,?,?,?)",parentIdx,childIdx,ownerIdx,seen);

        }
        public void write(IBytedeque c)
        {
            c.writeInt(parentIdx);
            c.writeInt(childIdx);
            c.writeInt(ownerIdx);
            c.writeByte(seen);
        }
        public String dump()
        {
            return "Parent="+Integer.toString(parentIdx)+" Child="+Integer.toString(childIdx)+" Owner="+Integer.toString(ownerIdx)+" Seen="+Byte.toString(seen);
            //return "Seen="+Integer.toBinaryString(b.ByteToInt32(seen))+" Parent="+Integer.toString(parentIdx)+" Child="+Integer.toString(childIdx)+" Owner="+Integer.toString(ownerIdx);
        }
        public String toString()
        {
            return dump();
        }
        public boolean equals(Object o)
        {
            if(o==null) return false;
            if(o.getClass()!=Ref.class) return false;
            Ref o2 = (Ref)o;
            if(o2.parentIdx!=parentIdx) return false;
            if(o2.childIdx!=childIdx) return false;
            //if(o2.ownerIdx!=ownerIdx) return false; //we shouldn't need this.
            //if(o2.seen!=seen) return false; //we shouldn't need this.
            return true;
        }
        public int hashCode()
        {
            return parentIdx>>16+childIdx;
        }
    }

    //public static void SaveNode(int nodeId, byte[] nodeData, Guid revisionId, ConnectionState cs)
    public static void SaveNode(byte[] nodeData, AuthServer.VaultNodeChanged changenode, ConnectionState cs)
    {
        //shared.ByteArrayBytestream c = shared.ByteArrayBytestream.createFromByteArray(nodeData);
        RawNode newnode = new RawNode(nodeData);

        //why do this, when we can just sql update?  Because we want to update the "fields" field.
        Node oldnode = Node.getNodeWithIndex(changenode.nodeId);
        //oldnode.n.mergeFrom(newnode); //brings the new fields to be saved in.

        if(newnode.nodeIdx!=null && newnode.nodeIdx!=changenode.nodeId) m.throwUncaughtException("unexpected"); //is it trying to change the idx?
        newnode.nodeIdx = changenode.nodeId; //because it isn't already set.(at least, not always.)
        long newfields = newnode.fields | oldnode.n.fields;
        //long newfields = newnode.fields;
        newnode.update(newfields,changenode.revisionId/*,cs*/);

        //Manager.comm.triggerNodeChange(changenode.nodeId, Comm.CommItem.NodeChange(changenode, cs));

        //int dummy=0;
    }

    public static void test()
    {
        //read all nodes
        //Manager.testinit();
        File f = new File("d:/DelmeDrizzleTest/vault/");
        for(File child: f.listFiles())
        {
            if(child.isFile())
            {
                byte[] data = FileUtils.ReadFile(child);
                shared.ByteArrayBytestream c = shared.ByteArrayBytestream.createFromByteArray(data);
                RawNode n = new RawNode(c);
                if(n.nodeType==NodeType.kNodeSDL)
                {
                    SDLNode n2 = (SDLNode)n.getInterface();
                    byte[] sdldata = n2.sdlData();
                    if(sdldata!=null)
                    {
                        shared.ByteArrayBytestream c2 = shared.ByteArrayBytestream.createFromByteArray(sdldata);
                        uru.context c3 = uru.context.createFromBytestream(c2);
                        c3.readversion = 6; //moul
                        //SdlBinary sdlbin = new SdlBinary(c3,Manager.manager);
                        //c3.extra = Manager.manager;
                        SdlBinary sdlbin = new SdlBinary(c3);
                    }
                    int dummy=0;
                }
                if(c.getBytesRemaining()!=0)
                {
                    int dummy=0;
                }
                //m.msg("Type="+Integer.toString(n.nodeType)+" Fields="+Long.toBinaryString(n.fields));
                m.msg(n.toString());
            }
        }
    }

}
