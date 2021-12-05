from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *
PileSDL = ptAttribString(1, 'SDL Rope Pile')
PowerSDL = ptAttribString(2, 'SDL Power')
HorizSDL = ptAttribString(3, 'SDL Horiz')
VertSDL = ptAttribString(4, 'SDL Vert')
HorizDrag = ptAttribActivator(5, 'act: Horiz Draggable')
VertDrag = ptAttribActivator(6, 'act: Vert Draggable')
HorizAnim = ptAttribAnimation(7, 'anim: Horiz Anim')
VertAnim = ptAttribAnimation(8, 'anim: Vert Anim')
actEnter = ptAttribActivator(9, 'act: Enter Button')
respCablesStart = ptAttribResponder(10, 'resp: Cable Start')
respCablesStop = ptAttribResponder(11, 'resp: Cable Stop')
respEnterOff = ptAttribResponder(12, 'resp: EnterBtn Off')
respEnterBlink = ptAttribResponder(13, 'resp: EnterBtn Blink')
respEnterOn = ptAttribResponder(14, 'resp: EnterBtn On')
respHorizSfx = ptAttribResponder(15, 'resp: Horiz Drag Sfx')
respVertSfx = ptAttribResponder(16, 'resp: Vert Drag Sfx')
actSliderSteps = {}
for i in range(21):
    ptAttribActivator(17+i*2,   'act: Horiz Step %s' % i)
    ptAttribActivator(17+i*2+1, 'act: Vert  Step %s' % i)
    actSliderSteps   [17+i*2] = i*(1/21.)
Horiz = false
Vert = false
scopespeed = 0.3
TransitionTime = 0
EnterShouldBlink = false
HorizSDLPrevVal = 0.0
VertSDLPrevVal = 0.0

