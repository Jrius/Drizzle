# -*- coding: utf-8 -*-
#==============================================================================#
#                                                                              #
#    This is a patched file that was originally written by Cyan Worlds Inc.    #
#    See the file AUTHORS for more info about the contributors of the changes  #
#                                                                              #
#    This program is distributed in the hope that it will be useful,           #
#    but WITHOUT ANY WARRANTY; without even the implied warranty of            #
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                      #
#                                                                              #
#    You may re-use the code in this file within the context of Uru.           #
#                                                                              #
#==============================================================================#
from Plasma import *
from PlasmaTypes import *
import xRandom
import string
objColumns = ptAttribSceneobjectList(1, 'obj: column phys')
animColumn = ptAttribAnimation(2, 'anim: columns', byObject=1)
objSpheres = ptAttribSceneobjectList(3, 'obj: spheres')
objLilBoxes = ptAttribSceneobjectList(4, "obj: lil' boxes")
objBigBoxes = ptAttribSceneobjectList(5, 'obj: big boxes')
objRamps = ptAttribSceneobjectList(6, 'obj: ramps')
objRects = ptAttribSceneobjectList(7, 'obj: rectangles')
warpSpheres = ptAttribSceneobjectList(8, 'obj: sphere warps - home')
warpLilBoxes = ptAttribSceneobjectList(9, "obj: lil' box warps - home")
warpBigBoxes = ptAttribSceneobjectList(10, 'obj: big box warps - home')
warpRamps = ptAttribSceneobjectList(11, 'obj: ramp warps - home')
warpRects = ptAttribSceneobjectList(12, 'obj: rectangle warps - home')
warpWidgetsPlay = ptAttribSceneobjectList(13, 'obj: widget warps - play')
clkColumnUp = ptAttribActivatorList(14, 'clk: column up')
clkColumnDn = ptAttribActivatorList(15, 'clk: column down')
warpPlayers = ptAttribSceneobjectList(16, 'obj: player warps')
respWallToggle = ptAttribResponder(17, 'resp: toggle wall', ['on', 'off'], netForce=0)
respGameOps = ptAttribResponder(18, 'resp: game ops', ['start', 'end', 'reset'], netForce=0)
respSfxColumn = ptAttribResponderList(19, 'resp: sfx column', statelist=['up', 'down', 'off'], byObject=1)
rgnWallSensors = ptAttribActivatorList(20, 'rgn sns: wall hit sensors')
respBulkMoveSFX = ptAttribResponder(22, 'resp: BulkMove SFX', statelist=['off', 'on', 'end'], netForce=0)
respCreateWidgetSFX = ptAttribResponder(23, 'resp: Create Widget SFX', netForce=0)
kInitPos = 9
kMinPos = 0
kMaxPos = 19
LocalAvatar = None
listObjCols = []
byteColumns = []
listSfxCols = []
kMaxEachWidget = 4
kSphere = 'Sphere'
kLilBox = 'LilBox'
kBigBox = 'BigBox'
kRamp = 'Ramp'
kRect = 'Rect'
kEmoteTimerID = 500
kSFXCompleteID = 501
kEmoteTimer = 2.0
kBulkMoveVar = 'BulkMove'
sdlStartPt = 'jlakPlayerStartPt'
sdlWall = 'jlakForceField'
sdlSphere = 'jlakCurrentSphere'
sdlLilBox = 'jlakCurrentLilBox'
sdlBigBox = 'jlakCurrentBigBox'
sdlRamp = 'jlakCurrentRamp'
sdlRect = 'jlakCurrentRectangle'
sdlColumns = ['jlakColumn00',
 'jlakColumn01',
 'jlakColumn02',
 'jlakColumn03',
 'jlakColumn04',
 'jlakColumn05',
 'jlakColumn06',
 'jlakColumn07',
 'jlakColumn08',
 'jlakColumn09',
 'jlakColumn10',
 'jlakColumn11',
 'jlakColumn12',
 'jlakColumn13',
 'jlakColumn14',
 'jlakColumn15',
 'jlakColumn16',
 'jlakColumn17',
 'jlakColumn18',
 'jlakColumn19',
 'jlakColumn20',
 'jlakColumn21',
 'jlakColumn22',
 'jlakColumn23',
 'jlakColumn24']
