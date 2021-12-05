"""
Adapted to multiplayer environment.

Issue: obviously having the master player linking in-out while another player links in could reset the puzzle.

Otherwise works fine.
#"""

from Plasma import *
from PlasmaTypes import *
import string
import copy
import time
SDLDoor = ptAttribString(1, 'SDL: door')
ActButtons = ptAttribActivatorList(2, 'clk: list of 5 buttons')
RespButtons = ptAttribResponderList(3, 'resp: list of 5 buttons', byObject=1)
RespDoor = ptAttribResponder(4, 'resp: door', ['open', 'close'])
ObjButtons = ptAttribSceneobjectList(5, 'objects: list of 5 buttons')
boolDoor = 0
btnNum = 0
btnList = []
respList = []
objList = []
solutionList = [5, 4, 2, 1, 3]
currentList = [0, 0, 0, 0, 0]

class lakiVilla(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6366
        version = 2
        self.version = version
        print 'lakiVilla v. ',
        print version,
        print '.0'


    def OnServerInitComplete(self):
        global btnList
        global respList
        global objList
        global boolDoor
        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(SDLDoor.value)
        ageSDL.setFlags(SDLDoor.value, 1, 1)
        ageSDL.setNotify(self.key, SDLDoor.value, 0.0)
        for button in ActButtons.value:
            tempName = button.getName()
            btnList.append(tempName)
        print 'btnList = ',
        print btnList
        for resp in RespButtons.value:
            tempResp = resp.getName()
            respList.append(tempResp)
        print 'respList = ',
        print respList
        for obj in ObjButtons.value:
            tempObj = obj.getName()
            objList.append(tempObj)
        print 'objList = ',
        print objList
        boolDoor = ageSDL[SDLDoor.value][0]
        if boolDoor:
            RespDoor.run(self.key, state='open', fastforward=1)
        else:
            RespDoor.run(self.key, state='close', fastforward=1)


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global boolDoor
        ageSDL = PtGetAgeSDL()
        if (VARname == SDLDoor.value):
            boolDoor = ageSDL[VARname][0]
            if boolDoor:
                RespDoor.run(self.key, state='open')
            else:
                RespDoor.run(self.key, state='close')


    def OnNotify(self, state, id, events):
        global objList
        global btnNum
        global respList
        if ((id == ActButtons.id) and state):
            i = 0
            print 'lakiVilla.OnNotify: disabling 5 button clickables'
            for btn in ActButtons.value:
                ActButtons.value[i].disable()
                i += 1
            for event in events:
                if (event[0] == kFacingEvent):
                    xEvent = event[3]
                    btnName = xEvent.getName()
                    i = 0
                    for obj in objList:
                        if (obj == btnName):
                            btnNum = i
                            break
                        else:
                            i += 1
                    print 'btnNum =',
                    print (btnNum + 1)
                    RespButtons.run(self.key, objectName=respList[btnNum])

        if not PtWasLocallyNotified(self.key): return

        if (id == RespButtons.id):
            self.CheckButtons()
        if (id == RespDoor.id):
            i = 0
            print 'lakiVilla.OnNotify: callback from Door resp, reenabling 5 button clickables'
            for btn in ActButtons.value:
                ActButtons.value[i].enable()
                i += 1


    def CheckButtons(self):
        global btnNum
        global currentList
        global boolDoor
        print 'lakiVilla.ICheckButtons(): called.'
        checkNum = (btnNum + 1)
        currentList.append(checkNum)
        while ((len(currentList) > len(solutionList))):
            del currentList[0]
        print ('solution list: ' + str(solutionList))
        print ('current list: ' + str(currentList))
        ageSDL = PtGetAgeSDL()
        if (currentList == solutionList):
            if self.sceneobject.isLocallyOwned():
                ageSDL[SDLDoor.value] = (1,)
        elif boolDoor:
            if self.sceneobject.isLocallyOwned():
                ageSDL[SDLDoor.value] = (0,)
        else:
            i = 0
            for btn in ActButtons.value:
                print 'lakiVilla.ICheckButtons: reenabling 5 button clickables'
                ActButtons.value[i].enable()
                i += 1


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



