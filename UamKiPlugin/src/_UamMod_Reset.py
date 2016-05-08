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

import _UamEvents
#import _UamUtils

delpath = None

def UamOnKiCommand(command):
    print "UamModReset.UamOnKiCommand: "+`command`
    if command=="/reset":
        #resets the current age, with the help of the server's /!resetage command
        
        import _UamUtils
        import uam
        import Plasma
        
        curage = _UamUtils.GetAgeName()
        prefix = _UamUtils.GetSequencePrefix(curage)
        print "curage="+`curage`+" prefix="+`prefix`
        
        #detect if this is an Age we can reset
        #if not _UamUtils.IsThisRestorationAge():
        if prefix < 100:
            uam.PrintKiMessage("This Age cannot be reset.  You can only reset fan Ages.")
            return True
        
        #detect if there are any other players
        if _UamUtils.GetNumOfOtherPlayers() > 0:
            uam.PrintKiMessage("There are other players in the Age.  You must be alone to reset the Age.")
            return True
            
        ageVault = Plasma.ptAgeVault()
        
        #reset Age devices
        devfolder = ageVault.getAgeDevicesFolder()  #Should be a ptVaultFolderNode
        print "devfolder: "+`devfolder`
        devfolder.removeAllNodes()
        
        #reset Age chronicles
        chronfolder = ageVault.getChronicleFolder() #Should be a ptVaultFolderNode
        print "chronfolder: "+`chronfolder`
        chronfolder.removeAllNodes()
        
        #reset Player chronicles for this Agename from D'Lanor's system
        playerVault = Plasma.ptVault()
        entry = playerVault.findChronicleEntry("UserAges")
        if type(entry)==type(None):
            print "No UserAges chronicle folder."
        else:
            chronRefList = entry.getChildNodeRefList()
            for subChron in chronRefList:
                theChild = subChron.getChild()
                theChild = theChild.upcastToChronicleNode()
                if theChild.chronicleGetName()==curage:
                    #found it so delete it
                    print "Found Player chronicle node, so deleting it for Age: "+curage
                    entry.removeNode(theChild)
                    break
        
        
        #reset Age SDL node (but the SDL is still stored in the .sav file/server)
        #sdlnode = ageVault.getAgeSDL() #Should be a ptSDLStateDataRecord.  We can get the ptVaultSDLNode through the AgeInfoNode if we want that instead.
        #print "sdlnode: "+`sdlnode`
        #sdlnode.setStateDataRecord(None)
        #sdlnode.save()
        #print "sdlnode vars: "+`sdlnode.getVarList()`  #returns e.g. ["isPowerOn","SdlVar2"]
        #sdlval = sdlnode.findVar("isPowerOn")
        #print "sdlnode var: "+`sdlval`  #returns ptSimpleStateVariable, but maybe sometimes a ptSDLStateDataRecord?
        #vars = sdlnode.getVarList()
        #for var in vars:
        #    statevar = sdlnode.findVar(var)
        #    if isinstance(statevar,ptSimpleStateVariable):
        #        default = statevar.getDefault()
        #        print "default: "+`default`
        #    else:
        #        print "Error: statevar was actually: "+`statevar`
        ageinfo = ageVault.getAgeInfo() #Should be a ptVaultAgeInfoNode
        print "ageinfo: "+`ageinfo`
        sdlnode = ageinfo.getAgeSDL() #Should be a ptVaultSDLNode
        print "sdlnode: "+`sdlnode`
        #sdlnode.setStateDataRecord(None)
        #sdlnode.save()
        ageinfo.removeNode(sdlnode)
        
        guid = ageVault.getAgeGuid()
        print "guid: "+`guid`
        
        
        print "offline reset done!"
            
        #get the server to reset the SDL and physics
        if _UamUtils.GetGame()=="pots":
            #delete the .sav file
            hexname = _UamUtils.GetPlayerNameInHex()
            path = "sav/"+hexname+"/current/"+Plasma.PtGetAgeName()+"_"+guid+".sav"
            print path
            global delpath
            delpath = path
            #uam.PrintKiMessage("You should quit immediatly, then delete this file and restart Uru: "+path)
            #import os
            #os.remove("test.txt")
            #os.remove(path)
            #relink
            #Plasma.ptNetLinkingMgr().linkToPlayersAge(Plasma.PtGetLocalClientID())
            uam.PrintKiMessage("Linking you to the nexus to finish resetting the Age...")
            uam._ki.ISendRTChat("/nexus")

        elif _UamUtils.GetGame()=="alcugs":
            print "sending reset command to server..."
            uam._ki.ISendRTChat("/!resetage")
            #The /!resetage command should relink us
        return True
            
_UamEvents.RegisterForKiCommand(UamOnKiCommand)

def UamOnNewAgeLoaded():
    print "_UamModReset.UamOnNewAgeLoaded"
    global delpath
    if delpath!=None:
        #delete the file
        import os
        os.remove(delpath)
        delpath = None
_UamEvents.RegisterForOnServerInitComplete(UamOnNewAgeLoaded)
