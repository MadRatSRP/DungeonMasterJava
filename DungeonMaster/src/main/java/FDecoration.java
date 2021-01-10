import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectOutputStream;

class FDecoration extends Floor {
    static public boolean[] ADDEDPICS = new boolean[4];
    public int number; //id of decoration
    private int[] xadjust, yadjust;
    public int level, xcoord, ycoord; //used in doAction to drop any water els
    
    public FDecoration(int num) {
        super();
        number = num;
        mapchar = 'F';
        setPics();
    }
    
    public void setNumber(int number) {
        if (this.number != number) {
            this.number = number;
            setPics();
        }
    }
    
    protected void setPics() {
        //super.setPics();
        pic = new Image[4][5];
        xadjust = new int[9];
        yadjust = new int[3];
        switch (number) {
            case 0:
                pic[1][2] = loadPic("puddle1.gif");
                pic[2][2] = loadPic("puddle2.gif");
                pic[3][2] = loadPic("puddle3.gif");
                
                //row 1
                yadjust[0] = 240;
                xadjust[0] = -44;  //col 1
                xadjust[1] = 193;  //col 2
                xadjust[2] = 440;  //col 3
                
                //row 2
                yadjust[1] = 198;
                xadjust[3] = 20;   //col 1
                xadjust[4] = 202;  //col 2
                xadjust[5] = 390;  //col 3
                
                //row 3
                yadjust[2] = 170;
                xadjust[6] = 70;   //col 1
                xadjust[7] = 208;  //col 2
                xadjust[8] = 350;  //col 3
                
                break;
            case 1:
                pic[1][2] = loadPic("grass1.gif");
                pic[2][2] = loadPic("grass2.gif");
                pic[3][2] = loadPic("grass3.gif");
                
                //row 1
                yadjust[0] = 231;
                xadjust[0] = -70;  //col 1
                xadjust[1] = 172;  //col 2
                xadjust[2] = 400;  //col 3
                
                //row 2
                yadjust[1] = 185;
                xadjust[3] = 10;   //col 1
                xadjust[4] = 190;  //col 2
                xadjust[5] = 360;  //col 3
                
                //row 3
                yadjust[2] = 160;
                xadjust[6] = 60;   //col 1
                xadjust[7] = 204;  //col 2
                xadjust[8] = 340;  //col 3
                
                break;
            case 2:
                pic[0][2] = loadPic("seal02.gif");
                pic[1][1] = loadPic("seal11.gif");
                pic[1][2] = loadPic("seal12.gif");
                pic[1][3] = loadPic("seal13.gif");
                pic[2][1] = loadPic("seal21.gif");
                pic[2][2] = loadPic("seal22.gif");
                pic[2][3] = loadPic("seal23.gif");
                pic[3][1] = loadPic("seal31.gif");
                pic[3][2] = loadPic("seal32.gif");
                pic[3][3] = loadPic("seal33.gif");
                
                //row 1
                yadjust[0] = 224;
                xadjust[0] = 0;    //col 1
                xadjust[1] = 143;  //col 2
                xadjust[2] = 389;  //col 3
                
                //row 2
                yadjust[1] = 186;
                xadjust[3] = 0;    //col 1
                xadjust[4] = 165;  //col 2
                xadjust[5] = 335;  //col 3
                
                //row 3
                yadjust[2] = 163;
                xadjust[6] = 39;   //col 1
                xadjust[7] = 177;  //col 2
                xadjust[8] = 310;  //col 3
                
                break;
            case 3:
                //pic[0][2] = loadPic("fgrate02.gif");
                pic[1][1] = loadPic("fgrate11.gif");
                pic[1][2] = loadPic("fgrate12.gif");
                pic[1][3] = loadPic("fgrate13.gif");
                pic[2][1] = loadPic("fgrate21.gif");
                pic[2][2] = loadPic("fgrate22.gif");
                pic[2][3] = loadPic("fgrate23.gif");
                pic[3][1] = loadPic("fgrate31.gif");
                pic[3][2] = loadPic("fgrate32.gif");
                pic[3][3] = loadPic("fgrate33.gif");
                
                //row 1
                yadjust[0] = 227;
                xadjust[0] = 0;    //col 1
                xadjust[1] = 161;  //col 2
                xadjust[2] = 422;  //col 3
                
                //row 2
                yadjust[1] = 189;
                xadjust[3] = 0;    //col 1
                xadjust[4] = 181;  //col 2
                xadjust[5] = 363;  //col 3
                
                //row 3
                yadjust[2] = 167;
                xadjust[6] = 51;   //col 1
                xadjust[7] = 194;  //col 2
                xadjust[8] = 330;  //col 3
                
                break;
        }
        if (number < 2) {
            pic[1][1] = pic[1][2];
            pic[1][3] = pic[1][2];
            pic[2][1] = pic[2][2];
            pic[2][3] = pic[2][2];
            pic[3][1] = pic[3][2];
            pic[3][3] = pic[3][2];
        }
        if (!ADDEDPICS[number]) {
            tracker.addImage(pic[1][2], 0);
            tracker.addImage(pic[2][2], 0);
            tracker.addImage(pic[3][2], 0);
            if (number > 1) {
                if (number == 2) tracker.addImage(pic[0][2], 0);
                tracker.addImage(pic[1][1], 0);
                tracker.addImage(pic[1][3], 0);
                tracker.addImage(pic[2][1], 0);
                tracker.addImage(pic[2][3], 0);
                tracker.addImage(pic[3][1], 0);
                tracker.addImage(pic[3][3], 0);
            }
            ADDEDPICS[number] = true;
        }
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (col == 0 || col == 4) return;
            //else if (row!=0 && (row!=1 || col==2)) {
        else if (row != 0) {
            g.drawImage(pic[row][col], xadjust[(row - 1) * 3 + col - 1], yadjust[row - 1], obs);
        } else if (number == 2 && col == 2) {
            g.drawImage(pic[0][2], 109, 293, obs);
        }
        drawContents(g, 3 - row, col - 1);
    }
    
