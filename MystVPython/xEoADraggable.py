"""
Replaces the plAxisAnimModifiers (which are broken in multiplayer) in Myst V Ages.

Due to let's say "oddities" in Plasma, we no longer have anim event modifiers either. Instead, the whole SDL/anim stuff is handled by this Python script.
"""

from Plasma import *
from PlasmaTypes import *

activator = ptAttribActivator(2, "Main clickable")
activatorIncrease = ptAttribActivator(3, "Clickable - increase (if multipos)")
activatorDecrease = ptAttribActivator(4, "Clickable - decrease (if multipos)")
sdlName = ptAttribString(5, "SDL variable")
sdlValueToTimeStr = ptAttribString(6, "SDL value to time list")
animation = ptAttribAnimation(7, "Progress animation")
reverse = ptAttribBoolean(8, "Reverse animation progression", default=0)
looping = ptAttribBoolean(9, "Looping", default=0)
autoReenable = ptAttribBoolean(10, "Auto reenable after anim", default=1)

# sdlValueToTimeStr converted to [(sdl value, time),]
sdlValueToTime = []
# whether to progress forward or backward in sdlValuesArray
curDirectionIsForward = True
# old stage at which the draggable was, before SDL changes
oldIndex = -1

class xEoADraggable(ptResponder):

    def __init__(self):
        ptResponder.__init__(self)
        self.id = -1
        self.version = 1
        print("xEoADraggable.__init__")

    def OnServerInitComplete(self):
        global sdlValueToTime
        global oldIndex
        # parse sdlValueToTimeStr (quick and dirty code)
        if sdlValueToTimeStr.value:
            if sdlValueToTimeStr.value[0] != '{' or sdlValueToTimeStr.value[-1] != '}':
                raise RuntimeError("sdlValueToTimeStr should be a dictionary")
            sdlValueToTime = [
                (
                    float(val.split(':')[0]),
                    float(val.split(':')[1])
                )
                for val in sdlValueToTimeStr.value[1:-1].split(',')
            ]
        else:
            # not specified ? Assume a simple boolean and ignore time
            sdlValueToTime = [(int(reverse.value), None), (int(not reverse.value), None)]
        self.DebugLog("sdlValueToTime: %s" % sdlValueToTime)
        if sdlName.value:
            # register SDL notifications
            ageSDL = PtGetAgeSDL()
            ageSDL.sendToClients(sdlName.value)
            ageSDL.setFlags(sdlName.value, 1, 1)
            ageSDL.setNotify(self.key, sdlName.value, 0.0)
            oldIndex = self.GetCurIndex()

    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global curDirectionIsForward
        global oldIndex
        newValue = PtGetAgeSDL()[VARname][0]
        self.DebugLog("xEoADraggable.OnSDLNotify: %s to %s" % (VARname, newValue))
        if VARname != sdlName.value:
            return

        # SDL value changed !

        # get the new draggable stage index
        curIndex = self.GetCurIndex()

        # disable the clickable
        self.EnableClickables(False)

        # keep track of progress direction
        curDirectionIsForward = oldIndex < curIndex or curIndex == 0

        # if anim times were specified...
        if sdlValueToTimeStr.value:
            # get animation times
            timeOld = sdlValueToTime[oldIndex][1]
            timeNew = sdlValueToTime[curIndex][1]
            timeDiff = timeNew - timeOld

            if looping.value:
                # Plasma's animator is stupid. playToTime makes it loop endlessly when using looping anims.
                # But lo ! Handling the loop ourself makes it work correctly !
                if oldIndex == len(sdlValueToTime) - 1 and curIndex == 0:
                    # looping forward - hax the thing
                    timeDiff = timeOld - sdlValueToTime[oldIndex - 1][1]
                    timeNew = timeOld + timeDiff
                elif oldIndex == 0 and curIndex == len(sdlValueToTime) - 1:
                    # looping backward - again, hax the thing
                    timeDiff = timeNew - sdlValueToTime[curIndex - 1][1]
                    timeOld = timeNew + timeDiff
                # further hax the thing by disabling looping and skipping to anim begin.
                animation.value.looped(0)
                animation.value.skipToTime(timeOld)
                animation.value.playToTime(timeNew)
            else:
                # run the lever anim
                animation.value.playToTime(timeNew)
        else:
            # just play ping-pong
            animation.value.backwards(not curIndex)
            animation.value.play()
            if autoReenable.value:
                timeDiff = 1
            else:
                timeDiff = None

        # remember our current index for next SDL change
        oldIndex = curIndex

        if timeDiff != None:
            # come back when animation is finished to reenable clickables
            PtAtTimeCallback(self.key, timeDiff, 1)

    def OnNotify(self, state, id, events):
        if not state:
            return
        if PtFindAvatar(events) != PtGetLocalAvatar() or not PtWasLocallyNotified(self.key):
            return

        if not sdlName.value:
            # simple oneshot - play the lever anim...
            animation.value.netForce(1)
            animation.value.play()
            self.EnableClickables(False)
            # some other system is bound to reenable the clickable at some point, no need to add a callback...
            return

        if id == activator.id:
            self.PlayAuto()
        elif id == activatorIncrease.id:
            self.PlayIncrease()
        elif id == activatorDecrease.id:
            self.PlayDecrease()

    def PlayAuto(self):
        self.DebugLog("xEoADraggable: main clickable triggered.")
        curIndex = self.GetCurIndex()
        curVal = sdlValueToTime[curIndex][0]
        curTime = sdlValueToTime[curIndex][1]
        ageSDL = PtGetAgeSDL()

        nextIndex = -1
        if looping.value:
            nextIndex = (curIndex + 1) % len(sdlValueToTime)
        else:
            if curDirectionIsForward and curIndex + 1 < len(sdlValueToTime):
                nextIndex = curIndex + 1
            elif curIndex - 1 >= 0:
                nextIndex = curIndex - 1

        if nextIndex >= 0:
            nextVal = sdlValueToTime[nextIndex][0]
            nextTime = sdlValueToTime[nextIndex][1]
            self.DebugLog("xEoADraggable: playing from state %s (sdl %s, time %s) to %s (sdl %s, time %s)" % (curIndex, curVal, curTime, nextIndex, nextVal, nextTime))
            ageSDL[sdlName.value] = (nextVal,)

    def PlayIncrease(self):
        self.DebugLog("xEoADraggable: increase clickable triggered.")
        nextIndex = self.GetCurIndex() + 1
        if nextIndex < len(sdlValueToTime):
            ageSDL = PtGetAgeSDL()
            ageSDL[sdlName.value] = (sdlValueToTime[nextIndex][0],)
        else:
            self.DebugLog("xEoADraggable: ...but already at max.")

    def PlayDecrease(self):
        self.DebugLog("xEoADraggable: decrease clickable triggered.")
        nextIndex = self.GetCurIndex() - 1
        if nextIndex >= 0:
            ageSDL = PtGetAgeSDL()
            ageSDL[sdlName.value] = (sdlValueToTime[nextIndex][0],)
        else:
            self.DebugLog("xEoADraggable: ...but already at min.")

    def OnTimer(self, id):
        # Reenable the activators after the animation finished playing
        self.EnableClickables(True)

    def EnableClickables(self, enable):
        if enable:
            activator.enable()
            activatorIncrease.enable()
            activatorDecrease.enable()
        else:
            activator.disable()
            activatorIncrease.disable()
            activatorDecrease.disable()

    def GetCurIndex(self):
        ageSDL = PtGetAgeSDL()
        curValue = ageSDL[sdlName.value][0]
        self.DebugLog("curValue: %s" % curValue)
        curValue = round(curValue, 3)
        i = 0
        for t in sdlValueToTime:
            if abs(t[0] - curValue) <= 0.001:
                return i
            i += 1
        raise RuntimeError("Couldn't find cur SDL index for var %s with value %s ?..." % (sdlName.value, curValue))

    def DebugLog(self, msg):
        PtPrintToScreen(msg)
        print(msg)





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
    global glue_paramKeys
    global glue_params
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
                print ('%s has id %d which is already defined in %s' %
                      (obj.name, obj.id, glue_params[obj.id].name))
        else:
            glue_params[obj.id] = obj
    elif type(obj) == type([]):
        for o in obj:
            glue_findAndAddAttribs(o, glue_params)
    elif type(obj) == type({}):
        for o in obj.values():
            glue_findAndAddAttribs(o, glue_params)
    elif type(obj) == type(()):
        for o in obj:
            glue_findAndAddAttribs(o, glue_params)


