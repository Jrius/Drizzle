"""
Second version of this script (since first one was buggy).

- events from player will be ignored if the previous responder state is not done running. This avoids spamming which causes the crack
    animation to be stuck between two stages.
- however, creaking sound will still play when entering the region (does not interfere with jumping on the crack)
- removed all online code, online version will work as offline one. Because it was too much of a mess. However, if you want to code
    some clever code requiring multiple players jumping at once on the crack to break it, feel free.

Behavior:
    Stepping on the ice once will crack it (first state)
    Stepping again will only play sound (noadvance)
    Jumping on it will crack it more (mid)
    Stepping on it will play heavier sound (noadvance_slate)
    Jumping on it again will crack it for good (last)

#"""


from Plasma import *
from PlasmaTypes import *
import string
sdlCrack = ptAttribString(1, 'SDL: crack')
actCrack = ptAttribActivator(2, 'rgn snsr: crack for Player')
respCrack = ptAttribResponder(3, 'resp: crack', ['none', 'noadvance', 'first', 'mid', 'last', 'noadvance_slate'], byObject=1)

# legacy
#sdlSlate = ptAttribString(4, 'SDL: have slate') # useless
#actAtBottom = ptAttribActivator(5, 'rgn: fell to bottom (novice)') # useless
#objWarpPlayer = ptAttribSceneobject(6, 'obj: warp point for Player') # useless
#actCrackBahro = ptAttribActivator(7, 'rgn snsr: crack for Bahro') # useless

byteCrack = 0           # the state of the crack. 0: closed. 1: small fissure. 2: big crack. 3: open
ignoreJumpEvent = 0     # skip redundant ground impact event
responderEnabled = 1    # don't run the responder if previous state is not ended

class thgrCave(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6382
        version = 9
        self.version = version
        print '__init__thgrCave v. ',
        print version,
        print '.0'


    def OnServerInitComplete(self):
        global byteCrack
        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(sdlCrack.value)
        ageSDL.setFlags(sdlCrack.value, 1, 1)
        ageSDL.setNotify(self.key, sdlCrack.value, 0.0)
        byteCrack = ageSDL[sdlCrack.value][0]
        if byteCrack:
            if (byteCrack == 1):
                print 'thgrCave.OnServerInitComplete(): SDL says crack is at first stage'
                respCrack.run(self.key, state='first', fastforward=1)
            elif (byteCrack == 2):
                print 'thgrCave.OnServerInitComplete(): SDL says crack is at middle stage'
                respCrack.run(self.key, state='mid', fastforward=1)
            elif (byteCrack == 3):
                print 'thgrCave.OnServerInitComplete(): SDL says crack is at last stage'
                respCrack.run(self.key, state='mid', fastforward=1)
                respCrack.run(self.key, state='last', fastforward=1)
        else:
            print 'thgrCave.OnServerInitComplete(): SDL says crack hasn\'t started yet'
            respCrack.run(self.key, state='none', fastforward=1)


    def OnNotify(self, state, id, events):
        global byteCrack
        global responderEnabled

        if (id == respCrack.id):
            for event in events:
                if (event[0] == kCallbackEvent):
                    print "Got callback from crack responder."
                    responderEnabled = 1

        if ((id == actCrack.id) and state):
            for event in events:
                if (event[0] == kCollisionEvent):
                    player = PtGetLocalAvatar()
                    if (PtFindAvatar(events) == player) and PtWasLocallyNotified(self.key):
                        if (event[1] == 1):
                            print 'thgrCave.OnNotify(): Player is on crack, crack=', byteCrack
                            if byteCrack == 0:
                                ageSDL = PtGetAgeSDL()
                                ageSDL[sdlCrack.value] = (1,)
                                player.avatar.registerForBehaviorNotify(self.key)
                            elif byteCrack == 1:
                                respCrack.run(self.key, state='noadvance', netForce=1)
                                player.avatar.registerForBehaviorNotify(self.key)
                            elif byteCrack == 2:
                                respCrack.run(self.key, state='noadvance_slate', netForce=1)
                                player.avatar.registerForBehaviorNotify(self.key)
                            elif byteCrack == 3: pass ## ice is totally broken, no need to play sound
                        else:
                            print 'thgrCave.OnNotify(): Player is off crack.'
                            player.avatar.unRegisterForBehaviorNotify(self.key)


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global byteCrack
        global responderEnabled
        ageSDL = PtGetAgeSDL()
        changedVar = VARname
        newVal = ageSDL[changedVar][0]
        print 'thgrCave.OnSDLNotify(): SDL for ',
        print changedVar,
        print ' changed to ',
        print newVal
        if (changedVar == sdlCrack.value):
            byteCrack = newVal
            if byteCrack:
                if (byteCrack == 1):
                    respCrack.run(self.key, state='first')
                    responderEnabled=0
                elif (byteCrack == 2):
                    respCrack.run(self.key, state='mid')
                    responderEnabled=0
                elif (byteCrack == 3):
                    respCrack.run(self.key, state='last')
                    responderEnabled=0
            else:
                respCrack.run(self.key, state='none')


    def OnBehaviorNotify(self,type,id,state):
        global byteCrack
        global ignoreJumpEvent
        if type == PtBehaviorTypes.kBehaviorTypeGroundImpact and state:
            print 'thgrCave.OnNotify(): Ground impact ! Crack=', byteCrack
            if byteCrack == 0: ## we never jump on it in state 0
                pass
            elif byteCrack == 1 and not ignoreJumpEvent:
                if responderEnabled:
                    PtGetAgeSDL()[sdlCrack.value] = (2,)
                ignoreJumpEvent = 1 ## just in case, might not be required
                PtAtTimeCallback(self.key, 2, 1)
            elif byteCrack == 2 and not ignoreJumpEvent:
                if responderEnabled:
                    PtGetAgeSDL()[sdlCrack.value] = (3,)
                ignoreJumpEvent = 1
                PtAtTimeCallback(self.key, 2, 1)


    def OnTimer(self, id):
        global ignoreJumpEvent
        ignoreJumpEvent = 0


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



