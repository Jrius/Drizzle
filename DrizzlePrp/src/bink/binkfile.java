/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bink;

import shared.*;

//Info from http://wiki.multimedia.cx/index.php?title=Bink_Container
public strictfp class binkfile
{
    byte[] magic;
    int filesizeminus8;
    public int numFrames;
    int largestFrameSize;
    int maybeNumFramesAgain;
    public int videoWidth;
    public int videoHeight;
    public int framesPerSecondDividend;
    public int framesPerSecondDivisor;
    
    public binkfile(IBytestream c)
    {
        magic = c.readBytes(4); e.ensure(b.BytesToString(magic).equals("BIKi")); //other versions of this magic number besides i are probably okay for as far as we're parsing.
        filesizeminus8 = c.readInt();
        numFrames = c.readInt();
        largestFrameSize = c.readInt();
        maybeNumFramesAgain = c.readInt();
        videoWidth = c.readInt();
        videoHeight = c.readInt();
        framesPerSecondDividend = c.readInt();
        framesPerSecondDivisor = c.readInt();
        
    }
    
    public float getLengthInSeconds()
    {
        float length = ((float)(numFrames*framesPerSecondDivisor))/((float)framesPerSecondDividend);
        return length;
    }
}
