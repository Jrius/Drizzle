"""
Replaces the plAxisAnimModifiers (which are broken in multiplayer) in Myst V Ages.
"""


from Plasma import *
from PlasmaTypes import *

interactableType = ptAttribDropDownList(1, "Type of interactable", (
    # Animation simply plays forward on click.
    "SingleUse",
    # Animation either is a toggle, or has multiple stopping points. SDL variable name and value array must be specified.
    "MultiPos"))
activator = ptAttribActivator(2, "Main clickable")
activatorIncrease = ptAttribActivator(3, "Clickable - increase (if multipos)")
activatorDecrease = ptAttribActivator(4, "Clickable - decrease (if multipos)")
sdlName = ptAttribString(5, "SDL variable (if multipos)")
sdlValuesArrayStr = ptAttribString(6, "Values through which to progress (if multipos)", "0, 1")
animation = ptAttribAnimation(7, "Progress animation")
reverse = ptAttribBoolean(8, "Reverse animation progression", default=0)

# sdlValuesArrayStr converted to an array of ints/floats
sdlValuesArray = []
# whether to progress forward or backward in sdlValuesArray
curDirectionIsForward = True

class xEoAMultiposInteractable(ptResponder):

    def __init__(self):
        ptResponder.__init__(self)
        self.id = -1
        self.version = 1
        print("xEoaMultiposInteractable.__init__")

    def OnServerInitComplete(self):
        global sdlValuesArray
        if interactableType.value == "MultiPos":
            sdlValuesArray = [float(val.strip()) for val in sdlValuesArrayStr.value.split(',')]

    def OnNotify(self, state, id, events):
        global curDirectionIsForward
        if not state:
            return
        if PtFindAvatar(events) != PtGetLocalAvatar() or not PtWasLocallyNotified(self.key):
            return

        if id == activator.id:
            print("xEoaMultiposInteractable: main clickable triggered.")
            if interactableType.value == "SingleUse":
                print("xEoaMultiposInteractable: fire and forget anim.")
                if reverse.value:
                    animation.value.backwards(True)
                animation.value.play()
            else:
                ageSDL = PtGetAgeSDL()
                try:
                    oldValueIndex = curValueIndex = sdlValuesArray.index(ageSDL[sdlName.value][0])
                except ValueError:
                    print("xEoaMultiposInteractable: cannot find SDL value index ? Cur value: %s, array: %s" % (ageSDL[sdlName.value][0], sdlValuesArray))
                    raise
                arrayLength = len(sdlValuesArray)
                if (curDirectionIsForward and oldValueIndex + 1 < arrayLength) or oldValueIndex == 0:
                    curValueIndex += 1
                    curDirectionIsForward = True
                else:
                    curValueIndex -= 1
                    curDirectionIsForward = False
                print("xEoaMultiposInteractable: animation synced to %s, which is at value %s. Progressing to %s." % (sdlName.value, oldValueIndex, curValueIndex))
                self.PlayToPercentage(curValueIndex / float(len(sdlValuesArray) - 1), curDirectionIsForward)
        elif id == activatorIncrease.id and interactableType.value == "MultiPos":
            ageSDL = PtGetAgeSDL()
            oldValueIndex = curValueIndex = sdlValuesArray.index(ageSDL[sdlName.value][0])
            arrayLength = len(sdlValuesArray)
            if oldValueIndex + 1 < arrayLength:
                curValueIndex += 1
                print("xEoaMultiposInteractable: animation synced to %s, which is at value %s. Increasing to %s." % (sdlName.value, oldValueIndex, curValueIndex))
                self.PlayToPercentage(curValueIndex / float(len(sdlValuesArray) - 1), True)
        elif id == activatorDecrease.id and interactableType.value == "MultiPos":
            ageSDL = PtGetAgeSDL()
            oldValueIndex = curValueIndex = sdlValuesArray.index(ageSDL[sdlName.value][0])
            arrayLength = len(sdlValuesArray)
            if oldValueIndex > 0:
                curValueIndex -= 1
                print("xEoaMultiposInteractable: animation synced to %s, which is at value %s. Decreasing to %s." % (sdlName.value, oldValueIndex, curValueIndex))
                self.PlayToPercentage(curValueIndex / float(len(sdlValuesArray) - 1), False)

    def PlayToPercentage(self, percent, forward):
        # We have to ensure we're roughly 1 frame after the requested time, in order to trigger animevents that may be placed on the target frame.
        if curDirectionIsForward:
            percent = min(percent + 0.01, 1)
        else:
            percent = max(percent - 0.01, 0)

        if reverse.value:
            percent = 1 - percent

        # Similarly, note that a bug in Plamza causes playToPercentage() to sometime fail to trigger beginning/end anim event modifiers.
        # Thus when we're sure to reach 0% or 100%, use regular play() instead.
        if percent == 0:
            animation.value.backwards(True)
            animation.value.play()
        elif percent == 1:
            animation.value.backwards(False)
            animation.value.play()
        else:
            animation.value.playToPercentage(percent)





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
