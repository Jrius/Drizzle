/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Concurrent;
import prpobjects.plNetMsgSDLState;
import prpobjects.Guid;
import shared.*;
import prpobjects.uruobj;
import uru.Bytedeque;
import uru.server.*;
import java.io.File;
import java.util.Vector;
import moulserver.Node.*;
import moulserver.GameMainServer.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Queue;
import moulserver.Comm.*;
//import moulserver.NetServer.ConnectionState;
import prpobjects.*;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.IdentityHashMap;
import parsers.ageparser.Agefile;
import parsers.ageparser.PageInfo;
import java.util.HashMap;

public class GameServer extends Thread
{
    public static final int minShutdown = 600; //600=10min

    Manager manager;

    public int gameServerNumber;
    String ageFilename;
    Guid ageInstanceGuid;
    Agefile ageinfo;

    AtomicInteger timeToShutdown;
    ConnectionState dummycs = ConnectionState.dummy();

    Queue<Comm.CommItem> items = Concurrent.getConcurrentQueue();

    //Vars only for *this* thread:
    //Set<Uruobjectdesc> clones = Concurrent.getConcurrentSet();
    //ArrayList<Uruobjectdesc> clones = new ArrayList();
    ArrayList<plLoadCloneMsg> clones = new ArrayList();
    ArrayList<SdlState> sdlstates = new ArrayList();
    ArrayList<ConnectionState> connections = new ArrayList();
    IdentityHashMap<ConnectionState,plLoadCloneMsg> connectionToClone = new IdentityHashMap();
    HashMap<Integer,ConnectionState> playeridToConnection = new HashMap();
    ArrayList<Page> pages = new ArrayList();
    int sdlIdx;
    String sdlStatedesc;


    private SdlState findSdlState(Uruobjectdesc desc, SdlBinary sdlbin)
    {
        for(SdlState sdl: sdlstates)
        {
            //we must match the desc *and* the sdl name, because the same desc (e.g. Male sceneobject) can be used for multiple sdls.
            if(sdl.desc.equals(desc) && sdl.sdlvars.name.toString().equals(sdlbin.name.toString())) return sdl;
        }
        return null;
    }
    public plLoadCloneMsg findClone(plLoadCloneMsg msg2)
    {
        for(plLoadCloneMsg clone: clones)
        {
            if(clone.cloneKey.equals(msg2.cloneKey)) return clone;
        }
        return null;
    }
    private Page findPage(int pagenum)
    {
        for(Page page: pages)
        {
            if(page.pageinfo.pagenum==pagenum) return page;
        }
        return null;
    }

    public GameServer(int number, String filename, Guid guid, Manager manager)
    {
        this.manager = manager;
        gameServerNumber = number;
        ageFilename = filename;
        ageInstanceGuid = guid;
        timeToShutdown = new AtomicInteger(shared.DateTimeUtils.getCurrentTimeInSeconds());
        setMinimumShutdownTime(minShutdown);

        ageinfo = manager.agesinfo.getAge(ageFilename);
        for(PageInfo pageinfo: ageinfo.pages)
        {
            Page page = new Page();
            page.pageinfo = pageinfo;
            pages.add(page);
        }
    }

