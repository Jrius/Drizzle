"""
Moved the stuff to show book from xYeeshaBinder to this file - this means we can no longer collect the books (which is better IMHO),
but also has the side effect of not being able to hear Yeesha's voiceover.
#"""

from Plasma import *
from PlasmaTypes import *
from PlasmaConstants import *

if PtGetLanguage() == PtLanguage.kFrench:
    import xYeeshaJournalsContentFre as localizedContent
elif PtGetLanguage() == PtLanguage.kGerman:
    import xYeeshaJournalsContentGer as localizedContent
else:
    import xYeeshaJournalsContentEng as localizedContent

fragmentName = ptAttribString(1, 'Fragment name')
binderPosition = ptAttribInt(2, 'Position in binder (1-based)', -1)
actClickable = ptAttribActivator(3, 'Actvtr: Clickable')
gJournalBook = None
gCommonBookCode = '<font face=\"Yeesha2\" size=20 spacing=-10><margin top=32 left=32 bottom=32 right=0>'
gFragmentHeader = [
                    (u'<cover src=\"xYeeshaBookletCover00*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover01*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover02*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover03*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover04*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover05*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover06*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover07*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover08*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover09*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover10*1#0.hsm\">' + gCommonBookCode),
                    (u'<cover src=\"xYeeshaBookletCover11*1#0.hsm\">' + gCommonBookCode)]

class xYeeshaBinderFragment(ptModifier):


    def __init__(self):
        ptModifier.__init__(self)
        self.id = 6405
        self.version = 2
        print ('xYeeshaBinderFragment.__init__: version - %d' % self.version)


    def OnServerInitComplete(self):
        PtLoadDialog('bkYeeshaBinder')


    def __del__(self):
        PtUnloadDialog('bkYeeshaBinder')


    def OnNotify(self, state, id, events):
        if (not PtWasLocallyNotified(self.key)) or (PtFindAvatar(events) != PtGetLocalAvatar()): return
        if (id == actClickable.id and state):
            if state:
                print ('xYeeshaBinderFragment.OnNotify(): reading fragment ' + fragmentName.value)
                self.IOpenYeeshaBook()


    def IGetContents(self, fragmentNumber):
        if fragmentNumber == -1:
            return (gFragmentHeader[0] + localizedContent.xYeeshaJournals[0])
        return (gFragmentHeader[fragmentNumber] + localizedContent.xYeeshaJournals[fragmentNumber])


    def IOpenYeeshaBook(self):
        global gJournalBook
        gJournalBook = ptBook(self.IGetContents(binderPosition.value), self.key)
        gJournalBook.setSize(1.0, 1.0)
        #gJournalBook.setGUI('bkYeeshaBinder')
        gJournalBook.setGUI('bkBook')
        gJournalBook.show(0)


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



