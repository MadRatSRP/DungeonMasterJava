import java.awt.*;
import java.awt.image.ImageObserver;

class Floor extends MapObject {
    
    public Floor() {
        super();
        mapchar = '0';
        isPassable = true;
        canPassProjs = true;
        canHoldItems = true;
        drawItems = true;
        drawFurtherItems = true;
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //[ ][ ][ ][ ][ ]  2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        //test for pit above - requires floor to know own xcoord,ycoord
        //if (dmnew.level>0 && (dmnew.DungeonMap[dmnew.level-1][xcoord][ycoord] instanceof Pit) && ((Pit)dmnew.DungeonMap[dmnew.level-1][xcoord][ycoord]).isOpen) {
        //draw hole in ceiling
        //}
        if (col > 0 && col < 4) drawContents(g, 3 - row, col - 1);
    }
    
}
