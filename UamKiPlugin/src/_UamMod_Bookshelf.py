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

#Stuff relating to customizing your bookshelf in a multi-user friendly manner.

import _UamEvents
import Plasma
import uam
import xLinkMgr
import _UamUtils


def UamOnKiCommand(command):
    print "UamModBookshelf.UamOnKiCommand: "+`command`
    if command.startswith("/"):
        parts = command.split()
        numparts = len(parts)
        if parts[0]=="/bookshelf":
            import Plasma
            import uam
            import _UamUtils
            vault = Plasma.ptVault()
            if not vault.inMyPersonalAge():
                #_ki.IAddRTChat(None, "You can only use /bookshelf from within your own Relto.", 0)
                uam.PrintKiMessage("You can only use /bookshelf from within your own Relto.")
                return True
            try:
                booknum = int(parts[1])
                agename = parts[2]
                agename = _UamUtils.GetCorrectFilename(agename)
            except:
                #_ki.IAddRTChat(None, "Usage: e.g. to set the 3rd book to Galamay:  /bookshelf 3 Galamay", 0)
                uam.PrintKiMessage("Usage: e.g. to set the 3rd book to Galamay:  /bookshelf 3 Galamay")
                return True
            if booknum < 1 or booknum > 18:
                #_ki.IAddRTChat(None, "The book number must be between 1 and 18.", 0)
                uam.PrintKiMessage("The book number must be between 1 and 18.")
                return True
            if not (_UamUtils._IsRestorationAge(agename) or agename=="default" or agename=="none" or agename=="empty"):
                #_ki.IAddRTChat(None, "The Age must be a fan Age.", 0)
                uam.PrintKiMessage("The Age must be a fan Age.")
                return True
            #add to list
            #import uam
            booksstr = uam.GetAgeChronicle("UamBooks")
            books = _UamUtils._StringToDict(booksstr)
            print "books: "+`books`
            books[str(booknum)] = agename
            #save list
            newbooksstr = _UamUtils._DictToString(books)
            uam.SetAgeChronicle("UamBooks",newbooksstr)
            #_ki.IAddRTChat(None, "The book for "+agename+" has been put on the shelf at position "+str(booknum)+"  (You won't see it until you relink though.)", 0)
            uam.PrintKiMessage("The book for "+agename+" has been put on the shelf at position "+str(booknum)+"  (You won't see it until you relink though.)")
            return True
            
_UamEvents.RegisterForKiCommand(UamOnKiCommand)


#Listen and set bookshelf values
print "UamEvents3 setting bookshelf..."
import xUserKI
if xUserKI.gUserKIVersion=="3.4":
    print "Detected ki 3.4"
    import xULM
    def _ParseULMFile(old_method = xULM.ParseULMFile):
        old_method() #Parses the ULMServerLinkBook.inf file and fills out xULM.ULMBooks and xULM.ULMData
        #ULMBooks looks like {"Link19":"Bookname",...} where Bookname could be the AgeFilename or just the tag used for the cover.
        #ULMData looks like {"Link19":[["AgeFilenameOrEmpty", "Desc ription","LinkInPoint","Optional Text","linkTypeWhichCanBeEmpty"], <<other pages>>]}
        #bookstr should look like "1=Aerie;2=EderRiltahInaltahv"
        #xLinkMgr.AvailableLinks looks like {"AgeFilename":["Age Name","dataserver","basic"],}
        #xLinkMgr.AvailableSpawnpoints looks like {"AgeFilename":{"LinkInPointDefault":"SpawnPointName",...},...}
        print "UamEvents3 ParseULMFile"
        import uam
        import xLinkMgr
        bookstr = uam.GetAgeChronicle("UamBooks") #Could be "" if not set before.
        print bookstr
        if bookstr != "": #Stupid Python behavior where "".split() returns [""] :P
            books = bookstr.split(";")
            print "books="+`books`
            xLinkMgr.ResetAvailableLinks()  #move this here, so we only do it once :P
            for book in books:
                print "book="+`book`
                parts = book.split("=")
                print "parts="+`parts`
                booknum = int(parts[0])
                agename = parts[1]
                #This isn't set yet, so we *could* call xLinkMgr.ResetAvailableLinks
                #xLinkMgr.ResetAvailableLinks()
                if agename=="none" or agename=="empty":
                    #Let's empty the entry
                    link = "Link"+str(booknum+18)
                    #xULM.ULMBooks.pop(link,None)  #set the default to None, so that no exception is thrown if it's not in the dict
                    #xULM.ULMData.pop(link,None)  #set the default to None, so that no exception is thrown if it's not in the dict
                    xULM.ULMBooks[link] = None
                    xULM.ULMData[link] = []
                elif agename=="default":
                    pass #we'll just skip it then and the default will be used.
                elif _UamUtils._IsRestorationAge(agename): 
                    #desc = xLinkMgr._AvailableLinks[agename][0]
                    desc = xLinkMgr._AvailableLinks[agename].displayName
                    link = "Link"+str(booknum+18)
                    xULM.ULMBooks[link] = agename
                    xULM.ULMData[link] = [[agename,desc,"LinkInPointDefault","",""]] #Should we grab a different LinkPoint or linktype?
                else:
                    print "Skipping "+agename
                    
    xULM.ParseULMFile = _ParseULMFile    
