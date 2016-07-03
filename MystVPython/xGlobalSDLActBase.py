#Decompiled with Drizzle30!  Enjoy :)

from PlasmaTypes import *
actTriggerer = ptAttribActivator(1, 'Triggerer')
sdlVar = ptAttribString(2, 'SDL Var')
floatStateLow = ptAttribFloat(3, 'State threshold low', default=0.0, rang=(0.0, 1.0))
floatStateHi = ptAttribFloat(4, 'State threshold hi', default=1.0, rang=(0.0, 1.0))
SubIDStart = 10

def CheckNotify(state, id, events):
    if ((id == actTriggerer.id) and ((state >= floatStateLow.value) and (state <= floatStateHi.value))):
        return 1
    else:
        return 0



