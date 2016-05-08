/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Concurrent;
import shared.Str;
import prpobjects.Guid;
import shared.*;
import prpobjects.uruobj;
import uru.Bytedeque;
import uru.server.*;
import java.io.File;
import java.util.Vector;
import moulserver.Node.*;
import moulserver.Comm.*;
import java.util.Queue;
//import moulserver.NetServer.ConnectionState;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AuthServer extends Thread
{

    final static short kCli2Auth_PingRequest = 0;
    final static short kCli2Auth_ClientRegisterRequest = 1;
    final static short kCli2Auth_ClientSetCCRLevel = 2;
    final static short kCli2Auth_AcctLoginRequest = 3;
    final static short kCli2Auth_AcctSetEulaVersion = 4;
    final static short kCli2Auth_AcctSetDataRequest = 5;
    final static short kCli2Auth_AcctSetPlayerRequest = 6;
    final static short kCli2Auth_AcctCreateRequest = 7;
    final static short kCli2Auth_AcctChangePasswordRequest = 8;
    final static short kCli2Auth_AcctSetRolesRequest = 9;
    final static short kCli2Auth_AcctSetBillingTypeRequest = 10;
    final static short kCli2Auth_AcctActivateRequest = 11;
    final static short kCli2Auth_AcctCreateFromKeyRequest = 12;
    final static short kCli2Auth_PlayerDeleteRequest = 13;
    final static short kCli2Auth_PlayerUndeleteRequest = 14;
    final static short kCli2Auth_PlayerSelectRequest = 15;
    final static short kCli2Auth_PlayerRenameRequest = 16;
    final static short kCli2Auth_PlayerCreateRequest = 17;
    final static short kCli2Auth_PlayerSetStatus = 18;
    final static short kCli2Auth_PlayerChat = 19;
    final static short kCli2Auth_UpgradeVisitorRequest = 20;
    final static short kCli2Auth_SetPlayerBanStatusRequest = 21;
    final static short kCli2Auth_KickPlayer = 22;
    final static short kCli2Auth_ChangePlayerNameRequest = 23;
    final static short kCli2Auth_SendFriendInviteRequest = 24;
    final static short kCli2Auth_VaultNodeCreate = 25;
    final static short kCli2Auth_VaultNodeFetch = 26;
    final static short kCli2Auth_VaultNodeSave = 27; //VaultSaveNodeReply
    final static short kCli2Auth_VaultNodeDelete = 28;
    final static short kCli2Auth_VaultNodeAdd = 29;
    final static short kCli2Auth_VaultNodeRemove = 30;
    final static short kCli2Auth_VaultFetchNodeRefs = 31;
    final static short kCli2Auth_VaultInitAgeRequest = 32;
    final static short kCli2Auth_VaultNodeFind = 33; //VaultNodeFindReply
    final static short kCli2Auth_VaultSetSeen = 34;
    final static short kCli2Auth_VaultSendNode = 35;
    final static short kCli2Auth_AgeRequest = 36;
    final static short kCli2Auth_FileListRequest = 37;
    final static short kCli2Auth_FileDownloadRequest = 38;
    final static short kCli2Auth_FileDownloadChunkAck = 39;
    final static short kCli2Auth_PropagateBuffer = 40;
    final static short kCli2Auth_GetPublicAgeList = 41;
    final static short kCli2Auth_SetAgePublic = 42;
    final static short kCli2Auth_LogPythonTraceback = 43;
    final static short kCli2Auth_LogStackDump = 44;
    final static short kCli2Auth_LogClientDebuggerConnect = 45;
    final static short kCli2Auth_ScoreCreate = 46;
    final static short kCli2Auth_ScoreDelete = 47;
    final static short kCli2Auth_ScoreGetScores = 48;
    final static short kCli2Auth_ScoreAddPoints = 49;
    final static short kCli2Auth_ScoreTransferPoints = 50;
    final static short kCli2Auth_ScoreSetPoints = 51;
    final static short kCli2Auth_ScoreGetRanks = 52;
    final static short kCli2Auth_AcctExistsRequest = 53;

    final static short kAuth2Cli_PingReply = 0;
    final static short kAuth2Cli_ServerAddr = 1;
    final static short kAuth2Cli_NotifyNewBuild = 2;
    final static short kAuth2Cli_ClientRegisterReply = 3;
    final static short kAuth2Cli_AcctLoginReply = 4;
    final static short kAuth2Cli_AcctData = 5;
    final static short kAuth2Cli_AcctPlayerInfo = 6;
    final static short kAuth2Cli_AcctSetPlayerReply = 7;
    final static short kAuth2Cli_AcctCreateReply = 8;
    final static short kAuth2Cli_AcctChangePasswordReply = 9;
    final static short kAuth2Cli_AcctSetRolesReply = 10;
    final static short kAuth2Cli_AcctSetBillingTypeReply = 11;
    final static short kAuth2Cli_AcctActivateReply = 12;
    final static short kAuth2Cli_AcctCreateFromKeyReply = 13;
    final static short kAuth2Cli_PlayerList = 14;
    final static short kAuth2Cli_PlayerChat = 15;
    final static short kAuth2Cli_PlayerCreateReply = 16;
    final static short kAuth2Cli_PlayerDeleteReply = 17;
    final static short kAuth2Cli_UpgradeVisitorReply = 18;
    final static short kAuth2Cli_SetPlayerBanStatusReply = 19;
    final static short kAuth2Cli_ChangePlayerNameReply = 20;
    final static short kAuth2Cli_SendFriendInviteReply = 21;
    final static short kAuth2Cli_FriendNotify = 22;
    final static short kAuth2Cli_VaultNodeCreated = 23;
    final static short kAuth2Cli_VaultNodeFetched = 24;
    final static short kAuth2Cli_VaultNodeChanged = 25;
    final static short kAuth2Cli_VaultNodeDeleted = 26;
    final static short kAuth2Cli_VaultNodeAdded = 27;
    final static short kAuth2Cli_VaultNodeRemoved = 28;
    final static short kAuth2Cli_VaultNodeRefsFetched = 29;
    final static short kAuth2Cli_VaultInitAgeReply = 30;
    final static short kAuth2Cli_VaultNodeFindReply = 31;
    final static short kAuth2Cli_VaultSaveNodeReply = 32;
    final static short kAuth2Cli_VaultAddNodeReply = 33;
    final static short kAuth2Cli_VaultRemoveNodeReply = 34;
    final static short kAuth2Cli_AgeReply = 35;
    final static short kAuth2Cli_FileListReply = 36;
    final static short kAuth2Cli_FileDownloadChunk = 37;
    final static short kAuth2Cli_PropagateBuffer = 38;
    final static short kAuth2Cli_KickedOff = 39;
    final static short kAuth2Cli_PublicAgeList = 40;
    final static short kAuth2Cli_ScoreCreateReply = 41;
    final static short kAuth2Cli_ScoreDeleteReply = 42;
    final static short kAuth2Cli_ScoreGetScoresReply = 43;
    final static short kAuth2Cli_ScoreAddPointsReply = 44;
    final static short kAuth2Cli_ScoreTransferPointsReply = 45;
    final static short kAuth2Cli_ScoreSetPointsReply = 46;
    final static short kAuth2Cli_ScoreGetRanksReply = 47;
    final static short kAuth2Cli_AcctExistsReply = 48;

    Queue<CommItem> items = Concurrent.getConcurrentQueue();
    Comm comm = new Comm();
    Manager manager;
    AuthServerSecureFiles securefiles = new AuthServerSecureFiles();

    public AuthServer(Manager manager)
    {
        this.manager = manager;
    }

    public void run()
    {
        m.msg("Starting AuthServer...");

        while(true)
        {
            CommItem item = items.poll();
            if(item!=null)
            {
                if(item.type==CommItemType.HandleMessage)
                {
                    HandleMessage(item.msg,item.connstate);
                }
                else if(item.type==CommItemType.GetAgeSdl)
                {
                    Node.AgeInfoNode ageinfo = Node.AgeInfoNode.getFromAgeInstanceGuid(item.guid);
                    Node sdl = null;
                    for(Node node: ageinfo.getChildrenNodes())
                    {
                        if(node.getType()==Node.NodeType.kNodeSDL)
                        {
                            sdl = node;
                            break;
                        }
                    }
                    if(sdl!=null)
                    {
                        manager.comm.Respond(item, CommItem.ResponseNode(sdl));
                    }
                    else
                    {
                        m.throwUncaughtException("node not found");
                    }
                }
                else if(item.type==CommItemType.SaveNode)
                {
                    Node nodeToSave = item.node;
                    VaultNodeChanged reply2 = new VaultNodeChanged();
                    reply2.nodeId = nodeToSave.getIdx();
                    reply2.revisionId = Guid.fullyRandom();
                    byte[] nodeData = nodeToSave.getAllBytes();
                    Node.SaveNode(nodeData,reply2,null);

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

    //public static void HandleAuthServerPacket(ConnectServer.ServerThread sock)
    //{
    //    m.throwUncaughtException("deprecated");
    //}

    public void HandleMessage(Object msg, ConnectionState cs)
    {

        //IBytestream c = sock.in;
        //short msgId = c.readShort();
        Class klass = msg.getClass();
        
        //if(msgId==AuthServer.kCli2Auth_PingRequest)
        if(klass==PingRequest.class)
        {
            m.msg("AuthServer PingRequest");
            PingRequest request = (PingRequest)msg;

            PingReply reply = new PingReply();
            reply.timestamp = request.timestamp;
            reply.transId = request.transId;
            reply.buffersize = request.buffer.length;
            reply.buffer = request.buffer;
            SendMsg(cs,reply);
        }
        //else if(msgId==AuthServer.kCli2Auth_ClientRegisterRequest)
        else if(klass==ClientRegisterRequest.class)
        {
            m.msg("AuthServer ClientRegisterRequest");
            ClientRegisterRequest request = (ClientRegisterRequest)msg;
            //byte[] b1 = c.readBytes(14);
            //int b2 = c.readInt();
            //int b3 = c.readInt();
            //int b4 = c.readShort(); //0
            //short b5 = c.readShort();
            ClientRegisterReply reply = new ClientRegisterReply();
            //reply.serverchallenge = 0; //should be random I guess :P
            //reply.serverchallenge = sock.rng.nextInt();
            //reply.serverchallenge = 0x8b835969;
            reply.serverchallenge = RandomUtils.rng.nextInt();
            cs.serverchallenge = reply.serverchallenge;
            SendMsg(cs,reply);
        }
        //else if(msgId==AuthServer.kCli2Auth_AcctLoginRequest)
        else if(klass==AcctLoginRequest.class)
        {
            AcctLoginRequest request = (AcctLoginRequest)msg;
            m.msg("AuthServer AcctLoginRequest: " + request.accountName.toString());
            Database.accountinfo user = manager.database.GetUser(request.accountName.toString());
            AcctLoginReply reply = new AcctLoginReply();
            reply.transId = request.transId;
            if(user==null)
            {
                reply.result = ENetError.kNetErrAccountNotFound;
                reply.accountUUID = Guid.none();
                reply.accountFlags = 0;
                reply.billingType = 0;
            }
            else
            {
                //byte[] expectedhash = shared.CryptHashes.GetHash(new byte[]{}, CryptHashes.Hashtype.sha1);
                //byte[] givenhash = request.getProperPasswordHash(user.accountname);
                //boolean isPasswordCorrect = b.isEqual(user.passwordhash, givenhash);
                //if(!isPasswordCorrect)
                if(!request.checkPassword(user.passwordhash, cs.serverchallenge))
                {
                    reply.result = ENetError.kNetErrAuthenticationFailed;
                    reply.accountUUID = Guid.none();
                    reply.accountFlags = 0;
                    reply.billingType = 0;
                }
                else
                {
                    reply.result = ENetError.kNetSuccess;
                    reply.accountUUID = user.guid;
                    reply.accountFlags = user.flags;
                    reply.billingType = user.billingtype;

                    //we've authenticated the user, so put it in the ConnectionState.
                    cs.setAccount(user);
                }
            }
            reply.encryptionKey = SuperManager.GetTalcumNotthedroids();

            if(reply.result==ENetError.kNetSuccess)
            {
                //send ServerAddr (doesn't seem necessary, and the address doesn't seem to fit anything.)
                //The address seems to be from a private intranet, so perhaps it was used to inform the client of precisely which server it was connected to behind a gateway.
                ServerAddr msg1 = new ServerAddr();
                msg1.address = 0x0A0B0C0D;
                msg1.guid = Guid.none(); //is this okay?
                SendMsg(cs,msg1);

                //get all the PlayerNodes whose accountGuid matches.
                Vector<PlayerNode> nodes = manager.database.sqlquery("SELECT * FROM vault WHERE "+Database.type+"=? AND "+Database.uuid_1+"=?", NodeType.kNodePlayer, cs.account.guid).cast();
                for(PlayerNode node: nodes)
                {
                    AcctPlayerInfo msg2 = new AcctPlayerInfo();
                    msg2.transId = request.transId;
                    msg2.playerId = node.getIdx();
                    msg2.playerName = new Str(node.getPlayerName());
                    msg2.avatarModel = new Str(node.getAvatarShape());
                    msg2.explorer = node.getExplorer();
                    SendMsg(cs,msg2);
                }
            }

            //send the reply *after* the other messages
            SendMsg(cs,reply);

        }
        //else if(msgId==kCli2Auth_FileListRequest)
        else if(klass==FileListRequest.class)
        {
            m.msg("AuthServer FileListRequest");
            //secure download queue
            FileListRequest request = (FileListRequest)msg;

            FileListReply reply = new FileListReply();
            reply.transId = request.transId;
            reply.result = ENetError.kNetSuccess;
            reply.manifest = SecureDownloadManifest.getManifest(request.dir.toString(), request.extension.toString(), manager, securefiles);
            //reply.manifest = SecureDownloadManifest.getEmptyManifest(); //client crashes with manifest with no entries.
            AuthServer.SendMsg(cs, reply);
        }
        //else if(msgId==kCli2Auth_FileDownloadRequest)
        else if(klass==FileDownloadRequest.class)
        {
            try{
                m.msg("AuthServer FileDownloadRequest");
                FileDownloadRequest request = (FileDownloadRequest)msg;

                //File f = FileServer.GetFile(request.filename.toString(), true, manager);
                File f = FileServer.GetFile(request.filename.toString(), manager.settings.getAuthFileserverPath());

                //we used to read from a file here, but we're now reading into ram and encrypting.
                //java.io.FileInputStream fis = new java.io.FileInputStream(f);
                //int filesize = (int)f.length();
                byte[] encdata = securefiles.GetEncrypted(f.getAbsolutePath());
                java.io.ByteArrayInputStream fis = new java.io.ByteArrayInputStream(encdata);
                int filesize = encdata.length;
                ChunkSendHandler.ChunkFile chunk = cs.chunksendhandler.startfile(request.filename.toString(), filesize, request.transId, fis, true);

                FileDownloadChunk reply = new FileDownloadChunk();
                reply.transId = request.transId;
                reply.result = ENetError.kNetSuccess;
                //byte[] data = FileUtils.ReadFile(f);
                reply.filesize = filesize;
                //reply.chunkOffset = 0;
                reply.chunkOffset = chunk.offset();
                //reply.buffer = data;
                reply.buffer = chunk.read();
                SendMsg(cs,reply);
            }catch(Exception e){throw new shared.nested(e);}
        }
        //else if(msgId==kCli2Auth_FileDownloadChunkAck)
        else if(klass==FileDownloadChunkAck.class)
        {
            //m.msg("AuthServer FileDownloadChunkAck");
            FileDownloadChunkAck ack = (FileDownloadChunkAck)msg;
            
            //Thank ya' kindly, yet again!
            ChunkSendHandler.ChunkFile chunk = cs.chunksendhandler.ack(ack.transId);
            if(!chunk.done)
            {
                //send another!
                FileDownloadChunk reply = new FileDownloadChunk();
                reply.transId = ack.transId;
                reply.result = ENetError.kNetSuccess;
                reply.filesize = chunk.filesize;
                reply.chunkOffset = chunk.offset();
                reply.buffer = chunk.read();
                SendMsg(cs,reply);
            }
            else
            {
                cs.chunksendhandler.clearfile(ack.transId);
            }

        }
        //else if(msgId==kCli2Auth_AcctSetPlayerRequest)
        else if(klass==AcctSetPlayerRequest.class)
        {
            m.msg("AuthServer AcctSetPlayerRequest");
            AcctSetPlayerRequest request = (AcctSetPlayerRequest)msg;
            //cs.playerId = request.playerId; //0 means none.
            cs.setPlayerIdx(request.playerId);

            //nulify old cs.listener if it exists
            if(cs.listener!=null)
            {
                cs.listener.unregister();
            }

            if(cs.playerId!=0)
            {
                PlayerInfoNode playerinfo = PlayerInfoNode.getFromPlayerId(cs.playerId);
                cs.playerInfoIdx = playerinfo.getIdx();
                //cs.listener = comm.MakeListener();
                //cs.listener.registerForPlayerIdx(cs.playerId);
                //cs.listener.registerForNodeLink(cs.playerId);

                //initialise nodemanager, and lock everything.
                cs.nodemgr = NodeManager.CreateNewNodeManager(cs.playerId);
                
                //Manager.comm.registerForNodeChange(cs.playerInfoIdx, cs.items); //we should register all their nodes.
            }
            else
            {
                //release nodemanager
            }

            AcctSetPlayerReply reply = new AcctSetPlayerReply();
            reply.transId = request.transId;
            reply.result = ENetError.kNetSuccess;
            SendMsg(cs,reply);

        }
        //else if(msgId==kCli2Auth_PlayerCreateRequest)
        else if(klass==PlayerCreateRequest.class)
        {
            m.msg("AuthServer PlayerCreateRequest");
            PlayerCreateRequest request = (PlayerCreateRequest)msg;

            PlayerCreateReply reply = new PlayerCreateReply();
            //Manager.database.CreatePlayer(request.playerName.toString(),request.avatarShape.toString());
            String playerName = request.playerName.toString();
            Node.PlayerNode playernode = Node.PlayerNode.findByName(playerName);
            if(playernode==null)
            {
                //create the player, as requested.
                //playernode = new Node.PlayerNode(Node.getRoot(), playerName, request.avatarShape.toString(), sock.user.guid, 1);
                //Node.PlayerInfoNode playerinfonode = new Node.PlayerInfoNode(playernode, playernode.getIdx());
                //Node.AgeNode avatarclosetnode = new Node.AgeNode(playernode, playerinfonode, "AvatarCustomization", "AvatarCustomization", playerName+"'s", "AvatarCustomizationDescription");
                playernode = NodeUtils.CreatePlayer(playerName, request.avatarShape.toString(), new Guid(cs.account.guid));
                reply.result = ENetError.kNetSuccess;
                reply.playerId = playernode.getIdx();
                reply.explorer = playernode.getExplorer(); //what is this?
            }
            else
            {
                reply.result = ENetError.kNetErrPlayerAlreadyExists;
                reply.playerId = 0;
                reply.explorer = 0;
            }
            reply.transId = request.transId;
            reply.playerName = request.playerName;
            reply.avatarModel = request.avatarShape;
            SendMsg(cs,reply);
        }
        //else if(msgId==kCli2Auth_VaultFetchNodeRefs)
        else if(klass==VaultFetchNodeRefs.class)
        {
            m.msg("AuthServer VaultFetchNodeRefs");
            VaultFetchNodeRefs request = (VaultFetchNodeRefs)msg;

            VaultNodeRefsFetched reply = new VaultNodeRefsFetched();
            reply.transId = request.transId;
            //reply.refs =  Node.getChildrenOfIdx(request.nodeId);
            reply.refs = Node.FindTreeRefs(request.nodeId);
            for(Node.Ref ref: reply.refs)
            {
                m.msg("    "+ref.dump());
            }
            reply.result = ENetError.kNetSuccess;

            //subscribe if this is the sort of node we should subscribe to.
            manager.vaultlistener.SubscribeIfApplicable(request.nodeId, cs.weakitems, reply.refs);

            SendMsg(cs,reply);
        }
        //else if(msgId==kCli2Auth_VaultNodeFetch)
        else if(klass==VaultNodeFetch.class)
        {
            m.msg("AuthServer VaultNodeFetch");
            VaultNodeFetch request = (VaultNodeFetch)msg;

            Node n = Node.getNodeWithIndex(request.nodeId);
            //if we can't find it, return a ENetError.kNetErrVaultNodeNotFound.
            byte[] nodedata = n.getAllBytes();

            //test it
            if(false)
            {
                IBytestream c2 = shared.ByteArrayBytestream.createFromByteArray(nodedata);
                RawNode rn = new RawNode(c2);
                int dummy=0;
            }
            
            VaultNodeFetched reply = new VaultNodeFetched();
            reply.transId = request.transId;
            reply.result = ENetError.kNetSuccess;
            reply.data = nodedata;
            SendMsg(cs,reply);
        }
        //else if(msgId==kCli2Auth_VaultNodeSave)
        else if(klass==VaultNodeSave.class)
        {
            VaultNodeSave request = (VaultNodeSave)msg;
            m.msg("AuthServer VaultNodeSave: "+Integer.toString(request.nodeId));

            VaultSaveNodeReply reply = new VaultSaveNodeReply();
            reply.transId = request.transId;
            reply.result = ENetError.kNetSuccess;
            SendMsg(cs,reply);

            //notify others, including the caller.
            VaultNodeChanged reply2 = new VaultNodeChanged();
            reply2.nodeId = request.nodeId;
            reply2.revisionId = request.revisionId;
            Node.SaveNode(request.nodeData,reply2,cs);

            //todo: notify others
            //VaultNodeChanged reply2 = new VaultNodeChanged();
            //reply2.nodeId = request.nodeId;
            //reply2.revisionId = request.revisionId;
            //SendMsg(cs,reply2);
            //Manager.comm.triggerNodeChange(reply2.nodeId, new Comm.CommItem_NodeChange(reply2));
            //SendMsg(cs,reply2); //send to ourself.

        }
        //else if(msgId==kCli2Auth_VaultInitAgeRequest)
        else if(klass==VaultInitAgeRequest.class)
        {
            m.msg("AuthServer VaultInitAgeRequest");
            VaultInitAgeRequest request = (VaultInitAgeRequest)msg;

            //we must use the requested guid or the game freezes.
            //AgeNode age = NodeUtils.CreateAge(sock.playerId, request.ageFilename.toString());
            NodeUtils.CreateAgeReturnInfo info = NodeUtils.CreateAge(cs.playerId, request.ageFilename.toString(), request.ageinstanceGuid, cs.playerInfoIdx, request.ageInstanceName.toString(),request.ageUserName.toString(),request.ageDescription.toString());
                //AgeInfoNode ageinfo = NodeUtils.CreateAgeInfo(age, cs.playerInfoIdx, request.ageFilename.toString(), request.ageInstanceName.toString(), request.ageUserName.toString(), request.ageDescription.toString());
                //age.addChild(ageinfo);

            VaultInitAgeReply reply = new VaultInitAgeReply();
            reply.transId = request.transId;
            reply.result = ENetError.kNetSuccess;
            reply.ageVaultId = info.age.getIdx();
            reply.ageInfoVaultId = info.ageinfo.getIdx();
            SendMsg(cs, reply);

        }
        //else if(msgId==kCli2Auth_VaultNodeCreate)
        else if(klass==VaultNodeCreate.class)
        {
            m.msg("AuthServer VaultNodeCreate");
            VaultNodeCreate request = (VaultNodeCreate)msg;

            RawNode n = new RawNode(request.nodeData);
            n.setFieldsOnNewNode();
            n.insert();

            VaultNodeCreated reply = new VaultNodeCreated();
            reply.transId = request.transId;
            reply.result = ENetError.kNetSuccess;
            reply.nodeId = n.nodeIdx;
            SendMsg(cs,reply);
        }
        //else if(msgId==kCli2Auth_VaultNodeAdd)
        else if(klass==VaultNodeAdd.class)
        {
            m.msg("AuthServer VaultNodeAdd");
            VaultNodeAdd request = (VaultNodeAdd)msg;

            //add node link
            Node.createLink(request.parentId, request.childId, request.ownerId, (byte)0);

            //todo: send VaultNodeAdded messages to subscribers, including ourself
            //VaultNodeAdded reply = new VaultNodeAdded();
            VaultNodeAdded reply2 = new VaultNodeAdded();
            reply2.parentId = request.parentId;
            reply2.childId = request.childId;
            reply2.ownerId = request.ownerId;
            SendMsg(cs,reply2);

            VaultAddNodeReply reply = new VaultAddNodeReply();
            reply.transId = request.transId;
            reply.result = ENetError.kNetSuccess;
            SendMsg(cs,reply);
        }
        //else if(msgId==kCli2Auth_AgeRequest)
        else if(klass==AgeRequest.class)
        {
            m.msg("AuthServer AgeRequest");
            AgeRequest request = (AgeRequest)msg;

            AgeReply reply = new AgeReply();
            reply.transId = request.transId;
            reply.result = ENetError.kNetSuccess;
            //reply.ageMCPId = 42; //is this the id of the game server, so you can have many game servers per address?
            GameServer gameserver = manager.gamemainserver.requestGameserverFromGuid(request.ageInstanceGuid,request.ageName.toString(),cs.playerId);
            reply.ageMCPId = gameserver.gameServerNumber;
            //cs.currentGameServer = new WeakReference(gameserver);
            //cs.currentGameServer = Manager.gamemainserver.
            //cs.currentGameServer = request.ageInstanceGuid;
            reply.ageInstanceGuid = request.ageInstanceGuid;
            AgeNode age = AgeNode.findFromGuid(request.ageInstanceGuid);
            reply.ageVaultId = age.getIdx();
            reply.gameServerAddress = manager.address;
            //reply.gameServerAddress = b.reverseEndianness(Manager.address);
            SendMsg(cs,reply);
        }
        else if(klass==LogPythonTraceback.class)
        {
            m.msg("AuthServer LogPythonTraceback");
            LogPythonTraceback log = (LogPythonTraceback)msg;
            m.msg(log.text.toString());
        }
        else if(klass==LogStackDump.class)
        {
            m.msg("AuthServer LogStackDump");
            //This occurs when the *previous* session had a stackdump.  So it just lets us know that a stackdump occured during the player's last session.  A good idea, actually!
            LogStackDump log = (LogStackDump)msg;
            m.msg(log.dumptext.toString());
        }
        else if(klass==VaultNodeFind.class)
        {
            m.msg("AuthServer VaultNodeFind");
            VaultNodeFind request = (VaultNodeFind)msg;

            VaultNodeFindReply reply = new VaultNodeFindReply();
            reply.transId = request.transId;
            
            RawNode rawnode = new RawNode(request.nodedata);
            reply.nodeIds = rawnode.makeSqlQuery();
            if(reply.nodeIds.length==1)
            {
                reply.result = ENetError.kNetSuccess;
            }
            else if(reply.nodeIds.length==0)
            {
                reply.result = ENetError.kNetErrVaultNodeNotFound;
            }
            else
            {
                m.throwUncaughtException("is this okay?");
            }

            SendMsg(cs,reply);

            //reply.result; //should contain kNetErrVaultNodeNotFound if it was not found.
            int dummy=0;
        }
        else
        {
            m.throwUncaughtException("Unhandled AuthServer packet: "+klass.getSimpleName());
        }

    }
    
    public static AuthMsg ReadMessage(IBytestream c, boolean isServer)
    {

        short msgId = c.readShort();

        if(isServer)
        {
            switch(msgId)
            {
                case AuthServer.kCli2Auth_PingRequest:
                    return new PingRequest(c);
                case AuthServer.kCli2Auth_ClientRegisterRequest:
                    return new ClientRegisterRequest(c);
                case AuthServer.kCli2Auth_AcctLoginRequest:
                    return new AcctLoginRequest(c);
                case kCli2Auth_FileListRequest:
                    return new FileListRequest(c);
                case kCli2Auth_FileDownloadRequest:
                    return new FileDownloadRequest(c);
                case kCli2Auth_FileDownloadChunkAck:
                    return new FileDownloadChunkAck(c);
                case kCli2Auth_AcctSetPlayerRequest:
                    return new AcctSetPlayerRequest(c);
                case kCli2Auth_PlayerCreateRequest:
                    return new PlayerCreateRequest(c);
                case kCli2Auth_VaultFetchNodeRefs:
                    return new VaultFetchNodeRefs(c);
                case kCli2Auth_VaultNodeFetch:
                    return new VaultNodeFetch(c);
                case kCli2Auth_VaultNodeSave:
                    return new VaultNodeSave(c);
                case kCli2Auth_VaultInitAgeRequest:
                    return new VaultInitAgeRequest(c);
                case kCli2Auth_VaultNodeCreate:
                    return new VaultNodeCreate(c);
                case kCli2Auth_VaultNodeAdd:
                    return new VaultNodeAdd(c);
                case kCli2Auth_AgeRequest:
                    return new AgeRequest(c);
                case kCli2Auth_VaultNodeFind:
                    return new VaultNodeFind(c);
                case kCli2Auth_LogPythonTraceback:
                    return new LogPythonTraceback(c);
                //talcumproxy
                case kCli2Auth_ScoreGetScores:
                    return new ScoreGetScores(c);
                case kCli2Auth_PlayerDeleteRequest:
                    return new PlayerDeleteRequest(c);
                case kCli2Auth_LogStackDump:
                    return new LogStackDump(c);
                default:
                    throw new shared.uncaughtexception("Unread AuthServer packet: "+Short.toString(msgId));
            }
        }
        else
        {
            switch(msgId)
            {
                case AuthServer.kAuth2Cli_PingReply:
                    return new PingReply(c);
                case AuthServer.kAuth2Cli_ClientRegisterReply:
                    return new ClientRegisterReply(c);
                case AuthServer.kAuth2Cli_ServerAddr:
                    return new ServerAddr(c);
                case AuthServer.kAuth2Cli_AcctPlayerInfo:
                    return new AcctPlayerInfo(c);
                case AuthServer.kAuth2Cli_AcctLoginReply:
                    return new AcctLoginReply(c);
                case AuthServer.kAuth2Cli_FileListReply:
                    return new FileListReply(c);
                case AuthServer.kAuth2Cli_FileDownloadChunk:
                    return new FileDownloadChunk(c);
                case AuthServer.kAuth2Cli_VaultNodeFetched:
                    return new VaultNodeFetched(c);
                case AuthServer.kAuth2Cli_VaultNodeRefsFetched:
                    return new VaultNodeRefsFetched(c);
                //start of ones added for TalcumProxy
                case AuthServer.kAuth2Cli_AcctSetPlayerReply:
                    return new AcctSetPlayerReply(c);
                case AuthServer.kAuth2Cli_VaultNodeFindReply:
                    return new VaultNodeFindReply(c);
                case AuthServer.kAuth2Cli_VaultNodeChanged:
                    return new VaultNodeChanged(c);
                case AuthServer.kAuth2Cli_VaultSaveNodeReply:
                    return new VaultSaveNodeReply(c);
                case AuthServer.kAuth2Cli_AgeReply:
                    return new AgeReply(c);
                case AuthServer.kAuth2Cli_ScoreGetScoresReply:
                    return new ScoreGetScoresReply(c);
                case AuthServer.kAuth2Cli_PlayerDeleteReply:
                    return new PlayerDeleteReply(c);
                case AuthServer.kAuth2Cli_PlayerCreateReply:
                    return new PlayerCreateReply(c);
                case AuthServer.kAuth2Cli_VaultInitAgeReply:
                    return new VaultInitAgeReply(c);
                case AuthServer.kAuth2Cli_VaultNodeCreated:
                    return new VaultNodeCreated(c);
                case AuthServer.kAuth2Cli_VaultNodeAdded:
                    return new VaultNodeAdded(c);
                case AuthServer.kAuth2Cli_VaultAddNodeReply:
                    return new VaultAddNodeReply(c);
                case AuthServer.kAuth2Cli_KickedOff:
                    return new KickedOff(c);
                default:
                    throw new shared.uncaughtexception("Unread AuthServer packet: "+Short.toString(msgId));
            }
        }

    }

    public static class LogStackDump extends AuthMsg
    {
        Str dumptext;

        public LogStackDump(){}
        public LogStackDump(IBytestream c)
        {
            dumptext = Str.readAsUtf16Sized16(c);
            //m.msg("Client stack dump! Reason: ",dumptext.toString());
        }
        public short type(){return AuthServer.kCli2Auth_LogStackDump;}
        public void write(IBytedeque c)
        {
            dumptext.writeAsUtf16Sized16(c);
        }
    }
    public static class KickedOff extends AuthMsg
    {
        int reason;

        public KickedOff(){}
        public KickedOff(IBytestream c)
        {
            reason = c.readInt();
            m.msg("Kicked off! Reason: ",Integer.toString(reason));
        }
        public short type(){return AuthServer.kAuth2Cli_KickedOff;}
        public void write(IBytedeque c)
        {
            c.writeInt(reason);
        }
    }

    public static class PlayerDeleteReply extends AuthMsg
    {
        int transId;
        int result;

        public PlayerDeleteReply(){}
        public PlayerDeleteReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
        }
        public short type(){return AuthServer.kAuth2Cli_PlayerDeleteReply;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
        }
    }

    public static class PlayerDeleteRequest extends AuthMsg
    {
        int transId;
        int playerId;

        public PlayerDeleteRequest(){}
        public PlayerDeleteRequest(IBytestream c)
        {
            transId = c.readInt();
            playerId = c.readInt();
        }
        public short type(){return AuthServer.kCli2Auth_PlayerDeleteRequest;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(playerId);
        }
    }

    public static class ScoreGetScoresReply extends AuthMsg
    {
        int transId;
        int result;
        int scoreCount;
        private int size;
        byte[] scoreData;

        public ScoreGetScoresReply(){}
        public ScoreGetScoresReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            scoreCount = c.readInt();
            size = c.readInt();
            if(size!=0)
            {
                int dummy=0;
            }
            scoreData = c.readBytes(size); //I think this is right
        }
        public short type(){return AuthServer.kAuth2Cli_ScoreGetScoresReply;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(scoreCount);
            c.writeInt(scoreData.length);
            c.writeBytes(scoreData);
        }
    }

    public static class ScoreGetScores extends AuthMsg
    {
        int transId;
        int ownerIdx;
        Str gamename; //e.g. "PelletDrop"

        public ScoreGetScores(){}
        public ScoreGetScores(IBytestream c)
        {
            transId = c.readInt();
            ownerIdx = c.readInt();
            gamename = Str.readAsUtf16Sized16(c);
        }
        public short type(){return AuthServer.kCli2Auth_ScoreGetScores;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(ownerIdx);
            gamename.writeAsUtf16Sized16(c);
        }
    }

    public static class LogPythonTraceback extends AuthMsg
    {
        Str text;

        public LogPythonTraceback(){}
        public LogPythonTraceback(IBytestream c)
        {
            text = Str.readAsUtf16Sized16(c);
        }
        public short type(){return AuthServer.kCli2Auth_LogPythonTraceback;}
        public void write(IBytedeque c)
        {
            text.writeAsUtf16Sized16(c);
        }
    }

    public static class VaultNodeFindReply extends AuthMsg
    {
        int transId;
        int result;
        int numNodesFound;
        int[] nodeIds;

        public VaultNodeFindReply(){}
        public VaultNodeFindReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            numNodesFound = c.readInt();
            //if(numNodesFound!=0)
            //{
            //    int dummy=0;
            //}
            nodeIds = c.readInts(numNodesFound);
        }
        public short type(){return AuthServer.kAuth2Cli_VaultNodeFindReply;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(nodeIds.length);
            c.writeInts(nodeIds);
        }
    }

    public static class VaultNodeFind extends AuthMsg
    {
        int transId;
        private int size;
        byte[] nodedata;

        public VaultNodeFind(){}
        public VaultNodeFind(IBytestream c)
        {
            transId = c.readInt();
            size = c.readInt();
            nodedata = c.readBytes(size);
            //RawNode node = new RawNode(nodedata);
            int dummy=0;
        }
        public short type(){return AuthServer.kCli2Auth_VaultNodeFind;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(nodedata.length);
            c.writeBytes(nodedata);
        }
        public String dump()
        {
            StringBuilder r = new StringBuilder();
            r.append(super.dump());
            RawNode node = new RawNode(nodedata);
            r.append(" : "+node.toString());
            return r.toString();
        }
    }

    public static class AgeReply extends AuthMsg
    {
        int transId;
        int result;
        int ageMCPId;
        Guid ageInstanceGuid;
        int ageVaultId;
        int gameServerAddress;
        
        public AgeReply(){}
        public AgeReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            ageMCPId = c.readInt();
            ageInstanceGuid = new Guid(c);
            ageVaultId = c.readInt();
            gameServerAddress = c.readInt();
        }
        public short type(){return AuthServer.kAuth2Cli_AgeReply;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(ageMCPId);
            ageInstanceGuid.write(c);
            c.writeInt(ageVaultId);
            c.writeInt(gameServerAddress);
        }
    }

    public static class AgeRequest extends AuthMsg
    {
        int transId;
        Str ageName;
        Guid ageInstanceGuid;

        public AgeRequest(IBytestream c)
        {
            transId = c.readInt();
            ageName = Str.readAsUtf16Sized16(c);
            ageInstanceGuid = new Guid(c);
        }
        public short type(){ return AuthServer.kCli2Auth_AgeRequest; }
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            ageName.writeAsUtf16Sized16(c);
            ageInstanceGuid.write(c);
        }
    }

    public static class VaultAddNodeReply extends AuthMsg
    {
        int transId;
        int result;

        public VaultAddNodeReply(){}
        public VaultAddNodeReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
        }
        public short type(){return AuthServer.kAuth2Cli_VaultAddNodeReply;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
        }
    }
    
    public static class VaultNodeAdded extends AuthMsg
    {
        int parentId;
        int childId;
        int ownerId;
        
        public VaultNodeAdded(){}
        public VaultNodeAdded(IBytestream c)
        {
            parentId = c.readInt();
            childId = c.readInt();
            ownerId = c.readInt();
        }
        public short type(){return AuthServer.kAuth2Cli_VaultNodeAdded;}
        public void write(IBytedeque c)
        {
            c.writeInt(parentId);
            c.writeInt(childId);
            c.writeInt(ownerId);
        }
    }

    public static class VaultNodeAdd extends AuthMsg
    {
        int transId;
        int parentId;
        int childId;
        int ownerId;

        public VaultNodeAdd(IBytestream c)
        {
            transId = c.readInt();
            parentId = c.readInt();
            childId = c.readInt();
            ownerId = c.readInt();
        }
        public short type(){ return AuthServer.kCli2Auth_VaultNodeAdd; }
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(parentId);
            c.writeInt(childId);
            c.writeInt(ownerId);
        }
    }

    public static class VaultNodeCreated extends AuthMsg
    {
        int transId;
        int result;
        int nodeId;

        public VaultNodeCreated(){}
        public VaultNodeCreated(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            nodeId = c.readInt();
        }
        public short type(){return AuthServer.kAuth2Cli_VaultNodeCreated;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(nodeId);
        }

    }

    public static class VaultNodeCreate extends AuthMsg
    {
        int transId;
        private int size;
        byte[] nodeData;

        public VaultNodeCreate(IBytestream c)
        {
            transId = c.readInt();
            size = c.readInt();
            nodeData = c.readBytes(size);
        }
        public short type(){ return AuthServer.kCli2Auth_VaultNodeCreate; }
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(nodeData.length);
            c.writeBytes(nodeData);
        }
    }
    
    public static class VaultInitAgeReply extends AuthMsg
    {
        int transId;
        int result;
        int ageVaultId;
        int ageInfoVaultId;
        
        public VaultInitAgeReply(){}
        public VaultInitAgeReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            ageVaultId = c.readInt();
            ageInfoVaultId = c.readInt();
        }
        public short type(){return AuthServer.kAuth2Cli_VaultInitAgeReply;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(ageVaultId);
            c.writeInt(ageInfoVaultId);
        }
    }

    public static class VaultInitAgeRequest extends AuthMsg
    {
        int transId;
        Guid ageinstanceGuid;
        Guid parentAgeGuid;
        Str ageFilename;
        Str ageInstanceName;
        Str ageUserName;
        Str ageDescription;
        int ageSequenceNumber; //0, e.g. Not the sequence prefix, it seems.
        int ageLanguage; //-1 means no particular language?

        public VaultInitAgeRequest(IBytestream c)
        {
            transId = c.readInt();
            ageinstanceGuid = new Guid(c);
            parentAgeGuid = new Guid(c);
            ageFilename = Str.readAsUtf16Sized16(c);
            ageInstanceName = Str.readAsUtf16Sized16(c);
            ageUserName = Str.readAsUtf16Sized16(c);
            ageDescription = Str.readAsUtf16Sized16(c);
            ageSequenceNumber = c.readInt();
            ageLanguage = c.readInt();
        }
        public short type(){ return AuthServer.kCli2Auth_VaultInitAgeRequest; }
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            ageinstanceGuid.write(c);
            parentAgeGuid.write(c);
            ageFilename.writeAsUtf16Sized16(c);
            ageInstanceName.writeAsUtf16Sized16(c);
            ageUserName.writeAsUtf16Sized16(c);
            ageDescription.writeAsUtf16Sized16(c);
            c.writeInt(ageSequenceNumber);
            c.writeInt(ageLanguage);
        }
    }
    
    public static class VaultSaveNodeReply extends AuthMsg
    {
        int transId;
        int result;
        
        public VaultSaveNodeReply(){}
        public VaultSaveNodeReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
        }
        public short type(){return AuthServer.kAuth2Cli_VaultSaveNodeReply;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
        }
    }

    public static class VaultNodeChanged extends AuthMsg
    {
        int nodeId;
        Guid revisionId;
        
        public VaultNodeChanged(){}
        public VaultNodeChanged(IBytestream c)
        {
            nodeId = c.readInt();
            revisionId = new Guid(c);
        }
        public short type(){return AuthServer.kAuth2Cli_VaultNodeChanged;}
        
        public void write(IBytedeque c)
        {
            c.writeInt(nodeId);
            revisionId.write(c);
        }
        public String dump()
        {
            return super.dump() + " : nodeId="+Integer.toString(nodeId)+" revisionId="+revisionId.toString();
        }
    }

    public static class VaultNodeSave extends AuthMsg
    {
        int transId;
        int nodeId;
        Guid revisionId;
        private int size;
        byte[] nodeData;

        public VaultNodeSave(IBytestream c)
        {
            transId = c.readInt();
            nodeId = c.readInt();
            revisionId = new Guid(c);
            size = c.readInt();
            nodeData = c.readBytes(size);
        }
        public VaultNodeSave(){}
        public short type(){return AuthServer.kCli2Auth_VaultNodeSave;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(nodeId);
            revisionId.write(c);
            c.writeInt(nodeData.length);
            c.writeBytes(nodeData);
        }
        public String dump()
        {
            RawNode raw = new RawNode(nodeData);
            return super.dump() + " : nodeId="+Integer.toString(nodeId)+" revisionId="+revisionId.toString()+" node="+raw.dump();
        }
    }
    
    public static class VaultNodeFetched extends AuthMsg
    {
        int transId;
        int result;
        int sizeofbytes;
        byte[] data;
        public VaultNodeFetched(){}
        public VaultNodeFetched(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            sizeofbytes = c.readInt();
            data = c.readBytes(sizeofbytes);

            //if(true) //test
            //{
            //    m.msg("testing rawnode");
            //    RawNode node = new RawNode(shared.ByteArrayBytestream.createFromByteArray(data));
            //}
        }
        public short type(){return AuthServer.kAuth2Cli_VaultNodeFetched;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(data.length);
            c.writeBytes(data);
        }
        public String dump()
        {
            RawNode raw = new RawNode(data);
            return super.dump() + " : " + raw.dump();
        }
    }

    public static class VaultNodeFetch extends AuthMsg
    {
        int transId;
        int nodeId;
        public VaultNodeFetch(){}
        public VaultNodeFetch(IBytestream c)
        {
            transId = c.readInt();
            nodeId = c.readInt();
        }
        public short type(){return AuthServer.kCli2Auth_VaultNodeFetch;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(nodeId);
        }
        public String dump()
        {
            return super.dump() + " : nodeIdx=" + Integer.toString(nodeId);
        }
    }

    public static class VaultNodeRefsFetched extends AuthMsg
    {
        public int transId;
        public int result;
        private int numrefs;
        public ArrayList<Node.Ref> refs;

        public VaultNodeRefsFetched()
        {
            refs = new ArrayList<Node.Ref>();
        }
        public VaultNodeRefsFetched(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            numrefs = c.readInt();
            refs = new ArrayList<Node.Ref>();
            for(int i=0;i<numrefs;i++)
            {
                refs.add(new Node.Ref(c));
            }
        }
        public short type(){return AuthServer.kAuth2Cli_VaultNodeRefsFetched;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(refs.size());
            for(Node.Ref ref: refs)
            {
                ref.write(c);
            }
        }
        public String dump()
        {
            StringBuilder r = new StringBuilder();
            r.append(super.dump() + " : ");
            for(Node.Ref ref: refs)
            {
                r.append("\n    "+ref.dump());
            }
            return r.toString();
        }
    }

    public static class VaultFetchNodeRefs extends AuthMsg
    {
        int transId;
        int nodeId;

        public VaultFetchNodeRefs(IBytestream c)
        {
            transId = c.readInt();
            nodeId = c.readInt();
        }
        public VaultFetchNodeRefs(){}
        public short type(){return AuthServer.kCli2Auth_VaultFetchNodeRefs;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(nodeId);
        }
        public String dump()
        {
            return super.dump() + " : nodeId=" + Integer.toString(nodeId);
        }
    }

    public static class PlayerCreateReply extends AuthMsg
    {
        int transId;
        int result;
        int playerId;
        int explorer;
        Str playerName; //Utf16.Sized16 playerName;
        Str avatarModel; //Utf16.Sized16 avatarModel;

        public PlayerCreateReply(){}
        public PlayerCreateReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            playerId = c.readInt();
            explorer = c.readInt();
            playerName = Str.readAsUtf16Sized16(c);
            avatarModel = Str.readAsUtf16Sized16(c);
        }

        public short type(){return AuthServer.kAuth2Cli_PlayerCreateReply;}
        public Integer transid(){return transId;}

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(playerId);
            c.writeInt(explorer);
            playerName.writeAsUtf16Sized16(c);//playerName.write(c);
            //playerName.writeAsUtf16Sized16(c);//avatarModel.write(c); //bug remembered for posterity^^
            avatarModel.writeAsUtf16Sized16(c);
        }
    }

    public static class PlayerCreateRequest extends AuthMsg
    {
        int transId;
        //Utf16.Sized16 playerName;
        //Utf16.Sized16 avatarShape;
        //Utf16.Sized16 friendInvite;
        Str playerName;
        Str avatarShape;
        Str friendInvite;

        public PlayerCreateRequest(IBytestream c)
        {
            transId = c.readInt();
            //playerName = new Utf16.Sized16(c);
            //avatarShape = new Utf16.Sized16(c);
            //friendInvite = new Utf16.Sized16(c);
            playerName = Str.readAsUtf16Sized16(c);
            avatarShape = Str.readAsUtf16Sized16(c);
            friendInvite = Str.readAsUtf16Sized16(c);
        }
        public PlayerCreateRequest(){}
        public short type(){return AuthServer.kCli2Auth_PlayerCreateRequest;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            //playerName.write(c);
            //avatarShape.write(c);
            //friendInvite.write(c);
            playerName.writeAsUtf16Sized16(c);
            avatarShape.writeAsUtf16Sized16(c);
            friendInvite.writeAsUtf16Sized16(c);
        }
    }

    public static class AcctSetPlayerReply extends AuthMsg
    {
        int transId;
        int result;

        public AcctSetPlayerReply(){}
        public AcctSetPlayerReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
        }

        public short type(){return AuthServer.kAuth2Cli_AcctSetPlayerReply;}
        public Integer transid(){return transId;}

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
        }
    }

    public static class AcctSetPlayerRequest extends AuthMsg
    {
        int transId;
        int playerId;

        public AcctSetPlayerRequest(IBytestream c)
        {
            transId = c.readInt();
            playerId = c.readInt();
        }
        public AcctSetPlayerRequest(){}
        public short type(){return AuthServer.kCli2Auth_AcctSetPlayerRequest;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(playerId);
        }
    }

    public static class FileDownloadChunkAck extends AuthMsg
    {
        int transId;

        public short type(){return AuthServer.kCli2Auth_FileDownloadChunkAck;}
        public Integer transid(){return transId;}

        public FileDownloadChunkAck(){}

        public FileDownloadChunkAck(IBytestream c)
        {
            transId = c.readInt();
        }

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
        }
    }

    public static class FileDownloadChunk extends AuthMsg
    {
        int transId;
        int result;
        int filesize;
        int chunkOffset;
        int buffersize;
        byte[] buffer;

        public short type(){return AuthServer.kAuth2Cli_FileDownloadChunk;}
        public Integer transid(){return transId;}

        public FileDownloadChunk(){}

        public FileDownloadChunk(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            filesize = c.readInt();
            chunkOffset = c.readInt();
            buffersize = c.readInt();
            buffer = c.readBytes(buffersize);
        }

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(filesize);
            c.writeInt(chunkOffset);
            c.writeInt(buffer.length);
            c.writeBytes(buffer);
        }
    }

    public static class FileDownloadRequest extends AuthMsg
    {
        int transId;
        Str filename;

        public short type(){return AuthServer.kCli2Auth_FileDownloadRequest;}
        public Integer transid(){return transId;}

        public FileDownloadRequest(){}

        public FileDownloadRequest(IBytestream c)
        {
            transId = c.readInt();
            filename = Str.readAsUtf16Sized16(c);
        }

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            filename.writeAsUtf16Sized16(c);
        }
    }

    public static class AcctPlayerInfo extends AuthMsg
    {
        int transId;
        int playerId; //428706
        Str playerName; //Alcug
        Str avatarModel; //male
        int explorer; //1 (as opposed to visitor? or staff?)

        public AcctPlayerInfo(IBytestream c)
        {
            transId = c.readInt();
            playerId = c.readInt();
            playerName = Str.readAsUtf16Sized16(c);
            avatarModel = Str.readAsUtf16Sized16(c);
            explorer = c.readInt();
        }

        public AcctPlayerInfo(){}

        public short type(){return AuthServer.kAuth2Cli_AcctPlayerInfo;}
        public Integer transid(){return transId;}

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(playerId);
            playerName.writeAsUtf16Sized16(c);
            avatarModel.writeAsUtf16Sized16(c);
            c.writeInt(explorer);
        }
    }
    public static class ServerAddr extends AuthMsg
    {
        int address; //this looks like a ipv4 address.  e.g. 0x0AC312B0(moulagain) 0x0AD75F1F(magiquest). 10.x.x.x addresses are reserved for private use, so perhaps these are from Cyan's intranet or amazon's cloud thingy.
        byte[] guid;

        public ServerAddr(IBytestream c)
        {
            address = c.readInt();
            guid = c.readBytes(16);
        }
        public ServerAddr(){}
        public short type(){return AuthServer.kAuth2Cli_ServerAddr;}
        public void write(IBytedeque c)
        {
            c.writeInt(address);
            c.writeBytes(guid);
        }
    }
    public static class FileListReply extends AuthMsg
    {
        int transId;
        int result;
        private int charcount; //should not be directly set, as it is not used anyway.
        //short[] data;
        SecureDownloadManifest manifest;

        public short type(){ return AuthServer.kAuth2Cli_FileListReply; }
        public Integer transid(){return transId;}

        public FileListReply(){}

        public FileListReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            charcount = c.readInt();
            //data = c.readShorts(charcount);
            manifest = new SecureDownloadManifest(c);
        }

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            //c.writeInt(charcount);
            //c.writeShorts(data);

            Bytedeque d = new Bytedeque(Format.moul);
            manifest.write(d);
            byte[] manifestbs = d.getAllBytes();

            c.writeInt(manifestbs.length/2); //charcount
            c.writeBytes(manifestbs);

        }
    }

    public static class FileListRequest extends AuthMsg
    {
        int transId;
        Str dir; //"Python" //was Utf16.Sized16
        Str extension; //"pak"

        public FileListRequest(){}

        public short type(){return AuthServer.kCli2Auth_FileListRequest;}
        public Integer transid(){return transId;}

        public FileListRequest(IBytestream c)
        {
            transId = c.readInt();
            dir = Str.readAsUtf16Sized16(c);
            extension = Str.readAsUtf16Sized16(c);
        }

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            dir.writeAsUtf16Sized16(c);
            extension.writeAsUtf16Sized16(c);
        }
    }

    public static class AcctLoginReply extends AuthMsg
    {
        int transId;
        int result;
        byte[] accountUUID; //byte[16]
        int accountFlags; //8
        int billingType; //1  (1=full?)
        //byte[] encryptionKey; //byte[16], notthedroids key
        int[] encryptionKey; //int[4]

        public AcctLoginReply(){}

        public AcctLoginReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            accountUUID = c.readBytes(16);
            accountFlags = c.readInt();
            billingType = c.readInt();
            encryptionKey = c.readInts(4);
        }

        public short type(){ return AuthServer.kAuth2Cli_AcctLoginReply; }
        public Integer transid(){return transId;}

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeBytes(accountUUID);
            c.writeInt(accountFlags);
            c.writeInt(billingType);
            c.writeInts(encryptionKey);
        }
    }

    public static class AcctLoginRequest extends AuthMsg
    {
        int transId;
        int clientchallenge;
        Str accountName; //simply the login name. //was Utf16.Sized16
        byte[] passwordHash; //20 bytes, 160 bits, so probably SHA1.
        Str authToken; //""
        Str OS; //e.g. "win"

        public AcctLoginRequest(IBytestream c)
        {
            transId = c.readInt();
            clientchallenge = c.readInt(); //Always 0? Well, we shouldn't need to care, because rc4 is chained.
            accountName = Str.readAsUtf16Sized16(c);
            passwordHash = c.readBytes(20);
            authToken = Str.readAsUtf16Sized16(c); //empty?
            OS = Str.readAsUtf16Sized16(c);
        }

        public AcctLoginRequest(){}

        public short type(){return AuthServer.kCli2Auth_AcctLoginRequest;}
        public Integer transid(){return transId;}

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(clientchallenge);
            accountName.writeAsUtf16Sized16(c);
            if(passwordHash.length!=20) m.throwUncaughtException("unexpected");
            c.writeBytes(passwordHash);
            authToken.writeAsUtf16Sized16(c);
            OS.writeAsUtf16Sized16(c);
        }

        /*public byte[] getProperPasswordHash(String username)
        {
            if(!isUsernameEmailForm(username))
            {
                //fix how Cyan produces it as b3 b2 b1 b0 b7 b6 b5 b4, etc.
                byte[] r = new byte[20];
                for(int i=0;i<5;i++)
                {
                    for(int j=0;j<4;j++)
                    {
                        r[i*4+j] = passwordHash[i*4+(3-j)];
                    }
                }
                return r;
            }
            else
            {
                return passwordHash;
            }
        }*/
        public static byte[] getStoredHash(String username, String password)
        {
            if (isUsernameEmailForm(username))
            {
                byte[] pwbs = b.Utf16ToBytes(password);
                byte[] unbs = b.Utf16ToBytes(username.toLowerCase());
                byte[] both = new byte[pwbs.length+unbs.length];
                b.CopyBytes(pwbs, both, 0);
                b.CopyBytes(unbs, both, pwbs.length);
                both[pwbs.length-1] = 0;
                both[pwbs.length-2] = 0;
                both[both.length-1] = 0;
                both[both.length-2] = 0;
                jonelo.jacksum.adapt.gnu.crypto.hash.Sha0 sha0 = new jonelo.jacksum.adapt.gnu.crypto.hash.Sha0();
                sha0.update(both, 0, both.length);
                return sha0.digest();
            }
            else
            {
                return shared.CryptHashes.GetHash(b.StringToBytes(password), CryptHashes.Hashtype.sha1);
            }
        }
        
        public boolean checkPassword(byte[] storedhash, int serverchallenge)
        {
            return b.isEqual(passwordHash, getTransmittedHash(accountName.toString(), storedhash, serverchallenge, clientchallenge));
        }

        /*private void setIntHash(byte[] properhash)
        {
            byte[] r = new byte[20];
            for(int i=0;i<5;i++)
            {
                for(int j=0;j<4;j++)
                {
                    r[i*4+j] = properhash[i*4+(3-j)];
                }
            }
            this.passwordHash = r;
        }*/
        public static boolean isUsernameEmailForm(String username)
        {
            return username.matches(".+\\@.+\\..+"); //x@x.x, correctly allowing weird things like a.b@c@b.a@d.
        }
        public static byte[] getTransmittedHash(String username, byte[] storedhash, int serverchallenge, int clientchallenge)
        {
            if (isUsernameEmailForm(username))
            {
                byte[] all = new byte[4+4+storedhash.length];
                b.loadInt32IntoBytes(clientchallenge, all, 0);
                b.loadInt32IntoBytes(serverchallenge, all, 4);
                b.CopyBytes(storedhash, all, 8);
                jonelo.jacksum.adapt.gnu.crypto.hash.Sha0 sha0 = new jonelo.jacksum.adapt.gnu.crypto.hash.Sha0();
                sha0.update(all, 0, all.length);
                return sha0.digest();
            }
            else
            {
                //fix how Cyan produces it as b3 b2 b1 b0 b7 b6 b5 b4, etc.
                byte[] r = new byte[20];
                for(int i=0;i<5;i++)
                {
                    for(int j=0;j<4;j++)
                    {
                       r[i*4+j] = storedhash[i*4+(3-j)];
                    }
                }
                return r;
            }
        }
        public void setPassword(String username, String password, int serverchallenge, int clientchallenge)
        {
            //type=1 when username is simple, like on Talcum, but when long one like email address on Moulagain, it uses the other form(type=2), and it's unreversed, bizarrely.
            //after testing, I find that if it looks like x@x.x, then it uses the 2nd type. Other forms don't, including @x.x, x@x., and x@.x
            /*boolean isEmailForm = isUsernameEmailForm(username);
            //isEmailForm = true;
            if(!isEmailForm)
            {
                byte[] pwbytes = b.StringToBytes(password);
                byte[] hash = shared.CryptHashes.GetHash(pwbytes, CryptHashes.Hashtype.sha1);
                
                setIntHash(hash);
            }
            else
            {
                byte[] pwbs = b.Utf16ToBytes(password); // +(char)0);
                byte[] unbs = b.Utf16ToBytes(username.toLowerCase()); //+(char)0);
                byte[] both = new byte[pwbs.length+unbs.length];
                b.CopyBytes(pwbs, both, 0);
                b.CopyBytes(unbs, both, pwbs.length);
                both[pwbs.length-1] = 0;
                both[pwbs.length-2] = 0;
                both[both.length-1] = 0;
                both[both.length-2] = 0;
                jonelo.jacksum.adapt.gnu.crypto.hash.Sha0 sha0 = new jonelo.jacksum.adapt.gnu.crypto.hash.Sha0();
                sha0.update(both, 0, both.length);
                byte[] UnAndPwHash = sha0.digest();

                byte[] all = new byte[4+4+UnAndPwHash.length];
                b.loadInt32IntoBytes(clientchallenge, all, 0);
                b.loadInt32IntoBytes(serverchallenge, all, 4);
                b.CopyBytes(UnAndPwHash, all, 8);
                sha0.update(all, 0, all.length);
                byte[] hash = sha0.digest();

                this.passwordHash = hash;
            }

            //shared.CryptHashes.GetHash(null, CryptHashes.Hashtype.md5)
            */
            this.passwordHash = getTransmittedHash(username, getStoredHash(username, password), serverchallenge, clientchallenge);
        }

    }

    public static class PingRequest extends AuthMsg
    {
        int timestamp;
        int transId;
        private int buffersize;
        byte[] buffer;

        public PingRequest(IBytestream c)
        {
            timestamp = c.readInt();
            transId = c.readInt();
            buffersize = c.readInt();
            buffer = c.readBytes(buffersize);
        }
        public short type(){ return AuthServer.kCli2Auth_PingRequest; }
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(timestamp);
            c.writeInt(transId);
            c.writeInt(buffer.length);
            c.writeBytes(buffer);
        }
    }

    public static class PingReply extends AuthMsg
    {
        int timestamp;
        int transId;
        int buffersize;
        byte[] buffer;

        public PingReply(){}
        public PingReply(IBytestream c)
        {
            timestamp = c.readInt();
            transId = c.readInt();
            buffersize = c.readInt();
            buffer = c.readBytes(buffersize);
        }

        public short type(){ return AuthServer.kAuth2Cli_PingReply; }
        public Integer transid(){return transId;}

        public void write(IBytedeque c)
        {
            c.writeInt(timestamp);
            c.writeInt(transId);
            c.writeInt(buffer.length);
            c.writeBytes(buffer);
        }

    }

    public static class ClientRegisterReply extends AuthMsg
    {
        int serverchallenge;

        public ClientRegisterReply(){}

        public ClientRegisterReply(IBytestream c)
        {
            serverchallenge = c.readInt();
        }

        public short type(){ return AuthServer.kAuth2Cli_ClientRegisterReply; }

        public void write(IBytedeque c)
        {
            c.writeInt(serverchallenge);
        }
    }

    public static class ClientRegisterRequest extends AuthMsg
    {
        int buildId;

        public ClientRegisterRequest(){}

        public ClientRegisterRequest(IBytestream c)
        {
            buildId = c.readInt();
        }
        public short type(){return AuthServer.kCli2Auth_ClientRegisterRequest;}

        public void write(IBytedeque c)
        {
            c.writeInt(buildId);
        }
    }

    public static abstract class AuthMsg extends Server.ServerMsg
    {
        public abstract short type();

        public byte[] GetMsgBytes()
        {
            IBytedeque c = new Bytedeque(Format.moul);
            c.writeShort(this.type());
            this.write(c);
            byte[] data = c.getAllBytes();
            return data;
        }
    }

    /*static void SendMsg(Sock sock, AuthMsg msg) //hack for old MoulagainClient, cause I'm lazy :P
    {
        ConnectionState hack = ConnectionState.dummy();
        hack.sock = sock.sock;
        hack.out = sock.out;
        SendMsg(hack,msg);
    }*/

    static void SendMsg(ConnectionState cs, AuthMsg msg)
    {
        //if(ConnectServer.allatonce)
        //{
            Bytedeque c = new uru.Bytedeque(Format.moul);
            c.writeShort(msg.type());
            msg.write(c);
            byte[] data = c.getAllBytes();

            //sock.out.writeBytes(data);
            //sock.out.flush();
            cs.sendMsgBytes(data);
        //}
        //else
        //{
        //    m.throwUncaughtException("deprecated");
        //    //sock.out.writeShort(msg.type());
        //    //msg.write(sock.out);
        //    //sock.out.flush();
        //}
    }

}
