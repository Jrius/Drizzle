from Plasma import *
from PlasmaTypes import *
import string
actTurnedOn = ptAttribActivator(1, 'AnimEvnt: Turned On')
respTrue = ptAttribResponder(2, 'resp: turn on')
respFalse = ptAttribResponder(3, 'resp: turn off')
AgeSDLgene = ptAttribString(4, 'AgeSDL: generator')
AgeSDLfan = ptAttribString(5, 'AgeSDL: fan')
respWiggleFanCrank = ptAttribResponder(6, 'resp: wiggle fan crank')
respLaddersDown = ptAttribResponder(7, 'resp: ladders down')
respLaddersUp = ptAttribResponder(8, 'resp: ladders up')
actTurnedOff = ptAttribActivator(9, 'AnimEvnt: Turned Off')
respRewindCrankAnim = ptAttribResponder(10, 'resp: RewindCrankAnim')
actFanClickable = ptAttribActivator(11, 'act: Fan Clickable')
FanCrankThatWiggles = ptAttribSceneobject(12, 'Fan Crank That Wiggles')
actFanDraggable = ptAttribActivator(13, 'act: Fan Draggable')
FanCrankThatDrags = ptAttribSceneobject(14, 'Fan Crank That Drags')
respaway = ptAttribResponder(15, 'resp: Move Wiggle Proxy')
respreturn = ptAttribResponder(16, 'resp: Return Wiggle Proxy')
#respSolutionCam = ptAttribResponder(17, 'resp: Novice Solution Cam', ['run', 'reset'])
respSolutionCam = ptAttribResponder(17, 'resp: Novice Solution Cam')
puzzlesolved = false
climbLadders = []
camSO = None

