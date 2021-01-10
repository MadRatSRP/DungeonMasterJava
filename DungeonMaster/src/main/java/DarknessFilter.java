import java.awt.image.RGBImageFilter;
//import java.awt.Color;

class DarknessFilter extends RGBImageFilter {
    
    private float factor = .7f;
    
    public DarknessFilter() {
        // The filter's operation does not depend on the
        // pixel's location, so IndexColorModels can be
        // filtered directly.
        canFilterIndexColorModel = true;
    }
    
    public void setDarks(int drk) {
        factor = .7f;
        for (int i = 1; i < drk; i++) factor *= .7f;
    }
    
    public final int filterRGB(int x, int y, int rgb) {
        //swaps red and blue:
        //return ((rgb & 0xff00ff00)
        //    | ((rgb & 0xff0000) >> 16)
        //    | ((rgb & 0xff) << 16));
        //return rgb & 0xff00ff00; //- green
        //return rgb & 0xff00ffff; //- aqua
        //return rgb & 0xff0000ff; //- blue
        //return rgb & 0xffff00ff; //- purple
        //return rgb & 0xffff0000; //- red
        //return rgb & 0xffffff00; //- yellow
        //return rgb & 0x55555555; //- kind of dark, but weird
        //return rgb - 0x00444444; //- dark, but 00 - 01 = ff wraps around to bright white
        //return rgb/0x10101001;
        
        /*
        int red = ((rgb << 8) >> 24);
        int green = ((rgb << 16) >> 24);
        int blue = ((rgb << 24) >> 24);
        if (x==0) System.out.println("orig rgb = "+red+","+green+","+blue);
        red/=divider;
        green/=divider;
        blue/=divider;
        int frgb = ((rgb & 0xff000000) | ((red << 16) & 0x00ff0000) | ((green << 8) & 0x0000ff00) | (blue & 0x000000ff));
        if (x==0) {
                red = ((frgb << 8) >> 24);
                green = ((frgb << 16) >> 24);
                blue = ((frgb << 24) >> 24);
                System.out.println("filt rgb = "+red+","+green+","+blue);
        }
        return frgb; //((rgb & 0xff000000) | ((red << 16) & 0x00ff0000) | ((green << 8) & 0x0000ff00) | (blue & 0x000000ff));
        */
        
        //darken using Color.darker()
        //Color rgbcolor = new Color(rgb,true);
        //for (int i=0;i<numdarks;i++) rgbcolor = rgbcolor.darker();
        //return rgbcolor.getRGB();
        
        //darken using inlined Color.darker()
        //return ( (((rgb >> 24) & 0xff)<<24) | ((int)(((rgb >> 16) & 0xff)*factor) << 16) | ((int)(((rgb >> 8) & 0xff)*factor) << 8) | ((int)(((rgb >> 0) & 0xff)*factor) << 0) );
        return ((((rgb >> 24) & 0xff) << 24) | ((int) (((rgb >> 16) & 0xff) * factor) << 16) | ((int) (((rgb >> 8) & 0xff) * factor) << 8) | (int) ((rgb & 0xff) * factor));
        
    }
}
                                                                                                                                                                                         
