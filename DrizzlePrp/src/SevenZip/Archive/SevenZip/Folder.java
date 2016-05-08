package SevenZip.Archive.SevenZip;

import java.io.IOException;

import SevenZipCommon.RecordVector;
import SevenZipCommon.IntVector;
import SevenZipCommon.LongVector;

import SevenZip.Archive.Common.BindPair;

public class Folder {
    public RecordVector<CoderInfo> Coders = new RecordVector<CoderInfo>();
    public RecordVector<BindPair> BindPairs = new RecordVector<BindPair>();
    public IntVector PackStreams = new IntVector();
    public LongVector UnPackSizes = new LongVector();
    public int UnPackCRC;
    public boolean UnPackCRCDefined;
    Folder() {
        UnPackCRCDefined = false;
    }

    long GetUnPackSize() throws IOException {
        if (UnPackSizes.isEmpty())
            return 0;
        for (int i = UnPackSizes.size() - 1; i >= 0; i--)
            if (FindBindPairForOutStream(i) < 0)
                return UnPackSizes.get(i);
        throw new IOException("1"); // throw 1  // TBD
    }
    
    int FindBindPairForInStream(int inStreamIndex) {
        for(int i = 0; i < BindPairs.size(); i++)
            if (BindPairs.get(i).InIndex == inStreamIndex)
                return i;
        return -1;
    }
    
    int FindBindPairForOutStream(int outStreamIndex) {
        for(int i = 0; i < BindPairs.size(); i++)
            if (BindPairs.get(i).OutIndex == outStreamIndex)
                return i;
        return -1;
    }
    
     int FindPackStreamArrayIndex(int inStreamIndex) {
        for(int i = 0; i < PackStreams.size(); i++)
            if (PackStreams.get(i) == inStreamIndex)
                return i;
        return -1;
    }
      
    int GetNumOutStreams() {
        int result = 0;
        for (int i = 0; i < Coders.size(); i++)
            result += Coders.get(i).NumOutStreams;
        return result;
    }
    
}