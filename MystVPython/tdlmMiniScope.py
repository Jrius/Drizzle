"""
Improved zoom slider to use smooth zooming.

Unfortunately, it seems increasing sensitivity doesn't work, I don't know why. Maybe it's related to the animation being too short or something.

Also, clicking on a draggable warps it to where the cursor is (engine limitation), which makes it very hard to control at high zoom level.

However, the good news are SDL spamming is reduced online, to avoid frying up the server. Plus, I disabled autoaiming, which was useless, annoying,
and not coded correctly. You're welcome.

I added some Drizzle PostMod updates, camera woozywazzyness should no longer happen, even in third person view (except when exiting the scope).
#"""


from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *
import string
kTagIDVert = 50
kTagIDSpin = 51
kTagIDZoom = 52
kTagIDExitGUI = 53
act = ptAttribActivator(1, 'Clickable to engage scope')
camera = ptAttribSceneobject(2, 'Scope camera')
GUI = ptAttribDropDownList(3, 'GUI Dialog Name', ('MiniScope01', 'MiniScope02', 'MiniScope03'))
SpinAnim = ptAttribAnimation(4, 'Spin Animation')
TiltAnim = ptAttribAnimation(5, 'Tilt Animation')
SpinSDL = ptAttribString(6, 'SDL Spin')
TiltSDL = ptAttribString(7, 'SDL Tilt')
ZoomSDL = ptAttribString(8, 'SDL Zoom')
SpinSlider = None
TiltSlider = None
ZoomSlider = None
Solution01H = 1644.55
Solution01V = 104.2
Solution02H = 1025
Solution02V = 98
curTilt = 0
curSpin = 0
curZoom = 0
engaged = false

# AutoAim: doesn't really look good IMHO. You could try adjusting values, or turn it off completely.
# This is still required to enable links to pedestals.
ThresholdH = 20 # default 20
ThresholdV = 2  # default 2
doAutoAim = 0   # default 1

