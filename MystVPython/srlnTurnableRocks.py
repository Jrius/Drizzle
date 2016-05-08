"""
Replaced .getDraggableValue() by anim event modifiers, works great.

Heh, that's how Cyan originally did. I didn't modify activators 3 to 10
#"""


from Plasma import *
from PlasmaTypes import *
PillarSDL = ptAttribString(1, 'Age SDL Var Name')
actNotch00 = ptAttribActivator(2, 'act: Draggable Rock')
actNotch01 = ptAttribActivator(3, 'act: animation reached stage 0')
actNotch02 = ptAttribActivator(4, 'act: animation reached stage 1')
actNotch03 = ptAttribActivator(5, 'act: animation reached stage 2')
actNotch04 = ptAttribActivator(6, 'act: animation reached stage 3')
actNotch05 = ptAttribActivator(7, 'act: animation reached stage 4')
actNotch06 = ptAttribActivator(8, 'act: animation reached stage 5')
actNotch07 = ptAttribActivator(9, 'act: animation reached stage 6')
actNotch08 = ptAttribActivator(10, 'act: animation reached stage 7')
Position00 = ptAttribResponder(11, 'resp: Position 00')
Position01 = ptAttribResponder(12, 'resp: Position 01')
Position02 = ptAttribResponder(13, 'resp: Position 02')
Position03 = ptAttribResponder(14, 'resp: Position 03')
Position04 = ptAttribResponder(15, 'resp: Position 04')
Position05 = ptAttribResponder(16, 'resp: Position 05')
Position06 = ptAttribResponder(17, 'resp: Position 06')
Position07 = ptAttribResponder(18, 'resp: Position 07')
DeadEndNode = ptAttribSceneobject(19, 'Dead End Novice Node')

class srlnTurnableRocks(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6201
        version = 4
        self.version = version
        print '__init__srlnTurnableRocks v. ',
        print version,
        print '.2'


    def OnServerInitComplete(self):
        ageSDL = PtGetAgeSDL()
        ageSDL.sendToClients(PillarSDL.value)
        ageSDL.setFlags(PillarSDL.value, 1, 1)
        ageSDL.setNotify(self.key, PillarSDL.value, 0.0)
        if (len(actNotch00.value) == 0):
            print 'When I got here ',
            print PillarSDL.value,
            print ' value was ',
            print ageSDL[PillarSDL.value][0]
            if (ageSDL[PillarSDL.value][0] == 0):
                print '\tPutting the passage below Turnable Rock ',
                print PillarSDL.value,
                print ' to position # 0'
                Position00.run(self.key, fastforward=1)
            elif (ageSDL[PillarSDL.value][0] == 1):
                print '\tPutting the passage below Turnable Rock ',
                print PillarSDL.value,
                print ' to position # 1'
                Position01.run(self.key, fastforward=1)
            elif (ageSDL[PillarSDL.value][0] == 2):
                print '\tPutting the passage below Turnable Rock ',
                print PillarSDL.value,
                print ' to position # 2'
                Position02.run(self.key, fastforward=1)
            elif (ageSDL[PillarSDL.value][0] == 3):
                print '\tPutting the passage below Turnable Rock ',
                print PillarSDL.value,
                print ' to position # 3'
                Position03.run(self.key, fastforward=1)
            elif (ageSDL[PillarSDL.value][0] == 4):
                print '\tPutting the passage below Turnable Rock ',
                print PillarSDL.value,
                print ' to position # 4'
                Position04.run(self.key, fastforward=1)
            elif (ageSDL[PillarSDL.value][0] == 5):
                print '\tPutting the passage below Turnable Rock ',
                print PillarSDL.value,
                print ' to position # 5'
                Position05.run(self.key, fastforward=1)
            elif (ageSDL[PillarSDL.value][0] == 6):
                print '\tPutting the passage below Turnable Rock ',
                print PillarSDL.value,
                print ' to position # 6'
                Position06.run(self.key, fastforward=1)
            elif (ageSDL[PillarSDL.value][0] == 7):
                print '\tPutting the passage below Turnable Rock ',
                print PillarSDL.value,
                print ' to position # 7'
                Position07.run(self.key, fastforward=1)
            else:
                print 'srlnTurnableRocks: ERROR: Unexpected value.'


    def OnNotify(self, state, id, events):
        if (not (state)):
            return
        if ((not (PtWasLocallyNotified(self.key))) or (PtFindAvatar(events) != PtGetLocalAvatar())):
            return
        if 3 <= id <= 10:
            print ('srlnTurnableRocks: spinning rock at stage %d' % (id - 3))
            ageSDL = PtGetAgeSDL()
            ageSDL[PillarSDL.value] = ((id - 3),)


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        if (len(actNotch00.value) == 0):
            ageSDL = PtGetAgeSDL()
            print 'BELOW: Opening the passage below ',
            print PillarSDL.value,
            print ' to position #',
            print ageSDL[PillarSDL.value][0]
            if (ageSDL[PillarSDL.value][0] == 0):
                Position00.run(self.key)
            elif (ageSDL[PillarSDL.value][0] == 1):
                Position01.run(self.key)
            elif (ageSDL[PillarSDL.value][0] == 2):
                Position02.run(self.key)
            elif (ageSDL[PillarSDL.value][0] == 3):
                Position03.run(self.key)
            elif (ageSDL[PillarSDL.value][0] == 4):
                Position04.run(self.key)
            elif (ageSDL[PillarSDL.value][0] == 5):
                Position05.run(self.key)
            elif (ageSDL[PillarSDL.value][0] == 6):
                Position06.run(self.key)
            elif (ageSDL[PillarSDL.value][0] == 7):
                Position07.run(self.key)


    def ManageDeadEndNode(self):
        return
        if (PillarSDL.value == 'TurnableRock01'):
            solution = 3
        elif (PillarSDL.value == 'TurnableRock02'):
            solution = 2
        elif (PillarSDL.value == 'TurnableRock03'):
            solution = 7
        elif (PillarSDL.value == 'TurnableRock04'):
            solution = 7
        else:
            print 'srlnTurnalbeRocks: I can\'t tell which Turnable Rock I am.'
            return
        ageSDL = PtGetAgeSDL()
        if (ageSDL[PillarSDL.value][0] == solution):
            print 'srlnTurnableRocks: Dead End Novice Node by rock ',
            print PillarSDL.value,
            print ' is ENABLED.'
            DeadEndNode.value.enable()
        else:
            print 'srlnTurnableRocks: Dead End Novice Node by rock ',
            print PillarSDL.value,
            print ' is DISABLED.'
            DeadEndNode.value.disable()


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



