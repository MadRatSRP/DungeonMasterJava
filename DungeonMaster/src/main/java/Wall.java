import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;

class Wall extends MapObject {
    
    static protected java.awt.Image[][] wallpic = new java.awt.Image[4][5];
    static protected java.awt.Image[][] altpic = new java.awt.Image[4][5];
    static public boolean ADDEDPICS;
    static public String currentdir = "Maps";
    
    public Wall() {
        super();
        mapchar = '1';
        canPassMons = false;
        canPassImmaterial = true;
        setPics();
    }
    
    //don't set pics - used for sidedwalls
    public Wall(int n) {
        super();
        mapchar = '1';
        canPassMons = false;
        canPassImmaterial = true;
    }
    
    protected void setPics() {
        
        if (!ADDEDPICS) {
                        /*
                        wallpic[0][1] = dmnew.tk.createImage(mapdir+"wall01.gif");
                        wallpic[0][3] = dmnew.tk.createImage(mapdir+"wall03.gif");
                        wallpic[1][1] = dmnew.tk.createImage(mapdir+"wall11.gif");
                        wallpic[1][3] = dmnew.tk.createImage(mapdir+"wall13.gif");
                        wallpic[2][1] = dmnew.tk.createImage(mapdir+"wall21.gif");
                        wallpic[2][3] = dmnew.tk.createImage(mapdir+"wall23.gif");
                        wallpic[1][2] = dmnew.tk.createImage(mapdir+"wall12.gif");
                        wallpic[2][2] = dmnew.tk.createImage(mapdir+"wall22.gif");
                        wallpic[3][0] = dmnew.tk.createImage(mapdir+"wall30.gif");
                        wallpic[3][1] = dmnew.tk.createImage(mapdir+"wall31.gif");
                        wallpic[3][2] = dmnew.tk.createImage(mapdir+"wall32.gif");
                        wallpic[3][3] = dmnew.tk.createImage(mapdir+"wall33.gif");
                        wallpic[3][4] = dmnew.tk.createImage(mapdir+"wall34.gif");
                        
                        tracker.addImage(wallpic[0][1],0);
                        tracker.addImage(wallpic[0][3],0);
                        tracker.addImage(wallpic[1][1],0);
                        tracker.addImage(wallpic[1][2],0);
                        tracker.addImage(wallpic[1][3],0);
                        tracker.addImage(wallpic[2][1],0);
                        tracker.addImage(wallpic[2][2],0);
                        tracker.addImage(wallpic[2][3],0);
                        tracker.addImage(wallpic[3][0],0);
                        tracker.addImage(wallpic[3][1],0);
                        tracker.addImage(wallpic[3][2],0);
                        tracker.addImage(wallpic[3][3],0);
                        tracker.addImage(wallpic[3][4],0);

                        altpic[0][1] = dmnew.tk.createImage(mapdir+"altwall01.gif");
                        altpic[0][3] = dmnew.tk.createImage(mapdir+"altwall03.gif");
                        altpic[1][1] = dmnew.tk.createImage(mapdir+"altwall11.gif");
                        altpic[1][3] = dmnew.tk.createImage(mapdir+"altwall13.gif");
                        altpic[2][1] = dmnew.tk.createImage(mapdir+"altwall21.gif");
                        altpic[2][3] = dmnew.tk.createImage(mapdir+"altwall23.gif");
                        altpic[1][2] = dmnew.tk.createImage(mapdir+"altwall12.gif");
                        altpic[2][2] = dmnew.tk.createImage(mapdir+"altwall22.gif");
                        altpic[3][1] = dmnew.tk.createImage(mapdir+"altwall31.gif");
                        altpic[3][2] = dmnew.tk.createImage(mapdir+"altwall32.gif");
                        altpic[3][3] = dmnew.tk.createImage(mapdir+"altwall33.gif");
                        altpic[3][0]=wallpic[3][0];
                        altpic[3][4]=wallpic[3][4];
        
                        tracker.addImage(altpic[0][1],0);
                        tracker.addImage(altpic[0][3],0);
                        tracker.addImage(altpic[1][1],0);
                        tracker.addImage(altpic[1][2],0);
                        tracker.addImage(altpic[1][3],0);
                        tracker.addImage(altpic[2][1],0);
                        tracker.addImage(altpic[2][2],0);
                        tracker.addImage(altpic[2][3],0);
                        tracker.addImage(altpic[3][1],0);
                        tracker.addImage(altpic[3][2],0);
                        tracker.addImage(altpic[3][3],0);
                        */
            redoPics();
            ADDEDPICS = true;
        }
        
    }
    