def glue_getParamDict():
    global glue_paramKeys
    global glue_params
    if type(glue_params) == type(None):
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
    pd = glue_getParamDict()
    if (pd != None):
        if type(glue_paramKeys) == type([]):
            if (number >= 0) and (number < len(glue_paramKeys)):
                return pd[glue_paramKeys[number]].getdef()
            else:
                print ('glue_getParam: Error! %d out of range of attribute list' % number)
        else:
            pl = pd.values()
            if (number >= 0) and (number < len(pl)):
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
                        if type(pd[id].value) != type([]):
                            pd[id].value = []
                    except AttributeError:
                        pd[id].value = []
                    pd[id].value.append(value)
                else:
                    pd[id].value = value
        elif glue_verbose:
            print "setParam: can't find id=", id
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
    pd = glue_getParamDict()
    if pd != None:
        if type(glue_paramKeys) == type([]):
            if (number >= 0) and (number < len(glue_paramKeys)):
                return pd[glue_paramKeys[number]].getVisInfo()
            else:
                print ('glue_getVisInfo: Error! %d out of range of attribute list' % number)
        else:
            pl = pd.values()
            if (number >= 0) and (number < len(pl)):
                return pl[number].getVisInfo()
            elif glue_verbose:
                print ('glue_getVisInfo: Error! %d out of range of attribute list' % number)
    if glue_verbose:
        print 'GLUE: Attribute list error'
    return None
