import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

class MultFloorSwitchData extends FloorData {
    public MapPoint xy; //point containing xy of switch
    public boolean haspic = true;//,playsound = true; //does it have a picture/make a click?
    public ArrayList switchlist; //the collection of switches
    public boolean[] changing; //index of each switch that is changing
    
    //xyp - mappoint identifying location of this multwallswitch2
    //pic - true if has picture
    ////snd - true if makes a click sound - note: switches in switchlist should be set for no sound or pic!
    //sl - arraylist containing wallswitches (which must have same level,x,y,side)
    //public MultFloorSwitchData(MapPoint xyp,boolean pic,boolean snd,ArrayList sl) {
    public MultFloorSwitchData(MapPoint xyp, boolean pic, ArrayList sl) {
        super();
        xy = xyp;
        haspic = pic;
        //playsound = snd;
        switchlist = sl;
        changing = new boolean[switchlist.size()];
        for (int i = 0; i < switchlist.size(); i++) {
            changing[i] = false;
        }
        mapchar = 'S';
    }
    
    //for load routine
    public MultFloorSwitchData() {
        super();
        mapchar = 'S';
    }
    
    public void changeLevel(int amt, int lvlnum) {
        xy = new MapPoint(xy.level + amt, xy.x, xy.y);
        for (Iterator i = switchlist.iterator(); i.hasNext(); ) {
            ((FloorSwitchData) i.next()).changeLevel(amt, lvlnum);
        }
    }
    
    public void setMapCoord(int level, int x, int y) {
        xy = new MapPoint(level, x, y);
        for (Iterator i = switchlist.iterator(); i.hasNext(); ) {
            ((FloorSwitchData) i.next()).setMapCoord(level, x, y);
        }
    }
    
    public String toString() {
        return "Multiple Floor Switches";
    }
    
    public int[] getTarget(int index) {
        if (index > switchlist.size()) return null;
        FloorSwitchData tempdata = (FloorSwitchData) switchlist.get(index);
        int[] target = new int[3];
        target[0] = tempdata.targetlevel;
        target[1] = tempdata.targetx;
        target[2] = tempdata.targety;
        return target;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(xy);
        so.writeBoolean(haspic);
        //so.writeBoolean(playsound);
        so.writeObject(changing);
        so.writeInt(switchlist.size());
        for (Iterator i = switchlist.iterator(); i.hasNext(); ) {
            ((FloorSwitchData) i.next()).save(so);
        }
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        xy = (MapPoint) si.readObject();
        haspic = si.readBoolean();
        //playsound = si.readBoolean();
        changing = (boolean[]) si.readObject();
        int numswitches = si.readInt();
        switchlist = new ArrayList(numswitches);
        for (int i = 0; i < numswitches; i++) {
            switchlist.add(DMEditor.loadMapData(si, -1, 0, 0));
        }
    }
}