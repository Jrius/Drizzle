/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.EnumMap;
import java.util.EnumSet;
import shared.b;

public  enum op
{
    STOP_CODE(0,T.DEF),
    POP_TOP(1,T.DEF),
    ROT_TWO(2,T.DEF),
    ROT_THREE(3,T.DEF),
    DUP_TOP(4,T.DEF),
    ROT_FOUR(5,T.DEF),
    _TRAP(6,T.DEF), //put in to break decompilation???
    UNARY_POSITIVE(10,T.DEF),
    UNARY_NEGATIVE(11,T.DEF),
    UNARY_NOT(12,T.DEF),
    UNARY_CONVERT(13,T.DEF),
    UNARY_INVERT(15,T.DEF),
    BINARY_POWER(19,T.DEF),
    BINARY_MULTIPLY(20,T.DEF),
    BINARY_DIVIDE(21,T.DEF),
    BINARY_MODULO(22,T.DEF),
    BINARY_ADD(23,T.DEF),
    BINARY_SUBTRACT(24,T.DEF),
    BINARY_SUBSCR(25,T.DEF),
    BINARY_FLOOR_DIVIDE(26,T.DEF),
    BINARY_TRUE_DIVIDE(27,T.DEF),
    INPLACE_FLOOR_DIVIDE(28,T.DEF),
    INPLACE_TRUE_DIVIDE(29,T.DEF),
    SLICE_0(30,T.DEF), //SHOULD BE +0
    SLICE_1(31,T.DEF),
    SLICE_2(32,T.DEF),
    SLICE_3(33,T.DEF),
    STORE_SLICE_0(40,T.DEF),
    STORE_SLICE_1(41,T.DEF),
    STORE_SLICE_2(42,T.DEF),
    STORE_SLICE_3(43,T.DEF),
    DELETE_SLICE_0(50,T.DEF),
    DELETE_SLICE_1(51,T.DEF),
    DELETE_SLICE_2(52,T.DEF),
    DELETE_SLICE_3(53,T.DEF),

    INPLACE_ADD(55,T.DEF),
    INPLACE_SUBTRACT(56,T.DEF),
    INPLACE_MULTIPLY(57,T.DEF),
    INPLACE_DIVIDE(58,T.DEF),
    INPLACE_MODULO(59,T.DEF),
    STORE_SUBSCR(60,T.DEF),
    DELETE_SUBSCR(61,T.DEF),

    BINARY_LSHIFT(62,T.DEF),
    BINARY_RSHIFT(63,T.DEF),
    BINARY_AND(64,T.DEF),
    BINARY_XOR(65,T.DEF),
    BINARY_OR(66,T.DEF),
    INPLACE_POWER(67,T.DEF),
    GET_ITER(68,T.DEF),

    PRINT_EXPR(70,T.DEF),
    PRINT_ITEM(71,T.DEF),
    PRINT_NEWLINE(72,T.DEF),
    PRINT_ITEM_TO(73,T.DEF),
    PRINT_NEWLINE_TO(74,T.DEF),
    INPLACE_LSHIFT(75,T.DEF),
    INPLACE_RSHIFT(76,T.DEF),
    INPLACE_AND(77,T.DEF),
    INPLACE_XOR(78,T.DEF),
    INPLACE_OR(79,T.DEF),
    BREAK_LOOP(80,T.DEF),

    LOAD_LOCALS(82,T.DEF),
    RETURN_VALUE(83,T.DEF),
    IMPORT_STAR(84,T.DEF),
    EXEC_STMT(85,T.DEF),
    YIELD_STMT(86,T.DEF),

    POP_BLOCK(87,T.DEF),
    END_FINALLY(88,T.DEF),
    BUILD_CLASS(89,T.DEF),

    STORE_NAME(90,T.NAME),
    DELETE_NAME(91,T.NAME),
    UNPACK_SEQUENCE(92,T.DEF),
    FOR_ITER(93,T.JREL),
    STORE_ATTR(95,T.NAME),
    DELETE_ATTR(96,T.NAME),
    STORE_GLOBAL(97,T.NAME),
    DELETE_GLOBAL(98,T.NAME),
    DUP_TOPX(99,T.DEF),
    LOAD_CONST(100,T.DEF_HASCONST),
    LOAD_NAME(101,T.NAME),
    BUILD_TUPLE(102,T.DEF),
    BUILD_LIST(103,T.DEF),
    BUILD_MAP(104,T.DEF),
    LOAD_ATTR(105,T.NAME),
    COMPARE_OP(106,T.DEF_HASCOMPARE),

    IMPORT_NAME(107,T.NAME),
    IMPORT_FROM(108,T.NAME),
    JUMP_FORWARD(110,T.JREL),
    JUMP_IF_FALSE(111,T.JREL),
    JUMP_IF_TRUE(112,T.JREL),
    JUMP_ABSOLUTE(113,T.JABS),
    FOR_LOOP(114,T.JREL), //Python22 has this, but Python23 doesn't

