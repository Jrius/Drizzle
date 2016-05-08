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
import _UamTimer

#register for ki notifications
def UamOnKiCommand(command):
    #print "UamModHelp.UamOnKiCommand: "+`command`
    agename = _UamUtils.GetAgeName()
    if agename=="BahroCave02" or agename=="BahroCave" or agename=="LiveBahroCaves":
        if command=="/nexus" or command=="/hood" or command.startswith("/link "):
            uam.PrintKiMessage("You can't link out of a Bahro Cave this way.")
            return True #intercept further handling
            
_UamEvents.RegisterForKiCommand(UamOnKiCommand)



def UamOnServerInitComplete():
    #print "UamModGameFixes.UamOnServerInitComplete: "+`command`
    agename = _UamUtils.GetAgeName()
    if agename=="Neighborhood" or agename=="NeighborhoodMOUL": # or agename=="KirelMOUL" or agename=="Neighborhood02":
        #Thanksgiving/Halloween:
        if _UamUtils.IsDayInRange(10,25,11,1):  #oct 25 to nov 1.  Halloween is Oct 31, and Thanksgiving day in the US is Nov 25. (October's 2nd monday is in Canada, so somewhere from Oct 8 to Oct 14.)
            print "enable halloween stuff"
            uam.SetAgeSdl("nb01ThanksgivingVis",1)
        else:
            print "disable halloween stuff"
            uam.SetAgeSdl("nb01ThanksgivingVis",0)
        #New Years':
        if _UamUtils.IsDayInRange(12,30,1,2):  #dec 30 to jan 2.
            print "enable new years stuff"
            uam.SetAgeSdl("nb01HappyNewYearVis",1)
        else:
            print "disable new years stuff"
            uam.SetAgeSdl("nb01HappyNewYearVis",0)
    elif agename=="BaronCityOffice":
        #Christmas:
        #if _UamUtils.IsDayInRange(11,30,1,1):  #nov 30 to jan 1.
        if _UamUtils.IsDayInRange(12,7,1,8):  #Changed to start after Dutch Sinterklaas festivities and continue to Epiphany and the Ukrainian Christmas day.
            print "enable Christmas stuff"
            uam.SetAgeSdl("bcoChristmasVis",1)
        else:
            print "disable Christmas stuff"
            uam.SetAgeSdl("bcoChristmasVis",0)
        
_UamEvents.RegisterForOnServerInitComplete(UamOnServerInitComplete)


