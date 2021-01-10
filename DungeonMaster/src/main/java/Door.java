import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
//import java.util.ArrayList;
//import java.util.Iterator;

class Door extends MapObject {
    
    //static public boolean ADDEDWOOD,ADDEDGRATE,ADDEDMETAL,ADDEDFANCYWOOD,ADDEDFANCYMETAL,ADDEDBLACK,ADDEDWOODWINDOW,ADDEDBLACKWINDOW,ADDEDRED,ADDEDGLASS,ADDEDNORMAL,ADDEDBUTTON,ADDEDKEY;
    static public boolean[] ADDEDPIC = new boolean[12];
    static public boolean ADDEDNORMAL, ADDEDBUTTON, ADDEDKEY;
    static public Image[][] doorpic = new Image[13][3];
    static public Image[][] openpic = new Image[3][3];
    static public Image[][] brokenpic = new Image[6][3];
    static private HashMap custompics;// = new HashMap();
    
    public boolean isOpen;
    public boolean isBreakable;
    public boolean isBroken;
    public int breakpoints;
    private int picklock = 0;
    public int changecount = 4;
    private MapPoint xy;
    public int side;
    public boolean isclosing = true;
    public int opentype;
    public int pictype;
    private String custompic;
    private boolean transparent;
    private int keynumber; //-1 if not a key door
    public boolean consumeskey; //true if item gone when used (for key door)
    private int count, maxcount;
    private boolean resetcount;
    public int xc, yc;
    private int animcount = 0;//for opening and closing delay
    
    static final boolean CLOSED = false;
    static final boolean OPEN = true;
    static final int NORMAL = 0;
    static final int BUTTON = 1;
    static final int KEY = 2;
    static final int WOOD = 0;
    static final int GRATE = 1;
    static final int METAL = 2;
    static final int FANCYWOOD = 3;
    static final int FANCYMETAL = 4;
    static final int BLACK = 5;
    static final int WOODWINDOW = 6;
    static final int BLACKWINDOW = 7;
    static final int RED = 8;
    static final int GLASS = 9;
    static final int IRONCROSS = 10;
    static final int IRONFACE = 11;
    static final int CUSTOM = 12;
    
    //xyp - mappoint containing level and xy coord of this door
    //sd - side must face to see (0 for north/south, 1 for east/west)
    //pt - picture type -> wood,grate,metal,woodwindow,metalwindow
    //ot - open type -> normal(by something else),button,key
    //op - is it open? (true if starts open, else false)
    //brka - is it breakable
    //isbrk - is it broken
    //kn - keynumber (item# that opens it - only used for key open type)
    //cons - key destroyed when used? (would usually set to true for coin type)
    //public Door(MapPoint xyp,int sd,int pt,int ot,boolean op,boolean brka,boolean isbrk,int kn,boolean cons,int mcnt,int cnt,boolean rcnt) {
    public Door(MapPoint xyp, int sd, int pt, int ot, boolean op, boolean brka, boolean isbrk, int kn, boolean cons, int mcnt, int cnt, boolean rcnt, int pklk) {
        super();
        mapchar = 'd';
        xy = xyp;
        side = sd;
        pictype = pt;
        opentype = ot;
        keynumber = kn;
        consumeskey = cons;
        maxcount = mcnt;
        count = cnt;
        resetcount = rcnt;
        isOpen = op;
        isBreakable = brka;
        isBroken = isbrk;
        picklock = pklk;
        canHoldItems = true;
        drawItems = true;
        if (isOpen) {
            drawFurtherItems = true;
            isPassable = true;
            canPassProjs = true;
            canPassMons = true;
            changecount = 0;
            isclosing = false;
        }
        if (pictype == GRATE) {
            canPassProjs = true;
            transparent = true;
            drawFurtherItems = true;
        } else if (pictype == GLASS) {
            transparent = true;
            drawFurtherItems = true;
        }
        setPics();
    }
    
