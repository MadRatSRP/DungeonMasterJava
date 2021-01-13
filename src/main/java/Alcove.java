import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

class Alcove extends Wall {
    
    static public boolean ADDEDPICS;
    static protected Image[][] alcovepic = new Image[4][5];
    private ArrayList northside = new ArrayList();
    private ArrayList southside = new ArrayList();
    private ArrayList eastside = new ArrayList();
    private ArrayList westside = new ArrayList();
    private boolean isSwitch = false;
    private MultWallSwitch2 alcoveswitch;
    
    public Alcove() {
        super();
        mapchar = '[';
        //canPassMons = false;
        setPics();
    }
    
    public Alcove(MultWallSwitch2 fs) {
        super();
        mapchar = '[';
        //canPassMons = false;
        alcoveswitch = fs;
        isSwitch = true;
        setPics();
    }
    
    protected void setPics() {
        super.setPics();
        if (!ADDEDPICS) {
                        /*
                        alcovepic[0][1] = wallpic[0][1];
                        alcovepic[0][3] = wallpic[0][3];
                        alcovepic[1][1] = dmnew.tk.createImage(mapdir+"alcove11.gif");
                        alcovepic[1][2] = dmnew.tk.createImage(mapdir+"alcove12.gif");
                        alcovepic[1][3] = dmnew.tk.createImage(mapdir+"alcove13.gif");
                        alcovepic[2][1] = dmnew.tk.createImage(mapdir+"alcove21.gif");
                        alcovepic[2][2] = dmnew.tk.createImage(mapdir+"alcove22.gif");
                        alcovepic[2][3] = dmnew.tk.createImage(mapdir+"alcove23.gif");
                        alcovepic[3][0] = wallpic[3][0];
                        alcovepic[3][1] = dmnew.tk.createImage(mapdir+"alcove31.gif");
                        alcovepic[3][2] = dmnew.tk.createImage(mapdir+"alcove32.gif");
                        alcovepic[3][3] = dmnew.tk.createImage(mapdir+"alcove33.gif");
                        alcovepic[3][4] = wallpic[3][4];
                        */
            alcovepic[0][1] = wallpic[0][1];
            alcovepic[0][3] = wallpic[0][3];
            alcovepic[1][1] = dmnew.tk.getImage(mapdir + "alcove11.gif");
            alcovepic[1][2] = dmnew.tk.getImage(mapdir + "alcove12.gif");
            alcovepic[1][3] = dmnew.tk.getImage(mapdir + "alcove13.gif");
            alcovepic[2][1] = dmnew.tk.getImage(mapdir + "alcove21.gif");
            alcovepic[2][2] = dmnew.tk.getImage(mapdir + "alcove22.gif");
            alcovepic[2][3] = dmnew.tk.getImage(mapdir + "alcove23.gif");
            alcovepic[3][0] = wallpic[3][0];
            alcovepic[3][1] = dmnew.tk.getImage(mapdir + "alcove31.gif");
            alcovepic[3][2] = dmnew.tk.getImage(mapdir + "alcove32.gif");
            alcovepic[3][3] = dmnew.tk.getImage(mapdir + "alcove33.gif");
            alcovepic[3][4] = wallpic[3][4];
            
            tracker.addImage(alcovepic[1][1], 0);
            tracker.addImage(alcovepic[1][2], 0);
            tracker.addImage(alcovepic[1][3], 0);
            tracker.addImage(alcovepic[2][1], 0);
            tracker.addImage(alcovepic[2][2], 0);
            tracker.addImage(alcovepic[2][3], 0);
            tracker.addImage(alcovepic[3][1], 0);
            tracker.addImage(alcovepic[3][2], 0);
            tracker.addImage(alcovepic[3][3], 0);
            ADDEDPICS = true;
        }
    }
    
