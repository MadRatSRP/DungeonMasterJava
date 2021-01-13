import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class LauncherData extends SidedWallData {
    public int type, spnumber, power, castpower, style;
    public Item it;
    public int level, xcoord, ycoord;
    public int ammocount = -1;//neg # = unlimited ammo
    public int ammocount2;
    public boolean noprojend;
    public int shootrate; //0 means not continuous
    public int shootcounter = 0;
    public boolean isShooting;
    public static final String[] spells = {"Fireball", "Lightning", "Poison Bolt", "Ven Cloud", "Dispell Immaterial", "Arc Bolt", "Weakness", "Feeble Mind", "Slow", "Strip Defenses", "Silence", "Door Open"};
    public static final String[] powers = {"LO", "UM", "ON", "EE", "PAL", "MON"};
    
    //old form:
    //public LauncherData(int lvl,int xc,int yc,int s,int t,int num,int p,boolean npe,int sr,int am,int am2,int scnt,boolean issh) {
    //        this(lvl,xc,yc,s,t,num,p,0,npe,sr,am,am2,scnt,issh);
    //}

        /*
        //less old form:
        public LauncherData(int lvl,int xc,int yc,int s,int t,int num,int p,int sty,boolean npe,int sr,int am,int am2,int scnt,boolean issh) {
                super(s);
                level = lvl; xcoord = xc; ycoord = yc;
                mapchar = 'l';
                type = t;
                if (type==0) spnumber = num;
                else it = new Item(num);
                power = p;
                style = sty;
                noprojend = npe;
                shootrate = sr;
                ammocount = am;
                ammocount2 = am2;
                shootcounter = scnt;
                isShooting = issh;
        }
        */
    
    //lvl,xc,yc - level,xcoord,ycoord of this launcher
    //s - dir must face to see(hole on opp side)
    //t - type: 0-spell,else item
    //p - power: spellpower or shotpow
    //cp - cast power for spells and bombs
    //sty - style: 0-both,1-left,2-right,3-random
    //npe - noprojend: if true, projs don't fade away
    //sr - shootrate (0 for not continuous)
    //am - ammocount (-1 for unlimited)
    //am2 - # ammo for continuous to reset to when run out and shut off (0 for no reset)
    //scnt - counter value for continuous launchers
    //issh - is it shooting now?
    public LauncherData(int lvl, int xc, int yc, int s, int t, int p, int cp, int sty, boolean npe, int sr, int am, int am2, int scnt, boolean issh) {
        super(s);
        level = lvl;
        xcoord = xc;
        ycoord = yc;
        mapchar = 'l';
        type = t;
        power = p;
        castpower = cp;
        style = sty;
        noprojend = npe;
        shootrate = sr;
        ammocount = am;
        ammocount2 = am2;
        shootcounter = scnt;
        isShooting = issh;
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
        //if (side==0) return "Launcher Facing South";
        //else if (side==1) return "Launcher Facing East";
        //else if (side==2) return "Launcher Facing North";
        //else return "Launcher Facing West";
        StringBuffer s = new StringBuffer("Launcher");
        if (type == 0) {
            s.append(" (");
            switch (spnumber) {
                case 44:
                    s.append(spells[0]);
                    break;
                case 335:
                    s.append(spells[1]);
                    break;
                case 51:
                    s.append(spells[2]);
                    break;
                case 31:
                    s.append(spells[3]);
                    break;
                case 52:
                    s.append(spells[4]);
                    break;
                case 642:
                    s.append(spells[5]);
                    break;
                case 461:
                    s.append(spells[6]);
                    break;
                case 363:
                    s.append(spells[7]);
                    break;
                case 362:
                    s.append(spells[8]);
                    break;
                case 664:
                    s.append(spells[9]);
                    break;
                case 523:
                    s.append(spells[10]);
                    break;
                case 6:
                    s.append(spells[11]);
                    break;
            }
            s.append(" <" + powers[power] + ">)");
        } else {
            s.append(" (" + it.name + ")");
        }
        if (side == 0) s.append(" Facing South");
        else if (side == 1) s.append(" Facing East");
        else if (side == 2) s.append(" Facing North");
        else s.append(" Facing West");
        return s.toString();
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(level);
        so.writeInt(xcoord);
        so.writeInt(ycoord);
        so.writeInt(type);
        so.writeInt(power);
        so.writeInt(castpower);
        so.writeInt(style);
        so.writeBoolean(noprojend);
        so.writeInt(shootrate);
        so.writeInt(ammocount);
        so.writeInt(ammocount2);
        so.writeInt(shootcounter);
        so.writeBoolean(isShooting);
        if (type == 0) so.writeInt(spnumber);
        else so.writeObject(it);
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        if (type == 0) spnumber = si.readInt();
        else it = (Item) si.readObject();
    }
    
}