class tdlmRopePile(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6211
        version = 4
        self.version = version
        print '__init__ tdlmRopePile v.',
        print version,
        print '.4'


    def OnServerInitComplete(self):
        global HorizSDLPrevVal
        global VertSDLPrevVal
        ageSDL = PtGetAgeSDL()

        ageSDL.sendToClients(PileSDL.value)
        ageSDL.setFlags(PileSDL.value, 1, 1)
        ageSDL.setNotify(self.key, PileSDL.value, 0.0)

        ageSDL.sendToClients(PowerSDL.value)
        ageSDL.setFlags(PowerSDL.value, 1, 1)
        ageSDL.setNotify(self.key, PowerSDL.value, 0.0)

        ageSDL.sendToClients(HorizSDL.value)
        ageSDL.setFlags(HorizSDL.value, 1, 1)
        ageSDL.setNotify(self.key, HorizSDL.value, 0.0)

        ageSDL.sendToClients(VertSDL.value)
        ageSDL.setFlags(VertSDL.value, 1, 1)
        ageSDL.setNotify(self.key, VertSDL.value, 0.0)

        HorizSDLPrevVal = ageSDL[HorizSDL.value][0]
        VertSDLPrevVal  = ageSDL[VertSDL .value][0]

        horizSliderProgress = ageSDL[HorizSDL.value + "Slider"][0]
        vertSliderProgress  = ageSDL[VertSDL .value + "Slider"][0]

        # not sure we're running correct animation... whatever
        HorizAnim.animation.skipToTime((horizSliderProgress * (10 / 3.0)))
        VertAnim .animation.skipToTime((vertSliderProgress * (10 / 3.0)))


    def OnNotify(self, state, id, events):
        global EnterShouldBlink
        if (not (state)):
            return

        if (not PtWasLocallyNotified(self.key) or PtFindAvatar(events) != PtGetLocalAvatar()): return

        ageSDL = PtGetAgeSDL()
        if ((id == HorizDrag.id) or (id == VertDrag.id)):
            print "Notify from draggable activator"
            raise RuntimeError("WHAT ?!")

            """EnterShouldBlink = true
            if (ageSDL[PowerSDL.value][0] == 0):
                print 'tdlmBigScope: Pillar 1 power is off, so the enter button doesn\'t blink.'
            else:
                for event in events:
                    if ((event[0] == kCallbackEvent) and (event[1] == PtEventCallbackType.kValueChanged)):
                        respEnterBlink.run(self.key)
            if (id == HorizDrag.id):
                respHorizSfx.run(self.key)
            elif (id == VertDrag.id):
                respVertSfx.run(self.key) #"""
        elif (id == actEnter.id):
            if (ageSDL[PowerSDL.value][0] == 0):
                print 'tdlmRopePile: Pillar 1 power is off, so the enter button doesn\'t respond.'
            else:
                EnterShouldBlink = false

                horizSliderProgress = ageSDL[HorizSDL.value + "Slider"][0]
                vertSliderProgress  = ageSDL[VertSDL .value + "Slider"][0]

                print 'tldmRopePile: Enter button pressed. Current value of draggables:'
                print '\tHoriz = ',
                print horizSliderProgress
                print '\tVert = ',
                print vertSliderProgress
                ageSDL[HorizSDL.value] = (horizSliderProgress,)
                ageSDL[VertSDL.value] = (vertSliderProgress,)
        elif id != -1:
            print "tdlmBigScope: notify from id", id

            EnterShouldBlink = true
            if (ageSDL[PowerSDL.value][0] == 0):
                print 'tdlmBigScope: Pillar 1 power is off, so the enter button doesn\'t blink... yet'
            else:
                respEnterBlink.run(self.key, netForce=1)

            # Not readable, I know, but I like to show off
            ageSDL[ (VertSDL, HorizSDL) [id%2] .value + "Slider" ] = \
                    ( actSliderSteps[ (id-1, id)[id%2] ] ,)

            print "Sliders progress now = (%s, %s)" % (ageSDL[HorizSDL.value + "Slider"][0], ageSDL[VertSDL.value + "Slider"])


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global EnterShouldBlink
        global scopespeed
        global TransitionTime
        global HorizSDLPrevVal
        global VertSDLPrevVal
        ageSDL = PtGetAgeSDL()
        if (VARname == PowerSDL.value):
            if (ageSDL[VARname][0] == 0):
                print 'tdlmBigScope: Pillar 1 Power just turned off. Turning Enter Btn material off.'
                respEnterOff.run(self.key)
            elif (ageSDL[VARname][0] == 1):
                if (EnterShouldBlink == true):
                    print 'The slider was moved, or the enter was previous blinking, before the power was turned on.'
                    print 'Enter Button for kiosk ',
                    print PowerSDL.value,
                    print ' should now blink.'
                    respEnterBlink.run(self.key)
            return
        # fixed transition time ? How ugly is that ?!
        #if ((VARname == VertSDL.value) or (VARname == HorizSDL.value)):
        #    respCablesStart.run(self.key)
        #    respEnterOn.run(self.key)
        #    PtAtTimeCallback(self.key, 5, 1)
        if (VARname == HorizSDL.value):
            newhoriz = ageSDL[HorizSDL.value][0]
            Htransitiontime = ((abs((HorizSDLPrevVal - ageSDL[VARname][0])) * 3.33) / scopespeed)

            HorizSDLPrevVal = ageSDL[HorizSDL.value][0]

            if (Htransitiontime > TransitionTime):
                TransitionTime = Htransitiontime
                respCablesStart.run(self.key)
                PtClearTimerCallbacks(self.key)
                PtAtTimeCallback(self.key, (TransitionTime + 0.2), 1)
                respEnterOn.run(self.key)
            else:
                print 'Turning off enter. Horiz transtion is moving, but it will be done before the Vertical movement.'
                respEnterOff.run(self.key)
                TransitionTime = 0
                return
        elif (VARname == VertSDL.value):
            newvert = ageSDL[VertSDL.value][0]
            Vtransitiontime = ((abs((VertSDLPrevVal - ageSDL[VARname][0])) * 3.33) / scopespeed)

            VertSDLPrevVal  = ageSDL[VertSDL.value][0]

            if (newvert <= 0.33):
                print '\tThere should be LOTS of rope pile.'
                ageSDL[PileSDL.value] = (0,)
            elif ((newvert > 0.33) and (newvert < 0.66)):
                print '\tThere should be SOME rope pile.'
                ageSDL[PileSDL.value] = (1,)
            elif (newvert >= 0.66):
                print '\tThere should be NO pile.'
                ageSDL[PileSDL.value] = (2,)

            if (Vtransitiontime > TransitionTime):
                TransitionTime = Vtransitiontime
                respCablesStart.run(self.key)
                PtClearTimerCallbacks(self.key)
                PtAtTimeCallback(self.key, (TransitionTime + 0.2), 1)
                respEnterOn.run(self.key)
            else:
                print 'Turning off enter. Vert transition is moving, but it will be done before the Horizontal movement.'
                respEnterOff.run(self.key)
                TransitionTime = 0
                return
        #elif (VARname == HorizSDL.value):
        #    print 'tdlmRopePile.OnSDLNotify: New ScopePile Horizontal draggable value = ',
        #    print HorizDrag.getDraggableValue()
        HorizDrag.disable()
        VertDrag.disable()


    def OnTimer(self, id):
        global TransitionTime
        if (id == 1):
            print 'The big scope finished moving. Stopping cables. Enter button Off.'
            respCablesStop.run(self.key)
            respEnterOff.run(self.key)
            HorizDrag.enable()
            VertDrag.enable()
            TransitionTime = 0


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



