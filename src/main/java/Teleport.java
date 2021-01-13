import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

class Teleport extends Floor {
    
    static public boolean ADDEDPICS;
    static protected Image[][] teleportpic = new Image[4][5];
    static private int telcount = 0;//used to figure if back should be mirrored
    static public int currentcycle = 2, lastcycle = 0;
    private int activatecycle = -1;
    private int type;
    private int level, xcoord, ycoord; //level and xy of this teleporter
    private int targetlevel, targetx, targety; //level and xy of mapsquare to teleport to
    private int newface; //mainly for spinners, -1 no change, 0,1,2,3 correspond to north,west,south,east, 4 means spin right, 5 means spin left, 6 means spin 180 deg, 7 means random
    public int blinkcounter = 0;
    private int blinkrateon = 0, blinkrateoff = 0;
    private int animcounter = 0; //used to determine when to mirror/flip teleport pic so looks animated
    private int count, maxcount;
    private boolean resetcount;
    private boolean isReusable;
    public boolean wasUsed;
    public boolean isSwitched;//true if has own floor switch to activate itself
    private boolean switchVisible;
    private int delay, reset;//for switched type
    private int delaycounter = 0, resetcounter = 0;
    public boolean delaying, resetting;
    private boolean isBlinker;
    public boolean isVisible;
    private boolean isActive;
    public boolean isOn;
    private boolean playsound, swapplaces;
    
    static final int PARTY = 1;
    static final int MONSTER = 2;
    static final int ITEM = 4;
    static final int PROJ = 8;
    
    public Teleport() {
        super();
        mapchar = 't';
    }
    
    protected void setPics() {
        if (!ADDEDPICS) {
            teleportpic[0][1] = dmnew.tk.getImage(mapdir + "teleport01.gif");
            teleportpic[0][2] = dmnew.tk.getImage(mapdir + "teleport02.gif");
            teleportpic[0][3] = dmnew.tk.getImage(mapdir + "teleport03.gif");
            teleportpic[1][1] = dmnew.tk.getImage(mapdir + "teleport11.gif");
            teleportpic[1][2] = dmnew.tk.getImage(mapdir + "teleport12.gif");
            teleportpic[1][3] = dmnew.tk.getImage(mapdir + "teleport13.gif");
            teleportpic[2][1] = dmnew.tk.getImage(mapdir + "teleport21.gif");
            teleportpic[2][2] = dmnew.tk.getImage(mapdir + "teleport22.gif");
            teleportpic[2][3] = dmnew.tk.getImage(mapdir + "teleport23.gif");
            teleportpic[3][0] = dmnew.tk.getImage(mapdir + "teleport30.gif");
            teleportpic[3][1] = dmnew.tk.getImage(mapdir + "teleport31.gif");
            teleportpic[3][2] = dmnew.tk.getImage(mapdir + "teleport32.gif");
            teleportpic[3][3] = dmnew.tk.getImage(mapdir + "teleport33.gif");
            teleportpic[3][4] = dmnew.tk.getImage(mapdir + "teleport34.gif");
            tracker.addImage(teleportpic[0][1], 0);
            tracker.addImage(teleportpic[0][2], 0);
            tracker.addImage(teleportpic[0][3], 0);
            tracker.addImage(teleportpic[1][1], 0);
            tracker.addImage(teleportpic[1][2], 0);
            tracker.addImage(teleportpic[1][3], 0);
            tracker.addImage(teleportpic[2][1], 0);
            tracker.addImage(teleportpic[2][2], 0);
            tracker.addImage(teleportpic[2][3], 0);
            tracker.addImage(teleportpic[3][0], 0);
            tracker.addImage(teleportpic[3][1], 0);
            tracker.addImage(teleportpic[3][2], 0);
            tracker.addImage(teleportpic[3][3], 0);
            tracker.addImage(teleportpic[3][4], 0);
            ADDEDPICS = true;
        }
        if (switchVisible && !FloorSwitch.ADDEDPICS) {
            FloorSwitch.staticSetPics();
        }
    }
    
