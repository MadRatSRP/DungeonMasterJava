import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

class Pit extends Floor {
    static public boolean ADDEDPICS, ADDEDCONCEALED;
    static public Image[][] cpic = new Image[4][3];
    private int level, xcoord, ycoord;
    public boolean isConcealed = false;//true if can only see outline, and only when right near - not implemented yet
    public boolean isIllusionary = false;//if true, always looks open
    private boolean isSupplies = false;//true if supplies for quick type
    private int delay, reset;//for supplies type
    public int delaycounter = 0, resetcounter = 0;//for supplies type
    public boolean delaying, resetting;
    private boolean monswork;
    private int count, maxcount;
    private boolean resetcount;
    public boolean isOpen = true;
    public boolean isContinuous = false;//true if blinks
    public boolean isActive = true;//true if starts blinking, false if needs a switch to start
    public int blinkrateo, blinkratec, blinkcounter = 0;
    public boolean screamed = false;
    
    //lvl,x,y - level and xy coords of this generator
    //blnk - blink rate (how long between each open and close, ignored if not added to mapstochange)
    public Pit(int lvl, int x, int y, boolean isop, boolean isconc, boolean isillus, boolean issup, boolean iscont, boolean isac, int blnko, int blnkc, int dly, int rst, boolean mw, int mcnt, int cnt, boolean rcnt) {
        super();
        mapchar = 'p';
        level = lvl;
        xcoord = x;
        ycoord = y;
        isOpen = isop;
        isConcealed = isconc;
        isIllusionary = isillus;
        isSupplies = issup;
        isContinuous = iscont;
        isActive = isac;
        blinkrateo = blnko;
        blinkratec = blnkc;
        delay = dly;
        reset = rst;
        monswork = mw;
        maxcount = mcnt;
        count = cnt;
        resetcount = rcnt;
        setPics();
    }
    
    public void tryTeleport() {
        if (isOpen) dropall(level + 1);
        else if (isSupplies) activate();
    }
    
    public void tryTeleport(dmnew.Monster mon) {
        if (mon.isflying) return;
        if (isOpen) dropall(level + 1);
        else if (isSupplies && monswork) activate();
    }
    
    public boolean tryTeleport(Item it) {
        if (!isOpen) return false; //item doesn't fall
        addItem(it);
        dropall(level + 1);
        return true;
    }
    
    public boolean changeState() {
        if (isSupplies) {
            if (delaying) {
                delaycounter++;
                if (delaycounter >= delay) {
                    delaying = false;
                    delaycounter = 0;
                    openOrClose();
                    if (reset > 0) {
                        resetting = true;
                        return true;
                    } else return false;
                }
            } else if (resetting) {
                resetcounter++;
                if (resetcounter >= reset) {
                    resetting = false;
                    resetcounter = 0;
                    openOrClose();
                    return false;
                }
            } else return false;
            return true;
        } else if (!isActive) return true;//was return false
        blinkcounter++;
        if (isOpen) {
            if (blinkcounter > blinkrateo) {
                openOrClose();
                blinkcounter = 0;
            }
        } else if (blinkcounter > blinkratec) {
            openOrClose();
            blinkcounter = 0;
        }
        return true;
    }
    
    public void toggle() {
        if (isContinuous) isActive = !isActive;
        else if (isSupplies && (delay > 0 || reset > 0)) {
            if (delaying || resetting) return;
            if (level == dmnew.level) dmnew.playSound("switch.wav", xcoord, ycoord);
            if (delay > 0) delaying = true;
            else {
                openOrClose();
                resetting = true;
            }
            //add to mapstochange
            MapPoint xy = new MapPoint(level, xcoord, ycoord);
            if (!dmnew.mapstochange.contains(xy)) dmnew.mapstochange.add(xy);
            dmnew.mapchanging = true;
        } else openOrClose();
    }
    
    public void activate() {
        if ((!isOpen && !isContinuous) || (isContinuous && !isActive)) {
            if (count > 0) {
                if (isSupplies && level == dmnew.level) dmnew.playSound("switch.wav", xcoord, ycoord);
                count--;
                return;
            }
            if (resetcount) count = maxcount;
            toggle();
        }
    }
    
    public void deactivate() {
        if (isOpen && !isContinuous) toggle();
        else if (isContinuous && isActive) {
            toggle();
            if (!isActive && isOpen) openOrClose();
        } else if (count < maxcount) {
            if (isSupplies && level == dmnew.level) dmnew.playSound("switch.wav", xcoord, ycoord);
            count++;
        }
    }
    
