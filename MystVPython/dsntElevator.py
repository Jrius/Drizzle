from Plasma import *
from PlasmaTypes import *
import string
ElevatorSDL = ptAttribString(1, 'AgeSDL Elevator')
actTop = ptAttribActivator(2, 'AnimEvnt: Top Lever pulled')
animTop = ptAttribAnimation(3, 'anim: Top Lever')
actBottom = ptAttribActivator(4, 'AnimEvnt: Bottom Lever pulled')
animBottom = ptAttribAnimation(5, 'anim: Bottom Lever')
actInside = ptAttribActivator(6, 'AnimEvnt: Inside Lever pulled')
animInside = ptAttribAnimation(7, 'anim: Inside Lever')
respRaiseElev = ptAttribResponder(8, 'resp: Raise Elevator')
respLowerElev = ptAttribResponder(9, 'resp: Lower Elevator')
actReachedTop = ptAttribActivator(10, 'act: Has Reached Top')
actReachedBottom = ptAttribActivator(11, 'act: Has Reached Bottom')
respLeverPull = ptAttribResponder(12, 'deprecated')
respLeverReset = ptAttribResponder(13, 'deprecated')
actDraggableTop = ptAttribActivator(14, 'act: Top Lever Draggable')
actDraggableInside = ptAttribActivator(15, 'act: Inside Lever Draggable')
actDraggableBottom = ptAttribActivator(16, 'act: Bottom Lever Draggable')
ElevCam = ptAttribSceneobject(17, 'deprecated')
xRgnClearBoth = ptAttribResponder(18, 'resp: Clear Both xRgns')
xRgnReleaseTop = ptAttribResponder(19, 'resp: Release Top xRgn')
xRgnReleaseBottom = ptAttribResponder(20, 'resp: Release Bottom xRgn')
animSafetyTop = ptAttribAnimation(21, 'anim: Top Station Safety Rail')
animSafetyBottom = ptAttribAnimation(22, 'anim: Btm Station Safety Rail')
#respCamGoingUp = ptAttribResponder(23, 'resp: Cam Going up', ['run', 'reset']) # might be possible to simply push and pop the camstack. Will have to check
#respCamGoingDown = ptAttribResponder(24, 'resp: Cam Going down', ['run', 'reset'])
respCamGoingUp = ptAttribResponder(23, 'resp: Cam Going up')
respCamGoingDown = ptAttribResponder(24, 'resp: Cam Going down')
topWarp = ptAttribSceneobject(25, 'obj: top warp')
bottomWarp = ptAttribSceneobject(26, 'obj: bottom warp')
actInsideElev = ptAttribActivator(27, "act: inside elevator")
elevatormoving = false
riding = false
todown = false
soCamera1 = None
soCamera2 = None
isTopElev = False
soElevParent = None

