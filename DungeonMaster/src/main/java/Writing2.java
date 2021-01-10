import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

class Writing2 extends SidedWall {
    static public boolean ADDEDPICS;
    private String[] message;
    private int[][] intmess;
    static final private int[] yadj = {68, 87, 103, 120, 134, 146, 44, 56, 66, 78, 88, 96, 35, 43, 49, 58, 64, 70};
    //protected int side; //dir party must face to see
    static protected BufferedImage fontsrc, fontcol1src, fontcol3src;
    static protected BufferedImage[][] font = new BufferedImage[3][38];
    static protected Image[] col1 = new Image[18];
    static protected Image[] col3 = new Image[18];
    static protected Image[][] writingpic = new Image[5][3];
    
    //sde - direction must face to see
    //mes - string array containing message to display
    public Writing2(int sde, String[] mes) {
        super(sde);
        //side = sde;
        mapchar = 'w';
        message = mes;
        intmess = new int[message.length][];
        //parse message into indexes
        for (int line = 0; line < message.length; line++) {
            message[line] = message[line].toLowerCase();
            intmess[line] = new int[message[line].length()];
            for (int i = 0; i < message[line].length(); i++) {
                char c = message[line].charAt(i);
                if (c >= 'a' && c <= 'z') intmess[line][i] = (int) c - 97;
                else if (c == ' ') intmess[line][i] = -1;
                else if (c == '\'') intmess[line][i] = 37;
                else if (c == '?') intmess[line][i] = 36;
                else if (c >= '0' && c <= '9') intmess[line][i] = 26 + (int) c - 48;
                else intmess[line][i] = -1;
            }
        }
        super.setPics();
        if (!ADDEDPICS) setPics();
        facingside[1][2] = writingpic[0][1];
        facingside[2][1] = writingpic[1][0];
        facingside[2][2] = writingpic[1][1];
        facingside[2][3] = writingpic[1][2];
        facingside[3][1] = writingpic[2][0];
        facingside[3][2] = writingpic[2][1];
        facingside[3][3] = writingpic[2][2];
        col1pic[1] = writingpic[3][0];
        col1pic[2] = writingpic[3][1];
        col1pic[3] = writingpic[3][2];
        col3pic[1] = writingpic[4][0];
        col3pic[2] = writingpic[4][1];
        col3pic[3] = writingpic[4][2];
    }
    
    public void setMessage(String[] message) {
        boolean found = false;
        if (this.message.length != message.length) found = true;
        int line = 0;
        while (!found && line < message.length) {
            message[line] = message[line].toLowerCase();
            if (!this.message[line].equals(message[line])) found = true;
            else line++;
            //System.out.println("found = "+found+"\n"+this.message[line]+"\n"+message[line]);
        }
        if (found) {
            //System.out.println("making new message");
            this.message = message;
            intmess = new int[message.length][];
            //parse message into indexes
            for (line = 0; line < message.length; line++) {
                //message[line] = message[line].toLowerCase();
                intmess[line] = new int[message[line].length()];
                for (int i = 0; i < message[line].length(); i++) {
                    char c = message[line].charAt(i);
                    if (c >= 'a' && c <= 'z') intmess[line][i] = (int) c - 97;
                    else if (c == ' ') intmess[line][i] = -1;
                    else if (c == '\'') intmess[line][i] = 37;
                    else if (c == '?') intmess[line][i] = 36;
                    else if (c >= '0' && c <= '9') intmess[line][i] = 26 + (int) c - 48;
                    else intmess[line][i] = -1;
                }
            }
        }
    }
    
