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

#import string
#from Plasma import *
#from PlasmaTypes import *
#from PlasmaKITypes import *
#from PlasmaVaultConstants import *
#from PlasmaNetConstants import *
#from xPsnlVaultSDL import *
#import xLinkMgr
#import os
#import PlasmaNetConstants

#import _UamEvents

print "xUserKIUamInterface loaded"
#import UamEvents2


#global _initialized
#global _ki
_initialized = False

def _Initialize(ki2):
    global _initialized
    #global _ki
    if not _initialized:
        _initialized = True
        print "xUserKIUamInterface._Initialize"

        #_ki = ki2
        #import uam
        #uam._ki = ki2
        #print "xUserKIUamInterface: registering for events"
        #try:
        #    import BlackBarBodyKIHandler; #This is the module that xKI.py is glued to.
        #except:
        #    import traceback
        #    traceback.print_exc()
        #    print "Exception is probably because we're in 3dsmax..."
        #    return
            
        import uam
        uam._ki = ki2
        
        #print dir(BlackBarBodyKIHandler)
        #print dir(BlackBarBodyKIHandler.xKI)
        #def _OnVaultNotify(self, event, tupdata, old_method = BlackBarBodyKIHandler.xKI.OnVaultNotify):
        #    #print "UamEvents3 newOnVaultNotify"
        #    old_method(self, event, tupdata)
        #    _UamEvents._OnVaultNotify(event,tupdata)
        #def _OnVaultEvent(self, event, tupdata, old_method = BlackBarBodyKIHandler.xKI.OnVaultEvent):
        #    #print "UamEvents3 newOnVaultEvent"
        #    old_method(self, event, tupdata)
        #    _UamEvents._OnVaultEvent(event,tupdata)
        #def _OnAgeVaultEvent(self, event, tupdata, old_method = BlackBarBodyKIHandler.xKI.OnAgeVaultEvent):
        #    #print "UamEvents3 newOnAgeVaultEvent"
        #    old_method(self, event, tupdata)
        #    _UamEvents._OnAgeVaultEvent(event,tupdata)
        #def _OnServerInitComplete(self, old_method = BlackBarBodyKIHandler.xKI.OnServerInitComplete):
        #    #print "UamEvents3 newOnServerInitComplete"
        #    old_method(self)
        #    _UamEvents._OnNewAgeLoaded(PtGetAgeName())
        #def _OnNotify(self, state, id, events, old_method = BlackBarBodyKIHandler.xKI.OnNotify):
        #    print "xUserKIUamInterface._OnNotify"
        #   if _UamEvents._OnNotify(state, id, events):
        #        return
        #    print "xUserKIUamInterface._OnNotify 2"
        #    old_method(self, state, id, events)
        #def _ICheckChatCommands(self, chatmessage, silent = false, old_method = BlackBarBodyKIHandler.xKI.ICheckChatCommands):
        #    #print "UamEvents3 newICheckChatCommands"
        #    if _UamEvents._OnKiCommand(chatmessage):
        #        return None  #hmm... this would allow ages to block ki commands, which is not the intention.  If it becomes a problem, we'll need a "/override /nexus" style command.
        #    #if _KICommand(chatmessage):
        #    #    return None
        #    return old_method(self, chatmessage, silent)
        #One can't intercept __get__, __set__, and __delete__ as they are private.
        #Class method change
        #xKI.xKI.OnVaultEvent = _OnVaultEvent  #replace
        #xKI.xKI.OnVaultNotify = _OnVaultNotify
        #xKI.xKI.OnAgeVaultEvent = _OnAgeVaultEvent
        #print "UamEvents3 hi..."
        #import BlackBarBodyKIHandler; #This is the module that xKI.py is glued to.
        #BlackBarBodyKIHandler.xKI.OnVaultNotify = _OnVaultNotify
        #BlackBarBodyKIHandler.xKI.OnVaultEvent = _OnVaultEvent
        #BlackBarBodyKIHandler.xKI.OnAgeVaultEvent = _OnAgeVaultEvent
        #BlackBarBodyKIHandler.xKI.OnServerInitComplete = _OnServerInitComplete
        #BlackBarBodyKIHandler.xKI.OnNotify = _OnNotify
        #BlackBarBodyKIHandler.xKI.ICheckChatCommands = _ICheckChatCommands
        #BlackBarBodyKIHandler.__get__ = _get
        #BlackBarBodyKIHandler.__set__ = _set
        #BlackBarBodyKIHandler.__delete__ = _delete
        import _UamEvents #probably already got done when setting the KI above!
        #_UamEvents.Initialize()
        
        #load all the modules
        import _UamModules
        _UamModules.Initialize()
        