    protected void setPics() {
        if (!ADDEDPIC[GRATE] && pictype == GRATE) {
            doorpic[GRATE][0] = dmnew.tk.createImage(mapdir + "grate1.gif");
            doorpic[GRATE][1] = dmnew.tk.createImage(mapdir + "grate2.gif");
            doorpic[GRATE][2] = dmnew.tk.createImage(mapdir + "grate3.gif");
            brokenpic[GRATE][0] = dmnew.tk.createImage(mapdir + "grate-brk1.gif");
            brokenpic[GRATE][1] = dmnew.tk.createImage(mapdir + "grate-brk2.gif");
            brokenpic[GRATE][2] = dmnew.tk.createImage(mapdir + "grate-brk3.gif");
            tracker.addImage(doorpic[GRATE][0], 0);
            tracker.addImage(doorpic[GRATE][1], 0);
            tracker.addImage(doorpic[GRATE][2], 0);
            tracker.addImage(brokenpic[GRATE][0], 0);
            tracker.addImage(brokenpic[GRATE][1], 0);
            tracker.addImage(brokenpic[GRATE][2], 0);
            ADDEDPIC[GRATE] = true;
        } else if (!ADDEDPIC[WOOD] && pictype == WOOD) {
            doorpic[WOOD][0] = dmnew.tk.createImage(mapdir + "wood1.gif");
            doorpic[WOOD][1] = dmnew.tk.createImage(mapdir + "wood2.gif");
            doorpic[WOOD][2] = dmnew.tk.createImage(mapdir + "wood3.gif");
            brokenpic[WOOD][0] = dmnew.tk.createImage(mapdir + "wood-brk1.gif");
            brokenpic[WOOD][1] = dmnew.tk.createImage(mapdir + "wood-brk2.gif");
            brokenpic[WOOD][2] = dmnew.tk.createImage(mapdir + "wood-brk3.gif");
            tracker.addImage(doorpic[WOOD][0], 0);
            tracker.addImage(doorpic[WOOD][1], 0);
            tracker.addImage(doorpic[WOOD][2], 0);
            tracker.addImage(brokenpic[WOOD][0], 0);
            tracker.addImage(brokenpic[WOOD][1], 0);
            tracker.addImage(brokenpic[WOOD][2], 0);
            ADDEDPIC[WOOD] = true;
        } else if (!ADDEDPIC[METAL] && pictype == METAL) {
            doorpic[METAL][0] = dmnew.tk.createImage(mapdir + "metal1.gif");
            doorpic[METAL][1] = dmnew.tk.createImage(mapdir + "metal2.gif");
            doorpic[METAL][2] = dmnew.tk.createImage(mapdir + "metal3.gif");
            tracker.addImage(doorpic[METAL][0], 0);
            tracker.addImage(doorpic[METAL][1], 0);
            tracker.addImage(doorpic[METAL][2], 0);
            ADDEDPIC[METAL] = true;
        } else if (!ADDEDPIC[FANCYWOOD] && pictype == FANCYWOOD) {
            doorpic[FANCYWOOD][0] = dmnew.tk.createImage(mapdir + "fancywood1.gif");
            doorpic[FANCYWOOD][1] = dmnew.tk.createImage(mapdir + "fancywood2.gif");
            doorpic[FANCYWOOD][2] = dmnew.tk.createImage(mapdir + "fancywood3.gif");
            tracker.addImage(doorpic[FANCYWOOD][0], 0);
            tracker.addImage(doorpic[FANCYWOOD][1], 0);
            tracker.addImage(doorpic[FANCYWOOD][2], 0);
            ADDEDPIC[FANCYWOOD] = true;
        } else if (!ADDEDPIC[FANCYMETAL] && pictype == FANCYMETAL) {
            doorpic[FANCYMETAL][0] = dmnew.tk.createImage(mapdir + "fancymetal1.gif");
            doorpic[FANCYMETAL][1] = dmnew.tk.createImage(mapdir + "fancymetal2.gif");
            doorpic[FANCYMETAL][2] = dmnew.tk.createImage(mapdir + "fancymetal3.gif");
            tracker.addImage(doorpic[FANCYMETAL][0], 0);
            tracker.addImage(doorpic[FANCYMETAL][1], 0);
            tracker.addImage(doorpic[FANCYMETAL][2], 0);
            ADDEDPIC[FANCYMETAL] = true;
        } else if (!ADDEDPIC[BLACK] && pictype == BLACK) {
            doorpic[BLACK][0] = dmnew.tk.createImage(mapdir + "blackdoor1.gif");
            doorpic[BLACK][1] = dmnew.tk.createImage(mapdir + "blackdoor2.gif");
            doorpic[BLACK][2] = dmnew.tk.createImage(mapdir + "blackdoor3.gif");
            brokenpic[BLACK][0] = dmnew.tk.createImage(mapdir + "blackdoor-brk1.gif");
            brokenpic[BLACK][1] = dmnew.tk.createImage(mapdir + "blackdoor-brk2.gif");
            brokenpic[BLACK][2] = dmnew.tk.createImage(mapdir + "blackdoor-brk3.gif");
            tracker.addImage(doorpic[BLACK][0], 0);
            tracker.addImage(doorpic[BLACK][1], 0);
            tracker.addImage(doorpic[BLACK][2], 0);
            tracker.addImage(brokenpic[BLACK][0], 0);
            tracker.addImage(brokenpic[BLACK][1], 0);
            tracker.addImage(brokenpic[BLACK][2], 0);
            ADDEDPIC[BLACK] = true;
        } else if (!ADDEDPIC[WOODWINDOW] && pictype == WOODWINDOW) {
            doorpic[WOODWINDOW][0] = dmnew.tk.createImage(mapdir + "wood-w1.gif");
            doorpic[WOODWINDOW][1] = dmnew.tk.createImage(mapdir + "wood-w2.gif");
            doorpic[WOODWINDOW][2] = dmnew.tk.createImage(mapdir + "wood-w3.gif");
            tracker.addImage(doorpic[WOODWINDOW][0], 0);
            tracker.addImage(doorpic[WOODWINDOW][1], 0);
            tracker.addImage(doorpic[WOODWINDOW][2], 0);
            ADDEDPIC[WOODWINDOW] = true;
        } else if (!ADDEDPIC[BLACKWINDOW] && pictype == BLACKWINDOW) {
            doorpic[BLACKWINDOW][0] = dmnew.tk.createImage(mapdir + "blackdoor-w1.gif");
            doorpic[BLACKWINDOW][1] = dmnew.tk.createImage(mapdir + "blackdoor-w2.gif");
            doorpic[BLACKWINDOW][2] = dmnew.tk.createImage(mapdir + "blackdoor-w3.gif");
            tracker.addImage(doorpic[BLACKWINDOW][0], 0);
            tracker.addImage(doorpic[BLACKWINDOW][1], 0);
            tracker.addImage(doorpic[BLACKWINDOW][2], 0);
            ADDEDPIC[BLACKWINDOW] = true;
        } else if (!ADDEDPIC[RED] && pictype == RED) {
            doorpic[RED][0] = dmnew.tk.createImage(mapdir + "reddoor1.gif");
            doorpic[RED][1] = dmnew.tk.createImage(mapdir + "reddoor2.gif");
            doorpic[RED][2] = dmnew.tk.createImage(mapdir + "reddoor3.gif");
            tracker.addImage(doorpic[RED][0], 0);
            tracker.addImage(doorpic[RED][1], 0);
            tracker.addImage(doorpic[RED][2], 0);
            ADDEDPIC[RED] = true;
        } else if (!ADDEDPIC[GLASS] && pictype == GLASS) {
            doorpic[GLASS][0] = dmnew.tk.createImage(mapdir + "glass1.png");
            doorpic[GLASS][1] = dmnew.tk.createImage(mapdir + "glass2.png");
            doorpic[GLASS][2] = dmnew.tk.createImage(mapdir + "glass3.png");
            tracker.addImage(doorpic[GLASS][0], 0);
            tracker.addImage(doorpic[GLASS][1], 0);
            tracker.addImage(doorpic[GLASS][2], 0);
            ADDEDPIC[GLASS] = true;
        } else if (!ADDEDPIC[IRONCROSS] && pictype == IRONCROSS) {
            doorpic[IRONCROSS][0] = dmnew.tk.createImage(mapdir + "ironcrossdoor1.gif");
            doorpic[IRONCROSS][1] = dmnew.tk.createImage(mapdir + "ironcrossdoor2.gif");
            doorpic[IRONCROSS][2] = dmnew.tk.createImage(mapdir + "ironcrossdoor3.gif");
            tracker.addImage(doorpic[IRONCROSS][0], 0);
            tracker.addImage(doorpic[IRONCROSS][1], 0);
            tracker.addImage(doorpic[IRONCROSS][2], 0);
            ADDEDPIC[IRONCROSS] = true;
        } else if (!ADDEDPIC[IRONFACE] && pictype == IRONFACE) {
            doorpic[IRONFACE][0] = dmnew.tk.createImage(mapdir + "ironfacedoor1.gif");
            doorpic[IRONFACE][1] = dmnew.tk.createImage(mapdir + "ironfacedoor2.gif");
            doorpic[IRONFACE][2] = dmnew.tk.createImage(mapdir + "ironfacedoor3.gif");
            tracker.addImage(doorpic[IRONFACE][0], 0);
            tracker.addImage(doorpic[IRONFACE][1], 0);
            tracker.addImage(doorpic[IRONFACE][2], 0);
            ADDEDPIC[IRONFACE] = true;
        }
        if (!ADDEDNORMAL && opentype == NORMAL) {
            openpic[NORMAL][0] = dmnew.tk.getImage(mapdir + "opennorm1.gif");
            openpic[NORMAL][1] = dmnew.tk.getImage(mapdir + "opennorm2.gif");
            openpic[NORMAL][2] = dmnew.tk.getImage(mapdir + "opennorm3.gif");
            tracker.addImage(openpic[NORMAL][0], 0);
            tracker.addImage(openpic[NORMAL][1], 0);
            tracker.addImage(openpic[NORMAL][2], 0);
            ADDEDNORMAL = true;
        } else if (!ADDEDBUTTON && opentype == BUTTON) {
            openpic[BUTTON][0] = dmnew.tk.getImage(mapdir + "openbut1.gif");
            openpic[BUTTON][1] = dmnew.tk.getImage(mapdir + "openbut2.gif");
            openpic[BUTTON][2] = dmnew.tk.getImage(mapdir + "openbut3.gif");
            tracker.addImage(openpic[BUTTON][0], 0);
            tracker.addImage(openpic[BUTTON][1], 0);
            tracker.addImage(openpic[BUTTON][2], 0);
            ADDEDBUTTON = true;
        } else if (!ADDEDKEY) {
            openpic[KEY][0] = dmnew.tk.getImage(mapdir + "openkey1.gif");
            openpic[KEY][1] = dmnew.tk.getImage(mapdir + "openkey2.gif");
            openpic[KEY][2] = dmnew.tk.getImage(mapdir + "openkey3.gif");
            tracker.addImage(openpic[KEY][0], 0);
            tracker.addImage(openpic[KEY][1], 0);
            tracker.addImage(openpic[KEY][2], 0);
            ADDEDKEY = true;
        }
    }
    