class tdlmMiniScope(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6213
        version = 3
        self.version = version
        print '__init__tdlmMiniScope v.',
        print version,
        print '.12'


    def OnServerInitComplete(self):
        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(SpinSDL.value)
        ageSDL.setFlags(SpinSDL.value, 1, 1)
        ageSDL.setNotify(self.key, SpinSDL.value, 0.0)
        ageSDL.sendToClients(TiltSDL.value)
        ageSDL.setFlags(TiltSDL.value, 1, 1)
        ageSDL.setNotify(self.key, TiltSDL.value, 0.0)
        ageSDL.sendToClients(ZoomSDL.value)
        ageSDL.setFlags(ZoomSDL.value, 1, 1)
        ageSDL.setNotify(self.key, ZoomSDL.value, 0.0)
        print 'tdlmMiniScope: When I got here, ',
        print GUI.value,
        print ' was set to:'
        print '\tSpin value (',
        print SpinSDL.value,
        print ') was ',
        print ageSDL[SpinSDL.value][0]
        SpinAnim.animation.skipToTime((ageSDL[SpinSDL.value][0] / 30))
        print '\tTilt value (',
        print TiltSDL.value,
        print ') was ',
        print ageSDL[TiltSDL.value][0]
        TiltAnim.animation.skipToTime((ageSDL[TiltSDL.value][0] / 30))
        print '\tZoom value (',
        print ZoomSDL.value,
        print ') was ',
        print ageSDL[ZoomSDL.value][0]

        #TiltAnim.animation.speed(116) # allow us to rotate it fast
        #SpinAnim.animation.speed(116)


    def OnNotify(self, state, id, events):
        global SpinSlider
        global TiltSlider
        global ZoomSlider
        global engaged
        if (not (state)):
            return
        if not (PtFindAvatar(events) == PtGetLocalAvatar()) or not (PtWasLocallyNotified(self.key)): return
        ageSDL = PtGetAgeSDL()
        if (id == act.id):
            engaged = true
            print 'tdlmMiniScope.OnNotify: Activated. Trying to engage telescope.'
            cam = ptCamera()
            cam.undoFirstPerson()
            cam.disableFirstPersonOverride()
            #camera.value.pushCamera(camera.sceneobject.getKey()) # doesn't seem to work on PotS
            cam.save(camera.sceneobject.getKey())
            if (type(GUI.value) != type(None)):
                PtLoadDialog('MiniScope', self.key)
                if PtIsDialogLoaded('MiniScope'):
                    """print '\tSpin slider set to value ',
                    print ageSDL[SpinSDL.value][0]
                    SpinSlider.setValue(ageSDL[SpinSDL.value][0])
                    print '\tTilt slider set to value ',
                    print ageSDL[TiltSDL.value][0]
                    TiltSlider.setValue(ageSDL[TiltSDL.value][0])
                    print '\tZoom slider and camera FOV set to value ',
                    print ageSDL[ZoomSDL.value][0]
                    ZoomSlider.setValue(ageSDL[ZoomSDL.value][0])
                    scopecamera = ptCamera()
                    scopecamera.setFOV(ageSDL[ZoomSDL.value][0], 0) #"""
                    #print '\tHiding Slate'
                    #PtSendPythonNotify('Hide', 'Bahro Slate', self.key)
                    PtShowDialog('MiniScope')


    def OnGUINotify(self, id, control, event):
        global Solution01H
        global Solution01V
        global Solution02H
        global Solution02V
        global SpinSlider
        global TiltSlider
        global ZoomSlider
        global ThresholdH
        global ThresholdV
        global curTilt
        global curSpin
        global curZoom
        global engaged
        ageSDL = PtGetAgeSDL()
        if (GUI.value == 'MiniScope01'):
            SolutionH = Solution01H
            SolutionV = Solution01V
        elif (GUI.value == 'MiniScope02'):
            SolutionH = Solution02H
            SolutionV = Solution02V
        if (event == kDialogLoaded):
            SpinSlider = ptGUIControlKnob(control.getControlFromTag(kTagIDSpin))
            TiltSlider = ptGUIControlKnob(control.getControlFromTag(kTagIDVert))
            ZoomSlider = ptGUIControlKnob(control.getControlFromTag(kTagIDZoom))

            ageSDL = PtGetAgeSDL()
            curSpin = ageSDL[SpinSDL.value][0]
            curTilt = ageSDL[TiltSDL.value][0]
            curZoom = ageSDL[ZoomSDL.value][0]
            SpinSlider.setValue(curSpin)
            TiltSlider.setValue(curTilt)
            ZoomSlider.setValue(curZoom)
            scopecamera = ptCamera()
            scopecamera.setFOV(curZoom, 0)

            SpinSlider.disable()
            TiltSlider.disable()
            ZoomSlider.disable()
            PtAtTimeCallback(self.key, 0.1, 1)
        if control:
            contTag = control.getTagID()
        else:
            return
        if (contTag in [kTagIDVert, kTagIDZoom, kTagIDSpin]):
            newvalue = control.getValue()
        else:
            newvalue = 0
        if (contTag == kTagIDSpin):
            if (GUI.value == 'MiniScope03'):
                if PtIsSinglePlayerMode():
                    ageSDL[SpinSDL.value] = (newvalue,)
                else:
                    SpinAnim.animation.skipToTime((newvalue / 30))
                curSpin = newvalue
            elif ((newvalue > (SolutionH - ThresholdH)) and (newvalue < (SolutionH + ThresholdH)) and doAutoAim):
                ## this is wrong, because it means it will autoaim on two lines forming a +. We want it to focus on a dot.
                ## If you feel like doing this, autoaim should check BOTH X and Y rotation values.
                print 'AutoAiming Spin value! newvalue WAS ',
                print newvalue
                if PtIsSinglePlayerMode():
                    ageSDL[SpinSDL.value] = (SolutionH,)
                else:
                    SpinAnim.animation.skipToTime((SolutionH / 30))
                curSpin = newvalue
            else:
                if PtIsSinglePlayerMode():
                    ageSDL[SpinSDL.value] = (newvalue,)
                else:
                    SpinAnim.animation.skipToTime((newvalue / 30))
                curSpin = newvalue
        elif (contTag == kTagIDVert):
            if (GUI.value == 'MiniScope03'):
                if PtIsSinglePlayerMode():
                    ageSDL[TiltSDL.value] = (newvalue,)
                else:
                    TiltAnim.animation.skipToTime((newvalue / 30))
                curTilt = newvalue
            elif ((newvalue > (SolutionV - ThresholdV)) and (newvalue < (SolutionV + ThresholdV)) and doAutoAim):
                print 'AutoAiming Tilt value! newvalue WAS ',
                print newvalue
                if PtIsSinglePlayerMode():
                    ageSDL[TiltSDL.value] = (SolutionV,)
                else:
                    TiltAnim.animation.skipToTime((SolutionV / 30))
                curTilt = newvalue
            else:
                if PtIsSinglePlayerMode():
                    ageSDL[TiltSDL.value] = (newvalue,)
                else:
                    TiltAnim.animation.skipToTime((newvalue / 30))
                curTilt = newvalue
        elif (contTag == kTagIDZoom):
            if PtIsSinglePlayerMode():
                ageSDL[ZoomSDL.value] = (int(newvalue),)
            else:
                scopecamera = ptCamera()
                scopecamera.setFOV(newvalue, 0.1) # setting the changetime to 0.1 gives us a much smoother transition
            curZoom = newvalue
        elif (contTag == kTagIDExitGUI):
            print '\tThe MiniScope GUI should now be hidden.'
            PtHideDialog('MiniScope')
            engaged = false
            cam = ptCamera()
            #camera.value.popCamera(camera.sceneobject.getKey()) # doesn't seem to work on PotS
            cam.restore(camera.sceneobject.getKey())
            cam.enableFirstPersonOverride()
            print '\tPopping back to player camera.'
            #print '\tShowing the Slate'
            #PtSendPythonNotify('Show', 'Bahro Slate', self.key)
            print 'When I left the scope, '
            print '\tSpin value (',
            print SpinSDL.value,
            print ') was ',
            print ageSDL[SpinSDL.value][0]
            print '\tTilt value (',
            print TiltSDL.value,
            print ') was ',
            print ageSDL[TiltSDL.value][0]
            print '\tZoom value (',
            print ZoomSDL.value,
            print ') was ',
            print ageSDL[ZoomSDL.value][0]

        ## always check solution view
        self.CheckPedestalSight()


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        ageSDL = PtGetAgeSDL()
        if (VARname == TiltSDL.value):
            #print '\tTrying to tilt to time ',
            #print (ageSDL[VARname][0] / 30),
            #print ' seconds'
            #TiltAnim.animation.playToTime((ageSDL[VARname][0] / 30)) # make smooth rotation
            TiltAnim.animation.skipToTime((ageSDL[VARname][0] / 30))
        if (VARname == SpinSDL.value):
            #print '\tTrying to spin to time ',
            #print (ageSDL[VARname][0] / 30),
            #print ' seconds'
            #SpinAnim.animation.playToTime((ageSDL[VARname][0] / 30))
            SpinAnim.animation.skipToTime((ageSDL[VARname][0] / 30))
        if (VARname == ZoomSDL.value):
            #print '\tTrying to set FOV to ',
            #print ageSDL[VARname][0],
            #print ' degrees'
            if engaged:
                scopecamera = ptCamera()
                scopecamera.setFOV(ageSDL[VARname][0], 0.1) # setting the changetime to 0.1 gives us a much smoother transition


    def CheckPedestalSight(self):
        global Solution01H
        global Solution01V
        global Solution02H
        global Solution02V
        global ThresholdH
        global ThresholdV
        global curTilt
        global curSpin
        global curZoom
        ageSDL = PtGetAgeSDL()

        if (GUI.value == 'MiniScope01'):
            if not ageSDL["MiniScope01LOS"][0]:
                #print "Ped not visible from here."
                return
            if ageSDL["TdlmActivePedestals01"][0]:
                #print "Ped already enabled."
                return
            if (curSpin > (Solution01H - ThresholdH)) \
                        and (curSpin < (Solution01H + ThresholdH)):
                if (curTilt > (Solution01V - ThresholdV)) \
                            and (curTilt < (Solution01V + ThresholdV)):
                    if curZoom < 18: ## should be the last ticks of the slider (which goes all the way down to 2 degrees)
                        print "Now seeing first pedestal ! Unlocking link to it..."
                        ageSDL["TdlmActivePedestals01"] = (1,)


        elif (GUI.value == 'MiniScope02'):
            print "Checking scope 2..."

            if ageSDL["TdlmActivePedestals02"][0]:
                #print "Ped already enabled."
                return

            if (curSpin > (Solution02H - ThresholdH)) \
                        and (curSpin < (Solution02H + ThresholdH)):
                if (curTilt > (Solution02V - ThresholdV)) \
                            and (curTilt < (Solution02V + ThresholdV)):
                    if curZoom < 18:
                        # check for big scopes LOS
                        # Scope 2:
                        #   horiz in range [0, 0.3]
                        # Scope 3:
                        #   horiz in range [.8, 1]
                        #   vert in range [.32, .67]
                        bs2h = float(ageSDL["BigScope02Horiz"][0])
                        bs3h = float(ageSDL["BigScope03Horiz"][0])
                        bs3v = float(ageSDL["BigScope03Vert"][0])
                        if bs2h > .3 or bs3h < .8 or bs3v < .32 or bs3v > .67:
                            return

                        # you're seeing the damn thing, congrats !
                        print "Now seeing second pedestal ! Unlocking link to it..."
                        ageSDL["TdlmActivePedestals02"] = (1,)


    def OnTimer(self, id):
        global SpinSlider
        global TiltSlider
        global ZoomSlider
        if (id == 1):
            print 'Sliders enabled 1 second after GUI engaged.'
            SpinSlider.enable()
            TiltSlider.enable()
            ZoomSlider.enable()
            if not PtIsSinglePlayerMode():
                PtAtTimeCallback(self.key, 0.5, 2)
        elif id == 2:
            print "tdlmMiniScope: updating SDL position."
            ageSDL = PtGetAgeSDL()
            ageSDL[SpinSDL.value] = (curSpin,)
            ageSDL[TiltSDL.value] = (curTilt,)
            ageSDL[ZoomSDL.value] = (curZoom,)
            if engaged:
                PtAtTimeCallback(self.key, 0.5, 2)


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



