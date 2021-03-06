package pythondec3;

public interface PythonDec22sym {
    public final static int
      TK_LAND = 16,
      TK_EAT_EXPR = 27,
      TK_EAT_KWARG = 105,
      TK_ANTIEAT_DESIG = 60,
      TK_EAT_CONST = 116,
      TK_EAT_LOADCLOSURE = 75,
      TK_EXPR_LIST = 6,
      TK_LOAD_CONST_MAKE_FUNCTION = 7,
      TK_DUP_TOP = 29,
      TK_POP_TOP = 18,
      TK_PRINT_ITEM = 106,
      TK_PRINT_NEWLINE = 30,
      TK_PRINT_ITEM_TO = 107,
      TK_PRINT_NEWLINE_TO = 76,
      TK_RETURN_VALUE = 108,
      TK_ROT_TWO = 64,
      TK_ROT_THREE = 61,
      TK_ROT_FOUR = 109,
      TK_BINARY_ADD = 43,
      TK_BINARY_SUBTRACT = 44,
      TK_BINARY_MULTIPLY = 45,
      TK_BINARY_DIVIDE = 46,
      TK_BINARY_TRUE_DIVIDE = 47,
      TK_BINARY_FLOOR_DIVIDE = 48,
      TK_BINARY_MODULO = 49,
      TK_BINARY_LSHIFT = 50,
      TK_BINARY_RSHIFT = 51,
      TK_BINARY_AND = 52,
      TK_BINARY_OR = 53,
      TK_BINARY_XOR = 54,
      TK_BINARY_POWER = 55,
      TK_BINARY_VALUE = 56,
      TK_BINARY_SUBSCR = 40,
      TK_BUILD_CLASS = 77,
      TK_LOAD_LOCALS = 8,
      TK_POP_BLOCK = 65,
      TK_BREAK_LOOP = 34,
      TK_GET_ITER = 78,
      TK_END_FINALLY = 72,
      TK_IMPORT_STAR = 110,
      TK_STORE_SUBSCR = 71,
      TK_SLICE_0 = 20,
      TK_SLICE_1 = 41,
      TK_SLICE_2 = 42,
      TK_SLICE_3 = 62,
      TK_STORE_SLICE_0 = 79,
      TK_STORE_SLICE_1 = 80,
      TK_STORE_SLICE_2 = 81,
      TK_STORE_SLICE_3 = 82,
      TK_INPLACE_ADD = 83,
      TK_INPLACE_SUBTRACT = 84,
      TK_INPLACE_MULTIPLY = 85,
      TK_INPLACE_DIVIDE = 86,
      TK_INPLACE_TRUE_DIVIDE = 87,
      TK_INPLACE_FLOOR_DIVIDE = 88,
      TK_INPLACE_MODULO = 89,
      TK_INPLACE_POWER = 90,
      TK_INPLACE_LSHIFT = 91,
      TK_INPLACE_RSHIFT = 92,
      TK_INPLACE_AND = 93,
      TK_INPLACE_XOR = 94,
      TK_INPLACE_OR = 95,
      TK_UNARY_CONVERT = 21,
      TK_UNARY_INVERT = 22,
      TK_UNARY_NEGATIVE = 23,
      TK_UNARY_NOT = 24,
      TK_UNARY_POSITIVE = 25,
      TK_DELETE_SUBSCR = 96,
      TK_EXEC_STMT = 97,
      TK_YIELD_STMT = 111,
      TK_DELETE_SLICE_0 = 98,
      TK_DELETE_SLICE_1 = 99,
      TK_DELETE_SLICE_2 = 100,
      TK_DELETE_SLICE_3 = 101,
      TK_LOAD_NAME = 9,
      TK_STORE_NAME = 66,
      TK_LOAD_CONST = 3,
      TK_LOAD_GLOBAL = 10,
      TK_LOAD_FAST = 11,
      TK_STORE_ATTR = 102,
      TK_LOAD_ATTR = 17,
      TK_COMPARE_OP = 35,
      TK_IMPORT_NAME = 73,
      TK_IMPORT_FROM = 74,
      TK_STORE_GLOBAL = 67,
      TK_STORE_FAST = 68,
      TK_DELETE_GLOBAL = 31,
      TK_DELETE_NAME = 32,
      TK_DELETE_ATTR = 103,
      TK_DELETE_FAST = 33,
      TK_STORE_DEREF = 69,
      TK_LOAD_DEREF = 12,
      TK_LOAD_CLOSURE = 13,
      TK_BUILD_LIST = 4,
      TK_BUILD_TUPLE = 5,
      TK_MAKE_FUNCTION = 117,
      TK_CALL_FUNCTION = 19,
      TK_CALL_FUNCTION_VAR = 57,
      TK_CALL_FUNCTION_KW = 58,
      TK_CALL_FUNCTION_VAR_KW = 70,
      TK_DUP_TOPX = 104,
      TK_UNPACK_SEQUENCE = 63,
      TK_RAISE_VARARGS = 28,
      TK_JUMP_IF_TRUE = 26,
      TK_JUMP_IF_FALSE = 15,
      TK_JUMP_FORWARD = 1,
      TK_SETUP_LOOP = 36,
      TK_JUMP_ABSOLUTE = 2,
      TK_FOR_ITER = 112,
      TK_FOR_LOOP = 113,
      TK_SETUP_FINALLY = 37,
      TK_SETUP_EXCEPT = 38,
      TK_CONTINUE_LOOP = 39,
      TK_BUILD_MAP = 14,
      TK_MAKE_CLOSURE = 114,
      TK_BUILD_SLICE = 59,
      TK_EOF_TOKEN = 115,
      TK_ERROR_TOKEN = 118;

