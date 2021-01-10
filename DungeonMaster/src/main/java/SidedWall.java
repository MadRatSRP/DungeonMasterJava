import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectOutputStream;

class SidedWall extends Wall {
    public int side; //dir party must face to see special directly
    protected Image[][] facingside;
    protected Image[] col1pic;
    protected Image[] col3pic;
    
    public SidedWall(int sde) {
        super(0);//wall constructor that doen't call setpics
        side = sde;
        facingside = new Image[4][5];
        col1pic = new Image[4];
        col3pic = new Image[4];
    }
    
    protected void setPics() {
        super.setPics();
        facingside = new Image[4][5];
        col1pic = new Image[4];
        col3pic = new Image[4];
        for (int i = 0; i < 4; i++) {
            col1pic[i] = wallpic[i][1];
            col3pic[i] = wallpic[i][3];
            for (int j = 0; j < 5; j++) facingside[i][j] = wallpic[i][j];
        }
    }
    
    public void redoSidedPics() {
        for (int i = 0; i < 4; i++) {
            col1pic[i] = wallpic[i][1];
            col3pic[i] = wallpic[i][3];
            for (int j = 0; j < 5; j++) facingside[i][j] = wallpic[i][j];
        }
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //   [ ][ ][ ]     2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (hasMons && col == 2 && row == 1 && dmnew.magicvision > 0) {
            drawContents(g, 3 - row, col - 1);
        }
        if (dmnew.facing == side) {
            if (col == 3) xc = xc - facingside[row][col].getWidth(null);
            if (facingside[row][col] != wallpic[row][col]) g.drawImage(facingside[row][col], xc, yc, obs);
            else if (dmnew.mirrorback) g.drawImage(wallpic[row][col], xc, yc, obs);
            else g.drawImage(altpic[row][col], xc, yc, obs);
            return;
        }
        
        int testface = side - 1;
        if (testface < 0) testface = 3;
        if (dmnew.facing == testface && col == 1) {
            if (row != 0 || dmnew.mirrorback) g.drawImage(col1pic[row], xc, yc, obs);
            else if (this instanceof Stairs)
                g.drawImage(col1pic[0], xc, yc, obs); //if too slow, could override this whole method in stairs class
            else g.drawImage(altpic[0][1], xc, yc, obs);
            return;
        }
        
        testface = side + 1;
        if (testface > 3) testface = 0;
        if (dmnew.facing == testface && col == 3) {
            xc = xc - col3pic[row].getWidth(null);
            if (row != 0 || dmnew.mirrorback) g.drawImage(col3pic[row], xc, yc, obs);
            else if (this instanceof Stairs) g.drawImage(col3pic[0], xc, yc, obs); //see above
            else g.drawImage(altpic[0][3], xc, yc, obs);
            return;
        }
        
        if (col == 3) xc = xc - wallpic[row][3].getWidth(null);
        if (dmnew.mirrorback) g.drawImage(wallpic[row][col], xc, yc, obs);
        else g.drawImage(altpic[row][col], xc, yc, obs);
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(side);
    }
}
