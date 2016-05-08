/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import prpobjects.Guid;
import shared.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Vector;

public class Database
{
    public static final String fields = "fields";
    public static final String idx = "idx";
    public static final String createTime = "createTime";
    public static final String modifyTime = "modifyTime";
    public static final String createAgeName = "createAgeName";
    public static final String createAgeUuid = "createAgeUuid";
    public static final String creatorUuid = "creatorUuid";
    public static final String creatorIdx = "creatorIdx";
    public static final String type = "type";
    public static final String int_1 = "int_1";
    public static final String int_2 = "int_2";
    public static final String int_3 = "int_3";
    public static final String int_4 = "int_4";
    public static final String uint_1 = "uint_1";
    public static final String uint_2 = "uint_2";
    public static final String uint_3 = "uint_3";
    public static final String uint_4 = "uint_4";
    public static final String uuid_1 = "uuid_1";
    public static final String uuid_2 = "uuid_2";
    public static final String uuid_3 = "uuid_3";
    public static final String uuid_4 = "uuid_4";
    public static final String str_1 = "str_1";
    public static final String str_2 = "str_2";
    public static final String str_3 = "str_3";
    public static final String str_4 = "str_4";
    public static final String str_5 = "str_5";
    public static final String str_6 = "str_6";
    public static final String lstr_1 = "lstr_1";
    public static final String lstr_2 = "lstr_2";
    public static final String text_1 = "text_1";
    public static final String text_2 = "text_2";
    public static final String blob_1 = "blob_1";
    public static final String blob_2 = "blob_2";

    private Connection conn;

    public void initialise(String dbfile)
    {
        try{
            Class.forName("org.h2.Driver");
            //String dbfile = manager.getDatabasefile();
            String username = "admin";
            String password = "";
            conn = DriverManager.getConnection("jdbc:h2:"+dbfile,username,password);
            setup();
        }catch(Exception e){
            throw new nested(e);
        }
    }

    public void sqlupdate(String sql, Object... parameters)
    {
        try{
            PreparedStatement stmt = _sql(sql,parameters);
            stmt.execute();
        }catch(Exception e){
            throw new nested(e);
        }
    }
    public Results sqlquery(String sql, Object... parameters)
    {
        try{
            PreparedStatement stmt = _sql(sql,parameters);
            ResultSet r = stmt.executeQuery();
            return new Results(r);
        }catch(Exception e){
            throw new nested(e);
        }
    }
    public PreparedStatement _sql(String sql, Object... parameters) throws SQLException
    {
        PreparedStatement stmt = conn.prepareStatement(sql);
        for(int i=1;i<=parameters.length;i++)
        {
            Object obj = parameters[i-1];
            if(obj==null)
            {
                int dummy=0;
            }
            Class klass = obj.getClass();
            if(klass.isPrimitive())
            {
                if(klass==int.class) stmt.setInt(i, (Integer)obj);
                else m.throwUncaughtException("unhandled");
            }
            else if(klass.isArray())
            {
                Class arrayklass = klass.getComponentType();
                if(arrayklass==byte.class) stmt.setBytes(i, (byte[])obj);
                else m.throwUncaughtException("unhandled");
            }
            else if(klass==Integer.class) stmt.setInt(i, (Integer)obj);
            else if(klass==String.class) stmt.setString(i, (String)obj);
            else if(klass==Byte.class) stmt.setByte(i, (Byte)obj);
            else if(klass==Long.class) stmt.setLong(i, (Long)obj);
            else m.throwUncaughtException("unhandled");
        }
        return stmt;
    }

