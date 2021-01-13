//timers:
//resets after certain time
//delay before works
//both of above
// -- kind of like supplies for quick pit switches - these probably own special type
//need a goesbothways flag (a switch that only opens a door, but is reusable, vs a switch that opens or closes a door)
//for multiple targets, have changeto[],oldMapObject[],targetx[],targety[]

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class FloorSwitch extends Floor {
    static public boolean ADDEDPICS;
    static public Image[][] fswitchpic = new Image[4][5];
    protected MapPoint xy, target;
    protected int type;
    private int targetlevel, targetx, targety; //xy of target mapsquare to change
    protected boolean isReusable;
    protected boolean wasUsed;
    protected boolean playsound;
    public boolean haspic; //visible or not?
    protected boolean switchstate;
    public int actiontype;
    protected int delay; //delay time before works, 0 for none
    protected int reset; //time until resets, 0 for no reset
    protected int changecount = 0; //for delay and reset
    public boolean delaying, resetting, resetnotrigger; //set true in tryswitch if has delay/reset
    private MapObject changeto;
    private MapObject oldMapObject;
    private String soundstring;
    private int loopsound;
    private boolean abrupt, retainitems;
    //public boolean partyflag = false, monflag = false;
    public int switchface;
    
    //switch types:
    //works with everything
    //any constant weight
    //party or mon constant weight
    //item on/off only
    //party/mon step on only
    //party/mon step off only
    static final int NORMAL = 0;       //creatures & items, regardless what is on square
    static final int CONSTANT = 1;     //creatures & items, unless something already on square
    static final int CREATURE = 2;     //creatures only, regardless what is on square
    static final int ITEMONLY = 3;     //items only, regardless what is on square
    static final int STEPON = 4;       //creatures - stepping on only, regardless what is on square
    static final int STEPOFF = 5;      //creatures - stepping off only, regardless what is on square
    static final int MON = 6;          //monsters only, regardless what is on square
    static final int MONSTEPON = 7;    //monsters - stepping on only, regardless what is on square
    static final int MONSTEPOFF = 8;   //monsters - stepping off only, regardless what is on square
    static final int PARTY = 9;        //party only, regardless what is on square
    static final int PARTYSTEPON = 10; //party - stepping on only, regardless what is on square
    static final int PARTYSTEPOFF = 11;//party - stepping off only, regardless what is on square
    static final int CONSTANTON = 12;  //stepping on only, unless item is on square
    static final int CONSTANTOFF = 13; //stepping off only, unless item is on square
    
    //?itemconst?//items only, regardless what is on square
        
        /*
        //construct a floor switch
        // xyp - mappoint containing level and xy coords of this switch
        // typ - see above ints
        // lvl,x,y - level and xy coords of target mapsquare
        // reu - is the switch reusable?
        // dly - delay time before switch works, 0 for none (note this acts a bit strange with constant weight switches -> if weight removed before delay time up, not switched again)
        // rset - time before switch resets, 0 for no reset
        // chngto - mapobject to change target into
        // pic - does switch have a pic, or is it invisible?
        // snd - does switch make a sound when activated?
        public FloorSwitch(MapPoint xyp,int typ,int lvl,int x,int y,boolean reu,int dly,int rset,MapObject chngto,boolean pic,boolean snd) {
                super();
                mapchar = 's';
                xy = xyp;
                type = typ;
                targetlevel = lvl;
                targetx = x;
                targety = y;
                isReusable = reu;
                delay = dly;
                reset = rset;
                changeto = chngto;
                haspic = pic;
                playsound = snd;
                if (haspic) setPics();
        }
        //constructor for switch that opens a door or triggers a proj launcher
        // xyp - point containing xy coords of this switch
        // typ - see above ints
        // x,y - x and y coords of target mapsquare
        // reu - is the switch reusable?
        // dly - delay time before switch works, 0 for none
        // rset - time before switch resets, 0 for no reset
        // pic - does switch have a pic, or is it invisible?
        // snd - does switch make a sound when activated?
        public FloorSwitch(MapPoint xyp,int typ,int lvl,int x,int y,boolean reu,int dly,int rset,boolean pic,boolean snd) {
                super();
                mapchar = 's';
                xy = xyp;
                type = typ;
                targetlevel = lvl;
                targetx = x;
                targety = y;
                isReusable = reu;
                delay = dly;
                reset = rset;
                activates = true;
                haspic = pic;
                playsound = snd;
                if (haspic) setPics();
        }
        */
    
    //constructor for use by load routine
    public FloorSwitch() {
        super();
        mapchar = 's';
    }
        
        /*
        //constructor for use by multfloorswitch
        protected FloorSwitch(MapPoint xyp, int typ, boolean reu, int dly, int rset, boolean pic, boolean snd) { 
                super();
                mapchar = 's';
                xy = xyp;
                type = typ;
                isReusable = reu;
                delay = dly;
                reset = rset;
                haspic = pic;
                playsound = snd;
                if (!ADDEDPICS && haspic) staticSetPics();
        }
        */
    
    public static void staticSetPics() {
        fswitchpic[1][1] = dmnew.tk.getImage(mapdir + "fswitch11.gif");
        fswitchpic[1][2] = dmnew.tk.getImage(mapdir + "fswitch12.gif");
        fswitchpic[1][3] = dmnew.tk.getImage(mapdir + "fswitch13.gif");
        fswitchpic[2][1] = dmnew.tk.getImage(mapdir + "fswitch21.gif");
        fswitchpic[2][2] = dmnew.tk.getImage(mapdir + "fswitch22.gif");
        fswitchpic[2][3] = dmnew.tk.getImage(mapdir + "fswitch23.gif");
        fswitchpic[3][1] = dmnew.tk.getImage(mapdir + "fswitch31.gif");
        fswitchpic[3][2] = dmnew.tk.getImage(mapdir + "fswitch32.gif");
        fswitchpic[3][3] = dmnew.tk.getImage(mapdir + "fswitch33.gif");
        tracker.addImage(fswitchpic[1][1], 0);
        tracker.addImage(fswitchpic[1][2], 0);
        tracker.addImage(fswitchpic[1][3], 0);
        tracker.addImage(fswitchpic[2][1], 0);
        tracker.addImage(fswitchpic[2][2], 0);
        tracker.addImage(fswitchpic[2][3], 0);
        tracker.addImage(fswitchpic[3][1], 0);
        tracker.addImage(fswitchpic[3][2], 0);
        tracker.addImage(fswitchpic[3][3], 0);
        ADDEDPICS = true;
    }
    
    public static void redoPics() {
        String newmapdir = Wall.currentdir + File.separator;
        File testfile = new File(newmapdir + "fswitch12.gif");
        if (!testfile.exists()) return;
        
        fswitchpic[1][1] = dmnew.tk.getImage(newmapdir + "fswitch11.gif");
        fswitchpic[1][2] = dmnew.tk.getImage(newmapdir + "fswitch12.gif");
        fswitchpic[1][3] = dmnew.tk.getImage(newmapdir + "fswitch13.gif");
        fswitchpic[2][1] = dmnew.tk.getImage(newmapdir + "fswitch21.gif");
        fswitchpic[2][2] = dmnew.tk.getImage(newmapdir + "fswitch22.gif");
        fswitchpic[2][3] = dmnew.tk.getImage(newmapdir + "fswitch23.gif");
        fswitchpic[3][1] = dmnew.tk.getImage(newmapdir + "fswitch31.gif");
        fswitchpic[3][2] = dmnew.tk.getImage(newmapdir + "fswitch32.gif");
        fswitchpic[3][3] = dmnew.tk.getImage(newmapdir + "fswitch33.gif");
        
        tracker.addImage(fswitchpic[1][1], 5);
        tracker.addImage(fswitchpic[1][2], 5);
        tracker.addImage(fswitchpic[1][3], 5);
        tracker.addImage(fswitchpic[2][1], 5);
        tracker.addImage(fswitchpic[2][2], 5);
        tracker.addImage(fswitchpic[2][3], 5);
        tracker.addImage(fswitchpic[3][1], 5);
        tracker.addImage(fswitchpic[3][2], 5);
        tracker.addImage(fswitchpic[3][3], 5);
        
        try {
            tracker.waitForID(5, 2000);
        } catch (InterruptedException ex) {
        }
        
        tracker.removeImage(fswitchpic[1][1], 5);
        tracker.removeImage(fswitchpic[1][2], 5);
        tracker.removeImage(fswitchpic[1][3], 5);
        tracker.removeImage(fswitchpic[2][1], 5);
        tracker.removeImage(fswitchpic[2][2], 5);
        tracker.removeImage(fswitchpic[2][3], 5);
        tracker.removeImage(fswitchpic[3][1], 5);
        tracker.removeImage(fswitchpic[3][2], 5);
        tracker.removeImage(fswitchpic[3][3], 5);
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (col == 0 || col == 4) return;
        else if (haspic && row != 0) {
            if (col == 3) xc -= fswitchpic[row][col].getWidth(null);
            if (col == 2 && dmnew.mirrorback)
                g.drawImage(fswitchpic[row][col], xc + fswitchpic[row][col].getWidth(null), yc, xc, yc + fswitchpic[row][col].getHeight(null), 0, 0, fswitchpic[row][col].getWidth(null), fswitchpic[row][col].getHeight(null), obs);
            else g.drawImage(fswitchpic[row][col], xc, yc, obs);
        }
        drawContents(g, 3 - row, col - 1);
    }
    
    //party or mon steps on or off switch, item set on or taken off switch
    //order of setting hasmons,hasitems,hasparty in dmnew is crucial
    // -- when going off, set false then test
    // -- when going on,  test then set true   -- test is same for both
    public void tryFloorSwitch(int onoff) {
        if (!isReusable && wasUsed) return;
        if (switchface > 0 && (onoff == PARTYSTEPPINGON || onoff == PARTYSTEPPINGOFF) && dmnew.facing != switchface - 1) {
            //if (partyflag) { hasParty = true; partyflag = false; }
            //if (monflag) { hasMons = true; monflag = false; }
            return;
        }
        if (type == CONSTANT || type == CONSTANTON || type == CONSTANTOFF) {
            if ((hasItems && (onoff != PUTITEM || mapItems.size() > 1)) || (hasParty && onoff != PARTYSTEPPINGON && onoff != PARTYSTEPPINGOFF && (switchface == 0 || switchface - 1 == dmnew.facing))) {
                //if (partyflag) { hasParty = true; partyflag = false; }
                //if (monflag) { hasMons = true; monflag = false; }
                return;
            } else if (hasMons) { //flying and dying mons don't affect switches
                dmnew.Monster tempmon;
                int found = 0, numtofind = (onoff == MONSTEPPINGON) ? 2 : 1;
                int i = 0;
                while (i < 6 && found < numtofind) {
                    tempmon = (dmnew.Monster) dmnew.dmmons.get(xy.level + "," + xy.x + "," + xy.y + "," + i);
                    if (tempmon != null && !tempmon.isflying && !tempmon.isdying) found++;
                    if (i == 3) i = 5;
                    else i++;
                }
                //System.out.println("numtofind = "+numtofind+", found = "+found);
                if (found >= numtofind) {
                    //if (partyflag) { hasParty = true; partyflag = false; }
                    //if (monflag) { hasMons = true; monflag = false; }
                    return;
                }
            }
            //if (partyflag) { hasParty = true; partyflag = false; }
            //if (monflag) { hasMons = true; monflag = false; }
            if (type == CONSTANTON && onoff != PARTYSTEPPINGON && onoff != MONSTEPPINGON && onoff != PUTITEM) return;
            //else if (type==CONSTANT && delaying) { delaying = false; resetting = false; return; }
            if (delaying) {
                delaying = false;
                changecount = 0;
                return;
            }//turn off delay/reset for constant switches
            else if (resetting) {
                changecount = 0;
                return;
            }
        }
        //if (partyflag) { hasParty = true; partyflag = false; }
        //if (monflag) { hasMons = true; monflag = false; }
        
        if (type == CREATURE && (onoff == PUTITEM || onoff == TOOKITEM)) return;
        else if (type == ITEMONLY && onoff != PUTITEM && onoff != TOOKITEM) return;
        else if (type == STEPON && onoff != PARTYSTEPPINGON && onoff != MONSTEPPINGON) return;
        else if (type == STEPOFF && (onoff == PUTITEM || onoff == TOOKITEM)) return;
        else if (type == MON && onoff != MONSTEPPINGON && onoff != MONSTEPPINGOFF) return;
        else if (type == MONSTEPON && onoff != MONSTEPPINGON) return;
        else if (type == MONSTEPOFF && onoff != MONSTEPPINGOFF) return;
        else if (type == PARTY && onoff != PARTYSTEPPINGON && onoff != PARTYSTEPPINGOFF) return;
        else if (type == PARTYSTEPON && onoff != PARTYSTEPPINGON) return;
        else if (type == PARTYSTEPOFF && onoff != PARTYSTEPPINGOFF) return;
                /*
                else if (type==CONSTANTON && (hasItems || hasParty || (onoff!=PARTYSTEPPINGON && onoff!=MONSTEPPINGON && onoff!=PUTITEM))) return;
                else if (type==CONSTANTOFF && (hasItems || hasParty)) return;
                else if (type==CONSTANTON || type==CONSTANTOFF && hasMons) {
                        //check for mons that aren't flying or immaterial
                        dmnew.Monster tempmon;
                        boolean found = false;
                        int i=0;
                        while (i<6 && !found) {
                                tempmon = (dmnew.Monster)dmnew.dmmons.get(xy.level+","+xy.x+","+xy.y+","+i);
                                if (tempmon!=null && !tempmon.isflying && !tempmon.isImmaterial && !tempmon.isdying) found=true;
                                else if (i==3) i=5;
                                else i++;
                        }
                        if (found) return;
                }
                */
        //hasn't failed, so play sound if should
        if (playsound && xy.level == dmnew.level) dmnew.playSound("switch.wav", xy.x, xy.y);
        //if (partyflag) hasParty = true;
        //if (monflag) hasMons = true;
        //dmmap.update(xy.level,xy.x,xy.y,'s');
        if ((type == STEPOFF || type == CONSTANTOFF) && (onoff == PARTYSTEPPINGON || onoff == MONSTEPPINGON || onoff == PUTITEM))
            return; //put here so will click when step on
        
        //System.out.println("got here -> delaying = "+delaying+", resetting = "+resetting+", actiontype = "+actiontype);
        //are these 2 correct? or should it depend more on type (party only is like constant, etc.)
        if (delaying && actiontype > 2 && actiontype != 6 && actiontype != 7) {
            delaying = false;
            resetting = false;
            changecount = 0;
            return;
        }//turn off delay and do nothing else for these types of switches???
        else if (resetting && actiontype > 2 && actiontype != 6 && actiontype != 7) {
            changecount = 0;
            return;
        }//reset counter set back to 0 for these types???
        //is this next one correct?
        if (delaying || resetting) return;
        
        //now do switch function
        if (delay > 0) delaying = true;
        else if (actiontype == 0) {
            dmnew.DungeonMap[targetlevel][targetx][targety].toggle();
            switchstate = !switchstate;
        } else if (actiontype == 1) {
            dmnew.DungeonMap[targetlevel][targetx][targety].activate();
            switchstate = !switchstate;
        } else if (actiontype == 2) {
            dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
            switchstate = !switchstate;
        } else if (actiontype == 3) {
            if (!switchstate) dmnew.DungeonMap[targetlevel][targetx][targety].activate();
            else dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
            switchstate = !switchstate;
        } else if (actiontype == 4) {
            if (!switchstate) dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
            else dmnew.DungeonMap[targetlevel][targetx][targety].activate();
            switchstate = !switchstate;
        } else if (actiontype == 5) {
            if (!switchstate && dmnew.DungeonMap[targetlevel][targetx][targety] != changeto) {
                if (!changeto.isPassable || changeto.mapchar == '>') {
                    if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) delaying = true;
                    else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                        int i = 0;
                        dmnew.Monster tempmon;
                        while (i < 6 && !delaying) {
                            tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                            if (tempmon != null && !tempmon.isImmaterial) delaying = true;
                            else if (i == 3) i = 5;
                            else i++;
                        }
                    }
                }
                if (!delaying && !changeto.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                    delaying = true;
                if (!delaying) {
                    oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                    dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                    switchstate = !switchstate;
                    if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                    if (oldMapObject.hasCloud) {
                        oldMapObject.hasCloud = false;
                        changeto.hasCloud = true;
                    }
                    if (oldMapObject.numProjs > 0) {
                        changeto.numProjs = oldMapObject.numProjs;
                        oldMapObject.numProjs = 0;
                    }
                    if (oldMapObject.hasMons) {
                        changeto.hasMons = true;
                        oldMapObject.hasMons = false;
                    }
                    if (oldMapObject.hasParty) {
                        changeto.hasParty = true;
                        oldMapObject.hasParty = false;
                    }
                    if (retainitems && oldMapObject.hasItems) {
                        while (oldMapObject.mapItems.size() > 0) {
                            changeto.addItem((Item) oldMapObject.mapItems.remove(0));
                        }
                        changeto.hasItems = true;
                        oldMapObject.hasItems = false;
                    }
                    changeto.doAction();
                }
                dmnew.needredraw = true;
            } else if (switchstate && dmnew.DungeonMap[targetlevel][targetx][targety] != oldMapObject) {
                if (!oldMapObject.isPassable || oldMapObject.mapchar == '>') {
                    if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) delaying = true;
                    else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                        int i = 0;
                        dmnew.Monster tempmon;
                        while (i < 6 && !delaying) {
                            tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                            if (tempmon != null && !tempmon.isImmaterial) delaying = true;
                            else if (i == 3) i = 5;
                            else i++;
                        }
                    }
                }
                if (!delaying && !oldMapObject.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                    delaying = true;
                if (!delaying) {
                    dmnew.DungeonMap[targetlevel][targetx][targety] = oldMapObject;
                    switchstate = !switchstate;
                    if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                    if (changeto.hasCloud) {
                        changeto.hasCloud = false;
                        oldMapObject.hasCloud = true;
                    }
                    if (changeto.numProjs > 0) {
                        oldMapObject.numProjs = changeto.numProjs;
                        changeto.numProjs = 0;
                    }
                    if (changeto.hasMons) {
                        oldMapObject.hasMons = true;
                        changeto.hasMons = false;
                    }
                    if (changeto.hasParty) {
                        oldMapObject.hasParty = true;
                        changeto.hasParty = false;
                    }
                    if (retainitems && changeto.hasItems) {
                        while (changeto.mapItems.size() > 0) {
                            oldMapObject.addItem((Item) changeto.mapItems.remove(0));
                        }
                        oldMapObject.hasItems = true;
                        changeto.hasItems = false;
                    }
                    oldMapObject.doAction();
                }
                dmnew.needredraw = true;
            }
        } else if (actiontype == 6) {
            if (dmnew.DungeonMap[targetlevel][targetx][targety] != changeto) {
                if (!changeto.isPassable || changeto.mapchar == '>') {
                    if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) delaying = true;
                    else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                        int i = 0;
                        dmnew.Monster tempmon;
                        while (i < 6 && !delaying) {
                            tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                            if (tempmon != null && !tempmon.isImmaterial) delaying = true;
                            else if (i == 3) i = 5;
                            else i++;
                        }
                    }
                }
                if (!delaying && !changeto.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                    delaying = true;
                if (!delaying) {
                    oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                    dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                    switchstate = !switchstate;
                    if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                    if (oldMapObject.hasCloud) {
                        oldMapObject.hasCloud = false;
                        changeto.hasCloud = true;
                    }
                    if (oldMapObject.numProjs > 0) {
                        changeto.numProjs = oldMapObject.numProjs;
                        oldMapObject.numProjs = 0;
                    }
                    if (oldMapObject.hasMons) {
                        changeto.hasMons = true;
                        oldMapObject.hasMons = false;
                    }
                    if (oldMapObject.hasParty) {
                        changeto.hasParty = true;
                        oldMapObject.hasParty = false;
                    }
                    if (retainitems && oldMapObject.hasItems) {
                        while (oldMapObject.mapItems.size() > 0) {
                            changeto.addItem((Item) oldMapObject.mapItems.remove(0));
                        }
                        changeto.hasItems = true;
                        oldMapObject.hasItems = false;
                    }
                    changeto.doAction();
                }
                dmnew.needredraw = true;
            }
        } else if (actiontype == 7) {
            dmnew.playSound(soundstring, targetx, targety, loopsound);
        } else if (actiontype == 8)
            dmnew.stopSounds(abrupt);//stop all looping sounds, if !abrupt let them play out first
        wasUsed = true;
        if (delaying || reset > 0) {
            if (reset > 0) resetting = true;
            if (!dmnew.mapstochange.contains(xy)) dmnew.mapstochange.add(xy);
            dmnew.mapchanging = true;
        }
    }
    
    public boolean changeState() {
        boolean tempbool = true;
        //System.out.println("got here -> delaying = "+delaying+", resetting = "+resetting);
        if (delaying) {
            changecount++;
            if (changecount > delay) {
                if (doSwitch()) {
                    delaying = false;
                    changecount = 0;
                    if (reset == 0) tempbool = false; //if doesn't reset remove from mapchanging
                    else resetting = true;
                } else changecount--;
            }
        } else if (resetting) {
            changecount++;
            if (changecount > reset) {
                if (resetnotrigger || doSwitch()) {
                    resetting = false;
                    changecount = 0;
                    tempbool = false; //remove from mapchanging
                } else changecount--;
            }
        } else {
            changecount = 0;
            tempbool = false;
        }
        return tempbool;
    }
    
    public void toggle() {
        if (delaying || resetting) return; //do nothing if switch state changing
        if (delay > 0 || !doSwitch()) {
            delaying = true;
            if (!dmnew.mapstochange.contains(xy)) dmnew.mapstochange.add(xy);
            dmnew.mapchanging = true;
            return;
        } else if (reset > 0) {
            resetting = true;
            if (!dmnew.mapstochange.contains(xy)) dmnew.mapstochange.add(xy);
            dmnew.mapchanging = true;
        }
    }
    
    public void activate() {
        toggle();
    }
    
    public void doAction() {
        if (hasParty) {
            //hasParty = false;
            //partyflag = true;//tells it to reset hasparty in tryFloorSwitch method -> fix in case floorswitch changes itself into something else
            tryFloorSwitch(PARTYSTEPPINGON);
        }
        if (hasMons) {
            //hasMons = false;
            //monflag = true;//tells it to reset hasmons in tryFloorSwitch method -> fix in case floorswitch changes itself into something else
            tryFloorSwitch(MONSTEPPINGON);
        }
    }
    
    protected boolean doSwitch() {
        if (actiontype == 0) {
            dmnew.DungeonMap[targetlevel][targetx][targety].toggle();
            switchstate = !switchstate;
        } else if (actiontype == 1) {
            dmnew.DungeonMap[targetlevel][targetx][targety].activate();
            switchstate = !switchstate;
        } else if (actiontype == 2) {
            dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
            switchstate = !switchstate;
        } else if (actiontype == 3) {
            if (!switchstate) dmnew.DungeonMap[targetlevel][targetx][targety].activate();
            else dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
            switchstate = !switchstate;
        } else if (actiontype == 4) {
            if (!switchstate) dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
            else dmnew.DungeonMap[targetlevel][targetx][targety].activate();
            switchstate = !switchstate;
        } else if (actiontype == 5) {
            if (!switchstate && dmnew.DungeonMap[targetlevel][targetx][targety] != changeto) {
                if (!changeto.isPassable || changeto.mapchar == '>') {
                    if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) return false;
                    else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                        int i = 0;
                        dmnew.Monster tempmon;
                        while (i < 6) {
                            tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                            if (tempmon != null && !tempmon.isImmaterial) return false;
                            else if (i == 3) i = 5;
                            else i++;
                        }
                    }
                }
                if (!changeto.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                    return false;
                oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                switchstate = !switchstate;
                if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                if (oldMapObject.hasCloud) {
                    oldMapObject.hasCloud = false;
                    changeto.hasCloud = true;
                }
                if (oldMapObject.numProjs > 0) {
                    changeto.numProjs = oldMapObject.numProjs;
                    oldMapObject.numProjs = 0;
                }
                if (oldMapObject.hasMons) {
                    changeto.hasMons = true;
                    oldMapObject.hasMons = false;
                }
                if (oldMapObject.hasParty) {
                    changeto.hasParty = true;
                    oldMapObject.hasParty = false;
                }
                if (retainitems && oldMapObject.hasItems) {
                    while (oldMapObject.mapItems.size() > 0) {
                        changeto.addItem((Item) oldMapObject.mapItems.remove(0));
                    }
                    changeto.hasItems = true;
                    oldMapObject.hasItems = false;
                }
                changeto.doAction();
            } else if (switchstate && dmnew.DungeonMap[targetlevel][targetx][targety] != oldMapObject) {
                if (!oldMapObject.isPassable || oldMapObject.mapchar == '>') {
                    if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) return false;
                    else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                        int i = 0;
                        dmnew.Monster tempmon;
                        while (i < 6) {
                            tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                            if (tempmon != null && !tempmon.isImmaterial) return false;
                            else if (i == 3) i = 5;
                            else i++;
                        }
                    }
                }
                if (!oldMapObject.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                    return false;
                dmnew.DungeonMap[targetlevel][targetx][targety] = oldMapObject;
                switchstate = !switchstate;
                if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                if (changeto.hasCloud) {
                    changeto.hasCloud = false;
                    oldMapObject.hasCloud = true;
                }
                if (changeto.numProjs > 0) {
                    oldMapObject.numProjs = changeto.numProjs;
                    changeto.numProjs = 0;
                }
                if (changeto.hasMons) {
                    oldMapObject.hasMons = true;
                    changeto.hasMons = false;
                }
                if (changeto.hasParty) {
                    oldMapObject.hasParty = true;
                    changeto.hasParty = false;
                }
                if (retainitems && changeto.hasItems) {
                    while (changeto.mapItems.size() > 0) {
                        oldMapObject.addItem((Item) changeto.mapItems.remove(0));
                    }
                    oldMapObject.hasItems = true;
                    changeto.hasItems = false;
                }
                oldMapObject.doAction();
            }
        } else if (actiontype == 6) {
            if (dmnew.DungeonMap[targetlevel][targetx][targety] != changeto) {
                if (!changeto.isPassable || changeto.mapchar == '>') {
                    if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) return false;
                    else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                        int i = 0;
                        dmnew.Monster tempmon;
                        while (i < 6) {
                            tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                            if (tempmon != null && !tempmon.isImmaterial) return false;
                            else if (i == 3) i = 5;
                            else i++;
                        }
                    }
                }
                if (!changeto.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                    return false;
                oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                switchstate = !switchstate;
                if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                if (oldMapObject.hasCloud) {
                    oldMapObject.hasCloud = false;
                    changeto.hasCloud = true;
                }
                if (oldMapObject.numProjs > 0) {
                    changeto.numProjs = oldMapObject.numProjs;
                    oldMapObject.numProjs = 0;
                }
                if (oldMapObject.hasMons) {
                    changeto.hasMons = true;
                    oldMapObject.hasMons = false;
                }
                if (oldMapObject.hasParty) {
                    changeto.hasParty = true;
                    oldMapObject.hasParty = false;
                }
                if (retainitems && oldMapObject.hasItems) {
                    while (oldMapObject.mapItems.size() > 0) {
                        changeto.addItem((Item) oldMapObject.mapItems.remove(0));
                    }
                    changeto.hasItems = true;
                    oldMapObject.hasItems = false;
                }
                changeto.doAction();
            }
        } else if (actiontype == 7) {
            dmnew.playSound(soundstring, targetx, targety, loopsound);
        } else if (actiontype == 8)
            dmnew.stopSounds(abrupt);//stop all looping sounds, if !abrupt let them play out first
        dmnew.needredraw = true;
        return true;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(xy);
        so.writeInt(type);
        so.writeInt(targetlevel);
        so.writeInt(targetx);
        so.writeInt(targety);
        so.writeBoolean(isReusable);
        so.writeInt(delay);
        so.writeInt(reset);
        so.writeBoolean(haspic);
        so.writeBoolean(playsound);
        so.writeInt(changecount);
        so.writeBoolean(delaying);
        so.writeBoolean(resetting);
        so.writeBoolean(resetnotrigger);
        so.writeBoolean(switchstate);
        so.writeInt(switchface);
        so.writeBoolean(wasUsed);
        so.writeInt(actiontype);
        //if (actiontype>=5 && actiontype<7 && (isReusable || !wasUsed)) {
        if (actiontype == 5 || actiontype == 6) {
            changeto.save(so);
            if (switchstate && actiontype == 5) oldMapObject.save(so);
            so.writeBoolean(retainitems);
        } else if (actiontype == 7) {
            so.writeUTF(soundstring);
            so.writeInt(loopsound);
        } else if (actiontype == 8) so.writeBoolean(abrupt);
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        xy = (MapPoint) si.readObject();
        type = si.readInt();
        targetlevel = si.readInt();
        targetx = si.readInt();
        targety = si.readInt();
        target = new MapPoint(targetlevel, targetx, targety);
        isReusable = si.readBoolean();
        delay = si.readInt();
        reset = si.readInt();
        haspic = si.readBoolean();
        playsound = si.readBoolean();
        changecount = si.readInt();
        delaying = si.readBoolean();
        resetting = si.readBoolean();
        resetnotrigger = si.readBoolean();
        switchstate = si.readBoolean();
        switchface = si.readInt();
        wasUsed = si.readBoolean();
        actiontype = si.readInt();
        //if (actiontype>=5 && actiontype<7 && (isReusable || !wasUsed)) {
        if (actiontype == 5 || actiontype == 6) {
            changeto = dmnew.loadMapObject(si);
            if (switchstate && actiontype == 5) oldMapObject = dmnew.loadMapObject(si);
            retainitems = si.readBoolean();
        } else if (actiontype == 7) {
            soundstring = si.readUTF();
            loopsound = si.readInt();
        } else if (actiontype == 8) abrupt = si.readBoolean();
        if (!ADDEDPICS && haspic) staticSetPics();
    }
}
