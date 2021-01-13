import java.io.IOException;
import java.io.ObjectOutputStream;

class FulYaPitData extends FloorData {
    public int keynumber;
    public MapPoint xy, keytarget, nonkeytarget;
    
    //xy - mappoint of the pit
    //keynumber - item # for key
    //keytarget - mappoint to be activated if key dropped in pit
    //nonkeytarget - mappoint to be activated if some other item dropped in pit
    public FulYaPitData(MapPoint xy, int keynumber, MapPoint keytarget, MapPoint nonkeytarget) {
        super();
        mapchar = 'y';
        //canPassMons = false;
        //canPassImmaterial = false;
        this.xy = xy;
        this.keynumber = keynumber;
        this.keytarget = keytarget;
        this.nonkeytarget = nonkeytarget;
    }
    
    public String toString() {
        return "FulYa Pit";
    }
    
    public void changeLevel(int amt, int lvlnum) {
        if (keytarget.level >= lvlnum) keytarget = new MapPoint(keytarget.level + amt, keytarget.x, keytarget.y);
        if (keytarget.level < 0) keytarget = new MapPoint(0, keytarget.x, keytarget.y);
        if (nonkeytarget.level >= lvlnum)
            nonkeytarget = new MapPoint(nonkeytarget.level + amt, nonkeytarget.x, nonkeytarget.y);
        if (nonkeytarget.level < 0) nonkeytarget = new MapPoint(0, nonkeytarget.x, nonkeytarget.y);
        xy = new MapPoint(xy.level + amt, xy.x, xy.y);
    }
    
    public void setMapCoord(int level, int x, int y) {
        xy = new MapPoint(level, x, y);
    }
    
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(xy);
        so.writeInt(keynumber);
        so.writeObject(keytarget);
        so.writeObject(nonkeytarget);
    }
}