#        print "UamEvents3 setting bookshelf..."
#        import xULM
#        def _ParseULMFile(old_method = xULM.ParseULMFile):
#            old_method() #Parses the ULMServerLinkBook.inf file and fills out xULM.ULMBooks and xULM.ULMData
#            #ULMBooks looks like {"Link19":"Bookname",...} where Bookname could be the AgeFilename or just the tag used for the cover.
#            #ULMData looks like {"Link19":[["AgeFilenameOrEmpty", "Desc ription","LinkInPoint","Optional Text","linkTypeWhichCanBeEmpty"], <<other pages>>]}
#            #bookstr should look like "1=Aerie;2=EderRiltahInaltahv"
#            #xLinkMgr.AvailableLinks looks like {"AgeFilename":["Age Name","dataserver","basic"],}
#            #xLinkMgr.AvailableSpawnpoints looks like {"AgeFilename":{"LinkInPointDefault":"SpawnPointName",...},...}
#            print "UamEvents3 ParseULMFile"
#            import uam
#            import xLinkMgr
#            bookstr = uam.GetAgeChronicle("UamBooks") #Could be "" if not set before.
#            print bookstr
#            if bookstr != "": #Stupid Python behavior where "".split() returns [""] :P
#                books = bookstr.split(";")
#                print "books="+`books`
#                for book in books:
#                    print "book="+`book`
#                    parts = book.split("=")
#                    print "parts="+`parts`
#                    booknum = int(parts[0])
#                    agename = parts[1]
#                    #This isn't set yet, so we *could* call xLinkMgr.ResetAvailableLinks
#                    xLinkMgr.ResetAvailableLinks()
#                    if _IsRestorationAge(agename): 
#                        #desc = xLinkMgr._AvailableLinks[agename][0]
#                       desc = xLinkMgr._AvailableLinks[agename].displayName
#                        link = "Link"+str(booknum+18)
#                        xULM.ULMBooks[link] = agename
#                        xULM.ULMData[link] = [[agename,desc,"LinkInPointDefault","",""]] #Should we grab a different LinkPoint or linktype?
#                    else:
#                        print "Skipping "+agename
#                        
#        xULM.ParseULMFile = _ParseULMFile    

#def _KICommand(chatmessage): 
#    import uam
#    if chatmessage.startswith("/"):
#        parts = chatmessage.split()
#        numparts = len(parts)
#        if parts[0]=="/bookshelf":
#            import _UamUtils
#            vault = ptVault()
#            if not vault.inMyPersonalAge():
#                #_ki.IAddRTChat(None, "You can only use /bookshelf from within your own Relto.", 0)
#                uam.PrintKiMessage("You can only use /bookshelf from within your own Relto.")
#                return True
#            try:
#                booknum = int(parts[1])
#                agename = parts[2]
#                agename = _UamUtils.GetCorrectFilename(agename)
#            except:
#                #_ki.IAddRTChat(None, "Usage: e.g. to set the 3rd book to Galamay:  /bookshelf 3 Galamay", 0)
#                uam.PrintKiMessage("Usage: e.g. to set the 3rd book to Galamay:  /bookshelf 3 Galamay")
#                return True
#            if booknum < 1 or booknum > 18:
#                #_ki.IAddRTChat(None, "The book number must be between 1 and 18.", 0)
#                uam.PrintKiMessage("The book number must be between 1 and 18.")
#                return True
#            if not _IsRestorationAge(agename):
#                #_ki.IAddRTChat(None, "The Age must be a fan Age.", 0)
#                uam.PrintKiMessage("The Age must be a fan Age.")
#                return True
#            #add to list
#            import uam
#            booksstr = uam.GetAgeChronicle("UamBooks")
#            books = _StringToDict(booksstr)
#            print "books: "+`books`
#            books[str(booknum)] = agename
#            #save list
#            newbooksstr = _DictToString(books)
#            uam.SetAgeChronicle("UamBooks",newbooksstr)
#            #_ki.IAddRTChat(None, "The book for "+agename+" has been put on the shelf at position "+str(booknum)+"  (You won't see it until you relink though.)", 0)
#            uam.PrintKiMessage("The book for "+agename+" has been put on the shelf at position "+str(booknum)+"  (You won't see it until you relink though.)")
#            return True
            
            
        
#For dealing with String->String dictionaries (musn't contain ; nor = )
#def _DictToString(dict):
#    str_list = []
#    first = True
#    for key in dict:
#        val = dict[key]
#        if not first:
#            str_list.append(";")
#        str_list.append(key+"="+val)
#        first = False
#    result = ''.join(str_list)
#    return result
#def _StringToDict(str):
#    result = {}
#    if str=="":
#        return result
#    list = str.split(";")
#    for item in list:
#       parts = item.split("=")
#        result[parts[0]] = parts[1]
#   return result
#        
#        
#def _IsRestorationAge(agename):
#    #if agename=="MarshScene":
#    #    return True
#    #if agename=="MountainScene":
#    #    return True
#    import xLinkMgr
#    for age in xLinkMgr._RestorationLinks:
#        curagename = age[0]
#        if curagename==agename:
#            return True
#    return False

#Called by Offline-KI, and is the main entrance point.
def OnEarlyInit(ki):
    _Initialize(ki)

