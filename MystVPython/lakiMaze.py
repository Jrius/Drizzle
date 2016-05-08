"""
Lots of rewriting here. Works offline, let's hope for the best online.
#"""

from Plasma import *
from PlasmaTypes import *
import string
import copy
import time
SDLDoor =           ptAttribString(1, 'SDL: exit door')
ActButtons =        ptAttribActivatorList(2, 'clk: list of button-lights')
RespButtons =       ptAttribResponderList(3, 'resp: list of button-lights', byObject=1)
RespDoor =          ptAttribResponder(4, 'resp: exit door', ['open', 'close'])
ActDoorOpenBtn =    ptAttribActivator(5, 'act: door open btn')
RespDoorOpenBtn =   ptAttribResponder(6, 'resp: door open btn')
ActLevLwrUse =      ptAttribActivator(7, 'drag: lower lever')
ActLevUprUse =      ptAttribActivator(8, 'drag: upper lever')
ActLevLwrSet =      ptAttribActivator(9, 'anm evt: lower lever set')
ActLevUprSet =      ptAttribActivator(10, 'anm evt: upper lever set')
RespLevLwr =        ptAttribResponder(11, 'resp: lower lever', ['set', 'return'])
RespLevUpr =        ptAttribResponder(12, 'resp: upper lever', ['set', 'return'])
ActPlateLwr =       ptAttribActivator(13, 'rgn snsr: lower pressure plate')
ActPlateUpr =       ptAttribActivator(14, 'rgn snsr: upper pressure plate')
RespElev =          ptAttribResponder(15, 'resp: elevator', ['up', 'down'])
SDLElevLev =        ptAttribString(16, 'SDL: elev levers')
RespLevAtStart =    ptAttribResponder(17, 'resp: elev levers up at start', ['lower', 'upper'])
ActElevAtTop =      ptAttribActivator(18, 'anm evt: elevator at top')
ActOnElev =         ptAttribActivator(19, 'rgn snsr: player on elevator')
ActGateEscape =     ptAttribActivator(20, 'rgn snsr: gate escape')
SDLGate1 =          ptAttribString(21, 'SDL: arena gate 1')
SDLGate2 =          ptAttribString(22, 'SDL: arena gate 2')
RespBigBlocker =    ptAttribResponder(23, 'resp: elev big blocker', ['on', 'off'])
boolDoor = 0
btnNum = 0
btnList = []
respList = []
currentList = [0, 0, 0, 0]
PlayerOnPlate = 0
BahroOnPlate = 0
leverSet = 0
byteLev = 0
elevRising = 0
AtTop = 0
AtBottom = 1
PlayerOnElev = 0
boolGate1 = 0
boolGate2 = 0
WaitForBahro = 0
respBaseName = 'cRespMazeButton'
solutionList = [1, 9, 13, 14]
kExitDoorSecs = 7
kBahroHackSecs = 0.5
kExitDoorID = 1
kBahroHackID = 2
kPlateUprID = 4
kPlateUprSecs = 7

ignoreLevEvents = false
ignoreTimer = 3
ignoreID = 6

soElevatorParent = None

