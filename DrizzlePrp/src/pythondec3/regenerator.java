/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3;

import pythondec.*;
import pythondec3.ast.*;
import java.util.Vector;
import java.util.LinkedList;
import shared.m;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


public class regenerator
{
    //Python 2.3 replaces jumps to JUMP_ABSOLUTE or JUMP_RELATIVE with the jump to the final destination.
    //Plan: search for one of the structures (forstmt?, ifstmt, whilestmt, tryexcept, tryfinally?)
    //And find the end of it, then find the extra LANDs after it.
    //Then bring those extra LANDS just inside, and recurse.

    private ArrayList<Tok> oldtokens;
    private PyCode code;
    private LinkedList<Tok> ts = new LinkedList();
    //private ArrayDeque<blocktype> stack = new ArrayDeque();
    private ArrayList<LandMoveInfo> moves = new ArrayList();
    private ArrayDeque<Status> fullstack = new ArrayDeque();
    private int dontmove = 0;

    public enum blocktype
    {
        ROOT,
        IF,
        LOOP,
        WHILEIF, //still a while statement, but the inner conditional portion.
        FOR,
        EXCEPT_MAIN,
        EXCEPT_CATCH,
        EXCEPT_COND,
        EXCEPT_CATCHALL,
        LISTCOMP_FOR,
        LISTCOMP_IF,
        FINALLY,
    }
    public static class LandMoveInfo
    {
        Tok jumptoken; //the jump token the land comes from
        Tok desttoken; //the correct destination
    }
    public static class Status
    {
        Tok first;
        Tok last;
        blocktype type;
    }
    public Status current()
    {
        Status r = fullstack.peekFirst();
        return r;
    }
    public boolean hasParentOfType(blocktype type)
    {
        Iterator<Status> i = fullstack.descendingIterator();
        while(i.hasNext())
        {
            Status s = i.next();
            if(s.type==type) return true;
        }
        return false;
    }
    public void updateStatus(blocktype type, Tok first, Tok last)
    {
        Status parent = current();
        if(parent!=null)
            if(before(first,parent.first) || after(last,parent.last))
                m.throwUncaughtException("child should not be outside of parent");

        Status s = new Status();
        s.type = type;
        s.first = first;
        s.last = last;
        fullstack.push(s);
    }
    public void popStatus()
    {
        fullstack.pop();
    }
    /*public Status parent()
    {
        Status s = fullstack.peekFirst();
        return s;
    }*/
    public void addLandMove(Tok jump, Tok dest)
    {
        if(dontmove!=0)
            return;

        final boolean usequeue = false;
        if(usequeue)
        {
            LandMoveInfo lmi = new LandMoveInfo();
            lmi.jumptoken = jump;
            lmi.desttoken = dest;
            moves.add(lmi);
        }
        else
        {
            Tok f = current().first;
            Tok l = current().last;
            int find = f.getTokenIndex();
            int lind = l.getTokenIndex();
            int dind = dest.getTokenIndex();
            int jumpoffset = jump.oi.offset;

            //make sure the destination is within this range.   (if dind==lind, then dest=l anyway
            if(dind<=find || dind>=lind)
                dest = l;

            //find the Land that belongs to Jump:
            /*Tok land = null;
            for(Tok t: ts)
            {
                if(t.oi.o==op.LAND)
                {
                    int curLandOffset = (Integer)t.oi.pattr;
                    if(jumpoffset==curLandOffset)
                    {
                        land = t;
                        break;
                    }
                }
            }*/
            Tok land = tryToFindLand(jump);
            if(land==null)
                m.throwUncaughtException("unexpected");

            //move the land
            moveToken(land,dest);
        }
    }

    public Tok tryToFindLand(Tok jump)
    {
        int jumpoffset = jump.oi.offset;

        Tok land = null;
        for(Tok t: ts)
        {
            if(t.oi.o==op.LAND)
            {
                int curLandOffset = (Integer)t.oi.pattr;
                if(jumpoffset==curLandOffset)
                {
                    land = t;
                    break;
                }
            }
        }

        return land;
    }

    public void replaceToken(Tok oldtok, Tok newtok)
    {
        int curpos = ts.indexOf(oldtok);
        ts.set(curpos, newtok);
        this.renumber();
    }

    public regenerator(PyCode code)
    {
        this.code = code;
        ts.addAll(this.code.tokens);
    }

    public void regenerateStructure()
    {
        try{
            Tok first = ts.getFirst();
            Tok last = ts.getLast();
            regen(blocktype.ROOT,first,last);
        }catch(FoundException e){
            e.printStackTrace();
            m.throwUncaughtException("Token was found but nothing caught it.");
        }catch(java.lang.RuntimeException e){
            m.msg("************* orig disassembly ******************");
            m.msg("function/class name: "+code.name.toString());
            for(Tok tok: code.tokens)
            {
                m.msg(tok.toString());
            }
            m.msg("************* end of orig disassembly ***********");
            m.msg("************* current disassembly ******************");
            m.msg("function/class name: "+code.name.toString());
            for(Tok tok: ts)
            {
                m.msg(tok.toString());
            }
            m.msg("************* end of current disassembly ***********");
            throw e;
        }
        //assign the result
        oldtokens = code.tokens;
        code.tokens = new ArrayList();
        int i = 0;
        for(Tok tok: ts)
        {
            tok.setTokenIndex(i);
            code.tokens.add(tok);
            i++;
        }
        int dummy=0;
    }
    private class FoundException extends java.lang.Exception //is a good thing^^
    {
        Tok token;

