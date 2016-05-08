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

#register for ki notifications
import _UamEvents

#<img src="TextureName" align=center resize=no blend=alpha>  //note that TextureName is the name of the texture without the *0#0.hsm at the end.

text = """
<margin right=30 left=50 top=30>
<font size=18 face=Courier color=221166>
<p align=center>
The Uam KI Plugin!

<font size=16 face=Courier color=221166>
<p align=left>

To show this help at any time, simply type:
/uam

The bookshelf feature lets you set the books you would like.  And visitors to your Relto even see your books!  You can set a book to any fan Age, make it empty, or set it to the default.
/bookshelf 4 Aerie
/bookshelf 4 empty
/bookshelf 4 default

You can reset an Age you're in, perhaps because you would like to play it again, or perhaps because you're playing online and someone has already solved the puzzles there.
/reset

The Relto Page feature lets you have fan-made Relto Pages without slowing down the game if you don't turn them on!  Just flip to the back of your Relto book to find any that are installed.  But you can only change them while on your own Relto!

There are a number of cheat commands.  I recommend that you not use them, as they take away some of the fun.  But here they are for those in a hurry!
/opencleftdoor
/getki
/getmarkers


"""

#class UamModHelp:
#    def UamOnKiCommand(self, command):
def UamOnKiCommand(command):
    print "UamModHelp.UamOnKiCommand: "+`command`
    if command=="/uam":
        #show the book
        import uam
        uam.DisplayJournal(text, True)
        return True
    #if command=="/uamtest 1":
    #    import uam
    #    uam.DisplayLinkingBook("Aerie","LinkInPointDefault")
    #    return True
            
#obj = UamModHelp()
#_UamEvents.RegisterForKiCommand(obj)
_UamEvents.RegisterForKiCommand(UamOnKiCommand)