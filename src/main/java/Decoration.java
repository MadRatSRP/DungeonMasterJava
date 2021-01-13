import java.io.IOException;
import java.io.ObjectOutputStream;

class Decoration extends SidedWall2 {
    static public boolean[] ADDEDPICS = new boolean[10];
    private int number; //id of decoration
    
    public Decoration(int sde, int num) {
        super(sde);
        number = num;
        mapchar = 'D';
        setPics();
    }
    
    public void setNumber(int number) {
        if (this.number != number) {
            this.number = number;
            setPics();
        }
    }
    
    protected void setPics() {
        super.setPics();
        switch (number) {
            case 0:
                facingside[0] = loadPic("wallring1.gif");
                facingside[1] = loadPic("wallring2.gif");
                facingside[2] = loadPic("wallring3.gif");
                col1pic[0] = loadPic("wallringcol11.gif");
                col1pic[1] = loadPic("wallringcol12.gif");
                col1pic[2] = loadPic("wallringcol13.gif");
                col3pic[0] = loadPic("wallringcol31.gif");
                col3pic[1] = loadPic("wallringcol32.gif");
                col3pic[2] = loadPic("wallringcol33.gif");
                //xy for facing
                xadjust[0] = 134;
                yadjust[0] = 82;
                xadjust[1] = 87;
                yadjust[1] = 53;
                xadjust[2] = 64;
                yadjust[2] = 38;
                //xy for col1
                xadjust[3] = 97;
                yadjust[3] = 81;
                xadjust[4] = 136;
                yadjust[4] = 51;
                xadjust[5] = 160;
                yadjust[5] = 39;
                //xy for col3
                xadjust[6] = 14;
                yadjust[6] = 81;
                xadjust[7] = 8;
                yadjust[7] = 51;
                xadjust[8] = 6;
                yadjust[8] = 39;
                break;
            case 1:
                facingside[0] = loadPic("wallhook1.gif");
                facingside[1] = loadPic("wallhook2.gif");
                facingside[2] = loadPic("wallhook3.gif");
                col1pic[0] = loadPic("wallhookcol11.gif");
                col1pic[1] = loadPic("wallhookcol12.gif");
                col1pic[2] = loadPic("wallhookcol13.gif");
                col3pic[0] = loadPic("wallhookcol31.gif");
                col3pic[1] = loadPic("wallhookcol32.gif");
                col3pic[2] = loadPic("wallhookcol33.gif");
                //xy for facing
                xadjust[0] = 134;
                yadjust[0] = 82;
                xadjust[1] = 87;
                yadjust[1] = 53;
                xadjust[2] = 64;
                yadjust[2] = 38;
                //xy for col1
                xadjust[3] = 97;
                yadjust[3] = 81;
                xadjust[4] = 136;
                yadjust[4] = 51;
                xadjust[5] = 160;
                yadjust[5] = 39;
                //xy for col3
                xadjust[6] = 13;
                yadjust[6] = 81;
                xadjust[7] = 7;
                yadjust[7] = 51;
                xadjust[8] = 5;
                yadjust[8] = 39;
                break;
            case 2:
                facingside[0] = loadPic("wallslime1.gif");
                facingside[1] = loadPic("wallslime2.gif");
                facingside[2] = loadPic("wallslime3.gif");
                col1pic[0] = loadPic("wallslimecol11.gif");
                col1pic[1] = loadPic("wallslimecol12.gif");
                col1pic[2] = loadPic("wallslimecol13.gif");
                col3pic[0] = loadPic("wallslimecol31.gif");
                col3pic[1] = loadPic("wallslimecol32.gif");
                col3pic[2] = loadPic("wallslimecol33.gif");
                //xy for facing
                xadjust[0] = 136;
                yadjust[0] = 226;
                xadjust[1] = 87;
                yadjust[1] = 140;
                xadjust[2] = 64;
                yadjust[2] = 100;
                //xy for col1
                xadjust[3] = 95;
                yadjust[3] = 189;
                xadjust[4] = 135;
                yadjust[4] = 125;
                xadjust[5] = 159;
                yadjust[5] = 93;
                //xy for col3
                xadjust[6] = 12;
                yadjust[6] = 189;
                xadjust[7] = 6;
                yadjust[7] = 125;
                xadjust[8] = 4;
                yadjust[8] = 93;
                break;
            case 3:
                facingside[0] = loadPic("wallgrate1.gif");
                facingside[1] = loadPic("wallgrate2.gif");
                facingside[2] = loadPic("wallgrate3.gif");
                col1pic[0] = loadPic("wallgratecol11.gif");
                col1pic[1] = loadPic("wallgratecol12.gif");
                col1pic[2] = loadPic("wallgratecol13.gif");
                col3pic[0] = loadPic("wallgratecol31.gif");
                col3pic[1] = loadPic("wallgratecol32.gif");
                col3pic[2] = loadPic("wallgratecol33.gif");
                //xy for facing
                xadjust[0] = 121;
                yadjust[0] = 220;
                xadjust[1] = 79;
                yadjust[1] = 139;
                xadjust[2] = 57;
                yadjust[2] = 98;
                //xy for col1
                xadjust[3] = 95;
                yadjust[3] = 178;
                xadjust[4] = 135;
                yadjust[4] = 117;
                xadjust[5] = 159;
                yadjust[5] = 88;
                //xy for col3
                xadjust[6] = 12;
                yadjust[6] = 178;
                xadjust[7] = 6;
                yadjust[7] = 117;
                xadjust[8] = 4;
                yadjust[8] = 88;
                break;
            case 4:
                facingside[0] = loadPic("walldrain1.gif");
                facingside[1] = loadPic("walldrain2.gif");
                facingside[2] = loadPic("walldrain3.gif");
                col1pic[0] = loadPic("walldraincol11.gif");
                col1pic[1] = loadPic("walldraincol12.gif");
                col1pic[2] = loadPic("walldraincol13.gif");
                col3pic[0] = loadPic("walldraincol31.gif");
                col3pic[1] = loadPic("walldraincol32.gif");
                col3pic[2] = loadPic("walldraincol33.gif");
                //xy for facing
                xadjust[0] = 135;
                yadjust[0] = 199;
                xadjust[1] = 89;
                yadjust[1] = 127;
                xadjust[2] = 66;
                yadjust[2] = 92;
                //xy for col1
                xadjust[3] = 97;
                yadjust[3] = 166;
                xadjust[4] = 136;
                yadjust[4] = 111;
                xadjust[5] = 160;
                yadjust[5] = 81;
                //xy for col3
                xadjust[6] = 13;
                yadjust[6] = 166;
                xadjust[7] = 7;
                yadjust[7] = 111;
                xadjust[8] = 5;
                yadjust[8] = 81;
                break;
            case 5:
                facingside[0] = loadPic("wallcrack1.gif");
                facingside[1] = loadPic("wallcrack2.gif");
                facingside[2] = loadPic("wallcrack3.gif");
                col1pic[0] = loadPic("wallcrackcol11.gif");
                col1pic[1] = loadPic("wallcrackcol12.gif");
                //col1pic[2] = loadPic("wallcrackcol13.gif");
                col1pic[2] = blankpic;
                col3pic[0] = loadPic("wallcrackcol31.gif");
                col3pic[1] = loadPic("wallcrackcol32.gif");
                col3pic[2] = loadPic("wallcrackcol33.gif");
                //xy for facing
                xadjust[0] = 212;
                yadjust[0] = 131;  //xadjust[0]=129;  yadjust[0]=199;
                xadjust[1] = 134;
                yadjust[1] = 86;
                xadjust[2] = 102;
                yadjust[2] = 62;
                //xy for col1
                xadjust[3] = 115;
                yadjust[3] = 114;  //xadjust[3]=65;   yadjust[3]=130;
                //xadjust[4]=151;  yadjust[4]=72;   //xadjust[4]=121;  yadjust[4]=84;
                xadjust[4] = 146;
                yadjust[4] = 72;   //xadjust[4]=121;  yadjust[4]=84;
                
                //xadjust[5]=167;  yadjust[5]=55;   //xadjust[5]=151;  yadjust[5]=58;
                //xy for col3
                xadjust[6] = 47;
                yadjust[6] = 129;
                xadjust[7] = 23;
                yadjust[7] = 83;
                xadjust[8] = 12;
                yadjust[8] = 58;
                break;
            case 6:
                facingside[0] = loadPic("scratch1.gif");
                facingside[1] = loadPic("scratch2.gif");
                facingside[2] = loadPic("scratch3.gif");
                col1pic[0] = loadPic("scratchcol11.gif");
                col1pic[1] = loadPic("scratchcol12.gif");
                col1pic[2] = loadPic("scratchcol13.gif");
                col3pic[0] = loadPic("scratchcol31.gif");
                col3pic[1] = loadPic("scratchcol32.gif");
                col3pic[2] = loadPic("scratchcol33.gif");
                //xy for facing
                xadjust[0] = 100;
                yadjust[0] = 87;
                xadjust[1] = 64;
                yadjust[1] = 58;
                xadjust[2] = 49;
                yadjust[2] = 42;
                //xy for col1
                xadjust[3] = 78;
                yadjust[3] = 84;
                xadjust[4] = 127;
                yadjust[4] = 56;
                xadjust[5] = 155;
                yadjust[5] = 40;
                //xy for col3
                xadjust[6] = 5;
                yadjust[6] = 83;
                xadjust[7] = 2;
                yadjust[7] = 52;
                xadjust[8] = 2;
                yadjust[8] = 37;
                break;
            case 7:
                facingside[0] = loadPic("chaos1.gif");
                facingside[1] = loadPic("chaos2.gif");
                facingside[2] = loadPic("chaos3.gif");
                col1pic[0] = loadPic("chaoscol11.gif");
                col1pic[1] = loadPic("chaoscol12.gif");
                col1pic[2] = loadPic("chaoscol13.gif");
                col3pic[0] = loadPic("chaoscol31.gif");
                col3pic[1] = loadPic("chaoscol32.gif");
                col3pic[2] = loadPic("chaoscol33.gif");
                //xy for facing
                xadjust[0] = 71;
                yadjust[0] = 67;
                xadjust[1] = 45;
                yadjust[1] = 43;
                xadjust[2] = 35;
                yadjust[2] = 31;
                //xy for col1
                xadjust[3] = 72;
                yadjust[3] = 69;
                xadjust[4] = 119;
                yadjust[4] = 41;
                xadjust[5] = 150;
                yadjust[5] = 32;
                //xy for col3
                xadjust[6] = 0;
                yadjust[6] = 69;
                xadjust[7] = 0;
                yadjust[7] = 41;
                xadjust[8] = 0;
                yadjust[8] = 32;
                break;
            case 8:
                facingside[0] = loadPic("chaos1-p.gif");
                facingside[1] = loadPic("chaos2-p.gif");
                facingside[2] = loadPic("chaos3-p.gif");
                col1pic[0] = loadPic("chaoscol11-p.gif");
                col1pic[1] = loadPic("chaoscol12-p.gif");
                col1pic[2] = loadPic("chaoscol13-p.gif");
                col3pic[0] = loadPic("chaoscol31-p.gif");
                col3pic[1] = loadPic("chaoscol32-p.gif");
                col3pic[2] = loadPic("chaoscol33-p.gif");
                //xy for facing
                xadjust[0] = 71;
                yadjust[0] = 67;
                xadjust[1] = 45;
                yadjust[1] = 43;
                xadjust[2] = 35;
                yadjust[2] = 31;
                //xy for col1
                xadjust[3] = 72;
                yadjust[3] = 69;
                xadjust[4] = 119;
                yadjust[4] = 41;
                xadjust[5] = 150;
                yadjust[5] = 32;
                //xy for col3
                xadjust[6] = 0;
                yadjust[6] = 69;
                xadjust[7] = 0;
                yadjust[7] = 41;
                xadjust[8] = 0;
                yadjust[8] = 32;
                break;
            case 9:
                facingside[0] = loadPic("demon1.gif");
                facingside[1] = loadPic("demon2.gif");
                facingside[2] = loadPic("demon3.gif");
                col1pic[0] = loadPic("demoncol11.gif");
                col1pic[1] = loadPic("demoncol12.gif");
                col1pic[2] = loadPic("demoncol13.gif");
                col3pic[0] = loadPic("demoncol31.gif");
                col3pic[1] = loadPic("demoncol32.gif");
                col3pic[2] = loadPic("demoncol33.gif");
                //xy for facing
                xadjust[0] = 74;
                yadjust[0] = 65;
                xadjust[1] = 51;
                yadjust[1] = 46;
                xadjust[2] = 37;
                yadjust[2] = 33;
                //xy for col1
                xadjust[3] = 84;
                yadjust[3] = 68;
                xadjust[4] = 129;
                yadjust[4] = 44;
                xadjust[5] = 155;
                yadjust[5] = 35;
                //xy for col3
                xadjust[6] = 4;
                yadjust[6] = 68;
                xadjust[7] = 3;
                yadjust[7] = 44;
                xadjust[8] = 2;
                yadjust[8] = 35;
                break;
        }
        if (!ADDEDPICS[number]) {
            tracker.addImage(facingside[0], 0);
            tracker.addImage(facingside[1], 0);
            tracker.addImage(facingside[2], 0);
            tracker.addImage(col1pic[0], 0);
            tracker.addImage(col1pic[1], 0);
            tracker.addImage(col1pic[2], 0);
            tracker.addImage(col3pic[0], 0);
            tracker.addImage(col3pic[1], 0);
            tracker.addImage(col3pic[2], 0);
            ADDEDPICS[number] = true;
        }
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(number);
    }
}