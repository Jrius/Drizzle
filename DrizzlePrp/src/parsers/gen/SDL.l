
Options in effect for C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/SDL.g:

    ACTION-BLOCK=("./SDL.java","/.","./")

    AST-DIRECTORY=""  AST-TYPE="Ast"  NOATTRIBUTES  NOAUTOMATIC-AST  BACKTRACK
    BYTE  CONFLICTS  
    DAT-DIRECTORY="C:\Documents and Settings\user\Desktop\drizzle\DrizzlePrp\grammars\"
    
    DAT-FILE="C:\Documents and Settings\user\Desktop\drizzle\DrizzlePrp\grammars\SDLdcl.data"
    DCL-FILE="./SDLdcl.java"  NODEBUG  DEF-FILE="./SDLdef.java"  NOEDIT  
    NOERROR-MAPS  ESCAPE='$'  EXPORT-TERMINALS=("./SDLexp.java","","")  
    EXTENDS-PARSETABLE  FACTORY="new "  FILE-PREFIX="SDL"  FIRST  FOLLOW  NOGLR
    NOGOTO-DEFAULT  
    GRM-FILE="C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/SDL.g"
    IMP-FILE="./SDLimp.java"  
    INCLUDE-DIRECTORY="C:\Documents and Settings\user\Desktop\drizzle\DrizzlePrp\grammars\"
    LALR=1  LEGACY  LIST  MARGIN=4  MAX-CASES=1024  NAMES=OPTIMIZED  NT-CHECK  
    OR_MARKER='|'  OUT-DIRECTORY="./"  PACKAGE="parsers.gen"  NOPARENT-SAVE  
    PARSETABLE-INTERFACES="lpg.runtime.ParseTable"  PREFIX="TK_"  PRIORITY  
    PROGRAMMING_LANGUAGE=JAVA  PRS-FILE="./SDLprs.java"  NOQUIET  READ-REDUCE  
    REMAP-TERMINALS  SCOPES  NOSERIALIZE  NOSHIFT-DEFAULT  NOSINGLE-PRODUCTIONS
    NOSOFT-KEYWORDS  STATES  SUFFIX=""  SYM-FILE="./SDLsym.java"  
    TAB-FILE="SDL.t"  TABLE  TEMPLATE="./btParserTemplateDust.gi"  
    TRACE=CONFLICTS  VARIABLES  VERBOSE  NOVISITOR  VISITOR-TYPE="Visitor"  
    WARNINGS  XREF  


Predefined Macros:

    $rule_number
    $rule_text
    $rule_size
    $input_file
    $current_line
    $next_line
    $#identifier
    $symbol_declarations
    $num_rules
    $num_terminals
    $num_nonterminals
    $num_non_terminals
    $num_symbols
    $template
    $file_prefix
    $package
    $ast_package
    $ast_type
    $exp_type
    $prs_type
    $sym_type
    $action_type
    $visitor_type
    $prefix
    $suffix
    $entry_name
    $entry_marker



Defined Symbols:



    $Header
    /.
                    //
                    // Rule $rule_number:  $rule_text
                    //
                    ./


    $BeginAction
    /.$Header$case $rule_number: {
                       //#line2 $next_line "$input_file$"./


    $EndAction
    /.                break;
                    }./


    $BeginJava
    /.$BeginAction
                        $symbol_declarations
                        ./


    $EndJava
    /.$EndAction./


    $NoAction
    /.$Header$case $rule_number:
                        break;./


    $BadAction
    /.$Header$case $rule_number:
                        throw new Error("No action specified for rule " + $rule_number);./


    $NullAction
    /.$Header$case $rule_number:
                        setResult(null);
                        break;./


    $BeginActions
    /.
            public void ruleAction(int ruleNumber)
            {
                switch (ruleNumber)
                {./


    $SplitActions
    /.
    	            default:
    	                ruleAction$rule_number(ruleNumber);
    	                break;
    	        }
    	        return;
    	    }
    	
    	    public void ruleAction$rule_number(int ruleNumber)
    	    {
    	        switch (ruleNumber)
    	        {
                    //#line $next_line "$input_file$"./


    $EndActions
    /.
                    default:
                        break;
                }
                return;
            }./


    $entry_declarations
    /.
            public $ast_class parse$entry_name()
            {
                return parse$entry_name(null, 0);
            }
                
            public $ast_class parse$entry_name(Monitor monitor)
            {
                return parse$entry_name(monitor, 0);
            }
                
            public $ast_class parse$entry_name(int error_repair_count)
            {
                return parse$entry_name(null, error_repair_count);
            }
                
            public $ast_class parse$entry_name(Monitor monitor, int error_repair_count)
            {
                btParser.setMonitor(monitor);
                
                try
                {
                    return ($ast_class) btParser.fuzzyParseEntry($sym_type.$entry_marker, error_repair_count);
                }
                catch (BadParseException e)
                {
                    prsStream.reset(e.error_token); // point to error token
    
                    DiagnoseParser diagnoseParser = new DiagnoseParser(prsStream, prsTable);
                    diagnoseParser.diagnoseEntry($sym_type.$entry_marker, e.error_token);
                }
    
                return null;
            }
        ./


    $additional_interfaces
    /../


    $ast_class
    /.$ast_type./


    $unimplemented_symbols_warning
    /.false./


    $setSym1
    /. // macro setSym1 is deprecated. Use function setResult
                    getParser().setSym1./


    $setResult
    /. // macro setResult is deprecated. Use function setResult
                     getParser().setSym1./


    $getSym
    /. // macro getSym is deprecated. Use function getRhsSym
                  getParser().getSym./


    $getToken
    /. // macro getToken is deprecated. Use function getRhsTokenIndex
                    getParser().getToken./


    $getIToken
    /. // macro getIToken is deprecated. Use function getRhsIToken
                     prsStream.getIToken./


    $getLeftSpan
    /. // macro getLeftSpan is deprecated. Use function getLeftSpan
                       getParser().getFirstToken./


    $getRightSpan
    /. // macro getRightSpan is deprecated. Use function getRightSpan
                        getParser().getLastToken./