    public static void redoPics() {
        String newmapdir = Wall.currentdir + File.separator;
        boolean didreplace = false;
        File testfile = new File(newmapdir + "opennorm1.gif");
        if (testfile.exists()) {
            openpic[NORMAL][0] = dmnew.tk.getImage(newmapdir + "opennorm1.gif");
            openpic[NORMAL][1] = dmnew.tk.getImage(newmapdir + "opennorm2.gif");
            openpic[NORMAL][2] = dmnew.tk.getImage(newmapdir + "opennorm3.gif");
            tracker.addImage(openpic[NORMAL][0], 5);
            tracker.addImage(openpic[NORMAL][1], 5);
            tracker.addImage(openpic[NORMAL][2], 5);
            didreplace = true;
        }
        testfile = new File(newmapdir + "openbut1.gif");
        if (testfile.exists()) {
            openpic[BUTTON][0] = dmnew.tk.getImage(newmapdir + "openbut1.gif");
            openpic[BUTTON][1] = dmnew.tk.getImage(newmapdir + "openbut2.gif");
            openpic[BUTTON][2] = dmnew.tk.getImage(newmapdir + "openbut3.gif");
            tracker.addImage(openpic[BUTTON][0], 5);
            tracker.addImage(openpic[BUTTON][1], 5);
            tracker.addImage(openpic[BUTTON][2], 5);
            didreplace = true;
        }
        testfile = new File(newmapdir + "openkey1.gif");
        if (testfile.exists()) {
            openpic[KEY][0] = dmnew.tk.getImage(newmapdir + "openkey1.gif");
            openpic[KEY][1] = dmnew.tk.getImage(newmapdir + "openkey2.gif");
            openpic[KEY][2] = dmnew.tk.getImage(newmapdir + "openkey3.gif");
            tracker.addImage(openpic[KEY][0], 5);
            tracker.addImage(openpic[KEY][1], 5);
            tracker.addImage(openpic[KEY][2], 5);
            didreplace = true;
        }
        if (didreplace && !newmapdir.equals("Maps")) {
            try {
                tracker.waitForID(5, 2000);
            } catch (InterruptedException ex) {
            }
            tracker.removeImage(openpic[NORMAL][0], 5);
            tracker.removeImage(openpic[NORMAL][1], 5);
            tracker.removeImage(openpic[NORMAL][2], 5);
            tracker.removeImage(openpic[BUTTON][0], 5);
            tracker.removeImage(openpic[BUTTON][1], 5);
            tracker.removeImage(openpic[BUTTON][2], 5);
            tracker.removeImage(openpic[KEY][0], 5);
            tracker.removeImage(openpic[KEY][1], 5);
            tracker.removeImage(openpic[KEY][2], 5);
        }
    }
    
