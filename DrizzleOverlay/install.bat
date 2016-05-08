@echo off

rem Copyright Dustin Bernard 2008
rem This file is released under the GPLv3, or (at your option) any later version.

echo Make sure Uru is not running and that you have Path of the Shell or Complete Chronicles installed.  Press ctrl-c to quit or any other key to continue.
pause
echo Trying to find POTS folder...
set urupath=null
rem used to check for PrimeToX2.exe

if exist "%ProgramFiles%\ubi soft\cyan worlds\uru - ages beyond myst\UruExplorer.exe" set urupath=%ProgramFiles%\ubi soft\cyan worlds\uru - ages beyond myst\
if exist "%ProgramFiles%\ubi soft\cyan worlds\uru - complete chronicles\UruExplorer.exe" set urupath=%ProgramFiles%\ubi soft\cyan worlds\uru - complete chronicles\
if exist "%ProgramFiles%\ubi soft\cyan worlds\myst uru complete chronicles\UruExplorer.exe" set urupath=%ProgramFiles%\ubi soft\cyan worlds\myst uru complete chronicles\
if exist "c:\program files\ubi soft\cyan worlds\uru - ages beyond myst\UruExplorer.exe" set urupath=c:\program files\ubi soft\cyan worlds\uru - ages beyond myst\
if exist "c:\program files\ubi soft\cyan worlds\uru - complete chronicles\UruExplorer.exe" set urupath=c:\program files\ubi soft\cyan worlds\uru - complete chronicles\
if exist "c:\program files\ubi soft\cyan worlds\myst uru complete chronicles\UruExplorer.exe" set urupath=c:\program files\ubi soft\cyan worlds\myst uru complete chronicles\
if exist "d:\program files\ubi soft\cyan worlds\uru - ages beyond myst\UruExplorer.exe" set urupath=d:\program files\ubi soft\cyan worlds\uru - ages beyond myst\
if exist "d:\program files\ubi soft\cyan worlds\uru - complete chronicles\UruExplorer.exe" set urupath=d:\program files\ubi soft\cyan worlds\uru - complete chronicles\
if exist "d:\program files\ubi soft\cyan worlds\myst uru complete chronicles\UruExplorer.exe" set urupath=d:\program files\ubi soft\cyan worlds\myst uru complete chronicles\
if exist "e:\program files\ubi soft\cyan worlds\uru - ages beyond myst\UruExplorer.exe" set urupath=e:\program files\ubi soft\cyan worlds\uru - ages beyond myst\
if exist "e:\program files\ubi soft\cyan worlds\uru - complete chronicles\UruExplorer.exe" set urupath=e:\program files\ubi soft\cyan worlds\uru - complete chronicles\
if exist "e:\program files\ubi soft\cyan worlds\myst uru complete chronicles\UruExplorer.exe" set urupath=e:\program files\ubi soft\cyan worlds\myst uru complete chronicles\
if exist "h:\program files\ubi soft\cyan worlds\uru - ages beyond myst\UruExplorer.exe" set urupath=h:\program files\ubi soft\cyan worlds\uru - ages beyond myst\
if exist "h:\program files\ubi soft\cyan worlds\uru - complete chronicles\UruExplorer.exe" set urupath=h:\program files\ubi soft\cyan worlds\uru - complete chronicles\
if exist "h:\program files\ubi soft\cyan worlds\myst uru complete chronicles\UruExplorer.exe" set urupath=h:\program files\ubi soft\cyan worlds\myst uru complete chronicles\

if "%urupath%"=="null" goto notfound
echo An installation of Uru was found: %urupath%
echo Press enter if this is ThePathOfTheShell or CompleteChronicles and this is where you want to install, or Ctrl-C to quit.
pause
echo Copying files...
xcopy ..\dat\*.* "%urupath%\dat\*.*"
xcopy ..\Python\*.* "%urupath%\Python\*.*"
xcopy ..\SDL\*.* "%urupath%\SDL\*.*"
xcopy ..\sfx\*.* "%urupath%\sfx\*.*"
mkdir "%urupath%\volatile"
xcopy /e ..\volatile\*.* "%urupath%\volatile\*.*"
xcopy ..\*.* "%urupath%\*.*"
echo All done!
goto done
:notfound
echo Unable to find Pots folder. You'll have to install manually.
goto done
:done
pause