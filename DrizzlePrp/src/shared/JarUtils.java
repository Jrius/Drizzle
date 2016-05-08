/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.Vector;
import java.io.File;

public class JarUtils
{
    //private static String thisjarpath;
    /*public static String GetMainClass()
    {
        for(final java.util.Map.Entry<String, String> entry : System.getenv().entrySet())
        {
            if(entry.getKey().startsWith("JAVA_MAIN_CLASS"))
            {
                return entry.getValue();
            }
        }
        return null;
    }*/
    public static String GetThisJarPath()
    {
        try{

            //String mainclassname = System.getenv("JAVA_MAIN_CLASS");
            //String mainclassname = GetMainClass();
            //Class mainclass = Class.forName(mainclassname);
            //return GetJarPath(mainclass);
            return GetJarPath(JarUtils.class);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String GetJarPath(Class klass)
    {
        try{
            java.net.URI uri = klass.getProtectionDomain().getCodeSource().getLocation().toURI();
            String r = uri.getPath();
            //String r = uri.toString().replace("file:/", "");
            return r;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static void MergeThisJarWithLibs()
    {
        String jarfile = GetThisJarPath();
        MergeJarWithLibs(jarfile);
    }
    public static void MergeJarWithLibs(String jarfilename)
    {
        File jarfile = new File(jarfilename);
        Vector<File> jars = new Vector();
        m.msg("Main jarfile: ",jarfile.getAbsolutePath());
        File libsdir = new File(jarfile.getParent()+"/lib/");
        m.msg("Libs dir: ",libsdir.getAbsolutePath());
        for(File libfile: libsdir.listFiles())
        {
            if(libfile.isFile() && libfile.getName().endsWith(".jar"))
            {
                m.msg("Lib jarfile: ",libfile.getAbsolutePath());
                jars.add(libfile);
            }
        }
        jars.add(jarfile); //do this one last, so that it overwrites the others' manifests.

        MergeJars(jars,jarfile.getParentFile());
    }
    public static void MergeJars(Vector<File> jars, File outputdir)
    {

        //delete old temp folder.
        File tempdir = new File(outputdir+"/tempmerge/");
        FileUtils.DeleteTree(tempdir,true);

        //extract jars
        String curfilename = null;
        for(File jar: jars)
        {
            zip.extractZipFile(jar.getAbsolutePath(), tempdir);
            curfilename = jar.getName();
        }

        //delete signature files
        File manifestdir = new File(outputdir+"/tempmerge/META-INF/");
        for(File child: manifestdir.listFiles())
        {
            if(child.exists() && child.isFile())
            {
                String filename = child.getName().toLowerCase();
                if(filename.endsWith(".rsa") || filename.endsWith(".dsa") || filename.endsWith(".sf") || filename.startsWith("sig-"))
                {
                    //delete it
                    FileUtils.DeleteFile(child.getAbsolutePath(), true);
                }
            }
        }

        //merge jars
        File outfile = new File(outputdir+"/"+curfilename+"-Merged.jar");
        zip.createZipFile(tempdir, outfile);
    }
}