    public static void redoPics() {
        String newmapdir = currentdir + File.separator;
        File testfile = new File(newmapdir + "wall12.gif");
        if (!testfile.exists()) return;
        wallpic[0][1] = dmnew.tk.getImage(newmapdir + "wall01.gif");
        wallpic[0][3] = dmnew.tk.getImage(newmapdir + "wall03.gif");
        wallpic[1][1] = dmnew.tk.getImage(newmapdir + "wall11.gif");
        wallpic[1][3] = dmnew.tk.getImage(newmapdir + "wall13.gif");
        wallpic[2][1] = dmnew.tk.getImage(newmapdir + "wall21.gif");
        wallpic[2][3] = dmnew.tk.getImage(newmapdir + "wall23.gif");
        wallpic[1][2] = dmnew.tk.getImage(newmapdir + "wall12.gif");
        wallpic[2][2] = dmnew.tk.getImage(newmapdir + "wall22.gif");
        wallpic[3][0] = dmnew.tk.getImage(newmapdir + "wall30.gif");
        wallpic[3][1] = dmnew.tk.getImage(newmapdir + "wall31.gif");
        wallpic[3][2] = dmnew.tk.getImage(newmapdir + "wall32.gif");
        wallpic[3][3] = dmnew.tk.getImage(newmapdir + "wall33.gif");
        wallpic[3][4] = dmnew.tk.getImage(newmapdir + "wall34.gif");
        
        tracker.addImage(wallpic[0][1], 5);
        tracker.addImage(wallpic[0][3], 5);
        tracker.addImage(wallpic[1][1], 5);
        tracker.addImage(wallpic[1][2], 5);
        tracker.addImage(wallpic[1][3], 5);
        tracker.addImage(wallpic[2][1], 5);
        tracker.addImage(wallpic[2][2], 5);
        tracker.addImage(wallpic[2][3], 5);
        tracker.addImage(wallpic[3][0], 5);
        tracker.addImage(wallpic[3][1], 5);
        tracker.addImage(wallpic[3][2], 5);
        tracker.addImage(wallpic[3][3], 5);
        tracker.addImage(wallpic[3][4], 5);
        
        altpic[0][1] = dmnew.tk.getImage(newmapdir + "altwall01.gif");
        altpic[0][3] = dmnew.tk.getImage(newmapdir + "altwall03.gif");
        altpic[1][1] = dmnew.tk.getImage(newmapdir + "altwall11.gif");
        altpic[1][3] = dmnew.tk.getImage(newmapdir + "altwall13.gif");
        altpic[2][1] = dmnew.tk.getImage(newmapdir + "altwall21.gif");
        altpic[2][3] = dmnew.tk.getImage(newmapdir + "altwall23.gif");
        altpic[1][2] = dmnew.tk.getImage(newmapdir + "altwall12.gif");
        altpic[2][2] = dmnew.tk.getImage(newmapdir + "altwall22.gif");
        altpic[3][1] = dmnew.tk.getImage(newmapdir + "altwall31.gif");
        altpic[3][2] = dmnew.tk.getImage(newmapdir + "altwall32.gif");
        altpic[3][3] = dmnew.tk.getImage(newmapdir + "altwall33.gif");
        altpic[3][0] = wallpic[3][0];
        altpic[3][4] = wallpic[3][4];
        
        tracker.addImage(altpic[0][1], 5);
        tracker.addImage(altpic[0][3], 5);
        tracker.addImage(altpic[1][1], 5);
        tracker.addImage(altpic[1][2], 5);
        tracker.addImage(altpic[1][3], 5);
        tracker.addImage(altpic[2][1], 5);
        tracker.addImage(altpic[2][2], 5);
        tracker.addImage(altpic[2][3], 5);
        tracker.addImage(altpic[3][1], 5);
        tracker.addImage(altpic[3][2], 5);
        tracker.addImage(altpic[3][3], 5);
        
        try {
            tracker.waitForID(5, 2000);
        } catch (InterruptedException ex) {
        }
        
        tracker.removeImage(wallpic[0][1], 5);
        tracker.removeImage(wallpic[0][3], 5);
        tracker.removeImage(wallpic[1][1], 5);
        tracker.removeImage(wallpic[1][2], 5);
        tracker.removeImage(wallpic[1][3], 5);
        tracker.removeImage(wallpic[2][1], 5);
        tracker.removeImage(wallpic[2][2], 5);
        tracker.removeImage(wallpic[2][3], 5);
        tracker.removeImage(wallpic[3][0], 5);
        tracker.removeImage(wallpic[3][1], 5);
        tracker.removeImage(wallpic[3][2], 5);
        tracker.removeImage(wallpic[3][3], 5);
        tracker.removeImage(wallpic[3][4], 5);
        tracker.removeImage(altpic[0][1], 5);
        tracker.removeImage(altpic[0][3], 5);
        tracker.removeImage(altpic[1][1], 5);
        tracker.removeImage(altpic[1][2], 5);
        tracker.removeImage(altpic[1][3], 5);
        tracker.removeImage(altpic[2][1], 5);
        tracker.removeImage(altpic[2][2], 5);
        tracker.removeImage(altpic[2][3], 5);
        tracker.removeImage(altpic[3][1], 5);
        tracker.removeImage(altpic[3][2], 5);
        tracker.removeImage(altpic[3][3], 5);
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //   [ ][ ][ ]     2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if ((row == 0 && col == 2) || (row < 3 && (col == 0 || col == 4))) return;
        if (col == 3) xc = xc - wallpic[row][3].getWidth(obs);
        else if (hasMons && col == 2 && row == 1 && dmnew.magicvision > 0) {
            drawContents(g, 3 - row, col - 1);
        }
        if (dmnew.mirrorback) g.drawImage(wallpic[row][col], xc, yc, obs);
        else g.drawImage(altpic[row][col], xc, yc, obs);
    }
    
}