        public FoundException(Tok token)
        {
            this.token = token;
        }
    }
    private Tok regen(blocktype type, Tok first, Tok last) throws FoundException
    {
        updateStatus(type,first,last);
        Tok result=null;
        try{
            result = regen2(type,first,last);
        }catch(FoundException e){
            popStatus();
            throw e;
        }
        popStatus();
        return result;
    }
    private Tok regen2(blocktype type, Tok first, Tok last) throws FoundException
    {
        //Status parent = current(); //must be before the updateStatus line.
        //updateStatus(type,first,last);
        Status parent = current();
        //Tok result = null;

        Tok curt = getTokAtRelativePos(first,1);
        while(true)
        {
            //search for start of next block
            //Tok curt = code.tokens.get(pos);

            if(curt.getTokenIndex()>1485) //1487
            {
                int d4 = 0;
            }

            if(curt.oi.o==op.JUMP_IF_TRUE || curt.oi.o==op.JUMP_IF_FALSE)
            {
                //if statement and short-circuitAndOr exprs, and inner structure of while stmt
                PyCode dummy2 = code;
                Tok elsestart = getTokPointedToByTok(curt);
                //elsestart is the elseblock if this is an ifstmt, and to a JUMP_IF_TRUE or JUMP_IF_FALSE if this is a short-circuit and/or expr.
                //so if elsestart is JUMP_IF_TRUE or JUMP_IF_FALSE
                //wait, no, it can be something else if it is say (not(a or b))
                if(elsestart.oi.o==op.POP_TOP)
                {
                    Tok jump = getTokAtRelativePos(elsestart,-2);
                    if(jump.oi.o==op.JUMP_ABSOLUTE || jump.oi.o==op.JUMP_FORWARD)
                    {
                        Tok jumpdest = getTokPointedToByTok(jump);
                        if(parent.type==blocktype.LOOP)
                        {
                            //inner structure of while block.
                            //if(isBefore(jump,jumpdest))
                            //    m.throwUncaughtException("unexpected"); //this means this is a regular if stmt I guess.
                            //Tok expr = jumpdest;
                            //recurse on loop body
                            //Tok start = curt;
                            //Tok end = jump;
                            Tok tester = getTokAtRelativePos(elsestart,1);
                            if(tester.oi.o==op.POP_BLOCK)
                            {
                                int dummy=0;
                                //then this is the main if stmt, and so this loop is a while loop.
                                //stack.push(blocktype.WHILEIF); //is this the correct type?
                                regen(blocktype.WHILEIF,curt,jump);
                                //stack.pop();
                                //continue with else clause
                                curt = getTokAtRelativePos(jump,1);
                            }
                            else
                            {
                                //otherwise it's just an if stmt in a loop.
                                int dummy=0;
                                //addLandMove(jump,last);
                                addLandMove(jump,jumpdest);
                                //stack.push(blocktype.IF);
                                regen(blocktype.IF,curt,jump);
                                //stack.pop();
                                curt = getTokAtRelativePos(jump,1);
                            }
                        }
                        else if(parent.type==blocktype.EXCEPT_CATCH)
                        {
                            //"except Exception" (except_cond) clause
                            int dummy=0;
                            //stack.push(blocktype.EXCEPT_COND);
                            regen(blocktype.EXCEPT_COND,curt,jump);
                            //stack.pop();
                            ////curt = last; //skip out immediately
                            //return jump; //skip out immediately
                            throw new FoundException(jump);
                        }
                        else
                        {
                            //if stmt or if clause of list comprehension?
                            //if(isBefore(jumpdest,jump))
                            //    m.throwUncaughtException("unexpected"); //this means this is a loop I guess.
                            //Tok nextStmtOutsideIfblock = jumpdest;

                            //int numLands = getLandsAttachedToTok(nextStmtOutsideIfblock);
                            //int extralands = numLands - 1; //we expect 1 LAND here
                            //now let's move them to the interior
                            //moveLands(extralands,nextStmtOutsideIfblock,jump);

                            //int jumpdest2 = jump.oi.pointerDest;
                            //int endofscope = last.oi.offset;
                            //if(jumpdest2>endofscope)
                            addLandMove(jump,jumpdest);

                            //now recurse
                            //Tok newfirst = curt;
                            //Tok newlast = jump;
                            //stack.push(blocktype.IF);
                            regen(blocktype.IF,curt,jump);
                            //stack.pop();
                            //curt = nextStmtOutsideIfblock;
                            //continue with else clause
                            curt = getTokAtRelativePos(jump,1);
                        }
                    }
                    else
                    {
                        //a short-circuit and/or but not inside an if expr.
                        //e.g. "x==3 and y = 5;"
                        curt = getTokAtRelativePos(curt,1);
                    }
                }
                //if(elsestart.oi.o==op.JUMP_IF_TRUE || elsestart.oi.o==op.JUMP_IF_FALSE)
                else if(elsestart.oi.o==op.ROT_TWO)
                {
                    //compare lists, e.g. a<b<c
                    //I believe we can safely ignore this and skip to elsestart
                    curt = elsestart;
                }
                else
                {
                    //short-circuit and/or
                    //just ignore it:
                    curt = getTokAtRelativePos(curt,1);
                }
            }
            else if(curt.oi.o==op.SETUP_LOOP)
            {
                //while loop or for loop
                Tok nextStmtAfterLoop = getTokPointedToByTok(curt);
                Tok tester = getTokAtRelativePosIgnoringLands(curt,1);
                if(tester.oi.o==op.JUMP_FORWARD)
                {
                    //special case for While(1) loops

                    //addLandMove(curt,last);

                    //while looks like a nested if statement
                    //ignore the JUMP_FORWARD token by setting that as the upper bound:
                    //stack.push(blocktype.LOOP);
                    //regenerateStructure2(tester,min(last,nextStmtAfterLoop)); //should hit an "if" statement.
                    //stack.pop();
                    //curt = nextStmtAfterLoop; //we're done so no need to continue

                    addLandMove(curt,nextStmtAfterLoop);
                    regen(blocktype.LOOP,tester,last);
                    curt = last;
                }
                else
                {
                    //regular while or for loop
                    addLandMove(curt,nextStmtAfterLoop);

                    //while looks like a nested if statement
                    //Tok start = curt;
                    //Tok end = last;
                    //stack.push(blocktype.LOOP);
                    regen(blocktype.LOOP,curt,last); //should hit an "if" statement.
                    //stack.pop();
                    //curt = nextStmtAfterLoop; //we're done so no need to continue
                    curt = last;
                }

            }
            else if(curt.oi.o==op.FOR_ITER)
            {
                //for loops and for clauses of list comprehensions. this should only occur inside a SETUP_LOOP
                if(parent.type==blocktype.LOOP)
                {
                    //for loop
                    Tok poptop = getTokPointedToByTok(curt); //also elsestart
                    Tok jump = getTokAtRelativePos(poptop,-2);
                    //curt is the source of extra LANDs.
                    //jump is the dest of extra LANDs.
                    //there should either be all the lands we need at dest, or none.  Because it's a JUMP, so they should have been redirected to the final destination.
                    //int numlandsAtSource = getLandsAttachedToTok(curt);
                    //int numlandsAtDest = getLandsAttachedToTok(jump); //should be 0 or all.
                    //int numlandsToMove = (numlandsAtDest==0)?1:0; //we shouldn't need to move more than one, I think.
                    //moveLands(numlandsToMove,curt,jump);
                    //recurse
                    //Tok start = curt;
                    //Tok end = jump;
                    //stack.push(blocktype.FOR);
                    regen(blocktype.FOR,curt,jump);
                    //stack.pop();
                    curt = getTokAtRelativePos(jump,1);
                }
                else
                {
                    //list comprehension?
                    Tok outofloop = getTokPointedToByTok(curt); //also elsestart
                    Tok jump = getTokAtRelativePos(outofloop,-2);
                    //stack.push(blocktype.LISTCOMP_FOR);
                    regen(blocktype.LISTCOMP_FOR,curt,jump);
                    //stack.pop();
                    curt = getTokAtRelativePos(jump,1);
                }
            }
            else if(curt.oi.o==op.SETUP_FINALLY)
            {
                //nothing needed for try finally blocks.
                curt = getTokAtRelativePos(curt,1);

                //try{
                //regen(blocktype.FINALLY,curt,last);
                //curt = last;
            }
            else if(curt.oi.o==op.SETUP_EXCEPT)
            {
                //try except except ... blocks.
                //we must recurse on the main section and each of the except blocks

                Tok poptop = getTokPointedToByTok(curt);
                Tok jump = getTokAtRelativePos(poptop,-2);
                //stack.push(blocktype.EXCEPT_MAIN);
                regen(blocktype.EXCEPT_MAIN,curt,jump);
                //stack.pop();

                Tok startOfElse = this.getTokPointedToByTok(jump);
                //scan the rest
                //stack.push(blocktype.EXCEPT_CATCH);
                //regenerateStructure2(jump,last);
                //stack.pop();
                ArrayList<Tok> jumps = new ArrayList();
                Tok starter = getTokAtRelativePosIgnoringLands(jump,1);
                while(true)
                {
                    if(starter.oi.o==op.DUP_TOP)
                    {
                        //except_cond
                        Tok curend;
                        Tok tryend;
                        //stack.push(blocktype.EXCEPT_CATCH);
                        //Tok curend = regen(blocktype.EXCEPT_CATCH,starter,min(startOfElse,last));

                        //do it without knowing where the end is:
                        tryend = last;
                        dontmove++;
                        curend = null;
                        try{
                            regen(blocktype.EXCEPT_CATCH,starter,tryend);
                        }catch(FoundException e){
                            curend = e.token;
                        }
                        if(curend==null)
                            m.throwUncaughtException("unexpected");
                        if(curend.oi.o!=op.JUMP_ABSOLUTE && curend.oi.o!=op.JUMP_FORWARD)
                            m.throwUncaughtException("unexpected");
                        dontmove--;

                        //do it again, knowing where the end is:
                        //tryend = getTokAtRelativePos(curend,1); //move forward one to include this token!
                        tryend = curend;
                        //curend = null;
                        try{
                            regen(blocktype.EXCEPT_CATCH,starter,tryend);
                        }catch(FoundException e){
                            curend = e.token;
                        }
                        if(curend!=tryend)
                            m.throwUncaughtException("unexpected");
                        if(curend.oi.o!=op.JUMP_ABSOLUTE && curend.oi.o!=op.JUMP_FORWARD)
                            m.throwUncaughtException("unexpected");


                        jumps.add(curend);
                        starter = getTokAtRelativePos(curend,3);
                        //stack.pop();
                    }
                    else if(starter.oi.o==op.POP_TOP)
                    {
                        //except
                        if(curt.getTokenIndex()==753)
                        {
                            int dummy=0;
                        }
                        //stack.push(blocktype.EXCEPT_CATCHALL);
                        //Tok curend = regen(blocktype.EXCEPT_CATCHALL,starter,min(startOfElse,last));
                        Tok curend;
                        Tok tryend;

                        //do it without knowing where the end is:
                        tryend = last;
                        dontmove++;
                        curend = null;
                        try{
                            regen(blocktype.EXCEPT_CATCHALL,starter,tryend);
                        }catch(FoundException e){
                            curend = e.token;
                        }
                        if(curend==null)
                            m.throwUncaughtException("unexpected");
                        if(curend.oi.o!=op.JUMP_ABSOLUTE && curend.oi.o!=op.JUMP_FORWARD)
                            m.throwUncaughtException("unexpected");
                        dontmove--;

                        //do it again, knowing where the end is:
                        //tryend = getTokAtRelativePos(curend,1); //move forward one to include this token!
                        tryend = curend;
                        //curend = null;
                        try{
                            regen(blocktype.EXCEPT_CATCHALL,starter,tryend);
                        }catch(FoundException e){
                            curend = e.token;
                        }
                        if(curend!=tryend)
                            m.throwUncaughtException("unexpected");
                        if(curend.oi.o!=op.JUMP_ABSOLUTE && curend.oi.o!=op.JUMP_FORWARD)
                            m.throwUncaughtException("unexpected");

                        jumps.add(curend);
                        starter = getTokAtRelativePosIgnoringLands(curend,1);
                        ////starter = curend;
                        //stack.pop();
                    }
                    else if(starter.oi.o==op.END_FINALLY)
                    {
                        //ending section
                        break;
                    }
                    else
                    {
                        m.throwUncaughtException("unexpected");
                    }
                }

                Tok elsestart = getTokAtRelativePos(starter,1); // =beginning of stmt* or =last
                Tok jumpdest = getTokPointedToByTok(jump);
                if(elsestart!=jumpdest)
                {
                    int dummy=0; //jumpdest should be the correct one.
                }
                addLandMove(jump,jumpdest);
                for(Tok j: jumps)
                {
                    Tok jdest = getTokPointedToByTok(j);
                    //addLandMove(j,last);
                    addLandMove(j,jdest);
                }

                curt = elsestart;
            }
            else if(curt.oi.o==op.JUMP_ABSOLUTE || curt.oi.o==op.JUMP_FORWARD)
            {
                //could be a continue statement or the end of the except clause
                //A continue stmt will always jump backwards (to the start of the loop), and the except clause will always jump forwards (past the else clause).
                //No, it might jump backwords if it's been redirected of course...
                //But an except clause is always followed by an END_FINALLY token!
                Tok dest = getTokPointedToByTok(curt);
                Tok tester = this.getTokAtRelativePosIgnoringLands(curt, 1);
                //if(isBefore(curt,dest))
                if(tester.oi.o==op.END_FINALLY)
                {
                    //end of except clause
                    //if(parent.type!=blocktype.EXCEPT_CATCHALL)
                    //    m.throwUncaughtException("unexpected");
                    if(!hasParentOfType(blocktype.EXCEPT_CATCHALL))
                        m.throwUncaughtException("unexpected");
                    //return curt;
                    throw new FoundException(curt);
                }
                else
                {
                    //should be continue, or possibly an optimised "if(expr and 1):"
                    //continue should always be JUMP_ABSOLUTE.
                    //so if it's JUMP_FORWARD, it should be an optimised short-circuit expr.
                    if(curt.oi.o!=op.JUMP_ABSOLUTE)
                    {
                        //m.throwUncaughtException("unexpected"); //this could be okay, but I don't have it in the grammar file either.
                        //m.warn("Experimental optimised short-circuit expr.");
                        //fixes Myst5's xLinkingBookGUIPopup and Moul/Mqo's xPsnlVaultSDL
                        //change it to a LOAD_CONST (which is unoptimised) and kill its LAND.
                        Tok land = tryToFindLand(curt);
                        boolean successfullyremoved = ts.remove(land);
                        if(!successfullyremoved)
                            m.throwUncaughtException("unexpected");
                        //Tok newt = Tok.customToken(null, ts., last);
                        //curt.oi.o = op.LOAD_CONST;
                        //curt.oi.pattr = 1;
                        Tok newt = Tok.fakeToken(op.LOAD_CONST, curt.getPrsStream(), curt.oi.offset, PyInt.create(1));
                        replaceToken(curt, newt); //this will renumber too.
                        curt = newt;
                        //renumber();

                        //move LAND //no don't do this, the LAND is correct, if we interpret as:
                        //  if x==3:
                        //      if 1:
                        //m.warn("Experimental short-circuit optimisation support");
                        //Tok jumpiffalse = getTokAtRelativePosIgnoringLands(curt, -2);
                        //Tok nexttok = getTokAtRelativePosIgnoringLands(curt, 1);
                        //addLandMove(jumpiffalse,nexttok);

                    }
                    curt = getTokAtRelativePos(curt,1);
                }
            }
            else if(curt.oi.o==op.END_FINALLY)
            {
                //this is the end of a finally block, do nothing?
                ////this is inside an except statement.
                //return curt;

                //throw new FoundException(curt);

                curt = getTokAtRelativePos(curt,1);
            }
            else
            {
                //move forward one token
                Tok newcurt = getTokAtRelativePos(curt,1);
                curt = newcurt;
            }

            //if(curt==last)
            //    break;
            
            if(!before(curt,last))
                break;

        }

        return null;

    }
    /*private Tok regenerateStructure2(Tok first, Tok last)
    {
        //m.throwUncaughtException();
        
        //m.msg("Regen: from token "+Integer.toString(first.getTokenIndex())+" to "+Integer.toString(last.getTokenIndex())+"  stack: "+stack.toString());

        if(code.name.toString().equals("PyString: compile_dir"))
        {
            int d3 = 0;
        }

        //int pos = 0;
        Tok curt = getTokAtRelativePos(first,1);
        while(true)
        {
            //search for start of next block
            //Tok curt = code.tokens.get(pos);

            if(curt.getTokenIndex()>1485) //1487
            {
                int d4 = 0;
            }

            if(curt.oi.o==op.JUMP_IF_TRUE || curt.oi.o==op.JUMP_IF_FALSE)
            {
                //if statement and short-circuitAndOr exprs, and inner structure of while stmt
                PyCode dummy2 = code;
                Tok elsestart = getTokPointedToByTok(curt);
                //elsestart is the elseblock if this is an ifstmt, and to a JUMP_IF_TRUE or JUMP_IF_FALSE if this is a short-circuit and/or expr.
                //so if elsestart is JUMP_IF_TRUE or JUMP_IF_FALSE
                //wait, no, it can be something else if it is say (not(a or b))
                if(elsestart.oi.o==op.POP_TOP)
                {
                    Tok jump = getTokAtRelativePos(elsestart,-2);
                    if(jump.oi.o==op.JUMP_ABSOLUTE || jump.oi.o==op.JUMP_FORWARD)
                    {
                        Tok jumpdest = getTokPointedToByTok(jump);
                        if(stack.peekFirst()==blocktype.LOOP)
                        {
                            //inner structure of while block.
                            //if(isBefore(jump,jumpdest))
                            //    m.throwUncaughtException("unexpected"); //this means this is a regular if stmt I guess.
                            //Tok expr = jumpdest;
                            //recurse on loop body
                            //Tok start = curt;
                            //Tok end = jump;
                            Tok tester = getTokAtRelativePos(elsestart,1);
                            if(tester.oi.o==op.POP_BLOCK)
                            {
                                //then this is the main if stmt, and so this loop is a while loop.
                                stack.push(blocktype.WHILEIF); //is this the correct type?
                                regenerateStructure2(curt,jump);
                                stack.pop();
                                //continue with else clause
                                curt = getTokAtRelativePos(jump,1);
                            }
                            else
                            {
                                //otherwise it's just an if stmt in a loop.
                                fallLand(jump,last,first,last);
                                stack.push(blocktype.IF);
                                regenerateStructure2(curt,jump);
                                stack.pop();
                                curt = getTokAtRelativePos(jump,1);
                            }
                        }
                        else if(stack.peekFirst()==blocktype.EXCEPT_CATCH)
                        {
                            //"except Exception" (except_cond) clause
                            stack.push(blocktype.EXCEPT_COND);
                            regenerateStructure2(curt,jump);
                            stack.pop();
                            //curt = last; //skip out immediately
                            return jump; //skip out immediately
                        }
                        else
                        {
                            //if stmt or if clause of list comprehension?
                            //if(isBefore(jumpdest,jump))
                            //    m.throwUncaughtException("unexpected"); //this means this is a loop I guess.
                            Tok nextStmtOutsideIfblock = jumpdest;

                            //int numLands = getLandsAttachedToTok(nextStmtOutsideIfblock);
                            //int extralands = numLands - 1; //we expect 1 LAND here
                            //now let's move them to the interior
                            //moveLands(extralands,nextStmtOutsideIfblock,jump);

                            //int jumpdest2 = jump.oi.pointerDest;
                            //int endofscope = last.oi.offset;
                            //if(jumpdest2>endofscope)
                            fallLand(jump,last,first,last);

                            //now recurse
                            //Tok newfirst = curt;
                            //Tok newlast = jump;
                            stack.push(blocktype.IF);
                            regenerateStructure2(curt,jump);
                            stack.pop();
                            //curt = nextStmtOutsideIfblock;
                            //continue with else clause
                            curt = getTokAtRelativePos(jump,1);
                        }
                    }
                    else
                    {
                        //unknown?
                        //could be a short-circuit and/or but not inside an if expr.
                        //e.g. "x==3 and y = 5;"
                        curt = getTokAtRelativePos(curt,1);
                    }
                }
                //if(elsestart.oi.o==op.JUMP_IF_TRUE || elsestart.oi.o==op.JUMP_IF_FALSE)
                else if(elsestart.oi.o==op.ROT_TWO)
                {
                    //compare lists, e.g. a<b<c
                    //I believe we can safely ignore this and skip to elsestart
                    curt = elsestart;
                }
                else
                {
                    //short-circuit and/or
                    //just ignore it:
                    curt = getTokAtRelativePos(curt,1);
                }
            }
            else if(curt.oi.o==op.SETUP_LOOP)
            {
                //while loop or for loop
                Tok nextStmtAfterLoop = getTokPointedToByTok(curt);
                Tok tester = getTokAtRelativePosIgnoringLands(curt,1);
                if(tester.oi.o==op.JUMP_FORWARD)
                {
                    //special case for While(1) loops

                    fallLand(curt,last,first,last);

                    //while looks like a nested if statement
                    stack.push(blocktype.LOOP);
                    //ignore the JUMP_FORWARD token by setting that as the upper bound:
                    regenerateStructure2(tester,min(last,nextStmtAfterLoop)); //should hit an "if" statement.
                    stack.pop();
                    curt = nextStmtAfterLoop; //we're done so no need to continue
                }
                else
                {
                    //regular while or for loop
                    fallLand(curt,last,first,last);

                    //while looks like a nested if statement
                    //Tok start = curt;
                    //Tok end = last;
                    stack.push(blocktype.LOOP);
                    regenerateStructure2(curt,min(last,nextStmtAfterLoop)); //should hit an "if" statement.
                    stack.pop();
                    curt = nextStmtAfterLoop; //we're done so no need to continue
                }

            }
            else if(curt.oi.o==op.FOR_ITER)
            {
                //for loops and for clauses of list comprehensions. this should only occur inside a SETUP_LOOP
                if(stack.peekFirst()==blocktype.LOOP)
                {
                    //for loop
                    Tok poptop = getTokPointedToByTok(curt); //also elsestart
                    Tok jump = getTokAtRelativePos(poptop,-2);
                    //curt is the source of extra LANDs.
                    //jump is the dest of extra LANDs.
                    //there should either be all the lands we need at dest, or none.  Because it's a JUMP, so they should have been redirected to the final destination.
                    //int numlandsAtSource = getLandsAttachedToTok(curt);
                    //int numlandsAtDest = getLandsAttachedToTok(jump); //should be 0 or all.
                    //int numlandsToMove = (numlandsAtDest==0)?1:0; //we shouldn't need to move more than one, I think.
                    //moveLands(numlandsToMove,curt,jump);
                    //recurse
                    //Tok start = curt;
                    //Tok end = jump;
                    stack.push(blocktype.FOR);
                    regenerateStructure2(curt,jump);
                    stack.pop();
                    curt = getTokAtRelativePos(jump,1);
                }
                else
                {
                    //list comprehension?
                    Tok outofloop = getTokPointedToByTok(curt); //also elsestart
                    Tok jump = getTokAtRelativePos(outofloop,-2);
                    stack.push(blocktype.LISTCOMP_FOR);
                    regenerateStructure2(curt,jump);
                    stack.pop();
                    curt = getTokAtRelativePos(jump,1);
                }
            }
            else if(curt.oi.o==op.SETUP_FINALLY)
            {
                //nothing needed for try finally blocks.
                curt = getTokAtRelativePos(curt,1);
            }
            else if(curt.oi.o==op.SETUP_EXCEPT)
            {
                //try except except ... blocks.
                //we must recurse on the main section and each of the except blocks

                Tok poptop = getTokPointedToByTok(curt);
                Tok jump = getTokAtRelativePos(poptop,-2);
                stack.push(blocktype.EXCEPT_MAIN);
                regenerateStructure2(curt,jump);
                stack.pop();

                Tok startOfElse = this.getTokPointedToByTok(jump);
                //scan the rest
                //stack.push(blocktype.EXCEPT_CATCH);
                //regenerateStructure2(jump,last);
                //stack.pop();
                ArrayList<Tok> jumps = new ArrayList();
                Tok starter = getTokAtRelativePosIgnoringLands(jump,1);
                while(true)
                {
                    if(starter.oi.o==op.DUP_TOP)
                    {
                        //except_cond
                        stack.push(blocktype.EXCEPT_CATCH);
                        Tok curend = regenerateStructure2(starter,min(startOfElse,last));
                        jumps.add(curend);
                        starter = getTokAtRelativePos(curend,3);
                        stack.pop();
                    }
                    else if(starter.oi.o==op.POP_TOP)
                    {
                        //except
                        if(curt.getTokenIndex()==753)
                        {
                            int dummy=0;
                        }
                        stack.push(blocktype.EXCEPT_CATCHALL);
                        Tok curend = regenerateStructure2(starter,min(startOfElse,last));
                        jumps.add(curend);
                        starter = getTokAtRelativePos(curend,1);
                        //starter = curend;
                        stack.pop();
                    }
                    else if(starter.oi.o==op.END_FINALLY)
                    {
                        //ending section
                        break;
                    }
                    else
                    {
                        m.throwUncaughtException("unexpected");
                    }
                }

                Tok elsestart = getTokAtRelativePos(starter,1); // =beginning of stmt* or =last
                fallLand(jump,elsestart,first,last);
                for(Tok j: jumps)
                {
                    fallLand(j,last,first,last);
                }

                curt = elsestart;
            }
            else if(curt.oi.o==op.JUMP_ABSOLUTE || curt.oi.o==op.JUMP_FORWARD)
            {
                //could be a continue statement or the end of the except clause
                //A continue stmt will always jump backwards (to the start of the loop), and the except clause will always jump forwards (past the else clause).
                //No, it might jump backwords if it's been redirected of course...
                //But an except clause is always followed by an END_FINALLY token!
                Tok dest = getTokPointedToByTok(curt);
                Tok tester = this.getTokAtRelativePos(curt, 1);
                //if(isBefore(curt,dest))
                if(tester.oi.o==op.END_FINALLY)
                {
                    //end of except clause
                    if(stack.peekFirst()!=blocktype.EXCEPT_CATCHALL)
                        m.throwUncaughtException("unexpected");
                    return curt;
                }
                else
                {
                    //should be continue
                    if(curt.oi.o!=op.JUMP_ABSOLUTE)
                        m.throwUncaughtException("unexpected"); //this could be okay, but I don't have it in the grammar file either.
                    curt = getTokAtRelativePos(curt,1);
                }
            }
            else if(curt.oi.o==op.END_FINALLY)
            {
                //this is inside an except statement.
                return curt;
            }
            else
            {
                //move forward one token
                Tok newcurt = getTokAtRelativePos(curt,1);
                curt = newcurt;
            }

            //if(curt.equals(last))
            //    break;
            if(!isBefore(curt,last))
                break;

        }
        return null;
    }*/
    private Tok min(Tok t1, Tok t2)
    {
        int i1 = t1.getTokenIndex();
        int i2 = t2.getTokenIndex();
        if(i1<i2) return t1;
        else return t2;
    }
    private Tok max(Tok t1, Tok t2)
    {
        int i1 = t1.getTokenIndex();
        int i2 = t2.getTokenIndex();
        if(i1>i2) return t1;
        else return t2;
    }
    /*private void fallLand(Tok jump, Tok dest, Tok first, Tok last)
    {
        //moves the LAND token associated with Jump and attaches it to Last.
        final boolean ensureRange = true; //checks to see if this points outside the scope before moving it.

        int jumpoffset = jump.oi.offset;
        int destoffset = jump.oi.pointerDest;
        int startofscopeOffset = first.oi.offset;
        int endofscopeOffset = last.oi.offset;

        if(ensureRange)
        {
            //if((startofscopeOffset==-1 || startofscopeOffset<=destoffset) && (endofscopeOffset==-1 || destoffset<=endofscopeOffset))
            if((startofscopeOffset==-1 || startofscopeOffset<destoffset) && (endofscopeOffset==-1 || destoffset<endofscopeOffset))
            {
                //int dummy=0;
                return; //no need to move a LAND.
            }
        }

        //find the Land that belongs to Jump:
        Tok land = null;
        for(Tok t: ts)
        {
            if(t.oi.o==op.LAND)
            {
                int curLandOffset = (Integer)t.oi.pattr;
                if(jumpoffset==curLandOffset)
                {
                    land = t;
                    break;
                }
            }
        }
        if(land==null)
            m.throwUncaughtException("unexpected");
        //move the land
        moveToken(land,dest);
    }*/
    /*private void regenerateStructure2(Tok first, Tok last)
    {
        String msg = "Regen: from token "+Integer.toString(first.getTokenIndex())+" to "+Integer.toString(last.getTokenIndex());
        msg += "  stack: "+stack.toString();
        m.msg(msg);

        //int pos = 0;
        Tok curt = first;
        while(true)
        {
            //search for start of next block
            //Tok curt = code.tokens.get(pos);

            if(curt.oi.o==op.JUMP_IF_TRUE || curt.oi.o==op.JUMP_IF_FALSE)
            {
                //if statement and short-circuitAndOr exprs, and inner structure of while stmt
                Tok elsestart = getTokPointedToByTok(curt);
                //elsestart is the elseblock if this is an ifstmt, and to a JUMP_IF_TRUE or JUMP_IF_FALSE if this is a short-circuit and/or expr.
                //so if elsestart is JUMP_IF_TRUE or JUMP_IF_FALSE
                //wait, no, it can be something else if it is say (not(a or b))
                if(elsestart.oi.o==op.POP_TOP)
                {
                    Tok jump = getTokAtRelativePos(elsestart,-2);
                    Tok jumpdest = getTokPointedToByTok(jump);
                    if(jump.oi.o==op.JUMP_ABSOLUTE || jump.oi.o==op.JUMP_FORWARD)
                    {
                        if(stack.peekFirst()==blocktype.LOOP)
                        {
                            //inner structure of while block.
                            if(isBefore(jump,jumpdest))
                                m.throwUncaughtException("unexpected"); //this means this is a regular if stmt I guess.
                            Tok expr = jumpdest;
                            //recurse on loop body
                            Tok start = getTokAtRelativePos(curt,1);
                            Tok end = getTokAtRelativePos(jump,-1);
                            stack.push(blocktype.IF); //is this the correct type?
                            regenerateStructure2(start,end);
                            stack.pop();
                            //continue with else clause
                            curt = elsestart;
                        }
                        else
                        {
                            //if stmt
                            //if(isBefore(jumpdest,jump))
                            //    m.throwUncaughtException("unexpected"); //this means this is a loop I guess.
                            Tok nextStmtOutsideIfblock = jumpdest;
                            int numLands = getLandsAttachedToTok(nextStmtOutsideIfblock);
                            int extralands = numLands - 1; //we expect 1 LAND here
                            //now let's move them to the interior
                            moveLands(extralands,nextStmtOutsideIfblock,jump);
                            //now recurse
                            Tok newfirst = getTokAtRelativePos(curt,1);
                            Tok newlast = getTokAtRelativePos(nextStmtOutsideIfblock,-1);
                            stack.push(blocktype.IF);
                            regenerateStructure2(newfirst,newlast);
                            stack.pop();
                            //curt = nextStmtOutsideIfblock;
                            //continue with else clause
                            curt = elsestart;
                        }
                    }
                    else
                    {
                        //unknown?
                        int dummy=0;
                    }
                }
                //if(elsestart.oi.o==op.JUMP_IF_TRUE || elsestart.oi.o==op.JUMP_IF_FALSE)
                else
                {
                    //short-circuit and/or
                    //just ignore it:
                    curt = getTokAtRelativePos(curt,1);
                }
            }
            else if(curt.oi.o==op.SETUP_LOOP)
            {
                //while loop or for loop
                Tok nextStmtAfterLoop = getTokPointedToByTok(curt);
                Tok jump1 = getTokAtRelativePosIgnoringLands(curt,2);
                if(jump1.oi.o==op.JUMP_FORWARD)
                {
                    //special case for While(1) loops
                    m.throwUncaughtException("unhandled");
                }
                //while looks like a nested if statement
                Tok start = getTokAtRelativePos(curt,1);
                Tok end = getTokAtRelativePos(nextStmtAfterLoop,-1);
                stack.push(blocktype.LOOP);
                regenerateStructure2(start,end); //should hit an "if" statement.
                stack.pop();
                curt = nextStmtAfterLoop;
            }
            else if(curt.oi.o==op.FOR_ITER)
            {
                //for loop. this should only occur inside a SETUP_LOOP
                if(stack.peekFirst()!=blocktype.LOOP)
                    m.throwUncaughtException("unexpected");

                Tok poptop = getTokPointedToByTok(curt); //also elsestart
                Tok jump = getTokAtRelativePos(poptop,-2);
                //curt is the source of extra LANDs.
                //jump is the dest of extra LANDs.
                //there should either be all the lands we need at dest, or none.  Because it's a JUMP, so they should have been redirected to the final destination.
                int numlandsAtSource = getLandsAttachedToTok(curt);
                int numlandsAtDest = getLandsAttachedToTok(jump); //should be 0 or all.
                int numlandsToMove = (numlandsAtDest==0)?1:0; //we shouldn't need to move more than one, I think.
                moveLands(numlandsToMove,curt,jump);
                //recurse
                Tok start = getTokAtRelativePos(curt,1);
                Tok end = getTokAtRelativePos(jump,-1);
                stack.push(blocktype.FOR);
                regenerateStructure2(start,end);
                stack.pop();
                curt = getTokAtRelativePos(jump,1);
            }
            else
            {
                //move forward one token
                Tok newcurt = getTokAtRelativePos(curt,1);
                curt = newcurt;
            }

            //if(curt.equals(last))
            //    break;
            if(!isBefore(curt,last))
                break;
            
        }
    }*/
    private boolean before(Tok shouldbebefore, Tok shouldbeafter)
    {
        int befind = shouldbebefore.getTokenIndex();
        int aftind = shouldbeafter.getTokenIndex();
        boolean r = befind<aftind;
        return r;
    }
    private boolean after(Tok shouldbeafter, Tok shouldbebefore)
    {
        int befind = shouldbebefore.getTokenIndex();
        int aftind = shouldbeafter.getTokenIndex();
        boolean r = befind<aftind;
        return r;
    }
    private void moveLands(int num, Tok from, Tok to)
    {
        if(num==0) return;

        //if(isBefore(from,to))
        //    m.throwUncaughtException("unhandled");
        
        //this method modifies the token list.
        int fromind = from.getTokenIndex();
        int toind = to.getTokenIndex();
        //if(fromind<=toind) m.throwUncaughtException("unhandled");
        //Vector<Tok> lands = new Vector();
        if(fromind>toind)
        {
            for(int i=0;i<num;i++)
            {
                Tok l = ts.remove(fromind-1-i);
                l.debugstr = "(moved)";
                ts.add(toind, l);
            }
        }
        else
        {
            //eg with for stmts
            for(int i=0;i<num;i++)
            {
                Tok l = ts.remove(fromind-1-i);
                l.debugstr = "(moved)";
                ts.add(toind-1, l);
            }
        }

        renumber();  //this could be more efficient; just renumber the needed range.
    }
    private void moveToken(Tok from, Tok to)
    {
        //this method modifies the token list.

        int fromind = from.getTokenIndex();
        int toind = to.getTokenIndex();

        if(fromind>toind)
        {
            Tok l = ts.remove(fromind);
            l.debugstr = "(moved)";
            ts.add(toind, l);
        }
        else if(fromind<toind)
        {
            Tok l = ts.remove(fromind);
            l.debugstr = "(moved)";
            ts.add(toind-1, l);
        }
        //else just leave it where it is.

        renumber();  //this could be more efficient; just renumber the needed range.
    }
    private void renumber()
    {
        int i=0;
        for(Tok t: ts)
        {
            t.setTokenIndex(i);
            i++;
        }
    }
    private Tok getTokPointedToByTok(Tok pointer)
    {
        if(pointer.oi==null || pointer.oi.pointerDest==null)
        {
            int dummy=0;
        }
        int offset = pointer.oi.pointerDest;
        for(Tok t: ts)
        {
            int to = t.oi.offset;
            if(to>=offset)
            {
                if(to==offset) return t;
                m.throwUncaughtException("unexpected");
                //it should be EXPR_LIST...
                if(t.oi.o!=op.EXPR_LIST) m.throwUncaughtException("unexpected");
                //it should be the first item in the list.
                java.util.List<Ast> list = (java.util.List<Ast>)t.oi.pattr;
                Tok first = (Tok)list.get(0);
                if(first.oi.offset!=offset) m.throwUncaughtException("unexpected");
                return t; //should we return 'first' instead?  Probably not.
            }

        }

        throw new shared.uncaughtexception("unexpected");
        //This could be *lots* of trouble.  This only happened after the EXPR_LIST fake token, because now some of these tokens can be tied up inside of it.
        //So the next token should be an EXPR_LIST.  Let's do this above.

    }
    private int getLandsAttachedToTok(Tok token)
    {
        int pos = ts.indexOf(token);
        int numlands = 0;
        while(true)
        {
            pos--;
            Tok t = ts.get(pos);
            if(t.oi.o==op.LAND)
            {
                numlands++;
            }
            else
            {
                break;
            }
        }
        return numlands;
    }
    private Tok getTokAtRelativePos(Tok starttok, int relpos)
    {
        int ind = ts.indexOf(starttok);
        ind = ind + relpos;
        if(ind<0 || ind>=ts.size())
        {
            int dummy=0;
        }
        Tok r = ts.get(ind);
        return r;
    }
    private Tok getTokAtRelativePosIgnoringLands(Tok starttok, int relpos)
    {
        int ind = ts.indexOf(starttok);
        int count = 0;
        if(relpos>0)
        {
            while(true)
            {
                ind = ind + 1;
                Tok r = ts.get(ind);
                if(r.oi.o!=op.LAND) count++;
                if(count==relpos) return r;
            }
        }
        else
        {
            while(true)
            {
                ind = ind - 1;
                Tok r = ts.get(ind);
                if(r.oi.o!=op.LAND) count--;
                if(count==relpos) return r;
            }
        }
        //return null;
    }





    //private Tok getTokAtOffset(int offset)
    //{
    //    for(Tok t: code.tokens)
    //    {
    //        if(t.oi.offset==offset) return t;
    //    }
    //    throw new shared.uncaughtexception("unexpected");
    //}

}
