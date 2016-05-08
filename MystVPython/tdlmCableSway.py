from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *
SlateSDL = ptAttribString(1, 'SDL: Adam\'s Slate SDL')
AnimCable01 = ptAttribAnimation(2, 'anim: Cable01')
AnimCable02 = ptAttribAnimation(3, 'anim: Cable02')
AnimCable03 = ptAttribAnimation(4, 'anim: Cable03')
AnimCable04 = ptAttribAnimation(5, 'anim: Cable04')
AnimCable05 = ptAttribAnimation(6, 'anim: Cable05')
AnimCable06 = ptAttribAnimation(7, 'anim: Cable06')
AnimCable07 = ptAttribAnimation(8, 'anim: Cable07')
respSfxSlow = ptAttribResponder(9, 'resp: Sfx Slow Sway')
respSfxFast = ptAttribResponder(10, 'resp: Sfx Fast Sway')
slowspeed = 1.0
fastspeed = 5.0

class tdlmCableSway(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6224
        version = 2
        self.version = version
        print '__init__ tdlmCableSway v.',
        print version,
        print '.0'


    def OnFirstUpdate(self):
        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(SlateSDL.value)
        ageSDL.setFlags(SlateSDL.value, 1, 1)
        ageSDL.setNotify(self.key, SlateSDL.value, 0.0)
        print ' tdlmCableSway: When I got here, ',
        print SlateSDL.value,
        print ' was set to: ',
        print ageSDL[SlateSDL.value][0]
        if (ageSDL[SlateSDL.value][0] == 0):
            print '\tTodelmer Seasons are currently not changing.'
            self.Slow()
        else:
            print '\tA Season change is currently in progress.'
            self.Fast()


    def Slow(self):
        print '\tThe Cables sway at their normal (slow) speed.'
        AnimCable01.value.speed(slowspeed)
        AnimCable02.value.speed(slowspeed)
        AnimCable03.value.speed(slowspeed)
        AnimCable04.value.speed(slowspeed)
        AnimCable05.value.speed(slowspeed)
        AnimCable06.value.speed(slowspeed)
        AnimCable07.value.speed(slowspeed)
        respSfxSlow.run(self.key)


    def Fast(self):
        print '\tThe Cables sway at their fast speed.'
        AnimCable01.value.speed(fastspeed)
        AnimCable02.value.speed(fastspeed)
        AnimCable03.value.speed(fastspeed)
        AnimCable04.value.speed(fastspeed)
        AnimCable05.value.speed(fastspeed)
        AnimCable06.value.speed(fastspeed)
        AnimCable07.value.speed(fastspeed)
        respSfxFast.run(self.key)


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        if (VARname == SlateSDL.value):
            ageSDL = PtGetAgeSDL()
            if (ageSDL[SlateSDL.value][0] == 0):
                print 'tdlmCableSway.OnSDLNotify: The Season change just stopped.'
                self.Slow()
            else:
                print 'tdlmCableSway.OnSDLNotify: The Season change just started.'
                self.Fast()


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



