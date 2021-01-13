import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

class OneAlcove extends SidedWall {
    
    static public boolean ADDEDPICS;
    //private Vector holding = new Vector();
    private boolean isSwitch = false;
    private MultWallSwitch2 alcoveswitch;
    
    public OneAlcove(int sde) {
        super(sde);
        //canPassMons = false;
        mapchar = ']';
        setPics();
    }
    
    public OneAlcove(int sde, MultWallSwitch2 fs) {
        super(sde);
        mapchar = ']';
        //canPassMons = false;
        alcoveswitch = fs;
        isSwitch = true;
        setPics();
    }
    
    protected void setPics() {
        super.setPics();
        //comp = new JPanel();
        //comp.setMinimumSize(new Dimension(0,0));
        //comp.setMaximumSize(new Dimension(0,0));
        //comp.setPreferredSize(new Dimension(0,0));
        //comp.setSize(0,0);
        //dmnew.mapcomp.add(comp);
        facingside[1][2] = loadPic("alcove12.gif");
        facingside[2][1] = loadPic("onealcove21.gif");
        facingside[2][2] = loadPic("alcove22.gif");
        facingside[2][3] = loadPic("onealcove23.gif");
        facingside[3][1] = loadPic("onealcove31.gif");
        facingside[3][2] = loadPic("alcove32.gif");
        facingside[3][3] = loadPic("onealcove33.gif");
        col1pic[1] = loadPic("alcove11.gif");
        col1pic[2] = loadPic("onealcovecol12.gif");
        col1pic[3] = loadPic("onealcovecol13.gif");
        col3pic[1] = loadPic("alcove13.gif");
        col3pic[2] = loadPic("onealcovecol32.gif");
        col3pic[3] = loadPic("onealcovecol33.gif");
        if (!ADDEDPICS) {
            tracker.addImage(facingside[1][2], 0);
            tracker.addImage(facingside[2][1], 0);
            tracker.addImage(facingside[2][2], 0);
            tracker.addImage(facingside[2][3], 0);
            tracker.addImage(facingside[3][1], 0);
            tracker.addImage(facingside[3][2], 0);
            tracker.addImage(facingside[3][3], 0);
            tracker.addImage(col1pic[1], 0);
            tracker.addImage(col1pic[2], 0);
            tracker.addImage(col1pic[3], 0);
            tracker.addImage(col3pic[1], 0);
            tracker.addImage(col3pic[2], 0);
            tracker.addImage(col3pic[3], 0);
            ADDEDPICS = true;
        }
    }
    
    public void redoAlcovePics() {
        String newmapdir = Wall.currentdir + File.separator;
        File testfile = new File(newmapdir + "alcove12.gif");
        if (!testfile.exists()) return;
        redoSidedPics();
        facingside[1][2] = dmnew.tk.getImage(newmapdir + "alcove12.gif");
        facingside[2][1] = dmnew.tk.getImage(newmapdir + "onealcove21.gif");
        facingside[2][2] = dmnew.tk.getImage(newmapdir + "alcove22.gif");
        facingside[2][3] = dmnew.tk.getImage(newmapdir + "onealcove23.gif");
        facingside[3][1] = dmnew.tk.getImage(newmapdir + "onealcove31.gif");
        facingside[3][2] = dmnew.tk.getImage(newmapdir + "alcove32.gif");
        facingside[3][3] = dmnew.tk.getImage(newmapdir + "onealcove33.gif");
        col1pic[1] = dmnew.tk.getImage(newmapdir + "alcove11.gif");
        col1pic[2] = dmnew.tk.getImage(newmapdir + "onealcovecol12.gif");
        col1pic[3] = dmnew.tk.getImage(newmapdir + "onealcovecol13.gif");
        col3pic[1] = dmnew.tk.getImage(newmapdir + "alcove13.gif");
        col3pic[2] = dmnew.tk.getImage(newmapdir + "onealcovecol32.gif");
        col3pic[3] = dmnew.tk.getImage(newmapdir + "onealcovecol33.gif");
        
        tracker.addImage(facingside[1][2], 5);
        tracker.addImage(facingside[2][1], 5);
        tracker.addImage(facingside[2][2], 5);
        tracker.addImage(facingside[2][3], 5);
        tracker.addImage(facingside[3][1], 5);
        tracker.addImage(facingside[3][2], 5);
        tracker.addImage(facingside[3][3], 5);
        tracker.addImage(col1pic[1], 5);
        tracker.addImage(col1pic[2], 5);
        tracker.addImage(col1pic[3], 5);
        tracker.addImage(col3pic[1], 5);
        tracker.addImage(col3pic[2], 5);
        tracker.addImage(col3pic[3], 5);
        
        try {
            tracker.waitForID(5, 2000);
        } catch (InterruptedException ex) {
        }
        
        tracker.removeImage(facingside[1][2], 5);
        tracker.removeImage(facingside[2][1], 5);
        tracker.removeImage(facingside[2][2], 5);
        tracker.removeImage(facingside[2][3], 5);
        tracker.removeImage(facingside[3][1], 5);
        tracker.removeImage(facingside[3][2], 5);
        tracker.removeImage(facingside[3][3], 5);
        tracker.removeImage(col1pic[1], 5);
        tracker.removeImage(col1pic[2], 5);
        tracker.removeImage(col1pic[3], 5);
        tracker.removeImage(col3pic[1], 5);
        tracker.removeImage(col3pic[2], 5);
        tracker.removeImage(col3pic[3], 5);
    }
    
