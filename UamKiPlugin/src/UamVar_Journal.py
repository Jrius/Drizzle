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

from PlasmaTypes import *  #You need this "from x import *" line, because even though the log doesn't give an error, 3dsmax won't show this file otherwise!  It's a mystery, because it works to have "import PlasmaTypes" and PlasmaTypes.ptAttribString, etc. so long as this is also here.
import Plasma
import _UamVars
import uam
import _UamUtils
import os


# Transitions:
# None->x is ignored because we don't want anything to happen on initial link-in.
# ""->"1" is the trigger we're looking for

_vartolisten = ptAttribString(1, "Uamvar to trigger book:", "")
#_text_en = ptAttribString(2, "Text(English):", "")
#_text_de = ptAttribString(3, "Text(German)(optional):", "")
#_text_fr = ptAttribString(4, "Text(French)(optional):", "")
_journalname = ptAttribString(2, "Journal name:","Journal1")
_showopen = ptAttribBoolean(5, "Start Opened?", default=1)
_booktype = ptAttribDropDownList(6, "Book type:", ("Old Book","Notebook"))  #Problem with no default selected.  Plasma bug :P



class UamVar_Journal(ptResponder):

    varname = None
    #text_en = None
    #text_de = None
    #text_fr = None
    journalname = None
    showopen = None
    booktype = None
    
    def __init__(self):
        self.id = 31290 #must have and must be unique (3dsmax will let you know if it's not unique) and must be a 16bit (signed?) int.  So let's keep them under 32768.  And it must never change, or it will anger any .max files that use it.
        self.version = 1  #must have and must be at least 1.  It can go up, but never down, or it will anger those .max files again.
        print "UamVarJournal.__init__"
    
    def OnInit(self):
        print "UamVarJournal.OnInit"
        self.varname = str(_vartolisten.value)
        #self.text_en = str(_text_en.value)
        #self.text_de = str(_text_de.value)
        #self.text_fr = str(_text_fr.value)
        self.journalname = str(_journalname.value)
        self.showopen = int(_showopen.value)
        #print "booktype: "+`_booktype`
        #print "booktype: "+`dir(_booktype)`
        self.booktype = str(getattr(_booktype,"value","Old Book")) #Stupid Plasma bug.  We want _booktype.value, or "OldBook" if .value isn't defined.
        #self.booktype = str(_booktype.value)
        _UamVars.RegisterVar(self.varname)
        _UamVars.ListenToVar(self.varname, self)
        if self.FindFile()==None:
            _UamVars.Error("Unable to find journal: "+"ageresources/"+_UamUtils.GetAgeName()+"--"+self.journalname+".txt")
        
    def FindFile(self):
        #Tries:
        #  ageresources/Agename--JournalName--lang1.txt
        #  ageresources/Agename--JournalName.txt
        #  ageresources/Agename--JournalName--lang2.txt
        #  ageresources/Agename--JournalName--lang3.txt
        filebase = "ageresources/"+_UamUtils.GetAgeName()+"--"+self.journalname
        lang = _UamUtils.GetLanguage()
        filename = filebase+"--"+lang+".txt"
        if os.path.isfile(filename):
            return filename
        filename = filebase+".txt"
        if os.path.isfile(filename):
            return filename
        if lang=="en":
            lang2 = "de"
            lang3 = "fr"
        elif lang=="de":
            lang2 = "en"
            lang3 = "fr"
        elif lang=="fr":
            lang2 = "en"
            lang3 = "de"
        filename = filebase+"--"+lang2+".txt"
        if os.path.isfile(filename):
            return filename
        filename = filebase+"--"+lang3+".txt"
        if os.path.isfile(filename):
            return filename
        return None

    def UamListenEvent(self, uamvar, prev, next):
        print "UamVarJournal.UamListenEvent uamvar="+uamvar+" prev="+`prev`+" next="+`next`
        if prev=="" and next=="1":
            print "Showing book..."
            
            #Find text
            #lang = _UamUtils.GetLanguage()
            #text = ""
            #if lang=="en":
            #    if self.text_en!="":
            #        text = self.text_en
            #    elif self.text_de!="":
            #        text = self.text_de
            #    else:
            #        text = self.text_fr
            #elif lang=="de":
            #    if self.text_de!="":
            #        text = self.text_de
            #    elif self.text_en!="":
            #        text = self.text_en
            #    else:
            #        text = self.text_fr
            #elif lang=="fr":
            #    if self.text_fr!="":
            #        text = self.text_fr
            #    elif self.text_en!="":
            #        text = self.text_en
            #    else:
            #        text = self.text_de
            #else:
            #    raise "Unexpected language: "+lang
            filename = self.FindFile()
            text = _UamUtils.ReadJournal(filename)
                
            #Do <br> conversions
            #text = text.replace("<br>","\n")
            
            #Open the book
            if self.booktype=="Old Book":
                uam.DisplayBook(text, self.showopen)
            elif self.booktype=="Notebook":
                uam.DisplayJournal(text, self.showopen)

                
                
                
#Decompiled with Drizzle28!  Enjoy :)

global glue_cl
global glue_inst
global glue_params
global glue_paramKeys
glue_cl = None
glue_inst = None
glue_params = None
glue_paramKeys = None
try:
    x = glue_verbose
