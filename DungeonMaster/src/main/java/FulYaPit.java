import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectOutputStream;

class FulYaPit extends Floor {
    static public boolean ADDEDPICS;
    private int keynumber, animcounter = 0;
    private MapPoint xy, keytarget, nonkeytarget;
    private boolean isconsuming = false;
    private static Image[][] pic = new Image[4][4];
    private static Image[][] apic = new Image[4][4];
    private static Image c1pic, c2pic;
    
    //xy - mappoint of the pit
    //keynumber - item # for key
    //keytarget - mappoint to be activated if key dropped in pit
    //nonkeytarget - mappoint to be activated if some other item dropped in pit
    public FulYaPit(MapPoint xy, int keynumber, MapPoint keytarget, MapPoint nonkeytarget) {
        super();
        mapchar = 'y';
        //canPassMons = false;
        //canPassImmaterial = false;
        this.xy = xy;
        this.keynumber = keynumber;
        this.keytarget = keytarget;
        this.nonkeytarget = nonkeytarget;
        if (!ADDEDPICS) setPics();
    }
    
    public void tryTeleport() {
        //kill party
        dmnew.playSound("burn.wav", -1, -1);
        dmnew.playSound("scream.wav", -1, -1);
        dmnew.needredraw = true;
        dmnew.dview.repaint();
        for (int i = 0; i < dmnew.numheroes; i++) {
            dmnew.hero[i].damage(dmnew.hero[i].maxhealth, dmnew.POISONHIT);
        }
    }
    
    public void tryTeleport(dmnew.Monster mon) {
        //kill mon unless isflying
        if (!mon.isflying) {
            if (xy.level == dmnew.level) {
                dmnew.playSound("burn.wav", xy.x, xy.y);
                int xdist = xy.x - dmnew.partyx;
                if (xdist < 0) xdist *= -1;
                int ydist = xy.y - dmnew.partyy;
                if (ydist < 0) ydist *= -1;
                if (xdist < 5 && ydist < 5) dmnew.message.setMessage(mon.name + " is consumed by the FulYa Pit.", 5);
            }
            mon.pitDeath();
        }
    }
    
    public boolean tryTeleport(Item it) {
        //if key, teleport to keytarget, else teleport to nonkeytarget
        isconsuming = true;
        animcounter = 0;
        dmnew.needredraw = true;
        dmnew.dview.repaint();
        if (xy.level == dmnew.level) dmnew.playSound("burn.wav", xy.x, xy.y);
        int targetlevel, targetx, targety;
        if (it.number == keynumber) {
            //dmnew.DungeonMap[keytarget.level][keytarget.x][keytarget.y].activate();
            targetlevel = keytarget.level;
            targetx = keytarget.x;
            targety = keytarget.y;
        } else {
            //dmnew.DungeonMap[nonkeytarget.level][nonkeytarget.x][nonkeytarget.y].activate();
            targetlevel = nonkeytarget.level;
            targetx = nonkeytarget.x;
            targety = nonkeytarget.y;
        }
        
        //test for floorswitch
        dmnew.DungeonMap[targetlevel][targetx][targety].tryFloorSwitch(MapObject.PUTITEM);
        //test for teleport
        if (targetlevel != xy.level || targetx != xy.x || targety != xy.y) {
            if (!dmnew.DungeonMap[targetlevel][targetx][targety].tryTeleport(it))
                dmnew.DungeonMap[targetlevel][targetx][targety].addItem(it);
        }
        //next line removed so item simply destroyed
        //else dmnew.DungeonMap[targetlevel][targetx][targety].addItem(it);
        
        return true;
    }
    
    public boolean changeState() {
        //do animation like teleport
        if (xy.level == dmnew.level) {
            int xdist = xy.x - dmnew.partyx;
            if (xdist < 0) xdist *= -1;
            int ydist = xy.y - dmnew.partyy;
            if (ydist < 0) ydist *= -1;
            if (xdist < 5 && ydist < 5) { //close, so anim and repaint
                if (isconsuming) {
                    animcounter = (animcounter + 1) % 8;
                    if (animcounter == 0) isconsuming = false;
                    if (animcounter == 0 || animcounter == 2 || animcounter == 4 || animcounter == 6)
                        dmnew.needredraw = true;
                } else {
                    animcounter = (animcounter + 1) % 4;
                    if (animcounter == 0 || animcounter == 2) dmnew.needredraw = true;
                }
            }
        }
        return true;
    }
    
    public void doAction() {
        if (hasParty) tryTeleport();
        else if (hasMons) {
            boolean stillmons = false;
            int i = 0;
            dmnew.Monster tempmon;
            while (i < 6) {
                tempmon = (dmnew.Monster) dmnew.dmmons.get(xy.level + "," + xy.x + "," + xy.y + "," + i);
                if (tempmon != null) {
                    tryTeleport(tempmon);
                    if (!tempmon.isdying) stillmons = true;
                    else dmnew.dmmons.remove(xy.level + "," + xy.x + "," + xy.y + "," + i);
                }
                if (i == 3) i = 5;
                else i++;
            }
            hasMons = stillmons;
        }
        if (hasItems) {
            while (mapItems.size() > 0) {
                tryTeleport((Item) mapItems.remove(0));
            }
        }
    }
    
