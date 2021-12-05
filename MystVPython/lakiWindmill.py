"""
Also takes care of fog settings when using enveffect.

Unfortunately, tree movement is out of question. Unless you want to re-rig and reanim the 80 or so trees, with their 50 or so bones, and using two animations per bone...

To avoid multiple players changing the same SDL var, changes are only done by the sceneobject master
Should now work fine.

Annoying bug: each times the cage rotates, collision loses 1/4 of rotation. Can't find why. Might be fixed by simply rotating it manually through python.
#"""

from Plasma import *
from PlasmaTypes import *
sdlWind = ptAttribString(1, 'SDL: wind')
sdlGear = ptAttribString(2, 'SDL: gear')
sdlDoor1 = ptAttribString(3, 'SDL: door1')
sdlDoor2 = ptAttribString(4, 'SDL: door2')
respWindmill = ptAttribResponder(5, 'resp: windmill', ['on', 'off'])
actGear = ptAttribActivator(6, 'drag: gear lever')
actHeight = ptAttribActivator(7, 'drag: cage height lever')
actRotate = ptAttribActivator(8, 'drag: cage rotation lever')
actDoor1 = ptAttribActivator(9, 'clk: door1 button')
actDoor2 = ptAttribActivator(10, 'clk: door2 button')
respGearLev = ptAttribResponder(11, 'resp: gear lever at start', ['on', 'off'])
respHeight = ptAttribResponder(12, 'resp: cage height', ['LowToMid', 'MidToHigh', 'HighToMid', 'MidToLow'])
respRotate = ptAttribResponder(13, 'resp: cage rotation', ['use', 'kill', 'rotsetup'])
respDoor1 = ptAttribResponder(14, 'resp: door1', ['open', 'close'])
respDoor2 = ptAttribResponder(15, 'resp: door2', ['stuck'])
anmEvtHgtLev = ptAttribActivator(16, 'anm evt: cage height lever')
anmEvtRotate = ptAttribActivator(17, 'anm evt: cage rotate')
sdlHeight = ptAttribString(18, 'SDL: cage height')
respHgtLevReset = ptAttribResponder(19, 'resp: reset height lever')
anmEvtRotLev = ptAttribActivator(20, 'anm evt: cage rotate lever')
respRotLevReset = ptAttribResponder(21, 'resp: reset rotate lever')
anmEvtGearLevOn = ptAttribActivator(22, 'anm evt: gear lever on')
anmEvtGearLevOff = ptAttribActivator(23, 'anm evt: gear lever off')
respDoorBtn1 = ptAttribResponder(24, 'resp: door btn 1', ['press', 'reenable'])
respDoorBtn2 = ptAttribResponder(25, 'resp: door btn 2', ['press', 'reenable'])
NodeRgnLow = ptAttribSceneobject(26, 'obj: node region - low')
NodeRgnMid = ptAttribSceneobject(27, 'obj: node region - mid')
NodeRgnHigh = ptAttribSceneobject(28, 'obj: node region - high')
respWindFX = ptAttribResponder(30, 'resp: wind FX', ['on', 'off'])
sdlRotSetup = ptAttribString(31, 'SDL: rotation setup')
actMazeNoFog1 = ptAttribActivator(32, 'rgn sns: maze no-fog 1')
actMazeNoFog2 = ptAttribActivator(33, 'rgn sns: maze no-fog 2')
actMazeNoFog3 = ptAttribActivator(34, 'rgn sns: maze no-fog 3')
actCanalDrop = ptAttribActivator(35, 'rgn sns: canal drop')
respCanalDrop = ptAttribResponder(36, 'resp: canal drop', ['on', 'off'])
objTree = ptAttribSceneobject(37, 'obj: any tree dummy')
boolWind = 0
boolGear = 0
boolDoor1 = 0
Active = 0
DoingHeight = 0
DoingRotate = 0
byteHeight = 0
InFogRgn1 = 0
InFogRgn2 = 0
InFogRgn3 = 0
gear = 0
soTree = None
RotSetupDone = 0
kDelayFXSecs = 1.0
kDelayFXID = 1
"""
hiDegX_1 = 0.1
hiDegY_1 = 6.0
hiDirX_1 = 0.25
hiDirY_1 = 0.5
hiSpd_1 = 1.0
hiDegX_2 = 0.25
hiDegY_2 = 5.0
hiDirX_2 = 0.2
hiDirY_2 = 1.5
hiSpd_2 = 1.25
hiDegX_3 = 1.0
hiDegY_3 = 7.0
hiDirX_3 = 0.1
hiDirY_3 = 1.0
hiSpd_3 = 0.75
hiDegX_4 = 2.0
hiDegY_4 = 10.0
hiDirX_4 = 1.0
hiDirY_4 = 0.1
hiSpd_4 = 3.0
loDegX_1 = 0.5
loDegY_1 = 0.05
loDirX_1 = 0.05
loDirY_1 = 0.25
loSpd_1 = 0.37
loDegX_2 = 1.5
loDegY_2 = 0.25
loDirX_2 = 0.12
loDirY_2 = 1.0
loSpd_2 = 0.5
loDegX_3 = 0.5
loDegY_3 = 0.35
loDirX_3 = 0.25
loDirY_3 = 1.5
loSpd_3 = 0.75
loDegX_4 = 0.75
loDegY_4 = 0.12
loDirX_4 = 0.25
loDirY_4 = 0.5
loSpd_4 = 1.0 #"""