    private void openOrClose() {
        isOpen = !isOpen;
        if (isOpen) dropall(level + 1);
        else if (level == dmnew.level || level == dmnew.level - 1) {
            int xdist = xcoord - dmnew.partyx;
            if (xdist < 0) xdist *= -1;
            int ydist = ycoord - dmnew.partyy;
            if (ydist < 0) ydist *= -1;
            if (xdist < 5 && ydist < 5) dmnew.needredraw = true;
        }
    }
    
    public void doAction() {
        if (isOpen && (hasParty || hasMons || hasItems)) {
            //if (hasParty) dmnew.mirrorback=!dmnew.mirrorback;
            dropall(level + 1);
        }
    }
    
    private boolean checkForPit(int droplevel) {
        //if another pit below, increase droplevel
        if ((dmnew.DungeonMap[droplevel][xcoord][ycoord] instanceof Pit) && ((Pit) dmnew.DungeonMap[droplevel][xcoord][ycoord]).isOpen) {
            //ignores any flying mons hovering over the pit below
            if (hasParty) {
                dmnew.needdrawdungeon = true;
                dmnew.dview.repaint();
                try {
                    Thread.currentThread().sleep(200);
                } catch (InterruptedException e) {
                }
                dmnew.level = droplevel;
                dmnew.mirrorback = !dmnew.mirrorback;
                dmnew.updateDark();
            }
            dropall(droplevel + 1);
            return true;
        }
        return false;
    }
    