    public void tryTeleport(dmnew.Monster mon) {
        if (mon.number != 11) return;
        dropall(level + 1);
    }
    
    public void dropall(int droplevel) {
        if (droplevel >= dmnew.numlevels) return;
        //if a pit/floor grate below, increase droplevel
        if ((droplevel + 1) < dmnew.numlevels && ((dmnew.DungeonMap[droplevel][xcoord][ycoord] instanceof FDecoration && ((FDecoration) dmnew.DungeonMap[droplevel][xcoord][ycoord]).number == 3) || (dmnew.DungeonMap[droplevel][xcoord][ycoord] instanceof Pit && ((Pit) dmnew.DungeonMap[droplevel][xcoord][ycoord]).isOpen))) {
            //ignores any mons over the pit/floor grate below
            dropall(droplevel + 1);
            return;
        }
        //drop water els
        if (hasMons) {
            boolean montest = false;
            dmnew.Monster tempmon, tempmon2;
            for (int i = 0; i < 6; i++) {
                tempmon = (dmnew.Monster) dmnew.dmmons.get(level + "," + xcoord + "," + ycoord + "," + i);
                if (tempmon != null) {
                    if (tempmon.number != 11) montest = true;
                    else {
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
                        //in case falls on floorswitch
                        dmnew.DungeonMap[droplevel][xcoord][ycoord].tryFloorSwitch(MapObject.MONSTEPPINGON);
                        dmnew.DungeonMap[droplevel][xcoord][ycoord].hasMons = true;
                        dmnew.dmmons.put(droplevel + "," + xcoord + "," + ycoord + "," + tempmon.subsquare, tempmon);
                        //in case falls on active teleporter
                        dmnew.DungeonMap[droplevel][xcoord][ycoord].tryTeleport(tempmon);
                    }
                }
                if (i == 3) i++;
            }
            if (!montest) hasMons = false;
        }
    }
    
    public void doAction() {
        if (number == 3 && hasMons) dropall(level + 1);
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(number);
        if (number == 3) {
            so.writeInt(level);
            so.writeInt(xcoord);
            so.writeInt(ycoord);
        }
    }
}