class dsntElevator(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6208
        version = 10
        self.version = version
        print '__init__dsntElevator v.',
        print version,
        print '.4'


    def OnServerInitComplete(self):
        global soCamera1
        global soCamera2
        global isTopElev
        global soElevParent
        ageSDL = PtGetAgeSDL()
        ageSDL.setFlags(ElevatorSDL.value, 1, 1)
        ageSDL.sendToClients(ElevatorSDL.value)
        ageSDL.setNotify(self.key, ElevatorSDL.value, 0.0)
        
        ## lower speed to counterpart speeded animation
        animTop.animation.speed(0.3)
        animInside.animation.speed(0.3)
        animBottom.animation.speed(0.3)
        
        print 'dsntElevator: When I got here',
        print ElevatorSDL.value,
        print ' value was ',
        print ageSDL[ElevatorSDL.value][0]
        if (ageSDL[ElevatorSDL.value][0] == 1):
            print '\tRaising Elevator and releasing top xRgn, and raising the Bottom Station Safety Rail'
            respRaiseElev.run(self.key, fastforward=1)
            xRgnReleaseTop.run(self.key)
            animSafetyBottom.animation.playToTime(1)
        else:
            print '\tRaising the Top Station Safety rail.'
            xRgnReleaseBottom.run(self.key)
            animSafetyTop.animation.playToTime(1)
        
        if ElevatorSDL.value == "ElevatorARaised":
            isTopElev = True
            soCamera1 = PtFindSceneobject("ElevCamATop", "DescentMystV")
            soCamera2 = PtFindSceneobject("ElevCamABtm", "DescentMystV")
            soElevParent = PtFindSceneobject("StupidityCorrector01", "DescentMystV")
        else:
            soCamera1 = PtFindSceneobject("ElevCamBTop", "DescentMystV")
            soCamera2 = PtFindSceneobject("ElevCamBBtm", "DescentMystV")
            soElevParent = PtFindSceneobject("StupidityCorrector02", "DescentMystV")


    def OnNotify(self, state, id, events):
        global elevatormoving
        global riding
        global soElevParent
        ageSDL = PtGetAgeSDL()
        if (not (state)):
            return
        if ((id == actTop.id) or ((id == actBottom.id) or (id == actInside.id))):
            if elevatormoving:
                print 'dsntElevator: I\'m not going to respond because the elevator is moving. id =',
                print id
                return
            else:
                #if (id == actInside.id):
                #    riding = true
                #else:
                #    riding = false
                PtAtTimeCallback(self.key, 1, 3)
                player = PtFindAvatar(events)
                if (player == PtGetLocalAvatar() and PtWasLocallyNotified(self.key)):
                    i = ageSDL[ElevatorSDL.value][0]
                    i = abs((i - 1))
                    ageSDL[ElevatorSDL.value] = (i,)
                    print 'now, i = ',
                    print i
        if (id == actTop.id):
            print 'Elevator ',
            print ElevatorSDL.value,
            print ' Top lever was pulled.'
        elif (id == actBottom.id):
            print 'Elevator ',
            print ElevatorSDL.value,
            print ' Bottom lever was pulled.'
        elif (id == actInside.id):
            print 'Elevator ',
            print ElevatorSDL.value,
            print ' Inside lever was pulled.'
            #print 'Disabling menu while player is supposedly on the elevator.'
            #PtSendPythonNotify('Hide', 'Menu Bar', self.key)
            #PtSendPythonNotify('Hide', 'Bahro Slate', self.key)
            #if (not (PtIsExpertMode())):
            #    print 'In novice mode, so we\'ll disable player input for duration of elevator ride'
            #    PtEnableInput(0)
            #else:
            #    print 'In expert mode, so we\'ll disable movement so the mouse disappears.'
            #    PtEnableMovement(0)
        elif ((id == actReachedTop.id) or (id == actReachedBottom.id)):
            PtAtTimeCallback(self.key, 2.5, 2)
            if (id == actReachedBottom.id):
                print 'Elevator ',
                print ElevatorSDL.value,
                print ' has reached the bottom. Releasing Bottom xRgn.'
                xRgnReleaseBottom.run(self.key)
                print '\tLowering Bottom Station Safety Rail.'
                animSafetyBottom.animation.playToTime(0)
                #if riding:
                #    player = PtGetLocalAvatar()
                #    player.physics.warpObj(bottomWarp.value.getKey())
                #    print '\tWarping Player to the bottom'
            else:
                print 'Elevator ',
                print ElevatorSDL.value,
                print ' has reached the top. Releasing Top xRgn.'
                xRgnReleaseTop.run(self.key)
                print '\tLowering Top Station Safety Rail.'
                animSafetyTop.animation.playToTime(0)
                #if riding:
                #    player = PtGetLocalAvatar()
                #    player.physics.warpObj(topWarp.value.getKey())
                #    print '\tWarping Player to the top'
            #if (not (PtIsExpertMode())):
            #    print 'In novice mode, so we\'ll send player to closest node and re-enable input'
            #    PtClosestNode()
            #    PtEnableInput(1)
            #else:
            #    print 'In expert mode, so we\'ll enable movement so the mouse reappears.'
            #    PtEnableMovement(1)
            animTop.animation.playToTime(0)
            animInside.animation.playToTime(0)
            animBottom.animation.playToTime(0)
            PtAtTimeCallback(self.key, 3, 1)
        if id == actInsideElev.id and state and PtWasLocallyNotified(self.key):
            for event in events:
                if (event[0] == kCollisionEvent):
                    if (event[1] == 1):
                        av = PtFindAvatar(events)
                        PtAttachObject(av, soElevParent)
                        print "Attaching avatar to elevator..."
                        if (av == PtGetLocalAvatar()):
                            print "...which is our avatar"
                            riding = true
                    else:
                        av = PtFindAvatar(events)
                        PtDetachObject(av, soElevParent)
                        print "Detaching avatar to elevator..."
                        if (av == PtGetLocalAvatar()):
                            print "...which is our avatar"
                            riding = false


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global todown
        global riding
        global elevatormoving
        global soCamera1
        global soCamera2
        global isTopElev
        ageSDL = PtGetAgeSDL()
        if ageSDL[ElevatorSDL.value][0]:
            print 'Elevator ',
            print ElevatorSDL.value,
            print ' is now going UP.'
            respRaiseElev.run(self.key)
            todown = false
            if riding:
                print '\tPlayer is on the elevator so I\'m running the Elevator going UP responder.'
                cam = ptCamera() # comment out if you want to keep first person
                cam.undoFirstPerson()
                cam.disableFirstPersonOverride()
                respCamGoingUp.run(self.key)
                # workaround for camera reset
                #cam.save(soCamera2.getKey())
                soCamera2.pushCameraCut(PtGetLocalAvatar().getKey())
                if isTopElev:
                    PtAtTimeCallback(self.key, 15, 999) # need to switch cam after exactly 15 sec
        else:
            print 'Elevator ',
            print ElevatorSDL.value,
            print ' is now going DOWN.'
            respLowerElev.run(self.key)
            todown = true
            if riding:
                print '\tPlayer is on the elevator so I\'m running the Elevator going DOWN responder.'
                cam = ptCamera() # comment out if you want to keep first person
                cam.undoFirstPerson()
                cam.disableFirstPersonOverride()
                respCamGoingDown.run(self.key)
                # workaround for camera reset
                #cam.save(soCamera1.getKey())
                soCamera1.pushCameraCut(PtGetLocalAvatar().getKey())
                if isTopElev:
                    PtAtTimeCallback(self.key, 15, 1999) # need to switch cam after exactly 15 sec
        elevatormoving = true
        print '\tDisabling the three draggables.'
        actDraggableTop.disable()
        actDraggableInside.disable()
        actDraggableBottom.disable()


    def OnTimer(self, id):
        global elevatormoving
        global riding
        global todown
        print 'dsntElevator.OnTimer: id =',
        print id
        if (id == 1):
            print '\tOnTimer: Levers have been reset. Re-enabling the three draggables.'
            actDraggableTop.enable()
            actDraggableInside.enable()
            actDraggableBottom.enable()
        elif (id == 2):
            print '\tOnTimer: elevator moving is now false'
            elevatormoving = false
            if riding:
                #riding = false
                print '\tPlayer has reached his destination. Popping camera back to avatar POV.'
                cam = ptCamera()
                cam.enableFirstPersonOverride()
                
                # workaround for camera reset
                if todown:
                    if isTopElev:
                        #cam.restore(soCamera2.getKey())
                        soCamera2.popCamera(PtGetLocalAvatar().getKey())
                    else:
                        #cam.restore(soCamera1.getKey())
                        soCamera1.popCamera(PtGetLocalAvatar().getKey())
                else:
                    if isTopElev:
                        #cam.restore(soCamera1.getKey())
                        soCamera1.popCamera(PtGetLocalAvatar().getKey())
                    else:
                        #cam.restore(soCamera2.getKey())
                        soCamera2.popCamera(PtGetLocalAvatar().getKey())
                
                #if todown:
                #    respCamGoingDown.run(self.key, 'reset')
                #else:
                #    respCamGoingUp.run(self.key, 'reset')
                
                #PtForceFirstPersonCam()
                #print '\tRe-enabling menus now that the rider gotten off the elevator.'
                #PtSendPythonNotify('Show', 'Menu Bar', self.key)
                #PtSendPythonNotify('Show', 'Bahro Slate', self.key)
                
                if riding:
                    PtEnableMovementKeys()
        elif (id == 3):
            print 'dsntElevator.OnTimer: Clearing both the top and bottom xRgns now that the Elevator is safely moving.'
            if todown:
                animSafetyTop.animation.playToTime(1)
            else:
                animSafetyBottom.animation.playToTime(1)
            
            if riding:
                PtDisableMovementKeys()
            
            xRgnClearBoth.run(self.key)
        elif id == 999:
            cam = ptCamera()
            #cam.restore(soCamera2.getKey())
            soCamera2.popCamera(PtGetLocalAvatar().getKey())
            #cam.save(soCamera1.getKey())
            soCamera1.pushCameraCut(PtGetLocalAvatar().getKey())
        elif id == 1999:
            cam = ptCamera()
            #cam.restore(soCamera1.getKey())
            soCamera1.popCamera(PtGetLocalAvatar().getKey())
            #cam.save(soCamera2.getKey())
            soCamera2.pushCameraCut(PtGetLocalAvatar().getKey())


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