    //need change xy bounds when pic final
    public void tryWallSwitch(int x, int y) {
        if (opentype == BUTTON && x > 336 && x < 356 && y > 103 && y < 128 && !isBroken) {
            dmnew.playSound("switch.wav", -1, -1);
            toggle();
        }
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        if (opentype == NORMAL || isBroken) return true;
        else if (x > 320 && x < 356 && y > 93 && y < 138) {
            if (opentype == BUTTON) {
                dmnew.playSound("switch.wav", -1, -1);
                toggle();
                dmnew.doorkeyflag = true;
            }
            //trying to pick lock
            else if (picklock > 0 && it.number == 71) {
                dmnew.playSound("switch.wav", -1, -1);
                int chance = dmnew.hero[dmnew.leader].nlevel * dmnew.hero[dmnew.leader].dexterity / 10;
                if (chance < dmnew.randGen.nextInt(40) + picklock) {
                    dmnew.message.setMessage(dmnew.hero[dmnew.leader].name + " fails to pick the lock.", 4);
                    return true;
                }
                dmnew.hero[dmnew.leader].gainxp('n', 1);
                toggle();
                return true;
            }
                        /*
                        //can try use lock picks if takes iron key or just needs lock picks
                        else if ((keynumber==31 || keynumber==71) && dmnew.iteminhand && dmnew.inhand.number==71 && dmnew.inhand==it) {
                                dmnew.playSound("switch.wav",-1,-1);
                                int chance = dmnew.hero[dmnew.leader].nlevel*dmnew.hero[dmnew.leader].dexterity/10;
                                if (chance<dmnew.randGen.nextInt(40)+20) {
                                        dmnew.message.setMessage(dmnew.hero[dmnew.leader].name+" fails to pick the lock.",4);
                                        dmnew.doorkeyflag = true;
                                        return true;
                                }
                                toggle();
                                dmnew.doorkeyflag = true;
                        }
                        */
            else if (it.number == keynumber) { // && !wasUsed) {
                dmnew.playSound("switch.wav", -1, -1);
                toggle();
                if (!consumeskey) dmnew.doorkeyflag = true;
                return !consumeskey;
                //return false;
                //consumeskey stuff needed to be dealt with in dmnew to avoid throwing key after use
                //note: return true means keys reusable, false would destroy them
            } else dmnew.doorkeyflag = true;
        }
        return true;
    }
    