    public static void redoPics() {
        String newmapdir = Wall.currentdir + File.separator;
        File testfile = new File(newmapdir + "alcove21.gif");
        if (!testfile.exists()) return;
        
        alcovepic[0][1] = wallpic[0][1];
        alcovepic[0][3] = wallpic[0][3];
        alcovepic[1][1] = dmnew.tk.getImage(newmapdir + "alcove11.gif");
        alcovepic[1][2] = dmnew.tk.getImage(newmapdir + "alcove12.gif");
        alcovepic[1][3] = dmnew.tk.getImage(newmapdir + "alcove13.gif");
        alcovepic[2][1] = dmnew.tk.getImage(newmapdir + "alcove21.gif");
        alcovepic[2][2] = dmnew.tk.getImage(newmapdir + "alcove22.gif");
        alcovepic[2][3] = dmnew.tk.getImage(newmapdir + "alcove23.gif");
        alcovepic[3][0] = wallpic[3][0];
        alcovepic[3][1] = dmnew.tk.getImage(newmapdir + "alcove31.gif");
        alcovepic[3][2] = dmnew.tk.getImage(newmapdir + "alcove32.gif");
        alcovepic[3][3] = dmnew.tk.getImage(newmapdir + "alcove33.gif");
        alcovepic[3][4] = wallpic[3][4];
        
        tracker.addImage(alcovepic[1][1], 5);
        tracker.addImage(alcovepic[1][2], 5);
        tracker.addImage(alcovepic[1][3], 5);
        tracker.addImage(alcovepic[2][1], 5);
        tracker.addImage(alcovepic[2][2], 5);
        tracker.addImage(alcovepic[2][3], 5);
        tracker.addImage(alcovepic[3][1], 5);
        tracker.addImage(alcovepic[3][2], 5);
        tracker.addImage(alcovepic[3][3], 5);
        
        try {
            tracker.waitForID(5, 2000);
        } catch (InterruptedException ex) {
        }
        
        tracker.removeImage(alcovepic[1][1], 5);
        tracker.removeImage(alcovepic[1][2], 5);
        tracker.removeImage(alcovepic[1][3], 5);
        tracker.removeImage(alcovepic[2][1], 5);
        tracker.removeImage(alcovepic[2][2], 5);
        tracker.removeImage(alcovepic[2][3], 5);
        tracker.removeImage(alcovepic[3][1], 5);
        tracker.removeImage(alcovepic[3][2], 5);
        tracker.removeImage(alcovepic[3][3], 5);
    }
    
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if ((row == 0 && col == 2) || (row < 4 && (col == 0 || col == 4))) return;
        if (hasMons && col == 2 && row == 1 && dmnew.magicvision > 0) {
            drawContents(g, 3 - row, col - 1);
        } else if (col == 3) xc -= alcovepic[row][3].getWidth(null);
        g.drawImage(alcovepic[row][col], xc, yc, obs);
        if (row == 0 || col == 0 || col == 4 || (row == 1 && col != 2)) return;
        
        Iterator iter;
        switch (dmnew.facing) {
            case dmnew.NORTH:
                if (southside.isEmpty()) return;
                iter = southside.iterator();
                break;
            case dmnew.SOUTH:
                if (northside.isEmpty()) return;
                iter = northside.iterator();
                break;
            case dmnew.EAST:
                if (westside.isEmpty()) return;
                iter = westside.iterator();
                break;
            case dmnew.WEST:
            default:
                if (eastside.isEmpty()) return;
                iter = eastside.iterator();
                break;
        }
        int dist = 3 - row;
        int coldist = 0;
        if (col == 1) {
            if (row == 2) coldist = -58;
            else coldist = -12;
        } else if (col == 3) {
            if (row == 2) coldist = 58;
            else coldist = 12;
        }
        int drawx, drawy;
        Item tempitem;
        int widthscale, heightscale, xoffsetscale, yoffsetscale;
        float scale, picwidth, picheight;
        
        if (row == 3) darkfilt.setDarks(3);
        else if (row == 2) darkfilt.setDarks(2);
        else if (row == 1) darkfilt.setDarks(1);
        
