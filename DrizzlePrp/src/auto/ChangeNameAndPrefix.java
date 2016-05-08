/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import prpobjects.prpfile;
import prpobjects.Pageid;
import prpobjects.Urustring;
import prpobjects.Uruobjectdesc;
import shared.m;
import prpobjects.Typeid;
import prpobjects.PrpRootObject;

public class ChangeNameAndPrefix
{
    public static void ChangeNameAndPrefix(String inputfilename, String outputfolder, String newname, String newprefix, boolean doRenamePythonAndOgg)
    {
        //read old one:
        prpfile prp = prpfile.createFromFile(inputfilename, false);

        //change name:
        ChangeName(prp,newname,doRenamePythonAndOgg);

        //change prefix:
        int newpre = Integer.parseInt(newprefix);
        ChangePrefix(prp,newpre);

        //save it!
        String outputfilename = outputfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);

    }
    public static void ChangeName(String inputfilename, String outputfolder, String newname, boolean doRenamePython)
    {
        //m.msg("Not currently changing Python refs.");

        //read old one:
        prpfile prp = prpfile.createFromFile(inputfilename, true);

        //change the name
        ChangeName(prp,newname,doRenamePython);

        //save it!
        String outputfilename = outputfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);

    }
    public static void ChangePrefix(String inputfilename, String outputfolder, String newprefix)
    {
        //read old one:
        prpfile prp = prpfile.createFromFile(inputfilename, false);

        if (newprefix.startsWith("\\"))
            newprefix = newprefix.substring(1);
        int newpre = Integer.parseInt(newprefix);
        ChangePrefix(prp,newpre);

        //save it!
        String outputfilename = outputfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);
    }
    public static void ChangePagename(String inputfilename, String outputfolder, String newname)
    {

        //read old one:
        prpfile prp = prpfile.createFromFile(inputfilename, true);

        prp.header.pagename = Urustring.createFromString(newname);

        //save it!
        String outputfilename = outputfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);

    }
    public static void ChangePagenumber(String inputfilename, String outputfolder, String newnumber)
    {

        //read old one:
        prpfile prp = prpfile.createFromFile(inputfilename, false);

        int newnum = Integer.parseInt(newnumber);
        ChangePagenumber(prp,newnum);

        //save it!
        String outputfilename = outputfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);

    }
    public static void ChangePagenumber(prpfile prp, int newnum)
    {
        Pageid oldpid = prp.header.pageid.deepClone();
        prp.header.pageid.setPagenum(newnum);
        for(Uruobjectdesc desc: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, prp))
        {
            if(desc.pageid.getPageNumber()==oldpid.getPageNumber())
            {
                desc.pageid.setPagenum(newnum);
            }
        }
    }
    public static void ChangePrefix(prpfile prp, int newpre)
    {
        //calculate new values:
        //int newpre = Integer.parseInt(newprefix);
        Pageid oldpid = prp.header.pageid.deepClone();


        prp.header.pageid.setSequencePrefix(newpre);

        //change uruobjectdesc entries:
        for(Uruobjectdesc desc: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, prp))
        {
            if(desc.pageid.getSequencePrefix()==oldpid.getSequencePrefix())
            {
                desc.pageid.setSequencePrefix(newpre);
            }
        }
    }
    public static void ChangeName(prpfile prp, String newname, boolean doRenamePythonAndOgg)
    {
        //calculate new values:
        String oldname = prp.header.agename.toString();
        Urustring newnam = Urustring.createFromString(newname);

        //change header entries:
        prp.header.agename = newnam;

        if(doRenamePythonAndOgg)
        {
            //rename python files
            for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plPythonFileMod))
            {
                prpobjects.plPythonFileMod pfm = ro.castTo();
                String curpyfile = pfm.pyfile.toString();
                boolean ignore = false;
                for(String py: auto.fileLists.partialListOfSharedPythonFiles)
                {
                    if(curpyfile.equals(py))
                    {
                        ignore = true;
                        break;
                    }
                }
                if(!ignore)
                {
                    String newpyfile;
                    if(ro.header.desc.objectname.toString().equals("VeryVerySpecialPythonFileMod"))
                        newpyfile = newname;
                    else
                        newpyfile = newname+"_"+curpyfile;
                    m.msg("Changing Python File from "+curpyfile+" to "+newpyfile);
                    pfm.pyfile = Urustring.createFromString(newpyfile);
                    ro.markAsChanged();
                }
            }
            //rename sounds
            for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSoundBuffer))
            {
                prpobjects.plSoundBuffer sb = ro.castTo();
                String curoggfile = sb.oggfile.toString();
                boolean ignore = false;
                for(String ogg: auto.fileLists.partialListofSharedSoundFiles)
                {
                    if(curoggfile.equals(ogg))
                    {
                        ignore = true;
                        break;
                    }
                }
                if(!ignore)
                {
                    String newoggfile = newname+"_"+curoggfile;
                    m.msg("Changing Ogg file from "+curoggfile+" to "+newoggfile);
                    sb.oggfile = Urustring.createFromString(newoggfile);
                    ro.markAsChanged();
                }
            }
            m.msg("(If any of these renamings should not happen(i.e. because it is a Pots file), that filename should be added to the list of shared files in the Drizzle source.)");
        }
    }
}
