import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Sconce extends SidedWall2 {
    
    static public boolean ADDEDPICS;
    private Torch torch;
    private boolean hasTorch;
    private boolean isSwitch = false;
    private MultWallSwitch2 sconceswitch;
    //private Image[] swappics = new Image[9];
    private Image[] notorchpics = new Image[9];
    private Image[] torchpics = new Image[9];
    
    public Sconce(int sde, boolean hasTorch) {
        super(sde);
        mapchar = '}';
        this.hasTorch = hasTorch;
        if (hasTorch) torch = new Torch();
        setPics();
    }
    
    public Sconce(int sde, boolean hasTorch, MultWallSwitch2 ss) {
        super(sde);
        mapchar = '}';
        this.hasTorch = hasTorch;
        if (hasTorch) torch = new Torch();
        sconceswitch = ss;
        isSwitch = true;
        setPics();
    }
    
    //for loading
    public Sconce(int sde) {
        super(sde);
        mapchar = '}';
        setPics();
    }
    
    protected void setPics() {
        super.setPics();
        notorchpics[0] = loadPic("sconce1.gif");
        notorchpics[1] = loadPic("sconce2.gif");
        notorchpics[2] = loadPic("sconce3.gif");
        notorchpics[3] = loadPic("sconce11.gif");
        notorchpics[4] = loadPic("sconce13.gif");
        notorchpics[5] = loadPic("sconce21.gif");
        notorchpics[6] = loadPic("sconce23.gif");
        notorchpics[7] = loadPic("sconce31.gif");
        notorchpics[8] = loadPic("sconce33.gif");
        torchpics[0] = loadPic("sconce-t1.gif");
        torchpics[1] = loadPic("sconce-t2.gif");
        torchpics[2] = loadPic("sconce-t3.gif");
        torchpics[3] = loadPic("sconce-t11.gif");
        torchpics[4] = loadPic("sconce-t13.gif");
        torchpics[5] = loadPic("sconce-t21.gif");
        torchpics[6] = loadPic("sconce-t23.gif");
        torchpics[7] = loadPic("sconce-t31.gif");
        torchpics[8] = loadPic("sconce-t33.gif");
                /*
                if (hasTorch) { 
                        facingside[0]=loadPic("sconce-t1.gif");
                        facingside[1]=loadPic("sconce-t2.gif");
                        facingside[2]=loadPic("sconce-t3.gif");
                        col1pic[0]=loadPic("sconce-t11.gif");
                        col3pic[0]=loadPic("sconce-t13.gif");
                        col1pic[1]=loadPic("sconce-t21.gif");
                        col3pic[1]=loadPic("sconce-t23.gif");
                        col1pic[2]=loadPic("sconce-t31.gif");
                        col3pic[2]=loadPic("sconce-t33.gif");
                        //xy for facing
                        xadjust[0]=138;  yadjust[0]=58;
                        xadjust[1]=90;   yadjust[1]=35;
                        xadjust[2]=66;   yadjust[2]=26;
                        //xy for col1
                        xadjust[3]=94;   yadjust[3]=61;
                        xadjust[4]=134;  yadjust[4]=39;
                        xadjust[5]=159;  yadjust[5]=31;
                        //xy for col3
                        xadjust[6]=10;    yadjust[6]=61;
                        xadjust[7]=5;    yadjust[7]=39;
                        xadjust[8]=3;    yadjust[8]=31;
                        swappics[0]=loadPic("sconce1.gif");
                        swappics[1]=loadPic("sconce2.gif");
                        swappics[2]=loadPic("sconce3.gif");
                        swappics[3]=loadPic("sconce11.gif");
                        swappics[4]=loadPic("sconce13.gif");
                        swappics[5]=loadPic("sconce21.gif");
                        swappics[6]=loadPic("sconce23.gif");
                        swappics[7]=loadPic("sconce31.gif");
                        swappics[8]=loadPic("sconce33.gif");
                }
                else { 
                        facingside[0]=loadPic("sconce1.gif");
                        facingside[1]=loadPic("sconce2.gif");
                        facingside[2]=loadPic("sconce3.gif");
                        col1pic[0]=loadPic("sconce11.gif");
                        col3pic[0]=loadPic("sconce13.gif");
                        col1pic[1]=loadPic("sconce21.gif");
                        col3pic[1]=loadPic("sconce23.gif");
                        col1pic[2]=loadPic("sconce31.gif");
                        col3pic[2]=loadPic("sconce33.gif");
                        //xy for facing
                        xadjust[0]=138;  yadjust[0]=81;
                        xadjust[1]=90;   yadjust[1]=50;
                        xadjust[2]=66;   yadjust[2]=37;
                        //xy for col1
                        xadjust[3]=94;   yadjust[3]=77;
                        xadjust[4]=134;  yadjust[4]=50;
                        xadjust[5]=159;  yadjust[5]=38;
                        //xy for col3
                        xadjust[6]=10;    yadjust[6]=77;
                        xadjust[7]=5;    yadjust[7]=50;
                        xadjust[8]=3;    yadjust[8]=38;
                        swappics[0]=loadPic("sconce-t1.gif");
                        swappics[1]=loadPic("sconce-t2.gif");
                        swappics[2]=loadPic("sconce-t3.gif");
                        swappics[3]=loadPic("sconce-t11.gif");
                        swappics[4]=loadPic("sconce-t13.gif");
                        swappics[5]=loadPic("sconce-t21.gif");
                        swappics[6]=loadPic("sconce-t23.gif");
                        swappics[7]=loadPic("sconce-t31.gif");
                        swappics[8]=loadPic("sconce-t33.gif");
                }
                */
        if (!ADDEDPICS) {
                        /*
                        tracker.addImage(facingside[0],1);
                        tracker.addImage(facingside[1],1);
                        tracker.addImage(facingside[2],1);
                        tracker.addImage(col1pic[0],1);
                        tracker.addImage(col1pic[1],1);
                        tracker.addImage(col1pic[2],1);
                        tracker.addImage(col3pic[0],1);
                        tracker.addImage(col3pic[1],1);
                        tracker.addImage(col3pic[2],1);
                        tracker.addImage(swappics[0],1);
                        tracker.addImage(swappics[1],1);
                        tracker.addImage(swappics[2],1);
                        tracker.addImage(swappics[3],1);
                        tracker.addImage(swappics[4],1);
                        tracker.addImage(swappics[5],1);
                        tracker.addImage(swappics[6],1);
                        tracker.addImage(swappics[7],1);
                        tracker.addImage(swappics[8],1);
                        */
            tracker.addImage(notorchpics[0], 0);
            tracker.addImage(notorchpics[1], 0);
            tracker.addImage(notorchpics[2], 0);
            tracker.addImage(notorchpics[3], 0);
            tracker.addImage(notorchpics[4], 0);
            tracker.addImage(notorchpics[5], 0);
            tracker.addImage(notorchpics[6], 0);
            tracker.addImage(notorchpics[7], 0);
            tracker.addImage(notorchpics[8], 0);
            tracker.addImage(torchpics[0], 0);
            tracker.addImage(torchpics[1], 0);
            tracker.addImage(torchpics[2], 0);
            tracker.addImage(torchpics[3], 0);
            tracker.addImage(torchpics[4], 0);
            tracker.addImage(torchpics[5], 0);
            tracker.addImage(torchpics[6], 0);
            tracker.addImage(torchpics[7], 0);
            tracker.addImage(torchpics[8], 0);
            ADDEDPICS = true;
        }
        swapPics();
    }
    
    private void swapPics() {
                /*
                Image[] temppic = swappics;
                swappics[0]=facingside[0];
                swappics[1]=facingside[1];
                swappics[2]=facingside[2];
                swappics[3]=col1pic[0];
                swappics[4]=col3pic[0];
                swappics[5]=col1pic[1];
                swappics[6]=col3pic[1];
                swappics[7]=col1pic[2];
                swappics[8]=col3pic[2];
                facingside[0]=temppic[0];
                facingside[1]=temppic[1];
                facingside[2]=temppic[2];
                col1pic[0]=temppic[3];
                col3pic[0]=temppic[4];
                col1pic[1]=temppic[5];
                col3pic[1]=temppic[6];
                col1pic[2]=temppic[7];
                col3pic[2]=temppic[8];
                */
        if (hasTorch) {
            facingside[0] = torchpics[0];
            facingside[1] = torchpics[1];
            facingside[2] = torchpics[2];
            col1pic[0] = torchpics[3];
            col3pic[0] = torchpics[4];
            col1pic[1] = torchpics[5];
            col3pic[1] = torchpics[6];
            col1pic[2] = torchpics[7];
            col3pic[2] = torchpics[8];
            //xy for facing
            xadjust[0] = 138;
            yadjust[0] = 58;
            xadjust[1] = 90;
            yadjust[1] = 35;
            xadjust[2] = 66;
            yadjust[2] = 26;
            //xy for col1
            xadjust[3] = 94;
            yadjust[3] = 61;
            xadjust[4] = 134;
            yadjust[4] = 39;
            xadjust[5] = 159;
            yadjust[5] = 31;
            //xy for col3
            xadjust[6] = 10;
            yadjust[6] = 61;
            xadjust[7] = 5;
            yadjust[7] = 39;
            xadjust[8] = 3;
            yadjust[8] = 31;
        } else {
            facingside[0] = notorchpics[0];
            facingside[1] = notorchpics[1];
            facingside[2] = notorchpics[2];
            col1pic[0] = notorchpics[3];
            col3pic[0] = notorchpics[4];
            col1pic[1] = notorchpics[5];
            col3pic[1] = notorchpics[6];
            col1pic[2] = notorchpics[7];
            col3pic[2] = notorchpics[8];
            //xy for facing
            xadjust[0] = 138;
            yadjust[0] = 81;
            xadjust[1] = 90;
            yadjust[1] = 50;
            xadjust[2] = 66;
            yadjust[2] = 37;
            //xy for col1
            xadjust[3] = 94;
            yadjust[3] = 77;
            xadjust[4] = 134;
            yadjust[4] = 50;
            xadjust[5] = 159;
            yadjust[5] = 38;
            //xy for col3
            xadjust[6] = 10;
            yadjust[6] = 77;
            xadjust[7] = 5;
            yadjust[7] = 50;
            xadjust[8] = 3;
            yadjust[8] = 38;
        }
    }
    
    public void tryWallSwitch(int x, int y) {
        //get torch
        if (!hasTorch || x < 137 || x > 308 || y < 80 || y > 230) return;
        hasTorch = false;
        swapPics();
        dmnew.inhand = torch;
        dmnew.iteminhand = true;
        dmnew.hero[dmnew.leader].load += dmnew.inhand.weight;
        if (isSwitch) sconceswitch.tryWallSwitch(torch);
        dmnew.needredraw = true;
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        //put torch
        if (hasTorch || x < 137 || x > 308 || y < 80 || y > 230 || it.number != 9) return true;
        hasTorch = true;
        swapPics();
        torch = (Torch) dmnew.inhand;
        if (isSwitch) sconceswitch.tryWallSwitch(torch);
        dmnew.needredraw = true;
        return false;
    }
    
    public boolean changeState() {
        if (sconceswitch != null) return sconceswitch.changeState();
        else return false;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(hasTorch);
        if (hasTorch) so.writeObject(torch);
        so.writeBoolean(isSwitch);
        if (isSwitch) sconceswitch.save(so);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        hasTorch = si.readBoolean();
        if (hasTorch) {
            torch = (Torch) si.readObject();
            swapPics();
        }
        isSwitch = si.readBoolean();
        if (isSwitch) sconceswitch = (MultWallSwitch2) dmnew.loadMapObject(si);
    }
    
}
