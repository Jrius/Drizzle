/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package alcugsinterface;

import shared.*;

public class Packet
{
    byte id;
    byte ver;
    int checksum;
    
    int packetcounter;
    byte packettype;
    int zero;
    byte datafragmentcount;
    Int24 messagenum;
    byte totalNumberOfDataFragments;
    int zero2;
    byte lastFragmentedPacketAck;
    Int24 lastAckReceived;
    int dataSize;
    
    public Packet(byte[] packet)
    {
        ByteArrayBytestream c = ByteArrayBytestream.createFromByteArray(packet);
    //    parse(c,packet);
    //}
    
    //public static void parse(ByteArrayBytestream c, byte[] packet)
    //{
        LoginContext user = new LoginContext();
        user.isAuthenicated = false;

        id = c.readByte();
        if(id!=0x03)
        {
            m.warn("packet did not start with 3.");
            return;
        }
        ver = c.readByte();
        if(ver!=0x02)
        {
            m.warn("packer ver not equal to 2.");
            return;
        }
        checksum = c.readInt();
        
        //check checksum...
        int cs = uru_checksum(packet,user);
        if(cs!=checksum)
        {
            m.warn("checksum for packet wrong.");
            return;
        }
        
        //decrypt packet...
        uru.UruCrypt.DecryptUruMessageInPlace(packet);
        
        
        packetcounter = c.readInt();
        packettype = c.readByte();
        zero = c.readInt();
        datafragmentcount = c.readByte();
        messagenum = new Int24(c);
        //messagenum.toInt();
        totalNumberOfDataFragments = c.readByte();
        zero2 = c.readInt();
        lastFragmentedPacketAck = c.readByte();
        lastAckReceived = new Int24(c);
        dataSize = c.readInt();
        
        switch(packettype)
        {
            case (byte)0x42:
                m.msg("got plNetClientComm msg.");
                PlNetClientComm nc = new PlNetClientComm(c);
                break;
            case (byte)0x80:
                m.msg("got ack msg.");
                PlNetAck na = new PlNetAck(c,this);
                break;
            default:
                m.msg("got unknown msg type: 0x",Integer.toHexString(packettype));
                break;
        }
        
        int dummy=0;
    }
    
    public static int uru_checksum(byte[] packet, LoginContext user)
    {
        int size = packet.length;
        byte[] buf = packet;
        byte[] aux_hash = user.passwordHash;
        
        
	//code for the V2 - Checksum algorithm
        int aux_size=size-6;
	if(user.isAuthenicated) aux_size+=32;
	//allocate the space for the buffer
	//DBG(4,"Allocating md5buffer - %i bytes...\n",aux_size);
	//md5buffer = (Byte *)malloc(sizeof(Byte)*(aux_size));
        byte[] md5buffer = new byte[aux_size];
	//		if(md5buffer==NULL) {
	//			DBG(4,"md5buffer is NULL?\n");
	//		}
        for(int i=6; i<size; i++)
        {
            md5buffer[i-6]=buf[i];
        }
	//Concatenate the ASCII passwd md5 hash as required by V2 algorithm
        if(user.isAuthenicated)
        {
            for(int i=size; i<(aux_size+6); i++)
            {
                md5buffer[i-6]=aux_hash[i-size];
            }
        }
			//print2log(f_chkal,"passwd_hash: %s\n",aux_hash);
			//print2log(f_chkal,"to be md5sumed:\n");
			//dump_packet(f_chkal,md5buffer,aux_size,0,5);
        byte[] hash = shared.CryptHashes.GetMd5(md5buffer);
			//MD5(md5buffer, aux_size, hash);
			//print2log(f_chkal,"\n<-\n");

			//aux = *((U32 *)hash);
			//free(md5buffer);

			//break;
        
        int result = b.BytesToInt32(hash, 0); //grab the first 4 bytes.
        return result;
    }

}
