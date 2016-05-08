%options template=./btParserTemplateDust.gi
%options Programming_Language=java            --language to output in
%options margin=4
%options backtrack                            --use a backtracking parser, so it accepts ambiguous grammars.
%options prefix=TK_                           --prefix for token names
--%options action-block=("*.java", "/.", "./")  --where to put the actions, and what delimiters mark them
--%options ParseTable=lpg.runtime.ParseTable
%options package=parsers.gen                   --java package to make the generated files a part of
--%options ast_directory=./ast,automatic_ast=toplevel,var=nt,visitor=default
--%options ast_directory=./ast,automatic_ast=toplevel,variables=non_terminals,visitor=default
%options variables=both
%options out_directory=./
%options table    --whether to produce the sym and prs files.
--%options error_maps
%options scopes
%options nt-check
--%options debug
--%options trace=full
%options verbose

--%options glr           --(sets backtrack, lalr=1, single-productions.  But also maintains other ASTs.)
--there are some simple bugs that occur with the generation of Ast.java, but you can fix them.  But setNextAST is never called, so glr must not be in effect yet.

--handy macros:
--$rule_size (the number of items on the rhs of a rule.

----new options  (none of these seem to have much/any effect.  It seems only upping lalr to 2 chance the conflict count and that sped it up a smidge.)
--%options lalr=6    --got rid of a shift-reduce conflict and made a very slight increase in performance.
--%options soft-keywords    --unknown effect
--%options goto-default    --unknow effect
--%options priority
--%options shift-default

--discoveries:
--on shift/reduce conflicts, the order of the rules matters!  E.g. "print x" can be decompiled as two statements (one without a newline and one just with a newline), and if those rules come before the rule with them merged, they will be read as two rules.  So keep the other one on top!
--if getting a parse error that does not make sense, build-all in netbeans, as this can be the problem for some unknown reason, even though netbeans says it is rebuilding that module anyway.
--"illegal respecification of macro $expr" (e.g.) means that "expr" appeared more than once on the rhs of the rule.  Rename some of them with "expr$name" to silence this warning.
--something like "expr ::= expr_loadconst" and "expr_loadconst ::= LOAD_CONST" is a *huge* performance hit.  It completely borked up BUILD_LIST rules to the point where it would grind to a halt due to backtracking while doing longer lists.  Without this rule, it was almost perfect efficiency.
--an "unqualified exec is not allowed in function..." error is probably caused by the __module__=__name__ line which shouldn't be there.
--commenting out the dyn_tuple_expr and dyn_mkfunc lines increased the efficiency of the test from 0.00513 to 0.5, which is awesome!  These are strongly connected components, so they are causing trouble.
--making dyn_mkfunc an expr has a big performance hit and makes it show up in the strongly connected list.


%Terminals

    --regular
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
    --VAULT

    --have inner values:
    NAME
    NUMBER
    COMMENT
    
%End

%Start
    statedesc_star
%End

%Alias
%End

%Define
%End

%Rules

    statedesc_star ::= %empty
    statedesc_star ::= statedesc_star statedesc
    
    statedesc ::= STATEDESC NAME LEFTCURLY VERSION NUMBER varline_star RIGHTCURLY
    
    varline_star ::= %empty
    varline_star ::= varline_star varline
    
    varline ::= VAR NAME NAME LEFTSQUARE NUMBER RIGHTSQUARE option_star
    
    option_star ::= %empty
    option_star ::= option_star option
    
    option ::= DEFAULT EQUALS value
    option ::= DEFAULTOPTION EQUALS value
    option ::= DISPLAYOPTION EQUALS value
    
    value ::= NUMBER
    value ::= NAME

    
    --stmt_plus ::= stmt              /. set(new StmtList(s(1))); ./
    --stmt_plus ::= stmt_plus stmt    /. set(s(1).add(s(2))); ./
    --stmt_star ::= %empty            /. set(new StmtList()); ./
    --stmt_star ::= stmt_star stmt    /. set(s(1).add(s(2))); ./
    
    
    --designator ::= STORE_NAME                   /. set(new Dsgn.Storename(t(1))); ./

    
    --inplace assignments. (inplace means that the object ref *may* stay the same, though the object might be altered.)
    --stmt ::= expr expr$e2 inplace_op designator                                                                                  /. set(new Stmt.Inplace(s(1),s(2),s(3),s(4))); ./       --a += "124"
    --stmt ::= expr DUP_TOP SLICE_0 expr$e2 inplace_op ROT_TWO STORE_SLICE_0                                                       /. set(new Stmt.Inplace(new Dsgn.Slice(s(1),null,null),s(4),s(5))); ./    --/. set(new Stmt.Inplace.Slice(s(1),null,null,s(4),s(5))); ./    --a[:] += "124"
    --stmt ::= expr expr$e2 DUP_TOPX EAT_EXPR EAT_EXPR$e5 SLICE_1 expr$e7 inplace_op ROT_THREE STORE_SLICE_1                       /. set(new Stmt.Inplace(new Dsgn.Slice(s(1),s(2),null),s(7),s(8))); ./    --/. set(new Stmt.Inplace.Slice(s(1),null,s(2),s(7),s(8))); ./        --a[42:] += "124"
    --stmt ::= expr expr$e2 DUP_TOPX EAT_EXPR EAT_EXPR$e5 SLICE_2 expr$e7 inplace_op ROT_THREE STORE_SLICE_2                       /. set(new Stmt.Inplace(new Dsgn.Slice(s(1),null,s(2)),s(7),s(8))); ./    --/. set(new Stmt.Inplace.Slice(s(1),s(2),null,s(7),s(8))); ./        --a[:42] += "124"
    --stmt ::= expr expr$e2 expr$e3 DUP_TOPX EAT_EXPR EAT_EXPR$e6 EAT_EXPR$e7 SLICE_3 expr$e9 inplace_op ROT_FOUR STORE_SLICE_3    /. set(new Stmt.Inplace(new Dsgn.Slice(s(1),s(2),s(3)),s(9),s(10))); ./    --/. set(new Stmt.Inplace.Slice(s(1),s(2),s(3),s(9),s(10))); ./    --a[2:3] += "124"
    --stmt ::= expr DUP_TOP LOAD_ATTR expr$e4 inplace_op ROT_TWO STORE_ATTR                                                        /. set(new Stmt.Inplace(new Dsgn.Attr(s(1),t(7)),s(4),s(5))); ./          --/. set(new Stmt.Inplace.Attr(s(1),s(4),s(5),t(7))); ./          --a.hi += "124"
    --stmt ::= expr expr$e2 DUP_TOPX EAT_EXPR EAT_EXPR$e5 BINARY_SUBSCR expr$e7 inplace_op ROT_THREE STORE_SUBSCR                  /. set(new Stmt.Inplace(new Dsgn.Subscr(s(1),s(2)),s(7),s(8))); ./        --/. set(new Stmt.Inplace.Subscr(s(1),s(2),s(7),s(8))); ./        --a[3] += "124"
    --inplace_op ::= INPLACE_ADD             /. set(t(1)); ./
    --inplace_op ::= INPLACE_SUBTRACT        /. set(t(1)); ./
    
%End
















--Dynamically generated stuff:
