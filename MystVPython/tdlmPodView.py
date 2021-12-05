"""
Probably won't work due to DynamicCamMap not existing.
#"""

from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *
ScopeRgn = ptAttribActivator(1, 'Rgn by Scope01 panel')
Zoom = ptAttribString(2, 'SDL: Zoom Controls')
Wide = ptAttribSceneobject(3, 'Extreme Wide Pod View')
Medium = ptAttribSceneobject(4, 'Medium Pod View')
Slate = ptAttribSceneobject(5, 'Slate Pod View')
SDLSeason = ptAttribString(6, 'SDL: Todelmer Seasons')
HorizDrag = ptAttribActivator(7, 'act: Horiz Draggable')
VertDrag = ptAttribActivator(8, 'act: Vert Draggable')
actEnter = ptAttribActivator(9, 'act: Enter Button')
SlateSDL = ptAttribString(10, 'SDL: Slate updated')
HorizSDL = ptAttribString(11, 'SDL: Horiz Percentage')
VertSDL = ptAttribString(12, 'SDL: Vert Percentage')
PodViewH = 0.17
PodViewV = 0.145

class tdlmPodView(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6220
        version = 5
        self.version = version
        print '__init__ tdlmPodView v.',
        print version,
        print '.2'


    def OnServerInitComplete(self):
        ageSDL = PtGetAgeSDL()

        ageSDL.sendToClients(Zoom.value)
        ageSDL.setFlags(Zoom.value, 1, 1)
        ageSDL.setNotify(self.key, Zoom.value, 0.0)

        ageSDL.sendToClients(SDLSeason.value)
        ageSDL.setFlags(SDLSeason.value, 1, 1)
        ageSDL.setNotify(self.key, SDLSeason.value, 0.0)

        ageSDL.sendToClients(SlateSDL.value)
        ageSDL.setFlags(SlateSDL.value, 1, 1)
        ageSDL.setNotify(self.key, SlateSDL.value, 0.0)

        ageSDL.sendToClients(HorizSDL.value)
        ageSDL.setFlags(HorizSDL.value, 1, 1)
        ageSDL.setNotify(self.key, HorizSDL.value, 0.0)

        ageSDL.sendToClients(VertSDL.value)
        ageSDL.setFlags(VertSDL.value, 1, 1)
        ageSDL.setNotify(self.key, VertSDL.value, 0.0)
        self.CanBeSeen()


    def CanBeSeen(self):
        ageSDL = PtGetAgeSDL()
        if (ageSDL[SDLSeason.value][0] != 2):
            print '\tIt\'s the wrong season (',
            print ageSDL[SDLSeason.value][0],
            print '), so the Pod can\'t possibly be seen.'
            Wide.value.draw.disable()
            Medium.value.draw.disable()
            Slate.value.draw.disable()
            return
        elif (ageSDL[SlateSDL.value][0] != 0):
            print '\tSeason change is still in progress, so the Pod can\'t possibly be seen.'
            Wide.value.draw.disable()
            Medium.value.draw.disable()
            Slate.value.draw.disable()
            return
        elif (abs((ageSDL[HorizSDL.value][0] - PodViewH)) > 0.005):
            print '\tBigScope 01 is not aligned correctly horizontally (',
            print ageSDL[HorizSDL.value][0],
            print '), so the Pod can\'t possibly be seen.'
            print 'difference = ',
            print abs((ageSDL[HorizSDL.value][0] - PodViewH))
            Wide.value.draw.disable()
            Medium.value.draw.disable()
            Slate.value.draw.disable()
            return
        elif (abs((ageSDL[VertSDL.value][0] - PodViewV)) > 0.005):
            print '\tBigScope 01 is not aligned correctly vertically (',
            print ageSDL[VertSDL.value][0],
            print '), so the Pod can\'t possibly be seen.'
            print 'diff = ',
            print abs((ageSDL[VertSDL.value][0] - PodViewV))
            Wide.value.draw.disable()
            Medium.value.draw.disable()
            Slate.value.draw.disable()
            return
        else:
            self.PodThroughScope()


    def OnNotify(self, state, id, events):
        if not (PtFindAvatar(events) == PtGetLocalAvatar()) or not (PtWasLocallyNotified(self.key)): return
        if (id == ScopeRgn.id):
            for event in events:
                if ((event[0] == 1) and (event[1] == 1)):
                    print 'tdlmPodView: The player just approached the BigScope01 Panel.'
                    self.CanBeSeen()
                elif ((event[0] == 1) and (event[1] == 0)):
                    print 'tdlmPodView: The player just walked away from the BigScope01 Panel'
                    Wide.value.draw.disable()
                    Medium.value.draw.disable()
                    Slate.value.draw.disable()
        elif (id == HorizDrag.id):
            print 'tdlmPodView: Horiz Updated: ', "???"
            #print HorizDrag.getDraggableValue()
        elif (id == VertDrag.id):
            print 'tdlmPodView: Vert Updated: ', "???"
            #print VertDrag.getDraggableValue()


    def PodThroughScope(self):
        ageSDL = PtGetAgeSDL()
        if (ageSDL[Zoom.value][0] == 0):
            print '\tShowing the wide (very small) view of the pod.'
            Wide.value.draw.enable()
            Medium.value.draw.disable()
            Slate.value.draw.disable()
        elif (ageSDL[Zoom.value][0] == 1):
            print '\tShowing the Medium view of the pod.'
            Wide.value.draw.disable()
            Medium.value.draw.enable()
            Slate.value.draw.disable()
        elif (ageSDL[Zoom.value][0] == 2):
            print '\tShowing the Slate within the Pod.'
            Wide.value.draw.disable()
            Medium.value.draw.disable()
            #Slate.value.draw.enable() # only looks bad, change is done in BigScopeZoom
            Slate.value.draw.disable()


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        if (VARname == Zoom.value):
            print 'tdlmPodView.OnSDLNotify: The Zoom value was just changed.'
        elif (VARname == SlateSDL.value):
            print 'tdlmPodView.OnSDLNotify: Adam\'s slate time just started or stopped.'
        self.CanBeSeen()


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