    public void breakDoor(int pow, boolean isweapon, boolean shouldpause) {
        //if (isOpen || isBroken || changecount!=4 || (isweapon && pictype>0) || (!isweapon && pictype==0)) return;
        //if (isOpen || isBroken || (isweapon && pictype>0) || (!isweapon && pictype==0)) return;
        if (isOpen || isBroken) return;
        if (isweapon && xy.level == dmnew.level) {
            if (shouldpause) try {
                Thread.currentThread().sleep(200);
            } catch (InterruptedException e) {
            }
            dmnew.playSound("thunk.wav", xy.x, xy.y);
        }
        if (!isweapon && pictype == 0) pow = 4 * pow / 5;
        if (!isBreakable || pow < breakpoints) return;
        isBroken = true;
        isOpen = true;
        changecount = 0;
        drawFurtherItems = true;
        isPassable = true;
        canPassProjs = true;
        dmnew.needredraw = true;
        if (isweapon && xy.level == dmnew.level) dmnew.playSound("doorbreak.wav", xy.x, xy.y);
    }
    
    public void toggle() {
        if (isBroken) return;
        isclosing = !isclosing;
        if (!dmnew.mapstochange.contains(xy)) dmnew.mapstochange.add(xy);
        dmnew.mapchanging = true;
    }
    