    public void run()
    {
        m.msg("Starting GameServer: "+ageFilename+", "+ageInstanceGuid.toString());

        //do startup stuff
        Comm.CommItem noderesponse = manager.comm.SendAndWait(Comm.CommItem.GetAgeSdl(this.ageInstanceGuid),manager.authserver.items);
        SDLNode sdl = noderesponse.node.cast();
        sdlIdx = sdl.getIdx();
        sdlStatedesc = sdl.getStatedescName();

        while(true)
        {
            CommItem item = items.poll();
            if(item!=null)
            {
                if(item.type==CommItemType.HandleMessage)
                {
                    HandleMessage(item.msg,item.connstate);
                }
                else if(item.type==CommItemType.NodeChange)
                {
                    AuthServer.VaultNodeChanged msg = (AuthServer.VaultNodeChanged)item.msg;
                    Node n = Node.getNodeWithIndex(msg.nodeId);
                    if(n.getType()==Node.NodeType.kNodePlayerInfo)
                    {
                        if(item.connstate!=dummycs) //don't react to our own changes :P
                        {
                            Node.PlayerInfoNode playerinfo = (Node.PlayerInfoNode)n;
                            int linkstatus = playerinfo.getCurrentLinkStatus();
                            m.msg("GameServer: player link status set to: "+Integer.toString(linkstatus));
                            if(playerinfo.getCurrentAgeFilename().equals(""))
                            {
                                //they are quitting the Age.
                                //playerinfo.setCurrentLinkStatus(0);
                                //playerinfo.n.updateAll(Guid.fullyRandom(),dummycs); //this null is correct, as there is no ConnectionState who has already seen this change.

                                //m.msg("Terminating GameServer connection.");
                                //ConnectionState cs = playeridToConnection.get(playerinfo.getPlayerId());
                                //cs.terminate(); //hmm, this isn't right.
                                //plNetMsgTerminated termmsg = plNetMsgTerminated.createWithPlayeridxReason(playerinfo.getPlayerId(), (byte)2);
                                //PropagateBuffer bufmsg = PropagateBuffer.createWithObj(Typeid.plNetMsgTerminated, termmsg);
                                //GameServer.SendMsg(cs, bufmsg);
                            }
                        }
                    }
                    else m.throwUncaughtException("unexpected");
                }
                else
                {
                    m.throwUncaughtException("unhandled");
                }
            }
            else
            {
                try{
                    Thread.sleep(Manager.msToSleep);
                }catch(Exception e){}
            }
        }
    }

    private void HandleMessage(Object msg, ConnectionState cs)
    {
        Class klass = msg.getClass();

        if(klass==JoinAgeRequest.class)
        {
            m.msg("GameServer JoinAgeRequest");
            JoinAgeRequest request = (JoinAgeRequest)msg;

            //add them to the pile
            connections.add(cs);
            cs.player = Node.getNodeWithIndex(cs.playerId);
            cs.playerInfoIdx = Node.PlayerInfoNode.getFromPlayerId(cs.playerId).getIdx();
            playeridToConnection.put(cs.playerId, cs);
            manager.comm.registerForNodeChange(cs.playerInfoIdx, items); //let's be notified if this node changes.

            JoinAgeReply reply = new JoinAgeReply();
            reply.transId = request.transId;
            reply.result = ENetError.kNetSuccess;
            SendMsg(cs,reply);

            //send a page msg, but should this come after they get the vault nodes?
            boolean isOwner = true; //should this be different if someone is already here?
            Location pageloc = Location.createWithPrefixPagenumPagetype(0, 0, 4);
            plNetMsgGroupOwner bmsg = plNetMsgGroupOwner.createWithInfo(pageloc, isOwner);
            PropagateBuffer bmsg2 = PropagateBuffer.createWithObj(Typeid.plNetMsgGroupOwner, bmsg);
            TestMsg(bmsg2);
            SendMsg(cs,bmsg2);

        }
        else if(klass==PropagateBuffer.class)
        {
            //m.msg("GameServer PropagateBuffer");
            PropagateBuffer request = (PropagateBuffer)msg;

            //IBytestream stream = shared.ByteArrayBytestream.createFromByteArray(request.data);
            //uru.context ctxt = uru.context.createFromBytestream(stream);
            //ctxt.readversion = 6;
            try{
                //prpobjects.PrpTaggedObject obj = new prpobjects.PrpTaggedObject(ctxt);
                prpobjects.PrpTaggedObject obj = request.obj;
                //m.msg("GameServer PropagateBuffer: "+obj.type.toString());
                //if(ctxt.in.getBytesRemaining()!=0)
                //    m.throwUncaughtException("did not read all");

                if(obj.type==Typeid.plNetMsgLoadClone)
                {
                    plNetMsgLoadClone loadclonemsg = (plNetMsgLoadClone)obj.prpobject.object;
                    HandlePlNetMsgLoadClone(loadclonemsg,cs);
                }
                else if(obj.type==Typeid.plNetMsgPlayerPage)
                {
                    plNetMsgPlayerPage msg2 = (plNetMsgPlayerPage)obj.prpobject.object;
                    HandlePlNetMsgPlayerPage(msg2,cs);
                }
                else if(obj.type==Typeid.plNetMsgTestAndSet)
                {
                    plNetMsgTestAndSet msg2 = (plNetMsgTestAndSet)obj.prpobject.object;
                    HandlePlNetMsgTestAndSet(msg2,cs);
                }
                else if(obj.type==Typeid.plNetMsgMembersListReq)
                {
                    plNetMsgMembersListReq msg2 = (plNetMsgMembersListReq)obj.prpobject.object;
                    HandlePlNetMsgMembersListReq(msg2,cs);
                }
                else if(obj.type==Typeid.plNetMsgGameStateRequest)
                {
                    plNetMsgGameStateRequest msg2 = (plNetMsgGameStateRequest)obj.prpobject.object;
                    HandlePlNetMsgGameStateRequest(msg2,cs);
                }
                else if(obj.type==Typeid.plNetMsgRelevanceRegions)
                {
                    plNetMsgRelevanceRegions msg2 = (plNetMsgRelevanceRegions)obj.prpobject.object;
                    HandlePlNetMsgRelevanceRegions(msg2,cs);
                }
                else if(obj.type==Typeid.plNetMsgGameMessage)
                {
                    plNetMsgGameMessage msg2 = (plNetMsgGameMessage)obj.prpobject.object;
                    HandlePlNetMsgGameMessage(msg2,cs);
                }
                else if(obj.type==Typeid.plNetMsgPagingRoom)
                {
                    plNetMsgPagingRoom msg2 = (plNetMsgPagingRoom)obj.prpobject.object;
                    HandlePlNetMsgPagingRoom(msg2,cs);
                }
                else if(obj.type==Typeid.plNetMsgSDLState)
                {
                    plNetMsgSDLState msg2 = (plNetMsgSDLState)obj.prpobject.object;
                    HandlePlNetMsgSDLState(msg2,cs);
                }
                else if(obj.type==Typeid.plNetMsgSDLStateBCast)
                {
                    plNetMsgSDLStateBCast msg2 = (plNetMsgSDLStateBCast)obj.prpobject.object;
                    HandlePlNetMsgSDLStateBCast(msg2,cs);
                }
                else
                {
                    m.warn("unhandled");
                }

            }catch(Exception e){
                //e.printStackTrace();
                //m.throwUncaughtException("error reading object.");
                throw new shared.nested(e);
            }

            int dummy=0;
        }
        else
        {
            m.throwUncaughtException("Unhandled GameServer packet: "+klass.getSimpleName());
        }

    }

