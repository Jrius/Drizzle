from Plasma import *
from PlasmaTypes import *
sdlRain = ptAttribString(1, 'SDL: rain')
actPools = ptAttribActivator(2, 'clk: pools interest well')
respPoolsReflect = ptAttribResponder(3, 'resp: pools reflect', ['enter', 'exit'])
respPoolsDrain = ptAttribResponder(4, 'resp: pools drain')
boolRain = 0
kTimerWaitToReflect = 6
kTimerReflecting = 120
kTimerWaitToDrain = 0.5

class srlnPool(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6345
        version = 5
        self.version = version
        print '__init__srlnPool v. ',
        print version,
        print '.0'


    def OnServerInitComplete(self):
        global boolRain
        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(sdlRain.value)
        ageSDL.setFlags(sdlRain.value, 1, 1)
        ageSDL.setNotify(self.key, sdlRain.value, 0.0)
        boolRain = ageSDL[sdlRain.value][0]
        if boolRain:
            print 'srlnPool.OnAgeDataInitialized(): SDL says it\'s raining'


    def OnNotify(self, state, id, events):
        if not PtWasLocallyNotified(self.key): return
        if (id == respPoolsReflect.id):
            print 'srlnPool.OnNotify(): pools done reflecting'
        elif (id == respPoolsDrain.id):
            print 'srlnPool.OnNotify(): pools drained'


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global boolRain
        ageSDL = PtGetAgeSDL()
        if (VARname == sdlRain.value):
            boolRain = ageSDL[VARname][0]
            print 'srlnPool.OnSDLNotify(): SDL value of ',
            print sdlRain.value,
            print ' changed to ',
            print boolRain
            if (not (boolRain)):
                PtAtTimeCallback(self.key, kTimerWaitToReflect, 1)


    def OnTimer(self, id):
        if (id == 1):
            respPoolsReflect.run(self.key, state='enter')
            print 'srlnPool.OnTimer(): setting reflect to 1, now setting timer for ',
            print kTimerReflecting,
            print ' seconds'
            PtAtTimeCallback(self.key, kTimerReflecting, 2)
        elif (id == 2):
            respPoolsReflect.run(self.key, state='exit')
            PtAtTimeCallback(self.key, kTimerWaitToDrain, 3)
        elif (id == 3):
            print 'srlnPool.OnTimer(): setting reflect to 0, will now drain pools'
            respPoolsDrain.run(self.key)


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



