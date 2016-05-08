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

import weakref
import traceback
import PlasmaConstants

#Registrations should be done in the __init__ of ptModifiers.
#These lists of listeners should be weakrefs, to make sure they can be garbage collected.

print "_UamEvents loading..."

#Load the KI's module...
try:
    import BlackBarBodyKIHandler; #This is the module that xKI.py is glued to.
except:
    import traceback
    traceback.print_exc()
    print "Exception is probably because we're in 3dsmax..."
    import xKI as BlackBarBodyKIHandler  #Just to keep 3dsmax happy!

__OnTimerListeners = []
def RegisterForOnTimer(listener):
    __OnTimerListeners.append(weakref.ref(listener))
def _OnTimer(self, id, old_method = BlackBarBodyKIHandler.xKI.OnTimer):
    #print "_UamEvents._OnTimer"
    for weaklistener in __OnTimerListeners:
        listener = weaklistener()  #this makes it non weak
        if listener==None:
            __OnTimerListeners.remove(weaklistener)
        else:
            try:
                listener(id)
            except:
                traceback.print_exc()
    old_method(self, id)
BlackBarBodyKIHandler.xKI.OnTimer = _OnTimer


__OnNotifyListeners = []
def RegisterForOnNotify(listener):
    print "UamEvents.RegisterForOnNotify: "+`listener`
    __OnNotifyListeners.append(weakref.ref(listener))
def _OnNotify(self, state, id, events, old_method = BlackBarBodyKIHandler.xKI.OnNotify):
    #print "xUserKIUamInterface._OnNotify"
    #if _UamEvents._OnNotify(state, id, events):
    #    return
    #print "xUserKIUamInterface._OnNotify 2"
    print "_UamEvents._OnNotify"
    handled = False
    for weaklistener in __OnNotifyListeners:
        listener = weaklistener()  #this makes it non weak
        if listener==None:
            __OnNotifyListeners.remove(weaklistener)
        else:
            try:
                #listener.UamOnNotify(state, id, events)
                if listener(state,id,events):
                    handled = True
            except:
                traceback.print_exc()
    if handled:
        return
    old_method(self, state, id, events)
BlackBarBodyKIHandler.xKI.OnNotify = _OnNotify


__ServerInitCompleteListeners = []
__Started = False
def RegisterForOnServerInitComplete(listener):
    #make sure it's a weakref
    print "UamEvents.RegisterForOnServerInitComplete: "+`listener`
    __ServerInitCompleteListeners.append(weakref.ref(listener))
def _OnServerInitComplete(self, old_method = BlackBarBodyKIHandler.xKI.OnServerInitComplete):
    #print "UamEvents3 newOnServerInitComplete"
    print "_UamEvents._OnServerInitComplete"
    global __Started
    if not __Started:
        __Started = True
        _OnFirstServerInitComplete()
    for weaklistener in __ServerInitCompleteListeners:
        listener = weaklistener()  #this makes it non weak
        if listener==None:
            __ServerInitCompleteListeners.remove(weaklistener)
        else:
            try:
                #listener.UamOnNewAgeLoaded(agename)
                #listener(agename)
                listener()
            except:
                traceback.print_exc()
    old_method(self)
    #_UamEvents._OnNewAgeLoaded(PtGetAgeName())
BlackBarBodyKIHandler.xKI.OnServerInitComplete = _OnServerInitComplete


#event is a PtVaultConstants.PtVaultNotifyTypes item.
__VaultNotifyListeners = []
def RegisterForVaultNotify(listener):
    print "UamEvents.RegisterForVaultNotify: "+`listener`
    __VaultNotifyListeners.append(weakref.ref(listener))
def _OnVaultNotify(self, event, tupdata, old_method = BlackBarBodyKIHandler.xKI.OnVaultNotify):
    #print "UamEvents3 newOnVaultNotify"
    print "_UamEvents._OnVaultNotify"
    for weaklistener in __VaultNotifyListeners:
        listener = weaklistener()
        if listener==None:
            __VaultNotifyListeners.remove(weaklistener)
        else:
            try:
                #listener.UamOnVaultNotify(event,tupdata)
                listener(event,tupdata)
            except:
                traceback.print_exc()
    old_method(self, event, tupdata)
    #_UamEvents._OnVaultNotify(event,tupdata)
BlackBarBodyKIHandler.xKI.OnVaultNotify = _OnVaultNotify


__VaultEventListeners = []
def RegisterForVaultEvent(listener):
    print "UamEvents.RegisterForVaultEvent: "+`listener`
    __VaultEventListeners.append(weakref.ref(listener))
