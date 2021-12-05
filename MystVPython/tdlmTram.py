"""
Note: I REALLY wasn't in the mood to adapt it to multiplayer after changing all those SDL things.

Tram ladders added in dusttest.prp are now disabled for the ride too - for some reason, the smartseek disables parenting for half
a second, resulting in the player flying behind the tram before falling...
There is no problem with being on the ladder when the tram starts, though.
#"""

from Plasma import *
from PlasmaTypes import *
import time
sdlPower1 = ptAttribString(1, 'SDL: power for Pillar 1')
sdlPower2 = ptAttribString(2, 'SDL: power for Pillar 3')
respPower = ptAttribResponder(3, 'resp: tram power', ['on', 'off'])
sdlCarLever = ptAttribString(4, 'SDL: car lever')
evtCarLeverMid = ptAttribActivator(5, 'anm evt: car lever mid')
evtCarLeverFront = ptAttribActivator(6, 'anm evt: car lever front')
evtCarLeverBack = ptAttribActivator(7, 'anm evt: car lever rev')
sdlCrankDir1 = ptAttribString(8, 'SDL: crank dir 1')
sdlCrankDir2 = ptAttribString(9, 'SDL: crank dir 2')
evtCrankDir1Fwd = ptAttribActivator(10, 'anm evt: crank dir 1 fwd')
evtCrankDir1Rev = ptAttribActivator(11, 'anm evt: crank dir 1 rev')
evtCrankDir2Fwd = ptAttribActivator(12, 'anm evt: crank dir 2 fwd')
evtCrankDir2Rev = ptAttribActivator(13, 'anm evt: crank dir 2 rev')
actCrankArm1 = ptAttribActivator(14, 'drag: Tram Pump, Pillar 1')
respCrankArm1 = ptAttribResponder(15, 'resp: Tram Pump, Pillar 1', ['forward', 'reverse', 'stop'])
actCrankArm2 = ptAttribActivator(16, 'drag: Tram Pump, Pillar 3')
respCrankArm2 = ptAttribResponder(17, 'resp: Tram Pump, Pillar 3', ['forward', 'reverse', 'stop'])
respCarMove = ptAttribResponder(18, 'resp: tram car move', ['forward', 'reverse', 'stop'])
evtCarAtStart = ptAttribActivator(19, 'anm evt: car at start')
evtCarAtMid = ptAttribActivator(20, 'anm evt: car at midpoint')
evtCarAtEnd = ptAttribActivator(21, 'anm evt: car at end')
objCarMaster = ptAttribSceneobject(22, 'scn obj: car master dummy')
animCarLever = ptAttribAnimation(23, 'anim: car lever')
sdlCarPos = ptAttribString(24, 'SDL: car pos')
respCarLeverReset = ptAttribResponder(25, 'resp: car lever reset', ['FrontToMid', 'BackToMid'])
respDoor1 = ptAttribResponder(26, 'resp: car door 1', ['open', 'close'])
respDoor2 = ptAttribResponder(27, 'resp: car door 2', ['open', 'close'])
respLadders = ptAttribResponder(28, 'resp: tram ladders', ['on', 'off'])
sdlUnblockLOS = ptAttribString(29, 'SDL: Unblock LOS')
animTram = ptAttribAnimation(30, 'anim: tram')
animDock1Pump = ptAttribAnimation(31, 'anim: Dock 1 Pump')
animDock3Pump = ptAttribAnimation(32, 'anim: Dock 3 Pump')
actTramLever = ptAttribActivator(33, 'act: Tram Lever')
respSfxPumpReset01 = ptAttribResponder(34, 'resp: Pump 1 Reset Sfx')
respSfxPumpReset03 = ptAttribResponder(35, 'resp: Pump 3 Reset Sfx')
actLadder01 = ptAttribActivator(36, 'act: Ladder 01')
actLadder02 = ptAttribActivator(37, 'act: Ladder 02')
boolPower1 = 0
boolPower2 = 0
byteCarLever = 0
boolCrankDir1 = 0
boolCrankDir2 = 0
power = 0
cranking = 0
door1 = 0
door2 = 0
AlreadyMoving = false
kCarLeverMid = 0.54
kCarLeverEnd = 1.0
kFramesPerPump = 20
prevCarFrame = 0
ladders = []

