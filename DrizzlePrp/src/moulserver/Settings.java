/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;



public class Settings
{
    public String username;
    public String password;

    //not modifiable:
    private static final String pathToFiles = "fileserver";
    private static final String pathToExternalPatcherManifest = "install/Expanded/ClientSetupNew.mfs_moul";
    private String domainname;
    private String mainpassword;
    private String datapath;
    
    //other settings elsewhere:
    //size of file chunks, which affects the rate at which download progress is updated. In ChunkSendHandler.java

    public String getDatapath()
    {
        //these should be modifiable by the user:
        //return "H:/DontBackup/prps/mouldataserver";
        return datapath;
    }
    public void setDatapath(String val)
    {
        datapath = val;
    }
    /*public String getPathToCleanFiles()
    {
        return getDatapath()+"/files/";
    }*/
    public String getPathToAgeFiles() //path to the .age files for the server
    {
        return getDatapath()+"/fileserver/agefiles/";
    }
    public String getPathToSdlFiles() //path to the .sdl files for the server
    {
        return getDatapath()+"/fileserver/SecureDownload/SDL/";
    }
    public String getFileserverPath()
    {
        return getDatapath()+"/"+pathToFiles;
    }
    public String getAuthFileserverPath()
    {
        return getFileserverPath()+"/SecureDownload/";
    }
    public String getPathToExternalPatcherManifest()
    {
        return getFileserverPath()+"/"+pathToExternalPatcherManifest;
    }
    public String getPathToMainManifest()
    {
        return getFileserverPath()+"/"+"game_clients/drcExplorer/client.mfs_moul";
    }
    public String getPathToAgeManifests()
    {
        return getFileserverPath()+"/"+"game_data/dat/";
    }
    public String getDomainName()
    {
        return domainname;
    }
    public void setDomainName(String domainname)
    {
        this.domainname = domainname;
    }
    public String getDatabasefile()
    {
        return getDatapath()+"/database/data";
    }
    public void setMainPassword(String password)
    {
        mainpassword = password;
    }
    public String getMainPassword()
    {
        return mainpassword;
    }
}
