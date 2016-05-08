"""
Fixed Laki not reanimating.

Fixing bird. Will need new IA. Bird will probably be unable to move (too complicated otherwise)
#"""


from Plasma import *
from PlasmaTypes import *
import string
import time
import xRandom
SpawnBird = ptAttribSceneobject(1, 'obj: bird spawner')
Act1Bird = ptAttribActivator(2, 'rgn sns: bird trigger 1')
Warp1Bird = ptAttribSceneobject(3, 'obj: bird warp 1')
Act2Bird = ptAttribActivator(4, 'rgn sns: bird trigger 2')
Warp2Bird = ptAttribSceneobject(5, 'obj: bird warp 2')
Act3Bird = ptAttribActivator(6, 'rgn sns: bird trigger 3')
Warp3Bird = ptAttribSceneobject(7, 'obj: bird warp 3')
Act4Bird = ptAttribActivator(8, 'rgn sns: bird trigger 4')
Warp4Bird = ptAttribSceneobject(9, 'obj: bird warp 4')
listPaths = ['path1', 'path2', 'path3']
listBehs = ['beh1', 'beh2', 'beh3']
respFishPath1 = ptAttribResponder(20, 'resp: fish path 1', listPaths)
respFishPath2 = ptAttribResponder(21, 'resp: fish path 2', listPaths)
respFishPath3 = ptAttribResponder(22, 'resp: fish path 3', listPaths)
RespFishPaths = (respFishPath1, respFishPath2, respFishPath3)
respFishBeh1 = ptAttribResponder(23, 'resp: fish behs 1', listBehs)
respFishBeh2 = ptAttribResponder(24, 'resp: fish behs 2', listBehs)
respFishBeh3 = ptAttribResponder(25, 'resp: fish behs 3', listBehs)
RespFishBehs = (respFishBeh1, respFishBeh2, respFishBeh3)
isBirdSpawned = 0
soBird = ptAttribSceneobject(26, 'obj: real bird handle')
birdAnims = {}

chompId = 27
idleId = 28
swallowId = 29
vocalizeId = 30
walkId = 31

birdAnims["chomp"]      = ptAttribResponder(chompId, 'resp: bird chomp (aka Birdy miam-miam !)', netForce=1) # PirBirdChomp
birdAnims["idle"]       = ptAttribResponder(idleId, 'resp: bird idle (aka uhh, why am I here exactly ?)', netForce=1) # PirBirdIdle
birdAnims["swallow"]    = ptAttribResponder(swallowId, 'resp: bird swallow (aka hummm tastes good)', netForce=1) # PirBirdSwallow
birdAnims["vocalize"]   = ptAttribResponder(vocalizeId, 'resp: bird vocalize (aka toocloseBLAAAAARH!)', netForce=1) # PirBirdVocalize
birdAnims["walk"]       = ptAttribResponder(walkId, 'resp: bird walk (aka stayawayHALPZ!)', ["run", "reset"], netForce=1) # PirBirdWalk
birdRegion = -1
birdBrain = None

