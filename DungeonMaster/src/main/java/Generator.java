//note: currently will not generate even if only one mon is standing on
//very complex to deal with subsquares and numtogen otherwise

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class Generator extends Floor {
    static public boolean ADDEDPICS = false;
    public MonsterData monster;
    public int genrate, gencounter, gencounter2;
    private int numtogen;
    private int maxgen;
    private int level, xcoord, ycoord;
    private int count, maxcount;
    private boolean resetcount;
    private static dmnew dmapp;
    public boolean isContinuous = false, isActive = true, delaying = false;
    
    //lvl,x,y - level and xy coords of this generator
    //dm - the dmnew app (needed to create new mons)
    //genr - generation rate (how fast a continuous generator spits them out, 0 if not continuos)
    //rest monster stuff
    public Generator(int lvl, int x, int y, int numgen, int mgen, dmnew dm, int genr, boolean isac, int genc, int genc2, int mcnt, int cnt, boolean rcnt, MonsterData mon) {
        super();
        mapchar = 'g';
        isPassable = true;
        canPassProjs = true;
        canHoldItems = true;
        drawItems = true;
        drawFurtherItems = true;
        level = lvl;
        xcoord = x;
        ycoord = y;
        numtogen = numgen;
        maxgen = mgen;
        dmapp = dm;
        genrate = genr;
        if (genrate > 0) isContinuous = true;
        isActive = isac;
        gencounter = genc;
        gencounter2 = genc2;
        monster = mon;
        maxcount = mcnt;
        count = cnt;
        resetcount = rcnt;
        if (monster.number == 10) setPics();
    }
    
    public boolean changeState() {
        if (delaying) {
            //only set by non-continuous generators that were blocked
            if (hasMons || hasParty) return true;
            delaying = false;
            generate();
            return false;
        }
        if (!isActive) return true; //was return false
        gencounter++;
        if (gencounter > genrate) {
            if (!hasMons && !hasParty) {
                generate();
                gencounter = 0;
                gencounter2++;
                if (gencounter2 == maxgen) {
                    gencounter2 = 0;
                    isActive = !isActive;
                }
            } else gencounter--;
        }
        return true;
    }
    
    public void toggle() {
        if (isContinuous) isActive = !isActive;
        else if (!hasMons && !hasParty) generate();
        else {
            delaying = true;
            MapPoint xy = new MapPoint(level, xcoord, ycoord);
            if (!dmnew.mapstochange.contains(xy)) dmnew.mapstochange.add(xy);
            dmnew.mapchanging = true;
        }
    }
    
    public void activate() {
        if (!isContinuous || !isActive) {
            if (count > 0) {
                count--;
                return;
            }
            if (resetcount) count = maxcount;
            toggle();
        }
    }
    
    public void deactivate() {
        if (isContinuous && isActive) toggle();
        else if (count < maxcount) count++;
    }
    
    private void generate() {
        boolean[] sub = new boolean[4];
        dmnew.Monster tempmon;
        int num;
        if (numtogen == 5) num = dmnew.randGen.nextInt(4) + 1;
        else num = numtogen;
        for (int j = 0; j < num; j++) {
            if (monster.number > 27)
                tempmon = dmapp.new Monster(monster.number, xcoord, ycoord, level, monster.name, monster.picstring, monster.soundstring, monster.footstep, monster.canusestairs, monster.isflying, monster.ignoremons, monster.canteleport);
            else tempmon = dmapp.new Monster(monster.number, xcoord, ycoord, level);
            tempmon.maxhealth = monster.maxhealth;
            tempmon.health = monster.health;
            tempmon.maxmana = monster.maxmana;
            tempmon.mana = monster.mana;
            tempmon.power = monster.power;
            tempmon.defense = monster.defense;
            tempmon.magicresist = monster.magicresist;
            tempmon.speed = monster.speed;
            tempmon.poison = monster.poison;
            tempmon.fearresist = monster.fearresist;
            tempmon.movespeed = monster.movespeed;
            tempmon.attackspeed = monster.attackspeed;
            tempmon.facing = monster.facing;
            tempmon.subsquare = monster.subsquare;
            if (tempmon.subsquare != 5) {
                tempmon.subsquare = dmnew.randGen.nextInt(4);
                while (sub[tempmon.subsquare]) {
                    tempmon.subsquare = (tempmon.subsquare + 1) % 4;
                }
                sub[tempmon.subsquare] = true;
            }
            tempmon.currentai = monster.currentai;
            tempmon.defaultai = monster.defaultai;
            tempmon.HITANDRUN = monster.HITANDRUN;
            tempmon.isImmaterial = monster.isImmaterial;
            tempmon.hurtitem = monster.hurtitem;
            tempmon.needitem = monster.needitem;
            tempmon.needhandneck = monster.needhandneck;
            tempmon.gamewin = monster.gamewin;
            if (tempmon.gamewin) {
                tempmon.endanim = monster.endanim;
                tempmon.endsound = monster.endsound;
            }
            tempmon.timecounter = dmnew.randGen.nextInt(100);
            tempmon.movecounter = dmnew.randGen.nextInt(tempmon.movespeed + 1);
            tempmon.useammo = monster.useammo;
            tempmon.pickup = monster.pickup;
            tempmon.steal = monster.steal;
            tempmon.poisonimmune = monster.poisonimmune;
            //if (monster.ammonumber>0) { tempmon.ammonumber=monster.ammonumber; tempmon.ammo=monster.ammo; }
            tempmon.hasmagic = monster.hasmagic;
            if (tempmon.hasmagic) {
                tempmon.numspells = monster.numspells;
                if (tempmon.numspells > 0) tempmon.knownspells = monster.knownspells;
                else tempmon.hasmagic = false;
                tempmon.castpower = monster.castpower;
                tempmon.manapower = monster.manapower;
                tempmon.minproj = monster.minproj;
                tempmon.hasheal = monster.hasheal;
                tempmon.hasdrain = monster.hasdrain;
            }
            for (int i = 0; i < monster.carrying.size(); i++) {
                tempmon.carrying.add(Item.createCopy((Item) monster.carrying.get(i)));
            }
            if (monster.equipped != null) {
                tempmon.equipped = new ArrayList();
                for (int i = 0; i < monster.equipped.size(); i++) {
                    tempmon.equipped.add(Item.createCopy((Item) monster.equipped.get(i)));
                }
            }
            dmapp.dmmons.put(level + "," + xcoord + "," + ycoord + "," + tempmon.subsquare, tempmon);
            //if (tempmon.isImmaterial) hasImmaterialMons = true;
        }
        hasMons = true;
        if (level == dmapp.level) {
            int xdist = xcoord - dmapp.partyx;
            if (xdist < 0) xdist *= -1;
            int ydist = ycoord - dmapp.partyy;
            if (ydist < 0) ydist *= -1;
            if (xdist < 5 && ydist < 5) dmapp.needredraw = true;
        }
    }
    
    protected void setPics() {
        pic = new Image[4][5];
        pic[1][1] = loadPic("generator11.gif");
        pic[1][2] = loadPic("generator12.gif");
        pic[1][3] = loadPic("generator13.gif");
        pic[2][1] = loadPic("generator21.gif");
        pic[2][2] = loadPic("generator22.gif");
        pic[2][3] = loadPic("generator23.gif");
        pic[3][1] = loadPic("generator31.gif");
        pic[3][2] = loadPic("generator32.gif");
        pic[3][3] = loadPic("generator33.gif");
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
        if (row == 0 || col == 0 || col == 4 || monster.number != 10) {
            if (col > 0 && col < 4) drawContents(g, 3 - row, col - 1);
            return;
        }
        //draw fire-elemental generator - only 1 with pic
        if (row == 1) {
            if (col == 2) g.drawImage(pic[1][2], 164, 227, obs);
            else if (col == 1) g.drawImage(pic[1][1], 0, 227, obs);
            else g.drawImage(pic[1][3], xc - 28, 227, obs);
        } else if (row == 2) {
            if (col == 2) g.drawImage(pic[2][2], 186, 189, obs);
            else if (col == 1) g.drawImage(pic[2][1], 2, 189, obs);
            else g.drawImage(pic[2][3], 363, 189, obs);
        } else if (row == 3) {
            if (col == 2) g.drawImage(pic[3][2], 196, 167, obs);
            else if (col == 1) g.drawImage(pic[3][1], 59, 167, obs);
            else g.drawImage(pic[3][3], 330, 167, obs);
        }
        if (col > 0 && col < 4) drawContents(g, 3 - row, col - 1);
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(level);
        so.writeInt(xcoord);
        so.writeInt(ycoord);
        so.writeInt(numtogen);
        so.writeInt(maxgen);
        so.writeInt(genrate);
        so.writeBoolean(isActive);
        so.writeInt(gencounter);
        so.writeInt(gencounter2);
        so.writeInt(maxcount);
        so.writeInt(count);
        so.writeBoolean(resetcount);
        monster.save(so);
        so.writeBoolean(delaying);
    }
    
}
