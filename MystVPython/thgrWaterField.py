"""
I feel sorry for whoever had to write the script for this puzzle. That must have taken some time.
#"""


from Plasma import *
from PlasmaTypes import *
import time
waterFieldStates = ['left_on', 'left_off', 'right_on', 'right_off']
SDLThermalValveW1 = ptAttribString(1, 'SDL: thermal valve W1')
SDLThermalValveW2 = ptAttribString(2, 'SDL: thermal valve W2')
SDLThermalValveW3 = ptAttribString(3, 'SDL: thermal valve W3')
SDLThermalValveE1 = ptAttribString(4, 'SDL: thermal valve E1')
SDLThermalValveE2 = ptAttribString(5, 'SDL: thermal valve E2')
SDLThermalValveE3 = ptAttribString(6, 'SDL: thermal valve E3')
RespWestControls = ptAttribResponder(7, 'resp: west field controls', ['off', 'on'])
RespEastControls = ptAttribResponder(8, 'resp: east field controls', ['off', 'on'])
SDLHeat = ptAttribString(9, 'SDL: heat')
SDLFieldValveW1 = ptAttribString(10, 'SDL: field valve W1')
SDLFieldValveW2 = ptAttribString(11, 'SDL: field valve W2')
SDLFieldValveW3 = ptAttribString(12, 'SDL: field valve W3')
SDLFieldValveE1 = ptAttribString(13, 'SDL: field valve E1')
SDLFieldValveE2 = ptAttribString(14, 'SDL: field valve E2')
SDLFieldValveE3 = ptAttribString(15, 'SDL: field valve E3')
RespWaterField = ptAttribResponderList(16, 'resp: water-field', statelist=waterFieldStates, byObject=1)
AnimFieldValveW1Init = ptAttribAnimation(17, 'anim: field valve W1 on init')
AnimFieldValveW2Init = ptAttribAnimation(18, 'anim: field valve W2 on init')
AnimFieldValveW3Init = ptAttribAnimation(19, 'anim: field valve W3 on init')
AnimFieldValveE1Init = ptAttribAnimation(20, 'anim: field valve E1 on init')
AnimFieldValveE2Init = ptAttribAnimation(21, 'anim: field valve E2 on init')
AnimFieldValveE3Init = ptAttribAnimation(22, 'anim: field valve E3 on init')
ActWater = ptAttribActivator(23, 'rgn sns: in the water')
NodeRgnAbove = ptAttribSceneobject(24, 'obj: node region - high')
NodeRgnBelow = ptAttribSceneobject(25, 'obj: node region - low')
RespInWater = ptAttribResponder(26, 'resp: player in water', ['in', 'out', 'out_w2ANDw3', 'out_w3'])
boolThermalValveW1 = 0
boolThermalValveW2 = 0
boolThermalValveW3 = 0
boolThermalValveE1 = 0
boolThermalValveE2 = 0
boolThermalValveE3 = 0
listWestSDLVals = []
listEastSDLVals = []
boolHeat = 0
byteFieldValveW1 = 0
byteFieldValveW2 = 0
byteFieldValveW3 = 0
byteFieldValveE1 = 0
byteFieldValveE2 = 0
byteFieldValveE3 = 0
oldFieldValveW1 = 0
oldFieldValveW2 = 0
oldFieldValveW3 = 0
oldFieldValveE1 = 0
oldFieldValveE2 = 0
oldFieldValveE3 = 0
listRespWaterField = []
oldWest = ''
newWest = ''
oldEast = ''
newEast = ''
onInit = 0
listCurStates = ['', '', '', '', '', '']
westControls = ''
eastControls = ''
PlayerInWater = 0
#CurField = 0
kValveAnimMid = 0.5
kValveAnimEnd = 1.0
kTimerFieldDrop = 0.2

