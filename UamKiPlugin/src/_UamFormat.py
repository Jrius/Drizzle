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


def IsVarname(str):
    if len(str)==0:
        return False
    if not (str[0].isalpha() or str[0]=='_'):
        return False
    for l in str:
        if not (l.isalnum() or l=='_'):
            return False
    return True


def IsUamVarname(str):
    if len(str)==0:
        return False
    if not (str[0].isalpha()):
        return False
    for l in str:
        if not (l.isalnum() or l=='_'):
            return False
    return True


        