    public void activate() {
        if (isclosing) {
            if (count > 0) {
                count--;
                return;
            }
            if (resetcount) count = maxcount;
            toggle();
        }
    }
    
    public void deactivate() {
        if (!isclosing) toggle();
        else if (count < maxcount) count++;
    }
    
    public boolean changeState() {
        if (isBroken) return false;
        boolean tempbool = true;
        if (++animcount % 2 == 0) return true;
        if (isclosing) changecount++;
        else changecount--;
        if (changecount < 0) changecount = 0;
        else if (changecount > 4) changecount = 4;
        //changecount --> (0) open,(1) 1/4 closed,(2) half closed,(3) 3/4 closed,(4) closed
        if (xy.level == dmnew.level) dmnew.playSound("door.wav", xy.x, xy.y);
        
        //bash whatever in way unless immaterial
        if (isclosing && hasParty && changecount > 1) {
            dmnew.playSound("thunk.wav", -1, -1);
            //damage all heroes
            for (int i = 0; i < dmnew.numheroes; i++) {
                dmnew.hero[i].damage(dmnew.randGen.nextInt(10) + 5, dmnew.DOORHIT);
            }
            changecount -= 2; //was --
        } else if (isclosing && hasMons && changecount > 0) {
            //damage mons -- must take into consideration height of mon
            dmnew.Monster tempmon;
            boolean shouldthunk = false;
            for (int i = 0; i < 6; i++) {
                tempmon = (dmnew.Monster) dmnew.dmmons.get(xy.level + "," + xy.x + "," + xy.y + "," + i);
                if (tempmon != null && !tempmon.isdying && !tempmon.isImmaterial && ((changecount > 3) || (changecount > 2 && tempmon.towardspic.getHeight(null) > 100) || (changecount > 1 && tempmon.towardspic.getHeight(null) > 150))) { //was 50 100
                    tempmon.damage(dmnew.randGen.nextInt(10) + 5, dmnew.DOORHIT);
                    if (xy.level == dmnew.level) shouldthunk = true;
                    changecount -= 2; //was --
                }
                if (i == 3) i++;
            }
            if (shouldthunk) dmnew.playSound("thunk.wav", xy.x, xy.y);
        }
        if (isclosing && changecount > 1) {
            isOpen = false;
            isPassable = false;
            if (pictype != GRATE) {
                canPassProjs = false;
            }
            if (changecount == 4) {
                tempbool = false;
                if (!transparent) drawFurtherItems = false;
            }
        } else if (!isclosing && changecount == 3) drawFurtherItems = true;
        else if (!isclosing && changecount < 2) {
            isOpen = true;
            isPassable = true;
            canPassProjs = true;
            if (changecount == 0) tempbool = false;
        }
        if (xy.level == dmnew.level) {
            int xdist = xy.x - dmnew.partyx;
            if (xdist < 0) xdist *= -1;
            int ydist = xy.y - dmnew.partyy;
            if (ydist < 0) ydist *= -1;
            if (xdist < 5 && ydist < 5) dmnew.needredraw = true; //close, so repaint
        }
        return tempbool;
    }
    
