import java.awt.*;

public class Action {
    public int actiontype;
    public Object action;
    public int reusable;
    
    public static final String[] actionnames = {"End Event", "Toggle a Target", "Activate a Target", "Deactivate a Target", "Character Joins", "Receive Item", "Healing", "Change Text", "Change Picture", "Play Sound/Movie", "Set Choice Visible", "Set Choice Invisible", "Learn Ability", "Question", "Change Text Color", "Change Text Align", "Change Picture Align", "Receive Experience", "End Choice"};
    
    public Action() {
    }
    
    public Action(int actiontype, Object action, int reusable) {
        this.actiontype = actiontype;
        this.action = action;
        this.reusable = reusable;
    }
    
    public String toString() {
        String s = actionnames[actiontype];
        if ((actiontype > 0 && actiontype < 4) || actiontype == 6) {
            int targetlevel = ((MapPoint) action).level;
            int targetx = ((MapPoint) action).x;
            int targety = ((MapPoint) action).y;
            s += " <" + targetlevel + "," + targetx + "," + targety + ">";
        } else if (actiontype == 4) s += " <" + ((HeroData) action).name + ">";
        else if (actiontype == 5) s += " <" + ((Item) action).name + ">";
        else if (actiontype == 8) s += " <" + (String) action + ">";
        else if (actiontype == 9) s += " <" + (String) action + ">";
        else if (actiontype == 12) s += " <" + (SpecialAbility) action + ">";
        else if (actiontype == 14)
            s += " <" + ((Color) action).getRed() + "," + ((Color) action).getGreen() + "," + ((Color) action).getBlue() + ">";
        else if (actiontype == 15 || actiontype == 16) {
            int newalign = ((Integer) action).intValue();
            String astring;
            if (newalign == 0) astring = "Bottom";
            else if (newalign == 1) astring = "Center";
            else astring = "Top";
            s += " <" + astring + ">";
        }
        return s;
    }
}
