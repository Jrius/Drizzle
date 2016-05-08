"""
Note: no PtGetGlobalSDLVar for timeElapsed, so wil have to find some sort of solution.


#"""

from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *
SeasonSDL = ptAttribString(1, 'SDL Season')
PistonAnim = ptAttribAnimation(2, 'anim: Drive Piston')
Face0to1 = ptAttribResponder(3, 'resp: Face 0to1')
Face1to2 = ptAttribResponder(4, 'resp: Face 1to2')
Face2to3 = ptAttribResponder(5, 'resp: Face 2to3')
Face3to0 = ptAttribResponder(6, 'resp: Face 3to0')
PowerSDL = ptAttribString(7, 'SDL Power')
WhichFaceSDL = ptAttribString(8, 'SDL Which Face')
TurnFace = ptAttribActivator(9, 'act: Turn Face')
SlateSDL = ptAttribString(10, 'SDL: Adam\'s Slate SDL')
respFastSfx = ptAttribResponder(11, 'resp: Play Fast Sfx')
respSlowSfx = ptAttribResponder(12, 'resp: Play Slow Sfx')
alreadychanging = false

class tdlmClock(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6221
        version = 4
        self.version = version
        print '__init__ tdlmClock v.',
        print version,
        print '.2b'


    def OnFirstUpdate(self):
        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(SlateSDL.value)
        ageSDL.setFlags(SlateSDL.value, 1, 1)
        ageSDL.setNotify(self.key, SlateSDL.value, 0.0)
        
        ageSDL.sendToClients(PowerSDL.value)
        ageSDL.setFlags(PowerSDL.value, 1, 1)
        ageSDL.setNotify(self.key, PowerSDL.value, 0.0)
        
        ageSDL.sendToClients(WhichFaceSDL.value)
        ageSDL.setFlags(WhichFaceSDL.value, 1, 1)
        ageSDL.setNotify(self.key, WhichFaceSDL.value, 0.0)
        
        ageSDL.sendToClients(SlateSDL.value)
        ageSDL.setFlags(SlateSDL.value, 1, 1)
        ageSDL.setNotify(self.key, SlateSDL.value, 0.0)
        
        if (ageSDL[SlateSDL.value][0] == 0):
            print ' tdlmClock: When I got here, ',
            print WhichFaceSDL.value,
            print ' was set to: ',
            print ageSDL[WhichFaceSDL.value][0]
            print '\tTodelmer Seasons are currently not changing.'
            if   (ageSDL[WhichFaceSDL.value][0] == 0):
                Face3to0.run(self.key, fastforward=1)
            elif (ageSDL[WhichFaceSDL.value][0] == 1):
                Face0to1.run(self.key, fastforward=1)
            elif (ageSDL[WhichFaceSDL.value][0] == 2):
                Face1to2.run(self.key, fastforward=1)
            elif (ageSDL[WhichFaceSDL.value][0] == 3):
                Face2to3.run(self.key, fastforward=1)
            if   (ageSDL[PowerSDL.value][0] == 1):
                print '\tStarting clock.'
                PistonAnim.animation.play()
                PistonAnim.value.speed(0.01)
                respSlowSfx.run(self.key)
            else:
                print '\tStopping clock.'
                PistonAnim.animation.stop()
        else:
            print 'tdlmClock: A season change was in progress when I arrived here.'
            if (ageSDL[PowerSDL.value][0] == 0):
                print '\tBut the power is off, so I don\'t care.'
            else:
                timeElapsed = PtGetDniTime()
                SlateSDLvalue = ageSDL[SlateSDL.value][0]
                print 'SlateSDL (endtime) = ',
                print SlateSDLvalue
                print 'timeElapsed = ',
                print timeElapsed
                timeleft = SlateSDLvalue - timeElapsed
                if timeleft <= 0: return
                print '\tSpeeding up the piston for the remaining ',
                print timeleft,
                print ' seconds of the Season change.'
                PistonAnim.animation.play()
                PistonAnim.value.speed(10)
                respFastSfx.run(self.key)
                PtAtTimeCallback(self.key, timeleft, 1)


    def OnNotify(self, state, id, events):
        global alreadychanging
        ageSDL = PtGetAgeSDL()
        if alreadychanging:
            return
        if   (ageSDL[WhichFaceSDL.value][0] == 0):
            Face0to1.run(self.key)
            if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                ageSDL[WhichFaceSDL.value] = (1,)
        elif (ageSDL[WhichFaceSDL.value][0] == 1):
            Face1to2.run(self.key)
            if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                ageSDL[WhichFaceSDL.value] = (2,)
        elif (ageSDL[WhichFaceSDL.value][0] == 2):
            Face2to3.run(self.key)
            if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                ageSDL[WhichFaceSDL.value] = (3,)
        elif (ageSDL[WhichFaceSDL.value][0] == 3):
            Face3to0.run(self.key)
            if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                ageSDL[WhichFaceSDL.value] = (0,)
        print 'tdlmClock: Time flies when you\'re having fun in Todelmer.'
        print '\tSetting clock to face #',
        print ageSDL[WhichFaceSDL.value][0]
        alreadychanging = true
        PtAtTimeCallback(self.key, 0.08, 2)


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        ageSDL = PtGetAgeSDL()
        if (VARname == PowerSDL.value):
            if (ageSDL[VARname][0] == 0):
                print 'tdlmClock.OnSDLNotify: The power just turned off. Clock stops.'
                PistonAnim.animation.stop()
            else:
                print 'tdlmClock.OnSDLNotify: The power just turned on. Clock starts.'
                if (ageSDL[SlateSDL.value][0] == 0):
                    print '\tClock starts at its normal slow speed.'
                    PistonAnim.animation.play()
                    PistonAnim.value.speed(0.01)
                    respSlowSfx.run(self.key)
                else:
                    print '\tA season change was in progress. Clock plays at fast speed.'
                    PistonAnim.animation.play()
                    PistonAnim.value.speed(10)
                    respFastSfx.run(self.key)
        elif (VARname == SlateSDL.value):
            if (ageSDL[VARname][0] == 0):
                print 'Adam\'s slate SDL just set back to 0.'
                return
            else:
                print 'tdlmClock.OnSDLNotify: The season just changed. Speeding up the piston during the 90 second transition.'
                PistonAnim.value.speed(10)
                respFastSfx.run(self.key)
                PtAtTimeCallback(self.key, 90, 1)


    def OnTimer(self, id):
        global alreadychanging
        if (id == 1):
            print 'tdlmClock.OnTimer: Time transition complete. Returning piston to normal speed.'
            PistonAnim.value.speed(0.01)
            respSlowSfx.run(self.key)
        elif (id == 2):
            alreadychanging = false


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



