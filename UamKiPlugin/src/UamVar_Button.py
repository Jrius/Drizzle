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

#from Plasma import *
from PlasmaTypes import *  #You need this "from x import *" line, because even though the log doesn't give an error, 3dsmax won't show this file otherwise!  It's a mystery, because it works to have "import PlasmaTypes" and PlasmaTypes.ptAttribString, etc. so long as this is also here.
#from PlasmaKITypes import *
#from PlasmaVaultConstants import *
import Plasma
#import PlasmaTypes
import _UamVars


# null->"whatever" transitions don't matter for buttons.
# ""->"1" transitions are the triggering caused by the button.
# 

#We can safely change the descriptive text
#We can safely add or change a default string
#We can safely change the order of these lines
#We can even remove or add items without changing the version.
#But we should never remove one and use its number again.
#ptAttribString values can <= 30,000 chars long.
button = ptAttribActivator(1, "The clickable thing:")
vartoset = ptAttribString(2, "Uamvar to set when clicked:", "")


class UamVar_Button(ptResponder):

    varname = None
    triggerstate = 1.0 #keydown.  Perhaps this can be set in 3dsmax in a future version.
    
    def __init__(self):
        self.id = 31288 #must have and must be unique (3dsmax will let you know if it's not unique) and must be a 16bit (signed?) int.  So let's keep them under 32768.  And it must never change, or it will anger any .max files that use it.
        self.version = 1  #must have and must be at least 1.  It can go up, but never down, or it will anger those .max files again.
        print "UamVarButton.__init__"
        #Attributes are not set yet

    #detects clicks from a ptAttribActivator, e.g.
    def OnNotify(self, state, id, events):
        print "UamVarButton.OnNotify state="+`state`+" id="+`id`+" events="+`events`
        #state tells us if it was a keyup or keydown event.  state=1.0 means keydown, state=0.0 means keyup
        if Plasma.PtWasLocallyNotified(self.key):  #make sure it was us doing the clicking.
            if (id == button.id) and (state==self.triggerstate):
                #print "Clicked!"
                print "Clicked button: "+`button`
                print `dir(vartoset)`
                print `vartoset.value`
                #_UamVars.SetVar("testvarname","testvarvalue3")
                _UamVars.SetVar(self.varname,"")  #turn it off if it isn't already
                _UamVars.SetVar(self.varname,"1") #turn it on to trigger it.  An ""->"1" edge.
                
    def OnInit(self):
        #This seems to be the earliest that the vars are set
        self.varname = vartoset.value
        print "Varname: "+self.varname
        _UamVars.RegisterVar(self.varname)
        #Hmm... we can get the plKey for the source, but there doesn't seem to be a way to get the type.  So we can't tell if this is a clickable or what.
        #print "Button: "+`button`
        #print "Buttondir: "+`dir(button)`
        #print "Buttonval: "+`button.value`  #list of Plasma.ptKey
        #print "Buttonbyobj: "+`button.byObject`
        #print "Buttonid: "+`button.id`
        #for key in button.value:
        #    print "Buttonkey: "+`key`
        #    print "  members: "+`dir(key)`
        #    print "  name: "+`key.getName()` #e.g. "Clickable01"
        #    print "  so: "+`key.getSceneObject()` #Plasma.ptSceneObject
        
    #def OnFirstUpdate(self):
    #    print "UamVarButton.OnFirstUpdate"


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
