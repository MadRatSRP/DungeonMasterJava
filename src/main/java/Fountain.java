import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Fountain extends SidedWall {
    static public boolean ADDEDPICS;
    private MultWallSwitch2 fountainswitch;
    private boolean cantakeitems = true;//if true, items are retrieved when clicked
    private int canputitems = -1;//max size of items that can be put in, -1 for none
    
    public Fountain(int s) {
        super(s);
        mapchar = 'f';
        setPics();
    }
    
    public void tryWallSwitch(int x, int y) {
        if (dmnew.facing == side && x > 170 && x < 276 && y > 160 && y < 200) {
            dmnew.playSound("gulp.wav", -1, -1);
            dmnew.hero[dmnew.leader].water = 1000;
            if (cantakeitems && hasItems && !dmnew.iteminhand) {
                dmnew.inhand = (Item) mapItems.remove(0);
                if (mapItems.size() == 0) hasItems = false;
                dmnew.hero[dmnew.leader].load += dmnew.inhand.weight;
                dmnew.iteminhand = true;
                if (fountainswitch != null) fountainswitch.tryWallSwitch(-1, -1, dmnew.inhand);
            } else if (fountainswitch != null) fountainswitch.tryWallSwitch(-1, -1);
        }
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        boolean takeitem = false;
        if (dmnew.facing == side && x > 170 && x < 276 && y > 160 && y < 200) {
            dmnew.playSound("gulp.wav", -1, -1);
            dmnew.hero[dmnew.leader].water = 1000;
            if (it.number == 73) {
                float weightchange = (0.2f * (float) (3 - ((Waterskin) it).drinks));//each drink weighs .2
                it.weight += weightchange;
                dmnew.hero[dmnew.leader].load += weightchange;
                if (((Waterskin) it).drinks == 0) ((Waterskin) it).swapPics();
                ((Waterskin) it).drinks = 3;
            } else if (it.number == 7) {
                dmnew.inhand = new Item(72); //turn flask into water flask - have to reference inhand, not it (need change inhand pointer)
                dmnew.hero[dmnew.leader].load += 0.2f;//water flask has one drink
            }
            if (fountainswitch != null) {
                if (!fountainswitch.tryWallSwitch(-1, -1, it)) {
                    //remove item from hand
                    takeitem = true;
                }
            }
            if (canputitems >= 0 && it.size <= canputitems && it.number != 72 && it.number != 73) {
                addItem(it);
                if (dmnew.inhand != null) {
                    //remove item from hand
                    takeitem = true;
                }
            }
            //else takeitem = false;
        }
        return !takeitem;
    }
    
    protected void setPics() {
        super.setPics();
        facingside[1][2] = loadPic("fountain12.gif");
        facingside[2][1] = loadPic("fountain21.gif");
        facingside[2][2] = loadPic("fountain22.gif");
        facingside[2][3] = loadPic("fountain23.gif");
        facingside[3][1] = loadPic("fountain31.gif");
        facingside[3][2] = loadPic("fountain32.gif");
        facingside[3][3] = loadPic("fountain33.gif");
        col1pic[1] = loadPic("fountaincol11.gif");
        col1pic[2] = loadPic("fountaincol12.gif");
        col1pic[3] = loadPic("fountaincol13.gif");
        col3pic[1] = loadPic("fountaincol31.gif");
        col3pic[2] = loadPic("fountaincol32.gif");
        col3pic[3] = loadPic("fountaincol33.gif");
        if (!ADDEDPICS) {
            tracker.addImage(facingside[1][2], 0);
            tracker.addImage(facingside[2][1], 0);
            tracker.addImage(facingside[2][2], 0);
            tracker.addImage(facingside[2][3], 0);
            tracker.addImage(facingside[3][1], 0);
            tracker.addImage(facingside[3][2], 0);
            tracker.addImage(facingside[3][3], 0);
            tracker.addImage(col1pic[1], 0);
            tracker.addImage(col1pic[2], 0);
            tracker.addImage(col1pic[3], 0);
            tracker.addImage(col3pic[1], 0);
            tracker.addImage(col3pic[2], 0);
            tracker.addImage(col3pic[3], 0);
            ADDEDPICS = true;
        }
                /*
                facingside[0] = loadPic("fountain1.gif");
                facingside[1] = loadPic("fountain2.gif");
                facingside[2] = loadPic("fountain3.gif");
                col1pic[0] = loadPic("fountaincol11.gif");
                col1pic[1] = loadPic("fountaincol12.gif");
                col1pic[2] = loadPic("fountaincol13.gif");
                col3pic[0] = loadPic("fountaincol31.gif");
                col3pic[1] = loadPic("fountaincol32.gif");
                col3pic[2] = loadPic("fountaincol33.gif");
                tracker.addImage(facingside[0],1);
                tracker.addImage(facingside[1],1);
                tracker.addImage(facingside[2],1);
                tracker.addImage(col1pic[0],1);
                tracker.addImage(col1pic[1],1);
                tracker.addImage(col1pic[2],1);
                tracker.addImage(col3pic[0],1);
                tracker.addImage(col3pic[1],1);
                tracker.addImage(col3pic[2],1);

                //xy for facing
                xadjust[0]=109;  yadjust[0]=55;
                xadjust[1]=76;   yadjust[1]=36;
                xadjust[2]=57;   yadjust[2]=26;
                //xy for col1
                xadjust[3]=80;   yadjust[3]=58;
                xadjust[4]=127;  yadjust[4]=38;
                xadjust[5]=154;  yadjust[5]=29;
                //xy for col3
                xadjust[6]=4;    yadjust[6]=58;
                xadjust[7]=1;    yadjust[7]=38;
                xadjust[8]=0;    yadjust[8]=29;
                */
        
    }
    
    public void redoFountainPics() {
        String newmapdir = Wall.currentdir + File.separator;
        File testfile = new File(newmapdir + "fountain12.gif");
        if (!testfile.exists()) return;
        redoSidedPics();
        facingside[1][2] = dmnew.tk.getImage(newmapdir + "fountain12.gif");
        facingside[2][1] = dmnew.tk.getImage(newmapdir + "fountain21.gif");
        facingside[2][2] = dmnew.tk.getImage(newmapdir + "fountain22.gif");
        facingside[2][3] = dmnew.tk.getImage(newmapdir + "fountain23.gif");
        facingside[3][1] = dmnew.tk.getImage(newmapdir + "fountain31.gif");
        facingside[3][2] = dmnew.tk.getImage(newmapdir + "fountain32.gif");
        facingside[3][3] = dmnew.tk.getImage(newmapdir + "fountain33.gif");
        col1pic[1] = dmnew.tk.getImage(newmapdir + "fountaincol11.gif");
        col1pic[2] = dmnew.tk.getImage(newmapdir + "fountaincol12.gif");
        col1pic[3] = dmnew.tk.getImage(newmapdir + "fountaincol13.gif");
        col3pic[1] = dmnew.tk.getImage(newmapdir + "fountaincol31.gif");
        col3pic[2] = dmnew.tk.getImage(newmapdir + "fountaincol32.gif");
        col3pic[3] = dmnew.tk.getImage(newmapdir + "fountaincol33.gif");
        
        tracker.addImage(facingside[1][2], 5);
        tracker.addImage(facingside[2][1], 5);
        tracker.addImage(facingside[2][2], 5);
        tracker.addImage(facingside[2][3], 5);
        tracker.addImage(facingside[3][1], 5);
        tracker.addImage(facingside[3][2], 5);
        tracker.addImage(facingside[3][3], 5);
        tracker.addImage(col1pic[1], 5);
        tracker.addImage(col1pic[2], 5);
        tracker.addImage(col1pic[3], 5);
        tracker.addImage(col3pic[1], 5);
        tracker.addImage(col3pic[2], 5);
        tracker.addImage(col3pic[3], 5);
        
        try {
            tracker.waitForID(5, 2000);
        } catch (InterruptedException ex) {
        }
        
        tracker.removeImage(facingside[1][2], 5);
        tracker.removeImage(facingside[2][1], 5);
        tracker.removeImage(facingside[2][2], 5);
        tracker.removeImage(facingside[2][3], 5);
        tracker.removeImage(facingside[3][1], 5);
        tracker.removeImage(facingside[3][2], 5);
        tracker.removeImage(facingside[3][3], 5);
        tracker.removeImage(col1pic[1], 5);
        tracker.removeImage(col1pic[2], 5);
        tracker.removeImage(col1pic[3], 5);
        tracker.removeImage(col3pic[1], 5);
        tracker.removeImage(col3pic[2], 5);
        tracker.removeImage(col3pic[3], 5);
    }
    
    public void doAction() {
        if (dmnew.leveldir[dmnew.level] != null) redoFountainPics();
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(cantakeitems);
        so.writeInt(canputitems);
        if (fountainswitch != null) {
            so.writeBoolean(true);
            fountainswitch.save(so);
        } else so.writeBoolean(false);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        cantakeitems = si.readBoolean();
        canputitems = si.readInt();
        if (si.readBoolean()) fountainswitch = (MultWallSwitch2) dmnew.loadMapObject(si);
    }
    
}
