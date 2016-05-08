"""
Runs a camera responder on link-in, to make it look like the old flybies from previous Myst games.
"""


from Plasma import *
from PlasmaTypes import *
from PlasmaKITypes import *
lastAge = ptAttribString(1, "Last Age we're linking from")
spawnPointName = ptAttribString(2, 'Link In point (optional)')
#respLinkPanel = ptAttribResponder(3, 'Responder to run (animation)', ['set', 'run', 'reset'])
respLinkPanel = ptAttribResponder(3, 'Responder to run (animation)')
camObj = ptAttribSceneobject(4, "Object: camera to switch to")

class xLinkInFlyover(ptResponder):


    def __init__(self):
        print 'xLinkInFlyover.__init__'
        ptResponder.__init__(self)
        self.id = 5355
        self.version = 1


    def OnFirstUpdate(self):
        try:
            spointName = spawnPointName.value
        except:
            spointName = None
        if (PtGetPrevAgeName() == lastAge.value):
            linkmgr = ptNetLinkingMgr()
            link = linkmgr.getCurrAgeLink()
            spawnPoint = link.getSpawnPoint()
            spName = spawnPoint.getName()
            if ((not (spointName)) or (spName == spointName)):
                print 'xLinkInFlyover: We\'re linking from the good Age, preparing to run cam.'
                cam = ptCamera()
                cam.undoFirstPerson()
                cam.disableFirstPersonOverride()
                cam.save(camObj.sceneobject.getKey())
                respLinkPanel.run(self.key)
                PtFadeLocalAvatar(1)
                PtSendKIMessage(kDisableKIandBB, 0)
                PtDisableMovementKeys()
                avatar = PtGetLocalAvatar()
                avatar.physics.suppress(1)
                avatar.avatar.registerForBehaviorNotify(self.key)
                PtAtTimeCallback(self.key, 25, 2)


    def OnServerInitComplete(self):
        pass


    def OnBehaviorNotify(self, type, id, state):
        print 'xLinkInFlyover: OnBehaviorNotify'
        if ((type == PtBehaviorTypes.kBehaviorTypeLinkIn) and state):
            #respLinkPanel.run(self.key)
            avatar = PtGetLocalAvatar()
            avatar.avatar.unRegisterForBehaviorNotify(self.key)
            PtEnableControlKeyEvents(self.key)


    def OnNotify(self, state, id, events):
        if (id == respLinkPanel.id):
            print '--Cam responder done. Fading Out--'
            PtDisableControlKeyEvents(self.key)
            PtFadeOut(1.5, 1)
            PtAtTimeCallback(self.key, 2.5, 1)


    def OnControlKeyEvent(self, controlKey, activeFlag):
        if activeFlag:
            print 'Manually abording linking panel cam'
            print '--Fading Out--'
            PtDisableControlKeyEvents(self.key)
            PtClearTimerCallbacks(self.key)
            PtFadeOut(1.5, 1)
            PtAtTimeCallback(self.key, 2.5, 1)


    def OnTimer(self, id):
        if id == 1:
            self.FadeIn()
        elif id == 2:
            print '--Cam responder done. Fading Out--'
            PtDisableControlKeyEvents(self.key)
            PtFadeOut(1.5, 1)
            PtAtTimeCallback(self.key, 2.5, 1)


    def FadeIn(self):
        print '--Fading In--'
        #respLinkPanel.run(self.key, 'reset')
        cam = ptCamera()
        cam.restore(camObj.sceneobject.getKey())
        cam.enableFirstPersonOverride()
        PtFadeLocalAvatar(0)
        PtGetLocalAvatar().physics.suppress(0)
        PtSendKIMessage(kEnableKIandBB, 0)
        PtEnableMovementKeys()
        PtFadeIn(1.5, 1)


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



