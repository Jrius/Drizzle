grammar SDLgrammar;

//options{
//output=AST; //to create ASTs.
//ASTLabelType=SdlTree; //the custom class that tree nodes are.
//}

@header{
//code to be placed at the start of the parser file.
package parsers.gen;
import parsers.sdlparser.*;
import java.util.Vector;
}

@lexer::header{
//code to be placed at the start of the lexer file.
package parsers.gen;
} 

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');

STATEDESC: S T A T E D E S C;
LEFTCURLY: '{';
RIGHTCURLY: '}';
LEFTPARENS: '(';
RIGHTPARENS: ')';
LEFTSQUARE: '[';
RIGHTSQUARE: ']';
EQUALS: '=';
DOLLAR: '$';
COMMA: ',';
SEMICOLON: ';';
VERSION: V E R S I O N;
VAR: V A R;
DEFAULT: D E F A U L T;
DISPLAYOPTION: D I S P L A Y O P T I O N;
DEFAULTOPTION: D E F A U L T O P T I O N;

NAME : ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

NATNUM : '-'? ('0'|(('1'..'9')('0'..'9')*));

floatnum: '-'? NATNUM '.' NATNUM;
//negintnum: '-' NATNUM;
//    EOL





COMMENT
    :   '#' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
//    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

STRING
    //:  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    |  '\'' ( ESC_SEQ | ~('\\'|'\'') )* '\''
    ;

//sdlfile: (statedesc)*;
sdlfile returns [Sdlfile val]: {$val = new Sdlfile();} (statedesc {$val.add($statedesc.val);})*; // { $val =  new Sdlfile($statedesc.val); };
//statedesc : STATEDESC NAME LEFTCURLY VERSION NATNUM varline* RIGHTCURLY;
statedesc returns [Statedesc val]: STATEDESC NAME LEFTCURLY VERSION NATNUM {$val = new Statedesc($NAME.text,Integer.parseInt($NATNUM.text));} (varline {$val.add($varline.val);})* RIGHTCURLY; // -> ^(STATEDESC NAME NATNUM varline* );
//varline : VAR type NAME LEFTSQUARE (NATNUM|) RIGHTSQUARE option* SEMICOLON?;
varline returns [Varline val]: VAR type NAME {$val = new Varline($type.val,$NAME.text);} LEFTSQUARE (NATNUM{$val.setIndex(Integer.parseInt($NATNUM.text));}|) RIGHTSQUARE (option {$val.add($option.val);})* SEMICOLON?;
type returns [Type val]: NAME{$val = new TypeStd($NAME.text);} | DOLLAR NAME {$val = new TypeStatedesc($NAME.text);};
option returns [Option val]: DEFAULT EQUALS value {$val = new OptionDefault($value.val);} | DEFAULTOPTION EQUALS value {$val = new OptionDefaultoption($value.val);} | DISPLAYOPTION EQUALS value {$val = new OptionDisplayoption($value.val);};
value returns [Value val]: NAME {$val = new ValueName($NAME.text);} | NATNUM {$val = new ValueInt(Integer.parseInt($NATNUM.text));} | floatnum {$val = new ValueFloat(Float.parseFloat($floatnum.text));} | STRING  {$val = new ValueString($STRING.text);} | tuple {$val = $tuple.val;};
//tuple: LEFTPARENS tuplelist RIGHTPARENS;
//tuplelist: | value (COMMA value)*;// | value ',' tuplelist;
tuple returns [ValueTuple val]: LEFTPARENS {$val = new ValueTuple();} ( | v1=value {$val.add($v1.val);} (COMMA v2=value {$val.add($v2.val);} )*) RIGHTPARENS;


//CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
//    ;

//ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
//    ;

//INT :	'0'..'9'+
//    ;

//fragment
//EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
//    |   '\\' ('0'..'7') ('0'..'7')
//    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
