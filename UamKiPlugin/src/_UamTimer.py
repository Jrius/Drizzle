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
import _UamEvents
import uam
import weakref

#Should this use weak refs for the callback functions?  It's hard to decide...

#Timer
_curtimerstart = 10000 #stay away from xKI's values
_curtimerend = 2000000000 #2 billion
_curtimer = _curtimerstart
_timers = {}
#_useweakrefs = True
def _GetNextTimerId():
    global _curtimer
    _curtimer += 1
    if _curtimer > _curtimerend:
        _curtimer = _curtimerstart
    return _curtimer
    
def Timer(callback, time, isweak, removewhenlink):
    #calls the function with the given context in 'time' seconds
    #print "_UamTimer.Timer adding timer"
    id = _GetNextTimerId()
    if isweak:
        callback =  weakref.ref(callback)
    _timers[id] = ( callback, isweak, removewhenlink )
    Plasma.PtAtTimeCallback(uam._ki.key, time, id)
    
def _OnTimer(id):
    global _timers
    #print "_UamTimer._OnTimer"
    tuple = _timers.get(id)
    if tuple!=None:
        "_UamTimer._OnTimer this is ours!"
        del _timers[id] #remove from dictionary
        
        (callback, isweak, removewhenlink) = tuple
        #if type(callback).__name__=="weakref": #It should be either "weakref" or "function"
        if isweak:
            callback = callback() #makes it no longer a weakref, not to be confused with 2 lines down, where it actually calls the function!
        if callback!=None:
            callback()
        else:
            #garbage collector got 'em!
            print "_UamUtils._OnTimer callback not called because it was garbage collected"
_UamEvents.RegisterForOnTimer(_OnTimer)

def Reset():
    print "_UamTimer.Reset"
    for id in _timers:
        (callback, isweak, removewhenlink) = _timers[id]
        if removewhenlink:
            print "Removing timer because of link-out."
            del _timers[id]