    protected void setPics() {
                
                /*
                writingpic[0][1] = dmnew.tk.createImage(mapdir+"writ12.gif");
                writingpic[1][0] = dmnew.tk.createImage(mapdir+"writ21.gif");
                writingpic[1][1] = dmnew.tk.createImage(mapdir+"writ22.gif");
                writingpic[1][2] = dmnew.tk.createImage(mapdir+"writ23.gif");
                writingpic[2][0] = dmnew.tk.createImage(mapdir+"writ31.gif");
                writingpic[2][1] = dmnew.tk.createImage(mapdir+"writ32.gif");
                writingpic[2][2] = dmnew.tk.createImage(mapdir+"writ33.gif");
                writingpic[3][0] = dmnew.tk.createImage(mapdir+"writcol11.gif");
                writingpic[3][1] = dmnew.tk.createImage(mapdir+"writcol12.gif");
                writingpic[3][2] = dmnew.tk.createImage(mapdir+"writcol13.gif");
                writingpic[4][0] = dmnew.tk.createImage(mapdir+"writcol31.gif");
                writingpic[4][1] = dmnew.tk.createImage(mapdir+"writcol32.gif");
                writingpic[4][2] = dmnew.tk.createImage(mapdir+"writcol33.gif");
                */
        writingpic[0][1] = dmnew.tk.getImage(mapdir + "writ12.gif");
        writingpic[1][0] = dmnew.tk.getImage(mapdir + "writ21.gif");
        writingpic[1][1] = dmnew.tk.getImage(mapdir + "writ22.gif");
        writingpic[1][2] = dmnew.tk.getImage(mapdir + "writ23.gif");
        writingpic[2][0] = dmnew.tk.getImage(mapdir + "writ31.gif");
        writingpic[2][1] = dmnew.tk.getImage(mapdir + "writ32.gif");
        writingpic[2][2] = dmnew.tk.getImage(mapdir + "writ33.gif");
        writingpic[3][0] = dmnew.tk.getImage(mapdir + "writcol11.gif");
        writingpic[3][1] = dmnew.tk.getImage(mapdir + "writcol12.gif");
        writingpic[3][2] = dmnew.tk.getImage(mapdir + "writcol13.gif");
        writingpic[4][0] = dmnew.tk.getImage(mapdir + "writcol31.gif");
        writingpic[4][1] = dmnew.tk.getImage(mapdir + "writcol32.gif");
        writingpic[4][2] = dmnew.tk.getImage(mapdir + "writcol33.gif");
        
        Image src = dmnew.tk.createImage("Maps/writing.gif");
        tracker.addImage(src, 5);
        try {
            tracker.waitForID(5);
        } catch (InterruptedException e) {
        }
        tracker.removeImage(src, 5);
        
        fontsrc = new BufferedImage(src.getWidth(null), src.getHeight(null), BufferedImage.TYPE_INT_ARGB);//was PRE
        fontsrc.createGraphics().drawImage(src, 0, 0, null);
        
        for (int i = 0; i < 38; i++) {
            font[0][i] = fontsrc.getSubimage(i * 13, 0, 13, 15);
        }
        for (int i = 0; i < 38; i++) {
            font[1][i] = fontsrc.getSubimage(i * 9, 16, 9, 10);
        }
        for (int i = 0; i < 38; i++) {
            font[2][i] = fontsrc.getSubimage(i * 6, 27, 6, 8);
        }
        
        col3[0] = dmnew.tk.createImage(mapdir + "line1col31.gif");
        col3[1] = dmnew.tk.createImage(mapdir + "line2col31.gif");
        col3[2] = dmnew.tk.createImage(mapdir + "line3col31.gif");
        col3[3] = dmnew.tk.createImage(mapdir + "line4col31.gif");
        col3[4] = dmnew.tk.createImage(mapdir + "line5col31.gif");
        col3[5] = dmnew.tk.createImage(mapdir + "line6col31.gif");
        col3[6] = dmnew.tk.createImage(mapdir + "line1col32.gif");
        col3[7] = dmnew.tk.createImage(mapdir + "line2col32.gif");
        col3[8] = dmnew.tk.createImage(mapdir + "line3col32.gif");
        col3[9] = dmnew.tk.createImage(mapdir + "line4col32.gif");
        col3[10] = dmnew.tk.createImage(mapdir + "line5col32.gif");
        col3[11] = dmnew.tk.createImage(mapdir + "line6col32.gif");
        col3[12] = dmnew.tk.createImage(mapdir + "line1col33.gif");
        col3[13] = dmnew.tk.createImage(mapdir + "line2col33.gif");
        col3[14] = dmnew.tk.createImage(mapdir + "line3col33.gif");
        col3[15] = dmnew.tk.createImage(mapdir + "line4col33.gif");
        col3[16] = dmnew.tk.createImage(mapdir + "line5col33.gif");
        col3[17] = dmnew.tk.createImage(mapdir + "line6col33.gif");
        col1[0] = dmnew.tk.createImage(mapdir + "line1col11.gif");
        col1[1] = dmnew.tk.createImage(mapdir + "line2col11.gif");
        col1[2] = dmnew.tk.createImage(mapdir + "line3col11.gif");
        col1[3] = dmnew.tk.createImage(mapdir + "line4col11.gif");
        col1[4] = dmnew.tk.createImage(mapdir + "line5col11.gif");
        col1[5] = dmnew.tk.createImage(mapdir + "line6col11.gif");
        col1[6] = dmnew.tk.createImage(mapdir + "line1col12.gif");
        col1[7] = dmnew.tk.createImage(mapdir + "line2col12.gif");
        col1[8] = dmnew.tk.createImage(mapdir + "line3col12.gif");
        col1[9] = dmnew.tk.createImage(mapdir + "line4col12.gif");
        col1[10] = dmnew.tk.createImage(mapdir + "line5col12.gif");
        col1[11] = dmnew.tk.createImage(mapdir + "line6col12.gif");
        col1[12] = dmnew.tk.createImage(mapdir + "line1col13.gif");
        col1[13] = dmnew.tk.createImage(mapdir + "line2col13.gif");
        col1[14] = dmnew.tk.createImage(mapdir + "line3col13.gif");
        col1[15] = dmnew.tk.createImage(mapdir + "line4col13.gif");
        col1[16] = dmnew.tk.createImage(mapdir + "line5col13.gif");
        col1[17] = dmnew.tk.createImage(mapdir + "line6col13.gif");
        
        tracker.addImage(writingpic[0][1], 0);
        tracker.addImage(writingpic[1][0], 0);
        tracker.addImage(writingpic[1][1], 0);
        tracker.addImage(writingpic[1][2], 0);
        tracker.addImage(writingpic[2][0], 0);
        tracker.addImage(writingpic[2][1], 0);
        tracker.addImage(writingpic[2][2], 0);
        tracker.addImage(writingpic[3][0], 0);
        tracker.addImage(writingpic[3][1], 0);
        tracker.addImage(writingpic[3][2], 0);
        tracker.addImage(writingpic[4][0], 0);
        tracker.addImage(writingpic[4][1], 0);
        tracker.addImage(writingpic[4][2], 0);
        
        tracker.addImage(col3[0], 0);
        tracker.addImage(col3[1], 0);
        tracker.addImage(col3[2], 0);
        tracker.addImage(col3[3], 0);
        tracker.addImage(col3[4], 0);
        tracker.addImage(col3[5], 0);
        tracker.addImage(col3[6], 0);
        tracker.addImage(col3[7], 0);
        tracker.addImage(col3[8], 0);
        tracker.addImage(col3[9], 0);
        tracker.addImage(col3[10], 0);
        tracker.addImage(col3[11], 0);
        tracker.addImage(col3[12], 0);
        tracker.addImage(col3[13], 0);
        tracker.addImage(col3[14], 0);
        tracker.addImage(col3[15], 0);
        tracker.addImage(col3[16], 0);
        tracker.addImage(col3[17], 0);
        
        tracker.addImage(col1[0], 0);
        tracker.addImage(col1[1], 0);
        tracker.addImage(col1[2], 0);
        tracker.addImage(col1[3], 0);
        tracker.addImage(col1[4], 0);
        tracker.addImage(col1[5], 0);
        tracker.addImage(col1[6], 0);
        tracker.addImage(col1[7], 0);
        tracker.addImage(col1[8], 0);
        tracker.addImage(col1[9], 0);
        tracker.addImage(col1[10], 0);
        tracker.addImage(col1[11], 0);
        tracker.addImage(col1[12], 0);
        tracker.addImage(col1[13], 0);
        tracker.addImage(col1[14], 0);
        tracker.addImage(col1[15], 0);
        tracker.addImage(col1[16], 0);
        tracker.addImage(col1[17], 0);
        
        ADDEDPICS = true;
    }
    
