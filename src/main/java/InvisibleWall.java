import java.awt.*;
import java.awt.image.ImageObserver;

class InvisibleWall extends Wall {
    
    public InvisibleWall() {
        super();
        mapchar = 'i';
        canPassMons = false;
        canPassImmaterial = true;
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (dmnew.dispell > 0) {
            if (col == 3) xc = xc - wallpic[row][3].getWidth(null);
            else if (hasMons && col == 2 && row == 1 && dmnew.magicvision > 0) {
                drawContents(g, 3 - row, col - 1);
            }
            if (dmnew.mirrorback) dmnew.dview.offg2.drawImage(wallpic[row][col], xc, yc, obs);
            else dmnew.dview.offg2.drawImage(altpic[row][col], xc, yc, obs);
        } else if (hasMons && row > 0 && col > 0 && col < 4) {
            drawContents(g, 3 - row, col - 1);
        }
    }
    
}