class tdlmTram(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6218
        version = 7
        self.version = version
        print '__init__tdlmTram v. ',
        print version,
        print '.1'


    def OnServerInitComplete(self):
        global boolPower1
        global boolPower2
        global byteCarLever
        global boolCrankDir1
        global boolCrankDir2
        global power
        global door1
        global door2
        global prevCarFrame
        global ladders
        ageSDL = PtGetAgeSDL()

        for varname in (
                    sdlPower1,
                    sdlPower2,
                    sdlCarLever,
                    sdlCrankDir1,
                    sdlCrankDir2,
                    sdlCarPos,
                    sdlUnblockLOS,
                ):
            ageSDL.sendToClients(varname.value)
            ageSDL.setFlags(varname.value, 1, 1)
            ageSDL.setNotify(self.key, varname.value, 0.0)

        ## lower speed to counterpart speeded animation
        animDock1Pump.animation.speed(0.1)
        animDock3Pump.animation.speed(0.1)


        ladders = [PtFindSceneobject("TramLdr1b", "Todelmer"),
                   PtFindSceneobject("TramLdr1t", "Todelmer"),
                   PtFindSceneobject("TramLdr2b", "Todelmer"),
                   PtFindSceneobject("TramLdr2t", "Todelmer")]

        boolPower1 = ageSDL[sdlPower1.value][0]
        boolPower2 = ageSDL[sdlPower2.value][0]
        byteCarLever = ageSDL[sdlCarLever.value][0]
        boolCrankDir1 = ageSDL[sdlCrankDir1.value][0]
        boolCrankDir2 = ageSDL[sdlCrankDir2.value][0]
        prevCarFrame = ageSDL[sdlCarPos.value][0]
        print 'tdlmTram.OnAgeDataInitialized(): ',
        print sdlPower1.value,
        print '= ',
        print boolPower1
        print 'tdlmTram.OnAgeDataInitialized(): ',
        print sdlPower2.value,
        print '= ',
        print boolPower2
        print 'tdlmTram.OnAgeDataInitialized(): ',
        print sdlCarLever.value,
        print '= ',
        print byteCarLever
        print 'tdlmTram.OnAgeDataInitialized(): ',
        print sdlCrankDir1.value,
        print '= ',
        print boolCrankDir1
        print 'tdlmTram.OnAgeDataInitialized(): ',
        print sdlCrankDir2.value,
        print '= ',
        print boolCrankDir2
        if (boolPower1 and boolPower2):
            print 'tdlmTram.OnAgeDataInitialized(): power on in Pillars 1 & 3, so tram has POWER'
            power = 1
            respPower.run(self.key, state='on', fastforward=1)
        else:
            print 'tdlmTram.OnAgeDataInitialized(): power off somewhere, so tram has no power'
            respPower.run(self.key, state='off', fastforward=1)
        animCarLever.animation.skipToTime(0.5)
        print 'tdlmTram.OnAgeDataInitialized: The tram is at frame ',
        print ageSDL[sdlCarPos.value][0],
        print ' of 1000.'
        animTram.animation.skipToTime(float((ageSDL[sdlCarPos.value][0] / 30.0)))
        print '\tSkipping tram animation to time ',
        print float((ageSDL[sdlCarPos.value][0] / 30.0))
        if (ageSDL[sdlCarPos.value][0] == 0):
            print '\tThe tram is at the Pillar 1. Opening tram door facing pillar 1.'
            respDoor1.run(self.key, state='open', fastforward=1)
            door1 = 1
        elif (ageSDL[sdlCarPos.value][0] == 500):
            print '\tThe tram is at the midpoint. Hopefully it stays there.'
        elif (ageSDL[sdlCarPos.value][0] == 1000):
            print '\tThe tram is at the Pillar 3. Opening tram door facing pillar 3.'
            respDoor2.run(self.key, state='open', fastforward=1)
            door2 = 1
        else:
            print '\tThe tram is not at a Pillar.'


    def OnNotify(self, state, id, events):
        global AlreadyMoving
        global cranking
        global boolCrankDir1
        global door1
        global door2
        ageSDL = PtGetAgeSDL()
        if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
            if ((id == evtCrankDir1Fwd.id) and state):
                print '\t\tCrankDir break 1'
                ageSDL[sdlCrankDir1.value] = (0,)
            elif ((id == evtCrankDir1Rev.id) and state):
                print '\t\tCrankDir break 2'
                ageSDL[sdlCrankDir1.value] = (1,)
            elif ((id == evtCrankDir2Fwd.id) and state):
                print '\t\tCrankDir break 3'
                ageSDL[sdlCrankDir2.value] = (1,)
            elif ((id == evtCrankDir2Rev.id) and state):
                print '\t\tCrankDir break 4'
                ageSDL[sdlCrankDir2.value] = (0,)
            elif ((id == evtCarLeverMid.id) and state):
                if AlreadyMoving:
                    return
                print 'Car lever set to middle. 0.'
                ageSDL[sdlCarLever.value] = (0,)
            elif ((id == evtCarLeverFront.id) and state):
                if AlreadyMoving:
                    return
                print 'Car lever set to forward. 1.'
                ageSDL[sdlCarLever.value] = (1,)
            elif ((id == evtCarLeverBack.id) and state):
                if AlreadyMoving:
                    return
                print 'Car lever set to reverse. 2.'
                ageSDL[sdlCarLever.value] = (2,)
        if (id == actCrankArm1.id):
            if state:
                # ????
                #if (actCrankArm1.getDraggableValue() != 1):
                #    return
                if cranking:
                    print 'Cranking.'
                    return
                else:
                    print 'Pump on Dock 1 manipulated. '
                    cranking = 1
                    if (boolCrankDir1 == 1):
                        if (ageSDL[sdlCarPos.value][0] != 1000):
                            respCrankArm1.run(self.key, state='forward')
                            respCarMove.run(self.key, state='forward')
                            print '\t...tram inches forward'
                            PrevPos = ageSDL[sdlCarPos.value][0]
                            if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                                if ((PrevPos + kFramesPerPump) > 1000):
                                    print 'tdlmTram: Can\'t go past Pillar 3. Just going to frame 1000 instead.'
                                    ageSDL[sdlCarPos.value] = (1000,)
                                else:
                                    ageSDL[sdlCarPos.value] = ((PrevPos + kFramesPerPump),)
                            if door1:
                                door1 = 0
                                respDoor1.run(self.key, state='close')
                            elif door2:
                                door2 = 0
                                respDoor2.run(self.key, state='close')
                        else:
                            print 'Can\'t crank tram forward anymore. It\'s already at pillar 3. sdlCarPos=',
                            print ageSDL[sdlCarPos.value][0]
                    elif (boolCrankDir1 == 0):
                        if (ageSDL[sdlUnblockLOS.value][0] == 0):
                            print 'This is the first time you\'ve tried to move the tram backwards.'
                            print '\tTurning the wheel to unblock the view of P2 from Miniscope1'
                            if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                                ageSDL[sdlUnblockLOS.value] = (1,)
                        if (ageSDL[sdlCarPos.value][0] != 0):
                            respCrankArm1.run(self.key, state='reverse')
                            respCarMove.run(self.key, state='reverse')
                            print '\t...tram inches backwards'
                            PrevPos = ageSDL[sdlCarPos.value][0]
                            if ((PrevPos - kFramesPerPump) < 0):
                                if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                                    ageSDL[sdlCarPos.value] = (0,)
                                print 'tdlmTram: Can\'t reverse past Pillar 1. Just going to frame 0 instead.'
                            else:
                                if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                                    ageSDL[sdlCarPos.value] = ((PrevPos - kFramesPerPump),)
                            if door1:
                                door1 = 0
                                respDoor1.run(self.key, state='close')
                            elif door2:
                                door2 = 0
                                respDoor2.run(self.key, state='close')
                        else:
                            print 'Can\'t crank tram backwards anymore. It\'s already at pillar 1.'
                print 'Trying to animate pump 1 backwards...'
                actCrankArm1.disable()
                animDock1Pump.animation.playToTime(0)
                respSfxPumpReset01.run(self.key)
                PtAtTimeCallback(self.key, 4, 2)
        elif (id == actCrankArm2.id):
            if state:
                # ???
                #if (actCrankArm2.getDraggableValue() != 1):
                #    return
                if cranking:
                    return
                else:
                    print '#'
                    print 'Pump on Dock 3 manipulated.'
                    cranking = 1
                    if (ageSDL[sdlCrankDir2.value][0] == 1):
                        if (ageSDL[sdlCarPos.value][0] != 1000):
                            respCrankArm2.run(self.key, state='forward')
                            respCarMove.run(self.key, state='forward')
                            print '\t...tram inches forward'
                            PrevPos = ageSDL[sdlCarPos.value][0]
                            if ((PrevPos + kFramesPerPump) > 1000):
                                print 'tdlmTram: Can\'t go past Pillar 3. Just going to frame 1000 instead.'
                                if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                                    ageSDL[sdlCarPos.value] = (1000,)
                            else:
                                if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                                    ageSDL[sdlCarPos.value] = ((PrevPos + kFramesPerPump),)
                            if door1:
                                door1 = 0
                                respDoor1.run(self.key, state='close')
                            elif door2:
                                door2 = 0
                                respDoor2.run(self.key, state='close')
                        else:
                            print 'Can\'t crank tram forward anymore. It\'s already at pillar 3.'
                    elif (ageSDL[sdlCrankDir2.value][0] == 0):
                        if (ageSDL[sdlCarPos.value][0] != 0):
                            respCrankArm2.run(self.key, state='reverse')
                            respCarMove.run(self.key, state='reverse')
                            print '\t...tram inches backwards'
                            PrevPos = ageSDL[sdlCarPos.value][0]
                            if ((PrevPos - kFramesPerPump) < 0):
                                if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                                    ageSDL[sdlCarPos.value] = (0,)
                                print 'tdlmTram: Can\'t reverse past Pillar 1. Just going to frame 0 instead.'
                            else:
                                if (PtFindAvatar(events) == PtGetLocalAvatar()) and (PtWasLocallyNotified(self.key)):
                                    ageSDL[sdlCarPos.value] = ((PrevPos - kFramesPerPump),)
                            if door1:
                                door1 = 0
                                respDoor1.run(self.key, state='close')
                            elif door2:
                                door2 = 0
                                respDoor2.run(self.key, state='close')
                        else:
                            print 'Can\'t crank tram backwards anymore. It\'s already at pillar 1.'
                print 'Trying to animate pump 3 handle backwards.'
                actCrankArm2.disable()
                animDock3Pump.animation.playToTime(0)
                respSfxPumpReset03.run(self.key)
                PtAtTimeCallback(self.key, 4, 3)
        elif (id == respCarLeverReset.id):
            print 'tdlmTram: The lever has just reset to the middle.'
            self.CheckPower()


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global boolPower1
        global boolPower2
        global power
        global byteCarLever
        global AlreadyMoving
        global door1
        global door2
        global boolCrankDir1
        global boolCrankDir2
        global prevCarFrame
        ageSDL = PtGetAgeSDL()
        print 'tdlmTram.OnSDLNotify(): SDL for ',
        print VARname,
        print ' changed to ',
        print ageSDL[VARname][0]
        if ((VARname == sdlPower1.value) or (VARname == sdlPower2.value)):
            if (VARname == sdlPower1.value):
                boolPower1 = ageSDL[VARname][0]
            if (VARname == sdlPower2.value):
                boolPower2 = ageSDL[VARname][0]
            if (boolPower1 and boolPower2):
                if (not (power)):
                    respPower.run(self.key, state='on')
                    power = 1
            elif power:
                respPower.run(self.key, state='off')
                power = 0
        elif (VARname == sdlCarPos.value):
            print 'tdlmTram.OnSDLNotify: The Tram animates from frame ',
            print prevCarFrame,
            print ' to frame ',
            print ageSDL[VARname][0]
            if (prevCarFrame < ageSDL[VARname][0]):
                print '\t(which is forwards)'
                respCarMove.run(self.key, state='forward')
            elif (prevCarFrame > ageSDL[VARname][0]):
                print '\t(which is backwards)'
                respCarMove.run(self.key, state='reverse')
            else:
                print '(I\'m not sure how those two values are the same?)'
            targettime = float((ageSDL[VARname][0] / 30.0))
            if (abs((prevCarFrame - ageSDL[VARname][0])) == 500):
                animTram.animation.speed(1)
                print '\tThe ridden tram moves quickly'
            else:
                animTram.animation.speed(0.2)
                print '\tThe cranked tram moves slowly.'
            animTram.animation.playToTime(targettime)
            prevCarFrame = ageSDL[VARname][0]
        elif (VARname == sdlCarLever.value):
            byteCarLever = ageSDL[VARname][0]
            if byteCarLever:
                if AlreadyMoving:
                    print 'The tram is already in motion. Ignoring car lever pull.'
                    return
                if (byteCarLever == 1):
                    if ((ageSDL[sdlCarPos.value][0] != 0) and (ageSDL[sdlCarPos.value][0] != 500)):
                        print 'You can\'t go forward from Pillar 3. Car Lever pull has no effect.'
                        print 'sdlCarPos = ',
                        print ageSDL[sdlCarPos.value][0]
                        return
                    self.PlayerOnTram()
                    respCarMove.run(self.key, state='forward')
                    print 'Disabling the tram lever for the duration of the ride.'
                    actTramLever.disable()
                    if (ageSDL[sdlCarPos.value][0] == 0):
                        print 'Tram animates from Pillar 1 to the midway pillar.'
                        ageSDL[sdlCarPos.value] = (500,)
                        AlreadyMoving = true
                        PtAtTimeCallback(self.key, 16.6, 4)
                    elif (ageSDL[sdlCarPos.value][0] == 500):
                        print 'Tram animates from midway pillar to Pillar 3.'
                        ageSDL[sdlCarPos.value] = (1000,)
                        AlreadyMoving = true
                        PtAtTimeCallback(self.key, 16.6, 4)
                elif (byteCarLever == 2):
                    if ((ageSDL[sdlCarPos.value][0] != 1000) and (ageSDL[sdlCarPos.value][0] != 500)):
                        print 'You can\'t go backwards from Pillar 1. Car Lever pull has no effect.'
                        print 'sdlCarPos = ',
                        print ageSDL[sdlCarPos.value][0]
                        return
                    self.PlayerOnTram()
                    respCarMove.run(self.key, state='reverse')
                    print 'Disabling the tram lever for the duration of the ride.'
                    actTramLever.disable()
                    if (ageSDL[sdlCarPos.value][0] == 1000):
                        print 'Tram animates from Pillar 3 to the midway point.'
                        ageSDL[sdlCarPos.value] = (500,)
                        AlreadyMoving = true
                        PtAtTimeCallback(self.key, 16.6, 5)
                    elif (ageSDL[sdlCarPos.value][0] == 500):
                        print 'Tram animates from midway pillar to Pillar 1.'
                        ageSDL[sdlCarPos.value] = (0,)
                        AlreadyMoving = true
                        PtAtTimeCallback(self.key, 16.6, 5)
                if door1:
                    door1 = 0
                    respDoor1.run(self.key, state='close')
                    print 'tdlmTram.OnSDLNotify: Closing tram door facing pillar 1.'
                elif door2:
                    door2 = 0
                    respDoor2.run(self.key, state='close')
                    print 'tdlmTram.OnSDLNotify: Closing tram door facing pillar 3.'
            elif (not (byteCarLever)):
                #print 'Re-enabling menu bar, now that the tram ride is over.'
                #PtSendPythonNotify('Show', 'Menu Bar', self.key)
                self.CheckPower()
                #if (not (PtIsExpertMode())):
                #    print 'In novice mode, so we\'ll send player to closest node and re-enable movement'
                #    PtClosestNode()
                #    PtEnableMovementKeys(1)
                #    print '\tAlso, set camera to not always cut.'
                #    PtSetCamCutAlways(0)
                #else:
                #    print 'In expert mode, so just enabling movement...'
                #    PtEnableMovement(1)
                #PtEnableMovementKeys(1)
        elif (VARname == sdlCrankDir1.value):
            boolCrankDir1 = ageSDL[VARname][0]
        elif (VARname == sdlCrankDir2.value):
            boolCrankDir2 = ageSDL[VARname][0]


    def PlayerOnTram(self):
        for ladder in ladders:
            ladder.physics.suppress(1)
        print '\tDisabling tram ladders during the tram ride.'


    def OnTimer(self, id):
        global cranking
        global door2
        global door1
        global AlreadyMoving
        print 'tdlmTram.OnTimer: id = ',
        print id
        ageSDL = PtGetAgeSDL()
        if (id == 2):
            cranking = 0
            print 'Dock crank 1 fully resets. Tram anim frame = ',
            print ageSDL[sdlCarPos.value][0]
            actCrankArm1.enable()
            respCarMove.run(self.key, state='stop')
            self.OpenADoor()
        elif (id == 3):
            cranking = 0
            print 'Dock crank 3 fully resets. Tram anim frame = ',
            print ageSDL[sdlCarPos.value][0]
            actCrankArm2.enable()
            respCarMove.run(self.key, state='stop')
            self.OpenADoor()
        elif (id == 4):
            print 'The tram has finished moving forwards. Player was riding.'
            respCarMove.run(self.key, state='stop')
            PtAtTimeCallback(self.key, 2, 6)
            print 'tdlmTram.OnNotify(): will reset car lever from front to mid'
            respCarLeverReset.run(self.key, state='FrontToMid')
            ageSDL[sdlCarLever.value] = (0,)
            print '\tRe-enabling ladders tram.'
            actLadder01.enable()
            actLadder02.enable()
            for ladder in ladders:
                ladder.physics.suppress(0)
            if (ageSDL[sdlCarPos.value][0] == 1000):
                door2 = 1
                respDoor2.run(self.key, state='open')
                print 'tdlmTram.OnTimer: Opening tram door facing pillar 3.'
        elif (id == 5):
            print 'The tram has finished moving backwards. Player was riding.'
            respCarMove.run(self.key, state='stop')
            PtAtTimeCallback(self.key, 2, 6)
            print 'tdlmTram.OnNotify(): will reset car lever from back to mid'
            respCarLeverReset.run(self.key, state='BackToMid')
            ageSDL[sdlCarLever.value] = (0,)
            print '\tRe-enabling ladders tram.'
            actLadder01.enable()
            actLadder02.enable()
            for ladder in ladders:
                ladder.physics.suppress(0)
            if (ageSDL[sdlCarPos.value][0] == 0):
                door1 = 1
                respDoor1.run(self.key, state='open')
                print 'tdlmTram.OnTimer: Opening tram door facing pillar 1.'
        elif (id == 6):
            AlreadyMoving = false
            print 'tdlmTram: AlreadyMoving reset to false.'
            print '\tDetaching player from the tram.'
            # no longer required ! Subworld !
            return
            player = PtGetLocalAvatar()
            PtDetachObject(player, objCarMaster.value)
            player.physics.suppress(0)


    def OpenADoor(self):
        global door2
        global door1
        ageSDL = PtGetAgeSDL()
        if (ageSDL[sdlCarPos.value][0] == 1000):
            if (door2 == 1):
                print '\tThe tram door facing pillar 3 was already open.'
            else:
                door2 = 1
                print '\tYou weren\'t riding, but opening tram door facing pillar 3.'
                respDoor2.run(self.key, state='open')
                self.CheckPower()
        elif (ageSDL[sdlCarPos.value][0] == 0):
            if (door1 == 1):
                print '\tThe tram door facing pillar 1 was already open.'
            else:
                door1 = 1
                respDoor1.run(self.key, state='open')
                print '\tYou weren\'t riding, but opening tram door facing pillar 1.'
                self.CheckPower()


    def CheckPower(self):
        global power
        ageSDL = PtGetAgeSDL()
        if ((ageSDL[sdlPower1.value][0] == 1) and (ageSDL[sdlPower2.value][0] == 1)):
            print '\tThe power is on. Re-enabling the tram lever draggable.'
            actTramLever.enable()
            power = 1
        else:
            print '\tThe power is off somewhere. Making sure the tram lever draggable is still disabled.'
            actTramLever.disable()
            power = 0


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



