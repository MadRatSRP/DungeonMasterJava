import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectOutputStream;

class PowerGem extends Floor {
    
    static public boolean ADDEDPICS = false;
    private boolean wasUsed = false;
    private Image[] usedpic = new Image[3];
    
    public PowerGem(boolean wasUsed) {
        //super(0);
        super();
        isPassable = false;
        canPassMons = false;
        canPassImmaterial = false;
        mapchar = 'G';
        this.wasUsed = wasUsed;
        setPics();
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        //attach gem to firestaff
        if (wasUsed || x < 170 || x > 280 || y < 90 || y > 150 || it.number != 248) return true;
        wasUsed = true;
        dmnew.hero[dmnew.leader].load -= dmnew.inhand.weight;
        dmnew.inhand = new Item(249);
        dmnew.hero[dmnew.leader].load += dmnew.inhand.weight;
        dmnew.needredraw = true;
        return true;
    }
    
    protected void setPics() {
        pic = new Image[3][1];
        pic[0][0] = loadPic("powergem1.png");
        pic[1][0] = loadPic("powergem2.png");
        pic[2][0] = loadPic("powergem3.png");
        usedpic[0] = loadPic("powergem-u1.gif");
        usedpic[1] = loadPic("powergem-u2.gif");
        usedpic[2] = loadPic("powergem-u3.gif");
        if (!ADDEDPICS) {
            tracker.addImage(pic[0][0], 0);
            tracker.addImage(pic[1][0], 0);
            tracker.addImage(pic[2][0], 0);
            tracker.addImage(usedpic[0], 0);
            tracker.addImage(usedpic[1], 0);
            tracker.addImage(usedpic[2], 0);
            ADDEDPICS = true;
        }
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (row == 0 || col == 0 || col == 4) {
            //if (col>0 && col<4) drawContents(g,3-row,col-1);
            return;
        }
        Image temppic;
        if (row == 1) {
            if (!wasUsed) temppic = pic[0][0];
            else temppic = usedpic[0];
            if (col == 2) g.drawImage(temppic, 176, 100, obs);
            else if (col == 1) g.drawImage(temppic, -50, 100, obs);
            else g.drawImage(temppic, 409, 100, obs);
        } else if (row == 2) {
            if (!wasUsed) temppic = pic[1][0];
            else temppic = usedpic[1];
            if (col == 2) g.drawImage(temppic, 193, 100, obs);
            else if (col == 1) g.drawImage(temppic, 30, 100, obs);
            else g.drawImage(temppic, 364, 100, obs);
        } else if (row == 3) {
            if (!wasUsed) temppic = pic[2][0];
            else temppic = usedpic[2];
            if (col == 2) g.drawImage(temppic, 202, 100, obs);
            else if (col == 1) g.drawImage(temppic, 70, 100, obs);
            else g.drawImage(temppic, 338, 100, obs);
        }
        //if (col>0 && col<4) drawContents(g,3-row,col-1);
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(wasUsed);
    }
}