def _OnVaultEvent(self, event, tupdata, old_method = BlackBarBodyKIHandler.xKI.OnVaultEvent):
    #print "_UamEvents._OnVaultEvent"
    for weaklistener in __VaultEventListeners:
        listener = weaklistener()
        if listener==None:
            __VaultEventListeners.remove(weaklistener)
        else:
            try:
                #listener.UamOnVaultEvent(event,tupdata)
                listener(event,tupdata)
            except:
                traceback.print_exc()
    #print "UamEvents3 newOnVaultEvent"
    old_method(self, event, tupdata)
    #_UamEvents._OnVaultEvent(event,tupdata)
BlackBarBodyKIHandler.xKI.OnVaultEvent = _OnVaultEvent


__AgeVaultEventListeners = []
def RegisterForAgeVaultEvent(listener):
    print "UamEvents.RegisterForAgeVaultEvent: "+`listener`
    __AgeVaultEventListeners.append(weakref.ref(listener))
def _OnAgeVaultEvent(self, event, tupdata, old_method = BlackBarBodyKIHandler.xKI.OnAgeVaultEvent):
    #print "UamEvents3 newOnAgeVaultEvent"
    #print "_UamEvents._OnAgeVaultEvent"
    for weaklistener in __AgeVaultEventListeners:
        listener = weaklistener()
        if listener==None:
            __AgeVaultEventListeners.remove(weaklistener)
        else:
            try:
                #listener.UamOnAgeVaultEvent(event,tupdata)
                listener(event,tupdata)
            except:
                traceback.print_exc()
    old_method(self, event, tupdata)
    #_UamEvents._OnAgeVaultEvent(event,tupdata)
BlackBarBodyKIHandler.xKI.OnAgeVaultEvent = _OnAgeVaultEvent


__KiCommandListeners = []
def RegisterForKiCommand(listener):
    print "UamEvents.RegisterForKiCommand: "+`listener`
    __KiCommandListeners.append(weakref.ref(listener))
#def _OnKiCommand(command):
def _ICheckChatCommands(self, chatmessage, silent = False, old_method = BlackBarBodyKIHandler.xKI.ICheckChatCommands):
    print "UamEvents._OnKiCommand"
    handled = False
    for weaklistener in __KiCommandListeners:
        listener = weaklistener()
        if listener==None:
            __KiCommandListeners.remove(weaklistener)
        else:
            try:
                #if listener.UamOnKiCommand(command):
                if listener(chatmessage):
                    handled = True
            except:
                traceback.print_exc()
    #return handled
    #print "UamEvents3 newICheckChatCommands"
    #if _UamEvents._OnKiCommand(chatmessage):
    #    return None  #hmm... this would allow ages to block ki commands, which is not the intention.  If it becomes a problem, we'll need a "/override /nexus" style command.
    if handled:
        return None
    #if _KICommand(chatmessage):
    #    return None
    return old_method(self, chatmessage, silent)
BlackBarBodyKIHandler.xKI.ICheckChatCommands = _ICheckChatCommands


#__OnInitListeners = []
#def RegisterForOnInit(listener):
#    __OnInitListeners.append(weakref.ref(listener))
#def _OnInit(self, old_method = BlackBarBodyKIHandler.xKI.OnInit):
#    print "_UamEvents._OnInit"
#    for weaklistener in __OnInitListeners:
#        listener = weaklistener()
#        if listener==None:
#            __OnInitListeners.remove(weaklistener)
#        else:
#            try:
#                listener()
#            except:
#                traceback.print_exc()
#    old_method(self)
#BlackBarBodyKIHandler.xKI.OnInit = _OnInit


__OnAvatarSpawnListeners = []
def RegisterForOnAvatarSpawn(listener):
    __OnAvatarSpawnListeners.append(weakref.ref(listener))
def _OnAvatarSpawn(self, null, old_method = BlackBarBodyKIHandler.xKI.OnAvatarSpawn):
    print "_UamEvents._OnAvatarSpawn"
    for weaklistener in __OnAvatarSpawnListeners:
        listener = weaklistener()
        if listener==None:
            __OnAvatarSpawnListeners.remove(weaklistener)
        else:
            try:
                listener(null)
            except:
                traceback.print_exc()
    old_method(self, null)
BlackBarBodyKIHandler.xKI.OnAvatarSpawn = _OnAvatarSpawn


#__OnFirstUpdateListeners = []
#def RegisterForOnFirstUpdate(listener):
#    __OnFirstUpdateListeners.append(weakref.ref(listener))
#def _OnFirstUpdate(self, old_method = BlackBarBodyKIHandler.xKI.OnFirstUpdate):
#    print "_UamEvents._OnFirstUpdate"
#    for weaklistener in __OnFirstUpdateListeners:
#        listener = weaklistener()
#        if listener==None:
#            __OnFirstUpdateListeners.remove(weaklistener)
#        else:
#            try:
#                listener()
#            except:
#                traceback.print_exc()
#    old_method(self)
#BlackBarBodyKIHandler.xKI.OnFirstUpdate = _OnFirstUpdate


__OnPageLoadListeners = []
def RegisterForOnPageLoad(listener):
    __OnPageLoadListeners.append(weakref.ref(listener))
