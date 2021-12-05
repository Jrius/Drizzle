from Plasma import *
from PlasmaTypes import *
import string
act1 = ptAttribActivator(1, 'act: Door Ext')
act2 = ptAttribActivator(2, 'act: Door Int')
act3 = ptAttribActivator(3, 'act: Behind Door')
respDoorOpen = ptAttribResponder(4, 'resp: Open Door')
respDoorClose = ptAttribResponder(5, 'resp: Close Door')
respStairsHide = ptAttribResponder(6, 'resp: Hide Stairs')
respStairsUnhide = ptAttribResponder(7, 'resp: Unhide Stairs')
DoorSDL = ptAttribString(8, 'AgeSDL: Door SDL')
StairsSDL = ptAttribString(9, 'AgeSDL: Stairs SDL')
NodeRgnAbove = ptAttribSceneobject(10, 'Node Rgn Above')
NodeRgnBelow = ptAttribSceneobject(11, 'Node Rgn Below')
PowerSDL = ptAttribString(12, 'AgeSDL: Power')
act4 = ptAttribActivator(13, 'act: Below Stairs')

class tdlmDoorStairs(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6212
        version = 4
        self.version = version
        print '__init__tdlmDoorStairs v.',
        print version,
        print '.3'


    def OnServerInitComplete(self):
        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(DoorSDL.value)
        ageSDL.setFlags(DoorSDL.value, 1, 1)
        ageSDL.setNotify(self.key, DoorSDL.value, 0.0)
        ageSDL.sendToClients(StairsSDL.value)
        ageSDL.setFlags(StairsSDL.value, 1, 1)
        ageSDL.setNotify(self.key, StairsSDL.value, 0.0)
        ageSDL.sendToClients(PowerSDL.value)
        ageSDL.setFlags(PowerSDL.value, 1, 1)
        ageSDL.setNotify(self.key, PowerSDL.value, 0.0)
        if (ageSDL[DoorSDL.value][0] == 1):
            print 'tdlmDoorStairs.OnInit: Door ',
            print DoorSDL.value,
            print ' was open when I got here.'
            respDoorOpen.run(self.key, fastforward=1)
        elif (ageSDL[DoorSDL.value][0] == 0):
            print 'tdlmDoorStairs.OnInit: Door ',
            print DoorSDL.value,
            print ' was closed when I got here.'
            respDoorClose.run(self.key, fastforward=1)
        if (ageSDL[StairsSDL.value][0] == 0):
            print 'tdlmDoorStairs.OnInit: Stairs ',
            print StairsSDL.value,
            print ' were HIDDEN when I got here.'
            respStairsHide.run(self.key, fastforward=1)
        elif (ageSDL[StairsSDL.value][0] == 1):
            print 'tdlmDoorStairs.OnInit: Stairs ',
            print StairsSDL.value,
            print ' were UNHidden when I got here.'
            respStairsUnhide.run(self.key, fastforward=1)
            #PtAtTimeCallback(self.key, 2, 5)


    def OnNotify(self, state, id, events):
        if (not (state)):
            return
        print 'tdlmDoorStairs.OnNotify id = ',
        print id
        ageSDL = PtGetAgeSDL()
        if (ageSDL[PowerSDL.value][0] == 0):
            print 'tdlmDoorStairs: Power is off. Pushing this button has no effect.'
            if (id == act1.id):
                PtAtTimeCallback(self.key, 2, 1)
            elif (id == act2.id):
                PtAtTimeCallback(self.key, 2, 2)
            elif (id == act3.id):
                PtAtTimeCallback(self.key, 2, 3)
            elif (id == act4.id):
                PtAtTimeCallback(self.key, 2, 4)
        else:
            if not (PtFindAvatar(events) == PtGetLocalAvatar()) or not (PtWasLocallyNotified(self.key)): return
            if (id == act1.id):
                if (ageSDL[DoorSDL.value][0] == 1):
                    print 'ERROR: Tried to open the door (from outside) that was already opened. This clickable should be inactive.'
                else:
                    ageSDL[DoorSDL.value] = (1,)
            if (id == act2.id):
                if (ageSDL[DoorSDL.value][0] == 1):
                    print 'ERROR: Tried to open the door (from inside) that was already opened. This clickable should be inactive.'
                else:
                    ageSDL[DoorSDL.value] = (1,)
            if (id == act3.id):
                print 'DoorSDL.value.getValue() = ',
                print ageSDL[DoorSDL.value][0]
                if (ageSDL[DoorSDL.value][0] == 1):
                    print 'Since the door was open, I\'ll close the door instead of lowering the stairs.'
                    ageSDL[DoorSDL.value] = (0,)
                else:
                    print 'The door was already closed, so now I can mess with the stairs:'
                    if (ageSDL[StairsSDL.value][0] == 0):
                        print 'Since the Stairs are up, put them down.'
                        ageSDL[StairsSDL.value] = (1,)
                    elif (ageSDL[StairsSDL.value][0] == 1):
                        print 'Since the Stairs are down, put them up.'
                        ageSDL[StairsSDL.value] = (0,)
            if (id == act4.id):
                if (ageSDL[StairsSDL.value][0] == 0):
                    print 'Since the Stairs are up, put them down.'
                    ageSDL[StairsSDL.value] = (1,)
                elif (ageSDL[StairsSDL.value][0] == 1):
                    print 'Since the Stairs are down, put them up.'
                    ageSDL[StairsSDL.value] = (0,)


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        ageSDL = PtGetAgeSDL()
        if (VARname == DoorSDL.value):
            if (ageSDL[VARname][0] == 1):
                print 'tdlmDoorStairs.OnSDLNotify: Door',
                print ageSDL[VARname][0],
                print 'was just opened.'
                respDoorOpen.run(self.key)
            elif (ageSDL[VARname][0] == 0):
                print 'tdlmDoorStairs.OnSDLNotify: Door',
                print ageSDL[VARname][0],
                print 'was just closed.'
                respDoorClose.run(self.key)
        if (VARname == StairsSDL.value):
            if (ageSDL[VARname][0] == 1):
                print 'tdlmDoorStairs.OnSDLNotify: Stairs',
                print ageSDL[VARname][0],
                print 'was just revealed.'
                respStairsUnhide.run(self.key)
                #NodeRgnAbove.value.disable()
                #NodeRgnBelow.value.enable()
            elif (ageSDL[VARname][0] == 0):
                print 'tdlmDoorStairs.OnSDLNotify: Stairs',
                print ageSDL[VARname][0],
                print 'was just concealed.'
                respStairsHide.run(self.key)
                #NodeRgnAbove.value.enable()
                #NodeRgnBelow.value.disable()


    def OnTimer(self, id):
        print 'tdlmDoorStairs.OnTimer: Button ',
        print id,
        print ' re-enabled.'
        if (id == 1):
            act1.enable()
        elif (id == 2):
            act2.enable()
        elif (id == 3):
            act3.enable()
        elif (id == 4):
            act4.enable()
        #elif (id == 5):
        #    print 'tdlmDoorStairs: Setting openstair novice nodes after a short init delay.'
        #    NodeRgnAbove.value.disable()
        #    NodeRgnBelow.value.enable()


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



