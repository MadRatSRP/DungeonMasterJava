//only a mon or the party can occupy the pair of stairs at a single time:
//dont let party enter stairs square and call tryteleport on the mons (make them come up/down)
//dont let mons enter stairs square if party is in stairs below/above

//mons aren't teleported until they step into further subsquares (this should fix the floating in the back problem)

//if dungeonmap[dmnew.level-1/+1][dmnew.partyx][dmnew.partyy].hasMons
//if dungeonmap[mon.level-1/+1][mon.x][mon.y].hasParty

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

class Stairs extends SidedWall {
    static public boolean ADDEDPICSUP;
    static public boolean ADDEDPICSDOWN;
    public boolean goesUp;
    
    public Stairs(int sde, boolean dir) {
        super(sde);
        mapchar = '>';
        isPassable = true;
        canPassMons = true;
        canPassImmaterial = true;
        canHoldItems = true;
        drawItems = true;
        drawFurtherItems = true;
        goesUp = dir;
        setPics();
    }
    
    public void addItem(Item i) {
        //check side and facing to determine proper subsquare
                /*
                int s=dmnew.randGen.nextInt(2);
                if (dmnew.facing==side) {
                   if (dmnew.subsquare[i.subsquare]>1) s=-1;
                   else s+=2;
                }
                else if ((dmnew.facing-side)%2==0) {
                   if (dmnew.subsquare[i.subsquare]<2) s=-1;
                }
                else if (dmnew.facing==(side+1)%4) {
                   if (s!=0) s=2;
                }
                else if (dmnew.facing==(side+3)%4) {
                   if (s==0) s=3;
                }
                if (s!=-1) {
                   for (int j=0;j<4;j++) {
                      if (dmnew.subsquare[j]==s) i.subsquare=j;
                   }
                }
                */
                /*
                if (side==0 && i.subsquare<2) i.subsquare+=2;
                else if (side==2 && i.subsquare>1) i.subsquare-=2;
                else if (side==1 && i.subsquare!=1 && i.subsquare!=2) {
                        if (i.subsquare==0) i.subsquare=1;
                        else i.subsquare=2;
                }
                else if (side==3 && i.subsquare!=0 && i.subsquare!=3) {
                        if (i.subsquare==1) i.subsquare=0;
                        else i.subsquare=3;
                }
                */
        if (side == 0) {
            if (i.subsquare == 0) i.subsquare = 3;
            else if (i.subsquare == 1) i.subsquare = 2;
        } else if (side == 2) {
            if (i.subsquare == 2) i.subsquare = 1;
            else if (i.subsquare == 3) i.subsquare = 0;
        } else if (side == 1) {
            if (i.subsquare == 0) i.subsquare = 1;
            else if (i.subsquare == 3) i.subsquare = 2;
        } else {
            if (i.subsquare == 1) i.subsquare = 0;
            else if (i.subsquare == 2) i.subsquare = 3;
        }
        if (mapItems == null) mapItems = new ArrayList(4);
        mapItems.add(i);
        hasItems = true;
    }
    
