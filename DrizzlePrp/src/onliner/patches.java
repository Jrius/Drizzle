/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package onliner;

//these '+'s get optimized to a single string.

public class patches
{
    public static class patch
    {
        String old_str;
        String new_str;
        public patch(String old_str, String new_str)
        {
            this.old_str = old_str;
            this.new_str = new_str;
        }
    }
    public static final String[] problems = {
        //"linkMgr.linkToAge(", //this should be replaced by the patches.
        //"ptNetLinkingMgr()",
        ".linkToAge(",
        "exec(", //can be difficult to verify safety, but can probably be let slide sometimes.
        //"eval(", //shows up in the glue code, but we must still be careful.
        "os.system(", //should definitely not be invoking command-line stuff.
        "urllib.", //network connection
        "urllib2.", //network connection
    };
    public static final patch[] patches = {

        /*new patch(
            "        als = ptAgeLinkStruct()\n"+
            "        ainfo = ptAgeInfoStruct()\n"+
            "        ainfo.setAgeFilename(ageName)\n"+
            "        ainfo.setAgeInstanceName(self.IConvertAgeInstanceName(ageName))\n"+
            "        als.setAgeInfo(ainfo)\n"+
            "        if ((type(spTitle) == type(None)) or (len(spTitle) == 0)):\n"+
            "            if ((linkRule == PtLinkingRules.kOriginalBook) or PtIsSinglePlayerMode()):\n"+
            "                if (spawnPoint == 'LinkInPointDefault'):\n"+
            "                    spTitle = 'Default'\n"+
            "                else:\n"+
            "                    print 'Warning: Empty spawnpoint title not allowed, check your link destinations!'\n"+
            "                    return\n"+
            "            else:\n"+
            "                print 'Empty spawnpoint title allowed, continue linking'\n"+
            "                spTitle = ''\n"+
            "        als.setLinkingRules(linkRule)\n"+
            "        spPoint = ptSpawnPointInfo(spTitle, spawnPoint)\n"+
            "        als.setSpawnPoint(spPoint)\n"+
            "        linkMgr = ptNetLinkingMgr()\n"+
            "        linkMgr.linkToAge(als)\n"+
            "        print ('Linking to age %s, spawnpoint %s with title %s, using linkingrule %d' % (ageName, spawnPoint, spTitle, linkRule))\n"
        ,
            "        import uam\n"+
            "        uam.LinkToAge(ageName, spawnPoint)\n"
        ),*/ //should be covered by new rule below
        new patch(
            "                linkMgr = ptNetLinkingMgr()\n"+
            "                linkMgr.linkToAge(ageLinkStruct)\n"
        ,
            "                import uam\n"+
            "                uam.LinkToAge(ageLinkStruct.getAgeInfo().getAgeFilename(), ageLinkStruct.getSpawnPoint().getName())\n"
        ),
        new patch(
            "            mgr = ptNetLinkingMgr()\n"+
            "            mgr.setEnabled(1)\n"+
            "            mgr.linkToAge(link)\n"
        ,
            "            import uam\n"+
            "            uam.LinkToAge(link.getAgeInfo().getAgeFilename(), link.getSpawnPoint().getName())\n"
        ),
        new patch(
            "                        linkMgr = ptNetLinkingMgr()\n"+
            "                        linkMgr.linkToAge(als)\n"
        ,
            "                        import uam\n"+
            "                        uam.LinkToAge(als.getAgeInfo().getAgeFilename(), als.getSpawnPoint().getName())\n"
        ),
        new patch(
            "        linkMgr = ptNetLinkingMgr()\n"+
            "        linkMgr.linkToAge(als)\n"
        ,
            "        import uam\n"+
            "        uam.LinkToAge(als.getAgeInfo().getAgeFilename(), als.getSpawnPoint().getName())\n"
        ),

};

}
