/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.io.InputStream;
import SevenZip.Archive.IInArchive;
import SevenZip.Archive.SevenZip.Handler;
import SevenZip.IInStream;
import SevenZip.MyRandomAccessFile;
import java.io.RandomAccessFile;
import java.io.IOException;
import SevenZip.Archive.SevenZipEntry;
import SevenZip.ArchiveExtractCallback;
import java.io.File;
import SevenZip.MyRandomAccessFile;

import SevenZip.Archive.IArchiveExtractCallback;
import SevenZip.HRESULT;

//7zip needs to do random file access.
//And you need to implement IInStream, which MyRandomAccessFile does for actual files.
//I could easily create a similar one for Byte Arrays if we want to keep this in memory.
//I also modified SevenZip.Archive.SevenZip.Handler by commenting out the exception catcher, so I can catch them instead.
public class sevenzip
{
    public static interface FileIncluder
    {
        public boolean includeFile(String filename);
    }
    public static boolean check(String filename, String basefolder, FileIncluder includer)
    {
        MyRandomAccessFile istream = null;
        try
        {
            //part of the hack to set outputfolder:
            String root = basefolder+"/";

            //use this for files on disk.
            istream = new MyRandomAccessFile(filename,"r");

            IInArchive archive = new Handler();
            int result = archive.Open(istream);
            if(result!=0&&result!=1)
            {
                //we get a value of 1 sometimes, but it works fine.  No exclusive lock maybe?
                m.err("Problem opening/reading 7z file.");
            }

            /*ArchiveExtractCallback callback = new ArchiveExtractCallback();
            callback.Init(archive);

            //the -1 says extract all files, so we don't need the 1st argument.
            //the NExtract_NAskMode_kExtract means we want to extract the files.
            int result2 = archive.Extract(null, -1, IInArchive.NExtract_NAskMode_kExtract, callback);
            if(result2!=0)
            {
                //error
                m.err("Error during extraction.");
            }
            long numerrors = callback.NumErrors;
            if(numerrors!=0)
            {
                //errors during extraction.
                m.err("Errors during extraction: "+Long.toString(numerrors));
            }*/

            //this block goes through all the entries.
            int count = archive.size();
            for(int i=0;i<count;i++)
            {
                SevenZipEntry entry = archive.getEntry(i);
                if(!entry.isDirectory())
                {
                    String name = entry.getName();
                    File fullfilename = new File(basefolder+"/"+name);
                    /*File f = new File(fullfilename);
                    if(f.exists() && f.isFile())
                    {
                        m.msg("Deleting "+fullfilename);
                        boolean success = f.delete();
                        if(!success)
                        {
                            m.warn("Unable to delete file: "+name);
                        }
                    }*/
                    //FileUtils.DeleteFile2(fullfilename);
                    if(!entry.isDirectory() && includer.includeFile(name))
                    {
                        long correctsize = entry.getSize();
                        int correctcrc = (int)entry.getCrc();
                        if(!fullfilename.exists()) return false;
                        long actualsize = fullfilename.length();
                        if(actualsize!=correctsize) return false;
                        if(correctsize!=0) //bit of a weird behavior: .7z files have the crc as -1 if the size is 0, but the crc should be 0.
                        {
                            byte[] data = FileUtils.ReadFile(fullfilename);
                            int actualcrc = SevenZipCommon.CRC.CalculateDigest(data, data.length);
                            if(actualcrc!=correctcrc) return false;
                        }
                    }
                }
            }


            archive.close();
            //istream.close();
            return true;
        }
        catch(Exception e)
        {
            m.err("Error during 7zip checking.");
            return false;
        }
        finally
        {
            try
            {
                if(istream!=null) istream.close();
            }
            catch(Exception e){}
        }
    }
    public static void delete(String filename, String outputfolder)
    {
        MyRandomAccessFile istream = null;
        try
        {
            //part of the hack to set outputfolder:
            String root = outputfolder+"/";
            
            //use this for files on disk.
            istream = new MyRandomAccessFile(filename,"r");

            IInArchive archive = new Handler();
            int result = archive.Open(istream);
            if(result!=0&&result!=1)
            {
                //we get a value of 1 sometimes, but it works fine.  No exclusive lock maybe?
                m.err("Problem opening/reading 7z file.");
            }
            
            /*ArchiveExtractCallback callback = new ArchiveExtractCallback();
            callback.Init(archive);
            
            //the -1 says extract all files, so we don't need the 1st argument.
            //the NExtract_NAskMode_kExtract means we want to extract the files.
            int result2 = archive.Extract(null, -1, IInArchive.NExtract_NAskMode_kExtract, callback);
            if(result2!=0)
            {
                //error
                m.err("Error during extraction.");
            }
            long numerrors = callback.NumErrors;
            if(numerrors!=0)
            {
                //errors during extraction.
                m.err("Errors during extraction: "+Long.toString(numerrors));
            }*/
            
            //this block goes through all the entries.
            int count = archive.size();
            for(int i=0;i<count;i++)
            {
                SevenZipEntry entry = archive.getEntry(i);
                if(!entry.isDirectory())
                {
                    String name = entry.getName();
                    String fullfilename = outputfolder+"/"+name;
                    /*File f = new File(fullfilename);
                    if(f.exists() && f.isFile())
                    {
                        m.msg("Deleting "+fullfilename);
                        boolean success = f.delete();
                        if(!success)
                        {
                            m.warn("Unable to delete file: "+name);
                        }
                    }*/
                    FileUtils.DeleteFile2(fullfilename);
                }
            }
            
            
            archive.close();
            //istream.close();
        }
        catch(Exception e)
        {
            m.err("Error during 7zip deletion.");
        }
        finally
        {
            try
            {
                if(istream!=null) istream.close();
            }
            catch(Exception e){}
        }
    }
    public static long getTotalSize(IInArchive archive)
    {
        long result = 0;
        int numentries = archive.size();
        for(int i=0;i<numentries;i++)
        {
            long entrysize = archive.getEntry(i).getSize();
            result += entrysize;
        }
        return result;
    }
    public static boolean extract(String filename, String outputfolder)
    {
        MyRandomAccessFile istream = null;
        try
        {
            //part of the hack to set outputfolder:
            String root = outputfolder+"/";
            
            //use this for files on disk.
            istream = new MyRandomAccessFile(filename,"r");

            IInArchive archive = new Handler();
            int result = archive.Open(istream);
            if(result!=0)
            {
                m.err("Problem opening/reading 7z file.");
                return false;
            }
            
            try
            {
                //long bytesneeded = getTotalSize(archive) + 10000000; //give 10MB extra space to be safe.
                //File outputfile = new File(root);
                //long bytesavailable = outputfile.getUsableSpace();
                //if(bytesneeded>bytesavailable)
                long bytesneeded = getTotalSize(archive);
                if(!FileUtils.HasFreeSpace(root, bytesneeded))
                {
                    m.err("It doesn't appear that there is enough free space available.");
                    return false;
                }
            }
            catch(Exception e){} //if there's a exception, just plow ahead.
            
            ModifiedArchiveExtractCallback callback = new ModifiedArchiveExtractCallback(root);
            callback.Init(archive);
            
            //the -1 says extract all files, so we don't need the 1st argument.
            //the NExtract_NAskMode_kExtract means we want to extract the files.
            //exceptions are caught and printed inside of this Extract call.
            int result2 = archive.Extract(null, -1, IInArchive.NExtract_NAskMode_kExtract, callback);
            if(result2!=0)
            {
                //error
                m.err("Error during extraction.");
                return false;
            }
            long numerrors = callback.NumErrors;
            if(numerrors!=0)
            {
                //errors during extraction.
                m.err("Errors during extraction: ",Long.toString(numerrors));
                return false;
            }
            
            //this block goes through all the entries.
            int count = archive.size();
            for(int i=0;i<count;i++)
            {
                SevenZipEntry entry = archive.getEntry(i);
                //entry.
            }
            
            
            archive.close();
            //istream.close();
        }
        catch(Exception e)
        {
            if(e instanceof IOException && e.getMessage().equals("There is not enough space on the disk"))
            {
                m.err("Not enough disk space to finish extracting.  You shouldn't link to this Age until this is resolved.  Deleting extracted files...");
                delete(filename, outputfolder);
                m.err("Not enough disk space.  Free up some space, then try again.");
                return false;
            }
            m.err("Error during 7zip extraction.");
            return false;
        }
        finally
        {
            try
            {
                if(istream!=null) istream.close();
            }
            catch(Exception e){}
        }
        return true;
    }
    
