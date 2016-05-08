package SevenZip.Archive.SevenZip;

import SevenZipCommon.ByteBuffer;

public class AltCoderInfo {
    public MethodID MethodID;
    public ByteBuffer Properties;
    
    public AltCoderInfo() {
        MethodID = new MethodID();
        Properties = new ByteBuffer();
    } 
}
