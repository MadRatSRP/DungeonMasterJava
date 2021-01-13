import java.io.IOException;
import java.io.ObjectOutputStream;

class PitData extends FloorData {
    private int level, xcoord, ycoord;
    public boolean isOpen;
    public boolean isConcealed;//if hard to see
    public boolean isIllusionary;//if always looks open
    public boolean isContinuous;//if blinking
    public boolean isActive;//for blinking, if starts blinking, or must be turned on by switch
    public int blinkrateo, blinkratec, blinkcounter = 0;//for blinking
    public boolean isSupplies = false;//true if supplies for quick type
    public int delay, reset, delaycounter = 0, resetcounter = 0;//for supplies type
    public boolean delaying, resetting, monswork, resetcount;
    public int count, maxcount;
    
    //lvl,x,y - level and xy coords of this generator
    //blnk - blink rate (how long between each open and close, ignored if not added to mapstochange)
    //blnkc - starting blink counter
    public PitData(int lvl, int x, int y, boolean isop, boolean isconc, boolean isillus, boolean issup, boolean iscont, boolean isac, int blnko, int blnkc, int dly, int rst, boolean mw, int mcnt, int cnt, boolean rcnt, int blnkcnt, boolean ding, int dcntr, boolean ring, int rcntr) {
        super();
        mapchar = 'p';
        level = lvl;
        xcoord = x;
        ycoord = y;
        isOpen = isop;
        isConcealed = isconc;
        isIllusionary = isillus;
        isSupplies = issup;
        isContinuous = iscont;
        isActive = isac;
        blinkrateo = blnko;
        blinkratec = blnkc;
        blinkcounter = blnkcnt;
        delay = dly;
        reset = rst;
        monswork = mw;
        maxcount = mcnt;
        count = cnt;
        resetcount = rcnt;
        delaying = ding;
        resetting = ring;
        delaycounter = dcntr;
        resetcounter = rcntr;
    }
    
    public void changeLevel(int amt, int lvlnum) {
        level += amt;
    }
    
    public void setMapCoord(int level, int x, int y) {
        this.level = level;
        xcoord = x;
        ycoord = y;
    }
    
    public String toString() {
        StringBuffer s = new StringBuffer();
        if (isContinuous) s.append("Blinking ");
        if (isConcealed) s.append("Concealed ");
        if (isIllusionary) s.append("Illusionary ");
        if (isSupplies) s.append("Quick ");
        s.append("Pit");
        return s.toString();
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(level);
        so.writeInt(xcoord);
        so.writeInt(ycoord);
        so.writeBoolean(isOpen);
        so.writeBoolean(isConcealed);
        so.writeBoolean(isIllusionary);
        so.writeBoolean(isSupplies);
        so.writeBoolean(isContinuous);
        so.writeBoolean(isActive);
        so.writeInt(blinkrateo);
        so.writeInt(blinkratec);
        so.writeInt(delay);
        so.writeInt(reset);
        so.writeBoolean(monswork);
        so.writeInt(maxcount);
        so.writeInt(count);
        so.writeBoolean(resetcount);
        so.writeInt(blinkcounter);
        so.writeBoolean(delaying);
        so.writeInt(delaycounter);
        so.writeBoolean(resetting);
        so.writeInt(resetcounter);
    }
    
}