        while (iter.hasNext()) {
            tempitem = (Item) iter.next();
            //temppic = Item.darkpic[tempitem.number][3-row];
            temppic = (Image) Item.pics.get(tempitem.dpicstring + "-d" + (3 - row));
            if (temppic == null) {
                temppic = dmnew.tk.createImage(new FilteredImageSource(tempitem.dpic.getSource(), darkfilt));
                tracker.addImage(temppic, 0);
                try {
                    tracker.waitForID(0);
                } catch (InterruptedException e) {
                }
                tracker.removeImage(temppic);
                //Item.darkpic[tempitem.number][3-row]=temppic;
                Item.pics.put(tempitem.dpicstring + "-d" + (3 - row), temppic);
            }
            picwidth = (float) temppic.getWidth(null);
            picheight = (float) temppic.getHeight(null);
            //scale = 2.4f-((float)dist*.6f);
            scale = 2.3f - ((float) dist * .6f);
            widthscale = (int) (picwidth / scale);
            heightscale = (int) (picheight / scale);
            xoffsetscale = (int) ((float) tempitem.xoffset / scale);
            yoffsetscale = (int) ((float) tempitem.yoffset / scale);
            drawx = xc + alcovepic[row][col].getWidth(null) / 2 - widthscale / 2 + xoffsetscale + coldist;
            drawy = yc + alcovepic[row][col].getHeight(null) / 2 + (29 + dist * 15) - heightscale + yoffsetscale;
            if (tempitem.number > 199 && tempitem.number < 221) {
                if (row == 3) drawy += 2;
                    //else if (row==2) drawy-=2;
                else if (row == 1) drawy += 8;
            } else if (row == 2) drawy -= 4;
            else if (row == 1) drawy += 4;
            g.drawImage(temppic, drawx, drawy, widthscale, heightscale, obs);
        }
    }
    
    public void tryWallSwitch(int x, int y) {
        //get an item
        if (x < 137 || x > 308 || y < 150 || y > 230) return;
        switch (dmnew.facing) {
            case dmnew.NORTH:
                if (!southside.isEmpty()) {
                    dmnew.inhand = (Item) southside.get(southside.size() - 1);
                    southside.remove(southside.size() - 1);
                    dmnew.iteminhand = true;
                    dmnew.hero[dmnew.leader].load += dmnew.inhand.weight;
                    if (isSwitch) alcoveswitch.tryWallSwitch(dmnew.inhand);
                    dmnew.needredraw = true;
                }
                break;
            case dmnew.SOUTH:
                if (!northside.isEmpty()) {
                    dmnew.inhand = (Item) northside.get(northside.size() - 1);
                    northside.remove(northside.size() - 1);
                    dmnew.iteminhand = true;
                    dmnew.hero[dmnew.leader].load += dmnew.inhand.weight;
                    if (isSwitch) alcoveswitch.tryWallSwitch(dmnew.inhand);
                    dmnew.needredraw = true;
                }
                break;
            case dmnew.EAST:
                if (!westside.isEmpty()) {
                    dmnew.inhand = (Item) westside.get(westside.size() - 1);
                    westside.remove(westside.size() - 1);
                    dmnew.iteminhand = true;
                    dmnew.hero[dmnew.leader].load += dmnew.inhand.weight;
                    if (isSwitch) alcoveswitch.tryWallSwitch(dmnew.inhand);
                    dmnew.needredraw = true;
                }
                break;
            case dmnew.WEST:
                if (!eastside.isEmpty()) {
                    dmnew.inhand = (Item) eastside.get(eastside.size() - 1);
                    eastside.remove(eastside.size() - 1);
                    dmnew.iteminhand = true;
                    dmnew.hero[dmnew.leader].load += dmnew.inhand.weight;
                    if (isSwitch) alcoveswitch.tryWallSwitch(dmnew.inhand);
                    dmnew.needredraw = true;
                }
                break;
        }
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        //put an item
        if (x < 137 || x > 308 || y < 150 || y > 230) return true;
        if (isSwitch && !alcoveswitch.tryWallSwitch(it)) return false;
        switch (dmnew.facing) {
            case dmnew.NORTH:
                southside.add(it);
                break;
            case dmnew.SOUTH:
                northside.add(it);
                break;
            case dmnew.EAST:
                westside.add(it);
                break;
            case dmnew.WEST:
                eastside.add(it);
                break;
        }
        dmnew.needredraw = true;
        return false;
    }
    
    public void addItem(Item it) {
        addItem(it, dmnew.randGen.nextInt(4));
    }
    
    public void addItem(Item it, int side) {
        switch (side) {
            case dmnew.NORTH:
                if (isSwitch && !alcoveswitch.tryWallSwitch(it, 2)) {
                } else northside.add(it);
                break;
            case dmnew.WEST:
                if (isSwitch && !alcoveswitch.tryWallSwitch(it, 3)) {
                } else westside.add(it);
                break;
            case dmnew.SOUTH:
                if (isSwitch && !alcoveswitch.tryWallSwitch(it, 0)) {
                } else southside.add(it);
                break;
            default:
                if (isSwitch && !alcoveswitch.tryWallSwitch(it, 1)) {
                } else eastside.add(it);
                break;
        }
    }
    
    public boolean changeState() {
        if (alcoveswitch != null) return alcoveswitch.changeState();
        else return false;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(northside);
        so.writeObject(southside);
        so.writeObject(eastside);
        so.writeObject(westside);
        so.writeBoolean(isSwitch);
        if (isSwitch) alcoveswitch.save(so);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        northside = (ArrayList) si.readObject();
        southside = (ArrayList) si.readObject();
        eastside = (ArrayList) si.readObject();
        westside = (ArrayList) si.readObject();
        isSwitch = si.readBoolean();
        if (isSwitch) alcoveswitch = (MultWallSwitch2) dmnew.loadMapObject(si);
    }
    
}
