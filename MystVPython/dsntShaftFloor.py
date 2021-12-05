from Plasma import *
from PlasmaTypes import *
import string
FloorRaisedSDL = ptAttribString(1, 'Age SDL Floor Raised')
BlueTimer = ptAttribActivator(2, 'act: Blue Timer')
YellowTimer = ptAttribActivator(3, 'act: Yellow Timer')
FloorCenter = ptAttribActivator(4, 'deprecated')
respRaiseFloor = ptAttribResponder(5, 'resp: Raise Floor')
respLowerFloor = ptAttribResponder(6, 'resp: Lower Floor')
TooHighToJump = ptAttribActivator(7, 'act: Floor Jumpable Height')
respEnableBlocker = ptAttribResponder(8, 'resp: Enable JumpBlocker')
respDisableBlocker = ptAttribResponder(9, 'resp: Disable JumpBlocker')
respEnableLadders = ptAttribResponder(10, 'resp: Enable Ladders')
respDisableLadders = ptAttribResponder(11, 'resp: Disable Ladders')
actSideDoorsSeal = ptAttribActivator(12, 'act: Side Doors Sealed')
respSideDoorsSeal = ptAttribResponder(13, 'resp: SideDoorsSeal')
respSideDoorsUnseal = ptAttribResponder(14, 'resp: SideDoorsUnseal')
actCrank = ptAttribActivator(15, 'act: the Man Crank')
respRedGlowStart = ptAttribResponder(16, 'resp: Red Glow Start')
respRedGlowStop = ptAttribResponder(17, 'resp: Red Glow Stop')
respBlueGlowStart = ptAttribResponder(18, 'resp: Blue Glow Start')
respBlueGlowStop = ptAttribResponder(19, 'resp: Blue Glow Stop')
respYellowGlowStart = ptAttribResponder(20, 'resp: Yellow Glow Start')
respYellowGlowStop = ptAttribResponder(21, 'resp: Yellow Glow Stop')
actOnFloor = ptAttribActivator(22, 'rgn sensor: on floor')
NodeRgnAbove = ptAttribSceneobject(23, 'Node Rgn Above')
NodeRgnBelow = ptAttribSceneobject(24, 'Node Rgn Below')
respCameraUp = ptAttribResponder(25, 'resp: FloorGoingUp Cam')
respCameraDown = ptAttribResponder(26, 'resp: FloorGoingDown Cam')
respPitBlock = ptAttribResponder(27, 'resp: PitBlocker Enable') # no longer required: current floor physics take care of it.
respPitUnblock = ptAttribResponder(28, 'resp: PitBlocker Disable')
RedTop = ptAttribActivator(29, 'act: Red Btn Top')
RedBottom = ptAttribActivator(30, 'act: Red Btn Bottom')
respButtonsSink = ptAttribResponder(31, 'resp: Buttons Sink')
respButtonsRise = ptAttribResponder(32, 'resp: Buttons Rise')
NodeUnderLoweredFloor = ptAttribSceneobject(33, 'Node Under Lowered Floor')
NodeUnderRaisedFloor = ptAttribSceneobject(34, 'Node Under Raised Floor')
objLadder01 = ptAttribSceneobject(35, 'so: Ladder #1')
objLadder02 = ptAttribSceneobject(36, 'so: Ladder #2')
objLadder03 = ptAttribSceneobject(37, 'so: Ladder #3')
respCatwalkBlock = ptAttribResponder(38, 'resp: PitBlocker Block')
respCatwalkUnblock = ptAttribResponder(39, 'resp: PitBlocker Unblock')
actDisableFloorCollider = ptAttribActivator(40, 'act: disable floor collider once under floor')
floormoving = false
floorraised = false
bluepushed = false
yellowpushed = false
justcamedown = false
currentcolor = 0
timerduration = 68
kBlueTimerID = 1
kYellowTimerID = 2
kRedTimerID = 3
kDelayRaiseID = 4
kDelayLowerID = 5
kNNodesFloorUpID = 6
kNNodesFloorDownID = 7
BounceTimer = 30
DelayTimer = 5
ReplacedByRed = false
OnFloor = 0
climbLadders = ()
floorParent = None
floorCollision = None

