from Plasma import *
from PlasmaTypes import *
import string
Open01 = ptAttribActivator(1, 'act: Handle01 Open')
Close01 = ptAttribActivator(2, 'act: Handle01 Close')
animHandle01 = ptAttribAnimation(3, 'anim: Handle01')
Open02 = ptAttribActivator(4, 'act: Handle02 Open')
Close02 = ptAttribActivator(5, 'act: Handle02 Close')
animHandle02 = ptAttribAnimation(6, 'anim: Handle02')
respTrue = ptAttribResponder(7, 'resp: open door')
respFalse = ptAttribResponder(8, 'resp: close door')
AgeSDLdoor = ptAttribString(9, 'AgeSDL: door SDL')

class dsntTwoHandleDoor(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6223
        version = 1
        self.version = version
        print '__init__dsntTwoHandleDoor v.',
        print version,
        print '.0'


    def OnServerInitComplete(self):
        ageSDL = PtGetAgeSDL()
        ageSDL.setFlags(AgeSDLdoor.value, 1, 1)
        ageSDL.sendToClients(AgeSDLdoor.value)
        ageSDL.setNotify(self.key, AgeSDLdoor.value, 0.0)
        self.DisableHandles()

        ## lower speed to counterpart speeded animation
        animHandle01.animation.speed(0.3)
        animHandle02.animation.speed(0.3)

        if (ageSDL[AgeSDLdoor.value][0] == 1):
            print 'dsntTwoHandleDoor.OnInit: Door',
            print AgeSDLdoor.value,
            print ' was open when I got here.'
            respTrue.run(self.key, fastforward=1)
            animHandle01.animation.playToTime(3)
            animHandle02.animation.playToTime(3)
        elif (ageSDL[AgeSDLdoor.value][0] == 0):
            print 'dsntTwoHandleDoor.OnInit: Door',
            print AgeSDLdoor.value,
            print ' was closed when I got here.'
            respFalse.run(self.key, fastforward=1)
            animHandle01.animation.playToTime(0)
            animHandle02.animation.playToTime(0)


    def OnNotify(self, state, id, events):
        if (not (state)):
            return
        ageSDL = PtGetAgeSDL()
        if (id == Open01.id):
            print 'dsntTwoHandleDoor: Handle 1 just set to fully open.'
            if (ageSDL[AgeSDLdoor.value][0] == 1):
                print '\tIgnoring, because the door is already open.'
            else:
                if ((PtFindAvatar(events) == PtGetLocalAvatar()) and PtWasLocallyNotified(self.key)):
                    ageSDL[AgeSDLdoor.value] = (1,)
                self.DisableHandles()
                animHandle02.animation.playToTime(3)
        elif (id == Close01.id):
            print 'dsntTwoHandleDoor: Handle 1 just set to fully closed.'
            if (ageSDL[AgeSDLdoor.value][0] == 0):
                print '\tIgnoring, because the door is already closed.'
            else:
                if ((PtFindAvatar(events) == PtGetLocalAvatar()) and PtWasLocallyNotified(self.key)):
                    ageSDL[AgeSDLdoor.value] = (0,)
                self.DisableHandles()
                animHandle02.animation.playToTime(0)
        elif (id == Open02.id):
            print 'dsntTwoHandleDoor: Handle 2 just set to fully open.'
            if (ageSDL[AgeSDLdoor.value][0] == 1):
                print '\tIgnoring, because the door is already open.'
            else:
                if ((PtFindAvatar(events) == PtGetLocalAvatar()) and PtWasLocallyNotified(self.key)):
                    ageSDL[AgeSDLdoor.value] = (1,)
                self.DisableHandles()
                animHandle01.animation.playToTime(3)
        elif (id == Close02.id):
            print 'dsntTwoHandleDoor: Handle 2 just set to fully closed.'
            if (ageSDL[AgeSDLdoor.value][0] == 0):
                print '\tIgnoring, because the door is already closed.'
            else:
                if ((PtFindAvatar(events) == PtGetLocalAvatar()) and PtWasLocallyNotified(self.key)):
                    ageSDL[AgeSDLdoor.value] = (0,)
                self.DisableHandles()
                animHandle01.animation.playToTime(0)


    def DisableHandles(self):
        print 'dsntTwoHandleDoor: Handles disabled.'
        Open01.disable()
        Close01.disable()
        Open02.disable()
        Close02.disable()
        PtAtTimeCallback(self.key, 4, 1)


    def OnTimer(self, id):
        if (id == 1):
            print 'dsntTwoHandleDoor: Handles enabled.'
            Open01.enable()
            Close01.enable()
            Open02.enable()
            Close02.enable()


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        ageSDL = PtGetAgeSDL()
        if (VARname == AgeSDLdoor.value):
            if (ageSDL[AgeSDLdoor.value][0] == 1):
                print 'dsntTwoHandleDoor.OnSDLNotify: Door',
                print AgeSDLdoor.value,
                print 'was just opened.'
                respTrue.run(self.key)
            elif (ageSDL[AgeSDLdoor.value][0] == 0):
                print 'dsntTwoHandleDoor.OnSDLNotify: Door',
                print AgeSDLdoor.value,
                print 'was just closed.'
                respFalse.run(self.key)


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



