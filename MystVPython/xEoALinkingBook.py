from Plasma import *
from PlasmaTypes import *
import string
import os
import xLinkMgr
actClickableBook = ptAttribActivator(1, 'Actvtr: Clickable small book')
respLinkResponder = ptAttribResponder(2, 'Rspndr: Link out')
TargetAge = ptAttribString(3, 'Name of Linking Panel', 'Siralehn')
gLinkingBook = None
ageName = None
spName = None

BookStart1 = '<font size=10>'
LinkStart = '<img src=\"'
TransLinkStart = '<img opacity=0.7 src=\"'
kFirstLinkPanelID = 100
LinkEnd = ('*1#0.hsm\" align=center link=%d blend=alpha>' % kFirstLinkPanelID)
LinkEndPage = '*1#0.hsm\" align=center link=%d blend=alpha>'
LinkEndNoLink = '*1#0.hsm\" align=center blend=alpha>'
PageStart = '<pb>'
MovieLinkStart = '<movie src=\"avi\\'
MovieLinkEnd = ('.bik\" align=center link=%d resize=yes>' % kFirstLinkPanelID)

xAgeLinkingBooks = {
        'DireboThgr':
            (1.0, 1.0,
            (BookStart1 + PageStart + MovieLinkStart + 'direboWithAlpha' + MovieLinkEnd + '<img src=\"xLinkingBookIslandSchematic01*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "direboWithAlpha",
            "Direbo",
            "LinkInPoint4"), # duh, those link in points are all messed up !
        'DireboTdlm':
            (1.0, 1.0,
            (BookStart1 + PageStart + MovieLinkStart + 'direboWithAlpha' + MovieLinkEnd + '<img src=\"xLinkingBookIslandSchematic02*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "direboWithAlpha",
            "Direbo",
            "LinkInPoint3"),
        'DireboSrln':
            (1.0, 1.0,
            (BookStart1 + PageStart + MovieLinkStart + 'direboWithAlpha' + MovieLinkEnd + '<img src=\"xLinkingBookIslandSchematic03*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "direboWithAlpha",
            "Direbo",
            "LinkInPoint1"),
        'DireboLaki':
            (1.0, 1.0,
            (BookStart1 + PageStart + MovieLinkStart + 'direboWithAlpha' + MovieLinkEnd + '<img src=\"xLinkingBookIslandSchematic04*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "direboWithAlpha",
            "Direbo",
            "LinkInPoint2"),
        'DescentRestAreaA':
            (1.0, 1.0,
            (BookStart1 + PageStart + MovieLinkStart + 'dsntRestStop1WithAlpha' + MovieLinkEnd + '<img src=\"xLinkingBookIslandSchematic01*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "dsntRestStop1WithAlpha",
            "DescentMystV",
            "LinkInFromThgrDirebo"),
        'DescentRestAreaB':
            (1.0, 1.0,
            (BookStart1 + PageStart + MovieLinkStart + 'dsntRestStop2WithAlpha' + MovieLinkEnd + '<img src=\"xLinkingBookIslandSchematic02*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "dsntRestStop2WithAlpha",
            "DescentMystV",
            "LinkInFromTdlmDirebo"),
        'DescentRestAreaC':
            (1.0, 1.0,
            (BookStart1 + PageStart + MovieLinkStart + 'dsntRestStop3WithAlpha' + MovieLinkEnd + '<img src=\"xLinkingBookIslandSchematic03*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "dsntRestStop3WithAlpha",
            "DescentMystV",
            "LinkInFromSrlnDirebo"),
        'DescentRestAreaD':
            (1.0, 1.0,
            (BookStart1 + PageStart + MovieLinkStart + 'dsntRestStop4WithAlpha' + MovieLinkEnd + '<img src=\"xLinkingBookIslandSchematic04*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "dsntRestStop4WithAlpha",
            "DescentMystV",
            "LinkInFromLakiDirebo"),
    }

## panels for descent are wrong - we're not linking to the volcano. Good enough either way, if you ask me.
xAgeLinkingBooksStatic = {
        'DireboThgr':
            (1.0, 1.0,
            (BookStart1 + PageStart + LinkStart + 'xlinkpaneldirebo' + LinkEnd + '<img src=\"xLinkingBookIslandSchematic01*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "Direbo",
            "LinkInPoint4"),
        'DireboTdlm':
            (1.0, 1.0,
            (BookStart1 + PageStart + LinkStart + 'xlinkpaneldirebo' + LinkEnd + '<img src=\"xLinkingBookIslandSchematic02*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "Direbo",
            "LinkInPoint3"),
        'DireboSrln':
            (1.0, 1.0,
            (BookStart1 + PageStart + LinkStart + 'xlinkpaneldirebo' + LinkEnd + '<img src=\"xLinkingBookIslandSchematic03*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "Direbo",
            "LinkInPoint1"),
        'DireboLaki':
            (1.0, 1.0,
            (BookStart1 + PageStart + LinkStart + 'xlinkpaneldirebo' + LinkEnd + '<img src=\"xLinkingBookIslandSchematic04*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "Direbo",
            "LinkInPoint2"),
        'DescentRestAreaA':
            (1.0, 1.0,
            (BookStart1 + PageStart + LinkStart + 'xlinkpaneldescentmystv' + LinkEnd + '<img src=\"xLinkingBookIslandSchematic01*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "DescentMystV",
            "LinkInFromThgrDirebo"),
        'DescentRestAreaB':
            (1.0, 1.0,
            (BookStart1 + PageStart + LinkStart + 'xlinkpaneldescentmystv' + LinkEnd + '<img src=\"xLinkingBookIslandSchematic02*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "DescentMystV",
            "LinkInFromTdmlDirebo"),
        'DescentRestAreaC':
            (1.0, 1.0,
            (BookStart1 + PageStart + LinkStart + 'xlinkpaneldescentmystv' + LinkEnd + '<img src=\"xLinkingBookIslandSchematic03*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "DescentMystV",
            "LinkInFromSrlnDirebo"),
        'DescentRestAreaD':
            (1.0, 1.0,
            (BookStart1 + PageStart + LinkStart + 'xlinkpaneldescentmystv' + LinkEnd + '<img src=\"xLinkingBookIslandSchematic04*1#0.hsm\" pos=128,256 blend=alpha resize=\"no\">'),
            "DescentMystV",
            "LinkInFromLakiDirebo"),
    }

class xEoALinkingBook(ptModifier):


    def __init__(self):
        ptModifier.__init__(self)
        self.id = 6402
        version = 1
        self.version = version
        PtDebugPrint(('__init__xEoALinkingBook v%d' % version))


    def IGetClassName(self):
        fullName = str(self)
        className = fullName[(fullName.find('.') + 1):]
        className = className[:className.find(' ')]
        return className


    def OnNotify(self, state, id, events):
        global gLinkingBook
        if (id == actClickableBook.id):
            if not PtWasLocallyNotified(self.key) or PtFindAvatar(events) != PtGetLocalAvatar(): return
            if state:
                actClickableBook.disable()
                self.IShowBook()
                return
        else:
            for event in events:
                if (event[0] == PtEventType.kBook):
                    PtDebugPrint(('xEoALinkingBook: BookNotify  event=%d, id=%d' % (event[1], event[2])), level=kDebugDumpLevel)
                    if (event[1] == PtBookEventTypes.kNotifyImageLink):
                        if (event[2] >= kFirstLinkPanelID):
                            PtDebugPrint(('xEoALinkingBook: BookNotify: hit linking panel %s' % event[2]), level=kDebugDumpLevel)
                            gLinkingBook.hide()
                            #respLinkResponder.run(self.key)
                            xLinkMgr.LinkToAge(ageName, spName)
                    if (event[1] == PtBookEventTypes.kNotifyShow):
                        PtDebugPrint('xEoALinkingBook:Book: NotifyShow', level=kDebugDumpLevel)
                    if (event[1] == PtBookEventTypes.kNotifyHide):
                        PtDebugPrint('xEoALinkingBook:Book: NotifyHide', level=kDebugDumpLevel)
                        actClickableBook.enable()


    def IShowBook(self):
        global gLinkingBook
        global ageName
        global spName
        agePanel = TargetAge.value
        showOpen = 1
        if agePanel:
            if 1:
                params = xAgeLinkingBooks[agePanel]
                if (len(params) == 6):
                    (width, height, bookdef, videoFile, ageName, spName) = params
                    if not os.path.exists('avi\\%s.bik' % videoFile):
                        (width, height, bookdef) = xAgeLinkingBooksStatic[agePanel]
                    gui = 'BkBook'
                elif (len(params) == 3):
                    (width, height, bookdef, ageName, spName) = params
                    gui = 'BkBook'
                print bookdef
                gLinkingBook = ptBook(bookdef, self.key)
                gLinkingBook.setSize(width, height)
                if (not (showOpen)):
                    if (not (self.IsThereACover(bookdef))):
                        showOpen = 1
                gLinkingBook.setGUI(gui)
                gLinkingBook.show(showOpen)
        else:
            PtDebugPrint(('xEoALinkingBook: no age link panel' % agePanel), level=kErrorLevel)


    def IsThereACover(self, bookHtml):
        idx = bookHtml.find('<cover')
        if (idx > 0):
            return 1
        return 0


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



