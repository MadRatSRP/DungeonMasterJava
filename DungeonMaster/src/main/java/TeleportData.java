import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class TeleportData extends FloorData {
    
    public int type;
    public int level, xcoord, ycoord; //level and xy of this teleporter
    public int targetlevel, targetx, targety; //level and xy of mapsquare to teleport to
    public int newface; //mainly for spinners, -1 no change, 0,1,2,3 correspond to north,west,south,east, 4 means spin right, 5 means spin left, 6 means spin 180 deg, 7 means random direction
    public int blinkcounter;
    public int blinkrateon, blinkrateoff;
    public int maxcount, count;
    public boolean resetcount;
    public boolean isSwitched;
    public boolean switchVisible;
    public boolean delaying, resetting;
    public int delay, reset, delaycounter = 0, resetcounter = 0;
    public boolean isReusable;
    public boolean wasUsed;
    public boolean isVisible;
    public boolean isActive;
    public boolean isOn;
    public boolean playsound;
    public boolean swapplaces;
    
    /*from Teleport.java:
    static final int PARTY = 1;
    static final int MONSTER = 2;
    static final int ITEM = 4;
    static final int PROJ = 8;
    */
    //for loading:
    public TeleportData() {
        super();
        mapchar = 't';
    }
    
    //for wizard:
    public TeleportData(int lvl, int xc, int yc, int typ, int tlvl, int x, int y, int fac, int blnkon, int blnkof, int blnkc, boolean issw, boolean svis, int dly, int rst, boolean reu, boolean vis, boolean act, boolean on, boolean snd, boolean swap, int mcnt, int cnt, boolean rcnt, boolean ding, int dcntr, boolean ring, int rcntr, boolean wusd) {
        super();
        mapchar = 't';
        level = lvl;
        xcoord = xc;
        ycoord = yc;
        type = typ;
        targetlevel = tlvl;
        targetx = x;
        targety = y;
        newface = fac;
        blinkrateon = blnkon;
        blinkrateoff = blnkof;
        blinkcounter = blnkc;
        isSwitched = issw;
        switchVisible = svis;
        delay = dly;
        reset = rst;
        isReusable = reu;
        isVisible = vis;
        isActive = act;
        isOn = on;
        playsound = snd;
        swapplaces = swap;
        maxcount = mcnt;
        count = cnt;
        resetcount = rcnt;
        delaying = ding;
        delaycounter = dcntr;
        resetting = ring;
        resetcounter = rcntr;
        wasUsed = wusd;
    }
    
    public void changeLevel(int amt, int lvlnum) {
        //if (amt>0) {
        if (targetlevel >= lvlnum) targetlevel += amt;
        //}
        //else {
        //if (targetlevel>=lvlnum) targetlevel+=amt;
        if (targetlevel < 0) targetlevel = 0;
        //}
        level += amt;
    }
    
    public void setMapCoord(int level, int x, int y) {
        this.level = level;
        xcoord = x;
        ycoord = y;
    }
    
    public String toString() {
        if (blinkrateon > 0 || blinkrateoff > 0) return "Blinking Teleport";
        else return "Teleport";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(level);
        so.writeInt(xcoord);
        so.writeInt(ycoord);
        so.writeInt(type);
        so.writeInt(targetlevel);
        so.writeInt(targetx);
        so.writeInt(targety);
        so.writeInt(newface);
        so.writeInt(blinkrateon);
        so.writeInt(blinkrateoff);
        so.writeInt(blinkcounter);
        so.writeBoolean(isSwitched);
        so.writeBoolean(switchVisible);
        so.writeInt(delay);
        so.writeInt(reset);
        so.writeBoolean(isReusable);
        so.writeBoolean(isVisible);
        so.writeBoolean(isActive);
        so.writeBoolean(isOn);
        so.writeBoolean(playsound);
        so.writeBoolean(swapplaces);
        so.writeInt(maxcount);
        so.writeInt(count);
        so.writeBoolean(resetcount);
        so.writeBoolean(delaying);
        so.writeInt(delaycounter);
        so.writeBoolean(resetting);
        so.writeInt(resetcounter);
        so.writeBoolean(wasUsed);
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        level = si.readInt();
        xcoord = si.readInt();
        ycoord = si.readInt();
        type = si.readInt();
        targetlevel = si.readInt();
        targetx = si.readInt();
        targety = si.readInt();
        newface = si.readInt();
        blinkrateon = si.readInt();
        blinkrateoff = si.readInt();
        blinkcounter = si.readInt();
        isSwitched = si.readBoolean();
        switchVisible = si.readBoolean();
        delay = si.readInt();
        reset = si.readInt();
        isReusable = si.readBoolean();
        isVisible = si.readBoolean();
        isActive = si.readBoolean();
        isOn = si.readBoolean();
        playsound = si.readBoolean();
        swapplaces = si.readBoolean();
        //swapplaces = false;
        maxcount = si.readInt();
        count = si.readInt();
        resetcount = si.readBoolean();
        delaying = si.readBoolean();
        delaycounter = si.readInt();
        resetting = si.readBoolean();
        resetcounter = si.readInt();
        wasUsed = si.readBoolean();
    }
}