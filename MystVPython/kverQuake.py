from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *
import xRandom
import whrandom
sdlCanQuake = ptAttribString(1, 'SDL: Can Quake')
respBaseStart = ptAttribResponder(2, 'resp: Base Sfx start')
respBaseStop = ptAttribResponder(3, 'resp: Base Sfx stop')
respPeakStart = ptAttribResponder(4, 'resp: Peak Sfx start')
respPeakStop = ptAttribResponder(5, 'resp: Peak Sfx stop')
minDelay = 180
maxDelay = 300
minDuration = 5
maxDuration = 10
fadeinDuration = 1
fadeoutDuration = 1
baseStrengthMin = 0.05
baseStrengthMax = 0.2
peakStrengthMin = 0.4
peakStrengthMax = 0.8
peakProb = 0.2
peakDurationMin = 1
peakDurationMax = 3
peakDelayMin = 4
peakDelayMax = 6
quaking = false
rampUpTarget = 0
rampDownTarget = 0
currentShake = 0

class kverQuake(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6227
        version = 1
        self.version = version
        print '__init__ kverQuake v.',
        print version,
        print '.6'


    def OnFirstUpdate(self):
        xRandom.seed((PtGetDniTime() % 256))
        whrandom.seed()
        PtClearTimerCallbacks(self.key)
        self.NextQuakeDelay()


    def NextQuakeDelay(self):
        QuakeDelay = xRandom.randint(minDelay, maxDelay)
        print 'kverQuake: The next quake will happen in ',
        print QuakeDelay,
        print ' seconds.'
        PtAtTimeCallback(self.key, QuakeDelay, 1)


    def QuakeStart(self):
        global quaking
        global rampUpTarget
        quaking = true
        baseStrength = whrandom.uniform(baseStrengthMin, baseStrengthMax)
        print '##'
        print 'kverQuake: The quake starts with a base strength of ',
        print baseStrength
        rampUpTarget = baseStrength
        self.RampUp()
        prob = whrandom.uniform(0, 1)
        if (prob < peakProb):
            peakDelay = whrandom.uniform(peakDelayMin, peakDelayMax)
            print '\tA Peak will occur ',
            print peakDelay,
            print ' seconds later.'
            PtAtTimeCallback(self.key, peakDelay, 2)
            peakDuration = whrandom.uniform(peakDurationMin, peakDurationMax)
            print '\tThe peak will last for ',
            print peakDuration,
            print ' seconds.'
            PtAtTimeCallback(self.key, (peakDelay + peakDuration), 3)
            Duration = ((whrandom.uniform(minDuration, maxDuration) + peakDelay) + peakDuration)
        else:
            print '\tA Peak will NOT occur.'
            Duration = whrandom.uniform(minDuration, maxDuration)
        print '\tThe quake will last for ',
        print Duration,
        print ' seconds.'
        PtAtTimeCallback(self.key, Duration, 4)
        respBaseStart.run(self.key)


    def QuakeStop(self):
        global quaking
        print 'kverQuake.QuakeStop: The begins to ramp down towards stopping.'
        quaking = false
        self.NextQuakeDelay()
        rampDownTarget = 0
        self.RampDown()
        respBaseStop.run(self.key)


    def RampUp(self):
        global currentShake
        global rampUpTarget
        if (currentShake >= rampUpTarget):
            print 'kverQuake.RampUp: The quake reaches it\'s new full strength of ',
            print rampUpTarget
            currentShake = rampUpTarget
        else:
            currentShake = (currentShake + 0.0050000000000000001)
            PtAtTimeCallback(self.key, 0.01, 5)
        print 'kverQuake.RampUp: currentShake = ',
        print currentShake
        PtSetCameraShake(1, currentShake)


    def RampDown(self):
        global currentShake
        global rampDownTarget
        if (currentShake <= rampDownTarget):
            print 'kverQuake.RampDown: The quake reaches it\'s new dampered strength of ',
            print rampDownTarget
            currentShake = rampDownTarget
        else:
            currentShake = (currentShake - 0.0050000000000000001)
            PtAtTimeCallback(self.key, 0.01, 6)
        print 'kverQuake.RampDown: currentShake = ',
        print currentShake
        if (currentShake == 0):
            print 'kverQuake.RampDown: Turning the camera shake COMPLETELY off.'
            PtSetCameraShake(0, 0)
        else:
            PtSetCameraShake(1, currentShake)


    def OnTimer(self, id):
        if (id == 1):
            if (sdlCanQuake.value.getValue() == 0):
                print 'kverQuake.OnTimer: A quake would happen, but Esher is speaking.'
                self.NextQuakeDelay()
            else:
                self.QuakeStart()
        elif (id == 2):
            peakStrength = whrandom.uniform(peakStrengthMin, peakStrengthMax)
            print 'OnTimer: The quake peak of ',
            print peakStrength,
            print ' starts.'
            rampUpTarget = peakStrength
            self.RampUp()
            respPeakStart.run(self.key)
        elif (id == 3):
            baseStrength = whrandom.uniform(baseStrengthMin, baseStrengthMax)
            print 'OnTimer: The quake peak ends. Ramping down to a base of ',
            print baseStrength
            rampDownTarget = baseStrength
            self.RampDown()
            respPeakStop.run(self.key)
        elif (id == 4):
            self.QuakeStop()
        elif (id == 5):
            self.RampUp()
        elif (id == 6):
            self.RampDown()


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global quaking
        value = ageSDL[sdlCanQuake][0]
        print 'kverQuake.OnSDLNotify: CanQuake = ',
        print value,
        print ' quaking = ',
        print quaking
        if quaking:
            print '\tSomehow, the sound of Esher\'s voice calms the groaning earth.'
            PtClearTimerCallbacks(self.key)
            self.QuakeStop()


    def OnLinkFX(self, linkingIn, starting):
        global quaking
        if ((not (linkingIn)) and (not (starting))):
            if quaking:
                print 'kverQuake.OnLinkFX: The earthquakes stop as you leave the age of Descent.'
                #PtSetCameraShake(0, 0)
                respBaseStop.run(self.key)
                respPeakStop.run(self.key)
            else:
                print 'kverQuake.OnLinkFX: It wasn\'t quaking when you left the age of Descent.'


    def OnBackdoorMsg(self, target, param):
        if (target == 'quake'):
            if (param == 'start'):
                PtClearTimerCallbacks(self.key)
                self.QuakeStart()
            elif (param == 'stop'):
                print 'kverQuake.Backdoor: The quake stops.'
                self.QuakeStop()


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



