"""
Siralehn.py
SDL hook for Siralehn, on Complete Chronicles
Additionaly, this handles the rain, so that it's random.

In EoA, when using the Slate it rains for 1'30" (though the pools take 1'05" to fill)
Then there is a 2'06" delay 'till the water is drained.

Currently it rains 2 to 10 minutes after last rain.
It rains for 1'10" to 1'30"

The rain is handled by the owner of the Age.



Note: will have to see about using PtGetDniTime for this, this could make it behave even better...

#"""

from Plasma import *
from PlasmaTypes import *
import random
amMaster = 0
doRainTimerID = 1
stopRainTimerID = 2
checkMasterTimerID = 3
minimumDelay = 120
maximumDelay = 600
minimumDuration = 70
maximumDuration = 90


class Siralehn(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6350
        version = 5
        self.version = version


    def OnServerInitComplete(self):
        print "Linked in, checking whether we're Rain Master..."
        if self.sceneobject.isLocallyOwned():
            amMaster = 1
        else:
            amMaster = 0
            print "I'm not the master, but I might be someday."
            PtAtTimeCallback(self.key, 30, checkMasterTimerID)
            return

        ageSDL = PtGetAgeSDL()
        if ageSDL['runEnvEffect'][0] and not len(PtGetPlayerList()):
            print "Huh, no one in the Age but rain is on. Turning off."
            ageSDL['runEnvEffect'] = (0,)

        print "I'm master, I decide when to turn the rain on."
        self.DelayBeforeRainStart()


    def DelayBeforeRainStart(self):
        delay = (minimumDelay + (random.random() * (maximumDelay - minimumDelay)))
        print ("Next downpour will come in %d seconds." % delay)
        PtAtTimeCallback(self.key, delay, doRainTimerID)


    def DelayBeforeRainStop(self):
        delay = (minimumDuration + (random.random() * (maximumDuration - minimumDuration)))
        print ("It will rain for %d seconds." % delay)
        PtAtTimeCallback(self.key, delay, stopRainTimerID)


    def OnTimer(self, id):
        ageSDL = PtGetAgeSDL()
        if (id == doRainTimerID):
            print "It's now raining. Take shelter."
            ageSDL['runEnvEffect'] = (1,)
            self.DelayBeforeRainStop()
        elif (id == stopRainTimerID):
            print "It's sunny again."
            ageSDL['runEnvEffect'] = (0,)
            self.DelayBeforeRainStart()
        elif (id == checkMasterTimerID):
            if self.sceneobject.isLocallyOwned():
                print "Wow, I'm now the Rain Master !"
                if (ageSDL['runEnvEffect'][0] == 1):
                    self.DelayBeforeRainStop()
                else:
                    self.DelayBeforeRainStart()
            else:
                PtAtTimeCallback(self.key, 30, checkMasterTimerID)



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
    global glue_paramKeys
    global glue_params
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
                print ('%s has id %d which is already defined in %s' %
                      (obj.name, obj.id, glue_params[obj.id].name))
        else:
            glue_params[obj.id] = obj
    elif type(obj) == type([]):
        for o in obj:
            glue_findAndAddAttribs(o, glue_params)
    elif type(obj) == type({}):
        for o in obj.values():
            glue_findAndAddAttribs(o, glue_params)
    elif type(obj) == type(()):
        for o in obj:
            glue_findAndAddAttribs(o, glue_params)


def glue_getParamDict():
    global glue_paramKeys
    global glue_params
    if type(glue_params) == type(None):
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
    pd = glue_getParamDict()
    if (pd != None):
        if type(glue_paramKeys) == type([]):
            if (number >= 0) and (number < len(glue_paramKeys)):
                return pd[glue_paramKeys[number]].getdef()
            else:
                print ('glue_getParam: Error! %d out of range of attribute list' % number)
        else:
            pl = pd.values()
            if (number >= 0) and (number < len(pl)):
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
                        if type(pd[id].value) != type([]):
                            pd[id].value = []
                    except AttributeError:
                        pd[id].value = []
                    pd[id].value.append(value)
                else:
                    pd[id].value = value
        elif glue_verbose:
            print "setParam: can't find id=", id
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
    pd = glue_getParamDict()
    if pd != None:
        if type(glue_paramKeys) == type([]):
            if (number >= 0) and (number < len(glue_paramKeys)):
                return pd[glue_paramKeys[number]].getVisInfo()
            else:
                print ('glue_getVisInfo: Error! %d out of range of attribute list' % number)
        else:
            pl = pd.values()
            if (number >= 0) and (number < len(pl)):
                return pl[number].getVisInfo()
            elif glue_verbose:
                print ('glue_getVisInfo: Error! %d out of range of attribute list' % number)
    if glue_verbose:
        print 'GLUE: Attribute list error'
    return None
