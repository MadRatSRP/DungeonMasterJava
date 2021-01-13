import java.awt.*;
import java.awt.image.ImageObserver;

class FakeWall extends Wall {
    
    public FakeWall() {
        super();
        mapchar = '2';
        isPassable = true;
        canPassProjs = true;
        canHoldItems = true;
        canPassMons = true;
        canPassImmaterial = true;
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (hasParty || dmnew.dispell > 0) {
            drawItems = true;
            drawFurtherItems = true;
            if (col > 0 && col < 4) drawContents(g, 3 - row, col - 1);
        } else {
            if (col == 2 && row == 1 && dmnew.magicvision > 0) {
                drawItems = true;
                drawFurtherItems = true;
                drawContents(g, 3 - row, col - 1);
                if (dmnew.mirrorback) g.drawImage(wallpic[row][col], xc, yc, obs); //wall pic
                else g.drawImage(altpic[row][col], xc, yc, obs); //alt wall pic
            } else {
                drawItems = false;
                drawFurtherItems = false;
                if (col == 3) xc -= wallpic[row][3].getWidth(null);
                if (dmnew.mirrorback) g.drawImage(wallpic[row][col], xc, yc, obs); //wall pic
                else g.drawImage(altpic[row][col], xc, yc, obs); //alt wall pic
            }
        }
    }
    
    public void tryWallSwitch(int x, int y) {
        dmnew.playSound("swing.wav", -1, -1);
    }
}