class dsntFanGenerator(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6206
        version = 9
        self.version = version
        print '__init__dsntFanGenerator v.',
        print version,
        print '.2'


    def OnServerInitComplete(self):
        global puzzlesolved
        global climbLadders
        global camSO
        ageSDL = PtGetAgeSDL()
        ageSDL.setFlags(AgeSDLgene.value, 1, 1)
        ageSDL.sendToClients(AgeSDLgene.value)
        ageSDL.setNotify(self.key, AgeSDLgene.value, 0.0)
        ageSDL.setFlags(AgeSDLfan.value, 1, 1)
        ageSDL.sendToClients(AgeSDLfan.value)
        ageSDL.setNotify(self.key, AgeSDLfan.value, 0.0)
        climbLadders = [PtFindSceneobject('Region.002_bottom', 'DescentMystV'), PtFindSceneobject('Region.002_top', 'DescentMystV')]
        camSO = PtFindSceneobject("Camera01", 'DescentMystV')
        if (len(respLaddersDown.value) == 0):
            print 'dsnt Generator: When I got here ',
            print AgeSDLgene.value,
            print ' value was ',
            print ageSDL[AgeSDLgene.value][0]
            if (ageSDL[AgeSDLgene.value][0] == 1):
                print '\tFastforwarding the generator to the ON state.'
                respTrue.run(self.key, fastforward=1)
            elif (ageSDL[AgeSDLgene.value][0] == 0):
                print '\tFastforwarding the generator to the OFF state, including the handle.'
                respFalse.run(self.key, fastforward=1)
                respRewindCrankAnim.run(self.key, fastforward=1)
        else:
            print 'dsnt Fan: When I got here ',
            print AgeSDLfan.value,
            print ' value was ',
            print ageSDL[AgeSDLfan.value][0]
            if (ageSDL[AgeSDLfan.value][0] == 1):
                print '\tFastforwarding the fan to the ON state.'
                respTrue.run(self.key, fastforward=1)
            elif (ageSDL[AgeSDLfan.value][0] == 0):
                print '\tFastforwarding the fan to the OFF state.'
                respFalse.run(self.key, fastforward=1)
                respRewindCrankAnim.run(self.key, fastforward=1)
            if ((ageSDL[AgeSDLgene.value][0] == 1) and (ageSDL[AgeSDLfan.value][0] == 1)):
                print '\tBoth the fan and the generator are on. Lowering the solution ladders.'
                respLaddersDown.run(self.key, fastforward=1)
                puzzlesolved = true
                for ladder in climbLadders:
                    ladder.physics.suppress(0)
            else:
                respLaddersUp.run(self.key, fastforward=1)
                for ladder in climbLadders:
                    ladder.physics.suppress(1)
            if (ageSDL[AgeSDLgene.value][0] == 1):
                print '\tdsntfangenerator.OnAgeDataInit: Wiggle crank disabled and away. Drag crank enabled.'
                actFanClickable.disable()
                FanCrankThatWiggles.value.draw.disable()
                respaway.run(self.key, fastforward=1)
                actFanDraggable.enable()
                FanCrankThatDrags.value.draw.enable()
            else:
                print '\tdsntfangenerator.OnAgeDataInit: Wiggle crank enabled and returned. Drag crank disabled.'
                actFanClickable.enable()
                FanCrankThatWiggles.value.draw.enable()
                respreturn.run(self.key, fastforward=1)
                actFanDraggable.disable()
                FanCrankThatDrags.value.draw.disable()


    def OnNotify(self, state, id, events):
        ageSDL = PtGetAgeSDL()
        if (not (state)):
            return
        if (not PtWasLocallyNotified(self.key)) or (PtFindAvatar(events) != PtGetLocalAvatar()): return
        if (len(respLaddersDown.value) == 0):
            if (id == actTurnedOn.id):
                ageSDL[AgeSDLgene.value] = (1,)
            elif (id == actTurnedOff.id):
                ageSDL[AgeSDLgene.value] = (0,)
                ageSDL[AgeSDLfan.value] = (0,)
        else:
            if (id == actTurnedOn.id):
                ageSDL[AgeSDLfan.value] = (1,)
            elif (id == actTurnedOff.id):
                ageSDL[AgeSDLfan.value] = (0,)
            if (id == actFanClickable.id):
                print 'The fan crank should wiggle.'


    def EnableDragCrank(self):
        print '\tWiggle crank disabled and away. Drag crank enabled.'
        actFanClickable.disable()
        FanCrankThatWiggles.value.draw.disable()
        respaway.run(self.key)
        actFanDraggable.enable()
        FanCrankThatDrags.value.draw.enable()


    def EnableClickCrank(self):
        print '\tWiggle crank enabled and returned. Drag crank disabled.'
        actFanClickable.enable()
        FanCrankThatWiggles.value.draw.enable()
        respreturn.run(self.key)
        actFanDraggable.disable()
        FanCrankThatDrags.value.draw.disable()


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global puzzlesolved
        ageSDL = PtGetAgeSDL()
        if (len(respLaddersDown.value) == 0):
            if (VARname == AgeSDLgene.value):
                print 'dsnt Generator.OnSDLNotify:'
                if (ageSDL[AgeSDLgene.value][0] == 1):
                    respTrue.run(self.key)
                    print '\tThe generator was just turned ON.'
                elif (ageSDL[AgeSDLgene.value][0] == 0):
                    respFalse.run(self.key)
                    print '\tThe generator was just turned OFF.'
        else:
            if (VARname == AgeSDLfan.value):
                print 'dsnt Fan.OnSDLNotify:'
                if (ageSDL[AgeSDLfan.value][0] == 1):
                    respTrue.run(self.key)
                    print '\tThe fan was just turned ON.'
                    PtAtTimeCallback(self.key, 2, 2)
                elif (ageSDL[AgeSDLfan.value][0] == 0):
                    respFalse.run(self.key)
                    print '\tThe fan was just turned OFF.'
                print '\tDisabling the draggable activator until fans speed up / slow down.'
                actFanDraggable.disable()
            if (VARname == AgeSDLgene.value):
                print 'The fan script heard the generator status change:'
                if (ageSDL[AgeSDLgene.value][0] == 1):
                    print '\tthe generator turned on above..'
                    self.EnableDragCrank()
                elif (ageSDL[AgeSDLgene.value][0] == 0):
                    print '\tthe generator turned off above. Rewinding the Fan_Draggable animation below.'
                    respRewindCrankAnim.run(self.key)
                    self.EnableClickCrank()
            if ((ageSDL[AgeSDLfan.value][0] == 1) and (ageSDL[AgeSDLgene.value][0] == 1)):
                print '\tThe Generator/Fan puzzle was just solved. Lowering the solution ladders.'
                respLaddersDown.run(self.key)
                puzzlesolved = true
                for ladder in climbLadders:
                    ladder.physics.suppress(0)
            elif puzzlesolved:
                print '\tThe Generator/Fan puzzle just became unsolved. Raising the solution ladders.'
                respLaddersUp.run(self.key)
                puzzlesolved = false
                for ladder in climbLadders:
                    ladder.physics.suppress(1)


    def OnTimer(self, id):
        if (id == 1):
            print '\tLadder fully dropped. Popping camera back to avatar POV.'
            #respSolutionCam.run(self.key, state='reset')
            ptCamera().restore(camSO.getKey())
        elif (id == 2):
            print '\tTrying to run the Novice Solution Cam.'
            #respSolutionCam.run(self.key, state='run')
            respSolutionCam.run(self.key)
            ptCamera().save(camSO.getKey())
            PtAtTimeCallback(self.key, 8, 1)


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