except NameError:
    glue_verbose = 0

def glue_getClass():
    global glue_cl
    if (glue_cl == None):
        try:
            cl = eval(glue_name)
            if issubclass(cl, ptModifier):
                glue_cl = cl
            elif glue_verbose:
                print ('Class %s is not derived from modifier' % cl.__name__)
        except NameError:
            if glue_verbose:
                try:
                    print ('Could not find class %s' % glue_name)
                except NameError:
                    print 'Filename/classname not set!'
    return glue_cl


def glue_getInst():
    global glue_inst
    if (type(glue_inst) == type(None)):
        cl = glue_getClass()
        if (cl != None):
            glue_inst = cl()
    return glue_inst


def glue_delInst():
    global glue_inst
    global glue_cl
    global glue_params
    global glue_paramKeys
    if (type(glue_inst) != type(None)):
        del glue_inst
    glue_cl = None
    glue_params = None
    glue_paramKeys = None


def glue_getVersion():
    inst = glue_getInst()
    ver = inst.version
    glue_delInst()
    return ver


def glue_findAndAddAttribs(obj, glue_params):
    if isinstance(obj, ptAttribute):
        if glue_params.has_key(obj.id):
            if glue_verbose:
                print 'WARNING: Duplicate attribute ids!'
                print ('%s has id %d which is already defined in %s' % (obj.name, obj.id, glue_params[obj.id].name))
        else:
            glue_params[obj.id] = obj
    elif (type(obj) == type([])):
        for o in obj:
            glue_findAndAddAttribs(o, glue_params)
    elif (type(obj) == type({})):
        for o in obj.values():
            glue_findAndAddAttribs(o, glue_params)
    elif (type(obj) == type(())):
        for o in obj:
            glue_findAndAddAttribs(o, glue_params)


def glue_getParamDict():
    global glue_params
    global glue_paramKeys
    if (type(glue_params) == type(None)):
        glue_params = {}
        gd = globals()
        for obj in gd.values():
            glue_findAndAddAttribs(obj, glue_params)
        glue_paramKeys = glue_params.keys()
        glue_paramKeys.sort()
        glue_paramKeys.reverse()
    return glue_params


def glue_getClassName():
    cl = glue_getClass()
    if (cl != None):
        return cl.__name__
    if glue_verbose:
        print ('Class not found in %s.py' % glue_name)
    return None


def glue_getBlockID():
    inst = glue_getInst()
    if (inst != None):
        return inst.id
    if glue_verbose:
        print ('Instance could not be created in %s.py' % glue_name)
    return None


def glue_getNumParams():
    pd = glue_getParamDict()
    if (pd != None):
        return len(pd)
    if glue_verbose:
        print ('No attributes found in %s.py' % glue_name)
    return 0


def glue_getParam(number):
    global glue_paramKeys
    pd = glue_getParamDict()
    if (pd != None):
        if (type(glue_paramKeys) == type([])):
            if ((number >= 0) and (number < len(glue_paramKeys))):
                return pd[glue_paramKeys[number]].getdef()
            else:
                print ('glue_getParam: Error! %d out of range of attribute list' % number)
        else:
            pl = pd.values()
            if ((number >= 0) and (number < len(pl))):
                return pl[number].getdef()
            elif glue_verbose:
                print ('glue_getParam: Error! %d out of range of attribute list' % number)
    if glue_verbose:
        print 'GLUE: Attribute list error'
    return None


def glue_setParam(id, value):
    pd = glue_getParamDict()
    if (pd != None):
        if pd.has_key(id):
            try:
                pd[id].__setvalue__(value)
            except AttributeError:
                if isinstance(pd[id], ptAttributeList):
                    try:
                        if (type(pd[id].value) != type([])):
                            pd[id].value = []
                    except AttributeError:
                        pd[id].value = []
                    pd[id].value.append(value)
                else:
                    pd[id].value = value
        elif glue_verbose:
            print 'setParam: can\'t find id=',
            print id
    else:
        print 'setParma: Something terribly has gone wrong. Head for the cover.'


def glue_isNamedAttribute(id):
    pd = glue_getParamDict()
    if (pd != None):
        try:
            if isinstance(pd[id], ptAttribNamedActivator):
                return 1
            if isinstance(pd[id], ptAttribNamedResponder):
                return 2
        except KeyError:
            if glue_verbose:
                print ('Could not find id=%d attribute' % id)
    return 0


def glue_isMultiModifier():
    inst = glue_getInst()
    if isinstance(inst, ptMultiModifier):
        return 1
    return 0


def glue_getVisInfo(number):
    global glue_paramKeys
    pd = glue_getParamDict()
    if (pd != None):
        if (type(glue_paramKeys) == type([])):
            if ((number >= 0) and (number < len(glue_paramKeys))):
                return pd[glue_paramKeys[number]].getVisInfo()
            else:
                print ('glue_getVisInfo: Error! %d out of range of attribute list' % number)
        else:
            pl = pd.values()
            if ((number >= 0) and (number < len(pl))):
                return pl[number].getVisInfo()
            elif glue_verbose:
                print ('glue_getVisInfo: Error! %d out of range of attribute list' % number)
    if glue_verbose:
        print 'GLUE: Attribute list error'
    return None