    public static void redoPics() {
        String newmapdir = Wall.currentdir + File.separator;
        File testfile = new File(newmapdir + "teleport12.gif");
        if (!testfile.exists()) return;
        
        teleportpic[0][1] = dmnew.tk.getImage(newmapdir + "teleport01.gif");
        teleportpic[0][2] = dmnew.tk.getImage(newmapdir + "teleport02.gif");
        teleportpic[0][3] = dmnew.tk.getImage(newmapdir + "teleport03.gif");
        teleportpic[1][1] = dmnew.tk.getImage(newmapdir + "teleport11.gif");
        teleportpic[1][2] = dmnew.tk.getImage(newmapdir + "teleport12.gif");
        teleportpic[1][3] = dmnew.tk.getImage(newmapdir + "teleport13.gif");
        teleportpic[2][1] = dmnew.tk.getImage(newmapdir + "teleport21.gif");
        teleportpic[2][2] = dmnew.tk.getImage(newmapdir + "teleport22.gif");
        teleportpic[2][3] = dmnew.tk.getImage(newmapdir + "teleport23.gif");
        teleportpic[3][0] = dmnew.tk.getImage(newmapdir + "teleport30.gif");
        teleportpic[3][1] = dmnew.tk.getImage(newmapdir + "teleport31.gif");
        teleportpic[3][2] = dmnew.tk.getImage(newmapdir + "teleport32.gif");
        teleportpic[3][3] = dmnew.tk.getImage(newmapdir + "teleport33.gif");
        teleportpic[3][4] = dmnew.tk.getImage(newmapdir + "teleport34.gif");
        
        tracker.addImage(teleportpic[0][1], 5);
        tracker.addImage(teleportpic[0][2], 5);
        tracker.addImage(teleportpic[0][3], 5);
        tracker.addImage(teleportpic[1][1], 5);
        tracker.addImage(teleportpic[1][2], 5);
        tracker.addImage(teleportpic[1][3], 5);
        tracker.addImage(teleportpic[2][1], 5);
        tracker.addImage(teleportpic[2][2], 5);
        tracker.addImage(teleportpic[2][3], 5);
        tracker.addImage(teleportpic[3][0], 5);
        tracker.addImage(teleportpic[3][1], 5);
        tracker.addImage(teleportpic[3][2], 5);
        tracker.addImage(teleportpic[3][3], 5);
        tracker.addImage(teleportpic[3][4], 5);
        
        try {
            tracker.waitForID(5, 2000);
        } catch (InterruptedException ex) {
        }
        
        tracker.removeImage(teleportpic[0][1], 5);
        tracker.removeImage(teleportpic[0][2], 5);
        tracker.removeImage(teleportpic[0][3], 5);
        tracker.removeImage(teleportpic[1][1], 5);
        tracker.removeImage(teleportpic[1][2], 5);
        tracker.removeImage(teleportpic[1][3], 5);
        tracker.removeImage(teleportpic[2][1], 5);
        tracker.removeImage(teleportpic[2][2], 5);
        tracker.removeImage(teleportpic[2][3], 5);
        tracker.removeImage(teleportpic[3][0], 5);
        tracker.removeImage(teleportpic[3][1], 5);
        tracker.removeImage(teleportpic[3][2], 5);
        tracker.removeImage(teleportpic[3][3], 5);
        tracker.removeImage(teleportpic[3][4], 5);
        
    }
    
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (col > 0 && col < 4) {
            if (isSwitched && switchVisible && row != 0) {
                int fxc = xc;
                if (col == 3) fxc -= FloorSwitch.fswitchpic[row][col].getWidth(null);
                if (col == 2 && dmnew.mirrorback)
                    g.drawImage(FloorSwitch.fswitchpic[row][col], fxc + FloorSwitch.fswitchpic[row][col].getWidth(null), yc, fxc, yc + FloorSwitch.fswitchpic[row][col].getHeight(null), 0, 0, FloorSwitch.fswitchpic[row][col].getWidth(null), FloorSwitch.fswitchpic[row][col].getHeight(null), obs);
                else g.drawImage(FloorSwitch.fswitchpic[row][col], fxc, yc, obs);
            }
            drawContents(g, 3 - row, col - 1);
        }
        if (!isVisible || !isOn || (row != 3 && (col == 0 || col == 4))) return;
        if (col == 3) xc -= teleportpic[row][3].getWidth(null);
        