class lakiCreatures(ptResponder):


    def __init__(self):
        ptResponder.__init__(self)
        self.id = 6370
        version = 4
        self.version = version
        print '__init__lakiCreatures v. ',
        print version,
        print '.0'


    def OnServerInitComplete(self):
        xRandom.seed((PtGetDniTime() % 256))
        x = 1
        for resp in RespFishPaths:
            randPath = xRandom.randint(0, (len(listPaths) - 1))
            initPath = listPaths[randPath]
            print 'lakiCreatures.OnAgeDataInitialized(): fish #',
            print x,
            print ' starts with path: ',
            print initPath
            resp.run(self.key, state=('%s' % initPath))
            x += 1
        x = 1
        for fish in RespFishBehs:
            randBeh = xRandom.randint(0, (len(listBehs) - 1))
            initBeh = listBehs[randBeh]
            print 'lakiCreatures.OnAgeDataInitialized(): fish #',
            print x,
            print ' starts with beh: ',
            print initBeh
            fish.run(self.key, state=('%s' % initBeh))
            x += 1
        
        # force the bird over the network, to make sure it's ok to everyone
        soBird.value.netForce(1)
    
    
    def OnTimer(self, id):
        global birdBrain
        birdBrain.OnTimer(id)


    def OnNotify(self, state, id, events):
        global isBirdSpawned
        global soBird
        global birdRegion
        global birdBrain
        if (not (state)):
            return
        if ((id == Act1Bird.id) or ((id == Act2Bird.id) or ((id == Act3Bird.id) or (id == Act4Bird.id)))):
            
            if PtFindAvatar(events) != PtGetLocalAvatar() or not PtWasLocallyNotified(self.key) or not self.sceneobject.isLocallyOwned():
                return
            
            if (not (isBirdSpawned)):
                print 'lakiCreatures.OnNotify(): some region triggered.  Spawning creature...'
                #soBird = ptSceneobject.getSpawnedNPC(SpawnBird.value)
                soBird = soBird.value
                birdBrain = PirBirdBrain(self.key)
                isBirdSpawned = 1
            if (id == Act1Bird.id):
                if (birdRegion != 1):
                    print 'lakiCreatures.OnNotify(): region 1 triggered.  Warping creature...'
                    soBird.physics.warpObj(Warp1Bird.value.getKey())
                    birdRegion = 1
                    birdBrain.OnRegionChange()
            return # unfortunately other areas are not really good to use with bird, so...
            if (id == Act2Bird.id):
                if (birdRegion != 2):
                    print 'lakiCreatures.OnNotify(): region 2 triggered.  Warping creature...'
                    soBird.physics.warpObj(Warp2Bird.value.getKey())
                    birdRegion = 2
                    birdBrain.OnRegionChange()
            elif (id == Act3Bird.id):
                if (birdRegion != 3):
                    print 'lakiCreatures.OnNotify(): region 3 triggered.  Warping creature...'
                    soBird.physics.warpObj(Warp3Bird.value.getKey())
                    birdRegion = 3
                    birdBrain.OnRegionChange()
            elif (id == Act4Bird.id):
                if (birdRegion != 4):
                    print 'lakiCreatures.OnNotify(): region 4 triggered.  Warping creature...'
                    soBird.physics.warpObj(Warp4Bird.value.getKey())
                    birdRegion = 4
                    birdBrain.OnRegionChange()
        elif (id == respFishPath1.id):
            for event in events:
                if (event[0] == kCallbackEvent):
                    randState = xRandom.randint(0, (len(listPaths) - 1))
                    newState = listPaths[randState]
                    respFishPath1.run(self.key, state=('%s' % newState))
        elif (id == respFishPath2.id):
            for event in events:
                if (event[0] == kCallbackEvent):
                    randState = xRandom.randint(0, (len(listPaths) - 1))
                    newState = listPaths[randState]
                    respFishPath2.run(self.key, state=('%s' % newState))
        elif (id == respFishPath3.id):
            for event in events:
                if (event[0] == kCallbackEvent):
                    randState = xRandom.randint(0, (len(listPaths) - 1))
                    newState = listPaths[randState]
                    respFishPath3.run(self.key, state=('%s' % newState))
        elif (id == respFishBeh1.id):
            for event in events:
                if (event[0] == kCallbackEvent):
                    randState = xRandom.randint(0, 100)
                    if (randState <= 40):
                        newState = 'beh1'
                    elif (randState >= 80):
                        newState = 'beh3'
                    else:
                        newState = 'beh2'
                    respFishBeh1.run(self.key, state=('%s' % newState))
        elif (id == respFishBeh2.id):
            for event in events:
                if (event[0] == kCallbackEvent):
                    randState = xRandom.randint(0, 100)
                    if (randState <= 40):
                        newState = 'beh1'
                    elif (randState >= 80):
                        newState = 'beh3'
                    else:
                        newState = 'beh2'
                    respFishBeh2.run(self.key, state=('%s' % newState))
        elif (id == respFishBeh3.id):
            for event in events:
                if (event[0] == kCallbackEvent):
                    randState = xRandom.randint(0, 100)
                    if (randState <= 40):
                        newState = 'beh1'
                    elif (randState >= 80):
                        newState = 'beh3'
                    else:
                        newState = 'beh2'
                    respFishBeh3.run(self.key, state=('%s' % newState))
        
        elif id == birdAnims["chomp"].id \
            or id == birdAnims["idle"].id \
            or id == birdAnims["swallow"].id \
            or id == birdAnims["vocalize"].id \
            or id == birdAnims["walk"].id:
            print "Got callback from bird. Id =", id
            birdBrain.OnBirdAnimDone(id)


class PirBirdBrain:

    """State of things:
    - won't use walk animation of bird, because it requires warping it around, with lots of multiplayer code so that it avoids
        avatars and obstacles. That's doable, but too much work.
    - bird animations are random
    - bird only spawns in the ribcage of an old Laki. It offers him shelter from those nasty avatars.
    - bird will start shouting when avatars are nearby.
    
    If anyone reading this wants to rescript the whole bird AI and walking behavior, best of luck.
    """
    
    def __init__(self, timerkey):
        self.pos = soBird.position()    # current position for the bird
        self.key = timerkey             # key to use for AtTimeCallback()
        
        self.tooCloseAv = []            # avatars the bird is shouting at
    
    
    def OnBirdAnimDone(self, animid):
        if len(self.tooCloseAv):
            #self.msg("Bird worried, will shout at avatar")
            self.animate("vocalize")
            return
        
        else:
            if animid == chompId:
                # chances are:
                # - swallow (50%)
                # - chomp   (30%)
                # - idle    (15%)
                
                p = xRandom.randint(0, 99)
                
                if p < 60:
                    self.animate("swallow")
                elif p < 85:
                    self.animate("chomp")
                else:
                    self.animate("idle")
            
            elif animid == idleId:
                # chances are:
                # - idle    (50%)
                # - chomp   (30%)
                # - swallow (0%)
                
                p = xRandom.randint(0, 99)
                
                if p < 50:
                    self.animate("idle")
                else:
                    self.animate("chomp")
            
            elif animid == swallowId:
                # chances are:
                # - chomp   (50%)
                # - idle    (30%)
                # - swallow (20%)
                
                p = xRandom.randint(0, 99)
                
                if p < 50:
                    self.animate("chomp")
                elif p < 80:
                    self.animate("idle")
                else:
                    self.animate("swallow")
            
            elif animid == vocalizeId:
                pass
                # chances are:
                # - idle    (%)
                # - chomp   (%)
                # - swallow (%)
                
                #-#-# DO SOMETHING HERE #-#-#
                #self.msg("Vocalizing: uuhh, why would I do that ?")
            
            elif animid == walkId:
                pass
                #self.msg("Bird: Why whould I be walking ???")
    
    def animate(self, action):
        if action == "walk":
            birdAnims[action].run(self.key, "run")
        else:
            birdAnims[action].run(self.key)
        #self.msg("Bird: now doing %s." % action)
    
    def OnRegionChange(self):
        # reset bird here
        self.animate("idle") # reset bird behavior
    
    def OnTimer(self, id):
        pass
    
    #def msg(self, m):
    #    print m
    #    PtSendKIMessage(25, str(m))


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