Start:

    statedesc_star

Eof:

    EOF_TOKEN

Error:

    ERROR_TOKEN

Terminals:

    STATEDESC
    EOL
    LEFTCURLY
    RIGHTCURLY
    LEFTPARENS
    RIGHTPARENS
    LEFTSQUARE
    RIGHTSQUARE
    EQUALS
    VERSION
    VAR
    DEFAULT
    DISPLAYOPTION
    DEFAULTOPTION
    NAME
    NUMBER
    COMMENT
    EOF_TOKEN
    ERROR_TOKEN


Soft Keywords:



Rules:

0      '$accept' ::= statedesc_star

1      statedesc_star ::= $Empty
2      statedesc_star ::= statedesc_star statedesc
3      statedesc ::= STATEDESC NAME LEFTCURLY VERSION NUMBER varline_star RIGHTCURLY
4      varline_star ::= $Empty
5      varline_star ::= varline_star varline
6      varline ::= VAR NAME NAME LEFTSQUARE NUMBER RIGHTSQUARE option_star
7      option_star ::= $Empty
8      option_star ::= option_star option
9      option ::= DEFAULT EQUALS value
10     option ::= DEFAULTOPTION EQUALS value
11     option ::= DISPLAYOPTION EQUALS value
12     value ::= NUMBER
13     value ::= NAME


RANK RULE
==== ====
1    3     statedesc ::= STATEDESC NAME LEFTCURLY VERSION NUMBER varline_star RIGHTCURLY
2    2     statedesc_star ::= statedesc_star statedesc
3    1     statedesc_star ::=
4    0     $accept ::= statedesc_star
5    6     varline ::= VAR NAME NAME LEFTSQUARE NUMBER RIGHTSQUARE option_star
6    5     varline_star ::= varline_star varline
7    4     varline_star ::=
8    9     option ::= DEFAULT EQUALS value
9    10    option ::= DEFAULTOPTION EQUALS value
10   11    option ::= DISPLAYOPTION EQUALS value
11   8     option_star ::= option_star option
12   7     option_star ::=
13   12    value ::= NUMBER
14   13    value ::= NAME
C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/SDL.g:53:5:53:7:3210:3212: Informative: The terminal EOL is useless.
C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/SDL.g:56:5:56:14:3247:3256: Informative: The terminal LEFTPARENS is useless.
C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/SDL.g:57:5:57:15:3262:3272: Informative: The terminal RIGHTPARENS is useless.
C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/SDL.g:71:5:71:11:3446:3452: Informative: The terminal COMMENT is useless.
C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi:26:5:26:15:607:617: Informative: The terminal ERROR_TOKEN is useless.


 IBM LALR Parser Generator              2.0.17 (May 18 2009)



Cross-reference table:


COMMENT  ==>> 

DEFAULT  ==>> 9 

DEFAULTOPTION  ==>> 10 

DISPLAYOPTION  ==>> 11 

EOL  ==>> 

EQUALS  ==>> 9 10 11 

ERROR_TOKEN  ==>> 

LEFTCURLY  ==>> 3 

LEFTPARENS  ==>> 

LEFTSQUARE  ==>> 6 

NAME  ==>> 3 6 6 13 

NUMBER  ==>> 3 6 12 

RIGHTCURLY  ==>> 3 

RIGHTPARENS  ==>> 

RIGHTSQUARE  ==>> 6 

STATEDESC  ==>> 3 

VAR  ==>> 6 

VERSION  ==>> 3 

option  ==>> 9 10 11 

