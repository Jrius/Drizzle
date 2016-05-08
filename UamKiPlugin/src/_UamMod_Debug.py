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
import Plasma
import uam

_test = None

def UamOnKiCommand(command):
    print "UamModDebug.UamOnKiCommand: "+`command`
    args = command.split()  #splits on whitespace
    if command=="/uamtest 1":
        uam.DisplayLinkingBook("Aerie","LinkInPointDefault")
        return True
    if command=="/uamtest 2":
        uam.EnableReltoPage("DustCushions")
        return True
    if command=="/uamtest 3":
        def _TimerTest():
            uam.PrintKiMessage("The timer works!")
        uam.SetTimer(_TimerTest, 1.0)
        return True
    if command=="/uamtest 4":
        val = uam.GetAgeSdl("psnlLibraryDoorClosed")
        uam.PrintKiMessage("psnlLibraryDoorClosed set to: "+`val`)
        return True
    if command=="/uamtest 5":
        uam.SetAgeSdl("psnlLibraryDoorClosed",1)
        val = uam.GetAgeSdl("psnlLibraryDoorClosed",0)
        uam.PrintKiMessage("psnlLibraryDoorClosed set to: "+`val`)
        return True
    if command=="/uamtest 6":
        if _UamUtils._IsRestorationAge("Aerie") and not _UamUtils._IsRestorationAge("ThisIsNotAnAgename"):
            uam.PrintKiMessage("Test6 successful!")
        return True
    if command=="/uamtest 7":
        if _UamUtils.GetCorrectFilename("aerie")=="Aerie":
            uam.PrintKiMessage("Test7 successful!")
        return True
    if command=="/uamtest 8":
        uam.LinkToAge("Aerie","LinkInPointDefault")
        return True
    if command=="/listmodules":
        import sys
        #uam.PrintKiMessage(`sys.modules.keys()`)
        print "modules: "+`sys.modules.keys()`
        return True
    if args[0]=="/show2":
        print "show:"+args[1]
        agename = _UamUtils.GetAgeName()
        av = Plasma.PtFindSceneobject(args[1], agename)
        av.draw.enable()
        av.physics.suppress(0)
        return True
    if args[0]=="/hide2":
        success = _UamUtils.HideObject(args[1])
        if not success:
            uam.PrintKiMessage("Unable to find object: "+args[1])
        return True

_UamEvents.RegisterForKiCommand(UamOnKiCommand)

    
# Findings about garbage collection:
# Modules are forced to be unloaded when linking out, even if there are references to a function in them still alive.
# But that function, passed through SetTimer will be kept, even when the modules is no longer listed in sys.modules.keys()
# But if only a weakref is kept to that function, then even it will be lost!
# So... I guess we shouldn't allow Ages to have non-weak references, or else we should remove all items from the timer list when linking out.