    public void doAction() {
        if (dmnew.leveldir[dmnew.level] != null) redoAlcovePics();
    }
    
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        super.drawPic(row, col, xc, yc, g, obs);
        if (row == 0 || col == 0 || col == 4 || (row == 1 && col != 2) || dmnew.facing != side || !hasItems) return;
        
        if (col == 3) xc = xc - facingside[row][3].getWidth(null);
        
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
        
        if (hasItems) {
            Iterator i = mapItems.iterator();
            while (i.hasNext()) {
                tempitem = (Item) i.next();
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
                //if (row==1) scale-=0.1f;
                widthscale = (int) (picwidth / scale);
                heightscale = (int) (picheight / scale);
                xoffsetscale = (int) ((float) tempitem.xoffset / scale);
                yoffsetscale = (int) ((float) tempitem.yoffset / scale);
                drawx = xc + facingside[row][col].getWidth(null) / 2 - widthscale / 2 + xoffsetscale + coldist;
                drawy = yc + facingside[row][col].getHeight(null) / 2 + (29 + dist * 15) - heightscale + yoffsetscale;
                if (tempitem.number > 199 && tempitem.number < 221) {
                    if (row == 3) drawy += 2;
                        //else if (row==2) drawy-=2;
                    else if (row == 1) drawy += 8;
                } else if (row == 2) drawy -= 4;
                else if (row == 1) drawy += 4;
                //if (row==2) drawy-=4;
                //else if (row==1) drawy+=4;
                g.drawImage(temppic, drawx, drawy, widthscale, heightscale, obs);
            }
        }
    }
    
    public void tryWallSwitch(int x, int y) {
        //get an item
        if (x < 137 || x > 308 || y < 150 || y > 230 || dmnew.facing != side || !hasItems) return;
        if (isSwitch) alcoveswitch.tryWallSwitch((Item) mapItems.get(mapItems.size() - 1));
        dmnew.inhand = (Item) mapItems.remove(mapItems.size() - 1);
        //mapItems.remove(mapItems.size()-1);
        if (mapItems.size() == 0) hasItems = false;
        dmnew.iteminhand = true;
        dmnew.hero[dmnew.leader].load += dmnew.inhand.weight;
        dmnew.needredraw = true;
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        //put an item
        if (x < 137 || x > 308 || y < 150 || y > 230 || dmnew.facing != side) return true;
        if (isSwitch && !alcoveswitch.tryWallSwitch(it)) return false;
        if (mapItems == null) mapItems = new ArrayList(4);
        mapItems.add(it);
        hasItems = true;
        dmnew.needredraw = true;
        return false;
    }
    
    public void addItem(Item it) {
        if (isSwitch && !alcoveswitch.tryWallSwitch(it, side)) return;
        if (mapItems == null) mapItems = new ArrayList(4);
        mapItems.add(it);
        hasItems = true;
    }
    
    public boolean changeState() {
        if (alcoveswitch != null) return alcoveswitch.changeState();
        else return false;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(isSwitch);
        if (isSwitch) alcoveswitch.save(so);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        isSwitch = si.readBoolean();
        if (isSwitch) alcoveswitch = (MultWallSwitch2) dmnew.loadMapObject(si);
    }
}
