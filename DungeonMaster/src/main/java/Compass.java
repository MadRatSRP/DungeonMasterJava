import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

class Compass extends Item {
    
    transient private static Image[] compasspics;
    transient private static ArrayList compasslist = new ArrayList(1);
    static final long serialVersionUID = 3420921603740542780L; // local class serialVersionUID=7107475295829507450
    
    public Compass() {
        super();
        name = "Compass";
        number = 8;
        type = OTHER;
        weight = 0.1f;
        picstring = "compass-n.gif";
        dpicstring = "dcompass.gif";
        setPics();
    }
    
    //static methods dealing with compass items in the dungeon -> need references to them in order to update pic
    public static void addCompass(Item comp) {
        boolean found = false;
        int i = 0;
        while (!found && i < compasslist.size()) {
            if (comp == (Item) compasslist.get(i)) found = true;
            else i++;
        }
        if (!found) compasslist.add(comp);
    }
    
    public static void clearList() {
        compasslist.clear();
    }
    
    public static void updateCompass(int facing) {
        for (int i = 0; i < compasslist.size(); i++) {
            ((Compass) compasslist.get(i)).setPic(facing);
        }
    }
    
    public static boolean haveCompass() {
        return !compasslist.isEmpty();
    }
    
    public void setPic(int facing) {
        //set pic
        pic = compasspics[facing];
        if (facing == 0) picstring = "compass-n.gif";
        else if (facing == 1) picstring = "compass-e.gif";
        else if (facing == 2) picstring = "compass-s.gif";
        else picstring = "compass-w.gif";
    }
    
    public void setPics() {
        if (compasspics == null) {
            compasspics = new Image[4];
            compasspics[0] = (Image) pics.get("compass-n.gif");
            compasspics[1] = (Image) pics.get("compass-e.gif");
            compasspics[2] = (Image) pics.get("compass-s.gif");
            compasspics[3] = (Image) pics.get("compass-w.gif");
            if (compasspics[0] == null) {
                compasspics[0] = tk.getImage("Items" + File.separator + "compass-n.gif");
                ImageTracker.addImage(compasspics[0], 0);
            }
            if (compasspics[1] == null) {
                compasspics[1] = tk.getImage("Items" + File.separator + "compass-e.gif");
                ImageTracker.addImage(compasspics[1], 0);
            }
            if (compasspics[2] == null) {
                compasspics[2] = tk.getImage("Items" + File.separator + "compass-s.gif");
                ImageTracker.addImage(compasspics[2], 0);
            }
            if (compasspics[3] == null) {
                compasspics[3] = tk.getImage("Items" + File.separator + "compass-w.gif");
                ImageTracker.addImage(compasspics[3], 0);
            }
        }
        pic = compasspics[0];
        dpic = (Image) pics.get(dpicstring);
        if (dpic == null) {
            dpic = tk.createImage("Items" + File.separator + dpicstring);
            pics.put(dpicstring, dpic);
            ImageTracker.addImage(dpic, 0);
        }
    }
    
    private void readObject(ObjectInputStream s) throws IOException {
        setPics();
    }
    
}