    public void redoWritPics() {
        String newmapdir = Wall.currentdir + File.separator;
        File testfile = new File(newmapdir + "writ12.gif");
        if (!testfile.exists()) {
            ADDEDPICS = true;
            return;
        }
        redoSidedPics();
        
        if (!ADDEDPICS) {
            writingpic[0][1] = dmnew.tk.getImage(newmapdir + "writ12.gif");
            writingpic[1][0] = dmnew.tk.getImage(newmapdir + "writ21.gif");
            writingpic[1][1] = dmnew.tk.getImage(newmapdir + "writ22.gif");
            writingpic[1][2] = dmnew.tk.getImage(newmapdir + "writ23.gif");
            writingpic[2][0] = dmnew.tk.getImage(newmapdir + "writ31.gif");
            writingpic[2][1] = dmnew.tk.getImage(newmapdir + "writ32.gif");
            writingpic[2][2] = dmnew.tk.getImage(newmapdir + "writ33.gif");
            writingpic[3][0] = dmnew.tk.getImage(newmapdir + "writcol11.gif");
            writingpic[3][1] = dmnew.tk.getImage(newmapdir + "writcol12.gif");
            writingpic[3][2] = dmnew.tk.getImage(newmapdir + "writcol13.gif");
            writingpic[4][0] = dmnew.tk.getImage(newmapdir + "writcol31.gif");
            writingpic[4][1] = dmnew.tk.getImage(newmapdir + "writcol32.gif");
            writingpic[4][2] = dmnew.tk.getImage(newmapdir + "writcol33.gif");
            
            tracker.addImage(writingpic[0][1], 5);
            tracker.addImage(writingpic[1][0], 5);
            tracker.addImage(writingpic[1][1], 5);
            tracker.addImage(writingpic[1][2], 5);
            tracker.addImage(writingpic[2][0], 5);
            tracker.addImage(writingpic[2][1], 5);
            tracker.addImage(writingpic[2][2], 5);
            tracker.addImage(writingpic[3][0], 5);
            tracker.addImage(writingpic[3][1], 5);
            tracker.addImage(writingpic[3][2], 5);
            tracker.addImage(writingpic[4][0], 5);
            tracker.addImage(writingpic[4][1], 5);
            tracker.addImage(writingpic[4][2], 5);
            
            try {
                tracker.waitForID(5, 2000);
            } catch (InterruptedException ex) {
            }
            
            tracker.removeImage(writingpic[0][1], 5);
            tracker.removeImage(writingpic[1][0], 5);
            tracker.removeImage(writingpic[1][1], 5);
            tracker.removeImage(writingpic[1][2], 5);
            tracker.removeImage(writingpic[2][0], 5);
            tracker.removeImage(writingpic[2][1], 5);
            tracker.removeImage(writingpic[2][2], 5);
            tracker.removeImage(writingpic[3][0], 5);
            tracker.removeImage(writingpic[3][1], 5);
            tracker.removeImage(writingpic[3][2], 5);
            tracker.removeImage(writingpic[4][0], 5);
            tracker.removeImage(writingpic[4][1], 5);
            tracker.removeImage(writingpic[4][2], 5);
        }
        ADDEDPICS = true;
        
        facingside[1][2] = writingpic[0][1];
        facingside[2][1] = writingpic[1][0];
        facingside[2][2] = writingpic[1][1];
        facingside[2][3] = writingpic[1][2];
        facingside[3][1] = writingpic[2][0];
        facingside[3][2] = writingpic[2][1];
        facingside[3][3] = writingpic[2][2];
        col1pic[1] = writingpic[3][0];
        col1pic[2] = writingpic[3][1];
        col1pic[3] = writingpic[3][2];
        col3pic[1] = writingpic[4][0];
        col3pic[2] = writingpic[4][1];
        col3pic[3] = writingpic[4][2];
    }
    