    protected void setPics() {
        super.setPics();
        if (!goesUp) {
            facingside[0][2] = loadPic("stairsdown02.gif");
            facingside[1][1] = loadPic("stairsdown11.gif");
            facingside[1][2] = loadPic("stairsdown12.gif");
            facingside[1][3] = loadPic("stairsdown13.gif");
            facingside[2][1] = loadPic("stairsdown21.gif");
            facingside[2][2] = loadPic("stairsdown22.gif");
            facingside[2][3] = loadPic("stairsdown23.gif");
            facingside[3][1] = loadPic("stairsdown31.gif");
            facingside[3][2] = loadPic("stairsdown32.gif");
            facingside[3][3] = loadPic("stairsdown33.gif");
            col1pic[1] = loadPic("stairsdowncol11.gif");
            col3pic[1] = loadPic("stairsdowncol31.gif");
        } else {
            facingside[0][2] = loadPic("stairsup02.gif");
            facingside[1][1] = loadPic("stairsup11.gif");
            facingside[1][2] = loadPic("stairsup12.gif");
            facingside[1][3] = loadPic("stairsup13.gif");
            facingside[2][1] = loadPic("stairsup21.gif");
            facingside[2][2] = loadPic("stairsup22.gif");
            facingside[2][3] = loadPic("stairsup23.gif");
            facingside[3][1] = loadPic("stairsup31.gif");
            facingside[3][2] = loadPic("stairsup32.gif");
            facingside[3][3] = loadPic("stairsup33.gif");
            col1pic[1] = loadPic("stairsupcol11.gif");
            col3pic[1] = loadPic("stairsupcol31.gif");
        }
        col1pic[0] = loadPic("stairs01.gif");
        col1pic[2] = loadPic("stairs21.gif");
        col1pic[3] = blankpic;
        col3pic[0] = loadPic("stairs03.gif");
        col3pic[2] = loadPic("stairs23.gif");
        col3pic[3] = blankpic;
        if ((goesUp && !ADDEDPICSUP) || (!goesUp && !ADDEDPICSDOWN)) {
                        /*
                        tracker.addImage(facingside[0][2],5);
                        tracker.addImage(facingside[1][1],5);
                        tracker.addImage(facingside[1][2],5);
                        tracker.addImage(facingside[1][3],5);
                        tracker.addImage(facingside[2][1],5);
                        tracker.addImage(facingside[2][2],5);
                        tracker.addImage(facingside[2][3],5);
                        tracker.addImage(facingside[3][1],5);
                        tracker.addImage(facingside[3][2],5);
                        tracker.addImage(facingside[3][3],5);
                        tracker.addImage(col1pic[0],5);
                        tracker.addImage(col1pic[1],5);
                        tracker.addImage(col1pic[2],5);
                        tracker.addImage(col3pic[0],5);
                        tracker.addImage(col3pic[1],5);
                        tracker.addImage(col3pic[2],5);
                        try { tracker.waitForID(5); }
                        catch(InterruptedException e) {}
                        */
            tracker.addImage(facingside[0][2], 0);
            tracker.addImage(facingside[1][1], 0);
            tracker.addImage(facingside[1][2], 0);
            tracker.addImage(facingside[1][3], 0);
            tracker.addImage(facingside[2][1], 0);
            tracker.addImage(facingside[2][2], 0);
            tracker.addImage(facingside[2][3], 0);
            tracker.addImage(facingside[3][1], 0);
            tracker.addImage(facingside[3][2], 0);
            tracker.addImage(facingside[3][3], 0);
            tracker.addImage(col1pic[0], 0);
            tracker.addImage(col1pic[1], 0);
            tracker.addImage(col1pic[2], 0);
            tracker.addImage(col3pic[0], 0);
            tracker.addImage(col3pic[1], 0);
            tracker.addImage(col3pic[2], 0);
            if (goesUp) ADDEDPICSUP = true;
            else ADDEDPICSDOWN = true;
        }
    }
    