    public void dropall(int droplevel) {
        if (hasParty && !screamed && !dmnew.climbing) {
            dmnew.playSound("scream.wav", -1, -1);
            screamed = true;
        }
        if (checkForPit(droplevel)) return;
        
        boolean monsfall = false, partyfalls = false;
        dmnew.Monster tempmon = null;
        Graphics tempg = null;
        int damage;
        
        //drop items
        if (hasItems) {
            Item tempitem;
            while (mapItems.size() > 0) {
                tempitem = (Item) mapItems.remove(0);
                //stuff falls on mons
                if (dmnew.DungeonMap[droplevel][xcoord][ycoord].hasMons) {
                    tempmon = (dmnew.Monster) dmnew.dmmons.get(droplevel + "," + xcoord + "," + ycoord + "," + tempitem.subsquare);
                    if (tempmon == null)
                        tempmon = (dmnew.Monster) dmnew.dmmons.get(droplevel + "," + xcoord + "," + ycoord + "," + 5);
                    if (tempmon != null && !tempmon.isImmaterial) {
                        tempmon.damage((droplevel - level) * dmnew.randGen.nextInt(10), dmnew.WEAPONHIT);
                    }
                }
                //stuff falls on party
                if (dmnew.DungeonMap[droplevel][xcoord][ycoord].hasParty) {
                    for (int i = 0; i < dmnew.numheroes; i++) {
                        dmnew.hero[i].damage((droplevel - level) * dmnew.randGen.nextInt(10), dmnew.DOORHIT);
                    }
                }
                //in case stuff falls on active teleporter
                if (!dmnew.DungeonMap[droplevel][xcoord][ycoord].tryTeleport(tempitem)) {
                    dmnew.DungeonMap[droplevel][xcoord][ycoord].addItem(tempitem);
                    //in case stuff falls on floorswitch
                    dmnew.DungeonMap[droplevel][xcoord][ycoord].tryFloorSwitch(MapObject.PUTITEM);
                }
                if (checkForPit(droplevel)) return;
            }
            hasItems = false;
        }
        //drop non-flying mons
        if (hasMons) {
            boolean montest = false;
            dmnew.Monster tempmon2;
            for (int i = 0; i < 6; i++) {
                tempmon = (dmnew.Monster) dmnew.dmmons.get(level + "," + xcoord + "," + ycoord + "," + i);
                if (tempmon != null) {
                    if (tempmon.isflying) montest = true;
                    else if (tempmon.number == 24 && tempmon.teleport()) {
                    } else {
                        dmnew.dmmons.remove(level + "," + xcoord + "," + ycoord + "," + i);
                        tempmon.level = droplevel;
                        //fall on other mons
                        if (dmnew.DungeonMap[droplevel][xcoord][ycoord].hasMons) {
                            if (tempmon.subsquare == 5) {
                                for (int j = 0; j < 6; j++) {
                                    tempmon2 = (dmnew.Monster) dmnew.dmmons.get(droplevel + "," + xcoord + "," + ycoord + "," + j);
                                    if (tempmon2 != null) {
                                        if ((tempmon2.number == 24 || tempmon2.number == 26) && tempmon2.teleport()) {
                                        } else tempmon2.pitDeath();
                                    }
                                    if (i == 3) i++;
                                }
                            } else {
                                tempmon2 = (dmnew.Monster) dmnew.dmmons.get(droplevel + "," + xcoord + "," + ycoord + "," + tempmon.subsquare);
                                if (tempmon2 != null && ((tempmon2.number != 24 && tempmon2.number != 26) || !tempmon2.teleport()))
                                    tempmon2.pitDeath();
                            }
                        }
                        //fall on party
                        else if (dmnew.DungeonMap[droplevel][xcoord][ycoord].hasParty && !tempmon.isImmaterial) {
                            for (int j = 0; j < dmnew.numheroes; j++) {
                                if (tempmon.subsquare == 5 || tempmon.subsquare == dmnew.hero[j].subsquare) {
                                    damage = (droplevel - level) * (dmnew.randGen.nextInt(16) + 20);
                                    damage += (droplevel - level) * 15 + ((droplevel - level) - 2) * 15;
                                    dmnew.hero[j].damage(damage, dmnew.WEAPONHIT);
                                }
                            }
                            //falling mon damaged
                            //tempmon.damage((droplevel-level)*(Math.abs(dmnew.randGen.nextInt()%10+20)),dmnew.WEAPONHIT);
                        }
                        //continue if not gameover (since party could die if mon falls on them)
                        if (!dmnew.gameover) {
                            //add mon to new square
                            dmnew.DungeonMap[droplevel][xcoord][ycoord].hasMons = true;
                            dmnew.dmmons.put(droplevel + "," + xcoord + "," + ycoord + "," + tempmon.subsquare, tempmon);
                            //in case falls on active teleporter
                            dmnew.DungeonMap[droplevel][xcoord][ycoord].tryTeleport(tempmon);
                            //if not gameover and didn't teleport:
                            if (!dmnew.gameover && tempmon.level == droplevel && tempmon.x == xcoord && tempmon.y == ycoord) {
                                //in case falls on floorswitch
                                dmnew.DungeonMap[droplevel][xcoord][ycoord].tryFloorSwitch(MONSTEPPINGON);
                                //if not gameover, mon not dying, and mon not immaterial, damage from fall:
                                if (!tempmon.isdying && !tempmon.isImmaterial && !dmnew.gameover) {
                                    damage = (droplevel - level) * (dmnew.randGen.nextInt(16) + 20);
                                    damage += (droplevel - level) * 15 + ((droplevel - level) - 2) * 15;
                                    tempmon.damage(damage, dmnew.WEAPONHIT);
                                }
                            }
                        }
                        if (checkForPit(droplevel)) return;
                    }
                }
                if (i == 3) i++;
            }
            if (!montest) hasMons = false;
        }
        
        //drop party
        if (hasParty) {
            screamed = false;
            dmnew.needredraw = true;
            dmnew.needdrawdungeon = true;
            dmnew.dview.repaint();
            hasParty = false;
            dmnew.walkqueue.clear();
            try {
                Thread.currentThread().sleep(200);
            } catch (InterruptedException e) {
            }
            dmnew.level = droplevel;
            dmnew.DungeonMap[droplevel][xcoord][ycoord].hasParty = true;
            dmnew.mirrorback = !dmnew.mirrorback;
            dmnew.nomirroradjust = true;
            dmnew.updateDark();
            //fall on mons
            if (dmnew.DungeonMap[droplevel][xcoord][ycoord].hasMons) {
                for (int i = 0; i < 6; i++) {
                    if (i == 5 || dmnew.heroatsub[i] != -1) {
                        tempmon = (dmnew.Monster) dmnew.dmmons.get(level + "," + xcoord + "," + ycoord + "," + i);
                        if (tempmon != null && !tempmon.isdying) {
                            if ((tempmon.number == 24 || tempmon.number == 26) && tempmon.teleport()) {
                            } else if (!tempmon.isImmaterial) {
                                damage = (droplevel - level) * (dmnew.randGen.nextInt(16) + 20);
                                damage += (droplevel - level) * 15 + ((droplevel - level) - 2) * 15;
                                tempmon.damage(damage, dmnew.WEAPONHIT);
                            }
                        }
                    }
                    if (i == 3) i = 5;
                }
            }
            //continue if not gameover and didn't get moved (since falling on mon could kill it and trigger switch)
            if (!dmnew.gameover && dmnew.DungeonMap[droplevel][xcoord][ycoord].hasParty) {
                if (checkForPit(droplevel)) return;
                //in case falls on active teleporter
                dmnew.DungeonMap[droplevel][xcoord][ycoord].tryTeleport();
                //hurt from fall if not teleported:
                if (!dmnew.climbing && dmnew.DungeonMap[droplevel][xcoord][ycoord].hasParty) {
                    for (int i = 0; i < dmnew.numheroes; i++) {
                        damage = (droplevel - level) * (dmnew.randGen.nextInt(16) + 20);
                        damage += (droplevel - level) * 15 + ((droplevel - level) - 2) * 15;
                        dmnew.hero[i].damage(damage, dmnew.WEAPONHIT);
                    }
                } else if (dmnew.floatcounter == 0) dmnew.climbing = false;
                //in case falls on floorswitch (if not gameover and didn't get moved)
                if (!dmnew.gameover && dmnew.DungeonMap[droplevel][xcoord][ycoord].hasParty)
                    dmnew.DungeonMap[droplevel][xcoord][ycoord].tryFloorSwitch(PARTYSTEPPINGON);
            }
        }
        if (level == dmnew.level || level == dmnew.level - 1) {
            int xdist = xcoord - dmnew.partyx;
            if (xdist < 0) xdist *= -1;
            int ydist = ycoord - dmnew.partyy;
            if (ydist < 0) ydist *= -1;
            if (xdist < 5 && ydist < 5) dmnew.needredraw = true;
        }
    }
    
