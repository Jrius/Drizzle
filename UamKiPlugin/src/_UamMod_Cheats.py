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
import _UamUtils
import uam
import Plasma
import PlasmaKITypes


def UamOnKiCommand(command):
    #print "_UamModCheats.UamOnKiCommand: "+`command`
    if(command=="/opencleftdoor"):
        print "_UamModCheats: Opening Cleft Door"
        #Calling this twice is fine.  And we can call it only from the Cleft.
        if _UamUtils.GetAgeName()!="Cleft":
            uam.PrintKiMessage("You can only do this in the Cleft.")
            return True
        sdl = Plasma.PtGetAgeSDL()
        varname = "clftBahroDoorClosed"
        index = 0
        value = 0  #i.e. we want it opened
        sdl.setFlags(varname, 1, 1)
        sdl.sendToClients(varname)
        sdl.setIndex(varname, index, value)
        return True
    if(command=="/getki"):
        print "_UamModCheats: getting ki"
        #Calling this twice should be okay, since kNormalKI is the highest level, so we won't be downgrading it ever.
        #We can call this from any Age.
        #Add the normal ki:
        Plasma.PtSendKIMessageInt(PlasmaKITypes.kUpgradeKILevel, PlasmaKITypes.kNormalKI)
        #Set the chronicle directly, because the Offline-KI blocks this for some bizarre reason.
        uam._SetPlayerChronicle(PlasmaKITypes.kChronicleKILevel, str(PlasmaKITypes.kNormalKI), PlasmaKITypes.kChronicleKILevelType)
        return True
    if(command=="/getmarkers"):
        #Register the new link.  From IUpdateNexusLink from grtzKIMarkerMachine.py
        #This can be called multiple times okay, because the chron is never set higher and adding the spawnpoint more than once doesn't create duplicate copies.  And it can be called from any Age.
        #But you need to restart Uru for some unknown reason, before you can use F8 to create a mission.
        print "_UamModCheats: getting markers"
        success = GetMarkersAddGZLink();
        if success:
            #Set the chronicle saying that we've got
            uam._SetPlayerChronicle("KIMarkerLevel","3")
            uam.PrintKiMessage("You should now be able to do marker missions!  (Though you may need to restart Uru first.)")
        else:
            uam.PrintKiMessage("You need to visit the Great Zero at least once first.")
        return True
            
_UamEvents.RegisterForKiCommand(UamOnKiCommand)



#Similar functionality to grtzMarkerMachine.IUpdateNexusLink
def GetMarkersAddGZLink():
    agelinks = Plasma.ptVault().getAgesIOwnFolder().getChildNodeRefList()
    for agelink in agelinks:
        aln = agelink.getChild().upcastToAgeLinkNode()
        age = aln.getAgeInfo().getAgeFilename()
        if age=="GreatZero":
            spawnpoint = Plasma.ptSpawnPointInfo("Great Zero","BigRoomLinkInPoint")
            aln.addSpawnPoint(spawnpoint)
            aln.save()
            return True
    return False