class lakiWindmill(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6362
        version = 11
        self.version = version
        print '__init__lakiWindmill v. ',
        print version,
        print '.0'


    def OnServerInitComplete(self):
        global boolWind
        global boolGear
        global boolDoor1
        global byteHeight
        global RotSetupDone
        global soTree
        global Active
        ageSDL = PtGetAgeSDL()
        for var in (
                        sdlWind,
                        sdlGear,
                        sdlDoor1,
                        sdlDoor2,
                        sdlHeight,
                        sdlRotSetup,
                    ):
            ageSDL.sendToClients(var.value)
            ageSDL.setFlags(var.value, 1, 1)
            ageSDL.setNotify(self.key, var.value, 0.0)
        boolWind =      ageSDL[sdlWind.value][0]
        boolGear =      ageSDL[sdlGear.value][0]
        boolDoor1 =     ageSDL[sdlDoor1.value][0]
        byteHeight =    ageSDL[sdlHeight.value][0]
        RotSetupDone =  ageSDL[sdlRotSetup.value][0]
        print 'lakiWindmill.OnAgeDataInitialized(): SDL for RotSetupDone = ',
        print RotSetupDone
        if (not (RotSetupDone)):
            respRotate.run(self.key, state='use')
        soTree = objTree.value
        self.IDoWindFX(boolWind, 1, 0)
        if boolWind:
            print 'lakiWindmill.OnAgeDataInitialized(): SDL says it\'s windy'
            respWindmill.run(self.key, state='on')
        else:
            print 'lakiWindmill.OnAgeDataInitialized(): SDL says it\'s NOT windy'
        if boolDoor1:
            respDoor1.run(self.key, state='open', fastforward=1)
        else:
            respDoor1.run(self.key, state='close', fastforward=1)
        if boolGear:
            respGearLev.run(self.key, state='on', fastforward=1)
            if boolWind:
                Active = 1
            else:
                Active = 0
        if byteHeight:
            if (byteHeight == 1):
                respHeight.run(self.key, state='LowToMid', fastforward=1)
                #self.IDoNodes('mid')
            elif (byteHeight == 2):
                respHeight.run(self.key, state='MidToHigh', fastforward=1)
                #self.IDoNodes('high')
            elif (byteHeight == 3):
                respHeight.run(self.key, state='HighToMid', fastforward=1)
                #self.IDoNodes('mid')
        else:
            respHeight.run(self.key, state='MidToLow', fastforward=1)
            #self.IDoNodes('low')


    def OnNotify(self, state, id, events):
        global Active
        global boolDoor1
        global gear
        global boolGear
        global boolWind
        global DoingHeight
        global byteHeight
        global DoingRotate
        global RotSetupDone
        global InFogRgn1
        global InFogRgn2
        global InFogRgn3
        ageSDL = PtGetAgeSDL()
        if ((id == actGear.id) and state):
            pass
        if ((id == actHeight.id) and state):
            pass
        if ((id == actRotate.id) and state):
            pass
        if ((id == actDoor1.id) and state):
            respDoorBtn1.run(self.key, state='press')
        if ((id == actDoor2.id) and state):
            respDoorBtn2.run(self.key, state='press')
        if (id == respDoorBtn1.id):
            if Active:
                if boolDoor1:
                    if self.sceneobject.isLocallyOwned():
                        ageSDL[sdlDoor1.value] = (0,)
                else:
                    if self.sceneobject.isLocallyOwned():
                        ageSDL[sdlDoor1.value] = (1,)
            else:
                respDoorBtn1.run(self.key, state='reenable')
        if (id == respDoorBtn2.id):
            if Active:
                respDoor2.run(self.key, state='stuck')
            else:
                respDoorBtn2.run(self.key, state='reenable')
        if (id == respWindmill.id):
            print 'lakiWindmill.OnNotify(): got callback from windmill'
        if ((id == anmEvtGearLevOn.id) and state):
            print 'lakiWindmill.OnNotify(): anim event for clutch ON'
            if gear:
                return
            gear = 1
            if boolGear:
                if boolWind:
                    Active = 1
            elif self.sceneobject.isLocallyOwned():
                ageSDL[sdlGear.value] = (1,)
        if ((id == anmEvtGearLevOff.id) and state):
            print 'lakiWindmill.OnNotify(): anim event for clutch OFF'
            if (not (gear)):
                return
            gear = 0
            if boolGear:
                if self.sceneobject.isLocallyOwned():
                    ageSDL[sdlGear.value] = (0,)
            else:
                Active = 0
        if ((id == anmEvtHgtLev.id) and state):
            if DoingHeight:
                return
            if Active:
                if (byteHeight == 3):
                    byteHeight = 0
                else:
                    byteHeight += 1
                if self.sceneobject.isLocallyOwned():
                    ageSDL[sdlHeight.value] = (byteHeight,)
                DoingHeight = 1
            else:
                respHgtLevReset.run(self.key)
                if boolGear:
                    respGearLev.run(self.key, state='off')
        if (id == respHeight.id):
            respHgtLevReset.run(self.key)
        if (id == respHgtLevReset.id):
            DoingHeight = 0
        if ((id == anmEvtRotLev.id) and state):
            if DoingRotate:
                return
            if Active:
                respRotate.run(self.key, state='use')
                DoingRotate = 1
            else:
                respRotLevReset.run(self.key)
                if boolGear:
                    respGearLev.run(self.key, state='off')
        if ((id == anmEvtRotate.id) and state):
            respRotate.run(self.key, state='kill')
        if (id == respRotate.id):
            if RotSetupDone:
                respRotLevReset.run(self.key)
            elif self.sceneobject.isLocallyOwned():
                ageSDL[sdlRotSetup.value] = (1,)
        if (id == respRotLevReset.id):
            DoingRotate = 0
        """if ((id == actMazeNoFog1.id) and state):
            for event in events:
                if (event[0] == kCollisionEvent):
                    if PtWasLocallyNotified(self.key) and PtFindAvatar(events) == PtGetLocalAvatar():
                        if (event[1] == 1):
                            InFogRgn1 = 1
                            if ((not (InFogRgn2)) and (not (InFogRgn3))):
                                print 'lakiWindmill.OnNotify(): Player has entered a Maze-NoFog region'
                                if boolWind:
                                    self.IDoWindFX(0, 1, 1)
                        else:
                            InFogRgn1 = 0
                            if ((not (InFogRgn2)) and (not (InFogRgn3))):
                                print 'lakiWindmill.OnNotify(): Player has exited a Maze-NoFog region'
                                if boolWind:
                                    self.IDoWindFX(1, 1, 1)
        if ((id == actMazeNoFog2.id) and state):
            for event in events:
                if (event[0] == kCollisionEvent):
                    if PtWasLocallyNotified(self.key) and PtFindAvatar(events) == PtGetLocalAvatar():
                        if (event[1] == 1):
                            InFogRgn2 = 1
                            if ((not (InFogRgn1)) and (not (InFogRgn3))):
                                print 'lakiWindmill.OnNotify(): Player has entered a Maze-NoFog region'
                                if boolWind:
                                    self.IDoWindFX(0, 1, 1)
                        else:
                            InFogRgn2 = 0
                            if ((not (InFogRgn1)) and (not (InFogRgn3))):
                                print 'lakiWindmill.OnNotify(): Player has exited a Maze-NoFog region'
                                if boolWind:
                                    self.IDoWindFX(1, 1, 1)
        if ((id == actMazeNoFog3.id) and state):
            for event in events:
                if (event[0] == kCollisionEvent):
                    if PtWasLocallyNotified(self.key) and PtFindAvatar(events) == PtGetLocalAvatar():
                        if (event[1] == 1):
                            InFogRgn3 = 1
                            if ((not (InFogRgn1)) and (not (InFogRgn2))):
                                print 'lakiWindmill.OnNotify(): Player has entered a Maze-NoFog region'
                                if boolWind:
                                    self.IDoWindFX(0, 1, 1)
                        else:
                            InFogRgn3 = 0
                            if ((not (InFogRgn1)) and (not (InFogRgn2))):
                                print 'lakiWindmill.OnNotify(): Player has exited a Maze-NoFog region'
                                if boolWind:
                                    self.IDoWindFX(1, 1, 1) #"""
        if ((id == actCanalDrop.id) and state):
            for event in events:
                if (event[0] == kCollisionEvent):
                    if PtWasLocallyNotified(self.key) and PtFindAvatar(events) == PtGetLocalAvatar():
                        if (event[1] == 1):
                            respCanalDrop.run(self.key, state='off')
                        else:
                            respCanalDrop.run(self.key, state='on')


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global boolWind
        global InFogRgn1
        global InFogRgn2
        global InFogRgn3
        global boolGear
        global Active
        global DoingHeight
        global DoingRotate
        global boolDoor1
        global byteHeight
        global RotSetupDone
        changedVar = VARname
        ageSDL = PtGetAgeSDL()
        value = ageSDL[VARname][0]
        print 'lakiWindmill.OnSDLNotify(): SDL for ',
        print changedVar,
        print ' changed to ',
        print value
        if (changedVar == sdlWind.value):
            boolWind = value
            if ((not (InFogRgn1)) and ((not (InFogRgn2)) and (not (InFogRgn3)))):
                PtAtTimeCallback(self.key, kDelayFXSecs, kDelayFXID)
            else:
                self.IDoWindFX(boolWind, 1, 0)
            if boolWind:
                respWindmill.run(self.key, state='on')
                if boolGear:
                    Active = 1
            else:
                if boolGear:
                    respGearLev.run(self.key, state='off')
                respWindmill.run(self.key, state='off')
                Active = 0
        if (changedVar == sdlGear.value):
            boolGear = value
            if (boolGear and boolWind):
                Active = 1
            else:
                Active = 0
                if DoingHeight:
                    pass
                if DoingRotate:
                    respRotate.run(self.key, state='kill')
        if (changedVar == sdlDoor1.value):
            boolDoor1 = value
            if boolDoor1:
                respDoor1.run(self.key, state='open')
            else:
                respDoor1.run(self.key, state='close')
        if (changedVar == sdlHeight.value):
            byteHeight = value
            if (byteHeight == 0):
                respHeight.run(self.key, state='MidToLow')
                #self.IDoNodes('low')
            elif (byteHeight == 1):
                respHeight.run(self.key, state='LowToMid')
                #self.IDoNodes('mid')
            elif (byteHeight == 2):
                respHeight.run(self.key, state='MidToHigh')
                #self.IDoNodes('high')
            elif (byteHeight == 3):
                respHeight.run(self.key, state='HighToMid')
                #self.IDoNodes('mid')
        if (changedVar == sdlRotSetup.value):
            RotSetupDone = value


    def IDoNodes(self, height):
        return
        print 'lakiWindmill.IDoNodes(): will now adjust nodes for cage in ',
        print height,
        print ' position'
        if (height == 'low'):
            NodeRgnLow.value.enable()
            NodeRgnMid.value.disable()
            NodeRgnHigh.value.disable()
        elif (height == 'mid'):
            NodeRgnLow.value.disable()
            NodeRgnMid.value.enable()
            NodeRgnHigh.value.disable()
        elif (height == 'high'):
            NodeRgnLow.value.disable()
            NodeRgnMid.value.disable()
            NodeRgnHigh.value.enable()
        else:
            print 'lakiWindmill.IDoNodes(): ERROR, invalid position; must use \'low\', \'mid\', or \'high\''


    def IDoWindFX(self, wind, ff, onlyFog):
        global soTree
        if wind:
            print 'lakiWindmill.IDoWindFX(): turning wind FX on...'
            PtConsole('Graphics.Renderer.Fog.SetDefLinear -4000 7000 1')
            PtConsole('Graphics.Renderer.Fog.SetDefColor .9 .8 .6')
            #soTree.setWave(1, hiDegX_1, hiDegY_1, hiDirX_1, hiDirY_1, hiSpd_1)
            #soTree.setWave(2, hiDegX_2, hiDegY_2, hiDirX_2, hiDirY_2, hiSpd_2)
            #soTree.setWave(3, hiDegX_3, hiDegY_3, hiDirX_3, hiDirY_3, hiSpd_3)
            #soTree.setWave(4, hiDegX_4, hiDegY_4, hiDirX_4, hiDirY_4, hiSpd_4)
            if (not (onlyFog)):
                respWindFX.run(self.key, state='on', fastforward=ff)
        else:
            print 'lakiWindmill.IDoWindFX(): turning wind FX off...'
            PtConsole('Graphics.Renderer.Fog.SetDefColor 1 1 1')
            PtConsole('Graphics.Renderer.SetClearColor 07 011 018')
            PtConsole('Graphics.Renderer.Fog.SetDefLinear 0 0 0')
            #soTree.setWave(1, loDegX_1, loDegY_1, loDirX_1, loDirY_1, loSpd_1)
            #soTree.setWave(2, loDegX_2, loDegY_2, loDirX_2, loDirY_2, loSpd_2)
            #soTree.setWave(3, loDegX_3, loDegY_3, loDirX_3, loDirY_3, loSpd_3)
            #soTree.setWave(4, loDegX_4, loDegY_4, loDirX_4, loDirY_4, loSpd_4)
            if (not (onlyFog)):
                respWindFX.run(self.key, state='off', fastforward=1)


    def OnTimer(self, id):
        global boolWind
        if (id == kDelayFXID):
            self.IDoWindFX(boolWind, 1, 0)


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



