package parsers.gen;

public interface SDLsym {
    public final static int
      TK_STATEDESC = 4,
      TK_EOL = 15,
      TK_LEFTCURLY = 5,
      TK_RIGHTCURLY = 6,
      TK_LEFTPARENS = 16,
      TK_RIGHTPARENS = 17,
      TK_LEFTSQUARE = 7,
      TK_RIGHTSQUARE = 8,
      TK_EQUALS = 2,
      TK_VERSION = 9,
      TK_VAR = 10,
      TK_DEFAULT = 11,
      TK_DISPLAYOPTION = 12,
      TK_DEFAULTOPTION = 13,
      TK_NAME = 1,
      TK_NUMBER = 3,
      TK_COMMENT = 18,
      TK_EOF_TOKEN = 14,
      TK_ERROR_TOKEN = 19;

    public final static String orderedTerminalSymbols[] = {
                 "",
                 "NAME",
                 "EQUALS",
                 "NUMBER",
                 "STATEDESC",
                 "LEFTCURLY",
                 "RIGHTCURLY",
                 "LEFTSQUARE",
                 "RIGHTSQUARE",
                 "VERSION",
                 "VAR",
                 "DEFAULT",
                 "DISPLAYOPTION",
                 "DEFAULTOPTION",
                 "EOF_TOKEN",
                 "EOL",
                 "LEFTPARENS",
                 "RIGHTPARENS",
                 "COMMENT",
                 "ERROR_TOKEN"
             };

    public final static int numTokenKinds = orderedTerminalSymbols.length;
    public final static boolean isValidForParser = true;
}
