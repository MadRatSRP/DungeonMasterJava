import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectOutputStream;

class Stormbringer extends Floor {
    
    static public boolean ADDEDPICS = false;
    private boolean wasUsed = false;
    private Image[] usedpic = new Image[3];
    private Item storm;
    
    public Stormbringer(boolean wasUsed) {
        //super(0);
        super();
        isPassable = false;
        canPassMons = false;
        canPassImmaterial = false;
        mapchar = '!';
        this.wasUsed = wasUsed;
        if (!wasUsed) storm = new Item(215);
        setPics();
    }
    
    public void tryWallSwitch(int x, int y) {
        //get stormbringer
        if (wasUsed || x < 137 || x > 308 || y < 80 || y > 230) return;
        wasUsed = true;
        //System.out.println("got here 1");
        dmnew.inhand = storm;//new Item(215);
        //System.out.println("got here 2");
        dmnew.iteminhand = true;
        dmnew.hero[dmnew.leader].load += dmnew.inhand.weight;
        //System.out.println("got here 3");
        dmnew.needredraw = true;
        //System.out.println("got here 4");
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        //put back stormbringer
        if (!wasUsed || x < 137 || x > 308 || y < 80 || y > 230 || it.number != 215) return true;
        wasUsed = false;
        storm = it;
                /*
                dmnew.inhand = new Item(215);
                dmnew.iteminhand = true;
                dmnew.hero[dmnew.leader].load+=dmnew.inhand.weight;
                */
        dmnew.needredraw = true;
        return false;
    }
    
    protected void setPics() {
        pic = new Image[3][1];
        pic[0][0] = loadPic("stormbringer1.gif");
        pic[1][0] = loadPic("stormbringer2.gif");
        pic[2][0] = loadPic("stormbringer3.gif");
        usedpic[0] = loadPic("stormbringer-u1.gif");
        usedpic[1] = loadPic("stormbringer-u2.gif");
        usedpic[2] = loadPic("stormbringer-u3.gif");
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
            if (col == 2) g.drawImage(temppic, 183, 70, obs);
            else if (col == 1) g.drawImage(temppic, -50, 70, obs);
            else g.drawImage(temppic, 419, 70, obs);
        } else if (row == 2) {
            if (!wasUsed) temppic = pic[1][0];
            else temppic = usedpic[1];
            if (col == 2) g.drawImage(temppic, 199, 89, obs);
            else if (col == 1) g.drawImage(temppic, 30, 89, obs);
            else g.drawImage(temppic, 370, 89, obs);
        } else if (row == 3) {
            if (!wasUsed) temppic = pic[2][0];
            else temppic = usedpic[2];
            if (col == 2) g.drawImage(temppic, 205, 90, obs);
            else if (col == 1) g.drawImage(temppic, 70, 90, obs);
            else g.drawImage(temppic, 342, 90, obs);//448-36-70
        }
        //if (col>0 && col<4) drawContents(g,3-row,col-1);
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(wasUsed);
    }
}