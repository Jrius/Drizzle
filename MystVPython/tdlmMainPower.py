"""
Now using fancy anim event modifier

Instead of using 40 anim event modifier like I did for scope console, we now only have 6.
This means joysticks will reset when leaving the Age if the power isn't on.
#"""


from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *
import string
HorizSDL = ptAttribString(1, 'SDL: Horiz Percentage')
VertSDL = ptAttribString(2, 'SDL: Vert Percentage')
MainPowerSDL = ptAttribString(3, 'SDL: Main Power')
actHorizDrag = ptAttribActivator(4, 'act: Horiz Drag')
actVertDrag = ptAttribActivator(5, 'act: Vert Drag')
camera = ptAttribSceneobject(6, 'View camera')
actHorizUpdated = ptAttribActivator(7, 'deprecated')
actVertUpdated = ptAttribActivator(8, 'deprecated')
WhichPillar = ptAttribDropDownList(9, 'Which Pillar', ('Pillar01', 'Pillar03'))
HorizDragAnim = ptAttribAnimation(10, 'anim: Horiz Drag ')
VertDragAnim = ptAttribAnimation(11, 'anim: Vert Drag')
actHorizIn   = ptAttribActivator(12, 'AnimEvent: Horiz OK')
actVertIn    = ptAttribActivator(13, 'AnimEvent: Vert OK')
actHorizOut1 = ptAttribActivator(14, 'AnimEvent: Horiz out 1')
actVertOut1  = ptAttribActivator(15, 'AnimEvent: Vert out 1')
actHorizOut2 = ptAttribActivator(16, 'AnimEvent: Horiz out 2')
actVertOut2  = ptAttribActivator(17, 'AnimEvent: Vert out 2')
Solution01H = 0.9
Solution01V = 0.49
Solution01Threshold = 0.25 # originally .1 in MystV, we're increasing it since DynamicEnvMaps don't render the same way as DynamicCamMaps
Solution03H = 0.62
Solution03V = 0.78
Solution03Threshold = 0.25 # same
joyHOk = false
joyVOk = false

class tdlmMainPower(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6214
        version = 6
        self.version = version
        print '__init__tdlmMainPower v.',
        print version,
        print '.0'


    def OnFirstUpdate(self):
        global joyHOk
        global joyVOk
        ageSDL = PtGetAgeSDL()
        #ageSDL.sendToClients(HorizSDL.value)
        #ageSDL.setFlags(HorizSDL.value, 1, 1)
        #ageSDL.setNotify(self.key, HorizSDL.value, 0.0)
        #ageSDL.sendToClients(VertSDL.value)
        #ageSDL.setFlags(VertSDL.value, 1, 1)
        #ageSDL.setNotify(self.key, VertSDL.value, 0.0)
        ageSDL.sendToClients(MainPowerSDL.value)
        ageSDL.setFlags(MainPowerSDL.value, 1, 1)
        ageSDL.setNotify(self.key, MainPowerSDL.value, 0.0)
        print 'tdlmMainPower: When I got here:'
        if ageSDL[MainPowerSDL.value][0]:
            joyHOk = true
            joyVOk = true
            if WhichPillar.value == 'Pillar01':
                print "Pillar 1 power was on."
                HorizDragAnim.animation.skipToTime(Solution01H * (1 / 3.0))
                VertDragAnim .animation.skipToTime(Solution01V * (1 / 3.0))
            elif WhichPillar.value == 'Pillar03':
                print "Pillar 3 power was on."
                HorizDragAnim.animation.skipToTime(Solution03H * (1 / 3.0))
                VertDragAnim .animation.skipToTime(Solution03V * (1 / 3.0))


    def OnNotify(self, state, id, events):
        global joyHOk
        global joyVOk
        if not (PtFindAvatar(events) == PtGetLocalAvatar()) or not (PtWasLocallyNotified(self.key)): return
        if (not (state)):
            return
        if id == actHorizIn.id:
            joyHOk = true
            ageSDL = PtGetAgeSDL()
            if joyVOk and not ageSDL[MainPowerSDL.value][0]:
                print "Found correct solution, enabling power..."
                ageSDL[MainPowerSDL.value] = (1,)
        elif id == actVertIn.id:
            joyVOk = true
            ageSDL = PtGetAgeSDL()
            if joyHOk and not ageSDL[MainPowerSDL.value][0]:
                print "Found correct solution, enabling power..."
                ageSDL[MainPowerSDL.value] = (1,)
        elif id == actHorizOut1.id or id == actHorizOut2.id:
            joyHOk = false
            ageSDL = PtGetAgeSDL()
            if ageSDL[MainPowerSDL.value][0]:
                print "Joystick moved, disabling power..."
                ageSDL[MainPowerSDL.value] = (0,)
        elif id == actVertOut1.id or id == actVertOut2.id:
            joyVOk = false
            ageSDL = PtGetAgeSDL()
            if ageSDL[MainPowerSDL.value][0]:
                print "Joystick moved, disabling power..."
                ageSDL[MainPowerSDL.value] = (0,)


    def CheckSolution(self):
        global Solution01H
        global Solution01V
        global Solution01Threshold
        global Solution03H
        global Solution03V
        global Solution03Threshold
        print "ERROR ! This is legacy and should not be called !"
        return
        ageSDL = PtGetAgeSDL()
        if (WhichPillar.value == 'Pillar01'):
            SolutionH = Solution01H
            SolutionV = Solution01V
            SolutionThreshold = Solution01Threshold
        elif (WhichPillar.value == 'Pillar03'):
            SolutionH = Solution03H
            SolutionV = Solution03V
            SolutionThreshold = Solution03Threshold
        else:
            print 'tdlmMainPower: Error! I can\'t tell which Power Puzzle I\'m attached to.'
            return
        if (((SolutionH - SolutionThreshold) < ageSDL[HorizSDL.value][0]) and (ageSDL[HorizSDL.value][0] < (SolutionH + SolutionThreshold))):
            print 'Horizontal is correctly aligned in ',
            print WhichPillar.value,
            print '. Current = ',
            print ageSDL[HorizSDL.value][0]
            HorizSolved = true
        else:
            HorizSolved = false
            print '\tmin: ',
            print (SolutionH - SolutionThreshold),
            print ' Current: ',
            print ageSDL[HorizSDL.value][0],
            print ' Max: ',
            print (SolutionH + SolutionThreshold)
        if (((SolutionV - SolutionThreshold) < ageSDL[VertSDL.value][0]) and (ageSDL[VertSDL.value][0] < (SolutionV + SolutionThreshold))):
            print 'Vertical is correctly aligned in ',
            print WhichPillar.value,
            print '. Current = ',
            print ageSDL[VertSDL.value][0]
            VertSolved = true
        else:
            VertSolved = false
            print '\tmin: ',
            print (SolutionV - SolutionThreshold),
            print ' Current: ',
            print ageSDL[VertSDL.value][0],
            print ' Max: ',
            print (SolutionV + SolutionThreshold)
        if (HorizSolved and VertSolved):
            print '\tMain power turning on in ',
            print WhichPillar.value
            if ageSDL[MainPowerSDL.value][0] == 0:
                ageSDL[MainPowerSDL.value] = (1,)
        else:
            if ageSDL[MainPowerSDL.value][0]:
                ageSDL[MainPowerSDL.value] = (0,)


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