def _OnPageLoad(self, what, room, old_method = BlackBarBodyKIHandler.xKI.OnPageLoad):
    print "_UamEvents._OnPageLoad what="+`what`+" room="+`room`
    for weaklistener in __OnPageLoadListeners:
        listener = weaklistener()
        if listener==None:
            __OnPageLoadListeners.remove(weaklistener)
        else:
            try:
                listener(what, room)
            except:
                traceback.print_exc()
    old_method(self, what, room)
BlackBarBodyKIHandler.xKI.OnPageLoad = _OnPageLoad

    
__OnBehaviorNotifyListeners = []
def RegisterForOnBehaviorNotify(listener):
    __OnBehaviorNotifyListeners.append(weakref.ref(listener))
def _OnBehaviorNotify(self, type, id, state, old_method = BlackBarBodyKIHandler.xKI.OnBehaviorNotify):
    #print "_UamEvents._OnBehaviorNotify type="+`type`+" id="+`id`+" state="+`state`
    for weaklistener in __OnBehaviorNotifyListeners:
        listener = weaklistener()
        if listener==None:
            __OnBehaviorNotifyListeners.remove(weaklistener)
        else:
            try:
                listener(type,id,state)
            except:
                traceback.print_exc()
    old_method(self, type, id, state)
BlackBarBodyKIHandler.xKI.OnBehaviorNotify = _OnBehaviorNotify


def Initialize():
    print "_UamEvents.Initialize"
    #Don't do anything here; it's all done when the module is loaded.
    
    #try:
    #    import BlackBarBodyKIHandler; #This is the module that xKI.py is glued to.
    #except:
    #    import traceback
    #    traceback.print_exc()
    #    print "Exception is probably because we're in 3dsmax..."
    #    return
    

#Listen for link-outs
def OnBehaviorNotify(type, id, state):
    #print "_UamVars.OnBehaviorNotify"
    if type==PlasmaConstants.PtBehaviorTypes.kBehaviorTypeLinkOut and state:
        print "We're linking out..."
        #Reset _UamVars
        import _UamVars
        _UamVars.Reset()
        #Reset Timers
        import _UamTimer
        _UamTimer.Reset()
RegisterForOnBehaviorNotify(OnBehaviorNotify)


def _OnFirstServerInitComplete():
    print "_UamEvents._OnFirstServerInitComplete"
    try:
        #import _UamMod_GameFixes
        #_UamMod_GameFixes.CheckMsgs()
        pass
    except:
        pass

    
    
# Ki events:
#    def OnInit(self):  #Called after *this* object is initialized
#    def OnControlKeyEvent(self, controlKey, activeFlag):
#    def OnServerInitComplete(self):  #Called after an Age is loaded.  All PythonFileMods should be loaded at this point, unless they are on an unpaged-in page.
#    def OnBehaviorNotify(self, type, id, state):
#    def OnAvatarSpawn(self, null):
#    def OnFirstUpdate(self):  #Called after *this* object is initialized some more (I think this might be at the same time as OnServerInitComplete, but OnServerInitComplete is passed to all listeners, and not just the object it applies to.)
#    def OnDefaultKeyCaught(self, ch, isDown, isShift, isCtrl, keycode):
#    def OnNotify(self, state, id, events):
#    def OnPageLoad(self, what, room):
#    def OnGUINotify(self, id, control, event):
#    def OnKIMsg(self, command, value):
#    def OnRTChat(self, player, message, flags):
#    def OnTimer(self, id):
#    def OnScreenCaptureDone(self, image):
#    def OnMemberUpdate(self):
#    def OnRemoteAvatarInfo(self, player):
#    def OnVaultNotify(self, event, tupdata):
#    def OnAgeVaultEvent(self, event, tupdata):
#    def OnVaultEvent(self, event, tupdata):
#    def OnCCRMsg(self, msgtype, message, ccrplayerid):
#    def OnMarkerMsg(self, msgType, gameMasterID, tupdata):

#Loading order:
#  First, OnPageLoad with what=2 is called for each page on the last Age (except BuiltIn and Textures, I guess)
#  Then it downloads the AgeVault
#  Then the __init__ methods of the PythonFileMods are called
#  Then OnInit methods of the PythonFileMods are called (The ptAttribs are available now)
#  Then OnPageLoad with what=1 is called for the new pages
#  Then the PlayerVault is updated to reflect the new current Age, I guess.
#  Then OnFirstUpdate methods of the PythonFileMods are called
#  Then OnServerInitComplete is called for all listeners
#  Then OnAvatarSpawn is called for all listeners
#  (When a page is loaded dynamically, it also gets an OnPageLoad with what=1, but I suppose that when it gets it depends on when the page is loaded.  I imagine that any pfms in the page get their OnInit and OnFirstUpdates called before and after the OnPageLoad, just like normal.)