    LOAD_GLOBAL(116,T.NAME),
    CONTINUE_LOOP(119,T.JABS),
    SETUP_LOOP(120,T.JREL),
    SETUP_EXCEPT(121,T.JREL),
    SETUP_FINALLY(122,T.JREL),

    LOAD_FAST(124,T.DEF_HASLOCAL),
    STORE_FAST(125,T.DEF_HASLOCAL),
    DELETE_FAST(126,T.DEF_HASLOCAL),

    SET_LINENO(127,T.DEF), //Python22 has this, but Python23 doesn't

    RAISE_VARARGS(130,T.DEF),
    CALL_FUNCTION(131,T.DEF),
    MAKE_FUNCTION(132,T.DEF),
    BUILD_SLICE(133,T.DEF),

    MAKE_CLOSURE(134,T.DEF),
    LOAD_CLOSURE(135,T.DEF_HASFREE),
    LOAD_DEREF(136,T.DEF_HASFREE),
    STORE_DEREF(137,T.DEF_HASFREE),

    CALL_FUNCTION_VAR(140,T.DEF),
    CALL_FUNCTION_KW(141,T.DEF),
    CALL_FUNCTION_VAR_KW(142,T.DEF),

    EXTENDED_ARG(143,T.DEF),

    //fake ones
    LAND,
    CUSTOM,
    EXPR_LIST,
    SOF,
    EOF,

    //special
    none, //used to indicate no token; i.e. out of index range.
    module,
    come_from,
    code,

    //structures
    assign,
    return_stmt,
    call_stmt,
    importstar2,
    build_tuple,
    mkfunc,
    classdef,
    call_function,

    //convenience
    designator,
    statement,
    expr,
    ;

    /*private static op[] exprs = {
        LOAD_CONST,LOAD_NAME,
    };
    private static op[] designators = {
        STORE_NAME,
    };
    private static op[] stmts = {
    };*/
    /*private static op[][] hierdata = {
        new op[]{
            expr, LOAD_CONST, LOAD_NAME, build_tuple,
        },
        new op[]{
            statement, assign, return_stmt, importstar2,
        },
        new op[]{
            designator, STORE_NAME, STORE_GLOBAL,
        },
        //new op[]{  },
    };*/
    private int num;
    private T type;
    private boolean isBytecode;

    private static final int HAVE_ARGUMENT = 90;

    static HashMap<Integer,op> ops;
    //static HashSet<op> exprs;
    //static HashMap<op,HashSet<op>> hier;
    //static EnumHierarchy hier;

    static{
        //make lookup array
        ops = new HashMap();
        for(op o: op.values())
        {
            if(o.isBytecode)
            {
                ops.put(o.num, o);
            }
        }

        //exprs = new HashSet();
        /*hier = new HashMap();
        for(op o: op.values())
        {
            HashSet<op> s = new HashSet();
            for(op[] curh: hierdata)
            {
                for(op o2: curh)
                {
                    if(o==o2)
                    {
                        s.add(curh[0]);
                    }
                }
            }
            hier.put(o, s);
        }
        for(op[] curh: hierdata)
        {
            HashSet<op> s = new HashSet();
            for(int i=1;i<curh.length;i++)
            {
                op curo = curh[i];
                s.add(curo);
            }
            hier.put(curh[0], s);
        }*/
        //hier = new EnumHierarchy(hierdata);
    }

    op()
    {
        isBytecode = false;
    }
    op(int num, T type)
    {
        this.num = num;
        this.type = type;
        isBytecode = true;
    }
    /*public boolean is(op type)
    {
        if(this==type) return true;
        return hier.isXaY(this, type);
    }
    public boolean isExpression()
    {
        return hier.isXaY(this, op.expr);
    }
    public boolean isStatement()
    {
        return hier.isXaY(this, op.statement);
    }*/
    public int getbytecode()
    {
        return num;
    }
    public static op get(byte val)
    {
        return ops.get(b.ByteToInt32(val));
    }
    public static enum T
    {
        DEF,NAME,JREL,DEF_HASCONST,DEF_HASCOMPARE,JABS,DEF_HASLOCAL,DEF_HASFREE,
        //def,name,jrel,def_hasconst,def_hascompare,jabs,def_haslocal,def_hasfree,
    }
    public boolean hasArgument()
    {
        //return b.ByteToInt32(num)>=HAVE_ARGUMENT;
        return num>=HAVE_ARGUMENT;
    }
    public boolean hasExtendedArgument()
    {
        return num>=EXTENDED_ARG.num;
    }
    public boolean hasJrel()
    {
        return type==T.JREL;
    }
    public boolean hasJabs()
    {
        return type==T.JABS;
    }
    public boolean hasName()
    {
        return type==T.NAME;
    }
    public boolean hasConst()
    {
        return type==T.DEF_HASCONST;
    }
    public boolean hasFree()
    {
        return type==T.DEF_HASFREE;
    }
    public boolean hasLocal()
    {
        return type==T.DEF_HASLOCAL;
    }
    public boolean hasCompare()
    {
        return type==T.DEF_HASCOMPARE;
    }
}
