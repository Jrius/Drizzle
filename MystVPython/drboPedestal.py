"""
Removed most SDL-across-Age tech, because that's hacky and wouldn't be really fine in multiplayer instances.

Direbo will only have the first link available.

Other Ages will have the SDL var in their own SDL record. Allows people to /!resetage.

Also, now each pedestal has its own SDL variable. Why ? Because if we use a single var with all enabled ped in it, sendToClients
and setFlags might actually /disable/ SDL change notify. Crazy, heh ?
Doesn't require anything PRP-wise, only changes are in the Python files, and in the SDL record (obviously).
#"""


from Plasma import *
from PlasmaTypes import *
#import xAgeSDL
import xLinkMgr
actDict = {}
spDict = {}
sdlActivePedestals = ptAttribString(1, 'Active pedestals var')
actDict['Take'] = ptAttribActivator(2, 'Take symbol click')
actDict['Keep'] = ptAttribActivator(3, 'Keep symbol click')
actDict['01'] = ptAttribActivator(4, 'Ped 1 symbol click')
actDict['02'] = ptAttribActivator(5, 'Ped 2 symbol click')
actDict['03'] = ptAttribActivator(6, 'Ped 3 symbol click')
respSymbolGlow = ptAttribResponder(7, 'Symbol glow resp', ['Keep', '01', '02', '03'])
respLink = ptAttribResponder(8, 'Link resp', ['Take', 'Keep', '01', '02', '03'])
spDict[4] = ptAttribSceneobject(9, "Spawn point 01 (optional)")
spDict[5] = ptAttribSceneobject(10, "Spawn point 02 (optional)")
spDict[6] = ptAttribSceneobject(11, "Spawn point 03 (optional)")
spDict[3] = ptAttribSceneobject(12, "Spawn point keep (optional)")
actDict[2] = 'Take'
actDict[3] = 'Keep'
actDict[4] = '01'
actDict[5] = '02'
actDict[6] = '03'
spDict[40] = "LinkInPed1"
spDict[50] = "LinkInPed2"
spDict[60] = "LinkInPed3"
spDict[30] = "LinkIn%sKeep"
spDict[20] = "LinkInTake"
spDict["Thgr"] = "Tahgira"
spDict["Tdlm"] = "Todelmer"
spDict["Srln"] = "Siralehn"
spDict["Laki"] = "Laki"
isDirebo = False
ignoreThird = False

class drboPedestal(ptModifier):


    def __init__(self):
        ptModifier.__init__(self)
        self.id = 6115
        self.version = 1
        print 'drboPedestal.__init__: version -',
        print self.version


    def OnServerInitComplete(self):
        global isDirebo
        global ignoreThird
        isDirebo = (PtGetAgeName() == "Direbo")
        ignoreThird = (PtGetAgeName() in ["Todelmer", "Siralehn"])
        
        if isDirebo:
            return
        
        self.activeList = []
        ageSDL = PtGetAgeSDL()
        for suffix in ["01", "02", "03", "Keep"]:
            if ignoreThird and suffix=="03": continue
            
            ageSDL.sendToClients(sdlActivePedestals.value + suffix)
            ageSDL.setFlags(sdlActivePedestals.value + suffix, 1, 1)
            ageSDL.setNotify(self.key, sdlActivePedestals.value + suffix, 0.0)
            
            if ageSDL[sdlActivePedestals.value + suffix][0]:
                respSymbolGlow.run(self.key, state=suffix, fastforward=1)
                actDict[suffix].enable()
                self.activeList.append(suffix)
        
        print "drboPedestal: activeList=", self.activeList
    
    
    def OnTimer(self, id):
        if id == 625:
            PtFadeIn(1.5, 1)
        else:
            actDict[actDict[id]].enable()


    def OnNotify(self, state, id, events):
        if (not PtWasLocallyNotified(self.key)) or (PtFindAvatar(events) != PtGetLocalAvatar()): return
        
        if (actDict.has_key(id) and state):
            actDict[actDict[id]].disable()
            # prevents reloading Age when we can fakelink
            if isDirebo:
                # ok, so that's ugly, but at least it's working fine, and uses xLinkMgr
                if id != 3:
                    xLinkMgr.LinkToAge(spDict[sdlActivePedestals.value[:4]], spDict[id*10])
                else:
                    xLinkMgr.LinkToAge(spDict[sdlActivePedestals.value[:4]], spDict[id*10] % (sdlActivePedestals.value[:4]))
            else:
                PtFadeOut(1, 1)
                PtFakeLinkAvatarToObject(PtGetLocalAvatar().getKey(), spDict[id].value.getKey())
                PtAtTimeCallback(self.key, 1.5, 625)
            PtAtTimeCallback(self.key, 2, id)


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        print "drboPedestal: OnSDLNotify, varname", VARname
        if VARname.startswith(sdlActivePedestals.value):
            print "New symbol available"
            ageSDL = PtGetAgeSDL()
            ped = VARname[ len(sdlActivePedestals.value) : ]
            if ped != "" and not (ped in self.activeList):
                self.activeList.append(ped)
                respSymbolGlow.run(self.key, state=ped)
                actDict[ped].enable()
                print "Enabling link to", ped


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



