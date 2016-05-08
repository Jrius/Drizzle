#
#    Drizzle - A general Myst tool.
#    Copyright (C) 2004-2008  Hastin Zylstra, Dustin Bernard.
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
# 

# emacs-mode: -*- python-*-
from Plasma import *
from PlasmaTypes import *
#from PlasmaKITypes import *
#from PlasmaVaultConstants import *
#from PlasmaNetConstants import *

import PlasmaControlKeys
import sys        #used for catching exceptions.
import traceback  #used for catching exceptions.

class TDrizzle(ptModifier,):
    __module__ = __name__

    source = None;  #the xKI ptModifier that we use for receiving events.

    #flymode
    curX = 0.0
    curY = 0.0
    curZ = 0.0
    isForward = 0
    isBackward = 0
    isLeft = 0
    isRight = 0
    isUp = 0
    isDown = 0
    isRotLeft = 0
    isRotRight = 0
    isRun = 0
    isAccel = 0
    lastJumpUp = 0
    isFirstTimer = 1
    isSpecialMode = 0
    isExitMode = 0
    rotate = 0
    kFlyMode = 55
    rotSpeed = 0.10000000000000001
    ySpeed = 1
    xSpeed = 1
    zSpeed = 1
    lastX = 0
    lastY = 0
    lastZ = 0
    lastRot = 0
    #/flymode
    #polltime = 0.10000000000000001
    polltime = 0.03
    defaultRotationSpeed = 0.10000000000000001
    defaultRotationSpeedRun = 0.40000000000000002
    #defaultTranslationAccel = 0.20000000000000001
    defaultTranslationAccel = 0.2
    defaultRotationAccel = 0.02

    #Python file command (Dustin added:)
    lastCommand = -1;
    timeBetweenChecks = 4.0;
    #/Python file command (/Dustin)

    curBook = None;
    ref1 = None;

    #timer ids
    kInit = 2714;
    #kFlyMode is defined above.
    kPythonCommand = 2715;
    #2800 to 2899 are reserved for passing to the external file.


    def __init__(self):
        PtDebugPrint("drizzle: Initialising");
        ptModifier.__init__(self)
        self.id = 14174;  #random
        self.version = 1;
        PtDebugPrint("drizzle: Done initialising");

    def OnInit(self, caller):
        PtDebugPrint("drizzle: activate");
        self.source = caller;
        #key = self.key
        #PtDebugPrint("drizzle: activate:"+key);
        PtAtTimeCallback(caller.key, 10.0, self.kInit);
        PtDebugPrint("drizzle: done activate");

    def OnServerInitComplete(self, caller):
        PtDebugPrint("drizzle: onserverinitcomplete.")
        self.callExternalPythonFile("pythoncommand.txt", 2)

    #xKI was incable of intercepting these
    #def OnAvatarSpawn(self, caller):
    #    PtDebugPrint("drizzle: onavatarspawn.")
    #    self.callExternalPythonFile("pythoncommand.txt", 4)

    #xKI was incable of intercepting these
    #def OnFirstUpdate(self, caller):
    #    pass

    def OnKIMsg(self, caller, command, value):
        PtDebugPrint("drizzle: onkimsg: command="+`command`+" value="+`value`);
        if(command==2):
            if(value >= 424200.0 and value < 424300):
                keystroke = int(value - 424200.0);
                self.callExternalPythonFile("pythoncommand.txt", 1, keystroke);
                return 1; #stop the normal handler.
            else:
                return 0;
        else:
            return 0; #we'll let the normal handler run.

    #def bindKey(self, keycodestring, consolestring):
    #    keymap = ptKeyMap();
    #    keymap.bindKeyToConsoleCommand(keycodestring, consolestring);

    def callExternalPythonFile(self, filename, flag, value=None):
        #Dustin added:
        PtDebugPrint("dustin: timer");
        #PtPrintToScreen("dustin: screen");
        try:
            f = open(filename, "r");
            filecontents = f.read();
            f.close();
            exec(filecontents);
        except Exception, inst:
            cla, exc, trbk = sys.exc_info()
            excName = cla.__name__
            try:
                excArgs = exc.__dict__["args"]
            except KeyError:
                excArgs = "<no args>"
            excTb = traceback.format_tb(trbk, 5) #5 is the max traceback level. It can be changed.
            PtPrintToScreen("Exception occured: name="+`excName`);
            PtPrintToScreen(" msg="+`excArgs`);
            PtPrintToScreen(" trace="+`excTb`);
            PtDebugPrint("Exception occured: name="+`excName`);
            PtDebugPrint(" msg="+`excArgs`);
            PtDebugPrint(" trace="+`excTb`);
            #PtPrintToScreen(inst); #print the exception, doesn't seem to work anyway.
        #PtDebugPrint("dustin: timer end");
        #/Dustin

    #has to be called manually.
    def OnControlKeyEvent(self, caller, controlKey, activeFlag):  #(self, caller, controlKey, activeFlag):
        #global isBackward
        #global isExitMode
        #global lastJumpUp
        #global isRun
        #global isAccel
        #global isRotRight
        #global isForward
        #global isUp
        #global isLeft
        #global isRight
        #global lastRot
        #global isRotLeft
        #global isDown
        #global lastY
        #global lastX
        #global lastZ
        #PtDebugPrint("drizzle: keypress="+str(controlKey));
        #PtPrintToScreen("drizzle: keypress="+str(controlKey));
        if (controlKey == PlasmaControlKeys.kKeyMoveForward):
            if (activeFlag == 1):
                self.isForward = 1
                self.lastY = 1
            else:
                self.lastY = 0
                self.isForward = 0
        elif (controlKey == PlasmaControlKeys.kKeyMoveBackward):
            if (activeFlag == 1):
                self.isBackward = 1
                self.lastY = 2
            else:
                self.lastY = 0
                self.isBackward = 0
        elif (controlKey == PlasmaControlKeys.kKeyStrafeLeft):
            if (activeFlag == 1):
                self.isLeft = 1
                self.lastX = 2
            else:
                self.lastX = 0
                self.isLeft = 0
        elif (controlKey == PlasmaControlKeys.kKeyStrafeRight):
            if (activeFlag == 1):
                self.lastX = 1
                self.isRight = 1
            else:
                self.lastX = 0
                self.isRight = 0
        elif (controlKey == PlasmaControlKeys.kKeyJump):
            if (activeFlag == 1):
                if (self.lastJumpUp == 1):
                    self.lastJumpUp = 0
                    self.isDown = 1
                    self.lastZ = 2
                else:
                    self.lastJumpUp = 1
                    self.isUp = 1
                    self.lastZ = 1
            else:
                self.lastZ = 0
                self.isUp = 0
                self.isDown = 0
        elif (controlKey == PlasmaControlKeys.kKeyRotateLeft):
            if (activeFlag == 1):
                self.isRotLeft = 1
                self.lastRot = 2
            else:
                self.lastRot = 0
                self.isRotLeft = 0
        elif (controlKey == PlasmaControlKeys.kKeyRotateRight):
            if (activeFlag == 1):
                self.lastRot = 1
                self.isRotRight = 1
                self.lastRot = 1
            else:
                self.lastRot = 0
                self.isRotRight = 0
        elif (controlKey == 13):
            if (activeFlag == 1):
                self.isRun = 1
            else:
                self.isRun = 0
        elif (controlKey == 14):
            if (activeFlag == 1):
                self.isAccel = 1
            else:
                self.isAccel = 0
        #elif ((controlKey == PlasmaControlKeys.kKeyExitMode) and (activeFlag == 1)):
        #    self.isExitMode = 1



    #has to be called manually.
    def OnTimer(self, caller, id):   # (self, caller, id):
        #global rotSpeed
        #global isFirstTimer
        #global rotate
        #global ySpeed
        #global MainMatrix
        #global CurrentFadeTick
        #global xSpeed
        #global lastX
        #global xlatVector
        #global isExitMode
        #global FadeMode
        #global lastRot
        #global isSpecialMode
        #global lastY
        #global zSpeed
        #global lastZ
        #PtDebugPrint("drizzle: ontimer");
        update = 0
        avatar = PtGetLocalAvatar()
        if (id == self.kInit):    #initialise
            #if (self.isFirstTimer == 1):
            #    self.isFirstTimer = 0
            PtEnableControlKeyEvents(caller.key)
            self.isSpecialMode = 0
            avatar.physics.enableCollision()
            avatar.physics.suppress(0)
            PtEnableMovementKeys()
            PtAtTimeCallback(caller.key, self.polltime, self.kFlyMode)

            #Python file command
            PtDebugPrint("dustin: hi");
            self.timeBetweenChecks = 4.0;
            self.lastCommand = -1;
            self.callExternalPythonFile("pythoncommand.txt", 3)
            PtAtTimeCallback(caller.key, self.timeBetweenChecks, self.kPythonCommand);
            PtDebugPrint("dustin:hi3");
            #PtPrintToScreen("dustin: hi2");
            #/Python file command
        elif (id == self.kPythonCommand):
            self.callExternalPythonFile("pythoncommand.txt", 0)
            PtAtTimeCallback(caller.key, self.timeBetweenChecks, self.kPythonCommand);
        elif (id >= 2800 and id <=2899):
            self.callExternalPythonFile("pythoncommand.txt", 4, id-2800)
        elif (id == self.kFlyMode):
            if ((self.isForward == 0) and (self.isBackward == 0)):
                self.ySpeed = 1
                self.lastY = 0
            if ((self.isLeft == 0) and (self.isRight == 0)):
                self.xSpeed = 1
                self.lastX = 0
            if ((self.isDown == 0) and (self.isUp == 0)):
                self.zSpeed = 1
                self.lastZ = 0
            if ((self.isRotLeft == 0) and (self.isRotRight == 0)):
                self.rotSpeed = self.defaultRotationSpeed
                self.lastRot = 0
            if (self.isRun == 1):
                runSpeed = 10
                runrotSpeed = self.defaultRotationSpeedRun
            else:
                runSpeed = 0
                runrotSpeed = 0
            if (self.isForward == 1):
                if ((self.lastY == 1) and (self.isAccel == 1)):
                    self.ySpeed = (self.ySpeed + self.defaultTranslationAccel)
                else:
                    self.ySpeed = 1
                xlatVector = ptVector3(0, (-self.ySpeed - runSpeed), 0)
                self.lastY = 1
                update = 1
            if (self.isBackward == 1):
                if ((self.lastY == 2) and (self.isAccel == 1)):
                    self.ySpeed = (self.ySpeed + self.defaultTranslationAccel)
                else:
                    self.ySpeed = 1
                xlatVector = ptVector3(0, (self.ySpeed + runSpeed), 0)
                self.lastY = 2
                update = 1
            if (self.isRight == 1):
                if ((self.lastX == 1) and (self.isAccel == 1)):
                    self.xSpeed = (self.xSpeed + self.defaultTranslationAccel)
                else:
                    self.xSpeed = 1
                xlatVector = ptVector3((-self.xSpeed - runSpeed), 0, 0)
                self.lastX = 1
                update = 1
            if (self.isLeft == 1):
                if ((self.lastX == 2) and (self.isAccel == 1)):
                    self.xSpeed = (self.xSpeed + self.defaultTranslationAccel)
                else:
                    self.xSpeed = 1
                xlatVector = ptVector3((self.xSpeed + runSpeed), 0, 0)
                self.lastX = 2
                update = 1
            if (self.isDown == 1):
                if ((self.lastZ == 2) and (self.isAccel == 1)):
                    self.zSpeed = (self.zSpeed + self.defaultTranslationAccel)
                else:
                    self.zSpeed = 1
                xlatVector = ptVector3(0, 0, (-self.zSpeed - runSpeed))
                self.lastZ = 2
                update = 1
            if (self.isUp == 1):
                if ((self.lastZ == 1) and (self.isAccel == 1)):
                    self.zSpeed = (self.zSpeed + self.defaultTranslationAccel)
                else:
                    self.zSpeed = 1
                xlatVector = ptVector3(0, 0, (self.zSpeed + runSpeed))
                self.lastZ = 1
                update = 1
            if (self.isRotLeft == 1):
                self.rotate = 1
                if ((self.lastRot == 2) and (self.isAccel == 1)):
                    self.rotSpeed = (self.rotSpeed + self.defaultRotationAccel)
                else:
                    self.rotSpeed = self.defaultRotationSpeed
                self.lastRot = 2
                if (update == 1):
                    update = 3
                else:
                    update = 2
            if (self.isRotRight == 1):
                self.rotate = 2
                if ((self.lastRot == 1) and (self.isAccel == 1)):
                    self.rotSpeed = (self.rotSpeed + self.defaultRotationAccel)
                else:
                    self.rotSpeed = self.defaultRotationSpeed
                self.lastRot = 1
                if (update == 1):
                    update = 3
                else:
                    update = 2

            if (self.isExitMode == 1):
                self.isExitMode = 0
                if (self.isSpecialMode == 1):
                    self.isSpecialMode = 0
                    avatar.physics.enableCollision()
                    avatar.physics.suppress(0)
                    PtEnableMovementKeys()
                    PtEnableMouseMovement()
                else:
                    self.isSpecialMode = 1
                    avatar.physics.disableCollision()
                    avatar.physics.suppress(1)
                    PtDisableMovementKeys()
                    PtEnableMouseMovement()
                    xlatVector = ptVector3(0, 0, 0)
                    update = 1
            if ((self.isSpecialMode == 1) and (update >= 1)):
                MainMatrix = avatar.getLocalToWorld()
                rotateMatrix = avatar.getLocalToWorld()
                xlatMatrix = avatar.getLocalToWorld()
                if (update == 1):
                    xlatMatrix.makeTranslateMat(xlatVector)
                    avatar.physics.warp((MainMatrix * xlatMatrix))
                elif (update == 2):
                    if (self.rotate == 1):
                        rotateMatrix.makeRotateMat(2, (-self.rotSpeed - runrotSpeed))
                    elif (self.rotate == 2):
                        rotateMatrix.makeRotateMat(2, (self.rotSpeed + runrotSpeed))
                    avatar.physics.warp((MainMatrix * rotateMatrix))
                    self.rotate = 0
                elif (update == 3):
                    if (self.rotate == 1):
                        rotateMatrix.makeRotateMat(2, (-self.rotSpeed - runrotSpeed))
                    elif (self.rotate == 2):
                        rotateMatrix.makeRotateMat(2, (self.rotSpeed + runrotSpeed))
                    xlatMatrix.makeTranslateMat(xlatVector)
                    avatar.physics.warp(((MainMatrix * rotateMatrix) * xlatMatrix))
                    self.rotate = 0
            PtAtTimeCallback(caller.key, self.polltime, self.kFlyMode)



drizzle = TDrizzle();








# local variables:
# tab-width: 4
