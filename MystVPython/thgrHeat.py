"""
Reworked a part to get the Direbo SDL correctly (when forcing the berg away if we linked to it before it detached).
Will need to see how we can link to the keep without the slates (as for each Age...)
#"""


from Plasma import *
from PlasmaTypes import *
import xAgeSDL
sdlHeat = ptAttribString(1, 'SDL: heat')
respHeat1 = ptAttribResponder(2, 'resp: heat grp 1', ['on', 'off'])
sdlBerg = ptAttribString(3, 'SDL: berg')
actBerg = ptAttribActivator(4, 'rgn sns: berg')
respBerg = ptAttribResponder(5, 'resp: berg')
sdlActivePeds = ptAttribString(6, 'SDL: active pedestals')
respHeat2 = ptAttribResponder(7, 'resp: heat grp 2', ['on', 'off'])
boolHeat = 0
boolBerg = 0
OnInit = 0

class thgrHeat(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6380
        version = 3
        self.version = version
        print '__init__thgrHeat v. ',
        print version,
        print '.0'


    def OnServerInitComplete(self):
        global boolHeat
        global boolBerg
        global OnInit
        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(sdlHeat.value)
        ageSDL.setFlags(sdlHeat.value, 1, 1)
        ageSDL.setNotify(self.key, sdlHeat.value, 0.0)
        ageSDL.sendToClients(sdlBerg.value)
        ageSDL.setFlags(sdlBerg.value, 1, 1)
        ageSDL.setNotify(self.key, sdlBerg.value, 0.0)
        boolHeat = ageSDL[sdlHeat.value][0]
        boolBerg = ageSDL[sdlBerg.value][0]

        """direboSDL = xAgeSDL.xAgeSDL("Direbo")
        for i in range(5):
            activePed = direboSDL.getValue(sdlActivePeds.value, i)
            if (activePed == 'Keep'):
                if (not (boolBerg)):
                    # probably debugging, in case we manage to draw the symbol without looking at it
                    # (hehehe, even when having the template in front of you, it's simply impossible
                    # to draw something the perceptron will accept (love that name, BTW). So no risk
                    # someone will skip steps)
                    OnInit = 1
                    boolBerg = 1
                    ageSDL[sdlBerg.value] = (1,)
                break"""
        """ageSDL = PtGetAgeSDL()
        for i in range(5):
            activePed = ageSDL[sdlActivePeds.value][i]
            if (activePed == 'Keep'):
                if (not (boolBerg)):
                    OnInit = 1
                    boolBerg = 1
                    ageSDL[sdlBerg.value] = (1,)
                break #"""
        ## third version of pedestal SDL ! Meh.
        ageSDL = PtGetAgeSDL()
        if ageSDL[sdlActivePeds.value + "Keep"][0]:
            if (not (boolBerg)):
                OnInit = 1
                boolBerg = 1
                ageSDL[sdlBerg.value] = (1,)
        if boolHeat:
            print 'thgrHeat.OnAgeDataInitialized(): SDL says heat\'s on'
            respHeat1.run(self.key, state='on', fastforward=1)
            respHeat2.run(self.key, state='on', fastforward=1)
        else:
            print 'thgrHeat.OnAgeDataInitialized(): SDL says heat\'s off'
            respHeat1.run(self.key, state='off', fastforward=1)
            respHeat2.run(self.key, state='off', fastforward=1)
        if boolBerg:
            print 'thgrHeat.OnAgeDataInitialized(): SDL says berg is away'
            actBerg.disableActivator()
            respBerg.run(self.key, fastforward=1)


    def OnNotify(self, state, id, events):
        if ((id == actBerg.id) and state):
            #PtSetCameraShake(1, 0.5)
            #PtAtTimeCallback(self.key, 2.9, 1)
            if (PtWasLocallyNotified(self.key) and PtFindAvatar(events) == PtGetLocalAvatar()):
                ageSDL = PtGetAgeSDL()
                ageSDL[sdlBerg.value] = (1,)
                ageSDL[sdlActivePeds.value + "Keep"] = (1,) ## also enable link to this ped


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global boolHeat
        global boolBerg
        global OnInit
        ageSDL = PtGetAgeSDL()
        if (VARname == sdlHeat.value):
            print 'thgrHeat.OnSDLNotify(): SDL for',
            print VARname,
            print 'changed to',
            print ageSDL[VARname][0]
            boolHeat = ageSDL[VARname][0]
            if boolHeat:
                respHeat1.run(self.key, state='on')
                respHeat2.run(self.key, state='on')
            else:
                respHeat1.run(self.key, state='off')
                respHeat2.run(self.key, state='off')
        elif (VARname == sdlBerg.value):
            print 'thgrHeat.OnSDLNotify(): SDL for',
            print VARname,
            print 'changed to',
            print ageSDL[VARname][0]
            boolBerg = ageSDL[VARname][0]
            if boolBerg:
                respBerg.run(self.key, fastforward=OnInit)
                actBerg.disableActivator()
                OnInit = 0


    #def OnTimer(self, id):
    #    if (id == 1):
    #        PtSetCameraShake(0, 0)


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