    public void doAction() {
        if (dmnew.leveldir[dmnew.level] != null) redoWritPics();
    }
    
    // 0  1  2  3  4
    //[ ][ ][ ][ ][ ]  3
    //   [ ][ ][ ]     2
    //   [ ][ ][ ]     1
    //   [ ][x][ ]     0
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        super.drawPic(row, col, xc, yc, g, obs);
        if (row == 0 || col == 0 || col == 4 || (dmnew.facing == side && row == 1 && col != 2)) return;
        int w = facingside[row][col].getWidth(null);
        int h = facingside[row][col].getHeight(null);
        if (col == 3) xc -= w;
        int xadj;
        if (dmnew.facing == side) {
            xc += w / 2;
            if (row == 1) {
                yc += 70;
                for (int j = 0; j < message.length; j++) {
                    if (j == 3) yc += 2;
                    xadj = -message[j].length() * 13 / 2;
                    for (int i = 0; i < message[j].length(); i++) {
                        if (intmess[j][i] != -1)
                            g.drawImage(font[0][intmess[j][i]], xc + xadj + i * 13, yc + j * 21, obs);
                    }
                }
            } else if (row == 2) {
                yc += 45;
                if (col == 3) xc += 63;
                else if (col == 1) xc -= 62;
                for (int j = 0; j < message.length; j++) {
                    if (j == 3) yc++;
                    xadj = -message[j].length() * 9 / 2;
                    for (int i = 0; i < message[j].length(); i++) {
                        if (intmess[j][i] != -1)
                            g.drawImage(font[1][intmess[j][i]], xc + xadj + i * 9, yc + j * 14, obs);
                    }
                }
            } else {
                yc += 34;
                if (col == 3) xc += 14;
                else if (col == 1) xc -= 15;
                for (int j = 0; j < message.length; j++) {
                    if (j == 3) yc += 3;
                    xadj = -message[j].length() * 6 / 2;
                    for (int i = 0; i < message[j].length(); i++) {
                        if (intmess[j][i] != -1)
                            g.drawImage(font[2][intmess[j][i]], xc + xadj + i * 6, yc + j * 9, obs);
                    }
                }
            }
            return;
        }
        //col1
        int testface = side - 1;
        if (testface < 0) testface = 3;
        if (dmnew.facing == testface && col == 1) {
            if (row == 1) xc += 73;
            else if (row == 2) xc += 122;
            else xc += 147;
            for (int i = 0; i < message.length; i++) {
                if (!message[i].equals("")) g.drawImage(col1[i + (row - 1) * 6], xc, yc + yadj[i + (row - 1) * 6], obs);
            }
            return;
        }
        
        //col3
        testface = side + 1;
        if (testface > 3) testface = 0;
        if (dmnew.facing == testface && col == 3) {
            if (row == 1) xc += 4;
            else if (row == 2) xc += 2;
            else xc += 2;
            for (int i = 0; i < message.length; i++) {
                if (!message[i].equals("")) g.drawImage(col3[i + (row - 1) * 6], xc, yc + yadj[i + (row - 1) * 6], obs);
            }
            return;
        }
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        //so.writeInt(side);
        so.writeObject(message);
    }
}
