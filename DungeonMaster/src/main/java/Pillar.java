import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

class Pillar extends Wall {
    
    static public boolean ADDEDPICS = false;
    static protected Image[] pillarpic = new Image[9];
    static private HashMap custompics;// = new HashMap();
    static public boolean swapmirror = false;
    private int type;
    private boolean mirror;
    private String custompic;
    
    public Pillar(int type, boolean mirror) {
        super(0);//use super constructor that doesn't call setpics (since won't be used)
        this.type = type;
        this.mirror = mirror;
        mapchar = 'P';
        if (!ADDEDPICS && type < 2) setPics();
    }
    
    public void setPillar(int type, boolean mirror) {
        this.mirror = mirror;
        this.type = type;
    }
    
    protected void setPics() {
        pillarpic[0] = dmnew.tk.createImage("Maps" + File.separator + "pillara1.gif");
        pillarpic[1] = dmnew.tk.createImage("Maps" + File.separator + "pillara2.gif");
        pillarpic[2] = dmnew.tk.createImage("Maps" + File.separator + "pillara3.gif");
        pillarpic[3] = dmnew.tk.createImage("Maps" + File.separator + "pillarb1.gif");
        pillarpic[4] = dmnew.tk.createImage("Maps" + File.separator + "pillarb2.gif");
        pillarpic[5] = dmnew.tk.createImage("Maps" + File.separator + "pillarb3.gif");
        tracker.addImage(pillarpic[0], 0);
        tracker.addImage(pillarpic[1], 0);
        tracker.addImage(pillarpic[2], 0);
        tracker.addImage(pillarpic[3], 0);
        tracker.addImage(pillarpic[4], 0);
        tracker.addImage(pillarpic[5], 0);
        ADDEDPICS = true;
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (row == 0 || col == 0 || col == 4) return;
        if (type == 2) {
            pillarpic[6] = (Image) custompics.get(custompic + "1");
            pillarpic[7] = (Image) custompics.get(custompic + "2");
            pillarpic[8] = (Image) custompics.get(custompic + "3");
        }
        if (row == 1) {
            if (col == 2) {
                if ((mirror && !swapmirror) || (!mirror && swapmirror))
                    g.drawImage(pillarpic[type * 3], 153 + pillarpic[type * 3].getWidth(null), 30, 153, 30 + pillarpic[type * 3].getHeight(null), 0, 0, pillarpic[type * 3].getWidth(null), pillarpic[type * 3].getHeight(null), obs);
                else g.drawImage(pillarpic[type * 3], 153, 30, obs);
            } else if (col == 1) {
                if ((mirror && !swapmirror) || (!mirror && swapmirror))
                    g.drawImage(pillarpic[type * 3], -79 + pillarpic[type * 3].getWidth(null), 30, -79, 30 + pillarpic[type * 3].getHeight(null), 0, 0, pillarpic[type * 3].getWidth(null), pillarpic[type * 3].getHeight(null), obs);
                else g.drawImage(pillarpic[type * 3], -79, 30, obs);
            } else {
                if ((mirror && !swapmirror) || (!mirror && swapmirror))
                    g.drawImage(pillarpic[type * 3], 384 + pillarpic[type * 3].getWidth(null), 30, 384, 30 + pillarpic[type * 3].getHeight(null), 0, 0, pillarpic[type * 3].getWidth(null), pillarpic[type * 3].getHeight(null), obs);
                else g.drawImage(pillarpic[type * 3], 384, 30, obs);
            }
        } else if (row == 2) {
            if (col == 2) {
                if ((mirror && !swapmirror) || (!mirror && swapmirror))
                    g.drawImage(pillarpic[type * 3 + 1], 178 + pillarpic[type * 3 + 1].getWidth(null), 56, 178, 56 + pillarpic[type * 3 + 1].getHeight(null), 0, 0, pillarpic[type * 3 + 1].getWidth(null), pillarpic[type * 3 + 1].getHeight(null), obs);
                else g.drawImage(pillarpic[type * 3 + 1], 178, 56, obs);
            } else if (col == 1) {
                if ((mirror && !swapmirror) || (!mirror && swapmirror))
                    g.drawImage(pillarpic[type * 3 + 1], 7 + pillarpic[type * 3 + 1].getWidth(null), 56, 7, 56 + pillarpic[type * 3 + 1].getHeight(null), 0, 0, pillarpic[type * 3 + 1].getWidth(null), pillarpic[type * 3 + 1].getHeight(null), obs);
                else g.drawImage(pillarpic[type * 3 + 1], 7, 56, obs);
            } else {
                if ((mirror && !swapmirror) || (!mirror && swapmirror))
                    g.drawImage(pillarpic[type * 3 + 1], 348 + pillarpic[type * 3 + 1].getWidth(null), 56, 348, 56 + pillarpic[type * 3 + 1].getHeight(null), 0, 0, pillarpic[type * 3 + 1].getWidth(null), pillarpic[type * 3 + 1].getHeight(null), obs);
                else g.drawImage(pillarpic[type * 3 + 1], 348, 56, obs);
            }
        } else if (row == 3) {
            if (col == 2) {
                if ((mirror && !swapmirror) || (!mirror && swapmirror))
                    g.drawImage(pillarpic[type * 3 + 2], 190 + pillarpic[type * 3 + 2].getWidth(null), 70, 190, 70 + pillarpic[type * 3 + 2].getHeight(null), 0, 0, pillarpic[type * 3 + 2].getWidth(null), pillarpic[type * 3 + 2].getHeight(null), obs);
                else g.drawImage(pillarpic[type * 3 + 2], 190, 70, obs);
            } else if (col == 1) {
                if ((mirror && !swapmirror) || (!mirror && swapmirror))
                    g.drawImage(pillarpic[type * 3 + 2], 54 + pillarpic[type * 3 + 2].getWidth(null), 70, 54, 70 + pillarpic[type * 3 + 2].getHeight(null), 0, 0, pillarpic[type * 3 + 2].getWidth(null), pillarpic[type * 3 + 2].getHeight(null), obs);
                else g.drawImage(pillarpic[type * 3 + 2], 54, 70, obs);
            } else {
                if ((mirror && !swapmirror) || (!mirror && swapmirror))
                    g.drawImage(pillarpic[type * 3 + 2], 329 + pillarpic[type * 3 + 2].getWidth(null), 70, 329, 70 + pillarpic[type * 3 + 2].getHeight(null), 0, 0, pillarpic[type * 3 + 2].getWidth(null), pillarpic[type * 3 + 2].getHeight(null), obs);
                else g.drawImage(pillarpic[type * 3 + 2], 329, 70, obs);
            }
        }
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(type);
        so.writeBoolean(mirror);
        if (type == 2) so.writeUTF(custompic);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        custompic = si.readUTF();
        if (custompics == null) custompics = new HashMap();
        if (!custompics.containsKey(custompic + "1")) {
            String ftype = ".gif";
            File testfile = new File(mapdir + custompic + "1.gif");
            if (!testfile.exists()) ftype = ".png";
            Image temppic = dmnew.tk.createImage(mapdir + custompic + "1" + ftype);
            custompics.put(custompic + "1", temppic);
            tracker.addImage(temppic, 0);
            temppic = dmnew.tk.createImage(mapdir + custompic + "2" + ftype);
            custompics.put(custompic + "2", temppic);
            tracker.addImage(temppic, 0);
            temppic = dmnew.tk.createImage(mapdir + custompic + "3" + ftype);
            custompics.put(custompic + "3", temppic);
            tracker.addImage(temppic, 0);
        }
    }
}