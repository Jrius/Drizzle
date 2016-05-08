from Plasma import *
from PlasmaTypes import *
sdlBig1 = ptAttribString(1, 'SDL: big 1')
sdlBig2 = ptAttribString(2, 'SDL: big 2')
sdlMed1 = ptAttribString(3, 'SDL: med 1')
sdlMed2 = ptAttribString(4, 'SDL: med 2')
sdlMed3 = ptAttribString(5, 'SDL: med 3')
sdlSmall1 = ptAttribString(6, 'SDL: small 1')
sdlSmall2 = ptAttribString(7, 'SDL: small 2')
dragRopes = ptAttribActivatorList(8, 'drag: list of ropes')
respRopes = ptAttribResponderList(9, 'resp: list of ropes', byObject=1)
respDoors = ptAttribResponder(10, 'resp: hut doors', ['LowToMid', 'LowToHigh', 'MidToLow', 'MidToHigh', 'HighToMid', 'HighToLow'])
evntBig1 = ptAttribActivator(11, 'anm evt: big 1')
evntBig2 = ptAttribActivator(12, 'anm evt: big 2')
evntMed1 = ptAttribActivator(13, 'anm evt: med 1')
evntMed2 = ptAttribActivator(14, 'anm evt: med 2')
evntMed3 = ptAttribActivator(15, 'anm evt: med 3')
evntSmall1 = ptAttribActivator(16, 'anm evt: small 1')
evntSmall2 = ptAttribActivator(17, 'anm evt: small 2')
ropeNum = -1
dragList = ['cDragHutBig1', 'cDragHutBig2', 'cDragHutMed1', 'cDragHutMed2', 'cDragHutMed3', 'cDragHutSmall1', 'cDragHutSmall2']
respList = []
listSDLs = [sdlBig1, sdlBig2, sdlMed1, sdlMed2, sdlMed3, sdlSmall1, sdlSmall2]
boolBig1 = 0
boolBig2 = 0
boolMed1 = 0
boolMed2 = 0
boolMed3 = 0
boolSmall1 = 0
boolSmall2 = 0
doorPos = 1
WaitToChange = 0
kBig = 10
kMed = 3
kSmall = 1
kSolution = 15

