#
#    Copyright 2005-2010 Dustin Bernard
#
#    This file is part of UruAgeManager/Drizzle.
#
#    UruAgeManager/Drizzle is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    UruAgeManager/Drizzle is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with UruAgeManager/Drizzle.  If not, see <http://www.gnu.org/licenses/>.
#

global _initialized
_initialized = False

import _UamFormat

ModulesLoaded = []
ModulesFailed = []

def Initialize():
    global _initialized
    if not _initialized:
        _initialized = True
        print "_UamModules.Initialize"
        
        #get the text file listing the modules to load and load them
        try:
            f = open("./config/UamModules.txt", "r")
            for line in f:
                line = line.strip()
                print "line:" + line
                if line != "" and (not line.startswith("#")):
                    #this is a module name, so import it
                    if _UamFormat.IsVarname(line):
                        exec("import "+line)
                        ModulesLoaded.append(line)
                    else:
                        print "Error: module name is invalid."
                        ModulesFailed.append(line)
            f.close()
        except:
            import traceback
            traceback.print_exc()
        

            