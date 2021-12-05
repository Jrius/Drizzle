"""
not finished

will have to register how many players there are in each regions, get their weight and add it to the pillar
#"""


from Plasma import *
from PlasmaTypes import *
import string
weightStates = ['none', 'one', 'two']
sdlWeight1 = ptAttribString(1, 'SDL: weight button 1')
sdlWeight2 = ptAttribString(2, 'SDL: weight button 2')
sdlWeight3 = ptAttribString(3, 'SDL: weight button 3')
sdlWeight4 = ptAttribString(4, 'SDL: weight button 4')
sdlRegister = ptAttribString(5, 'SDL: register weight')
sdlWhereSlate = ptAttribString(6, 'SDL: which pedestal slate at')
sdlDroppedSlate = ptAttribString(7, 'SDL: slate is dropped')
actWeights = ptAttribActivatorList(8, 'clk: weight buttons')
actRegister = ptAttribActivator(9, 'clk: register button')
actPedestal = ptAttribActivator(10, 'rgn snsr: arena pedestal')
actScales = ptAttribActivator(11, 'rgn snsr: scales')
respWeights = ptAttribResponderList(12, 'resp: weight buttons', statelist=weightStates, byObject=1)
respRegister = ptAttribResponder(13, 'resp: register weight', ['press', 'unpress'])
respPedestal = ptAttribResponder(14, 'resp: arena pedestal', ['LowToMed', 'MedToHigh', 'HighToMed', 'MedToLow', 'LowToHigh', 'HighToLow'])
respScalesPlate = ptAttribResponder(15, 'resp: scales plate', ['down', 'up'])
respScalesLights = ptAttribResponder(16, 'resp: scales lights', ['PlayerOnlyEnter', 'PlayerOnlyExit', 'PlayerSlateEnter', 'PlayerSlateExit', 'SlateOnlyEnter', 'SlateOnlyExit'])
actBleacherLeft = ptAttribActivator(17, 'rgn snsr: left bleacher')
actBleacherRight = ptAttribActivator(18, 'rgn snsr: right bleacher')
respBleacherLeft = ptAttribResponder(19, 'resp: left bleacher', ['on', 'off'])
respBleacherRight = ptAttribResponder(20, 'resp: right bleacher', ['on', 'off'])
byteWeight1 = 0
byteWeight2 = 0
byteWeight3 = 0
byteWeight4 = 0
intRegister = 0
byteWhereSlate = 0
boolDroppedSlate = 1
listActWeights = []
listRespWeights = []
listSDLVals = []
listSDLNames = [sdlWeight1, sdlWeight2, sdlWeight3, sdlWeight4]
BtnNum = 0
PedestalPos = None
PedInMotion = 0
PlayerPos = 0
RegisterBtn = 0
SlateInMotion = 0
WaitForBahro = 0
kArenaPedestal = 104
kWeight1 = 100
kWeight2 = 75
kWeight3 = 50
kWeight4 = 25
kPedestalWeight = 225
kSlateWeight = 25
kPlayerWeight = 150
registeredAv = 0 ## num of av on ped
registeredAvScale = 0 ## num of av on scale

