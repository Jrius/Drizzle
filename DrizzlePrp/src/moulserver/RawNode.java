/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Str;
import prpobjects.Guid;
import moulserver.Node.*;
import moulserver.Node.NodeType.*;
import shared.*;
import java.util.Vector;
import java.util.ArrayList;

public class RawNode
{
    public static final int kNodeIdx = 0;
    public static final int kCreateTime = 1;
    public static final int kModifyTime = 2;
    public static final int kCreateAgeName = 3;
    public static final int kCreateAgeUuid = 4;
    public static final int kCreatorUuid = 5;
    public static final int kCreatorIdx = 6;
    public static final int kNodeType = 7;
    public static final int kInt32_1 = 8;
    public static final int kInt32_2 = 9;
    public static final int kInt32_3 = 10;
    public static final int kInt32_4 = 11;
    public static final int kUint32_1 = 12;
    public static final int kUint32_2 = 13;
    public static final int kUint32_3 = 14;
    public static final int kUint32_4 = 15;
    public static final int kUuid_1 = 16;
    public static final int kUuid_2 = 17;
    public static final int kUuid_3 = 18;
    public static final int kUuid_4 = 19;
    public static final int kString64_1 = 20;
    public static final int kString64_2 = 21;
    public static final int kString64_3 = 22;
    public static final int kString64_4 = 23;
    public static final int kString64_5 = 24;
    public static final int kString64_6 = 25;
    public static final int kIString64_1 = 26;
    public static final int kIString64_2 = 27;
    public static final int kText_1 = 28;
    public static final int kText_2 = 29;
    public static final int kBlob_1 = 30;
    public static final int kBlob_2 = 31;
    public static final int kNumFields = 32;


    long fields;
    Integer nodeIdx; //int nodeIdx;
    Integer createTime; //int createTime; //seconds since 1970.
    Integer modifyTime; //int modifyTime; //seconds since 1970.
    Str createAgeName; //can be null. Utf16.SizedAndNT createAgeName;
    Guid createAgeUuid; //can be null
    Guid creatorUuid;
    Integer creatorIdx; //int creatorIdx;
    Integer nodeType; //int nodeType;
    Integer int32_1,int32_2,int32_3,int32_4;
    Integer uint32_1,uint32_2,uint32_3,uint32_4;
    Guid uuid_1,uuid_2,uuid_3,uuid_4;
    Str string64_1,string64_2,string64_3,string64_4,string64_5,string64_6;
    Str iString64_1,iString64_2;
    Str text_1b,text_2b;
    Blob blob_1b,blob_2b;

    public RawNode(int type)
    {
        nodeIdx = Manager.manager.database.getNextIdx();
        nodeType = type;
        createTime = 0; //createTime is fine as 0.
        modifyTime = 0; //modifyTime is fine as 0.
        //createAgeName can be null.
        //createAgeUuid can be null.
        creatorUuid = Guid.none2();
        creatorIdx = 0; //creatorIdx is fine as 0.
        //nodeType should be set
    }
    private RawNode(){}
    public static RawNode createEmpty()
    {
        return new RawNode();
    }