    public void setup()
    {

        try{
            //The TIMESTAMP values should perhaps have a precision set. The java.sql.Timestamp page has good info on what to set it to.
            conn.prepareStatement("CREATE SEQUENCE IF NOT EXISTS idx_seq;").execute();
            conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS accounts ( "+
                "  accountname VARCHAR(255) NOT NULL, "+
                "  passwordhash BINARY(20) NOT NULL, "+
                "  guid BINARY(16) NOT NULL,"+
                "  flags INTEGER NOT NULL,"+
                "  billingtype INTEGER NOT NULL,"+
                "  talc_extra INTEGER,"+
                "  PRIMARY KEY(accountname) "+
                ")").execute();
//    long fields;
//    int nodeIdx;
//    int createTime; //seconds since 1970.
//    int modifyTime; //seconds since 1970.
//    Utf16.SizedAndNT createAgeName;
//    Guid createAgeUuid;
//    Guid creatorUuid;
//    int creatorIdx;
//    int nodeType;
//    Integer int32_1,int32_2,int32_3,int32_4;
//    Integer uint32_1,uint32_2,uint32_3,uint32_4;
//    Guid uuid_1,uuid_2,uuid_3,uuid_4;
//    Utf16.SizedAndNT string64_1,string64_2,string64_3,string64_4,string64_5,string64_6;
//    Utf16.SizedAndNT iString64_1,iString64_2;
//    Utf16.SizedAndNT text_1b,text_2b;
//    Blob blob_1b,blob_2b;
            conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS vault ("+
                    //header things
                    fields+" BIGINT NOT NULL,"+
                    idx+" INTEGER NOT NULL,"+ //idx
                    createTime+" INTEGER NOT NULL,"+ //crt_time
                    modifyTime+" INTEGER NOT NULL,"+ //mod_time
                    createAgeName+" VARCHAR(255),"+ //age_name
                    createAgeUuid+" BINARY(16),"+ //age_guid
                    creatorUuid+" BINARY(16) NOT NULL,"+
                    creatorIdx+" INTEGER NOT NULL,"+ //creator
                    type+" INTEGER NOT NULL,"+ //type
                    //variables
                    int_1+" INTEGER,"+ //int_1
                    int_2+" INTEGER,"+ //int_2
                    int_3+" INTEGER,"+ //int_3
                    int_4+" INTEGER,"+ //int_4
                    uint_1+" INTEGER,"+ //uint_1
                    uint_2+" INTEGER,"+ //uint_2
                    uint_3+" INTEGER,"+ //uint_3
                    uint_4+" INTEGER,"+ //uint_4
                    uuid_1+" BINARY(16),"+
                    uuid_2+" BINARY(16),"+
                    uuid_3+" BINARY(16),"+
                    uuid_4+" BINARY(16),"+
                    str_1+" VARCHAR(255),"+ //str_1
                    str_2+" VARCHAR(255),"+ //str_2
                    str_3+" VARCHAR(255),"+ //str_3
                    str_4+" VARCHAR(255),"+ //str_4
                    str_5+" VARCHAR(255),"+ //str_5
                    str_6+" VARCHAR(255),"+ //str_6
                    lstr_1+" VARCHAR(255),"+ //lstr_1
                    lstr_2+" VARCHAR(255),"+ //lstr_1
                    text_1+" VARCHAR(65535),"+ //text_1 //increased from 255 as sharper's journal needed it.
                    text_2+" VARCHAR(65535),"+ //text_2
                    blob_1+" BINARY(10000000)," + //blob_1 //10MB should be tons, and we shouldn't need actual blobs, because h2 only stores what we actually use. //DEFAULT(0x)
                    blob_2+" BINARY(10000000)," + //blob_2 //10MB should be tons, and we shouldn't need actual blobs, because h2 only stores what we actually use. //DEFAULT(0x)
                    //not present in Moul?
                    "  permissions INTEGER,"+
                    "  owner INTEGER,"+
                    "  grp INTEGER,"+
                    "  age_time TIMESTAMP,"+
                    "  talc_extra INTEGER,"+
                    //keys
                    "  PRIMARY KEY("+idx+")" +
                    //let's also CREATE INDEX... on type, owner, creator, age_name, age_guid, int_1, str_4, lstr_1, and lstr_2, as that's what Alcugs does.
                ")").execute();
//            conn.prepareStatement(
//                "CREATE TABLE IF NOT EXISTS vault ("+
//                "  idx INTEGER NOT NULL,"+ //was AUTO_INCREMENT
//                "  type INTEGER NOT NULL,"+
//                "  permissions INTEGER,"+
//                "  owner INTEGER,"+
//                "  grp INTEGER,"+
//                "  mod_time TIMESTAMP,"+
//                "  creator INTEGER,"+
//                "  crt_time TIMESTAMP,"+
//                "  age_time TIMESTAMP,"+
//                "  age_name VARCHAR(255),"+
//                "  age_guid BINARY(16),"+ //DEFAULT(0x00000000000000000000000000000000)
//                "  int_1 INTEGER,"+
//                "  int_2 INTEGER,"+
//                "  int_3 INTEGER,"+
//                "  int_4 INTEGER,"+
//                "  uint_1 INTEGER,"+
//                "  uint_2 INTEGER,"+
//                "  uint_3 INTEGER,"+
//                "  uint_4 INTEGER,"+
//                "  str_1 VARCHAR(255),"+
//                "  str_2 VARCHAR(255),"+
//                "  str_3 VARCHAR(255),"+
//                "  str_4 VARCHAR(255),"+
//                "  str_5 VARCHAR(255),"+
//                "  str_6 VARCHAR(255),"+
//                "  lstr_1 VARCHAR(255),"+
//                "  lstr_2 VARCHAR(255),"+
//                "  text_1 VARCHAR(255),"+
//                "  text_2 VARCHAR(255),"+
//                "  blob_1 BINARY(10000000)," + //10MB should be tons, and we shouldn't need actual blobs, because h2 only stores what we actually use. //DEFAULT(0x)
//                "  blob_2 BINARY(10000000)," + //10MB should be tons, and we shouldn't need actual blobs, because h2 only stores what we actually use. //DEFAULT(0x)
//                "  talc_extra INTEGER,"+
//                "  PRIMARY KEY(idx)" +
//                //let's also CREATE INDEX... on type, owner, creator, age_name, age_guid, int_1, str_4, lstr_1, and lstr_2, as that's what Alcugs does.
//                ")").execute();
            conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS ref_vault ( "+
                "  parent_idx INTEGER NOT NULL,"+
                "  child_idx INTEGER NOT NULL,"+
                "  owner_idx INTEGER NOT NULL,"+
                "  seen TINYINT NOT NULL,"+
                "  timestamp TIMESTAMP,"+
                "  talc_extra INTEGER,"+
                "  PRIMARY KEY(parent_idx,child_idx)"+
                ")").execute();

        }catch(Exception e){
            throw new nested(e);
        }
    }