    private void HandlePlNetMsgSDLStateBCast(plNetMsgSDLStateBCast msg, ConnectionState cs)
    {
        m.msg("PlNetMsgSDLStateBCast");

        //just like NetMsgSDLSTate, but broadcasted to other players.

        saveSDLState(msg.parent);

        //todo:broadcast
        
    }
    private void HandlePlNetMsgSDLState(plNetMsgSDLState msg, ConnectionState cs)
    {
        m.msg("PlNetMsgSDLState");

        saveSDLState(msg);
    }
    private void saveSDLState(plNetMsgSDLState msg)
    {
        SdlBinary sdlbin = msg.getSdlBinary();
        Uruobjectdesc desc = msg.parent.parent.desc;
        SdlState sdlstate = findSdlState(desc,sdlbin);
        if(sdlstate==null)
        {
            //we don't have it yet, so just add it.
            sdlstate = new SdlState();
            sdlstate.desc = desc;
            sdlstate.sdlvars = sdlbin;
            sdlstates.add(sdlstate);
        }
        else
        {
            //we have it already, so we need to update it.
            sdlstate.sdlvars.update(sdlbin);
        }

        //update the vault with this, if it is the Age SDL. (as opposed to a clone/physical)
        String statedescname = sdlbin.name.toString();
        if(statedescname.equals(sdlStatedesc))
        {
            byte[] sdlbin_bs = sdlbin.compileAlone(Format.moul);
            Node.SDLNode sdlnode = new Node.SDLNode(RawNode.createEmpty());
            sdlnode.setType(Node.NodeType.kNodeSDL);
            sdlnode.setIdx(sdlIdx);
            sdlnode.setStatedescName(statedescname);
            sdlnode.setSdlBytecode(sdlbin_bs);
            manager.authserver.items.add(Comm.CommItem.SaveNode(sdlnode));
        }
    }
    private void HandlePlNetMsgPagingRoom(plNetMsgPagingRoom msg, ConnectionState cs)
    {
        m.msg("PlNetMsgPagingRoom");

        if(msg.parent.rooms.length!=1)
        {
            m.warn("Unhandled pagingroom");
        }

        int pagenum = msg.parent.rooms[0].location.pageid.getPageNumber();
        Page page = findPage(pagenum);
        if(page.loc==null) page.loc = msg.parent.rooms[0].location; //Same as Alcugs hack, so we don't have to read prps when the server starts up.
        
        if(page==null)
        {
            m.throwUncaughtException("unhandled");
        }

        if(msg.isPagingOut())
        {
            removePlayerFromPage(page,cs.playerId,cs);
        }
        else
        {
            //if they are not on the list, add them.
            if(!page.players.contains((Integer)cs.playerId))
            {
                page.players.add(cs.playerId);
                boolean isOwner;
                if(page.ownersPlayerIdx==0)
                {
                    page.ownersPlayerIdx = cs.playerId;
                    isOwner = true;
                }
                else
                {
                    isOwner = false;
                }

                //send msg
                plNetMsgGroupOwner bmsg = plNetMsgGroupOwner.createWithInfo(page.loc, isOwner);
                PropagateBuffer bmsg2 = PropagateBuffer.createWithObj(Typeid.plNetMsgGroupOwner, bmsg);
                TestMsg(bmsg2);
                SendMsg(cs,bmsg2);

            }
        }

    }
    private static class Page
    {
        PageInfo pageinfo;
        Location loc = null; //The same as the Alcugs hack, where we will set this after a request.  We could alternatively have read it from prp files, or just guessed it from the District name.
        
