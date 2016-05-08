"""
Note: updated draggables to not use .getDraggableValue()
#"""


from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *
HorizSDL = ptAttribString(1, 'SDL: Horiz Percentage')
VertSDL = ptAttribString(2, 'SDL: Vert Percentage')
HorizDrag = ptAttribActivator(3, 'act: Horiz Draggable')
VertDrag = ptAttribActivator(4, 'act: Vert Draggable')
actEnter = ptAttribActivator(5, 'act: Enter Button')
HorizAnim = ptAttribAnimation(6, 'anim: Camera Horiz')
VertAnim = ptAttribAnimation(7, 'anim: Camera Vert')
respCablesStart = ptAttribResponder(8, 'resp: Cable Start')
respCablesStop = ptAttribResponder(9, 'resp: Cable Stop')
respEnterOff = ptAttribResponder(10, 'resp: EnterBtn Off')
respEnterBlink = ptAttribResponder(11, 'resp: EnterBtn Blink')
respEnterOn = ptAttribResponder(12, 'resp: EnterBtn On')
respHorizSfx = ptAttribResponder(13, 'resp: Horiz Drag Sfx')
respVertSfx = ptAttribResponder(14, 'resp: Vert Drag Sfx')
PowerSDL = ptAttribString(15, 'SDL: Pillar 1 Power')
HorizDragAnim = ptAttribAnimation(16, 'anim: Horiz Drag ')
VertDragAnim = ptAttribAnimation(17, 'anim: Vert Drag')
WhichScope = ptAttribDropDownList(18, 'Is This Scope01?', ('Yup', 'Nope'))
actSliderSteps = {}
for i in range(21):
    ptAttribActivator(19+i*2,   'act: Horiz Step %s' % i)
    ptAttribActivator(19+i*2+1, 'act: Vert  Step %s' % i)
    actSliderSteps   [19+i*2] = i*(1/21.)
scopespeed = 0.3
PodViewH = 0.17
PodViewV = 0.145
ThresholdH = 0.1
ThresholdV = 0.1
TransitionTime = 0
EnterShouldBlink = false
HorizSDLPrevVal = 0.0
VertSDLPrevVal = 0.0