    public void setPics() {
        pic = new Image[4][5];
        //ceiling pics
        if (!ADDEDPICS && !ADDEDCONCEALED) {
            cpic[0][0] = loadPic("cpit01.gif");
            cpic[0][1] = loadPic("cpit02.gif");
            cpic[0][2] = loadPic("cpit03.gif");
            cpic[1][0] = loadPic("cpit11.gif");
            cpic[1][1] = loadPic("cpit12.gif");
            cpic[1][2] = loadPic("cpit13.gif");
            cpic[2][0] = loadPic("cpit21.gif");
            cpic[2][1] = loadPic("cpit22.gif");
            cpic[2][2] = loadPic("cpit23.gif");
            cpic[3][0] = loadPic("cpit31.gif");
            cpic[3][1] = loadPic("cpit32.gif");
            cpic[3][2] = loadPic("cpit33.gif");
            tracker.addImage(cpic[0][0], 0);
            tracker.addImage(cpic[0][1], 0);
            tracker.addImage(cpic[0][2], 0);
            tracker.addImage(cpic[1][0], 0);
            tracker.addImage(cpic[1][1], 0);
            tracker.addImage(cpic[1][2], 0);
            tracker.addImage(cpic[2][0], 0);
            tracker.addImage(cpic[2][1], 0);
            tracker.addImage(cpic[2][2], 0);
            tracker.addImage(cpic[3][0], 0);
            tracker.addImage(cpic[3][1], 0);
            tracker.addImage(cpic[3][2], 0);
        }
        //standard pics
        if (!isConcealed) {
            pic[0][1] = loadPic("pit01.gif");
            pic[0][2] = loadPic("pit02.gif");
            pic[0][3] = loadPic("pit03.gif");
            pic[1][1] = loadPic("pit11.gif");
            pic[1][2] = loadPic("pit12.gif");
            pic[1][3] = loadPic("pit13.gif");
            pic[2][1] = loadPic("pit21.gif");
            pic[2][2] = loadPic("pit22.gif");
            pic[2][3] = loadPic("pit23.gif");
            pic[3][1] = loadPic("pit31.gif");
            pic[3][2] = loadPic("pit32.gif");
            pic[3][3] = loadPic("pit33.gif");
            if (!ADDEDPICS) {
                tracker.addImage(pic[0][1], 0);
                tracker.addImage(pic[0][2], 0);
                tracker.addImage(pic[0][3], 0);
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
        } else {
            pic[0][1] = loadPic("concealpit01.gif");
            pic[0][2] = loadPic("concealpit02.gif");
            pic[0][3] = loadPic("concealpit03.gif");
            pic[1][1] = loadPic("concealpit11.gif");
            pic[1][2] = loadPic("concealpit12.gif");
            pic[1][3] = loadPic("concealpit13.gif");
            pic[2][1] = loadPic("concealpit21.gif");
            pic[2][2] = loadPic("concealpit22.gif");
            pic[2][3] = loadPic("concealpit23.gif");
            pic[3][1] = blankpic;
            pic[3][2] = blankpic;
            pic[3][3] = blankpic;
            if (!ADDEDCONCEALED) {
                tracker.addImage(pic[0][1], 1);
                tracker.addImage(pic[0][2], 1);
                tracker.addImage(pic[0][3], 1);
                tracker.addImage(pic[1][1], 1);
                tracker.addImage(pic[1][2], 1);
                tracker.addImage(pic[1][3], 1);
                tracker.addImage(pic[2][1], 1);
                tracker.addImage(pic[2][2], 1);
                tracker.addImage(pic[2][3], 1);
                ADDEDCONCEALED = true;
            }
        }
    }
    
    public static void redoPics() {
        String newmapdir = Wall.currentdir + File.separator;
        File testfile = new File(newmapdir + "cpit12.gif");
        if (!testfile.exists()) return;
        cpic[0][0] = dmnew.tk.getImage(newmapdir + "cpit01.gif");
        cpic[0][1] = dmnew.tk.getImage(newmapdir + "cpit02.gif");
        cpic[0][2] = dmnew.tk.getImage(newmapdir + "cpit03.gif");
        cpic[1][0] = dmnew.tk.getImage(newmapdir + "cpit11.gif");
        cpic[1][1] = dmnew.tk.getImage(newmapdir + "cpit12.gif");
        cpic[1][2] = dmnew.tk.getImage(newmapdir + "cpit13.gif");
        cpic[2][0] = dmnew.tk.getImage(newmapdir + "cpit21.gif");
        cpic[2][1] = dmnew.tk.getImage(newmapdir + "cpit22.gif");
        cpic[2][2] = dmnew.tk.getImage(newmapdir + "cpit23.gif");
        cpic[3][0] = dmnew.tk.getImage(newmapdir + "cpit31.gif");
        cpic[3][1] = dmnew.tk.getImage(newmapdir + "cpit32.gif");
        cpic[3][2] = dmnew.tk.getImage(newmapdir + "cpit33.gif");
        tracker.addImage(cpic[0][0], 5);
        tracker.addImage(cpic[0][1], 5);
        tracker.addImage(cpic[0][2], 5);
        tracker.addImage(cpic[1][0], 5);
        tracker.addImage(cpic[1][1], 5);
        tracker.addImage(cpic[1][2], 5);
        tracker.addImage(cpic[2][0], 5);
        tracker.addImage(cpic[2][1], 5);
        tracker.addImage(cpic[2][2], 5);
        tracker.addImage(cpic[3][0], 5);
        tracker.addImage(cpic[3][1], 5);
        tracker.addImage(cpic[3][2], 5);
        try {
            tracker.waitForID(5, 2000);
        } catch (InterruptedException ex) {
        }
        tracker.removeImage(cpic[0][0], 5);
        tracker.removeImage(cpic[0][1], 5);
        tracker.removeImage(cpic[0][2], 5);
        tracker.removeImage(cpic[1][0], 5);
        tracker.removeImage(cpic[1][1], 5);
        tracker.removeImage(cpic[1][2], 5);
        tracker.removeImage(cpic[2][0], 5);
        tracker.removeImage(cpic[2][1], 5);
        tracker.removeImage(cpic[2][2], 5);
        tracker.removeImage(cpic[3][0], 5);
        tracker.removeImage(cpic[3][1], 5);
        tracker.removeImage(cpic[3][2], 5);
    }
    
    public void redoPitPics() {
        String newmapdir = Wall.currentdir + File.separator;
        File testfile;
        if (!isConcealed) {
            testfile = new File(newmapdir + "pit12.gif");
            if (!testfile.exists()) return;
            pic[0][1] = dmnew.tk.getImage(newmapdir + "pit01.gif");
            pic[0][2] = dmnew.tk.getImage(newmapdir + "pit02.gif");
            pic[0][3] = dmnew.tk.getImage(newmapdir + "pit03.gif");
            pic[1][1] = dmnew.tk.getImage(newmapdir + "pit11.gif");
            pic[1][2] = dmnew.tk.getImage(newmapdir + "pit12.gif");
            pic[1][3] = dmnew.tk.getImage(newmapdir + "pit13.gif");
            pic[2][1] = dmnew.tk.getImage(newmapdir + "pit21.gif");
            pic[2][2] = dmnew.tk.getImage(newmapdir + "pit22.gif");
            pic[2][3] = dmnew.tk.getImage(newmapdir + "pit23.gif");
            pic[3][1] = dmnew.tk.getImage(newmapdir + "pit31.gif");
            pic[3][2] = dmnew.tk.getImage(newmapdir + "pit32.gif");
            pic[3][3] = dmnew.tk.getImage(newmapdir + "pit33.gif");
        } else {
            testfile = new File(newmapdir + "concealpit12.gif");
            if (!testfile.exists()) return;
            pic[0][1] = dmnew.tk.getImage(newmapdir + "concealpit01.gif");
            pic[0][2] = dmnew.tk.getImage(newmapdir + "concealpit02.gif");
            pic[0][3] = dmnew.tk.getImage(newmapdir + "concealpit03.gif");
            pic[1][1] = dmnew.tk.getImage(newmapdir + "concealpit11.gif");
            pic[1][2] = dmnew.tk.getImage(newmapdir + "concealpit12.gif");
            pic[1][3] = dmnew.tk.getImage(newmapdir + "concealpit13.gif");
            pic[2][1] = dmnew.tk.getImage(newmapdir + "concealpit21.gif");
            pic[2][2] = dmnew.tk.getImage(newmapdir + "concealpit22.gif");
            pic[2][3] = dmnew.tk.getImage(newmapdir + "concealpit23.gif");
        }
        tracker.addImage(pic[0][1], 5);
        tracker.addImage(pic[0][2], 5);
        tracker.addImage(pic[0][3], 5);
        tracker.addImage(pic[1][1], 5);
        tracker.addImage(pic[1][2], 5);
        tracker.addImage(pic[1][3], 5);
        tracker.addImage(pic[2][1], 5);
        tracker.addImage(pic[2][2], 5);
        tracker.addImage(pic[2][3], 5);
        if (!isConcealed) {
            tracker.addImage(pic[3][1], 5);
            tracker.addImage(pic[3][2], 5);
            tracker.addImage(pic[3][3], 5);
        }
        
        try {
            tracker.waitForID(5, 2000);
        } catch (InterruptedException ex) {
        }
        
        tracker.removeImage(pic[0][1], 5);
        tracker.removeImage(pic[0][2], 5);
        tracker.removeImage(pic[0][3], 5);
        tracker.removeImage(pic[1][1], 5);
        tracker.removeImage(pic[1][2], 5);
        tracker.removeImage(pic[1][3], 5);
        tracker.removeImage(pic[2][1], 5);
        tracker.removeImage(pic[2][2], 5);
        tracker.removeImage(pic[2][3], 5);
        if (!isConcealed) {
            tracker.removeImage(pic[3][1], 5);
            tracker.removeImage(pic[3][2], 5);
            tracker.removeImage(pic[3][3], 5);
        }
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        //super.drawPic(row,col,xc,yc,g,obs);
        //draw a pit if open or illusionary
        if ((isOpen || (isIllusionary && dmnew.dispell == 0)) && col != 0 && col != 4 && (!isConcealed || row < 3)) {
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
            
            if (col == 2 && dmnew.mirrorback)
                g.drawImage(pic[row][col], xc + rowadjustx + pic[row][2].getWidth(null), yc + rowadjusty, xc + rowadjustx, yc + rowadjusty + pic[row][2].getHeight(null), 0, 0, pic[row][2].getWidth(null), pic[row][2].getHeight(null), null);
            else g.drawImage(pic[row][col], xc + rowadjustx, yc + rowadjusty, null);
        }
        if (col > 0 && col < 4) drawContents(g, 3 - row, col - 1);
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(level);
        so.writeInt(xcoord);
        so.writeInt(ycoord);
        so.writeBoolean(isOpen);
        so.writeBoolean(isConcealed);
        so.writeBoolean(isIllusionary);
        so.writeBoolean(isSupplies);
        so.writeBoolean(isContinuous);
        so.writeBoolean(isActive);
        so.writeInt(blinkrateo);
        so.writeInt(blinkratec);
        so.writeInt(delay);
        so.writeInt(reset);
        so.writeBoolean(monswork);
        so.writeInt(maxcount);
        so.writeInt(count);
        so.writeBoolean(resetcount);
        so.writeInt(blinkcounter);
        so.writeBoolean(delaying);
        so.writeInt(delaycounter);
        so.writeBoolean(resetting);
        so.writeInt(resetcounter);
    }
}