class thgrWaterField(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6386
        version = 6
        self.version = version
        print 'thgrWaterField v. ',
        print version,
        print '.0'


    def OnServerInitComplete(self):
        global boolThermalValveW1
        global boolThermalValveW2
        global boolThermalValveW3
        global boolThermalValveE1
        global boolThermalValveE2
        global boolThermalValveE3
        global boolHeat
        global byteFieldValveW1
        global byteFieldValveW2
        global byteFieldValveW3
        global byteFieldValveE1
        global byteFieldValveE2
        global byteFieldValveE3
        global listWestSDLVals
        global listEastSDLVals
        global oldFieldValveW1
        global oldFieldValveW2
        global oldFieldValveW3
        global oldFieldValveE1
        global oldFieldValveE2
        global oldFieldValveE3
        global listRespWaterField
        global listCurStates
        global westControls
        global eastControls
        #global CurField
        ageSDL = PtGetAgeSDL()
        for sdlvar in (
                            SDLThermalValveW1, SDLThermalValveW2, SDLThermalValveW3,
                            SDLThermalValveE1, SDLThermalValveE2, SDLThermalValveE3,
                            SDLFieldValveW1, SDLFieldValveW2, SDLFieldValveW3,
                            SDLFieldValveE1, SDLFieldValveE2, SDLFieldValveE3,
                            SDLHeat,
                       ):
            # screw 1k lines code
            ageSDL.sendToClients(sdlvar.value)
            ageSDL.setFlags(sdlvar.value, 1, 1)
            ageSDL.setNotify(self.key, sdlvar.value, 0.0)

        boolThermalValveW1 = ageSDL[SDLThermalValveW1.value][0]
        boolThermalValveW2 = ageSDL[SDLThermalValveW2.value][0]
        boolThermalValveW3 = ageSDL[SDLThermalValveW3.value][0]
        boolThermalValveE1 = ageSDL[SDLThermalValveE1.value][0]
        boolThermalValveE2 = ageSDL[SDLThermalValveE2.value][0]
        boolThermalValveE3 = ageSDL[SDLThermalValveE3.value][0]
        boolHeat = ageSDL[SDLHeat.value][0]
        byteFieldValveW1 = ageSDL[SDLFieldValveW1.value][0]
        byteFieldValveW2 = ageSDL[SDLFieldValveW2.value][0]
        byteFieldValveW3 = ageSDL[SDLFieldValveW3.value][0]
        byteFieldValveE1 = ageSDL[SDLFieldValveE1.value][0]
        byteFieldValveE2 = ageSDL[SDLFieldValveE2.value][0]
        byteFieldValveE3 = ageSDL[SDLFieldValveE3.value][0]
        listWestSDLVals = [boolThermalValveW1, boolThermalValveW2, boolThermalValveW3]
        listEastSDLVals = [boolThermalValveE1, boolThermalValveE2, boolThermalValveE3]
        print 'thgrWaterField.OnServerInitComplete(): thermal valve West SDL list = ',
        print listWestSDLVals
        print 'thgrWaterField.OnServerInitComplete(): thermal valve East SDL list = ',
        print listEastSDLVals
        self.UpdateControls('west', 1)
        self.UpdateControls('east', 1)
        oldFieldValveW1 = byteFieldValveW1
        oldFieldValveW2 = byteFieldValveW2
        oldFieldValveW3 = byteFieldValveW3
        oldFieldValveE1 = byteFieldValveE1
        oldFieldValveE2 = byteFieldValveE2
        oldFieldValveE3 = byteFieldValveE3
        print 'thgrWaterField.OnServerInitComplete(): field valve W1 = ',
        print byteFieldValveW1
        print 'thgrWaterField.OnServerInitComplete(): field valve W2 = ',
        print byteFieldValveW2
        print 'thgrWaterField.OnServerInitComplete(): field valve W3 = ',
        print byteFieldValveW3
        print 'thgrWaterField.OnServerInitComplete(): field valve E1 = ',
        print byteFieldValveE1
        print 'thgrWaterField.OnServerInitComplete(): field valve E2 = ',
        print byteFieldValveE2
        print 'thgrWaterField.OnServerInitComplete(): field valve E3 = ',
        print byteFieldValveE3
        for resp in RespWaterField.value:
            thisResp = resp.getName()
            listRespWaterField.append(thisResp)
        print 'thgrWaterField.OnServerInitComplete(): listRespWaterField =',
        print listRespWaterField
        if (byteFieldValveW1 == 1):
            listCurStates[0] = 'left_on'
        elif (byteFieldValveW1 == 2):
            listCurStates[0] = 'right_on'
            AnimFieldValveW1Init.animation.skipToTime(kValveAnimEnd)
        elif (not (byteFieldValveW1)):
            listCurStates[0] = 'left_off'
            AnimFieldValveW1Init.animation.skipToTime(kValveAnimMid)
        if (byteFieldValveW2 == 1):
            listCurStates[1] = 'left_on'
        elif (byteFieldValveW2 == 2):
            listCurStates[1] = 'right_on'
            AnimFieldValveW2Init.animation.skipToTime(kValveAnimEnd)
        elif (not (byteFieldValveW2)):
            listCurStates[1] = 'left_off'
            AnimFieldValveW2Init.animation.skipToTime(kValveAnimMid)
        if (byteFieldValveW3 == 1):
            listCurStates[2] = 'left_on'
        elif (byteFieldValveW3 == 2):
            listCurStates[2] = 'right_on'
            AnimFieldValveW3Init.animation.skipToTime(kValveAnimEnd)
        elif (not (byteFieldValveW3)):
            listCurStates[2] = 'left_off'
            AnimFieldValveW3Init.animation.skipToTime(kValveAnimMid)
        if (byteFieldValveE1 == 1):
            listCurStates[3] = 'left_on'
        elif (byteFieldValveE1 == 2):
            listCurStates[3] = 'right_on'
            AnimFieldValveE1Init.animation.skipToTime(kValveAnimEnd)
        elif (not (byteFieldValveE1)):
            listCurStates[3] = 'left_off'
            AnimFieldValveE1Init.animation.skipToTime(kValveAnimMid)
        if (byteFieldValveE2 == 1):
            listCurStates[4] = 'left_on'
        elif (byteFieldValveE2 == 2):
            listCurStates[4] = 'right_on'
            AnimFieldValveE2Init.animation.skipToTime(kValveAnimEnd)
        elif (not (byteFieldValveE2)):
            listCurStates[4] = 'left_off'
            AnimFieldValveE2Init.animation.skipToTime(kValveAnimMid)
        if (byteFieldValveE3 == 1):
            listCurStates[5] = 'left_on'
        elif (byteFieldValveE3 == 2):
            listCurStates[5] = 'right_on'
            AnimFieldValveE3Init.animation.skipToTime(kValveAnimEnd)
        elif (not (byteFieldValveE3)):
            listCurStates[5] = 'left_off'
            AnimFieldValveE3Init.animation.skipToTime(kValveAnimMid)
        if (not (boolHeat)):
            n = 0
            for state in listCurStates:
                listCurStates[n] = 'left_off'
                n += 1
            print 'thgrWaterField.OnServerInitComplete(): listCurStates = ',
            print listCurStates
            #print 'thgrWaterField.OnServerInitComplete(): field is NOT active, so we\'ll make sure the \'above\' and \'below\' nodes are disabled'
            #NodeRgnAbove.value.disable()
            #NodeRgnBelow.value.disable()
            return
        elif ((westControls == 'off') or (eastControls == 'off')):
            if (westControls == 'off'):
                listCurStates[0] = 'left_off'
                listCurStates[1] = 'left_off'
                listCurStates[2] = 'left_off'
            if (eastControls == 'off'):
                listCurStates[3] = 'left_off'
                listCurStates[4] = 'left_off'
                listCurStates[5] = 'left_off'
            print 'thgrWaterField.OnServerInitComplete(): listCurStates = ',
            print listCurStates
            #print 'thgrWaterField.OnServerInitComplete(): field is NOT active, so we\'ll make sure the \'above\' nodes are disabled'
            #NodeRgnAbove.value.disable()
            #NodeRgnBelow.value.disable()
            return
        if byteFieldValveW1:
            if (byteFieldValveW1 == 1):
                initState = 'left_on'
                RespWaterField.run(self.key, objectName=listRespWaterField[0], state=initState, fastforward=1)
                print 'thgrWaterField.OnServerInitComplete(): will run',
                print listRespWaterField[0],
                print 'with state: ',
                print initState
                listCurStates[0] = initState
            elif (byteFieldValveW1 == 2):
                initState = 'right_on'
                RespWaterField.run(self.key, objectName=listRespWaterField[0], state=initState, fastforward=1)
                print 'thgrWaterField.OnServerInitComplete(): will run',
                print listRespWaterField[0],
                print 'with state: ',
                print initState
                listCurStates[0] = initState
                if byteFieldValveW2:
                    if (byteFieldValveW2 == 1):
                        initState2 = 'left_on'
                        RespWaterField.run(self.key, objectName=listRespWaterField[1], state=initState2, fastforward=1)
                        print 'thgrWaterField.OnServerInitComplete(): will run',
                        print listRespWaterField[1],
                        print 'with state: ',
                        print initState2
                        listCurStates[1] = initState2
                        if byteFieldValveW3:
                            if (byteFieldValveW3 == 1):
                                initState3 = 'left_on'
                                RespWaterField.run(self.key, objectName=listRespWaterField[2], state=initState3, fastforward=1)
                                print 'thgrWaterField.OnServerInitComplete(): will run',
                                print listRespWaterField[2],
                                print 'with state: ',
                                print initState3
                                listCurStates[2] = initState3
                            elif (byteFieldValveW3 == 2):
                                initState3 = 'right_on'
                                RespWaterField.run(self.key, objectName=listRespWaterField[2], state=initState3, fastforward=1)
                                print 'thgrWaterField.OnServerInitComplete(): will run',
                                print listRespWaterField[2],
                                print 'with state: ',
                                print initState3
                                listCurStates[2] = initState3
                    elif (byteFieldValveW2 == 2):
                        initState2 = 'right_on'
                        RespWaterField.run(self.key, objectName=listRespWaterField[1], state=initState2, fastforward=1)
                        print 'thgrWaterField.OnServerInitComplete(): will run',
                        print listRespWaterField[1],
                        print 'with state: ',
                        print initState2
                        listCurStates[1] = initState2
        if byteFieldValveE1:
            if (byteFieldValveE1 == 1):
                initState = 'left_on'
                RespWaterField.run(self.key, objectName=listRespWaterField[3], state=initState, fastforward=1)
                print 'thgrWaterField.OnServerInitComplete(): will run',
                print listRespWaterField[3],
                print 'with state: ',
                print initState
                listCurStates[3] = initState
                if byteFieldValveE2:
                    if (byteFieldValveE2 == 1):
                        initState2 = 'left_on'
                        RespWaterField.run(self.key, objectName=listRespWaterField[4], state=initState2, fastforward=1)
                        print 'thgrWaterField.OnServerInitComplete(): will run',
                        print listRespWaterField[4],
                        print 'with state: ',
                        print initState2
                        listCurStates[4] = initState2
                    elif (byteFieldValveE2 == 2):
                        initState2 = 'right_on'
                        RespWaterField.run(self.key, objectName=listRespWaterField[4], state=initState2, fastforward=1)
                        print 'thgrWaterField.OnServerInitComplete(): will run',
                        print listRespWaterField[4],
                        print 'with state: ',
                        print initState2
                        listCurStates[4] = initState2
                        if byteFieldValveE3:
                            if (byteFieldValveE3 == 1):
                                initState3 = 'left_on'
                                RespWaterField.run(self.key, objectName=listRespWaterField[5], state=initState3, fastforward=1)
                                print 'thgrWaterField.OnServerInitComplete(): will run',
                                print listRespWaterField[5],
                                print 'with state: ',
                                print initState3
                                listCurStates[5] = initState3
                            elif (byteFieldValveE3 == 2):
                                initState3 = 'right_on'
                                RespWaterField.run(self.key, objectName=listRespWaterField[5], state=initState3, fastforward=1)
                                print 'thgrWaterField.OnServerInitComplete(): will run',
                                print listRespWaterField[5],
                                print 'with state: ',
                                print initState3
                                listCurStates[5] = initState3
            elif (byteFieldValveE1 == 2):
                initState = 'right_on'
                RespWaterField.run(self.key, objectName=listRespWaterField[3], state=initState, fastforward=1)
                print 'thgrWaterField.OnServerInitComplete(): will run',
                print listRespWaterField[3],
                print 'with state: ',
                print initState
                listCurStates[3] = initState
        print 'thgrWaterField.OnServerInitComplete(): listCurStates = ',
        print listCurStates
        #CurField = 0
        #for state in listCurStates:
        #    if (state[-1:] == 'n'):
        #        CurField = 1
        #        break
        #PtAtTimeCallback(self.key, 0, 1)
        #if CurField:
            #print 'thgrWaterField.OnServerInitComplete(): field is active, so will enable \'above\' nodes'
            #NodeRgnAbove.value.enable()
        #else:
            #print 'thgrWaterField.OnServerInitComplete(): field is NOT active, so we\'ll make sure the \'above\' nodes are disabled'
            #NodeRgnAbove.value.disable()


    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global boolThermalValveW1
        global boolThermalValveW2
        global boolThermalValveW3
        global boolThermalValveE1
        global boolThermalValveE2
        global boolThermalValveE3
        global boolHeat
        global byteFieldValveW1
        global oldFieldValveW1
        global byteFieldValveW2
        global oldFieldValveW2
        global byteFieldValveW3
        global oldFieldValveW3
        global byteFieldValveE1
        global oldFieldValveE1
        global byteFieldValveE2
        global oldFieldValveE2
        global byteFieldValveE3
        global oldFieldValveE3
        global listWestSDLVals
        global listEastSDLVals
        ageSDL = PtGetAgeSDL()
        print 'thgrWaterField.OnSDLNotify(): ',
        print VARname,
        print 'changed to ',
        print ageSDL[VARname][0]
        isThermal = 0
        isField = 0
        if (VARname == SDLThermalValveW1.value):
            boolThermalValveW1 = ageSDL[VARname][0]
            isThermal = 1
        elif (VARname == SDLThermalValveW2.value):
            boolThermalValveW2 = ageSDL[VARname][0]
            isThermal = 2
        elif (VARname == SDLThermalValveW3.value):
            boolThermalValveW3 = ageSDL[VARname][0]
            isThermal = 3
        elif (VARname == SDLThermalValveE1.value):
            boolThermalValveE1 = ageSDL[VARname][0]
            isThermal = 4
        elif (VARname == SDLThermalValveE2.value):
            boolThermalValveE2 = ageSDL[VARname][0]
            isThermal = 5
        elif (VARname == SDLThermalValveE3.value):
            boolThermalValveE3 = ageSDL[VARname][0]
            isThermal = 6
        elif (VARname == SDLHeat.value):
            boolHeat = ageSDL[VARname][0]
            PtAtTimeCallback(self.key, 1.0, 2)
        elif (VARname == SDLFieldValveW1.value):
            isField = 1
            oldFieldValveW1 = byteFieldValveW1
            byteFieldValveW1 = ageSDL[VARname][0]
            self.UpdateField(isField, oldFieldValveW1, byteFieldValveW1)
        elif (VARname == SDLFieldValveW2.value):
            isField = 2
            oldFieldValveW2 = byteFieldValveW2
            byteFieldValveW2 = ageSDL[VARname][0]
            self.UpdateField(isField, oldFieldValveW2, byteFieldValveW2)
        elif (VARname == SDLFieldValveW3.value):
            isField = 3
            oldFieldValveW3 = byteFieldValveW3
            byteFieldValveW3 = ageSDL[VARname][0]
            print 'old value W3 = ',
            print oldFieldValveW3
            print 'newvalue W3 = ',
            print byteFieldValveW3
            self.UpdateField(isField, oldFieldValveW3, byteFieldValveW3)
        elif (VARname == SDLFieldValveE1.value):
            isField = 4
            oldFieldValveE1 = byteFieldValveE1
            byteFieldValveE1 = ageSDL[VARname][0]
            self.UpdateField(isField, oldFieldValveE1, byteFieldValveE1)
        elif (VARname == SDLFieldValveE2.value):
            isField = 5
            oldFieldValveE2 = byteFieldValveE2
            byteFieldValveE2 = ageSDL[VARname][0]
            self.UpdateField(isField, oldFieldValveE2, byteFieldValveE2)
        elif (VARname == SDLFieldValveE3.value):
            isField = 6
            oldFieldValveE3 = byteFieldValveE3
            byteFieldValveE3 = ageSDL[VARname][0]
            self.UpdateField(isField, oldFieldValveE3, byteFieldValveE3)
        if isThermal:
            if ((isThermal > 0) and (isThermal < 4)):
                listWestSDLVals = [boolThermalValveW1, boolThermalValveW2, boolThermalValveW3]
                print 'thgrWaterField.OnSDLNotify(): thermal valve West SDL list = ',
                print listWestSDLVals
                self.UpdateControls('west')
            elif ((isThermal > 3) and (isThermal < 7)):
                listEastSDLVals = [boolThermalValveE1, boolThermalValveE2, boolThermalValveE3]
                print 'thgrWaterField.OnSDLNotify(): thermal valve East SDL list = ',
                print listEastSDLVals
                self.UpdateControls('east')
            isThermal = 0


    def OnNotify(self, state, id, events):
        global PlayerInWater
        global listCurStates
        #global CurField
        if ((id == ActWater.id) and state):
            for event in events:
                if (event[0] == kCollisionEvent):
                    player = PtGetLocalAvatar()
                    if (PtFindAvatar(events) == player and PtWasLocallyNotified(self.key)):
                        if (event[1] == 1):
                            if (not (PlayerInWater)):
                                PlayerInWater = 1
                                #print 'thgrWaterField.OnNotify(): Player has ENTERED the water; will disable \'above\' nodes and enable \'below\' nodes'
                                print 'thgrWaterField.OnNotify(): Player has ENTERED the water'
                                RespInWater.run(self.key, state='in') # I *think* this one mustn't be netforced, because it toggles collisions local to the avatar (to allow climbing out of pond.)
                                #NodeRgnAbove.value.disable()
                                #NodeRgnBelow.value.enable()
                                #if (not (PtIsExpertMode())):
                                #    print 'In novice mode, so we\'ll send player to closest node'
                                #    PtClosestNode()
                        elif PlayerInWater:
                            PlayerInWater = 0
                            #print 'thgrWaterField.OnNotify(): Player has EXITED the water; will disable \'below\' nodes'
                            print 'thgrWaterField.OnNotify(): Player has EXITED the water'
                            if (listCurStates[1] != 'left_on'):
                                RespInWater.run(self.key, state='out_w2ANDw3')
                            elif (listCurStates[2] != 'right_on'):
                                RespInWater.run(self.key, state='out_w3')
                            else:
                                RespInWater.run(self.key, state='out')
                            #NodeRgnBelow.value.disable()
                            #if CurField:
                            #    print '...and as field is still active, will re-enable \'above\' nodes'
                            #    NodeRgnAbove.value.enable()


    def UpdateControls(self, area, onInit = 0):
        global newWest
        global listWestSDLVals
        global boolHeat
        global oldWest
        global westControls
        global oldFieldValveW1
        global byteFieldValveW1
        global oldFieldValveW2
        global byteFieldValveW2
        global oldFieldValveW3
        global byteFieldValveW3
        global newEast
        global listEastSDLVals
        global oldEast
        global eastControls
        global oldFieldValveE1
        global byteFieldValveE1
        global oldFieldValveE2
        global byteFieldValveE2
        global oldFieldValveE3
        global byteFieldValveE3
        if (area == 'west'):
            newWest = 'on'
            for sdl in listWestSDLVals:
                if (not (sdl)):
                    newWest = 'off'
                    break
            if boolHeat:
                newState = newWest
            else:
                newState = 'off'
            if onInit:
                RespWestControls.run(self.key, state=newState, fastforward=1)
                print 'thgrWaterField.UpdateControls(): On init, West field controls are : ',
                print newState
                oldWest = newState
                westControls = newState
            elif (newState != oldWest):
                RespWestControls.run(self.key, state=newState, fastforward=0)
                print 'thgrWaterField.UpdateControls(): West field controls are now: ',
                print newState
                oldWest = newState
                westControls = newState
                self.UpdateField(1, oldFieldValveW1, byteFieldValveW1, 1)
                self.UpdateField(2, oldFieldValveW2, byteFieldValveW2, 1)
                self.UpdateField(3, oldFieldValveW3, byteFieldValveW3, 1)
        elif (area == 'east'):
            newEast = 'on'
            for sdl in listEastSDLVals:
                if (not (sdl)):
                    newEast = 'off'
                    break
            if boolHeat:
                newState = newEast
            else:
                newState = 'off'
            if onInit:
                RespEastControls.run(self.key, state=newState, fastforward=1)
                print 'thgrWaterField.UpdateControls(): On init, East field controls are: ',
                print newState
                oldEast = newState
                eastControls = newState
            elif (newState != oldEast):
                RespEastControls.run(self.key, state=newState, fastforward=0)
                print 'thgrWaterField.UpdateControls(): East field controls are now: ',
                print newState
                oldEast = newState
                eastControls = newState
                self.UpdateField(4, oldFieldValveE1, byteFieldValveE1, 1)
                self.UpdateField(5, oldFieldValveE2, byteFieldValveE2, 1)
                self.UpdateField(6, oldFieldValveE3, byteFieldValveE3, 1)


    def UpdateField(self, field, oldVal, newVal, otherChanged = 0):
        global westControls
        global eastControls
        global listRespWaterField
        global listCurStates
        global byteFieldValveW2
        global byteFieldValveW3
        global byteFieldValveE2
        global byteFieldValveE3
        #global CurField
        field = (field - 1)
        if otherChanged:
            if ((((field == 0) or ((field == 1) or (field == 2))) and (westControls == 'on')) or (((field == 3) or ((field == 4) or (field == 5))) and (eastControls == 'on'))):
                if (newVal == 1):
                    newState = 'left_on'
                elif (newVal == 2):
                    newState = 'right_on'
                else:
                    return
            elif (newVal == 1):
                newState = 'left_off'
            elif (newVal == 2):
                newState = 'right_off'
            else:
                return
        elif ((newVal == 1) and (not (oldVal))):
            newState = 'left_on'
        elif ((newVal == 2) and (not (oldVal))):
            newState = 'right_on'
        elif ((oldVal == 1) and (not (newVal))):
            newState = 'left_off'
        elif ((oldVal == 2) and (not (newVal))):
            newState = 'right_off'
        else:
            return
        if (field == 0):
            if ((newState == 'left_on') or (newState == 'left_off')):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
            elif (newState == 'right_on'):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
                if (byteFieldValveW2 == 1):
                    newState2 = 'left_on'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
                    if (byteFieldValveW3 == 1):
                        newState3 = 'left_on'
                        RespWaterField.run(self.key, objectName=listRespWaterField[(field + 2)], state=newState3)
                        print 'thgrWaterField.UpdateField(): will run',
                        print listRespWaterField[(field + 2)],
                        print 'with state: ',
                        print newState3
                        listCurStates[(field + 2)] = newState3
                    elif (byteFieldValveW3 == 2):
                        newState3 = 'right_on'
                        RespWaterField.run(self.key, objectName=listRespWaterField[(field + 2)], state=newState3)
                        print 'thgrWaterField.UpdateField(): will run',
                        print listRespWaterField[(field + 2)],
                        print 'with state: ',
                        print newState3
                        listCurStates[(field + 2)] = newState3
                elif (byteFieldValveW2 == 2):
                    newState2 = 'right_on'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
            elif (newState == 'right_off'):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
                if (listCurStates[(field + 1)] == 'left_on'):
                    newState2 = 'left_off'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
                    if (listCurStates[(field + 2)] == 'left_on'):
                        newState3 = 'left_off'
                        RespWaterField.run(self.key, objectName=listRespWaterField[(field + 2)], state=newState3)
                        print 'thgrWaterField.UpdateField(): will run',
                        print listRespWaterField[(field + 2)],
                        print 'with state: ',
                        print newState3
                        listCurStates[(field + 2)] = newState3
                    elif (listCurStates[(field + 2)] == 'right_on'):
                        newState3 = 'right_off'
                        RespWaterField.run(self.key, objectName=listRespWaterField[(field + 2)], state=newState3)
                        print 'thgrWaterField.UpdateField(): will run',
                        print listRespWaterField[(field + 2)],
                        print 'with state: ',
                        print newState3
                        listCurStates[(field + 2)] = newState3
                elif (listCurStates[(field + 1)] == 'right_on'):
                    newState2 = 'right_off'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
        elif (field == 1):
            if (listCurStates[0] != 'right_on'):
                return
            if ((newState == 'right_on') or (newState == 'right_off')):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
            elif (newState == 'left_on'):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
                if (byteFieldValveW3 == 1):
                    newState2 = 'left_on'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
                elif (byteFieldValveW3 == 2):
                    newState2 = 'right_on'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
            elif (newState == 'left_off'):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
                if (listCurStates[(field + 1)] == 'left_on'):
                    newState2 = 'left_off'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
                elif (listCurStates[(field + 1)] == 'right_on'):
                    newState2 = 'right_off'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
        elif (field == 2):
            if (listCurStates[1] != 'left_on'):
                return
            RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
            print 'thgrWaterField.UpdateField(): will run',
            print listRespWaterField[field],
            print 'with state: ',
            print newState
            listCurStates[field] = newState
        elif (field == 3):
            if ((newState == 'right_on') or (newState == 'right_off')):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
            elif (newState == 'left_on'):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
                if (byteFieldValveE2 == 1):
                    newState2 = 'left_on'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
                elif (byteFieldValveE2 == 2):
                    newState2 = 'right_on'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
                    if (byteFieldValveE3 == 1):
                        newState3 = 'left_on'
                        RespWaterField.run(self.key, objectName=listRespWaterField[(field + 2)], state=newState3)
                        print 'thgrWaterField.UpdateField(): will run',
                        print listRespWaterField[(field + 2)],
                        print 'with state: ',
                        print newState3
                        listCurStates[(field + 2)] = newState3
                    elif (byteFieldValveE3 == 2):
                        newState3 = 'right_on'
                        RespWaterField.run(self.key, objectName=listRespWaterField[(field + 2)], state=newState3)
                        print 'thgrWaterField.UpdateField(): will run',
                        print listRespWaterField[(field + 2)],
                        print 'with state: ',
                        print newState3
                        listCurStates[(field + 2)] = newState3
            elif (newState == 'left_off'):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
                if (listCurStates[(field + 1)] == 'left_on'):
                    newState2 = 'left_off'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
                elif (listCurStates[(field + 1)] == 'right_on'):
                    newState2 = 'right_off'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
                    if (listCurStates[(field + 2)] == 'left_on'):
                        newState3 = 'left_off'
                        RespWaterField.run(self.key, objectName=listRespWaterField[(field + 2)], state=newState3)
                        print 'thgrWaterField.UpdateField(): will run',
                        print listRespWaterField[(field + 2)],
                        print 'with state: ',
                        print newState3
                        listCurStates[(field + 2)] = newState3
                    elif (listCurStates[(field + 2)] == 'right_on'):
                        newState3 = 'right_off'
                        RespWaterField.run(self.key, objectName=listRespWaterField[(field + 2)], state=newState3)
                        print 'thgrWaterField.UpdateField(): will run',
                        print listRespWaterField[(field + 2)],
                        print 'with state: ',
                        print newState3
                        listCurStates[(field + 2)] = newState3
        elif (field == 4):
            if (listCurStates[3] != 'left_on'):
                return
            if ((newState == 'left_on') or (newState == 'left_off')):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
            elif (newState == 'right_on'):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
                if (byteFieldValveE3 == 1):
                    newState2 = 'left_on'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
                elif (byteFieldValveE3 == 2):
                    newState2 = 'right_on'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
            elif (newState == 'right_off'):
                RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
                print 'thgrWaterField.UpdateField(): will run',
                print listRespWaterField[field],
                print 'with state: ',
                print newState
                listCurStates[field] = newState
                if (listCurStates[(field + 1)] == 'left_on'):
                    newState2 = 'left_off'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
                elif (listCurStates[(field + 1)] == 'right_on'):
                    newState2 = 'right_off'
                    RespWaterField.run(self.key, objectName=listRespWaterField[(field + 1)], state=newState2)
                    print 'thgrWaterField.UpdateField(): will run',
                    print listRespWaterField[(field + 1)],
                    print 'with state: ',
                    print newState2
                    listCurStates[(field + 1)] = newState2
        elif (field == 5):
            if (listCurStates[4] != 'right_on'):
                return
            RespWaterField.run(self.key, objectName=listRespWaterField[field], state=newState)
            print 'thgrWaterField.UpdateField(): will run',
            print listRespWaterField[field],
            print 'with state: ',
            print newState
            listCurStates[field] = newState
        print 'thgrWaterField.UpdateField(): listCurStates = ',
        print listCurStates
        #NewField = 0
        #for state in listCurStates:
        #    if (state[-1:] == 'n'):
        #        NewField = 1
        #        break
        #if (NewField != CurField):
        #    CurField = NewField
            #if CurField:
            #    print 'thgrWaterField.UpdateField(): field is active, so will enable \'above\' nodes'
            #    NodeRgnAbove.value.enable()
            #else:
            #    print 'thgrWaterField.UpdateField(): field is NOT active, so we\'ll make sure the \'above\' nodes are disabled'
            #    NodeRgnAbove.value.disable()


    def OnTimer(self, id):
        #global CurField
        #if (id == 1):
        #    if CurField:
        #        print 'thgrWaterField.OnTimer(): field is active, so will enable \'above\' nodes'
        #        NodeRgnAbove.value.enable()
        #    else:
        #        print 'thgrWaterField.OnTimer(): field is NOT active, so we\'ll make sure the \'above\' nodes are disabled'
        #        NodeRgnAbove.value.disable()
        #elif (id == 2):
        if (id == 2):
            self.UpdateControls('west')
            self.UpdateControls('east')


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