    public void doAction() {
        if (changecount == 0 || changecount == 4 && dmnew.mapstochange.contains(xy)) dmnew.mapstochange.remove(xy);
    }
    
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        
        if (col == 0 || col == 4) return;
        if (row == 0 && col == 2 && dmnew.facing != side && (dmnew.facing + 2) % 4 != side) {
            if (dmnew.magicvision > 0) dmnew.dview.offg2.drawImage(dmnew.doortrack, xc + 204, 0, obs);
            else g.drawImage(dmnew.doortrack, xc + 204, 0, obs);
        } else if (row == 0) drawFurtherItems = true;
        this.xc = xc;
        this.yc = yc;
        drawContents(dmnew.dview.offg, 3 - row, col - 1);
        if (row == 0 && changecount == 4 && !transparent) drawFurtherItems = false;
    }
    
    public void drawDoor(Graphics2D g, int row, int col) {
        Graphics2D dg = g;
        if (row == 1 && col == 2 && dmnew.magicvision > 0 && pictype != 8)
            dg = dmnew.dview.offg2; //note: can't see thru red doors
        if (col == 2) g.drawImage(openpic[opentype][row - 1], xc, yc, null);
        else if (row == 1) {
            if (col == 1) g.drawImage(openpic[opentype][0], xc - 197, yc, null);//was -192
            else g.drawImage(openpic[opentype][0], xc - 124, yc, null);//was -128
        } else if (row == 2) {
            if (col == 1) g.drawImage(openpic[opentype][1], xc - 40, yc, null);
            else g.drawImage(openpic[opentype][1], xc - 168, yc, null);
        } else if (row == 3) {
            if (col == 1) g.drawImage(openpic[opentype][2], xc + 18, yc, null);
            else {
                xc = xc - openpic[opentype][2].getWidth(null) - 18;
                g.drawImage(openpic[opentype][2], xc, yc, null);
            }
        }
        
        if (changecount != 0 || isBroken) {
            if (pictype == CUSTOM) {
                doorpic[CUSTOM][0] = (Image) custompics.get(custompic + "1");
                doorpic[CUSTOM][1] = (Image) custompics.get(custompic + "2");
                doorpic[CUSTOM][2] = (Image) custompics.get(custompic + "3");
            }
            int h = doorpic[pictype][row - 1].getHeight(null);
            int w = doorpic[pictype][row - 1].getWidth(null);
            int rowadjustx = 0, rowadjusty = 0;
            if (row == 1) {
                rowadjusty = 18;
                if (col == 2) rowadjustx = 59;
                else if (col == 1) rowadjustx = -138;
                else rowadjustx = -64;
            } else if (row == 2) {
                rowadjusty = 9;
                if (col == 2) rowadjustx = 37;
                else if (col == 1) rowadjustx = -2;
                else rowadjustx = 3;
            } else if (row == 3) {
                rowadjusty = 6;
                if (col == 2) rowadjustx = 26;
                else if (col == 1) rowadjustx = 45;
                else rowadjustx = 25;
            }
            
            if (row != 3 && row != 1 && col == 3)
                xc = xc - doorpic[pictype][row - 1].getWidth(null) - 1; //corrects blankpic (???)
            
            if (changecount == 0) dg.drawImage(brokenpic[pictype][row - 1], xc + rowadjustx, yc + rowadjusty, null);
            else if (changecount == 4) {
                dg.drawImage(doorpic[pictype][row - 1], xc + rowadjustx, yc + rowadjusty, null);
            } else if (changecount == 1) {
                dg.setClip(xc + rowadjustx, yc + rowadjusty, w, h / 4);
                dg.drawImage(doorpic[pictype][row - 1], xc + rowadjustx, yc + rowadjusty - 3 * h / 4, null);
                dg.setClip(null);
            } else if (((Door) this).changecount == 2) {
                dg.setClip(xc + rowadjustx, yc + rowadjusty, w, h / 2);
                dg.drawImage(doorpic[pictype][row - 1], xc + rowadjustx, yc + rowadjusty - h / 2, null);
                dg.setClip(null);
            } else if (((Door) this).changecount == 3) {
                dg.setClip(xc + rowadjustx, yc + rowadjusty, w, 3 * h / 4);
                dg.drawImage(doorpic[pictype][row - 1], xc + rowadjustx, yc + rowadjusty - h / 4, null);
                dg.setClip(null);
            }
        }
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(xy);
        so.writeInt(side);
        so.writeInt(pictype);
        so.writeInt(opentype);
        so.writeBoolean(isOpen);
        so.writeBoolean(isBreakable);
        so.writeBoolean(isBroken);
        so.writeInt(keynumber);
        so.writeBoolean(consumeskey);
        so.writeInt(maxcount);
        so.writeInt(count);
        so.writeBoolean(resetcount);
        so.writeInt(picklock);
        so.writeInt(changecount);
        so.writeBoolean(isclosing);
        if (isBreakable && !isBroken) so.writeInt(breakpoints);
        if (pictype == CUSTOM) {
            so.writeUTF(custompic);
            so.writeBoolean(transparent);
        }
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        changecount = si.readInt();
        isclosing = si.readBoolean();
        if (isBreakable && !isBroken) breakpoints = si.readInt();
        if (pictype == CUSTOM) {
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
            transparent = si.readBoolean();
            if (transparent) drawFurtherItems = true;
        }
    }
}
