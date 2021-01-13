import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class OneAlcoveData extends SidedWallData {
    public boolean isSwitch = false;
    public MultWallSwitchData alcoveswitchdata;
    
    public OneAlcoveData(int sde) {
        super(sde);
        mapchar = ']';
    }
    
    public OneAlcoveData(int sde, MultWallSwitchData fs) {
        super(sde);
        mapchar = ']';
        alcoveswitchdata = fs;
        isSwitch = true;
        alcoveswitchdata.setFacing(sde);
    }
    
    
    public void addItem(Item i) {
        if (i == null) return;
        if (mapItems == null) mapItems = new ArrayList();
        i.subsquare = (side + 2) % 4;
        mapItems.add(i);
        hasItems = true;
        numitemsin[(side + 2) % 4]++;
    }
    
    public void removeItem(int index) {
        if (mapItems.size() == 1) hasItems = false;
        numitemsin[(side + 2) % 4]--;
        mapItems.remove(index);
    }
    
    public void changeLevel(int amt, int lvlnum) {
        if (isSwitch) alcoveswitchdata.changeLevel(amt, lvlnum);
    }
    
    public void setMapCoord(int level, int x, int y) {
        if (isSwitch) alcoveswitchdata.setMapCoord(level, x, y);
    }
    
    public String toString() {
        String s;
        if (side == 0) s = "Alcove Facing South";
        else if (side == 1) s = "Alcove Facing East";
        else if (side == 2) s = "Alcove Facing North";
        else s = "Alcove Facing West";
        if (isSwitch) s += " (Switch)";
        return s;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(isSwitch);
        if (isSwitch) alcoveswitchdata.save(so);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        isSwitch = si.readBoolean();
        if (isSwitch) alcoveswitchdata = (MultWallSwitchData) DMEditor.loadMapData(si, -1, 0, 0);
    }
}        