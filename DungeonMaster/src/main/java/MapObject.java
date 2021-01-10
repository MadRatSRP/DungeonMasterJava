//need draw ceiling pits, then items at 0,1, then mons at 0,1, then projs at 0,1
//then all at 2,3, then clouds

import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

class MapObject {
    
    protected Image[][] pic;
    protected static final Image blankpic = dmnew.tk.createImage("blank.gif");
    public static MediaTracker tracker;
    protected char mapchar;
    protected static String mapdir = "Maps" + java.io.File.separator;
    
    protected static Image temppic;
    protected static DarknessFilter darkfilt = new DarknessFilter();
    
    static final int PARTYSTEPPINGON = 0;
    static final int PARTYSTEPPINGOFF = 1;
    static final int PUTITEM = 2;
    static final int TOOKITEM = 3;
    static final int MONSTEPPINGON = 4;
    static final int MONSTEPPINGOFF = 5;
    
    public int numProjs = 0;
    
    public ArrayList mapItems;
    
    public boolean canHoldItems;            //true if can set items down on
    public boolean isPassable;              //true if can material walk through (doesn't affect immaterial)
    public boolean hasParty;                //true if party standing on this square
    public boolean canPassProjs;            //true if can fire projectiles through
    public boolean canPassMons = true;      //true if any mons can step on (if !ispassable, only affects immaterial)
    public boolean canPassImmaterial = true;//true if immaterial mon can step on
    public boolean hasMons;                 //true if has a mon standing on
    //public boolean hasImmaterialMons;       //true if has immaterial mon in
    public boolean hasCloud;                //true if has poison cloud in
    public boolean hasItems;                //true if has any items on
    public boolean drawItems;               //true if should draw any, false if embedded in wall or something
    public boolean drawFurtherItems;        //true if should draw 0,1 subsquares (true for floor, false for closed door, etc)
    
    public MapObject() {
        //if (tracker==null) tracker = dmnew.ImageTracker;
        //mapItems = new ArrayList(4);
        //pic = new Image[4][5];
        //if (blankpic==null) blankpic = dmnew.tk.createImage("blank.gif");
        //blankpic = dmnew.tk.getImage("blank.gif");//file doesn't actually exist
    }
    
    //public boolean canWalk() {
    //        if (isPassable && numProjs<1 && !hasMons) return true;
    //        else return false;
    //}
    
    public void tryWallSwitch(int x, int y) {
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        return true;
    } //if inhand is destroyed (key used), returns false
    
    public void tryFloorSwitch(int onoff) {
    } //can set for on, off, or either
    
    public void tryTeleport() {
    }
    
    public void tryTeleport(dmnew.Monster mon) {
    }
    
    public boolean tryTeleport(Item it) {
        return false;
    }
    
    public void tryTeleport(dmnew.Projectile p) {
    }
    
    public boolean changeState() {
        return false;
    }
    
    public void activate() {
    }
    
    public void deactivate() {
    }
    
    public void toggle() {
    }
    
    public void doAction() {
    }
    
    public void addItem(Item i) {
        if (mapItems == null) mapItems = new ArrayList(4);
        mapItems.add(i);
        hasItems = true;
    }
    
    public Item pickUpItem(int square) {
        Item inh = null;
        int index = mapItems.size() - 1;
        int findex = -1;
        int numfound = 0;
        boolean found = false;
        while (index > -1 && (numfound < 2 || !found)) {
            Item tempitem = (Item) mapItems.get(index);
            if (!found && tempitem.subsquare == square) {
                found = true;
                inh = tempitem;
                findex = index;
            }
            numfound++;
            index--;
        }
        if (found && numfound == 1) hasItems = false;
        if (findex != -1) mapItems.remove(findex);
        return inh;
    }
    
    //public void addSmoke(int subsquare, int maxhealth) {
    //add smoke to subsquare, initial size depends on maxhealth
    //...
    //}
    
    protected Image loadPic(String imgfile) {
        return dmnew.tk.getImage(mapdir + imgfile);
        //return dmnew.tk.createImage(mapdir+imgfile);
    }
    
    protected void setPics() {
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (col == 3) xc = xc - pic[row][3].getWidth(null);
        g.drawImage(pic[row][col], xc, yc, obs);
        if (col > 0 && col < 4) {
            drawContents(g, 3 - row, col - 1);
        }
    }
    