class dsntShaftFloor(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6205
        version = 11
        self.version = version
        print '__init__dsntShaftFloor v.',
        print version,
        print '.0'


    def OnServerInitComplete(self):
        global currentcolor
        global climbLadders
        global floorParent
        global floorCollision
        global floorraised
        ageSDL = PtGetAgeSDL()
        ageSDL.setFlags(FloorRaisedSDL.value, 1, 1)
        ageSDL.sendToClients(FloorRaisedSDL.value)
        ageSDL.setNotify(self.key, FloorRaisedSDL.value, 0.0)
        print 'dsntShaftFloor: When I got here ',
        print FloorRaisedSDL.value,
        print ' value was ',
        print ageSDL[FloorRaisedSDL.value][0]

        try:
            climbLadders = (PtFindSceneobject('Region.003_bottom', 'DescentMystV'), PtFindSceneobject('Region.003_top', 'DescentMystV'), PtFindSceneobject('Region.004_bottom', 'DescentMystV'), PtFindSceneobject('Region.004_top', 'DescentMystV'), PtFindSceneobject('Region.005_bottom', 'DescentMystV'), PtFindSceneobject('Region.005_top', 'DescentMystV'))
            floorCollision = PtFindSceneobject('NewFloorProxy', 'DescentMystV')
        except: pass
        floorParent = PtFindSceneobject('NewFloorParent', 'DescentMystV')

        PtClearTimerCallbacks(self.key)
        if (ageSDL[FloorRaisedSDL.value][0] == 1):
            print '\tRaising the Great Shaft Floor'
            respRaiseFloor.run(self.key, fastforward=1)
            respRedGlowStart.run(self.key)
            currentcolor = 3
            respSideDoorsSeal.run(self.key)
            floorraised = true
            PtAtTimeCallback(self.key, 2, kNNodesFloorUpID)
            #respPitBlock.run(self.key)
            respPitUnblock.run(self.key)
            respEnableLadders.run(self.key)
            respDisableBlocker.run(self.key)
            respCatwalkUnblock.run(self.key)
            for ladder in climbLadders:
                ladder.physics.suppress(0)
        else:
            PtAtTimeCallback(self.key, 2, kNNodesFloorDownID)
            respDisableBlocker.run(self.key)
            respPitUnblock.run(self.key)
            respDisableLadders.run(self.key)
            respCatwalkBlock.run(self.key)
            print '\tDisabling the Red Bottom button.'
            RedBottom.disable()
            for ladder in climbLadders:
                ladder.physics.suppress(1)


    def OnNotify(self, state, id, events):
        global OnFloor
        global bluepushed
        global yellowpushed
        global justcamedown
        global currentcolor
        global floorraised
        global floormoving
        global floorParent
        global floorCollision
        global ReplacedByRed
        if (not (state)):
            return
        print 'ShaftFloor.OnNotify id = ',
        print id,
        print ' OnFloor = ',
        print OnFloor
        if (id == BlueTimer.id):
            print 'OnNotify: The Blue timer was just started.'
            bluepushed = true
            PtAtTimeCallback(self.key, timerduration, kBlueTimerID)
            self.UpdateGlowColor()
        elif (id == YellowTimer.id):
            print 'OnNotify: The Yellow timer was just started.'
            yellowpushed = true
            PtAtTimeCallback(self.key, timerduration, kYellowTimerID)
            self.UpdateGlowColor()
        elif (id == RedBottom.id):
            print 'dsntShaftFloor: You just pressed the Red BOTTOM button'
            print '\tblue=',
            print bluepushed,
            print ' yellow=',
            print yellowpushed,
            print ' justcamedown=',
            print justcamedown,
            print ' currentcolor=',
            print currentcolor,
            print ' f.raised=',
            print floorraised,
            print ' f.moving=',
            print floormoving
            if (bluepushed and yellowpushed):
                self.ToggleFloorState(((PtFindAvatar(events) == PtGetLocalAvatar()) and PtWasLocallyNotified(self.key)))
                #print '\tHiding the Menu Bar and Disabling Slate immediately.'
                #PtSendPythonNotify('Hide', 'Menu Bar', self.key)
                #PtSendPythonNotify('Disable', 'Bahro Slate', self.key)
            elif justcamedown:
                self.ToggleFloorState(((PtFindAvatar(events) == PtGetLocalAvatar()) and PtWasLocallyNotified(self.key)))
                #print '\tHiding the Menu Bar and Disabling Slate immediately.'
                #PtSendPythonNotify('Hide', 'Menu Bar', self.key)
                #PtSendPythonNotify('Disable', 'Bahro Slate', self.key)
            else:
                print 'ERROR: I have NO idea how you could push the Red Bottom button, given the current state of the floor variables.'
        elif (id == RedTop.id):
            print 'dsntShaftFloor: You just pressed the Red TOP button'
            print '\tblue=',
            print bluepushed,
            print ' yellow=',
            print yellowpushed,
            print ' justcamedown=',
            print justcamedown,
            print ' currentcolor=',
            print currentcolor,
            print ' f.raised=',
            print floorraised,
            print ' f.moving=',
            print floormoving
            if (floorraised and (not (floormoving))):
                self.ToggleFloorState(((PtFindAvatar(events) == PtGetLocalAvatar()) and PtWasLocallyNotified(self.key)))
                respButtonsSink.run(self.key)
                #print '\tHiding the Menu Bar and Disabling Slate immediately.'
                #PtSendPythonNotify('Hide', 'Menu Bar', self.key)
                #PtSendPythonNotify('Disable', 'Bahro Slate', self.key)
        elif (id == respRaiseFloor.id):
            print 'The floor has finished raising. The man crank under the floor is re-enabled'
            floormoving = false
            floorraised = true
            respButtonsRise.run(self.key)
            actCrank.enable(self.key)
            print '\tDisabling the jump blocker.'
            respDisableBlocker.run(self.key)
            #print '\tDisabling the lower node region. Enabling the upper node region. 1'
            #NodeRgnAbove.value.enable()
            #NodeUnderRaisedFloor.value.disable()
            #NodeRgnBelow.value.disable()
            #NodeUnderLoweredFloor.value.enable()
            print '\tDisabling the catwalk blocker.'
            respCatwalkUnblock.run(self.key)
            #print '\tRe-enabling the Menu Bar and Slate.'
            #PtSendPythonNotify('Show', 'Menu Bar', self.key)
            #PtSendPythonNotify('Enable', 'Bahro Slate', self.key)
            #if (not (PtIsExpertMode())):
            #    print 'In novice mode, so we\'ll go to a FP cam, re-enable input and send player to closest node'
            #    PtClosestNode()
            #    PtEnableMovement(1)
        elif (id == respLowerFloor.id):
            print 'The floor has finished lowering.  The man crank under the floor is re-enabled. Bounce Timer starting.'
            floormoving = false
            floorraised = false
            justcamedown = true
            respButtonsRise.run(self.key)
            actCrank.enable(self.key)
            PtAtTimeCallback(self.key, BounceTimer, kRedTimerID)
            #print '\tDisabling the upper node region. Enabling the lower node region. 2'
            #NodeRgnAbove.value.disable()
            #NodeUnderRaisedFloor.value.enable()
            #NodeRgnBelow.value.enable()
            #NodeUnderLoweredFloor.value.disable()
            #print '\tRe-enabling the Menu Bar and Bahro Slate.'
            #PtSendPythonNotify('Show', 'Menu Bar', self.key)
            #PtSendPythonNotify('Enable', 'Bahro Slate', self.key)
            respPitUnblock.run(self.key)
            #if (not (PtIsExpertMode())):
            #    print 'In novice mode, so we\'ll go to a FP cam, re-enable input and send player to closest node'
            #    PtClosestNode()
            #    PtEnableMovement(1)
        elif (id == TooHighToJump.id):
            if (not (floorraised)):
                print '\tThe raising floor is now too high to jump off of. Jumpblock On. Ladders Enabled.'
                respEnableBlocker.run(self.key)
                respEnableLadders.run(self.key)
                for ladder in climbLadders:
                    ladder.physics.suppress(0)
            elif floorraised:
                print '\tThe lowering floor is now low enough to jump off. Jumpblocker Off. Ladders Disabled.'
                respDisableBlocker.run(self.key)
                respDisableLadders.run(self.key)
                print '\tKicking any squatters off the ladders.'
                for ladder in climbLadders:
                    ladder.physics.suppress(1)
                #PtForceOutOfLadder(objLadder01.value.getKey()) # will have to replace ladders with multistate, this will allow us to kick
                #PtForceOutOfLadder(objLadder02.value.getKey()) # the avatar out !
                #PtForceOutOfLadder(objLadder03.value.getKey())
        elif (id == actSideDoorsSeal.id):
            if (not (floorraised)):
                print '\tThe lowering counterweights have now blocked the doors.'
                respSideDoorsSeal.run(self.key)
            else:
                print '\tThe raising counterweights have now unblocked the doors.'
                respSideDoorsUnseal.run(self.key)
        elif (id == actCrank.id):
            print 'The crank below the floor has been engaged.'
            self.ToggleFloorState(((PtFindAvatar(events) == PtGetLocalAvatar()) and PtWasLocallyNotified(self.key)))
        elif (id == respYellowGlowStop.id):
            if (not (ReplacedByRed)):
                print 'OnNotify: The Yellow Glow just turned off.'
                self.UpdateGlowColor()
            else:
                print '\t(Yellow was ReplacedByRed)'
                ReplacedByRed = false
        elif (id == respBlueGlowStop.id):
            if (not (ReplacedByRed)):
                print 'OnNotify: The Blue Glow just turned off.'
                self.UpdateGlowColor()
            else:
                print '\t(Blue was ReplacedByRed)'
                ReplacedByRed = false
        elif (id == respRedGlowStart.id):
            print 'OnNotify: The red pedestal is fully raised. Enabling Red Bottom button.'
            RedBottom.enable()
        elif (id == respRedGlowStop.id):
            print 'OnNotify: The Red Glow just turned off.'
            self.UpdateGlowColor()
        elif (id == actOnFloor.id):
            for event in events:
                if (event[0] == kCollisionEvent):
                    avatar = PtFindAvatar(events)
                    player = PtGetLocalAvatar()
                    if (not (PtWasLocallyNotified(self.key))):
                        return
                    """if (avatar == player):
                        if (event[1] == 1):
                            OnFloor = 1
                            print 'OnNotify(): Player is now on floor elev'
                        else:
                            OnFloor = 0
                            print 'OnNotify(): Player is now off floor elev'#"""
                    if (event[1] == 1):
                        PtAttachObject(avatar, floorParent)
                        print 'OnNotify(): Some player is now on floor elev'
                    else:
                        PtDetachObject(avatar, floorParent)
                        print 'OnNotify(): Some player is now off floor elev'
        elif id == actDisableFloorCollider.id:
            for event in events:
                if (event[0] == kCollisionEvent):
                    avatar = PtFindAvatar(events)
                    player = PtGetLocalAvatar()
                    if (not (PtWasLocallyNotified(self.key))):
                        return
                    if (avatar == player):
                        if (event[1] == 1):
                            print "Under floor, disabling floor collision"
                            floorCollision.physics.suppress(1)
                        else:
                            print "Re-enabling floor collision"
                            floorCollision.physics.suppress(0)


    def UpdateGlowColor(self):
        global bluepushed
        global yellowpushed
        global justcamedown
        global currentcolor
        global floorraised
        global floormoving
        global ReplacedByRed
        print '\tblue=',
        print bluepushed,
        print ' yellow=',
        print yellowpushed,
        print ' justcamedown=',
        print justcamedown,
        print ' currentcolor=',
        print currentcolor,
        print ' f.raised=',
        print floorraised,
        print ' f.moving=',
        print floormoving
        if floormoving:
            print 'UpdateGlowColor: The Floor is moving. I\'m not going to mess with the Seal Glow color (which should be red?).'
        elif floorraised:
            print 'UpdateGlowColor: The Floor is raised. I\'m not going to mess with the Seal Glow color (which should be red?).'
        elif (bluepushed and (not (yellowpushed))):
            if justcamedown:
                print 'UpdateGlowColor: You started the blue timer within 30 seconds of when the floor came down.'
                print '\tKilling bounce timer.'
                respRedGlowStop.run(self.key)
                RedBottom.disable()
                justcamedown = false
            print 'UpdateGlowColor: Turning on Blue Glow'
            respBlueGlowStart.run(self.key)
            currentcolor = 1
        elif (yellowpushed and (not (bluepushed))):
            if justcamedown:
                print 'UpdateGlowColor: You started the yellow timer within 30 seconds of when the floor came down.'
                print '\tKilling bounce timer.'
                respRedGlowStop.run(self.key)
                RedBottom.disable()
                justcamedown = false
            print 'UpdateGlowColor: Turning on Yellow Glow'
            respYellowGlowStart.run(self.key)
            currentcolor = 2
        elif (yellowpushed and (bluepushed and (not (justcamedown)))):
            print 'UpdateGlowColor: Both the yellow and blue timers are running. Turning on Red Glow.'
            if (currentcolor == 1):
                print '\t(and turning off blue glow first)'
                respBlueGlowStop.run(self.key)
            elif (currentcolor == 2):
                print '\t(and turning off yellow glow first)'
                respYellowGlowStop.run(self.key)
            ReplacedByRed = true
            respRedGlowStart.run(self.key)
            currentcolor = 3
        elif justcamedown:
            print 'UpdateGlowColor: Turning on Red Glow. The floor just came down. Should expire within 30 seconds.'
            respRedGlowStart.run(self.key)
            currentcolor = 3
        elif ((not (bluepushed)) and ((not (yellowpushed)) and (not (justcamedown)))):
            print 'UpdateGlowColor: Neither timer is running, and justcamedown=false. Seal should have no color.'
            if (currentcolor == 1):
                print 'UpdateGlowColor: I think the Blue glow is on, so I\'m turning Blue off.'
                respBlueGlowStop.run(self.key)
                currentcolor = 0
            elif (currentcolor == 2):
                print 'UpdateGlowColor:  I think the Yellow glow is on, so I\'m turning Yellow off.'
                respYellowGlowStop.run(self.key)
                currentcolor = 0
            elif (currentcolor == 3):
                print 'UpdateGlowColor:  I think the Red glow is on, so I\'m turning Red off. Hopefully something else comes on...'
                respRedGlowStop.run(self.key)
                currentcolor = 0
                print '\tDisabling the Red Bottom button.'
                RedBottom.disable()
            elif (currentcolor == 0):
                print 'UpdateGlowColor: I don\'t think the seal is glowing. Nothing to turn off.'
        print 'UpdateGlowColor: Now currentcolor=',
        print currentcolor


    def ToggleFloorState(self, isme):
        global floormoving
        global floorraised
        global justcamedown
        ageSDL = PtGetAgeSDL()
        if (not (floormoving)):
            if (not (floorraised)):
                if justcamedown:
                    print '\tYou\'ve re-entered the red glowing seal within 30 seconds of the floor lowering. It\'s going back up.'
                print '\tThe floor will raise now.'
                if isme:
                    ageSDL[FloorRaisedSDL.value] = (1,)
                floormoving = true
                respButtonsSink.run(self.key)
                actCrank.disable(self.key)
            if floorraised:
                print 'The floor will now lower.'
                if isme:
                    ageSDL[FloorRaisedSDL.value] = (0,)
                floormoving = true
                actCrank.disable(self.key)
                respButtonsSink.run(self.key)
        else:
            print 'ToggleFloorState: The floor is moving. I can\'t toggle its state.'


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        ageSDL = PtGetAgeSDL()
        if (ageSDL[FloorRaisedSDL.value][0] == 1):
            PtAtTimeCallback(self.key, DelayTimer, kDelayRaiseID)
        elif (ageSDL[FloorRaisedSDL.value][0] == 0):
            PtAtTimeCallback(self.key, 1, kDelayLowerID)
        else:
            print 'Error: Unexpected result in dsntShaftFloor.OnSDLNotify.'


    def OnTimer(self, id):
        global bluepushed
        global floormoving
        global floorraised
        global yellowpushed
        global justcamedown
        global OnFloor
        if (id == kBlueTimerID):
            print 'OnTimer: The Blue Timer has just expired.'
            bluepushed = false
            BlueTimer.enable(self.key)
            if ((not (floormoving)) and (not (floorraised))):
                respBlueGlowStop.run(self.key)
                if yellowpushed:
                    respRedGlowStop.run(self.key)
                else:
                    print '\tbut I\'m leaving the Red glowing because the floor is not stopped at the bottom.'
        elif (id == kYellowTimerID):
            print 'OnTimer: The Yellow Timer has just expired.'
            yellowpushed = false
            YellowTimer.enable(self.key)
            if ((not (floormoving)) and (not (floorraised))):
                respYellowGlowStop.run(self.key)
                if bluepushed:
                    respRedGlowStop.run(self.key)
                else:
                    print '\tbut I\'m leaving the Red glowing because the floor is not stopped at the bottom.'
        elif (id == kRedTimerID):
            print 'OnTimer: The Red (bounce) Timer has just expired.'
            if justcamedown:
                justcamedown = false
                if ((not (floormoving)) and (not (floorraised))):
                    self.UpdateGlowColor()
                else:
                    print '\tbut I\'m leaving the Red glowing because the floor is not stopped at the bottom.'
            else:
                print '\tbut I think the bounce timer was previously killed, because you restarted yellow '
                print '\tor blue within 30 seconds of the floor finishing lowering.'
                self.UpdateGlowColor()
        elif (id == kDelayRaiseID):
            respRaiseFloor.run(self.key)
            #respPitBlock.run(self.key)
            respPitUnblock.run(self.key)
            if (not (OnFloor)):
                print 'The player hit the bottom red button, but did NOT ride the floor up...'
                #print 'So Menu Bar and Slate are re-enabled.'
                #PtSendPythonNotify('Show', 'Menu Bar', self.key)
                #PtSendPythonNotify('Enable', 'Bahro Slate', self.key)
                #print 'Immediately disabling the upper node region. Immediately disabling the lower node region.'
                #NodeRgnAbove.value.disable()
                #NodeUnderRaisedFloor.value.enable()
                #NodeRgnBelow.value.disable()
                #NodeUnderLoweredFloor.value.enable()
            #if ((not (PtIsExpertMode())) and OnFloor):
            #    PtEnableMovement(0)
        elif (id == kDelayLowerID):
            respLowerFloor.run(self.key)
            respEnableBlocker.run(self.key)
            #if ((not (PtIsExpertMode())) and OnFloor):
            #    PtEnableMovement(0)
            print '\tEnabling Catwalk blockers.'
            respCatwalkBlock.run(self.key)
        elif (id == kNNodesFloorUpID):
            pass
            #print '\tOnPlayerSpawned: Setting novice nodes as if the Floor were raised.'
            #NodeRgnAbove.value.enable()
            #NodeUnderRaisedFloor.value.disable()
            #NodeRgnBelow.value.disable()
            #NodeUnderLoweredFloor.value.enable()
        elif (id == kNNodesFloorDownID):
            pass
            #print '\tOnPlayerSpawned: Setting novice nodes as if the Floor were lowered.'
            #NodeRgnAbove.value.disable()
            #NodeUnderRaisedFloor.value.enable()
            #NodeRgnBelow.value.enable()
            #NodeUnderLoweredFloor.value.disable()


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



