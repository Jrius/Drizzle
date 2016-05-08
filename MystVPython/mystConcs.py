from Plasma import *
from PlasmaTypes import *
import string
spawnEsher = ptAttribSceneobject(1, 'obj: Esher spawner')
respConcs = ptAttribResponder(2, 'resp: Esher concs', ['NoTablet', 'GotTablet'], byObject=1)
sdlTabletDropped = ptAttribString(3, 'sdl: tablet dropped')
actNoTablet = ptAttribActivator(4, 'rgn sns: Esher/NoTablet')
warpEsher1 = ptAttribSceneobject(5, 'obj: warp Esher/NoTablet')
warpPlayer1 = ptAttribSceneobject(6, 'obj: warp Player/NoTablet')
behEsher1 = ptAttribString(7, 'beh: Esher/NoTablet')
sdlTabletPlaced = ptAttribString(8, 'sdl: tablet placed')
warpEsher2 = ptAttribSceneobject(9, 'obj: warp Esher/GotTablet')
warpPlayer2 = ptAttribSceneobject(10, 'obj: warp Player/GotTablet')
behEsher2 = ptAttribString(11, 'beh: Esher/GotTablet')
sdlEsherInMyst = ptAttribString(12, 'sdl: Esher in Myst')
FollowCam = 1
DisableMovement = 1
AnimKey = None
AnimName = ''
byteEsherInMyst = 0
boolTabletDropped = 0
byteTabletPlaced = 0
GotTablet = 0
ListAnimsDone = []
SoEsher = None
CamOffset = None
boolSkipAllNPC = 0
kLocationMyst = 3
kBlendIn = 0.8
kEnd = 6
kTime = 3
kLinkResp = 'cRespLink'
sdlEsherGoggles = 'global.NPC.EsherGoggles'
CamOffsetX1 = 0.0
CamOffsetY1 = 0.0
CamOffsetZ1 = 3.5
CamOffsetX2 = 0.0
CamOffsetY2 = 0.0
CamOffsetZ2 = 3.5
sdlSkipAllNPC = 'global.NPC.SkipAllNPC'
sdlEncountersDone = 'global.NPC.EncountersDone'
kEsherLinkOut = 1
kMarkerLinkOut = 'linkout'
kEndItTimer = 1.1
kDoWarpSecs = 0.5
kLinkInTimer = 0
kResetTimer = 3
kDoWarpID = 1
kEndItID = 2
kResetID = 3
kEsher2LinkInSecs = 5
kEsher2LinkInID = 4

