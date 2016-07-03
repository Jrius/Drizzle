"""An easiest way to set and get SDLs throughout the Ages.
Only works for the Vault version of the SDL.
You should avoid using it in public instances."""

from Plasma import *
from PlasmaConstants import *
import types

class xAgeSDL:

    def __init__(self, requestedAgeName = PtGetAgeName()):
        self.ageSDL = None
        self.ageSDLRecord = None
        vault = ptVault()
        myAges = vault.getAgesIOwnFolder()
        myAges = myAges.getChildNodeRefList()
        for ageInfo in myAges:
            link = ageInfo.getChild()
            link = link.upcastToAgeLinkNode()
            info = link.getAgeInfo()
            if (not (info)):
                continue
            ageName = info.getAgeFilename()
            if (ageName == requestedAgeName):
                self.ageSDL = info.getAgeSDL()
                self.ageSDLRecord = self.ageSDL.getStateDataRecord()
        if (self.ageSDLRecord == None):
            raise RuntimeError, ('Could not find %s SDL !' % requestedAgeName)


    def getVar(self, varname):
        return self.ageSDLRecord.findVar(varname)


    def getValue(self, var, id = 0):
        ssvar = self.getVar(var)
        vartype = ssvar.getType()
        if (vartype == PtSDLVarType.kInt):
            retval = ssvar.getInt(id)
        elif (vartype == PtSDLVarType.kFloat):
            retval = ssvar.getFloat(id)
        elif (vartype == PtSDLVarType.kDouble):
            retval = ssvar.getDouble(id)
        elif (vartype == PtSDLVarType.kBool):
            retval = ssvar.getBool(id)
        elif (vartype == PtSDLVarType.kString32):
            retval = ssvar.getString(id)
        else:
            retval = ssvar.getInt(id)
        return retval


    def setValue(self, var, val, id = 0):
        ssvar = self.getVar(var)
        vartype = ssvar.getType()
        if (vartype == PtSDLVarType.kInt):
            if (type(val) in (types.IntType, types.LongType)):
                ssvar.setInt(val, id)
        elif (vartype == PtSDLVarType.kFloat):
            if (type(val) in (types.IntType, types.LongType, types.FloatType)):
                ssvar.setFloat(val, id)
        elif (vartype == PtSDLVarType.kDouble):
            if (type(val) in (types.IntType, types.LongType, types.FloatType)):
                ssvar.setDouble(val, id)
        elif (vartype == PtSDLVarType.kBool):
            if val:
                ssvar.setBool(1, id)
            else:
                ssvar.setBool(0, id)
        elif (vartype == PtSDLVarType.kString32):
            if (type(val) == type('')):
                ssvar.setString(val, id)
            else:
                ssvar.setString(str(val), id)
        elif (type(val) in (types.IntType, types.LongType)):
            ssvar.setInt(val, id)
        self.ageSDL.setStateDataRecord(self.ageSDLRecord)
        self.ageSDL.save()


    def getCount(self, var):
        i = 0
        while (True):
            try:
                self.getValue(var, i)
            except:
                break
            i += 1
            if (i > 100):
                raise RuntimeError, 'This script failed, there is no way to know the length of a simple state variable'
        return i


    def getSDLs(self):
        return self.ageSDLRecord.getVarList()