        if (animcounter < 2) g.drawImage(teleportpic[row][col], xc, yc, obs); //draw normally
        else { //flip the image vertically
            int picwidth = teleportpic[row][col].getWidth(null);
            int picheight = teleportpic[row][col].getHeight(null);
            if (col == 1) {
                g.drawImage(teleportpic[row][3], xc + picwidth, yc, xc, yc + picheight, 0, 0, picwidth, picheight, obs);
            } else if (col == 3) {
                g.drawImage(teleportpic[row][1], xc + picwidth, yc, xc, yc + picheight, 0, 0, picwidth, picheight, obs);
            } else if (col == 0) {
                g.drawImage(teleportpic[row][4], xc + picwidth, yc, xc, yc + picheight, 0, 0, picwidth, picheight, obs);
            } else if (col == 4) {
                g.drawImage(teleportpic[row][0], xc + picwidth, yc, xc, yc + picheight, 0, 0, picwidth, picheight, obs);
            } else
                g.drawImage(teleportpic[row][col], xc, yc + picheight, xc + picwidth, yc, 0, 0, picwidth, picheight, obs);
        }
    }
    
    //tel party
    public void tryTeleport() {
        if ((type & PARTY) == 0 || (!isReusable && wasUsed)) return;
        //if (!isOn && isSwitched) { toggle(); return; }
        if (!isOn && isSwitched) {
            activate();
            return;
        } else if (!isOn) return;
        dmnew.nomirroradjust = false;
        telcount++;
        int origx = dmnew.partyx, origy = dmnew.partyy, origface = dmnew.facing;
        if (playsound) dmnew.playSound("teleport.wav", -1, -1);
        //change facing if should
        if (newface != -1) {
            boolean randomface = false;
            if (newface == 7) {
                randomface = true;
                newface = dmnew.randGen.nextInt(4);
            }
            int tempface = newface;
            if (newface == 4) { //spin right
                tempface = dmnew.facing - 1;
                if (tempface < 0) tempface = 3;
            } else if (newface == 5) { //spin left
                tempface = dmnew.facing + 1;
                if (tempface > 3) tempface = 0;
            } else if (newface == 6) { //spin 180 deg
                tempface = (dmnew.facing + 2) % 4;
            }
            int tempss[] = new int[4];
            dmnew.facing = tempface;
            dmnew.Projectile tempp;
            for (Iterator i = dmnew.dmprojs.iterator(); i.hasNext(); ) {
                tempp = (dmnew.Projectile) i.next();
                if (tempp.it != null && tempp.it.hasthrowpic) {
                    int s = 2; //left
                    if (dmnew.facing == tempp.direction) {
                        s = 0;
                    } //away
                    else if ((dmnew.facing - tempp.direction) % 2 == 0) {
                        s = 1;
                    }//towards - was Math.abs(facing-tem...)%2
                    else if (dmnew.facing == (tempp.direction + 1) % 4) {
                        s = 3;
                    } //right = dpic
                    tempp.pic = tempp.it.throwpic[s];
                }
            }
            if (randomface) newface = 7; //reset newface value
        }
        //if changing location too, do so (and test for mons in the way)
        if (targetlevel != level || targetx != xcoord || targety != ycoord) {
            hasParty = false;
            boolean movedmons = false;
            //if mon is in spot party teleporting to, trade places (unless this teleport already has mons on it too)
            if (swapplaces && !hasMons && dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                dmnew.DungeonMap[targetlevel][targetx][targety].hasMons = false;
                hasMons = true;
                dmnew.Monster tempmon;
                for (int sub = 0; sub < 6; sub++) {
                    tempmon = (dmnew.Monster) dmnew.dmmons.remove(targetlevel + "," + targetx + "," + targety + "," + sub);
                    if (tempmon != null) {
                        tempmon.level = level;
                        tempmon.x = dmnew.partyx;
                        tempmon.y = dmnew.partyy;
                        dmnew.dmmons.put(tempmon.level + "," + tempmon.x + "," + tempmon.y + "," + sub, tempmon);
                    }
                    if (sub == 3) sub = 5;
                }
                movedmons = true;
            }
            //move party to new location
            dmnew.level = targetlevel;
            dmnew.partyx = targetx;
            dmnew.partyy = targety;
            dmnew.DungeonMap[targetlevel][targetx][targety].hasParty = true;
            //if moved mons on target, check for switch step off
            int oldfacing = dmnew.facing;
            MapObject oldtarget = dmnew.DungeonMap[targetlevel][targetx][targety];
            if (movedmons) dmnew.DungeonMap[targetlevel][targetx][targety].tryFloorSwitch(MONSTEPPINGOFF);
            //if game not over, haven't been moved, and target square unchanged then test switch (and then teleport)
            if (!dmnew.gameover && dmnew.level == targetlevel && dmnew.partyx == targetx && dmnew.partyy == targety && dmnew.facing == oldfacing && oldtarget == dmnew.DungeonMap[targetlevel][targetx][targety]) {
                //test for switch step on
                dmnew.DungeonMap[targetlevel][targetx][targety].tryFloorSwitch(MapObject.PARTYSTEPPINGON);
                //if game not over, haven't been moved, and target square unchanged then test for pit/teleporter
                if (!dmnew.gameover && dmnew.level == targetlevel && dmnew.partyx == targetx && dmnew.partyy == targety && dmnew.facing == oldfacing && oldtarget == dmnew.DungeonMap[targetlevel][targetx][targety]) {
                    dmnew.DungeonMap[targetlevel][targetx][targety].tryTeleport();
                }
            }
            dmnew.updateDark();
        }
        telcount--;
        if (telcount == 0 && !dmnew.nomirroradjust && (dmnew.facing != origface || dmnew.partyx != origx || dmnew.partyy != origy || dmnew.level != level))
            dmnew.mirrorback = !dmnew.mirrorback;
        //dmnew.nomirroradjust = false;
        wasUsed = true;
    }
    
    //tel mon - takes monster being teleported
    public void tryTeleport(dmnew.Monster mon) {
        if ((type & MONSTER) == 0 || (!isReusable && wasUsed)) return;
        //if (!isOn && isSwitched) { toggle(); return; }
        if (!isOn && isSwitched) {
            activate();
            return;
        } else if (!isOn) return;
        if (newface != -1) {
            boolean randomface = false;
            if (newface == 7) {
                randomface = true;
                newface = dmnew.randGen.nextInt(4);
            }
            int tempface = newface;
            if (newface == 4) { //spin right
                tempface = mon.facing - 1;
                if (tempface < 0) tempface = 3;
            } else if (newface == 5) { //spin left
                tempface = mon.facing + 1;
                if (tempface > 3) tempface = 0;
            } else if (newface == 6) { //spin 180 deg
                tempface = (mon.facing + 2) % 4;
            }
            mon.facing = tempface;
            if (randomface) newface = 7;
        }
        if (targetlevel != level || targetx != xcoord || targety != ycoord) {
            dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
            hasMons = false;
            int i = 0;
            while (i < 6 && !hasMons) {
                if (dmnew.dmmons.get(level + "," + xcoord + "," + ycoord + "," + i) != null) hasMons = true;
                if (i == 3) i = 5;
                else i++;
            }
            boolean movedparty = false;
            //if party is in spot mon teleporting to, trade places
            if (swapplaces && dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) {
                if (playsound) dmnew.playSound("teleport.wav", -1, -1);
                dmnew.DungeonMap[targetlevel][targetx][targety].hasParty = false;
                hasParty = true;
                dmnew.partyx = mon.x;
                dmnew.partyy = mon.y;
                dmnew.level = mon.level;
                dmnew.updateDark();
                dmnew.needredraw = true;
                movedparty = true;
            }
            //if mon is in spot mon teleporting to, trade places (always if possible, regardless of swap setting)
            if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                dmnew.Monster tempmon = (dmnew.Monster) dmnew.dmmons.remove(targetlevel + "," + targetx + "," + targety + "," + mon.subsquare);
                if (tempmon != null) {
                    tempmon.x = mon.x;
                    tempmon.y = mon.y;
                    tempmon.level = mon.level;
                    dmnew.dmmons.put(tempmon.level + "," + tempmon.x + "," + tempmon.y + "," + tempmon.subsquare, tempmon);
                    hasMons = true;
                } else if (mon.subsquare == 5) {
                    //moving a big mon, so move all mons on new square to this one (won't be any others on this one to get in way, won't be a sub 5 mon on target)
                    for (int j = 0; j < 4; j++) {
                        tempmon = (dmnew.Monster) dmnew.dmmons.remove(targetlevel + "," + targetx + "," + targety + "," + j);
                        if (tempmon != null) {
                            tempmon.x = mon.x;
                            tempmon.y = mon.y;
                            tempmon.level = mon.level;
                            dmnew.dmmons.put(tempmon.level + "," + tempmon.x + "," + tempmon.y + "," + tempmon.subsquare, tempmon);
                            hasMons = true;
                        }
                    }
                } else {
                    tempmon = (dmnew.Monster) dmnew.dmmons.remove(targetlevel + "," + targetx + "," + targety + "," + 5);
                    if (tempmon != null) {
                        if (swapplaces) {
                            //must move the big mon to this square
                            //if this square already has any other mons on it they will die (the one being teleported has already been removed)
                            if (hasMons) for (int j = 0; j < 4; j++) {
                                dmnew.Monster tempmon2 = (dmnew.Monster) dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + j);
                                if (tempmon2 != null) tempmon2.pitDeath();
                            }
                            tempmon.x = mon.x;
                            tempmon.y = mon.y;
                            tempmon.level = mon.level;
                            dmnew.dmmons.put(tempmon.level + "," + tempmon.x + "," + tempmon.y + "," + tempmon.subsquare, tempmon);
                            hasMons = true;
                        } else {
                            //must kill the big mon on target square
                            tempmon.pitDeath();
                            dmnew.DungeonMap[targetlevel][targetx][targety].hasMons = false;
                        }
                    }
                }
            }
            //add mon to new square
            mon.x = targetx;
            mon.y = targety;
            mon.level = targetlevel;
            dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
            dmnew.DungeonMap[targetlevel][targetx][targety].hasMons = true;
            //test for floor switches if party was moved
            MapObject oldtarget = dmnew.DungeonMap[targetlevel][targetx][targety];
            int oldfacing = mon.facing;
            if (movedparty) dmnew.DungeonMap[targetlevel][targetx][targety].tryFloorSwitch(PARTYSTEPPINGOFF);
            //test for pit/teleporter if target has not changed and mon has not moved
            if (mon.x == targetx && mon.y == targety && mon.level == targetlevel && mon.facing == oldfacing && dmnew.DungeonMap[targetlevel][targetx][targety] == oldtarget)
                dmnew.DungeonMap[targetlevel][targetx][targety].tryTeleport(mon);
        }
        wasUsed = true;
    }
    
    //tel items - takes item
    public boolean tryTeleport(Item it) {
        
        if ((type & ITEM) == 0 || (!isReusable && wasUsed)) return false;
        if (!isOn && isSwitched) activate();//toggle();
        if (isOn) {
            if (targetlevel != level || targetx != xcoord || targety != ycoord) {
                //test for alcove
                if (newface < 4 && dmnew.DungeonMap[targetlevel][targetx][targety] instanceof Alcove) {
                    if (newface > -1) ((Alcove) dmnew.DungeonMap[targetlevel][targetx][targety]).addItem(it, newface);
                    else {
                        if (it.subsquare == 1) it.subsquare = 3;
                        else if (it.subsquare == 3) it.subsquare = 1;
                        ((Alcove) dmnew.DungeonMap[targetlevel][targetx][targety]).addItem(it, it.subsquare);
                    }
                }
                //test for teleport
                else if (!dmnew.DungeonMap[targetlevel][targetx][targety].tryTeleport(it)) {
                    dmnew.DungeonMap[targetlevel][targetx][targety].addItem(it);
                    //test for floorswitch
                    dmnew.DungeonMap[targetlevel][targetx][targety].tryFloorSwitch(MapObject.PUTITEM);
                }
            } else dmnew.DungeonMap[targetlevel][targetx][targety].addItem(it);
            wasUsed = true;
            return true;
        } else return false;
    }
    
    //tel projs - takes proj
    public void tryTeleport(dmnew.Projectile p) {
        if (!isOn || (type & PROJ) == 0 || (!isReusable && wasUsed)) return;
        boolean randomface = false;
        if (newface == 7) {
            randomface = true;
            newface = dmnew.randGen.nextInt(4);
        }
        
        int oldface = newface;
        if (newface < 4) {
            if ((p.direction + 1) % 4 == newface) newface = 5;
            else if ((newface + 1) % 4 == p.direction) newface = 4;
        }
        
        if (newface == 5 && (((p.direction == 0 || p.direction == 2) && (p.subsquare == 0 || p.subsquare == 2)) || ((p.direction == 1 || p.direction == 3) && (p.subsquare == 1 || p.subsquare == 3)))) {
            if (newface != oldface) newface = oldface;
            return;
        }
        if (newface == 4 && (((p.direction == 0 || p.direction == 2) && (p.subsquare == 1 || p.subsquare == 3)) || ((p.direction == 1 || p.direction == 3) && (p.subsquare == 0 || p.subsquare == 2)))) {
            if (newface != oldface) newface = oldface;
            return;
        }
        
        if (targetlevel == level && targetx == xcoord && targety == ycoord) {
            if (newface == 5 && p.direction == 0 && p.subsquare == 1) p.notelnext = true;
            else if (newface == 5 && p.direction == 2 && p.subsquare == 3) p.notelnext = true;
            else if (newface == 5 && p.direction == 1 && p.subsquare == 0) p.notelnext = true;
            else if (newface == 5 && p.direction == 3 && p.subsquare == 2) p.notelnext = true;
            else if (newface == 4 && p.direction == 0 && p.subsquare == 0) p.notelnext = true;
            else if (newface == 4 && p.direction == 2 && p.subsquare == 2) p.notelnext = true;
            else if (newface == 4 && p.direction == 1 && p.subsquare == 3) p.notelnext = true;
            else if (newface == 4 && p.direction == 3 && p.subsquare == 1) p.notelnext = true;
        }
        
        if (targetlevel != level || targetx != xcoord || targety != ycoord) {
            dmnew.DungeonMap[p.level][p.x][p.y].numProjs--;
            p.level = targetlevel;
            p.x = targetx;
            p.y = targety;
        }
        if (newface != -1) {
            int tempface = newface;
            if (newface == 4) { //spin right
                tempface = p.direction - 1;
                if (tempface < 0) tempface = 3;
            } else if (newface == 5) { //spin left
                tempface = p.direction + 1;
                if (tempface > 3) tempface = 0;
            } else if (newface == 6) { //spin 180 deg
                tempface = (p.direction + 2) % 4;
            }
            p.direction = tempface;
            if (p.it != null && p.it.hasthrowpic && p.level == dmnew.level) {
                int s = 2; //left
                if (dmnew.facing == p.direction) {
                    s = 0;
                } //away
                else if ((dmnew.facing - p.direction) % 2 == 0) {
                    s = 1;
                }//towards - was Math.abs(facing-tem...)%2
                else if (dmnew.facing == (p.direction + 1) % 4) {
                    s = 3;
                } //right = dpic
                p.pic = p.it.throwpic[s];
            }
            if (randomface) newface = 7;
            else if (newface != oldface) newface = oldface;
        }
        if (targetlevel != level || targetx != xcoord || targety != ycoord) {
            dmnew.DungeonMap[targetlevel][targetx][targety].numProjs++;
            //test for teleport
            dmnew.DungeonMap[targetlevel][targetx][targety].tryTeleport(p);
        }
        wasUsed = true;
    }
    
    public boolean changeState() {
        if (isSwitched) {
            if (activatecycle == currentcycle) {
            } //just activated this cycle, so do nothing yet
            else if (delaying) {
                delaycounter++;
                if (delaycounter >= delay) {
                    delaying = false;
                    delaycounter = 0;
                    isOn = !isOn;
                    if (isOn) doTeleport();
                    if (reset > 0) {
                        resetting = true;
                    }
                }
            } else if (resetting) {
                resetcounter++;
                if (resetcounter >= reset) {
                    resetting = false;
                    resetcounter = 0;
                    isOn = !isOn;
                    if (isOn) doTeleport();
                }
            }
            activatecycle = -1;
        } else if (isBlinker && isActive) {
            blinkcounter++;
            if (isOn) {
                if (blinkcounter > blinkrateon) {
                    blinkcounter = 0;
                    isOn = false;
                    dmnew.needredraw = true;
                }
            } else if (blinkcounter > blinkrateoff) {
                blinkcounter = 0;
                isOn = true;
                dmnew.needredraw = true;
                doTeleport();
            }
        }
        if (isOn && isVisible && level == dmnew.level) {
            int xdist = xcoord - dmnew.partyx;
            if (xdist < 0) xdist *= -1;
            int ydist = ycoord - dmnew.partyy;
            if (ydist < 0) ydist *= -1;
            if (xdist < 5 && ydist < 5) { //close, so anim and repaint
                animcounter = (animcounter + 1) % 4;
                if (animcounter == 0 || animcounter == 2) dmnew.needredraw = true;
            }
        }
        return true;
    }
    
    public void doAction() {
        if (isOn && (hasParty || hasMons || hasItems || numProjs > 0)) doTeleport();
    }
    
    public void doTeleport() {
        if (wasUsed && !isReusable) return;
        //teleport any mons,party,projs,items on it
        if (hasParty) {
            tryTeleport();
        }
        if (hasMons && (type & MONSTER) != 0) {
            dmnew.Monster tempmon;
            int i = 0;
            while (i < 6 && hasMons) {
                tempmon = (dmnew.Monster) dmnew.dmmons.get(level + "," + xcoord + "," + ycoord + "," + i);
                if (tempmon != null) tryTeleport(tempmon);
                if (i == 3) i = 5;
                else i++;
            }
        }
        if (hasItems && (type & ITEM) != 0 && (targetx != xcoord || targety != ycoord || targetlevel != level)) {
            hasItems = false;
            while (mapItems.size() > 0) {
                tryTeleport((Item) mapItems.remove(0));
            }
        }
        if (numProjs > 0 && (type & PROJ) != 0 && (targetx != xcoord || targety != ycoord || newface != -1 || targetlevel != level)) {
            dmnew.Projectile p;
            int tempnum = numProjs;
            int index1 = 0;
            int index2 = 0;
            while (index1 < tempnum && index2 < dmnew.dmprojs.size()) {
                p = (dmnew.Projectile) dmnew.dmprojs.get(index2);
                if (p.level == level && p.x == xcoord && p.y == ycoord) {
                    tryTeleport(p);
                    index1++;
                }
                index2++;
            }
            //if (targetx!=xcoord || targety!=ycoord || targetlevel!=level) numProjs=0;
        }
        dmnew.needredraw = true;
    }
    
    public void toggle() {
        if (isBlinker) {
            blinkcounter = 0;
            isActive = !isActive;
        } else if (isSwitched && (delay > 0 || reset > 0)) {
            if (delaying || resetting) return;
            if (level == dmnew.level) dmnew.playSound("switch.wav", xcoord, ycoord);
            activatecycle = currentcycle;
            if (delay > 0) delaying = true;
            else {
                isOn = !isOn;
                if (isOn) doTeleport();
                resetting = true;
            }
        } else {
            isOn = !isOn;
            if (isOn) doTeleport();
        }
    }
    
    public void activate() {
        if (!isOn || (isBlinker && !isActive)) {
            if (count > 0) {
                if (isSwitched && level == dmnew.level) dmnew.playSound("switch.wav", xcoord, ycoord);
                count--;
                return;
            }
            if (resetcount) count = maxcount;
            toggle();
        }
    }
    
    public void deactivate() {
        if (isOn && !isBlinker) toggle();
        else if (isBlinker && isActive) {
            toggle();
            if (!isActive) isOn = false;
        } else if (count < maxcount) {
            if (isSwitched && level == dmnew.level) dmnew.playSound("switch.wav", xcoord, ycoord);
            count++;
        }
    }
    
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(level);
        so.writeInt(xcoord);
        so.writeInt(ycoord);
        so.writeInt(type);
        so.writeInt(targetlevel);
        so.writeInt(targetx);
        so.writeInt(targety);
        so.writeInt(newface);
        so.writeInt(blinkrateon);
        so.writeInt(blinkrateoff);
        so.writeInt(blinkcounter);
        so.writeBoolean(isSwitched);
        so.writeBoolean(switchVisible);
        so.writeInt(delay);
        so.writeInt(reset);
        so.writeBoolean(isReusable);
        so.writeBoolean(isVisible);
        so.writeBoolean(isActive);
        so.writeBoolean(isOn);
        so.writeBoolean(playsound);
        so.writeBoolean(swapplaces);
        so.writeInt(maxcount);
        so.writeInt(count);
        so.writeBoolean(resetcount);
        so.writeBoolean(delaying);
        so.writeInt(delaycounter);
        so.writeBoolean(resetting);
        so.writeInt(resetcounter);
        so.writeBoolean(wasUsed);
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        level = si.readInt();
        xcoord = si.readInt();
        ycoord = si.readInt();
        type = si.readInt();
        targetlevel = si.readInt();
        targetx = si.readInt();
        targety = si.readInt();
        newface = si.readInt();
        blinkrateon = si.readInt();
        blinkrateoff = si.readInt();
        blinkcounter = si.readInt();
        if (blinkrateon > 0 || blinkrateoff > 0) isBlinker = true;
        isSwitched = si.readBoolean();
        switchVisible = si.readBoolean();
        delay = si.readInt();
        reset = si.readInt();
        isReusable = si.readBoolean();
        isVisible = si.readBoolean();
        isActive = si.readBoolean();
        isOn = si.readBoolean();
        playsound = si.readBoolean();
        swapplaces = si.readBoolean();
        maxcount = si.readInt();
        count = si.readInt();
        resetcount = si.readBoolean();
        delaying = si.readBoolean();
        delaycounter = si.readInt();
        resetting = si.readBoolean();
        resetcounter = si.readInt();
        wasUsed = si.readBoolean();
        if ((!ADDEDPICS && isVisible) || (switchVisible && !FloorSwitch.ADDEDPICS)) setPics();
    }
}
