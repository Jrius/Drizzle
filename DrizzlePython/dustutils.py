from Plasma import *
from PlasmaTypes import *


def dustlink(agename, spawnpoint):
    print "Attempting to link..."
    import PlasmaNetConstants
    als = ptAgeLinkStruct()
    ainfo = ptAgeInfoStruct()
    ainfo.setAgeFilename(agename)
    als.setAgeInfo(ainfo)
    als.setLinkingRules(PlasmaNetConstants.PtLinkingRules.kBasicLink)
    spTitle= (('linkname' + agename) + spawnpoint)
    spnpnt = ptSpawnPointInfo(spTitle, spawnpoint)
    als.setSpawnPoint(spnpnt)
    linkMgr = ptNetLinkingMgr()
    linkMgr.linkToAge(als)
    #PtPrintToScreen("Linking...")
    print "Linking..."

def drboGate_OnServerInitComplete(self, respOpenGate):
    #PtPrintToScreen(`respOpenGate`)
    print "opening gate..."
    respOpenGate.run(self.key, state='open', fastforward=1)
    print "gate opened. Removing trouble physicals..."
    PtFindSceneobject("LakiGateExcludeRgn01","Direbo").physics.suppress(1)
    PtFindSceneobject("LakiGateExcludeRgn02","Direbo").physics.suppress(1)
    PtFindSceneobject("SrlnGateExcludeRgn01","Direbo").physics.suppress(1)
    PtFindSceneobject("SrlnGateExcludeRgn02","Direbo").physics.suppress(1)
    PtFindSceneobject("TdlmGateExcludeRgn01","Direbo").physics.suppress(1)
    PtFindSceneobject("TdlmGateExcludeRgn02","Direbo").physics.suppress(1)
    PtFindSceneobject("ThgrGateExcludeRgn01","Direbo").physics.suppress(1)
    PtFindSceneobject("ThgrGateExcludeRgn02","Direbo").physics.suppress(1)
    print "done removing trouble physicals."
    #PtPrintToScreen("done")

def drboPedestal_OnServerInitComplete(self, respSymbolGlow):
    print "responders possible states:" + `respSymbolGlow.state_list`
    respSymbolGlow.run(self.key, state="Keep", fastforward=1)
    respSymbolGlow.run(self.key, state="01", fastforward=1)
    respSymbolGlow.run(self.key, state="02", fastforward=1)
    respSymbolGlow.run(self.key, state="03", fastforward=1)

def drboPedestal_OnNotify(state, id, events, sdlActivePedestals):
    print "onnotify"
    print `state`
    print `id`
    print `events`
    #print `actDict`
    if(state): return

    ap = sdlActivePedestals.value
    agename = ""
    spawnpoint = ""

    if ap=="global.Direbo.LakiActivePedestals": agename = "Laki"
    if ap=="global.Direbo.SrlnActivePedestals": agename = "Siralehn"
    if ap=="global.Direbo.TdlmActivePedestals": agename = "Todelmer"
    if ap=="global.Direbo.ThgrActivePedestals": agename = "Tahgira"
    print "agename:"+`agename`

    if(agename=="Laki"):
        if(id==2): spawnpoint = "LinkInTake"
        if(id==3): spawnpoint = "LinkInLakiKeep"
        if(id==4): spawnpoint = "LinkInPed1"
        if(id==5): spawnpoint = "LinkInArenaPedestal"
        if(id==6): spawnpoint = "LinkInPed3"
    if(agename=="Siralehn"):
        if(id==2): spawnpoint = "LinkInTake"
        if(id==3): spawnpoint = "LinkInSrlnKeep"
        if(id==4): spawnpoint = "LinkInPed1"
        if(id==5): spawnpoint = "LinkInPed2"
        #if(id==6): spawnpoint = "LinkInPed3"
    if(agename=="Tahgira"):
        if(id==2): spawnpoint = "LinkInTake"
        if(id==3): spawnpoint = "LinkInThgrKeep"
        if(id==4): spawnpoint = "LinkInPed1"
        if(id==5): spawnpoint = "LinkInPed2"
        if(id==6): spawnpoint = "LinkInPed3"
    if(agename=="Todelmer"):
        if(id==2): spawnpoint = "LinkInTake"
        if(id==3): spawnpoint = "LinkInTdlmKeep"
        if(id==4): spawnpoint = "LinkInPed1"
        if(id==5): spawnpoint = "LinkInPed2"
        #if(id==6): spawnpoint = "LinkInPed3"
    print "spawnpoint:"+`spawnpoint`

    dustlink(agename,spawnpoint)


