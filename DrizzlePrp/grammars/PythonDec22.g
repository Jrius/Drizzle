%options template=./btParserTemplateDust.gi
%options Programming_Language=java            --language to output in
%options margin=4
%options backtrack                            --use a backtracking parser, so it accepts ambiguous grammars.
%options prefix=TK_                           --prefix for token names
--%options action-block=("*.java", "/.", "./")  --where to put the actions, and what delimiters mark them
--%options ParseTable=lpg.runtime.ParseTable
%options package=pythondec3                   --java package to make the generated files a part of
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

%Globals
    /.
        //import lpg.runtime.*;
        //Dustin added
        import pythondec3.ast.*;
        //Dustin added end
    ./
%End


%Terminals
    --fake tokens
    --EAT_BL
    --EAT_BT
    --EAT_MF
    LAND
    EAT_EXPR
    EAT_KWARG
    ANTIEAT_DESIG
    EAT_CONST
    EAT_LOADCLOSURE
    EXPR_LIST
    LOAD_CONST_MAKE_FUNCTION
    --no arguments
    DUP_TOP
    POP_TOP
    PRINT_ITEM
    PRINT_NEWLINE
    PRINT_ITEM_TO
    PRINT_NEWLINE_TO
    RETURN_VALUE
    ROT_TWO
    ROT_THREE
    ROT_FOUR
    BINARY_ADD
    BINARY_SUBTRACT
    BINARY_MULTIPLY
    BINARY_DIVIDE
    BINARY_TRUE_DIVIDE
    BINARY_FLOOR_DIVIDE
    BINARY_MODULO
    BINARY_LSHIFT
    BINARY_RSHIFT
    BINARY_AND
    BINARY_OR
    BINARY_XOR
    BINARY_POWER
    BINARY_VALUE
    BINARY_SUBSCR
    BUILD_CLASS
    LOAD_LOCALS
    POP_BLOCK
    BREAK_LOOP
    GET_ITER
    END_FINALLY
    IMPORT_STAR
    STORE_SUBSCR
    SLICE_0
    SLICE_1
    SLICE_2
    SLICE_3
    STORE_SLICE_0
    STORE_SLICE_1
    STORE_SLICE_2
    STORE_SLICE_3
    INPLACE_ADD
    INPLACE_SUBTRACT
    INPLACE_MULTIPLY
    INPLACE_DIVIDE
    INPLACE_TRUE_DIVIDE
    INPLACE_FLOOR_DIVIDE
    INPLACE_MODULO
    INPLACE_POWER
    INPLACE_LSHIFT
    INPLACE_RSHIFT
    INPLACE_AND
    INPLACE_XOR
    INPLACE_OR
    UNARY_CONVERT
    UNARY_INVERT
    UNARY_NEGATIVE
    UNARY_NOT
    UNARY_POSITIVE
    DELETE_SUBSCR
    EXEC_STMT
    YIELD_STMT
    DELETE_SLICE_0
    DELETE_SLICE_1
    DELETE_SLICE_2
    DELETE_SLICE_3
    --with arguments
    LOAD_NAME
    STORE_NAME
    LOAD_CONST
    LOAD_GLOBAL
    LOAD_FAST
    STORE_ATTR
    LOAD_ATTR
    COMPARE_OP
    IMPORT_NAME
    IMPORT_FROM
    STORE_GLOBAL
    STORE_FAST
    DELETE_GLOBAL
    DELETE_NAME
    DELETE_ATTR
    DELETE_FAST
    STORE_DEREF
    LOAD_DEREF
    LOAD_CLOSURE
    --dynamic args
    BUILD_LIST
    BUILD_TUPLE
    MAKE_FUNCTION
    CALL_FUNCTION
    CALL_FUNCTION_VAR
    CALL_FUNCTION_KW
    CALL_FUNCTION_VAR_KW
    DUP_TOPX
    UNPACK_SEQUENCE
    RAISE_VARARGS
    --with arguments, but we don't care about them
    JUMP_IF_TRUE
    JUMP_IF_FALSE
    JUMP_FORWARD
    SETUP_LOOP
    JUMP_ABSOLUTE
    FOR_ITER
    FOR_LOOP  --why is this not documented in the python manual?
    SETUP_FINALLY
    SETUP_EXCEPT
    CONTINUE_LOOP
    BUILD_MAP  --argument is always 0
    MAKE_CLOSURE
    BUILD_SLICE
%End

%Start
    stmt_plus
%End