    public void redoStairsPics() {
        String newmapdir = Wall.currentdir + File.separator;
        File testfile;
        if (!goesUp) {
            testfile = new File(newmapdir + "stairsdown12.gif");
            if (testfile.exists()) {
                redoSidedPics();
                facingside[0][2] = dmnew.tk.getImage(newmapdir + "stairsdown02.gif");
                facingside[1][1] = dmnew.tk.getImage(newmapdir + "stairsdown11.gif");
                facingside[1][2] = dmnew.tk.getImage(newmapdir + "stairsdown12.gif");
                facingside[1][3] = dmnew.tk.getImage(newmapdir + "stairsdown13.gif");
                facingside[2][1] = dmnew.tk.getImage(newmapdir + "stairsdown21.gif");
                facingside[2][2] = dmnew.tk.getImage(newmapdir + "stairsdown22.gif");
                facingside[2][3] = dmnew.tk.getImage(newmapdir + "stairsdown23.gif");
                facingside[3][1] = dmnew.tk.getImage(newmapdir + "stairsdown31.gif");
                facingside[3][2] = dmnew.tk.getImage(newmapdir + "stairsdown32.gif");
                facingside[3][3] = dmnew.tk.getImage(newmapdir + "stairsdown33.gif");
                col1pic[1] = dmnew.tk.getImage(newmapdir + "stairsdowncol11.gif");
                col3pic[1] = dmnew.tk.getImage(newmapdir + "stairsdowncol31.gif");
            } else return;
        } else {
            testfile = new File(newmapdir + "stairsup12.gif");
            if (testfile.exists()) {
                redoSidedPics();
                facingside[0][2] = dmnew.tk.getImage(newmapdir + "stairsup02.gif");
                facingside[1][1] = dmnew.tk.getImage(newmapdir + "stairsup11.gif");
                facingside[1][2] = dmnew.tk.getImage(newmapdir + "stairsup12.gif");
                facingside[1][3] = dmnew.tk.getImage(newmapdir + "stairsup13.gif");
                facingside[2][1] = dmnew.tk.getImage(newmapdir + "stairsup21.gif");
                facingside[2][2] = dmnew.tk.getImage(newmapdir + "stairsup22.gif");
                facingside[2][3] = dmnew.tk.getImage(newmapdir + "stairsup23.gif");
                facingside[3][1] = dmnew.tk.getImage(newmapdir + "stairsup31.gif");
                facingside[3][2] = dmnew.tk.getImage(newmapdir + "stairsup32.gif");
                facingside[3][3] = dmnew.tk.getImage(newmapdir + "stairsup33.gif");
                col1pic[1] = dmnew.tk.getImage(newmapdir + "stairsupcol11.gif");
                col3pic[1] = dmnew.tk.getImage(newmapdir + "stairsupcol31.gif");
            } else return;
        }
        col1pic[0] = dmnew.tk.getImage(newmapdir + "stairs01.gif");
        col1pic[2] = dmnew.tk.getImage(newmapdir + "stairs21.gif");
        col3pic[0] = dmnew.tk.getImage(newmapdir + "stairs03.gif");
        col3pic[2] = dmnew.tk.getImage(newmapdir + "stairs23.gif");
        tracker.addImage(facingside[0][2], 5);
        tracker.addImage(facingside[1][1], 5);
        tracker.addImage(facingside[1][2], 5);
        tracker.addImage(facingside[1][3], 5);
        tracker.addImage(facingside[2][1], 5);
        tracker.addImage(facingside[2][2], 5);
        tracker.addImage(facingside[2][3], 5);
        tracker.addImage(facingside[3][1], 5);
        tracker.addImage(facingside[3][2], 5);
        tracker.addImage(facingside[3][3], 5);
        tracker.addImage(col1pic[0], 5);
        tracker.addImage(col1pic[1], 5);
        tracker.addImage(col1pic[2], 5);
        tracker.addImage(col3pic[0], 5);
        tracker.addImage(col3pic[1], 5);
        tracker.addImage(col3pic[2], 5);
        
        try {
            tracker.waitForID(5, 2000);
        } catch (InterruptedException ex) {
        }
        
        tracker.removeImage(facingside[0][2], 5);
        tracker.removeImage(facingside[1][1], 5);
        tracker.removeImage(facingside[1][2], 5);
        tracker.removeImage(facingside[1][3], 5);
        tracker.removeImage(facingside[2][1], 5);
        tracker.removeImage(facingside[2][2], 5);
        tracker.removeImage(facingside[2][3], 5);
        tracker.removeImage(facingside[3][1], 5);
        tracker.removeImage(facingside[3][2], 5);
        tracker.removeImage(facingside[3][3], 5);
        tracker.removeImage(col1pic[0], 5);
        tracker.removeImage(col1pic[1], 5);
        tracker.removeImage(col1pic[2], 5);
        tracker.removeImage(col3pic[0], 5);
        tracker.removeImage(col3pic[1], 5);
        tracker.removeImage(col3pic[2], 5);
    }
    