class lakiArena(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6364
        version = 6
        self.version = version
        print 'lakiArena v. ',
        print version,
        print '.0'


    def OnServerInitComplete(self):
        global PedInMotion
        global byteWeight1
        global byteWeight2
        global byteWeight3
        global byteWeight4
        global intRegister
        global byteWhereSlate
        global boolDroppedSlate
        global listSDLVals
        global listActWeights
        global listRespWeights
        global PedestalPos
        ageSDL = PtGetAgeSDL()
        for var in (
                        sdlWeight1,
                        sdlWeight2,
                        sdlWeight3,
                        sdlWeight4,
                        sdlRegister,
                    ):
            ageSDL.sendToClients(var.value)
            ageSDL.setFlags(var.value, 1, 1)
            ageSDL.setNotify(self.key, var.value, 0.0)
        byteWeight1 = ageSDL[sdlWeight1.value][0]
        byteWeight2 = ageSDL[sdlWeight2.value][0]
        byteWeight3 = ageSDL[sdlWeight3.value][0]
        byteWeight4 = ageSDL[sdlWeight4.value][0]
        intRegister = ageSDL[sdlRegister.value][0]
        #byteWhereSlate = ageSDL[sdlWhereSlate.value][0]
        #boolDroppedSlate = ageSDL[sdlDroppedSlate.value][0]
        print 'lakiArena.OnAgeDataInitialized(): ',
        print sdlWeight1.value,
        print ' is set to ',
        print byteWeight1
        print 'lakiArena.OnAgeDataInitialized(): ',
        print sdlWeight2.value,
        print ' is set to ',
        print byteWeight2
        print 'lakiArena.OnAgeDataInitialized(): ',
        print sdlWeight3.value,
        print ' is set to ',
        print byteWeight3
        print 'lakiArena.OnAgeDataInitialized(): ',
        print sdlWeight4.value,
        print ' is set to ',
        print byteWeight4
        print 'lakiArena.OnAgeDataInitialized(): ',
        print sdlRegister.value,
        print ' is set to ',
        print intRegister
        #print 'lakiArena.OnAgeDataInitialized(): ',
        #print sdlWhereSlate.value.getName(),
        #print ' is set to ',
        #print byteWhereSlate
        #print 'lakiArena.OnAgeDataInitialized(): ',
        #print sdlDroppedSlate.value.getName(),
        #print ' is set to ',
        #print boolDroppedSlate
        listSDLVals = [byteWeight1, byteWeight2, byteWeight3, byteWeight4]
        for act in actWeights.value:
            thisAct = act.getName()
            listActWeights.append(thisAct)
        for resp in respWeights.value:
            thisResp = resp.getName()
            if (thisResp not in listRespWeights):
                listRespWeights.append(thisResp)
        print 'lakiArena.OnAgeDataInitialized(): listRespWeights = ',
        print listRespWeights
        n = 0
        for resp in listRespWeights:
            if (ageSDL[listSDLNames[n].value][0] == 2):
                respWeights.run(self.key, objectName=resp, state=weightStates[(listSDLVals[n] - 1)], fastforward=1)
            respWeights.run(self.key, objectName=resp, state=weightStates[listSDLVals[n]], fastforward=1)
            n += 1
        if (intRegister == self.calcWeight()):
            respPedestal.run(self.key, state='LowToMed', fastforward=1)
            PedestalPos = 'med'
        elif (intRegister > self.calcWeight()):
            respPedestal.run(self.key, state='MedToHigh', fastforward=1)
            PedestalPos = 'high'
        elif (intRegister < self.calcWeight()):
            respPedestal.run(self.key, state='MedToLow', fastforward=1)
            PedestalPos = 'low'

        # release arena blockers
        respBleacherLeft.run(self.key, state='off')
        respBleacherRight.run(self.key, state='off')


        print 'lakiArena.OnPlayerSpawned(): Weight = ',
        print self.calcWeight()
        PedInMotion = 0
        PtAtTimeCallback(self.key, 1, 1)


    def OnTimer(self, id):
        global intRegister
        global PedInMotion
        global RegisterBtn
        if (id == 1):
            print 'lakiArena.OnTimer(): callback on init, will now check weight'
            if (intRegister != self.calcWeight()):
                self.updatePillar()
        elif (id == 2):
            if (not (PedInMotion)):
                self.updatePillar()
        #elif (id == 3):
        #    PtSetCamCutAlways(1)
        #    PtSetCameraShake(1, 0.14999999999999999)
        #elif (id == 4):
        #    PtSetCameraShake(1, 0.14999999999999999)
        elif (id == 5):
            respRegister.run(self.key, state='press')
        elif (id == 6):
            respRegister.run(self.key, state='unpress')
            RegisterBtn = 0


    def OnNotify(self, state, id, events):
        global PlayerPos
        global PedInMotion
        global RegisterBtn
        global WaitForBahro
        global BtnNum
        global byteWeight1
        global byteWeight2
        global byteWeight3
        global byteWeight4
        global intRegister
        global SlateInMotion
        global boolDroppedSlate
        global registeredAv
        global registeredAvScale
        if (not (state)):
            return
        ageSDL = PtGetAgeSDL()
        if (id == actPedestal.id):
            for event in events:
                if (event[0] == kCollisionEvent) and PtWasLocallyNotified(self.key):
                    if (event[1] == 1):
                        registeredAv += 1
                        print 'lakiArena.OnNotify(): some player is now on Arena pillar'
                    else:
                        registeredAv -= 1
                        print 'lakiArena.OnNotify(): some player is now off Arena pillar'
                        #PtSendPythonNotify('Enable', 'SlateAction', self.key)
                    print 'lakiArena.OnNotify(): new Weight = ',
                    print self.calcWeight()
                    if self.sceneobject.isLocallyOwned() and ((not (PedInMotion)) and (not (RegisterBtn))):
                        self.updatePillar()
        if (id == actWeights.id):
            for event in events:
                if (event[0] == kFacingEvent):
                    xBtn = event[3]
                    BtnName = xBtn.getName()
                    BtnNum = (string.atoi(BtnName[-1:]) - 1)
                    oldVal = ageSDL[listSDLNames[BtnNum].value][0]
                    if (oldVal == 2):
                        newVal = 0
                    else:
                        newVal = (oldVal + 1)
                    if self.sceneobject.isLocallyOwned():
                        ageSDL[listSDLNames[BtnNum].value] = (newVal,)
        if (id == actRegister.id):
            if (not (PedInMotion)):
                RegisterBtn = 1
                respRegister.run(self.key, state='press')
        if (id == respWeights.id):
            pass
        if (id == respRegister.id):
            if PedInMotion:
                return
            registerVal = ((((byteWeight1 * kWeight1) + (byteWeight2 * kWeight2)) + (byteWeight3 * kWeight3)) + (byteWeight4 * kWeight4))
            if (registerVal != intRegister):
                ageSDL[sdlRegister.value] = (registerVal,)
            elif RegisterBtn:
                PtAtTimeCallback(self.key, 0.4, 6)
        if (id == respPedestal.id):
            PedInMotion = 0
            #if (PlayerPos == 1):
            #    PtSetCameraShake(0, 0)
            #    PtSendPythonNotify('Enable', 'SlateAction', self.key)
            #if ((not (PtIsExpertMode())) and PlayerPos):
            #    PtSetCamCutAlways(0)
            #    PtClosestNode()
            #    PtEnableInput(1)
            if SlateInMotion:
                SlateInMotion = 0
                self.updatePillar()
            else:
                respRegister.run(self.key, state='unpress')
                RegisterBtn = 0
        if (id == actScales.id):
            for event in events:
                if (event[0] == kCollisionEvent):
                    if PtWasLocallyNotified(self.key):
                        if (event[1] == 1):
                            registeredAvScale += 1
                            print 'lakiArena.OnNotify(): Player is now on Arena scales'
                            respScalesPlate.run(self.key, state='down')
                            print 'boolDroppedSlate = ',
                            print boolDroppedSlate
                            if (not (boolDroppedSlate)):
                                respScalesLights.run(self.key, state='PlayerSlateEnter')
                            else:
                                respScalesLights.run(self.key, state='PlayerOnlyEnter')
                        else:
                            registeredAvScale -= 1
                            print 'lakiArena.OnNotify(): Player is now off Arena scales'
                            if registeredAvScale == 0:
                                respScalesPlate.run(self.key, state='up')
                            if (not (boolDroppedSlate)):
                                respScalesLights.run(self.key, state='PlayerSlateExit')
                            else:
                                respScalesLights.run(self.key, state='PlayerOnlyExit')
        """if (id == actBleacherLeft.id):
            for event in events:
                if (event[0] == kCollisionEvent):
                    if (PtFindAvatar(events) == PtGetLocalAvatar()):
                        if (event[1] == 1):
                            respBleacherLeft.run(self.key, state='on')
                        else:
                            respBleacherLeft.run(self.key, state='off')
        if (id == actBleacherRight.id):
            for event in events:
                if (event[0] == kCollisionEvent):
                    if (PtFindAvatar(events) == PtGetLocalAvatar()):
                        if (event[1] == 1):
                            respBleacherRight.run(self.key, state='on')
                        else:
                            respBleacherRight.run(self.key, state='off') #"""


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global byteWeight1
        global byteWeight2
        global byteWeight3
        global byteWeight4
        global intRegister
        global PedInMotion
        global listSDLVals
        global listRespWeights
        global BtnNum
        global boolDroppedSlate
        global PlayerPos
        global SlateInMotion
        global byteWhereSlate
        global WaitForBahro
        print 'lakiArena.OnSDLNotify(): SDL for ',
        print VARname,
        print ' changed to',
        ageSDL = PtGetAgeSDL()
        newVal = ageSDL[VARname][0]
        print newVal
        if (VARname == sdlWeight1.value):
            byteWeight1 = newVal
            isBtn = 1
        elif (VARname == sdlWeight2.value):
            byteWeight2 = newVal
            isBtn = 1
        elif (VARname == sdlWeight3.value):
            byteWeight3 = newVal
            isBtn = 1
        elif (VARname == sdlWeight4.value):
            byteWeight4 = newVal
            isBtn = 1
        elif (VARname == sdlRegister.value):
            intRegister = newVal
            isBtn = 0
            if (not (PedInMotion)):
                self.updatePillar()
        else:
            isBtn = 0
        if isBtn:
            listSDLVals = [byteWeight1, byteWeight2, byteWeight3, byteWeight4]
            print 'lakiArena.OnSDLNotify(): listSDLVals now = ',
            print listSDLVals
            respWeights.run(self.key, objectName=listRespWeights[BtnNum], state=weightStates[listSDLVals[BtnNum]])
            isBtn = 0
            BtnNum = 0
        if (VARname == sdlDroppedSlate.value):
            boolDroppedSlate = newVal
            if (PlayerPos == 2):
                print 'On scales, and boolDroppedSlate now = ',
                print boolDroppedSlate
                if (not (boolDroppedSlate)):
                    respScalesLights.run(self.key, state='SlateOnlyEnter')
                else:
                    respScalesLights.run(self.key, state='SlateOnlyExit')
            elif (not (PedInMotion)):
                self.updatePillar()
            else:
                SlateInMotion = 1
        if (VARname == sdlWhereSlate.value):
            byteWhereSlate = newVal
            if (not (PedInMotion)):
                if (PlayerPos == 1):
                    self.updatePillar()
                elif (byteWhereSlate == kArenaPedestal):
                    WaitForBahro = 1
            else:
                SlateInMotion = 1


    def updatePillar(self, ff = 0):
        global PedInMotion
        global PedestalPos
        global intRegister
        global PlayerPos
        global RegisterBtn
        if (PedInMotion and (not (ff))):
            return
        print 'lakiArena.updatePillar(): PedestalPos currently at: ',
        print PedestalPos
        if (intRegister == self.calcWeight()):
            if (PedestalPos == 'low'):
                respPedestal.run(self.key, state='LowToMed', fastforward=ff)
                PedInMotion = 1
            elif (PedestalPos == 'high'):
                respPedestal.run(self.key, state='HighToMed', fastforward=ff)
                PedInMotion = 1
            PedestalPos = 'med'
        elif (intRegister > self.calcWeight()):
            if (PedestalPos == 'low'):
                respPedestal.run(self.key, state='LowToHigh', fastforward=ff)
                PedInMotion = 1
            elif (PedestalPos == 'med'):
                respPedestal.run(self.key, state='MedToHigh', fastforward=ff)
                PedInMotion = 1
            PedestalPos = 'high'
        elif (intRegister < self.calcWeight()):
            if (PedestalPos == 'med'):
                respPedestal.run(self.key, state='MedToLow', fastforward=ff)
                PedInMotion = 1
            elif (PedestalPos == 'high'):
                respPedestal.run(self.key, state='HighToLow', fastforward=ff)
                PedInMotion = 1
            PedestalPos = 'low'
        if PedInMotion:
            print 'lakiArena.updatePillar(): PedestalPos changing to: ',
            print PedestalPos
            #if ((PlayerPos == 1) and (not (ff))):
            #    PtSendPythonNotify('Disable', 'SlateAction', self.key)
            #    if PtIsExpertMode():
            #        PtAtTimeCallback(self.key, 0.4, 4)
            #if ((not (PtIsExpertMode())) and (PlayerPos and (not (ff)))):
            #    PtAtTimeCallback(self.key, 0.39, 3)
            #    PtEnableInput(0)
            if ((not (RegisterBtn)) and (not (ff))):
                RegisterBtn = 1
                if (registeredAv):
                    PtAtTimeCallback(self.key, 0.4, 5)
                else:
                    respRegister.run(self.key, state='press')
        elif RegisterBtn:
            RegisterBtn = 0
            respRegister.run(self.key, state='unpress')


    def calcWeight(self):
        global PlayerPos
        global boolDroppedSlate
        global byteWhereSlate
        global registeredAv
        thisWeight = kPedestalWeight
        thisWeight += kPlayerWeight * registeredAv
        return thisWeight


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