    public void setPics() {
        pic = new Image[4][5];
        pic[0][1] = loadPic("pit01.gif");
        pic[0][2] = loadPic("fulya02.gif");
        pic[0][3] = loadPic("pit03.gif");
        pic[1][1] = loadPic("fulya11.gif");
        pic[1][2] = loadPic("fulya12.gif");
        pic[1][3] = loadPic("fulya13.gif");
        pic[2][1] = loadPic("fulya21.gif");
        pic[2][2] = loadPic("fulya22.gif");
        pic[2][3] = loadPic("fulya23.gif");
        pic[3][1] = loadPic("fulya31.gif");
        pic[3][2] = loadPic("fulya32.gif");
        pic[3][3] = loadPic("fulya33.gif");
        apic[0][1] = pic[0][1];
        apic[0][2] = pic[0][2];
        apic[0][3] = pic[0][3];
        apic[1][1] = loadPic("fulya11-alt.gif");
        apic[1][2] = loadPic("fulya12-alt.gif");
        apic[1][3] = loadPic("fulya13-alt.gif");
        apic[2][1] = loadPic("fulya21-alt.gif");
        apic[2][2] = loadPic("fulya22-alt.gif");
        apic[2][3] = loadPic("fulya23-alt.gif");
        apic[3][1] = loadPic("fulya31-alt.gif");
        apic[3][2] = loadPic("fulya32-alt.gif");
        apic[3][3] = loadPic("fulya33-alt.gif");
        c1pic = loadPic("fulya-con1.png");
        c2pic = loadPic("fulya-con2.png");
        if (!Pit.ADDEDPICS) {
            tracker.addImage(pic[0][1], 0);
            tracker.addImage(pic[0][3], 0);
        }
        tracker.addImage(pic[0][2], 0);
        tracker.addImage(pic[1][1], 0);
        tracker.addImage(pic[1][2], 0);
        tracker.addImage(pic[1][3], 0);
        tracker.addImage(pic[2][1], 0);
        tracker.addImage(pic[2][2], 0);
        tracker.addImage(pic[2][3], 0);
        tracker.addImage(pic[3][1], 0);
        tracker.addImage(pic[3][2], 0);
        tracker.addImage(pic[3][3], 0);
        tracker.addImage(apic[1][1], 0);
        tracker.addImage(apic[1][2], 0);
        tracker.addImage(apic[1][3], 0);
        tracker.addImage(apic[2][1], 0);
        tracker.addImage(apic[2][2], 0);
        tracker.addImage(apic[2][3], 0);
        tracker.addImage(apic[3][1], 0);
        tracker.addImage(apic[3][2], 0);
        tracker.addImage(apic[3][3], 0);
        tracker.addImage(c1pic, 0);
        tracker.addImage(c2pic, 0);
        ADDEDPICS = true;
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (row == 0 && col == 2) g.drawImage(pic[0][2], 0, 0, null);
        else if (isconsuming && row == 1 && col == 2) {
            int rowadjustx = 22, rowadjusty = 156; //202-46;
            if (animcounter < 2 || (animcounter > 3 && animcounter < 6)) { //draw normally
                if (dmnew.mirrorback)
                    g.drawImage(c1pic, xc + rowadjustx + c1pic.getWidth(null), yc + rowadjusty, xc + rowadjustx, yc + rowadjusty + c1pic.getHeight(null), 0, 0, c1pic.getWidth(null), c1pic.getHeight(null), null);
                else g.drawImage(c1pic, xc + rowadjustx, yc + rowadjusty, null);
            } else { //draw alt pics
                if (dmnew.mirrorback)
                    g.drawImage(c2pic, xc + rowadjustx + c2pic.getWidth(null), yc + rowadjusty, xc + rowadjustx, yc + rowadjusty + c2pic.getHeight(null), 0, 0, c2pic.getWidth(null), c2pic.getHeight(null), null);
                else g.drawImage(c2pic, xc + rowadjustx, yc + rowadjusty, null);
            }
            drawContents(g, 3 - row, col - 1);
        } else if (col != 0 && col != 4) {
            int rowadjustx = 0, rowadjusty = 0;
            if (row == 0) {
                rowadjustx = 50;
                rowadjusty = 296;
            } else if (row == 1) {
                rowadjustx = 22;
                rowadjusty = 202;
            } else if (row == 2) {
                rowadjustx = 16;
                rowadjusty = 136;
            } else {
                rowadjustx = 11;
                rowadjusty = 102;
            }
            
            if (col == 3) xc -= pic[row][3].getWidth(null);
            if (col != 2) rowadjustx = 0;
            
            if (animcounter < 2) { //draw normally
                if (col == 2 && dmnew.mirrorback)
                    g.drawImage(pic[row][col], xc + rowadjustx + pic[row][2].getWidth(null), yc + rowadjusty, xc + rowadjustx, yc + rowadjusty + pic[row][2].getHeight(null), 0, 0, pic[row][2].getWidth(null), pic[row][2].getHeight(null), null);
                else g.drawImage(pic[row][col], xc + rowadjustx, yc + rowadjusty, null);
            } else { //draw alt pics
                if (col == 2 && dmnew.mirrorback)
                    g.drawImage(apic[row][col], xc + rowadjustx + apic[row][2].getWidth(null), yc + rowadjusty, xc + rowadjustx, yc + rowadjusty + apic[row][2].getHeight(null), 0, 0, apic[row][2].getWidth(null), apic[row][2].getHeight(null), null);
                else g.drawImage(apic[row][col], xc + rowadjustx, yc + rowadjusty, null);
            }
            drawContents(g, 3 - row, col - 1);
        }
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(xy);
        so.writeInt(keynumber);
        so.writeObject(keytarget);
        so.writeObject(nonkeytarget);
    }
}