    public RawNode(Results r)
    {
        fields = r.getLong(1);

        for(int i=0;i<kNumFields;i++)
        {
            if((fields&(1<<i))!=0) //if bit is set
            {
                switch(i)
                {
                    case kNodeIdx:
                        nodeIdx = r.getInt(2);
                        break;
                    case kCreateTime:
                        createTime = r.getInt(3);
                        break;
                    case kModifyTime:
                        modifyTime = r.getInt(4);
                        break;
                    case kCreateAgeName:
                        createAgeName = new Str(r.getString(5));
                        break;
                    case kCreateAgeUuid:
                        createAgeUuid = new Guid(r.getBytes(6));
                        break;
                    case kCreatorUuid:
                        creatorUuid = new Guid(r.getBytes(7));
                        break;
                    case kCreatorIdx:
                        creatorIdx = r.getInt(8);
                        break;
                    case kNodeType:
                        nodeType = r.getInt(9);
                        break;
                    case kInt32_1:
                        int32_1 = r.getInt(10);
                        break;
                    case kInt32_2:
                        int32_2 = r.getInt(11);
                        break;
                    case kInt32_3:
                        int32_3 = r.getInt(12);
                        break;
                    case kInt32_4:
                        int32_4 = r.getInt(13);
                        break;
                    case kUint32_1:
                        uint32_1 = r.getInt(14);
                        break;
                    case kUint32_2:
                        uint32_2 = r.getInt(15);
                        break;
                    case kUint32_3:
                        uint32_3 = r.getInt(16);
                        break;
                    case kUint32_4:
                        uint32_4 = r.getInt(17);
                        break;
                    case kUuid_1:
                        uuid_1 = new Guid(r.getBytes(18));
                        break;
                    case kUuid_2:
                        uuid_2 = new Guid(r.getBytes(19));
                        break;
                    case kUuid_3:
                        uuid_3 = new Guid(r.getBytes(20));
                        break;
                    case kUuid_4:
                        uuid_4 = new Guid(r.getBytes(21));
                        break;
                    case kString64_1:
                        string64_1 = new Str(r.getString(22));
                        break;
                    case kString64_2:
                        string64_2 = new Str(r.getString(23));
                        break;
                    case kString64_3:
                        string64_3 = new Str(r.getString(24));
                        break;
                    case kString64_4:
                        string64_4 = new Str(r.getString(25));
                        break;
                    case kString64_5:
                        string64_5 = new Str(r.getString(26));
                        break;
                    case kString64_6:
                        string64_6 = new Str(r.getString(27));
                        break;
                    case kIString64_1:
                        iString64_1 = new Str(r.getString(28));
                        break;
                    case kIString64_2:
                        iString64_2 = new Str(r.getString(29));
                        break;
                    case kText_1:
                        text_1b = new Str(r.getString(30));
                        break;
                    case kText_2:
                        text_2b = new Str(r.getString(31));
                        break;
                    case kBlob_1:
                        blob_1b = new Blob(r.getBytes(32));
                        break;
                    case kBlob_2:
                        blob_2b = new Blob(r.getBytes(33));
                        break;
                    default:
                        throw new shared.uncaughtexception("Unhandled node flag");
                }
            }
        }
    }
    public RawNode(byte[] rawdata)
    {
        this(shared.ByteArrayBytestream.createFromByteArray(rawdata));
    }
    public RawNode(IBytestream c)
    {
        fields = c.readLong();
        //int idx;

        for(int i=0;i<kNumFields;i++)
        {
            if((fields&(1<<i))!=0) //if bit is set
            {
                switch(i)
                {
                    case kNodeIdx:
                        nodeIdx = c.readInt();
                        break;
                    case kCreateTime:
                        createTime = c.readInt();
                        break;
                    case kModifyTime:
                        modifyTime = c.readInt();
                        break;
                    case kCreateAgeName:
                        createAgeName = Str.readAsUtf16SizedAndNT(c);//createAgeName = new Utf16.SizedAndNT(c);
                        break;
                    case kCreateAgeUuid:
                        createAgeUuid = new Guid(c);
                        break;
                    case kCreatorUuid:
                        creatorUuid = new Guid(c);
                        break;
                    case kCreatorIdx:
                        creatorIdx = c.readInt();
                        break;
                    case kNodeType:
                        nodeType = c.readInt();
                        break;
                    case kInt32_1:
                        int32_1 = c.readInt();
                        break;
                    case kInt32_2:
                        int32_2 = c.readInt();
                        break;
                    case kInt32_3:
                        int32_3 = c.readInt();
                        break;
                    case kInt32_4:
                        int32_4 = c.readInt();
                        break;
                    case kUint32_1:
                        uint32_1 = c.readInt();
                        break;
                    case kUint32_2:
                        uint32_2 = c.readInt();
                        break;
                    case kUint32_3:
                        uint32_3 = c.readInt();
                        break;
                    case kUint32_4:
                        uint32_4 = c.readInt();
                        break;
                    case kUuid_1:
                        uuid_1 = new Guid(c);
                        break;
                    case kUuid_2:
                        uuid_2 = new Guid(c);
                        break;
                    case kUuid_3:
                        uuid_3 = new Guid(c);
                        break;
                    case kUuid_4:
                        uuid_4 = new Guid(c);
                        break;
                    case kString64_1:
                        string64_1 = Str.readAsUtf16SizedAndNT(c);
                        break;
                    case kString64_2:
                        string64_2 = Str.readAsUtf16SizedAndNT(c);
                        break;
                    case kString64_3:
                        string64_3 = Str.readAsUtf16SizedAndNT(c);
                        break;
                    case kString64_4:
                        string64_4 = Str.readAsUtf16SizedAndNT(c);
                        break;
                    case kString64_5:
                        string64_5 = Str.readAsUtf16SizedAndNT(c);
                        break;
                    case kString64_6:
                        string64_6 = Str.readAsUtf16SizedAndNT(c);
                        break;
                    case kIString64_1:
                        iString64_1 = Str.readAsUtf16SizedAndNT(c);
                        break;
                    case kIString64_2:
                        iString64_2 = Str.readAsUtf16SizedAndNT(c);
                        break;
                    case kText_1:
                        text_1b = Str.readAsUtf16SizedAndNT(c);
                        break;
                    case kText_2:
                        text_2b = Str.readAsUtf16SizedAndNT(c);
                        break;
                    case kBlob_1:
                        blob_1b = new Blob(c);
                        break;
                    case kBlob_2:
                        blob_2b = new Blob(c);
                        break;
                    default:
                        throw new shared.uncaughtexception("Unhandled node flag");
                }
            }
        }
    }
    public int[] makeSqlQuery()
    {
        StringBuilder r = new StringBuilder();
        ArrayList<Object> params = new ArrayList();
        boolean first = true;

        r.append("SELECT * FROM vault WHERE ");
        for(int i=0;i<kNumFields;i++)
        {
            if((fields&(1<<i))!=0) //if bit is set
            {
                if(!first) r.append(" AND ");
                switch(i)
                {
                    case kNodeIdx:
                        r.append(Database.idx);
                        params.add(nodeIdx);
                        break;
                    case kCreateTime:
                        r.append(Database.createTime);
                        params.add(createTime);
                        break;
                    case kModifyTime:
                        r.append(Database.modifyTime);
                        params.add(modifyTime);
                        break;
                    case kCreateAgeName:
                        r.append(Database.createAgeName);
                        params.add(createAgeName);
                        break;
                    case kCreateAgeUuid:
                        r.append(Database.createAgeUuid);
                        params.add(createAgeUuid);
                        break;
                    case kCreatorUuid:
                        r.append(Database.creatorUuid);
                        params.add(creatorUuid);
                        break;
                    case kCreatorIdx:
                        r.append(Database.creatorIdx);
                        params.add(creatorIdx);
                        break;
                    case kNodeType:
                        r.append(Database.type);
                        params.add(nodeType);
                        break;
                    case kInt32_1:
                        r.append(Database.int_1);
                        params.add(int32_1);
                        break;
                    case kInt32_2:
                        r.append(Database.int_2);
                        params.add(int32_2);
                        break;
                    case kInt32_3:
                        r.append(Database.int_3);
                        params.add(int32_3);
                        break;
                    case kInt32_4:
                        r.append(Database.int_4);
                        params.add(int32_4);
                        break;
                    case kUint32_1:
                        r.append(Database.uint_1);
                        params.add(uint32_1);
                        break;
                    case kUint32_2:
                        r.append(Database.uint_2);
                        params.add(uint32_2);
                        break;
                    case kUint32_3:
                        r.append(Database.uint_3);
                        params.add(uint32_3);
                        break;
                    case kUint32_4:
                        r.append(Database.uint_4);
                        params.add(uint32_4);
                        break;
                    case kUuid_1:
                        r.append(Database.uuid_1);
                        params.add(uuid_1);
                        break;
                    case kUuid_2:
                        r.append(Database.uuid_2);
                        params.add(uuid_2);
                        break;
                    case kUuid_3:
                        r.append(Database.uuid_3);
                        params.add(uuid_3);
                        break;
                    case kUuid_4:
                        r.append(Database.uuid_4);
                        params.add(uuid_4);
                        break;
                    case kString64_1:
                        r.append(Database.str_1);
                        params.add(string64_1);
                        break;
                    case kString64_2:
                        r.append(Database.str_2);
                        params.add(string64_2);
                        break;
                    case kString64_3:
                        r.append(Database.str_3);
                        params.add(string64_3);
                        break;
                    case kString64_4:
                        r.append(Database.str_4);
                        params.add(string64_4);
                        break;
                    case kString64_5:
                        r.append(Database.str_5);
                        params.add(string64_5);
                        break;
                    case kString64_6:
                        r.append(Database.str_6);
                        params.add(string64_6);
                        break;
                    case kIString64_1:
                        r.append(Database.lstr_1);
                        params.add(iString64_1);
                        break;
                    case kIString64_2:
                        r.append(Database.lstr_2);
                        params.add(iString64_2);
                        break;
                    case kText_1:
                        r.append(Database.text_1);
                        params.add(text_1b);
                        break;
                    case kText_2:
                        r.append(Database.text_2);
                        params.add(text_2b);
                        break;
                    case kBlob_1:
                        r.append(Database.blob_1);
                        params.add(this.blob_1b);
                        break;
                    case kBlob_2:
                        r.append(Database.blob_2);
                        params.add(this.blob_2b);
                        break;
                    default:
                        throw new shared.uncaughtexception("Unhandled node flag");
                }
                r.append("=?");
                first = false;
            }
        }
        String sql = r.toString();
        Object[] params2 = params.toArray();
        Results results = Manager.manager.database.sqlquery(sql, params2);
        //return results;

        ArrayList<Integer> idxs = new ArrayList();
        while(results.next())
        {
            int idx = results.getInt(Database.idx);
            idxs.add(idx);
        }

        int[] idx2 = new int[idxs.size()];
        for(int i=0;i<idx2.length;i++)
        {
            idx2[i] = idxs.get(i);
        }
        return idx2;
    }
    public String toString()
    {
        return dump();
    }
    public String dump()
    {
        StringBuilder r = new StringBuilder();
        for(int i=0;i<kNumFields;i++)
        {
            if((fields&(1<<i))!=0) //if bit is set
            {
                String s;
                switch(i)
                {
                    case kNodeIdx:
                        s = "NodeIdx="+Integer.toString(nodeIdx);
                        break;
                    case kCreateTime:
                        s = "CreateTime="+Integer.toString(createTime);
                        break;
                    case kModifyTime:
                        s = "ModifyTime="+Integer.toString(modifyTime);
                        break;
                    case kCreateAgeName:
                        s = "CreateAgeName="+createAgeName.toString();
                        break;
                    case kCreateAgeUuid:
                        s = "CreateAgeUuid="+createAgeUuid.toString();
                        break;
                    case kCreatorUuid:
                        s = "CreatorUuid="+creatorUuid.toString();
                        break;
                    case kCreatorIdx:
                        s = "CreatorIdx="+Integer.toString(creatorIdx);
                        break;
                    case kNodeType:
                        s = "NodeType="+Integer.toString(nodeType);
                        break;
                    case kInt32_1:
                        s = "Int32_1="+int32_1.toString();
                        break;
                    case kInt32_2:
                        s = "Int32_2="+int32_2.toString();
                        break;
                    case kInt32_3:
                        s = "Int32_3="+int32_3.toString();
                        break;
                    case kInt32_4:
                        s = "Int32_4="+int32_4.toString();
                        break;
                    case kUint32_1:
                        s = "Uint32_1="+uint32_1.toString();
                        break;
                    case kUint32_2:
                        s = "Uint32_2="+uint32_2.toString();
                        break;
                    case kUint32_3:
                        s = "Uint32_3="+uint32_3.toString();
                        break;
                    case kUint32_4:
                        s = "Uint32_4="+uint32_4.toString();
                        break;
                    case kUuid_1:
                        s = "Uuid_1="+uuid_1.toString();
                        break;
                    case kUuid_2:
                        s = "Uuid_2="+uuid_2.toString();
                        break;
                    case kUuid_3:
                        s = "Uuid_3="+uuid_3.toString();
                        break;
                    case kUuid_4:
                        s = "Uuid_4="+uuid_4.toString();
                        break;
                    case kString64_1:
                        s = "String64_1="+string64_1.toString();
                        break;
                    case kString64_2:
                        s = "String64_2="+string64_2.toString();
                        break;
                    case kString64_3:
                        s = "String64_3="+string64_3.toString();
                        break;
                    case kString64_4:
                        s = "String64_4="+string64_4.toString();
                        break;
                    case kString64_5:
                        s = "String64_5="+string64_5.toString();
                        break;
                    case kString64_6:
                        s = "String64_6="+string64_6.toString();
                        break;
                    case kIString64_1:
                        s = "IString64_1="+iString64_1.toString();
                        break;
                    case kIString64_2:
                        s = "IString64_2="+iString64_2.toString();
                        break;
                    case kText_1:
                        s = "Text_1="+text_1b.toString();
                        break;
                    case kText_2:
                        s = "Text_2="+text_2b.toString();
                        break;
                    case kBlob_1:
                        if(nodeType==Node.NodeType.kNodeSDL|| nodeType==Node.NodeType.kNodeImage)
                        {
                            s = "Blob_1=[[binary data omitted]]";
                        }
                        else
                        {
                            s = "Blob_1="+blob_1b.toString();
                        }
                        break;
                    case kBlob_2:
                        s = "Blob_2="+blob_2b.toString();
                        break;
                    default:
                        throw new shared.uncaughtexception("Unhandled node flag");
                }
                int maxsize = 40000;
                if(s.length()>maxsize) s = s.substring(0, maxsize);
                s = s.replace("\0", "[[0]]");
                s = s.replace("\r\n", "[[rn]]");
                s = s.replace("\n", "[[n]]");
                r.append(s);
                r.append("  ");
            }
        }
        return r.toString();
    }
    public int numFields()
    {
        int r = 0;
        for(int i=0;i<kNumFields;i++)
        {
            if((fields&(1<<i))!=0) //if bit is set
            {
                r++;
            }
        }
        return r;
    }
    private void regenerateFieldsFlag()
    {
        long newfields = 0;
        for(int i=0;i<kNumFields;i++)
        {
            boolean has = false;
            switch(i)
            {
                case kNodeIdx:
                    if(nodeIdx!=null) has = true;
                    break;
                case kCreateTime:
                    if(createTime!=null) has = true;
                    break;
                case kModifyTime:
                    if(modifyTime!=null) has = true;
                    break;
                case kCreateAgeName:
                    if(createAgeName!=null) has = true;
                    break;
                case kCreateAgeUuid:
                    if(createAgeUuid!=null) has = true;
                    break;
                case kCreatorUuid:
                    if(creatorUuid!=null) has = true;
                    break;
                case kCreatorIdx:
                    if(creatorIdx!=null) has = true;
                    break;
                case kNodeType:
                    if(nodeType!=null) has = true;
                    break;
                case kInt32_1:
                    if(int32_1!=null) has = true;
                    break;
                case kInt32_2:
                    if(int32_2!=null) has = true;
                    break;
                case kInt32_3:
                    if(int32_3!=null) has = true;
                    break;
                case kInt32_4:
                    if(int32_4!=null) has = true;
                    break;
                case kUint32_1:
                    if(uint32_1!=null) has = true;
                    break;
                case kUint32_2:
                    if(uint32_2!=null) has = true;
                    break;
                case kUint32_3:
                    if(uint32_3!=null) has = true;
                    break;
                case kUint32_4:
                    if(uint32_4!=null) has = true;
                    break;
                case kUuid_1:
                    if(uuid_1!=null) has = true;
                    break;
                case kUuid_2:
                    if(uuid_2!=null) has = true;
                    break;
                case kUuid_3:
                    if(uuid_3!=null) has = true;
                    break;
                case kUuid_4:
                    if(uuid_4!=null) has = true;
                    break;
                case kString64_1:
                    if(string64_1!=null) has = true;
                    break;
                case kString64_2:
                    if(string64_2!=null) has = true;
                    break;
                case kString64_3:
                    if(string64_3!=null) has = true;
                    break;
                case kString64_4:
                    if(string64_4!=null) has = true;
                    break;
                case kString64_5:
                    if(string64_5!=null) has = true;
                    break;
                case kString64_6:
                    if(string64_6!=null) has = true;
                    break;
                case kIString64_1:
                    if(iString64_1!=null) has = true;
                    break;
                case kIString64_2:
                    if(iString64_2!=null) has = true;
                    break;
                case kText_1:
                    if(text_1b!=null) has = true;
                    break;
                case kText_2:
                    if(text_2b!=null) has = true;
                    break;
                case kBlob_1:
                    if(blob_1b!=null) has = true;
                    break;
                case kBlob_2:
                    if(blob_2b!=null) has = true;
                    break;
                default:
                    throw new shared.uncaughtexception("Unhandled node flag");
            }
            if(has) newfields |= (1L<<i);
        }
        fields = newfields;
    }
    public void insert()
    {
        regenerateFieldsFlag();
        insertOrUpdate(true, 0);
    }
    public void update(long newfields, Guid revision/*, ConnectionState cs*/) //newfields list the *only* fields that will be updated.  Old ones will be left alone.
    {
        regenerateFieldsFlag();
        insertOrUpdate(false, newfields);
        
        //notify others
        //AuthServer.VaultNodeChanged msg = new AuthServer.VaultNodeChanged();
        //msg.nodeId = this.nodeIdx;
        //msg.revisionId = revision;

        //WE NEED TO NOTIFY THE OTHERS!
        //Manager.manager.comm.triggerNodeChange(this.nodeIdx, Comm.CommItem.NodeChange(msg, cs));
        //Manager.manager.vaultlistener.sendMsgToNodeListeners(this.nodeIdx, Comm.CommItem.SendMessage(msg));
        Manager.manager.vaultlistener.SignalNodeChanged(this.nodeIdx, revision);
    }
    public void updateAll(Guid revision/*, ConnectionState cs*/)
    {
        regenerateFieldsFlag();
        long newfields = fields;
        update(newfields,revision/*,cs*/);
    }
    private void insertOrUpdate(boolean isInsert, long newfields)
    {
        int numfields = numFields();
        StringBuilder r = new StringBuilder();
        //Vector<String> f = new Vector<String>();
        Vector<Object> o = new Vector<Object>();

        if(isInsert)
            r.append("INSERT INTO vault(");
        else
            r.append("UPDATE vault SET ");

        if(isInsert)
        {
            r.append(Database.fields);
            o.add(fields);
        }
        else
        {
            r.append(Database.fields);
            o.add(newfields); //use this new merged number.
            r.append("=?");
        }

        //Manager.database.sqlupdate("INSERT INTO vault(nodeIdx,nodeType,iString64_1,uint32_1) VALUES(?,?,?,?)", n.nodeIdx,n.nodeType,n.iString64_1,n.uint32_1);
        //boolean first = true;
        for(int i=0;i<kNumFields;i++)
        {
            if((fields&(1<<i))!=0) //if bit is set
            {
                //if(first) first = false;
                //else r.append(',');
                if(isInsert)
                    r.append(','); //we've already got a field, so just put this in.
                else
                    r.append(',');

                switch(i)
                {
                    case kNodeIdx:
                        r.append(Database.idx);
                        o.add(nodeIdx);
                        break;
                    case kCreateTime:
                        r.append(Database.createTime);
                        o.add(createTime);
                        break;
                    case kModifyTime:
                        r.append(Database.modifyTime);
                        o.add(modifyTime);
                        break;
                    case kCreateAgeName:
                        r.append(Database.createAgeName);
                        o.add(createAgeName.toString());
                        break;
                    case kCreateAgeUuid:
                        r.append(Database.createAgeUuid);
                        o.add(createAgeUuid.toBytes());
                        break;
                    case kCreatorUuid:
                        r.append(Database.creatorUuid);
                        o.add(creatorUuid.toBytes());
                        break;
                    case kCreatorIdx:
                        r.append(Database.creatorIdx);
                        o.add(creatorIdx);
                        break;
                    case kNodeType:
                        r.append(Database.type);
                        o.add(nodeType);
                        break;
                    case kInt32_1:
                        r.append(Database.int_1);
                        o.add(int32_1);
                        break;
                    case kInt32_2:
                        r.append(Database.int_2);
                        o.add(int32_2);
                        break;
                    case kInt32_3:
                        r.append(Database.int_3);
                        o.add(int32_3);
                        break;
                    case kInt32_4:
                        r.append(Database.int_4);
                        o.add(int32_4);
                        break;
                    case kUint32_1:
                        r.append(Database.uint_1);
                        o.add(uint32_1);
                        break;
                    case kUint32_2:
                        r.append(Database.uint_2);
                        o.add(uint32_2);
                        break;
                    case kUint32_3:
                        r.append(Database.uint_3);
                        o.add(uint32_3);
                        break;
                    case kUint32_4:
                        r.append(Database.uint_4);
                        o.add(uint32_4);
                        break;
                    case kUuid_1:
                        r.append(Database.uuid_1);
                        o.add(uuid_1.toBytes());
                        break;
                    case kUuid_2:
                        r.append(Database.uuid_2);
                        o.add(uuid_2.toBytes());
                        break;
                    case kUuid_3:
                        r.append(Database.uuid_3);
                        o.add(uuid_3.toBytes());
                        break;
                    case kUuid_4:
                        r.append(Database.uuid_4);
                        o.add(uuid_4.toBytes());
                        break;
                    case kString64_1:
                        r.append(Database.str_1);
                        o.add(string64_1.toString());
                        break;
                    case kString64_2:
                        r.append(Database.str_2);
                        o.add(string64_2.toString());
                        break;
                    case kString64_3:
                        r.append(Database.str_3);
                        o.add(string64_3.toString());
                        break;
                    case kString64_4:
                        r.append(Database.str_4);
                        o.add(string64_4.toString());
                        break;
                    case kString64_5:
                        r.append(Database.str_5);
                        o.add(string64_5.toString());
                        break;
                    case kString64_6:
                        r.append(Database.str_6);
                        o.add(string64_6.toString());
                        break;
                    case kIString64_1:
                        r.append(Database.lstr_1);
                        o.add(iString64_1.toString());
                        break;
                    case kIString64_2:
                        r.append(Database.lstr_2);
                        o.add(iString64_2.toString());
                        break;
                    case kText_1:
                        r.append(Database.text_1);
                        o.add(text_1b.toString());
                        break;
                    case kText_2:
                        r.append(Database.text_2);
                        o.add(text_2b.toString());
                        break;
                    case kBlob_1:
                        r.append(Database.blob_1);
                        o.add(blob_1b.toBytes());
                        break;
                    case kBlob_2:
                        r.append(Database.blob_2);
                        o.add(blob_2b.toBytes());
                        break;
                    default:
                        throw new shared.uncaughtexception("Unhandled node flag");
                }

                if(isInsert)
                    {}
                else
                    r.append("=?");

            }
        }
        if(isInsert)
        {
            r.append(") VALUES(?");
            for(int i=0;i<numfields;i++)
            {
                //if(i!=0) r.append(',');
                r.append(','); //we've already got one, so just put this here.
                r.append('?');
            }
            r.append(") ;");
        }
        else
        {
            r.append(" WHERE "+Database.idx+"=? ;");
            o.add(this.nodeIdx);
        }
        String sql = r.toString();

        Manager.manager.database.sqlupdate(sql, o.toArray());
    }
    public void mergeFrom(RawNode n)
    {
        //this will merge the n into the current RawNode.
        //be careful where you call this, as the fields flags might not be set yet.
        //regenerateFieldsFlag();

        for(int i=0;i<kNumFields;i++)
        {
            if((n.fields&(1<<i))!=0) //if bit is set
            {
                switch(i)
                {
                    case kNodeIdx:
                        //woah, we shouldn't need to change the index!!
                        if(this.nodeIdx!=n.nodeIdx) m.throwUncaughtException("unexpected");
                        break;
                    case kCreateTime:
                        this.createTime = n.createTime;
                        break;
                    case kModifyTime:
                        this.modifyTime = n.modifyTime;
                        break;
                    case kCreateAgeName:
                        this.createAgeName = n.createAgeName;
                        break;
                    case kCreateAgeUuid:
                        this.createAgeUuid = n.createAgeUuid;
                        break;
                    case kCreatorUuid:
                        this.creatorUuid = n.creatorUuid;
                        break;
                    case kCreatorIdx:
                        this.creatorIdx = n.creatorIdx;
                        break;
                    case kNodeType:
                        //hmm... should this be permissable? Maybe.
                        if(this.nodeType!=n.nodeType)
                            m.throwUncaughtException("unexpected");
                        this.nodeType = n.nodeType;
                        break;
                    case kInt32_1:
                        this.int32_1 = n.int32_1;
                        break;
                    case kInt32_2:
                        this.int32_2 = n.int32_2;
                        break;
                    case kInt32_3:
                        this.int32_3 = n.int32_3;
                        break;
                    case kInt32_4:
                        this.int32_4 = n.int32_4;
                        break;
                    case kUint32_1:
                        this.uint32_1 = n.uint32_1;
                        break;
                    case kUint32_2:
                        this.uint32_2 = n.uint32_2;
                        break;
                    case kUint32_3:
                        this.uint32_3 = n.uint32_3;
                        break;
                    case kUint32_4:
                        this.uint32_4 = n.uint32_4;
                        break;
                    case kUuid_1:
                        this.uuid_1 = n.uuid_1;
                        break;
                    case kUuid_2:
                        this.uuid_2 = n.uuid_2;
                        break;
                    case kUuid_3:
                        this.uuid_3 = n.uuid_3;
                        break;
                    case kUuid_4:
                        this.uuid_4 = n.uuid_4;
                        break;
                    case kString64_1:
                        this.string64_1 = n.string64_1;
                        break;
                    case kString64_2:
                        this.string64_2 = n.string64_2;
                        break;
                    case kString64_3:
                        this.string64_3 = n.string64_3;
                        break;
                    case kString64_4:
                        this.string64_4 = n.string64_4;
                        break;
                    case kString64_5:
                        this.string64_5 = n.string64_5;
                        break;
                    case kString64_6:
                        this.string64_6 = n.string64_6;
                        break;
                    case kIString64_1:
                        this.iString64_1 = n.iString64_1;
                        break;
                    case kIString64_2:
                        this.iString64_2 = n.iString64_2;
                        break;
                    case kText_1:
                        this.text_1b = n.text_1b;
                        break;
                    case kText_2:
                        this.text_2b = n.text_2b;
                        break;
                    case kBlob_1:
                        this.blob_1b = n.blob_1b;
                        break;
                    case kBlob_2:
                        this.blob_2b = n.blob_2b;
                        break;
                    default:
                        throw new shared.uncaughtexception("Unhandled node flag");
                }
            }
        }
    }
    public Node getInterface()
    {
        switch(nodeType)
        {
            //case kNodeInvalid:
            //case kNodeVNodeMgrLow:
            //case kNodePlayer:
            //case kNodeAge:
            //case kNodeGameServer:
            //case kNodeAdmin:
            //case kNodeVaultServer:
            //case kNodeCCR:
            //case kNodeVNodeMgrHigh:
            //case kNodeFolder:
            //case kNodePlayerInfo:
            //case kNodeSystem:
            //case kNodeImage:
            //case kNodeTextNote:
            case NodeType.kNodeSDL:
                return new SDLNode(this);
            //case kNodeAgeLink:
            //case kNodeChronicle:
            //case kNodePlayerInfoList:
            //case kNodeUNUSED:
            //case kNodeMarker:
            //case kNodeAgeInfo:
            //case kNodeAgeInfoList:
            //case kNodeMarkerList:
            default:
                throw new shared.uncaughtexception("unhandled");

        }
    }