sdlGUILock = 'jlakGUIButtonsLocked'
byteStartPt = 0
boolWall = 0
byteSphere = 0
byteLilBox = 0
byteBigBox = 0
byteRamp = 0
byteRect = 0
byteColumns = []
boolGUILock = 0
PendingCol = -1
PendingPos = -1
PendingDir = ''
kZeroed = None
warpWidgets = None
listWarpPlayers = []
doSuppressOnHome = False # true to prevent any movement outside the arena, false to let'em bump on the WidgetsHome collider


class jlakField(ptResponder,):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6005
        self.version = 10
        self.bulkMove = 0



    def OnFirstUpdate(self):
        pass


    def OnServerInitComplete(self):
        global byteRect
        global boolGUILock
        global byteStartPt
        global byteRamp
        global LocalAvatar
        global kZeroed
        global byteLilBox
        global boolWall
        global byteSphere
        global warpWidgets
        global byteBigBox
        LocalAvatar = PtGetLocalAvatar()
        ageSDL = PtGetAgeSDL()
        ageSDL.setFlags(sdlStartPt, 1, 1)
        ageSDL.setFlags(sdlWall, 1, 1)
        ageSDL.setFlags(sdlSphere, 1, 1)
        ageSDL.setFlags(sdlLilBox, 1, 1)
        ageSDL.setFlags(sdlBigBox, 1, 1)
        ageSDL.setFlags(sdlRamp, 1, 1)
        ageSDL.setFlags(sdlRect, 1, 1)
        ageSDL.setFlags(sdlGUILock, 1, 1)
        ageSDL.sendToClients(sdlStartPt)
        ageSDL.sendToClients(sdlWall)
        ageSDL.sendToClients(sdlSphere)
        ageSDL.sendToClients(sdlLilBox)
        ageSDL.sendToClients(sdlBigBox)
        ageSDL.sendToClients(sdlRamp)
        ageSDL.sendToClients(sdlRect)
        ageSDL.sendToClients(sdlGUILock)
        ageSDL.setNotify(self.key, sdlStartPt, 0.0)
        ageSDL.setNotify(self.key, sdlWall, 0.0)
        ageSDL.setNotify(self.key, sdlSphere, 0.0)
        ageSDL.setNotify(self.key, sdlLilBox, 0.0)
        ageSDL.setNotify(self.key, sdlBigBox, 0.0)
        ageSDL.setNotify(self.key, sdlRamp, 0.0)
        ageSDL.setNotify(self.key, sdlRect, 0.0)
        ageSDL.setNotify(self.key, sdlGUILock, 0.0)
        byteStartPt = ageSDL[sdlStartPt][0]
        boolWall = ageSDL[sdlWall][0]
        byteSphere = ageSDL[sdlSphere][0]
        byteLilBox = ageSDL[sdlLilBox][0]
        byteBigBox = ageSDL[sdlBigBox][0]
        byteRamp = ageSDL[sdlRamp][0]
        byteRect = ageSDL[sdlRect][0]
        if (len(PtGetPlayerList()) == 0):
            PtDebugPrint("jlakField.OnServerInitComplete():\tResetting GUI lock as we're the only ones here!")
            ageSDL[sdlGUILock] = (0,)
            boolGUILock = 0
        else:
            boolGUILock = ageSDL[sdlGUILock][0]
        print 'jlakField.OnServerInitComplete():  byteStartPt = ',
        print byteStartPt
        print 'jlakField.OnServerInitComplete():  boolWall = ',
        print boolWall
        print 'jlakField.OnServerInitComplete():  byteSphere = ',
        print byteSphere
        print 'jlakField.OnServerInitComplete():  byteLilBox = ',
        print byteLilBox
        print 'jlakField.OnServerInitComplete():  byteBigBox = ',
        print byteBigBox
        print 'jlakField.OnServerInitComplete():  byteRamp = ',
        print byteRamp
        print 'jlakField.OnServerInitComplete():  byteRect = ',
        print byteRect
        print 'jlakField.OnServerInitComplete():  boolGUILock = ',
        print boolGUILock
        for sdl in sdlColumns:
            ageSDL.setFlags(sdl, 1, 1)
            ageSDL.sendToClients(sdl)
            ageSDL.setNotify(self.key, sdl, 0.0)
            val = ageSDL[sdl][0]
            byteColumns.append(val)

        print 'jlakField.OnServerInitComplete():  byteColumns = ',
        print byteColumns
        kZeroed = ptVector3(0, 0, 0)
        for objCol in objColumns.value:
            obj = objCol.getName()
            listObjCols.append(obj)

        print 'listObjCols = ',
        print listObjCols
        if (len(clkColumnUp.value) != len(listObjCols)):
            print 'error!  not enough up clickables'
        elif (len(clkColumnDn.value) != len(listObjCols)):
            print 'error!  not enough down clickables'
        for resp in respSfxColumn.value:
            aResp = resp.getName()
            listSfxCols.append(aResp)

        for warp in warpPlayers.value:
            pt = warp.getName()
            listWarpPlayers.append(pt)

        if (not len(PtGetPlayerList())):
            print 'jlakField.OnServerInitComplete(): on link-in, am only player here.  Will reset player start point'
            newPoint = byteStartPt
            while (newPoint == byteStartPt):
                newPoint = xRandom.randint(0, (len(listWarpPlayers) - 1))

            byteStartPt = newPoint
            if (byteSphere == len(objSpheres.value)):
                self.WarpWidgetsHome(0)
            if (byteLilBox == len(objLilBoxes.value)):
                self.WarpWidgetsHome(1)
            if (byteBigBox == len(objBigBoxes.value)):
                self.WarpWidgetsHome(2)
            if (byteRamp == len(objRamps.value)):
                self.WarpWidgetsHome(3)
            if (byteRect == len(objRects.value)):
                self.WarpWidgetsHome(4)
        elif (len(PtGetPlayerList()) == 1):
            print 'jlakField.OnServerInitComplete(): on link-in, 1 player already here.  Will warp 2 points away (across from previous point)'
            byteStartPt = (byteStartPt + 2)
            if (byteStartPt == 4):
                byteStartPt = 0
            elif (byteStartPt >= 5):
                byteStartPt = 1
        else:
            print 'jlakField.OnServerInitComplete(): on link-in, 2 or more players already here.  Will warp to next point'
            if (byteStartPt == 0):
                byteStartPt = 2
            elif (byteStartPt == 1):
                byteStartPt = 0
            elif (byteStartPt == 2):
                byteStartPt = 3
            elif (byteStartPt == 3):
                byteStartPt = 1
        print 'jlakField.OnServerInitComplete(): player start point = ',
        print byteStartPt
        onInit = 1
        i = 0
        for pos in byteColumns:
            if (pos != 0):
                self.MoveColumn(i, pos, onInit)
            i += 1

        warpWidgets = warpWidgetsPlay.value[0].getKey()
        warpPt = warpPlayers.value[byteStartPt].getKey()
        LocalAvatar.physics.warpObj(warpPt)
        ageSDL[sdlStartPt] = (byteStartPt,)
        if boolWall:
            respWallToggle.run(self.key, state='on', fastforward=1, netPropagate=0)
            self.DoWallSensors(1)
        else:
            respWallToggle.run(self.key, state='off', fastforward=1, netPropagate=0)
            self.DoWallSensors(0)
        if (PtGetLocalKILevel() < 2):
            print 'jlakField.OnServerInitComplete(): KI level too low, columns will not be clickable'
            for up in clkColumnUp.value:
                up.disable()

            for dn in clkColumnDn.value:
                dn.disable()




    #def WallFix(self):
    #    sdl = PtGetAgeSDL()
    #    doSuppress = (not sdl['jlakForceField'][0])
    #    for direc in ['N',
    #     'E',
    #     'S',
    #     'W']:
    #        PtFindSceneobject(('FieldBounds' + direc), 'Jalak').physics.suppress(doSuppress)




    def OnSDLNotify(self, VARname, SDLname, playerID, tag):
        global byteRect
        global boolGUILock
        global byteStartPt
        global byteRamp
        global byteLilBox
        global boolWall
        global byteSphere
        global byteBigBox
        ageSDL = PtGetAgeSDL()
        PtDebugPrint(('jlakField.OnSDLNotify():\t VARname: %s, SDLname: %s, tag: %s, value: %d' % (VARname,
         SDLname,
         tag,
         ageSDL[VARname][0])))
        if (VARname == sdlStartPt):
            byteStartPt = ageSDL[sdlStartPt][0]
            print 'jlakField.OnSDLNotify(): byteStartPt = ',
            print byteStartPt
        if (VARname == sdlWall):
            boolWall = ageSDL[sdlWall][0]
            print 'jlakField.OnSDLNotify(): boolWall = ',
            print boolWall
            if boolWall:
                respWallToggle.run(self.key, state='on', fastforward=0, netPropagate=0)
                self.DoWallSensors(1)
            else:
                respWallToggle.run(self.key, state='off', fastforward=0, netPropagate=0)
                self.DoWallSensors(0)
        if (VARname in sdlColumns):
            id = sdlColumns.index(VARname)
            col = listObjCols[id]
            oldPos = byteColumns[id]
            newPos = ageSDL[sdlColumns[id]][0]
            diffPos = abs((newPos - oldPos))
            clkColumnUp.value[id].disable()
            clkColumnDn.value[id].disable()
            animColumn.byObject[col].playToTime(newPos)
            byteColumns[id] = newPos
            print ('jlakField.OnSDLNotify(): byteColumns[%d] = %d' % (id,
             newPos))
            if (newPos > oldPos):
                dir = 'up'
            else:
                dir = 'down'
            if (not self.bulkMove):
                respSfxColumn.run(self.key, state=dir, objectName=listSfxCols[id])
            PtAtTimeCallback(self.key, diffPos, id)
        if (VARname == sdlGUILock):
            boolGUILock = ageSDL[sdlGUILock][0]
            print 'jlakField.OnSDLNotify(): boolGUILock = ',
            print boolGUILock
        if (VARname == sdlSphere):
            byteSphere = ageSDL[sdlSphere][0]
            if (byteSphere == len(objSpheres.value)):
                self.WarpWidgetsHome(0)
            else:
                widget = objSpheres.value[byteSphere]
                widgetName = widget.getName()
                warpPt = warpWidgets
                widget.physics.warpObj(warpPt)
                widget.physics.suppress(0)
                widget.physics.damp(1)
                respCreateWidgetSFX.run(self.key)
                print 'jlakField.OnSDLNotify(): dropping: ',
                print widgetName
            print 'jlakField.OnSDLNotify(): byteSphere = ',
            print byteSphere
        if (VARname == sdlLilBox):
            byteLilBox = ageSDL[sdlLilBox][0]
            if (byteLilBox == len(objLilBoxes.value)):
                self.WarpWidgetsHome(1)
            else:
                widget = objLilBoxes.value[byteLilBox]
                widgetName = widget.getName()
                warpPt = warpWidgets
                widget.physics.warpObj(warpPt)
                widget.physics.suppress(0)
                widget.physics.damp(1)
                respCreateWidgetSFX.run(self.key)
                print 'jlakField.OnSDLNotify(): dropping: ',
                print widgetName
            print 'jlakField.OnSDLNotify(): byteLilBox = ',
            print byteLilBox
        if (VARname == sdlBigBox):
            byteBigBox = ageSDL[sdlBigBox][0]
            if (byteBigBox == len(objBigBoxes.value)):
                self.WarpWidgetsHome(2)
            else:
                widget = objBigBoxes.value[byteBigBox]
                widgetName = widget.getName()
                warpPt = warpWidgets
                widget.physics.warpObj(warpPt)
                widget.physics.suppress(0)
                widget.physics.damp(1)
                print 'jlakField.OnSDLNotify(): dropping: ',
                print widgetName
                respCreateWidgetSFX.run(self.key)
            print 'jlakField.OnSDLNotify(): byteBigBox = ',
            print byteBigBox
        if (VARname == sdlRamp):
            byteRamp = ageSDL[sdlRamp][0]
            if (byteRamp == len(objRamps.value)):
                self.WarpWidgetsHome(3)
            else:
                widget = objRamps.value[byteRamp]
                widgetName = widget.getName()
                warpPt = warpWidgets
                widget.physics.warpObj(warpPt)
                widget.physics.suppress(0)
                widget.physics.damp(1)
                respCreateWidgetSFX.run(self.key)
                print 'jlakField.OnSDLNotify(): dropping: ',
                print widgetName
            print 'jlakField.OnSDLNotify(): byteRamp = ',
            print byteRamp
        if (VARname == sdlRect):
            byteRect = ageSDL[sdlRect][0]
            if (byteRect == len(objRects.value)):
                self.WarpWidgetsHome(4)
            else:
                widget = objRects.value[byteRect]
                widgetName = widget.getName()
                warpPt = warpWidgets
                widget.physics.warpObj(warpPt)
                widget.physics.suppress(0)
                widget.physics.damp(1)
                respCreateWidgetSFX.run(self.key)
                print 'jlakField.OnSDLNotify(): dropping: ',
                print widgetName
            print 'jlakField.OnSDLNotify(): byteRect = ',
            print byteRect



    def OnNotify(self, state, id, events):
        ageSDL = PtGetAgeSDL()
        if (((id == clkColumnUp.id) or (id == clkColumnDn.id)) and (state and ((LocalAvatar == PtFindAvatar(events)) and (PtGetLocalKILevel() >= 2)))):
            for event in events:
                if (event[0] == kPickedEvent):
                    xEvent = event[3]
                    theClk = xEvent.getName()
                    print 'jlakField.OnNotify(): pressed: ',
                    print theClk
                    if (id == clkColumnUp.id):
                        direction = 1
                        numClk = string.atoi(theClk[6:])
                    else:
                        direction = 0
                        numClk = string.atoi(theClk[6:])
                    self.CalcPos(numClk, direction)

        elif (id == respBulkMoveSFX.id):
            if (self.sceneobject.isLocallyOwned() and boolGUILock):
                ageSDL[sdlGUILock] = (0,)
        elif (id == -1):
            if (events[0][1] == kBulkMoveVar):
                if (events[0][3] > 0):
                    self.bulkMove = 1
                    if (self.sceneobject.isLocallyOwned() and (not boolGUILock)):
                        print 'jlakField.OnNotify(): bulk-move requested, will ignore GUI bulk-move buttons until done'
                        ageSDL[sdlGUILock] = (1,)
                    respBulkMoveSFX.run(self.key, state='on', netPropagate=0)
                    PtAtTimeCallback(self.key, events[0][3], kSFXCompleteID)
            else:
                print ('incoming event: %s' % events[0][1])
                code = events[0][1]
                print ('playing command: %s' % code)
                exec code



    def CalcPos(self, id, dir):
        global PendingPos
        global PendingCol
        global PendingDir
        PtForceCursorHidden()
        if not PtFirstPerson():
            PtEmoteAvatar('KITap')
        PtAtTimeCallback(self.key, kEmoteTimer, kEmoteTimerID)
        oldPos = byteColumns[id]
        if dir:
            op = 'up'
            if (oldPos < kMaxPos):
                newPos = (oldPos + 1)
            else:
                print 'but already at max'
                return 
        else:
            op = 'down'
            if (oldPos > kMinPos):
                newPos = (oldPos - 1)
            else:
                print 'but already at min'
                return 
        PendingCol = id
        PendingPos = newPos
        PendingDir = op
        PtAtTimeCallback(self.key, 1.0, 100)



    def AutoColumns(self, mode, preset = None):
        print 'jlakField.AutoColumns(): requested all columns move using mode: ',
        print mode
        if boolGUILock:
            print "jlakField.AutoColumns():  but a previous move is currently in-progress... so it's DENIED!"
            return 
        anythingMoved = 0
        if (mode == 0):
            newPos = kMinPos
        elif (mode == 1):
            newPos = kInitPos
        elif (mode == 2):
            newPos = kMaxPos
        elif (mode == 3):
            lowPos = (kInitPos - 5)
            highPos = (kInitPos + 5)
            anythingMoved = 1
        elif (mode == 4):
            lowPos = kMinPos
            highPos = kMaxPos
            anythingMoved = 1
        elif (mode == 5):
            if (preset == None):
                print 'no preset, ignoring'
                return 
            else:
                print 'running preset...'
        i = 0
        maxDist = -1
        newColumns = []
        for col in listObjCols:
            if (mode in (0,
             1,
             2)):
                newColumns.append(newPos)
                if (byteColumns[i] != newPos):
                    maxDist = self.getMaxDist(maxDist, byteColumns[i], newPos)
                    anythingMoved = 1
            elif (mode in (3,
             4)):
                newPos = byteColumns[i]
                while (newPos == byteColumns[i]):
                    newPos = xRandom.randint(lowPos, highPos)

                maxDist = self.getMaxDist(maxDist, byteColumns[i], newPos)
                newColumns.append(newPos)
            elif (mode == 5):
                newPos = preset[i]
                if ((newPos < 0) or (newPos > 19)):
                    newPos = 9
                newColumns.append(newPos)
                if (byteColumns[i] != newPos):
                    maxDist = self.getMaxDist(maxDist, byteColumns[i], newPos)
                    anythingMoved = 1
            i += 1

        if (anythingMoved and (maxDist > 0)):
            notify = ptNotify(self.key)
            notify.clearReceivers()
            notify.addReceiver(self.key)
            notify.netPropagate(1)
            notify.netForce(1)
            notify.setActivate(1.0)
            notify.addVarNumber(kBulkMoveVar, maxDist)
            notify.send()
            i = 0
            for col in listObjCols:
                if (newColumns[i] != byteColumns[i]):
                    self.MoveColumn(i, newColumns[i])
                i += 1




    def getMaxDist(self, maxDist, oldPoint, newPoint):
        dist = abs((newPoint - oldPoint))
        if (dist > maxDist):
            maxDist = dist
        return maxDist



    def MoveColumn(self, id, pos, onInit = 0):
        ageSDL = PtGetAgeSDL()
        oldPos = byteColumns[id]
        col = listObjCols[id]
        if onInit:
            print 'jlakField.MoveColumn(): on init, fastforwarding column ',
            print id,
            print ' from position ',
            print oldPos,
            print ' to ',
            print pos
            animColumn.byObject[col].skipToTime(pos)
        else:
            ageSDL[sdlColumns[id]] = (pos,)



    def DropWidget(self, widget):
        ageSDL = PtGetAgeSDL()
        if (widget == kSphere):
            SphereCur = byteSphere
            if (SphereCur < (len(objSpheres.value) - 1)):
                SphereCur += 1
            else:
                SphereCur = 0
            ageSDL[sdlSphere] = (SphereCur,)
        elif (widget == kLilBox):
            LilBoxCur = byteLilBox
            if (LilBoxCur < (len(objLilBoxes.value) - 1)):
                LilBoxCur += 1
            else:
                LilBoxCur = 0
            ageSDL[sdlLilBox] = (LilBoxCur,)
        elif (widget == kBigBox):
            BigBoxCur = byteBigBox
            if (BigBoxCur < (len(objBigBoxes.value) - 1)):
                BigBoxCur += 1
            else:
                BigBoxCur = 0
            ageSDL[sdlBigBox] = (BigBoxCur,)
        elif (widget == kRamp):
            RampCur = byteRamp
            if (RampCur < (len(objRamps.value) - 1)):
                RampCur += 1
            else:
                RampCur = 0
            ageSDL[sdlRamp] = (RampCur,)
        elif (widget == kRect):
            RectCur = byteRect
            if (RectCur < (len(objRects.value) - 1)):
                RectCur += 1
            else:
                RectCur = 0
            ageSDL[sdlRect] = (RectCur,)



    def ResetWidgets(self):
        ageSDL = PtGetAgeSDL()
        ageSDL[sdlSphere] = (len(objSpheres.value),)
        ageSDL[sdlLilBox] = (len(objLilBoxes.value),)
        ageSDL[sdlBigBox] = (len(objBigBoxes.value),)
        ageSDL[sdlRamp] = (len(objRamps.value),)
        ageSDL[sdlRect] = (len(objRects.value),)



    def WarpWidgetsHome(self, widgetType):
        if (widgetType == 0):
            a = 0
            for widget in objSpheres.value:
                widgetName = widget.getName()
                warpPt = warpSpheres.value[a].getKey()
                widget.physics.warpObj(warpPt)
                widget.physics.damp(1)
                if doSuppressOnHome:
                    widget.physics.suppress(1)
                print 'jlakField.WarpWidgetsHome(): resetting: ',
                print widgetName
                a += 1

        elif (widgetType == 1):
            b = 0
            for widget in objLilBoxes.value:
                widgetName = widget.getName()
                warpPt = warpLilBoxes.value[b].getKey()
                widget.physics.warpObj(warpPt)
                widget.physics.damp(1)
                if doSuppressOnHome:
                    widget.physics.suppress(1)
                print 'jlakField.WarpWidgetsHome(): resetting: ',
                print widgetName
                b += 1

        elif (widgetType == 2):
            c = 0
            for widget in objBigBoxes.value:
                widgetName = widget.getName()
                warpPt = warpBigBoxes.value[c].getKey()
                widget.physics.warpObj(warpPt)
                widget.physics.damp(1)
                if doSuppressOnHome:
                    widget.physics.suppress(1)
                print 'jlakField.WarpWidgetsHome(): resetting: ',
                print widgetName
                c += 1

        elif (widgetType == 3):
            d = 0
            for widget in objRamps.value:
                widgetName = widget.getName()
                warpPt = warpRamps.value[d].getKey()
                widget.physics.warpObj(warpPt)
                widget.physics.damp(1)
                if doSuppressOnHome:
                    widget.physics.suppress(1)
                print 'jlakField.WarpWidgetsHome(): resetting: ',
                print widgetName
                d += 1

        elif (widgetType == 4):
            e = 0
            for widget in objRects.value:
                widgetName = widget.getName()
                warpPt = warpRects.value[e].getKey()
                widget.physics.warpObj(warpPt)
                widget.physics.damp(1)
                if doSuppressOnHome:
                    widget.physics.suppress(1)
                print 'jlakField.WarpWidgetsHome(): resetting: ',
                print widgetName
                e += 1




    def ToggleWall(self):
        print 'jlakField.ToggleWall()'
        ageSDL = PtGetAgeSDL()
        if boolWall:
            ageSDL[sdlWall] = (0,)
        else:
            ageSDL[sdlWall] = (1,)



    def DoWallSensors(self, state):
        print 'jlakField.DoWallSensors()'
        #self.WallFix()
        for rgn in rgnWallSensors.value:
            if state:
                rgn.enable()
            else:
                rgn.disable()




    def OnTimer(self, id):
        global PendingPos
        global PendingCol
        global PendingDir
        ageSDL = PtGetAgeSDL()
        if ((id >= 0) and (id <= 99)):
            respSfxColumn.run(self.key, state='off', objectName=listSfxCols[id])
            clkColumnUp.value[id].enable()
            clkColumnDn.value[id].enable()
        elif (id == 100):
            if ((PendingCol != -1) and ((PendingPos != -1) and (PendingDir != ''))):
                self.MoveColumn(PendingCol, PendingPos, 0)
                PendingCol = -1
                PendingPos = -1
                PendingDir = ''
            else:
                print 'timer callback to do pending column move, but vars are bad...?'
        elif (id == kEmoteTimerID):
            PtForceCursorShown()
        elif (id == kSFXCompleteID):
            self.bulkMove = 0
            respBulkMoveSFX.run(self.key, state='end', netPropagate=0)



    def SaveColumns(self, fName):
        fWrite = file(fName, 'w')
        print ("jlakField.SaveColumns(): writing to presets file '%s'" % fName)
        i = 0
        for pos in byteColumns:
            fWrite.write((str(pos) + '\n'))
            print ('jlakField.SaveColumns():  column: %d at position: %d' % (i,
             pos))
            i += 1

        fWrite.close()



    def LoadColumns(self, fName):
        try:
            fRead = file(fName, 'r')
        except:
            print ("jlakField.LoadColumns():  ERROR!  File '%s' not found, load canceled." % fName)
            return 
        preset = []
        i = 0
        for line in fRead:
            pos = string.atoi(line)
            if ((pos < 0) or (pos > 19)):
                print ('jlakField.LoadColumns():  ERROR!  Column %d has an invalid position of %d, must be an integer between 0 and 19.  Load canceled.' % (i,
                 pos))
                fRead.close()
                return 
            else:
                preset.append(pos)
            i += 1

        if (len(preset) != 25):
            print ('jlakField.LoadColumns():  ERROR!  File contains %d positions, must contain positions for 25 columns, only.  Load canceled.' % len(preset))
        else:
            print ("jlakField.LoadColumns(): reading preset file '%s'" % fName)
            print "this preset's columns = ",
            print preset
            self.AutoColumns(5, preset)
        fRead.close()



    def OnBackdoorMsg(self, target, param):
        print 'jlakField.OnBackdoorMsg()'
        if (target == 'height'):
            param = string.atoi(param)
            if ((param < kMinPos) or (param > kMaxPos)):
                print 'invalid height parameter, must be a # between ',
                print kMinPos,
                print ' and ',
                print kMaxPos
                return 
            newPos = param
            anythingMoved = 0
            i = 0
            for col in listObjCols:
                if (byteColumns[i] != newPos):
                    self.MoveColumn(i, newPos, 0)
                    anythingMoved = 1
                i += 1

            if anythingMoved:
                pass
        elif (target == 'reset'):
            if (param == 'widgets'):
                self.ResetWidgets()
            else:
                print "invalid reset parameter, can only use 'widgets' for now"


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



