/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import shared.m;

public class ColorUtils
{
    public static class RGB24
    {
        public int r;
        public int g;
        public int b;

        public RGB24(int r, int g, int b)
        {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public RGB convertToRGB()
        {
            double r2 = ((double)r)/255.0;
            double g2 = ((double)g)/255.0;
            double b2 = ((double)b)/255.0;
            RGB result = new RGB(r2,g2,b2);
            return result;
        }
        public java.awt.Color convertToJavaColor()
        {
            java.awt.Color result = new java.awt.Color(r, g, b);
            return result;
        }
    }
    public static class RGB
    {
        public double r;
        public double g;
        public double b;

        public RGB(double r, double g, double b)
        {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public HSV convertToHSV()
        {
            double min = getmin(r,g,b);
            double max = getmax(r,g,b);
            double h = gethue(r,g,b);
            double s = getsat(min,max);
            double v = max;

            HSV result = new HSV(h,s,v);
            return result;
        }
        public HSL convertToHSL()
        {
            double min = getmin(r,g,b);
            double max = getmax(r,g,b);
            double h = gethue(r,g,b);
            double l = 0.5*(max+min);
            double s = getsatHSL(min,max,l);

            HSL result = new HSL(h,s,l);
            return result;
        }

        public RGB24 convertToRGB24()
        {
            int r2 = (int)Math.round(r*255.0);
            int g2 = (int)Math.round(g*255.0);
            int b2 = (int)Math.round(b*255.0);

            RGB24 result = new RGB24(r2,g2,b2);
            return result;
        }

        public String toString()
        {
            return "r="+Double.toString(r)+"  g="+Double.toString(g)+"  b="+Double.toString(b);
        }

    }
    public static class HSL
    {
        public double h;
        public double s;
        public double l;

        public HSL(double h, double s, double l)
        {
            this.h = h;
            this.s = s;
            this.l = l;
        }
        public RGB convertToRGB()
        {
            double q;
            if(l<0.5)
            {
                q = l*(1+s);
            }
            else
            {
                q = l+s-(l*s);
            }

            double p = 2*l-q;
            double hk = h/360.0;
            double tr = hk+1.0/3.0;
            double tg = hk;
            double tb = hk-1.0/3.0;

            if(tr<0) tr += 1.0;
            if(tr>1) tr -= 1.0;
            if(tg<0) tg += 1.0;
            if(tg>1) tg -= 1.0;
            if(tb<0) tb += 1.0;
            if(tb>1) tb -= 1.0;

            double cr = convhelp(p,q,tr);
            double cg = convhelp(p,q,tg);
            double cb = convhelp(p,q,tb);

            RGB result = new RGB(cr,cg,cb);
            return result;
        }
        private double convhelp(double p, double q, double tc)
        {
            if(tc<1.0/6.0)
            {
                return p+((q-p)*6*tc);
            }
            else if(tc<1.0/2.0)
            {
                return q;
            }
            else if(tc<2.0/3.0)
            {
                return p+((q-p)*6*(2.0/3.00-tc));
            }
            else
            {
                return p;
            }
        }
    }
    public static class HSV
    {
        public double h;
        public double s;
        public double v;

        public HSV(double h, double s, double v)
        {
            this.h = h;
            this.s = s;
            this.v = v;
        }

        public void canonicalise()
        {
            if(h<0.0 || h>360.0)
            {
                h = modulus(h, 360.0);
            }
        }
        public void rotateHue(double angle)
        {
            h = h + angle;
            canonicalise();
        }
        public RGB convertToRGB()
        {
            int hi = (int)modulus(Math.floor(h/60.0),6);
            double f = getfractionalpart(h/60.0);

            double p = v*(1.0-s);
            double q = v*(1.0-f*s);
            double t = v*(1.0-(1.0-f)*s);

            switch(hi)
            {
                case 0:
                    return new RGB(v,t,p);
                case 1:
                    return new RGB(q,v,p);
                case 2:
                    return new RGB(p,v,t);
                case 3:
                    return new RGB(p,q,v);
                case 4:
                    return new RGB(t,p,v);
                case 5:
                    return new RGB(v,p,q);
                default:
                    m.throwUncaughtException("This should not be possible in convertToRGB.");
                    return null;
            }
        }

        public String toString()
        {
            return "h="+Double.toString(h)+"  s="+Double.toString(s)+"  v="+Double.toString(v);
        }
    }
    public static double modulus(double num, double div)
    {
        return (num % div + div) % div;
    }
    /*public static HSV RGBtoHSV(RGB rgb)
    {
        double r = rgb.r;
        double g = rgb.g;
        double b = rgb.b;
        double min = getmin(r,g,b);
        double max = getmax(r,g,b);
        double h = gethue(r,g,b);
        double s = getsat(min,max);
        double v = max;

        HSV result = new HSV(h,s,v);
        return result;
    }*/
    private static double gethue(double r, double g, double b)
    {
        double min = getmin(r,g,b);
        double max = getmax(r,g,b);
        if(max==min){
            return 0.0;
        }else if(max==r){
            return modulus(60*(g-b)/(max-min) + 360, 360.0);
        }else if(max==g){
            return 60*(b-r)/(max-min) + 120;
        }else{  // if(max==b){
            return 60*(r-g)/(max-min) + 240;
        }
    }
    private static double getsat(double min, double max)
    {
        if(max==0.0){
            return 0.0;
        }else{
            return 1.0-(min/max);
        }
    }
    private static double getsatHSL(double min, double max, double l)
    {
        if(max==min)
        {
            return 0;
        }
        else if(l<=0.5)
        {
            return (max-min)/(max+min);
        }
        else
        {
            return (max-min)/(2-(max+min));
        }

    }
    private static double getmin(double f1, double f2, double f3)
    {
        return Math.min(Math.min(f1, f2),f3);
    }
    private static double getmax(double f1, double f2, double f3)
    {
        return Math.max(Math.max(f1, f2),f3);
    }
    private static double getfractionalpart(double f)
    {
        return f-Math.floor(f);
    }
}
