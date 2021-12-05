"""
Environment effect handler, rewritten since we don't have Bahros or Slates anymore.
"""


from Plasma import *
from PlasmaTypes import *

actRunEffect = ptAttribActivator(1, "Activator: run environment effect")
respSpecialEffect = ptAttribResponder(2, "Responder: run glare FX")
respEffectOn = ptAttribResponder(3, "Responder: trigger some shiny visuals on effect active")
respEffectOff = ptAttribResponder(4, "Responder: normal visuals on effect inactive")

boolIsRunningEnvEffect = False
strSDLName = "runEnvEffect"
effectLength = 90 # 90 seconds of env effect

class xEnvEffect(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = -1
        self.version = -1


    def OnFirstUpdate(self):
        print("xEnvEffect: OnFirstUpdate")
        PtPageInNode("xSpecialEffectGlare")


    def OnServerInitComplete(self):
        PtLoadDialog("xSpecialEffectGlare", self.key)

        SlateSDLvalue = PtGetAgeSDL()[strSDLName][0]
        if SlateSDLvalue:
            curTime = PtGetDniTime()
            timeleft = SlateSDLvalue - curTime
            PtAtTimeCallback(self.key, timeleft, 1)
            boolIsRunningEnvEffect = True
            if respEffectOn:
                respEffectOn.run(self.key, fastforward=True)
        else:
            if respEffectOff:
                respEffectOff.run(self.key, fastforward=True)

        if PtGetAgeName() == "Tahgira":
            # MOAR time for Tahgira. Because ghost trigger is farther away from puzzle.
            effectLength = 180


    def __del__(self):
        PtHideDialog("xSpecialEffectGlare")
        PtUnloadDialog("xSpecialEffectGlare")
        PtPageOutNode("xSpecialEffectGlare")
        PtClearTimerCallbacks(self.key)


    def OnNotify(self, state, id, events):
        global boolIsRunningEnvEffect
        print("xEnvEffect: Received notify, state=%s, id=%d" % (state, id))
        if id == actRunEffect.id and state and not boolIsRunningEnvEffect:
            for event in events:
                if (event[1] == 1):
                    self.runEnvEffect()
                    break
        elif id == respSpecialEffect.id:
            print("Got callback from special effect responder")


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        pass


    def runEnvEffect(self):
        global boolIsRunningEnvEffect
        global strSDLName
        global effectLength
        print("#-#-# RUNNING SPECIAL EFFECT. HELL YEAH #-#-#")

        ## start env effect
        ageSDL = PtGetAgeSDL()
        if self.sceneobject.isLocallyOwned():
            ageSDL[strSDLName] = (PtGetDniTime()+effectLength,) ## we could use only 1 but Todelmer's season are a bit more complex...

        ## make glare thing
        PtShowDialog("xSpecialEffectGlare")
        respSpecialEffect.run(self.key)
        if respEffectOn:
            respEffectOn.run(self.key)

        ## come back when it will be done
        PtAtTimeCallback(self.key, effectLength, 1)

        boolIsRunningEnvEffect = True


    def stopEnvEffect(self):
        global boolCanRunEnvEffect
        global boolIsRunningEnvEffect
        global strSDLName

        print "#-#-# STOPPING ENV EFFECT #-#-#"

        ## stop env effect
        ageSDL = PtGetAgeSDL()
        if self.sceneobject.isLocallyOwned():
            ageSDL[strSDLName] = (0,)

        ## make glare thing
        PtShowDialog("xSpecialEffectGlare")
        respSpecialEffect.run(self.key)
        if respEffectOff:
            respEffectOff.run(self.key)

        boolIsRunningEnvEffect = False


    def OnTimer(self, id):
        if id == 1:
            self.stopEnvEffect()




### Python Glue ###

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