option_star  ==>> 7 8 

statedesc  ==>> 3 

statedesc_star  ==>> 1 2 

value  ==>> 12 13 

varline  ==>> 6 

varline_star  ==>> 4 5 



 IBM LALR Parser Generator              2.0.17 (May 18 2009)


First map for non-terminals:


'$accept'  ==>> STATEDESC EOF_TOKEN 

statedesc_star  ==>> '$empty' STATEDESC 

statedesc  ==>> STATEDESC 

varline_star  ==>> '$empty' VAR 

varline  ==>> VAR 

option_star  ==>> '$empty' DEFAULT DISPLAYOPTION DEFAULTOPTION 

option  ==>> DEFAULT DISPLAYOPTION DEFAULTOPTION 

value  ==>> NAME NUMBER 


 IBM LALR Parser Generator              2.0.17 (May 18 2009)


Follow Map:


'$accept'  ==>> EOF_TOKEN 

statedesc_star  ==>> STATEDESC EOF_TOKEN 

statedesc  ==>> STATEDESC EOF_TOKEN 

varline_star  ==>> RIGHTCURLY VAR 

varline  ==>> RIGHTCURLY VAR 

option_star  ==>> RIGHTCURLY VAR DEFAULT DISPLAYOPTION DEFAULTOPTION 

option  ==>> RIGHTCURLY VAR DEFAULT DISPLAYOPTION DEFAULTOPTION 

value  ==>> RIGHTCURLY VAR DEFAULT DISPLAYOPTION DEFAULTOPTION 


C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/SDL.g:76:5:76:18:3476:3489: Informative: Grammar is  LALR(1).

Number of Terminals: 19
Number of Nonterminals: 7
Number of Productions: 14
Number of Items: 46
Number of Scopes: 0
Number of States: 21
Number of Shift actions: 17
Number of Goto actions: 3
Number of Shift/Reduce actions: 7
Number of Goto/Reduce actions: 6
Number of Reduce actions: 12
Number of Shift-Reduce conflicts: 0
Number of Reduce-Reduce conflicts: 0


 IBM LALR Parser Generator              2.0.17 (May 18 2009)

Shift STATES: 


STATE 1 ------------------------------------------------------------------------
( )

'$accept' ::= .statedesc_star 

statedesc_star ::= . (1)
statedesc_star ::= .statedesc_star statedesc 

statedesc_star    Goto   2

EOF_TOKEN         Reduce 1
STATEDESC         Reduce 1

Default reduction to rule  1


STATE 2 ------------------------------------------------------------------------
( 1 )

'$accept' ::= statedesc_star . (0)
statedesc_star ::= statedesc_star  .statedesc 

statedesc ::= .STATEDESC NAME LEFTCURLY VERSION NUMBER varline_star RIGHTCURLY 

STATEDESC    Shift  3

statedesc    Gt/Rd  2

EOF_TOKEN    Accept


STATE 3 ------------------------------------------------------------------------
( 2 )

statedesc ::= STATEDESC  .NAME LEFTCURLY VERSION NUMBER varline_star RIGHTCURLY 

NAME    Shift  4



STATE 4 ------------------------------------------------------------------------
( 3 )

statedesc ::= STATEDESC NAME  .LEFTCURLY VERSION NUMBER varline_star RIGHTCURLY 

LEFTCURLY    Shift  5



STATE 5 ------------------------------------------------------------------------
( 4 )

statedesc ::= STATEDESC NAME LEFTCURLY  .VERSION NUMBER varline_star RIGHTCURLY 

VERSION    Shift  6



STATE 6 ------------------------------------------------------------------------
( 5 )

statedesc ::= STATEDESC NAME LEFTCURLY VERSION  .NUMBER varline_star RIGHTCURLY 

NUMBER    Shift  7



STATE 7 ------------------------------------------------------------------------
( 6 )

statedesc ::= STATEDESC NAME LEFTCURLY VERSION NUMBER  .varline_star RIGHTCURLY 

varline_star ::= . (4)
varline_star ::= .varline_star varline 

varline_star    Goto   8

VAR             Reduce 4
RIGHTCURLY      Reduce 4

Default reduction to rule  4


STATE 8 ------------------------------------------------------------------------
( 7 )

statedesc ::= STATEDESC NAME LEFTCURLY VERSION NUMBER varline_star  .RIGHTCURLY 
varline_star ::= varline_star  .varline 

varline ::= .VAR NAME NAME LEFTSQUARE NUMBER RIGHTSQUARE option_star 

RIGHTCURLY    Sh/Rd  3
VAR           Shift  9

varline       Gt/Rd  5



STATE 9 ------------------------------------------------------------------------
( 8 )

varline ::= VAR  .NAME NAME LEFTSQUARE NUMBER RIGHTSQUARE option_star 

NAME    Shift  10



