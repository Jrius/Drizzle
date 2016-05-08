/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

//this enum lists the possible read/write formats, and can contain many things from many contexts.

public enum Format
{
    //General formats:
    none,

    //Plasma formats:
    pots,
    moul, //this is newer that crowthistle/myst5, but is a mix of older and newer code, so it's hard to place.
    crowthistle,
    //myst5, //=crowthistle format, so far as I know.
    hexisle,
    mqo,
    realmyst,

}