    public void reset()
    {
        /*String[] sqls = new String[] {
            "DROP TABLE accounts",
        };

        for(String sql: sqls)
        {
            try{
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.execute();
            }catch(Exception e){
                throw new uncaughtnestedexception(e);
            }
        }*/
        try{
            //conn.prepareStatement("DROP TABLE IF EXISTS accounts").execute();
            //conn.prepareStatement("DROP TABLE IF EXISTS vault").execute();
            //conn.prepareStatement("DROP TABLE IF EXISTS ref_vault").execute();
            conn.prepareStatement("DROP ALL OBJECTS").execute();
        }catch(Exception e){
            throw new nested(e);
        }
        setup();
    }

    public String findString(String sql, Object... params)
    {
        //try{
            Results r = this.sqlquery(sql, params);
            if(!r.first()) m.throwUncaughtException("SQL query found not results!");
            String ret = r.getString(1);
            return ret;
        //}catch(Exception e){
        //    throw new shared.uncaughtnestedexception(e);
        //}
    }

    public int getNextIdx()
    {
        //try{
            Results r = this.sqlquery("SELECT NEXTVAL('idx_seq');");
            r.first();
            long num = r.getLong(1);
            int num2 = (int)num;
            return num2;
        //}catch(Exception e){
        //    throw new shared.uncaughtnestedexception(e);
        //}
    }

    public int getCurrentIdx() //handy for taking a look at how many we've already used up.
    {
        //try{
            Results r = this.sqlquery("SELECT CURRVAL('idx_seq');");
            r.first();
            long num = r.getLong(1);
            int num2 = (int)num;
            return num2;
        //}catch(Exception e){
        //    throw new shared.uncaughtnestedexception(e);
        //}
    }


    public void CreatePlayer(String playerName, String avatarModel)
    {

    }

    public void AddUser(String username, String password)
    {
        //byte[] pwbytes = b.StringToBytes(password);
        //byte[] hash = shared.CryptHashes.GetHash(pwbytes, CryptHashes.Hashtype.sha1);
        byte[] hash = AuthServer.AcctLoginRequest.getStoredHash(username, password);
        byte[] guid  = Guid.newRandomPlayer();
        int flags = 8; //normal
        int billingtype = 1; //normal
        this.sqlupdate("INSERT INTO accounts (accountname,passwordhash,guid,flags,billingtype) VALUES (?,?,?,?,?)", username,hash,guid,flags,billingtype);
    }
    
    public accountinfo GetUser(String username)
    {
        //try{
            Results results = this.sqlquery("SELECT * FROM accounts WHERE accountname=?", username);
            boolean hasrows = results.first();
            if(!hasrows) return null;
            accountinfo r = new accountinfo(results);
            return r;
        //}catch(Exception e){
        //    throw new uncaughtnestedexception(e);
        //}
    }

    public static class accountinfo //in table 'accounts'
    {
        String accountname;
        byte[] passwordhash;
        byte[] guid;
        int flags;
        int billingtype;

        public accountinfo(Results r)
        {
            try{

                accountname = r.getString("accountname");
                passwordhash = r.getBytes("passwordhash");
                guid = r.getBytes("guid");
                flags = r.getInt("flags");
                billingtype = r.getInt("billingtype");

            }catch(Exception e){
                throw new nested(e);
            }
        }
    }
    public static int getCurrentTime()
    {
        return shared.DateTimeUtils.getCurrentTimeInSeconds();
    }


}