        ArrayList<Integer> players = new ArrayList();
        int ownersPlayerIdx; //id of page owner.
    }
    private void removePlayerFromPage(Page page, int playerIdx, ConnectionState cs)
    {
        boolean waspresent = page.players.remove((Integer)playerIdx);
        if(!waspresent)
        {
            return; //is this normal?
        }

        //if they were the owner, find a new one.
        if(page.ownersPlayerIdx==playerIdx)
        {
            if(page.players.size()!=0)
            {
                int newOwnerPlayerIdx = page.players.get(0);
                Manager.ensurePlayerIsOnline(newOwnerPlayerIdx);
                page.ownersPlayerIdx = newOwnerPlayerIdx;

                //send new owner message.
                if(page.loc==null) m.throwUncaughtException("location was not set");
                plNetMsgGroupOwner msg = plNetMsgGroupOwner.createWithInfo(page.loc, true);
                PropagateBuffer msg2 = PropagateBuffer.createWithObj(Typeid.plNetMsgGroupOwner, msg);
                TestMsg(msg2);
                SendMsg(cs,msg2);
            }
            else
            {
                page.ownersPlayerIdx = 0; //then there will be no owner.
            }
        }

    }
    private void HandlePlNetMsgGameMessage(plNetMsgGameMessage msg, ConnectionState cs)
    {
        m.msg("PlNetMsgGameMessage: "+msg.parent.obj.type.toString());
        if(msg.parent.obj.type==Typeid.plAvatarInputStateMsg)
        {
            plAvatarInputStateMsg msg2 = (plAvatarInputStateMsg)msg.parent.obj.prpobject.object;
            m.msg("  plAvatarInputStateMsg: "+Short.toString(msg2.state));
        }

        //todo: pass this on to the players who need it.
    }
    private void HandlePlNetMsgGameStateRequest(plNetMsgGameStateRequest msg, ConnectionState cs)
    {
        m.msg("PlNetMsgGameStateRequest");
        int msgssent = 0;

        //send clones
        if(msg.parent.rooms.length==0)
        {
            //this is the initial request and so we'll be sending the global stuff.

            //sendClones
            for(plLoadCloneMsg clone: clones)
            {
                plNetMsgLoadClone lc = plNetMsgLoadClone.createWithInfo(cs.playerId, clone, true);
                PropagateBuffer outmsg = PropagateBuffer.createWithObj(Typeid.plNetMsgLoadClone, lc);
                TestMsg(outmsg);
                SendMsg(cs,outmsg);
                msgssent++;
            }

        }

        //send sdl states
        for(SdlState sdlstate: sdlstates)
        {
            if(sdlstate.desc.hasCloneIds()) continue; //don't send clones
            boolean requested = false;
            for(plNetMsgRoomsList.Room room: msg.parent.rooms)
            {
                if(sdlstate.desc.pageid.equals(room.location.pageid))
                {
                    //found the room for this sdl state!  So this is an sdl state we will send.
                    requested = true;
                    break;
                }
            }

            if(requested)
            {
                //send a msg
                Uruobjectdesc desc = sdlstate.desc; //is this right?  Alcugs has different inheritance here, and we need to set this.
                SdlBinary sdlbin = sdlstate.sdlvars;
                plNetMsgSDLState ss = plNetMsgSDLState.createWithInfo(desc, sdlbin, true);

                PropagateBuffer outmsg = PropagateBuffer.createWithObj(Typeid.plNetMsgSDLState, ss);
                TestMsg(outmsg);
                SendMsg(cs,outmsg);
                msgssent++;
            }
        }

        //send initial state
        if(msg.parent.rooms.length==0)
        {
            plNetMsgInitialAgeStateSent msg2 = plNetMsgInitialAgeStateSent.createWithNumsent(msgssent);
            PropagateBuffer outmsg = PropagateBuffer.createWithObj(Typeid.plNetMsgInitialAgeStateSent,msg2);
            TestMsg(outmsg);
            SendMsg(cs,outmsg);
        }

    }
    private void HandlePlNetMsgRelevanceRegions(plNetMsgRelevanceRegions msg, ConnectionState cs)
    {
        m.msg("PlNetMsgRelevanceRegions");
        //this is for controlling which users in the age get sent messages, but like Alcugs, we'll just ignore this, as that should work fine.
        //so everyone will get all the messages, but only the city really uses it anyway :P
    }
    private void HandlePlNetMsgMembersListReq(plNetMsgMembersListReq msg, ConnectionState cs)
    {
        m.msg("PlNetMsgMembersListReq");

        //create list (todo: could be more efficient by not regenerating from scratch each time.)
        plNetMsgMembersList reply = plNetMsgMembersList.createWithPlayeridx(cs.playerId);
        for(ConnectionState member: connections)
        {
            if(member==cs) continue; //skip the player making the request. (Or perhaps we should include them for efficiency, so we can just use the same list for everyone and update just that one?
            Uruobjectdesc avatarDesc = connectionToClone.get(cs).cloneKey.xdesc;
            plNetMsgMembersList.plNetMsgMemberInfoHelper info = plNetMsgMembersList.plNetMsgMemberInfoHelper.createWithAvatardesc(avatarDesc);
            info.clientGuid.setPlayerId(cs.playerId);
            info.clientGuid.setPlayerName(cs.player.getPlayerName());
            info.clientGuid.setAcctGuid(cs.player.getAccountGuid());
            //info.clientGuid.setCCRLevel(1);
            reply.members.add(info);
        }

        PropagateBuffer rep = PropagateBuffer.createWithObj(Typeid.plNetMsgMembersList, reply);

        TestMsg(rep);
        SendMsg(cs,rep);

    }
    private void HandlePlNetMsgPlayerPage(plNetMsgPlayerPage msg, ConnectionState cs)
    {
        m.msg("PlNetMsgPlayerPage");
        //m.msg(msg.desc.toString());
        //called when client links to its first age and when it quits
        //msg.unload is 0 when linking to first age
        //msg.desc if the clone desc again (e.g. the Male sceneobject).
    }
    private void HandlePlNetMsgTestAndSet(plNetMsgTestAndSet msg, ConnectionState cs)
    {
        //we don't understand this, this is Alcugs magic^^

        if(msg.parent.lockRequest!=0)
        {
            plServerReplyMsg reply = plServerReplyMsg.createDefault();
            reply.parent.receivers.add(msg.parent.parent.parent.desc.toRef());
            reply.parent.broadcastFlags = plMessage.kLocalPropagate;
            reply.type = plServerReplyMsg.kAffirm;

            PrpTaggedObject obj = PrpTaggedObject.createWithTypeidUruobj(Typeid.plServerReplyMsg, reply);

            plNetMsgGameMessage rep2 = plNetMsgGameMessage.createWithObjPlayeridx(obj, cs.playerId);

            PrpTaggedObject obj2 = PrpTaggedObject.createWithTypeidUruobj(Typeid.plNetMsgGameMessage, rep2);

            PropagateBuffer rep = PropagateBuffer.createWithObj(obj2);

            TestMsg(rep);
            SendMsg(cs,rep);
        }
    }