    //public void doAction() {
    //        if (dmnew.leveldir[dmnew.level]!=null) redoStairsPics();
    //}
    
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, java.awt.image.ImageObserver obs) {
        if (row == 0 && col == 2) {
            //draw on-the-stairs pic
            if (goesUp) g.drawImage(facingside[0][2], 0, 105, null);
            else g.drawImage(facingside[0][2], 0, 180, null);
            drawContents(g, 3, 1);
        } else super.drawPic(row, col, xc, yc, g, obs);
        if (col > 0 && col < 4) {
            if (row == 1 && col == 2 && dmnew.magicvision > 0) drawContents(dmnew.dview.offg, 3 - row, col - 1);
            else drawContents(g, 3 - row, col - 1);
        }
    }
    
    public void tryTeleport() {
        int levelchange = dmnew.level + 1;
        if (goesUp) levelchange -= 2; //results in dmnew.level-1
        
        //test in case mon is using the stairs, if is, move party back and bring mons up/down to current level
        if (dmnew.DungeonMap[levelchange][dmnew.partyx][dmnew.partyy].hasMons) {
            hasParty = false;
            for (int i = 0; i < 6; ) {
                dmnew.Monster mon = (dmnew.Monster) dmnew.dmmons.get(levelchange + "," + dmnew.partyx + "," + dmnew.partyy + "," + i);
                if (mon != null) ((Stairs) dmnew.DungeonMap[mon.level][mon.x][mon.y]).tryTeleport(mon);
                if (i == 3) i = 5;
                else i++;
            }
            if (side == 0) dmnew.partyy++;
            else if (side == 2) dmnew.partyy--;
            else if (side == 3) dmnew.partyx--;
            else dmnew.partyx++;
            dmnew.DungeonMap[dmnew.level][dmnew.partyx][dmnew.partyy].hasParty = true;
            dmnew.needredraw = true;
            return;
        }
        
        dmnew.DungeonMap[dmnew.level][dmnew.partyx][dmnew.partyy].hasParty = false;
        if (goesUp) {
            dmnew.level--;
        } else {
            dmnew.level++;
        }
        dmnew.DungeonMap[dmnew.level][dmnew.partyx][dmnew.partyy].hasParty = true;
        //if sidestepped onto stairs, turn so will be facing away
        //from them when get to next level
        //int tempss[] = new int[4];
        int willface = (((Stairs) dmnew.DungeonMap[dmnew.level][dmnew.partyx][dmnew.partyy]).side + 2) % 4;
        while (dmnew.facing != willface) {
            dmnew.facing++;
            if (dmnew.facing > 3) dmnew.facing = 0;
                        /*
                        for (int i=0;i<4;i++) {
                                tempss[i]=dmnew.subsquare[i];
                        }
                        dmnew.subsquare[0]=tempss[1];
                        dmnew.subsquare[1]=tempss[3];
                        dmnew.subsquare[2]=tempss[0];
                        dmnew.subsquare[3]=tempss[2];
                        */
        }
        
        dmnew.updateDark();
        //if (dmnew.darkfactor<dmnew.leveldark[dmnew.level]) dmnew.darkfactor=dmnew.leveldark[dmnew.level];
        dmnew.Projectile tempp;
        for (Iterator i = dmnew.dmprojs.iterator(); i.hasNext(); ) {
            tempp = (dmnew.Projectile) i.next();
            if (tempp.level == dmnew.level && tempp.it != null && tempp.it.hasthrowpic) {
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
                //if (s<3) tempp.pic = tempp.it.throwpic[s];
                //else tempp.pic = tempp.it.dpic;
            }
        }
        Graphics tempg = dmnew.dview.getGraphics();
        tempg.clearRect(0, 0, dmnew.dview.getSize().width, dmnew.dview.getSize().height);
        dmnew.dview.offg.clearRect(0, 0, dmnew.dview.getSize().width, dmnew.dview.getSize().height);
        dmnew.needredraw = true;
    }
    
    public void tryTeleport(dmnew.Monster mon) {
        int levelchange = mon.level + 1;
        if (goesUp) levelchange -= 2; //results in mon.level-1
        
        //figure out new facing and subsquare
        int blocksquare = 0;
        switch (side) {
            case 0:
                if (mon.subsquare == 2) {
                    blocksquare = 1;
                    dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
                    switch (((Stairs) dmnew.DungeonMap[levelchange][mon.x][mon.y]).side) {
                        case 2:
                            mon.subsquare = 1;
                            mon.facing = 0;
                            break;
                        case 0:
                            mon.subsquare = 2;
                            mon.facing = 2;
                            break;
                        case 3:
                            mon.subsquare = 0;
                            mon.facing = 1;
                            break;
                        case 1:
                            mon.subsquare = 2;
                            mon.facing = 3;
                            break;
                    }
                    dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
                } else if (mon.subsquare == 3) {
                    blocksquare = 0;
                    dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
                    switch (((Stairs) dmnew.DungeonMap[levelchange][mon.x][mon.y]).side) {
                        case 2:
                            mon.subsquare = 0;
                            mon.facing = 0;
                            break;
                        case 0:
                            mon.subsquare = 3;
                            mon.facing = 2;
                            break;
                        case 3:
                            mon.subsquare = 3;
                            mon.facing = 1;
                            break;
                        case 1:
                            mon.subsquare = 1;
                            mon.facing = 3;
                            break;
                    }
                    dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
                }
                break;
            case 2:
                if (mon.subsquare == 0) {
                    blocksquare = 3;
                    dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
                    switch (((Stairs) dmnew.DungeonMap[levelchange][mon.x][mon.y]).side) {
                        case 2:
                            mon.subsquare = 0;
                            mon.facing = 0;
                            break;
                        case 0:
                            mon.subsquare = 3;
                            mon.facing = 2;
                            break;
                        case 3:
                            mon.subsquare = 0;
                            mon.facing = 1;
                            break;
                        case 1:
                            mon.subsquare = 2;
                            mon.facing = 3;
                            break;
                    }
                    dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
                } else if (mon.subsquare == 1) {
                    blocksquare = 2;
                    dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
                    switch (((Stairs) dmnew.DungeonMap[levelchange][mon.x][mon.y]).side) {
                        case 2:
                            mon.subsquare = 1;
                            mon.facing = 0;
                            break;
                        case 0:
                            mon.subsquare = 2;
                            mon.facing = 2;
                            break;
                        case 3:
                            mon.subsquare = 3;
                            mon.facing = 1;
                            break;
                        case 1:
                            mon.subsquare = 1;
                            mon.facing = 3;
                            break;
                    }
                    dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
                }
                break;
            case 3:
                if (mon.subsquare == 0) {
                    blocksquare = 1;
                    dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
                    switch (((Stairs) dmnew.DungeonMap[levelchange][mon.x][mon.y]).side) {
                        case 2:
                            mon.subsquare = 0;
                            mon.facing = 0;
                            break;
                        case 0:
                            mon.subsquare = 2;
                            mon.facing = 2;
                            break;
                        case 3:
                            mon.subsquare = 0;
                            mon.facing = 1;
                            break;
                        case 1:
                            mon.subsquare = 1;
                            mon.facing = 3;
                            break;
                    }
                    dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
                } else if (mon.subsquare == 3) {
                    blocksquare = 2;
                    dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
                    switch (((Stairs) dmnew.DungeonMap[levelchange][mon.x][mon.y]).side) {
                        case 2:
                            mon.subsquare = 1;
                            mon.facing = 0;
                            break;
                        case 0:
                            mon.subsquare = 3;
                            mon.facing = 2;
                            break;
                        case 3:
                            mon.subsquare = 3;
                            mon.facing = 1;
                            break;
                        case 1:
                            mon.subsquare = 2;
                            mon.facing = 3;
                            break;
                    }
                    dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
                }
                break;
            case 1:
                if (mon.subsquare == 1) {
                    blocksquare = 0;
                    dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
                    switch (((Stairs) dmnew.DungeonMap[levelchange][mon.x][mon.y]).side) {
                        case 2:
                            mon.subsquare = 1;
                            mon.facing = 0;
                            break;
                        case 0:
                            mon.subsquare = 3;
                            mon.facing = 2;
                            break;
                        case 3:
                            mon.subsquare = 0;
                            mon.facing = 1;
                            break;
                        case 1:
                            mon.subsquare = 1;
                            mon.facing = 3;
                            break;
                    }
                    dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
                } else if (mon.subsquare == 2) {
                    blocksquare = 3;
                    dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
                    switch (((Stairs) dmnew.DungeonMap[levelchange][mon.x][mon.y]).side) {
                        case 2:
                            mon.subsquare = 0;
                            mon.facing = 0;
                            break;
                        case 0:
                            mon.subsquare = 2;
                            mon.facing = 2;
                            break;
                        case 3:
                            mon.subsquare = 3;
                            mon.facing = 1;
                            break;
                        case 1:
                            mon.subsquare = 2;
                            mon.facing = 3;
                            break;
                    }
                    dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
                }
                break;
        }
        
        //test in case party or other mon is using the stairs, if is, move mon back
        if (dmnew.DungeonMap[levelchange][mon.x][mon.y].hasParty || (dmnew.DungeonMap[levelchange][mon.x][mon.y].hasMons && (mon.subsquare == 5 || dmnew.dmmons.get(levelchange + "," + mon.x + "," + mon.y + "," + mon.subsquare) != null || dmnew.dmmons.get(levelchange + "," + mon.x + "," + mon.y + "," + 5) != null))) {
            dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
            hasMons = false;
            //hasImmaterialMons = false;
            if (side == 0) mon.y++;
            else if (side == 2) mon.y--;
            else if (side == 3) mon.x--;
            else mon.x++;
            mon.subsquare = blocksquare;
            mon.facing = side;
            dmnew.DungeonMap[mon.level][mon.x][mon.y].hasMons = true;
            //dmnew.DungeonMap[mon.level][mon.x][mon.y].hasImmaterialMons = mon.isImmaterial;
            dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
            return;
        }
        
        //nothing in way, so move up/down stairs
        dmnew.dmmons.remove(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare);
        hasMons = false;
        int i = 0;
        while (i < 6 && !hasMons) {
            if (dmnew.dmmons.get(mon.level + "," + mon.x + "," + mon.y + "," + i) != null) hasMons = true;
            if (i == 3) i = 5;
            else i++;
        }
        //hasImmaterialMons = false;
        mon.level = levelchange;
        dmnew.DungeonMap[levelchange][mon.x][mon.y].hasMons = true;
        //dmnew.DungeonMap[levelchange][mon.x][mon.y].hasImmaterialMons = mon.isImmaterial;
        dmnew.dmmons.put(mon.level + "," + mon.x + "," + mon.y + "," + mon.subsquare, mon);
        if (mon.level == dmnew.level) {
            int xdist = mon.x - dmnew.partyx;
            if (xdist < 0) xdist *= -1;
            int ydist = mon.y - dmnew.partyy;
            if (ydist < 0) ydist *= -1;
            if (xdist < 5 && ydist < 5) dmnew.needredraw = true; //close, so repaint
        }
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(goesUp);
    }
    
}