    public void drawContents(Graphics2D g, int dist, int i) {
        
        int drawdist = 0, sidedist = 0;
        int widthscale, heightscale, index, xoffsetscale, yoffsetscale;
        float scale, picwidth, picheight;
        
        int xadjust, yadjust;
        if (dmnew.facing == dmnew.NORTH) {
            xadjust = dmnew.partyx - 1 + i;
            yadjust = dmnew.partyy - 3 + dist;
        } else if (dmnew.facing == dmnew.SOUTH) {
            xadjust = dmnew.partyx + 1 - i;
            yadjust = dmnew.partyy + 3 - dist;
        } else if (dmnew.facing == dmnew.WEST) {
            xadjust = dmnew.partyx - 3 + dist;
            yadjust = dmnew.partyy + 1 - i;
        } else {
            xadjust = dmnew.partyx + 3 - dist;
            yadjust = dmnew.partyy - 1 + i;
        }
        
        //draw ceiling pits
        if (dmnew.level > 0 && dmnew.DungeonMap[dmnew.level - 1][xadjust][yadjust].mapchar == 'p' && ((Pit) dmnew.DungeonMap[dmnew.level - 1][xadjust][yadjust]).isOpen && (hasParty || dmnew.dispell > 0 || (dist == 2 && i == 1 && dmnew.magicvision > 0) || !(this instanceof FakeWall))) {
            if (dist == 0) {
                drawdist = 64;
                if (i == 0) sidedist = 9;
                else if (i == 1) sidedist = 161;
                else sidedist = 286;
            } else if (dist == 1) {
                drawdist = 49;
                if (i == 0) sidedist = 0;
                else if (i == 1) sidedist = 138;
                else sidedist = 308;
            } else if (dist == 2) {
                drawdist = 21;
                if (i == 0) sidedist = 0;
                else if (i == 1) sidedist = 87;
                else sidedist = 346;
            } else {
                drawdist = 0;
                if (i == 0) sidedist = 0;
                else if (i == 1) sidedist = 51;
                else sidedist = 419;
            }
            if (i == 1 && dmnew.mirrorback)
                g.drawImage(Pit.cpic[3 - dist][1], sidedist + Pit.cpic[3 - dist][1].getWidth(null), drawdist, sidedist, drawdist + Pit.cpic[3 - dist][1].getHeight(null), 0, 0, Pit.cpic[3 - dist][1].getWidth(null), Pit.cpic[3 - dist][1].getHeight(null), null);
            else g.drawImage(Pit.cpic[3 - dist][i], sidedist, drawdist, null);
        }
        
        
        Item tempitem;
        //Image temppic = null;
        dmnew.Monster tempmon;
        String picstring;//for mondarkpic caching
        int numitemtodraw = 0, numitemdrawn = 0, numprojdrawn = 0;
        if (hasItems) numitemtodraw = mapItems.size();
        int startsub = 0, endsub = 2;
        int subsquaretodraw = 0;
        int subface = 0;
        
        //go thru twice - have to draw subs 0,1 for everything before 2,3,5
        if (dist == 0) darkfilt.setDarks(3);
        else if (dist == 1) darkfilt.setDarks(2);
        else if (dist == 2) darkfilt.setDarks(1);
        for (int numtimes = 0; numtimes < 2; numtimes++) {
            
            //draw items
            subsquaretodraw = startsub;
            if (drawItems && numitemtodraw > 0 && numitemdrawn < numitemtodraw) {
                while (subsquaretodraw < endsub) {
                    int j = 0;
                    while (j < numitemtodraw && numitemdrawn < numitemtodraw) {
                        tempitem = (Item) mapItems.get(j);
                        if ((tempitem.subsquare + dmnew.facing) % 4 == subsquaretodraw) {
                            //draw it
                            numitemdrawn++;
                            widthscale = 0;
                            heightscale = 0;
                            scale = 0.0f;
                            picwidth = (float) tempitem.dpic.getWidth(null);
                            picheight = (float) tempitem.dpic.getHeight(null);
                            if (dist < 3 && (dist > 0 || ((tempitem.subsquare + dmnew.facing) % 4) > 1)) {
                                //temppic = tempitem.darkpic[dist];
                                //temppic = Item.darkpic[tempitem.number][dist];
                                temppic = (Image) Item.pics.get(tempitem.dpicstring + "-d" + dist);
                                if (temppic == null) {
                                    temppic = dmnew.tk.createImage(new FilteredImageSource(tempitem.dpic.getSource(), darkfilt));
                                    tracker.addImage(temppic, 0);
                                    try {
                                        tracker.waitForID(0, 5000);
                                    } catch (InterruptedException e) {
                                    }
                                    tracker.removeImage(temppic);
                                    //Item.darkpic[tempitem.number][dist]=temppic;
                                    Item.pics.put(tempitem.dpicstring + "-d" + dist, temppic);
                                }
                            } else temppic = tempitem.dpic;
                            switch ((tempitem.subsquare + dmnew.facing) % 4) {
                                case 0:
                                    if ((i != 0 || dist == 1) && dist > 0 && ((dmnew.magicvision > 0 && dist == 2) || drawFurtherItems)) {
                                        
                                        if (dist == 3) {
                                            drawdist = 120;
                                            if (i == 1) sidedist = -90;
                                            else sidedist = 260;//305-45;//was 260
                                        } else if (dist == 2) {
                                            drawdist = 42;//was 40
                                            if (i == 1) sidedist = -56;//was -56
                                            else sidedist = 165;//205-35;//was 170
                                        } else {
                                            drawdist = -2;//was -4
                                            if (i == 0) sidedist = -200;//-145-55;
                                            else if (i == 1) sidedist = -36;//was -36
                                            else sidedist = 120;//145-25;
                                        }
                                        
                                        scale = 2.8f - ((float) dist * .6f);
                                        widthscale = (int) (picwidth / scale);
                                        heightscale = (int) (picheight / scale);
                                        xoffsetscale = (int) ((float) tempitem.xoffset / scale);
                                        yoffsetscale = (int) ((float) tempitem.yoffset / scale);
                                        g.drawImage(temppic, 224 + sidedist - widthscale / 2 + xoffsetscale, 200 + drawdist - heightscale + yoffsetscale, widthscale, heightscale, null);
                                    }
                                    break;
                                case 1:
                                    if ((i != 2 || dist == 1) && dist > 0 && ((dmnew.magicvision > 0 && dist == 2) || drawFurtherItems)) {
                                        
                                        if (dist == 3) {
                                            drawdist = 120;
                                            if (i == 1) sidedist = 90;
                                            else sidedist = -260;//-305+45;//was -260
                                        } else if (dist == 2) {
                                            drawdist = 42;//was 40
                                            if (i == 1) sidedist = 56;//was 56
                                            else sidedist = -165;//-205+35;//was -170
                                        } else {
                                            drawdist = -2;//was -4
                                            if (i == 0) sidedist = -120;//-145+25;
                                            else if (i == 1) sidedist = 36;//was 36
                                            else sidedist = 200;//145+55
                                        }
                                        
                                        scale = 2.8f - ((float) dist * .6f);
                                        widthscale = (int) (picwidth / scale);
                                        heightscale = (int) (picheight / scale);
                                        xoffsetscale = (int) ((float) tempitem.xoffset / scale);
                                        yoffsetscale = (int) ((float) tempitem.yoffset / scale);
                                        g.drawImage(temppic, 224 + sidedist - widthscale / 2 + xoffsetscale, 200 + drawdist - heightscale + yoffsetscale, widthscale, heightscale, null);
                                    }
                                    break;
                                case 3:
                                    if ((i != 0 || dist < 2) && dist != 3) {
                                        
                                        if (dist == 2) {
                                            drawdist = 76;//48+6+20;//was 74
                                            if (i == 1) sidedist = -70;//was -70
                                            else sidedist = 206;//-205+25;//was 180
                                        } else if (dist == 1) {
                                            drawdist = 16;//-2+6+10;//was 14
                                            if (i == 0) sidedist = -222;//-145-77;
                                            else if (i == 1) sidedist = -46;//was -46
                                            else sidedist = 135;//145-15;//was 130
                                        } else if (dist == 0) {
                                            drawdist = -16;//-34+6;//was -18
                                            if (i == 0) sidedist = -176;//-125-35;//was -160
                                            else if (i == 1) sidedist = -28;//was -26
                                            else sidedist = 106;//125-5;//was 120
                                        }
                                        
                                        scale = 2.5f - ((float) dist * .6f);
                                        widthscale = (int) (picwidth / scale);
                                        heightscale = (int) (picheight / scale);
                                        xoffsetscale = (int) ((float) tempitem.xoffset / scale);
                                        yoffsetscale = (int) ((float) tempitem.yoffset / scale);
                                        g.drawImage(temppic, 224 + sidedist - widthscale / 2 + xoffsetscale, 200 + drawdist - heightscale + yoffsetscale, widthscale, heightscale, null);
                                    }
                                    break;
                                case 2:
                                    if ((i != 2 || dist < 2) && dist != 3) { //was i!=2 && dist!=3
                                        
                                        if (dist == 2) {
                                            drawdist = 76;//48+6+20;//was 74
                                            if (i == 1) sidedist = 70;//was 70
                                            else sidedist = -206;//-205+25;//was -180
                                        } else if (dist == 1) {
                                            drawdist = 16;//-2+6+10;//was 14
                                            if (i == 0) sidedist = -135;//-145+15;//was -130
                                            else if (i == 1) sidedist = 46;//was 46
                                            else sidedist = 222;//145+77;
                                        } else if (dist == 0) {
                                            drawdist = -16;//-34+6;//was -18
                                            if (i == 0) sidedist = -106;//-125+5;//was -120
                                            else if (i == 1) sidedist = 28;//was 26
                                            else sidedist = 176;//125+35;//was 160
                                        }
                                        
                                        scale = 2.5f - ((float) dist * .6f);
                                        widthscale = (int) (picwidth / scale);
                                        heightscale = (int) (picheight / scale);
                                        xoffsetscale = (int) ((float) tempitem.xoffset / scale);
                                        yoffsetscale = (int) ((float) tempitem.yoffset / scale);
                                        g.drawImage(temppic, 224 + sidedist - widthscale / 2 + xoffsetscale, 200 + drawdist - heightscale + yoffsetscale, widthscale, heightscale, null);
                                    }
                                    break;
                            }//end switch
                        }//end subsquare if
                        j++;
                    }//end while loop
                    subsquaretodraw++;
                }//end while loop
            }//end drawitems if
            
            if (endsub == 4) endsub = 6;
            
            //draw mons - do like items and projs now
            if (hasMons && dist != 3) {
                if (dist == 0) darkfilt.setDarks(2);
                else if (dist == 1) darkfilt.setDarks(1);
                while (subface < endsub) {
                    if (subface != 5) subsquaretodraw = (subface - dmnew.facing + 4) % 4;
                    else subsquaretodraw = 5;
                    tempmon = (dmnew.Monster) dmnew.dmmons.get(dmnew.level + "," + xadjust + "," + yadjust + "," + subsquaretodraw);
                    if (tempmon != null && (tempmon.isImmaterial || dmnew.dispell > 0 || (dist == 2 && i == 1 && dmnew.magicvision > 0) || !(this instanceof FakeWall))) {
                        if (!tempmon.isattacking && !tempmon.isdying && !tempmon.iscasting) {
                            if (dmnew.facing == tempmon.facing) {
                                if (dist < 2) {
                                    picstring = tempmon.picstring;
                                    if (tempmon.awaypic == tempmon.towardspic) picstring += "-toward";
                                    else picstring += "-away";
                                    picstring += dist;
                                    temppic = (Image) dmnew.mondarkpic.get(picstring);
                                    if (temppic == null) {
                                        temppic = dmnew.tk.createImage(new FilteredImageSource(tempmon.awaypic.getSource(), darkfilt));
                                        tracker.addImage(temppic, 0);
                                        try {
                                            tracker.waitForID(0, 5000);
                                        } catch (InterruptedException e) {
                                        }
                                        tracker.removeImage(temppic);
                                        dmnew.mondarkpic.put(picstring, temppic);
                                    }
                                } else temppic = tempmon.awaypic;
                            } else if ((dmnew.facing - tempmon.facing) % 2 == 0) {
                                if (dist < 2) {
                                    picstring = tempmon.picstring + "-toward" + dist;
                                    temppic = (Image) dmnew.mondarkpic.get(picstring);
                                    if (temppic == null) {
                                        temppic = dmnew.tk.createImage(new FilteredImageSource(tempmon.towardspic.getSource(), darkfilt));
                                        tracker.addImage(temppic, 0);
                                        try {
                                            tracker.waitForID(0, 5000);
                                        } catch (InterruptedException e) {
                                        }
                                        tracker.removeImage(temppic);
                                        dmnew.mondarkpic.put(picstring, temppic);
                                    }
                                } else temppic = tempmon.towardspic;
                            } else if (dmnew.facing == (tempmon.facing + 1) % 4) {
                                if (dist < 2) {
                                    picstring = tempmon.picstring;
                                    if (tempmon.rightpic == tempmon.towardspic) picstring += "-toward";
                                    else picstring += "-right";
                                    picstring += dist;
                                    temppic = (Image) dmnew.mondarkpic.get(picstring);
                                    if (temppic == null) {
                                        temppic = dmnew.tk.createImage(new FilteredImageSource(tempmon.rightpic.getSource(), darkfilt));
                                        tracker.addImage(temppic, 0);
                                        try {
                                            tracker.waitForID(0, 5000);
                                        } catch (InterruptedException e) {
                                        }
                                        tracker.removeImage(temppic);
                                        dmnew.mondarkpic.put(picstring, temppic);
                                    }
                                } else temppic = tempmon.rightpic;
                                if (!tempmon.rightpic.equals(tempmon.towardspic)) tempmon.mirrored = false;
                            } else {
                                if (dist < 2) {
                                    picstring = tempmon.picstring;
                                    if (tempmon.leftpic == tempmon.towardspic) picstring += "-toward";
                                    else picstring += "-left";
                                    picstring += dist;
                                    temppic = (Image) dmnew.mondarkpic.get(picstring);
                                    if (temppic == null) {
                                        temppic = dmnew.tk.createImage(new FilteredImageSource(tempmon.leftpic.getSource(), darkfilt));
                                        tracker.addImage(temppic, 0);
                                        try {
                                            tracker.waitForID(0, 5000);
                                        } catch (InterruptedException e) {
                                        }
                                        tracker.removeImage(temppic);
                                        dmnew.mondarkpic.put(picstring, temppic);
                                    }
                                } else temppic = tempmon.leftpic;
                                if (!tempmon.leftpic.equals(tempmon.towardspic)) tempmon.mirrored = false;
                            }
                        } else if (tempmon.isdying) temppic = dmnew.mondeathpic;
                        else if (tempmon.isattacking) {
                            if (dist < 2) {
                                picstring = tempmon.picstring;
                                if (tempmon.attackpic == tempmon.towardspic) picstring += "-toward";
                                else picstring += "-attack";
                                picstring += dist;
                                temppic = (Image) dmnew.mondarkpic.get(picstring);
                                if (temppic == null) {
                                    temppic = dmnew.tk.createImage(new FilteredImageSource(tempmon.attackpic.getSource(), darkfilt));
                                    tracker.addImage(temppic, 0);
                                    try {
                                        tracker.waitForID(0, 5000);
                                    } catch (InterruptedException e) {
                                    }
                                    tracker.removeImage(temppic);
                                    dmnew.mondarkpic.put(picstring, temppic);
                                }
                            } else temppic = tempmon.attackpic;
                        } else {
                            if (dist < 2) {
                                picstring = tempmon.picstring;
                                if (tempmon.castpic == tempmon.attackpic) picstring += "-attack";
                                else picstring += "-cast";
                                picstring += dist;
                                temppic = (Image) dmnew.mondarkpic.get(picstring);
                                if (temppic == null) {
                                    temppic = dmnew.tk.createImage(new FilteredImageSource(tempmon.castpic.getSource(), darkfilt));
                                    tracker.addImage(temppic, 0);
                                    try {
                                        tracker.waitForID(0, 5000);
                                    } catch (InterruptedException e) {
                                    }
                                    tracker.removeImage(temppic);
                                    dmnew.mondarkpic.put(picstring, temppic);
                                }
                            } else temppic = tempmon.castpic;
                        }
                        picwidth = (float) temppic.getWidth(null);
                        picheight = (float) temppic.getHeight(null);
                              /*
                              if (dist<2) {
                                temppic = dmnew.tk.createImage(new FilteredImageSource(temppic.getSource(),darkfilt));
                                tracker.addImage(temppic,0);
                                try { tracker.waitForID(0,5000); }
                                catch (InterruptedException e) {}
                                tracker.removeImage(temppic);
                              }
                              */
                        switch (subface) {
                            case 0:
                                if ((i != 0 || dist == 1) && dist > 0 && ((dmnew.magicvision > 0 && dist == 2) || drawFurtherItems)) {
                                    
                                    if (dist == 2) {
                                        drawdist = 42;//was 40
                                        if (i == 1) sidedist = -56;//was -56
                                        else sidedist = 165;//205-35;//was 170
                                    } else {
                                        drawdist = -2;//was -4
                                        if (i == 0) sidedist = -200;//-145-55;
                                        else if (i == 1) sidedist = -36;//was -36
                                        else sidedist = 120;//145-25;
                                    }
                                    
                                    //scale = 2.8f-((float)dist*.6f);
                                    scale = 2.5f - ((float) dist * .6f);
                                    widthscale = (int) (picwidth / scale);
                                    heightscale = (int) (picheight / scale);
                                    if (!dmnew.NOTRANS && ((tempmon.isImmaterial && tempmon.number != 10))) { //|| tempmon.isdying)) {
                                        if (tempmon.mirrored)
                                            dmnew.dview.offg2.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale, 224 + sidedist - widthscale / 2, 200 + drawdist, 0, 0, (int) picwidth, (int) picheight, null);
                                        else
                                            dmnew.dview.offg2.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale, widthscale, heightscale, null);
                                    } else if (tempmon.mirrored)
                                        g.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale, 224 + sidedist - widthscale / 2, 200 + drawdist, 0, 0, (int) picwidth, (int) picheight, null);
                                    else
                                        g.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale, widthscale, heightscale, null);
                                }
                                break;
                            case 1:
                                if ((i != 2 || dist == 1) && dist > 0 && ((dmnew.magicvision > 0 && dist == 2) || drawFurtherItems)) {
                                    
                                    if (dist == 2) {
                                        drawdist = 42;//was 40
                                        if (i == 1) sidedist = 56;//was 56
                                        else sidedist = -165;//-205+35;//was -170
                                    } else {
                                        drawdist = -2;//was -4
                                        if (i == 0) sidedist = -120;//-145+25;
                                        else if (i == 1) sidedist = 36;//was 36
                                        else sidedist = 200;//145+55
                                    }
                                    
                                    //scale = 2.8f-((float)dist*.6f);
                                    scale = 2.5f - ((float) dist * .6f);
                                    widthscale = (int) (picwidth / scale);
                                    heightscale = (int) (picheight / scale);
                                    if (!dmnew.NOTRANS && ((tempmon.isImmaterial && tempmon.number != 10))) { //|| tempmon.isdying)) {
                                        if (tempmon.mirrored)
                                            dmnew.dview.offg2.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale, 224 + sidedist - widthscale / 2, 200 + drawdist, 0, 0, (int) picwidth, (int) picheight, null);
                                        else
                                            dmnew.dview.offg2.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale, widthscale, heightscale, null);
                                    } else if (tempmon.mirrored)
                                        g.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale, 224 + sidedist - widthscale / 2, 200 + drawdist, 0, 0, (int) picwidth, (int) picheight, null);
                                    else
                                        g.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale, widthscale, heightscale, null);
                                }
                                break;
                            case 3:
                                if (i != 0 || dist < 2) {
                                    
                                    if (dist == 2) {
                                        drawdist = 80;//was 76
                                        if (i == 1) sidedist = -70;
                                        else sidedist = 206;
                                    } else if (dist == 1) {
                                        drawdist = 16;//was 16
                                        if (i == 0) sidedist = -222;
                                        else if (i == 1) sidedist = -46;
                                        else sidedist = 135;
                                    } else if (dist == 0) {
                                        drawdist = -16;//-16
                                        if (i == 0) sidedist = -176;
                                        else if (i == 1) sidedist = -28;
                                        else sidedist = 106;
                                    }
                                    
                                    //scale = 2.5f-((float)dist*.6f);
                                    scale = 2.2f - ((float) dist * .6f);
                                    widthscale = (int) (picwidth / scale);
                                    heightscale = (int) (picheight / scale);
                                    if (!dmnew.NOTRANS && ((tempmon.isImmaterial && tempmon.number != 10))) { //|| tempmon.isdying)) {
                                        if (tempmon.mirrored)
                                            dmnew.dview.offg2.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale, 224 + sidedist - widthscale / 2, 200 + drawdist, 0, 0, (int) picwidth, (int) picheight, null);
                                        else
                                            dmnew.dview.offg2.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale, widthscale, heightscale, null);
                                    } else if (tempmon.mirrored)
                                        g.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale, 224 + sidedist - widthscale / 2, 200 + drawdist, 0, 0, (int) picwidth, (int) picheight, null);
                                    else
                                        g.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale, widthscale, heightscale, null);
                                }
                                break;
                            case 2:
                                if (i != 2 || dist < 2) { //was i!=2 && dist!=3
                                    
                                    if (dist == 2) {
                                        drawdist = 80;//was 76
                                        if (i == 1) sidedist = 70;
                                        else sidedist = -206;
                                    } else if (dist == 1) {
                                        drawdist = 16;//was 16
                                        if (i == 0) sidedist = -135;
                                        else if (i == 1) sidedist = 46;
                                        else sidedist = 222;
                                    } else if (dist == 0) {
                                        drawdist = -16;//was -16
                                        if (i == 0) sidedist = -106;
                                        else if (i == 1) sidedist = 28;
                                        else sidedist = 176;
                                    }
                                    
                                    //scale = 2.5f-((float)dist*.6f);
                                    scale = 2.2f - ((float) dist * .6f);
                                    widthscale = (int) (picwidth / scale);
                                    heightscale = (int) (picheight / scale);
                                    if (!dmnew.NOTRANS && ((tempmon.isImmaterial && tempmon.number != 10))) { //|| tempmon.isdying)) {
                                        if (tempmon.mirrored)
                                            dmnew.dview.offg2.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale, 224 + sidedist - widthscale / 2, 200 + drawdist, 0, 0, (int) picwidth, (int) picheight, null);
                                        else
                                            dmnew.dview.offg2.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale, widthscale, heightscale, null);
                                    } else if (tempmon.mirrored)
                                        g.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale, 224 + sidedist - widthscale / 2, 200 + drawdist, 0, 0, (int) picwidth, (int) picheight, null);
                                    else
                                        g.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale, widthscale, heightscale, null);
                                }
                                break;
                            case 5:
                                
                                if (dist == 2) {
                                    drawdist = 80;//was 87
                                    if (i == 0) sidedist = -256;
                                    else if (i == 2) sidedist = 256;
                                    else sidedist = 0;
                                } else if (dist == 1) {
                                    drawdist = 16;//was 20
                                    if (i == 0) sidedist = -182;
                                    else if (i == 2) sidedist = 182;
                                    else sidedist = 0;
                                } else if (dist == 0) {
                                    drawdist = -16;//was -13
                                    if (i == 0) sidedist = -136;
                                    else if (i == 2) sidedist = 136;
                                    else sidedist = 0;
                                }
                                
                                //scale = 2.5f-((float)dist*.6f);
                                //scale = 2.35f-((float)dist*.6f);
                                scale = 2.2f - ((float) dist * .6f);
                                widthscale = (int) (picwidth / scale);
                                heightscale = (int) (picheight / scale);
                                if (!dmnew.NOTRANS && ((tempmon.isImmaterial && tempmon.number != 10))) { //|| tempmon.isdying)) {
                                    if (tempmon.mirrored)
                                        dmnew.dview.offg2.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale, 224 + sidedist - widthscale / 2, 200 + drawdist, 0, 0, (int) picwidth, (int) picheight, null);
                                    else
                                        dmnew.dview.offg2.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale, widthscale, heightscale, null);
                                } else if (tempmon.mirrored)
                                    g.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale, 224 + sidedist - widthscale / 2, 200 + drawdist, 0, 0, (int) picwidth, (int) picheight, null);
                                else
                                    g.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale, widthscale, heightscale, null);
                                
                                break;
                        }//end switch
                              /*
                              sidedist=0;
                              if (i==0) sidedist=-137-(48*dist)-dist*(dist-1)*14;
                              else if (i==2) sidedist=137+(48*dist)+dist*(dist-1)*14;
                              
                              drawdist=4;//was -8, then -2
                              if (dist==2) drawdist=70;//was 50, then 60
                              else if (dist==0) drawdist=-30;//was -38, then -34
                              
                              picwidth=(float)temppic.getWidth(null);
                              picheight=(float)temppic.getHeight(null);
                              scale = 2.0f+(float)dist*(-.5f);
                              widthscale = (int)(picwidth/scale);
                              heightscale=(int)(picheight/scale);
                              
                              //make ghostly - slow on my pc
                              if (!dmnew.NOTRANS && ((tempmon.isImmaterial && tempmon.number!=10) )) { //|| tempmon.isdying)) {
                                  if (tempmon.mirrored) dmnew.dview.offg2.drawImage(temppic,223+sidedist+widthscale/2,220+drawdist-heightscale,223+sidedist-widthscale/2,220+drawdist,0,0,(int)picwidth,(int)picheight,null);
                                  else dmnew.dview.offg2.drawImage(temppic,223+sidedist-widthscale/2,220+drawdist-heightscale,widthscale,heightscale,null);
                              }
                              else if (tempmon.mirrored) g.drawImage(temppic,223+sidedist+widthscale/2,220+drawdist-heightscale,223+sidedist-widthscale/2,220+drawdist,0,0,(int)picwidth,(int)picheight,null);
                              else g.drawImage(temppic,223+sidedist-widthscale/2,220+drawdist-heightscale,widthscale,heightscale,null);
                              */
                    }
                    if (subface == 3) subface = 5;
                    else subface++;
                }
                if (dist == 0) darkfilt.setDarks(3);
                else if (dist == 1) darkfilt.setDarks(2);
            }
            
            if (endsub == 6) endsub = 4;
            
            //draw projs
            dmnew.Projectile tempp;
            subsquaretodraw = startsub;
            if (numProjs > 0 && numprojdrawn < numProjs && !(dist == 3 && i != 1) && (hasParty || dmnew.dispell > 0 || (dist == 2 && i == 1 && dmnew.magicvision > 0) || !(this instanceof FakeWall))) {
                while (subsquaretodraw < endsub) {
                    index = 0;
                    int j = 0;
                    while (j < numProjs && numprojdrawn < numProjs) {
                        do {
                            tempp = (dmnew.Projectile) dmnew.dmprojs.get(index);
                            index++;
                        }
                        while (tempp.level != dmnew.level || tempp.x != xadjust || tempp.y != yadjust);
                        if ((tempp.subsquare + dmnew.facing) % 4 == subsquaretodraw) {
                            //draw it
                            numprojdrawn++;
                            temppic = null;
                            if (tempp.isending && tempp.sp != null) {
                                if (dist == 3) {
                                    g.drawImage(tempp.sp.endpic0, 0, 0, null);
                                } else temppic = tempp.sp.endpic;
                            }
                            //else if (dist!=3 || (tempp.justthrown && tempp.direction==dmnew.facing) || (!tempp.justthrown && tempp.direction!=dmnew.facing && (tempp.direction-dmnew.facing)%2==0)) {
                            else if (dist != 3 || (tempp.justthrown && tempp.direction == dmnew.facing) || (!tempp.justthrown && subsquaretodraw < 2 && dmnew.heroatsub[subsquaretodraw] == -1) || (!tempp.justthrown && tempp.direction != dmnew.facing && (tempp.direction - dmnew.facing) % 2 == 0)) {
                                //following line added to prevent drawing of projs hitting backs of mons:
                                if (!tempp.justthrown && tempp.direction != dmnew.facing && (tempp.direction - dmnew.facing) % 2 == 0 && (dmnew.dmmons.get(dmnew.level + "," + xadjust + "," + yadjust + "," + 5) != null || dmnew.dmmons.get(dmnew.level + "," + xadjust + "," + yadjust + "," + subsquaretodraw) != null)) {
                                } else if (dist < 3 && tempp.it != null) {
                                    //if (!tempp.it.hasthrowpic) temppic = Item.darkpic[tempp.it.number][dist];
                                    if (!tempp.it.hasthrowpic)
                                        temppic = (Image) Item.pics.get(tempp.it.dpicstring + "-d" + dist);
                                    if (temppic == null) {
                                        temppic = dmnew.tk.createImage(new FilteredImageSource(tempp.pic.getSource(), darkfilt));
                                        tracker.addImage(temppic, 0);
                                        try {
                                            tracker.waitForID(0, 5000);
                                        } catch (InterruptedException e) {
                                        }
                                        tracker.removeImage(temppic);
                                        //if (!tempp.it.hasthrowpic) Item.darkpic[tempp.it.number][dist] = temppic;
                                        if (!tempp.it.hasthrowpic)
                                            Item.pics.put(tempp.it.dpicstring + "-d" + dist, temppic);
                                    }
                                } else temppic = tempp.pic;
                            }
                            if (temppic != null) {
                                widthscale = 0;
                                heightscale = 0;
                                scale = 0.0f;
                                picwidth = (float) temppic.getWidth(null);
                                picheight = (float) temppic.getHeight(null);
                                switch ((tempp.subsquare + dmnew.facing) % 4) {
                                    case 0:
                                        if ((i != 0 || dist == 1) && dist > 0 && ((dmnew.magicvision > 0 && dist == 2) || drawFurtherItems)) {
                                            
                                            if (dist == 3) {
                                                drawdist = -80;//was 120, then -60
                                                if (i == 1) sidedist = -90;
                                                else sidedist = 260;
                                            } else if (dist == 2) {
                                                drawdist = -88;//was 42, then -70
                                                if (i == 1) sidedist = -56;
                                                else sidedist = 165;
                                            } else {
                                                drawdist = -92;//was -2, then -80
                                                if (i == 0) sidedist = -200;
                                                else if (i == 1) sidedist = -36;
                                                else sidedist = 120;
                                            }
                                            
                                            //if (tempp.sp!=null) scale = 2.8f-((float)dist*((float)tempp.sp.gain/10.0f));
                                            //else scale = 2.8f-((float)dist*.6f);
                                            scale = 2.8f - ((float) dist * .6f);
                                            if (tempp.sp != null && tempp.sp.type != 0)
                                                scale += (6.0f - ((float) tempp.sp.gain)) / 5.0f;
                                            widthscale = (int) (picwidth / scale);
                                            heightscale = (int) (picheight / scale);
                                            if (tempp.it != null && tempp.it.hasthrowpic && (i == 0 || (i == 1 && (dmnew.facing - tempp.direction) % 2 == 0)))
                                                g.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale / 2, 224 + sidedist - widthscale / 2, 200 + drawdist + heightscale / 2, 0, 0, (int) picwidth, (int) picheight, null);
                                            else
                                                g.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale / 2, widthscale, heightscale, null);
                                        }
                                        break;
                                    case 1:
                                        if ((i != 2 || dist == 1) && dist > 0 && ((dmnew.magicvision > 0 && dist == 2) || drawFurtherItems)) {
                                            
                                            if (dist == 3) {
                                                drawdist = -80;//was 120, then -60
                                                if (i == 1) sidedist = 90;
                                                else sidedist = -260;
                                            } else if (dist == 2) {
                                                drawdist = -88;//was 42, then -70
                                                if (i == 1) sidedist = 56;
                                                else sidedist = -165;
                                            } else {
                                                drawdist = -92;//was -2, then -80
                                                if (i == 0) sidedist = -120;
                                                else if (i == 1) sidedist = 36;
                                                else sidedist = 200;
                                            }
                                            
                                            //if (tempp.sp!=null) scale = 2.8f-((float)dist*.6f)+(6.0f-((float)tempp.sp.gain))/10.0f;
                                            //else scale = 2.8f-((float)dist*.6f);
                                            scale = 2.8f - ((float) dist * .6f);
                                            if (tempp.sp != null && tempp.sp.type != 0)
                                                scale += (6.0f - ((float) tempp.sp.gain)) / 5.0f;
                                            widthscale = (int) (picwidth / scale);
                                            heightscale = (int) (picheight / scale);
                                            g.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale / 2, widthscale, heightscale, null);
                                        }
                                        break;
                                    case 3:
                                        if ((i != 0 || dist < 2) && dist != 3) {
                                            
                                            if (dist == 2) {
                                                drawdist = -84;//was 76, then -65
                                                if (i == 1) sidedist = -70;
                                                else sidedist = 206;
                                            } else if (dist == 1) {
                                                drawdist = -90;//was 16, then -75
                                                if (i == 0) sidedist = -222;
                                                else if (i == 1) sidedist = -46;
                                                else sidedist = 135;
                                            } else if (dist == 0) {
                                                drawdist = -95;//was -16, then -85
                                                if (i == 0) sidedist = -176;
                                                else if (i == 1) sidedist = -28;//was -26
                                                else sidedist = 106;
                                            }
                                            
                                            //if (tempp.sp!=null) scale = 2.5f-((float)dist*((float)tempp.sp.gain/10.0f));
                                            //else scale = 2.5f-((float)dist*.6f);
                                            scale = 2.5f - ((float) dist * .6f);
                                            if (tempp.sp != null && tempp.sp.type != 0)
                                                scale += (6.0f - ((float) tempp.sp.gain)) / 5.0f;
                                            widthscale = (int) (picwidth / scale);
                                            heightscale = (int) (picheight / scale);
                                            if (tempp.it != null && tempp.it.hasthrowpic && (i == 0 || (i == 1 && (dmnew.facing - tempp.direction) % 2 == 0)))
                                                g.drawImage(temppic, 224 + sidedist + widthscale / 2, 200 + drawdist - heightscale / 2, 224 + sidedist - widthscale / 2, 200 + drawdist + heightscale / 2, 0, 0, (int) picwidth, (int) picheight, null);
                                            else
                                                g.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale / 2, widthscale, heightscale, null);
                                            //g.drawImage(temppic,224+sidedist-widthscale/2,200+drawdist-heightscale,widthscale,heightscale,null);
                                        }
                                        break;
                                    case 2:
                                        if ((i != 2 || dist < 2) && dist != 3) { //was i!=2 && dist!=3
                                            
                                            if (dist == 2) {
                                                drawdist = -84;//was 76
                                                if (i == 1) sidedist = 70;
                                                else sidedist = -206;
                                            } else if (dist == 1) {
                                                drawdist = -90;//was 16
                                                if (i == 0) sidedist = -135;
                                                else if (i == 1) sidedist = 46;
                                                else sidedist = 222;
                                            } else if (dist == 0) {
                                                drawdist = -95;//was -16
                                                if (i == 0) sidedist = -106;
                                                else if (i == 1) sidedist = 28;//was 26
                                                else sidedist = 176;
                                            }
                                            
                                            //if (tempp.sp!=null) scale = 2.5f-((float)dist*.6f)+(6.0f-((float)tempp.sp.gain))/10.0f;
                                            //else scale = 2.5f-((float)dist*.6f);
                                            scale = 2.5f - ((float) dist * .6f);
                                            if (tempp.sp != null && tempp.sp.type != 0)
                                                scale += (6.0f - ((float) tempp.sp.gain)) / 5.0f;
                                            widthscale = (int) (picwidth / scale);
                                            heightscale = (int) (picheight / scale);
                                            g.drawImage(temppic, 224 + sidedist - widthscale / 2, 200 + drawdist - heightscale / 2, widthscale, heightscale, null);
                                        }
                                        break;
                                }//end switch
                            }//end temppic if
                        }//end subsquare if
                        j++;
                    }//end while loop
                    subsquaretodraw++;
                }//end while loop
            }//end proj if
            
            
            //draw door if necessary
            if (numtimes == 0 && dist != 3 && this instanceof Door && (dmnew.facing == ((Door) this).side || (dmnew.facing + 2) % 4 == ((Door) this).side)) {
                int row = 3 - dist;
                int col = i + 1;
                ((Door) this).drawDoor(g, row, col);
                        /*
                        int xc = ((Door)this).xc; int yc = ((Door)this).yc;
                        Graphics2D dg = g;
                        if (row==1 && col==2 && dmnew.magicvision>0 && ((Door)this).pictype!=8) dg = dmnew.dview.offg2; //note: can't see thru red doors
                        if (col==2) g.drawImage(((Door)this).openpic[row-1],xc,yc,null);
                        else if (row==1) {
                                if (col==1) g.drawImage(((Door)this).openpic[(Door)this).opentype][0],xc-197,yc,null);//was -192
                                else g.drawImage(((Door)this).openpic[(Door)this).opentype][0],xc-124,yc,null);//was -128
                        }
                        else if (row==2) {
                                if (col==1) g.drawImage(((Door)this).openpic[(Door)this).opentype][1],xc-40,yc,null);
                                else g.drawImage(((Door)this).openpic[(Door)this).opentype][1],xc-168,yc,null);
                        }
                        else if (row==3) {
                                if (col==1) g.drawImage(((Door)this).openpic[(Door)this).opentype][2],xc+18,yc,null);
                                else {
                                        xc=xc-((Door)this).openpic[(Door)this).opentype][2].getWidth(null)-18;
                                        g.drawImage(((Door)this).openpic[(Door)this).opentype][2],xc,yc,null);
                                }
                        }
                        
                        if (((Door)this).changecount!=0 || ((Door)this).isBroken) {
                                int h = (Door)this).doorpic[row][2].getHeight(null);
                                int w = (Door)this).doorpic[row][2].getWidth(null);
                                int rowadjustx=0,rowadjusty=0;
                                if (row==1) {
                                        rowadjusty=18;
                                        if (col==2) rowadjustx=59;
                                        else if (col==1) rowadjustx=-138;
                                        else rowadjustx=-64;
                                }
                                else if (row==2) {
                                        rowadjusty=9;
                                        if (col==2) rowadjustx=37;
                                        else if (col==1) rowadjustx=-2;
                                        else rowadjustx=3;
                                }
                                else if (row==3) {
                                        rowadjusty=6;
                                        if (col==2) rowadjustx=26;
                                        else if (col==1) rowadjustx=45;
                                        else rowadjustx=25;
                                }
                                
                                if (row!=3 && row!=1 && col==3) xc=xc-pic[row][3].getWidth(null)-1; //corrects blankpic
                                
                                if (((Door)this).changecount==0) dg.drawImage(((Door)this).brokenpic[row-1],xc+rowadjustx,yc+rowadjusty,null);
                                else if (((Door)this).changecount==4) {
                                        dg.drawImage(pic[row][2],xc+rowadjustx,yc+rowadjusty,null);
                                }
                                else if (((Door)this).changecount==1) {
                                        //((Door)this).offscreen = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
                                        //((Door)this).offg = ((Door)this).offscreen.createGraphics();
                                        //((Door)this).offg.drawImage(pic[row][2],0,0,null);
                                        //dg.drawImage(((Door)this).offscreen.getSubimage(0,3*h/4,w,h/4),xc+rowadjustx,yc+rowadjusty,null);
                                        dg.setClip(xc+rowadjustx,yc+rowadjusty,w,h/4);
                                        dg.drawImage(pic[row][2],xc+rowadjustx,yc+rowadjusty-3*h/4,null);
                                        dg.setClip(null);
                                }
                                else if (((Door)this).changecount==2) {
                                        //((Door)this).offscreen = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
                                        //((Door)this).offg = ((Door)this).offscreen.createGraphics();
                                        //((Door)this).offg.drawImage(pic[row][2],0,0,null);
                                        //dg.drawImage(((Door)this).offscreen.getSubimage(0,h/2,w,h/2),xc+rowadjustx,yc+rowadjusty,null);
                                        dg.setClip(xc+rowadjustx,yc+rowadjusty,w,h/2);
                                        dg.drawImage(pic[row][2],xc+rowadjustx,yc+rowadjusty-h/2,null);
                                        dg.setClip(null);
                                }
                                else if (((Door)this).changecount==3) {
                                        //((Door)this).offscreen = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
                                        //((Door)this).offg = ((Door)this).offscreen.createGraphics();
                                        //((Door)this).offg.drawImage(pic[row][2],0,0,null);
                                        //dg.drawImage(((Door)this).offscreen.getSubimage(0,h/4,w,3*h/4),xc+rowadjustx,yc+rowadjusty,null);
                                        dg.setClip(xc+rowadjustx,yc+rowadjusty,w,3*h/4);
                                        dg.drawImage(pic[row][2],xc+rowadjustx,yc+rowadjusty-h/4,null);
                                        dg.setClip(null);
                                }
                        }
                        */
            }//end draw door itself
            
            
            startsub = 2;
            endsub = 4;
        }//end numtimes for loop
        
        //drawclouds
        dmnew.PoisonCloud tempcloud;
        
        if (hasCloud && dmnew.cloudchanging && !(dist == 3 && i != 1) && (hasParty || dmnew.dispell > 0 || (dist == 2 && i == 1 && dmnew.magicvision > 0) || !(this instanceof FakeWall))) {
            sidedist = 0;
            if (i == 0) sidedist = -132 - (20 * (dist * dist));//was 130-
            else if (i == 2) sidedist = 132 + (20 * (dist * dist));//was 130+
            
            Iterator it = dmnew.cloudstochange.iterator();
            while (it.hasNext()) {
                tempcloud = (dmnew.PoisonCloud) it.next();
                if (tempcloud.x == xadjust && tempcloud.y == yadjust) {
                    if (dist == 3) {
                        if (tempcloud.stagecounter < 1 || tempcloud.stagecounter == 4 || tempcloud.stagecounter == 5)
                            g.drawImage(dmnew.cloudpicin, 0, 0, null);
                        else g.drawImage(dmnew.cloudpicin, 448, 0, 0, 326, 0, 0, 448, 326, null);
                        break;
                    } else {
                        picwidth = 272.0f;//(float)dmnew.cloudpic[2-dist].getWidth(null);
                        picheight = 164.0f;//(float)dmnew.cloudpic[2-dist].getHeight(null);
                        scale = 3.5f - ((float) dist + 0.5f) + (0.5f * (6.0f - (float) tempcloud.stage));
                        widthscale = (int) (picwidth / scale);
                        heightscale = (int) (picheight / scale);
                        if (tempcloud.stagecounter < 1 || tempcloud.stagecounter == 4 || tempcloud.stagecounter == 5)
                            g.drawImage(dmnew.cloudpic[2 - dist], 223 + sidedist - widthscale / 2, 120 + dist * dist - heightscale / 2 - 4 + dist, widthscale, heightscale, null);//120 was 110
                        else
                            g.drawImage(dmnew.cloudpic[2 - dist], 223 + sidedist - widthscale / 2 + widthscale, 120 + dist * dist - heightscale / 2 - 4 + dist, 223 + sidedist - widthscale / 2, 120 + dist * dist - heightscale / 2 - 4 + dist + heightscale, 0, 0, (int) picwidth, (int) picheight, null);//ditto
                        break;
                    }
                }
            }
        }//end cloud if
        
        //drawcage
        if (dmnew.fluxchanging) {
            dmnew.FluxCage tempcage = (dmnew.FluxCage) dmnew.fluxcages.get(dmnew.level + "," + xadjust + "," + yadjust);
            if (tempcage != null) {
                if (!tempcage.mirrored)
                    g.drawImage(dmnew.cagepic[3 - dist][i], dmnew.cagex[3 - dist][i], dmnew.cagey[3 - dist], null);
                else if (i == 0)
                    g.drawImage(dmnew.cagepic[3 - dist][2], dmnew.cagex[3 - dist][0] + dmnew.cagepic[3 - dist][2].getWidth(null), dmnew.cagey[3 - dist], dmnew.cagex[3 - dist][0], dmnew.cagey[3 - dist] + dmnew.cagepic[3 - dist][2].getHeight(null), 0, 0, dmnew.cagepic[3 - dist][2].getWidth(null), dmnew.cagepic[3 - dist][2].getHeight(null), null);
                else if (i == 2)
                    g.drawImage(dmnew.cagepic[3 - dist][0], dmnew.cagex[3 - dist][2] + dmnew.cagepic[3 - dist][0].getWidth(null), dmnew.cagey[3 - dist], dmnew.cagex[3 - dist][2], dmnew.cagey[3 - dist] + dmnew.cagepic[3 - dist][0].getHeight(null), 0, 0, dmnew.cagepic[3 - dist][0].getWidth(null), dmnew.cagepic[3 - dist][0].getHeight(null), null);
                else
                    g.drawImage(dmnew.cagepic[3 - dist][i], dmnew.cagex[3 - dist][i] + dmnew.cagepic[3 - dist][i].getWidth(null), dmnew.cagey[3 - dist], dmnew.cagex[3 - dist][i], dmnew.cagey[3 - dist] + dmnew.cagepic[3 - dist][i].getHeight(null), 0, 0, dmnew.cagepic[3 - dist][i].getWidth(null), dmnew.cagepic[3 - dist][i].getHeight(null), null);
            }
        }//end drawcage
        
    }//end drawContents
    
    public String toString() {
        if (hasParty) return ("" + 'X');
        else if (hasMons) return ("" + 'M');
        else if (numProjs > 0) return ("" + 'P');
        else return ("" + mapchar);
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        so.writeChar(mapchar);
        so.writeBoolean(canHoldItems);
        so.writeBoolean(isPassable);
        so.writeBoolean(canPassProjs);
        so.writeBoolean(canPassMons);
        so.writeBoolean(canPassImmaterial);
        so.writeBoolean(drawItems);
        so.writeBoolean(drawFurtherItems);
        so.writeInt(numProjs);
        so.writeBoolean(hasParty);
        so.writeBoolean(hasMons);
        //so.writeBoolean(hasImmaterialMons);
        so.writeBoolean(hasItems);
        if (hasItems) so.writeObject(mapItems);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
    }
}

