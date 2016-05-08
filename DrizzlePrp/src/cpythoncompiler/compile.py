#Customizations will be prepended. These vars will be set(e.g.):
#pythonver='2.2'; filename='pyfile.py';

import sys;
import marshal
import __builtin__

#stupid hack because windows defaults to text-mode for stdout. Use -u instead
#if sys.platform == "win32":
#    import os, msvcrt
#    msvcrt.setmode(sys.stdout.fileno(), os.O_BINARY)

#print 'hi';
#print >> sys.stderr, 'ho';

#check version
curver = str(sys.version_info[0])+'.'+str(sys.version_info[1])
if pythonver != curver:
    print >>sys.stderr, 'Python version is wrong.  Wanted '+ pythonver+', but found '+curver
    #sys.exit('Wrong python version.')
    sys.exit(1);

#read
f = sys.stdin
codestring = f.read()
codestring = codestring.replace('\r\n','\n')
codestring = codestring.replace('\r','\n')
f.close()
if codestring and codestring[-1] != '\n':
    codestring = codestring + '\n'

#compile
try:
    codeobject = __builtin__.compile(codestring, filename, 'exec')
    marshal.dump(codeobject, sys.stdout)
    sys.stdout.flush()
except SyntaxError, detail:
    import traceback, sys
    lines = traceback.format_exception_only(SyntaxError, detail)
    for line in lines:
        sys.stderr.write(line.replace('File <string>', 'File "%s"' % filename))
    #sys.exit('Exception during compilation.')
    sys.exit(2)
    #return
#if not cfile:
#    cfile = file + (__debug__ and 'c' or 'o')
#fc = open(cfile, 'wb')
#fc.write('\0\0\0\0')
#wr_long(fc, timestamp)
#marshal.dump(codeobject, fc)
#fc.flush()
#fc.seek(0, 0)
#fc.write(MAGIC)
#fc.close()


