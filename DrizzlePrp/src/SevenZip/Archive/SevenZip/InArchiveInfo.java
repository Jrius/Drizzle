package SevenZip.Archive.SevenZip;

import SevenZipCommon.LongVector;

public class InArchiveInfo
{
    public byte ArchiveVersion_Major;
    public byte ArchiveVersion_Minor;
    
    public long StartPosition;
    public long StartPositionAfterHeader;
    public long DataStartPosition;
    public long DataStartPosition2;    
    LongVector FileInfoPopIDs = new LongVector();
    
    void Clear()
    {
        FileInfoPopIDs.clear();
    }    
}