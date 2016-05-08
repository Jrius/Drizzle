/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

/**
 *
 * @author user
 */
public enum Game
{
    pots(3),
    crowthistle(4),
    mystv(4),
    hexisle(7),
    moul(6),
    mqo(8),
    ;
    //3==pots,6==moul,7==hexisle,5==myst5,4==crowthistle(crowthistle&myst5 are the same),8==mqo

    final public int readversion;

    Game(int readversion)
    {
        this.readversion = readversion;
    }

    public static String getAllGamenames()
    {
        StringBuilder r = new StringBuilder();
        Game[] games = Game.values();
        for(int i=0;i<games.length;i++)
        {
            if(i!=0) r.append(", ");
            r.append(games[i].toString());
        }
        return r.toString();
    }
    public static Game getFromReadversion(int readversion)
    {
        //note that this may return crowthistle instead of mystv, e.g.
        for(Game game: Game.values())
        {
            if(game.readversion==readversion) return game;
        }

        throw new shared.uncaughtexception("unexpected");
    }
    public static Game getFromName(String gamename)
    {
        gamename = gamename.toLowerCase();
        Game r = Game.valueOf(gamename);
        return r;
    }
}