else:
    #Assuming this is 3.5+, which jumbled around the names
    print "assuming ki 3.5+"
    import xCustomReltoShelf
    def _ParseULMFile(old_method = xCustomReltoShelf.ParseULMFile):
        old_method() #Parses the ULMServerLinkBook.inf file and fills out xULM.ULMBooks and xULM.ULMData
        #ULMBooks looks like {"Link19":"Bookname",...} where Bookname could be the AgeFilename or just the tag used for the cover.
        #ULMData looks like {"Link19":[["AgeFilenameOrEmpty", "Desc ription","LinkInPoint","Optional Text","linkTypeWhichCanBeEmpty"], <<other pages>>]}
        #bookstr should look like "1=Aerie;2=EderRiltahInaltahv"
        #xLinkMgr.AvailableLinks looks like {"AgeFilename":["Age Name","dataserver","basic"],}
        #xLinkMgr.AvailableSpawnpoints looks like {"AgeFilename":{"LinkInPointDefault":"SpawnPointName",...},...}
        print "UamEvents3 ParseULMFile"
        import uam
        import xLinkMgr
        bookstr = uam.GetAgeChronicle("UamBooks") #Could be "" if not set before.
        print bookstr
        if bookstr != "": #Stupid Python behavior where "".split() returns [""] :P
            books = bookstr.split(";")
            print "books="+`books`
            xLinkMgr.ResetAvailableLinks()  #move this here, so we only do it once :P
            for book in books:
                print "book="+`book`
                parts = book.split("=")
                print "parts="+`parts`
                booknum = int(parts[0])
                agename = parts[1]
                #This isn't set yet, so we *could* call xLinkMgr.ResetAvailableLinks
                #xLinkMgr.ResetAvailableLinks()
                if agename=="none" or agename=="empty":
                    #Let's empty the entry
                    link = "Link"+str(booknum+18)
                    #xULM.ULMBooks.pop(link,None)  #set the default to None, so that no exception is thrown if it's not in the dict
                    #xULM.ULMData.pop(link,None)  #set the default to None, so that no exception is thrown if it's not in the dict
                    #xULM.ULMBooks[link] = None
                    #xULM.ULMData[link] = []
                    if link in xCustomReltoShelf._Books:
                        del xCustomReltoShelf._Books[link]
                elif agename=="default":
                    pass #we'll just skip it then and the default will be used.
                elif _UamUtils._IsRestorationAge(agename): 
                    #desc = xLinkMgr._AvailableLinks[agename][0]
                    desc = xLinkMgr._AvailableLinks[agename].displayName
                    link = "Link"+str(booknum+18)
                    #xULM.ULMBooks[link] = agename
                    #xULM.ULMData[link] = [[agename,desc,"LinkInPointDefault","",""]] #Should we grab a different LinkPoint or linktype?
                    book = xCustomReltoShelf._Book(booknum)
                    book.cover = agename
                    book.addPage(xCustomReltoShelf._LinkPage(agename,"LinkInPointDefault"))
                    xCustomReltoShelf._Books[link] = book
                else:
                    print "Skipping "+agename
                    
    xCustomReltoShelf.ParseULMFile = _ParseULMFile    
    

