import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectOutputStream;

class SidedWall2 extends Wall {
    protected int side; //dir party must face to see special directly
    protected int[] xadjust, yadjust;
    protected Image[] facingside;
    protected Image[] col1pic;
    protected Image[] col3pic;
    
    public SidedWall2(int sde) {
        super(0);//wall constructor that doen't call setpics
        side = sde;
        xadjust = new int[9];
        yadjust = new int[9];
        for (int i = 0; i < 9; i++) {
            xadjust[i] = 0;
            yadjust[i] = 0;
        }
        facingside = new Image[3];
        col1pic = new Image[3];
        col3pic = new Image[3];
    }
    
    protected void setPics() {
        super.setPics();
        //facingside = new Image[3];
        //col1pic = new Image[3];
        //col3pic = new Image[3];
        for (int i = 0; i < 3; i++) {
            col1pic[i] = blankpic;
            col3pic[i] = blankpic;
            facingside[i] = blankpic;
        }
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //   [ ][ ][ ]     2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        super.drawPic(row, col, xc, yc, g, obs);
        if (col == 0 || col == 4 || row == 0) return;
        if (col == 3) xc = xc - wallpic[row][col].getWidth(obs);
        
        if (dmnew.facing == side) {
            if (row == 1 && col != 2) return;
            //col correction:
            if (row == 2 && col == 1) xc -= 90;
            else if (row == 2 && col == 3) xc += 34;
            else if (row == 3 && col == 1) xc -= 4;
            else if (row == 3 && col == 3) xc += 23;//was 24
            g.drawImage(facingside[row - 1], xc + xadjust[row - 1], yc + yadjust[row - 1], obs);
            return;
        }
        
        int testface = side - 1;
        if (testface < 0) testface = 3;
        if (dmnew.facing == testface && col == 1) {
            g.drawImage(col1pic[row - 1], xc + xadjust[row + 2], yc + yadjust[row + 2], obs);
            return;
        }
        
        testface = side + 1;
        if (testface > 3) testface = 0;
        if (dmnew.facing == testface && col == 3) {
            g.drawImage(col3pic[row - 1], xc + xadjust[row + 5], yc + yadjust[row + 5], obs);
            return;
        }
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(side);
    }
    
}
