#
#    Copyright 2005-2010 Dustin Bernard
#
#    This file is part of UruAgeManager/Drizzle.
#
#    UruAgeManager/Drizzle is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    UruAgeManager/Drizzle is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with UruAgeManager/Drizzle.  If not, see <http://www.gnu.org/licenses/>.
#

import Plasma

#For dealing with String->String dictionaries (musn't contain ; nor = nor rely on whitespace on the ends. )
def _DictToString(dict):
    str_list = []
    first = True
    for key in dict:
        val = dict[key]
        if not first:
            str_list.append(";")
        str_list.append(key+"="+val)
        first = False
    result = ''.join(str_list)
    return result
def _StringToDict(str):
    result = {}
    if str=="":
        return result
    list = str.split(";")
    for item in list:
        #parts = item.split("=")
        #if len(parts)==2:
        #    result[parts[0].strip()] = parts[1].strip()  #remove whitespace off the ends
        #else:
        #    #skip this part
        #    pass
        ind = item.find("=")
        if ind!=-1:
            result[item[:ind].strip()] = item[ind+1:].strip()  #remove whitespace off the ends
        else:
            #skip this part
            pass
    return result
def _ListToString(list):
    str_list = []
    first = True
    for val in list:
        if not first:
            str_list.append(";")
        str_list.append(val)
        first = False
    result = ''.join(str_list)
    return result
def _StringToList(str):
    result = []
    if str=="":
        return result
    list = str.split(";")
    for item in list:
        item = item.strip()
        if item!="":
            result.append(item)
    return result

        
        
def _IsRestorationAge(agename):
    #if agename=="MarshScene":
    #    return True
    #if agename=="MountainScene":
    #    return True
    import xLinkMgr
    print "IsRestorationAge: "+`agename`
    for age in xLinkMgr._RestorationLinks:
        print "age: "+`age`
        curagename = age[0]
        print "curagename: "+`curagename`
        if curagename==agename:
            return True
    return False

def IsThisRestorationAge():
    #import Plasma
    return _IsRestorationAge(Plasma.PtGetAgeName())
    
def GetSequencePrefix(agename):
    print "_UamUtils.GetSequencePrefix"
    filename = "dat/"+GetCorrectFilename(agename)+".age"
    text = DecryptWdys(filename)
    #print "Dectext: "+`text`
    lines = text.split("\n")
    for line in lines:
        line = line.strip()  #get rid of \r if present
        print "line: "+line
        if line.startswith("SequencePrefix="):
            prefixstr = line[len("SequencePrefix="):].strip()
            prefix = int(prefixstr)
            return prefix
    raise "Sequence Prefix not found."

def DecryptWdys(filename):
    import struct
    f = file(filename, "rb") #must open in binary mode!
    header = f.read(12)
    if header!="whatdoyousee":
        raise "Excepted file to be a whatdoyousee file: "+`filename`
    (innerlength,) = struct.unpack("<i",f.read(4)) #'<' sets the byte ordering
    decblocks = []
    i = 0
    while i < innerlength:
        block = f.read(8)
        decblock = _DecryptWdysBlock(block)
        remainingbytes = innerlength - i
        if remainingbytes < 8:
            decblocks.append(decblock[:remainingbytes])  #cut off the redundant bytes
        else:
            decblocks.append(decblock)
        i += 8
    dec = "".join(decblocks)
    f.close()
    return dec
    
def _DecryptWdysBlock(block):
    import struct
    num_rounds = 32
    k = [0x6c0a5452,0x03827d0f,0x3a170b92,0x16db7fc2]
    delta = 0x9E3779B9L
    mask = 0xFFFFFFFFL  #so we can kill overflow
    (v0,v1) = struct.unpack("<LL",block)
    sum = (delta*num_rounds)&mask
    for i in range(num_rounds):
        #Hmm... python doesn't have a logical right-shift...
        v1 = (v1 - ((((v0 << 4) ^ (v0 >> 5)) + v0) ^ (sum + k[(sum >> 11) & 3])))&mask;
        sum = (sum-delta)&mask;
        v0 = (v0 - ((((v1 << 4) ^ (v1 >> 5)) + v1) ^ (sum + k[sum & 3])))&mask;
    decblock = struct.pack("<LL",v0,v1)
    return decblock

    
def GetCorrectFilename(agename):
    #Check which version of the offlineki is in use:
    import xUserKI
    if xUserKI.gUserKIVersion in ["3.4","3.5"]:
        import xLinkMgr
        return xLinkMgr.GetCorrectFilename(agename)
    else:
        import xLinkMgr
        return xLinkMgr.GetCorrectFilenameCase(agename)

#Returns a string identifying the game, e.g. "pots", "alcugs", etc.  (In the future, it could have "uu" and "moul" and "talcum" too.)  This should be set from _UamMod_Pots, e.g.
_game = None
def GetGame():
    return _game

    
    
def GetNumOfOtherPlayers():
    #Returns the number of players other than yourself
    #print "numplayers: "+`len(Plasma.PtGetPlayerList())`
    return len(Plasma.PtGetPlayerList())

def GetPlayerName():
    return Plasma.PtGetLocalPlayer().getPlayerName()

def GetAgeName():
    return Plasma.PtGetAgeName()
    
def GetPlayerNameInHex():
    #return GetPlayerName().encode("hex").upper()
    import binascii
    return binascii.b2a_hex(GetPlayerName()).upper()

def AmInMyRelto():
    return Plasma.ptVault().inMyPersonalAge()

    
def GetLanguage():
    langnum = Plasma.PtGetLanguage()
    if langnum==0:
        return "en"
    elif langnum==1:
        return "fr"
    elif langnum==2:
        return "de"
    else:
        raise "Unexpected language"
        
def ReadJournal(filename):
    try:
        file = open(filename, 'r')
        filecontents = file.read()
        file.close()
        return filecontents
    except Exception, detail:
        print 'NeolbahJournals: ',
        print detail
        return 'Error: journal not found: '+`filename`

def HideObject(objectname):
    #Hides and disables a sceneobject
    print "hide:"+objectname
    agename = GetAgeName()
    try:
        av = Plasma.PtFindSceneobject(objectname, agename)
    except:
        print "_UamUtils.HideObject unable to find object: "+objectname
        return False
    av.draw.disable()
    av.physics.suppress(1)
    return True

def IsDayInRange(startmonth, startday, endmonth, endday):
    #This is inclusive of the start and end day, and uses UTC.
    import time
    timestruct = time.gmtime(Plasma.PtGetDniTime())
    #year = timestruct[0]
    month = timestruct[1]  #from 1 to 12
    day = timestruct[2]  #from 1 to 31
    startx = startmonth*32+startday
    endx = endmonth*32+endday
    x = month*32+day
    #print "debug: "+`startx`+" "+`endx`+" "+`x`
    if startx <= endx:
        if startx <= x and x <= endx:
            return True
        else:
            return False
    else: #wraps around the year-end
        if x <= endx or x >= startx:  #must be outside 
            return True
        else:
            return False

def LoadPage(pagename):
    #This will load the pagename, even if it is not part of the current Age.
    print "_UamUtils.LoadPage: "+pagename
    Plasma.PtPageInNode(pagename)
    #There is no indication if it succeeded; you would have to listen to the page load event.
    #This page will not be unloaded if we link to another Age which contains the same page name.  E.g. loading OpenDayStuff in Tvel then linking to TvelStorm will not unload the page.

class Args:
    args = None
    def __init__(self, command):
        self.args = command.split()
    def get(self, num):
        if num < len(self.args):
            return self.args[num]
        else:
            return ""
    


