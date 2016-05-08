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

#All the public functions here should be implementable on all Uru versions.
import Plasma
import PlasmaConstants
import PlasmaKITypes

#global stuff
global _ki

#book stuff
_book = None
_agename = None
_spawnpoint = None
_islinkbook = None
#_isourbook = False

def LinkToAge(agename, spawnpoint):
    #Use the OfflineKI for Pots/Alcugs:
    import xLinkMgr
    xLinkMgr.LinkToAge(agename,spawnpoint)

def SetAgeChronicle(varname, value):
    print "SetAgeChronicle name=" + `varname` + " value="+`value`
    ageVault = Plasma.ptAgeVault()
    _SetChronicle(varname,value,ageVault)

def SetAgeSdl(varname, value, index=0):
    #index could default to 0
    sdl = Plasma.PtGetAgeSDL()
    sdl.setFlags(varname, 1, 1)
    sdl.sendToClients(varname)
    sdl.setIndex(varname, index, value)


def GetAgeSdl(varname, index=0):
    #index could default to 0
    sdl = Plasma.PtGetAgeSDL()
    return sdl[varname][index]

    
def _SetChronicle(varname, value, vault, type=1):
    chronnode = vault.findChronicleEntry(varname)
    print "cronnode:"+`chronnode`
    if chronnode==None:
        #create it
        vault.addChronicleEntry(varname,1,value) #type 1 seems to be stuff that *might* be for other players.  I think it might be ignored.
    else:
        chronnode.chronicleSetValue(value)
        chronnode.save()

def _SetPlayerChronicle(varname, value, type=1):
    print "SetPlayerChronicle name=" + `varname` + " value="+`value`
    vault = Plasma.ptVault()
    _SetChronicle(varname,value,vault,type)

def GetAgeChronicle(varname):
    print "GetAgeChronicle name=" + `varname`
    ageVault = Plasma.ptAgeVault()
    return _GetChronicle(varname,ageVault)
    
def _GetChronicle(varname, vault):
    chronnode = vault.findChronicleEntry(varname)
    print "cronnode:"+`chronnode`
    if chronnode==None:
        print "chronnode not found: "+`varname`
        #return None
        return ""
    else:
        value = chronnode.chronicleGetValue()
        print "chronnode value: "+`value`
        return value

def _GetPlayerChronicle(varname):        
    print "GetPlayerChronicle name=" + `varname`
    vault = Plasma.ptVault()
    return _GetChronicle(varname,vault)


def PrintKiMessage(msg):
    _ki.IAddRTChat(None, msg, 0)

def SetTimer(callback, time):
    import _UamTimer
    _UamTimer.Timer(callback, time, False, True) #isweak=False, so that we can use local functions, and removewhenlink=True, so that timers are cancelled when linking out.

def DisplayJournal(text, isOpen):
    global _islinkbook
    _islinkbook = False
    _DisplayBook(text, isOpen, "bkNotebook")

def DisplayBook(text, isOpen):
    global _islinkbook
    _islinkbook = False
    _DisplayBook(text, isOpen, "bkBook")
    

def _DisplayBook(text, isOpen, booktype):
    #cover can be None(detect if theres a cover), True, or False
    #booktype can be bkNotebook, bkBook, or bkBahroRockBook
    global _book
    #global _isourbook
    _book = Plasma.ptBook(text, _ki.key)
    #if cover==None:
    #    cover = text.find('<cover') >= 0  #showOpen if cover not found
    _book.setSize(1.0, 1.0)
    _book.setGUI(booktype)
    _book.allowPageTurning(True)
    #_isourbook = True
    #_book.show(not cover)
    _book.show(isOpen)

