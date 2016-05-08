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
import weakref
import _UamUtils
import _UamFormat
import uam
import traceback
import PlasmaConstants

#xNewHighLevelStarTrekDoor.py should be removed from the python source distribution, because it has an id number conflict. with xHighLevelStarTrekDoor.py

vars = {}  #dictionary mapping varnames to varobjects
#age = None
warnings = []
errors = []

def RegisterVar(uamvar):
    print "_UamVars.RegisterVar: "+uamvar
    #DumpState()
    #if _UamUtils.GetAgeName()!=age:
    #    Reset()
    varobj = vars.get(uamvar)
    if varobj==None:
        varobj = UamVar(uamvar)
        vars[uamvar] = varobj

def SetVar(uamvar, value):
    print "_UamVars.SetVar: "+uamvar+" value: "+value
    varobj = vars.get(uamvar)
    varobj.SetVar(value)
    
def ListenToVar(uamvar, listener):
    print "_UamVars.ListenToVar: "+uamvar
    #DumpState()
    varobj = vars.get(uamvar)
    print "varobj="+`varobj`
    print "varobjlisteners="+`varobj.listeners`
    varobj.ListenTo(listener)

def Reset():
    print "_UamVars.Reset"
    global vars
    #global age
    global warnings
    global errors
    vars = {}
    #age = _UamUtils.GetAgeName()
    warnings = []
    errors = []

def Error(msg):
    errors.append(msg);
    print "Error: "+msg
    raise msg

def Warning(msg):
    warnings.append(msg);
    print "Warning: "+msg

def DumpState():
    print "*********** Dumping UamVar states ************"
    print "vars:"
    for var in vars:
        varobj = vars[var]
        print "  varname="+`var`+":"
        print "    internalName="+`varobj.varname`
        print "    isGlobal="+`varobj.isglobal`
        print "    listeners:"
        for listener in varobj.listeners:
            print "      listener="+`listener`
    print "******************* (end) *********************"

class UamVar:

    #varname #= None
    #listeners #= []
    #isglobal #= False
    
    def __init__(self, varname):
        self.varname = varname
        self.listeners = []
        self.isglobal = False
        if not _UamFormat.IsUamVarname(varname):
            Error("Invalid Uamvar name: "+`varname`)
        self.isglobal = varname[0].isupper()  #The varname had to start with a letter.  If uppercase, it's global, else it's local.
        if self.isglobal:
            Error("Global uamvars are not supported yet; this uamvar should start with a lowercase letter to make it local: "+varname)
            
        
    def ListenTo(self, listener):
        self.listeners.append(weakref.ref(listener))
        
    def InitialSetting(self):
        prev = None
        next = uam.GetAgeChronicle(self.varname)
        self.NotifyListeners(prev,next)
        
    def SetVar(self, value):
        prev = uam.GetAgeChronicle(self.varname)
        next = value
        if prev!=next:  #only notify if changed
            uam.SetAgeChronicle(self.varname, next)
            self.NotifyListeners(prev,next)
        
    def NotifyListeners(self, prev, next):
        print "Notifying Listeners of: "+self.varname
        #DumpState()
        for weaklistener in self.listeners:
            listener = weaklistener()
            if listener==None:
                self.listeners.remove(weaklistener)
            else:
                try:
                    #listener(self.varname, prev, next)
                    listener.UamListenEvent(self.varname, prev, next)
                except:
                    traceback.print_exc()
        



#Listen to age linking to know when to reset vars
def OnServerInitComplete():
    #We're done loading the Age!
    print "_UamVars.OnServerInitComplete"
    
    #Send out the initial notifications
    for uamvar in vars:
        varobj = vars[uamvar]
        varobj.InitialSetting()
    
    #If there's errors, let the user know.
    if len(errors)>0:
        uam.PrintKiMessage("There are some problems with the Age; please fix them in 3dsmax:")
    for error in errors:
        uam.PrintKiMessage("  "+error)
_UamEvents.RegisterForOnServerInitComplete(OnServerInitComplete)

#def OnBehaviorNotify(type, id, state):
#    #print "_UamVars.OnBehaviorNotify"
#    if type==PlasmaConstants.PtBehaviorTypes.kBehaviorTypeLinkOut and state:
#        print "We're linking out..."
#        Reset()
#UamEvents.RegisterForOnBehaviorNotify(OnBehaviorNotify)



    