    public final static String orderedTerminalSymbols[] = {
                 "",
                 "JUMP_FORWARD",
                 "JUMP_ABSOLUTE",
                 "LOAD_CONST",
                 "BUILD_LIST",
                 "BUILD_TUPLE",
                 "EXPR_LIST",
                 "LOAD_CONST_MAKE_FUNCTION",
                 "LOAD_LOCALS",
                 "LOAD_NAME",
                 "LOAD_GLOBAL",
                 "LOAD_FAST",
                 "LOAD_DEREF",
                 "LOAD_CLOSURE",
                 "BUILD_MAP",
                 "JUMP_IF_FALSE",
                 "LAND",
                 "LOAD_ATTR",
                 "POP_TOP",
                 "CALL_FUNCTION",
                 "SLICE_0",
                 "UNARY_CONVERT",
                 "UNARY_INVERT",
                 "UNARY_NEGATIVE",
                 "UNARY_NOT",
                 "UNARY_POSITIVE",
                 "JUMP_IF_TRUE",
                 "EAT_EXPR",
                 "RAISE_VARARGS",
                 "DUP_TOP",
                 "PRINT_NEWLINE",
                 "DELETE_GLOBAL",
                 "DELETE_NAME",
                 "DELETE_FAST",
                 "BREAK_LOOP",
                 "COMPARE_OP",
                 "SETUP_LOOP",
                 "SETUP_FINALLY",
                 "SETUP_EXCEPT",
                 "CONTINUE_LOOP",
                 "BINARY_SUBSCR",
                 "SLICE_1",
                 "SLICE_2",
                 "BINARY_ADD",
                 "BINARY_SUBTRACT",
                 "BINARY_MULTIPLY",
                 "BINARY_DIVIDE",
                 "BINARY_TRUE_DIVIDE",
                 "BINARY_FLOOR_DIVIDE",
                 "BINARY_MODULO",
                 "BINARY_LSHIFT",
                 "BINARY_RSHIFT",
                 "BINARY_AND",
                 "BINARY_OR",
                 "BINARY_XOR",
                 "BINARY_POWER",
                 "BINARY_VALUE",
                 "CALL_FUNCTION_VAR",
                 "CALL_FUNCTION_KW",
                 "BUILD_SLICE",
                 "ANTIEAT_DESIG",
                 "ROT_THREE",
                 "SLICE_3",
                 "UNPACK_SEQUENCE",
                 "ROT_TWO",
                 "POP_BLOCK",
                 "STORE_NAME",
                 "STORE_GLOBAL",
                 "STORE_FAST",
                 "STORE_DEREF",
                 "CALL_FUNCTION_VAR_KW",
                 "STORE_SUBSCR",
                 "END_FINALLY",
                 "IMPORT_NAME",
                 "IMPORT_FROM",
                 "EAT_LOADCLOSURE",
                 "PRINT_NEWLINE_TO",
                 "BUILD_CLASS",
                 "GET_ITER",
                 "STORE_SLICE_0",
                 "STORE_SLICE_1",
                 "STORE_SLICE_2",
                 "STORE_SLICE_3",
                 "INPLACE_ADD",
                 "INPLACE_SUBTRACT",
                 "INPLACE_MULTIPLY",
                 "INPLACE_DIVIDE",
                 "INPLACE_TRUE_DIVIDE",
                 "INPLACE_FLOOR_DIVIDE",
                 "INPLACE_MODULO",
                 "INPLACE_POWER",
                 "INPLACE_LSHIFT",
                 "INPLACE_RSHIFT",
                 "INPLACE_AND",
                 "INPLACE_XOR",
                 "INPLACE_OR",
                 "DELETE_SUBSCR",
                 "EXEC_STMT",
                 "DELETE_SLICE_0",
                 "DELETE_SLICE_1",
                 "DELETE_SLICE_2",
                 "DELETE_SLICE_3",
                 "STORE_ATTR",
                 "DELETE_ATTR",
                 "DUP_TOPX",
                 "EAT_KWARG",
                 "PRINT_ITEM",
                 "PRINT_ITEM_TO",
                 "RETURN_VALUE",
                 "ROT_FOUR",
                 "IMPORT_STAR",
                 "YIELD_STMT",
                 "FOR_ITER",
                 "FOR_LOOP",
                 "MAKE_CLOSURE",
                 "EOF_TOKEN",
                 "EAT_CONST",
                 "MAKE_FUNCTION",
                 "ERROR_TOKEN"
             };

    public final static int numTokenKinds = orderedTerminalSymbols.length;
    public final static boolean isValidForParser = true;
}