    /*public static void extract2(String filename, String outputfolder)
    {
        m.err("This function isn't working yet.");
        try
        {
            //part of the hack to set outputfolder:
            String root = outputfolder+"/";
            
            //use this for files on disk.
            MyRandomAccessFile istream = new MyRandomAccessFile(filename,"r");

            IInArchive archive = new Handler();
            int result = archive.Open(istream);
            if(result!=0)
            {
                m.err("Problem opening/reading 7z file.");
            }
            
            
            SevenZip.Archive.SevenZip.ArchiveDatabaseEx db = new SevenZip.Archive.SevenZip.ArchiveDatabaseEx();
            SevenZip.Archive.SevenZip.InArchive archive2 = new SevenZip.Archive.SevenZip.InArchive();
            int ret = archive2.Open(istream, SevenZip.Archive.IInArchive.kMaxCheckStartPosition);
            //if (ret != SevenZip.HRESULT.S_OK) return ret;
            ret = archive2.ReadDatabase(db); // getTextPassword
            //if (ret != SevenZip.HRESULT.S_OK) return ret;
            db.Fill();
            
            int numItems = db.Files.size();
            for(int i=0;i<numItems;i++)
            {
                int folderIndex = i;
                SevenZip.Archive.SevenZip.Decoder decoder = new SevenZip.Archive.SevenZip.Decoder(false);
                int packStreamIndex = db.FolderStartPackStreamIndex.get(folderIndex); // CNum
                long folderStartPackPos = db.GetFolderStreamPos(folderIndex, 0);
                SevenZip.Archive.SevenZip.Folder folderInfo = db.Folders.get(folderIndex);
                java.io.ByteArrayOutputStream outStream = new java.io.ByteArrayOutputStream();
                SevenZip.Common.LocalCompressProgressInfo localCompressProgressSpec = new SevenZip.Common.LocalCompressProgressInfo();
                SevenZip.ICompressProgressInfo compressProgress = localCompressProgressSpec;
                decoder.Decode(istream, folderStartPackPos, db.PackSizes, packStreamIndex, folderInfo, outStream, compressProgress);
                byte[] output = outStream.toByteArray();
                int dummy=0;
            }
                
            //this block goes through all the entries.
            int count = archive.size();
            for(int i=0;i<count;i++)
            {
                SevenZipEntry entry = archive.getEntry(i);
                //db.
                //entry.
            }
            
            
            archive.close();
            //istream.close();
        }
        catch(Exception e)
        {
            m.err("Error during 7zip extraction.");
        }
    }*/
    