class lakiMaze(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6368
        version = 11
        self.version = version
        print 'lakiMaze v. ',
        print version,
        print '.0'


    def OnFirstUpdate(self):
        global btnList
        global respList
        global boolDoor
        global byteLev
        global leverSet
        global boolGate1
        global boolGate2
        global soElevatorParent
        global elevRising
        global AtTop
        global AtBottom
        ageSDL = PtGetAgeSDL()
        for var in (
                        SDLDoor,
                        SDLElevLev,
                        SDLGate1,
                        SDLGate2,
                    ):
            ageSDL.sendToClients(var.value)
            ageSDL.setFlags(var.value, 1, 1)
            ageSDL.setNotify(self.key, var.value, 0.0)
        for button in ActButtons.value:
            tempName = button.getName()
            btnList.append(tempName)
        print 'btnList = ',
        print btnList
        for resp in RespButtons.value:
            tempResp = resp.getName()
            respList.append(tempResp)
        print 'respList = ',
        print respList
        RespDoor.run(self.key, state='close', fastforward=1)
        boolDoor = ageSDL[SDLDoor.value][0]
        if boolDoor:
            ageSDL[SDLDoor.value] = (0,)
        boolGate1 = ageSDL[SDLGate1.value][0]
        boolGate2 = ageSDL[SDLGate2.value][0]
        
        # elev
        if PtIsSinglePlayerMode():
            RespBigBlocker.run(self.key, "off", fastforward=1)
            RespElev.run(self.key, "up", fastforward=1)
            AtBottom = 0
            AtTop = 1
            elevRising = 1
        else:
            RespBigBlocker.run(self.key, state='off')
            RespElev.run(self.key, state='down', fastforward=1)
        byteLev = ageSDL[SDLElevLev.value][0]
        print 'lakiMaze.OnAgeDataInitialized(): ',
        print SDLElevLev.value,
        print ' is set to ',
        print byteLev
        if (byteLev == 1):
            RespLevAtStart.run(self.key, state='lower', fastforward=1)
        elif (byteLev == 2):
            RespLevAtStart.run(self.key, state='upper', fastforward=1)
        leverSet = byteLev
        
        soElevatorParent = PtFindSceneobject("MazeElevPillar", "Laki")


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global boolDoor
        global byteLev
        global leverSet
        global boolGate1
        global boolGate2
        global ignoreLevEvents
        ageSDL = PtGetAgeSDL()
        changedVar = VARname
        value = ageSDL[VARname][0]
        if (changedVar == SDLDoor.value):
            boolDoor = value
            if boolDoor:
                ActDoorOpenBtn.disableActivator()
                RespDoor.run(self.key, state='open')
                PtAtTimeCallback(self.key, kExitDoorSecs, kExitDoorID)
                i = 0
                for btn in ActButtons.value:
                    print 'lakiMaze.OnSDLNotify(): disabling all button-light clickables'
                    ActButtons.value[i].disable()
                    i += 1
            else:
                RespDoor.run(self.key, state='close')
        elif (changedVar == SDLElevLev.value):
            byteLev = value
            if (byteLev == 1):
                RespLevLwr.run(self.key, state='set')
                print 'lakiMaze.OnSDLNotify(): Lower lever engaged'
                if (leverSet == 2):
                    RespLevUpr.run(self.key, state='return')
                    print 'lakiMaze.OnSDLNotify(): and Upper lever released'
                leverSet = 1
            elif (byteLev == 2):
                RespLevUpr.run(self.key, state='set')
                print 'lakiMaze.OnSDLNotify(): Upper lever engaged'
                if (leverSet == 1):
                    RespLevLwr.run(self.key, state='return')
                    print 'lakiMaze.OnSDLNotify(): and Lower lever released'
                leverSet = 2
            elif (not (byteLev)):
                if (leverSet == 1):
                    RespLevLwr.run(self.key, state='return')
                    print 'lakiMaze.OnSDLNotify(): Lower lever released'
                elif (leverSet == 2):
                    RespLevUpr.run(self.key, state='return')
                    print 'lakiMaze.OnSDLNotify(): Upper lever released'
                leverSet = 0
        elif (changedVar == SDLGate1.value):
            boolGate1 = value
        elif (changedVar == SDLGate2.value):
            boolGate2 = value


    def OnNotify(self, state, id, events):
        global leverSet
        global PlayerOnPlate
        global elevRising
        global AtTop
        global AtBottom
        global BahroOnPlate
        global PlayerOnElev
        global WaitForBahro
        global btnNum
        global boolGate1
        global boolGate2
        global ignoreLevEvents
        if ((id == ActLevLwrUse.id) and state):
            pass
        elif ((id == ActLevUprUse.id) and state):
            pass
        elif ((id == ActLevLwrSet.id) and state):
            # careful: this might fire even if the lever is unsetting.
            # that's why we use ignoreLevEvents var
            if (leverSet != 1) and not ignoreLevEvents:
                if self.sceneobject.isLocallyOwned():
                    ageSDL = PtGetAgeSDL()
                    ageSDL[SDLElevLev.value] = (1,)
        elif ((id == ActLevUprSet.id) and state):
            if (leverSet != 2) and not ignoreLevEvents:
                if self.sceneobject.isLocallyOwned():
                    ageSDL = PtGetAgeSDL()
                    ageSDL[SDLElevLev.value] = (2,)
        elif ((id == ActPlateLwr.id) and state):
            ageSDL = PtGetAgeSDL()
            for event in events:
                if (event[0] == kCollisionEvent):
                    if PtWasLocallyNotified(self.key):
                        if (event[1] == 1):
                            PlayerOnPlate = 1
                            if (leverSet == 1):
                                ignoreLevEvents = true
                                if PtFindAvatar(events) == PtGetLocalAvatar():
                                    ageSDL[SDLElevLev.value] = (0,)
                                if PtIsSinglePlayerMode():
                                    RespElev.run(self.key, state="down")
                                    elevRising = 0
                                    AtBottom = 0
                                    AtTop = 0
                                else:
                                    RespElev.run(self.key, state='up')
                                    elevRising = 1
                                    AtTop = 0
                                    AtBottom = 0
                                    RespBigBlocker.run(self.key, state='on')
                        else:
                            PlayerOnPlate = 0
                            if PtWasLocallyNotified(self.key):
                                if PtIsSinglePlayerMode():
                                    if AtBottom:
                                        PtAtTimeCallback(self.key, 5, 150)
                                    elif not AtTop:
                                        PtAtTimeCallback(self.key, 0, 150)
                                else:
                                    if elevRising:
                                        RespElev.run(self.key, state='down')
                                        RespBigBlocker.run(self.key, state='on')
                                        elevRising = 0
                                        AtTop = 0
                        print 'PlayerOnPlate = ',
                        print PlayerOnPlate
        elif ((id == ActPlateUpr.id) and state):
            ageSDL = PtGetAgeSDL()
            for event in events:
                if (event[0] == kCollisionEvent):
                    if PtWasLocallyNotified(self.key):
                        if (event[1] == 1):
                            PlayerOnPlate = 2
                            if (leverSet == 2):
                                ignoreLevEvents = true
                                if PtFindAvatar(events) == PtGetLocalAvatar():
                                    ageSDL[SDLElevLev.value] = (0,)
                                if PtIsSinglePlayerMode():
                                    RespElev.run(self.key, state="down")
                                    elevRising = 0
                                    AtBottom = 0
                                    AtTop = 0
                                else:
                                    RespElev.run(self.key, state='up')
                                    elevRising = 1
                                    AtTop = 0
                                    AtBottom = 0
                                    RespBigBlocker.run(self.key, state='on')
                        else:
                            PlayerOnPlate = 0
                            if PtWasLocallyNotified(self.key):
                                if PtIsSinglePlayerMode():
                                    if AtBottom:
                                        PtAtTimeCallback(self.key, 5, 150)
                                    elif not AtTop:
                                        PtAtTimeCallback(self.key, 0, 150)
                                else:
                                    if elevRising:
                                        RespElev.run(self.key, state='down')
                                        RespBigBlocker.run(self.key, state='on')
                                        elevRising = 0
                                        AtTop = 0
                        print 'PlayerOnPlate = ',
                        print PlayerOnPlate
        elif ((id == ActButtons.id) and state):
            for event in events:
                if (event[0] == kFacingEvent):
                    xEvent = event[3]
                    btnName = xEvent.getName()
                    print 'lakiMaze.OnNotify(): ',
                    print btnName,
                    print 'pressed.'
                    btnNum = (string.atoi(btnName[10:]) - 1)
                    thisResp = (respBaseName + str((btnNum + 1)))
                    RespButtons.run(self.key, objectName=thisResp)
        elif (id == RespButtons.id):
            self.CheckButtons()
        elif (id == RespDoor.id):
            i = 0
            print 'lakiMaze.OnNotify(): enabling all button-light clickables'
            for btn in ActButtons.value:
                ActButtons.value[i].enable()
                i += 1
            ActDoorOpenBtn.enableActivator()
        elif ((id == ActDoorOpenBtn.id) and state):
            RespDoorOpenBtn.run(self.key)
        elif (id == RespDoorOpenBtn.id):
            if self.sceneobject.isLocallyOwned():
                ageSDL = PtGetAgeSDL()
                ageSDL[SDLDoor.value] = (1,)
        elif (id == RespElev.id):
            if not elevRising:
                # aka ArrivedAtBottom
                RespBigBlocker.run(self.key, state='off')
                elevRising = 0
                AtTop = 0
                AtBottom = 1
                if not PtIsSinglePlayerMode():
                    ignoreLevEvents = false
        elif ((id == ActElevAtTop.id) and state):
            if elevRising:
                RespBigBlocker.run(self.key, state='off')
                elevRising = 0
                AtTop = 1
                if PtIsSinglePlayerMode():
                    ignoreLevEvents = false
            else:
                AtTop = 0
        elif ((id == ActOnElev.id) and state and PtWasLocallyNotified(self.key)):
            for event in events:
                if (event[0] == kCollisionEvent):
                    if PtFindAvatar(events) == PtGetLocalAvatar():
                        if (event[1] == 1):
                            PlayerOnElev = 1
                            PtAttachObject(PtFindAvatar(events), soElevatorParent)
                            #if PtIsSinglePlayerMode():
                            #    if AtTop or elevRising:
                            #        elevRising = 0
                            #        AtTop = 0
                            #        RespElev.run(self.key, state="down")
                        else:
                            PlayerOnElev = 0
                            PtDetachObject(PtFindAvatar(events), soElevatorParent)
                            #if PtIsSinglePlayerMode():
                            #    if not AtTop:
                            #        elevRising = 1
                            #        AtBottom = 0
                            #        RespElev.run(self.key, state="up")
                        print 'PlayerOnElev = ',
                        print PlayerOnElev
        elif ((id == ActGateEscape.id) and state):
            if not PtWasLocallyNotified(self.key) or PtFindAvatar(events) != PtGetLocalAvatar(): return
            if ((not (boolGate1)) and (not (boolGate2))):
                ageSDL = PtGetAgeSDL()
                ageSDL[SDLGate1.value] = (1,)


    def CheckButtons(self):
        global btnNum
        global currentList
        global boolDoor
        print 'lakiMaze.CheckButtons(): called.'
        checkNum = (btnNum + 1)
        currentList.append(checkNum)
        while ((len(currentList) > len(solutionList))):
            del currentList[0]
        print ('solution list: ' + str(solutionList))
        print ('current list: ' + str(currentList))
        ageSDL = PtGetAgeSDL()
        if (currentList == solutionList):
            if self.sceneobject.isLocallyOwned():
                ageSDL[SDLDoor.value] = (1,)
        elif boolDoor:
            if self.sceneobject.isLocallyOwned():
                ageSDL[SDLDoor.value] = (0,)


    def OnTimer(self, id):
        global WaitForBahro
        global elevRising
        global ignoreID
        global AtTop
        global AtBottom
        if (id == kExitDoorID):
            print 'lakiMaze.OnTimer(): door timer expired, shutting door now.'
            if self.sceneobject.isLocallyOwned():
                ageSDL = PtGetAgeSDL()
                ageSDL[SDLDoor.value] = (0,)
        elif (id == kBahroHackID):
            WaitForBahro = 0
        elif (id == kPlateUprID):
            RespElev.run(self.key, state='down')
            RespBigBlocker.run(self.key, state='on')
        elif id == ignoreID:
            ignoreLevEvents = false
            PtAtTimeCallback(self.key, ignoreTimer, ignoreID)
        elif id == 150: # re-raise elev
            RespElev.run(self.key, state="up")
            elevRising = true
            AtTop = 0
            AtBottom = 0


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



