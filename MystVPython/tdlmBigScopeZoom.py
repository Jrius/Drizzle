"""
Note: upon pressing one of the zoom buttons, we will push the camera to the scope's cam, and
load the telescope GUI. Pod view is handled by this script.
#"""


from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *
from PlasmaKITypes import *
import PlasmaControlKeys
import string
kFOV00 = 50
kFOV01 = 15
kFOV02 = 2
act00 = ptAttribActivator(1, 'act: Zoom button 00')
anim00 = ptAttribAnimation(2, 'anim Zoom button 00')
act01 = ptAttribActivator(3, 'act: Zoom button 01')
anim01 = ptAttribAnimation(4, 'anim Zoom button 01')
act02 = ptAttribActivator(5, 'act: Zoom button 02')
anim02 = ptAttribAnimation(6, 'anim Zoom button 02')
camera = ptAttribSceneobject(7, 'Scope camera')
shutter = ptAttribResponder(8, 'resp: Lens switch effect')
ZoomSDL = ptAttribString(9, 'SDL Zoom')

amLookingAtScope = False

## pod
currentSeasonIsPod = False
scopeAlignWPod = False
usingPodCam = False

podCamera = None

curZoomLevel = 0

class tdlmBigScopeZoom(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6215
        version = 2
        self.version = version
        print '__init__tdlmBigScopeZoom v.',
        print version,
        print '.1'


    def OnServerInitComplete(self):
        global amLookingAtScope
        global podCamera
        global currentSeasonIsPod
        global scopeAlignWPod
        global usingPodCam
        global curZoomLevel

        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(ZoomSDL.value)
        ageSDL.setFlags(ZoomSDL.value, 1, 1)
        ageSDL.setNotify(self.key, ZoomSDL.value, 0.0)
        print 'tdlmBigScopeZoom: When I got here, ',
        print ZoomSDL.value,
        print ' was set to:',
        print ageSDL[ZoomSDL.value][0]
        if (ageSDL[ZoomSDL.value][0] == 0):
            self.Zoom0()
        elif (ageSDL[ZoomSDL.value][0] == 1):
            self.Zoom1()
        elif (ageSDL[ZoomSDL.value][0] == 2):
            self.Zoom2()
        if ZoomSDL.value == "BigScope01Zoom":
            ## camera object
            podCamera = PtFindSceneobject("Camera-ViewIntoPod", "Todelmer")
            ## scope orientation
            ageSDL.sendToClients("BigScope01Horiz")
            ageSDL.setFlags("BigScope01Horiz", 1, 1)
            ageSDL.setNotify(self.key, "BigScope01Horiz", 0.0)
            ageSDL.sendToClients("BigScope01Vert")
            ageSDL.setFlags("BigScope01Vert", 1, 1)
            ageSDL.setNotify(self.key, "BigScope01Vert", 0.0)
            ## seasons
            ageSDL.sendToClients("CurrentSeason")
            ageSDL.setFlags("CurrentSeason", 1, 1)
            ageSDL.setNotify(self.key, "CurrentSeason", 0.0)
            ageSDL.sendToClients("runEnvEffect")
            ageSDL.setFlags("runEnvEffect", 1, 1)
            ageSDL.setNotify(self.key, "runEnvEffect", 0.0)

            currentSeasonIsPod = self.isPodSeason()
            scopeAlignWPod = self.isScopeAligned()


    def OnTimer(self, id):
        global amLookingAtScope
        global podCamera
        global currentSeasonIsPod
        global scopeAlignWPod
        global usingPodCam
        global curZoomLevel

        if id == 1:
            print "tdlmBigScopeZoom: OnTimer: switching to scope cam."
            ## engage telescope
            cam = ptCamera()
            cam.undoFirstPerson()
            cam.disableFirstPersonOverride()

            virtCam = ptCamera()
            if currentSeasonIsPod and scopeAlignWPod and curZoomLevel == 2:
                usingPodCam = True
                virtCam.save(podCamera.getKey())
                ageSDL = PtGetAgeSDL()
                if not ageSDL["TdlmActivePedestalsKeep"][0]:
                    print "First time viewing pod, unlocking link to it."
                    ageSDL["TdlmActivePedestalsKeep"] = (1,)
            else:
                usingPodCam = False
                virtCam.save(camera.sceneobject.getKey())
            PtEnableControlKeyEvents(self.key)

            PtLoadDialog("telescope",self.key)
            if PtIsDialogLoaded("telescope"):
                PtShowDialog("telescope")

            PtSendKIMessage(kDisableKIandBB,0)
            amLookingAtScope = True

            ## we MUST wait a while before updating the camera (probably 1 frame). Otherwise, it totally screws up the previous cam.
            PtAtTimeCallback(self.key, .3, 3)

            PtFadeIn(.3, 1)

        elif id == 2:
            print "tdlmBigScopeZoom: OnTimer: returning to normal"
            ## quit scope
            PtHideDialog("telescope")
            virtCam = ptCamera()
            if usingPodCam:
                virtCam.restore(podCamera.getKey())
            else:
                virtCam.restore(camera.sceneobject.getKey())
            PtRecenterCamera()
            cam = ptCamera()
            cam.enableFirstPersonOverride()
            PtSendKIMessage(kEnableKIandBB,0)
            PtFadeIn(.3, 1)

        elif id == 3:
            if usingPodCam:
                ptCamera().setFOV(50, 0.3)
                return

            print "updating camera fov"
            ageSDL = PtGetAgeSDL()
            if curZoomLevel == 0:
                ptCamera().setFOV(kFOV00, 0.3)
            elif curZoomLevel == 1:
                ptCamera().setFOV(kFOV01, 0.3)
            elif curZoomLevel == 2:
                ptCamera().setFOV(kFOV02, 0.3)
        elif id == 4:
            act00.enable()
            act01.enable()
            act02.enable()


    def OnGUINotify(self,id,control,event):
        "Notifications from the vignette"
        if event == kDialogLoaded:
            print "tdlmBigScopeZoom: OnGUINotify: showing telescope dialog"
            control.show()


    def OnControlKeyEvent(self,controlKey,activeFlag):
        global amLookingAtScope
        if not activeFlag: return
        if controlKey == PlasmaControlKeys.kKeyExitMode:
            print "tdlmBigScopeZoom: OnControlKeyEvent: received key, exiting."
            PtDisableControlKeyEvents(self.key)
            amLookingAtScope = false
            PtFadeOut(.3, 1)
            PtAtTimeCallback(self.key, .3, 2)
        elif controlKey == PlasmaControlKeys.kKeyMoveBackward or controlKey == PlasmaControlKeys.kKeyRotateLeft or controlKey == PlasmaControlKeys.kKeyRotateRight:
            print "tdlmBigScopeZoom: OnControlKeyEvent: received key, exiting."
            PtDisableControlKeyEvents(self.key)
            amLookingAtScope = false
            PtFadeOut(.3, 1)
            PtAtTimeCallback(self.key, .3, 2)


    def OnNotify(self, state, id, events):
        print "tdlmBigScopeZoom: OnNotify: state", state, ", id", id
        if (not (state)):
            return
        if not (PtFindAvatar(events) == PtGetLocalAvatar()) or not (PtWasLocallyNotified(self.key)): return

        print "tdlmBigScopeZoom: OnNotify: You clicked:", id

        ageSDL = PtGetAgeSDL()
        if (id == act00.id):
            self.disableInput()
            ageSDL[ZoomSDL.value] = (0,)
        elif (id == act01.id):
            self.disableInput()
            ageSDL[ZoomSDL.value] = (1,)
        elif (id == act02.id):
            self.disableInput()
            ageSDL[ZoomSDL.value] = (2,)

        if ageSDL["MainPower01"][0]:
            print "Since power is on, switching to telescope cam."
            PtFadeOut(.3, 1)
            PtAtTimeCallback(self.key, .3, 1)


    def isScopeAligned(self):
        ageSDL = PtGetAgeSDL()
        if (abs((ageSDL["BigScope01Horiz"][0] - 0.17)) > 0.005):
            print "BigScope 01 is not aligned correctly horizontally, so the Pod can't possibly be seen."
            return False
        elif (abs((ageSDL["BigScope01Vert"][0] - 0.145)) > 0.005):
            print "BigScope 01 is not aligned correctly vertically, so the Pod can't possibly be seen."
            return False
        print "BigScope 01 is aligned with pod."
        return True


    def isPodSeason(self):
        ageSDL = PtGetAgeSDL()
        if (ageSDL["CurrentSeason"][0] != 2):
            print "It's the wrong season, so the Pod can't possibly be seen."
            return False
        elif (ageSDL["runEnvEffect"][0] != 0):
            print "Season change is still in progress, so the Pod can't possibly be seen."
            return False
        print "Season OK, not changing: pod viewable"
        return True


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global amLookingAtScope
        global podCamera
        global currentSeasonIsPod
        global scopeAlignWPod
        global usingPodCam
        global curZoomLevel

        ageSDL = PtGetAgeSDL()
        newVal = ageSDL[VARname][0]

        if not amLookingAtScope:
            if VARname == ZoomSDL.value:
                if newVal == 0:
                    self.Zoom0()
                elif newVal == 1:
                    self.Zoom1()
                else:
                    self.Zoom2()
            elif VARname in ("CurrentSeason", "runEnvEffect"):
                currentSeasonIsPod = self.isPodSeason()
            elif VARname in ("BigScope01Horiz", "BigScope01Vert"):
                scopeAlignWPod = self.isScopeAligned()
            return

        if VARname == ZoomSDL.value:
            if newVal != curZoomLevel:
                curZoomLevel = newVal
                virtCam = ptCamera()
                if usingPodCam:
                    print "Zoom changed to %d, switching back to normal scope cam" % curZoomLevel
                    usingPodCam = False
                    virtCam.restore(podCamera.getKey())
                    virtCam.save(camera.sceneobject.getKey())
                    # update FOV once we switched to new camera
                    PtAtTimeCallback(self.key, .3, 3)

                elif currentSeasonIsPod and scopeAlignWPod and curZoomLevel == 2 and ZoomSDL.value == "BigScope01Zoom":
                    print "Zoom changed to 2, switching to pod view"
                    usingPodCam = True
                    virtCam.restore(camera.sceneobject.getKey())
                    virtCam.save(podCamera.getKey())
                    if not ageSDL["TdlmActivePedestalsKeep"][0]:
                        print "First time viewing pod, unlocking link to it."
                        ageSDL["TdlmActivePedestalsKeep"] = (1,)
                    PtAtTimeCallback(self.key, .3, 3)

                else:
                    print "Zoom changed to ", curZoomLevel
                    PtAtTimeCallback(self.key, 0, 3)

                if newVal == 0:
                    self.Zoom0()
                elif newVal == 1:
                    self.Zoom1()
                else:
                    self.Zoom2()

        elif VARname == "CurrentSeason":
            if usingPodCam:
                currentSeasonIsPod = self.isPodSeason()
                if not currentSeasonIsPod:
                    print "Season is no longer pod one, switching back to normal scope cam"
                    usingPodCam = False
                    virtCam.restore(podCamera.getKey())
                    virtCam.save(camera.sceneobject.getKey())
                    # update FOV once we switched to new camera
                    PtAtTimeCallback(self.key, .3, 3)
            elif ZoomSDL.value == "BigScope01Zoom":
                currentSeasonIsPod = self.isPodSeason()
                if currentSeasonIsPod:
                    if curZoomLevel == 2:
                        if scopeAlignWPod:
                            print "Season is now pod one, switching to pod view"
                            usingPodCam = True
                            virtCam.restore(camera.sceneobject.getKey())
                            virtCam.save(podCamera.getKey())
                            if not ageSDL["TdlmActivePedestalsKeep"][0]:
                                print "First time viewing pod, unlocking link to it."
                                ageSDL["TdlmActivePedestalsKeep"] = (1,)
                            PtAtTimeCallback(self.key, .3, 3)

        elif VARname == "runEnvEffect":
            if newVal == 0 and ZoomSDL.value == "BigScope01Zoom":
                if not usingPodCam:
                    currentSeasonIsPod = self.isPodSeason()
                    if currentSeasonIsPod:
                        if curZoomLevel == 2:
                            if scopeAlignWPod:
                                print "Season is now pod one, switching to pod view"
                                usingPodCam = True
                                virtCam.restore(camera.sceneobject.getKey())
                                virtCam.save(podCamera.getKey())
                                if not ageSDL["TdlmActivePedestalsKeep"][0]:
                                    print "First time viewing pod, unlocking link to it."
                                    ageSDL["TdlmActivePedestalsKeep"] = (1,)
                                PtAtTimeCallback(self.key, .3, 3)

        elif VARname in ("BigScope01Horiz", "BigScope01Vert") and ZoomSDL.value == "BigScope01Zoom":
            if not scopeAlignWPod:
                scopeAlignWPod = self.isScopeAligned()
                if scopeAlignWPod:
                    if curZoomLevel == 2:
                        print "Scope is now aligned, switching to pod view"
                        usingPodCam = True
                        virtCam.restore(camera.sceneobject.getKey())
                        virtCam.save(podCamera.getKey())
                        if not ageSDL["TdlmActivePedestalsKeep"][0]:
                            print "First time viewing pod, unlocking link to it."
                            ageSDL["TdlmActivePedestalsKeep"] = (1,)
                        PtAtTimeCallback(self.key, .3, 3)
            if scopeAlignWPod:
                scopeAlignWPod = self.isScopeAligned()
                if not scopeAlignWPod: ## probably true
                    if usingPodCam:
                        print "Alignment is no longer correct, switching back to normal scope cam"
                        usingPodCam = False
                        virtCam.restore(podCamera.getKey())
                        virtCam.save(camera.sceneobject.getKey())
                        # update FOV once we switched to new camera
                        PtAtTimeCallback(self.key, .3, 3)


    def Zoom0(self):
        global curZoomLevel
        curZoomLevel = 0
        anim00.animation.playToTime(0.5)
        anim01.animation.playToTime(0)
        anim02.animation.playToTime(0)

    def Zoom1(self):
        global curZoomLevel
        curZoomLevel = 1
        anim00.animation.playToTime(0)
        anim01.animation.playToTime(0.5)
        anim02.animation.playToTime(0)

    def Zoom2(self):
        global curZoomLevel
        curZoomLevel = 2
        anim00.animation.playToTime(0)
        anim01.animation.playToTime(0)
        anim02.animation.playToTime(0.5)


    def disableInput(self):
        act00.disable()
        act01.disable()
        act02.disable()
        PtAtTimeCallback(self.key, 1, 4)


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