    /*private static class ModifiedMyRandomAccessFile extends IInStream
    {
        RandomAccessFile f;
        public ModifiedMyRandomAccessFile(String filename) throws IOException
        {
            f  = new RandomAccessFile(filename, "r");
        }
        public long Seek(long offset, int seekOrigin) throws IOException
        {
            if (seekOrigin==STREAM_SEEK_SET)
            {
                f.seek(offset);
            }
            else if (seekOrigin==STREAM_SEEK_CUR)
            {
                f.seek(offset + f.getFilePointer());
            }
            return f.getFilePointer();
        }
        public int read() throws IOException
        {
            return f.read();
        }
        public int read(byte [] data, int off, int size) throws IOException
        {
            return f.read(data,off,size);
        }
        public void close() throws IOException
        {
            f.close();
            f = null;
        }   
    }*/
    
    //modified to use the "root" variable to set the locatio to extract to.
    //modified the PrintString("Extracting  "); part to only print it on one line instead of two.
    public static class ModifiedArchiveExtractCallback implements IArchiveExtractCallback // , ICryptoGetTextPassword,
    {
        String root = "";

        class OutputStream extends java.io.OutputStream {
            java.io.RandomAccessFile file;

            public OutputStream(java.io.RandomAccessFile f) {
                file = f;
            }

            public void close()  throws java.io.IOException {
                file.close();
                file = null;
            }
            /*
            public void flush()  throws java.io.IOException {
                file.flush();
            }
             */
            public void write(byte[] b)  throws java.io.IOException {
                file.write(b);
            }

            public void write(byte[] b, int off, int len)  throws java.io.IOException {
                file.write(b,off,len);
            }

            public void write(int b)  throws java.io.IOException {
                file.write(b);
            }
        }

        public int SetTotal(long size) {
            return HRESULT.S_OK;
        }

        public int SetCompleted(long completeValue) {
            return HRESULT.S_OK;
        }

        public void PrintString(String str) {
            System.out.print(str);
        }

        public void PrintNewLine() {
            System.out.println("");
        }
        public int PrepareOperation(int askExtractMode) {
            _extractMode = false;
            switch (askExtractMode) {
                case IInArchive.NExtract_NAskMode_kExtract:
                    _extractMode = true;
            };
            switch (askExtractMode) {
                case IInArchive.NExtract_NAskMode_kExtract:
                    //PrintString("Extracting  "+_filePath);
                    m.msg("Extracting ",_filePath);
                    break;
                case IInArchive.NExtract_NAskMode_kTest:
                    PrintString("Testing     ");
                    break;
                case IInArchive.NExtract_NAskMode_kSkip:
                    PrintString("Skipping    ");
                    break;
            };
            //PrintString(_filePath);
            return HRESULT.S_OK;
        }