STATE 10 -----------------------------------------------------------------------
( 9 )

varline ::= VAR NAME  .NAME LEFTSQUARE NUMBER RIGHTSQUARE option_star 

NAME    Shift  11



STATE 11 -----------------------------------------------------------------------
( 10 )

varline ::= VAR NAME NAME  .LEFTSQUARE NUMBER RIGHTSQUARE option_star 

LEFTSQUARE    Shift  12



STATE 12 -----------------------------------------------------------------------
( 11 )

varline ::= VAR NAME NAME LEFTSQUARE  .NUMBER RIGHTSQUARE option_star 

NUMBER    Shift  13



STATE 13 -----------------------------------------------------------------------
( 12 )

varline ::= VAR NAME NAME LEFTSQUARE NUMBER  .RIGHTSQUARE option_star 

RIGHTSQUARE    Shift  14



STATE 14 -----------------------------------------------------------------------
( 13 )

varline ::= VAR NAME NAME LEFTSQUARE NUMBER RIGHTSQUARE  .option_star 

option_star ::= . (7)
option_star ::= .option_star option 

option_star      Goto   15

DEFAULTOPTION    Reduce 7
DISPLAYOPTION    Reduce 7
DEFAULT          Reduce 7
VAR              Reduce 7
RIGHTCURLY       Reduce 7

Default reduction to rule  7


STATE 15 -----------------------------------------------------------------------
( 14 )

varline ::= VAR NAME NAME LEFTSQUARE NUMBER RIGHTSQUARE option_star . (6)
option_star ::= option_star  .option 

option ::= .DEFAULT EQUALS value 
option ::= .DEFAULTOPTION EQUALS value 
option ::= .DISPLAYOPTION EQUALS value 

DISPLAYOPTION    Shift  16
DEFAULTOPTION    Shift  17
DEFAULT          Shift  18

option           Gt/Rd  8

VAR              Reduce 6
RIGHTCURLY       Reduce 6

Default reduction to rule  6


STATE 16 -----------------------------------------------------------------------
( 15 )

option ::= DISPLAYOPTION  .EQUALS value 

EQUALS    Shift  19



STATE 17 -----------------------------------------------------------------------
( 15 )

option ::= DEFAULTOPTION  .EQUALS value 

EQUALS    Shift  20



STATE 18 -----------------------------------------------------------------------
( 15 )

option ::= DEFAULT  .EQUALS value 

EQUALS    Shift  21



STATE 19 -----------------------------------------------------------------------
( 16 )

option ::= DISPLAYOPTION EQUALS  .value 

value ::= .NUMBER 
value ::= .NAME 

NAME      Sh/Rd  13
NUMBER    Sh/Rd  12

value     Gt/Rd  11



STATE 20 -----------------------------------------------------------------------
( 17 )

option ::= DEFAULTOPTION EQUALS  .value 

value ::= .NUMBER 
value ::= .NAME 

NAME      Sh/Rd  13
NUMBER    Sh/Rd  12

value     Gt/Rd  10



STATE 21 -----------------------------------------------------------------------
( 18 )

option ::= DEFAULT EQUALS  .value 

value ::= .NUMBER 
value ::= .NAME 

NAME      Sh/Rd  13
NUMBER    Sh/Rd  12

value     Gt/Rd  9


Number of entries in base Action Table: 30
Additional space required for compaction of Action Table: 3.3%

Number of unique terminal states: 19
Number of Shift actions saved by merging: 4
Number of Conflict points saved by merging: 0
Number of Reduce actions saved by merging: 0
Number of Reduce saved by default: 11

Number of entries in Terminal Action Table: 40
Additional space required for compaction of Terminal Table: 0.0%


 IBM LALR Parser Generator              2.0.17 (May 18 2009)


Mapping of new state numbers into original numbers:

   14  ==>>      1
   15  ==>>      2
   17  ==>>      7
   19  ==>>      8
   20  ==>>     14
   22  ==>>     15
   23  ==>>      3
   25  ==>>      4
   27  ==>>     19
   30  ==>>     20
   32  ==>>     21
   34  ==>>      5
   35  ==>>      6
   36  ==>>      9
   37  ==>>     10
   38  ==>>     11
   39  ==>>     12
   40  ==>>     13
   41  ==>>     16
   42  ==>>     17
   43  ==>>     18

Actions in Compressed Tables:
     Number of Shifts: 17
     Number of Shift/Reduces: 3
     Number of conflict points: 0
     Number of Gotos: 3
     Number of Goto/Reduces: 6
     Number of Reduces: 1
     Number of Defaults: 4

Parsing Tables storage:
    Storage required for BASE_CHECK: 52 Bytes
    Storage required for BASE_ACTION: 46 Bytes
    Storage required for TERM_CHECK: 60 Bytes
    Storage required for TERM_ACTION: 41 Bytes
