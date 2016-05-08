To recompile a prp file, you also have to play with prputils.compiler.isNormalObjectToBeIncluded to target the objects you want.  The make cross-reference report button can be useful for deciding.

The slowness when starting up is due to the jfilechooser, which has a bug, which may be caused by something I installed in Windows.

Uru's network log file shows that EderDelin.py isn't being loaded.  I think I have to decompile it, then recompile it in the correct version.  I think moul uses python 2.3.3

After adding sounds to pots's /sfx directory, you'll have to run SoundDecompress.exe to decompress them to the StreamingCache folder. (I think moul does this on the fly for any files not found in StreamingCache.

fixing eder delin's python file(It was compiled as 2.3 not 2.2) made the sdl snow thing work, so it removed the snow when going to the age, and said so in python0.elf. (you should be able to remove the ederdelin.pak file to get the snow back)

The camerabrains don't seem to do anything, either because I didn't include the cameramodifier1 objects or because there is no physical mesh to interact with.

tetsonot requires the negilahn python files as well as a few others.

prpexplorer:
if prpexplorer reports that you need to install the directx sdk, you really just need to close the currently open images, then try again.
if prpexplorer is won't open, but only shows up on the task-bar, right-click and choose maximize.

if you get a memory overflow(this can especially happen in Bytedeque), set Java's max heap to a larger number
e.g: -Xmx500m (go to the project's properties, then the run part, and enter "-Xmx500m" in the VM Options box. Substitute 500 for however many megabytes you want.)

objects that are known to be different between pots and moul:
x0002Keyedobject???(maybe) might not exist in moul.
Uruobjectdesc*
PrpHeader*
PrpObjectIndex*
Transmatrix*
Pageid*
Typeid*
ParticleSystem???(maybe)
x000CBoundInterface???(maybe)
plDrawableSpans* (the normals are 1 byte instead of 1 short; there is an extra byte that changes behavior in the element poll)
Objheader*
x003F plHKPhysical is now plPXPhysical representing the change from Havok to PhysX
plAGMasterMod (based on my reverse-engineering.

versions are:
1 realmyst (plasma=1.00)
2 uru prime/uru live/to d'ni/until uru (plasma=2.00, build 63.11)
3 path of the shell/complete chronicles (plasma=2.00, build 63.12)
4 crowthistle (plasma=2.05)
5 myst 5 (plasma=2.10)
6 myst online (episode 9.853 release) (python=2.3.3, I think)
7 hexisle

to keep in mind:
plSynchedObject is seeing some low flags not mentioned on Cobbs, but they don't seem to affect anything.

stability:
to link, you can get away with plSceneNode, plCoordinateInterface, plSpawnPointModifier, plPythonFileMod, plSceneObject, plMipMap, plCubicEnvironMap (probably don't need plMipMap or plCubicEnvironMap)
compiling in drawinterfaces doesn't crash it, even though their references don't exist.
haven't a material that uses an AnimationLayer which you don't have compiled, will crash it. You can replace these materials with another one in the DrawableSpans reader and/or compiler.
to get meshes, you can get away with(a lot of these could probably be dropped too) plLayer, plMaterial, plDrawInterface, plDrawableSpans
to get lights (on your avatar, for instance) omniLightInfo,pointShadowMaster
other lights: plDirectionalLightInfo.
to get sounds: plAudioInterface, plWinAudio, plStereizer, plSoundBuffer, plRandomSoundMod, plWin32StreamingSound, plWin32StaticSound.
sprites: plViewFaceModifier
animation layers: plLayerAnimation, (comment out fixMaterials in the compiler), and a load of plLeafController junk.(which replaces a ton of classes in pots.)
animation: plMasterMod, plATCAnim
//I think this is false: plResponderModifier may crash it, presumeably depending on whether its links are in place, etc.
sometimes, plBoundInterface can crash Uru.
If plCoordinateInterfaces are crashing Uru, it can be caused by not having plFilterCoordInterfaces.  Make sure all CoordinateInterfaces and FilterCoordInterfaces are being exported.
If atcAnims are crashing Uru, you may be able to disable them and still link in, even with all drawinterfaces.

verified working:
plCoordinateInterface, //contains position info for an object.
plSpawnPointModifier, //identifies an object as a spawn point.
plPythonFileMod, //calls a python file function.
plMipMap, //a jpeg/bitmap/dxt image.
plCubicEnvironMap, //a 3-d cube of images.
plLayer, //surface drawing info(colors, blend-kind, refs to MipMaps, CubicEnvironMaps, LayerAnimations)
plMaterial, //a surface material(essentially just a container for plLayers)
plDrawableSpans, //meshes(vertices, surface triangles, material refs, etc)
plOmniLightInfo, //a kind of lamp cast on the avatar (amongst other things?)
plDirectionalLightInfo, //a kind of lamp cast on the avatar (amongst other things?)
plViewFaceModifier, //for sprites, light auras, etc.


troubleshooting:
if prpexplorer throws an exception, even before viewing an object, it may be a problem with either the header,objectindex, or a messed-up typeid(perhaps caused by a missing break statement)
if you link into an age that is completely black, and setting the fog won't even make your avatar appear, check to make sure your .age file is present and good.
if it crashes on linking to an age, and it deletes the prp files for that age, (and may say that it's corrupt), check alcugs to see if the sequence prefix is being used by another age.
if sounddecompress.exe crashes, it may be because you haven't run the game yet.

order I used:
ederdelin :great, a few missing animations, like the light in the alcove. Snow works!
guildpubs :great, missing the imager dynamic text
livebahrocaves :good, animated flames aren't there, so it looks really dark and cold.
tetsonot & extras1.pak & negilahn.pak :bad, I think almost every surface is animated involving lights, so you can only see stuff because of fog. The rain outside works though!
minkata & extras2.pak :good, the night sky animation isn't there though. The extra craters aren't there either; it seems that they are only present in the prp as Physicals, which may somehow be associated with drawspans, I guess. Sparkly doesn't seem to appear(it uses an sdl val in relto, I think). Soccer ball is there, though! As are the hiking boots.
jalak :great, missing blocks(didn't try very hard) and slow to start up, due to animationlayer removals.
payiferen :fair, (I had to change the sequence prefix)missing animated sky and urwin in half-buried in sand, but otherwise okay.
dereno :great, little fishes are there!
negilahn :great, try to find the urwin, monkey, and both fly clouds. Falling leaves work!
edertsogal: good, water is missing, though.

gameplay:
ederdelin: find 7 journey cloths, 2 pages, and 1 linking book, maintainer's marker. Watch it with snow; watch it without.
edertsogal: find 7 journey cloths, 1 page, 1 linking book, maintainer's marker. Watch the moon.
livebahrocaves: check out the planet's surface.
tetsonot: watch the rain. Find the linking book. Check out full day, which is ~15.75 hours.(you can change the system clock to cheat.)
payiferen: find the Urwin. Find the linking book. Check out the full day.
Dereno: find all the different kinds of fish. Find the linking book. Check out the full day.
negilahn :try to find the urwin, monkey, and both fly clouds. Find the page. Watch the leaves. Find the linking book. Check out the full day.
minkata: solve the journal puzzles to find the 5 caves, Visit everything at night. Find the boots and the soccer ball. Watch the suns.
jalak: lower the forcefield and hop accross.
guildpubs: visit all 5!

the pod spiral "works" in all 4 pods, is visible in negilahn and tetsonot, and linkout works in negilahn.

imperfect conversions:
plHKPhysical: can't always convert flags.
plAGMasterMod: drops some info at the end.  I don't know if it's important.