    private void HandlePlNetMsgLoadClone(plNetMsgLoadClone loadclonemsg, ConnectionState cs)
    {
        plLoadCloneMsg lc2 = loadclonemsg.tryToGetLoadCloneMsg();
        if(lc2==null) m.throwUncaughtException("msg was not a plLoadCloneMsg");
        plLoadCloneMsg cloneFromList = findClone(lc2);

        if(loadclonemsg.isLoading!=0)
        {

            if(cloneFromList!=null)
            {
                m.msg("Updating clone.");
                clones.remove(cloneFromList);
                clones.add(lc2);
            }
            else
            {
                m.msg("Adding clone.");
                //plLoadCloneMsg a;
                //a/.
                clones.add(lc2);
            }
            connectionToClone.put(cs, lc2);
        }
        else
        {
            m.msg("Removing clone.");
            boolean waspresent = clones.remove(cloneFromList);
            //remove the clone sdl vars.
            if(!waspresent) m.warn("Tried to remove clone that is not present.");
            removeSDLStates(loadclonemsg.desc.clonePlayerId,loadclonemsg.desc.cloneId);
        }
    }

    private void TestMsg(GameMsg msg)
    {
        final boolean enabled = true;

        if(enabled)
        {
            byte[] data = null;
            try{
                Bytedeque c = new uru.Bytedeque(Format.moul);
                c.writeShort(msg.type());
                msg.write(c);
                data = c.getAllBytes();
            }catch(Exception e){
                throw new shared.nested(e);
            }

            try{
                IBytestream in = shared.ByteArrayBytestream.createFromByteArray(data);
                Object readobj = GameMainServer.ReadMessage(in,false); //assume it is an outgoing message.
                if(in.getBytesRemaining()!=0) m.throwUncaughtException("didn't read all bytes");
            }catch(Exception e){
                throw new shared.nested(e);
            }
            int dummy=0;
        }
    }

