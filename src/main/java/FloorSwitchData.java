import java.io.*;

class FloorSwitchData extends FloorData {
    public MapPoint xy;
    public int type;
    public int targetlevel, targetx, targety; //xy of target mapsquare to change
    public boolean isReusable;
    public boolean wasUsed;
    public boolean playsound;
    public boolean haspic; //visible or not?
    public boolean switchstate;
    public int actiontype;
    public int delay; //delay time before works, 0 for none
    public int reset; //time until resets, 0 for no reset
    public int changecount = 0; //for delay and reset
    public boolean delaying, resetting, resetnotrigger; //set true in tryswitch if has delay/reset
    public MapData changeto;
    public MapData oldMapObject;
    public String soundstring;
    public int loopsound, switchface;
    public boolean abrupt, retainitems;

        /*
        static final int NORMAL = 0;       //creatures & items, regardless what is on square
        static final int CONSTANT = 1;     //creatures & items, unless something already on square
        static final int CREATURE = 2;     //creatures only, regardless what is on square
        static final int ITEMONLY = 3;     //items only, regardless what is on square
        static final int STEPON = 4;       //creatures - stepping on only, regardless what is on square
        static final int STEPOFF = 5;      //creatures - stepping off only, regardless what is on square
        static final int MON = 6;          //monsters only, regardless what is on square
        static final int MONSTEPON = 7;    //monsters - stepping on only, regardless what is on square
        static final int MONSTEPOFF = 8;   //monsters - stepping off only, regardless what is on square 
        static final int PARTY = 9;        //party only, regardless what is on square
        static final int PARTYSTEPON = 10; //party - stepping on only, regardless what is on square
        static final int PARTYSTEPOFF = 11;//party - stepping off only, regardless what is on square 
        static final int CONSTANTON = 12;  //stepping on only, unless item is on square
        static final int CONSTANTOFF = 13; //stepping off only, unless item is on square
        */
    
    
    //construct a floor switch
    // xyp - mappoint containing level and xy coords of this switch
    // typ - see above ints
    // lvl,x,y - level and xy coords of target mapsquare
    // reu - is the switch reusable?
    // pic - does switch have a pic, or is it invisible?
    // snd - does switch make a sound when activated?
    // dly - delay time before switch works, 0 for none (note this acts a bit strange with constant weight switches -> if weight removed before delay time up, not switched again)
    // rset - time before switch resets, 0 for no reset
    // rnt - true if doesn't trigger switch when reset happens (so reset just makes switch inactive for a time)
    // act - true if activates
    // chngto - mapobject to change target into
    // retainitems - for swap/setto types, if true old items transferred to new square
    public FloorSwitchData(MapPoint xyp, int typ, int lvl, int x, int y, boolean reu, boolean pic, boolean snd, int dly, int rset, boolean rnt, int act, MapData chngto, boolean rt, int sf) {
        super();
        mapchar = 's';
        xy = xyp;
        type = typ;
        targetlevel = lvl;
        targetx = x;
        targety = y;
        isReusable = reu;
        haspic = pic;
        playsound = snd;
        delay = dly;
        reset = rset;
        resetnotrigger = rnt;
        actiontype = act;
        changeto = chngto;
        retainitems = rt;
        switchface = sf;
    }
    
    //constructor for use by load routine
    public FloorSwitchData() {
        super();
        mapchar = 's';
    }
    
    public void changeLevel(int amt, int lvlnum) {
        //if (amt>0) {
        if (targetlevel >= lvlnum) targetlevel += amt;
        //}
        //else {
        //if (targetlevel>=lvlnum) targetlevel+=amt;
        if (targetlevel < 0) targetlevel = 0;
        //}
        xy = new MapPoint(xy.level + amt, xy.x, xy.y);
    }
    
    public void setMapCoord(int level, int x, int y) {
        xy = new MapPoint(level, x, y);
    }
    
    public String toString() {
        return "FloorSwitch";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(xy);
        so.writeInt(type);
        so.writeInt(targetlevel);
        so.writeInt(targetx);
        so.writeInt(targety);
        so.writeBoolean(isReusable);
        so.writeInt(delay);
        so.writeInt(reset);
        so.writeBoolean(haspic);
        so.writeBoolean(playsound);
        so.writeInt(changecount);
        so.writeBoolean(delaying);
        so.writeBoolean(resetting);
        so.writeBoolean(resetnotrigger);
        so.writeBoolean(switchstate);
        so.writeInt(switchface);
        so.writeBoolean(wasUsed);
        so.writeInt(actiontype);
        //if (actiontype>=5 && actiontype<7 && (isReusable || !wasUsed)) {
        if (actiontype == 5 || actiontype == 6) {
            changeto.save(so);
            if (switchstate && actiontype == 5) oldMapObject.save(so);
            so.writeBoolean(retainitems);
        } else if (actiontype == 7) {
            so.writeUTF(soundstring);
            so.writeInt(loopsound);
        } else if (actiontype == 8) so.writeBoolean(abrupt);
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        xy = (MapPoint) si.readObject();
        type = si.readInt();
        targetlevel = si.readInt();
        targetx = si.readInt();
        targety = si.readInt();
        isReusable = si.readBoolean();
        delay = si.readInt();
        reset = si.readInt();
        haspic = si.readBoolean();
        playsound = si.readBoolean();
        changecount = si.readInt();
        delaying = si.readBoolean();
        resetting = si.readBoolean();
        resetnotrigger = si.readBoolean();
        switchstate = si.readBoolean();
        switchface = si.readInt();
        //switchface = 0;
        wasUsed = si.readBoolean();
        actiontype = si.readInt();
        //if (actiontype>=5 && actiontype<7 && (isReusable || !wasUsed)) {
        if (actiontype == 5 || actiontype == 6) {
            changeto = DMEditor.loadMapData(si, -1, 0, 0);
            if (switchstate && actiontype == 5) oldMapObject = DMEditor.loadMapData(si, -1, 0, 0);
            retainitems = si.readBoolean();
        } else if (actiontype == 7) {
            soundstring = si.readUTF();
            loopsound = si.readInt();
        } else if (actiontype == 8) abrupt = si.readBoolean();
    }
}