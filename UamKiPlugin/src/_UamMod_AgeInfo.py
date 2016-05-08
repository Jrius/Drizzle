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
import os

_AgeInfo = None

def _ReadAgeInfo():
    try:
        #Let's read the text files into _AgeInfo
        global _AgeInfo
        _AgeInfo = {}
        
        if os.path.exists("img/UamAgeInfo/"):
            files = os.listdir("img/UamAgeInfo/")
        else:
            files = []
        for filename in files:
            if filename.startswith("UamAgeInfo--") and filename.endswith(".txt"):
                try:
                    agename = filename[len("UamAgeInfo--"):len(filename)-len(".txt")] #get the inside part
                    print "agename: "+agename
                    f = file("img/UamAgeInfo/"+filename,"r")
                    contents = f.read()
                    f.close()
                    
                    agedict = _UamUtils._StringToDict(contents)
                    
                    if "fullagename" not in agedict: #set the full agename to the filename if we don't have it.
                        agedict["fullagename"] = agename
                    if "info" not in agedict:
                        agedict["info"] = ""
                    hidden = agedict.get("hidden","false")
                    if hidden == "true":
                        agedict["hidden"] = True
                    else:
                        agedict["hidden"] = False
                    if "defaultspawnpoint" not in agedict:
                        agedict["defaultspawnpoint"] = "LinkInPointDefault"

                    #read the linktype, which can be linktype=basic; or linktype=original; or linktype=subage:parentagename;
                    if "linkingrule" not in agedict:
                        agedict["linkingrule"] = "basic"
                    linkingrule = agedict["linkingrule"]
                    if linkingrule in ("basic","original"):
                        agedict["parentage"] = ""
                    else:
                        if linkingrule.startswith("subage:"):
                            agedict["linkingrule"] = "subage"
                            agedict["parentage"] = linkingrule[7:]
                        else:
                            raise "Unhandled linkingrule."

                    #read the spawnpoints, listed as spawnpoints=SpawnPointName1:SpawnPointTitle1,SpawnPointName2,SpawnPointTitle2;
                    spawnpoints_str = agedict.get("spawnpoints","")
                    spawnpoints = {}
                    for spstr in spawnpoints_str.split(","):
                        spstr = spstr.strip()
                        if spstr != "":
                            spstrparts = spstr.split(":")
                            name = spstrparts[0].strip()
                            title = spstrparts[1].strip()
                            spawnpoints[name] = title
                    agedict["spawnpoints"] = spawnpoints
                        
                        
                    _AgeInfo[agename] = agedict
                
                except:
                    import traceback
                    traceback.print_exc()
    except:
        import traceback
        traceback.print_exc()

def _AddAgeInfo():
    import xLinkMgr
    for agename in _AgeInfo:
        agedict = _AgeInfo[agename]
        #if agedict["hidden"]:
        #    linktype = "link"
        #else:
        #    linktype = "restorationlink"
        fullagename = agedict["fullagename"]
        linkrule = agedict["linkingrule"]
        if linkrule == "subage":
            linkrule = "subageof("+agedict["parentage"]+")"
        defspawnpoint = agedict["defaultspawnpoint"]
        age = xLinkMgr._Age(fullagename,"dataserver",linkrule,defspawnpoint)
        age.description = agedict["info"]
        spawnpoints = agedict["spawnpoints"]
        for spname in spawnpoints:
            age.spawnpoints[spname] = spawnpoints[spname]
        if agedict["hidden"]:
            age.linktype = "hidden"
        else:
            age.linktype = "public"
            
        #Add in xLinkMgr:
        #xLinkMgr._AddAge(linktype,agename,age)
        xLinkMgr._AvailableLinks[agename] = age
        #if not agedict["hidden"]:
        #    xLinkMgr._RestorationLinks.append([agename, fullagename])
            
def _SortRestorationLinks():
    print "_UamMod_AgeInfo sorting..."
    import xLinkMgr
    def cmp2(a,b):
        return cmp(a[1].lower(),b[1].lower())  #we're comparing on the proper age name
    #xLinkMgr._RestorationLinks.sort(cmp2)
    
    #remove dupes
    #nodupes = []
    #lastitem = ""
    #for item in xLinkMgr._RestorationLinks:
    #    if item[1] != lastitem:
    #        nodupes.append(item)
    #    lastitem = item[1]
    #xLinkMgr._RestorationLinks = nodupes
    
    #Create RestorationLinks from scratch
    for item in xLinkMgr._RestorationLinks:
        age = xLinkMgr._AvailableLinks[item[0]]
        if not hasattr(age,"linktype"): #only set it if it isn't already.
            age.linktype = "public"
    
    #newrest = {}
    newrest = []
    for agename in xLinkMgr._AvailableLinks:
        age = xLinkMgr._AvailableLinks[agename]
        print "checking %s %s"%(agename,age.displayName)
        if hasattr(age,"linktype") and age.linktype=="public":
            #newrest[agename] = age.displayName
            newrest.append( (agename,age.displayName) )
            print "adding."
    newrest.sort(cmp2)
    
    xLinkMgr._RestorationLinks = newrest
            



import xUserKI
if xUserKI.gUserKIVersion=="3.4":
    print "Detected ki 3.4"
    raise "Error: _UamMod_AgeInfo, only supports offline-ki 3.5 or newer"
else:
    #Assuming this is 3.5+, which jumbled around the names
    print "assuming ki 3.5+"
    import xLinkMgr
    def _New_LoadAvailableLinks(old_method = xLinkMgr._LoadAvailableLinks):
        import xLinkMgr
        needsrefresh = (len(xLinkMgr._AvailableLinks) == 0)
        old_method() #Parses the AvailableLinks.inf file and populates the structures in xLinkMgr
        #import uam
        
        if needsrefresh:
            print "UamMod_AgeInfo loading data"
            _ReadAgeInfo() #reads from our files
            _AddAgeInfo()
            _SortRestorationLinks()
                    
    xLinkMgr._LoadAvailableLinks = _New_LoadAvailableLinks
    

