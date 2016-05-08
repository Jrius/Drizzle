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

#Add fan made Relto pages to the Relto book
#  Each one should consist of a single prp
#  And there should be a text configuration file, in /ageresources, like UamRelto--PageName.txt
#    That config file should say what the starting state is (on, off, unattained), but on can't be the default so pretend it's off
#    And it should have some text about the page, and the page number

#import xLinkingBookDefs

import _UamEvents
import PlasmaConstants
import os
import _UamUtils
import uam

ReltoPages = None #{}

haski = False
try:
    import BlackBarBodyKIHandler
    haski = True
except:
    import traceback
    traceback.print_exc()
    print "Exception is probably because we're in 3dsmax..."
    import xKI as BlackBarBodyKIHandler


#Listen to page defs for the Relto book
def _IGetYeeshaPageDefs(self, old_method = BlackBarBodyKIHandler.xKI.IGetYeeshaPageDefs):
    print "_UamModReltopages._IGetYeeshaPageDefs"
    result = old_method(self)
    
    #Can only change this while in your Relto
    import Plasma
    vault = Plasma.ptVault()
    if not vault.inMyPersonalAge():
        result += "<pb><pb><font size=20><p align=center>You can only change the fan-made pages while on your Relto."
        return result

    #Get the current status of the pages
    import uam
    chronstr = uam.GetAgeChronicle("UamReltoPages") #on, off, or unattained
    pages = _UamUtils._StringToDict(chronstr)
    

    for page in ReltoPages:
        pagedict = ReltoPages[page]
        pagenum = int(pagedict["pagenum"])
        lang = _UamUtils.GetLanguage()
        print "language: "+`lang`
        linktext = pagedict["text--"+lang]
        linknum = pagenum + 200  #just to get it out of Cyan's hair
        turnedon = 1  #1 or 0
        status = pages.get(page,pagedict["default"])  #Get the status or use the default for this page.
        print "status: "+status
        if status=="on":
            turnedon = 1
        else:
            turnedon = 0  #either off or unattained
        if status=="on" or status=="off":
            result += '<pb><font size=20><p align=center>'+linktext+'<pb><img src="xYeeshaPageAlphaSketchFiremarbles*1#0.hsm" align=center check=00ff18,00800c,'+str(turnedon)+' link='+str(linknum)+'>'
    return result
BlackBarBodyKIHandler.xKI.IGetYeeshaPageDefs = _IGetYeeshaPageDefs


#Listen to click event
def _OnNotify(state, id, events):
    global _book
    print "_UamModReltopages._OnNotify"
    for event in events:
        if event[0]==PlasmaConstants.PtEventType.kBook:
            print "event: "+`event`
            if event[1]==PlasmaConstants.PtBookEventTypes.kNotifyImageLink:
                pagenum = event[2] - 200
                print "page on pagenum: "+`pagenum`
                if TogglePage(pagenum):
                    print "Page clicked on!"
                    return True
            elif event[1]==PlasmaConstants.PtBookEventTypes.kNotifyCheckUnchecked:
                #event[2] is the link number, which go from 3 to 26 for the Cyan ones, including converted ones.
                pagenum = event[2] - 200
                print "page off pagenum: "+`pagenum`
                if TogglePage(pagenum):
                    print "Page clicked off!"
                    return True
_UamEvents.RegisterForOnNotify(_OnNotify)

def TogglePage(pagenum):
    #Get page for this pagenum
    pagedict = FindPage(pagenum)
    if pagedict==None:
        return False
    pagename = pagedict["pagename"]

    #Get the current status of the pages
    import uam
    chronstr = uam.GetAgeChronicle("UamReltoPages") #on, off, or unattained
    pages = _UamUtils._StringToDict(chronstr)
    print "chronstr: "+chronstr
    
    status = pages.get(pagename,pagedict["default"])  #Get the status or use the default for this page.
    if status=="on":
        status = "off"
    elif status=="off":
        status = "on"
    else:
        raise "Unexpected page state"
    
    #save the new status
    pages[pagename] = status
    pagesstr = _UamUtils._DictToString(pages)
    uam.SetAgeChronicle("UamReltoPages",pagesstr)
    print "chronstr: "+pagesstr
    return True
    
def FindPage(pagenum):
    for page in ReltoPages:
        pagedict = ReltoPages[page]
        curpagenum = int(pagedict["pagenum"])
        if pagenum==curpagenum:
            return pagedict
    return None