class mystConcs(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6608
        version = 1
        self.version = version
        print '__init__mystConcs v.',
        print version,
        print '.1'


    def OnFirstUpdate(self):
        global SoEsher
        global AnimKey
        global byteEsherInMyst
        global boolTabletDropped
        global boolSkipAllNPC
        global byteTabletPlaced
        return
        SoEsher = ptSceneobject.getSpawnedNPC(spawnEsher.value)
        AnimKey = ptAnimation(SoEsher.getKey())
        AnimKey.addKey(SoEsher.getKey())
        byteEsherInMyst = sdlEsherInMyst.value.getValue()
        boolTabletDropped = sdlTabletDropped.value.getValue()
        boolSkipAllNPC = PtGetGlobalSDLVar(sdlSkipAllNPC).getValue()
        byteTabletPlaced = sdlTabletPlaced.value.getValue()
        if (boolTabletDropped and ((not (byteEsherInMyst)) and (not (boolSkipAllNPC)))):
            if (byteTabletPlaced == kLocationMyst):
                actNoTablet.disableActivator()
                self.IFadeEsher('out', 1)
                PtSendPythonNotify('Hide', 'Menu Bar', self.key)
                if DisableMovement:
                    print 'Disabling player movement'
                    PtEnableMovement(0)
                PtAtTimeCallback(self.key, 3, kEsher2LinkInID)
            else:
                actNoTablet.enableActivator()
        else:
            actNoTablet.disableActivator()


    def OnNotify(self, state, id, events):
        if PtWasLocallyNotified(self.key) and PtFindAvatar(events) == PtGetLocalAvatar():
            if ((id == actNoTablet.id) and state):
                #self.IFadeEsher('out', 1)
                actNoTablet.disableActivator()
                #PtSendPythonNotify('Hide', 'Menu Bar', self.key)
                #self.IDoEsher(1)
                
                ## run the responder, if fastforwarded, will run nice trapped music,
                ## if not, will run escher's creepy music AND run nice trapped music
                ## don't run the GotTablet state, it's too much for exploration.
                respConcs.run(self.key, state="NoTablet", fastforward=0)


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global byteTabletPlaced
        return
        if (changedVar.getName() == sdlTabletPlaced.value.getName()):
            byteTabletPlaced = sdlTabletPlaced.value.getValue()
            if (byteTabletPlaced == kLocationMyst):
                self.IFadeEsher('out', 1)
                PtSendPythonNotify('Hide', 'Menu Bar', self.key)
                PtAtTimeCallback(self.key, kEsher2LinkInSecs, kEsher2LinkInID)


    def IDoEsher(self, conc):
        global AnimName
        global CamOffset
        global AnimKey
        global ListAnimsDone
        return
        if DisableMovement:
            print 'Disabling player movement'
            PtEnableMovement(0)
        goggles = PtGetGlobalSDLVar(sdlEsherGoggles)
        goggles.setValue(0)
        if (conc == 1):
            AnimName = behEsher1.value
            respConcs.run(self.key, state='NoTablet')
            CamOffset = ptVector3(CamOffsetX1, CamOffsetY1, CamOffsetZ1)
        elif (conc == 2):
            AnimName = behEsher2.value
            warp = warpEsher2.value
            respConcs.run(self.key, state='GotTablet')
            CamOffset = ptVector3(CamOffsetX2, CamOffsetY2, CamOffsetZ2)
            self.IFadeEsher('in')
        else:
            print 'mystConcs.IDoEsher(): ERROR, invalid argument specified.'
        PtLoadAgePage('Myst', AnimName, 0)
        AnimKey.setAnimName(AnimName)
        AnimKey.play(1)
        print 'mystConcs.IDoEsher1(): Esher playing anim: ',
        print AnimName
        AnimKey.addMarkerCallback(self.key, kMarkerLinkOut, kEsherLinkOut)
        if (AnimName not in ListAnimsDone):
            ListAnimsDone.append(AnimName)
        PtAtTimeCallback(self.key, kDoWarpSecs, kDoWarpID)


    def OnEventCallback(self, event, userData):
        global AnimName
        global ListAnimsDone
        print "Error: OnEventCallback: WHAT ?!?!?"
        return
        if (event == kTime):
            if (AnimName not in ListAnimsDone):
                ListAnimsDone.append(AnimName)
            if (userData == kEsherLinkOut):
                self.IFadeEsher('out')
                PtAtTimeCallback(self.key, kEndItTimer, kEndItID)
                if (AnimName == behEsher1.value):
                    sdlEsherInMyst.value.setValue(1)
                elif (AnimName == behEsher2.value):
                    sdlEsherInMyst.value.setValue(2)
                self.ILogEncounters(AnimName)


    def OnTimer(self, id):
        global AnimName
        global SoEsher
        global CamOffset
        return
        if (id == kDoWarpID):
            print 'mystConcs.OnTimer(): will now warp people'
            PtAnimateNextCamTransition()
            player = PtGetPlayerKey().getSceneObject()
            if (AnimName == behEsher1.value):
                player.physics.warpObj(warpPlayer1.value.getKey())
                self.IFadeEsher('in', 1)
                warp = warpEsher1.value
                SoEsher.physics.warpObj(warp.getKey())
            elif (AnimName == behEsher2.value):
                player.physics.warpObj(warpPlayer2.value.getKey())
                warp = warpEsher2.value
                SoEsher.physics.warpObj(warp.getKey())
            if FollowCam:
                PtSetCamLookAt(SoEsher, CamOffset)
        elif (id == kEndItID):
            if FollowCam:
                print 'mystConcs.OnTimer(): now clearing CamLookAt'
                PtClearCamLookAt()
            if (not (PtIsExpertMode())):
                print 'In novice mode, so we\'ll send player to closest node'
                PtClosestNode()
            if DisableMovement:
                print 'Re-enabling player movement'
                PtEnableMovement(1)
            EsherSpawn = spawnEsher.value
            SoEsher.physics.warpObj(EsherSpawn.getKey())
            PtSendPythonNotify('Show', 'Menu Bar', self.key)
            PtAtTimeCallback(self.key, kResetTimer, kResetID)
        elif (id == kResetID):
            self.IUnloadAnims()
        elif (id == kEsher2LinkInID):
            self.IDoEsher(2)


    def IFadeEsher(self, effect, ff = 0):
        global SoEsher
        return
        respList = SoEsher.getResponders()
        if (len(respList) > 0):
            for resp in respList:
                if (resp.getName() == kLinkResp):
                    respLink = ptAttribResponder(42, statelist=['out', 'in'])
                    respLink.__setvalue__(resp)
                    respLink.run(self.key, state=effect, fastforward=ff)


    def IUnloadAnims(self):
        global ListAnimsDone
        global SoEsher
        return
        for AnimDone in ListAnimsDone:
            print 'xNPCSpawner.IUnloadAnims(): now unloading anim: ',
            print AnimDone
            PtDetachAnimation(SoEsher.getKey(), AnimDone)
            PtUnloadPage(AnimDone)
        ListAnimsDone = []


    def ILogEncounters(self, encounter):
        return
        strEncountersDone = ''
        strEncountersDone = PtGetGlobalSDLVar(sdlEncountersDone).getValue()
        if (encounter in strEncountersDone):
            print 'mystConcs.ILogEncounters(): somehow this encounter was already logged, so will do nothing'
            return
        if (strEncountersDone == ''):
            strEncountersDone = encounter
        else:
            strEncountersDone = ((strEncountersDone + ',') + encounter)
        PtGetGlobalSDLVar(sdlEncountersDone).setValue(strEncountersDone)
        print 'mystConcs.ILogEncounters(): updating EncountersDone SDL, now: ',
        print strEncountersDone


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