    private static class SdlState
    {
        Uruobjectdesc desc;
        //ArrayList<SdlBinary.plStateDataRecord> sdlvars;
        SdlBinary sdlvars;
    }
    
    private void removeSDLStates(int clonePlayerId, int cloneId)
    {
        //ArrayList<SdlBinary> deletees = new ArrayList();
        Iterator<SdlState> it = sdlstates.iterator();
        while(it.hasNext())
        {
            SdlState sdlstate = it.next();
            if(sdlstate.desc.hasCloneIds() && sdlstate.desc.clonePlayerId==clonePlayerId && (cloneId==0 || sdlstate.desc.cloneId==cloneId))
            {
                m.msg("Removing sdl state.");
                it.remove();
            }
        }
    }

    public synchronized void setMinimumShutdownTime(int seconds)
    {
        int newTimeToQuit = DateTimeUtils.getCurrentTimeInSeconds()+seconds;
        if(timeToShutdown.get()<newTimeToQuit)
        {
            timeToShutdown.set(newTimeToQuit);
        }
    }

    public void expectPlayer(int playerIdx)
    {
        //add an item to the queue or just do the work here?
    }

    static void SendMsg(ConnectionState cs, GameMsg msg)
    {
        Bytedeque c = new uru.Bytedeque(Format.moul);
        //c.format = Format.moul;
        c.writeShort(msg.type());
        msg.write(c);
        byte[] data = c.getAllBytes();

        cs.sendMsgBytes(data);
    }


}
