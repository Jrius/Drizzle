/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import java.util.HashMap;

public class AuthServerSecureFiles
{

    private HashMap<String,byte[]> encryptedFiles = new HashMap();

    public void Reset()
    {
        //force everything to be reloaded.
        encryptedFiles.clear();
    }
    public byte[] GetEncrypted(String filename)
    {
        //takes in the absolute path to the file.

        byte[] encdata = encryptedFiles.get(filename);
        if(encdata==null)
        {
            byte[] unencdata = shared.FileUtils.ReadFile(filename);
            encdata = uru.UruCrypt.EncryptNotthedroids(unencdata, SuperManager.GetTalcumNotthedroids());
            //byte[] unencdata2 = uru.UruCrypt.DecryptNotthedroids(encdata, SuperManager.GetTalcumNotthedroids());
            //boolean correct = shared.b.isEqual(unencdata, unencdata2);
            encryptedFiles.put(filename, encdata);
        }
        return encdata;
    }
}
