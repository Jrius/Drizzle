// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g 2010-05-31 12:36:45

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SDLgrammarLexer extends Lexer {
    public static final int DOLLAR=38;
    public static final int NATNUM=47;
    public static final int LEFTPARENS=33;
    public static final int VERSION=41;
    public static final int EQUALS=37;
    public static final int EOF=-1;
    public static final int STATEDESC=30;
    public static final int NAME=46;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int LEFTCURLY=31;
    public static final int COMMA=39;
    public static final int RIGHTCURLY=32;
    public static final int VAR=42;
    public static final int COMMENT=48;
    public static final int D=7;
    public static final int E=8;
    public static final int F=9;
    public static final int G=10;
    public static final int A=4;
    public static final int B=5;
    public static final int C=6;
    public static final int L=15;
    public static final int M=16;
    public static final int N=17;
    public static final int RIGHTSQUARE=36;
    public static final int O=18;
    public static final int H=11;
    public static final int I=12;
    public static final int DEFAULT=43;
    public static final int J=13;
    public static final int K=14;
    public static final int U=24;
    public static final int T=23;
    public static final int W=26;
    public static final int V=25;
    public static final int Q=20;
    public static final int SEMICOLON=40;
    public static final int P=19;
    public static final int S=22;
    public static final int R=21;
    public static final int Y=28;
    public static final int X=27;
    public static final int Z=29;
    public static final int WS=49;
    public static final int DISPLAYOPTION=44;
    public static final int LEFTSQUARE=35;
    public static final int RIGHTPARENS=34;
    public static final int DEFAULTOPTION=45;
    public static final int STRING=50;

    // delegates
    // delegators

    public SDLgrammarLexer() {;} 
    public SDLgrammarLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SDLgrammarLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g"; }

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:3:7: ( '-' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:3:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:4:7: ( '.' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:4:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "A"
    public final void mA() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:12:11: ( ( 'a' | 'A' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:12:12: ( 'a' | 'A' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "A"

    // $ANTLR start "B"
    public final void mB() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:13:11: ( ( 'b' | 'B' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:13:12: ( 'b' | 'B' )
            {
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "B"

    // $ANTLR start "C"
    public final void mC() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:14:11: ( ( 'c' | 'C' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:14:12: ( 'c' | 'C' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "C"

    // $ANTLR start "D"
    public final void mD() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:15:11: ( ( 'd' | 'D' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:15:12: ( 'd' | 'D' )
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "D"

    // $ANTLR start "E"
    public final void mE() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:16:11: ( ( 'e' | 'E' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:16:12: ( 'e' | 'E' )
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "E"

    // $ANTLR start "F"
    public final void mF() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:17:11: ( ( 'f' | 'F' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:17:12: ( 'f' | 'F' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "F"

    // $ANTLR start "G"
    public final void mG() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:18:11: ( ( 'g' | 'G' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:18:12: ( 'g' | 'G' )
            {
            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "G"

    // $ANTLR start "H"
    public final void mH() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:19:11: ( ( 'h' | 'H' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:19:12: ( 'h' | 'H' )
            {
            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "H"

    // $ANTLR start "I"
    public final void mI() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:20:11: ( ( 'i' | 'I' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:20:12: ( 'i' | 'I' )
            {
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "I"

    // $ANTLR start "J"
    public final void mJ() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:21:11: ( ( 'j' | 'J' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:21:12: ( 'j' | 'J' )
            {
            if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "J"

    // $ANTLR start "K"
    public final void mK() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:22:11: ( ( 'k' | 'K' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:22:12: ( 'k' | 'K' )
            {
            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "K"

    // $ANTLR start "L"
    public final void mL() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:23:11: ( ( 'l' | 'L' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:23:12: ( 'l' | 'L' )
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "L"

    // $ANTLR start "M"
    public final void mM() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:24:11: ( ( 'm' | 'M' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:24:12: ( 'm' | 'M' )
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "M"

    // $ANTLR start "N"
    public final void mN() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:25:11: ( ( 'n' | 'N' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:25:12: ( 'n' | 'N' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "N"

    // $ANTLR start "O"
    public final void mO() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:26:11: ( ( 'o' | 'O' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:26:12: ( 'o' | 'O' )
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "O"

    // $ANTLR start "P"
    public final void mP() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:27:11: ( ( 'p' | 'P' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:27:12: ( 'p' | 'P' )
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "P"

    // $ANTLR start "Q"
    public final void mQ() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:28:11: ( ( 'q' | 'Q' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:28:12: ( 'q' | 'Q' )
            {
            if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Q"

    // $ANTLR start "R"
    public final void mR() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:29:11: ( ( 'r' | 'R' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:29:12: ( 'r' | 'R' )
            {
            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "R"

    // $ANTLR start "S"
    public final void mS() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:30:11: ( ( 's' | 'S' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:30:12: ( 's' | 'S' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "S"

    // $ANTLR start "T"
    public final void mT() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:31:11: ( ( 't' | 'T' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:31:12: ( 't' | 'T' )
            {
            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "T"

    // $ANTLR start "U"
    public final void mU() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:32:11: ( ( 'u' | 'U' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:32:12: ( 'u' | 'U' )
            {
            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "U"

    // $ANTLR start "V"
    public final void mV() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:33:11: ( ( 'v' | 'V' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:33:12: ( 'v' | 'V' )
            {
            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "V"

    // $ANTLR start "W"
    public final void mW() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:34:11: ( ( 'w' | 'W' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:34:12: ( 'w' | 'W' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "W"

    // $ANTLR start "X"
    public final void mX() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:35:11: ( ( 'x' | 'X' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:35:12: ( 'x' | 'X' )
            {
            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "X"

    // $ANTLR start "Y"
    public final void mY() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:36:11: ( ( 'y' | 'Y' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:36:12: ( 'y' | 'Y' )
            {
            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Y"

    // $ANTLR start "Z"
    public final void mZ() throws RecognitionException {
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:37:11: ( ( 'z' | 'Z' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:37:12: ( 'z' | 'Z' )
            {
            if ( input.LA(1)=='Z'||input.LA(1)=='z' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Z"

    // $ANTLR start "STATEDESC"
    public final void mSTATEDESC() throws RecognitionException {
        try {
            int _type = STATEDESC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:39:10: ( S T A T E D E S C )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:39:12: S T A T E D E S C
            {
            mS(); 
            mT(); 
            mA(); 
            mT(); 
            mE(); 
            mD(); 
            mE(); 
            mS(); 
            mC(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STATEDESC"

    // $ANTLR start "LEFTCURLY"
    public final void mLEFTCURLY() throws RecognitionException {
        try {
            int _type = LEFTCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:40:10: ( '{' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:40:12: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFTCURLY"

    // $ANTLR start "RIGHTCURLY"
    public final void mRIGHTCURLY() throws RecognitionException {
        try {
            int _type = RIGHTCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:41:11: ( '}' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:41:13: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHTCURLY"

    // $ANTLR start "LEFTPARENS"
    public final void mLEFTPARENS() throws RecognitionException {
        try {
            int _type = LEFTPARENS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:42:11: ( '(' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:42:13: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFTPARENS"

    // $ANTLR start "RIGHTPARENS"
    public final void mRIGHTPARENS() throws RecognitionException {
        try {
            int _type = RIGHTPARENS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:43:12: ( ')' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:43:14: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHTPARENS"

    // $ANTLR start "LEFTSQUARE"
    public final void mLEFTSQUARE() throws RecognitionException {
        try {
            int _type = LEFTSQUARE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:44:11: ( '[' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:44:13: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFTSQUARE"

    // $ANTLR start "RIGHTSQUARE"
    public final void mRIGHTSQUARE() throws RecognitionException {
        try {
            int _type = RIGHTSQUARE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:45:12: ( ']' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:45:14: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHTSQUARE"

    // $ANTLR start "EQUALS"
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:46:7: ( '=' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:46:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUALS"

    // $ANTLR start "DOLLAR"
    public final void mDOLLAR() throws RecognitionException {
        try {
            int _type = DOLLAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:47:7: ( '$' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:47:9: '$'
            {
            match('$'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOLLAR"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:48:6: ( ',' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:48:8: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "SEMICOLON"
    public final void mSEMICOLON() throws RecognitionException {
        try {
            int _type = SEMICOLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:49:10: ( ';' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:49:12: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMICOLON"

    // $ANTLR start "VERSION"
    public final void mVERSION() throws RecognitionException {
        try {
            int _type = VERSION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:50:8: ( V E R S I O N )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:50:10: V E R S I O N
            {
            mV(); 
            mE(); 
            mR(); 
            mS(); 
            mI(); 
            mO(); 
            mN(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VERSION"

    // $ANTLR start "VAR"
    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:51:4: ( V A R )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:51:6: V A R
            {
            mV(); 
            mA(); 
            mR(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VAR"

    // $ANTLR start "DEFAULT"
    public final void mDEFAULT() throws RecognitionException {
        try {
            int _type = DEFAULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:52:8: ( D E F A U L T )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:52:10: D E F A U L T
            {
            mD(); 
            mE(); 
            mF(); 
            mA(); 
            mU(); 
            mL(); 
            mT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEFAULT"

    // $ANTLR start "DISPLAYOPTION"
    public final void mDISPLAYOPTION() throws RecognitionException {
        try {
            int _type = DISPLAYOPTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:53:14: ( D I S P L A Y O P T I O N )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:53:16: D I S P L A Y O P T I O N
            {
            mD(); 
            mI(); 
            mS(); 
            mP(); 
            mL(); 
            mA(); 
            mY(); 
            mO(); 
            mP(); 
            mT(); 
            mI(); 
            mO(); 
            mN(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DISPLAYOPTION"

    // $ANTLR start "DEFAULTOPTION"
    public final void mDEFAULTOPTION() throws RecognitionException {
        try {
            int _type = DEFAULTOPTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:54:14: ( D E F A U L T O P T I O N )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:54:16: D E F A U L T O P T I O N
            {
            mD(); 
            mE(); 
            mF(); 
            mA(); 
            mU(); 
            mL(); 
            mT(); 
            mO(); 
            mP(); 
            mT(); 
            mI(); 
            mO(); 
            mN(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEFAULTOPTION"

    // $ANTLR start "NAME"
    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:56:6: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:56:8: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:56:28: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NAME"

    // $ANTLR start "NATNUM"
    public final void mNATNUM() throws RecognitionException {
        try {
            int _type = NATNUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:8: ( ( '-' )? ( '0' | ( ( '1' .. '9' ) ( '0' .. '9' )* ) ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:10: ( '-' )? ( '0' | ( ( '1' .. '9' ) ( '0' .. '9' )* ) )
            {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:10: ( '-' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='-') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:10: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:15: ( '0' | ( ( '1' .. '9' ) ( '0' .. '9' )* ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='0') ) {
                alt4=1;
            }
            else if ( ((LA4_0>='1' && LA4_0<='9')) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:16: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:20: ( ( '1' .. '9' ) ( '0' .. '9' )* )
                    {
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:20: ( ( '1' .. '9' ) ( '0' .. '9' )* )
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:21: ( '1' .. '9' ) ( '0' .. '9' )*
                    {
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:21: ( '1' .. '9' )
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:22: '1' .. '9'
                    {
                    matchRange('1','9'); 

                    }

                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:31: ( '0' .. '9' )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:58:32: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);


                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NATNUM"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:69:5: ( '#' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:69:9: '#' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match('#'); 
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:69:13: (~ ( '\\n' | '\\r' ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\u0000' && LA5_0<='\t')||(LA5_0>='\u000B' && LA5_0<='\f')||(LA5_0>='\u000E' && LA5_0<='\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:69:13: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:69:27: ( '\\r' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='\r') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:69:27: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:73:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:73:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:82:5: ( '\"' (~ ( '\"' ) )* '\"' | '\\'' (~ ( '\\'' ) )* '\\'' )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='\"') ) {
                alt9=1;
            }
            else if ( (LA9_0=='\'') ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:82:8: '\"' (~ ( '\"' ) )* '\"'
                    {
                    match('\"'); 
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:82:12: (~ ( '\"' ) )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='\u0000' && LA7_0<='!')||(LA7_0>='#' && LA7_0<='\uFFFF')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:82:14: ~ ( '\"' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:83:8: '\\'' (~ ( '\\'' ) )* '\\''
                    {
                    match('\''); 
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:83:13: (~ ( '\\'' ) )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( ((LA8_0>='\u0000' && LA8_0<='&')||(LA8_0>='(' && LA8_0<='\uFFFF')) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:83:15: ~ ( '\\'' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);

                    match('\''); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    public void mTokens() throws RecognitionException {
        // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:8: ( T__51 | T__52 | STATEDESC | LEFTCURLY | RIGHTCURLY | LEFTPARENS | RIGHTPARENS | LEFTSQUARE | RIGHTSQUARE | EQUALS | DOLLAR | COMMA | SEMICOLON | VERSION | VAR | DEFAULT | DISPLAYOPTION | DEFAULTOPTION | NAME | NATNUM | COMMENT | WS | STRING )
        int alt10=23;
        alt10 = dfa10.predict(input);
        switch (alt10) {
            case 1 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:10: T__51
                {
                mT__51(); 

                }
                break;
            case 2 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:16: T__52
                {
                mT__52(); 

                }
                break;
            case 3 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:22: STATEDESC
                {
                mSTATEDESC(); 

                }
                break;
            case 4 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:32: LEFTCURLY
                {
                mLEFTCURLY(); 

                }
                break;
            case 5 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:42: RIGHTCURLY
                {
                mRIGHTCURLY(); 

                }
                break;
            case 6 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:53: LEFTPARENS
                {
                mLEFTPARENS(); 

                }
                break;
            case 7 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:64: RIGHTPARENS
                {
                mRIGHTPARENS(); 

                }
                break;
            case 8 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:76: LEFTSQUARE
                {
                mLEFTSQUARE(); 

                }
                break;
            case 9 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:87: RIGHTSQUARE
                {
                mRIGHTSQUARE(); 

                }
                break;
            case 10 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:99: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 11 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:106: DOLLAR
                {
                mDOLLAR(); 

                }
                break;
            case 12 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:113: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 13 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:119: SEMICOLON
                {
                mSEMICOLON(); 

                }
                break;
            case 14 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:129: VERSION
                {
                mVERSION(); 

                }
                break;
            case 15 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:137: VAR
                {
                mVAR(); 

                }
                break;
            case 16 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:141: DEFAULT
                {
                mDEFAULT(); 

                }
                break;
            case 17 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:149: DISPLAYOPTION
                {
                mDISPLAYOPTION(); 

                }
                break;
            case 18 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:163: DEFAULTOPTION
                {
                mDEFAULTOPTION(); 

                }
                break;
            case 19 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:177: NAME
                {
                mNAME(); 

                }
                break;
            case 20 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:182: NATNUM
                {
                mNATNUM(); 

                }
                break;
            case 21 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:189: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 22 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:197: WS
                {
                mWS(); 

                }
                break;
            case 23 :
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:1:200: STRING
                {
                mSTRING(); 

                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    static final String DFA10_eotS =
        "\1\uffff\1\25\1\uffff\1\20\12\uffff\2\20\6\uffff\7\20\1\42\4\20"+
        "\1\uffff\13\20\1\62\1\63\2\20\2\uffff\2\20\1\71\2\20\1\uffff\6\20"+
        "\1\102\1\103\2\uffff";
    static final String DFA10_eofS =
        "\104\uffff";
    static final String DFA10_minS =
        "\1\11\1\60\1\uffff\1\124\12\uffff\1\101\1\105\6\uffff\1\101\2\122"+
        "\1\106\1\123\1\124\1\123\1\60\1\101\1\120\1\105\1\111\1\uffff\1"+
        "\125\1\114\1\104\1\117\1\114\1\101\1\105\1\116\1\124\1\131\1\123"+
        "\2\60\1\117\1\103\2\uffff\2\120\1\60\2\124\1\uffff\2\111\2\117\2"+
        "\116\2\60\2\uffff";
    static final String DFA10_maxS =
        "\1\175\1\71\1\uffff\1\164\12\uffff\1\145\1\151\6\uffff\1\141\2"+
        "\162\1\146\1\163\1\164\1\163\1\172\1\141\1\160\1\145\1\151\1\uffff"+
        "\1\165\1\154\1\144\1\157\1\154\1\141\1\145\1\156\1\164\1\171\1\163"+
        "\2\172\1\157\1\143\2\uffff\2\160\1\172\2\164\1\uffff\2\151\2\157"+
        "\2\156\2\172\2\uffff";
    static final String DFA10_acceptS =
        "\2\uffff\1\2\1\uffff\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
        "\15\2\uffff\1\23\1\24\1\25\1\26\1\27\1\1\14\uffff\1\17\17\uffff"+
        "\1\16\1\20\5\uffff\1\3\10\uffff\1\22\1\21";
    static final String DFA10_specialS =
        "\104\uffff}>";
    static final String[] DFA10_transitionS = {
            "\2\23\2\uffff\1\23\22\uffff\1\23\1\uffff\1\24\1\22\1\13\2\uffff"+
            "\1\24\1\6\1\7\2\uffff\1\14\1\1\1\2\1\uffff\12\21\1\uffff\1\15"+
            "\1\uffff\1\12\3\uffff\3\20\1\17\16\20\1\3\2\20\1\16\4\20\1\10"+
            "\1\uffff\1\11\3\uffff\3\20\1\17\16\20\1\3\2\20\1\16\4\20\1\4"+
            "\1\uffff\1\5",
            "\12\21",
            "",
            "\1\26\37\uffff\1\26",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\30\3\uffff\1\27\33\uffff\1\30\3\uffff\1\27",
            "\1\31\3\uffff\1\32\33\uffff\1\31\3\uffff\1\32",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\33\37\uffff\1\33",
            "\1\34\37\uffff\1\34",
            "\1\35\37\uffff\1\35",
            "\1\36\37\uffff\1\36",
            "\1\37\37\uffff\1\37",
            "\1\40\37\uffff\1\40",
            "\1\41\37\uffff\1\41",
            "\12\20\7\uffff\32\20\4\uffff\1\20\1\uffff\32\20",
            "\1\43\37\uffff\1\43",
            "\1\44\37\uffff\1\44",
            "\1\45\37\uffff\1\45",
            "\1\46\37\uffff\1\46",
            "",
            "\1\47\37\uffff\1\47",
            "\1\50\37\uffff\1\50",
            "\1\51\37\uffff\1\51",
            "\1\52\37\uffff\1\52",
            "\1\53\37\uffff\1\53",
            "\1\54\37\uffff\1\54",
            "\1\55\37\uffff\1\55",
            "\1\56\37\uffff\1\56",
            "\1\57\37\uffff\1\57",
            "\1\60\37\uffff\1\60",
            "\1\61\37\uffff\1\61",
            "\12\20\7\uffff\32\20\4\uffff\1\20\1\uffff\32\20",
            "\12\20\7\uffff\16\20\1\64\13\20\4\uffff\1\20\1\uffff\16\20"+
            "\1\64\13\20",
            "\1\65\37\uffff\1\65",
            "\1\66\37\uffff\1\66",
            "",
            "",
            "\1\67\37\uffff\1\67",
            "\1\70\37\uffff\1\70",
            "\12\20\7\uffff\32\20\4\uffff\1\20\1\uffff\32\20",
            "\1\72\37\uffff\1\72",
            "\1\73\37\uffff\1\73",
            "",
            "\1\74\37\uffff\1\74",
            "\1\75\37\uffff\1\75",
            "\1\76\37\uffff\1\76",
            "\1\77\37\uffff\1\77",
            "\1\100\37\uffff\1\100",
            "\1\101\37\uffff\1\101",
            "\12\20\7\uffff\32\20\4\uffff\1\20\1\uffff\32\20",
            "\12\20\7\uffff\32\20\4\uffff\1\20\1\uffff\32\20",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__51 | T__52 | STATEDESC | LEFTCURLY | RIGHTCURLY | LEFTPARENS | RIGHTPARENS | LEFTSQUARE | RIGHTSQUARE | EQUALS | DOLLAR | COMMA | SEMICOLON | VERSION | VAR | DEFAULT | DISPLAYOPTION | DEFAULTOPTION | NAME | NATNUM | COMMENT | WS | STRING );";
        }
    }
 

}