%Alias
%End

%Define
%End

%Rules

    
    stmt_plus ::= stmt              /. set(new StmtList(s(1))); ./
    stmt_plus ::= stmt_plus stmt    /. set(s(1).add(s(2))); ./
    stmt_star ::= %empty            /. set(new StmtList()); ./
    stmt_star ::= stmt_star stmt    /. set(s(1).add(s(2))); ./
    --stmt_star ::= stmt_plus          /. set(s(1)); ./
    
    
    designator ::= STORE_NAME                   /. set(new Dsgn.Storename(t(1))); ./
    designator ::= expr STORE_ATTR              /. set(new Dsgn.Storeattr(s(1),t(2))); ./
    designator ::= STORE_GLOBAL                 /. set(new Dsgn.Storeglobal(t(1))); ./
    designator ::= STORE_FAST                   /. set(new Dsgn.Storefast(t(1))); ./
    designator ::= expr expr$e2 STORE_SUBSCR    /. set(new Dsgn.Subscr(s(1),s(2))); ./
    designator ::= expr STORE_SLICE_0                    /. set(new Dsgn.Slice(s(1),null,null)); ./
    designator ::= expr expr$e2 STORE_SLICE_1            /. set(new Dsgn.Slice(s(1),s(2),null)); ./
    designator ::= expr expr$e2 STORE_SLICE_2            /. set(new Dsgn.Slice(s(1),null,s(2))); ./
    designator ::= expr expr$e2 expr$e3 STORE_SLICE_3    /. set(new Dsgn.Slice(s(1),s(2),s(3))); ./
    designator ::= STORE_DEREF                           /. set(new Dsgn.Name(t(1))); ./
    
    expr ::= LOAD_NAME                        /. set(new Expr.Loadname(t(1))); ./
    expr ::= LOAD_CONST                       /. set(new Expr.Loadconst(t(1))); ./
    expr ::= LOAD_GLOBAL                      /. set(new Expr.Loadglobal(t(1))); ./
    expr ::= LOAD_LOCALS                      /. set(new Expr.Loadlocals(t(1))); ./
    expr ::= LOAD_FAST                        /. set(new Expr.Loadfast(t(1))); ./
    expr ::= expr LOAD_ATTR                   /. set(new Expr.Loadattr(s(1),t(2))); ./
    expr ::= expr expr$e2 BINARY_SUBSCR       /. set(new Expr.Binarysubscript(s(1),s(2))); ./
    expr ::= LOAD_DEREF                       /. set(new Expr.Name(t(1))); ./

    
    --inplace assignments. (inplace means that the object ref *may* stay the same, though the object might be altered.)
    stmt ::= expr expr$e2 inplace_op designator                                                                                  /. set(new Stmt.Inplace(s(1),s(2),s(3),s(4))); ./       --a += "124"
    stmt ::= expr DUP_TOP SLICE_0 expr$e2 inplace_op ROT_TWO STORE_SLICE_0                                                       /. set(new Stmt.Inplace(new Dsgn.Slice(s(1),null,null),s(4),s(5))); ./    --/. set(new Stmt.Inplace.Slice(s(1),null,null,s(4),s(5))); ./    --a[:] += "124"
    stmt ::= expr expr$e2 DUP_TOPX EAT_EXPR EAT_EXPR$e5 SLICE_1 expr$e7 inplace_op ROT_THREE STORE_SLICE_1                       /. set(new Stmt.Inplace(new Dsgn.Slice(s(1),s(2),null),s(7),s(8))); ./    --/. set(new Stmt.Inplace.Slice(s(1),null,s(2),s(7),s(8))); ./        --a[42:] += "124"
    stmt ::= expr expr$e2 DUP_TOPX EAT_EXPR EAT_EXPR$e5 SLICE_2 expr$e7 inplace_op ROT_THREE STORE_SLICE_2                       /. set(new Stmt.Inplace(new Dsgn.Slice(s(1),null,s(2)),s(7),s(8))); ./    --/. set(new Stmt.Inplace.Slice(s(1),s(2),null,s(7),s(8))); ./        --a[:42] += "124"
    stmt ::= expr expr$e2 expr$e3 DUP_TOPX EAT_EXPR EAT_EXPR$e6 EAT_EXPR$e7 SLICE_3 expr$e9 inplace_op ROT_FOUR STORE_SLICE_3    /. set(new Stmt.Inplace(new Dsgn.Slice(s(1),s(2),s(3)),s(9),s(10))); ./    --/. set(new Stmt.Inplace.Slice(s(1),s(2),s(3),s(9),s(10))); ./    --a[2:3] += "124"
    stmt ::= expr DUP_TOP LOAD_ATTR expr$e4 inplace_op ROT_TWO STORE_ATTR                                                        /. set(new Stmt.Inplace(new Dsgn.Attr(s(1),t(7)),s(4),s(5))); ./          --/. set(new Stmt.Inplace.Attr(s(1),s(4),s(5),t(7))); ./          --a.hi += "124"
    stmt ::= expr expr$e2 DUP_TOPX EAT_EXPR EAT_EXPR$e5 BINARY_SUBSCR expr$e7 inplace_op ROT_THREE STORE_SUBSCR                  /. set(new Stmt.Inplace(new Dsgn.Subscr(s(1),s(2)),s(7),s(8))); ./        --/. set(new Stmt.Inplace.Subscr(s(1),s(2),s(7),s(8))); ./        --a[3] += "124"
    inplace_op ::= INPLACE_ADD             /. set(t(1)); ./
    inplace_op ::= INPLACE_SUBTRACT        /. set(t(1)); ./
    inplace_op ::= INPLACE_MULTIPLY        /. set(t(1)); ./
    inplace_op ::= INPLACE_DIVIDE          /. set(t(1)); ./
    inplace_op ::= INPLACE_TRUE_DIVIDE     /. set(t(1)); ./
    inplace_op ::= INPLACE_FLOOR_DIVIDE    /. set(t(1)); ./
    inplace_op ::= INPLACE_MODULO          /. set(t(1)); ./
    inplace_op ::= INPLACE_POWER           /. set(t(1)); ./
    inplace_op ::= INPLACE_LSHIFT          /. set(t(1)); ./
    inplace_op ::= INPLACE_RSHIFT          /. set(t(1)); ./
    inplace_op ::= INPLACE_AND             /. set(t(1)); ./
    inplace_op ::= INPLACE_XOR             /. set(t(1)); ./
    inplace_op ::= INPLACE_OR              /. set(t(1)); ./
    

    --class definition
    stmt ::= LOAD_CONST expr dyn_mkfunc CALL_FUNCTION BUILD_CLASS designator  /. set(new Stmt.Classdef(t(1),s(2),s(3),s(6))); ./
    stmt ::= LOAD_CONST expr closure CALL_FUNCTION BUILD_CLASS designator     /. set(new Stmt.Classdef(t(1),s(2),s(3),s(6))); ./

    --function definition
    --it was pretty insane, but it would thrash when allowing dyn_mkfunc to be an expr on its own.  The solution was to make dyn_mkfunc use a single core token: LOAD_CONST_MAKE_FUNCTION and have the token generator place that there.
    expr ::= dyn_mkfunc               /. set(s(1)); ./    --perhaps we don't need this on the expr list for now.
    --stmt ::= dyn_mkfunc designator    /. set(new Stmt.Mkfunc(s(1),s(2))); ./
    
    --slice expressions
    expr ::= expr SLICE_0                    /. set(new Expr.Slice(s(1),null,null)); ./
    expr ::= expr expr$e2 SLICE_1            /. set(new Expr.Slice(s(1),s(2),null)); ./
    expr ::= expr expr$e2 SLICE_2            /. set(new Expr.Slice(s(1),null,s(2))); ./
    expr ::= expr expr$e2 expr$e3 SLICE_3    /. set(new Expr.Slice(s(1),s(2),s(3))); ./
    
    --compare expression  (the list is for e.g. if(a<b<c<d):
    expr ::= expr expr$e2 COMPARE_OP     /. set(new Expr.Compare(s(1),s(2),t(3))); ./
    expr ::= expr cmp_list1 ROT_TWO POP_TOP LAND    /. set(s(2).add(new Expr.Complist.Comp(s(1),null))); ./
    cmp_list1 ::= expr DUP_TOP ROT_THREE COMPARE_OP JUMP_IF_FALSE POP_TOP cmp_list1 LAND    /. set(s(7).add(new Expr.Complist.Comp(s(1),t(4)))); ./
    cmp_list1 ::= expr DUP_TOP ROT_THREE COMPARE_OP JUMP_IF_FALSE POP_TOP cmp_list2 LAND    /. set(s(7).add(new Expr.Complist.Comp(s(1),t(4)))); ./
    cmp_list2 ::= expr COMPARE_OP JUMP_FORWARD    /. set((new Expr.Complist()).add(new Expr.Complist.Comp(s(1),t(2)))); ./
        
    --if statements
    ifcmd ::= JUMP_IF_FALSE           /. set(t(1)); ./
    ifcmd ::= JUMP_IF_TRUE            /. set(t(1)); ./
    --stmt ::= expr$a1 ifcmd$a2 POP_TOP stmt_star$a3 JUMP_FORWARD LAND POP_TOP$e7 stmt_star$a4 LAND$e9    /. set(new Stmt.Ifelse(s($a1),s($a2),s($a3),s($a4))); ./
    stmt ::= expr$a1 ifcmd$a2 POP_TOP stmt_star$a3 jump LAND POP_TOP$e7 stmt_star$a4 LAND$e9    /. set(new Stmt.Ifelse(s($a1),s($a2),s($a3),s($a4))); ./
    --stmt ::= expr$a1 ifcmd$a2 POP_TOP stmt_star$a3 jump LAND POP_TOP$e7 stmt_star$a4 land_plus$e9    /. set(new Stmt.Ifelse(s($a1),s($a2),s($a3),s($a4))); ./
    --stmt ::= expr$a1 ifcmd$a2 POP_TOP stmt_star$a3 jump LAND POP_TOP$e7 stmt_star$a4    /. set(new Stmt.Ifelse(s($a1),s($a2),s($a3),s($a4))); ./

    
    --while statements
    stmt ::= SETUP_LOOP land_star$e2 expr$a1 JUMP_IF_FALSE POP_TOP      stmt_star$a2 JUMP_ABSOLUTE LAND POP_TOP$e8 POP_BLOCK stmt_star$a3 LAND$e11    /. set(new Stmt.Whileelse(s($a1),s($a2),s($a3))); ./    --regular while
    stmt ::= SETUP_LOOP JUMP_FORWARD JUMP_IF_FALSE POP_TOP land_plus$e5 stmt_star$a2 JUMP_ABSOLUTE LAND POP_TOP$e8 POP_BLOCK stmt_star$a3 LAND$e11    /. set(new Stmt.Whileelse(null,s($a2),s($a3))); ./    --while(1):
    
    --break statements
    stmt ::= BREAK_LOOP  /. set(new Stmt.Break()); ./

    --continue statements
    stmt ::= continue_stmt             /. set(s(1)); ./
    continue_stmt ::= JUMP_ABSOLUTE    /. set(new Stmt.Continue()); ./
    continue_stmt ::= CONTINUE_LOOP    /. set(new Stmt.Continue()); ./          --is this not used anymore?
    
    land_plus ::= LAND              /. set(new List(t(1))); ./
    land_plus ::= land_plus LAND    /. set(s(1).add(t(2))); ./
    land_star ::= %empty            /. set(new List()); ./
    land_star ::= land_star LAND    /. set(s(1).add(t(2))); ./
    
    --for statements
	h_looper ::= GET_ITER land_plus FOR_ITER        /. set(new Stmt.Forelse(t(3))); ./
	h_looper ::= LOAD_CONST FOR_LOOP                   /. set(new Stmt.Forelse(t(2))); ./    --this FOR_LOOP opcode might not be used anymore.
    stmt ::= SETUP_LOOP expr h_looper designator stmt_star JUMP_ABSOLUTE LAND$e1 POP_BLOCK stmt_star$e2 LAND$e3    /. set(((Stmt.Forelse)s(3)).setvals(s(2),s(4),s(5),s(9))); ./
    

    --try-catch blocks  ('except_cond' is a conditional except block, 'except' is the catch-all except block)
    remover ::= designator    /. set(s(1)); ./
    remover ::= POP_TOP       /. set(null); ./
    stmt ::= try1_stmt    /. set(s(1)); ./
    stmt ::= try2_stmt    /. set(s(1)); ./
    try2_stmt ::= SETUP_FINALLY stmt_star POP_BLOCK LOAD_CONST LAND stmt_star$e1 END_FINALLY                                           /. set(new Stmt.Try.Tryfinally(s(2),t(4),s(6))); ./
    except_cond ::= DUP_TOP expr$a1 COMPARE_OP$a2 JUMP_IF_FALSE POP_TOP POP_TOP$e1 remover$a3 POP_TOP$e2 stmt_star$a4 jump LAND POP_TOP$e3    /. set(new Stmt.Try.Exceptcond(s($a1),t($a2),s($a3),s($a4))); ./
    except ::= POP_TOP$a POP_TOP$b POP_TOP stmt_star jump    /. set(new Stmt.Try.Except(s(4))); ./    --/. set(s(4)); ./
    
    try1_stmt ::= SETUP_EXCEPT stmt_star POP_BLOCK jump LAND$e5 exceptlist    /. set(((Stmt.Try.Tryexcept2)s(6)).setStmts(s(2))); ./
    exceptlist ::= except exceptlist LAND                                             /. set(s(2).add(s(1))); ./
    exceptlist ::= except_cond exceptlist LAND                                        /. set(s(2).add(s(1))); ./
    exceptlist ::= END_FINALLY LAND stmt_star                                         /. set(new Stmt.Try.Tryexcept2(s(3))); ./

    
    --import
    stmt ::= LOAD_CONST importer                             /. set(new Stmt.Import(t(1),null,s(2),null,1)); ./
    stmt ::= LOAD_CONST IMPORT_NAME IMPORT_STAR              /. set(new Stmt.Import(t(1),t(2),null,null,2)); ./
    stmt ::= LOAD_CONST IMPORT_NAME importer_plus POP_TOP    /. set(new Stmt.Import(t(1),t(2),null,s(3),3)); ./
    importer_plus ::= importer                               /. set(new List(s(1))); ./
    importer_plus ::= importer_plus importer                 /. set(s(1).add(s(2))); ./
    importer ::= IMPORT_NAME designator                      /. set(new Stmt.Import.Importer(t(1),null,s(2))); ./
    importer ::= IMPORT_NAME LOAD_ATTR designator            /. set(new Stmt.Import.Importer(t(1),t(2),s(3))); ./
    importer ::= IMPORT_FROM designator                      /. set(new Stmt.Import.Importer2(t(1),s(2))); ./
    
    --unary expressions
    expr ::= expr UNARY_CONVERT     /. set(new Expr.Unary(s(1),t(2))); ./
    expr ::= expr UNARY_INVERT      /. set(new Expr.Unary(s(1),t(2))); ./
    expr ::= expr UNARY_NEGATIVE    /. set(new Expr.Unary(s(1),t(2))); ./
    expr ::= expr UNARY_NOT         /. set(new Expr.Unary(s(1),t(2))); ./
    expr ::= expr UNARY_POSITIVE    /. set(new Expr.Unary(s(1),t(2))); ./
    
    --del statements
    stmt ::= del_stmt
    del_stmt ::= DELETE_GLOBAL                 /. set(new Stmt.Del(new Dsgn.Name(t(1)))); ./    --/. set(new Stmt.Del.Global(t(1))); ./
    del_stmt ::= expr expr$e2 DELETE_SUBSCR    /. set(new Stmt.Del(new Dsgn.Subscr(s(1),s(2)))); ./
    del_stmt ::= DELETE_NAME                   /. set(new Stmt.Del(new Dsgn.Name(t(1)))); ./
    del_stmt ::= expr DELETE_ATTR              /. set(new Stmt.Del(new Dsgn.Attr(s(1),t(2)))); ./
    del_stmt ::= DELETE_FAST                   /. set(new Stmt.Del(new Dsgn.Name(t(1)))); ./
    del_stmt ::= expr DELETE_SLICE_0                    /. set(new Stmt.Del(new Dsgn.Slice(s(1),null,null))); ./
    del_stmt ::= expr expr$e2 DELETE_SLICE_1            /. set(new Stmt.Del(new Dsgn.Slice(s(1),s(2),null))); ./
    del_stmt ::= expr expr$e2 DELETE_SLICE_2            /. set(new Stmt.Del(new Dsgn.Slice(s(1),null,s(2)))); ./
    del_stmt ::= expr expr$e2 expr$e3 DELETE_SLICE_3    /. set(new Stmt.Del(new Dsgn.Slice(s(1),s(2),s(3)))); ./
    
    --dictionary expressions
    expr ::= BUILD_MAP dictentry_star                            /. set(new Expr.Dictionary(s(2))); ./
    dictentry_star ::= %empty                                    /. set(new List()); ./
    dictentry_star ::= dictentry_star dictentry                  /. set(s(1).add(s(2))); ./
    dictentry ::= DUP_TOP expr ROT_TWO expr$e2 STORE_SUBSCR      /. set(new Expr.Dictionary.Entry(s(2),s(4))); ./    --Python2.2 uses this format.
    dictentry ::= DUP_TOP expr expr$e2 ROT_THREE STORE_SUBSCR    /. set(new Expr.Dictionary.Entry(s(3),s(2))); ./    --Python2.3 uses this format.
    
    --expression statement
    stmt ::= expr POP_TOP    /. set(new Stmt.Exprstmt(s(1))); ./
    
    --short circuit and/or operators
    expr ::= expr JUMP_IF_TRUE POP_TOP expr$e2 LAND             /. set(new Expr.ShortcircuitOr(s(1),s(4))); ./
    expr ::= expr JUMP_IF_FALSE POP_TOP expr$e2 LAND            /. set(new Expr.ShortcircuitAnd(s(1),s(4))); ./
    expr ::= jump JUMP_IF_FALSE POP_TOP LAND expr$a2 LAND$e6    /. set(new Expr.ShortcircuitAnd(null,s($a2))); ./    --Python2.3 optimised "if(1 and expr):"
    --expr ::= expr JUMP_IF_FALSE POP_TOP jump LAND (this is "if(x==3 and 1):", e.g.) ...the jump lands at the start of the stmt block.  Let's replace jump with LOAD_CONST 1, to remove this optimisation.
 
    --exec statement
    stmt ::= expr expr$e2 expr$e3 EXEC_STMT    /. set(new Stmt.Exec(s(1),s(2),s(3))); ./
    stmt ::= expr expr$e2 DUP_TOP EXEC_STMT    /. set(new Stmt.Exec(s(1),s(2),null)); ./
    
    --yield statement (used in generators to return the next element) (in Python2.2 generators and yield can only be used if the file from __future__ import generators)
    stmt ::= expr YIELD_STMT    /. set(new Stmt.Yield(s(1))); ./
    
    --assert statements
    stmt ::= expr JUMP_IF_FALSE POP_TOP expr$e4 JUMP_IF_TRUE POP_TOP$e6 LOAD_GLOBAL RAISE_VARARGS EAT_EXPR LAND LAND$e11 POP_TOP$e12                         /. set(new Stmt.Assert(s(1),s(4),t(7),null)); ./
    stmt ::= expr JUMP_IF_FALSE POP_TOP expr$e4 JUMP_IF_TRUE POP_TOP$e6 LOAD_GLOBAL expr$e8 RAISE_VARARGS EAT_EXPR EAT_EXPR$e12 LAND LAND$e13 POP_TOP$e14    /. set(new Stmt.Assert(s(1),s(4),t(7),s(8))); ./
    
    --closures
    expr ::= closure                                                    /. set(s(1)); ./
    closure ::= LOAD_CLOSURE LOAD_CONST MAKE_CLOSURE EAT_LOADCLOSURE    /. set((new ExprClosure(t(2))).addClosure(t(1))); ./
    closure ::= LOAD_CLOSURE closure EAT_LOADCLOSURE                    /. set(((ExprClosure)s(2)).addClosure(t(1))); ./
    closure ::= expr closure EAT_EXPR                                   /. set(((ExprClosure)s(2)).addExpr(s(1))); ./
    
    --slice building expressions (almost never used) (these are the only two possibilities)
    expr ::= expr expr$e2 BUILD_SLICE EAT_EXPR EAT_EXPR$e5                        /. set(new Expr.Buildslice(s(1),s(2),null)); ./
    expr ::= expr expr$e2 expr$e3 BUILD_SLICE EAT_EXPR EAT_EXPR$e6 EAT_EXPR$e7    /. set(new Expr.Buildslice(s(1),s(2),s(3))); ./
    
    --******************************* complete *************************************
 
    --assignment statements  (regular)
    stmt ::= expr$val h_assign_star$targets designator$var      /. set(new StmtAssign(s($val),s($targets).add(s($var)))); ./
    h_assign_star ::= %empty                                           /. set(new List()); ./
    h_assign_star ::= h_assign_star$ls h_assign$it                     /. set(s($ls).add(s($it))); ./
    h_assign ::= DUP_TOP designator$des                                /. set(s($des)); ./

    --return statements
    stmt ::= expr RETURN_VALUE                /. set(new StmtReturn(s(1))); ./

    --binary expressions
    expr ::= expr expr$e2 binary_op      /. set(new Expr.Binary(s(1),s(2),s(3))); ./
    binary_op ::= BINARY_ADD             /. set(t(1)); ./
    binary_op ::= BINARY_SUBTRACT        /. set(t(1)); ./
    binary_op ::= BINARY_MULTIPLY        /. set(t(1)); ./
    binary_op ::= BINARY_DIVIDE          /. set(t(1)); ./
    binary_op ::= BINARY_TRUE_DIVIDE     /. set(t(1)); ./
    binary_op ::= BINARY_FLOOR_DIVIDE    /. set(t(1)); ./
    binary_op ::= BINARY_MODULO          /. set(t(1)); ./
    binary_op ::= BINARY_LSHIFT          /. set(t(1)); ./
    binary_op ::= BINARY_RSHIFT          /. set(t(1)); ./
    binary_op ::= BINARY_AND             /. set(t(1)); ./
    binary_op ::= BINARY_OR              /. set(t(1)); ./
    binary_op ::= BINARY_XOR             /. set(t(1)); ./
    binary_op ::= BINARY_POWER           /. set(t(1)); ./
    binary_op ::= BINARY_VALUE           /. set(t(1)); ./
        

    --print statements
    stmt ::= PRINT_NEWLINE                          /. set(new StmtPrint(null,null,true)); ./
    stmt ::= expr$a PRINT_ITEM PRINT_NEWLINE        /. set(new StmtPrint(s($a),null,true)); ./
    stmt ::= expr$a PRINT_ITEM                      /. set(new StmtPrint(s($a),null,false)); ./
    stmt ::= expr$a h_pt_plus$b PRINT_NEWLINE_TO    /. set(new StmtPrint(s($b),s($a),true)); ./
    stmt ::= expr$a h_pt_plus$b POP_TOP             /. set(new StmtPrint(s($b),s($a),false)); ./
    stmt ::= expr$b PRINT_NEWLINE_TO                /. set(new StmtPrint(null,new List(s($b)),true)); ./
    h_pt_plus ::= h_pt$it                                 /. set(new List(s($it))); ./
    h_pt_plus ::= h_pt_plus$ls h_pt$it                    /. set(s($ls).add(s($it))); ./
    h_pt ::= DUP_TOP expr$ex ROT_TWO PRINT_ITEM_TO        /. set(s($ex)); ./
    
    --exception throwing  (since there is 0 to 3 arguments, we could have just hardcoded it instead.)
    stmt ::= dyn_raiseva                         /. set(s(1)); ./
    dyn_raiseva ::= RAISE_VARARGS                /. set(new Stmt.Raisevarargs()); ./
    dyn_raiseva ::= expr dyn_raiseva EAT_EXPR    /. set(s(2).add(s(1))); ./
    
    
    designator ::= dyn_unpack                             /. set(s(1)); ./
    dyn_unpack ::= UNPACK_SEQUENCE                        /. set(new Dsgn.UnpackSequence()); ./
    dyn_unpack ::= ANTIEAT_DESIG dyn_unpack designator    /. set(s(2).add(s(3))); ./
    
    expr ::= dyn_list_expr                             /. set(s(1)); ./
    dyn_list_expr ::= BUILD_LIST                       /. set(new Expr.Buildlist()); ./
    dyn_list_expr ::= expr dyn_list_expr EAT_EXPR      /. set(s(2).add(s(1))); ./
    expr ::= dyn_tuple_expr                            /. set(s(1)); ./
    dyn_tuple_expr ::= BUILD_TUPLE                     /. set(new Expr.Buildtuple()); ./
    dyn_tuple_expr ::= expr dyn_tuple_expr EAT_EXPR    /. set(s(2).add(s(1))); ./
    dyn_list_expr ::= EXPR_LIST BUILD_LIST             /. set(new Expr.Buildlist((new ExprList(t(1))).getvals())); ./
    dyn_tuple_expr ::= EXPR_LIST BUILD_TUPLE           /. set(new Expr.Buildtuple((new ExprList(t(1))).getvals())); ./
    --expr_star ::= %empty
    --expr_star ::= expr_star expr
    --tup2 ::= BUILD_TUPLE
    --tup2 ::= expr tup2
    --expr ::= UPCOMING_TUPLE tup2
    --dyn_tuple_expr ::= LOAD_CONST dyn_tuple_expr EAT_EXPR    /. set(s(2).add(t(1))); ./
    --dyn_mkfunc ::= LOAD_CONST MAKE_FUNCTION          /. set(new Expr.Makefunction(t(1))); ./
    dyn_mkfunc ::= LOAD_CONST_MAKE_FUNCTION          /. set(new Expr.Makefunction(t(1))); ./
    dyn_mkfunc ::= expr dyn_mkfunc EAT_EXPR            /. set(s(2).add(s(1))); ./
    --expr ::= dyn_shell
    --dyn_core ::= BUILD_LIST
    --dyn_core ::= BUILD_TUPLE
    --dyn_core ::= LOAD_CONST MAKE_FUNCTION
    --dyn_shell ::= dyn_core
    --dyn_shell ::= BUILD_LIST
    --dyn_shell ::= expr dyn_shell EAT_EXPR
    --dyn_shell ::= BUILD_TUPLE
    --dyn_shell ::= LOAD_CONST MAKE_FUNCTION
    
    --kwarg ::= LOAD_CONST expr         /. set(new Helpers.Kwarg(t(1),s(2))); ./
    expr ::= dyn_callfunc             /. set(s(1)); ./
    dyn_callfunc2 ::= CALL_FUNCTION                        /. set(new Expr.Callfunction(null,null)); ./
    dyn_callfunc2 ::= expr dyn_callfunc2 EAT_EXPR          /. set(((Expr.Callfunction)s(2)).addPoarg(s(1))); ./
    dyn_callfunc ::= expr dyn_callfunc2                    /. set(((Expr.Callfunction)s(2)).setName(s(1))); ./
    --dyn_callfunc2 ::= kwarg dyn_callfunc2 EAT_KWARG        /. set(((Expr.Callfunction)s(2)).addKwarg(s(1))); ./
    dyn_callfunc2 ::= LOAD_CONST expr dyn_callfunc2 EAT_KWARG        /. set(((Expr.Callfunction)s(3)).addKwarg(new Helpers.Kwarg(t(1),s(2)))); ./
    dyn_callfunc2 ::= expr CALL_FUNCTION_VAR               /. set(new Expr.Callfunction(s(1),null)); ./
    dyn_callfunc2 ::= expr CALL_FUNCTION_KW                /. set(new Expr.Callfunction(null,s(1))); ./
    dyn_callfunc2 ::= expr expr$e2 CALL_FUNCTION_VAR_KW    /. set(new Expr.Callfunction(s(1),s(2))); ./
    
    --dyn_callfunc ::= expr dyn_callfunc2
    --dyn_callfunc2 ::= CALL_FUNCTION
    --dyn_callfunc2 ::= expr CALL_FUNCTION_VAR
    --dyn_callfunc2 ::= expr CALL_FUNCTION_KW
    --dyn_callfunc2 ::= expr expr$e2 CALL_FUNCTION_VAR_KW
    --dyn_callfunc2 ::= expr dyn_callfunc2 EAT_EXPR
    ----dyn_callfunc2 ::= kwarg dyn_callfunc2 EAT_KWARG
    --dyn_callfunc2 ::= LOAD_CONST expr dyn_callfunc2 EAT_KWARG
    ----dyn_callfunc2 ::= LOAD_CONST dyn_callfunc2 EAT_CONST
    
    

    --makelist statements (aka list comprehensions)  e.g. a=[3*x for x in range(10) if x>1]  (I think it could be list_for instead
    --expr ::= BUILD_LIST DUP_TOP LOAD_ATTR designator list_iter del_stmt
    expr ::= BUILD_LIST DUP_TOP LOAD_ATTR designator list_for del_stmt               /. set(((Expr.Listfor)s(5)).setInfo(t(3),s(4),s(6))); ./
    list_iter ::= list_for                                                           /. set(s(1)); ./
    list_iter ::= list_if                                                            /. set(s(1)); ./
    list_iter ::= lc_body                                                            /. set(s(1)); ./
    list_for ::= expr h_looper designator list_iter JUMP_ABSOLUTE LAND               /. set(s(4).add(new Expr.Listfor.For(s(1),s(2),s(3)))); ./
    list_if ::= expr ifcmd POP_TOP list_iter jump LAND POP_TOP$e7 LAND$e8    /. set(s(4).add(new Expr.Listfor.If(s(1),s(2)))); ./
    lc_body ::= LOAD_NAME expr CALL_FUNCTION EAT_EXPR POP_TOP                        /. set(new Expr.Listfor(t(1),s(2))); ./
    lc_body ::= LOAD_FAST expr CALL_FUNCTION EAT_EXPR POP_TOP                        /. set(new Expr.Listfor(t(1),s(2))); ./
    
    jump ::= JUMP_FORWARD     /. set(t(1)); ./
    jump ::= JUMP_ABSOLUTE    /. set(t(1)); ./
    
%End
















--Dynamically generated stuff:
