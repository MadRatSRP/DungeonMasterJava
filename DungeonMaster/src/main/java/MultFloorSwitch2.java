//switch types:
//always works
//works only once
//button, key, coin (like key, but takes >1 item)
//can use up the key/coin or not

//timers:
//resets after certain time
//delay before works
//both of above

//import java.util.Vector;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

class MultFloorSwitch2 extends Floor {
    static public boolean ADDEDPICS;
    protected MapPoint xy; //point containing xy of switch
    public boolean haspic = true;//,playsound = true; //does it have a picture/make a click?
    protected ArrayList switchlist; //the collection of switches
    protected boolean[] changing; //index of each switch that is changing
        
        /*
        //xyp - mappoint identifying location of this multwallswitch2
        //pic - true if has picture
        ////snd - true if makes a click sound - note: switches in switchlist should be set for no sound or pic!
        //sl - arraylist containing wallswitches (which must have same level,x,y,side)
        //public MultFloorSwitch2(MapPoint xyp,boolean pic,boolean snd,ArrayList sl) {
        public MultFloorSwitch2(MapPoint xyp,boolean pic,ArrayList sl) {
                super();
                xy = xyp;
                haspic = pic;
                //playsound = snd;
                switchlist = sl;
                changing = new boolean[switchlist.size()];
                for (int i=0;i<switchlist.size();i++) { changing[i]=false; }
                mapchar = 'S';
                if (haspic) setPics();
        }
        */
    
    //for load routine
    public MultFloorSwitch2() {
        super();
        mapchar = 'S';
    }
    
    public void tryFloorSwitch(int onoff) {
        FloorSwitch f;
        int index = 0;
        for (Iterator i = switchlist.iterator(); i.hasNext(); index++) {
            f = (FloorSwitch) i.next();
            //if (f.type==FloorSwitch.CONSTANT) { f.hasParty=hasParty; f.hasItems=hasItems; f.hasMons=hasMons; }
            f.hasParty = hasParty;
            f.hasItems = hasItems;
            f.hasMons = hasMons;
            f.mapItems = mapItems;
            f.tryFloorSwitch(onoff);
            f.hasParty = false;
            f.hasItems = false;
            f.hasMons = false;
                        /*
                        if (f.type!=CONSTANT) f.tryFloorSwitch(onoff);
                        else if (!hasParty && !hasItems) {
                                boolean found = false;
                                if (hasMons) { //flying,immaterial, and dying mons don't affect switches
                                        dmnew.Monster tempmon;
                                        int i=0;
                                        while (i<6 && !found) {
                                                tempmon = (dmnew.Monster)dmnew.dmmons.get(xy.level+","+xy.x+","+xy.y+","+i);
                                                if (!tempmon.isflying && !tempmon.isImmaterial && !tempmon.isdying) found=true;
                                                else if (i==3) i=5;
                                                else i++;
                                        }
                                }
                                if (!found) f.tryFloorSwitch(onoff);
                        }
                        */
            if (f.delaying || f.resetting) changing[index] = true;
        }
    }
    
    public void toggle() {
        FloorSwitch f;
        int index = 0;
        for (Iterator i = switchlist.iterator(); i.hasNext(); index++) {
            f = (FloorSwitch) i.next();
            f.hasParty = hasParty;
            f.hasItems = hasItems;
            f.hasMons = hasMons;
            f.mapItems = mapItems;
            f.toggle();
            f.hasParty = false;
            f.hasItems = false;
            f.hasMons = false;
            if (f.delaying || f.resetting) changing[index] = true;
        }
    }
    
    public void activate() {
        toggle();
    }
    
    public boolean changeState() {
        FloorSwitch f;
        boolean tester = false;
        for (int index = 0; index < switchlist.size(); index++) {
            if (changing[index]) {
                f = (FloorSwitch) switchlist.get(index);
                changing[index] = f.changeState();
                if (changing[index]) tester = true;
            }
        }
        return tester;
    }
    
    public void doAction() {
        if (hasParty || hasMons) {
            FloorSwitch f;
            int index = 0;
            for (Iterator i = switchlist.iterator(); i.hasNext(); index++) {
                f = (FloorSwitch) i.next();
                f.hasParty = hasParty;
                f.hasItems = hasItems;
                f.hasMons = hasMons;
                f.mapItems = mapItems;
                f.doAction();
                f.hasParty = false;
                f.hasItems = false;
                f.hasMons = false;
                if (f.delaying || f.resetting) changing[index] = true;
            }
        }
    }
    
    public void turnTest(int oldfacing) {
        FloorSwitch f;
        int index = 0;
        for (Iterator i = switchlist.iterator(); i.hasNext(); index++) {
            f = (FloorSwitch) i.next();
            f.hasParty = hasParty;
            f.hasItems = hasItems;
            f.hasMons = hasMons;
            f.mapItems = mapItems;
            
            if (f.switchface - 1 == dmnew.facing) f.tryFloorSwitch(PARTYSTEPPINGON);
            else if (f.switchface - 1 == oldfacing) {
                int tf = dmnew.facing;
                dmnew.facing = oldfacing;
                f.tryFloorSwitch(PARTYSTEPPINGOFF);
                dmnew.facing = tf;
            }
            
            f.hasParty = false;
            f.hasItems = false;
            f.hasMons = false;
            if (f.delaying || f.resetting) changing[index] = true;
        }
    }
    
    protected void setPics() {
        pic = new Image[4][5];
        pic[1][1] = loadPic("fswitch11.gif");
        pic[1][2] = loadPic("fswitch12.gif");
        pic[1][3] = loadPic("fswitch13.gif");
        pic[2][1] = loadPic("fswitch21.gif");
        pic[2][2] = loadPic("fswitch22.gif");
        pic[2][3] = loadPic("fswitch23.gif");
        pic[3][1] = loadPic("fswitch31.gif");
        pic[3][2] = loadPic("fswitch32.gif");
        pic[3][3] = loadPic("fswitch33.gif");
        if (!ADDEDPICS) {
            tracker.addImage(pic[1][1], 0);
            tracker.addImage(pic[1][2], 0);
            tracker.addImage(pic[1][3], 0);
            tracker.addImage(pic[2][1], 0);
            tracker.addImage(pic[2][2], 0);
            tracker.addImage(pic[2][3], 0);
            tracker.addImage(pic[3][1], 0);
            tracker.addImage(pic[3][2], 0);
            tracker.addImage(pic[3][3], 0);
            ADDEDPICS = true;
        }
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (col == 0 || col == 4) return;
        else if (haspic && row != 0) {
            if (col == 3) xc -= pic[row][col].getWidth(null);
            //g.drawImage(pic[row][col],xc,yc,obs);
            if (col == 2 && dmnew.mirrorback)
                g.drawImage(pic[row][col], xc + pic[row][col].getWidth(null), yc, xc, yc + pic[row][col].getHeight(null), 0, 0, pic[row][col].getWidth(null), pic[row][col].getHeight(null), obs);
            else g.drawImage(pic[row][col], xc, yc, obs);
        }
        drawContents(g, 3 - row, col - 1);
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(xy);
        so.writeBoolean(haspic);
        //so.writeBoolean(playsound);
        so.writeObject(changing);
        so.writeInt(switchlist.size());
        for (Iterator i = switchlist.iterator(); i.hasNext(); ) {
            ((FloorSwitch) i.next()).save(so);
        }
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        xy = (MapPoint) si.readObject();
        haspic = si.readBoolean();
        //playsound = si.readBoolean();
        changing = (boolean[]) si.readObject();
        int numswitches = si.readInt();
        switchlist = new ArrayList(numswitches);
        for (int i = 0; i < numswitches; i++) {
            switchlist.add(dmnew.loadMapObject(si));
        }
        setPics();
    }
}
