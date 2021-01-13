import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import java.awt.*;

public class DungTheme extends DefaultMetalTheme {
    
    private FontUIResource dungfont;
    
    public DungTheme(Font font) {
        dungfont = new FontUIResource(font);
        //dungfont = new FontUIResource(new Font("SansSerif",Font.BOLD,12));
    }
    
    public String getName() {
        return "Dungeon";
    }
    
    private final ColorUIResource primary1 = new ColorUIResource(102, 102, 153);//unused by buttons? - was 102,102,153
    private final ColorUIResource primary2 = new ColorUIResource(128, 128, 192);//focus box - was 128,128,192
    private final ColorUIResource primary3 = new ColorUIResource(159, 159, 235);//unused by buttons? - was 159,159,235
    
    private final ColorUIResource secondary1 = new ColorUIResource(60, 60, 60); //lines around buttons (not the highlights)
    private final ColorUIResource secondary2 = new ColorUIResource(80, 80, 140); //selected button color
    private final ColorUIResource secondary3 = new ColorUIResource(80, 80, 80); //unselected foreground color
    
    protected ColorUIResource getPrimary1() {
        return primary1;
    }
    
    protected ColorUIResource getPrimary2() {
        return primary2;
    }
    
    protected ColorUIResource getPrimary3() {
        return primary3;
    }
    
    protected ColorUIResource getSecondary1() {
        return secondary1;
    }
    
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }
    
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }
    
    private final ColorUIResource highlight = new ColorUIResource(125, 125, 145);
    private final ColorUIResource darkshadow = new ColorUIResource(55, 55, 75);
    //private final ColorUIResource control               = new ColorUIResource(250, 2, 2);
    //private final ColorUIResource controlshadow         = new ColorUIResource(250, 2, 2);
    //private final ColorUIResource primarycontrol        = new ColorUIResource(250, 2, 2);
    //private final ColorUIResource primarycontrolshadow  = new ColorUIResource(250, 2, 2);
    
    public ColorUIResource getControlHighlight() {
        return highlight;
    }
    
    public ColorUIResource getControlDarkShadow() {
        return darkshadow;
    }
    //public ColorUIResource getControl() { return control; }
    //public ColorUIResource getControlShadow() { return controlshadow; }
    //public ColorUIResource getPrimaryControl() { return primarycontrol; }
    //public ColorUIResource getPrimaryControlDarkShadow() { return primarycontrolshadow; }
    
    //public FontUIResource getUserTextFont() { return dungfont; }
    //public FontUIResource getSystemTextFont() { return dungfont; }
    public FontUIResource getControlTextFont() {
        return dungfont;
    }
    
    public FontUIResource getMenuTextFont() {
        return dungfont;
    }
    
}
