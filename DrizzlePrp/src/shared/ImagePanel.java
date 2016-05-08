/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Image;
import java.net.URL;
import java.awt.image.BufferedImage;
import shared.m;

public class ImagePanel extends JPanel
{
    Image img=null;
    String name;
    boolean resize = false;
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(img!=null)
        {
            g.drawImage(img, 0, 0, this);
        }
    }
    
    public void setImageFile(String name)
    {
        this.name = name;
        //URL url = this.getClass().getResource(name);
        try{
            //BufferedImage img2 = javax.imageio.ImageIO.read(url);
            //img = img2;
            img = shared.GetResource.getResourceAsImage(name);
        }catch(Exception e)
        {
            m.err("Unable to load resource: ",name);
        }
        //this.jPanel32.getGraphics().drawImage(img, 0, 0, rootPane);
        if(this.resize) resize();
    }
    public boolean getResize()
    {
        return resize;
    }
    public void setResize(boolean resize)
    {
        this.resize = resize;
        if(this.name!=null) resize();
    }
    public void resize()
    {
        int h = img.getHeight(null);
        int w = img.getWidth(null);
        this.setSize(w, h);
    }
    public String getImageFile()
    {
        return this.name;
    }
    
}
