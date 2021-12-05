"""
Note: many clients will make the same SDL change at once because we can't yet find who triggered the season change first.
#"""

from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *
import string
SlateSDL = ptAttribString(1, 'SDL: Slate updated')
SeasonSDL = ptAttribString(2, 'SDL: Current Season')
SkyAnim = ptAttribAnimation(3, 'anim: Sky Transforms')
AlreadyChanging = false
kTransitionDuration = 90

class tdlmSeasons(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6219
        version = 2
        self.version = version
        print '__init__ tdlmSeasons v.',
        print version,
        print '.6'


    def OnServerInitComplete(self):
        global AlreadyChanging
        ageSDL = PtGetAgeSDL()

        ageSDL.sendToClients(SlateSDL.value)
        ageSDL.setFlags(SlateSDL.value, 1, 1)
        ageSDL.setNotify(self.key, SlateSDL.value, 0.0)

        ageSDL.sendToClients(SeasonSDL.value)
        ageSDL.setFlags(SeasonSDL.value, 1, 1)
        ageSDL.setNotify(self.key, SeasonSDL.value, 0.0)

        PtClearTimerCallbacks(self.key)
        print 'tdlmSeasons.OnInit: SeasonSDL = ',
        print ageSDL[SeasonSDL.value][0]
        if (ageSDL[SlateSDL.value][0] == 0):
            print '\tTodelmer Seasons are currently not changing.'
            if (ageSDL[SeasonSDL.value][0] == 0):
                SkyAnim.animation.skipToTime(0)
            elif (ageSDL[SeasonSDL.value][0] == 1):
                SkyAnim.animation.skipToTime(90)
            elif (ageSDL[SeasonSDL.value][0] == 2):
                SkyAnim.animation.skipToTime(180)
            elif (ageSDL[SeasonSDL.value][0] == 3):
                SkyAnim.animation.skipToTime(270)
            else:
                print '\tError: That\'s not a season number I expected. '
        else:
            print '\tA season change was in progress when I arrived here.'
            AlreadyChanging = true
            timeElapsed = PtGetDniTime()
            SlateSDLvalue = ageSDL[SlateSDL.value][0]
            print 'SlateSDL (endtime) = ',
            print SlateSDLvalue
            print 'timeElapsed = ',
            print timeElapsed
            timeleft = SlateSDLvalue - timeElapsed
            if timeleft <= 0: return
            endtime = (ageSDL[SeasonSDL.value][0] * 90)
            print 'timeleft = ',
            print timeleft,
            print ' endtime = ',
            print endtime
            print '\t First, skipping Sky Animation to time ',
            print (endtime - timeleft)
            SkyAnim.animation.skipToTime((endtime - timeleft))
            print '\t Next, playing Sky Animation to time ',
            print endtime
            SkyAnim.animation.playToTime(endtime)
            print '\t The Sky Animation will be done in ',
            print timeleft,
            print ' seconds.'
            PtAtTimeCallback(self.key, timeleft, 2)


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global AlreadyChanging
        ageSDL = PtGetAgeSDL()
        if (VARname == SlateSDL.value):
            if (ageSDL[VARname][0] == 0):
                print 'tdlmSeasons.OnSDLNotify: The slate SDL was just reset to 0. (Season change should stop now.)'
            else:
                print 'tdlmSeasons.OnSDLNotify: The Todelmer season symbol was just written on the slate. SDL value =',
                print ageSDL[VARname][0]
                if AlreadyChanging:
                    print '\tBut the seasons are already changing, so I\'m ignoring that request.'
                else:
                    AlreadyChanging = true
                    PtAtTimeCallback(self.key, kTransitionDuration, 1)
                    oldseason = ageSDL[SeasonSDL.value][0]
                    newseason = (oldseason + 1)
                    if (newseason == 4):
                        newseason = 0
                    ageSDL[SeasonSDL.value] = (newseason,)
        elif (VARname == SeasonSDL.value):
            print 'tdlmSeasons.OnSDLNotify: The new season in Todelmer is ',
            print ageSDL[VARname][0]
            if (ageSDL[VARname][0] == 0):
                self.Season3to0()
            elif (ageSDL[VARname][0] == 1):
                self.Season0to1()
            elif (ageSDL[VARname][0] == 2):
                self.Season1to2()
            elif (ageSDL[VARname][0] == 3):
                self.Season2to3()
        else:
            print 'tdlmSeason: Error. I\'m not sure what SDL notified me.'
            print '\tchangedVar = ',
            print VARname


    def Season0to1(self):
        SkyAnim.animation.skipToTime(0)
        SkyAnim.animation.playToTime(90)
        print '\tDoing spring (0) things.'


    def Season1to2(self):
        SkyAnim.animation.skipToTime(90)
        SkyAnim.animation.playToTime(180)
        print '\tDoing summer (1) things.'


    def Season2to3(self):
        SkyAnim.animation.skipToTime(180)
        SkyAnim.animation.playToTime(270)
        print '\tDoing fall (2) things.'


    def Season3to0(self):
        SkyAnim.animation.skipToTime(270)
        SkyAnim.animation.playToTime(360)
        print '\tDoing winter (3) things.'


    def OnTimer(self, id):
        global AlreadyChanging
        if (id == 1):
            print 'tdlmSeasons.OnTimer: Season transition completed.'
            AlreadyChanging = false
        elif (id == 2):
            print 'tdlmSeasons.OnTimer: NOW the season is over.'
            AlreadyChanging = false
            SkyAnim.animation.stop()


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