    public byte[] getBytes()
    {
        uru.Bytedeque c = new uru.Bytedeque(Format.moul);
        this.write(c);
        return c.getAllBytes();
    }

    public void write(IBytedeque c)
    {
        this.regenerateFieldsFlag();

        c.writeLong(fields);

        for(int i=0;i<kNumFields;i++)
        {
            if((fields&(1<<i))!=0) //if bit is set
            {
                switch(i)
                {
                    case kNodeIdx:
                        c.writeInt(nodeIdx);
                        break;
                    case kCreateTime:
                        c.writeInt(createTime);
                        break;
                    case kModifyTime:
                        c.writeInt(modifyTime);
                        break;
                    case kCreateAgeName:
                        createAgeName.writeAsUtf16SizedAndNT(c);
                        break;
                    case kCreateAgeUuid:
                        createAgeUuid.write(c);
                        break;
                    case kCreatorUuid:
                        creatorUuid.write(c);
                        break;
                    case kCreatorIdx:
                        c.writeInt(creatorIdx);
                        break;
                    case kNodeType:
                        c.writeInt(nodeType);
                        break;
                    case kInt32_1:
                        c.writeInt(int32_1);
                        break;
                    case kInt32_2:
                        c.writeInt(int32_2);
                        break;
                    case kInt32_3:
                        c.writeInt(int32_3);
                        break;
                    case kInt32_4:
                        c.writeInt(int32_4);
                        break;
                    case kUint32_1:
                        c.writeInt(uint32_1);
                        break;
                    case kUint32_2:
                        c.writeInt(uint32_2);
                        break;
                    case kUint32_3:
                        c.writeInt(uint32_3);
                        break;
                    case kUint32_4:
                        c.writeInt(uint32_4);
                        break;
                    case kUuid_1:
                        uuid_1.write(c);
                        break;
                    case kUuid_2:
                        uuid_2.write(c);
                        break;
                    case kUuid_3:
                        uuid_3.write(c);
                        break;
                    case kUuid_4:
                        uuid_4.write(c);
                        break;
                    case kString64_1:
                        string64_1.writeAsUtf16SizedAndNT(c);
                        break;
                    case kString64_2:
                        string64_2.writeAsUtf16SizedAndNT(c);
                        break;
                    case kString64_3:
                        string64_3.writeAsUtf16SizedAndNT(c);
                        break;
                    case kString64_4:
                        string64_4.writeAsUtf16SizedAndNT(c);
                        break;
                    case kString64_5:
                        string64_5.writeAsUtf16SizedAndNT(c);
                        break;
                    case kString64_6:
                        string64_6.writeAsUtf16SizedAndNT(c);
                        break;
                    case kIString64_1:
                        iString64_1.writeAsUtf16SizedAndNT(c);
                        break;
                    case kIString64_2:
                        iString64_2.writeAsUtf16SizedAndNT(c);
                        break;
                    case kText_1:
                        text_1b.writeAsUtf16SizedAndNT(c);
                        break;
                    case kText_2:
                        text_2b.writeAsUtf16SizedAndNT(c);
                        break;
                    case kBlob_1:
                        blob_1b.write(c);
                        break;
                    case kBlob_2:
                        blob_2b.write(c);
                        break;
                    default:
                        throw new shared.uncaughtexception("Unhandled node flag");
                }
            }
        }
    }

    public void setFieldsOnNewNode()
    {
        nodeIdx = Manager.manager.database.getNextIdx();
        createTime = Database.getCurrentTime();
        modifyTime = Database.getCurrentTime();

        creatorUuid = Guid.none2();
        creatorIdx = 0; //creatorIdx is fine as 0.

    }




}
