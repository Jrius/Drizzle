/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import java.util.Set;
import java.util.HashSet;

public class ageLists
{
    public static final String[] potsages = {
        "Ahnonay","AhnySphere01","AhnySphere02","AhnySphere03","AhnySphere04","AvatarCustomization",
        "BahroCave","BahroCave02","BaronCityOffice","city","Cleft","CustomAvatars",
        "Descent","DniCityX2Finale","Ercana","ErcanaCitySilo","Garden","Garrison",
        "Gira","GlobalAnimations","GlobalAvatars","GlobalClothing","GreatZero",
        "GUI","Kadish","Kveer","Myst","Neighborhood","Neighborhood02","Nexus",
        "Personal","Personal02","RestorationGuild","spyroom","Teledahn",
    };
    
    public static final String[] convertedages = {
        //Moul
        "AhnonayMOUL","Dereno","EderDelin","EderTsogal","GreatTreePub","GuildPub-Cartographers","GuildPub-Greeters","GuildPub-Maintainers","GuildPub-Messengers","GuildPub-Writers","Jalak","KirelMOUL","KveerMOUL","LiveBahroCaves","Minkata","Negilahn","NeighborhoodMOUL","Payiferen","Tetsonot",
        //Myst5
        "DescentMystV","Direbo","KveerMystV","Laki","MystMystV","Siralehn","Tahgira","Todelmer",
        //Crowthistle
        "MarshScene","MountainScene",
        //Hexisle
        "CatfishCanyon","DessertDesert","LouderSpace","MoldyDungeon","PlasmaMiasma","PumpkinJungle",
        //Magiquest
        "Courtyard","ForestMQ","PortalWell",
    };

    public static final String[] othercyanages = {
        "GlobalMarkers", //not in Pots, but in UU, e.g.
    };

    public static Set<String> AllCyanAges()
    {
        HashSet<String> r = new HashSet();
        for(String age: potsages) r.add(age);
        for(String age: convertedages) r.add(age);
        for(String age: othercyanages) r.add(age);
        return r;
    }
}