class tdlmBigScope(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6222
        version = 5
        self.version = version
        print '__init__ tdlmBigScope v.',
        print version,
        print '.7'


    def OnFirstUpdate(self):
        global HorizSDLPrevVal
        global VertSDLPrevVal
        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(HorizSDL.value)
        ageSDL.setFlags(HorizSDL.value, 1, 1)
        ageSDL.setNotify(self.key, HorizSDL.value, 0.0)
        ageSDL.sendToClients(VertSDL.value)
        ageSDL.setFlags(VertSDL.value, 1, 1)
        ageSDL.setNotify(self.key, VertSDL.value, 0.0)
        
        HorizSDLPrevVal = ageSDL[HorizSDL.value][0]
        VertSDLPrevVal  = ageSDL[VertSDL .value][0]
        
        # hack to make buttons usable
        horizSliderProgress = ageSDL[HorizSDL.value + "Slider"][0]
        vertSliderProgress  = ageSDL[VertSDL .value + "Slider"][0]
        
        HorizDragAnim   .animation.skipToTime(float( horizSliderProgress       * (10 / 3.0) ))
        HorizAnim       .animation.skipToTime(float( ageSDL[HorizSDL.value][0] * (10 / 3.0) ))
        VertDragAnim.animation.skipToTime(float( vertSliderProgress       * (10 / 3.0) ))
        VertAnim    .animation.skipToTime(float( ageSDL[VertSDL.value][0] * (10 / 3.0) ))


    def OnNotify(self, state, id, events):
        global EnterShouldBlink
        global PodViewH
        global ThresholdH
        global PodViewV
        global ThresholdV
        if (not (state)):
            return
        ageSDL = PtGetAgeSDL()
        
        if (not PtWasLocallyNotified(self.key) or PtFindAvatar(events) != PtGetLocalAvatar()): return
        
        if ((id == HorizDrag.id) or (id == VertDrag.id)):
            print "Notify from draggable activator"
            raise RuntimeError("WHAT ?!")
            
            """EnterShouldBlink = true
            if (ageSDL[PowerSDL.value][0] == 0):
                print 'tdlmBigScope: Pillar 1 power is off, so the enter button doesn\'t blink... yet'
            else:
                for event in events:
                    if ((event[0] == kCallbackEvent) and (event[1] == PtEventCallbackType.kValueChanged)):
                        respEnterBlink.run(self.key)
            # these responders don't actually exist
            #if (id == HorizDrag.id):
            #    respHorizSfx.run(self.key)
            #elif (id == VertDrag.id):
            #    respVertSfx.run(self.key)
            #"""
        
        elif (id == actEnter.id):
            if (ageSDL[PowerSDL.value][0] == 0):
                print 'tdlmBigScope: Pillar 1 power is off, so the enter button doesn\'t respond.'
            else:
                EnterShouldBlink = false
                
                horizSliderProgress = ageSDL[HorizSDL.value + "Slider"][0]
                vertSliderProgress  = ageSDL[VertSDL .value + "Slider"][0]
                
                print 'Enter button pressed. Current value of draggables:'
                if (WhichScope.value == 'Yup'):
                    if ((horizSliderProgress > (PodViewH - ThresholdH)) and (horizSliderProgress < (PodViewH + ThresholdH))):
                        print '\t\tAutoaim Horiz Podview Value!'
                        newvalueH = PodViewH
                    else:
                        newvalueH = horizSliderProgress
                    if ((vertSliderProgress > (PodViewV - ThresholdV)) and (vertSliderProgress < (PodViewV + ThresholdV))):
                        print '\t\tAuto Vert Podview value!'
                        newvalueV = PodViewV
                    else:
                        newvalueV = vertSliderProgress
                else:
                    newvalueH = horizSliderProgress
                    newvalueV = vertSliderProgress
                print '\tHoriz = ',
                print newvalueH
                print '\tVert = ',
                print newvalueV
                if ((ageSDL[HorizSDL.value][0] == newvalueH) and (ageSDL[VertSDL.value][0] == newvalueV)):
                    print 'Nothing moved... turn off blink'
                    respEnterOff.run(self.key)
                
                if not (PtFindAvatar(events) == PtGetLocalAvatar()) or not (PtWasLocallyNotified(self.key)): return
                
                ageSDL[HorizSDL.value] = (newvalueH,)
                ageSDL[VertSDL.value]  = (newvalueV,)
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
        if (VARname == HorizSDL.value):
            newhoriz = ageSDL[HorizSDL.value][0]
            HorizAnim.value.speed(scopespeed)
            HorizAnim.animation.playToTime(float((newhoriz * 3.33)))
            Htransitiontime = ((abs((HorizSDLPrevVal - ageSDL[VARname][0])) * 3.33) / scopespeed)
            
            HorizSDLPrevVal = ageSDL[HorizSDL.value][0]
            
            if (Htransitiontime > TransitionTime):
                print 'Horiz transition to ',
                print ageSDL[VARname][0],
                print ' will take ',
                print Htransitiontime,
                print ' seconds.'
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
            VertAnim.value.speed(scopespeed)
            VertAnim.animation.playToTime(float((newvert * 3.33)))
            Vtransitiontime = ((abs((VertSDLPrevVal - ageSDL[VARname][0])) * 3.33) / scopespeed)
            
            VertSDLPrevVal  = ageSDL[VertSDL.value][0]
            
            if (Vtransitiontime > TransitionTime):
                print 'Vert transition to ',
                print ageSDL[VARname][0],
                print ' will take ',
                print Vtransitiontime,
                print ' seconds.'
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
        else:
            print 'BigScope: Something other than horz and vert changed:',
            print VARname
            respEnterOff.run(self.key)
            TransitionTime = 0
            return
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



