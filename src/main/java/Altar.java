import java.awt.*;
import java.awt.image.ImageObserver;

class Altar extends OneAlcove {
    
    static public boolean ADDEDPICS;
    static protected Image[][] altarpic = new Image[5][3];
    
    public Altar(int sde) {
        super(sde);
        mapchar = 'a';
        setPics();
    }
    
    public Altar(int sde, MultWallSwitch2 fs) {
        super(sde, fs);
        mapchar = 'a';
        setPics();
    }
    
    protected void setPics() {
        super.setPics();
        if (!ADDEDPICS) {
            altarpic[0][1] = dmnew.tk.createImage(mapdir + "altar12.gif");
            altarpic[1][0] = dmnew.tk.createImage(mapdir + "altar21.gif");
            altarpic[1][1] = dmnew.tk.createImage(mapdir + "altar22.gif");
            altarpic[1][2] = dmnew.tk.createImage(mapdir + "altar23.gif");
            altarpic[2][0] = dmnew.tk.createImage(mapdir + "altar31.gif");
            altarpic[2][1] = dmnew.tk.createImage(mapdir + "altar32.gif");
            altarpic[2][2] = dmnew.tk.createImage(mapdir + "altar33.gif");
            altarpic[3][0] = dmnew.tk.createImage(mapdir + "altarcol11.gif");
            altarpic[3][1] = dmnew.tk.createImage(mapdir + "altarcol12.gif");
            altarpic[3][2] = dmnew.tk.createImage(mapdir + "altarcol13.gif");
            altarpic[4][0] = dmnew.tk.createImage(mapdir + "altarcol31.gif");
            altarpic[4][1] = dmnew.tk.createImage(mapdir + "altarcol32.gif");
            altarpic[4][2] = dmnew.tk.createImage(mapdir + "altarcol33.gif");
            
            tracker.addImage(altarpic[0][1], 0);
            tracker.addImage(altarpic[1][0], 0);
            tracker.addImage(altarpic[1][1], 0);
            tracker.addImage(altarpic[1][2], 0);
            tracker.addImage(altarpic[2][0], 0);
            tracker.addImage(altarpic[2][1], 0);
            tracker.addImage(altarpic[2][2], 0);
            tracker.addImage(altarpic[3][0], 0);
            tracker.addImage(altarpic[3][1], 0);
            tracker.addImage(altarpic[3][2], 0);
            tracker.addImage(altarpic[4][0], 0);
            tracker.addImage(altarpic[4][1], 0);
            tracker.addImage(altarpic[4][2], 0);
            ADDEDPICS = true;
        }
        facingside[1][2] = altarpic[0][1];
        facingside[2][1] = altarpic[1][0];
        facingside[2][2] = altarpic[1][1];
        facingside[2][3] = altarpic[1][2];
        facingside[3][1] = altarpic[2][0];
        facingside[3][2] = altarpic[2][1];
        facingside[3][3] = altarpic[2][2];
        col1pic[1] = altarpic[3][0];
        col1pic[2] = altarpic[3][1];
        col1pic[3] = altarpic[3][2];
        col3pic[1] = altarpic[4][0];
        col3pic[2] = altarpic[4][1];
        col3pic[3] = altarpic[4][2];
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        if (x < 137 || x > 308 || y < 150 || y > 220 || dmnew.facing != side) return true;
        else if (it.number == 75) {
            dmnew.message.setMessage("No Soul...", 4);//item bones, not party bones
            //return true; //don't let them be put in it
            return super.tryWallSwitch(x, y, it);
        } else if (it.number > 3) return super.tryWallSwitch(x, y, it);
            //so party bones can trigger too
        else {
            int oldsize = 0;
            if (hasItems) oldsize = mapItems.size();
            super.tryWallSwitch(x, y, it);
            //if not destroyed by switch, destroy now
            if (hasItems && mapItems.size() > oldsize) {
                mapItems.remove(mapItems.size() - 1);
                if (mapItems.size() == 0) hasItems = false;
            }
        }
        dmnew.bonesflag = it.number;
        return false; //bones disappear
        //put anim here? or in dmnew?
    }
    
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        super.drawPic(row, col, xc, yc, g, obs);
        if (col == 2 && row == 1 && dmnew.bonesflag > -1) {
            //draw anim
            //tracker.addImage(dmnew.altaranim,8);
            try {
                tracker.waitForID(8, 10000);
            } catch (InterruptedException e) {
            }
            //tracker.removeImage(dmnew.altaranim,8);
            g.drawImage(dmnew.altaranim, xc + 72, yc + 67, obs);
        }
    }
}
