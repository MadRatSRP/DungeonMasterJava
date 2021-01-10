import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

class MultWallSwitchData extends SidedWallData {
    public MapPoint xy; //point containing xy of switch
    public int picnumber; //for variety of button,key,coinslot graphics
    public int pictype; //button,key,or coin, only used for pictures
    public ArrayList switchlist; //the collection of switches
    public boolean[] changing; //index of each switch that is changing
    public boolean switchstate;
    
    //xyp - mappoint identifying location of this multwallswitch2
    //sde - direction must face to see/use switch
    //typ - picture type (button,key,coin)
    //picnum - picture number for variety (see setpics)
    //sl - arraylist containing wallswitches (which must have same level,x,y,side)
    public MultWallSwitchData(MapPoint xyp, int sde, int typ, int picnum, ArrayList sl) {
        super(sde);
        xy = xyp;
        pictype = typ;
        picnumber = picnum;
        switchlist = sl;
        changing = new boolean[switchlist.size()];
        for (int i = 0; i < switchlist.size(); i++) {
            changing[i] = false;
        }
        mapchar = '\\';
    }
    
    //for load routine
    public MultWallSwitchData(int sde) {
        super(sde);
        mapchar = '\\';
    }
    
    public void changeLevel(int amt, int lvlnum) {
        xy = new MapPoint(xy.level + amt, xy.x, xy.y);
        for (Iterator i = switchlist.iterator(); i.hasNext(); ) {
            ((WallSwitchData) i.next()).changeLevel(amt, lvlnum);
        }
    }
    
    public void setMapCoord(int level, int x, int y) {
        xy = new MapPoint(level, x, y);
        for (Iterator i = switchlist.iterator(); i.hasNext(); ) {
            ((WallSwitchData) i.next()).setMapCoord(level, x, y);
        }
    }
    
    public void setFacing(int side) {
        for (Iterator i = switchlist.iterator(); i.hasNext(); ) {
            ((WallSwitchData) i.next()).side = side;
        }
    }
    
    public String toString() {
        String s = "Multiple WallSwitch";
        boolean multkeys = false;
        int keynumber = 0;
        if (pictype > 0) {
            keynumber = ((WallSwitchData) switchlist.get(0)).keynumber;
            int i = 1;
            while (!multkeys && i < switchlist.size()) {
                if (((WallSwitchData) switchlist.get(i)).keynumber != keynumber) multkeys = true;
                else i++;
            }
            if (!multkeys) {
                Item tempitem = null;
                String keyname = null;
                //potion
                if (keynumber > 9 && keynumber < 31) {
                    tempitem = new Item(keynumber, 1, 1);
                    keyname = tempitem.name;
                }
                //chest
                else if (keynumber == 5) {
                    //tempitem = new Chest();
                    keyname = "Chest";
                }
                //scroll
                else if (keynumber == 4) {
                    //String[] mess = new String[5];
                    //tempitem = new Item(mess);
                    keyname = "Scroll";
                }
                //torch
                else if (keynumber == 9) {
                    //tempitem = new Torch();
                    keyname = "Torch";
                }
                //waterskin
                else if (keynumber == 73) {
                    //tempitem = new Waterskin();
                    keyname = "Waterskin";
                }
                //compass
                else if (keynumber == 8) {
                    //tempitem = new Compass();
                    keyname = "Compass";
                }
                //everything else
                else if (keynumber < 300) {
                    tempitem = new Item(keynumber);
                    keyname = tempitem.name;
                }
                if (keyname != null) {
                    if (pictype == 1) s = s + " (Key - " + keyname + ")";
                    else s = s + " (Coin - " + keyname + ")";
                } else if (pictype == 1) s = s + " (Key - Custom Item #" + (keynumber - 299) + ")";
                else s = s + " (Coin - Custom Item #" + (keynumber - 299) + ")";
            } else {
                if (pictype == 1) s += " (Key)";
                else s += " (Coin)";
            }
        } else s += " (Button)";
        if (side == 0) s += " Facing South";
        else if (side == 1) s += " Facing East";
        else if (side == 2) s += " Facing North";
        else s += " Facing West";
        return s;
    }
    
    public int[] getTarget(int index) {
        if (index > switchlist.size()) return null;
        WallSwitchData tempdata = (WallSwitchData) switchlist.get(index);
        int[] target = new int[3];
        target[0] = tempdata.targetlevel;
        target[1] = tempdata.targetx;
        target[2] = tempdata.targety;
        return target;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(xy);
        so.writeInt(pictype);
        so.writeInt(picnumber);
        so.writeObject(changing);
        so.writeBoolean(switchstate);
        so.writeInt(switchlist.size());
        for (Iterator i = switchlist.iterator(); i.hasNext(); ) {
            ((WallSwitchData) i.next()).save(so);
        }
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        xy = (MapPoint) si.readObject();
        pictype = si.readInt();
        picnumber = si.readInt();
        changing = (boolean[]) si.readObject();
        switchstate = si.readBoolean();
        //switchstate = false;
        int numswitches = si.readInt();
        switchlist = new ArrayList(numswitches);
        for (int i = 0; i < numswitches; i++) {
            switchlist.add(DMEditor.loadMapData(si, -1, 0, 0));
        }
    }
}