#class _AgeBook:
#    agename = None
#    spawnpoint = None
#    def __init__(self, agename, spawnpoint):
#        self.agename = agename
#        self.spawnpoint = spawnpoint
#    def OnNotify(self, state, id, events):
def _OnNotify(state, id, events):
    global _book
    print "uam._OnNotify"
    for event in events:
        if (event[0] == PlasmaConstants.PtEventType.kBook):
            print "event: "+`event`
            print "event2: "+`event[2]`
            if (event[1] == PlasmaConstants.PtBookEventTypes.kNotifyImageLink):
                #In this case, event[2] is the link# given in the <img> tag.
                #if ((self.curBookAge != None) and (self.curBookSpawnpoint != None)):
                #    if (not self.showFlybys):
                #        self.book.hide()
                #        self.linkToAge(self.curBookAge, self.curBookSpawnpoint)
                #        self.curBookAge = None
                #        self.curBookSpawnpoint = None
                #       self.inMovie = false
                #        if self.movie:
                #            self.movie.stop()
                #            self.movie = None
                #    else:
                #        self.book.hide()
                #        PtSetGlobalClickability(false)
                #        PtGUICursorDimmed()
                #        PtDisableRenderScene()
                #        self.inMovie = true
                #        PtAtTimeCallback(self.callersKey, self.movieReplayDelay, self.kDustinMovie)
                if event[2]==100:
                    #after this, the .kNotifyHide event will still be called, so the _book = None can be done then.
                    print "uam._OnNotify: linking"
                    _book.hide()
                    LinkToAge(_agename,_spawnpoint)
                    return True #don't let xKI.py process the rest!
            elif (event[1] == PlasmaConstants.PtBookEventTypes.kNotifyShow):
                if _book != None:
                    #_book is set, so this is our book!
                    if _islinkbook==True:
                        import booksDustGlobal
                        bookmap = booksDustGlobal.BookMapRight
                        import xLinkMgr
                        img = xLinkMgr.GetLinkingImage(_agename, _spawnpoint, width=410, height=168)
                        bookmap.textmap.drawImage(50, 60, img, 0)
            elif (event[1] == PlasmaConstants.PtBookEventTypes.kNotifyHide):
                #self.output('notifyhide')
                #self.book = None
                print "uam._OnNotify: hiding book"
                _book = None
import _UamEvents
_UamEvents.RegisterForOnNotify(_OnNotify)

    
#def DisplayLinkingBook(self, agename, keyToUse, cover = None, linkingImage = None, showText = false, showFlyby = false):
def DisplayLinkingBook(agename, spawnpoint):
    #self.showFlybys = showFlyby
    #self.curBookAge = None
    #self.curBookSpawnpoint = None
    #contents = ''
    #if (cover != None):
    #    contents = (((contents + '<cover src="') + cover) + '">')
    #if self.isAgeInstalled(agename):
    #    self.curBookAge = agename
    #    self.curBookSpawnpoint = self.getSpawnpoint(agename)
    #if ((self.curBookAge == None) or ((self.curBookSpawnpoint == None) or ((self.curBookAge == '') or (self.curBookSpawnpoint == '')))):
    #    self.curBookAge = None
    #    self.curBookSpawnpoint = None
    #    contents = (contents + '<font size=28 face=Uru ><p align=center >This Age is not installed.\n\n')
    #    if showText:
    #        contents = (((contents + self.getDescription(agename)) + '\n\n<font size=24 face=Uru>') + self.getText(agename))
    #    if (linkingImage != None):
    #        contents = (((contents + '<pb><img src="') + linkingImage) + '" align=center blend=alpha >')
    #    else:
    #        contents = (contents + '<pb><img src="xLinkPanelBlackVoid*1#0.hsm" align=center blend=alpha >')
    #else:
    #    contents = (contents + '<font size=28 face=Uru ><p align=center >')
    #    if showText:
    #        contents = (((contents + self.getDescription(agename)) + '\n\n<font size=24 face=Uru>') + self.getText(agename))
    #    if (linkingImage != None):
    #        contents = (((contents + '<pb><img src="') + linkingImage) + '" align=center link=100 blend=alpha >')
    #    else:
    #        contents = (contents + '<pb><img src="xLinkPanelBlackVoid*1#0.hsm" align=center link=100 blend=alpha >')
    #self.output('Dustin: UAM: ', contents)
    #self.callersKey = keyToUse
    
    global _book
    global _agename
    global _spawnpoint
    global _islinkbook
    _islinkbook = True
    #global _isourbook
    _agename = agename
    _spawnpoint = spawnpoint
    contents = '<pb><img src="xLinkPanelBlackVoid*1#0.hsm" align=center link=100 blend=alpha >'
    #bookobj = _AgeBook(agename,spawnpoint)
    #_book = Plasma.ptBook(contents, self.callersKey)
    _book = Plasma.ptBook(contents, _ki.key)
    _book.setSize(1.0, 1.0)
    _book.setGUI('BkBook')
    _book.allowPageTurning(True)
    #if (cover == None):
    #    self.book.show(1)
    #else:
    #    self.book.show(0)
    #_isourbook = True
    _book.show(1)
    #self.output('uam10g')


def EnableReltoPage(pagename):
    print "uam.EnableReltoPage: "+`pagename`
    import _UamUtils
    #get current task list
    tasksstr = _GetPlayerChronicle("UamTasks")
    tasks = _UamUtils._StringToList(tasksstr)
    #add item
    tasks.append("EnableReltoPage="+pagename)
    #save task list
    taskstr = _UamUtils._ListToString(tasks)
    _SetPlayerChronicle("UamTasks",taskstr)
    Plasma.PtSendKIMessageInt(PlasmaKITypes.kStartBookAlert, 0)  #Flash the Relto book.
    print "current tasks: "+taskstr
    
    