import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class DoorData extends MapData {
    
    public boolean isOpen, isBroken, isBreakable, resetcount;
    public int breakpoints;
    public int picklock;
    public int changecount = 4;
    public MapPoint xy;
    public int side;
    public boolean isclosing = true;
    public int opentype;
    public int pictype;
    public int keynumber; //-1 if not a key door
    public boolean consumeskey, transparent;
    public int count, maxcount;
    public String custompic;

        /*
        static final boolean CLOSED = false;
        static final boolean OPEN = true;
        static final int NORMAL = 0;
        static final int BUTTON = 1;
        static final int KEY = 2;
        static final int WOOD = 0;
        static final int GRATE = 1;
        static final int METAL = 2;
        static final int FANCYWOOD = 3;
        static final int FANCYMETAL = 4;
        static final int BLACK = 5;
        static final int WOODWINDOW = 6;
        static final int BLACKWINDOW = 7;
        static final int RED = 8;
        static final int GLASS = 9;
        static final int IRONCROSS = 10;
        static final int IRONFACE = 11;
        */
    
    //xyp - mappoint containing level and xy coord of this door
    //sd - side must face to see
    //pt - picture type -> wood,grate,metal,woodwindow,metalwindow
    //ot - open type -> normal(by something else),button,key
    //op - is it open? (true if starts open, else false)
    //brka - is it breakable?
    //isbrk - is it broken?
    //kn - keynumber (item# that opens it - only used for key open type)
    //cons - true if key is destroyed upon use
    //public DoorData(MapPoint xyp,int sd,int pt,int ot,boolean op,boolean brka,boolean isbrk,int kn,boolean cons,int mcnt,int cnt,boolean rcnt) {
    public DoorData(MapPoint xyp, int sd, int pt, int ot, boolean op, boolean brka, boolean isbrk, int kn, boolean cons, int mcnt, int cnt, boolean rcnt, int pklk) {
        super();
        mapchar = 'd';
        xy = xyp;
        side = sd;
        pictype = pt;
        opentype = ot;
        keynumber = kn;
        consumeskey = cons;
        maxcount = mcnt;
        count = cnt;
        resetcount = rcnt;
        picklock = pklk;
        isOpen = op;
        isBreakable = brka;
        isBroken = isbrk;
        canHoldItems = true;
        drawItems = true;
        if (isOpen) {
            drawFurtherItems = true;
            isPassable = true;
            canPassProjs = true;
            canPassMons = true;
            changecount = 0;
            isclosing = false;
        }
        if (pictype == 1) {
            canPassProjs = true;
            drawFurtherItems = true;
        }
    }
    
    public void changeLevel(int amt, int lvlnum) {
        xy = new MapPoint(xy.level + amt, xy.x, xy.y);
    }
    
    public void setMapCoord(int level, int x, int y) {
        xy = new MapPoint(level, x, y);
    }
    
    public String toString() {
        String s;
        if (isOpen) s = "Open ";
        else s = "Closed ";
        if (pictype == 0) s += "Wooden Door";
        else if (pictype == 1) s += "Grate Door";
        else if (pictype == 2) s += "Metal Door";
        else if (pictype == 3) s += "Fancy Wooden Door";
        else if (pictype == 4) s += "Fancy Metal Door";
        else if (pictype == 5) s += "Black Door";
        else if (pictype == 6) s += "Wooden Door w/Window";
        else if (pictype == 7) s += "Black Door w/Window";
        else if (pictype == 8) s += "Red Door";
        else if (pictype == 9) s += "Ven Glass Door";
        else if (pictype == 10) s += "Iron Cross Door";
        else if (pictype == 11) s += "Iron Face Door";
        else s += "Custom Door";
        if (isBroken) s += " (Broken)";
        else if (isBreakable) s += " (Breakable)";
        if (opentype == 1) s += " (Button)";
        else if (opentype == 2) {
            if (keynumber > 30 && keynumber < 47) s += " (Key - " + ItemWizard.keyitems[keynumber - 31] + ")";
            else s += " (Key)";
        }
        //else if (opentype==2) {
        //        Item tempit = new Item(keynumber);
        //        s=s+" (Key - "+tempit.name+")";
        //}
        if (side == 0) s += " Facing North/South";
        else s += " Facing East/West";
        return s;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(xy);
        so.writeInt(side);
        so.writeInt(pictype);
        so.writeInt(opentype);
        so.writeBoolean(isOpen);
        so.writeBoolean(isBreakable);
        so.writeBoolean(isBroken);
        so.writeInt(keynumber);
        so.writeBoolean(consumeskey);
        so.writeInt(maxcount);
        so.writeInt(count);
        so.writeBoolean(resetcount);
        so.writeInt(picklock);
        so.writeInt(changecount);
        so.writeBoolean(isclosing);
        //if (isBreakable && !isBroken && pictype==1 && breakpoints==150) breakpoints=280;
        if (isBreakable && !isBroken) so.writeInt(breakpoints);
        if (pictype == 12) {
            so.writeUTF(custompic);
            so.writeBoolean(transparent);
        }
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        changecount = si.readInt();
        isclosing = si.readBoolean();
        if (isBreakable && !isBroken) breakpoints = si.readInt();
        if (pictype == 12) {
            custompic = si.readUTF();
            transparent = si.readBoolean();
        }
    }
}