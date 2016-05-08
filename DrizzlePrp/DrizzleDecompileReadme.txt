(Supporting a file means that it is absolutely perfectly decompiled.)

support pure demarshalling (no c code)
support disassembly into tokens
start of deflattening and generating of source code.

support binary ops
support print statements
support assignment statements
//support test.py(my testing file)

support for statements
support if statements
support while statements
support try-finally and try-except blocks
support import statements
support integer literals
//support test2.py(my testing file)

support embedded PyCode objects
support classes
start of support for function calls
support tuples
support lists
support string literals
//support __future__.py

cleaned up spacing around classes and methods
remove extraneous return statements
fixed order of tuples and lists and function arguments
support class inheritance
add some paratheses for clarity
support subscripting
detect and include global vars
support shortcircuit 'and' and 'or'
fixed problem with __module__ = __name__ which affects the other decompyle
//support Ahnonay.py

Made checker that will compile original and compile decompiled version, and the disassemble the results and compare to ensure that they are exactly the same in every way that matters.
Separated disassembly from decompilation so they can be done in two separate passes.

added support for keyword arguments in functions
Float literals
//support ahnyIslandHut.py

Inplace operations +=, *=, etc. including for strange designators which must be treated specially due to the nature of inplace operations.
Can write token data straight out, and let us know if this happens when the token has no simple output string associated with it.
//support ahnyKadishDoor.py
//support ahnyKadishHut.py

Support assigning to variables listed in tuple form
//support ahnyLinkBookGUIPopup.py
//support ahnyMaintRoom.pyc
//support ahnySaveCloth.pyc
//AhnySphere01.pyc
//AhnySphere02.pyc
//AhnySphere03.pyc
//AhnySphere04.pyc


Put parentheses around the shortcircuit operators
//ahnyVogondolaRideV2.pyc

More sophisticated pyc comparison
//ahnySphereLinkBookGUIPopup1.pyc
//ahnySphereLinkBookGUIPopup2.pyc
//ahnySphereLinkBookGUIPopup3.pyc
//ahnySphereLinkBookGUIPopup4.pyc
//ahnyVogondolaRide.pyc
//airstream
//ascii

Support name deletion
Support varargs and varkeywords in function definition
//atexit.pyc
//BahroCave
//BahroCave02
//BaronCityOffice

Support exec
Some support for lambda
Support throwing exceptions
Support slice expressions
//Bastion.py

Support anonymous tuples in argument list
Support del attr statements
Better support for weird arguments
//bdb.py

Fixed bug with literal dictionaries
//bhroBahroYeeshaCave.pyc
//bhroStarfieldOrCavern.pyc
//bisect.pyc

Support del fast statements
Figured out cause of trashing: Build_list was conflicting with mk_func when mk_func was made into an expr.  Solution: make the core of mk_func a load_const_make_function instead of 2 separate tokens.  Hacky but it works.
Made automatic efficiency stats
List comprehensions
List comparisons
//calendar.py
//(everything up to cmd)

Better print and function call support
//cmd

Played with demangling names, but it's uneeded
//ConfigParser

Demarshaller support for PyLong
Fixed bug with else clause of for-else statements
//copy

Demarshaller support for PyComplex
//copy_reg

Added del slice statements
Added slice designator statements
//decompyle.py

Support yield statements
//difflib.py
//...

Complete PyLong support
//dis.py
//...

Fixed bug with else clause of while loops
//fpformat
//...

Added string for inplace_and, inplace_or, unary_invert
//gettest.py
//...

Made lambdas use the argument list code from functions.
//inspect.py
//...

Demarshal PyUnicode
Inplace_divide
Escape unicode support
//nxusBookMachine.py
//...

Fixed base class ordering
//pdb.py
//...

Fixed import check.
//shlex.py
//...

Complete print statement
//warnings.py
//...

******************* successfully decompiles all Pots files!!! **********************************

//...

Support assert statements  (up to 119 shift/reduce conflicts)
//calendars
//...

Support load_deref and store_deref  (upped to 130 shift/reduce conflicts)
Support closures (upped to 141 shift/reduce conflicts)
//cgitb.py
//...

Write globals to start of module too.  (shouldn't matter, but keeps pyc identical.)
//doctest.py
//...

Allow closures for class definitions too.  (up to 142 shift/reduce conflicts. Still fast on all files!)
//pydoc.py
//...


********************** successfully decompiles all /Lib files!!! ********************************

Doing various /Lib/whatever files
(The badparse* files are not supposed to be parsable, so they don't compile.)

Add the rest of the Inplace ops.
Demarshal Ellipsis (not that it's used anywhere :P)
Handle Build_slice (not that it's used anywhere either)
Fixed parentheses around lambdas
Fixed parens around unary_ops
//...

Advanced global use detection to pass even test_scopes.py :D
//test_scopes.py
//...

Hack for ** binary expression, because it has high precedence than -, so -2**3 is read as -(2**3)
//test_unary
//...

************************* successfully decompiles the entire Python 2.2 library!!! ********************

Amazingly, __future__.py from Crowthistle, compiled and decompiled as 2.3 worked without a single change except pointing Python22/python.exe to Python23/python.exe

Ran into huge problems with atext.py...
It turns out that Python2.3 optimizes jumps by compositioning them (e.g. a->b->c is replaced with a->c)
Wrote some framework code that moves the LAND statements to the correct places
Handles IF statements and WHILE statements at least a bit.
//atexit.py


bdb.dispatch_line (short-circuits)
bdb.dispatch_call

After a large rewrite of the regeneration code,
//bdb.py

Added detection for whether an 'if' structure is really the inner part of a while loop, or just an if stmt in a loop.
//cmd.py

Added support for while(1) (only different in Python2.3)
//codecs.py

Fixed problem with Except statements not continuing after the correct token. (They were skipping right to the end of the file.)
//compileall.py

Added support for Python2.3's different format for dictionary construction.
//ConfigParser.py

Added support for Python2.3's optimisation of "if(1 and expr):"  (increased shift/reduce conflicts from 142 to 166).
//test3.py

Regeneration: Fixed up bounds for loops
//decompyle.py

Got rid of kwarg and instead made the rule simply read a LOAD_CONST and expr on encountering an EAT_KWARG
This helped efficiency/prevented grinding to a halt.
//filecmp.py

Some more fixes, and support for comparison groups (e.g. a<b<c)
//pyclbr.py

Handle short-circuit and/or when they are not surround by 'if'
//random.py


***************************** successfully decompiles all Crowthistle files!!! ************************

***************************** successfully decompiles all Hexisle files!!!  ****************************

***************************** successfully decompiles all Myst5 files!!! ******************************

***************************** successfully decompiles all MQO files(as of the time of testing)!!! ******

Encountered problems with Moul and redid much of LAND moving routines.

***************************** successfully decompiles all Moul files!!! *******************************