class lakiFighterHut(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6369
        version = 2
        self.version = version
        print '__init__lakiFighterHut v. ',
        print version,
        print '.0'


    def OnFirstUpdate(self):
        global respList
        global boolBig1
        global boolBig2
        global boolMed1
        global boolMed2
        global boolMed3
        global boolSmall1
        global boolSmall2
        for resp in respRopes.value:
            thisResp = resp.getName()
            if (thisResp not in respList):
                respList.append(thisResp)
        print 'respList = ',
        print respList
        ageSDL = PtGetAgeSDL()
        for var in (
                        sdlBig1,
                        sdlBig2,
                        sdlMed1,
                        sdlMed2,
                        sdlMed3,
                        sdlSmall1,
                        sdlSmall2,
                    ):
            ageSDL.sendToClients(var.value)
            ageSDL.setFlags(var.value, 1, 1)
            ageSDL.setNotify(self.key, var.value, 0.0)
        boolBig1 = ageSDL[sdlBig1.value][0]
        boolBig2 = ageSDL[sdlBig2.value][0]
        boolMed1 = ageSDL[sdlMed1.value][0]
        boolMed2 = ageSDL[sdlMed2.value][0]
        boolMed3 = ageSDL[sdlMed3.value][0]
        boolSmall1 = ageSDL[sdlSmall1.value][0]
        boolSmall2 = ageSDL[sdlSmall2.value][0]
        print 'lakiFighterHut.OnAgeDataInitialized(): ',
        print sdlBig1.value,
        print ' is set to ',
        print boolBig1
        print 'lakiFighterHut.OnAgeDataInitialized(): ',
        print sdlBig2.value,
        print ' is set to ',
        print boolBig2
        print 'lakiFighterHut.OnAgeDataInitialized(): ',
        print sdlMed1.value,
        print ' is set to ',
        print boolMed1
        print 'lakiFighterHut.OnAgeDataInitialized(): ',
        print sdlMed2.value,
        print ' is set to ',
        print boolMed2
        print 'lakiFighterHut.OnAgeDataInitialized(): ',
        print sdlMed3.value,
        print ' is set to ',
        print boolMed3
        print 'lakiFighterHut.OnAgeDataInitialized(): ',
        print sdlSmall1.value,
        print ' is set to ',
        print boolSmall1
        print 'lakiFighterHut.OnAgeDataInitialized(): ',
        print sdlSmall2.value,
        print ' is set to ',
        print boolSmall2
        self.ICalcRocks(1)


    def OnNotify(self, state, id, events):
        global WaitToChange
        global ropeNum
        global boolBig1
        global boolBig2
        global boolMed1
        global boolMed2
        global boolMed3
        global boolSmall1
        global boolSmall2
        if (state and ((id == evntBig1.id) or ((id == evntBig2.id) or ((id == evntMed1.id) or ((id == evntMed2.id) or ((id == evntMed3.id) or ((id == evntSmall1.id) or (id == evntSmall2.id)))))))):
            if WaitToChange:
                return
        
        if not PtWasLocallyNotified(self.key): return
        
        if (id == respDoors.id):
            print 'lakiFighterHut.OnNotify(): callback from respDoors'
            self.IToggleRopes(1)
            ropeNum = -1
        
        if PtGetLocalAvatar() != PtFindAvatar(events): return
        
        ageSDL = PtGetAgeSDL()
        if ((id == evntBig1.id) and state):
            ropeNum = 0
            if boolBig1:
                ageSDL[sdlBig1.value] = (0,)
            else:
                ageSDL[sdlBig1.value] = (1,)
        elif ((id == evntBig2.id) and state):
            ropeNum = 1
            if boolBig2:
                ageSDL[sdlBig2.value] = (0,)
            else:
                ageSDL[sdlBig2.value] = (1,)
        elif ((id == evntMed1.id) and state):
            ropeNum = 2
            if boolMed1:
                ageSDL[sdlMed1.value] = (0,)
            else:
                ageSDL[sdlMed1.value] = (1,)
        elif ((id == evntMed2.id) and state):
            ropeNum = 3
            if boolMed2:
                ageSDL[sdlMed2.value] = (0,)
            else:
                ageSDL[sdlMed2.value] = (1,)
        elif ((id == evntMed3.id) and state):
            ropeNum = 4
            if boolMed3:
                ageSDL[sdlMed3.value] = (0,)
            else:
                ageSDL[sdlMed3.value] = (1,)
        elif ((id == evntSmall1.id) and state):
            ropeNum = 5
            if boolSmall1:
                ageSDL[sdlSmall1.value] = (0,)
            else:
                ageSDL[sdlSmall1.value] = (1,)
        elif ((id == evntSmall2.id) and state):
            ropeNum = 6
            if boolSmall2:
                ageSDL[sdlSmall2.value] = (0,)
            else:
                ageSDL[sdlSmall2.value] = (1,)
        if ((id == dragRopes.id) and state):
            pass


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global WaitToChange
        global ropeNum
        global respList
        global boolBig1
        global boolBig2
        global boolMed1
        global boolMed2
        global boolMed3
        global boolSmall1
        global boolSmall2
        ageSDL = PtGetAgeSDL()
        thisSDL = VARname
        print 'lakiFighterHut.OnSDLNotify(): SDL for ',
        print thisSDL,
        print ' changed' 
        WaitToChange = 1
        PtAtTimeCallback(self.key, 0.1, 1)
        sdlVal = ageSDL[VARname][0]
        if (not (sdlVal)):
            print 'lakiFighterHut.OnNotify(): Rope#: ',
            print (ropeNum + 1),
            print ' is now resetting...'
            dragRopes.value[ropeNum].disable()
            respRopes.run(self.key, objectName=respList[ropeNum])
        if (thisSDL == sdlBig1.value):
            boolBig1 = sdlVal
            self.ICalcRocks()
        elif (thisSDL == sdlBig2.value):
            boolBig2 = sdlVal
            self.ICalcRocks()
        elif (thisSDL == sdlMed1.value):
            boolMed1 = sdlVal
            self.ICalcRocks()
        elif (thisSDL == sdlMed2.value):
            boolMed2 = sdlVal
            self.ICalcRocks()
        elif (thisSDL == sdlMed3.value):
            boolMed3 = sdlVal
            self.ICalcRocks()
        elif (thisSDL == sdlSmall1.value):
            boolSmall1 = sdlVal
            self.ICalcRocks()
        elif (thisSDL == sdlSmall2.value):
            boolSmall2 = sdlVal
            self.ICalcRocks()


    def ICalcRocks(self, ff = 0):
        global boolBig1
        global boolBig2
        global boolMed1
        global boolMed2
        global boolMed3
        global boolSmall1
        global boolSmall2
        global doorPos
        global ropeNum
        rocks = (((((((boolBig1 * kBig) + (boolBig2 * kBig)) + (boolMed1 * kMed)) + (boolMed2 * kMed)) + (boolMed3 * kMed)) + (boolSmall1 * kSmall)) + (boolSmall2 * kSmall))
        print 'lakiFighterHut.ICalcRocks(): rocks = ',
        print rocks
        if (rocks == kSolution):
            if ((doorPos == 0) or ff):
                respDoors.run(self.key, state='LowToMid', fastforward=ff)
                doorPos = 1
                print 'lakiFighterHut.ICalcRocks(): now setting hut doors position to ',
                print doorPos
                if (not (ff)):
                    self.IToggleRopes(0)
            elif (doorPos == 2):
                respDoors.run(self.key, state='HighToMid', fastforward=ff)
                doorPos = 1
                print 'lakiFighterHut.ICalcRocks(): now setting hut doors position to ',
                print doorPos
                if (not (ff)):
                    self.IToggleRopes(0)
            else:
                dragRopes.value[ropeNum].enable()
                ropeNum = -1
        elif (rocks > kSolution):
            if (doorPos == 1):
                respDoors.run(self.key, state='MidToLow', fastforward=ff)
                doorPos = 0
                print 'lakiFighterHut.ICalcRocks(): now setting hut doors position to ',
                print doorPos
                if (not (ff)):
                    self.IToggleRopes(0)
            elif (doorPos == 2):
                respDoors.run(self.key, state='HighToLow', fastforward=ff)
                doorPos = 0
                print 'lakiFighterHut.ICalcRocks(): now setting hut doors position to ',
                print doorPos
                if (not (ff)):
                    self.IToggleRopes(0)
            else:
                dragRopes.value[ropeNum].enable()
                ropeNum = -1
        elif (rocks < kSolution):
            if ((doorPos == 0) or ff):
                respDoors.run(self.key, state='LowToHigh', fastforward=ff)
                doorPos = 2
                print 'lakiFighterHut.ICalcRocks(): now setting hut doors position to ',
                print doorPos
                if (not (ff)):
                    self.IToggleRopes(0)
            elif (doorPos == 1):
                respDoors.run(self.key, state='MidToHigh', fastforward=ff)
                doorPos = 2
                print 'lakiFighterHut.ICalcRocks(): now setting hut doors position to ',
                print doorPos
                if (not (ff)):
                    self.IToggleRopes(0)
            else:
                dragRopes.value[ropeNum].enable()
                ropeNum = -1


    def IToggleRopes(self, toggle):
        i = 0
        if toggle:
            print 'lakiFighterHut.IToggleRopes(): enabling 6 rope dragables'
            for rope in dragRopes.value:
                dragRopes.value[i].enable()
                i += 1
        else:
            print 'lakiFighterHut.IToggleRopes(): disabling 6 rope dragables'
            for rope in dragRopes.value:
                dragRopes.value[i].disable()
                i += 1


    def OnTimer(self, id):
        global WaitToChange
        if (id == 1):
            WaitToChange = 0


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