#Read definitions from /img/UamRelto folder?
def ReadPageInfo():
    global ReltoPages
    ReltoPages = {}
    try:
        print "_UamModReltopages reading page info"
        if os.path.exists("img/UamRelto/"):
            files = os.listdir("img/UamRelto/")
        else:
            files = []
        for filename in files:
            if filename.startswith("UamRelto--") and filename.endswith(".txt"):
                try:
                    pagename = filename[len("UamRelto--"):len(filename)-len(".txt")] #get the inside part
                    print "pagename: "+pagename
                    f = file("img/UamRelto/"+filename,"r")
                    contents = f.read()
                    f.close()
                    pagedict = _UamUtils._StringToDict(contents)
                    pagenum = int(pagedict["pagenum"])
                    if pagenum<100:
                        raise "pagenum must be over 100."
                    pagestate = pagedict["default"]
                    if pagestate!="off" and pagestate!="unattained":
                        raise "default must be either 'off' or 'unattained'"
                    en = pagedict.get("text--en")
                    de = pagedict.get("text--de")
                    fr = pagedict.get("text--fr")
                    hidetxt = pagedict.get("hide") #looks like Object1,Object2,Object3
                    hideitems = []
                    if hidetxt!=None:
                        for hideitem in hidetxt.split(","):
                            hideitem = hideitem.strip()
                            if hideitem!="":
                                hideitems.append(hideitem)
                    #default text is English if we got it, otherwise just the pagename.
                    if en!=None:
                        dt = en
                    else:
                        dt = pagename
                    if en==None:
                        en = dt
                    if de==None:
                        de = dt
                    if fr==None:
                        fr = dt
                    pagedict["text--en"] = en
                    pagedict["text--de"] = de
                    pagedict["text--fr"] = fr
                    pagedict["pagename"] = pagename
                    pagedict["hide"] = hideitems
                    ReltoPages[pagename] = pagedict
                except:
                    import traceback
                    traceback.print_exc()
    except:
        import traceback
        traceback.print_exc()

#Read the page info on startup, but we could change this to be when linking to Relto.
#ReadPageInfo()
        
#Listen for link-in to a Relto, so we can load the appropriate pages
def LoadReltoPages():
    print "_UamModReltopages.UamOnNewAgeLoaded"
    import _UamUtils
    import uam
    import Plasma
    if _UamUtils.GetAgeName()=="Personal":
    
        #Read any updated pages
        ReadPageInfo()
    
        #Do tasks given from other Ages
        if _UamUtils.AmInMyRelto():
            tasksstr = uam._GetPlayerChronicle("UamTasks")
            tasks = _UamUtils._StringToList(tasksstr)
            numtasks = len(tasks)
            for task in tasks:
                print "task to do: "+task
                if task.startswith("EnableReltoPage="):
                    page = task[len("EnableReltoPage="):]
                    #enable the page
                    pages = _UamUtils._StringToDict(uam.GetAgeChronicle("UamReltoPages"))
                    pages[page] = "on"  #whether it was unset or on or off or unattained, it is on now!
                    uam.SetAgeChronicle("UamReltoPages",_UamUtils._DictToString(pages))
                    #remove from task list
                    tasks.remove(task)
            if numtasks!=len(tasks):
                #removed some, so save
                uam._SetPlayerChronicle("UamTasks",_UamUtils._ListToString(tasks))
        
        #Load pages
        PagesToLoad = {} #set() #Sets don't exist in Python 2.2 :P
        ObjectsToHide = {} #set() #Sets don't exist in Python 2.2 :P
        print "Loading Uam pages..."
        chronstr = uam.GetAgeChronicle("UamReltoPages") #on, off, or unattained
        pages = _UamUtils._StringToDict(chronstr)
        print "UamReltoPages: "+chronstr
        for pagename in pages:
            status = pages[pagename]
            if status=="on":
                #PagesToLoad.add(pagename)
                PagesToLoad[pagename] = None #we're using this dict as a set
                for hideitem in ReltoPages[pagename]["hide"]:
                    #ObjectsToHide.add(hideitem)
                    ObjectsToHide[hideitem] = None #we're using this dict as a set
        #Turn into sorted lists
        PagesToLoad = PagesToLoad.keys()
        ObjectsToHide = ObjectsToHide.keys()
        PagesToLoad.sort()
        ObjectsToHide.sort()
        #Hide the objects
        for hideitem in ObjectsToHide:
            print "Hiding obj: "+hideitem
            _UamUtils.HideObject(hideitem)
        #Load the pages
        for pagename in PagesToLoad:
            try:
                print "Loading page: "+pagename
                Plasma.PtPageInNode("UamPage-"+pagename)  #doesn't throw an exception if page not present; simply doesn't load.
            except:
                import traceback
                traceback.print_exc()
        
            
def UamOnNewAgeLoaded():
    #LoadReltoPages()
    uam.SetTimer(LoadReltoPages,0.01)
_UamEvents.RegisterForOnServerInitComplete(UamOnNewAgeLoaded)