        public int SetOperationResult(int operationResult) throws java.io.IOException {
            switch(operationResult) {
                case IInArchive.NExtract_NOperationResult_kOK:
                    break;
                default:
                {
                    NumErrors++;
                    PrintString("     ");
                    switch(operationResult) {
                        case IInArchive.NExtract_NOperationResult_kUnSupportedMethod:
                            PrintString("Unsupported Method");
                            break;
                        case IInArchive.NExtract_NOperationResult_kCRCError:
                            PrintString("CRC Failed");
                            break;
                        case IInArchive.NExtract_NOperationResult_kDataError:
                            PrintString("Data Error");
                            break;
                        default:
                            PrintString("Unknown Error");
                    }
                }
            }
                /*
                if(_outFileStream != null && _processedFileInfo.UTCLastWriteTimeIsDefined)
                    _outFileStreamSpec->File.SetLastWriteTime(&_processedFileInfo.UTCLastWriteTime);
                 */
            if (_outFileStream != null) _outFileStream.close(); // _outFileStream.Release();
                /*
                if (_extractMode && _processedFileInfo.AttributesAreDefined)
                    NFile::NDirectory::MySetFileAttributes(_diskFilePath, _processedFileInfo.Attributes);
                 */
            //PrintNewLine();
            return HRESULT.S_OK;
        }

        java.io.OutputStream _outFileStream;

        public int GetStream(int index,
                java.io.OutputStream [] outStream,
                int askExtractMode) throws java.io.IOException {

            outStream[0] = null;

            SevenZipEntry item = _archiveHandler.getEntry(index);
            
            //Dustin
            if(item.isDirectory()) return HRESULT.S_OK; //skip directories.  This has the side-effect of not creating empty folders, which we shouldn't have hopefully.
            String name = item.getName().toLowerCase();
            if(name.startsWith("dat/")) _filePath = root + "dat/" + item.getName().substring(4);
            else if(name.startsWith("sfx/")) _filePath = root + "sfx/" + item.getName().substring(4);
            else if(name.startsWith("sdl/")) _filePath = root + "SDL/" + item.getName().substring(4);
            else if(name.startsWith("avi/")) _filePath = root + "avi/" + item.getName().substring(4);
            else if(name.startsWith("python/")) _filePath = root + "Python/" + item.getName().substring(7);
            else _filePath = root + item.getName();
            //end Dustin
            
            File file = new File(_filePath);

            switch (askExtractMode) {
                case IInArchive.NExtract_NAskMode_kTest:
                    return HRESULT.S_OK;

                case IInArchive.NExtract_NAskMode_kExtract:

                    try {
                        isDirectory = item.isDirectory();

                        if (isDirectory) {
                            if (file.isDirectory()) {
                                return HRESULT.S_OK;
                            }
                            if (file.mkdirs())
                                return HRESULT.S_OK;
                            else
                                return HRESULT.S_FALSE;
                        }


                        File dirs = file.getParentFile();
                        if (dirs != null) {
                            if (!dirs.isDirectory())
                                if (!dirs.mkdirs())
                                    return HRESULT.S_FALSE;
                        }

                        long pos = item.getPosition();
                        if (pos == -1) {
                            file.delete();
                        }

                        java.io.RandomAccessFile outStr = new java.io.RandomAccessFile(_filePath,"rw");

                        if (pos != -1) {
                            outStr.seek(pos);
                        }

                        outStream[0] = new OutputStream(outStr);
                    } catch (java.io.IOException e) {
                        return HRESULT.S_FALSE;
                    }

                    return HRESULT.S_OK;

            }

            // other case : skip ...

            return HRESULT.S_OK;

        }

        SevenZip.Archive.IInArchive _archiveHandler;  // IInArchive
        String _filePath;       // name inside arcvhive
        String _diskFilePath;   // full path to file on disk

        public long NumErrors;
        boolean PasswordIsDefined;
        String Password;
        boolean _extractMode;

        boolean isDirectory;

        public ModifiedArchiveExtractCallback(String root) {
            PasswordIsDefined = false;
            this.root = root;
        }


        public void Init(SevenZip.Archive.IInArchive archiveHandler) {
            NumErrors = 0;
            _archiveHandler = archiveHandler;
        }
    }

}
